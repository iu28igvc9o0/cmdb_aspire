package com.aspire.mirror.composite.service.scada.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * TODO
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.composite.service.scada.payload
 * 类名称:    ScadaBindValueResponse.java
 * 类描述:    TODO
 * 创建人:    JinSu
 * 创建时间:  2019/10/26 17:24
 * 版本:      v1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScadaBindValueResponse {
    private Boolean valueView;

    private Boolean colorView;

    private Boolean isBind;

    private Integer bandType;

    private List<SillVO> sillList;

    private String countType;

    private String id;

    private List<String> alertLevelList;

    private List<Map<String, String>> deviceList;

    private List<Map<String, String>> itemList;

    private Double value;

    private String name;

    private String unit;

    /**
     * 换算类型 1相乘 2相除
     */
    private String conversionType;

    /**
     * 换算值
     */
    private String conversionVal;
}
