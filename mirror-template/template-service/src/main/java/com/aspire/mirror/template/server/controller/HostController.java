package com.aspire.mirror.template.server.controller;

import com.aspire.mirror.template.api.dto.GeneralResponse;
import com.aspire.mirror.template.api.dto.HostBatchCreateRequest;
import com.aspire.mirror.template.api.dto.vo.MirrorHostVO;
import com.aspire.mirror.template.api.service.HostService;
import com.aspire.mirror.template.server.biz.HostBiz;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 主机控制类
 * <p>
 * 项目名称:  mirror平台
 * 类名称:    HostController
 * 类描述:    主机控制类
 * 创建人:    JinSu
 * 创建时间:  2020/11/4 10:13
 * 版本:      v1.0
 */
@RestController
@Slf4j
public class HostController implements HostService {
    @Autowired
    private HostBiz hostBiz;
    @Override
    public GeneralResponse batchCreateHosts(@RequestBody HostBatchCreateRequest request) {
        if (request == null || request.getHostList() == null) {
            log.error("HostController[batchCreateHosts] param is null");
            return new GeneralResponse(false, "批量创建主机信息参数为空");
        }
        return hostBiz.batchCreateHosts(request.getHostList());
    }

    @Override
    public MirrorHostVO getHostByIpAndPool(@RequestParam("ip") String ip, @RequestParam("pool") String pool) {
        if (StringUtils.isEmpty(ip) || StringUtils.isEmpty(pool)) {
            log.error("HostController[getHostByIpAndPool] param is invalid");
            return null;
        }
        return hostBiz.getHostByIpAndPool(ip, pool);
    }

    @Override
    public List<MirrorHostVO> getMonitorHostByProxyIdentity(@PathVariable("proxyIdentity") String proxyIdentity) {
        if (StringUtils.isEmpty(proxyIdentity)) {
            log.error("HostController[getMonitorHostByProxyIdentity] param is invalid");
            return null;
        }
        return hostBiz.getMonitorHostByProxyIdentity(proxyIdentity);
    }
}
