package com.aspire.ums.cdn.model;

import java.util.LinkedList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: CmdbUpdateInstance
 * Author:   zhu.juwang
 * Date:     2019/5/28 14:02
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@NoArgsConstructor
@AllArgsConstructor
public class CmdbUpdateInstance {
    /**
     * cmdb_instance表需要更新的字段信息
     */
    private List<CmdbInstanceTableColumn> instanceTableList;
    /**
     * 需要更新instance附属表的字段信息
     */
    private List<CmdbInstanceTableColumn> otherTableList;
    /**
     * 需要更新instance附属表的字段信息
     */
    private List<CmdbInstanceIpManager> ipManagerList;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CmdbInstanceTableColumn {
        private String filedName;
        private Object filedValue;
    }
    
    public List<CmdbInstanceTableColumn> getInstanceTableList() {
        return this.instanceTableList == null ? new LinkedList<>() : this.instanceTableList;
    }

    public void setInstanceTableList(List<CmdbInstanceTableColumn> instanceTableList) {
        this.instanceTableList = instanceTableList;
    }

    public List<CmdbInstanceTableColumn> getOtherTableList() {
        return otherTableList == null ? new LinkedList<>() : this.otherTableList;
    }

    public void setOtherTableList(List<CmdbInstanceTableColumn> otherTableList) {
        this.otherTableList = otherTableList;
    }

    public List<CmdbInstanceIpManager> getIpManagerList() {
        return ipManagerList == null ? new LinkedList<>() : this.ipManagerList;
    }

    public void setIpManagerList(List<CmdbInstanceIpManager> ipManagerList) {
        this.ipManagerList = ipManagerList;
    }
}
