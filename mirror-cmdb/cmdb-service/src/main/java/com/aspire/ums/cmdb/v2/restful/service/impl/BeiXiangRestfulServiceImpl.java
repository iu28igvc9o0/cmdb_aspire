package com.aspire.ums.cmdb.v2.restful.service.impl;

import com.aspire.ums.cmdb.v2.restful.mapper.BeiXiangRestfulMapper;
import com.aspire.ums.cmdb.v2.restful.service.IBeiXiangRestfulService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: BeiXiangRestfulServiceImpl
 * Author:   hangfang
 * Date:     2020/1/7
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Service
@Slf4j
public class BeiXiangRestfulServiceImpl implements IBeiXiangRestfulService {

    @Autowired
    private BeiXiangRestfulMapper beiXiangRestfulMapper;
    @Override
    public List<Map<String, Object>> countIdcTypePer(Integer top) {
        return beiXiangRestfulMapper.countIdcTypePer(top);
    }

    @Override
    public List<Map<String, Object>> countIdcDeviceNumber(Integer top) {
        return beiXiangRestfulMapper.countIdcDeviceNumber(top);
    }

    @Override
    public List<Map<String, Object>> countDepart1BizPer(Integer top) {
        return beiXiangRestfulMapper.countDepart1BizPer(top);
    }

    @Override
    public List<Map<String, Object>> countDepart1BizNumber(Integer top) {
        return beiXiangRestfulMapper.countDepart1BizNumber(top);
    }

    @Override
    public List<Map<String, Object>> countDepart1DeviceNumber(Integer top) {
        return beiXiangRestfulMapper.countDepart1DeviceNumber(top);
    }

    @Override
    public List<Map<String, Object>> countDeviceByIdcType(String idcType) {
        return beiXiangRestfulMapper.countDeviceByIdcType(idcType);
    }

    @Override
    public List<Map<String, Object>> countDepart1Device(Integer top) {
        return beiXiangRestfulMapper.countDepart1Device(top);
    }

    @Override
    public List<Map<String, Object>> roomDevicePer() {
        return beiXiangRestfulMapper.roomDevicePer();
    }
}
