package redrock.netty.common.auth;

import lombok.Data;
import redrock.netty.common.OperationResult;

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
