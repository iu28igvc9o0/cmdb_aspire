package com.aspire.ums.cmdb.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @Author: huanggongrui
 * @Description:
 * @Date: create in 2020/7/22 11:14
 */
@Data
public class UserData {

    @JsonProperty("userName")
    private String userName;
    @JsonProperty("fullName")
    private String fullName;
    @JsonProperty("email")
    private String email;
    @JsonProperty("gender")
    private Integer gender;
    @JsonProperty("birthday")
    private Long birthday;
    @JsonProperty("starTime")
    private Long starTime;
    @JsonProperty("endTime")
    private Long endTime;
    @JsonProperty("password")
    private String password;
    @JsonProperty("status")
    private Integer status;
    @JsonProperty("employeeType")
    private Integer employeeType;
    @JsonProperty("avatar")
    private String avatar;
    @JsonProperty("changeFlag")
    private Integer changeFlag;
    @JsonProperty("zhcnName")
    public String zhcnName;
    @JsonProperty("mobile")
    public String mobile;
    @JsonProperty("employeeNumber")
    public String employeeNumber;
    @JsonProperty("telephone")
    public String telephone;
    @JsonProperty("positionLevel")
    public String positionLevel;
    @JsonProperty("organizationId")
    public String organizationId;
    @JsonProperty("displayOrder")
    public String displayOrder;
    @JsonProperty("ou")
    public String ou;
    @JsonProperty("passwordEncrypted")
    public Boolean passwordEncrypted;
}
