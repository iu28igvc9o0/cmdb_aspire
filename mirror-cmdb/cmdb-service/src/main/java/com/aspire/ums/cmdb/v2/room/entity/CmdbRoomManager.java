package com.aspire.ums.cmdb.v2.room.entity;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* 描述：
* @author
* @date 2019-06-17 17:30:16
*/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CmdbRoomManager {

    /**
     * ID
     */
    private String id;
    /**
     * 机房编码
     */
    private String roomCode;
    /**
     * 机房名称
     */
    private String roomName;
    /**
     * pod ID
     */
    private String podId;
    /**
     * idcType ID
     */
    private String idcId;
    /**
     * 排序
     */
    private Integer sortIndex;
    /**
     * 是否删除
     */
    private String isDelete;
}