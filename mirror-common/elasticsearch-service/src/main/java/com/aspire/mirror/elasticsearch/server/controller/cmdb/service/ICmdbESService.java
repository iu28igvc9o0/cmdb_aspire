package com.aspire.mirror.elasticsearch.server.controller.cmdb.service;

import com.aspire.mirror.elasticsearch.api.dto.cmdb.Result;

import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2015-2020, 卓望数码有限公司
 * FileName: ICmdbESService
 * Author:   zhu.juwang
 * Date:     2020/2/14 17:28
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
public interface ICmdbESService {
    /* 新增ES数据
     * setBulkActions(1000):每添加1000个request，执行一次bulk操作
     * setBulkSize(new ByteSizeValue(5, ByteSizeUnit.MB)):每达到5M的请求size时，执行一次bulk操作
     * setFlushInterval(TimeValue.timeValueSeconds(5)):每5s执行一次bulk操作
     * setConcurrentRequests(1):默认是1，表示积累bulk requests和发送bulk是异步的，其数值表示发送bulk的并发线程数，设置为0表示二者同步的
     * setBackoffPolicy(BackoffPolicy.exponentialBackoff(TimeValue.timeValueMillis(100),3)):当ES由于资源不足发生异常
       EsRejectedExecutionException重試策略：默认（50ms, 8）,
       策略算法：start + 10 * ((int) Math.exp(0.8d * (currentlyConsumed)) - 1)
     **/
    Map<String, String> insert(List<Map<String, Object>> data, String index, String type);

    /**
     * 删除es索引
     * @param index es索引名称
     * @return
     */
    Map<String, Object> deleteIndex(String index);

    /**
     * 获取ES数据集合
     * @param querySetting 查询设置 包含:
     * {
     * 	"params": [{  // 查询参数设置
     * 		"operator": "操作符",
     * 		"filed": "字段名称",
     * 		"value": "字段值"
     * 	}],
     * 	"sort": [{  // 排序信息设置
     * 		"filed": "排序字段",
     * 		"type": "排序方式"
     * 	}],
     * 	"index": "", //索引名称
     * 	"type": "",  //类型
     * 	"currentPage": 1, // 当前页数
     * 	"pageSize": 100 // 每页记录数
     * }
     * @return ES中符合的记录集
     */
    Result<Map<String, Object>> list(Map<String, Object> querySetting);

    /**
     * 根据ID获取ES数据
     * @param id ID
     * @param index 索引名称
     * @param type 查询类型
     * @return ES中符合的记录集
     */
    Map<String, Object> getById(String id, String index, String type);

    /**
     * 分组统计ES数据
     * @param statsSetting 统计配置
     * @param index es索引
     * @param type 类型
     * @return
     */
    List<Map<String, Object>> stats(Map<String, Object> statsSetting, String index, String type);

    /**
     * 根据设备IP地址及资源池名称查询设备详细信息
     * @param params
     * @return
     */
    List<Map<String, Object>> queryDeviceByIpAndIdcType(Map<String, Object> params);

    /**
     * 获取ES数据集合
     * @param querySetting 查询设置 包含:
     * {
     * 	"params": [{  // 查询参数设置
     * 		"operator": "操作符",
     * 		"filed": "字段名称",
     * 		"value": "字段值"
     * 	}],
     * 	"result": [] //返回结果
     * 	"index": "", //索引名称
     * 	"type": "",  //类型
     * }
     * @return ES中符合的记录集
     */
    Map<String, Object> queryDetail(Map<String, Object> querySetting);
}
