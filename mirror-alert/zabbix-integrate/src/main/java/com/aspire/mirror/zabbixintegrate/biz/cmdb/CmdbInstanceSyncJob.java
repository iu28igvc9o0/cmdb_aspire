package com.aspire.mirror.zabbixintegrate.biz.cmdb;

import com.aspire.mirror.zabbixintegrate.dao.ZCmdbInstanceDao;
import com.aspire.mirror.zabbixintegrate.dao.po.ZCmdbInstance;
import com.aspire.mirror.zabbixintegrate.daoAlert.CmdbInstanceDao;
import com.aspire.mirror.zabbixintegrate.daoAlert.po.CmdbInstance;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author baiwp
 * @title: CmdbInstanceSyncJob
 * @projectName zabbix-integrate
 * @description: TODO
 * @date 2019/7/2514:27
 */
@Slf4j
@Component
@ConditionalOnProperty(value = "monitor.cmdb.instance.open", havingValue = "true")
public class CmdbInstanceSyncJob {
    @Value("${monitor.cmdb.instance.field}")
    private String field;
    @Value("${monitor.cmdb.instance.fieldValues}")
    private String fieldValues;
    private List<String> fieldValueList;
    @Value("${monitor.cmdb.instance.batchCount:200}")
    private Integer batchCount;

    @Autowired
    private ZCmdbInstanceDao zCmdbInstanceDao;
    @Autowired
    private CmdbInstanceDao cmdbInstanceDao;

    /**
     * @return ${return_type}
     * @throws
     * @description: TODO
     * @author baiwenping
     * @date 2019/7/25 14:46
     */
    @PostConstruct
    private void init() {
        if(StringUtils.isNotEmpty(fieldValues)) {
            String[] idcTypeArr = fieldValues.split(",");
            List<String> fieldValueList = new ArrayList<>();
            for (String idcType : idcTypeArr) {
                fieldValueList.add(idcType);
            }
            this.fieldValueList = fieldValueList;
        }
//        int count = zCmdbInstanceDao.count();
//        if(count == 0) {
//            syncNew();
//        }
    }

    /**
     *
     * @return ${return_type}
     * @throws
     * @description: TODO
     * @author baiwenping
     * @date 2019/7/25 14:45
     */
    @Scheduled(cron = "${monitor.cmdb.instance.cron: 0 0 1 * * ?}")
    public void run() {
        syncNew();
    }

    /**
     * 同步cmdb数据到zabbix
     * @return ${return_type}
     * @throws
     * @description: TODO
     * @author baiwenping
     * @date 2019/7/25 14:46
     */
    @Transactional
    public void syncNew() {
        log.info("start sync cmdb instance!");
        Map<String, Object> queryMap = new HashMap<>();

        if(null != fieldValueList && !org.springframework.util.StringUtils.isEmpty(field)) {
            log.info("cmdb field is: {}, field values are: {}.", field, fieldValueList);
            queryMap.put("fieldValues", fieldValueList);
            queryMap.put("field", field);
        }
        List<Map<String, Object>> mapList = cmdbInstanceDao.selectAllByField(queryMap);

        if (mapList.isEmpty()) {
            return;
        }
        log.info(" cmdb instance size is {}!", mapList.size());
        zCmdbInstanceDao.deleteAll();
        List<Map<String, Object>> zCmdbInstanceList = new ArrayList<>();
        for (int i = 0; i < mapList.size(); i++) {
            Map<String, Object> map =mapList.get(i);
            // 批量保存
            if (i != 0 && i % batchCount == 0) {
                zCmdbInstanceDao.insertBatchNew(zCmdbInstanceList);
                zCmdbInstanceList.clear();
            }
            if (map == null) {
                continue;
            }
            zCmdbInstanceList.add(map);
        }

        if (!zCmdbInstanceList.isEmpty()) {
            zCmdbInstanceDao.insertBatchNew(zCmdbInstanceList);
            zCmdbInstanceList.clear();
        }
        mapList.clear();
        log.info("end sync cmdb instance!");
    }

    /**
     * 同步cmdb数据到zabbix
     * @return ${return_type}
     * @throws
     * @description: TODO
     * @author baiwenping
     * @date 2019/7/25 14:46
     */
    @Deprecated
    private void sync() {

        Map<String, Object> queryMap = new HashMap<>();
        if(null != fieldValueList && !org.springframework.util.StringUtils.isEmpty(field)) {
            log.info("cmdb field is: {}, field values are: {}.", field, fieldValueList);
            queryMap.put("fieldValues", fieldValueList);
            queryMap.put("field", field);
        }
        queryMap.put("batchCount", this.batchCount);
        //查询cmdb实例总数，用于批量处理
        int count = cmdbInstanceDao.count(queryMap);
        if (count == 0) {
            return ;
        }
        zCmdbInstanceDao.deleteAll();

        int begin = 0;
        //批量新增
        while (begin < count) {
            queryMap.put("begin", begin);
            List<CmdbInstance> cmdbInstancesList = cmdbInstanceDao.list(queryMap);
            List<ZCmdbInstance> zCmdbInstanceList = fromPo(cmdbInstancesList);
            zCmdbInstanceDao.insertBatch(zCmdbInstanceList);
            begin += batchCount;
        }
    }

    /**
     * 转换bean
     * @param cmdbInstancesList
     * @return
     */
    public static List<ZCmdbInstance> fromPo(List<CmdbInstance> cmdbInstancesList) {
        List<ZCmdbInstance> zCmdbInstanceList = new ArrayList<>();
        if (CollectionUtils.isEmpty(cmdbInstancesList)) {
            return zCmdbInstanceList;
        }

        for (CmdbInstance cmdbInstance : cmdbInstancesList) {
            ZCmdbInstance zCmdbInstance = new ZCmdbInstance();
            BeanUtils.copyProperties(cmdbInstance, zCmdbInstance);
            zCmdbInstanceList.add(zCmdbInstance);
        }
        return zCmdbInstanceList;
    }
}
