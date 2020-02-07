package crown.webframework.framework.servlet;

import io.netty.handler.codec.http.HttpResponseStatus;
import crown.webframework.framework.annotation.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author kuangjunlin
 */
public class Dispatcher {

    private static Properties contextConfig = new Properties();

    private static List<String> classNames = new ArrayList<String>();

    private static Map<String, Object> ioc = new HashMap<String, Object>();

    private static List<Handler> handlerMapping = new ArrayList<Handler>();

    static {
        //1.加载配置文件
        doLoadConfig("/application.properties");
        //2.扫描所有相关联的类
        doScanner(contextConfig.getProperty("scanPackage"));
        //3.初始化所有相关联的类，并且将其保存在IOC容器里面
        doInstance();
        
        //4.执行依赖注入（把加了@Autowired注解的字段赋值）
        doAutowired();
        //Spring 和核心功能已经完成 IOC、DI
        
        //5.构造HandlerMapping，将URL和Method进行关联
        initHandlerMapping();

        System.out.println("Web MVC framework initialized");
    }
    /**
     * 匹配URL
     * @param req request
     * @param resp response
     * @throws Exception
     */
    public static void doDispatch(Request req, Response resp) throws Exception {
        Handler handler = getHandler(req);

        if(handler == null) {
            resp.sendError(HttpResponseStatus.NOT_FOUND, "404 not found!");
            return;
        }

        //获取方法的参数列表
        Class[] paramTypes = handler.method.getParameterTypes();

        //保存所有需要自动赋值的参数值
        Object[] paramValues = new Object[paramTypes.length];
        Map<String, Object> params = req.getParams();
        params.entrySet().stream()
                .filter(map -> handler.paramIndexMapping.containsKey(map.getKey()))
                .forEach(map -> {
                    Object value = map.getValue();
                    if (value instanceof String) {
                        int index = handler.paramIndexMapping.get(map.getKey());
                        paramValues[index] = convert(paramTypes[index], String.valueOf(value));
                    }
                });

        //设置方法中的request和response对象
        int reqIndex = handler.paramIndexMapping.get(Request.class.getName());
        paramValues[reqIndex] = req;

        int respIndex = handler.paramIndexMapping.get(Response.class.getName());
        paramValues[respIndex] = resp;

        handler.method.invoke(handler.controller, paramValues);
    }

    private static Handler getHandler(Request req) {
        if(handlerMapping.isEmpty()) {return null;}

        String url = req.getPath();

        for(Handler handler : handlerMapping) {
            Matcher matcher = handler.pattern.matcher(url);
            if(!matcher.matches()) { continue; }
            return handler;
        }
        return null;
    }

    private static Object convert(Class<?> type, String value) {
        if(Integer.class == type) {
            return Integer.valueOf(value);
        }
        return value;
    }

    private static void initHandlerMapping() {
        if(ioc.isEmpty()) { return; }

        for(Map.Entry<String, Object> entry : ioc.entrySet()) {
            Class<?> clazz = entry.getValue().getClass();

            if(!clazz.isAnnotationPresent(Controller.class)) {continue;}

            String baseUrl = "";
            if(clazz.isAnnotationPresent(RequestMapping.class)) {
                RequestMapping requestMapping = clazz.getAnnotation(RequestMapping.class);
                baseUrl = requestMapping.value();
            }
            Method[] methods = clazz.getMethods();
            for(Method method : methods) {
                if(!method.isAnnotationPresent(RequestMapping.class)){ continue;}
                RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                String regex = requestMapping.value();
                regex = (baseUrl + regex).replaceAll("/+", "/");
                Pattern pattern = Pattern.compile(regex);
                handlerMapping.add(new Handler(entry.getValue(), method, pattern));
                System.out.println("Mapping: " + regex + "," + method.getName());
            }
        }

    }

