package com.crm.file.controller;

import com.crm.file.dto.request.CreateFileRequest;
import com.crm.file.dto.request.UpdateFileRequest;
import com.crm.file.dto.response.FileResponse;
import com.crm.file.facade.FileFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileController {

    private final FileFacade facade;

    @GetMapping("{fileId}")
    public FileResponse getFile(@PathVariable("fileId") UUID fileId) {
        return facade.getFile(fileId);
    }

    @PostMapping
    public FileResponse createFile(@RequestBody CreateFileRequest request) {
        return facade.createFile(request);
    }

    @PatchMapping("{fileId}")
    public FileResponse updateFile(
            @PathVariable("fileId") UUID fileId,
            @RequestBody UpdateFileRequest request
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
