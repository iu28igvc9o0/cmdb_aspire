package com.aspire.ums.cmdb.v2.project.entity;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* 描述：
* @author
* @date 2019-06-17 17:30:43
*/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CmdbProjectManager {

    /**
     * ID
     */
    private String id;
    /**
     * 项目编码
     */
    private String projectCode;
    /**
     * 项目名称
     */
    private String projectName;
    /**
     * 资源池ID
     */
    private String idcId;
    /**
     * 排序
     */
    private Integer sortIndex;
    /**
     * 
     */
    private String isDelete;
}