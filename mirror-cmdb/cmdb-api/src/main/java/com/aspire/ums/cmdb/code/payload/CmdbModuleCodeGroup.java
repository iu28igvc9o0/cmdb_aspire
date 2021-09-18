package com.aspire.ums.cmdb.code.payload;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
* 描述：
* @author
* @date 2019-05-13 18:39:39
*/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CmdbModuleCodeGroup {

    /**
     * 分组ID
     */
    private String groupId;
    /**
     * 分组名称
     */
    private String groupName;
    /**
     * 模型ID
     */
    private String moduleId;
    /**
     * 是否删除
     */
    private Integer isDelete;
    /**
     * 排序
     */
    private Integer sortIndex;

    /**
     * 字段列表
     */
    private List<CmdbCode> codeList;
}