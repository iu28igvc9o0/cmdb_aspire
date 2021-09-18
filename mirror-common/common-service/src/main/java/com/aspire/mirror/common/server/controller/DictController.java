package com.aspire.mirror.common.server.controller;

import com.aspire.mirror.common.api.DictResponse;
import com.aspire.mirror.common.api.dto.model.ConfigDict;
import com.aspire.mirror.common.api.dto.model.DictDTO;
import com.aspire.mirror.common.api.service.DictService;
import com.aspire.mirror.common.server.biz.DictBiz;
import com.aspire.mirror.common.server.service.ConfigDictService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 字典控制层
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.common.server.controller
 * 类名称:    DictController.java
 * 类描述:    字典控制层
 * 创建人:    JinSu
 * 创建时间:  2018/11/12 10:37
 * 版本:      v1.0
 */
@RestController
@Slf4j
public class DictController implements DictService {
    @Autowired
    private ConfigDictService configDictService;

    /**
     * 查找所有字典数据
     *
     * @return
     */
    @Override
    public DictResponse findAll() {
        DictDTO dictDTO = dictBiz.findAll();
        DictResponse response = new DictResponse();
        BeanUtils.copyProperties(dictDTO, response);
        return response;
    }

    /**
     * 查找所有字典数据
     *
     * @return
     */
    @Override
    public DictResponse findList() {
        DictDTO dictDTO = dictBiz.findList();
        DictResponse response = new DictResponse();
        BeanUtils.copyProperties(dictDTO, response);
        return response;
    }


    @Override
    public List<ConfigDict> getDictsByType(String type, String pid, String pValue, String pType) {
        return configDictService.selectDictsByType(type, pid, pValue, pType);
    }


    @Autowired
    private DictBiz dictBiz;
}
