package redrock.netty.common;

/**
 * @author kuangjunlin
 */
public class RequestMessage extends Message<Operation> {
    public RequestMessage() {
    }

    @Override
    public Class getMessageBodyDecodeClass(String uri) {
        try {
            return OperationType.fromUri(uri).getOperationClazz();
        } catch (AssertionError e) {
            return null;
        }
    }

    public RequestMessage (Long streamId, Operation operation) {
        MessageHeader messageHeader = new MessageHeader();
        messageHeader.setStreamId(streamId);
        messageHeader.setUri(OperationType.fromOperation(operation).getUri());
        this.setMessageHeader(messageHeader);
        this.setMessageBody(operation);
    }
}
