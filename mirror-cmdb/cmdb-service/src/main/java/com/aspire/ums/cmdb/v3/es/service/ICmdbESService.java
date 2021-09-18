package com.aspire.ums.cmdb.v3.es.service;

import com.aspire.mirror.elasticsearch.api.dto.cmdb.Result;

import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2015-2020, 卓望数码有限公司
 * FileName: ICmdbESService
 * Author:   zhu.juwang
 * Date:     2020/2/17 9:45
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
public interface ICmdbESService {
    /**
     * 新增es数据
     * @param data 数据
     * @param index 索引
     * @param type 类型
     * @return
     */
    Map<String, String> insert(List<Map<String, Object>> data, String index, String type);

    /**
     * 删除索引
     * @param index 索引
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

    /**
     * 根据ID获取ES数据
     * @param id ID
     * @param index 索引名称
     * @param type 查询类型
     * @return ES中符合的记录集
     */
    Map<String, Object> getById(String id, String index, String type);

    /**
     * 写入ES模型实例数据
     * @param moduleId
     * @return
     */
    Map<String, String> writeModuleData(String moduleId);

    /**
     * @param moduleId 模型ID
     * @param instanceId 实例ID
     * 异步写入ES模型实例数据
     */
    void asyncRefresh(String moduleId, String instanceId);

    /**
     * 根据设备IP地址及资源池名称查询设备详细信息
     * @param params
     * @return
     */
    List<Map<String, Object>> queryDeviceByIpAndIdcType(Map<String, Object> params);

    /**
     * 统计ES数据结果
     * @param params 请求参数
     * @param index 索引名称
     * @param type 索引类型
     * @return
     */
    List<Map<String, Object>> stats(Map<String, Object> params, String index, String type);
}
