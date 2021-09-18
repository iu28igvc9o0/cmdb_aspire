package com.aspire.ums.cmdb.sync.payload;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

import org.springframework.format.annotation.DateTimeFormat;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 字典表.
 *
 * @author jiangxuwen
 * @date 2020/5/13 10:57
 */
@Data
public class ConfigDictDTO implements Serializable {

    private static final long serialVersionUID = 7824512669769126161L;

    private String id;

    private String label;

    private String value;

    private String type;

    private String description;

    private String parentId;

    private String deleteFlag;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateDate;
}
