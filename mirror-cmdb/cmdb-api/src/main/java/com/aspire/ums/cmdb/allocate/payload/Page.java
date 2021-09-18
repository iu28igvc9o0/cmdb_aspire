
package com.aspire.ums.cmdb.allocate.payload;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedList;
import java.util.List;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: Page
 * Author:   zhu.juwang
 * Date:     2019/3/20 9:22
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Page<T> {
    private Integer pageNo;
    private Integer pageSize;
    private Integer total;
    private Integer pages;
    private List<T> dataList;

    public void setDataList(List<T> dataList) {
        if (dataList != null) {
            this.total = dataList.size();
            if (pageSize != null && pageNo != null) {
                this.pages = getTotal() % getPageSize() == 0 ? getTotal() / getPageSize() : (getTotal() / getPageSize() + 1);
                List<T> cpList = new LinkedList<>();
                for (int i = (getPageNo() - 1) * getPageSize(); i < getPageNo() * getPageSize(); i ++) {
                    if (dataList.size() > i) {
                        cpList.add(dataList.get(i));
                    }
                }
                this.dataList = cpList;
            } else {
                this.dataList = dataList;
            }
        } else {
            this.total = 0;
            this.pages = 0;
        }
    }

    public Page(Integer pageNo, Integer pageSize) {
        this.pageNo = pageNo;
        this.pageSize = pageSize;
    }
}
