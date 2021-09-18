package com.aspire.mirror.template.server.biz;

import com.aspire.mirror.template.api.dto.GeneralResponse;
import com.aspire.mirror.template.api.dto.vo.MirrorHostVO;

import java.util.List;

/**
 * 主机业务层接口
 *
 * 项目名称: mirror平台
 * 包:      com.aspire.mirror.template.server.biz
 * 类名称:   HostBiz.java
 * 类描述:   主机业务层接口
 * 创建人:   JinSu
 * 创建时间:
 */
public interface HostBiz {
    /**
     * 批量新增自监控设备
     * @param hostList
     * @return
     */
    GeneralResponse batchCreateHosts(List<MirrorHostVO> hostList);

    MirrorHostVO getHostByIpAndPool(String ip, String pool);

    List<MirrorHostVO> getMonitorHostByProxyIdentity(String proxyIdentity);
}
