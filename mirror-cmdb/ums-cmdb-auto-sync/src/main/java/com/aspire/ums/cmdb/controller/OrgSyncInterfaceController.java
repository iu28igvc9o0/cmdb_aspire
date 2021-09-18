package com.aspire.ums.cmdb.controller;

import com.alibaba.fastjson.JSONArray;
import com.aspire.ums.cmdb.config.ASPIRE_1_0_0;
import com.aspire.ums.cmdb.schedule.EpcUserSyncTaskServiceV2;
import com.aspire.ums.cmdb.service.OrgSyncInterfaceService;
import com.aspire.ums.cmdb.sync.entity.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


/**
 * 同步接口信息表(OrgSyncInterface)表控制层
 *
 * @author makejava
 * @since 2020-12-03 15:44:34
 */
@RestController
@RequestMapping("/orgSyncInterface")
@Api(tags="SyncController")
public class OrgSyncInterfaceController {
    /**
     * 服务对象
     */
@Autowired
private OrgSyncInterfaceService orgSyncInterfaceService;


   @ASPIRE_1_0_0
    @ApiOperation(value = "增量组织同步", httpMethod = "GET", notes = "增量组织同步")
    @GetMapping("/testSuyanADDOrg")
    public CommonResult testSuyanADDOrg(@ApiParam("传参时间后, 会同步更新或者创建时间大于参数的 数据") String syncTime) throws Exception {
    CommonResult res = new CommonResult();
        orgSyncInterfaceService.syncAddOrgData(syncTime);
        return res;
    }

    /**
     * 同步用户到ldap UMS BPM
     * @param syncTime
     * @return
     * @throws Exception
     */
    @ASPIRE_1_0_0
    @GetMapping("/testSuyanAddUser")
    @ApiOperation(value = "增量用户同步", httpMethod = "GET", notes = "增量用户同步")
    public JSONArray testSuyanAddUser(@ApiParam("传参时间后, 会同步更新或者创建时间大于参数的 数据") String syncTime) throws Exception {

        v2.syncEpcUserData();
        return null;
    }
    /**
     * 同步用户 测试接口>>删除用户
     * * @return
     * @throws Exception
     */
    @ASPIRE_1_0_0
    @PostMapping("/testSuyanAddUserByFile")
    @ApiOperation(value = "同步用户 测试删除用户", httpMethod = "POST", notes = "增量用户同步")
    public JSONArray testSuyanAddUserByFile(@ApiParam("用户数据") @RequestBody String jsonData) throws Exception {
        v2.testSuyanAddUserByFile(jsonData);
        return null;
    }

  /*  @Scheduled(cron = "0 0/8 * * * ?" )
    public JSONArray gettoken() throws Exception {
    s.getSuZhouOautToken();


        return null;
    }*/

@Resource
private EpcUserSyncTaskServiceV2 v2;
}