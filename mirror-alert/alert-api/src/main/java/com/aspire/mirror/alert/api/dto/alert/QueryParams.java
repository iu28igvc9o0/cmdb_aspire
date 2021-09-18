package com.aspire.mirror.alert.api.dto.alert;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * @BelongsProject: mirror-alert
 * @BelongsPackage: com.aspire.mirror.alert.api.v2.dto
 * @Author: baiwenping
 * @CreateTime: 2020-02-29 00:19
 * @Description: ${Description}
 */
@Data
public class QueryParams {
    private List<QueryField> list;
    @JsonProperty("page_no")
    private Integer pageNum;
    @JsonProperty("page_size")
    private Integer pageSize;
    @JsonProperty("sort_name")
    private String sortName;

    @JsonProperty("filter_scene_id")
    private String filterSceneId;

}
