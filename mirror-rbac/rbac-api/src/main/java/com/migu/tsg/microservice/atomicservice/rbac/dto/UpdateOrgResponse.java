package com.migu.tsg.microservice.atomicservice.rbac.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.migu.tsg.microservice.atomicservice.rbac.dto.model.MemberDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 项目名称: rbac-api <br>
 * 包: com.migu.tsg.microservice.atomicservice.rbac.dto <br>
 * 类名称: UpdateOrgResponse.java <br>
 * 类描述: 修改公司信息的响应对象 <br>
 * 创建人: WangSheng <br>
 * 创建时间: 2017年8月16日上午9:47:00 <br>
 * 版本: v1.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateOrgResponse {

    /**
     * 创建时间
     */
    @JsonProperty("created_at")
    private String createdAt;

    /**
     * logo头像地址路径
     */
    @JsonProperty("logo_file")
    private String logoFile;

    /**
     * 组织名称(空间名称/根账号名称)
     */
    private String name;

    /**
     * 组织下的成员集合
     */
    private List<MemberDTO> members;

    /**
     * 组织ID
     */
    private Integer id;

    /**
     * 更新时间
     */
    @JsonProperty("updated_at")
    private String updatedAt;

    /**
     * 公司名称
     */
    private String company;
    
    /**
     * 公司邮箱
     */
    private String email;

    /**
     * 通用货币
     */
    private String currency;

    /**
     *
     */
    @JsonProperty("feature_flags")
    private Integer featureFlags;

}
