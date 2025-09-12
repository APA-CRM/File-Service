package com.crm.file.service;

import com.crm.file.enums.FileExtension;
import com.crm.sharedlib.exception.BadRequestException;
import org.springframework.stereotype.Service;

import static java.util.Objects.isNull;

@Service
public class FileExtensionFactory {

    public FileExtension getFileExtension(String fileName) {
        if (isNull(fileName)) {
            throw new BadRequestException("File doesn't have a name");
        }

        // + 1 because we need file extension name without a dot
        String fileExtensionName = fileName.substring(fileName.indexOf(".") + 1);

        for (FileExtension extension : FileExtension.values()) {
            for (String extensionName : extension.getExtensions()) {
                if (fileExtensionName.equals(extensionName)) {
                    return extension;
                }
            }
        }

        throw new BadRequestException("Unsupported file extension");
    }

}
