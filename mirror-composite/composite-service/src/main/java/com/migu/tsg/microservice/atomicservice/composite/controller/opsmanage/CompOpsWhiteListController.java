package com.migu.tsg.microservice.atomicservice.composite.controller.opsmanage;

import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.aspire.mirror.composite.service.opsmanage.ICompOpsWhiteListService;
import com.aspire.mirror.ops.api.domain.GeneralResponse;
import com.aspire.mirror.ops.api.domain.PageListQueryResult;
import com.aspire.mirror.ops.api.domain.whitelist.OpsWhitelistConstraint;
import com.aspire.mirror.ops.api.domain.whitelist.OpsWhitelistCruiseCheck;
import com.aspire.mirror.ops.api.domain.whitelist.OpsWhitelistCruiseCheck.OpsWhitelistCruiseCheckQueryParam;
import com.aspire.mirror.ops.api.domain.whitelist.OpsWhitelistHost;
import com.aspire.mirror.ops.api.domain.whitelist.OpsWhitelistHost.OpsWhitelistHostQueryParam;
import com.aspire.mirror.ops.api.domain.whitelist.OpsWhitelistVulnerability;
import com.aspire.mirror.ops.api.domain.whitelist.OpsWhitelistVulnerability.OpsWhitelistVulnerabilityQueryParam;
import com.aspire.mirror.ops.api.domain.whitelist.OpsWhitelistBaseline;
import com.aspire.mirror.ops.api.domain.whitelist.OpsWhitelistBaseline.OpsWhitelistBaselineQueryParam;
import com.aspire.mirror.ops.api.domain.whitelist.WhitelistConst.WhitelistTypeEnum;
import com.aspire.mirror.template.api.dto.model.MonitorItemPrototype;
import com.fasterxml.jackson.core.type.TypeReference;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.opsmanage.OpsWhitelistManageClient;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.EasyPoiUtil;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.JsonUtil;

import cn.afterturn.easypoi.excel.entity.ExportParams;
import lombok.extern.slf4j.Slf4j;

/**
 * @projectName: CompOpsWhiteListController
 * @description: 类
 * @author: xuxixuan
 * @create: 2021-03-011 9:12
 **/
@Slf4j
@RestController
public class CompOpsWhiteListController implements ICompOpsWhiteListService {
	
	@Autowired
	private OpsWhitelistManageClient whiteListClient;

	@Override
	@ResponseStatus(HttpStatus.OK)
	public PageListQueryResult<OpsWhitelistHost> queryWhitelistHostList(@RequestBody OpsWhitelistHostQueryParam queryParam) {
		return whiteListClient.queryWhitelistHostList(queryParam);
	}

	@Override
	@ResponseStatus(HttpStatus.OK)
	public OpsWhitelistHost queryOpsWhitelistHostByKeys(@RequestBody OpsWhitelistHost queryParam) {
		return whiteListClient.queryOpsWhitelistHostByKeys(queryParam);
	}

	@Override
	@ResponseStatus(HttpStatus.OK)
	public GeneralResponse saveWhitelistHost(@RequestBody OpsWhitelistHost whitelistHost) {
		return whiteListClient.saveWhitelistHost(whitelistHost);
	}
	
	@Override
	@ResponseStatus(HttpStatus.OK)
	public GeneralResponse batchSaveWhitelistHost(@RequestBody List<OpsWhitelistHost> batchWhitelistHost) {
		return whiteListClient.batchSaveWhitelistHost(batchWhitelistHost);
	}
	
	@Override
	@ResponseStatus(HttpStatus.OK)
	public GeneralResponse saveWhitelistLinkConstraints(@RequestBody Set<OpsWhitelistConstraint> linkConstraints) {
		return whiteListClient.saveWhitelistLinkConstraints(linkConstraints);
	}
	
	@Override
	@ResponseStatus(HttpStatus.OK)
	public List<OpsWhitelistConstraint> queryWhitelistConstraintListByTypeAndId(
			@PathVariable("whitelistType") WhitelistTypeEnum whitelistType, @PathVariable("whitelistId") String whitelistId) {
		return whiteListClient.queryWhitelistConstraintListByTypeAndId(whitelistType, whitelistId);
	}

	@Override
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void removeWhitelistHostById(@PathVariable("whitelistHostId") String whitelistHostId) {
		whiteListClient.removeWhitelistHostById(whitelistHostId);
	}
	
