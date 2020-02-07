package crown.netty.common;

import crown.netty.common.auth.AuthOperation;
import crown.netty.common.auth.AuthOperationResult;
import crown.netty.common.keepalive.KeepAliveOperation;
import crown.netty.common.keepalive.KeepAliveOperationResult;
import crown.netty.common.order.OrderOperation;
import crown.netty.common.order.OrderOperationResult;

import java.util.function.Predicate;

/**
 * @author kuangjunlin
 */
public enum OperationType {
    /**
     *  TODO
     */
    AUTH("/auth", AuthOperation.class, AuthOperationResult.class),
    KEEPALIVE("keepAlive", KeepAliveOperation.class, KeepAliveOperationResult.class),
    ORDER("order", OrderOperation.class, OrderOperationResult.class);

    private String uri;
    private Class<? extends Operation> operationClazz;
    private Class<? extends OperationResult> operationResultClazz;

    OperationType(String uri, Class<? extends Operation> operationClazz, Class<? extends OperationResult> operationResultClazz) {
        this.uri = uri;
        this.operationClazz = operationClazz;
        this.operationResultClazz = operationResultClazz;
    }

    public static OperationType fromUri(String uri){
        return getOperationType(requestType -> requestType.uri.equals(uri));
    }

    public static OperationType fromOperation(Operation operation){
        return getOperationType(requestType -> requestType.operationClazz == operation.getClass());
    }

    private static OperationType getOperationType(Predicate<OperationType> predicate){
        OperationType[] values = values();
        for (OperationType operationType : values) {
            if(predicate.test(operationType)){
                return operationType;
            }
        }

        throw new AssertionError("no found type");
    }

    public String getUri() {
        return uri;
    }
    public Class<? extends Operation> getOperationClazz() {
        return operationClazz;
    }

    public void setOperationClazz(Class<? extends Operation> operationClazz) {
        this.operationClazz = operationClazz;
    }

    public Class<? extends OperationResult> getOperationResultClazz() {
        return operationResultClazz;
    }

    public void setOperationResultClazz(Class<? extends OperationResult> operationResultClazz) {
        this.operationResultClazz = operationResultClazz;
    }

}
