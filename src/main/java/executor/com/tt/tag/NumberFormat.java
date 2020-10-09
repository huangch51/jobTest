package executor.com.tt.tag;

import java.lang.annotation.*;

/**
 * @author huangcaihuan
 * @Since 2020/9/25
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.ANNOTATION_TYPE,ElementType.FIELD,ElementType.METHOD,ElementType.PARAMETER})
public @interface NumberFormat {

    int point() default 1;

    String profile() default " ";

    boolean isPrint() default false;

    String percent() default "%";

}
