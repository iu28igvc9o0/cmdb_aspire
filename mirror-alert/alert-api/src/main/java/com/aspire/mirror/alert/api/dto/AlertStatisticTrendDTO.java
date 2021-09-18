package com.aspire.mirror.alert.api.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AlertStatisticTrendDTO {

    private String level;

    private String section;

    private Integer alertNum;
}
