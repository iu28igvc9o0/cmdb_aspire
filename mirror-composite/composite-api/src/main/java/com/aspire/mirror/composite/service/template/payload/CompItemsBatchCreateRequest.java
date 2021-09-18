package com.aspire.mirror.composite.service.template.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.validator.constraints.NotEmpty;

import java.io.Serializable;
import java.util.List;

/**
 * 指标批量创建对象
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.composite.service.template.payload
 * 类名称:    CompItemsBatchCreateRequest.java
 * 类描述:    指标批量创建对象
 * 创建人:    JinSu
 * 创建时间:  2018/8/8 15:05
 * 版本:      v1.0
 */
@NoArgsConstructor
@Data
@ToString
public class CompItemsBatchCreateRequest implements Serializable {
    private static final long serialVersionUID = 5550829551073639823L;
    /**
     * 监控项列表
     */
    @JsonProperty("list_item")
    @NotEmpty
    private List<CompItemsCreateRequest> listItem;
}
