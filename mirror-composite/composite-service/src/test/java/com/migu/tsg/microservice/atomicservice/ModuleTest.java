package com.migu.tsg.microservice.atomicservice;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.migu.tsg.microservice.atomicservice.composite.vo.cmdb.ExcelDataVo;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.io.FileUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import com.aspire.ums.cmdb.code.payload.CmdbCode;
import com.aspire.ums.cmdb.code.payload.CmdbV3CodeCascade;
import com.aspire.ums.cmdb.dict.payload.ConfigDict;
import com.aspire.ums.cmdb.module.payload.Module;
import com.aspire.ums.cmdb.v3.code.payload.CmdbV3CodeBindSource;
import com.aspire.ums.cmdb.v3.code.payload.CmdbV3CodeValidate;
import com.aspire.ums.cmdb.v3.config.payload.CmdbConfig;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.code.CmdbCodeClient;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.module.ModuleClient;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.v3.config.ICmdbConfigClient;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.dict.CmdbDictClient;
import com.migu.tsg.microservice.atomicservice.composite.common.StringUtils;
import com.migu.tsg.microservice.atomicservice.composite.common.excel2pdf.POICascadeUtils2;

/**
 * xxxxxx
 *
 * @author jiangxuwen
 * @date 2020/7/7 17:36
 */
@Slf4j
public class ModuleTest extends ApplicationTest {

    @Autowired
    private ModuleClient moduleClient;

    @Autowired
    private CmdbCodeClient codeClient;

    @Autowired
    private ICmdbConfigClient configClient;

    @Autowired
    private CmdbDictClient cmdbDictClient;

    @Test
    public void testDownloadTemplate() throws Exception {
        String moduleId = "10691117a10e4588bdf36225c104c60b";
        Module module = moduleClient.getModuleDetail(moduleId);
        String fileName = module.getName() + "导入模板.xlsx";
        List<CmdbCode> codeList = codeClient.getCodeListByModuleId(moduleId);
        CmdbConfig cmdbConfig = configClient.getConfigByCode("module_must_include_properties");
        String mustProperty = "id,module_id,insert_person,insert_time,update_person,update_time";
        if (cmdbConfig != null && cmdbConfig.getConfigValue() != null) {
            mustProperty = cmdbConfig.getConfigValue();
        }
        List<String> unExportList = Arrays.asList(mustProperty.split(","));
        List<ExcelDataVo> excelDataList = Lists.newArrayList();
        int rowIndex = 1;
        if (codeList != null && codeList.size() > 0) {
            for (CmdbCode code : codeList) {
                if (code.getAddReadOnly() != null && code.getAddReadOnly() == 1) {
                    continue;
                }
                if (unExportList.contains(code.getFiledCode())) {
                    continue;
                }

                fillDropDownData(code, rowIndex, excelDataList, codeList);
                rowIndex++;
            }
        }
        POICascadeUtils2 poiCascade = new POICascadeUtils2();
        XSSFWorkbook wb = new XSSFWorkbook();
        poiCascade.generateExcel(wb, excelDataList, fileName);

        String filePath = this.getClass().getResource("/").getPath() + "template/cmdb/" + fileName;
        File parentFile = new File(filePath).getParentFile();
        log.info("parentFile:{}", parentFile.getPath());
        FileUtils.forceMkdir(parentFile);
        FileUtils.deleteQuietly(new File(filePath));
        FileOutputStream outputStream = new FileOutputStream(new File(filePath));
        wb.write(outputStream);
    }

    private void fillDropDownData(CmdbCode code, int colIndex, List<ExcelDataVo> dataList, List<CmdbCode> codeList) {
        ExcelDataVo excelData = new ExcelDataVo();
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
                }
            }
            excelData.setFieldType(controlCode);
        } else if ("cascader".equals(controlCode)) {
            Map<String, Object> params = Maps.newHashMap();
            params.put("sql", code.getCodeBindSource().getTableSql());
            List<Map<String, Object>> tableDataList = moduleClient.queryTableData(params);
            tableDataList.forEach(e -> {
                // TODO:级联下拉id
                    setCascade(e, code, codeList, excelData);
                });
            excelData.setFieldType(controlCode);
            if ("parent".equals(excelData.getLevelType())) {
                // 级联的Excel下拉框，数字开头/包含中文括号,生成excel名称管理器会报错，需替换成"-",导入的时候需替换为原始值.
                excelData.setFieldValueList(tableDataList.stream()
                        .map(e -> StringUtils.replaceChineseCharacter(e.get("value").toString())).collect(Collectors.toList()));
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

    private void setTitle(CmdbCode code, ExcelDataVo excelData) {
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

    private void setCascade(Map<String, Object> value, CmdbCode code, List<CmdbCode> codeList, ExcelDataVo parent) {
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
                ExcelDataVo child = new ExcelDataVo();
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
