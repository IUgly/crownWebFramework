package crown.netty.common.order;

import lombok.Data;
import crown.netty.common.OperationResult;

/**
 * @author kuangjunlin
 */
@Data
public class OrderOperationResult extends OperationResult {
    private final int tableId;
    private final String dish;
    private final boolean complete;
}
