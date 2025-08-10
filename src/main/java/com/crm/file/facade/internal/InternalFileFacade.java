package com.crm.file.facade.internal;

import com.crm.file.dto.response.FileResponse;
import com.crm.file.mapper.FileMetadataMapper;
import com.crm.file.persistance.entity.FileMetadata;
import com.crm.file.service.FileService;
import com.crm.sharedlib.annotations.Facade;
import lombok.RequiredArgsConstructor;

@Facade
@RequiredArgsConstructor
public class InternalFileFacade {

    private final FileService fileService;

    private final FileMetadataMapper mapper;

    public FileResponse createDirectory(String name) {
        FileMetadata metadata = fileService.createDefaultDirectory(name);

        return mapper.toDto(metadata);
    }

}
