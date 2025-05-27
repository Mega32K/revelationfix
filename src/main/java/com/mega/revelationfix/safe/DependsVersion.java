package com.mega.revelationfix.safe;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.CLASS)
public @interface DependsVersion {
    String value() default "1.0.0";
}
