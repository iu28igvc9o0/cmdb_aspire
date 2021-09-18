package com.aspire.ums.cmdb.common.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.aspire.ums.cmdb.common.entity.DictEntity;
import com.aspire.ums.cmdb.common.service.DictService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: DictController
 * Author:   zhu.juwang
 * Date:     2019/3/12 9:12
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@RefreshScope
@RestController
@Slf4j
@RequestMapping("/cmdb/dict")
public class DictController {
    @Autowired
    private DictService dictService;

    /**
     * 根据字典值编码，查询字典值列表
     * @param code 字典编码
     * @return 字典值列表
     */
    @RequestMapping("/getDict/{code}")
    public JSONArray getDictList(@PathVariable("code") String code) {
        List<DictEntity> dictList=dictService.getDictByCode(code);
        if (dictList != null && dictList.size() >0) {
            return (JSONArray) JSON.toJSON(dictList);
        }
        return new JSONArray();
    }
}
