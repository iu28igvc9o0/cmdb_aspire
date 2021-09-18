package com.aspire.ums.cmdb.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Data;

/**
 * @Author: huanggongrui
 * @Description: 科管部用户同步响应实体
 * @Date: create in 2020/7/21 18:36
 */
@Data
public class UserVo {
    @JsonProperty("internalPerson")
    private List<UserData> internalPerson;
    @JsonProperty("guest")
    private List<UserData> guest;
    @JsonProperty("externalPerson")
    private List<UserData> externalPerson;
}
