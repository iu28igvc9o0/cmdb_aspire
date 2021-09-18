package com.aspire.mirror.elasticsearch.api.service.cmdb;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: ICmdbApprovalService
 * Author:   hangfang
 * Date:     2019/9/17
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@RequestMapping("/cmdb/approval")
public interface ICmdbApprovalService {

    @PostMapping("/insert")
    void insert(@RequestBody List<Map<String, Object>> approvals);

    @PostMapping("/list")
    Map<String, Object> query(@RequestBody Map<String, Object> query);

    @PostMapping("/getApproval")
    int getApproval(@RequestBody Map<String, Object> query);

}
