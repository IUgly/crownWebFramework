package crown.netty.common;

import lombok.Data;

/**
 * @author kuangjunlin
 */
@Data
public class ResponseMessage <T>{
    private int status;
    private String info;
    private T data;

    public ResponseMessage(int status, String info, T data) {
        this.status = status;
        this.info = info;
        this.data = data;
    }

    public ResponseMessage(int status, String info) {
        this.status = status;
        this.info = info;
    }

    public ResponseMessage() {
    }

    public ResponseMessage(ResponseStatus responseStatus, T data) {
        this(responseStatus);
        this.data = data;
    }

    public ResponseMessage(ResponseStatus responseStatus) {
        this.status = responseStatus.getValue();
        this.info = responseStatus.getReasonPhrase();
    }
}