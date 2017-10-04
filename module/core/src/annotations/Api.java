package annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface Api {
	
	public enum HttpType{ GET, POST};

	/**
	 * 接口名称
	 */
    String name();
    
    /**
     * 请求类型
     */
    HttpType type();

    /**
     * 请求URL
     */
    String url();

    /**
     * 参数列表
     */
    Param[] param() default {};
    
    /**
     * 返回值
     */
    Return[] ret() default {};
    
}
