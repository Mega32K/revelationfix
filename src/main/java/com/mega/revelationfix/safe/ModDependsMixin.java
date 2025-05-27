package com.mega.revelationfix.safe;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.CLASS)
public @interface ModDependsMixin {
    String value() default "revelationfix";
}
