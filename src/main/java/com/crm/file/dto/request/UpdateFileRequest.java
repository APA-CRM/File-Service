package com.crm.file.dto.request;

import com.crm.file.constraint.FileExtensionConstraint;
import com.crm.file.constraint.FileSizeConstraint;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

import static com.crm.file.enums.FileExtension.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UpdateFileRequest {

    @NotBlank(message = "Name can't be blank")
    private String name;

    private UUID parentFileId;

    @FileSizeConstraint(maxSizeProperty = "app.file.max-size")
    @FileExtensionConstraint(allowedExtensions = {PDF, DOCX, XLSX, JPEG, PNG, ZIP, RAR, MP3, MP4})
    private MultipartFile content;

}
