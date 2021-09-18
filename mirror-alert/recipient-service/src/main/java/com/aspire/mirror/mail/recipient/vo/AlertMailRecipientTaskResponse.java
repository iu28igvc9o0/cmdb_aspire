package com.aspire.mirror.mail.recipient.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AlertMailRecipientTaskResponse {
    private int code;
    private String message;

    public static AlertMailRecipientTaskResponse success() {
        AlertMailRecipientTaskResponse response = new AlertMailRecipientTaskResponse();
        response.setCode(1);
        return response;
    }

    public static AlertMailRecipientTaskResponse failed(String message) {
        AlertMailRecipientTaskResponse response = new AlertMailRecipientTaskResponse();
        response.setCode(-1);
        response.setMessage(message);
        return response;
    }
}
