package com.crm.file.mapper;

import com.crm.file.dto.request.CreateFileRequest;
import com.crm.file.dto.request.UpdateFileRequest;
import com.crm.file.dto.response.FileResponse;
import com.crm.file.dto.response.FileWithChildrenResponse;
import com.crm.file.persistance.entity.FileMetadata;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class FileMetadataMapper {

    public abstract FileMetadata updateEntity(UpdateFileRequest request, @MappingTarget FileMetadata metadata);

    public abstract FileMetadata toEntity(CreateFileRequest createFileRequest);

    public abstract FileResponse toDto(FileMetadata metadata);

    public abstract FileWithChildrenResponse toDtoWithChildren(FileMetadata metadata);

}
