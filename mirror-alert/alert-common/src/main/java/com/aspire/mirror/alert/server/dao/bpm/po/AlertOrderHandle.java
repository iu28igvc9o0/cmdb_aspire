package com.aspire.mirror.alert.server.dao.bpm.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 描述：
 *
 * @author
 * @date 2019-08-14 19:35:33
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlertOrderHandle {

    /**
     * ID
     */
    private Integer id;
    /**
     * 工单编号
     */
    private String orderId;
    /**
     * 启用状态，0-待处理，1-已处理
     */
    private String status;

    private String account;

    private String execUser;
    /**
     * 执行时间
     */
    private Date execTime;
    /**
     * 超时状态， 0-未超时；1-超时
     */
    private String expireStatus;
}