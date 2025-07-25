package com.crm.file.mapper;

import com.crm.file.dto.request.CreateFileRequest;
import com.crm.file.dto.response.FileResponse;
import com.crm.file.persistance.entity.FileMetadata;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class FileMetadataMapper {

    public abstract FileMetadata toEntity(CreateFileRequest createFileRequest);

    public abstract FileResponse toDto(FileMetadata metadata);

}
