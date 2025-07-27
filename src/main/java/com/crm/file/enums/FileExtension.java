package com.crm.file.enums;

import lombok.Getter;

@Getter
public enum FileExtension {
    PDF("application/pdf", "pdf"),
    DOCX("application/vnd.openxmlformats-officedocument.wordprocessingml.document", "docx", "doc"),
    XLSX("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "xlsx"),
    JPEG("image/jpeg", "jpeg", "jpg"),
    PNG("image/png", "png"),
    ZIP("application/zip", "zip"),
    RAR("application/vnd.rar", "rar"),
    MP3("audio/mpeg", "mp3"),
    MP4("video/mp4", "mp4");

    private final String mimeType;

    private final String[] extensions;

    FileExtension(String mimeType, String... extensions) {
        this.mimeType = mimeType;
        this.extensions = extensions;
    }
}
