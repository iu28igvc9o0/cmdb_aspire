package com.aspire.mirror.alert.server.vo.leakScan;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.File;
import java.util.List;

@NoArgsConstructor
@Data
public class SecurityLeakScanUploadFileSubstance {
    private String zipFileName;
    private String bizLine;
    private String dateStr;
    private String dateStrToClear;
    private List<File> fileList;
}
