package com.aspire.mirror.alert.server.biz.network.impl;

import com.aspire.mirror.alert.server.biz.network.IAlertNetworkBiz;
import com.aspire.mirror.alert.server.dao.monitor.AlertNetworkMapper;
import com.aspire.mirror.alert.server.dao.monitor.po.AlertNorm;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author hewang
 * @version 1.0
 * @date 2021/3/11 20:48
 */
@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class AlertNetworkBizImpl implements IAlertNetworkBiz {

    @Autowired
    private AlertNetworkMapper mapper;



    private static final String SUCCESS = "SUCCESS";
    private static final String ERROR = "ERROR";
    private static final Integer CASE = 1;

    @Override
    public List<AlertNorm> queryNetworkPortIndicators(String indicatorName, int pageNum, int pageSize) {

        return mapper.selectIndicatorsConfig(indicatorName, pageNum, pageSize);
    }

    @Override
    public String addNetworkPortIndicators(List<AlertNorm> responses) {
        try {
            int count = mapper.insertIndicators(responses);
            if (count > 0) {
                return SUCCESS;
            }
            return ERROR;
        } catch (Exception e) {
            log.error("insert Indicators error", e.getMessage());
            throw new RuntimeException("insert Indicators error");
        }
    }

    @Override
    public List<AlertNorm> queryTopReportTypeConfiguration(String userName) {
        if (StringUtils.isBlank(userName)) {
            log.info("req is null");
            throw new RuntimeException("req is null");
        }
        //根据用户名称查询指标
        List<AlertNorm> list = mapper.selectIndicatorsByName(userName);
        if (CollectionUtils.isEmpty(list)) {
            int count = mapper.selectIndicatorsByCount(userName);
            if (count == 0) {
                //查询用户名为空的指标
                String names = "";
                list = mapper.selectIndicatorsByName(names);
                if (CollectionUtils.isNotEmpty(list)) {
                    for (AlertNorm alertNormDTO : list) {
                        alertNormDTO.setUserName(userName);
                    }
                    //插入指标表默认数据
                    mapper.insertIndicators(list);
                    //查询指标表数据
                    list=mapper.selectIndicatorsByName(userName);
                }
            }
        }
        return list;
    }

    @Override
    public String updateTopReportTypeConfiguration(List<AlertNorm> list) {
        try {
            String userName = list.get(0).getUserName();
            if (StringUtils.isBlank(userName)) {
                return ERROR;
            }
            mapper.updateIndicators(list);
        } catch (Exception e) {
            log.error("update Indicators error", e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
        return SUCCESS;
    }

    @Override
    public String deleteTopReportTypeConfiguration(Integer id) {
        try {
            mapper.deleteIndicators(id);
        } catch (Exception e) {
            log.error("delete Indicators error", e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
        return SUCCESS;

    }
}
