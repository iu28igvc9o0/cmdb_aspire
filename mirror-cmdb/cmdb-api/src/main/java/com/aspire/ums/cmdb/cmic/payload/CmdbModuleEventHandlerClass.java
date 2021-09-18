package com.aspire.ums.cmdb.cmic.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* 描述：
* @author
* @date 2020-05-18 18:28:11
*/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CmdbModuleEventHandlerClass {

    /**
     * ID
     */
    private String id;
    /**
     * 事件分类
     */
    private String eventClass;
    /**
     * 处理类名称
     */
    private String handlerName;
    /**
     * 处理类
     */
    private String handlerClass;
    /**
     * 备注
     */
    private String remark;
}