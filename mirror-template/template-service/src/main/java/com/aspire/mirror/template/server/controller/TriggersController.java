package com.aspire.mirror.template.server.controller;

import com.aspire.mirror.template.api.dto.*;
import com.aspire.mirror.template.api.dto.model.FunctionsDTO;
import com.aspire.mirror.template.api.dto.model.TriggersDTO;
import com.aspire.mirror.template.api.dto.vo.DynamicModelBatchCreateVO;
import com.aspire.mirror.template.api.dto.vo.TriggersVO;
import com.aspire.mirror.template.api.service.TriggersService;
import com.aspire.mirror.template.server.biz.TriggersBiz;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.TimeoutException;

/**
 * 触发器控制层实现类
 * <p>
 * 项目名称: mirror平台
 * 包:      com.aspire.mirror.template.server.controller
 * 类名称:   TriggersController.java
 * 类描述:   触发器业务逻辑层实现类
 * 创建人:   JinSu
 * 创建时间: 2018-07-27 12:14:48
 */
@RestController
@CacheConfig(cacheNames = "TriggersCache")
public class TriggersController implements TriggersService {

    /**
     * 创建触发器信息
     *
     * @param triggersCreateRequest 触发器创建请求对象
     * @return TriggersCreateResponse 触发器创建响应对象
     */
//    public TriggersCreateResponse createdTriggers(@RequestBody final TriggersCreateRequest triggersCreateRequest){
//        if(null == triggersCreateRequest){
//            LOGGER.error("created param triggersCreateRequest is null");
//            throw new RuntimeException("triggersCreateRequest is null");
//        }
//        TriggersDTO triggersDTO = new TriggersDTO();
//        BeanUtils.copyProperties(triggersCreateRequest, triggersDTO);
//        triggersBiz.insert(triggersDTO);
//        TriggersCreateResponse triggersCreateResponse = new TriggersCreateResponse();
//        BeanUtils.copyProperties(triggersDTO, triggersCreateResponse);
//        return triggersCreateResponse;
//    }
    /**
     * 根据主键删除单条触发器信息
     *
     * @param triggerId 主键
     * @@return Result 返回结果
     */
    public ResponseEntity<String> deleteByPrimaryKey(@PathVariable("trigger_id") final String triggerId){
        try {
            triggersBiz.deleteByPrimaryKey(triggerId);
            return  new ResponseEntity<String>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            LOGGER.error("deleteByTriggerId error:" + e.getMessage(), e);
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 根据主键删除多条触发器信息
     *
     * @param triggerIds 主键（以逗号分隔）
     * @@return Result 返回结果
     */
//    public ResponseEntity<String> deleteByPrimaryKeyArrays(@PathVariable("trigger_ids") final String triggerIds) {
//        try {
//            if (StringUtils.isEmpty(triggerIds)) {
//                throw new RuntimeException("triggerIds is null");
//            }
//            String[] triggerIdArrays = triggerIds.split(",");
//            triggersBiz.deleteByPrimaryKeyArrays(triggerIdArrays);
//            return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
//        } catch (Exception e) {
//            LOGGER.error("deleteByPrimaryKeyArrays error:" + e.getMessage(), e);
//            return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }

    /**
     * 根据主键修改触发器信息
     *
     * @param triggersUpdateRequest 触发器修改请求对象
     * @return TriggersUpdateResponse 触发器修改响应对象
     */
    public TriggersUpdateResponse modifyByPrimaryKey(@PathVariable("trigger_id") final String triggerId, @RequestBody
    final TriggersUpdateRequest triggersUpdateRequest) {
        TriggersDTO triggersdTO = new TriggersDTO();
        triggersdTO.setTriggerId(triggerId);
        BeanUtils.copyProperties(triggersUpdateRequest, triggersdTO);

        triggersBiz.updateByPrimaryKey(triggersdTO);
        TriggersDTO findTriggersDTO = triggersBiz.selectByPrimaryKey(triggerId);
        TriggersUpdateResponse triggersUpdateResponse = new TriggersUpdateResponse();
        BeanUtils.copyProperties(triggersUpdateResponse, findTriggersDTO);
        return triggersUpdateResponse;
    }

    /**
     * 批量新增触发器信息
     *
     * @param triggersBatchCreateRequest
     * @return
     */
    @Override
    public List<TriggersCreateResponse> batchCreatedTriggers(@RequestBody @Validated final TriggersBatchCreateRequest
                                                                         triggersBatchCreateRequest) {
        if (null == triggersBatchCreateRequest) {
            throw new RuntimeException("batchCreatedTriggers param is null");
        }
        if (CollectionUtils.isEmpty(triggersBatchCreateRequest.getTriggerList())) {
            throw new RuntimeException("batchCreatedTriggers triggerList is empty");
        }
        List<TriggersDTO> listTriggerDTO = Lists.newArrayList();
        for (TriggersCreateRequest request : triggersBatchCreateRequest.getTriggerList()) {
            List<FunctionsCreateRequest> functionList = request.getFunctionList();
            TriggersDTO triggerDTO = new TriggersDTO();
            BeanUtils.copyProperties(request, triggerDTO);
            if (!CollectionUtils.isEmpty(functionList)) {
                List<FunctionsDTO> functionsDTOList = Lists.newArrayList();
                for (FunctionsCreateRequest functionReq : functionList) {
                    FunctionsDTO functionsDTO = new FunctionsDTO();
                    BeanUtils.copyProperties(functionReq, functionsDTO);
                    functionsDTOList.add(functionsDTO);
                }
                triggerDTO.setFunctionList(functionsDTOList);
            }
            listTriggerDTO.add(triggerDTO);
        }
        List<TriggersDTO> resp = triggersBiz.insertByBatch(listTriggerDTO);
        List<TriggersCreateResponse> createResponseList = Lists.newArrayList();
        for (TriggersDTO triggersDTO : resp) {
            TriggersCreateResponse createResponse = new TriggersCreateResponse();
            BeanUtils.copyProperties(triggersDTO, createResponse);
            createResponseList.add(createResponse);
        }
        return createResponseList;
    }

    @Override
    public ResponseEntity<String> deleteByItemIdArrays(@PathVariable("item_ids") String itemIds) {
        if (StringUtils.isEmpty(itemIds)) {
            throw new RuntimeException("deleteByItemIdArrays param[itemIds] is null");
        }
        try {
            String[] itemIdArrays = itemIds.split(",");
            triggersBiz.deleteByItemIdArrays(itemIdArrays);
            return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            LOGGER.error("deleteByPrimaryKeyArrays error:" + e.getMessage(), e);
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 根据主键查找触发器详情信息
     *
     * @param triggerId 触发器主键
     * @return TriggersVO 触发器详情响应对象
     */
    public TriggersDetailResponse findByPrimaryKey(@PathVariable("trigger_id") final String triggerId) {
        if (StringUtils.isEmpty(triggerId)) {
            LOGGER.warn("findByPrimaryKey param triggerId is null");
            return null;
        }
        TriggersDTO triggersDTO = triggersBiz.selectByPrimaryKey(triggerId);
        TriggersDetailResponse triggersVO = new TriggersDetailResponse();
        if (null != triggersDTO) {
            BeanUtils.copyProperties(triggersDTO, triggersVO);
        }

        return triggersVO;
    }

    /**
     * 根据监控项ID集合查找触发器
     *
     * @param itemIds
     * @return
     */
    @Override
    public List<TriggersDetailResponse> listByItemIdArrays(@PathVariable("item_ids") String itemIds) {
        if (StringUtils.isEmpty(itemIds)) {
            LOGGER.warn("listByItemIdArrays param itemIds is null");
            return null;
        }
        String[] itemIdArrays = itemIds.split(",");
        List<TriggersDTO> listTriggersDTO = triggersBiz.listByItemIdArrays(itemIdArrays);
        return dtoToVo(listTriggersDTO);
    }

    private List<TriggersDetailResponse> dtoToVo(List<TriggersDTO> listTriggersDTO) {
        List<TriggersDetailResponse> listTriggersVO = Lists.newArrayList();
        if (!CollectionUtils.isEmpty(listTriggersDTO)) {
            for (TriggersDTO triggersDTO : listTriggersDTO) {
                TriggersDetailResponse triggersVO = new TriggersDetailResponse();
                BeanUtils.copyProperties(triggersDTO, triggersVO);
                listTriggersVO.add(triggersVO);
            }
        }
        return listTriggersVO;
    }

    @Override
    public List<TriggersDetailResponse> listByTemplateId(@PathVariable("template_id") String templateId) {
        if (StringUtils.isEmpty(templateId)) {
            LOGGER.warn("listByTemplateId param templateId is empty");
        }
        List<TriggersDTO> listTriggersDTO = triggersBiz.listByTemplateId(templateId);
        return dtoToVo(listTriggersDTO);
    }

    @Override
    public void updateExpressionByItemId(@RequestBody TriggersUpdateRequest triggersUpdateRequest) {
        TriggersDTO triggersDTO = new TriggersDTO();
        BeanUtils.copyProperties(triggersUpdateRequest, triggersDTO);
        triggersBiz.updateExpressionByItemIdionByItemId(triggersDTO);
    }

    @Override
    public GeneralResponse batchDynamicModelData(@RequestBody DynamicModelBatchCreateVO dynamicModelBatchCreateVO) {
        if (dynamicModelBatchCreateVO == null || CollectionUtils.isEmpty(dynamicModelBatchCreateVO.getStandardDynamicModelExtList())) {
            LOGGER.error("batchDynamicModelData param dynamicModelBatchCreateVO is invalid");
            throw new RuntimeException("batchDynamicModelData param dynamicModelBatchCreateVO is invalid");
        }
        return triggersBiz.batchDynamicModelData(dynamicModelBatchCreateVO.getStandardDynamicModelExtList(), dynamicModelBatchCreateVO.getIndexType());
    }

    /**
     * 根据主键查询触发器集合信息
     *
     * @param triggerIds 触发器主键
     * @return TriggersVO 触发器查询响应对象
     */
    public List<TriggersVO> listByPrimaryKeyArrays(@PathVariable("trigger_id") final String triggerIds) {
        if (StringUtils.isEmpty(triggerIds)) {
            LOGGER.error("listByPrimaryKeyArrays param triggerIds is null");
            return Lists.newArrayList();
        }
        String[] triggerIdArrays = triggerIds.split(",");
        List<TriggersDTO> listTriggersDTO = triggersBiz.selectByPrimaryKeyArrays(triggerIdArrays);
        List<TriggersVO> listTriggersVO = Lists.newArrayList();
        if (!CollectionUtils.isEmpty(listTriggersDTO)) {
            for (TriggersDTO triggersDTO : listTriggersDTO) {
                TriggersVO triggersVO = new TriggersVO();
                BeanUtils.copyProperties(triggersDTO, triggersVO);
                listTriggersVO.add(triggersVO);
            }
        }
        return listTriggersVO;
    }


    @Override
    public GeneralResponse zabbixItemAndPrototypeRelationSync(@RequestBody ItemSyncRequest itemSyncRequest) {
        if (itemSyncRequest == null) {
            LOGGER.error("TemplateController[zabbixDiscoveryItemSync] param is null");
            return new GeneralResponse();
        }
        return triggersBiz.zabbixItemAndPrototypeRelationSync(itemSyncRequest);
    }
    /**
     * slf4j
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(TriggersController.class);

    @Autowired
    private TriggersBiz triggersBiz;

}