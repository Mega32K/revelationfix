package com.mega.revelationfix.safe;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.CLASS)
public @interface NoModDependsMixin {
    String value() default "revelationfix";
}
