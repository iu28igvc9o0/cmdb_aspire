package com.aspire.mirror.template.api.dto.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * 动态模型批量新增
 * <p>
 * 项目名称:  mirror平台
 * 类名称:    DynamicModelBatchCreateVO
 * 类描述:    动态模型批量新增
 * 创建人:    JinSu
 * 创建时间:  2020/11/6 10:53
 * 版本:      v1.0
 */
@Data
public class DynamicModelBatchCreateVO {
    @JsonProperty("index_type")
    private String indexType;
    /**
     * 动态阈值模型列表
     */
    @JsonProperty("dynamic_model_list")
    List<StandardDynamicModelExt> standardDynamicModelExtList;
}
