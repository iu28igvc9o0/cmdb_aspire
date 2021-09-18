package com.aspire.mirror.log.api.dto;

import com.aspire.mirror.log.api.dto.model.BucketInfoDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 业务日志直方图response
 * description: Copyright: Copyright (c) 2017 Company: 咪咕文化 tsg
 * package: com.migu.tsg.microservice.monitor.log.dto
 * title: BizLogChartListResponse.java
 * version: V1.0.0.0
 * author: sunke
 * date: 2017/11/23 16:07
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BizLogChartListResponse {

    //返回日志按时间维度的统计
    private List<BucketInfoDTO> buckets;
}