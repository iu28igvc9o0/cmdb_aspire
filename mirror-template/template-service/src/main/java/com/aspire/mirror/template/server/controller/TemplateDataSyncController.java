package com.aspire.mirror.template.server.controller;

import com.aspire.mirror.template.api.dto.ActionsDetailResponse;
import com.aspire.mirror.template.api.dto.ItemsDetailResponse;
import com.aspire.mirror.template.api.dto.TemplateDataSyncResponse;
import com.aspire.mirror.template.api.dto.TemplateDetailResponse;
import com.aspire.mirror.template.api.dto.TriggersDetailResponse;
import com.aspire.mirror.template.api.dto.model.ActionsDTO;
import com.aspire.mirror.template.api.dto.model.ItemsDTO;
import com.aspire.mirror.template.api.dto.model.TemplateDTO;
import com.aspire.mirror.template.api.dto.model.TemplateDataSyncDTO;
import com.aspire.mirror.template.api.dto.model.TriggersDTO;
import com.aspire.mirror.template.api.dto.vo.StandardDynamicModel;
import com.aspire.mirror.template.api.service.TemplateDataSyncService;
import com.aspire.mirror.template.common.entity.TemplateDataSyncTypeConstant;
import com.aspire.mirror.template.server.biz.ActionsBiz;
import com.aspire.mirror.template.server.biz.ItemsBiz;
import com.aspire.mirror.template.server.biz.TemplateBiz;
import com.aspire.mirror.template.server.biz.TemplateDataSyncBiz;
import com.aspire.mirror.template.server.biz.TriggersBiz;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 模板数据同步控制层
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.template.server.controller
 * 类名称:    TemplateDataSyncController.java
 * 类描述:    模板数据同步控制层
 * 创建人:    JinSu
 * 创建时间:  2018/9/10 18:11
 * 版本:      v1.0
 */
@RestController
public class TemplateDataSyncController implements TemplateDataSyncService {
    @Override
    public TemplateDataSyncResponse<TemplateDetailResponse> syncMonitorTemplateList(@PathVariable("syncSeq") String
                                                                                            startSyncSeq, @PathVariable("proxyIdentity") String proxyIdentity) {
        TemplateDataSyncResponse<TemplateDetailResponse> response = new
                TemplateDataSyncResponse<TemplateDetailResponse>();
//        String[] templateIdArray = getDataList(response, startSyncSeq, TemplateDataSyncTypeConstant
// .SYNC_TYPE_TEMPLATE);
        Map<String, String> dataMap = getDataList(response, startSyncSeq, TemplateDataSyncTypeConstant
                .SYNC_TYPE_TEMPLATE);
        String[] templateIdArray = dataMap.keySet().toArray(new String[0]);
        if (templateIdArray != null) {
            List<TemplateDTO> templateDTOList = templateBiz.selectByPrimaryKeyArraysAndProxyIdentity(templateIdArray, proxyIdentity);
            List<TemplateDetailResponse> listTemplateDetailResponse = Lists.newArrayList();
            if (!CollectionUtils.isEmpty(templateDTOList)) {
                for (TemplateDTO templateDTO : templateDTOList) {
                    TemplateDetailResponse templateDetail = new TemplateDetailResponse();
                    BeanUtils.copyProperties(templateDTO, templateDetail);
                    templateDetail.setOperateType(dataMap.get(templateDetail.getTemplateId()));
                    listTemplateDetailResponse.add(templateDetail);
                    //清除创建和修改的数据
                    dataMap.remove(templateDetail.getTemplateId());
                }
            }
            // 加入删除数据同步
            for (String dataId : dataMap.keySet()) {
                TemplateDetailResponse templateDetail = new TemplateDetailResponse();
                templateDetail.setTemplateId(dataId);
                templateDetail.setOperateType(dataMap.get(dataId));
                listTemplateDetailResponse.add(templateDetail);
            }
            response.setItemDataList(listTemplateDetailResponse);
        }
        return response;
    }

