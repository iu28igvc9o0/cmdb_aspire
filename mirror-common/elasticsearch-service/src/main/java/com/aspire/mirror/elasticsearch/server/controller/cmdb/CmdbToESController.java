package com.aspire.mirror.elasticsearch.server.controller.cmdb;

import com.aspire.mirror.elasticsearch.api.dto.cmdb.Result;
import com.aspire.mirror.elasticsearch.api.service.cmdb.ICmdbToESService;
import com.aspire.mirror.elasticsearch.server.controller.cmdb.service.ICmdbESService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: CmdbToESController
 * Author:   hangfang
 * Date:     2020/1/15
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@RestController
@Slf4j
public class CmdbToESController implements ICmdbToESService {

    @Autowired
    private ICmdbESService cmdbESService;

    /* 新增ES数据
     * setBulkActions(1000):每添加1000个request，执行一次bulk操作
     * setBulkSize(new ByteSizeValue(5, ByteSizeUnit.MB)):每达到5M的请求size时，执行一次bulk操作
     * setFlushInterval(TimeValue.timeValueSeconds(5)):每5s执行一次bulk操作
     * setConcurrentRequests(1):默认是1，表示积累bulk requests和发送bulk是异步的，其数值表示发送bulk的并发线程数，设置为0表示二者同步的
     * setBackoffPolicy(BackoffPolicy.exponentialBackoff(TimeValue.timeValueMillis(100),3)):当ES由于资源不足发生异常
       EsRejectedExecutionException重試策略：默认（50ms, 8）,
       策略算法：start + 10 * ((int) Math.exp(0.8d * (currentlyConsumed)) - 1)
     **/
    @Override
    public Map<String, String> insert(@RequestBody List<Map<String, Object>> data,
                       @RequestParam("index") String index,
                       @RequestParam("type") String type) {
        return this.cmdbESService.insert(data, index, type);
    }

    @Override
    public Map<String, Object> deleteIndex(@RequestParam("index") String index) {
        return this.cmdbESService.deleteIndex(index);
    }

    @Override
    public Result<Map<String, Object>> list(@RequestBody Map<String, Object> querySetting) {
        return this.cmdbESService.list(querySetting);
    }

    @Override
    public Map<String, Object> getById(String id, String index, String type) {
        return this.cmdbESService.getById(id, index, type);
    }

    @Override
    public List<Map<String, Object>> stats(@RequestBody Map<String, Object> statsSetting,
                                           @RequestParam("index") String index,
                                           @RequestParam("type") String type) {
        return this.cmdbESService.stats(statsSetting, index, type);
    }

    @Override
    public List<Map<String, Object>> queryDeviceByIpAndIdcType(@RequestBody Map<String, Object> params) {
        return this.cmdbESService.queryDeviceByIpAndIdcType(params);
    }

    @Override
    public Map<String, Object> queryDetail(@RequestBody Map<String, Object> querySetting) {
        return this.cmdbESService.queryDetail(querySetting);
    }
}
