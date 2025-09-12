package com.crm.file.facade;

import com.crm.file.dto.request.CreateFileRequest;
import com.crm.file.dto.request.UpdateFileRequest;
import com.crm.file.dto.response.FileResponse;
import com.crm.file.dto.response.FileWithChildrenResponse;
import com.crm.file.mapper.FileMetadataMapper;
import com.crm.file.persistance.entity.FileContent;
import com.crm.file.persistance.entity.FileMetadata;
import com.crm.file.service.FileService;
import com.crm.sharedlib.annotations.Facade;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Facade
@RequiredArgsConstructor
public class FileFacade {

    private final FileService fileService;

    private final FileMetadataMapper metadataMapper;

    @Transactional(readOnly = true)
    public FileWithChildrenResponse getFile(UUID id) {
        FileMetadata metadata = fileService.getFileMetadataOrThrowException(id);

        return metadataMapper.toDtoWithChildren(metadata);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<Resource> getFileContent(UUID id) {
        FileContent content = fileService.getFileContent(id);

        byte[] bytes = content.getContent();
        ByteArrayResource resource = new ByteArrayResource(bytes);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(
                MediaType.parseMediaType(content.getMetadata().getFileExtension().getMimeType())
        );

        return new ResponseEntity<Resource>(resource, headers, HttpStatus.OK);
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
