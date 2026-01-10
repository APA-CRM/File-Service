package com.crm.file.facade.internal;

import com.crm.file.persistance.entity.FileMetadata;
import com.crm.file.service.FileService;
import com.crm.sharedlib.core.annotations.Facade;
import com.crm.sharedlib.core.dto.response.FileIdResponse;
import lombok.RequiredArgsConstructor;

@Facade
@RequiredArgsConstructor
public class InternalFileFacade {

    private final FileService fileService;

    public FileIdResponse createDirectory(String name) {
        FileMetadata metadata = fileService.createDefaultDirectory(name);

        return new FileIdResponse(metadata.getId());
    }

}
