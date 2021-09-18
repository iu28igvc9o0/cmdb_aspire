package com.aspire.mirror.theme.server.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aspire.mirror.common.auth.GeneralAuthConstraintsAware;
import com.aspire.mirror.common.constant.Constant;
import com.aspire.mirror.common.entity.PageRequest;
import com.aspire.mirror.common.entity.PageResponse;
import com.aspire.mirror.common.entity.Result;
import com.aspire.mirror.common.util.DateUtil;
import com.aspire.mirror.theme.api.dto.*;
import com.aspire.mirror.theme.api.dto.model.BizThemeDTO;
import com.aspire.mirror.theme.api.dto.model.BizThemeDimDTO;
import com.aspire.mirror.theme.api.dto.model.ThemeDataDTO;
import com.aspire.mirror.theme.api.dto.vo.BizThemeDimVO;
import com.aspire.mirror.theme.api.service.BizThemeService;
import com.aspire.mirror.theme.common.entity.FieldUtil;
import com.aspire.mirror.theme.server.biz.BizThemeBiz;
import com.google.common.collect.Lists;
import io.krakens.grok.api.Grok;
import io.krakens.grok.api.GrokCompiler;
import io.krakens.grok.api.Match;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * 业务主题控制类
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.theme.server.controller
 * 类名称:    BIzThemeController.java
 * 类描述:    业务主题控制类
 * 创建人:    JinSu
 * 创建时间:  2018/10/23 17:05
 * 版本:      v1.0
 */
@RestController
public class BizThemeController implements BizThemeService {



    /**
     * 创建主题
     *
     * @param createRequest 创建主题请求
     * @return BizThemeCreateResponse 创建结果返回
     */
    @Override
    public BizThemeCreateResponse createdBizTheme(@RequestBody @Validated final BizThemeCreateRequest createRequest) {
        if (null == createRequest) {
            LOGGER.error("created param createRequest is null");
            throw new RuntimeException("createRequest is null");
        }
        BizThemeDTO bizThemeDTO = new BizThemeDTO();
        BeanUtils.copyProperties(createRequest, bizThemeDTO);
        List<BizThemeDimDTO> dimDTOS = Lists.newArrayList();
        for (BizThemeDimVO bizThemeDimVO : createRequest.getDimList()) {
            BizThemeDimDTO bizThemeDimDTO = new BizThemeDimDTO();
            BeanUtils.copyProperties(bizThemeDimVO, bizThemeDimDTO);
            dimDTOS.add(bizThemeDimDTO);
        }
        bizThemeDTO.setDimList(dimDTOS);
        String themeId = bizThemeBiz.insert(bizThemeDTO);

        BizThemeDTO themeDTO = bizThemeBiz.selectByPrimaryKey(themeId, new GeneralAuthConstraintsAware());
        BizThemeCreateResponse response = new BizThemeCreateResponse();
        BeanUtils.copyProperties(themeDTO, response);

        return response;
    }

    /**
     * 修改主题
     *
     * @param themeId       主题ID
     * @param updateRequest 修改请求
     * @return BizThemeUpdateResponse 修改返回
     */
    @Override
    public BizThemeUpdateResponse modifyByPrimaryKey(@PathVariable("theme_id") final String themeId, @RequestBody
    @Validated final BizThemeUpdateRequest updateRequest) {
        if (StringUtils.isEmpty(themeId)) {
            LOGGER.error("modify path param theme_is is empty");
            throw new RuntimeException("theme_is is empty");
        }
        BizThemeDTO bizThemeDTO = new BizThemeDTO();
        BeanUtils.copyProperties(updateRequest, bizThemeDTO);
        bizThemeDTO.setThemeId(themeId);
        if (!CollectionUtils.isEmpty(updateRequest.getDimList())) {
            List<BizThemeDimDTO> dimDTOList = Lists.newArrayList();
            for (BizThemeDimVO bizThemeDimVO : updateRequest.getDimList()) {
                BizThemeDimDTO dimDTO = new BizThemeDimDTO();
                BeanUtils.copyProperties(bizThemeDimVO, dimDTO);
                dimDTOList.add(dimDTO);
            }
            bizThemeDTO.setDimList(dimDTOList);
        }
        bizThemeBiz.updateByPrimaryKey(bizThemeDTO);

        BizThemeDTO themeDTO = bizThemeBiz.selectByPrimaryKey(themeId, new GeneralAuthConstraintsAware());
        BizThemeUpdateResponse response = new BizThemeUpdateResponse();
        BeanUtils.copyProperties(themeDTO, response);
        return response;
    }

    /**
     * 删除主题
     *
     * @param themeId 主题ID
     */
    @Override
    public void deleteByPrimaryKey(@PathVariable("theme_id") final String themeId) {
        if (StringUtils.isEmpty(themeId)) {
            throw new RuntimeException("themeId is null");
        }
        bizThemeBiz.deleteByPrimaryKey(themeId);
    }

