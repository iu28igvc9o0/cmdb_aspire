package com.aspire.mirror.alert.server.dao.mailAlert.po;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AlertMailFilterStrategy {
    private Integer id;
    private String filterId;
    private Integer mailField;
    private String alertField;
    private String fieldMatchValue;
    private Integer useReg;
    private String fieldMatchReg;
    private String fieldMatchTarget;
}
