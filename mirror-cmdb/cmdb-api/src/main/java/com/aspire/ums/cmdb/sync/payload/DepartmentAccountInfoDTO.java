package com.aspire.ums.cmdb.sync.payload;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * xxxxxx
 *
 * @author jiangxuwen
 * @date 2020/6/10 18:23
 */
@Data
public class DepartmentAccountInfoDTO implements Serializable {

    private static final long serialVersionUID = 6549128797311790964L;

    @JsonProperty("msgSerial")
    private Integer msgSerial;

    private String uuid;

    private String timestamp;

    @JsonProperty("data")
    private DepartmentAccountData data;

    @Data
    public static class DepartmentAccountData {

        @JsonProperty("orgUserList")
        private List<DepartmentAccountDetail> orgUserList;

        @Data
        public static class DepartmentAccountDetail {

            @JsonProperty("contactId")
            private String contactId;

            @JsonProperty("isValid")
            private String isValid;

            @JsonProperty("sex")
            private String sex;

            @JsonProperty("createdate")
            private String createDate;

            @JsonProperty("orgList")
            private List<OrgInfo> orgList;

            @JsonProperty("portalUserId")
            private String portalUserId;

            @JsonProperty("orgFullName")
            private String orgFullName;

            @JsonProperty("empNo")
            private String empNo;

            @JsonProperty("userId")
            private Long userId;

            @JsonProperty("orgId")
            private Long orgId;

            @JsonProperty("infoStartTime")
            private String infoStartTime;

            @JsonProperty("photoUrl")
            private String photoUrl;

            @JsonProperty("companyEmail")
            private String companyEmail;

            @JsonProperty("lockStatus")
            private String lockStatus;

            @JsonProperty("mobilePhone")
            private String mobilePhone;

            @JsonProperty("updatedate")
            private String updateDate;

            @JsonProperty("userCategory")
            private Integer userCategory;

            @JsonProperty("responsible")
            private String responsible;

            @JsonProperty("positionDesc")
            private String positionDesc;

            @JsonProperty("position")
            private String position;

            @JsonProperty("infoStopTime")
            private String infoStopTime;

            @JsonProperty("email")
            private String email;

            @JsonProperty("portalUserName")
            private String portalUserName;

            @Data
            public static class OrgInfo {

                @JsonProperty("byAppid")
                private String byAppid;

                @JsonProperty("operateFlag")
                private Integer operateFlag;

                @JsonProperty("orgCategory")
                private Integer orgCategory;

                @JsonProperty("oaType")
                private Integer oaType;

                @JsonProperty("contactId")
                private Long contactId;

                @JsonProperty("operateType")
                private Integer operateType;

                @JsonProperty("htxlFlag")
                private Integer htxlFlag;

                @JsonProperty("userId")
                private Long userId;

                @JsonProperty("orgId")
                private Long orgId;

                @JsonProperty("oaStatus")
                private Integer oaStatus;

                @JsonProperty("orgPositionName")
                private String orgPositionName;

                @JsonProperty("responsible")
                private Integer responsible;

                @JsonProperty("orgPositionId")
                private Integer orgPositionId;

                @JsonProperty("byType")
                private Integer byType;
            }
        }
    }
}
