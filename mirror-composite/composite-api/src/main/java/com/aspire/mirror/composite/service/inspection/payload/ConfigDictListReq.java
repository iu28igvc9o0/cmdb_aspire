package com.aspire.mirror.composite.service.inspection.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 项目名称:
 * 包: com.aspire.mirror.composite.service.inspection.payload
 * 类名称:
 * 类描述:
 * 创建人: PJX
 * 创建时间: 2019/4/16 14:28
 * 版本: v1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConfigDictListReq {
    
    //页数
    private int pageNum;
    //开始的下标
    private int startPageNum;
    //条数
    private int pageSize;
    
    private String pcode;
    
    private String dictCode;
    
    private String dictNote;
    
    private String colName;
    
}
