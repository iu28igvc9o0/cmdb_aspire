/**
 * @Title: ZookeeperConstant.java
 * @Package com.migu.tsg.microservice.atomicservice.log.kafka
 * @Description:  Copyright: Copyright (c) 2017 Company:咪咕文化 tsg
 * @author tsg-frank
 * @date 2017年7月28日 下午3:44:25
 * @version V1.0
 */

package com.aspire.mirror.log.server.constant;

/**
 * @ClassName: ZookeeperConstant
 * @Description
 * @author tsg-frank
 *
 */
public interface ZookeeperConstant {
    /***
     * zookeeper session 超时时间（单位/毫秒）
     */
    String SESSION_TIMEOUT_MS_DEFAULT = "10000";
    /***
     * zookeeper 同步时间（单位/毫秒）
     */
    String SYNC_TIME_MS_DEFAULT = "20000";
}
