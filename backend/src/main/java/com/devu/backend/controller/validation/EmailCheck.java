package com.devu.backend.controller.validation;

import javax.validation.Constraint;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EmailCheckValidator.class)
public @interface EmailCheck {

    String ERROR_MESSAGE = "잘못된 이메일 양식입니다.";

    String message() default ERROR_MESSAGE;

    Class[] groups() default {};

    Class[] payload() default {};
}
