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
 * 冲突IP查询返回类
 *
 * @Author: fanshenquan
 * @Datetime: 2020/5/27 10:24
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class CmdbIpClashFindPageResponse extends BaseClashEntity implements Serializable {
    private static final long serialVersionUID = 151495311686880249L;
    // 主体ID
    private String mainId;
    // 记录表ID
    private String recordId;

    // 冲突IP
    private String clashIp;

    // 累计切换次数
    private Integer changeTotal;
    // 系统推测
    private String systemInfer;

    // 处理状态 0-待处理,1-暂不处理,2-处理中,3-已处理
    private String handleStatus;
    private String handleStatusText;
    // 暂不处理原因
    private String notHandleReason;
    // 操作人
    private String operator;
    // 更新时间
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") //入参
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;
    // 是否已通知 0-否,1-是
    private String isNotify;
    // 工单号
    private String jobNumber;
    // 来源
    private String collectType;

    public void setHandleStatus(String handleStatus) {
        this.handleStatus = handleStatus;
        switch (String.valueOf(handleStatus)) {
            case "0":
                this.handleStatusText = "待处理";
                break;
            case "1":
                this.handleStatusText = "暂不处理";
                break;
            case "2":
                this.handleStatusText = "处理中";
                break;
            case "3":
                this.handleStatusText = "已处理";
                break;
            default:
                this.handleStatusText = "";
                break;
        }
    }
}
