package com.aspire.mirror.ops.biz.model;

import org.springframework.context.ApplicationEvent;

import com.aspire.mirror.ops.domain.OpsPipeline;
import com.aspire.mirror.ops.domain.OpsPipelineInstance;

import lombok.Getter;

/** 
 *
 * 项目名称: ops-service 
 * <p/>
 * 
 * 类名: OpsPipelineExecuteTimeoutEvent
 * <p/>
 *
 * 类功能描述: 作业执行超时事件
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2020年2月21日  
 *
 * @version	V1.0 
 * <br/>
 *
 * <b>Copyright(c)</b> 2020 卓望公司-版权所有 
 *
 */
@Getter
public class OpsPipelineExecuteTimeoutEvent extends ApplicationEvent {
	private static final long	serialVersionUID	= 2223412384047574725L;
	private OpsPipeline			pipeline;
	private OpsPipelineInstance	pipelineInstance;
	
	public OpsPipelineExecuteTimeoutEvent(Object source, OpsPipeline pipeline, OpsPipelineInstance pipelineInstance) {
		super(source);
		this.pipeline = pipeline;
		this.pipelineInstance = pipelineInstance;
	}
}
