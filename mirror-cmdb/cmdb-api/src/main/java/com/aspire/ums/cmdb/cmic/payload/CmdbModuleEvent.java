package com.aspire.ums.cmdb.cmic.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
* 描述：
* @author
* @date 2020-05-18 18:27:54
*/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CmdbModuleEvent {

    /**
     * ID
     */
    private String id;
    /**
     * 模型ID
     */
    private String moduleId;
    /**
     * 时间分类
     */
    private String eventClass;
    /**
     * 码表ID
     */
    private String codeId;
    /**
     * 事件类型
     */
    private String eventType;
    /**
     * 处理事件类对应cmdb_module_event_handler_class.id
     */
    private String handlerClassId;
    /**
     * 事件排序
     */
    private Integer eventOrder;
    /**
     * 页面事件处理
     */
    private String eventPageScript;
    /**
     * 新增时间
     */
    private Date insertTime;
    /**
     * 录用用户
     */
    private String insertPerson;
    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 更新人
     */
    private String updatePerson;
    /**
     * 是否删除
     */
    private Integer isDelete;
}