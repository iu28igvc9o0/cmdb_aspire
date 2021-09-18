package com.aspire.ums.cmdb.automate.service;

import com.aspire.ums.cmdb.automate.payload.easyops.AutomateHostDataDTO;

import java.util.List;
import java.util.Map;

/**
 * @author fanwenhui
 * @date 2020-08-24 9:37
 * @description 自动化模型业务逻辑接口
 */
public interface AutomateService {

    /**
     * 新增
     * @param request 主机配置详情
     */
    void instanceCreate(AutomateHostDataDTO request) throws Exception;

    /**
     * 通过存在kafka上的json进行异步新增
     * @param param 新增参数
     * @param synLogId kafka同步日志ID
     */
    void instanceCreateByJson(String param, String synLogId);

    /**
     * 通过存在kafka上的json进行异步修改
     * @param param 更新参数
     * @param synLogId kafka同步日志ID
     */
    void instanceUpdateByJson(String param, String synLogId);

    /**
     * 通过存在kafka上的json进行异步删除
     * @param param 删除参数
     * @param synLogId kafka同步日志ID
     */
    void instanceDeleteByJson(String param, String synLogId);

    /**
     * 通过主机IP查询对应的主机配置详情
     * @param ip 默认为管理IP
     */
    Map<String,Object> getAutomateHostDetail(String ip);

    /**
     * 获取主机配下tab页面的表单渲染字段
     */
    List<Map<String, Object>> getAutomateColumns();

    /**
     * 刷新主机配置信息字段到redis缓存
     */
    boolean syncModule4Redis();

    /**
     * 获取主机配置信息excel导出的字段
     */
    Map<String,List<String>> buildExportHeaderList();

    /**
     * 构建kafka同步日志参数
     * @param param 自动化入参
     * @param type 同步类型
     */
    Map<String,Object> buildSynParam(String param,String type);

    /**
     * 自动化配置文件同步，并入库CMDB
     */
    void synAutomateConfFile();

    /**
     * 查询主机IP对应的配置文件
     */
    List<Map<String,String>> findAutomateConfList(Map<String,Object> param);

}
