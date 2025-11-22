package com.crm.file.constraint.validator;

import com.crm.file.constraint.FileExtensionConstraint;
import com.crm.file.enums.FileExtension;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

import static java.util.Objects.isNull;

public class FileExtensionConstraintValidator implements ConstraintValidator<FileExtensionConstraint, MultipartFile> {

    private FileExtension[] allowedExtensions;

    @Override
    public void initialize(FileExtensionConstraint constraintAnnotation) {
        allowedExtensions = constraintAnnotation.allowedExtensions();
    }

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
        if (isNull(file) || file.isEmpty()) {
            return true;
        }

        String fileName = file.getOriginalFilename();

        String fileExtensionName = fileName.substring(fileName.indexOf(".") + 1);

        for (FileExtension extension : allowedExtensions) {
            for (String extensionName : extension.getExtensions()) {
                if (fileExtensionName.equals(extensionName)) {
                    return true;
                }
            }
        }

        return false;
    }
}
