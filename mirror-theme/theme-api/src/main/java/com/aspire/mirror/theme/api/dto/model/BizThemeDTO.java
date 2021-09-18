package com.aspire.mirror.theme.api.dto.model;

import com.aspire.mirror.common.auth.GeneralAuthConstraintsAware;
import lombok.Data;
import lombok.ToString;

import java.util.Date;
import java.util.List;

/**
 * 主题业务层实体
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.theme.api.dto.model
 * 类名称:    BizThemeDTO.java
 * 类描述:    主题业务层实体
 * 创建人:    JinSu
 * 创建时间:  2018/10/23 17:14
 * 版本:      v1.0
 */
@Data
@ToString
public class BizThemeDTO  extends GeneralAuthConstraintsAware {
    /**
     * 主题ID
     */
    private String themeId;

    /**
     * 主题编码
     */
    private String themeCode;
    /**
     * 关联对象类型
     * 1-设备ID
     * 2-业务系统
     */
    private String objectType;

    /**
     * object_id
     */
    private String objectId;

    /**
     * es索引名
     */
    private String indexName;

    /**
     * 数据类型0：数字1：字符串
     */
    private String valueType;

    /**
     * 上报类型0：接口接入1：日志接入
     */
    private String upType;

    /**
     * 上报状态0：成功1：失败
     */
    private String upStatus;

    /**
     * 最近上报值
     */
    private String lastUpValue;

    /**
     * 最近上报时间
     */
    private Date lastUpTime;

    /**
     * 上报开关0：开启1：关闭
     */
    private String upSwitch;

    /**
     * 状态0：正式1：临时
     */

    private String status;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 最近失败上报时间
     */
    private Date lastFailTime;

    /**  */
    private Integer interval;

    /**
     * 维度id集合
     */
    private List<BizThemeDimDTO> dimList;

    /**
     * 主题名称
     */
    private String themeName;

    /**
     * 维度id集合
     */
    private String dimIds;

    /**
     * 日志正则表达式
     */
    private String logReg;

    /**
     * 日志内容
     */
    private String logContent;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 删除标志
     */
    private String deleteFlag;

    /**
     * 主题主键维度编码
     */
    private String themeKey;
}
