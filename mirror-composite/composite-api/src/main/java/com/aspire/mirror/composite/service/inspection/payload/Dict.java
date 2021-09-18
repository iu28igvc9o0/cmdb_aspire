package com.aspire.mirror.composite.service.inspection.payload;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 项目名称:
 * 包: com.aspire.mirror.composite.service.inspection.payload
 * 类名称:
 * 类描述:
 * 创建人: PJX
 * 创建时间: 2019/4/16 14:25
 * 版本: v1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Dict {
    
    /** ID */
    private String dictId;
    
    /** 名称 */
    private String dictCode;
    
    /** 类型 */
    private String colName;
    
    /** 值 */
    private String dictNote;
    
    /** 父ID */
    private String upDict;
    
    /** 描述 */
    private String description;
    
    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date create_date;
    
    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date update_date;
    
    private String pname;
    
}
