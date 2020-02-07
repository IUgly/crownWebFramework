# crownWebFramework
基于Netty的仿Spring boot的简易web框架



### 使用方法

1. 在**Resource目录**中配置**application.properties**文件

   * 配置业务代码所在的**package**

   * 例如: 

     ```properties
     scanPackage=crown.demo
     ```

2. 在**配置的package**下编写业务代码

   * 例如: DemoApi

     ```java
     @Controller
     @RequestMapping("/demo")
     public class DemoApi {
         @Autowired
         private DemoService demoService;
     
         @RequestMapping("/query")
         public void query(Request req,
                           Response resp,
                           @RequestParam("name") String name){
             String result = demoService.get(name);
             resp.setJsonContent(result);
         }
     }
     ```

3. 在**WebServerApplication**下设置**服务端口**并运行

4. 访问对应服务即可