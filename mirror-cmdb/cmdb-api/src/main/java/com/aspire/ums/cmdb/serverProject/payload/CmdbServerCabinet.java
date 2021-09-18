package com.aspire.ums.cmdb.serverProject.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 描述：
 * 
 * @author
 * @date 2021-01-27 17:26:14
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CmdbServerCabinet {

    /**
     * 资产ID
     */
    private String id;

    /**
     * 删除标识
     */
    private Integer isDelete;

    /**
     * 资源池
     */
    private String idcType;

    /**
     * 机房
     */
    private String serverRoomLocation;

    /**
     * 机柜编码
     */
    private String serverCabinetCode;

    /**
     * 机柜规格
     */
    private String serverCabinetStandard;

    /**
     * 所属项目
     */
    private String projectBelongTo;

    /**
     * 所属业务
     */
    private String businessBelongTo;
}
