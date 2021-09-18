package com.aspire.mirror.composite.payload.cabinetAlert;

import com.fasterxml.jackson.annotation.JsonProperty;

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
public class ComAlertCabinetColumnReq {

    private String id;
   
    private String idcType;
   
    private String roomId;
   
    
    private String cabinetName;
    
    private Integer status;
    
    private Integer alertStatus;
    //1所有资源池2资源池3机房
    private int configType;
    
    private String cabinetColumn;
    
    private String deviceHost;
    
    private String businessConcat;
    
    private String idcCabinet;
    
    @JsonProperty("page_size")
    private Integer pageSize;

    @JsonProperty("page_no")
    private Integer pageNo;
 
}