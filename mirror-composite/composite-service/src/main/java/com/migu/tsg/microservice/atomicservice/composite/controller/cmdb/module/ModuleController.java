package com.migu.tsg.microservice.atomicservice.composite.controller.cmdb.module;

import com.aspire.mirror.composite.service.cmdb.module.IModuleAPI;
import com.aspire.ums.cmdb.cmic.payload.ExcelData;
import com.aspire.ums.cmdb.code.payload.CmdbCode;
import com.aspire.ums.cmdb.code.payload.CmdbSimpleCode;
import com.aspire.ums.cmdb.code.payload.CmdbV3CodeCascade;
import com.aspire.ums.cmdb.dict.payload.ConfigDict;
import com.aspire.ums.cmdb.module.payload.FullModule;
import com.aspire.ums.cmdb.module.payload.Module;
import com.aspire.ums.cmdb.module.payload.ModuleTag;
import com.aspire.ums.cmdb.v3.code.payload.CmdbV3CodeBindSource;
import com.aspire.ums.cmdb.v3.code.payload.CmdbV3CodeValidate;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.code.CmdbCodeClient;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.instance.InstanceClient;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.module.ModuleClient;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.v3.config.ICmdbConfigClient;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.v3.module.ICmdbV3ModuleGroupClient;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.dict.CmdbDictClient;
import com.migu.tsg.microservice.atomicservice.composite.common.excel2pdf.POIModuleUtils;
import com.migu.tsg.microservice.atomicservice.composite.controller.authcontext.ResAction;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@Slf4j
public class ModuleController implements IModuleAPI {
	
	@Autowired
	private ModuleClient moduleClient;

	@Autowired
	private CmdbCodeClient codeClient;

	@Autowired
	private ICmdbConfigClient configClient;

    @Autowired
    private CmdbDictClient cmdbDictClient;

    @Autowired
    private InstanceClient instanceClient;

    @Autowired
    private POIModuleUtils poiModuleUtils;

    @Autowired
    private ICmdbV3ModuleGroupClient moduleGroupClient;

	/**
	 *
	 *Description:获取所有的模型信息
	 * @return
	 */
    @Override
    public List<Module> getModuleTree(@RequestParam(value = "catalogId", required = false) String catalogId,
									  @RequestParam(value = "moduleType", required = false) String moduleType){
		return moduleClient.getModuleTree(catalogId, moduleType);
	}

	@Override
	public List<Module> getTreeByCatalogIdOrModuleId(@RequestParam(value = "catalogId", required = false) String catalogId,
													 @RequestParam(value = "moduleId", required = false) String moduleId) {
		return moduleClient.getTreeByCatalogIdOrModuleId(catalogId, moduleId);
	}

	@Override
	public Module getModuleDetail(@RequestParam String moduleId) {
		return moduleClient.getModuleDetail(moduleId);
	}

	@Override
	public List<Module> getModuleSelective(@RequestBody Module module) {
		return moduleClient.getModuleSelective(module);
	}

	@Override
	public Map<String, Object> getModuleExist(@RequestParam("catalogId") String catalogId,
											  @RequestParam("moduleCode") String moduleCode) {
		return moduleClient.getModuleExist(catalogId, moduleCode);
	}

	/**
	 *
	 *Description:新增模型
	 * @return
	 */
    @Override
    public Map<String, Object> addModule(@RequestParam("topCatalogId") String topCatalogId,
										@RequestBody Module module){
		return moduleClient.addModule(topCatalogId, module);
	}

	/**
	 *
	 *Description:更新模型
	 * @return
	 */
	@Override
	public Map<String, Object> updateModule(@RequestParam("topCatalogId") String topCatalogId,@RequestBody Module module) {
		return moduleClient.updateModule(topCatalogId, module);
	}

	@Override
	public Map<String, Object> updateModuleSort(@RequestParam("sortType") String sortType,
												@RequestParam("moduleId") String moduleId) {
		return moduleClient.updateModuleSort(sortType, moduleId);
	}

	/**
	 *
	 *Description:添加模型字段
	 * @return
	 */
	@Override
	public Map<String, Object> addModuleCode(@RequestBody Module module) {
		return moduleClient.addModuleCode(module);
	}

	/**
	 *
	 *Description:删除模型
	 * @return
	 */
	@Override
	public Map<String, Object> deleteModule(@PathVariable("moduleId") String moduleId) {
		return moduleClient.deleteModule(moduleId);
	}

	/**
	 *
	 *Description:获取模型标签
	 * @return
	 */
	@Override
	public List<ModuleTag> getModuleTag(@PathVariable("moduleId") String moduleId) {
		return moduleClient.getModuleTag(moduleId);
	}

	/**
	 * 查询表数据
	 */
	@Override
	public List<Map<String, Object>> queryTableData(@RequestBody Map<String, Object> queryData) {
		return moduleClient.queryTableData(queryData);
	}

