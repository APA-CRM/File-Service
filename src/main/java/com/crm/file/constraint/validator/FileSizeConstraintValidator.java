package com.crm.file.constraint.validator;

import com.crm.file.constraint.FileSizeConstraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import static java.util.Objects.isNull;

@RequiredArgsConstructor
@Component
public class FileSizeConstraintValidator implements ConstraintValidator<FileSizeConstraint, MultipartFile> {

    private static final int BYTES_PER_MB = 1_048_576;
    private final Environment environment;
    private boolean required;
    private int fileMaxSize;

    @Override
    public void initialize(FileSizeConstraint constraintAnnotation) {
        required = constraintAnnotation.required();
        fileMaxSize = Integer.parseInt(environment.getProperty(constraintAnnotation.maxSizeProperty()));
    }

    @Override
    @SneakyThrows
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
        if (isNull(file) || file.isEmpty()) {
            if (required) {
                context.disableDefaultConstraintViolation();

                context.buildConstraintViolationWithTemplate("File is required")
                        .addConstraintViolation();
            }
            return !required;
        }

        if (file.getBytes().length > fileMaxSize) {
            context.disableDefaultConstraintViolation();

            context.buildConstraintViolationWithTemplate(
                            "Max size of file is %d MB".formatted(fileMaxSize / BYTES_PER_MB)
                    )
                    .addConstraintViolation();
            return false;
        }

        return true;
    }

}
