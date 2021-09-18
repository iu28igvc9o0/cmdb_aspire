//package com.aspire.mirror.template.server.controller;
//
//import com.aspire.mirror.template.api.dto.TemplateDeviceCreateRequest;
//import com.aspire.mirror.template.api.dto.TemplateDeviceCreateResponse;
//import com.aspire.mirror.template.api.dto.TemplateDeviceUpdateRequest;
//import com.aspire.mirror.template.api.dto.TemplateDeviceUpdateResponse;
//import com.aspire.mirror.template.api.dto.model.TemplateDeviceDTO;
//import com.aspire.mirror.template.api.dto.vo.TemplateDeviceVO;
//import com.aspire.mirror.template.api.service.TemplateDeviceService;
//import com.aspire.mirror.template.server.biz.TemplateDeviceBiz;
//import com.google.common.collect.Lists;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.BeanUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.cache.annotation.CacheConfig;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.util.CollectionUtils;
//import org.springframework.util.StringUtils;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.List;
//
///**
// * monitor_template_device控制层实现类
// *
// * 项目名称: mirror平台
// * 包:      com.aspire.mirror.template.server.controller
// * 类名称:   TemplateDeviceController.java
// * 类描述:   monitor_template_device业务逻辑层实现类
// * 创建人:   JinSu
// * 创建时间: 2018-07-27 12:14:48
// */
//@RestController
//@CacheConfig(cacheNames = "TemplateDeviceCache")
//public class TemplateDeviceController implements TemplateDeviceService {
//
//    /**
//     * 创建monitor_template_device信息
//     *
//     * @param templateDeviceCreateRequest monitor_template_device创建请求对象
//     * @return TemplateDeviceCreateResponse monitor_template_device创建响应对象
//     */
//    public TemplateDeviceCreateResponse createdTemplateDevice(@RequestBody final TemplateDeviceCreateRequest templateDeviceCreateRequest){
//        if(null == templateDeviceCreateRequest){
//            LOGGER.error("created param templateDeviceCreateRequest is null");
//            throw new RuntimeException("templateDeviceCreateRequest is null");
//        }
//        TemplateDeviceDTO templateDeviceDTO = new TemplateDeviceDTO();
//        BeanUtils.copyProperties(templateDeviceCreateRequest, templateDeviceDTO);
//        templateDeviceBiz.insert(templateDeviceDTO);
//        TemplateDeviceCreateResponse templateDeviceCreateResponse = new TemplateDeviceCreateResponse();
//        BeanUtils.copyProperties(templateDeviceDTO, templateDeviceCreateResponse);
//        return templateDeviceCreateResponse;
//    }
//    /**
//     * 根据主键删除单条monitor_template_device信息
//     *
//     * @param templateDeviceId 主键
//     * @@return Result 返回结果
//     */
//    public ResponseEntity<String> deleteByPrimaryKey(@PathVariable("template_device_id") final String templateDeviceId){
//        try {
//            templateDeviceBiz.deleteByPrimaryKey(templateDeviceId);
//            return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
//        } catch (Exception e) {
//            LOGGER.error("deleteByProjectId error:" + e.getMessage(), e);
//            return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
//
//    /**
//     * 根据主键删除多条monitor_template_device信息
//     *
//     * @param templateDeviceIds 主键（以逗号分隔）
//     * @@return Result 返回结果
//     */
//    public ResponseEntity<String> deleteByPrimaryKeyArrays(@PathVariable("template_device_ids") final String templateDeviceIds){
//        try {
//            if (StringUtils.isEmpty(templateDeviceIds)) {
//                throw new RuntimeException("templateDeviceIds is null");
//            }
//            String[] templateDeviceIdArrays = templateDeviceIds.split(",");
//            templateDeviceBiz.deleteByPrimaryKeyArrays(templateDeviceIdArrays);
//            return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
//        } catch (Exception e) {
//            LOGGER.error("deleteByPrimaryKeyArrays error:" + e.getMessage(), e);
//            return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
//
//    /**
//     * 根据主键修改monitor_template_device信息
//     *
//     * @param templateDeviceUpdateRequest monitor_template_device修改请求对象
//     * @return TemplateDeviceUpdateResponse monitor_template_device修改响应对象
//     */
//    public TemplateDeviceUpdateResponse modifyByPrimaryKey(@PathVariable("template_device_id") final String templateDeviceId,@RequestBody final TemplateDeviceUpdateRequest templateDeviceUpdateRequest){
//        TemplateDeviceDTO templateDevicedTO = new TemplateDeviceDTO();
//        templateDevicedTO.setTemplateDeviceId(templateDeviceId);
//        BeanUtils.copyProperties(templateDeviceUpdateRequest, templateDevicedTO);
//
//        templateDeviceBiz.updateByPrimaryKey(templateDevicedTO);
//        TemplateDeviceDTO findTemplateDeviceDTO = templateDeviceBiz.selectByPrimaryKey(templateDeviceId);
//        TemplateDeviceUpdateResponse templateDeviceUpdateResponse = new TemplateDeviceUpdateResponse();
//        BeanUtils.copyProperties(templateDeviceUpdateResponse, findTemplateDeviceDTO);
//        return templateDeviceUpdateResponse;
//    }
//
//    /**
//     * 根据主键查找monitor_template_device详情信息
//     *
//     * @param templateDeviceId monitor_template_device主键
//     * @return TemplateDeviceVO monitor_template_device详情响应对象
//     */
//    public TemplateDeviceVO findByPrimaryKey(@PathVariable("template_device_id") final String templateDeviceId){
//        if (StringUtils.isEmpty(templateDeviceId)) {
//            LOGGER.warn("findByPrimaryKey param templateDeviceId is null");
//            return null;
//        }
//        TemplateDeviceDTO templateDeviceDTO = templateDeviceBiz.selectByPrimaryKey(templateDeviceId);
//        TemplateDeviceVO templateDeviceVO = new TemplateDeviceVO();
//        if (null != templateDeviceDTO) {
//            BeanUtils.copyProperties(templateDeviceDTO, templateDeviceVO);
//        }
//
//        return templateDeviceVO;
//    }
//
//    /**
//     * 根据主键查询monitor_template_device集合信息
//     *
//     * @param templateDeviceIds monitor_template_device主键
//     * @return TemplateDeviceVO monitor_template_device查询响应对象
//     */
//    public List<TemplateDeviceVO> listByPrimaryKeyArrays(@PathVariable("template_device_id") final String templateDeviceIds){
//        if (StringUtils.isEmpty(templateDeviceIds)) {
//            LOGGER.error("listByPrimaryKeyArrays param templateDeviceIds is null");
//            return Lists.newArrayList();
//        }
//        String[] templateDeviceIdArrays = templateDeviceIds.split(",");
//        List<TemplateDeviceDTO> listTemplateDeviceDTO = templateDeviceBiz.selectByPrimaryKeyArrays(templateDeviceIdArrays);
//        List<TemplateDeviceVO> listTemplateDeviceVO = Lists.newArrayList();
//        if (!CollectionUtils.isEmpty(listTemplateDeviceDTO)) {
//            for (TemplateDeviceDTO templateDeviceDTO : listTemplateDeviceDTO) {
//                TemplateDeviceVO templateDeviceVO = new TemplateDeviceVO();
//                BeanUtils.copyProperties(templateDeviceDTO, templateDeviceVO);
//                listTemplateDeviceVO.add(templateDeviceVO);
//            }
//        }
//        return listTemplateDeviceVO;
//    }
//
//    /** slf4j*/
//    private static final Logger LOGGER           = LoggerFactory.getLogger(TemplateDeviceController.class);
//
//    @Autowired
//    private TemplateDeviceBiz templateDeviceBiz;
//
//}