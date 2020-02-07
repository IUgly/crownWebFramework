package crown.demo.service;


import crown.webframework.framework.annotation.Service;


/**
 * @author kuangjunlin
 */
@Service
public class DemoServiceImpl implements DemoService {
    @Override
    public String get(String name) {
        return String.format("My name is %s.", name);
    }
}
