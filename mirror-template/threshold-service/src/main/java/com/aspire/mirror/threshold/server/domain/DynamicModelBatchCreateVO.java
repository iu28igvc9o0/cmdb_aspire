package com.aspire.mirror.threshold.server.domain;

import com.aspire.mirror.threshold.server.domain.StandardDynamicModelExt;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * TODO
 * <p>
 * 项目名称:  mirror平台
 * 类名称:    DynamicModelBatchCreateVO
 * 类描述:    TODO
 * 创建人:    JinSu
 * 创建时间:  2020/11/6 14:39
 * 版本:      v1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DynamicModelBatchCreateVO {
    @JsonProperty("index_type")
    private String indexType;
    /**
     * 动态阈值模型列表
     */
    @JsonProperty("dynamic_model_list")
    List<StandardDynamicModelExt> standardDynamicModelExtList;
}
