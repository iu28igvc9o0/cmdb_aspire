package com.aspire.ums.cmdb.maintenance.payload;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * 项目名称:
 * 包: com.aspire.mirror.composite.service.inspection.payload
 * 类名称:
 * 类描述:
 * 创建人: hangfang
 * 创建时间: 2019/10/25 14:25
 * 版本: v1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConfigDict {

    /**
     * ID
     */
    private String id;

    /**
     * 名称
     */
    private String name;

    /**
     * 类型
     */
    private String type;

    /**
     * 值
     */
    private String value;

    /**
     * 父ID
     */
    private String pid;

    /**
     * 描述
     */
    private String description;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date create_date;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date update_date;

    private String pname;

    private List<ConfigDict> subList;
}
