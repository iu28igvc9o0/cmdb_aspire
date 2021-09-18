package com.aspire.mirror.alert.server.dao.notify.po;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class AlertSubscribeRulesDetailShow {
    private AlertSubscribeRulesDetail alertSubscribeRulesDetail;
    private List<Reciver> reciverList;
    private List<AlertSubscribeRulesManagement>AlertSubscribeRulesManagementList;

}
