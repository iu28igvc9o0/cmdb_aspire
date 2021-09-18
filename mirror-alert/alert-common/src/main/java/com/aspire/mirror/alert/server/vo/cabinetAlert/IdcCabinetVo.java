package com.aspire.mirror.alert.server.vo.cabinetAlert;

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
//@AllArgsConstructor
public class IdcCabinetVo {

    private String idcType;

    private String roomId;

    private String idcCabinet;
   
    private String idcCabinetColumn;
    
    private int count;
    
    public IdcCabinetVo(String idcType, String roomId, String idcCabinet, String idcCabinetColumn, int count){
    	this.idcType = idcType;
    	this.roomId= idcType;
    	this.idcCabinet = idcCabinet;
    	this.idcCabinetColumn = idcCabinetColumn;
    	this.count = count;
    }
}