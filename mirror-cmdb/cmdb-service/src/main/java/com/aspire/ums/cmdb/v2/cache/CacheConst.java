package com.aspire.ums.cmdb.v2.cache;

import com.aspire.ums.cmdb.code.payload.FullCode;
import com.aspire.ums.cmdb.collectApproval.payload.CmdbCollectApproval;
import com.aspire.ums.cmdb.collectUnknown.payload.CmdbCollectUnknown;
import com.aspire.ums.cmdb.instance.payload.FullInstance;
import com.aspire.ums.cmdb.instance.payload.ImportProcess;
import com.aspire.ums.cmdb.module.payload.FullModule;

import java.util.*;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: CacheConst
 * Author:   zhu.juwang
 * Date:     2019/5/20 21:59
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
public class CacheConst {

    /**
     * 缓存所有的模型信息
     */
    public static Map<String, FullModule> CACHE_MODULE_MAP = new HashMap<>();

    /**
     * 缓存所有的码表信息
     */
    public static List<FullCode> CACHE_CODE_LIST = new LinkedList<>();

    /**
     * 缓存实例列表
     */
    public static Map<String, FullInstance> CACHE_INSTANCE_MAP = new LinkedHashMap<>();

//    /**
//     * 缓存导入实例的文件列表
//     */
//    public static volatile Map<String, Map<String, Object>> CACHE_IMPORT_LIST = new HashMap<>();

//    /**
//     * 缓存导入实例文件的进度
//     */
//    public static Map<String, ImportProcess> CACHE_IMPORT_DETAIL = new HashMap<>();

//    /**
//     * 缓存导入实例文件的进度
//     */
//    public static Map<String, List<Map<String, Object>>> CACHE_IMPORT_ERROR_LIST = new HashMap<>();


//    /**
//     * 缓存审核列表
//     */
//    public static volatile Map<String, Map<String, Object>> CACHE_BATCH_UPDATE_LIST = new HashMap<>();
//
//    public static volatile Map<String, List<CmdbCollectApproval>> CACHE_APPROVAL_LIST = new HashMap<>();
//
//    /**
//     * 缓存未知设备列表
//     */
//    public static volatile Map<String, List<CmdbCollectUnknown>> CACHE_COLLECT_UNKNOWN_LIST = new HashMap<>();
//
//    /**
//     * 缓存审核的进度
//     */
//    public static Map<String, ImportProcess> CACHE_APPROVAL_DETAIL = new HashMap<>();
//
//    /**
//     * 缓存审核错误
//     */
//    public static Map<String, List<Map<String, Object>>> CACHE_APPROVAL_ERROR_LIST = new HashMap<>();
}
