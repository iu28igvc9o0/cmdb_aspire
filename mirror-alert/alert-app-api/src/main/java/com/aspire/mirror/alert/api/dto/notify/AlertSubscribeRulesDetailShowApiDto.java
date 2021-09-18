package com.aspire.mirror.alert.api.dto.notify;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
public class AlertSubscribeRulesDetailShowApiDto {
    private AlertSubscribeRulesDetailDto alertSubscribeRulesDetail;
    private List<ReciverDto> reciverList;
    private List<AlertSubscribeRulesManagementDto>AlertSubscribeRulesManagementList;
}
