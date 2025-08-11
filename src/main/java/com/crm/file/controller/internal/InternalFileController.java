package com.crm.file.controller.internal;


import com.crm.file.facade.internal.InternalFileFacade;
import com.crm.sharedlib.dto.request.CreateDefaultFileRequest;
import com.crm.sharedlib.dto.response.FileIdResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/internal/files")
@RequiredArgsConstructor
public class InternalFileController {

    private final InternalFileFacade facade;

    @PostMapping("/default")
    public FileIdResponse createDefaultDirectory(
            @Valid @RequestBody CreateDefaultFileRequest request
    ) {
        return facade.createDirectory(request.getName());
    }

}
