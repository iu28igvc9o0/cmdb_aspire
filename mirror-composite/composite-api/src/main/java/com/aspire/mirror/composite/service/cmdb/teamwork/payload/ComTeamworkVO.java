package com.aspire.mirror.composite.service.cmdb.teamwork.payload;
import java.util.List;

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
public class ComTeamworkVO {

    /**
     * 
     */
    private String id;
    
    private String idJoin;//重启的作战共用的
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
    private  String twStartTime;
    /**
     * 
     */
    private  String twEndTime;
    /**
     * 
     */
    private String twUseTime;
    /**
     * 
     */
    private int isDelete;
    
    private List<ComTeamworkLabelVO>  labelList;
    
    private List<ComTeamworkUserManagerVO>  userList;
}