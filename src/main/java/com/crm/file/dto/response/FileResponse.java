package com.crm.file.dto.response;

import com.crm.file.enums.FileExtension;
import com.crm.file.enums.FileType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FileResponse {

    private UUID id;

    private String name;

    private FileType fileType;

    private FileExtension fileExtension;

    private Instant createdAt;

    private Instant updatedAt;

}
