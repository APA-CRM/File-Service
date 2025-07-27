package com.crm.file.constraint;

import com.crm.file.constraint.validator.FileSizeConstraintValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Target(ElementType.FIELD)
@Constraint(validatedBy = FileSizeConstraintValidator.class)
@Retention(RUNTIME)
public @interface FileSizeConstraint {

    String message() default "Invalid sign-up request";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
