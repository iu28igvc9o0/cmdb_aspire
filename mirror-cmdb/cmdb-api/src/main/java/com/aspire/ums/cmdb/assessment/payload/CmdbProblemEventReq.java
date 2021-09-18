package com.aspire.ums.cmdb.assessment.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 项目名称:
 * 包: com.aspire.ums.cmdb.faultEvent.payload
 * 类名称:
 * 类描述: 故障事件信息
 * 创建人: PJX
 * 创建时间: 2019/6/25 20:13
 * 版本: v1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CmdbProblemEventReq {
    //页数
    private int pageNum;
    //开始的下标
    private int startPageNum;
    //条数
    private int pageSize;
    
    private String province;
    
    private String createUsername;
    /**
     * 所属季度
     */
    private String quarter;
    
    public void setStartPageNum() {
        if (0 < this.pageNum) {
            this.startPageNum = (this.pageNum - 1) * this.pageSize;
        } else {
            this.startPageNum = 0;
        }
    }
}
