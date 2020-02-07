package redrock.webframework.framework.annotation;

import java.lang.annotation.*;

/**
 * @author webframework
 * @since 2019/11/6 12:38
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestParam {
    String value() default "";
}
