package com.aspire.ums.cmdb.v2.restful.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aspire.ums.cmdb.cmic.util.EventThreadUtils;
import com.aspire.ums.cmdb.common.Result;
import com.aspire.ums.cmdb.restful.payload.StatisticRequestEntity;
import com.aspire.ums.cmdb.sqlManage.service.CmdbSqlManageService;
import com.aspire.ums.cmdb.util.IpUtils;
import com.aspire.ums.cmdb.util.StringUtils;
import com.aspire.ums.cmdb.v2.instance.service.ICmdbInstanceService;
import com.aspire.ums.cmdb.v2.instance.service.ICmdbInstanceV3Service;
import com.aspire.ums.cmdb.v2.restful.mapper.SlaMsgRecordMapper;
import com.aspire.ums.cmdb.v2.restful.service.ICommonRestfulService;
import com.aspire.ums.cmdb.v2.restful.service.ISlaMagRecordService;
import com.aspire.ums.cmdb.v3.condication.payload.CmdbV3AccessUser;
import com.aspire.ums.cmdb.v3.condication.service.ICmdbV3AccessUserService;
import com.aspire.ums.cmdb.v3.config.payload.CmdbConfig;
import com.aspire.ums.cmdb.v3.config.service.ICmdbConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.UnknownHostException;
import java.util.*;

/**
 * Copyright (C), 2015-2020, 卓望数码有限公司
 * FileName: CommonRestfulServiceImpl
 * Author:   zhu.juwang
 * Date:     2020/3/13 11:22
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Service
@Slf4j
public class SlaMsgRecordImpl implements ISlaMagRecordService {

    
    @Autowired
    private SlaMsgRecordMapper slaMsgRecordMapper;

    @Override
    public List<Map<String, Object>> queryMsgRecord(Map<String, Object> params) {
     
        return slaMsgRecordMapper.queryMsgRecord(params);
    }



    @Override
    public void insert(Map<String, Object> Object) {
         slaMsgRecordMapper.insert(Object);;
    }
    
    @Override
    public List<Map<String,Object>> listNoWorkingDays(){
    	return slaMsgRecordMapper.listNoWorkingDays();
    }
}
