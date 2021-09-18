package com.aspire.mirror.alert.server.dao.monitorHttp.po;

import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * 自动化派单类
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.alert.server.dao.po
 * 类名称:    AlertConfig.java
 * 类描述:    自动化告警po类
 * 创建人:    qianch
 * 创建时间:  2019/1/24 16:58
 * 版本:      v1.0
 */
@Data
@NoArgsConstructor
public class MonitorHttpIdcType {
	
	/**
	 * ID
	 */
	private String idcType;
    /**
     * 告警名称
     */
    private String url;
    
   

}
