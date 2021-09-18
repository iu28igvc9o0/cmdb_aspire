package com.aspire.ums.cmdb.repertory.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.aspire.ums.cmdb.module.entity.Module;

public interface RepertoryService {
	
	/**
	 * 下载模板，获取byte
	 * @param moduleId
	 * @return
	 */
	public byte[] getExcel(String moduleId);
	
	/**
	 * 下载数据，获取byte
	 * @param moduleId
	 * @return
	 */
	public byte[] getExcelData(String moduleId);
	
	/**
	 * 根据模型id获取模型名称
	 * @param moduleId
	 * @return
	 */
	public String getModuleName(String moduleId);
	
	/**
	 * 模型实例上传
	 * @param file
	 * @param
	 * @return
	 */
	public Map<String,Object> uploadData(MultipartFile file, String circleId);
	
	
	
	
	public List<Module> selectModule();
	
}
