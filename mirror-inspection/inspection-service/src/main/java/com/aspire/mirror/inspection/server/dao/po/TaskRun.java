package com.aspire.mirror.inspection.server.dao.po;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/** 
* @author ZhangSheng 
* @version 2018年9月1日 上午10:25:26 
* @describe 任务运行页面封装类
*/
@Data
@NoArgsConstructor
@ToString
public class TaskRun implements Serializable{
	
	private static final long serialVersionUID = -7255909150932156013L;
	/** 
	 * 任务ID
	 */
    private String taskId;
    /**
     * 任务名称
     */
    private String name;
    
    private String status;
    /**
     * 开始时间
     */
    private Date execTimeStart;
    /**
     * 结束时间
     */
    private Date execTimeEnd;
}
