package com.aspire.ums.cmdb.v3.condication.payload;
import java.util.Date;

import com.aspire.ums.cmdb.code.payload.CmdbSimpleCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* 描述：
* @author
* @date 2020-03-16 09:19:57
*/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CmdbV3CondicationSortRelation {

    /**
     * ID
     */
    private String id;
    /**
     * 查询条件ID
     */
    private String condicationSettingId;
    /**
     * 码表ID
     */
    private String codeId;

    /**
     * 码表对象
     */
    private CmdbSimpleCode cmdbCode;
    /**
     * 
     */
    private String sortType;
    /**
     * 排序ID
     */
    private Integer sortIndex;
    /**
     * 是否删除
     */
    private Integer isDelete;
}