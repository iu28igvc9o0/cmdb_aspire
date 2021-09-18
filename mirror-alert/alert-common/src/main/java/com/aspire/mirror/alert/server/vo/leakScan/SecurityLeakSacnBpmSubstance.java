package com.aspire.mirror.alert.server.vo.leakScan;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.File;

@NoArgsConstructor
@Data
public class SecurityLeakSacnBpmSubstance {
    private String token;
    private String scanId;
    private File attachFile;
}
