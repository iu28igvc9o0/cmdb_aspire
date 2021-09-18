package com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.assessment;

import java.util.List;
import java.util.Map;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.aspire.ums.cmdb.assessment.payload.CmdbProduceAssessment;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: CmdbProduceAssessmentClient
 * Author:   hangfang
 * Date:     2019/7/10
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@FeignClient(value = "CMDB")
public interface CmdbProduceAssessmentClient {

    /**
     * 查询所有故障事件信息
     * @return 故障事件信息
     */
    @RequestMapping(value = "/cmdb/assessment/assessment/save", method = RequestMethod.POST)
    Map<String, Object> save(@RequestBody List<CmdbProduceAssessment> produceAssessments);

    /**
     * 根据countid查询评分数据
     */
    @RequestMapping(value = "/cmdb/assessment/assessment/listByCountIds", method = RequestMethod.POST)
    List<CmdbProduceAssessment> listByCountIds(List<String> countIds);
    /**
     * 批量审批所有信息
     */
    @RequestMapping(value = "/cmdb/assessment/assessment/approval", method = RequestMethod.PUT)
    Map<String, Object> approval(@RequestParam("status") Integer status,
                                 @RequestParam("province") String province,
                                 @RequestParam("quarter") String quarter);
    /**
     * 生成评分信息
     */
    @RequestMapping(value = "/cmdb/assessment/assessment/makeevaluate", method = RequestMethod.POST)
    Map<String, Object> makeEvaluate(@RequestParam("quarter") String quarter, @RequestParam("deviceType") String deviceType);

    /**
     * 导出评分信息
     */
    @RequestMapping(value = "/cmdb/assessment/assessment/exportevaluate", method = RequestMethod.POST)
    Map<String, Object> exportEvaluate(@RequestParam("quarter") String quarter, @RequestParam("deviceType") String deviceType);
}
