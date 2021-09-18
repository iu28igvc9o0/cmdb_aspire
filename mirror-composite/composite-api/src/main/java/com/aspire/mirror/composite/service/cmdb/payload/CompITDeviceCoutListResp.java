package com.aspire.mirror.composite.service.cmdb.payload;

import com.aspire.ums.cmdb.assessment.payload.CmdbItDeviceCount;
import com.aspire.ums.cmdb.assessment.payload.CmdbProduceAssessment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: CompITDeviceCoutListResponse
 * Author:   hangfang
 * Date:     2019/6/28
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompITDeviceCoutListResp {


    private Integer total;

    private List<Map<String, Map<String, CmdbItDeviceCount>>> data;
}
