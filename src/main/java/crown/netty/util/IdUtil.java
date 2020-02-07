package crown.netty.util;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author kuangjunlin
 */
public class IdUtil {
    public static final AtomicLong idx = new AtomicLong();

    public IdUtil() {
    }

    public static long nextId() { return idx.incrementAndGet(); }
}
