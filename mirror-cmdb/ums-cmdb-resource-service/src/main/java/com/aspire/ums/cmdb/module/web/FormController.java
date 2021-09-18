package com.aspire.ums.cmdb.module.web;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import com.aspire.ums.cmdb.module.entity.*;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.LoggerFactory;

import com.aspire.ums.cmdb.common.Constants;
import com.aspire.ums.cmdb.module.service.FormService;
import com.aspire.ums.cmdb.util.BusinessException;

import io.swagger.annotations.ApiOperation;
/**
 * 
 * <p>Project: ums-cmdb-service</p>
 *
 * @Description: 
 *
 * @author: mingjianyong
 *
 * @Date: 2017-7-7
 */
@RestController
@RequestMapping("/cmdb/form/")
public class FormController {
	
	@Autowired
	private FormService formService;
	 
    private static final Logger LOGGER = LoggerFactory.getLogger(FormController.class);

	
	@RequestMapping("updateForm")
	public Map<String,Object> updateForms(@RequestBody Map<String,Object> forms){
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			formService.updateForm(forms);
			map.put("success", true);
		} catch (BusinessException e) {
			e.printStackTrace();
			map.put("success", false);
			map.put("message",e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			map.put("success", false);
			map.put("message","保存失败！");
		}
		return map;
	}

	@ResponseBody
	@RequestMapping("/getForms")
	public Map<String,Object> getForms(@RequestBody Module module){
		List<FormBean> fbs = null;
		Map<String,Object> re = new HashMap<String,Object>();
		Map<String,Object> casoptions = new HashMap<String,Object>();
		try {
			fbs = formService.getForms(module);
			for(FormBean fb : fbs){
				if(Constants.MODULE_FORM_TYPE_CASCADER.equals(fb.getType())){
					List<OptionBean> obs = formService.getCascaderOptions(fb.getId());
					System.out.println(obs);
					casoptions.put(fb.getId(), obs);
				}
			}
			re.put("formData", fbs);
			re.put("cssops",casoptions);
		} catch (BusinessException e) {
			e.printStackTrace();
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return re;
	}
	@RequestMapping("getFormRule")
	public List<FormRule> getFormRule(){
		return formService.getFormRule();
	}
	/**
	 * 
	 *Description:根据formId获取脚本信息
	 * @return
	 */
	@RequestMapping("getScriptByformId")
	public FormScript getScriptByformId(String formId){
		FormScript result = formService.getScriptByformId(formId);
		System.out.println(result);
		return result;
	}
	
	@RequestMapping(value = "getBusiness/{businessCodes}", method = RequestMethod.GET)
	public List<Map<String, String>> getBusiness(@PathVariable("businessCodes") String businessCodes) {
    	LOGGER.info("!!!!!!!!getFirstBusiness!!!!!!!!!!!参数" + businessCodes);
		List<Map<String, String>> result = new ArrayList<>();
		if (null != businessCodes) {
			String[] idArr = businessCodes.split(",");
			List<String> ids = Arrays.asList(idArr);
			result = formService.getFirstBusiness(ids);
		}
		return result;
	}
	
	@RequestMapping(value = "findBusCodeAndName/{id}", method = RequestMethod.GET)
	@ApiOperation(value = "", notes = "")
	public List<Map<String, String>> findBusCodeAndName(@PathVariable("id") String id) {
		LOGGER.info("!!!!!!!!findBusCodeAndName!!!!!!!!!!!参数" + id);
		List<Map<String, String>> result = new ArrayList<>();
		if (null != id) {
			String[] idArr = id.split(",");
			List<String> ids = Arrays.asList(idArr);
			result = formService.findBusCodeAndName(ids);
		}
		return result;
	}
}
