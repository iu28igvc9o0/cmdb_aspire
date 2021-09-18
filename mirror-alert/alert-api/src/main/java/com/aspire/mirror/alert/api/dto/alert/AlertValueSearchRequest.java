package com.aspire.mirror.alert.api.dto.alert;

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
 * 包:        com.aspire.mirror.alert.api.dto
 * 类名称:    AlertValueSearchRequest.java
 * 类描述:    TODO
 * 创建人:    JinSu
 * 创建时间:  2019/10/25 18:27
 * 版本:      v1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlertValueSearchRequest {
    @NotNull
    private Map<String, List<String>> ipMap;

    @NotNull
    private List<String> itemIdList;

    @NotNull
    private List<String> alertLevel;
}
