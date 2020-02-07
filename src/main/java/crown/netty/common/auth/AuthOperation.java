package crown.netty.common.auth;

import lombok.Data;
import lombok.extern.java.Log;
import crown.netty.common.Operation;
import crown.netty.common.ResponseMessage;
import crown.netty.common.ResponseStatus;

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
