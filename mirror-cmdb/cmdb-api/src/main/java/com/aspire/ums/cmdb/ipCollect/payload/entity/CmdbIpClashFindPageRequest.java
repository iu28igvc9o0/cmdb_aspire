package com.aspire.ums.cmdb.ipCollect.payload.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.apache.commons.lang.StringUtils;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @Author: fanshenquan
 * @Datetime: 2020/5/27 10:41
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class CmdbIpClashFindPageRequest extends CmdbIpClashFindPageResponse implements Serializable {
    private static final long serialVersionUID = 8072020389633650364L;
    private int pageNo;
    private int pageSize;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") //入参
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") //入参
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") //入参
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateStartTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") //入参
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateEndTime;

    private List<String> ipList;
    // 来源
    private String collectType;
    // 数据来源 0-系统比对，1-复核比对
    private String checkType;

    public void updatePageNo() {
        this.pageNo = (pageNo - 1) * pageSize;
        this.ipList = StringUtils.isEmpty(super.getClashIp()) ? null : Arrays.asList(super.getClashIp().split(","));
        this.checkType = "1".equals(checkType) ? "1" : "0";
    }

}
