package com.aspire.mirror.indexproxy.basicdatasync;


import com.aspire.mirror.indexproxy.biz.BasicDataBiz;
import com.aspire.mirror.indexproxy.domain.MonitorObjectRecord;
import com.aspire.mirror.indexproxy.util.ExecuteUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * TODO
 * <p>
 * 项目名称:  mirror平台
 * 类名称:    MonitorObjectDataListSync
 * 类描述:    TODO
 * 创建人:    JinSu
 * 创建时间:  2020/11/11 20:43
 * 版本:      v1.0
 */
@Slf4j
public abstract class AbstractMonitorObjectDataListSync<T> {
    protected List<T> fetchResultList;
    @Autowired
    private BasicDataBiz basicDataBiz;

    List<T> getMonitorObject() {
        fetchResultList = fetchsyncItemDataList0();
        return fetchResultList;
    }
    // 子类实现
    protected abstract List<T> fetchsyncItemDataList0();

    public final void processSyncDataList() {
        if (CollectionUtils.isEmpty(fetchResultList)) {
            log.error("Monitor Object Sync fetchResultList is empty!");
            return;
        }
        List<MonitorObjectRecord> monitorObjects = processUpdateMonitorData(fetchResultList);

        insertMonitorObject(monitorObjects);
    }

    protected  void insertMonitorObject(List<MonitorObjectRecord> monitorObjects) {
        if (CollectionUtils.isEmpty(monitorObjects)) {
            log.error("Monitor Object Sync monitorObjects is empty!");
            return;
        }
        try {
            basicDataBiz.removeMonitorObjectByType(getMonitorType());
        } catch (Exception e) {
            log.error("AbstractMonitorObjectDataListSync delete monitor object is error!", e);
        }

        ExecuteUtil.partitionRun(monitorObjects, 100, (eachPair) -> this.addPartitonList(eachPair));
    }

    protected abstract String getMonitorType();

    protected  void addPartitonList(Pair<String, List<MonitorObjectRecord>> eachPair) {
        basicDataBiz.batchSaveMonitorObject(eachPair.getRight());
    }

    // 子类实现
    protected abstract List<MonitorObjectRecord> processUpdateMonitorData(List<T> dataItem);
}
