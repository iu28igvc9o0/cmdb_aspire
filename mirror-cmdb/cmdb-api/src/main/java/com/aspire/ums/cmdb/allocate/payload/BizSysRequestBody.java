package com.aspire.ums.cmdb.allocate.payload;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BizSysRequestBody {
    private String ip;
    private String bizSystem;
}
