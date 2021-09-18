package com.aspire.mirror.composite.service.inspection.payload;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 用户详情返回
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.composite.service.inspection.payload
 * 类名称:    CompUserDetailResponse.java
 * 类描述:    用户详情返回
 * 创建人:    JinSu
 * 创建时间:  2018/8/18 16:07
 * 版本:      v1.0
 */
@Data
@NoArgsConstructor
@ToString
public class CompUserDetailResponse implements Serializable {
    /**
     * 用户类型
     */
    private String type;
    /**
     * 创建时间
     */
    private Date created_at;
    /**
     * 修改时间
     */
    private Date updated_at;
    /**
     * 用户
     */
    private String username;
    /**
     * 项目
     */
    private List<String> projects;
    /**
     * 成员邮箱
     */
    private String email;

    /**
     * 成员名称
     */
    private String name;

    /**
     * 成员手机号码
     */
    private String mobile;
}
