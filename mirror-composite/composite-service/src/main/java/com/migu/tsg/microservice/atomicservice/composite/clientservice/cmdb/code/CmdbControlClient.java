package com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.code;

import java.util.List;
import java.util.Map;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.aspire.ums.cmdb.code.payload.CmdbControlType;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: CmdbCodeController
 * Author:   zhu.juwang
 * Date:     2019/5/13 18:58
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@FeignClient(value = "CMDB")
public interface CmdbControlClient {

    /**
     * 查询控件类型列表
     * @param queryParams {"controlCode": "", "controlName":""}
     */
    @RequestMapping(value = "/cmdb/code/control/list", method = RequestMethod.POST)
    List<CmdbControlType> list(@RequestBody Map<String, Object> queryParams);

    /**
     * 查询控件类型列表
     * @param controlId 控件ID
     */
    @RequestMapping(value = "/cmdb/code/control/get", method = RequestMethod.GET)
    CmdbControlType get(@RequestParam("controlId") String controlId);

    /**
     * 验证控件编码或名称是否已经存在
     * return {"flag": true} / {"flag": false}
     */
    @RequestMapping(value = "/cmdb/code/control/valid", method = RequestMethod.GET)
    Map<String, String> valid(@RequestParam("filed") String filed, @RequestParam("value") String value);

    /**
     * 新增控件信息
     * @param controlType 控件信息
     */
    @RequestMapping(value = "/cmdb/code/control/saveControl", method = RequestMethod.POST)
    void saveControl(@RequestBody CmdbControlType controlType);

    /**
     * 删除码表信息
     * @param controlTypeList 码表信息
     */
    @RequestMapping(value = "/cmdb/code/control/deleteControl", method = RequestMethod.DELETE)
    void deleteControl(@RequestBody List<CmdbControlType> controlTypeList);
}
