package crown.netty.common.keepalive;

import lombok.Data;
import crown.netty.common.Operation;
import crown.netty.common.ResponseMessage;
import crown.netty.common.ResponseStatus;

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
