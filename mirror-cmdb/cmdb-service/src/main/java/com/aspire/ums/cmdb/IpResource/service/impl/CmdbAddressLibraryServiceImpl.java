package com.aspire.ums.cmdb.IpResource.service.impl;

import com.aspire.ums.cmdb.IpResource.mapper.CmdbAddressLibraryMapper;
import com.aspire.ums.cmdb.IpResource.service.CmdbAddressLibraryService;
import com.aspire.ums.cmdb.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @Author: fanshenquan
 * @Datetime: 2020/6/30 16:04
 */
@Service("CmdbAddressLibraryService")
@Slf4j
public class CmdbAddressLibraryServiceImpl implements CmdbAddressLibraryService {

    @Resource
    private CmdbAddressLibraryMapper cmdbAddressLibraryMapper;

    @Override
    public Integer updateInnerSegment(Map<String, Object> map) {
        return cmdbAddressLibraryMapper.updateInnerSegment(map);
    }

    @Override
    public Integer updateIPV6Segment(Map<String, Object> map) {
        return cmdbAddressLibraryMapper.updateIPV6Segment(map);
    }

    @Override
    public Integer updatePublicSegment(Map<String, Object> map) {
        return cmdbAddressLibraryMapper.updatePublicSegment(map);
    }

    @Override
    public void delIpBySegment(Map<String, String> map) {
        try {
            if ( "inner".equals(map.get("ipDelType")) && (StringUtils.isEmpty(map.get("idcVal")) || StringUtils.isEmpty(map.get("segmentAddress")))) {
                log.info("删除参数不全:{}",map);
                return;
            }
            if ("inner".equals(map.get("ipDelType"))) {
                cmdbAddressLibraryMapper.delInnerIp(map);
            } else if ("public".equals(map.get("ipDelType"))) {
                cmdbAddressLibraryMapper.delPublicIp(map);
            } else if ("ipv6".equals(map.get("ipDelType"))) {
                cmdbAddressLibraryMapper.delIpv6Ip(map);
            }
        } catch (Exception e) {
            log.error("逻辑删除IP操作数据失败,{}",e.getMessage());
        }
    }
}
