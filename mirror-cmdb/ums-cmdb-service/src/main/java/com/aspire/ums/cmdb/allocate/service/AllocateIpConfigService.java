package com.aspire.ums.cmdb.allocate.service;

import com.aspire.ums.cmdb.allocate.entity.AllocateIpConfigDetail;
import com.aspire.ums.cmdb.allocate.entity.AllocateIpConfigListReq;
import com.aspire.ums.cmdb.allocate.entity.AllocateIpConfigRes;
import com.aspire.ums.cmdb.allocate.entity.Result;

import java.util.List;
import java.util.Map;

public interface AllocateIpConfigService {


    Result<AllocateIpConfigRes> getAllocateIpConfigData(int pageNum,
                                                        int startPageNum,
                                                        int pageSize,
                                                        int vpnId,
                                                        int networkId,
                                                        String bizSystem,
                                                        String ip,
                                                        String privateIp,
                                                        boolean isAdd);

    String insertAllocateIpConfig(List<AllocateIpConfigDetail> request, String name);

    String deleteAllocateIpConfigById(String ids, String name);

    List<Map<String, Object>> exportAllocateIpConfig(AllocateIpConfigListReq request);

    List<Map<String, Object>> getVpnData();

    List<Map<String, Object>> getNetworkById( long id);

}
