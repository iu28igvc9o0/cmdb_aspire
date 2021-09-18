package com.aspire.mirror.composite.service.order.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * 工单通用查询请求参数
 */

@NoArgsConstructor
@Data
public class HomePageInstAnalysisReq {

	
	//建单时间开始
	@JsonProperty("end_date")
	private String endDate;

    //建单时间结束
	@JsonProperty("start_date")
    private String startDate;


    //发起人
	@JsonProperty("origin_person")
    private String[] originPerson;

    //处理人
	@JsonProperty("solve_person")
    private String[] solvePerson;

	@NotNull
	@JsonProperty("page_size")
	private Integer pageSize;
	@NotNull
	@JsonProperty("page_no")
	private Integer pageNo;
}
