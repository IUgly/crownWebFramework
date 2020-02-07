package redrock.netty.common.keepalive;

import lombok.Data;
import redrock.netty.common.Operation;
import redrock.netty.common.ResponseMessage;
import redrock.netty.common.ResponseStatus;

/**
 * @author kuangjunlin
 */
@Data
public class KeepAliveOperation extends Operation {
    private long time;

    public KeepAliveOperation() {
        this.time = System.nanoTime();
    }



    @Override
    public ResponseMessage execute() {
        KeepAliveOperationResult orderResponse = new KeepAliveOperationResult(time);
        return new ResponseMessage<>(ResponseStatus.OK, orderResponse);
    }
}
