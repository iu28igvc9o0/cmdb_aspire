package com.aspire.mirror.alert.server.task.cmdbInstance;

import com.aspire.mirror.alert.server.clientservice.CmdbAlertRestfulClient;
import com.aspire.mirror.alert.server.dao.cmdbInstance.CmdbInstanceMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 * @author baiwenping
 * @Title: AlertAutoCheckCMDBDeleteStatusSchedule
 * @Package com.aspire.mirror.alert.server.schedule
 * @Description: TODO
 * @date 2021/1/8 15:30
 */
@Component
@Slf4j
public class AlertAutoCheckCMDBDeleteStatusSchedule {

    @Autowired
    private CmdbInstanceMapper cmdbInstanceMapper;

    @Autowired
    private CmdbAlertRestfulClient cmdbAlertRestfulClient;

    @Scheduled(cron = "0 30 22 * * ?")
    public void run() {
        log.info("自动删除cmdb删除的设备 开始");
        // 查询所有要判断的数据
        List<Map<String, String>> allList = cmdbInstanceMapper.getAllIdAndIp();
        if (CollectionUtils.isEmpty(allList)) {
            return;
        }
        int size = allList.size();
        log.info("查询所有设备信息列表，size: {}", size);
        for (int i = 1; i <= size; i= i + 1000) {
            int endIndex = i + 1000;
            if (endIndex > size) {
                endIndex = size;
            }
            List<Map<String, String>> maps = allList.subList(i, endIndex);
            //调用cmdb接口判断
            List<Map<String, Object>> list = cmdbAlertRestfulClient.checkInstanceDeleteStatus(maps);
            if (CollectionUtils.isEmpty(list)) {
                continue;
            }
            log.info("调用cmdbAlertRestfulClient验证cmdb数据删除状态，size: {}", size);
            // 删除cmdb实例数据
            for (Map<String, Object> map: list) {
                String isDelete = MapUtils.getString(map, "is_delete");
                String id = MapUtils.getString(map, "id");
                String ip = MapUtils.getString(map, "ip");
                if ("1".equals(isDelete)) {
                    log.info("删除cmdb instance实例数据，instance_id : {}, ip : {}", id, ip);
                    cmdbInstanceMapper.deleteByIdAndIp(id, ip);
                }
            }
        }

        log.info("自动删除cmdb删除的设备 结束");

    }
}
