package com.aspire.mirror.alert.server.vo.derive;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 描述：
 *
 * @author
 * @date 2019-08-14 19:35:33
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlertDeriveVo {

    /**
     * 告警屏蔽ID
     */
    private Long id;
    /**
     * 规则名称
     */
    private String name;
    /**
     * 规则描述
     */
    private String description;
    /**
     * 启用状态，0-停用，1-启用
     */
    private String status;
    /**
     * 生效时间范围
     */
    private String effectiveDateFrom;
    /**
     * 生效时间范围
     */
    private String effectiveDateTo;
    /**
     * 生效时间
     */
    private Date effectiveDate;
    /**
     * 失效时间
     */
    private Date expireDate;
    /**
     * 关联时间窗(m)
     */
    private Integer deriveActiveTimeout;
    /**
     * 告警阈值个数
     */
    private Integer alertThreshold;
    /**
     * 维护用户
     */
    private String operateUser;
    /**
     * 过滤条件
     */
    private String optionCondition;
    /**
     * 告警生成设置
     */
    private String alertSetting;
    /**
     * 创建人
     */
    private String creater;
    /**
     * 创建时间
     */
    private Date createdAt;
    /**
     * 修改人
     */
    private String editer;
    /**
     * 修改时间
     */
    private Date updatedAt;
    private int begin;
    private int pageSize;
}