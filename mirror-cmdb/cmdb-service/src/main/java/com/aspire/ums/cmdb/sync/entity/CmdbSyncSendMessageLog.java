package com.aspire.ums.cmdb.sync.entity;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @since 2020年05月13日 17:17:10
 * @author jiangxuwen
 * @version v1.0
 */
@Data
public class CmdbSyncSendMessageLog implements Serializable {

    private static final long serialVersionUID = 7971827926781086654L;

    /**
     * 主键id
     */
    @JsonProperty("id")
    @JSONField(name = "id")
    private Long id;

    /**
     * topic
     */
    @JsonProperty("topic")
    @JSONField(name = "topic")
    private String topic;

    /**
     * 发送端标识
     */
    @JsonProperty("instance_id")
    @JSONField(name = "instance_id")
    private String instanceId = "cmdb";

    /**
     * 模型Id.
     * 
     * @param
     * @return
     */
    @JsonProperty("module_id")
    @JSONField(name = "module_id")
    private String moduleId;

    /**
     * 操作类型
     */
    @JsonProperty("event_type")
    @JSONField(name = "event_type")
    private String eventType;

    /**
     * 操作表名
     */
    @JsonProperty("object_name")
    @JSONField(name = "object_name")
    private String objectName;

    /**
     * 操作表的id
     */
    @JsonProperty("object_id")
    @JSONField(name = "object_id")
    private String objectId;

    @JsonIgnore
    @JSONField(serialize = false)
    private Object data;

    /**
     * 版本号
     */
    @JsonProperty("object_version")
    @JSONField(name = "object_version")
    private Integer objectVersion;

    /**
	 * 
	 */
    @JsonProperty("object_timestamp")
    @JSONField(name = "object_timestamp")
    private Date objectTimestamp;

    /**
     * 操作人
     */
    @JsonProperty("operator")
    @JSONField(name = "operator")
    private String operator;

    /**
     * 操作时间
     */
    @JsonProperty("opt_time")
    @JSONField(name = "opt_time")
    private Date optTime;

    /**
     * 操作说明
     */
    @JsonProperty("opt_desc")
    @JSONField(name = "opt_desc")
    private String optDesc;

    /**
     * 消息id
     */
    @JsonProperty("msg_id")
    @JSONField(name = "msg_id")
    private String msgId;

    /**
     * 消息状态(0-待投递,1-投递成功,2-投递失败)
     */
    @JsonIgnore
    @JSONField(serialize = false)
    private String msgStatus;

    /**
     * 消息顺序(redis读取)
     */
    @JsonProperty("msg_sort")
    @JSONField(name = "msg_sort")
    private String msgSort;

    /**
     * 消息体内容
     */
    @JsonProperty("msg_body")
    @JSONField(name = "msg_body")
    private String msgBody;

    /**
     * 重试次数
     */
    @JsonIgnore
    @JSONField(serialize = false)
    private Integer tryCount;

    /**
     * 创建时间
     */
    @JsonProperty("create_time")
    @JSONField(name = "create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @JsonProperty("update_time")
    @JSONField(name = "update_time")
    private Date updateTime;

    @JsonIgnore
    @JSONField(serialize = false)
    private String objectKey;
}
