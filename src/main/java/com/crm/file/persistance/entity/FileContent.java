package com.crm.file.persistance.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@EntityListeners(AuditingEntityListener.class)
public class FileContent {

    @Id
    private UUID fileMetadataId;

    @OneToOne(optional = false, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @MapsId
    @JoinColumn(name = "fileMetadataId")
    private FileMetadata metadata;

    @Lob
    private byte[] content;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;
}
