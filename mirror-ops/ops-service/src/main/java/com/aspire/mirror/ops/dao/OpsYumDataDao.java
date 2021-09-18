/**
 *
 * 项目名： ops-service 
 * <p/> 
 *
 * 文件名:  OpsManageDataDao.java 
 * <p/>
 *
 * 功能描述: TODO 
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2019年10月29日 
 *
 * @version	V1.0
 * <p/>
 *
 *<b>Copyright(c)</b> 2019 卓望公司-版权所有<br/>
 *   
 */
package com.aspire.mirror.ops.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.aspire.mirror.ops.api.domain.OpsYumFileGroup;
import com.aspire.mirror.ops.api.domain.OpsYumFileGroupQueryModel;
import com.aspire.mirror.ops.api.domain.OpsYumFileModel;
import com.aspire.mirror.ops.api.domain.OpsYumFileQueryModel;
import com.aspire.mirror.ops.api.domain.OsDistributionModel;

/** 
 *
 * 项目名称: ops-service 
 * <p/>
 * 
 * 类名: OpsDataDao
 * <p/>
 *
 * 类功能描述: TODO
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2019年10月29日  
 *
 * @version	V1.0 
 * <br/>
 *
 * <b>Copyright(c)</b> 2019 卓望公司-版权所有 
 *
 */
@Mapper
public interface OpsYumDataDao {
	
	List<OsDistributionModel> fetchOsDistributionList();
	
	Integer queryTotalYumFileGroupCount(OpsYumFileGroupQueryModel param);
	
	List<OpsYumFileGroup> queryYumFileGroupList(OpsYumFileGroupQueryModel param);
	
	void createYumFileGroup(OpsYumFileGroup group);
	
	void updateYumFileGroup(OpsYumFileGroup group);
	
	void removeYumFileGroup(@Param("yumFileGroupId") Long yumFileGroupId);
	
	List<OpsYumFileModel> queryYumFileList(OpsYumFileQueryModel param);
	
	Integer queryTotalYumFileCount(OpsYumFileQueryModel param);
	
	OpsYumFileModel queryYumFileModelById(@Param("yumFileId") Long yumSourceId);
	
	void createYumFileModel(OpsYumFileModel yumSource);
	
	void updateYumFileModel(OpsYumFileModel yumSource);
	
	void removeYumFileModel(@Param("yumFileId") Long yumFileId);
}