	/**
	 * 导出模型配置模板
	 */
	@Override
    public Map<String, String> downloadImportTemplate(@PathVariable("moduleId") String moduleId,
                                                      @RequestParam(value = "tableHeaderCode", required = false) String tableHeaderCode,
                                                      HttpServletResponse response) {
        Map<String, String> returnMap = new HashMap<>();
        try {
            if (StringUtils.isEmpty(moduleId)) {
                returnMap.put("flag", "false");
                returnMap.put("msg", "模型编号不能为空");
                return returnMap;
            }
            Module module = moduleClient.getModuleDetail(moduleId);
            if (module == null) {
                returnMap.put("flag", "false");
                returnMap.put("msg", "未找到模型配置ID[" + moduleId + "]");
                return returnMap;
            }
            String fileName = module.getName()+"导入模板.xlsx";
            response.setHeader("Content-Disposition", "attachment;filename=".concat(String.valueOf(URLEncoder.encode
                    (fileName, "UTF-8"))));
            response.setHeader("Connection", "close");
            response.setHeader("Content-Type", "application/vnd.ms-excel");

            // 循环生成Cell
            final List<String> headers = new LinkedList<>();
            final Map<String, List<Map<String, Object>>> comboDataMap = new LinkedHashMap<>();
            List<CmdbSimpleCode> headerList;
            if(StringUtils.isNotEmpty(tableHeaderCode)) {
                headerList = moduleGroupClient.getAllCodeByGroupCode(moduleId, tableHeaderCode);
            } else {
                headerList = instanceClient.getInstanceHeader(moduleId, null);
            }
            if (headerList != null && headerList.size() > 0) {
                List<String> filterCodeList = Arrays.asList("id", "module_id", "insert_person", "insert_time", "update_person", "update_time");
                headerList.stream().forEach((code) -> {
                    CmdbCode cmdbCode = codeClient.getCodeByCodeId(code.getCodeId());
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
                        // 设置sheet2数据源
                        Map<String, Object> queryParams = new HashMap<>();
                        queryParams.put("codeId", cmdbCode.getCodeId());
                        List<Map<String, Object>> sourceDataList = codeClient.getCodeDataSource(queryParams);
                        comboDataMap.put(realTitle, sourceDataList);
                    }
                });
            }
            XSSFWorkbook wb = new XSSFWorkbook();
            poiModuleUtils.generateExcel(wb, module.getName(), headers, null, comboDataMap);
            ServletOutputStream outputStream;
            try {
                outputStream = response.getOutputStream();
                response.setHeader("Content-Disposition","attachment;filename=11111.xlsx");
                response.setContentType("application/vnd.ms-excel;charset=UTF-8");
                //写出文件,关闭流
                wb.write(outputStream);
                wb.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            log.error("导出模板失败.", e);
            returnMap.put("flag", "false");
            returnMap.put("msg", "模型[" + moduleId + "]excel数据下载失败！");
        } finally {
            try {
                response.getOutputStream().flush();
                response.getOutputStream().close();
            } catch (Exception e2) {
            }
        }
        return returnMap;
    }

	@Override
	public FullModule getModuleByInstanceId(@RequestParam("instance_id") String instanceId) {
		return moduleClient.getModuleByInstanceId(instanceId);
	}

	@Override
	public List<Map<String, Object>> getModuleData(@RequestBody Map<String, Object> queryParams,
												   @RequestParam(value = "moduleId", required = false) String moduleId,
												   @RequestParam(value = "moduleType", required = false) String moduleType) {
		return moduleClient.getModuleData(queryParams, moduleId, moduleType);
	}

	@Override
	public Map<String, Object> getParentInfo(@RequestParam("module_id") String moduleId) {
		return moduleClient.getParentInfo(moduleId);
	}

	@Override
    @ResAction(resType = "cmdb", action = "view", loadResFilter = true)
	public List<Map<String, Object>> getRefModuleDict(@RequestParam("codeId") String codeId){
		return moduleClient.getRefModuleDict(codeId);
	}

    @Override
    public Map<String, Map<String, String>> getModuleColumns(@RequestParam("moduleId") String moduleId) {
        return moduleClient.getModuleColumns(moduleId);
    }

    /**
     * 根据设备类型获取模型信息
     *
     * @param deviceType 设备类型
     * @return
     */
    @Override
    public Map<String, Object> getModuleIdByDeviceType(@RequestParam(value = "device_type") String deviceType) {
        return moduleClient.getModuleIdByDeviceType(deviceType);
    }

