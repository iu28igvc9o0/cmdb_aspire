package com.aspire.mirror.composite.payload.mailAlert;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CompAlertMailActionResponse {
    private int status;
    private String message;
}