    @Override
    public TemplateDataSyncResponse<StandardDynamicModel> syncMonitorTriggerDynamicModelList(@PathVariable("syncSeq") String startSyncSeq, @PathVariable("proxyIdentity") String proxyIdentity) {
        TemplateDataSyncResponse<StandardDynamicModel> response = new TemplateDataSyncResponse();
        Map<String, String> dataMap = getDataList(response, startSyncSeq, TemplateDataSyncTypeConstant.SYNC_TYPE_TRIGGER_MODEL);
        String[] modelIdArray = dataMap.keySet().toArray(new String[0]);
        if (modelIdArray.length > 0) {
            List<StandardDynamicModel> modelList = triggersBiz.selectDynamicModelByModelIdArrayAndProxyIdentity(modelIdArray, proxyIdentity);
            List<StandardDynamicModel> modelResponseList = Lists.newArrayList();
            if (!CollectionUtils.isEmpty(modelList)) {
                for (StandardDynamicModel standardDynamicModel : modelList) {
                    standardDynamicModel.setOperateType(dataMap.get(standardDynamicModel.getModelId()));
                    modelResponseList.add(standardDynamicModel);
                    //清除创建和修改的数据
                    dataMap.remove(standardDynamicModel.getModelId());
                }
            }
            // 加入删除数据同步
            for (String dataId : dataMap.keySet()) {
                StandardDynamicModel standardDynamicModel = new StandardDynamicModel();
                standardDynamicModel.setModelId(dataId);
                standardDynamicModel.setOperateType(dataMap.get(dataId));
                modelResponseList.add(standardDynamicModel);
            }
            response.setItemDataList(modelResponseList);
        }
        return response;
    }

    @Override
    public TemplateDataSyncResponse<ItemsDetailResponse> syncMonitorItemList(@PathVariable("syncSeq") String
                                                                                     startSyncSeq, @PathVariable("proxyIdentity") String proxyIdentity) {
        TemplateDataSyncResponse<ItemsDetailResponse> response = new TemplateDataSyncResponse<ItemsDetailResponse>();
        Map<String, String> dataMap = getDataList(response, startSyncSeq, TemplateDataSyncTypeConstant.SYNC_TYPE_ITEM);
        String[] itemIdArray = dataMap.keySet().toArray(new String[0]);
//        String[] itemIdArray = getDataList(response, startSyncSeq, TemplateDataSyncTypeConstant.SYNC_TYPE_ITEM);
        if (itemIdArray != null) {
            List<ItemsDTO> itemDTOList = itemsBiz.selectByPrimaryKeyArraysAndProxyIdentity(itemIdArray, proxyIdentity);
            List<ItemsDetailResponse> itemsDetailResponseList = Lists.newArrayList();
            if (!CollectionUtils.isEmpty(itemDTOList)) {
                for (ItemsDTO itemsDTO : itemDTOList) {
                    ItemsDetailResponse itemsDetailResponse = new ItemsDetailResponse();
                    BeanUtils.copyProperties(itemsDTO, itemsDetailResponse);
                    itemsDetailResponse.setOperateType(dataMap.get(itemsDetailResponse.getItemId()));

                    itemsDetailResponseList.add(itemsDetailResponse);
                    //清除创建和修改的数据
                    dataMap.remove(itemsDetailResponse.getItemId());
                }
            }
            // 加入删除数据同步
            for (String dataId : dataMap.keySet()) {
                ItemsDetailResponse itemsDetailResponse = new ItemsDetailResponse();
                itemsDetailResponse.setItemId(dataId);
                itemsDetailResponse.setOperateType(dataMap.get(dataId));
                itemsDetailResponseList.add(itemsDetailResponse);
            }
            response.setItemDataList(itemsDetailResponseList);
        }
        return response;
    }

    @Override
    public TemplateDataSyncResponse<TriggersDetailResponse> syncMonitorTriggerList(@PathVariable("syncSeq") String
                                                                                           startSyncSeq, @PathVariable("proxyIdentity") String proxyIdentity) {
        TemplateDataSyncResponse<TriggersDetailResponse> response = new
                TemplateDataSyncResponse<TriggersDetailResponse>();
        Map<String, String> dataMap = getDataList(response, startSyncSeq, TemplateDataSyncTypeConstant
                .SYNC_TYPE_TRIGGER);
        String[] triggerIdArray = dataMap.keySet().toArray(new String[0]);
        if (triggerIdArray != null) {
            List<TriggersDTO> triggersDTOList = triggersBiz.selectByPrimaryKeyArraysAndProxyIdentity(triggerIdArray, proxyIdentity);
            List<TriggersDetailResponse> listTrigger = Lists.newArrayList();
            if (!CollectionUtils.isEmpty(triggersDTOList)) {
                for (TriggersDTO triggersDTO : triggersDTOList) {
                    TriggersDetailResponse triggersDetailResponse = new TriggersDetailResponse();
                    BeanUtils.copyProperties(triggersDTO, triggersDetailResponse);
                    triggersDetailResponse.setOperateType(dataMap.get(triggersDetailResponse.getTriggerId()));
                    listTrigger.add(triggersDetailResponse);
                    //清除创建和修改的数据
                    dataMap.remove(triggersDetailResponse.getTriggerId());
                }
            }
            // 加入删除数据同步
            for (String dataId : dataMap.keySet()) {
                TriggersDetailResponse triggersDetailResponse = new TriggersDetailResponse();
                triggersDetailResponse.setTriggerId(dataId);
                triggersDetailResponse.setOperateType(dataMap.get(dataId));
                listTrigger.add(triggersDetailResponse);
            }
            response.setItemDataList(listTrigger);
        }
        return response;
    }

