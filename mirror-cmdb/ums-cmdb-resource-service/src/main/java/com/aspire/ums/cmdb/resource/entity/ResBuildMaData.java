package com.aspire.ums.cmdb.resource.entity;//package com.aspire.ums.cmdb.resource.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResBuildMaData implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private String buildName;

    private String resourcePool;

    private String status;

    @JsonProperty("create_time")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;

    @JsonProperty("build_detail_id")
    private Integer buildDetailId;

    private String configDetail;

    private Integer count;

    private Integer imports;

    private String serverName;

    private String serverType;

    private String unit;
}
