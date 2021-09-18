package com.aspire.ums.cmdb.systemManager.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 项目名称:
 * 包: com.aspire.ums.cmdb.systemManager.entity
 * 类名称:
 * 类描述:
 * 创建人: PJX
 * 创建时间: 2019/5/21 15:38
 * 版本: v1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BizSystemReq {
    //页数
    private int pageNum;
    //开始的下标
    private int startPageNum;
    //条数
    private int pageSize;
    
    private String pid;
    
    private String bizName;
    
    private String isdisable;
    
    public void setStartPageNum() {
        if (0 < this.pageNum) {
            this.startPageNum = (this.pageNum - 1) * this.pageSize;
        } else {
            this.startPageNum = 0;
        }
    }
}
