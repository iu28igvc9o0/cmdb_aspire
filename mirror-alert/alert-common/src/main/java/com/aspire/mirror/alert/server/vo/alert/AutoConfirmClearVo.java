package com.aspire.mirror.alert.server.vo.alert;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AutoConfirmClearVo {

    /**
     *  uuid
     */
    private String uuid;

    /**
     *  ip
     */
    private String deviceIp;
    /**
     *  资源池
     */
    private String idcType;
    /**
     *  业务系统
     */
    private String bizSys;
    /**
     *  告警等级
     */
    private String alertLevel;
    /**
     *  告警来源
     */
    private String source;
    /**
     *  监控项id
     */
    private String itemId;

    /**
     *  类型
     */
    private Integer autoType;

    /**
     *  内容
     */
    private String content;

    /**
     *  开始时间
     */
    private String startTime;

    /**
     *  结束时间
     */
    private String endTime;

    /**
     *  操作人
     */
    private String operator;
}
