package com.aspire.ums.cmdb.cmic.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @Author :  fanshenquan
 * @CreateDate :  2020/5/12 16:41
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CmdbModelTabsReq extends CmdbModelTabsBase {

    private int pageNo;
    private int pageSize;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") //入参
    private Date createTime;

    public CmdbModelTabsReq(String tabsId) {
        super(tabsId);
    }

    public void updatePageNo() {
        this.pageNo = (pageNo - 1) * pageSize;
    }
}
