package redrock.netty.common.keepalive;

import lombok.Data;
import redrock.netty.common.OperationResult;

@Data
public class KeepAliveOperationResult extends OperationResult {
    private final long time;
}
