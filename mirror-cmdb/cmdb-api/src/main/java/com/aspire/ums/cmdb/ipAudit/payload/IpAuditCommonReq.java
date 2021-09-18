package com.aspire.ums.cmdb.ipAudit.payload;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @Author: huanggongrui
 * @Description: IP稽核列表查询请求通用实体类
 * @Date: create in 2020/5/15 17:43
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IpAuditCommonReq {
    // 分页页标
    private Integer pageSize;
    // 每页数量
    private Integer pageNo;
    /**
     * 检测时间开始与结束
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") //入参
    private String checkTimeBegin;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") //入参
    private String checkTimeEnd;
    /**
     * 检测需记录的ip,逗号分隔
     */
    private String ipList;
    private List<String> ips;
    /**
     * 所属资源池
     */
    private String dc;
    private List<String> dcList;
    /**
     * 网关设备IP/资产管理IP
     */
    private String deviceIp;
    /**
     * 是否已通知:未通知,已通知
     */
    private String isNotify;
    /**
     * 处理状态:待处理,暂不处理
     */
    private String processingStatus;
    /**
     * 原因说明
     */
    private String reason;
    /**
     * 操作人
     */
    private String operator;
    /**
     * 操作时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") //入参
    private String operatingTimeBegin;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") //入参
    private String operatingTimeEnd;
    /**
     * 工单号
     */
    private String orderNum;
    /**
     * 更新人
     */
    private String updatePerson;

    public void updatePageNum() {
        this.setPageNo((this.getPageNo()-1) * this.getPageSize());
    }
}
