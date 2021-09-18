package com.migu.tsg.microservice.atomicservice.composite.service.rbac.payload;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@JsonInclude(Include.NON_NULL)
public class AuthProfileResponse {

    private String company;


    //"city": "",
    private String city;

    private String industry;

    //"position": "",
    private String position;

    @JsonProperty("informed_way")
    private String informedWay;

    private String realname;

    private Integer id;
    @JsonProperty("last_login")
    private String lastLogin;

    private String username;

    private String email;

    @JsonProperty("prefered_language")
    private String preferedLanguage;

    @JsonProperty("is_active")
    private Boolean isActive;

    @JsonProperty("is_trialuser")
    private Boolean isTrialuser;

    @JsonProperty("password_is_temp")
    private Boolean passwordIsTemp;

    @JsonProperty("joined_at")
    private String joinedTime;

    private String currency;

    @JsonProperty("account_type")
    private Integer accountType;

    @JsonProperty("is_available")
    private Boolean isAvailable;

    @JsonProperty("user_level")
    private String userLevel;

    //"company_website": "",
    @JsonProperty("company_website")
    private String companyWebsite;

    @JsonProperty("company_size")
    private Integer companySize;

    private Long mobile;

    @JsonProperty("feature_flags")
    private Integer featureFlags;

    @JsonProperty("logo_file")
    private String logoFile;

    //"app_created": null,
    @JsonProperty("app_created")
    private String appCreate;

    @JsonProperty("repo_created")
    private String repoCreateTime;

    @JsonProperty("api_revoked")
    private String apiRevokedTime;

    //"reference_code": null,
    @JsonProperty("reference_code")
    private String referenceCode;

    private String type;

    @JsonProperty("service_region_flags")
    private Integer serviceRegionFlags;

    //"apply_mode": "",
    @JsonProperty("apply_mode")
    private String applyMode;

    //"apply_service": "",
    @JsonProperty("apply_service")
    private String applyService;

    private String source;

    @JsonProperty("password_is_empty")
    private Boolean passIsEmpty;

    /**
     * 项目key
     * 例如：B1(包一),B2(包二)
     */
    private List<String> projects;
    /**
     * 项目values
     * 例如：B1(包一),B2(包二)
     */
    private List<String> projectNames;

}
