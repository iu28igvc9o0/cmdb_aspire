/**
 *
 * 项目名： ops-service 
 * <p/> 
 *
 * 文件名:  InspectionCountResp.java 
 * <p/>
 *
 * 功能描述: TODO 
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2020年4月7日 
 *
 * @version	V1.0
 * <p/>
 *
 *<b>Copyright(c)</b> 2020 卓望公司-版权所有<br/>
 *   
 */
package com.aspire.mirror.ops.clientservice.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * 巡检数返回
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.inspection.api.dto.model
 * 类名称:    InspectionCountResp.java
 * 类描述:    巡检数返回
 * 创建人:    JinSu
 * 创建时间:  2020/4/3 15:02
 * 版本:      v1.0
 */
@Data
@JsonInclude(Include.NON_NULL)
public class InspectionCountResp {
	
	@JsonProperty("total_num")
    private int totalNum;

    @JsonProperty("running_num")
    private int runningNum;

    @JsonProperty("failed_num")
    private int failedNum;

    @JsonProperty("success_num")
    private int successNum;
}
