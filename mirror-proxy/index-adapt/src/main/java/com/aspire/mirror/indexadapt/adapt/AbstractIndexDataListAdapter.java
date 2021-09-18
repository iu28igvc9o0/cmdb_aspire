package com.aspire.mirror.indexadapt.adapt;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 指标获取、适配、发布抽象实现    <br/>
 * Project Name:index-proxy
 * File Name:AbstractIndexDataListAdapter.java
 * Package Name:com.aspire.mirror.indexadapt.adapt
 * ClassName: AbstractIndexDataListAdapter <br/>
 * date: 2018年8月6日 下午3:55:11 <br/>
 *
 * @author pengguihua
 * @version @param <T>
 * @since JDK 1.6
 */
@Slf4j
public abstract class AbstractIndexDataListAdapter<T extends BaseRawIndexData>
        implements IndexDataListAdapter<T> {
    protected List<T> rawIndexDataList;
    protected List<StandardIndex> standardIndexDataList;

    @Autowired
    private StandardIndexDataListPublisher standardIndexDataListPublisher;

    // 子类实现
    @Override
    public abstract boolean preHandleAdapt();

    // 子类实现
    protected abstract List<T> fetchRawIndexDataList0();

    // 子类实现
    protected abstract List<StandardIndex> adapt2StandardIndex0(List<T> rawIndexList);

    // 子类实现
    @Override
    public abstract void postHandleAdapt();


    @Override
    public final List<T> fetchRawIndexDataList() {
        rawIndexDataList = fetchRawIndexDataList0();
        if (CollectionUtils.isNotEmpty(rawIndexDataList)) {
            log.info("This turn had fetched {} RawIndexDatas for adapter {}.",
                    rawIndexDataList.size(), getAdapterIdentity());
        }
        return rawIndexDataList;
    }

    @Override
    public final List<StandardIndex> adapt2StandardIndex() {
        if (rawIndexDataList == null) {
            return null;
        }
        return adapt2StandardIndex0(rawIndexDataList);
    }

    public final void publishStandardIndexDataList(List<StandardIndex> indexDataList) {
        standardIndexDataListPublisher.publishStandardIndexDataList(indexDataList, getAdapterIdentity());
    }

    @Override
    public void handleException() {
        // do nothing, the subclasses can override this method to release resources such as lock or
        // connections when execption
    }
}
