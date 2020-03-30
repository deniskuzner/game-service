package com.mozzartbet.gameservice.annotation;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Target({METHOD, TYPE})
@Retention(RUNTIME)
public @interface LogExecutionTime {

}
