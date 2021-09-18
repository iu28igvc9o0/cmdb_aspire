package com.aspire.mirror.template.server.controller;

import com.aspire.mirror.common.entity.PageRequest;
import com.aspire.mirror.common.entity.PageResponse;
import com.aspire.mirror.common.util.FieldUtil;
import com.aspire.mirror.template.api.dto.*;
import com.aspire.mirror.template.api.dto.model.ItemsDTO;
import com.aspire.mirror.template.api.dto.model.TemplateDTO;
import com.aspire.mirror.template.api.dto.vo.ZabbixTemplateSyncVo;
import com.aspire.mirror.template.api.service.TemplateService;
import com.aspire.mirror.template.server.biz.TemplateBiz;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
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
import java.util.Map;

/**
 * 模板控制层实现类
 * <p>
 * 项目名称: mirror平台
 * 包:      com.aspire.mirror.template.server.controller
 * 类名称:   TemplateController.java
 * 类描述:   模板业务逻辑层实现类
 * 创建人:   JinSu
 * 创建时间: 2018-07-27 12:14:48
 */
@RestController
@CacheConfig(cacheNames = "TemplateCache")
public class TemplateController implements TemplateService {
    /**
     * 创建模板信息
     *
     * @param templateCreateRequest 模板创建请求对象
     * @return TemplateCreateResponse 模板创建响应对象
     */
    public TemplateCreateResponse createdTemplate(@RequestBody @Validated final TemplateCreateRequest
                                                          templateCreateRequest) {
        if (null == templateCreateRequest) {
            LOGGER.error("created param templateCreateRequest is null");
            throw new RuntimeException("templateCreateRequest is null");
        }
        TemplateDTO templateDTO = new TemplateDTO();
        BeanUtils.copyProperties(templateCreateRequest, templateDTO);
        String templateId = templateBiz.insert(templateDTO);
        TemplateCreateResponse templateCreateResponse = new TemplateCreateResponse();
        BeanUtils.copyProperties(templateDTO, templateCreateResponse);
        templateCreateResponse.setTemplateId(templateId);
        return templateCreateResponse;
    }

