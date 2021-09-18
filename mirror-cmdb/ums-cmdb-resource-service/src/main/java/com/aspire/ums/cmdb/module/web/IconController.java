package com.aspire.ums.cmdb.module.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aspire.ums.cmdb.module.entity.Icon;
import com.aspire.ums.cmdb.module.mapper.IconMapper;
import com.aspire.ums.cmdb.module.service.IconService;
import com.aspire.ums.cmdb.util.BeansUtil;
import com.aspire.ums.cmdb.util.BusinessException;
import com.aspire.ums.cmdb.util.PagedResult;
import com.github.pagehelper.PageHelper;
/**
 * 
 * <p>Project: ums-cmdb-service</p>
 *
 * @Description: 获取模型图标信息
 *
 * @author: mingjianyong
 *
 * @Date: 2017-6-28
 */
@RestController
@RequestMapping("/cmdb/icon/")
public class IconController {
	
	@Autowired
	private IconMapper iconMapper;
	@Autowired
	private IconService iconService;
	
	@RequestMapping("getIcons")
	public PagedResult getIcon(Icon icon, Integer pageNumber,Integer pageSize){
		System.out.println(pageSize);
		PageHelper.startPage(pageNumber, pageSize ,"id");
		List<Map> list = iconMapper.selectIconAll(icon);
		System.out.println(list.size());
		
		return BeansUtil.toPagedResult(list);
	}
	@RequestMapping("/uploadIcon")
	public Map<String,Object> uploadIcon(HttpServletRequest request){
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			iconService.uploadIcon(request);
			map.put("success", true);
		} catch (BusinessException e) {
			map.put("message",new String(e.getMessage()));
			map.put("success", false);
		} catch (Exception e) {
			map.put("message",e.getMessage());
			map.put("success", false);
			e.printStackTrace();
		}
		return map;
	}
}
