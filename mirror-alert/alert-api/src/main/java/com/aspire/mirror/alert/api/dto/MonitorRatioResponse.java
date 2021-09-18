package com.aspire.mirror.alert.api.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MonitorRatioResponse {

    private String tenant;

    private String cpu;

    private String men;

    private String storage;

}
