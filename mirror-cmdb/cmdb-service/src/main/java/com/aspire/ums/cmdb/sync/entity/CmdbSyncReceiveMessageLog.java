package com.aspire.ums.cmdb.sync.entity;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 *
 * @since 2020年05月13日 17:17:10
 * @author jiangxuwen
 * @version v1.0
 */
@Data
public class CmdbSyncReceiveMessageLog implements Serializable {

    private static final long serialVersionUID = -2214688806255896152L;

    /**
     * 主键id
     */
    private Long id;

    /**
     * 接收到的id
     */
    private Long revId;

    /**
     * topic
     */
    private String topic;

    /**
     * 发送端标识
     */
    private String instanceId;

    /**
     * 操作类型
     */
    private String eventType;

    /**
     * 操作表名
     */
    private String objectName;

    /**
     * 操作表的id
     */
    private String objectId;

    /**
     * 版本号
     */
    private Integer objectVersion;

    /**
	 * 
	 */
    private Date objectTimestamp;

    /**
     * 操作人
     */
    private String operator;

    /**
     * 操作时间
     */
    private Date optTime;

    /**
     * 操作说明
     */
    private String optDesc;

    /**
     * 消息id
     */
    private String msgId;

    /**
     * 消息状态(0-待处理,1-处理成功,2-处理失败)
     */
    private String msgHandlerStatus;

    /**
     * 消息顺序(redis读取)
     */
    private String msgSort;

    /**
     * 消息体内容
     */
    private String msgBody;

    /**
     * 重试次数
     */
    private Integer tryCount;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

}
