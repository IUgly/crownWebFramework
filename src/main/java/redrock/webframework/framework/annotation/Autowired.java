package redrock.webframework.framework.annotation;

import java.lang.annotation.*;

/**
 * @author webframework
 * @since 2019/11/6 12:38
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Autowired {
    String value() default "";
}
