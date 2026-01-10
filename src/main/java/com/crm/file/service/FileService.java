package com.crm.file.service;

import com.crm.file.dto.request.CreateFileRequest;
import com.crm.file.dto.request.UpdateFileRequest;
import com.crm.file.enums.FileType;
import com.crm.file.mapper.FileMetadataMapper;
import com.crm.file.persistance.entity.FileContent;
import com.crm.file.persistance.entity.FileMetadata;
import com.crm.file.persistance.repository.FileContentRepository;
import com.crm.file.persistance.repository.FileMetadataRepository;
import com.crm.sharedlib.core.exception.BadRequestException;
import com.crm.sharedlib.core.exception.ConflictException;
import com.crm.sharedlib.core.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Service
@RequiredArgsConstructor
public class FileService {

    private final FileExtensionFactory fileExtensionFactory;

    private final FileContentRepository contentRepository;
    private final FileMetadataRepository metadataRepository;

    private final FileMetadataMapper metadataMapper;

    public FileMetadata getFileMetadataOrThrowException(UUID id) {
        return metadataRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("File is not found"));
    }

    public FileContent getFileContentOrThrowException(UUID id) {
        return contentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("File is not found"));
    }

    public FileContent getFileContent(UUID id) {
        FileContent content = getFileContentOrThrowException(id);

        if (content.getMetadata().getFileType() == FileType.DIRECTORY) {
            throw new ConflictException("This file is directory");
        }

        return content;
    }

    @Transactional
    public FileMetadata createFile(CreateFileRequest request) {
        validate(request.getFileType(), request.getContent());

        FileMetadata metadata = metadataMapper.toEntity(request);

        setParentFile(metadata, request.getParentFileId());

        if (request.getFileType() == FileType.FILE) {
            FileContent content = createFileContent(metadata, request.getContent());
            metadata.setFileExtension(
                    fileExtensionFactory.getFileExtension(request.getContent().getOriginalFilename())
            );

            contentRepository.save(content);
        }


        return metadataRepository.save(metadata);
    }

    @Transactional
    public FileMetadata createDefaultDirectory(String name) {
        FileMetadata metadata = new FileMetadata();
        metadata.setName(name);
        metadata.setFileType(FileType.DIRECTORY);

        return metadataRepository.save(metadata);
    }

    @Transactional
    @SneakyThrows
    public FileMetadata updateFile(UUID id, UpdateFileRequest request) {
        FileMetadata metadata = getFileMetadataOrThrowException(id);

        metadata = metadataMapper.updateEntity(request, metadata);

        setParentFile(metadata, request.getParentFileId());

        if (metadata.getFileType() == FileType.FILE && nonNull(request.getContent())) {
            FileContent content = getFileContentOrThrowException(id);
            content.setContent(request.getContent().getBytes());
            metadata.setFileExtension(
                    fileExtensionFactory.getFileExtension(request.getContent().getOriginalFilename())
            );

            contentRepository.save(content);
        }

        return metadataRepository.save(metadata);
    }

    @Transactional
    public void deleteFile(UUID id, Boolean forceDelete) {
        FileMetadata metadata = getFileMetadataOrThrowException(id);

        if (!metadata.getChildrenFiles().isEmpty() && !forceDelete) {
            throw new ConflictException("Directory contains files");
        }

        List<UUID> ids = metadata.getChildrenFiles()
                .stream().map(FileMetadata::getId)
                .toList();

        contentRepository.deleteAllById(ids);

        contentRepository.deleteById(id);
        metadataRepository.delete(metadata);
    }

    private FileMetadata getParentOrThrowException(UUID parentId) {
        return metadataRepository.findById(parentId)
                .orElseThrow(() -> new NotFoundException("Parent file is not found"));
    }

    @SneakyThrows
    private FileContent createFileContent(FileMetadata metadata, MultipartFile multipartFile) {
        FileContent content = new FileContent();
        content.setContent(multipartFile.getBytes());
        content.setMetadata(metadata);

        return content;
    }

    @SneakyThrows
    private void validate(FileType fileType, MultipartFile multipartFile) {
        if (fileType == FileType.FILE) {
            if (isNull(multipartFile) || multipartFile.isEmpty()) {
                throw new BadRequestException("File must have a content");
            }
        }
    }

    private void setParentFile(FileMetadata metadata, UUID parentId) {
        if (nonNull(parentId)) {
            FileMetadata parent = getParentOrThrowException(parentId);
            if (parent.getFileType() == FileType.FILE) {
                throw new ConflictException("Parent file has 'FILE' Type");
            }

            metadata.setParentFile(parent);
        }
    }

}
