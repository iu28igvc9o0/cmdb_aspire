package com.aspire.ums.cmdb.v2.index.serivce.impl;

import com.aspire.ums.cmdb.index.payload.ItCloudScreenRequest;
import com.aspire.ums.cmdb.index.payload.ScreenOnlineInfo;
import com.aspire.ums.cmdb.util.UUIDUtil;
import com.aspire.ums.cmdb.v2.index.mapper.ItCloudScreenOnlineInfoMapper;
import com.aspire.ums.cmdb.v2.index.serivce.ItCloudScreenOnlineInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @ClassName ItCloudScreenOnlineInfoServiceImpl
 * @Description 租户大屏上线信息——服务实现类
 * @Author luowenbo
 * @Date 2020/4/23 20:43
 * @Version 1.0
 */
@Service
public class ItCloudScreenOnlineInfoServiceImpl implements ItCloudScreenOnlineInfoService {
    @Autowired
    private ItCloudScreenOnlineInfoMapper mapper;

    @Override
    public void batchInsert(List<ScreenOnlineInfo> list) {
        if(list != null && !list.isEmpty()) {
            for(ScreenOnlineInfo item : list) {
                item.setId(UUIDUtil.getUUID());
                item.setInsertTime(new Date());
                item.setIsDelete("0");
            }
            mapper.batchInsert(list);
        }
    }

    @Override
    public void insert(ScreenOnlineInfo obj) {
        if(null != obj) {
            obj.setId(UUIDUtil.getUUID());
            obj.setInsertTime(new Date());
            obj.setIsDelete("0");
            mapper.insert(obj);
        }
    }

    @Override
    public void delete(ItCloudScreenRequest req) {
        mapper.delete(req);
    }
}
