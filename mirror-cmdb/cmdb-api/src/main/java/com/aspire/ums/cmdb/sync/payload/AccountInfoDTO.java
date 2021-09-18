package com.aspire.ums.cmdb.sync.payload;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Lists;

import lombok.Data;

/**
 * xxxxxx
 *
 * @author jiangxuwen
 * @date 2020/6/10 15:58
 */
@Data
public class AccountInfoDTO {

    @JsonProperty("msgSerial")
    private Integer msgSerial;

    private String uuid;

    private String timestamp;

    @JsonProperty("data")
    private AccountInfoData data;

    @Data
    public static class AccountInfoData {

        @JsonProperty("removeList")
        private List<AccountDetailInfo> removeList = Lists.newArrayList();

        @JsonProperty("addList")
        private List<AccountDetailInfo> addList = Lists.newArrayList();

        @JsonProperty("updateList")
        private List<AccountDetailInfo> updateList = Lists.newArrayList();

        @Data
        public static class AccountDetailInfo {

            @JsonProperty("updateDate")
            private String updateDate;

            @JsonProperty("loginName")
            private String loginName;

            @JsonProperty("name")
            private String name;

            @JsonProperty("mobile")
            private String mobile;

            @JsonProperty("company")
            private String company;

            @JsonProperty("id")
            private String id;

            @JsonProperty("email")
            private String email;

            @JsonProperty("remarks")
            private String remarks;

            @JsonProperty("createDate")
            private String createDate;
        }
    }
}
