package com.aspire.mirror.alert.api.dto.mailAlert;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class AlertMailActionResp {

    public static final int success = 1;
    public static final int failed = -1;

    private int status;
    private String message;
}
