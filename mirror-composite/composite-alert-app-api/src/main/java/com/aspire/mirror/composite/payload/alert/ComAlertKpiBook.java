package com.aspire.mirror.composite.payload.alert;

import java.util.List;

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
public class ComAlertKpiBook {

    private Integer status;
	 
	 private Integer id;

   private String interval;
   
   private String updated_at;

	private String subTopic;
	
	private String source;
    
    List<ComKpiData> kpiList;
   
}