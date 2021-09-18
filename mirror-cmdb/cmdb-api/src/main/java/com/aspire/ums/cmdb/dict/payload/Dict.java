package com.aspire.ums.cmdb.dict.payload;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 项目名称: 包: com.aspire.ums.cmdb.dict.entity 类名称: 类描述: 创建人: PJX 创建时间: 2019/4/15 09:49 版本: v1.0
 */
@Data
public class Dict implements Serializable {

    private static final long serialVersionUID = -6892397771689317664L;

    /** 标签ID */
    private String dictId;

    /** 标签名称 */
    private String dictCode;

    /** 标签类型 */
    private String colName;

    /** 标签值 */
    private String dictNote;

    /** 父标签ID */
    private String upDict;

    /** 描述 */
    private String description;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date create_date;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date update_date;

    /** 父标签名 */
    private String pname;

    /**
     * 字典值排序
     */
    private Integer sortIndex;

    /** 是否需同步到网管,网管同步过来的时候 需设置为false,防止循环双向同步. */
    @JSONField(serialize = false)
    @JsonIgnore
    private boolean syncFlag = true;
}
