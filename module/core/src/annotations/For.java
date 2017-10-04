package annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import play.db.Model;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface For {
    Class<? extends Model> value();
}