	@Override
	public Map<String, Object> importWhitelistHost(@RequestParam("file") MultipartFile file) {
        Map<String, Object> returnMap = new HashMap<>();
        returnMap.put("flag", "false");
        if (null == file) {
            returnMap.put("message", "文件不能为空！");
            return returnMap;
        }
        log.info("上传文件名称 -> {}", file.getOriginalFilename());
        if (!file.getOriginalFilename().matches("^.+\\.(?i)(xlsx)$") && !file.getOriginalFilename().matches("^.+\\.(?i)(xls)$")) {
            returnMap.put("message", "文件必须是excel格式！");
            return returnMap;
        }
        log.info("上传文件大小 -> {}", file.getSize());
        if (file.getSize() == 0) {
            returnMap.put("message", "文件不能为空！");
            return returnMap;
        }
        InputStream is = null;
        Workbook wb;
        try {
            is = file.getInputStream();
            wb = WorkbookFactory.create(is);
            Sheet sheet = wb.getSheetAt(0);
            int totalRows = sheet.getPhysicalNumberOfRows();
            if (totalRows <= 1) {
                returnMap.put("flag", "false");
                returnMap.put("message", "Excel格式不正确, 请先下载Excel模板后, 填写数据再上传!");
                return returnMap;
            }
            List<MutablePair<String, String>> poolNameMapAgentIp = new ArrayList<>();
            for (int r = 1; r < totalRows; r++) {
                if (sheet.getRow(r) == null) {
                    continue;
                }
                String poolName = getDataCellText(sheet.getRow(r).getCell(0));
                String ip = getDataCellText(sheet.getRow(r).getCell(1));
                poolNameMapAgentIp.add(MutablePair.of(poolName, ip));
            }
            
            GeneralResponse batchImportResponse = whiteListClient.batchImportOpsWhitelistHost(poolNameMapAgentIp);
            TypeReference<Map<String, List<OpsWhitelistHost>>> typeRef = new TypeReference<Map<String, List<OpsWhitelistHost>>>(){};
            Map<String, List<OpsWhitelistHost>> bizData = JsonUtil.jacksonConvert(batchImportResponse.getBizData(), typeRef);
            returnMap.put("flag", true);
            returnMap.put("errorHostList", bizData.get("errorHostList"));
            returnMap.put("successHostList", bizData.get("successHostList"));
            return returnMap;
        } catch (Exception e) {
            returnMap.put("flag", "false");
            returnMap.put("message", "解析Excel文件失败:" + e.getMessage());
            returnMap.put("error", e.getMessage());
            return returnMap;
        } finally {
        	if (is != null) {
        		try {
        			is.close();
        		} catch (Exception e) {
        			log.error(null, e);
        		}
        	}
        }
    }
	
	public void exportWhitelistHostList(@RequestBody OpsWhitelistHostQueryParam queryParam, HttpServletResponse response) {
		PageListQueryResult<OpsWhitelistHost> pageResult = whiteListClient.queryWhitelistHostList(queryParam);
		List<OpsWhitelistHost> resultList = pageResult.getDataList();
		resultList = resultList == null ? new ArrayList<>() : resultList;
		
		String fileName = "host_blacklist_export.xls";
		String title = "主机白名单列表";
		String sheetName = "主机白名单列表";
		ExportParams exportParam = new ExportParams(title, sheetName);
		EasyPoiUtil.exportExcel(resultList, exportParam, MonitorItemPrototype.class, fileName, response);
	}
	
	@SuppressWarnings("deprecation")
	protected String getDataCellText(Cell dataCell) {
		String cellValue = "";
		if (dataCell != null) {
			// 判断数据的类型
			switch (dataCell.getCellType()) {
			case Cell.CELL_TYPE_NUMERIC: // 数字
				if (HSSFDateUtil.isCellDateFormatted(dataCell)) {// 处理日期格式、时间格式
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					Date date = dataCell.getDateCellValue();
					cellValue = sdf.format(date);
				} else if (dataCell.getCellStyle().getDataFormat() == 0) {// 处理数值格式
																			// General格式
					DecimalFormat df = new DecimalFormat("#.##########");
					Double value = dataCell.getNumericCellValue();
					cellValue = df.format(value);
				} else {
					if ("#,##0.00".equals(dataCell.getCellStyle().getDataFormatString())) { // 货币格式‘#,##0.00’会在数字后添加随机的浮点数
						cellValue = String.valueOf(dataCell.getNumericCellValue());
					} else {
						// dataCell.setCellType(Cell.CELL_TYPE_STRING);
						// cellValue =
						// String.valueOf(dataCell.getRichStringCellValue().toString());
						DecimalFormat df = new DecimalFormat("#.##########");
						Double value = dataCell.getNumericCellValue();
						cellValue = df.format(value);
					}
				}
				break;
			case Cell.CELL_TYPE_STRING: // 字符串
				cellValue = String.valueOf(dataCell.getStringCellValue());
				break;
			case Cell.CELL_TYPE_BOOLEAN: // Boolean
				cellValue = String.valueOf(dataCell.getBooleanCellValue());
				break;
			case Cell.CELL_TYPE_FORMULA: // 公式
				cellValue = String.valueOf(dataCell.getCellFormula());
				break;
			default:
				break;
			}
		}
		return cellValue;
	}
	