    private static void doAutowired() {
        if(ioc.isEmpty()) {return;}
        for(Map.Entry<String, Object> entry: ioc.entrySet()) {
            //注入的意思就是把所有的IOC容器中加了@Autowired注解的字段赋值
            //包含私有字段
            Field[] fields = entry.getValue().getClass().getDeclaredFields();
            for(Field field : fields) {

                //判断是否加了@Autowired注解
                if(!field.isAnnotationPresent(Autowired.class)) { continue;}

                Autowired autowired = field.getAnnotation(Autowired.class);
                String beanName = autowired.value();
                if("".equals(beanName)) {
                    beanName = field.getType().getName();
                }
                //如果这个字段是私有字段的话，那么要强制访问
                field.setAccessible(true);
                try {
                    field.set(entry.getValue(), ioc.get(beanName));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void doInstance() {

        if(classNames.isEmpty()) {return;}

        for(String className: classNames) {
            try {
                Class<?> clazz = Class.forName(className);
                if(clazz.isAnnotationPresent(Controller.class)) {

                    Object instance = clazz.newInstance();
                    String beanName = lowerFirstCase(clazz.getSimpleName());
                    ioc.put(beanName, instance);
                } else if (clazz.isAnnotationPresent(Service.class)) {

                    Service service = clazz.getAnnotation(Service.class);
                    //2.优先使用自定义命名
                    String beanName = service.value();
                    if("".equals(beanName.trim())) {
                        //1.默认使用类名首字母小写
                        beanName = lowerFirstCase(clazz.getSimpleName());
                    }
                    Object instance = clazz.newInstance();
                    ioc.put(beanName, instance);
                    //3.自动类型匹配（例如：将实现类赋值给接口）
                    Class<?>[] interfaces = clazz.getInterfaces();
                    for(Class<?> inter: interfaces) {
                        ioc.put(inter.getName(), instance);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //利用ASCII码的差值
    private static String lowerFirstCase(String str) {
        char[] chars = str.toCharArray();
        chars[0] += 32;
        return String.valueOf(chars);
    }

    private static void doScanner(String basePackage) {

        URL url =  Object.class.getResource("/" + basePackage.replaceAll("\\.", "/"));
        File dir = new File(url.getFile());
        for(File file: dir.listFiles()) {
            if(file.isDirectory()){
                doScanner(basePackage + "." + file.getName());
            } else {
                String className = basePackage + "." + file.getName().replace(".class", "");
                classNames.add(className);
                System.out.println(className);
            }
        }
    }

    private static void doLoadConfig(String location) {
        InputStream inputStream = Object.class.getResourceAsStream(location);
        try {
            contextConfig.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Handler记录Controoler中的RequestMapping和Method的对应关系
     */
    private static class Handler {
        //方法对应的实例
        private Object controller;
        //映射的方法
        private Method method;
        //URL正则匹配
        private Pattern pattern;
        //参数顺序
        private Map<String, Integer> paramIndexMapping;

        public Handler(Object controller, Method method, Pattern pattern) {
            this.controller = controller;
            this.method = method;
            this.pattern = pattern;
            paramIndexMapping = new HashMap<String, Integer>();
            putParamIndexMapping(method);
        }

        private void putParamIndexMapping(Method method) {

            //提取方法中加了注解的参数
            Annotation[][] pa = method.getParameterAnnotations();
            for(int i = 0; i < pa.length; i ++) {
                for(Annotation a : pa[i]) {
                    if(a instanceof RequestParam) {
                        String paramName = ((RequestParam) a).value();
                        if(!"".equals(paramName)) {
                            paramIndexMapping.put(paramName, i);
                        }
                    }
                }
            }

            //提取方法中的request和response参数
            Class<?>[] paramTypes = method.getParameterTypes();
            for(int i = 0; i < paramTypes.length; i ++) {
                Class<?> type = paramTypes[i];

                if(type == Request.class || type == Response.class) {
                    paramIndexMapping.put(type.getName(), i);
                }
            }

        }

    }
}
