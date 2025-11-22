package com.crm.file.constraint;

import com.crm.file.constraint.validator.FileExtensionConstraintValidator;
import com.crm.file.enums.FileExtension;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Target(ElementType.FIELD)
@Constraint(validatedBy = FileExtensionConstraintValidator.class)
@Retention(RUNTIME)
public @interface FileExtensionConstraint {

    String message() default "Unsupported file extension";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    FileExtension[] allowedExtensions();
}
