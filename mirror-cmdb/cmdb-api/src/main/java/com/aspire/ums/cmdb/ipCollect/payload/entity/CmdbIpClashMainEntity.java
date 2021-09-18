package com.aspire.ums.cmdb.ipCollect.payload.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 冲突IP主表字段
 *
 * @Author: fanshenquan
 * @Datetime: 2020/5/23 14:05
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CmdbIpClashMainEntity implements Serializable {
    private static final long serialVersionUID = -6691261097453565782L;
    //主键id
    private String id;
    //关联子表id
    private String recordId;
    //冲突IP
    private String clashIp;
    //处理状态 0-待处理,1-暂不处理,2-处理中,3-已处理
    private String handleStatus;
    //暂不处理原因
    private String notHandleReason;
    // 操作人
    private String operator;
    // 工单号
    private String jobNumber;
    //创建时间
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") //入参
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date creatTime;
    //更新时间
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") //入参
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;
    // 采集类型
    private String collectType;
}