    @Override
    public TemplateDataSyncResponse<ActionsDetailResponse> syncMonitorActionList(@PathVariable("syncSeq") String
                                                                                         startSyncSeq, @PathVariable("proxyIdentity") String proxyIdentity) {
        TemplateDataSyncResponse<ActionsDetailResponse> response = new
                TemplateDataSyncResponse<ActionsDetailResponse>();
//        String[] actionIdArray = getDataList(response, startSyncSeq, TemplateDataSyncTypeConstant.SYNC_TYPE_ACTION,
//                operTypeMap);
        Map<String, String> dataMap = getDataList(response, startSyncSeq, TemplateDataSyncTypeConstant
                .SYNC_TYPE_ACTION);
        String[] actionIdArray = dataMap.keySet().toArray(new String[0]);
        if (actionIdArray != null) {
            List<ActionsDTO> actionsDTOList = actionsBiz.selectByPrimaryKeyArraysAndProxyIdentity(actionIdArray, proxyIdentity);
            List<ActionsDetailResponse> listAction = Lists.newArrayList();
            if (!CollectionUtils.isEmpty(actionsDTOList)) {
                for (ActionsDTO actionsDTO : actionsDTOList) {
                    ActionsDetailResponse actionsDetailResponse = new ActionsDetailResponse();
                    BeanUtils.copyProperties(actionsDTO, actionsDetailResponse);
                    actionsDetailResponse.setOperateType(dataMap.get(actionsDetailResponse.getActionId()));
                    listAction.add(actionsDetailResponse);
                    //清除创建和修改的数据
                    dataMap.remove(actionsDetailResponse.getActionId());
                }
            }
            // 加入删除数据同步
            for (String dataId : dataMap.keySet()) {
                ActionsDetailResponse actionsDetailResponse = new ActionsDetailResponse();
                actionsDetailResponse.setActionId(dataId);
                actionsDetailResponse.setOperateType(dataMap.get(dataId));
                listAction.add(actionsDetailResponse);
            }
            response.setItemDataList(listAction);
        }
        return response;
    }

    //    @Override
//    public ResponseEntity<String> createdTemplateDateSync(TemplateDataSyncCreateRequest createRequest) {
//        TemplateDataSyncDTO templateDataSyncCreateDTO = new TemplateDataSyncDTO();
//        BeanUtils.copyProperties(createRequest, templateDataSyncCreateDTO);
//        templateDataSyncBiz.insert(templateDataSyncCreateDTO);
//        return null;
//    }
    private Map<String, String> getDataList(TemplateDataSyncResponse response, String startSyncId, String syncType) {
        List<TemplateDataSyncDTO> list = templateDataSyncBiz.selectByStartSyncIdAndSyncType(Integer.parseInt
                (startSyncId), syncType);
        Map<String, String> dataMap = Maps.newHashMap();
        if (!CollectionUtils.isEmpty(list)) {
            for (TemplateDataSyncDTO templateDataSyncDTO : list) {
//                if (!dataIdList.contains(templateDataSyncDTO.getDataId())) {
//                    dataIdList.add(templateDataSyncDTO.getDataId());
//                }
                dataMap.put(templateDataSyncDTO.getDataId(), templateDataSyncDTO.getOperateType());
            }
            int syncId = list.get(list.size() - 1).getSyncId();
            response.setLastSyncSeq(syncId);
        }
        return dataMap;
    }

    @Autowired
    private TemplateDataSyncBiz templateDataSyncBiz;

    @Autowired
    private TemplateBiz templateBiz;

    @Autowired
    private ItemsBiz itemsBiz;

    @Autowired
    private TriggersBiz triggersBiz;

    @Autowired
    private ActionsBiz actionsBiz;
}
