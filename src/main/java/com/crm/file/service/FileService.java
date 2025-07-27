package com.crm.file.service;

import com.crm.file.dto.request.CreateFileRequest;
import com.crm.file.dto.request.UpdateFileRequest;
import com.crm.file.enums.FileExtension;
import com.crm.file.enums.FileType;
import com.crm.file.mapper.FileMetadataMapper;
import com.crm.file.persistance.entity.FileContent;
import com.crm.file.persistance.entity.FileMetadata;
import com.crm.file.persistance.repository.FileMetadataRepository;
import com.crm.sharedlib.exception.BadRequestException;
import com.crm.sharedlib.exception.ConflictException;
import com.crm.sharedlib.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Service
@RequiredArgsConstructor
public class FileService {

    private final FileMetadataRepository metadataRepository;

    private final FileMetadataMapper metadataMapper;

    public FileMetadata getFileOrThrowException(UUID id) {
        return metadataRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("File is not found"));
    }

    public FileMetadata getFileContent(UUID id) {
        FileMetadata metadata = getFileOrThrowException(id);

        if (metadata.getFileType() == FileType.DIRECTORY) {
            throw new ConflictException("This file is directory");
        }

        return metadata;
    }

    @Transactional
    public FileMetadata createFile(CreateFileRequest request) {
        validate(request.getFileType(), request.getContent());

        FileMetadata metadata = metadataMapper.toEntity(request);


        if (request.getFileType() == FileType.FILE) {
            createFileContent(metadata, request.getContent());
            metadata.setFileExtension(
                    getFileExtension(request.getContent().getOriginalFilename())
            );
        }

        if (nonNull(request.getParentFileId())) {
            FileMetadata parent = getParentOrThrowException(request.getParentFileId());
            metadata.setParentFile(parent);
        }

        return metadataRepository.save(metadata);
    }

    @Transactional
    @SneakyThrows
    public FileMetadata updateFile(UUID id, UpdateFileRequest request) {
        FileMetadata metadata = getFileOrThrowException(id);

        validate(metadata.getFileType(), request.getContent());

        metadata = metadataMapper.updateEntity(request, metadata);

        if (metadata.getFileType() == FileType.FILE) {
            metadata.getFileContent().setContent(request.getContent().getBytes());
            metadata.setFileExtension(
                    getFileExtension(request.getContent().getOriginalFilename())
            );
        }

        if (nonNull(request.getParentFileId())) {
            FileMetadata parent = getParentOrThrowException(request.getParentFileId());
            metadata.setParentFile(parent);
        }

        return metadataRepository.save(metadata);
    }

    @Transactional
    public void deleteFile(UUID id, Boolean forceDelete) {
        FileMetadata metadata = getFileOrThrowException(id);

        if (!metadata.getChildrenFiles().isEmpty() && !forceDelete) {
            throw new ConflictException("Directory contains files");
        }

        metadataRepository.delete(metadata);
    }

    private FileMetadata getParentOrThrowException(UUID parentId) {
        return metadataRepository.findById(parentId)
                .orElseThrow(() -> new NotFoundException("Parent file is not found"));
    }

    @SneakyThrows
    private void createFileContent(FileMetadata metadata, MultipartFile multipartFile) {
        FileContent content = new FileContent();
        content.setContent(multipartFile.getBytes());
        content.setMetadata(metadata);

        metadata.setFileContent(content);
    }

    @SneakyThrows
    private void validate(FileType fileType, MultipartFile multipartFile) {
        if (fileType == FileType.FILE) {
            if (isNull(multipartFile) || multipartFile.isEmpty()) {
                throw new BadRequestException("File must have a content");
            }
        }
    }

    private FileExtension getFileExtension(String fileName) {
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
