package com.aspire.mirror.alert.server.dao.alert.po;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 描述：
 *
 * @author
 * @date 2019-08-14 19:35:33
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlertStandardizationLog {

    private Date fromTime;
   
    private Date toTime;
    private Date execTime;
    
    private String execDuration;
    
    private String status;
    
    private String content;
    
    private Date createTime;
    
    private Integer type;
    
    private Integer configId;
   
}