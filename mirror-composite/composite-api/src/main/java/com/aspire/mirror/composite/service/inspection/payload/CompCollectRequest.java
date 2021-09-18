package com.aspire.mirror.composite.service.inspection.payload;

import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: CompCollectRequest
 * Author:   zhu.juwang
 * Date:     2019/3/13 17:37
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompCollectRequest {
    private List<CompCollectDetail> insertCollect;
    private List<String> deleteCollect;
    private String visitType;
    private JSONObject visitRequest;
}
