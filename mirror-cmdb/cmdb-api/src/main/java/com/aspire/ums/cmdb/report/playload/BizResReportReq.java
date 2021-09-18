package com.aspire.ums.cmdb.report.playload;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 项目名称:
 * 包: com.aspire.ums.cmdb.report.playload
 * 类名称:
 * 类描述:
 * 创建人: PJX
 * 创建时间: 2019/6/18 10:11
 * 版本: v1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BizResReportReq {
    //页数
    private int pageNum;
    //开始的下标
    private int startPageNum;
    //条数
    private int pageSize;

    private String bizSystem;
    
    private String idcType;
    
    private String department1;
    
    private String department2;
    
    private String deviceType;
    
    private String createTime1;
    
    private String createTime2;
    
    public void setStartPageNum() {
        if (0 < this.pageNum) {
            this.startPageNum = (this.pageNum - 1) * this.pageSize;
        } else {
            this.startPageNum = 0;
        }
    }
}