    /**
     * 分页查询
     *
     * @param pageRequest 分页请求对象
     * @return PageResponse<BizThemeDetailResponse>
     */
    @Override
    public PageResponse<BizThemeDetailResponse> pageList(@RequestBody @Validated final BizThemePageRequest
                                                                 pageRequest) {
        if (null == pageRequest) {
            LOGGER.warn("pageList param pageRequset is null");
            return null;
        }
        PageRequest page = new PageRequest();
        BeanUtils.copyProperties(pageRequest, page);
        Map<String, Object> map = FieldUtil.getFiledMap(pageRequest);
        for (String key : map.keySet()) {
            page.addFields(key, map.get(key));
        }
        PageResponse<BizThemeDTO> pageResult = bizThemeBiz.pageList(page);
        List<BizThemeDetailResponse> themeList = Lists.newArrayList();
        if (!CollectionUtils.isEmpty(pageResult.getResult())) {
            for (BizThemeDTO bizThemeDTO : pageResult.getResult()) {
                BizThemeDetailResponse bizThemeDetailResponse = new BizThemeDetailResponse();
                BeanUtils.copyProperties(bizThemeDTO, bizThemeDetailResponse);
//                List<BizThemeDimDetailResponse> dimDetailResponseList = Lists.newArrayList();
////                for(BizThemeDimDTO bizThemeDimDTO : bizThemeDTO.getDimList()) {
////                    BizThemeDimDetailResponse bizThemeDim = new BizThemeDimDetailResponse();
////                    BeanUtils.copyProperties(bizThemeDimDTO, bizThemeDim);
////                    dimDetailResponseList.add(bizThemeDim);
////                }
////                bizThemeDetailResponse.setDimList(dimDetailResponseList);
                themeList.add(bizThemeDetailResponse);
            }
        }
        PageResponse<BizThemeDetailResponse> result = new PageResponse<>();
        result.setCount(pageResult.getCount());
        result.setResult(themeList);
        return result;
    }

    /**
     * 查询列表
     *
     * @param searchRequest 查询请求
     * @return List<BizThemeDetailResponse> 结果列表
     */
    @Override
    public List<BizThemeDetailResponse> select(@RequestBody final BizThemeSearchRequest searchRequest) {
        if (null == searchRequest) {
            LOGGER.warn("select param searchRequest is null");
            return null;
        }
        BizThemeDTO param = new BizThemeDTO();
        BeanUtils.copyProperties(searchRequest, param);
        List<BizThemeDTO> themeDTOS = bizThemeBiz.select(param);
        List<BizThemeDetailResponse> themeList = Lists.newArrayList();
        if (!CollectionUtils.isEmpty(themeDTOS)) {
            for (BizThemeDTO bizThemeDTO : themeDTOS) {
                BizThemeDetailResponse bizThemeDetailResponse = new BizThemeDetailResponse();
                BeanUtils.copyProperties(bizThemeDTO, bizThemeDetailResponse);
                themeList.add(bizThemeDetailResponse);
            }
        }
        return themeList;
    }

    /**
     * 查询详情
     *
     * @param themeId 主题ID
     * @return BizThemeDetailResponse 主题详情返回
     */
    @Override
    public BizThemeDetailResponse findByPrimaryKey(@PathVariable("theme_id") final String themeId, @RequestBody GeneralAuthConstraintsAware authParam) {
        if (StringUtils.isEmpty(themeId)) {
            LOGGER.warn("findByPrimaryKey param themeId is null");
            return null;
        }
        BizThemeDTO bizThemeDTO = bizThemeBiz.selectByPrimaryKey(themeId, authParam);

        BizThemeDetailResponse bizThemeDetailResponse = new BizThemeDetailResponse();
        if (null != bizThemeDTO) {
            BeanUtils.copyProperties(bizThemeDTO, bizThemeDetailResponse);
            List<BizThemeDimDetailResponse> dimDetailResponseList = Lists.newArrayList();
            for (BizThemeDimDTO bizThemeDimDTO : bizThemeDTO.getDimList()) {
                BizThemeDimDetailResponse bizThemeDim = new BizThemeDimDetailResponse();
                BeanUtils.copyProperties(bizThemeDimDTO, bizThemeDim);
                dimDetailResponseList.add(bizThemeDim);
            }
            bizThemeDetailResponse.setDimList(dimDetailResponseList);
        }
        return bizThemeDetailResponse;
    }

    /**
     * 接收主题数据
     *
     * @param themeDataCreateRequest 主题数据对象
     * @return Result 接收主题返回结果
     */
    @Override
    public Result createThemeData(@RequestBody @Validated ThemeDataCreateRequest themeDataCreateRequest) {
        try {
            ThemeDataDTO themeDataDTO = new ThemeDataDTO();
            BeanUtils.copyProperties(themeDataCreateRequest, themeDataDTO);
            return bizThemeBiz.createThemeData(themeDataDTO);
        } catch (Exception e) {
            LOGGER.error("主题数据接收异常", e);
            return new Result("500", "主题接收处理异常！");
        }

    }

