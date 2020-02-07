package crown.webframework.framework.annotation;

import java.lang.annotation.*;

/**
 * @author webframework
 * @since 2019/11/6 12:38
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Controller {
    String value() default "";
}
