package com.aspire.ums.cmdb.sync.payload;

import java.io.Serializable;

import lombok.Data;

/**
 * 网管通过rabbitmq同步过来的业务线.
 *
 * @author jiangxuwen
 * @date 2020/5/20 11:14
 */
@Data
public class CmdbBusinessMessage implements Serializable {

    private static final long serialVersionUID = -2778299896678815396L;

    private Integer msgSerial;

    private String uuid;

    private String timestamp;

    private CmdbBusinessMessageData data;

}
