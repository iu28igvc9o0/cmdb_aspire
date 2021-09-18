package com.aspire.mirror.elasticsearch.api.service.cmdb;

import com.aspire.mirror.elasticsearch.api.dto.cmdb.Result;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: ICmdbToESService
 * Author:   hangfang
 * Date:     2020/1/15
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@RequestMapping("/cmdb/public")
public interface ICmdbToESService {

    @PostMapping("/insert")
    Map<String, String> insert(@RequestBody List<Map<String, Object>> data,
                @RequestParam("index") String index,
                @RequestParam("type") String type);

    @DeleteMapping("/delete")
    Map<String, Object> deleteIndex(@RequestParam("index") String index);

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
     * 	"result": ["id", "device_name", "ip"],
     *  "index": "", //索引名称
     *  "type": "",  //类型
     *  "currentPage": 1, // 当前页数
     *  "pageSize": 100 // 每页记录数
     * }
     * @return ES中符合的记录集
     */
    @PostMapping("/listByPage")
    Result<Map<String, Object>> list(@RequestBody Map<String, Object> querySetting);

    /**
     * 根据ID获取ES数据
     * @param id ID
     * @param index 索引名称
     * @param type 查询类型
     * @return ES中符合的记录集
     */
    @GetMapping("/getById")
    Map<String, Object> getById(@RequestParam("id") String id,
                                     @RequestParam("index") String index,
                                     @RequestParam("type") String type);

    /**
     * 分组统计ES数据
     * @param statsSetting 统计配置
     * @param index es索引
     * @param type 类型
     * @return
     */
    @PostMapping("/stats")
    List<Map<String, Object>> stats(@RequestBody Map<String, Object> statsSetting,
                                    @RequestParam("index") String index,
                                    @RequestParam("type") String type);

    /**
     * 根据设备IP地址及资源池名称查询设备详细信息
     * @param params
     * @return
     */
    @PostMapping("/queryDeviceByIpAndIdcType")
    List<Map<String, Object>> queryDeviceByIpAndIdcType(@RequestBody Map<String, Object> params);
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
    @PostMapping("/detail")
    Map<String, Object> queryDetail(@RequestBody Map<String, Object> querySetting);

}
