package com.aspire.ums.cmdb.automate.payload.easyops;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author fanwenhui
 * @date 2020-08-25 10:17
 * @description 主机模型-cpu入参
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AutomateHostDataCpuDTO implements Serializable {

    @JsonIgnore
    @JSONField(serialize = false)
    private static final long serialVersionUID = 7571424580287084857L;

    @JsonProperty("brand")
    @JSONField(name = "brand")
    private String brand;

    @JsonProperty("architecture")
    @JSONField(name = "architecture")
    private String architecture;

    @JsonProperty("hz")
    @JSONField(name = "hz")
    private String hz;

    @JsonProperty("cpu_pieces")
    @JSONField(name = "cpu_pieces")
    private String cpuPieces;

    @JsonProperty("physical_cores")
    @JSONField(name = "physical_cores")
    private String physicalCores;

    @JsonProperty("logical_cores")
    @JSONField(name = "logical_cores")
    private String logicalCores;

}
