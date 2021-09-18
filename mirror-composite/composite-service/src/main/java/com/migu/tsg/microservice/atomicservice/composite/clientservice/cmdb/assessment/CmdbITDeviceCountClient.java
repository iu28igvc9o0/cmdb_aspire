package com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.assessment;

import java.util.List;
import java.util.Map;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.aspire.ums.cmdb.assessment.payload.CmdbItDeviceCount;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: CmdbITDeviceCountClient
 * Author:   hangfang
 * Date:     2019/6/25 18:58
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@FeignClient(value = "CMDB")
public interface CmdbITDeviceCountClient {

    /**
     * 查询所有设备量信息
     * @return 设备量
     */
    @RequestMapping(value = "/cmdb/device/count/list", method = RequestMethod.POST)
    List<CmdbItDeviceCount> list(@RequestBody CmdbItDeviceCount deviceCount);


    /**
     * 存储设备量信息
     */
    @RequestMapping(value = "/cmdb/device/count/save", method = RequestMethod.POST)
    Map<String, Object> save(@RequestBody List<CmdbItDeviceCount> deviceCount);
    /**
     * 删除存储设备量信息
     */
    @RequestMapping(value = "/cmdb/device/count/delete", method = RequestMethod.POST)
    Map<String, Object> delete(@RequestBody List<String> produces);

    /**
     * 获取分支机构状态
     * @param quarter 实例数据
     * @return
     */
    @RequestMapping(value = "/cmdb/device/count/getProvinceStatus", method = RequestMethod.GET)
    List<Map<String, Object>> getProvinceStatus(@RequestParam("quarter") String quarter);

    @RequestMapping(value = "/cmdb/device/count/getProduceAndDeviceList", method = RequestMethod.POST)
    List<Map<String, Object>> getProduceAndDeviceList(@RequestBody Map<String,String> requestMp);
}