    /**
     * 获取主题数据
     *
     * @param themeId   主题ID
     * @param host      主机IP
     * @param bizCode   业务系统编码
     * @param themeCode 主题编码
     * @return BizThemeDataDetailResponse 主题详情
     */
    @Override
    public BizThemeDataDetailResponse getThemeData(@PathVariable("theme_id") String themeId, @RequestParam(value =
            "host", required = false) String host, @RequestParam(value = "biz_code", required = false) String
                                                           bizCode, @RequestParam(value = "theme_code", required =
            false) String themeCode) {
//        LOGGER.info("BizThemeController[getThemeData]");
        BizThemeDataDetailResponse bizThemeDataDetailResponse = new BizThemeDataDetailResponse();
        Map<String, Object> resMap = bizThemeBiz.getThemeData(themeId, host, bizCode, themeCode);
        bizThemeDataDetailResponse.setResMap(resMap);
        bizThemeDataDetailResponse.setSuccess(true);
        return bizThemeDataDetailResponse;
    }

    @Override
    public BizThemeDataDetailResponse getThemeDataHis(@PathVariable("theme_id") String themeId) {
//        LOGGER.info("BizThemeController[getThemeData]");
        BizThemeDataDetailResponse bizThemeDataDetailResponse = new BizThemeDataDetailResponse();
        Map<String, Object> resMap = bizThemeBiz.getThemeDataHis(themeId);
        bizThemeDataDetailResponse.setResMap(resMap);
        bizThemeDataDetailResponse.setSuccess(true);
        return bizThemeDataDetailResponse;
    }

    @Override
    public Map<String, Object> getTrendMapData(@RequestParam(value = "index_name") String indexName,
                                               @RequestParam(value = "theme_code") String themeCode,
                                               @RequestParam(value = "last_up_time_str") String lastUpTimeStr,
                                               @RequestParam(value = "start") String start,
                                               @RequestParam(value = "end") String end) {
        Map<String, Object> resultMap = new HashMap<>();
//        if (start == null) {
//            start = "0";
//        }
//        if (end == null) {
//            end = "0";
//        }
        Date lastUpTime;
        if (!StringUtils.isEmpty(lastUpTimeStr)) {
            lastUpTime = DateUtil.getDate(lastUpTimeStr, "yyyy-MM-dd HH:mm:ss");
        } else {
            return resultMap;
        }
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(lastUpTime);
        final long ed = calendar.getTimeInMillis();
        calendar.add(Calendar.HOUR_OF_DAY, -24);
        final long sta = calendar.getTimeInMillis();
        resultMap = bizThemeBiz.thematicHistoryInfo(indexName, themeCode, sta, ed);
        return resultMap;
    }

    @Override
    public List<Map<String, Object>> validLogContent(@RequestBody @Validated ThemeLogValidRequest validParam) {
        String logContent = validParam.getLogContent();
        String dimList = validParam.getDimList();
//        if (StringUtils.isEmpty(logContent)) {
//            LOGGER.warn("BizThemeController method[validLogContent] param logContent is empty");
//            throw new RuntimeException("logContent is empty");
//        }
//        if (StringUtils.isEmpty(dimList)) {
//            LOGGER.warn("BizThemeController method[validLogContent] param dimList is empty");
//            throw new RuntimeException("validReg is empty");
//        }
//        String[] logContentArray = logContent.split("\n");
        GrokCompiler grokCompiler = GrokCompiler.newInstance();
        List<Map<String, Object>> list = Lists.newArrayList();
        grokCompiler.registerDefaultPatterns();
        StringBuffer themeRegBuffer = new StringBuffer();
        JSONArray jsonArray = JSON.parseArray(dimList);
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            if (jsonObject.getString("match_flag").equals("1")) {
                themeRegBuffer.append("%{").append(jsonObject.getString("dim_reg")).append(":").append(jsonObject
                        .getString("dim_code")
                ).append("}");
            } else {
                //非字段直接拼装
                String dimReg = jsonObject.getString("dim_reg");
                for (String UnescapedReg : Constant.ESCAPE_DIM_REG_ARRAY) {

                    if (UnescapedReg.equals(dimReg)) {
//                        bizThemeDim.setDimReg("\\" + bizThemeDim.getDimReg());
                        dimReg = "\\" + dimReg;
                        break;
                    }
                }
                themeRegBuffer.append(dimReg);
            }
        }
//        for (String logContentItem : logContentArray) {
            final Grok grok = grokCompiler.compile(themeRegBuffer.toString());
//            Match grokMatch = grok.match(logContentItem);
            Match grokMatch = grok.match(logContent);
            Map<String, Object> resultMap = grokMatch.captureFlattened();
            list.add(resultMap);
//        }
        return list;
    }
//
//    @Override
//    public Object getTest(@RequestParam(name = "freshTime", required = false) String freshTime) {
//
//        return new Object();
//    }

    private static final Logger LOGGER = LoggerFactory.getLogger(BizThemeController.class);

    @Autowired
    private BizThemeBiz bizThemeBiz;

}
