package com.aspire.mirror.alert.server.vo.leakScan;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class ProcessLoadRequestVo {

    private String account;

    private String actDefId;

    private String flowKey;

    private String subject;

    private String businessKey;

    private String data;

    private String vars;
}
