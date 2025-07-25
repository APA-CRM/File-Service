package com.crm.file.facade;

import com.crm.file.dto.request.CreateFileRequest;
import com.crm.file.dto.request.UpdateFileRequest;
import com.crm.file.dto.response.FileResponse;
import com.crm.file.dto.response.FileWithChildrenResponse;
import com.crm.file.mapper.FileMetadataMapper;
import com.crm.file.persistance.entity.FileMetadata;
import com.crm.file.service.FileService;
import com.crm.sharedlib.annotations.Facade;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Facade
@RequiredArgsConstructor
public class FileFacade {

    private final FileService fileService;

    private final FileMetadataMapper metadataMapper;

    @Transactional(readOnly = true)
    public FileWithChildrenResponse getFile(UUID id) {
        FileMetadata metadata = fileService.getFileOrThrowException(id);

        return metadataMapper.toDtoWithChildren(metadata);
    }

    public Resource getFileContent(UUID id) {
        return fileService.getFileContent(id);
    }

    public FileResponse createFile(CreateFileRequest request) {
        FileMetadata metadata = fileService.createFile(request);

        return metadataMapper.toDto(metadata);
    }

    @Transactional
    public FileWithChildrenResponse updateFile(UUID id, UpdateFileRequest request) {
        FileMetadata metadata = fileService.updateFile(id, request);

        return metadataMapper.toDtoWithChildren(metadata);
    }

    public void deleteFile(UUID id, Boolean forceDelete) {
        fileService.deleteFile(id, forceDelete);
    }

}
