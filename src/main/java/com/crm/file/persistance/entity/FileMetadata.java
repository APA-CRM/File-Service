package com.crm.file.persistance.entity;

import com.crm.file.enums.FileType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@EntityListeners(AuditingEntityListener.class)
public class FileMetadata {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private FileType fileType;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private FileMetadata parentFile;

    @OneToOne(mappedBy = "metadata")
    @PrimaryKeyJoinColumn
    private FileContent fileContent;

    @OneToMany(mappedBy = "parentFile")
    private List<FileMetadata> childrenFiles;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;

}

