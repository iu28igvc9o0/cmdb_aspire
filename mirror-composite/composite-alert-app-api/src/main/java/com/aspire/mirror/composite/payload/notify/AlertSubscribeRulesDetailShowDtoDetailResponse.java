package com.aspire.mirror.composite.payload.notify;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class AlertSubscribeRulesDetailShowDtoDetailResponse {
    private AlertSubscribeRulesDetailShowDtoDetail alertSubscribeRulesDetailShowDtoDetail;
    private List<Reciver> reciverList;
   private List<AlertSubscribeRulesManagementRespone> AlertSubscribeRulesManagementResponeList;

}