	/**
	 * Methor:巡检白名单Controller
	 */
	
	@Override
	@ResponseStatus(HttpStatus.OK)
	public PageListQueryResult<OpsWhitelistCruiseCheck> queryWhitelistCruiseCheckList(
			@RequestBody OpsWhitelistCruiseCheckQueryParam queryParam) {
		return whiteListClient.queryWhitelistCruiseCheckList(queryParam);
	}

	@Override
	@ResponseStatus(HttpStatus.OK)
	public OpsWhitelistCruiseCheck queryOpsWhitelistCruiseCheckByKeys(@RequestBody OpsWhitelistCruiseCheck queryParam) {
		return whiteListClient.queryOpsWhitelistCruiseCheckByKeys(queryParam);
	}

	@Override
	@ResponseStatus(HttpStatus.OK)
	public GeneralResponse saveWhitelistCruiseCheck(@RequestBody OpsWhitelistCruiseCheck whitelistCruiseCheck) {
		return whiteListClient.saveWhitelistCruiseCheck(whitelistCruiseCheck);
	}

	@Override
	@ResponseStatus(HttpStatus.OK)
	public GeneralResponse removeWhitelistCruiseCheckById(@PathVariable("whitelistCruiseCheckId") String whitelistCruiseCheckId) {
		return whiteListClient.removeWhitelistCruiseCheckById(whitelistCruiseCheckId);
	}
	
	/**
	 * Methor:漏洞白名单Controller
	 */
	
	@Override
	@ResponseStatus(HttpStatus.OK)
	public PageListQueryResult<OpsWhitelistVulnerability> queryWhitelistVulnerabilityList(@RequestBody OpsWhitelistVulnerabilityQueryParam queryParam) {
		return whiteListClient.queryWhitelistVulnerabilityList(queryParam);
	}

	@Override
	@ResponseStatus(HttpStatus.OK)
	public OpsWhitelistVulnerability queryOpsWhitelistVulnerabilityByKeys(@RequestBody OpsWhitelistVulnerability queryParam) {
		return whiteListClient.queryOpsWhitelistVulnerabilityByKeys(queryParam);
	}

	@Override
	@ResponseStatus(HttpStatus.OK)
	public GeneralResponse saveWhitelistVulnerability(@RequestBody OpsWhitelistVulnerability whitelistVulnerability) {
		return whiteListClient.saveWhitelistVulnerability(whitelistVulnerability);
	}

	@Override
	@ResponseStatus(HttpStatus.OK)
	public GeneralResponse  removeWhitelistVulnerabilityById(@PathVariable("whitelistVulnerabilityId") String whitelistVulnerabilityId) {
		return whiteListClient.removeWhitelistVulnerabilityById(whitelistVulnerabilityId);
	}
	
	
	/**
	 * Methor:基线白名单Controller
	 */
	
	@Override
	@ResponseStatus(HttpStatus.OK)
	public PageListQueryResult<OpsWhitelistBaseline> queryWhitelistBaselineList(@RequestBody OpsWhitelistBaselineQueryParam queryParam) {
		return whiteListClient.queryWhitelistBaselineList(queryParam);
	}

	@Override
	@ResponseStatus(HttpStatus.OK)
	public OpsWhitelistBaseline queryOpsWhitelistBaselineByKeys(@RequestBody OpsWhitelistBaseline queryParam) {
		return whiteListClient.queryOpsWhitelistBaselineByKeys(queryParam);
	}

	@Override
	@ResponseStatus(HttpStatus.OK)
	public GeneralResponse saveWhitelistBaseline(@RequestBody OpsWhitelistBaseline whitelistBaseline) {
		return whiteListClient.saveWhitelistBaseline(whitelistBaseline);
	}

	@Override
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public GeneralResponse removeWhitelistBaselineById(@PathVariable("whitelistBaselineId") String whitelistBaselineId) {
		return whiteListClient.removeWhitelistBaselineById(whitelistBaselineId);
		
	}
}
