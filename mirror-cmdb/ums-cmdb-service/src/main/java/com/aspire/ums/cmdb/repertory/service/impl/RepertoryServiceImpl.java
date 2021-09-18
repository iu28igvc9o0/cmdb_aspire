package com.aspire.ums.cmdb.repertory.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.aspire.ums.cmdb.module.service.ModuleService;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import com.alibaba.fastjson.JSON;
import com.aspire.ums.cmdb.common.Constants;
import com.aspire.ums.cmdb.common.PoiTitle;
import com.aspire.ums.cmdb.maintain.entity.CasoptionsBean;
import com.aspire.ums.cmdb.maintain.entity.Instance;
import com.aspire.ums.cmdb.maintain.entity.FormValue;
import com.aspire.ums.cmdb.maintain.entity.InstanceCircle;
import com.aspire.ums.cmdb.maintain.entity.InstanceModel;
import com.aspire.ums.cmdb.maintain.entity.InstanceRelation;
import com.aspire.ums.cmdb.maintain.entity.ModuleRelation;
import com.aspire.ums.cmdb.maintain.entity.RelationLog;
import com.aspire.ums.cmdb.maintain.mapper.CasoptionsMapper;
import com.aspire.ums.cmdb.maintain.mapper.FormValueMapper;
import com.aspire.ums.cmdb.maintain.mapper.InstanceCircleMapper;
import com.aspire.ums.cmdb.maintain.mapper.InstanceMapper;
import com.aspire.ums.cmdb.maintain.mapper.InstanceRelationMapper;
import com.aspire.ums.cmdb.maintain.mapper.MaintainViewMapper;
import com.aspire.ums.cmdb.maintain.mapper.ModuleRelationMapper;
import com.aspire.ums.cmdb.maintain.mapper.RelationLogMapper;
import com.aspire.ums.cmdb.maintain.service.ConfigLogService;
import com.aspire.ums.cmdb.module.entity.Form;
import com.aspire.ums.cmdb.module.entity.FormOptions;
import com.aspire.ums.cmdb.module.entity.FormRule;
import com.aspire.ums.cmdb.module.entity.Module;
import com.aspire.ums.cmdb.module.mapper.FormMapper;
import com.aspire.ums.cmdb.module.mapper.FormOptionsMapper;
import com.aspire.ums.cmdb.module.mapper.FormParamMapper;
import com.aspire.ums.cmdb.module.mapper.FormRuleMapper;
import com.aspire.ums.cmdb.module.mapper.ModuleMapper;
import com.aspire.ums.cmdb.repertory.service.RepertoryService;
import com.aspire.ums.cmdb.util.DateUtils;
import com.aspire.ums.cmdb.util.ExcelImportUtils;
import com.aspire.ums.cmdb.util.StringUtils;
import com.aspire.ums.cmdb.util.UUIDUtil;

@Service
@Transactional
public class RepertoryServiceImpl implements RepertoryService {

	private final Logger logger = Logger.getLogger(getClass());

	@Autowired
	private ModuleMapper moduleMapper;

	@Autowired
	private FormMapper formMapper;

	@Autowired
	private FormParamMapper formParamMapper;

	@Autowired
	private FormOptionsMapper formOptionsMapper;

	@Autowired
	private FormRuleMapper formRuleMapper;

	@Autowired
	private ModuleRelationMapper moduleRelationMapper;

	@Autowired
	private InstanceMapper instanceMapper;

	@Autowired
	private FormValueMapper formValueMapper;

	@Autowired
	private InstanceRelationMapper instanceRelationMapper;
	
	@Autowired
	private InstanceCircleMapper instanceCircleMapper;
	
    @Autowired
    private MaintainViewMapper maintainViewMapper;
    
    @Autowired
    private CasoptionsMapper casoptionsMapper;
    
    @Autowired
    private ConfigLogService configLogService;
    
    @Autowired
    private RelationLogMapper relationLogMapper;

