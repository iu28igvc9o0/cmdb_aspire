package com.aspire.ums.cmdb.ipCollect.payload.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.apache.commons.lang.StringUtils;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @Author: fanshenquan
 * @Datetime: 2020/5/20 10:03
 */
@Data
public class CmdbVipCollectRequest extends CmdbVipCollectEntity implements Serializable {

    private static final long serialVersionUID = 5832551670680506L;

    private int pageNo;
    private int pageSize;
    private List<String> vipList;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") //入参
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") //入参
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endTime;

    // public void setVipList(String vipList) {
    //     this.vipList = Arrays.asList(vipList.split(","));
    // }

    public void updatePageNo() {
        this.pageNo = (pageNo - 1) * pageSize;
        this.vipList = StringUtils.isEmpty(super.getVip()) ? null : Arrays.asList(super.getVip().split(","));
    }
}
