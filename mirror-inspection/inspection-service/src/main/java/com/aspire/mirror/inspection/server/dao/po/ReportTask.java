package com.aspire.mirror.inspection.server.dao.po;

import java.io.Serializable;

import com.aspire.mirror.inspection.api.dto.model.ReportDTO;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** 
* @author ZhangSheng 
* @version 2018年8月21日 下午2:56:19 
* @describe 
*/

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ReportTask implements Serializable {
	
	private static final long serialVersionUID = -1839099837687493026L;

	/** 报告ID */
    private String reportId;

    /** 任务ID */
    private String taskId;
    
    /** 任务名称 */
    private String taskName;
    
    /** 任务类型 */
    private String taskType;

    /** 报告名称 */
    private String name;

    /** 创建时间 */
    private java.util.Date createTime;

    /** RUNNING-运行中
		FINNISHED运行完成 */
    private String status;

    /** 结束时间 */
    private java.util.Date finishTime;

    private String reportFilePath;

    private String result;
}