    /**
     * 根据主键删除多条模板信息
     *
     * @param templateIds 主键（以逗号分隔）
     * @@return Result 返回结果
     */
    public ResponseEntity<String> deleteByPrimaryKeyArrays(@PathVariable("template_ids") final String templateIds) {
        try {
            if (StringUtils.isEmpty(templateIds)) {
                throw new RuntimeException("templateIds is null");
            }
            String[] templateIdArrays = templateIds.split(",");
            templateBiz.deleteByPrimaryKeyArrays(templateIdArrays);
            return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            LOGGER.error("deleteByPrimaryKeyArrays error:" + e.getMessage(), e);
            return new ResponseEntity<String>("删除错误！", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 根据主键修改模板信息
     *
     * @param templateUpdateRequest 模板修改请求对象
     * @return TemplateUpdateResponse 模板修改响应对象
     */
    public TemplateUpdateResponse modifyByPrimaryKey(@PathVariable("template_id") final String templateId,
                                                     @RequestBody @Validated final TemplateUpdateRequest
                                                             templateUpdateRequest) {
        TemplateDTO templatedTO = new TemplateDTO();
        templatedTO.setTemplateId(templateId);
        BeanUtils.copyProperties(templateUpdateRequest, templatedTO);

        templateBiz.updateByPrimaryKey(templatedTO);
        TemplateDTO findTemplateDTO = templateBiz.selectByPrimaryKey(templateId);
        TemplateUpdateResponse templateUpdateResponse = new TemplateUpdateResponse();
        BeanUtils.copyProperties(findTemplateDTO, templateUpdateResponse);
        return templateUpdateResponse;
    }

    /**
     * 根据主键查找模板详情信息
     *
     * @param templateId 模板主键
     * @return TemplateVO 模板详情响应对象
     */
    public TemplateDetailResponse findByPrimaryKey(@PathVariable("template_id") final String templateId) {
        if (StringUtils.isEmpty(templateId)) {
            LOGGER.warn("findByPrimaryKey param templateId is null");
            return null;
        }
        TemplateDTO templateDTO = templateBiz.selectByPrimaryKey(templateId);
        TemplateDetailResponse templateVO = new TemplateDetailResponse();
        if (null != templateDTO) {
            BeanUtils.copyProperties(templateDTO, templateVO);
            transferItem(templateDTO, templateVO);
        }
        Gson gson = new Gson();
        LOGGER.info(gson.toJson(templateVO));
        return templateVO;
    }

    @Override
    public TemplateDetailResponse findByName(@PathVariable("template_name") String templateName) {
        if (StringUtils.isEmpty(templateName)) {
            LOGGER.warn("findByPrimaryKey param templateName is null");
            return null;
        }
        TemplateDTO templateDTO = templateBiz.selectByTemplateName(templateName);
        TemplateDetailResponse templateVO = new TemplateDetailResponse();
        if (null != templateDTO) {
            BeanUtils.copyProperties(templateDTO, templateVO);
            transferItem(templateDTO, templateVO);
        }
        Gson gson = new Gson();
        LOGGER.info(gson.toJson(templateVO));
        return templateVO;
    }

    /**
     * 查询模板列表
     *
     * @param templatePageRequest page参数
     * @return TemplateVO 模板详情响应对象
     */
    public PageResponse<TemplateDetailResponse> pageList(@RequestBody @Validated TemplatePageRequest
                                                                 templatePageRequest) {
        if (templatePageRequest == null) {
            LOGGER.warn("pageList param templatePageRequest is null");
            return null;
        }
        PageRequest page = new PageRequest();
        page.setPageNo(templatePageRequest.getPageNo());
        page.setPageSize(templatePageRequest.getPageSize());
//        page.addFields("name", templatePageRequest.getName());
//        page.addFields("type", templatePageRequest.getType());
//        page.addFields("funType", templatePageRequest.getFunType());
//        page.addFields("startTime", templatePageRequest.getCreateTimeStart());
//        page.addFields("endTime", templatePageRequest.getCreateTimeEnd());
        Map<String, Object> map = FieldUtil.getFiledMap(templatePageRequest);
        for (String key : map.keySet()) {
            page.addFields(key, map.get(key));
        }
        PageResponse<TemplateDTO> pageResult = templateBiz.pageList(page);
        List<TemplateDetailResponse> listTemplateVO = Lists.newArrayList();
        if (!CollectionUtils.isEmpty(pageResult.getResult())) {
            for (TemplateDTO templateDTO : pageResult.getResult()) {
                TemplateDetailResponse templateVO = new TemplateDetailResponse();
                BeanUtils.copyProperties(templateDTO, templateVO);
                transferItem(templateDTO, templateVO);
                listTemplateVO.add(templateVO);
            }
        }
        PageResponse<TemplateDetailResponse> result = new PageResponse<TemplateDetailResponse>();
        result.setCount(pageResult.getCount());
        result.setResult(listTemplateVO);
        return result;
    }

    private void transferItem(TemplateDTO templateDTO, TemplateDetailResponse templateVO) {
        List<ItemsDTO> listItemsDTO = templateDTO.getItemList();
        if (!CollectionUtils.isEmpty(listItemsDTO)) {
            List<ItemsDetailResponse> listItemsVO = Lists.newArrayList();
            for (ItemsDTO itemsDTO : listItemsDTO) {
                ItemsDetailResponse itemsVO = new ItemsDetailResponse();
                BeanUtils.copyProperties(itemsDTO, itemsVO);
                listItemsVO.add(itemsVO);
            }
            templateVO.setItemList(listItemsVO);
        } else {
            templateVO.setItemList(Lists.<ItemsDetailResponse>newArrayList());
        }
    }

    /**
     * 根据主键查询模板集合信息
     *
     * @param templateIds 模板主键
     * @return TemplateVO 模板查询响应对象
     */
    public List<TemplateDetailResponse> listByPrimaryKeyArrays(@PathVariable("template_ids") final String templateIds) {
        if (StringUtils.isEmpty(templateIds)) {
            LOGGER.error("listByPrimaryKeyArrays param templateIds is null");
            return Lists.newArrayList();
        }
        String[] templateIdArrays = templateIds.split(",");
        List<TemplateDTO> listTemplateDTO = templateBiz.selectByPrimaryKeyArrays(templateIdArrays);
        List<TemplateDetailResponse> listTemplateVO = Lists.newArrayList();
        if (!CollectionUtils.isEmpty(listTemplateDTO)) {
            for (TemplateDTO templateDTO : listTemplateDTO) {
                TemplateDetailResponse templateVO = new TemplateDetailResponse();
                BeanUtils.copyProperties(templateDTO, templateVO);
                listTemplateVO.add(templateVO);
            }
        }
        return listTemplateVO;
    }

    /**
     * 模板复制
     *
     * @param templateId 模板ID
     * @return TemplateCreateResponse 模板创建返回
     */
    @Override
    public TemplateCreateResponse copy(@PathVariable("template_id") String templateId) {
        if (StringUtils.isEmpty(templateId)) {
            LOGGER.error("copy template param templateId is empty");
            throw new RuntimeException("copy template param templateId is empty");
        }
        TemplateDTO templateDTO = templateBiz.copy(templateId);
        TemplateCreateResponse templateCreateResponse = new TemplateCreateResponse();
        BeanUtils.copyProperties(templateDTO, templateCreateResponse);
        return templateCreateResponse;
    }

    @Override
    public GeneralResponse zabbixTemplateSync(@RequestBody ZabbixTemplateSyncVo templateSyncVo) {
        if (templateSyncVo == null) {
            LOGGER.error("TemplateController[zabbixTemplateSync] param is null");
            throw new RuntimeException("TemplateController[zabbixTemplateSync] param is null");
        }
        return templateBiz.zabbixTemplateSync(templateSyncVo);
    }


    /**
     * slf4j
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(TemplateController.class);

    @Autowired
    private TemplateBiz templateBiz;
}