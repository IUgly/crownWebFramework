package redrock.netty.common.order;

import lombok.Data;
import redrock.netty.common.OperationResult;

/**
 * @author kuangjunlin
 */
@Data
public class OrderOperationResult extends OperationResult {
    private final int tableId;
    private final String dish;
    private final boolean complete;
}
