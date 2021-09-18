package com.aspire.ums.cmdb.v3.condication.payload;

import com.aspire.ums.cmdb.code.payload.CmdbCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* 描述：
* @author
* @date 2020-03-11 15:11:41
*/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CmdbV3CondicationReturnRelation {

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
    private CmdbCode cmdbCode;
    /**
     * 排序ID
     */
    private Integer sortIndex;
    /**
     * 是否删除
     */
    private Integer isDelete;
}