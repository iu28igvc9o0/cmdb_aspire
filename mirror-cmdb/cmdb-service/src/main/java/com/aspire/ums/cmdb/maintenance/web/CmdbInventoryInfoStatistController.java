package com.aspire.ums.cmdb.maintenance.web;

import com.aspire.ums.cmdb.common.Result;
import com.aspire.ums.cmdb.maintenance.ICmdbInventoryInfoStatistAPI;
import com.aspire.ums.cmdb.maintenance.payload.CmdbInventoryInfoStatistRequest;
import com.aspire.ums.cmdb.maintenance.service.ICmdbInventoryInfoStatistService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @ClassName CmdbInventoryInfoStatistController
 * @Description 维保设备信息与cmdb存量信息比对
 * @Author luowenbo
 * @Date 2020/2/16 15:51
 * @Version 1.0
 */
@RestController
@Slf4j
public class CmdbInventoryInfoStatistController implements ICmdbInventoryInfoStatistAPI {

    @Autowired
    private ICmdbInventoryInfoStatistService cmdbInventoryInfoStatistService;

    @Override
    public List<Map<String, Object>> firstLayer() {
        log.info("Request into CmdbInventoryInfoStatistController.firstLayer()  params -> {}");
        return cmdbInventoryInfoStatistService.firstLayer();
    }

    @Override
    public Result<Map<String, Object>> secondLayer(@RequestBody CmdbInventoryInfoStatistRequest req) {
        log.info("Request into CmdbInventoryInfoStatistController.secondLayer()  params -> {}",req);
        return cmdbInventoryInfoStatistService.secondLayer(req);
    }

    @Override
    public Result<Map<String, Object>> thirdLayer(@RequestBody CmdbInventoryInfoStatistRequest req) {
        log.info("Request into CmdbInventoryInfoStatistController.thirdLayer()  params -> {}",req);
        return cmdbInventoryInfoStatistService.thirdLayer(req);
    }
}
