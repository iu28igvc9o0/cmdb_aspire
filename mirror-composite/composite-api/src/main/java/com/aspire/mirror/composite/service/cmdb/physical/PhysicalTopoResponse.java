package com.aspire.mirror.composite.service.cmdb.physical;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 物理拓扑返回数据
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.composite.service.cmdb.physical
 * 类名称:    PhysicalTopoResponse.java
 * 类描述:    物理拓扑返回数据
 * 创建人:    JinSu
 * 创建时间:  2019/9/20 14:14
 * 版本:      v1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PhysicalTopoResponse {
    /**
     * 返回码
     */
    private Integer code;
    /**
     * 返回描述
     */
    private String msg;
    /**
     * 返回topo的xml信息
     */
    private String data;
}
