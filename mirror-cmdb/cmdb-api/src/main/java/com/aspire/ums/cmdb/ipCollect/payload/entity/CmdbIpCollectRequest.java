package com.aspire.ums.cmdb.ipCollect.payload.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @Author: fanshenquan
 * @Datetime: 2020/5/20 09:39
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CmdbIpCollectRequest extends CmdbIpCollectResponse implements Serializable {
    private static final long serialVersionUID = 5252550879988599162L;
    private List<String> ipList;
    private List<String> macList;
    private int pageNo;
    private int pageSize;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") //入参
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") //入参
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endTime;
    private String oneIpFlag; // IP去重标识,是 - 表示进行ip去重
    private String ipSort; // IP排序标识，asc - 正序，desc - 倒序，默认 - 正序
    private String hisFlag; // 是否只查询今天日期的表，不查询历史表，默认：不查询历史表，传个字符串1表示查询历史表

    public void updatePageNo() {
        this.pageNo = (pageNo - 1) * pageSize;
        this.ipList = StringUtils.isEmpty(super.getIp()) ? null : Arrays.asList(super.getIp().split(","));
        this.macList = StringUtils.isEmpty(super.getMac()) ? null : Arrays.asList(super.getMac().split(","));
    }
}
