package com.aspire.mirror.indexadapt.adapt.mirrorbizmonitordb;

import static com.aspire.mirror.indexadapt.adapt.IndexAdaptConst.DISTRIBUTE_LOCK_PREFIX;
import static java.lang.String.format;

import java.util.List;
import java.util.concurrent.locks.Lock;

import javax.annotation.PostConstruct;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import com.aspire.mirror.indexadapt.adapt.AbstractIndexDataListAdapter;
import com.aspire.mirror.indexadapt.adapt.mirrorbizmonitordb.model.Items;
import com.aspire.mirror.indexadapt.adapt.mirrorbizmonitordb.service.MirrorBizMonitorIndexService;
import com.aspire.mirror.indexadapt.helper.DistributeLockHelper;

import lombok.extern.slf4j.Slf4j;

/**
 * 针对migu的业务告警适配    <br/>
 * Project Name:index-proxy
 * File Name:MiguBizMonitorIndexDataListAdapter.java
 * Package Name:com.aspire.mirror.indexadapt.adapt.inspectiondb
 * ClassName: MiguBizMonitorIndexDataListAdapter <br/>
 * date: 2018年8月6日 下午3:57:07 <br/>
 *
 * @author pengguihua
 * @since JDK 1.6
 */
@Slf4j
@Component
@ConditionalOnProperty("indexAdapter.mirrorbizmonitorDb.switch")
class MirrorBizMonitorIndexDataListAdapter extends AbstractIndexDataListAdapter<Items> {
	@Value("${indexAdapter.mirrorbizmonitorDb.identity}")
	private String							identity;

	@Autowired
	private MirrorBizMonitorIndexService	service;

	@Autowired
	private DistributeLockHelper			distLockHelper;

	private Lock							rLock;

    @PostConstruct
    private void initRLock() {
        rLock = distLockHelper.getLock(DISTRIBUTE_LOCK_PREFIX + identity);
    }

    @Override
    public String getAdapterIdentity() {
        return identity;
    }

    @Override
    protected List<Items> fetchRawIndexDataList0() {
        return service.listAllBizMonitorTriggers();
    }

    @Override
    protected List<StandardIndex> adapt2StandardIndex0(List<Items> rawIndexList) {
        if (CollectionUtils.isEmpty(rawIndexList)) {
            return null;
        }
        return service.fetchAndAdapt2StandardIdxs(rawIndexList);
    }

    @Override
    public boolean preHandleAdapt() {
        try {
            return rLock.tryLock();
        } catch (Throwable e) {
            log.error(format("Error to get lock for adapter '%s'", getAdapterIdentity()), e);
            releaseLock();
        }
        return false;
    }

    @Override
    public void postHandleAdapt() {
        releaseLock();
    }

    private void releaseLock() {
        rLock.unlock();
    }

    @Override
    public void handleException() {
        releaseLock();
    }
}