	@Autowired
	ModuleService moduleService;
	/**
	 * 根据模型编号获取excel表头部分
	 * 
	 * @param moduleId
	 * @return
	 */
	@SuppressWarnings("rawtypes")
    public List<PoiTitle> getTitle(String moduleId) {
		List<PoiTitle> list = new ArrayList<PoiTitle>();
		// 固定列
		PoiTitle column_1 = new PoiTitle();
		column_1.setField(null);
		column_1.setComment("字段");
		column_1.setFormat("数据格式");
		column_1.setDef("默认值");
		list.add(column_1);

		PoiTitle column_2 = new PoiTitle();
		column_2.setField("2_ID");
		column_2.setComment("ID");
		column_2.setFieldType("ID");
		column_2.setFormat("请勿编辑此列");
		column_2.setDef("");
		list.add(column_2);

		// 可变列-模型表单列
		List<Form> formList = formMapper.getFormsByModuleId(moduleId);
		for (Form form : formList) {
			PoiTitle column_form = new PoiTitle();
			column_form.setField(form.getCode());
			column_form.setFormId(form.getId());
			column_form.setComment(form.getName() + (StringUtils.isNotEmpty(form.getUnit()) ? ("(" + form.getUnit().toUpperCase() + ")") : "")
					+ (form.getRequired().equals("true") ? "[必填]" : ""));
			column_form.setDef(form.getDefaultvalue());
			column_form.setFormat("");
			column_form.setFieldType(form.getType());
			column_form.setFormCode(form.getCode());
			column_form.setFormName(form.getName());

			Map<String, String> paramMap = new HashMap<String, String>();
			List<Map<String, String>> params = formParamMapper.getParamsMapByFormId(form.getId());
			for (Map<String, String> param : params) {
				String key = param.get("key");
				String value = param.get("value");
				paramMap.put(key, value);
			}
			List<String> optionList = formOptionsMapper.getOptionsMapByFormId(form.getId());
	        column_form.setFormOptions(formOptionsMapper.getByFormId(form.getId()));
	        column_form.setCasoptionsBeans(casoptionsMapper.getOptionBeanByFormId(form.getId()));

			switch (form.getType()) {
			case Constants.MODULE_FORM_TYPE_SINGLEROWTEXT: // 单行文本
				if (null != paramMap && StringUtils.isNotEmpty(paramMap.get(Constants.MODULE_FORM_PARAM_MINLENGTH))
						&& StringUtils.isNotEmpty(paramMap.get(Constants.MODULE_FORM_PARAM_MAXLENGTH))) {
					column_form.setMinLength(Integer.valueOf(paramMap.get(Constants.MODULE_FORM_PARAM_MINLENGTH)));
					column_form.setMaxLength(Integer.valueOf(paramMap.get(Constants.MODULE_FORM_PARAM_MAXLENGTH)));
					column_form.setFormat("长度范围(" + paramMap.get(Constants.MODULE_FORM_PARAM_MINLENGTH) + "-"
							+ paramMap.get(Constants.MODULE_FORM_PARAM_MAXLENGTH) + ")字符");
				}
				list.add(column_form);
				break;
			case Constants.MODULE_FORM_TYPE_MULTIROWTEXT: // 多行文本
				if (null != paramMap && StringUtils.isNotEmpty(paramMap.get(Constants.MODULE_FORM_PARAM_MINLENGTH))
						&& StringUtils.isNotEmpty(paramMap.get(Constants.MODULE_FORM_PARAM_MAXLENGTH))) {
					column_form.setMinLength(Integer.valueOf(paramMap.get(Constants.MODULE_FORM_PARAM_MINLENGTH)));
					column_form.setMaxLength(Integer.valueOf(paramMap.get(Constants.MODULE_FORM_PARAM_MAXLENGTH)));
					column_form.setFormat("长度范围(" + paramMap.get(Constants.MODULE_FORM_PARAM_MINLENGTH) + "-"
							+ paramMap.get(Constants.MODULE_FORM_PARAM_MAXLENGTH) + ")字符");
				}
				list.add(column_form);
				break;
			case Constants.MODULE_FORM_TYPE_RICHTEXT: // 富本文
				// 导入导出不处理
				break;
			case Constants.MODULE_FORM_TYPE_LISTSEL: // 下拉菜单
				if (null != optionList && !optionList.isEmpty()) {
					column_form.setOptions(optionList);
					column_form.setFormat("取值范围(单选):" + optionList + " 格式:[填入一个值即可]");
				}
				list.add(column_form);
				break;
			case Constants.MODULE_FORM_TYPE_SINGLESEL: // 单选
				if (null != optionList && !optionList.isEmpty()) {
					column_form.setOptions(optionList);
					column_form.setFormat("取值范围(单选):" + optionList + " 格式:[填入一个值即可]");
				}
				list.add(column_form);
				break;
			case Constants.MODULE_FORM_TYPE_MULTISEL: // 多选
				if (null != optionList && !optionList.isEmpty()) {
					column_form.setOptions(optionList);
					column_form.setFormat("取值范围(多选):" + optionList + " 格式:[填入多个值并以\",\"隔开]");
				}
				list.add(column_form);
				break;
			case Constants.MODULE_FORM_TYPE_DOUBLE: // 小数
				if (null != paramMap && StringUtils.isNotEmpty(paramMap.get(Constants.MODULE_FORM_PARAM_MIN))
						&& StringUtils.isNotEmpty(paramMap.get(Constants.MODULE_FORM_PARAM_MAX))
						&& StringUtils.isNotEmpty(paramMap.get(Constants.MODULE_FORM_PARAM_PRECISION))) {
					column_form.setMin(Integer.valueOf(paramMap.get(Constants.MODULE_FORM_PARAM_MIN)));
					column_form.setMax(Integer.valueOf(paramMap.get(Constants.MODULE_FORM_PARAM_MAX)));
					column_form.setPrecision(Integer.valueOf(paramMap.get(Constants.MODULE_FORM_PARAM_PRECISION)));
					column_form.setFormat("取值范围(" + paramMap.get(Constants.MODULE_FORM_PARAM_MIN) + "-"
							+ paramMap.get(Constants.MODULE_FORM_PARAM_MAX) + ") " + paramMap.get(Constants.MODULE_FORM_PARAM_PRECISION) + "位小数");
				}
				list.add(column_form);
				break;
			case Constants.MODULE_FORM_TYPE_INT:// 整数
				if (null != paramMap && StringUtils.isNotEmpty(paramMap.get(Constants.MODULE_FORM_PARAM_MIN))
						&& StringUtils.isNotEmpty(paramMap.get(Constants.MODULE_FORM_PARAM_MAX))) {
					column_form.setMin(Integer.valueOf(paramMap.get(Constants.MODULE_FORM_PARAM_MIN)));
					column_form.setMax(Integer.valueOf(paramMap.get(Constants.MODULE_FORM_PARAM_MAX)));
					column_form.setFormat("取值范围(" + paramMap.get(Constants.MODULE_FORM_PARAM_MIN) + "-"
							+ paramMap.get(Constants.MODULE_FORM_PARAM_MAX) + ")");
				}
				list.add(column_form);
				break;
			case Constants.MODULE_FORM_TYPE_IMAGE: // 图片
				// 导入导出不处理
				break;
			case Constants.MODULE_FORM_TYPE_FILE: // 附件
				// 导入导出不处理
				break;
			case Constants.MODULE_FORM_TYPE_DATETIME:// 时间
                column_form.setFormat("时间格式：yyyy-MM-dd HH:mm:ss");
                column_form.setFormatDate(false);
				if (null != paramMap && StringUtils.isNotEmpty(paramMap.get(Constants.MODULE_FORM_PARAM_FORMATDATE))) {
					column_form.setFormatDate(Boolean.valueOf(paramMap.get(Constants.MODULE_FORM_PARAM_FORMATDATE)));
					if (paramMap.get(Constants.MODULE_FORM_PARAM_FORMATDATE).equals("true")) {
						column_form.setFormat("时间格式：yyyy-mm-dd");
					} else {
					}
				}
				list.add(column_form);
				break;
			case Constants.MODULE_FORM_TYPE_TABLE: // 表格
				// 导入导出不处理
				break;
			case Constants.MODULE_FORM_TYPE_CASCADER: // 级联
	             if (null != optionList && !optionList.isEmpty()) {
	                    column_form.setOptions(optionList);
	                }
                column_form.setFormat(" 格式:[级别以\",\"隔开填入]");
				list.add(column_form);
				break;
			case Constants.MODULE_FORM_TYPE_GROUPLINE: // 属性分组
				// 导入导出不处理
				break;
			default:
				break;
			}
		}

		// 可变列-模型关系列
		ModuleRelation moduleRelation = new ModuleRelation();
		moduleRelation.setSourceModuleId(moduleId);
		List<Map> relations = moduleRelationMapper.getRetionByCondition(moduleRelation);
		for (Map map : relations) {
			PoiTitle column_form = new PoiTitle();
			column_form.setField(map.get("id").toString());
			column_form.setDef("");
			column_form.setRestriction(String.valueOf(map.get("restriction")));
			column_form.setFormat("名称[存在多个关系时，使用换行分隔][" + map.get("restrictionDec") + "]");

			if (StringUtils.isNotEmpty(map.get("sourceModuleId")) && map.get("sourceModuleId").equals(moduleId)) {
				column_form.setFieldType(Constants.MODULE_RELATION_SOURCE);
				column_form.setComment(map.get("relationName").toString() + "↗(" + map.get("targetModuleName").toString() + ")");
	            column_form.setModuleId((String) map.get("targetModuleId"));
	            column_form.setFormName( map.get("targetModuleName").toString());
			} else {
				column_form.setFieldType(Constants.MODULE_RELATION_TARGET);
				column_form.setComment(map.get("relationName").toString() + "↙(" + map.get("sourceModuleName").toString() + ")");
	            column_form.setModuleId((String)map.get("sourceModuleId"));
	            column_form.setFormName( map.get("sourceModuleName").toString());
			}
			list.add(column_form);
		}

		return list;
	}

