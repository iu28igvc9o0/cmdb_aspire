package com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.process;

import java.util.Map;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.aspire.ums.cmdb.collectApproval.payload.CmdbApprovalUpdateReq;
import com.aspire.ums.cmdb.instance.payload.ImportProcess;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: ApprovalProcessClient
 * Author:   hangfang
 * Date:     2019/9/23
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@FeignClient(value = "CMDB")
public interface ApprovalProcessClient {

    /**
     * 批量审核
     */
    @RequestMapping(value = "/cmdb/process/approval", method = RequestMethod.POST)
    Map<String, Object> approvalProcess(@RequestParam("userName") String userName, @RequestBody CmdbApprovalUpdateReq updateReq);


    /**
     * 批量审核
     */
    @RequestMapping(value = "/cmdb/process/batchUpdateInstance", method = RequestMethod.POST)
    Map<String, Object> batchUpdateInstance(@RequestParam("userName") String userName,
                                            @RequestParam("moduleId") String moduleId,
                                            @RequestBody Map<String, Object> batchUpdate);


    /**
     * 获取审核进度
     * @param processId 处理进程ID
     * @return
     */
    @RequestMapping(value = "/cmdb/process/approval/{processId}", method = RequestMethod.GET)
    Map<String, Object> getApprovalProcess(@PathVariable("processId") String processId);

    /**
     * 移除审核进度
     * @param processId 处理进程ID
     * @return
     */
    @RequestMapping(value = "/cmdb/process/remove/approval/{processId}", method = RequestMethod.DELETE)
    Map<String, Object> removeApprovalProcess(@PathVariable("processId") String processId);

    /**
     * 下载失败文件
     * @param processId 处理进程ID
     * @return
     */
    @RequestMapping(value = "/cmdb/process/approval/export/error/{processId}", method = RequestMethod.POST)
    ImportProcess exportErrorFile(@PathVariable("processId") String processId);

}
