package com.aspire.ums.cmdb.repertory.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.aspire.ums.cmdb.module.entity.Module;
import com.aspire.ums.cmdb.module.service.ModuleService;
import com.aspire.ums.cmdb.repertory.service.RepertoryService;
import com.aspire.ums.cmdb.util.ExcelImportUtils;
import com.aspire.ums.cmdb.util.StringUtils;

@RestController
@RequestMapping("/cmdb/repertory")
public class RepertoryController {

	private final Logger logger = Logger.getLogger(getClass());

	@Autowired
	private RepertoryService repertoryService;

	@Autowired
	ModuleService moduleService;

	@RequestMapping("/downloadData/{moduleId}")
	public void downloadData(@PathVariable("moduleId") String moduleId, HttpServletResponse response) {
		if (StringUtils.isEmpty(moduleId)) {
			logger.info("模型编号不能为空！");
			return;
		}
		try {
			String ModuleName = repertoryService.getModuleName(moduleId);
			if (StringUtils.isEmpty(ModuleName)) {
				logger.info("未找到编号为[" + moduleId + "]的模型！");
				return;
			}
			byte[] bytes = repertoryService.getExcelData(moduleId);
			response.setContentType("application/x-msdownload");
			response.setHeader("Content-Disposition", "attachment;filename=" + new String(ModuleName.getBytes(), "ISO8859-1") + ".xls");
			response.setContentLength(bytes.length);
			response.getOutputStream().write(bytes);
		} catch (Exception e) {
			logger.error("模型[" + moduleId + "]实例数据下载失败！", e);
		} finally {
			try {
				response.getOutputStream().flush();
				response.getOutputStream().close();
			} catch (Exception e2) {
				// TODO: handle exception
			}
		}

	}

	@RequestMapping("/downloadExcel/{moduleId}")
	public void downloadExcel(@PathVariable("moduleId") String moduleId, HttpServletResponse response) {
		if (StringUtils.isEmpty(moduleId)) {
			logger.info("模型编号不能为空！");
			return;
		}
		try {
			String ModuleName = repertoryService.getModuleName(moduleId);
			if (StringUtils.isEmpty(ModuleName)) {
				logger.info("未找到编号为[" + moduleId + "]的模型！");
				return;
			}
			byte[] bytes = repertoryService.getExcel(moduleId);
			response.setContentType("application/x-msdownload");
			response.setHeader("Content-Disposition", "attachment;filename=" + new String(ModuleName.getBytes(), "ISO8859-1") + ".xls");
			response.setContentLength(bytes.length);
			response.getOutputStream().write(bytes);
		} catch (Exception e) {
			logger.error("模型[" + moduleId + "]实例数据下载失败！", e);
		} finally {
			try {
				response.getOutputStream().flush();
				response.getOutputStream().close();
			} catch (Exception e2) {
				// TODO: handle exception
			}
		}

	}

	@RequestMapping("/uploadData/{circleId}")
	public Map<String, Object> uploadData(@PathVariable(value = "filename") MultipartFile file,@PathVariable("circleId") String circleId) {
		Map<String, Object> map = new HashMap<String, Object>();

		if (null == file) {
			map.put("success", false);
			map.put("message", "文件不能为空！");
			return map;
		}

		String fileName = file.getOriginalFilename();
		if (!ExcelImportUtils.validateExcel(fileName)) {
			map.put("success", false);
			map.put("message", "文件必须是excel格式！");
			return map;
		}

		long size = file.getSize();
		if (StringUtils.isEmpty(fileName) || size == 0) {
			map.put("success", false);
			map.put("message", "文件不能为空！");
			return map;
		}

		// excel名称与模型匹配
		Module module = moduleService.getModuleByModuleName(fileName.substring(0, fileName.indexOf(".")));
		if (null == module) {
			map.put("success", false);
			map.put("message", "excel名称未匹配到模型！");
			return map;
		}
		map = repertoryService.uploadData(file, module,circleId);
		return map;
	}

}
