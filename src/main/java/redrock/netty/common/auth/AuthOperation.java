package redrock.netty.common.auth;

import lombok.Data;
import lombok.extern.java.Log;
import redrock.netty.common.Operation;
import redrock.netty.common.ResponseMessage;
import redrock.netty.common.ResponseStatus;

/**
 * @author kuangjunlin
 */
@Data
@Log
public class AuthOperation extends Operation {
    private final String username;
    private final String password;
    @Override
    public ResponseMessage execute() {
        System.out.println("验证用户权限");
        if ("admin".equals(this.username)) {
            AuthOperationResult result = new AuthOperationResult(true);
            return new ResponseMessage<>(ResponseStatus.OK, result);
        }
        AuthOperationResult result = new AuthOperationResult(false);
        return new ResponseMessage<>(ResponseStatus.FAIL_401, result);
    }
}
