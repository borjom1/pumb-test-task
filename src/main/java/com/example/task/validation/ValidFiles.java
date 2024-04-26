package com.example.task.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
@Constraint(validatedBy = FilesValidator.class)
public @interface ValidFiles {

    String message() default "only .xml and .csv files are supported";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
