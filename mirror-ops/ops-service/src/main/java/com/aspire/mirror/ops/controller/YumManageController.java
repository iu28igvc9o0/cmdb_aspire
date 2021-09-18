/**
 *
 * 项目名： ops-service 
 * <p/> 
 *
 * 文件名:  OpsManageController.java 
 * <p/>
 *
 * 功能描述: TODO 
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2019年10月30日 
 *
 * @version	V1.0
 * <p/>
 *
 *<b>Copyright(c)</b> 2019 卓望公司-版权所有<br/>
 *   
 */
package com.aspire.mirror.ops.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.aspire.mirror.ops.api.domain.GeneralResponse;
import com.aspire.mirror.ops.api.domain.OpsYumFileGroup;
import com.aspire.mirror.ops.api.domain.OpsYumFileGroupQueryModel;
import com.aspire.mirror.ops.api.domain.OpsYumFileModel;
import com.aspire.mirror.ops.api.domain.OpsYumFileQueryModel;
import com.aspire.mirror.ops.api.domain.OsDistributionModel;
import com.aspire.mirror.ops.api.domain.PageListQueryResult;
import com.aspire.mirror.ops.api.domain.YumFileServerSource;
import com.aspire.mirror.ops.api.service.IYumManageService;
import com.aspire.mirror.ops.biz.OpsYumBiz;

/** 
 *
 * 项目名称: ops-service 
 * <p/>
 * 
 * 类名: YumManageController
 * <p/>
 *
 * 类功能描述: TODO
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2019年10月30日  
 *
 * @version	V1.0 
 * <br/>
 *
 * <b>Copyright(c)</b> 2019 卓望公司-版权所有 
 *
 */
@RestController
public class YumManageController implements IYumManageService {
	@Autowired
	private OpsYumBiz				yumBiz;
	
	@Override
	@ResponseStatus(HttpStatus.OK)
	public List<OsDistributionModel> fetchOsDistributionList() {
		return yumBiz.fetchOsDistributionList();
	}
	
	@Override
	@ResponseStatus(HttpStatus.OK)
	public List<OpsYumFileGroup> queryYumFileGroupTreeList(@RequestBody OpsYumFileGroupQueryModel param) {
		return yumBiz.queryYumFileGroupList(param);
	}
	
	@Override
	@ResponseStatus(HttpStatus.OK)
	public GeneralResponse uploadYumLocalFile(@RequestParam("file") MultipartFile file) {
		return yumBiz.uploadYumLocalFile(file);
	}
	
	@Override
	@ResponseStatus(HttpStatus.OK)
	public GeneralResponse transferYumFile(@RequestBody YumFileServerSource serverSource) {
		return yumBiz.transferYumFile(serverSource);
	}
	
	@Override
	@ResponseStatus(HttpStatus.OK)
	public GeneralResponse createYumFileGroup(@RequestBody OpsYumFileGroup group) {
		return yumBiz.createYumFileGroup(group);
	}
	
	@Override
	@ResponseStatus(HttpStatus.OK)
	public GeneralResponse updateYumFileGroup(@RequestBody OpsYumFileGroup group) {
		return yumBiz.updateYumFileGroup(group);
	}
	
	@Override
	@ResponseStatus(HttpStatus.OK)
	public GeneralResponse removeYumFileGroup(@PathVariable("yumFileGroupId") Long yumFileGroupId) {
		return yumBiz.removeYumFileGroup(yumFileGroupId);
	}
	
	@Override
	@ResponseStatus(HttpStatus.OK)
	public PageListQueryResult<OpsYumFileModel> queryYumSourceList(@RequestBody OpsYumFileQueryModel param) {
		param.setFileType(OpsYumFileModel.FILE_TYPE_YUM_SOURCE);
		return yumBiz.queryYumFileList(param);
	}
	
	@Override
	@ResponseStatus(HttpStatus.OK)
	public GeneralResponse createYumSourceModel(@RequestBody OpsYumFileModel yumSource) {
		yumSource.setFileType(OpsYumFileModel.FILE_TYPE_YUM_SOURCE);
		return yumBiz.createYumFileModel(yumSource);
	}
	
	@Override
	@ResponseStatus(HttpStatus.OK)
	public GeneralResponse updateYumSourceModel(@RequestBody OpsYumFileModel yumSource) {
		return yumBiz.updateYumFileModel(yumSource);
	}
	
	@Override
	@ResponseStatus(HttpStatus.OK)
	public GeneralResponse removeYumSourceModel(@PathVariable("yumSourceId") Long yumSourceId) {
		return yumBiz.removeYumFileModel(yumSourceId);
	}
	
	@Override
	@ResponseStatus(HttpStatus.OK)
	public PageListQueryResult<OpsYumFileModel> queryYumConfigList(@RequestBody OpsYumFileQueryModel param) {
		param.setFileType(OpsYumFileModel.FILE_TYPE_YUM_CONFIG);
		return yumBiz.queryYumFileList(param);
	}
	
	@Override
	@ResponseStatus(HttpStatus.OK)
	public GeneralResponse createYumConfigModel(@RequestBody OpsYumFileModel yumConfig) {
		yumConfig.setFileType(OpsYumFileModel.FILE_TYPE_YUM_CONFIG);
		return yumBiz.createYumFileModel(yumConfig);
	}
	
	@Override
	@ResponseStatus(HttpStatus.OK)
	public GeneralResponse updateYumConfigModel(@RequestBody OpsYumFileModel yumConfig) {
		yumConfig.setFileType(OpsYumFileModel.FILE_TYPE_YUM_CONFIG);
		return yumBiz.updateYumFileModel(yumConfig);
	}
	
	@Override
	@ResponseStatus(HttpStatus.OK)
	public GeneralResponse removeYumConfigModel(@PathVariable("yumConfigId") Long yumConfigId) {
		return yumBiz.removeYumFileModel(yumConfigId);
	}
}
