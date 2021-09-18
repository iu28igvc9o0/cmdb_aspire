package com.aspire.ums.cmdb.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * kafka配置.
 *
 * @author jiangxuwen
 * @date 2020/6/1 19:08
 */
@Component("syncOsaConfig")
public class SyncOsaConfig {

    /** 是否开启数据同步到网管. */
    @Value("${cmdb.enable.syncOsaFlag:false}")
    private boolean syncOsaFlag;

    /** kafka分区数. */
    @Value("${kafka.partitionNum:3}")
    private Integer partitionNum;

    public boolean isSyncOsaFlag() {
        return syncOsaFlag;
    }

    public void setSyncOsaFlag(boolean syncOsaFlag) {
        this.syncOsaFlag = syncOsaFlag;
    }

    public Integer getPartitionNum() {
        return partitionNum;
    }

    public void setPartitionNum(Integer partitionNum) {
        this.partitionNum = partitionNum;
    }
}
