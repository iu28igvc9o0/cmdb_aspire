/**
 *
 * 项目名： ops-service 
 * <p/> 
 *
 * 文件名:  InspectionTaskStatisticsQuery.java 
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

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * 巡检数量查询
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.inspection.api.dto.model
 * 类名称:    InspectionCountQueryModel.java
 * 类描述:    巡检数量查询
 * 创建人:    JinSu
 * 创建时间:  2020/4/3 14:57
 * 版本:      v1.0
 */
@Data
public class InspectionCountQueryModel {
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty("create_time_start")
    private Date createTimeStart;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty("create_time_end")
    private Date createTimeEnd;
}
