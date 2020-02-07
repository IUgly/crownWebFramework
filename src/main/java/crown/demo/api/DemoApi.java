package crown.demo.api;

import crown.demo.service.DemoService;
import crown.webframework.framework.annotation.Autowired;
import crown.webframework.framework.annotation.Controller;
import crown.webframework.framework.annotation.RequestMapping;
import crown.webframework.framework.annotation.RequestParam;
import crown.webframework.framework.servlet.Request;
import crown.webframework.framework.servlet.Response;

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
