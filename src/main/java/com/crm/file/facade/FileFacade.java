package com.crm.file.facade;

import com.crm.file.dto.request.CreateFileRequest;
import com.crm.file.dto.request.UpdateFileRequest;
import com.crm.file.dto.response.FileResponse;
import com.crm.file.mapper.FileMetadataMapper;
import com.crm.file.persistance.entity.FileMetadata;
import com.crm.file.service.FileService;
import com.crm.sharedlib.annotations.Facade;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Facade
@RequiredArgsConstructor
public class FileFacade {

    private final FileService fileService;

    private final FileMetadataMapper metadataMapper;

    public FileResponse getFile(UUID id) {
        FileMetadata metadata = fileService.getFileOrThrowException(id);

        return metadataMapper.toDto(metadata);
    }

    public FileResponse createFile(CreateFileRequest request) {
        FileMetadata metadata = fileService.createFile(request);

        return metadataMapper.toDto(metadata);
    }

    public FileResponse updateFile(UUID id, UpdateFileRequest request) {
        FileMetadata metadata = fileService.updateFile(id, request);

        return metadataMapper.toDto(metadata);
    }

    public void deleteFile(UUID id, Boolean forceDelete) {
        fileService.deleteFile(id, forceDelete);
    }

}
