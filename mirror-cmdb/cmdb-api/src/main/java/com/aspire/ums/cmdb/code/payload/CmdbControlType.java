package com.aspire.ums.cmdb.code.payload;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* 描述：
* @author
* @date 2019-05-13 18:39:39
*/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CmdbControlType {

    /**
     * 控件ID
     */
    private String controlId;
    /**
     * 控件编码
     */
    private String controlCode;
    /**
     * 控件名称
     */
    private String controlName;
    /**
     * 是否需要绑定数据源
     */
    private String isBindSource;
    /**
     * 是否删除
     */
    private String isDelete;
    /**
     * 排序
     */
    private Integer sortIndex;
}