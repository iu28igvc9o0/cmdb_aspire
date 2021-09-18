package com.aspire.mirror.inspection.server.biz.handler;

import com.aspire.mirror.inspection.api.dto.constant.TaskSyncOperate;
import com.aspire.mirror.inspection.api.dto.model.OpsApItem;
import com.aspire.mirror.inspection.api.dto.model.OpsApItemType;
import com.aspire.mirror.inspection.api.dto.model.TaskDTO;
import com.aspire.mirror.inspection.api.dto.model.TaskObjectDTO;
import com.aspire.mirror.inspection.server.clientservice.OpsServiceClient;
import com.aspire.mirror.inspection.server.clientservice.TemplateApiClient;
import com.aspire.mirror.inspection.server.clientservice.payload.ItemsDetailResponse;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * TODO
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.inspection.server.biz.handler
 * 类名称:    TaskSyncHandler.java
 * 类描述:    TODO
 * 创建人:    JinSu
 * 创建时间:  2020/3/20 14:14
 * 版本:      v1.0
 */
@Component
@Slf4j
public class TaskSyncHandler {
    @Autowired
    private OpsServiceClient opsServiceClient;
    @Autowired
    private TemplateApiClient templateApiClient;


    public void taskSyncDelete(String[] taskIdArrays) {
        try {
            List<OpsApItemType> opsApItemTypeList = Lists.newArrayList();
            for (String taskId : taskIdArrays) {
                OpsApItemType opsApItemType = new OpsApItemType();
                opsApItemType.setApItemType(taskId);
                opsApItemType.setApItemTypeName(null);
                opsApItemType.setSourceMark(TaskSyncOperate.XJ_MARK.getKey());
                opsApItemType.setManageType(TaskSyncOperate.D.getKey());
                opsApItemTypeList.add(opsApItemType);
            }
            opsServiceClient.syncApItemList(opsApItemTypeList);
        }catch (Exception e) {
            log.error("同步巡检任务"+ taskIdArrays +"删除失败！", e);
        }
    }

    public void taskSyncAll(TaskDTO taskDTO, String taskId) {
        try {
            OpsApItemType opsApItemType = new OpsApItemType();
            opsApItemType.setApItemType(taskId);
            opsApItemType.setApItemTypeName(taskDTO.getName());
            opsApItemType.setSourceMark(TaskSyncOperate.XJ_MARK.getKey());
            opsApItemType.setManageType(TaskSyncOperate.A.getKey());

            Set<String> templateIdSet = taskDTO.getObjectList().stream().map(TaskObjectDTO::getTemplateId).collect
                    (Collectors.toSet());
            List<ItemsDetailResponse> itemList = templateApiClient.listItemsByTemplateIds(Joiner.on(",").join
                    (templateIdSet));
            List<OpsApItem> apItemList = Lists.newArrayList();
            for (ItemsDetailResponse itemsDetailResponse : itemList) {
                OpsApItem opsApItem = new OpsApItem();
                opsApItem.setManageType(TaskSyncOperate.U.getKey());
                opsApItem.setApItem(itemsDetailResponse.getItemId());
                opsApItem.setApItemName(itemsDetailResponse.getName());
                opsApItem.setApItemValueType(itemsDetailResponse.getValueType().equals("LOG") ? 1 : 3);
                opsApItem.setExtendAttr("template_id", itemsDetailResponse.getTemplateId());
                opsApItem.setExtendAttr("template_name", itemsDetailResponse.getTemplateName());
                apItemList.add(opsApItem);
            }
            opsApItemType.setApItemList(apItemList);

            List<OpsApItemType> opsApItemTypeList = Lists.newArrayList();
            opsApItemTypeList.add(opsApItemType);
            opsServiceClient.syncApItemList(opsApItemTypeList);
        }catch (Exception e) {
            log.error("同步巡检任务（"+ taskId +"）全量数据失败！", e);
        }
    }
}
