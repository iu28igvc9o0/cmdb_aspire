package com.aspire.ums.cmdb.maintenance.payload;


import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Dscription: 维保厂商查询
 * @Author: PJX
 * @Version: V1.0
 */
@NoArgsConstructor
@Data
public class ProduceInfoRequest {

    // 分页页标
    private int pageSize;

    // 每页数量
    private int pageNum;

    //开始的下标
    private int startPageNum;

    private String produce;

    private String type;

    private String name;

    private String phone;

    private String email;

    private String orderBy;

    public void setStartPageNum() {
        if (0 < this.pageNum) {
            this.startPageNum = (this.pageNum - 1) * this.pageSize;
        } else {
            this.startPageNum = 0;
        }
    }


}
