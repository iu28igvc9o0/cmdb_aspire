package com.aspire.ums.cdn.clientservice.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* 描述：
* @author
* @date 2019-05-13 18:39:38
*/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CmdbCode {

    /**
     * ID
     */
    private String codeId;
    /**
     * 字段编码
     */
    private String filedCode;
    /**
     * 字段显示名称
     */
    private String filedName;
    /**
     * 字段分组
     */
    private String codeGroup;
    /**
     * 控件类型ID
    private CmdbControlType controlType;
     */
    /**
     * 绑定数据源类型
     */
    private String bindSourceType;
    /**
     * 绑定数据源
     */
    private String bindSource;
    /**
     * 关联的码表ID
     */
    private CmdbCode refCode;
    /**
     * 字段长度
     */
    private Integer codeLength;
    /**
     * 字段提示信息
     */
    private String codeTip;
    /**
     * 是否非空
     */
    private String isRequire;
    /**
     * 是否内置
     */
    private String isBuiltIn;
    /**
     * 是否支持数据采集
     */
    private String isCollect;
    /**
     * 采集数据来源 zabbix / promethus
     */
    private String collectSource;
    /**
     * 映射采集来源字段名
     */
    private String collectMapperKey;
    /**
     * 是否删除
     */
    private String isDelete;
    /**
     * 排序
     */
    private Integer sortIndex;
}