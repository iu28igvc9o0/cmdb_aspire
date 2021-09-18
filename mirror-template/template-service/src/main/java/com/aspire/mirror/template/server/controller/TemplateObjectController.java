package com.aspire.mirror.template.server.controller;

import com.aspire.mirror.template.api.dto.TemplateObjectBatchCreateRequest;
import com.aspire.mirror.template.api.dto.TemplateObjectCreateRequest;
import com.aspire.mirror.template.api.dto.TemplateObjectCreateResponse;
import com.aspire.mirror.template.api.dto.TemplateObjectDetailResponse;
import com.aspire.mirror.template.api.dto.model.TemplateObjectDTO;
import com.aspire.mirror.template.api.service.TemplateObjectService;
import com.aspire.mirror.template.server.biz.TemplateObjectBiz;
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

/**
 * 模板关联对象控制层实现类
 * <p>
 * 项目名称: mirror平台
 * 包:      com.aspire.mirror.template.server.controller
 * 类名称:   TemplateDeviceController.java
 * 类描述:   模板关联对象控制层业务逻辑层实现类
 * 创建人:   JinSu
 * 创建时间: 2018-07-27 12:14:48
 */
@RestController
@CacheConfig(cacheNames = "TemplateObjectCache")
public class TemplateObjectController implements TemplateObjectService {


    /**
     * slf4j
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(TemplateObjectController.class);

    /**
     * 根据模板删除
     *
     * @param templateIds 模板ID
     * @return
     */
    @Override
    public ResponseEntity<String> deleteByTemplateIds(@PathVariable("template_ids") final String templateIds) {
        try {
            templateObjectBiz.deleteByTemplateIds(templateIds);
            return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            LOGGER.error("deleteByPrimaryKey error:" + e.getMessage(), e);
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 批量创建模板关联对象
     *
     * @param batchCreateRequst 批量创建请求
     * @return
     */
    @Override
    public ResponseEntity<String> batchCreate(@RequestBody @Validated TemplateObjectBatchCreateRequest batchCreateRequst) {
        if (null == batchCreateRequst) {
            LOGGER.error("created param batchCreate is null");
            throw new RuntimeException("batchCreate is null");
        }
        if (CollectionUtils.isEmpty(batchCreateRequst.getTemplateObjectList())) {
            LOGGER.error("created param object batchCreate is empty");
            throw new RuntimeException("batchCreate param is empty");
        }
        List<TemplateObjectDTO> templateObjectDTOS = Lists.newArrayList();

        for (TemplateObjectCreateRequest createRequest : batchCreateRequst.getTemplateObjectList()) {
            TemplateObjectDTO templateObjectDTO = new TemplateObjectDTO();
            BeanUtils.copyProperties(createRequest, templateObjectDTO);
            templateObjectDTOS.add(templateObjectDTO);
        }
        templateObjectBiz.insertByBatch(templateObjectDTOS);

        return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
    }

    /**
     * 根据模板ID查询列表
     *
     * @param templateId 模板Id
     * @return
     */
    @Override
    public List<TemplateObjectDetailResponse> findByTemplateId(@PathVariable("template_id") final String templateId) {
        if (StringUtils.isEmpty(templateId)) {
            LOGGER.error("查询条件模板id不能为空");
        }
        List<TemplateObjectDTO> templateObjectDTOList = templateObjectBiz.selectByTemplateId(templateId);
        List<TemplateObjectDetailResponse> response = Lists.newArrayList();
        for (TemplateObjectDTO templateObjectDTO : templateObjectDTOList) {
            TemplateObjectDetailResponse templateObjectDetailResponse = new TemplateObjectDetailResponse();
            BeanUtils.copyProperties(templateObjectDTO, templateObjectDetailResponse);
            response.add(templateObjectDetailResponse);
        }
        return response;
    }


    @Autowired
    private TemplateObjectBiz templateObjectBiz;
}