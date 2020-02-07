package redrock.netty.common;

import lombok.Data;

/**
 * @author kuangjunlin
 */
@Data
public abstract class MessageBody <T> {
    private Integer status;
    private String info;
    private T data;
}
