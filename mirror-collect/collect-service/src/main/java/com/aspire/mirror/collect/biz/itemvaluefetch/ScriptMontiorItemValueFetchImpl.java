package com.aspire.mirror.collect.biz.itemvaluefetch;

import com.aspire.mirror.collect.api.payload.FetchObjectItemsValueRequest;
import com.aspire.mirror.collect.api.payload.GeneralResponse;
import com.aspire.mirror.collect.api.payload.MonitorScriptConfig;
import com.aspire.mirror.collect.api.payload.ObjectItemInfo;
import com.aspire.mirror.collect.api.payload.ObjectItemValueWrap;
import com.aspire.mirror.collect.api.payload.ScriptTargetAgent;
import com.aspire.mirror.collect.clientservice.ScriptItemServiceClient;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * 脚本类型指标
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.collect.biz.itemvaluefetch
 * 类名称:    ScriptMontiorItemValueFetchImpl.java
 * 类描述:    脚本类型指标
 * 创建人:    JinSu
 * 创建时间:  2019/11/13 13:40
 * 版本:      v1.0
 */
@Slf4j
@Component
@Qualifier("itemValFetchImpl")
public class ScriptMontiorItemValueFetchImpl implements  IMonitorItemValueFetch {

    @Autowired
    private ScriptItemServiceClient scriptItemClient;

    @Value("${SCRIPT_ITEM_TIMEOUT:120}")
    private Integer script_item_timeout = 200;

    @Value("${SCRIPT_INSPECTION_TARGET_USER:aspire}")
    private String targetOpsUser;


    private static ExecutorService threadService = initExecutorService();
    private static ExecutorService initExecutorService() {
        return new ThreadPoolExecutor(50, 100, 0L, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(5000),
                new ThreadFactory() {
                    private final AtomicInteger inc = new AtomicInteger(1);

                    @Override
                    public Thread newThread(Runnable runnable) {
                        return new Thread(runnable, "scriptItemExecTask_" + inc.getAndIncrement());
                    }
                });
    }
    @Override
    public void fetchMonitorItemValues(FetchObjectItemsValueRequest request, List<ObjectItemValueWrap> resultHolder) {
        List<ObjectItemInfo> intrestItemList = resolveIntrestItemList(request);
        if (intrestItemList.isEmpty()) {
            return;
        }
        fetchMonitorItemValues(intrestItemList);
    }
    private void fetchMonitorItemValues(List<ObjectItemInfo> itemInfoList) {
        Map<String, List<ObjectItemInfo>> itemGroup = itemInfoList.stream().collect(Collectors.groupingBy
                (ObjectItemInfo::getItemKey));
        final CountDownLatch latch = new CountDownLatch(itemGroup.size());
        for (String scriptId : itemGroup.keySet()) {
            threadService.submit(new ThreadCheck(scriptId, itemGroup.get(scriptId), latch));
        }
        try {
            latch.await();
        } catch (InterruptedException e) {
            log.error("Exeception when fetch script items value.", e);
        }
    }
    private List<ObjectItemInfo> resolveIntrestItemList(FetchObjectItemsValueRequest request) {
        List<ObjectItemInfo> resultList = new ArrayList<>();
        if (CollectionUtils.isEmpty(request.getObjectItemList())) {
            return resultList;
        }
        for (ObjectItemInfo itemInfo : request.getObjectItemList()) {
            if (ObjectItemInfo.API_SERVER_SCRIPT.equalsIgnoreCase(itemInfo.getApiServerType())) {
                resultList.add(itemInfo);
            }
        }
        return resultList;
    }
    private class ThreadCheck implements Runnable {
        private final String scriptId;
        private final List<ObjectItemInfo> objectItemInfoList;
        private final CountDownLatch latch;

        public ThreadCheck(final String scriptId, final List<ObjectItemInfo> objectItemInfoList, final CountDownLatch latch) {
            this.scriptId = scriptId;
            this.objectItemInfoList = objectItemInfoList;
            this.latch = latch;
        }

        @Override
        public void run() {
            try {
                MonitorScriptConfig monitorScriptConfig = new MonitorScriptConfig();
                monitorScriptConfig.setTargetOpsUser(targetOpsUser);
                monitorScriptConfig.setScriptId(scriptId);
                monitorScriptConfig.setTimeout(script_item_timeout);
                // 每个指标实例对应的脚本参数是一致的
                monitorScriptConfig.setScriptParam(objectItemInfoList.get(0).getScriptParam());
                monitorScriptConfig.setCustomizeParam(objectItemInfoList.get(0).getCustomizeParam());
                List<ScriptTargetAgent> targetAgentList = Lists.newArrayList();
                if (CollectionUtils.isNotEmpty(objectItemInfoList)) {
                    for (ObjectItemInfo objectItemInfo :objectItemInfoList) {
                        ScriptTargetAgent scriptTargetAgent = new ScriptTargetAgent();
                        scriptTargetAgent.setAgentHost(objectItemInfo.getObjectId());
                        scriptTargetAgent.setResultMessage(objectItemInfo);
                        targetAgentList.add(scriptTargetAgent);
                    }
                }
                monitorScriptConfig.setTargetAgentList(targetAgentList);
                GeneralResponse response = scriptItemClient.execSciptItem(monitorScriptConfig);
                if (!response.isFlag()) {
                    log.error("@@@@exec script return error, {}", response.getErrorTip());
                }
            }catch (Exception e) {
                log.error("@@@@@exec script item error!!", e);
            }finally {
                latch.countDown();
            }
        }
    }
}
