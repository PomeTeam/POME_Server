package com.example.pomeserver.global.util.authResolver;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import springfox.documentation.annotations.ApiIgnore;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@ApiIgnore
public @interface UserId {
}
