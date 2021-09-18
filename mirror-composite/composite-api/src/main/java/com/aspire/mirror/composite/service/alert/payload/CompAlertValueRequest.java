package com.aspire.mirror.composite.service.alert.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * TODO
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.composite.service.alert.payload
 * 类名称:    CompAlertValueRequest.java
 * 类描述:    TODO
 * 创建人:    JinSu
 * 创建时间:  2019/10/25 17:55
 * 版本:      v1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompAlertValueRequest {

    /**
     * 监控项ID
     */
    @NotEmpty
    private List<String> itemIdList;
    /**
     * 设备信息jsonString
     */
    @NotNull
    private String deviceString;

    /**
     * 告警级别列表
     */
    @NotEmpty
    private List<String> alertLevelList;
}
