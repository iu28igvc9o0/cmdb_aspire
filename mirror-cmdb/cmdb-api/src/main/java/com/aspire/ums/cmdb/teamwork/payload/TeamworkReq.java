package com.aspire.ums.cmdb.teamwork.payload;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* 描述：
* @author
* @date 2021-03-11 14:29:04
*/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeamworkReq {

   
    /**
     * 作战编号
     */
    private String twCode;
    /**
     * 作战标题
     */
    private String twTitle;
    /**
     * 作战场景
     */
    private String twSence;
    /**
     * 作战对象
     */
    private String twSource;
    /**
     * 
     */
    private String twContent;
    /**
     * 
     */
    private String twStatus;
    /**
     * 
     */
    private String twStartTime;
    /**
     * 
     */
    private String twEndTime;
    
    @JsonProperty("page_size")
    private Integer pageSize;

    @JsonProperty("page_no")
    private Integer pageNo;
   
   
}