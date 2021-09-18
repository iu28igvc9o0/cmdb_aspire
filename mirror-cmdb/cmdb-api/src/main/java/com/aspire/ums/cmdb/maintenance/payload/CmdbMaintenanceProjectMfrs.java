package com.aspire.ums.cmdb.maintenance.payload;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* 描述：
* @author
* @date 2019-07-29 22:31:45
*/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CmdbMaintenanceProjectMfrs {

    /**
     * ID
     */
    private String id;
    /**
     * 项目ID
     */
    private String projectId;
    /**
     * 厂家ID
     */
    private String produceId;
    /**
     * 厂家联系人名称
     */
    private String produceConcatName;
    /**
     * 厂家联系人电话
     */
    private String produceConcatPhone;
    /**
     * 厂家联系人邮箱
     */
    private String produceConcatEmail;
    /**
     * 是否删除
     */
    private String isDelete;
}