    private void fillDropDownData(CmdbCode code, int colIndex, List<ExcelData> dataList, List<CmdbCode> codeList) {
        ExcelData excelData = new ExcelData();
        excelData.setFieldCode(code.getFiledCode());
        setTitle(code, excelData);
        excelData.setColIndex(colIndex);
        String controlCode = code.getControlType().getControlCode();
        CmdbV3CodeBindSource bindSource = code.getCodeBindSource();
        if ("listSel".equals(controlCode)) {
            if (bindSource != null) {
                if ("数据字典".equals(bindSource.getBindSourceType())) {
                    List<ConfigDict> dictList = cmdbDictClient.getDictsByType(bindSource.getDictSource(), null, null, null);
                    excelData.setFieldValueList(dictList.stream().map(e -> e.getValue()).collect(Collectors.toList()));
                } else if ("数据表".equals(bindSource.getBindSourceType())) {
                    Map<String, Object> params = Maps.newHashMap();
                    params.put("sql", code.getCodeBindSource().getTableSql());
                    List<Map<String, Object>> tableDataList = moduleClient.queryTableData(params);
                    excelData.setFieldType(controlCode);
                    excelData.setFieldValueList(tableDataList.stream().map(e -> e.get("value")).collect(Collectors.toList()));
                } else if ("引用模型".equals(bindSource.getBindSourceType())) {
                    Map<String, Object> params = JSONObject.fromObject(bindSource.getRefModuleQuery());
                    Map<String, Object> queryParams = new HashMap<>();
                    queryParams.put("query", params);
                    List<Map<String, Object>> refDataList = moduleClient.getRefModuleDict(code.getCodeId());
                    excelData.setFieldType(controlCode);
                    excelData.setFieldValueList(refDataList.stream().map(e -> e.get("value")).collect(Collectors.toList()));
                }
            }
            excelData.setFieldType(controlCode);
        } else if ("cascader".equals(controlCode)) {
            List<Map<String, Object>> tableDataList = Lists.newArrayList();
            if ("数据字典".equals(bindSource.getBindSourceType())) {
                List<ConfigDict> dictList = cmdbDictClient.getDictsByType(bindSource.getDictSource(), null, null, null);
                for (ConfigDict dict : dictList) {
                    Map<String, Object> map = Maps.newHashMap();
                    map.put("id", dict.getValue());
                    tableDataList.add(map);
                }
            } else if ("数据表".equals(bindSource.getBindSourceType())) {
                Map<String, Object> params = Maps.newHashMap();
                params.put("sql", code.getCodeBindSource().getTableSql());
                tableDataList = moduleClient.queryTableData(params);
            } else if ("引用模型".equals(bindSource.getBindSourceType())) {
                Map<String, Object> params = JSONObject.fromObject(bindSource.getRefModuleQuery());
                Map<String, Object> queryParams = new HashMap<>();
                queryParams.put("query", params);
                tableDataList = moduleClient.getRefModuleDict(code.getCodeId());
                excelData.setFieldType(controlCode);
                excelData.setFieldValueList(tableDataList.stream().map(e -> e.get("value")).collect(Collectors.toList()));
            }
            tableDataList.forEach(e -> {
                // TODO:级联下拉id
                    setCascade(e, code, codeList, excelData);
                });
            excelData.setFieldType(controlCode);
            if ("parent".equals(excelData.getLevelType())) {
                // 级联的Excel下拉框，数字开头/包含中文括号,生成excel名称管理器会报错，需替换成"-",导入的时候需替换为原始值.
                excelData.setFieldValueList(tableDataList
                        .stream()
                        .map(e -> com.migu.tsg.microservice.atomicservice.composite.common.StringUtils.replaceChineseCharacter(e
                                .get("value").toString())).collect(Collectors.toList()));
            } else {
                excelData
                        .setFieldValueList(tableDataList.stream().map(e -> e.get("value").toString()).collect(Collectors.toList()));
            }
        } else {
            excelData.setFieldType("text");
            excelData.setFieldValueList(Lists.newArrayList());
        }
        dataList.add(excelData);
    }

    private void setTitle(CmdbCode code, ExcelData excelData) {
        List<CmdbV3CodeValidate> validates = code.getValidates();
        boolean isRequire = false;
        if (validates.size() > 0) {
            for (CmdbV3CodeValidate validate : validates) {
                if (("非空").equals(validate.getValidType())) {
                    isRequire = true;
                    break;
                }
            }
        }
        String title = code.getFiledName() + (isRequire ? "[必填]" : "");
        excelData.setFieldName(title);
    }

    private void setCascade(Map<String, Object> value, CmdbCode code, List<CmdbCode> codeList, ExcelData parent) {
        List<CmdbV3CodeCascade> cascadeList = code.getCascadeList();
        cascadeList.forEach(e -> {
            boolean isShow = isShow(e, codeList);
            if (!isShow) {
                return;
            }
            Map<String, Object> params = Maps.newHashMap();
            String sql = e.getSqlString();
            // TODO: 级联的是id吗？？？
                sql = sql.replace("?", value.get("id").toString());
                params.put("sql", sql);
                String subCode = e.getSubFiledCode();
                ExcelData child = new ExcelData();
                child.setFieldCode(subCode);
                List<Map<String, Object>> subDataList = moduleClient.queryTableData(params);
                child.setFieldType(code.getControlType().getControlCode());
                child.setFieldValueList(subDataList.stream().map(m -> m.get("value")).collect(Collectors.toList()));
                parent.setLevelType("parent");
                child.setParentFieldValue(value.get("value").toString());
                child.setLevelType("child");
                child.setParent(parent);
                parent.getChildList().add(child);
            });
    }

    private boolean isShow(CmdbV3CodeCascade cascade, List<CmdbCode> codeList) {
        boolean isShow = false;
        for (CmdbCode code1 : codeList) {
            if (code1.getFiledCode().equals(cascade.getSubFiledCode())) {
                isShow = true;
                break;
            }
        }
        return isShow;
    }
}