	/**
	 * 根据模型编号获取excel数据部分
	 * 
	 * @param moduleId
	 * @return
	 */
	public List<Map<String, String>> getContent(String moduleId) {
		List<Map<String, String>> content = new ArrayList<Map<String, String>>();

		List<Instance> instances = instanceMapper.getInstanceByModuleId(moduleId);
		for (Instance instance : instances) {
			Map<String, String> values = new HashMap<String, String>();
			// 实例表单数据
			List<Map<String, String>> forms = formValueMapper.getFormValueMapByInstanceId(instance.getId());
			if (null == forms || forms.isEmpty()) {
				continue;
			}
			for (Map<String, String> formMap : forms) {
				String formCode = formMap.get("formCode");
				String formValue = formMap.get("formValue");
				values.put(formCode, formValue);
			}
			values.put("2_ID", instance.getId());

			// 实例关系数据
			List<Map<String, String>> relations = instanceRelationMapper.getMuduleRelationIdAndInstanceName(instance.getId());
			for (Map<String, String> map : relations) {
				String moduleRelationId = map.get("moduleRelationId");
				String instanceName = map.get("instanceName");
				if (values.containsKey(moduleRelationId)) {
					values.put(moduleRelationId, values.get(moduleRelationId) + "\r\n" + instanceName);
				} else {
					values.put(moduleRelationId, instanceName);
				}
			}
			content.add(values);
		}
		return content;
	}

	/**
	 * 根据表头，表数据生成excel
	 * 
	 * @param titles
	 * @param
	 * @param workbook
	 */
	public void generateExcelForAs(List<PoiTitle> titles, List<Map<String, String>> contents, HSSFWorkbook workbook, String moduleId) {
		// 样式
		HSSFCellStyle commonCellStyle = workbook.createCellStyle();
		commonCellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 水平居中
		commonCellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直居中
		commonCellStyle.setWrapText(true);// 设置自动换行

		// 数据
		HSSFSheet sheet = workbook.createSheet();
		workbook.setSheetName(0, getModuleName(moduleId));
		sheet.setColumnWidth(0, 10 * 256); // 第一列宽度
		sheet.setDefaultColumnWidth(22); // 默认宽度
		// sheet.setDefaultRowHeight((short)(13*20));

		int excelRow = 0;
		int excelColomn = 0;
		HSSFRow row = null;

		// 第一行 字段行
		row = sheet.createRow(excelRow);
		for (PoiTitle pt : titles) {
			HSSFCellStyle firstColumnStyle = workbook.createCellStyle();
			firstColumnStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 水平居中
			firstColumnStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直居中
			firstColumnStyle.setWrapText(true);// 设置自动换行
			firstColumnStyle.setFillForegroundColor(IndexedColors.SKY_BLUE.getIndex());
			firstColumnStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);

			HSSFCell cell = row.createCell(excelColomn);
			cell.setCellStyle(firstColumnStyle);
			cell.setCellValue(pt.getComment());
			excelColomn++;
		}
		excelRow++;

		// 第二行 格式格式行
		excelColomn = 0;
		row = sheet.createRow(excelRow);
		for (PoiTitle pt : titles) {
			HSSFCell cell = row.createCell(excelColomn);
			cell.setCellStyle(commonCellStyle);
			cell.setCellValue(pt.getFormat());
			excelColomn++;
		}
		excelRow++;

		// 第三行 默认值行
		excelColomn = 0;
		row = sheet.createRow(excelRow);
		for (PoiTitle pt : titles) {
			HSSFCell cell = row.createCell(excelColomn);
			cell.setCellStyle(commonCellStyle);
			cell.setCellValue(pt.getDef());
			excelColomn++;
		}
		excelRow++;

		if (null == contents) {
			return;
		}

