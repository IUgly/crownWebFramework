package crown.netty.common.keepalive;

import lombok.Data;
import crown.netty.common.OperationResult;

@Data
public class KeepAliveOperationResult extends OperationResult {
    private final long time;
}
