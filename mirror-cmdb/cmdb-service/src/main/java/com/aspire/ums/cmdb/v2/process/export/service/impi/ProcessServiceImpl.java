package com.aspire.ums.cmdb.v2.process.export.service.impi;

import com.aspire.ums.cmdb.code.payload.CmdbCode;
import com.aspire.ums.cmdb.code.payload.CmdbSimpleCode;
import com.aspire.ums.cmdb.common.Constants;
import com.aspire.ums.cmdb.config.RequestAuthContext;
import com.aspire.ums.cmdb.deviceStatistic.service.DeviceStatisticService;
import com.aspire.ums.cmdb.deviceStatistic.util.POIModuleUtils;
import com.aspire.ums.cmdb.ftp.service.FtpService;
import com.aspire.ums.cmdb.instance.payload.ImportProcess;
import com.aspire.ums.cmdb.networkCard.service.ICmdbInstanceNetworkCardService;
import com.aspire.ums.cmdb.util.UUIDUtil;
import com.aspire.ums.cmdb.v2.code.service.ICmdbCodeService;
import com.aspire.ums.cmdb.v2.instance.service.ICmdbInstanceService;
import com.aspire.ums.cmdb.v2.module.service.ModuleService;
import com.aspire.ums.cmdb.v2.process.export.service.IProcessService;
import com.aspire.ums.cmdb.v2.process.export.thread.ExportProcessThread;
import com.aspire.ums.cmdb.v3.code.payload.CmdbV3CodeValidate;
import com.aspire.ums.cmdb.v3.redis.service.IRedisService;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.*;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: ProcessServiceImpl
 * Author:   zhu.juwang
 * Date:     2019/9/6 16:07
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Service
@Slf4j
public class ProcessServiceImpl implements IProcessService {

    @Autowired
    private DeviceStatisticService deviceStatisticService;
    @Autowired
    private ICmdbInstanceNetworkCardService cmdbInstanceNetworkCardService;
    @Autowired
    private ICmdbInstanceService instanceService;
    @Autowired
    private IRedisService redisService;
    @Autowired
    private ModuleService moduleService;
    @Autowired
    private ICmdbCodeService cmdbCodeService;
    @Autowired
    private POIModuleUtils poiModuleUtils;
    @Autowired
    private FtpService ftpService;

    @Override
    public List<Map<String, Object>> exportReport(String exportType, Map<String, Object> exportParams) {
        switch (exportType.toLowerCase(Locale.ENGLISH)) {
            case "selectidctypestatistic":
                return deviceStatisticService.selectIdcTypeStatistic();
            case "selectprojectstatistic":
                Assert.notNull(exportParams.get("idcType"), "Must contains params -> idcType, and value can't be NULL.");
                return deviceStatisticService.selectProjectStatistic(exportParams.get("idcType").toString());
            case "selectpodstatistic":
                Assert.notNull(exportParams.get("idcType"), "Must contains params -> idcType, and value can't be NULL.");
                Assert.notNull(exportParams.get("projectName"), "Must contains params -> projectName, and value can't be NULL.");
                return deviceStatisticService.selectPodStatistic(exportParams.get("idcType").toString(),
                        exportParams.get("projectName").toString());
            case "networkcard":
                Assert.notNull(exportParams.get("instanceId"), "Must contains params -> instanceId, and value can't be NULL.");
                return cmdbInstanceNetworkCardService.exportList(exportParams.get("instanceId").toString());
            default:
                return null;
        }
    }

    /**
     * 下载资源列表
     *
     * @param params
     * @return
     */
    @Override
    public Map<String, String> exportInstance(Map<String, Object> params, String moduleId) {
        Map<String, String> returnMap = Maps.newHashMap();
        final String processId = UUIDUtil.getUUID();
        try {
            List<CmdbSimpleCode> headerList = instanceService.getInstanceHeader(moduleId, null);
            Map<String, Map<String, String>> columnMap = moduleService.getModuleColumns(moduleId);

            final List<String> headers = new LinkedList<>();
            final List<String> keys = new LinkedList<>();
            final Map<String, List<Map<String, Object>>> comboDataMap = new LinkedHashMap<>();

            List<String> filterCodeList = Arrays.asList("id", "module_id", "insert_person", "insert_time", "update_person", "update_time");
            headerList.stream().forEach((code) -> {
                CmdbCode cmdbCode = cmdbCodeService.get(new CmdbCode(code.getCodeId()));
                if (cmdbCode != null && !filterCodeList.contains(code.getFiledCode())) {
                    List<CmdbV3CodeValidate> validates = cmdbCode.getValidates();
                    boolean isRequire = false;
                    if (validates != null && validates.size() > 0) {
                        for (CmdbV3CodeValidate validate : validates) {
                            if (("非空").equals(validate.getValidType())) {
                                isRequire = true;
                                break;
                            }
                        }
                    }
                    String realTitle = cmdbCode.getFiledName();
                    if (isRequire) {
                        realTitle = cmdbCode.getFiledName() + "[必填]";
                    }
                    headers.add(realTitle);
                    Map<String, String> codeInfo = columnMap.get(code.getFiledCode());
                    String headerKey = code.getFiledCode();
                    if (("ref").equals(codeInfo.get("type"))) {
                        headerKey = codeInfo.get("ref_name");
                    }
                    keys.add(headerKey);
                    // 设置sheet2数据源
                    List<Map<String, Object>> sourceDataList = cmdbCodeService.getRefCodeData(cmdbCode.getCodeId());
                    comboDataMap.put(realTitle, sourceDataList);
                }
            });
            // 获取总记录数
            Integer totalCount = instanceService.listV3Count(params, null);
            ImportProcess process = new ImportProcess();
            process.setStartTime(new Date());
            process.setProcessId(processId);
            process.setTotalRecord(totalCount);
            process.setSuccessCount(0);
            redisService.set(String.format(Constants.REDIS_PROCESS_DETAIL, processId), process);
            ExportProcessThread processThread = new ExportProcessThread();
            processThread.setRequestAuthContext(RequestAuthContext.currentRequestAuthContext());
            processThread.setTotalCount(totalCount);
            processThread.setComboDataMap(comboDataMap);
            processThread.setParams(params);
            processThread.setHeaders(headers);
            processThread.setKeys(keys);
            processThread.setProcessId(processId);
            processThread.setFtpService(ftpService);
            processThread.setInstanceService(instanceService);
            processThread.setPoiModuleUtils(poiModuleUtils);
            processThread.setRedisService(redisService);
            processThread.run();
            returnMap.put("processId", processId);
            returnMap.put("code", "success");
        } catch (Exception e) {
            returnMap.put("code", "error");
            returnMap.put("message", e.getMessage());
            return returnMap;
        }
        return returnMap;
    }


}
