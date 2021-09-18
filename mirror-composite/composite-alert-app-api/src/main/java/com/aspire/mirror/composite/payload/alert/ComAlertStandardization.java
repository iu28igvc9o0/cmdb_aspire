package com.aspire.mirror.composite.payload.alert;

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
public class ComAlertStandardization {

    private String conditions;
   
    private String source;
    private String displayCols;
    
    private String updated_at;
 
    private String topic;
    
    private Integer id;
    
    private Integer status;
    
    private String extraCols;
    
    private Integer countType;	
   
}