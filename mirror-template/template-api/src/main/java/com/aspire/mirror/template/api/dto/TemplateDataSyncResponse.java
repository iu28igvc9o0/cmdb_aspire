package com.aspire.mirror.template.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 同步数据返回
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.template.api.dto
 * 类名称:    TemplateDataSyncResponse.java
 * 类描述:    同步数据返回
 * 创建人:    JinSu
 * 创建时间:  2018/9/10 17:19
 * 版本:      v1.0
 */
@Data
@NoArgsConstructor
public class TemplateDataSyncResponse<T>  implements Serializable {
    private static final long serialVersionUID = 4179395986580204151L;
    @JsonProperty("lastSyncSeq")
    private Integer lastSyncSeq;

    @JsonProperty("dataList")
    private List<T> itemDataList;

}
