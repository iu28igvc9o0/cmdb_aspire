package com.aspire.mirror.redis.api;

import com.aspire.mirror.redis.api.payload.KeyValueRedisEntity;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: IRedisAPI
 * Author:   zhu.juwang
 * Date:     2019/12/17 20:10
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@RequestMapping("/redis")
public interface IRedisAPI {
    /**
     * 简单redis key-value结构存储缓存
     * @param keyValueRedisEntity .key redis缓存key
     * @param keyValueRedisEntity .value redis缓存值
     * @return
     */
    @RequestMapping(value = "/set", method = RequestMethod.POST)
    @ApiOperation(value = " 简单redis key-value结构存储缓存", notes = " 简单redis key-value结构存储缓存",
            tags = {"Redis Simple Key-Value API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "写入成功", response = Boolean.class),
            @ApiResponse(code = 500, message = "内部错误")})
    boolean set(@RequestBody KeyValueRedisEntity keyValueRedisEntity);

    /**
     * 批量删除redis key值
     * @param keyList redis 缓存key集合
     * @return
     */
    @RequestMapping(value = "/batch/remove", method = RequestMethod.DELETE)
    @ApiOperation(value = "批量删除redis key值", notes = "批量删除redis key值",
            tags = {"Redis Simple Key-Value API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "删除成功", response = Boolean.class),
            @ApiResponse(code = 500, message = "内部错误")})
    boolean remove(@RequestBody List<String> keyList);

    /**
     * 按照匹配规则,删除匹配的redis key
     * @param keyPattern key 规则
     * @return
     */
    @RequestMapping(value = "/remove/pattern", method = RequestMethod.DELETE)
    @ApiOperation(value = "按照匹配规则,删除匹配的redis key", notes = "按照匹配规则,删除匹配的redis key",
            tags = {"Redis Simple Key-Value API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "删除成功", response = Boolean.class),
            @ApiResponse(code = 500, message = "内部错误")})
    boolean removePattern(@RequestParam("pattern") String keyPattern);

    /**
     * 删除redis key值
     * @param key redis 缓存key
     * @return
     */
    @RequestMapping(value = "/remove", method = RequestMethod.DELETE)
    @ApiOperation(value = "删除redis key值", notes = "删除redis key值",
            tags = {"Redis Simple Key-Value API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "删除成功", response = Boolean.class),
            @ApiResponse(code = 500, message = "内部错误")})
    boolean remove(@RequestParam("key") String key);

    /**
     * 判断redis是否存在指定的key
     * @param key redis 缓存key
     * @return
     */
    @RequestMapping(value = "/exists", method = RequestMethod.GET)
    @ApiOperation(value = "判断redis是否存在指定的key", notes = "判断redis是否存在指定的key",
            tags = {"Redis Simple Key-Value API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = Boolean.class),
            @ApiResponse(code = 500, message = "内部错误")})
    boolean exists(@RequestParam("key") String key);

    /**
     * 读取redis指定的key缓存信息
     * @param key redis 缓存key
     * @return
     */
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    @ApiOperation(value = "读取redis指定的key缓存信息", notes = "读取redis指定的key缓存信息",
            tags = {"Redis Simple Key-Value API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = Boolean.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Object get(@RequestParam("key") String key);

//    /**
//     * 增加hash结构的redis数据
//     * @param key redis key
//     * @param hashKey hash值key
//     * @param value 数据值
//     */
//    void hashSet(String key, Object hashKey, Object value);
//
//    /**
//     * 获取hash结构的redis数据
//     * @param key redis key
//     * @param hashKey hash值key
//     * @return
//     */
//    Object hashGet(String key, Object hashKey);
//
//    /**
//     * 增加列表结构的redis数据
//     * @param key redis key值
//     * @param value redis value
//     */
//    void listSet(String key, Object value);
//
//    /**
//     * 获取列表结构的redis数据
//     * @param key redis key值
//     * @param start 开始位置
//     * @param end 结束位置, -1时 为最后一个位置
//     * @return
//     */
//    List<Object> listGet(String key, long start, long end);
//
//
//    Set<Object> collectionGet(String key);
//
//    void arrayListSet(String key, Object value, double scoure);
//
//    Set<Object> arrayListGet(String key, double source, double source1);
}
