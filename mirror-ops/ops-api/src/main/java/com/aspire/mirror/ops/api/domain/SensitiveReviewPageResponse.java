package com.aspire.mirror.ops.api.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 敏感指令审核分页查询返回
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.ops.api.domain
 * 类名称:    SensitiveReviewPageResponse.java
 * 类描述:    敏感指令分页查询返回
 * 创建人:    JinSu
 * 创建时间:  2020/2/18 10:00
 * 版本:      v1.0
 */
@Data
public class SensitiveReviewPageResponse extends SensitiveReview {
    @JsonProperty("content_type")
    private Integer contentType;                // 脚本内容类型    1 sh 2 bat 3 python  参考ScriptContentTypeEnum



    @JsonProperty("instance_classify")
    private Integer instanceClassify;

    @JsonProperty("instance_name")
    private String instanceName;

    @JsonProperty("pipeline_id")
    private String pipelineId;

//    @JsonProperty("deal_type")
//    private Integer dealType;

}
