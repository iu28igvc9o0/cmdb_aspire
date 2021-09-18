package com.aspire.mirror.theme.server.dao.po;

import com.aspire.mirror.common.auth.GeneralAuthConstraintsAware;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 业务主题持久对象类
 * <p>
 * 项目名称:  mirror平台
 * 包:       com.aspire.mirror.theme.server.dao.po
 * 类名称:    BizTheme.java
 * 类描述:    业务主题持久对象类，定义与表字段对应的属性
 * 创建人:    JinSu
 * 创建时间:  2018-010-22 12:14:48
 */
@Data
@NoArgsConstructor
public class BizTheme extends GeneralAuthConstraintsAware {
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
    private String dimIds;

    /**
     * 主题名称
     */
    private String themeName;

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

}
