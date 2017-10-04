package annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface Param {

    /**
     * 参数类型
     */
    Class<?> clazz();
    
    /**
     * 参数名称
     */
    String name();
    
}
