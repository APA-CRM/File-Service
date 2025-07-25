package com.crm.file.controller;

import com.crm.file.dto.request.CreateFileRequest;
import com.crm.file.dto.request.UpdateFileRequest;
import com.crm.file.dto.response.FileResponse;
import com.crm.file.dto.response.FileWithChildrenResponse;
import com.crm.file.facade.FileFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileController {

    private final FileFacade facade;

    @GetMapping("{fileId}")
    public FileWithChildrenResponse getFile(@PathVariable("fileId") UUID fileId) {
        return facade.getFile(fileId);
    }

    @GetMapping(value = "{fileId}/content", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public Resource getFileContent(@PathVariable("fileId") UUID fileId) {
        return facade.getFileContent(fileId);
    }

    @PostMapping
    public FileResponse createFile(@ModelAttribute CreateFileRequest request) {
        return facade.createFile(request);
    }

    @PatchMapping("{fileId}")
    public FileWithChildrenResponse updateFile(
            @PathVariable("fileId") UUID fileId,
            @ModelAttribute UpdateFileRequest request
    ) {
        return facade.updateFile(fileId, request);
    }

    @DeleteMapping("{fileId}")
    public ResponseEntity<Void> deleteFile(
            @PathVariable("fileId") UUID fileId,
            @RequestParam(value = "forceDelete", defaultValue = "false") Boolean forceDelete
    ) {
        facade.deleteFile(fileId, forceDelete);

        return ResponseEntity.noContent().build();
    }

}
