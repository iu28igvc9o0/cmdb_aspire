package com.migu.tsg.microservice.atomicservice.composite.controller.cmdb.process;

import com.aspire.mirror.composite.service.inspection.payload.ConfigDict;
import com.aspire.ums.cmdb.module.payload.Module;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.ConfigDictClient;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.module.ModuleClient;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;

import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: InstanceProcessThread
 * Author:   zhu.juwang
 * Date:     2019/8/9 10:55
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Data
@Slf4j
public class InstanceProcessThread extends ProcessThread {

    private ModuleClient moduleClient;
    private ConfigDictClient configDictClient;
    private Module module;

    @Override
    protected void setDataMap(Map<String, String> keyValueMap, String key, String value) {
        keyValueMap.put(key, value);
    }

    protected void setSpecialData(Map<String, Object> importData) {
        importData.put("moduleId", processParams.getModuleId());
        importData.put("moduleType", processParams.getModuleType());
    }

    protected String getHeaderCellText(Cell titleCell) {
        String cellText = titleCell.getStringCellValue().trim();
        if (cellText.contains("[必填]")) {
            cellText = cellText.replace("[必填]", "").trim();
        }
        if (cellText.contains("(必填)") || cellText.contains("（必填）")) {
            cellText = cellText.replace("(必填)", "").replace("（必填）","").trim();
        }
        if (cellText.equals("失败原因")) {
            cellText = null;
        }
        return cellText;
    }

    @Override
    protected void init() {
    }

    protected void valid() {
        if (processParams.getModuleId() == null) {
            String colName;
            String moduleType = processParams.getModuleType();
            if ("host".equals(moduleType)) {
                colName = "default_ci_module_id";
            } else if ("business".equalsIgnoreCase(moduleType)) {
                colName = "default_business_module_id";
            } else if ("dict".equalsIgnoreCase(moduleType)) {
                colName = "default_dict_module_id";
            } else {
                throw new RuntimeException("Not support import module by type -> " + moduleType);
            }
            List<ConfigDict> dicts = configDictClient.getDictsByType(colName, null, null, null);
            if (dicts.size() != 1) {
                throw new RuntimeException("Can't find default module_id config -> " + colName);
            }
        }
    }
}
