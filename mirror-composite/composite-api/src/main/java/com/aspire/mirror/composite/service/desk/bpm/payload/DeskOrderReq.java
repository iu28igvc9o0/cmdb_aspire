package com.aspire.mirror.composite.service.desk.bpm.payload;

import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 工单通用查询请求参数
 * @author menglinjie
 */

@NoArgsConstructor
@Data
public class DeskOrderReq {

	/**
	 * 查询类型 1待办 2已办 3我发起的
	 */
	private Integer type;

	/**
	 * 状态 running处理中  end关闭  all全部
	 */
    private String status;
    
    /**
     * 工单标题
     */
	private String subject;

	@NotNull
	private Integer pageSize;

	@NotNull
	private Integer pageNo;
	
	/**
	 * 只返回数量
	 */
	private String cntFlag;
}
