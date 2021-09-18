package com.aspire.ums.cmdb.cmic.web;

import com.aspire.ums.cmdb.cmic.ICmdbIPAPI;
import com.aspire.ums.cmdb.cmic.service.ICmdbIPService;
import com.aspire.ums.cmdb.common.Result;
import com.aspire.ums.cmdb.helper.JDBCHelper;
import com.aspire.ums.cmdb.sqlManage.CmdbSqlManage;
import java.util.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Copyright (C), 2015-2020, 卓望数码有限公司
 * FileName: CmdbIPController
 * Author:   zhu.juwang
 * Date:     2020/5/27 10:43
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@RestController
@Slf4j
public class CmdbIPController implements ICmdbIPAPI {

    @Autowired
    private ICmdbIPService ipService;
    @Autowired
    private JDBCHelper jdbcHelper;

    @Override
    public Map<String, Object> statisticsIpUseInfo(@RequestBody(required = false) Map<String, Object> conditions) {
        return ipService.statisticsIpUseInfo(conditions);
    }

    public List<String> test() {
        List<String> resultList = new LinkedList<>();
        String sql = "select * from cmdb_process_handler_manager cc where 1=1 and id in " +
        "<foreach collection=\"ids\" item=\"item\" separator=\",\" open=\"(\" close=\")\">" +
                "#{item} \n" +
         "</foreach>";
        Map<String, Object> params = new HashMap<>();
        params.put("ids", Arrays.asList("1", "2"));
        CmdbSqlManage cmdbSqlManage = new CmdbSqlManage();
        cmdbSqlManage.setChartSql(sql);
        cmdbSqlManage.setNeedAuth(0);
        Result<Map<String, Object>> queryList = jdbcHelper.getPagedList(
                cmdbSqlManage,  null, null, null, params,1,2);
//        FreemarkerUtil.generatorFile(new HashMap());
        return resultList;
    }
}
