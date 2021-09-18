package com.aspire.mirror.alert.server.dao.alert.po;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


/**
 * 告警记录类
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.alert.server.dao.po
 * 类名称:    Alerts.java
 * 类描述:    告警po类
 * 创建人:    JinSu
 * 创建时间:  2018/9/18 16:58
 * 版本:      v1.0
 */
@Data
@NoArgsConstructor
public class AlertReportOperateRecord {
	
	/**
	 * ID
	 */
	private Integer id;
    /**
     * 告警ID
     */
    private String alertId;
    
    /**
     * 操作人
     */
    private String userId;
    
    /**
     * 操作人名称
     */
    private String userName;
    
    /**
     * 通知类型, 0:短信,1:邮件
     */
    private Integer reportType;
     
    /**
     * 操作时间
     */
    private Date userEmail;

    
    private String destination;
    
    /**
     * 操作内容
     */
    private String message;
    
    private Integer status;
    //发送状态,0:失败,1:成功
    private Date createTime;
    
      
    
}
