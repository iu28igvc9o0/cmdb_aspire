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
 * @date 2020-08-25 10:27
 * @description 主机模型-服务信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AutomateHostDataServiceDTO implements Serializable {

    @JsonIgnore
    @JSONField(serialize = false)
    private static final long serialVersionUID = 7323350095153093682L;

    @JsonProperty("name")
    @JSONField(name = "name")
    private String name;

    @JsonProperty("cwd")
    @JSONField(name = "cwd")
    private String cwd;

    @JsonProperty("exe")
    @JSONField(name = "exe")
    private String exe;

    @JsonProperty("listening_ip")
    @JSONField(name = "listening_ip")
    private String listeningIp;

    @JsonProperty("listening_port")
    @JSONField(name = "listening_port")
    private String listeningPort;

    @JsonProperty("pname")
    @JSONField(name = "pname")
    private String pname;

    @JsonProperty("username")
    @JSONField(name = "username")
    private String username;

}
