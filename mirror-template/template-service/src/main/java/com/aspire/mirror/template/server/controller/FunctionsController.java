//package com.aspire.mirror.template.server.controller;
//
//import com.aspire.mirror.template.api.dto.FunctionsCreateRequest;
//import com.aspire.mirror.template.api.dto.FunctionsCreateResponse;
//import com.aspire.mirror.template.api.dto.FunctionsUpdateRequest;
//import com.aspire.mirror.template.api.dto.FunctionsUpdateResponse;
//import com.aspire.mirror.template.api.dto.model.FunctionsDTO;
//import com.aspire.mirror.template.api.dto.vo.FunctionsVO;
//import com.aspire.mirror.template.api.service.FunctionsService;
//import com.aspire.mirror.template.server.biz.FunctionsBiz;
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
// * monitor_functions控制层实现类
// *
// * 项目名称: mirror平台
// * 包:      com.aspire.mirror.template.server.controller
// * 类名称:   FunctionsController.java
// * 类描述:   monitor_functions业务逻辑层实现类
// * 创建人:   JinSu
// * 创建时间: 2018-07-27 12:14:48
// */
//@RestController
//@CacheConfig(cacheNames = "FunctionsCache")
//public class FunctionsController implements FunctionsService {
//
//    /**
//     * 创建monitor_functions信息
//     *
//     * @param functionsCreateRequest monitor_functions创建请求对象
//     * @return FunctionsCreateResponse monitor_functions创建响应对象
//     */
//    public FunctionsCreateResponse createdFunctions(@RequestBody final FunctionsCreateRequest functionsCreateRequest){
//        if(null == functionsCreateRequest){
//            LOGGER.error("created param functionsCreateRequest is null");
//            throw new RuntimeException("functionsCreateRequest is null");
//        }
//        FunctionsDTO functionsDTO = new FunctionsDTO();
//        BeanUtils.copyProperties(functionsCreateRequest, functionsDTO);
//        functionsBiz.insert(functionsDTO);
//        FunctionsCreateResponse functionsCreateResponse = new FunctionsCreateResponse();
//        BeanUtils.copyProperties(functionsDTO, functionsCreateResponse);
//        return functionsCreateResponse;
//    }
//    /**
//     * 根据主键删除单条monitor_functions信息
//     *
//     * @param functionId 主键
//     * @@return Result 返回结果
//     */
//    public ResponseEntity<String> deleteByPrimaryKey(@PathVariable("function_id") final String functionId){
//        try {
//            functionsBiz.deleteByPrimaryKey(functionId);
//            return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
//        } catch (Exception e) {
//            LOGGER.error("deleteByProjectId error:" + e.getMessage(), e);
//            return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
//
//    /**
//     * 根据主键删除多条monitor_functions信息
//     *
//     * @param functionIds 主键（以逗号分隔）
//     * @@return Result 返回结果
//     */
//    public ResponseEntity<String> deleteByPrimaryKeyArrays(@PathVariable("function_ids") final String functionIds){
//        try {
//            if (StringUtils.isEmpty(functionIds)) {
//                throw new RuntimeException("functionIds is null");
//            }
//            String[] functionIdArrays = functionIds.split(",");
//            functionsBiz.deleteByPrimaryKeyArrays(functionIdArrays);
//            return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
//        } catch (Exception e) {
//            LOGGER.error("deleteByPrimaryKeyArrays error:" + e.getMessage(), e);
//            return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
//
//    /**
//     * 根据主键修改monitor_functions信息
//     *
//     * @param functionsUpdateRequest monitor_functions修改请求对象
//     * @return FunctionsUpdateResponse monitor_functions修改响应对象
//     */
//    public FunctionsUpdateResponse modifyByPrimaryKey(@PathVariable("function_id") final String functionId,@RequestBody final FunctionsUpdateRequest functionsUpdateRequest){
//        FunctionsDTO functionsdTO = new FunctionsDTO();
//        functionsdTO.setFunctionId(functionId);
//        BeanUtils.copyProperties(functionsUpdateRequest, functionsdTO);
//
//        functionsBiz.updateByPrimaryKey(functionsdTO);
//        FunctionsDTO findFunctionsDTO = functionsBiz.selectByPrimaryKey(functionId);
//        FunctionsUpdateResponse functionsUpdateResponse = new FunctionsUpdateResponse();
//        BeanUtils.copyProperties(functionsUpdateResponse, findFunctionsDTO);
//        return functionsUpdateResponse;
//    }
//
//    /**
//     * 根据主键查找monitor_functions详情信息
//     *
//     * @param functionId monitor_functions主键
//     * @return FunctionsVO monitor_functions详情响应对象
//     */
//    public FunctionsVO findByPrimaryKey(@PathVariable("function_id") final String functionId){
//        if (StringUtils.isEmpty(functionId)) {
//            LOGGER.warn("findByPrimaryKey param functionId is null");
//            return null;
//        }
//        FunctionsDTO functionsDTO = functionsBiz.selectByPrimaryKey(functionId);
//        FunctionsVO functionsVO = new FunctionsVO();
//        if (null != functionsDTO) {
//            BeanUtils.copyProperties(functionsDTO, functionsVO);
//        }
//
//        return functionsVO;
//    }
//
//    /**
//     * 根据主键查询monitor_functions集合信息
//     *
//     * @param functionIds monitor_functions主键
//     * @return FunctionsVO monitor_functions查询响应对象
//     */
//    public List<FunctionsVO> listByPrimaryKeyArrays(@PathVariable("function_id") final String functionIds){
//        if (StringUtils.isEmpty(functionIds)) {
//            LOGGER.error("listByPrimaryKeyArrays param functionIds is null");
//            return Lists.newArrayList();
//        }
//        String[] functionIdArrays = functionIds.split(",");
//        List<FunctionsDTO> listFunctionsDTO = functionsBiz.selectByPrimaryKeyArrays(functionIdArrays);
//        List<FunctionsVO> listFunctionsVO = Lists.newArrayList();
//        if (!CollectionUtils.isEmpty(listFunctionsDTO)) {
//            for (FunctionsDTO functionsDTO : listFunctionsDTO) {
//                FunctionsVO functionsVO = new FunctionsVO();
//                BeanUtils.copyProperties(functionsDTO, functionsVO);
//                listFunctionsVO.add(functionsVO);
//            }
//        }
//        return listFunctionsVO;
//    }
//
//    /** slf4j*/
//    private static final Logger LOGGER           = LoggerFactory.getLogger(FunctionsController.class);
//
//    @Autowired
//    private FunctionsBiz functionsBiz;
//
//}