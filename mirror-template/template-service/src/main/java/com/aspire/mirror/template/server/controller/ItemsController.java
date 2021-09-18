package com.aspire.mirror.template.server.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.aspire.mirror.common.util.FieldUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aspire.mirror.common.entity.PageRequest;
import com.aspire.mirror.common.entity.PageResponse;
import com.aspire.mirror.template.api.dto.ItemsBatchCreateRequest;
import com.aspire.mirror.template.api.dto.ItemsCreateRequest;
import com.aspire.mirror.template.api.dto.ItemsCreateResponse;
import com.aspire.mirror.template.api.dto.ItemsDetailResponse;
import com.aspire.mirror.template.api.dto.ItemsPageRequest;
import com.aspire.mirror.template.api.dto.ItemsUpdateRequest;
import com.aspire.mirror.template.api.dto.ItemsUpdateResponse;
import com.aspire.mirror.template.api.dto.model.ItemsDTO;
import com.aspire.mirror.template.api.service.ItemsService;
import com.aspire.mirror.template.server.biz.ItemsBiz;
import com.google.common.collect.Lists;

/**
 * 监控项控制层实现类
 * <p>
 * 项目名称: mirror平台
 * 包:      com.aspire.mirror.template.server.controller
 * 类名称:   ItemsController.java
 * 类描述:   监控项业务逻辑层实现类
 * 创建人:   JinSu
 * 创建时间: 2018-07-27 12:14:47
 */
@RestController
@CacheConfig(cacheNames = "ItemsCache")
public class ItemsController implements ItemsService {

    /**
     * 创建监控项信息
     *
     * @param itemsCreateRequest 监控项创建请求对象
     * @return ItemsCreateResponse 监控项创建响应对象
     */
    public ItemsCreateResponse createdItems(@RequestBody final ItemsCreateRequest itemsCreateRequest) {
        if (null == itemsCreateRequest) {
            LOGGER.error("created param itemsCreateRequest is null");
            throw new RuntimeException("itemsCreateRequest is null");
        }
        ItemsDTO itemsDTO = new ItemsDTO();
        BeanUtils.copyProperties(itemsCreateRequest, itemsDTO);
        String itemId = itemsBiz.insert(itemsDTO);
        ItemsCreateResponse itemsCreateResponse = new ItemsCreateResponse();
        BeanUtils.copyProperties(itemsDTO, itemsCreateResponse);
        itemsCreateResponse.setItemId(itemId);
        return itemsCreateResponse;
    }

    /**
     * 批量新增监控项
     *
     * @param request 监控项创建请求对象
     * @return
     */
    @Override
    public List<ItemsCreateResponse> batchCreateItems(@RequestBody @Validated ItemsBatchCreateRequest request) {
        if (null == request) {
            LOGGER.error("created param batchCreateItems is null");
            throw new RuntimeException("batchCreateItems is null");
        }
        if (CollectionUtils.isEmpty(request.getListItem())) {
            LOGGER.error("created param object batchCreateItems is empty");
            throw new RuntimeException("batchCreateItems param is empty");
        }
        List<ItemsDTO> itemsDTOList = Lists.newArrayList();

        for (ItemsCreateRequest createRequest : request.getListItem()) {
            ItemsDTO itemsDTO = new ItemsDTO();
            BeanUtils.copyProperties(createRequest, itemsDTO);
            itemsDTOList.add(itemsDTO);
        }

        List<ItemsDTO> respListDTO = itemsBiz.insertByBatch(itemsDTOList);
        List<ItemsCreateResponse> resp = Lists.newArrayList();
        for (ItemsDTO itemsDTO : respListDTO) {
            ItemsCreateResponse createResponse = new ItemsCreateResponse();
            BeanUtils.copyProperties(itemsDTO, createResponse);
            resp.add(createResponse);
        }
        return resp;
    }
//
//    /**
//     * 根据主键删除单条监控项信息
//     *
//     * @param itemId 主键
//     * @@return Result 返回结果
//     */
//    public ResponseEntity<String> deleteByPrimaryKey(@PathVariable("item_id") final String itemId){
//        try {
//            itemsBiz.deleteByPrimaryKey(itemId);
//            return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
//        } catch (Exception e) {
//            LOGGER.error("deleteByPrimaryKey error:" + e.getMessage(), e);
//            return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }

