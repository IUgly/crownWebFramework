package redrock.netty.common.order;

import com.google.common.util.concurrent.Uninterruptibles;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import redrock.netty.common.Operation;
import redrock.netty.common.ResponseMessage;
import redrock.netty.common.ResponseStatus;

import java.util.concurrent.TimeUnit;

@Data
@Slf4j
public class OrderOperation extends Operation {
    private int tableId;
    private String dish;

    public OrderOperation(int tableId, String dish) {
        this.tableId = tableId;
        this.dish = dish;
    }

    @Override
    public ResponseMessage execute() {
        log.info("order`s executing startup with orderRequest:" + toString());

        //模拟任务消耗较长时间的情况
        Uninterruptibles.sleepUninterruptibly(3000, TimeUnit.SECONDS);
        log.info("order`s executing complete");
        OrderOperationResult orderResponse = new OrderOperationResult(tableId, dish, true);
        return new ResponseMessage<>(ResponseStatus.OK, orderResponse);
    }
}
