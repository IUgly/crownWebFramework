package redrock.netty.common;

/**
 * @author kuangjunlin
 */

public enum ResponseStatus {
    /**
     *  状态码
     */
    OK(200,"success"),
    FAIL_400(400, "客户端请求参数错误"),
    FAIL_401(401," Incorrect password or authentication failure "),
    FAIL_403(403, "服务器拒绝"),
    FAIL_503(503, "服务器超载，暂无法处理");

    private final int value;

    private final String reasonPhrase;

    ResponseStatus(int value, String reasonPhrase) {
        this.value = value;
        this.reasonPhrase = reasonPhrase;
    }

    public int getValue() {
        return value;
    }

    public String getReasonPhrase() {
        return reasonPhrase;
    }
}