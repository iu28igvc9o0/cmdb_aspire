package com.aspire.mirror.ops.api.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 敏感指令级别
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.ops.api.domain
 * 类名称:    SensitiveLevel
 * 类描述:    敏感指令级别
 * 创建人:    JinSu
 * 创建时间:  2020/9/17 15:40
 * 版本:      v1.0
 */
@Data
public class SensitiveLevel {
    private Long id;
    /**
     * 分级名
     */
    private String name;
    /**
     * 分级描述
     */
    private String levelDesc;
    /**
     * 执行角色列表
     */
    private List<String> execRoleList;
    /**
     * 审核角色列表
     */
    private List<String> auditRoleList;

    /**
     * 创建人
     */
    private String creater;
    /**
     * 创建时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    /**
     * 修改人
     */
    private String updater;
    /**
     * 修改时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    /**
     * 排序号
     */
    private Integer orderNum;

    @JsonIgnore
    public String getExecRoleListText() {
        return execRoleList == null ? null : StringUtils.join(execRoleList, ",");
    }


    public List<String> getExecRoleList(){
        return execRoleList == null ? new ArrayList<>() : execRoleList;
    }

    public void setExecRoleListText(String execRoleListText) {
        if (StringUtils.isBlank(execRoleListText)) {
            this.execRoleList = new ArrayList<>();
            return;
        }
        List<String> roleIdList
                = Arrays.asList(execRoleListText.split(",")).stream().collect(Collectors.toList());
        this.execRoleList = roleIdList;
    }

    @JsonIgnore
    public String getAuditRoleListText() {
        return auditRoleList == null ? null : StringUtils.join(auditRoleList, ",");
    }


    public List<String> getAuditRoleList(){
        return auditRoleList == null ? new ArrayList<>() : auditRoleList;
    }

    public void setAuditRoleListText(String auditRoleListText) {
        if (StringUtils.isBlank(auditRoleListText)) {
            this.auditRoleList = new ArrayList<>();
            return;
        }
        List<String> roleIdList
                = Arrays.asList(auditRoleListText.split(",")).stream().collect(Collectors.toList());
        this.auditRoleList = roleIdList;
    }
}
