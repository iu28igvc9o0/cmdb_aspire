package com.aspire.mirror.composite.service.alert.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * TODO
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.composite.service.alert.payload
 * 类名称:    CompMonitorRequest.java
 * 类描述:    TODO
 * 创建人:    JinSu
 * 创建时间:  2019/10/24 19:25
 * 版本:      v1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompMonitorRequest {

    /**
     * item列表
     */
    @NotNull
    private List<String> itemKeyList;

    /**
     * 设备信息jsonString
     */
    @NotNull
    private String deviceString;

    /**
     * 统计方式
     */
    @NotNull
    private String countType;
}
