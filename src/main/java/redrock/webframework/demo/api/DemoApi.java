package redrock.webframework.demo.api;

import redrock.webframework.demo.service.DemoService;
import redrock.webframework.framework.annotation.Autowired;
import redrock.webframework.framework.annotation.Controller;
import redrock.webframework.framework.annotation.RequestMapping;
import redrock.webframework.framework.annotation.RequestParam;
import redrock.webframework.framework.servlet.Request;
import redrock.webframework.framework.servlet.Response;

/**
 * @author kuangjunlin
 */
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
        System.out.println(result);
        resp.setJsonContent(result);
    }

    @RequestMapping("/add")
    public void add(Request req,
                    Response resp,
                    @RequestParam("a") Integer a,
                    @RequestParam("b") Integer b) {
        resp.setJsonContent("{\"info\":\"success\"}");
    }
}
