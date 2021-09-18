package com.aspire.mirror.composite.service.inspection.payload;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * 设备详情返回
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.composite.service.inspection.payload
 * 类名称:    CompDeviceDetailResponse.java
 * 类描述:    设备详情返回
 * 创建人:    JinSu
 * 创建时间:  2018/8/15 18:56
 * 版本:      v1.0
 */
@Data
@NoArgsConstructor
@ToString
public class CompDeviceListResponse implements Serializable {

    private static final long serialVersionUID = -59059607056103002L;

    private List<CompDeviceDetailResponse> dataList;
    private int total;
}
