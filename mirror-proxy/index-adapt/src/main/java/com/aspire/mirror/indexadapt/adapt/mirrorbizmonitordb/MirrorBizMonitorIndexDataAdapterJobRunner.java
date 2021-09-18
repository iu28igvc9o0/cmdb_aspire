package com.aspire.mirror.indexadapt.adapt.mirrorbizmonitordb;

import com.aspire.mirror.indexadapt.adapt.AbstractIndexDataAdapterJobRunner;
import com.aspire.mirror.indexadapt.adapt.AbstractIndexDataListAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 针对migu的业务指标适配调度业务执行    <br/>
 * Project Name:index-proxy
 * File Name:BizMonitorIndexDataAdapterJobRunner.java
 * Package Name:com.aspire.mirror.indexadapt.adapt.inspectiondb
 * ClassName: BizMonitorIndexDataAdapterJobRunner <br/>
 * date: 2018年10月10日 下午6:58:18 <br/>
 *
 * @author pengguihua
 * @since JDK 1.6
 */
@Component
@ConditionalOnProperty("indexAdapter.mirrorbizmonitorDb.switch")
class MirrorBizMonitorIndexDataAdapterJobRunner extends AbstractIndexDataAdapterJobRunner {
    @Autowired
    private MirrorBizMonitorIndexDataListAdapter inpsectionAdapter;

    @Override
    @Scheduled(cron = "${indexAdapter.mirrorbizmonitorDb.cron}")
    public void runAdaptJob() {
        super.runAdaptJob();
    }

    @Override
    protected AbstractIndexDataListAdapter<?> getIndexDataListAdapter() {
        return inpsectionAdapter;
    }
}