		// 第4-N行 数据行
		for (int i = 0; i < contents.size(); i++) { // 行控制
			Map<String, String> map = contents.get(i);
			row = sheet.createRow(excelRow);
			HSSFCell firstCell = row.createCell(0);
			firstCell.setCellStyle(commonCellStyle);
			firstCell.setCellValue(i == 0 ? "数据" : "");
			excelColomn = 1;
			for (int t = 1; t < titles.size(); t++) { // 列控制
				HSSFCell cell = row.createCell(excelColomn);
				cell.setCellStyle(commonCellStyle);
				if(Constants.MODULE_FORM_TYPE_LISTSEL.equals(titles.get(t).getFieldType())  || Constants.MODULE_FORM_TYPE_SINGLESEL.equals(titles.get(t).getFieldType())    
				        ){
				    if(titles.get(t).getFormOptions() != null && titles.get(t).getFormOptions().size() > 0){
                        for(FormOptions nf : titles.get(t).getFormOptions()){
                                if(nf.getValue().equals(map.get(titles.get(t).getField()))){
                                    cell.setCellValue(nf.getName()); 
                                }
                        }
                    }
				}else if( Constants.MODULE_FORM_TYPE_MULTISEL.equals(titles.get(t).getFieldType()) 
                        ){
                    List<String> newOps = new ArrayList<String>();
                    newOps = JSON.parseArray(map.get(titles.get(t).getField()),String.class);
                    String newStr = new String("");
                    if(titles.get(t).getFormOptions() != null && titles.get(t).getFormOptions().size() > 0){
                        for(FormOptions nf : titles.get(t).getFormOptions()){
                            for(String n:newOps){
                                if(nf.getValue().equals(n)){
                                    newStr+=nf.getName();
                                    newStr+=",";
                                }
                            }
                        }

                    }
                    if(newStr.contains(",")){newStr = newStr.substring(0, newStr.length() -1);}
                    cell.setCellValue(newStr); 
                }else if( Constants.MODULE_FORM_TYPE_CASCADER.equals(titles.get(t).getFieldType()) 
                        ){
                    List<String> newOps = new ArrayList<String>();
                    newOps = JSON.parseArray(map.get(titles.get(t).getField()),String.class);
                    String newStr = new String("");
                    if(titles.get(t).getCasoptionsBeans() != null && titles.get(t).getCasoptionsBeans().size() > 0){
                        for(CasoptionsBean nf1 : titles.get(t).getCasoptionsBeans()){
                            for(String n:newOps){
                                //一层
                                if(nf1.getValue().equals(n)){
                                    newStr+=nf1.getLabel();
                                    newStr+=",";
                                }
                                //二层
                                if(nf1.getChildren()!= null && nf1.getChildren().size()>0){
                                    for(CasoptionsBean nf2: nf1.getChildren()){
                                        if(nf2.getValue().equals(n)){
                                            newStr+=nf2.getLabel();
                                            newStr+=",";
                                        }
                                            if(nf2.getChildren()!= null && nf2.getChildren().size()>0){
                                                for(CasoptionsBean nf3: nf2.getChildren()){
                                                    if(nf3.getValue().equals(n)){
                                                        newStr+=nf3.getLabel();
                                                        newStr+=",";
                                                    }
                                                }
                                            }
                                    }
                                }
                            }
                        }
                        
                    }
                    if(newStr.contains(",")){newStr = newStr.substring(0, newStr.length() -1);}
                    cell.setCellValue(newStr); 
                }else{
		             cell.setCellValue(map.get(titles.get(t).getField())); 
				}
				excelColomn++;
			}
			excelRow++;
		}

	}

	@Override
	public byte[] getExcelData(String moduleId) {
		ByteArrayOutputStream out = null;
		try {
			HSSFWorkbook workbook = new HSSFWorkbook();
			generateExcelForAs(getTitle(moduleId), getContent(moduleId), workbook, moduleId);
			out = new ByteArrayOutputStream();
			HSSFWorkbook hssWb = (HSSFWorkbook) workbook;
			hssWb.write(out);
		} catch (IOException e) {
			logger.error("模型[" + moduleId + "]excel数据下载失败！", e);
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return out.toByteArray();
	}

	@Override
	public byte[] getExcel(String moduleId) {
		ByteArrayOutputStream out = null;
		try {
			HSSFWorkbook workbook = new HSSFWorkbook();
			generateExcelForAs(getTitle(moduleId), null, workbook, moduleId);
			out = new ByteArrayOutputStream();
			HSSFWorkbook hssWb = (HSSFWorkbook) workbook;
			hssWb.write(out);
		} catch (IOException e) {
			logger.error("模型[" + moduleId + "]excel模板下载失败！", e);
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return out.toByteArray();
	}

	@Override
	public String getModuleName(String moduleId) {
		Module module = moduleMapper.selectByPrimaryKey(moduleId);
		if (null == module) {
			return null;
		}
		return module.getName();
	}

	@Override
	public Map<String, Object> uploadData(MultipartFile file, String circleId) {
		Map<String, Object> map = new HashMap<String, Object>();
		InputStream is = null;
		Workbook wb = null;
		try {
			is = file.getInputStream();
			// 根据文件名判断文件是2003版本还是2007版本
			if (ExcelImportUtils.isExcel2007(file.getOriginalFilename())) {
				wb = new XSSFWorkbook(is);
			} else {
				wb = new HSSFWorkbook(is);
			}
			map = readExcel(wb,circleId);
		} catch (Exception e) {
			map.put("success", false);
			map.put("message", "excel文件错误！");
			logger.error("excel上传失败！", e);
		}
		return map;

	}

	public Map<String, Object> readExcel(Workbook wb, String circleId) {
		Map<String, Object> map = new HashMap<String, Object>();
		// 得到第一个shell
		Sheet sheet = wb.getSheetAt(0);

		// excel名称与模型匹配
		String name = sheet.getSheetName();
		Module module = moduleService.getModuleByModuleName(name);
		// 得到Excel的行数
		int totalRows = sheet.getPhysicalNumberOfRows();
		if (totalRows <= 3) {
			map.put("success", false);
			map.put("message", "excel格式不正确！");
			return map;
		}
		// 总列数
		int totalCells = 0;
		// 得到Excel的列数(前提是有行数)，从第二行算起
		if (sheet.getRow(1) != null) {
			totalCells = sheet.getRow(1).getPhysicalNumberOfCells();
		}

		List<PoiTitle> titles = getTitle(module.getId());
//		if (totalCells <= 2 || totalCells < titles.size()) {
//			map.put("success", false);
//			map.put("message", "excel格式不正确！");
//			return map;
//		}

		String message = "";
		Boolean result = true;
		// 循环Excel行数,数据行从第四行开始
		for (int r = 3; r < totalRows; r++) {
			List<PoiTitle> pts = new ArrayList<PoiTitle>();
			// 循环Excel列数,数据列从第二列开始
			for (int c = 1; c < totalCells; c++) {
				if (c > titles.size()) {
					break;
				}
				PoiTitle pt = (PoiTitle) titles.get(c).clone();
				pt.setRowNum(r + 1);
				pt.setColumnNum(c + 1);
				Cell cc = sheet.getRow(r).getCell(c);
				if(cc!=null){
				      cc.setCellType(Cell.CELL_TYPE_STRING);
			     }
				pt.setFieldValue(cc==null ? "" :cc.getStringCellValue().trim());
				pts.add(pt);
			}
			// 验证并入库
			message += validatePoi(pts, module,circleId);
		}

		if (message.trim().length() > 0) {
			result = false;
		}

		map.put("success", result);
		map.put("message", message);
		return map;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
    @Transactional
	public String validatePoi(List<PoiTitle> pts, Module module, String circleId) {
		String message = "";
		// 表单格式验证
		for (PoiTitle pt : pts) {
			String fieldValue = pt.getFieldValue();
			boolean isError = false;
			message += "第" + pt.getRowNum() + "行第" + pt.getColumnNum() + "列"+"[列名:"+pt.getFormName()+"]";
			if (StringUtils.isEmpty(fieldValue)) {
			    message="";
				continue;
			}
			switch (pt.getFieldType()) {
			case "ID": // 主键
				Instance uploadInstance = instanceMapper.getInstanceById(fieldValue);
				if(org.apache.commons.lang.StringUtils.isNotBlank(fieldValue) && uploadInstance == null ){
                    message += "不存在该ID的数据实例";
                    isError = true;
                    return message;
				}
				if (null != uploadInstance && !uploadInstance.getModuleId().equals(module.getId())) {
					message += "数据实例模型对应错误";
					isError = true;
					return message;
				}
				break;
			case Constants.MODULE_FORM_TYPE_SINGLEROWTEXT: // 单行文本
			    if("Y_name".equals(pt.getFormCode())){
			        Map inMap = new HashMap();
			        inMap.put("name", fieldValue);
			        inMap.put("moduleId", module.getId());
			        List<Map> clist = instanceMapper.checkInstanceName(inMap);
			        if( StringUtils.isEmpty(pts.get(0).getFieldValue())){
			            if(clist != null && clist.size() > 0 ){
			            message += fieldValue+"名称重复";
			            isError = true;
			            return message;
			            }
			        }else{
			            Instance ins = instanceMapper.getInstanceById(pts.get(0).getFieldValue());
			            if(ins != null && !ins.getName().equals(fieldValue) && clist != null && clist.size() > 0 ){
		                     message += fieldValue+"名称重复";
		                        isError = true;
		                        return message;
			            }
			        }
			    }
			    
				if (null != pt.getMinLength() && fieldValue.length() < pt.getMinLength()) {
					message += "数据长度小于" + pt.getMinLength() + "个字";
					isError = true;
					return message;
				}
				if (null != pt.getMaxLength() && fieldValue.length() > pt.getMaxLength()) {
					message += "数据长度超过" + pt.getMaxLength() + "个字";
					isError = true;
					return message;
				}
				if (null != pt.getValidation()) {
					FormRule formRule = formRuleMapper.selectByCode(pt.getValidation());
					if (null != formRule && !StringUtils.matches(formRule.getRule(), fieldValue)) {
						message += pt.getValidation() + "格式不正确";
						isError = true;
						return message;
					}
				}
				break;
			case Constants.MODULE_FORM_TYPE_MULTIROWTEXT: // 多行文本
				if (null != pt.getMinLength() && fieldValue.length() < pt.getMinLength()) {
					message += "数据长度小于" + pt.getMinLength() + "个字";
					isError = true;
					return message;
				}
				if (null != pt.getMaxLength() && fieldValue.length() > pt.getMaxLength()) {
					message += "数据长度超过" + pt.getMaxLength() + "个字";
					isError = true;
					return message;
				}
				break;
			case Constants.MODULE_FORM_TYPE_LISTSEL: // 下拉菜单
				if (null != pt.getOptions() && !pt.getOptions().isEmpty() && !pt.getOptions().contains(fieldValue)) {
					message += "数据为无效选项";
					isError = true;
					return message;
				}
				break;
			case Constants.MODULE_FORM_TYPE_SINGLESEL: // 单选
				if (null != pt.getOptions() && !pt.getOptions().isEmpty() && !pt.getOptions().contains(fieldValue)) {
					message += "数据为无效选项";
					isError = true;
					return message;
				}
				break;
			case Constants.MODULE_FORM_TYPE_MULTISEL: // 多选
				if (null != pt.getOptions() && !pt.getOptions().isEmpty()) {
					String[] options = fieldValue.split(",");
					for (String option : options) {
						if (!pt.getOptions().contains(option)) {
							message += "数据包含无效选项";
							isError = true;
							return message;
						}
					}
				}
				break;
			case Constants.MODULE_FORM_TYPE_DOUBLE: // 小数
				if (!StringUtils.isNum(fieldValue)) {
					message += "数据无效";
					isError = true;
					return message;
				}
				Double doubleFieldValue = Double.valueOf(fieldValue);
				if (null != pt.getPrecision()) {
					BigDecimal bg = new BigDecimal(doubleFieldValue);
					doubleFieldValue = bg.setScale(Integer.valueOf(pt.getPrecision()), BigDecimal.ROUND_HALF_UP).doubleValue();
					pt.setFieldValue(doubleFieldValue.toString()); // 根据小数位数重新赋值
				}
				if (null != pt.getMin() && doubleFieldValue < pt.getMin()) {
					message += "数值小于最小值" + pt.getMin();
					isError = true;
					return message;
				}
				if (null != pt.getMax() && doubleFieldValue > pt.getMax()) {
					message += "数值超过最大值" + pt.getMin();
					isError = true;
					return message;
				}

				break;
			case Constants.MODULE_FORM_TYPE_INT:// 整数
				if (!StringUtils.isInt(fieldValue)) {
					message += "数据无效";
					isError = true;
					return message;
				}
				Double intFieldValue = Double.valueOf(fieldValue);
				if (null != pt.getMin() && intFieldValue < pt.getMin()) {
					message += "数值小于最小值" + pt.getMin();
					isError = true;
					return message;
				}
				if (null != pt.getMax() && intFieldValue > pt.getMax()) {
					message += "数值超过最大值" + pt.getMin();
					isError = true;
					return message;
				}
				break;

			case Constants.MODULE_FORM_TYPE_DATETIME:// 时间
				Date finalDate = null;
				String finalFormat = null;
				if (pt.getFormatDate()) {
					finalDate = DateUtils.getDateFromString(fieldValue, DateUtils.DEFAULT_DATE_FMT);
					if (null == finalDate) {
						message += "数据日期格式错误";
						isError = true;
						return message;
					}

					finalFormat = DateUtils.DEFAULT_DATE_FMT;
				} else {
					finalDate = DateUtils.getDateFromString(fieldValue, DateUtils.DEFAULT_DATETIME_FMT);
					if (null == finalDate) {
						message += "数据日期格式错误";
						isError = true;
						return message;
					}
					finalFormat = DateUtils.DEFAULT_DATETIME_FMT;
				}
				pt.setFieldValue(DateUtils.datetimeToString(finalFormat, finalDate)); // 重设日期
				break;
			case Constants.MODULE_FORM_TYPE_CASCADER: // 级联
				// 不做验证处理。
				break;
			case Constants.MODULE_RELATION_SOURCE: // 关系起点
				String[] targetNames = fieldValue.split("\n");
				// 根据实例获取模型id
				for (String targetName : targetNames) {
                    Map inMap = new HashMap();
                    inMap.put("name", targetName);
                    inMap.put("moduleId", pt.getModuleId());
                    List<Map> clist = instanceMapper.checkInstanceName(inMap);
                    if(clist != null && clist.size() > 0){
						String targetModuleId = pt.getModuleId();
						// 获取模型关系
						ModuleRelation mr = new ModuleRelation();
						mr.setSourceModuleId(module.getId());
						mr.setTargetModuleId(targetModuleId);
						mr = moduleRelationMapper.getModuleRelation(mr);
						if (null == mr) {
							message += "数据模型关系不存在";
							isError = true;
							return message;
						}

						if (!mr.getId().equals(pt.getField())) {
							message += "数据模型对应错误";
							isError = true;
							return message;
						}

						// 判断是否可建立多个关系
						if (mr.getRestriction().equals(Constants.MODULE_RELATION_ONETOONE)
								|| mr.getRestriction().equals(Constants.MODULE_RELATION_MANYTOONE)) {
//                            Map inMap = new HashMap();
//                            inMap.put("moduleRelationId", mr.getId());
//                            List<Map> clist = moduleRelationMapper.checkRelationInstanceList(inMap);
						    
							if (targetNames.length > 1 ) {
								message += "数据实例最多一个";
								isError = true;
								return message;
							}
							
					        Map map = new HashMap();
					        map.put("instanceIds", new String[]{(String) clist.get(0).get("id")});
					        map.put("targetModuleId", module.getId());
					        if(StringUtils.isNotEmpty(pts.get(0).getFieldValue())){
					            map.put("targetInstanceId", pts.get(0).getFieldValue());
					        }
					        List<Map> list = instanceRelationMapper.checkInstanceRelation(map);

					        if(list != null && list.size() > 0 ){
					            for(Map m:list){
		                               message += (m.get("sname") + "已经和" + m.get("tname") + "建立联系; \n ");
		                                isError = true;
		                               
					            }
                                 if(isError){
                                     return message;
                                 }
					       }
						}
					} else {
						message += "数据模型匹配失败";
						isError = true;
						return message;
					}
				}
				break;
			case Constants.MODULE_RELATION_TARGET: // 关系终点
				String[] sourceNames = fieldValue.split("\n");
				// 根据实例获取模型id
				for (String sourceName : sourceNames) {
                    Map inMap = new HashMap();
                    inMap.put("name", sourceName);
                    inMap.put("moduleId", pt.getModuleId());
                    List<Map> clist = instanceMapper.checkInstanceName(inMap);
                    if(clist != null && clist.size() > 0){
						String sourceModuleId = pt.getModuleId();
						// 获取模型关系
						ModuleRelation mr = new ModuleRelation();
						mr.setSourceModuleId(sourceModuleId);
						mr.setTargetModuleId(module.getId());
						mr = moduleRelationMapper.getModuleRelation(mr);
						if (null == mr) {
							message += "数据模型关系不存在";
							isError = true;
							return message;
						}

						if (!mr.getId().equals(pt.getField())) {
							message += "数据模型对应错误";
							isError = true;
							return message;
						}

						// 判断是否可建立多个关系
						if (mr.getRestriction().equals(Constants.MODULE_RELATION_ONETOONE)
								|| mr.getRestriction().equals(Constants.MODULE_RELATION_ONETOMANY)) {
//			                    Map inMap = new HashMap();
//			                    inMap.put("moduleRelationId", mr.getId());
//			                    List<Map> clist = moduleRelationMapper.checkRelationInstanceList(inMap);
						    
							if (sourceNames.length > 1 ) {
								message += "数据实例最多一个";
								isError = true;
								return message;
							}
							
	                         Map map = new HashMap();
	                         map.put("instanceIds", new String[]{(String) clist.get(0).get("id")});
	                         map.put("targetModuleId", module.getId());
	                            if(StringUtils.isNotEmpty(pts.get(0).getFieldValue())){
	                                map.put("targetInstanceId", pts.get(0).getFieldValue());
	                            }
	                            List<Map> list = instanceRelationMapper.checkInstanceRelation(map);

	                            if(list != null && list.size() > 0 ){
	                                for(Map m:list){
	                                       message += (m.get("sname") + "已经和" + m.get("tname") + "建立联系; \n ");
	                                        isError = true;
	                                       
	                                }
	                                 if(isError){
	                                     return message;
	                                 }
	                           }
						}
					} else {
						message += "数据模型匹配失败";
						isError = true;
						return message;
					}
				}
				break;
			default:
				break;
			}
			if(!isError){
			    message ="";
			}
		}
		
		if(org.apache.commons.lang.StringUtils.isNotBlank(message)){
		    return message;
		}
		

        
		// 判断新增/修改实例
		String id = pts.get(0).getFieldValue();
		Date date = new Date();
		Instance instance = null;
		Boolean isAdd = true;
		if (StringUtils.isEmpty(id)) { // 新增实例
			instance = new Instance();
			instance.setId(UUIDUtil.getUUID());
			instance.setIsDelete(0);
			instance.setModuleId(module.getId());
			instance.setName(pts.get(1).getFieldValue());
			instance.setInsertTime(date);
			instance.setUpdateTime(date);
			instanceMapper.addInstance(instance);
			
		} else { // 修改实例
		    isAdd = false;
	        instance = new Instance();
	        instance.setId(id);
			instance.setName(pts.get(1).getFieldValue());
	        instanceMapper.update(instance);

			// 删除实例关系
			instanceRelationMapper.deleteByInstanceId(id);
		}
		
        //圈子归属
        if(!StringUtils.isEmpty(circleId) && !"undefined".equals(circleId)){
            InstanceCircle instanceCircle = new InstanceCircle();
            instanceCircle.setCircleId(circleId);
            instanceCircle.setInstanceId(instance.getId());
            instanceCircleMapper.insert(instanceCircle);
        }

		// 新增表单数据和实例关系
		List<FormValue> formValues = new ArrayList<FormValue>();
		for (int c = 1; c < pts.size(); c++) {
			PoiTitle pt = pts.get(c);
			if(StringUtils.isEmpty(pt.getFieldValue())){
				continue;
			}
			if (Constants.MODULE_RELATION_SOURCE.equals(pt.getFieldType())) {
				String[] targerNames = pt.getFieldValue().split("\n");
				for (String targerName : targerNames) {
			        Map inMap = new HashMap();
			        inMap.put("name", targerName);
			        inMap.put("moduleId", pt.getModuleId());
			        List<Map> clist = instanceMapper.checkInstanceName(inMap);
			        if(clist != null && clist.size() > 0){
						String targetModuleId = (String) clist.get(0).get("moduleId");
						// 获取模型关系
						ModuleRelation mr = new ModuleRelation();
						mr.setSourceModuleId(module.getId());
						mr.setTargetModuleId(targetModuleId);
						mr = moduleRelationMapper.getModuleRelation(mr);

						// 建立实例关系
						InstanceRelation ir = new InstanceRelation();
						ir.setId(UUIDUtil.getUUID());
						ir.setModuleRelationId(mr.getId());
						ir.setSourceInstanceId(instance.getId());
						ir.setTargerInstanceId((String) clist.get(0).get("id"));
						ir.setInsertTime(date);
						ir.setUpdateTime(date);
						instanceRelationMapper.insert(ir);
						
						
						//写日志
						try{
    		                Map inmap = new HashMap();
    		                inmap.put("sourceInstanceId", instance.getId());
    		                inmap.put("moduleRelationId", mr.getId());
    		                List<Map> outList = configLogService.getRelationInfoList(inmap);
    		                if(outList!=null && outList.size()>0){
    		                    RelationLog relationLog = new RelationLog();
    		                    relationLog.setAction(Constants.LOG_ACTION_TYPE_ADDINSTANCE_RELATION_NAME);
    		                    relationLog.setCircleId(circleId);
    		                    relationLog.setId(UUIDUtil.getUUID());
    		                    relationLog.setInstanceId(instance.getId());
    		                    
    		                    if(!instance.getId().equals((String) outList.get(0).get("sourceInstanceId"))){
    		                        relationLog.setName((String) outList.get(0).get("targetInstanceName"));
    		                        relationLog.setTargetName((String) outList.get(0).get("sourceInstanceName"));
    		                        }else{
    		                            relationLog.setName((String) outList.get(0).get("sourceInstanceName"));
    		                            relationLog.setTargetName((String) outList.get(0).get("targetInstanceName")); 
    		                   }
    		                    relationLog.setRelationName((String) outList.get(0).get("relationName"));
    		                    relationLogMapper.insert(relationLog);
    		                }
						}catch(Exception e){
				            logger.error("实例编辑关系日志写入失败！instanceId:[" + instance.getId().toString() + "]", e);
				            e.printStackTrace();
						}
					}
				}
			} else if (Constants.MODULE_RELATION_TARGET.equals(pt.getFieldType())) {
				String[] sourceNames = pt.getFieldValue().split("\n");
				for (String sourceName : sourceNames) {
				    
                    Map inMap = new HashMap();
                    inMap.put("name", sourceName);
                    inMap.put("moduleId", pt.getModuleId());
                    List<Map> clist = instanceMapper.checkInstanceName(inMap);
                    if(clist != null && clist.size() > 0){

						String sourceModuleId = (String) clist.get(0).get("moduleId");
						// 获取模型关系
						ModuleRelation mr = new ModuleRelation();
						mr.setSourceModuleId(sourceModuleId);
						mr.setTargetModuleId(module.getId());
						mr = moduleRelationMapper.getModuleRelation(mr);
						// 建立实例关系
						InstanceRelation ir = new InstanceRelation();
						ir.setId(UUIDUtil.getUUID());
						ir.setModuleRelationId(mr.getId());
						ir.setSourceInstanceId((String) clist.get(0).get("id"));
						ir.setTargerInstanceId(instance.getId());
						ir.setInsertTime(date);
						ir.setUpdateTime(date);
						instanceRelationMapper.insert(ir);
						
	                      //写日志
                        try{
                            Map inmap = new HashMap();
                            inmap.put("sourceInstanceId", instance.getId());
                            inmap.put("moduleRelationId", mr.getId());
                            List<Map> outList = configLogService.getRelationInfoList(inmap);
                            if(outList!=null && outList.size()>0){
                                RelationLog relationLog = new RelationLog();
                                relationLog.setAction(Constants.LOG_ACTION_TYPE_ADDINSTANCE_RELATION_NAME);
                                relationLog.setCircleId(circleId);
                                relationLog.setId(UUIDUtil.getUUID());
                                relationLog.setInstanceId(instance.getId());
                                
                                if(!instance.getId().equals((String) outList.get(0).get("sourceInstanceId"))){
                                    relationLog.setName((String) outList.get(0).get("targetInstanceName"));
                                    relationLog.setTargetName((String) outList.get(0).get("sourceInstanceName"));
                                    }else{
                                        relationLog.setName((String) outList.get(0).get("sourceInstanceName"));
                                        relationLog.setTargetName((String) outList.get(0).get("targetInstanceName")); 
                               }
                                relationLog.setRelationName((String) outList.get(0).get("relationName"));
                                relationLogMapper.insert(relationLog);
                            }
                        }catch(Exception e){
                            logger.error("实例编辑关系日志写入失败！instanceId:[" + instance.getId().toString() + "]", e);
                            e.printStackTrace();
                        }
					}
				}
			} else {
				FormValue formValue = new FormValue();
				formValue.setId(UUIDUtil.getUUID());
				formValue.setFormId(pt.getFormId());
				formValue.setFormCode(pt.getFormCode());
				 
				if(pt.getFieldType().equals(Constants.MODULE_FORM_TYPE_MULTISEL)){ // 多选特殊处理
					String[] multiSel = pt.getFieldValue().split(",");
					List<String> rseult = new ArrayList<String>();
					for(String ms : multiSel){
						if(!ms.startsWith("\"") && !ms.endsWith("\"")){
		                    if(pt.getFormOptions() != null && pt.getFormOptions().size() > 0){
		                        for(FormOptions nf : pt.getFormOptions()){
		                                if(nf.getName().equals(ms)){
		                                    rseult.add("\"" + nf.getValue() + "\"");
		                                }
		                        }
		                    }
						}
					}
					pt.setFieldValue(rseult.toString());
				}else if(pt.getFieldType().equals(Constants.MODULE_FORM_TYPE_LISTSEL) || pt.getFieldType().equals(Constants.MODULE_FORM_TYPE_SINGLESEL)){
				    String rseult = "";
                    if(pt.getFormOptions() != null && pt.getFormOptions().size() > 0){
                        for(FormOptions nf : pt.getFormOptions()){
                                if(nf.getName().equals(pt.getFieldValue())){
                                    rseult=nf.getValue();
                                }
                        }
                    }
                    pt.setFieldValue(rseult);
				}else if(pt.getFieldType().equals(Constants.MODULE_FORM_TYPE_CASCADER)){
                    String[] multiSel = pt.getFieldValue().split(",");
                    List<String> rseult = new ArrayList<String>();
                    if(pt.getCasoptionsBeans() != null && pt.getCasoptionsBeans().size() > 0){
                        for(CasoptionsBean nf1 : pt.getCasoptionsBeans()){
                            for(String ms : multiSel){
                                if(!ms.startsWith("\"") && !ms.endsWith("\"")){
                                                //一层
                                                if(nf1.getLabel().equals(ms)){
                                                    rseult.add("\"" + nf1.getValue() + "\"");
                                                }
                                                //二层
                                                if(nf1.getChildren()!= null && nf1.getChildren().size()>0){
                                                    for(CasoptionsBean nf2: nf1.getChildren()){
                                                        if(nf2.getLabel().equals(ms)){
                                                            rseult.add("\"" + nf2.getValue() + "\"");
                                                        }
                                                            if(nf2.getChildren()!= null && nf2.getChildren().size()>0){
                                                                for(CasoptionsBean nf3: nf2.getChildren()){
                                                                    if(nf3.getValue().equals(ms)){
                                                                        rseult.add("\"" + nf3.getValue() + "\"");
                                                                    }
                                                                }
                                                            }
                                                    }
                                                }
                                 }
                                        
                             }
                        }
                  }

                    pt.setFieldValue(rseult.toString());
                }
				
				formValue.setFormValue(pt.getFieldValue());
				
				formValue.setInstanceId(instance.getId());
				formValues.add(formValue);
			}
		}
		if(isAdd){
            configLogService.saveInstanceLog(instance.getId() ,Constants.LOG_ACTION_TYPE_ADDINSTANCE_NAME);
		}else{
            //写日志
            InstanceModel im = new InstanceModel();
            im.setId(id);
            im.setName(pts.get(1).getFieldValue());
            Map inMap = new HashMap();
            inMap.put("instanceId", id);
            im.setFormValues(formValues);
            configLogService.saveInstanceUpdateLog(im);
            
            // 删除实例所有表单数据
            formValueMapper.deleteByInstanceId(id);
		}
		formValueMapper.insert(formValues);

		return message;
	}
	
    @Override
    public List<Module> selectModule() {
        return maintainViewMapper.selectModule();
    }
}
