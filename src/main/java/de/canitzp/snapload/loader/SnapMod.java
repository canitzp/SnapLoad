package de.canitzp.snapload.loader;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author canitzp
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface SnapMod {
    String modid();

    String name() default "";

    String version() default "";

    String author() default "";

    Side[] sides() default {Side.CLIENT, Side.SERVER};
}