    /**
     * 根据主键删除多条监控项信息
     *
     * @param itemIds 主键（以逗号分隔）
     * @@return Result 返回结果
     */
    public ResponseEntity<String> deleteByPrimaryKeyArrays(@PathVariable("item_ids") final String itemIds) {
        try {
            if (StringUtils.isEmpty(itemIds)) {
                throw new RuntimeException("itemIds is null");
            }
            String[] itemIdArrays = itemIds.split(",");
            itemsBiz.deleteByPrimaryKeyArrays(itemIdArrays);
            return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            LOGGER.error("deleteByPrimaryKeyArrays error:" + e.getMessage(), e);
            return new ResponseEntity<String>("删除错误！", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 根据主键修改监控项信息
     *
     * @param itemsUpdateRequest 监控项修改请求对象
     * @return ItemsUpdateResponse 监控项修改响应对象
     */
    public ItemsUpdateResponse modifyByPrimaryKey(@PathVariable("item_id") final String itemId, @RequestBody final
    ItemsUpdateRequest itemsUpdateRequest) {
        ItemsDTO itemsdTO = new ItemsDTO();
        itemsdTO.setItemId(itemId);
        BeanUtils.copyProperties(itemsUpdateRequest, itemsdTO);

        itemsBiz.updateByPrimaryKey(itemsdTO);
        ItemsDTO findItemsDTO = itemsBiz.selectByPrimaryKey(itemId);
        ItemsUpdateResponse itemsUpdateResponse = new ItemsUpdateResponse();
        BeanUtils.copyProperties(findItemsDTO, itemsUpdateResponse);
        return itemsUpdateResponse;
    }

    /**
     * 根据主键查找监控项详情信息
     *
     * @param itemId 监控项主键
     * @return ItemsVO 监控项详情响应对象
     */
    public ItemsDetailResponse findByPrimaryKey(@PathVariable("item_id") final String itemId) {
        if (StringUtils.isEmpty(itemId)) {
            LOGGER.warn("findByPrimaryKey param itemId is null");
            return null;
        }
        ItemsDTO itemsDTO = itemsBiz.selectByPrimaryKey(itemId);
        ItemsDetailResponse itemsVO = new ItemsDetailResponse();
        if (null != itemsDTO) {
            BeanUtils.copyProperties(itemsDTO, itemsVO);
        }
        return itemsVO;
    }

    /**
     * 分页查询
     *
     * @param request 分页查询监控对象
     * @return PageResponse 分页查询对象
     */
    @Override
    public PageResponse<ItemsDetailResponse> pageList(@RequestBody final ItemsPageRequest request) {
        if (null == request) {
            LOGGER.warn("pageList param templatePageRequest is null");
            return null;
        }
        PageRequest pageRequest = new PageRequest();
        pageRequest.setPageSize(request.getPageSize());
        pageRequest.setPageNo(request.getPageNo());
//        pageRequest.addFields("name", request.getName());
//        pageRequest.addFields("templateName", request.getTemplateName());
//        pageRequest.addFields("key", request.getKey());
//        pageRequest.addFields("funType", request.getFunType());
//        pageRequest.addFields("templateId", request.getTemplateId());
        Map<String, Object> map = FieldUtil.getFiledMap(request);
        for (String key : map.keySet()) {
            pageRequest.addFields(key, map.get(key));
        }
        PageResponse<ItemsDTO> itemsDTOPageResponse = itemsBiz.pageList(pageRequest);
        List<ItemsDetailResponse> listItemResp = Lists.newArrayList();
        toVo(itemsDTOPageResponse.getResult(), listItemResp);
        PageResponse<ItemsDetailResponse> result = new PageResponse<ItemsDetailResponse>();
        result.setCount(itemsDTOPageResponse.getCount());
        result.setResult(listItemResp);
        return result;
    }

    /**
     * 根据主键查询monitor_items集合信息
     *
     * @param itemIds monitor_items主键
     * @return ItemsVO monitor_items查询响应对象
     */
    public List<ItemsDetailResponse> listByPrimaryKeyArrays(@RequestParam("item_ids") final String itemIds) {
        if (StringUtils.isEmpty(itemIds)) {
            LOGGER.error("listByPrimaryKeyArrays param itemIds is null");
            return Lists.newArrayList();
        }
        String[] itemIdArrays = itemIds.split(",");
        List<ItemsDTO> listItemsDTO = itemsBiz.selectByPrimaryKeyArrays(itemIdArrays);
        List<ItemsDetailResponse> listItemsVO = Lists.newArrayList();
        toVo(listItemsDTO, listItemsVO);
        return listItemsVO;
    }

    private void toVo(List<ItemsDTO> listItemsDTO, List<ItemsDetailResponse> listItemsVO) {
        if (!CollectionUtils.isEmpty(listItemsDTO)) {
            for (ItemsDTO itemsDTO : listItemsDTO) {
                ItemsDetailResponse itemsVO = new ItemsDetailResponse();
                BeanUtils.copyProperties(itemsDTO, itemsVO);
                listItemsVO.add(itemsVO);
            }
        }
    }

    @Override
    public List<ItemsDetailResponse> listItemsByTemplateIds(@PathVariable("joinedTempIds") String joinedTempIds) {
        List<ItemsDetailResponse> resultList = new ArrayList<ItemsDetailResponse>();
        if (StringUtils.isEmpty(joinedTempIds)) {
            LOGGER.error("listItemsByTemplateIds param joinedTempIds is null");
            return resultList;
        }

        String[] templateIdArr = StringUtils.split(joinedTempIds, ",");
        for (String tempId : templateIdArr) {
            if (StringUtils.isBlank(tempId)) {
                continue;
            }

            ItemsDTO param = new ItemsDTO();
            param.setTemplateId(tempId);
            List<ItemsDTO> listItemsDTO = itemsBiz.select(param);
            if (CollectionUtils.isEmpty(listItemsDTO)) {
                continue;
            }

            for (ItemsDTO itemsDTO : listItemsDTO) {
                ItemsDetailResponse itemsVO = new ItemsDetailResponse();
                BeanUtils.copyProperties(itemsDTO, itemsVO);
                if(itemsDTO.getItemExt() != null) {
                    itemsVO.setCustomizeParam(itemsDTO.getItemExt().getCustomizeParam());
                    itemsVO.setScriptParam(itemsDTO.getItemExt().getScriptParam());
                }
                resultList.add(itemsVO);
            }
        }
        return resultList;
    }

    /**
     * 根据监控项ID获取最新监控值
     *
     * @param itemId  监控项ID
     * @param sysCode
     * @return
     */
    @Override
    public String findLastUpValueByItemId(@PathVariable("item_id") final String itemId, @RequestParam(value =
            "sys_code", required = false) final String sysCode) {
        if (StringUtils.isEmpty(itemId)) {
            return null;
        }
        return itemsBiz.findLastUpValueByItemId(itemId, sysCode);
    }

    /**
     * 根据监控项ID获取主题计算结果
     * @param itemId 监控项ID
     * @param startTime 计算开始时间
     * @param endTime 计算结束时间
     * @return List<Map<String, Object>> 计算主题指标结果
     */
    @Override
    public List<Map<String, Object>> getThemeCalcResult(@PathVariable("item_id") String itemId, @RequestParam(value = "start_time", required = false) String startTime, @RequestParam(value="end_time", required = false) String endTime) {
        if (StringUtils.isEmpty(itemId)) {
            return null;
        }
        return itemsBiz.getThemeCalcResult(itemId, startTime, endTime);
    }

    /**
     * 根据主题ID获取监控项列表
     *
     * @param themeId 主题ID
     * @return List<ItemsDetailResponse> 监控项列表
     */
    @Override
    public List<ItemsDetailResponse> selectByThemeId(@PathVariable("theme_id") String themeId) {
        if (StringUtils.isEmpty(themeId)) {
            return null;
        }
        ItemsDTO itemsDTO = new ItemsDTO();
        itemsDTO.setBizThemeId(themeId);
        List<ItemsDTO> itemsDTOList = itemsBiz.select(itemsDTO);
        List<ItemsDetailResponse> listItemResp = Lists.newArrayList();
        toVo(itemsDTOList, listItemResp);
        return listItemResp;
    }

    /**
     * slf4j
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ItemsController.class);

    @Autowired
    private ItemsBiz itemsBiz;

}