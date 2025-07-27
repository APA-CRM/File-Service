package com.crm.file.constraint.validator;

import com.crm.file.constraint.FileSizeConstraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import static java.util.Objects.isNull;

@Component
public class FileSizeConstraintValidator implements ConstraintValidator<FileSizeConstraint, MultipartFile> {

    private static final int BYTES_PER_MB = 1_048_576;

    @Value("${app.file.max-size}")
    private Integer fileMaxSize;

    @Override
    @SneakyThrows
    public boolean isValid(MultipartFile value, ConstraintValidatorContext context) {
        if (isNull(value) || value.isEmpty()) {
            return true;
        }

        if (value.getBytes().length > fileMaxSize) {
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
