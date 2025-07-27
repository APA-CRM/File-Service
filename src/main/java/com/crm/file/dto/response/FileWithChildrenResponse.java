package com.crm.file.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FileWithChildrenResponse extends FileResponse {

    private List<FileResponse> childrenFiles;

}
