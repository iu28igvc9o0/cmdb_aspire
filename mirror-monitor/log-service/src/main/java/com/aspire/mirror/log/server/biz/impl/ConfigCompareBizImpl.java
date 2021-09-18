package com.aspire.mirror.log.server.biz.impl;

import com.aspire.mirror.common.entity.PageResponse;
import com.aspire.mirror.log.server.biz.ConfigCompareBiz;
import com.aspire.mirror.log.server.dao.ConfigCompareDao;
import com.aspire.mirror.log.server.dao.ConfigCompareLogsDao;
import com.aspire.mirror.log.server.dao.po.ConfigCompare;
import com.aspire.mirror.log.server.dao.po.ConfigCompareLogs;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

/**
 * @author baiwenping
 * @Title: ConfigCompareBizImpl
 * @Package com.aspire.mirror.log.server.biz.impl
 * @Description: TODO
 * @date 2020/12/21 10:34
 */
@Service
public class ConfigCompareBizImpl implements ConfigCompareBiz {

    @Autowired
    private ConfigCompareLogsDao configCompareLogsDao;
    @Autowired
    private ConfigCompareDao configCompareDao;

    /**
     *
     * @param masterIp
     * @param backupIp
     * @param compareTimeFrom
     * @param compareTimeTo
     * @param compareType
     * @param pageNum
     * @param pageSize
     * @return
     */
    public PageResponse<ConfigCompare> getCompareList(String masterIp,
                                                   String backupIp,
                                                   String compareTimeFrom,
                                                   String compareTimeTo,
                                                   String compareType,
                                                   Integer pageNum,
                                                   Integer pageSize) {
        Map<String, Object> map = Maps.newHashMap();
        map.put("masterIp", masterIp);
        map.put("backupIp", backupIp);
        map.put("compareTimeFrom", compareTimeFrom);
        map.put("compareTimeTo", compareTimeTo);
        map.put("compareType", compareType);
        int begin = (pageNum - 1) * pageSize;
        map.put("begin", begin < 0 ? 0 : begin);
        map.put("pageSize", pageSize);
        int pageWithCount = configCompareDao.findPageWithCount(map);
        List<ConfigCompare> pageWithResult = configCompareDao.findPageWithResult(map);
        PageResponse<ConfigCompare> pageResponse = new PageResponse<>();
        pageResponse.setCount(pageWithCount);
        pageResponse.setResult(pageWithResult);
        return pageResponse;
    }

    /**
     * 根据id列表查询
     * @param ids
     * @return
     */
    public List<ConfigCompare> getCompareListByIds (List<Integer> ids) {
        return configCompareDao.getByIds(ids);
    }

    public ConfigCompare getCompareById (Integer id) {
        return configCompareDao.get(id);
    }

    /**
     * 新增
     * @param configCompare
     */
    public void insert(@RequestBody ConfigCompare configCompare) {
        configCompareDao.insert(configCompare);
    }

    /**
     * 新增
     * @param configCompare
     */
    public void update(ConfigCompare configCompare) {
        configCompareDao.update(configCompare);
    }

    /**
     *
     * @param configCompareList
     */
    public void insertBatch(List<ConfigCompare> configCompareList) {
        configCompareDao.insertBatch(configCompareList);
    }
    /**
     * @param compareId
     * @return
     */
    @Override
    public List<ConfigCompareLogs> getLogs(Integer compareId) {
        return configCompareLogsDao.listByCompareId(compareId);
    }

    /**
     * 新增比对记录
     * @param configCompareLogs
     */
    public void insertLogs(ConfigCompareLogs configCompareLogs) {
        configCompareLogsDao.insert(configCompareLogs);
    }
}
