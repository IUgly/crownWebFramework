package crown.netty.common.auth;

import lombok.Data;
import crown.netty.common.OperationResult;

/**
 * @author kuangjunlin
 */
@Data
public class AuthOperationResult extends OperationResult{
    private boolean auth;

    public AuthOperationResult(boolean auth) {
        this.auth = auth;
    }
}
