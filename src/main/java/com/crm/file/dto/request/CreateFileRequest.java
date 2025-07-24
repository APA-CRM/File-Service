package com.crm.file.dto.request;

import com.crm.file.enums.FileType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CreateFileRequest {

    @NotBlank(message = "Name can't be blank")
    private String name;

    @NotNull(message = "File type can't be null")
    private FileType fileType;

    private UUID parentFileId;

    private MultipartFile multipartFile;

}
