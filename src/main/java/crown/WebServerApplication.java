package crown;

import crown.netty.server.Server;

/**
 * @author kuangjunlin
 */
public class WebServerApplication {
    public static void main(String[] args) {
        Server.run(8080);
    }
}
