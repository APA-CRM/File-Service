package com.crm.file.controller;

import com.crm.file.dto.request.CreateFileRequest;
import com.crm.file.dto.request.UpdateFileRequest;
import com.crm.file.dto.response.FileResponse;
import com.crm.file.dto.response.FileWithChildrenResponse;
import com.crm.file.facade.FileFacade;
import com.crm.sharedlib.core.enums.Action;
import com.crm.sharedlib.rbac.annotation.RequiresPermission;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static com.crm.sharedlib.core.enums.Resource.FILES;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileController {

    private final FileFacade facade;

    @GetMapping("{fileId}")
    @RequiresPermission(resource = FILES, action = Action.READ)
    public FileWithChildrenResponse getFile(@PathVariable("fileId") UUID fileId) {
        return facade.getFile(fileId);
    }

    @GetMapping("{fileId}/content")
    @RequiresPermission(resource = FILES, action = Action.READ)
    public ResponseEntity<Resource> getFileContent(@PathVariable("fileId") UUID fileId) {
        return facade.getFileContent(fileId);
    }

    @PostMapping
    @RequiresPermission(resource = FILES, action = Action.CREATE)
    public FileResponse createFile(@Valid @ModelAttribute CreateFileRequest request) {
        return facade.createFile(request);
    }

    @PatchMapping("{fileId}")
    @RequiresPermission(resource = FILES, action = Action.UPDATE)
    public FileWithChildrenResponse updateFile(
            @PathVariable("fileId") UUID fileId,
            @Valid @ModelAttribute UpdateFileRequest request
    ) {
        return facade.updateFile(fileId, request);
    }

    @DeleteMapping("{fileId}")
    @RequiresPermission(resource = FILES, action = Action.DELETE)
    public ResponseEntity<Void> deleteFile(
            @PathVariable("fileId") UUID fileId,
            @RequestParam(value = "forceDelete", defaultValue = "false") Boolean forceDelete
    ) {
        facade.deleteFile(fileId, forceDelete);

        return ResponseEntity.noContent().build();
    }

}
