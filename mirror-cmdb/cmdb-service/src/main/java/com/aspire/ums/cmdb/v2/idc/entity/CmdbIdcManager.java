package com.aspire.ums.cmdb.v2.idc.entity;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* 描述：
* @author
* @date 2019-06-17 17:29:30
*/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CmdbIdcManager {

    /**
     * ID
     */
    private String id;
    /**
     * 资源池编码
     */
    private String idcCode;
    /**
     * 资源池名称
     */
    private String idcName;
    /**
     * 排序
     */
    private Integer sortIndex;
    /**
     * 是否删除
     */
    private String isDelete;
}