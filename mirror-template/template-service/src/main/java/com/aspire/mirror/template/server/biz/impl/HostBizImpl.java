package com.aspire.mirror.template.server.biz.impl;

import com.aspire.mirror.template.api.dto.GeneralResponse;
import com.aspire.mirror.template.api.dto.vo.MirrorHostVO;
import com.aspire.mirror.template.server.biz.HostBiz;
import com.aspire.mirror.template.server.dao.HostDao;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 自监控设备业务层实现
 * <p>
 * 项目名称:  mirror平台
 * 类名称:    HostBizImpl
 * 类描述:    自监控设备业务层实现
 * 创建人:    JinSu
 * 创建时间:  2020/11/4 10:19
 * 版本:      v1.0
 */
@Service
public class HostBizImpl implements HostBiz {
    @Autowired
    private HostDao hostDao;

    /**
     * 批量新增自监控设备
     * @param hostList 主机列表
     * @return GeneralResponse 通用返回对象
     */
    @Override
    public GeneralResponse batchCreateHosts(List<MirrorHostVO> hostList) {
        List<MirrorHostVO> addList = Lists.newArrayList();
        for (MirrorHostVO hostVo : hostList) {
            MirrorHostVO host =  hostDao.selectByIpAndPool(hostVo.getIp(), hostVo.getPool());
            if (host == null) {
                addList.add(hostVo);
            }
        }
        if (!addList.isEmpty()) {
            hostDao.batchInsertHost(addList);
        }
        return new GeneralResponse();
    }

    @Override
    public MirrorHostVO getHostByIpAndPool(String ip, String pool) {
        return hostDao.selectByIpAndPool(ip, pool);
    }

    @Override
    public List<MirrorHostVO> getMonitorHostByProxyIdentity(String proxyIdentity) {
        return hostDao.selectByProxyIdentity(proxyIdentity);
    }

}
