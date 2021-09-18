package com.aspire.mirror.alert.api.dto.monthReport;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AlertsLevelCountsDTO {

    private int low;

    private int medium;

    private int high;

    private int serious;
    
    private int sum;
    
    private String idcType;
    private String bizSystem;
    
    
    public void setSum(){
    	
    	sum = low + medium +serious+high;
    }

}
