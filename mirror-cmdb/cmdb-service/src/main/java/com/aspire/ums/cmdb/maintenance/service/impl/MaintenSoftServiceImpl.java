package com.aspire.ums.cmdb.maintenance.service.impl;

import java.text.SimpleDateFormat;
import java.util.*;

import com.aspire.ums.cmdb.maintenance.mapper.CmdbMaintenanceSoftwareRecordMapper;
import com.aspire.ums.cmdb.maintenance.payload.CmdbMaintenanceSoftwareRecord;
import com.aspire.ums.cmdb.maintenance.payload.CmdbMaintenanceSoftwareRecordQuery;
import com.aspire.ums.cmdb.util.StringUtils;
import com.aspire.ums.cmdb.util.UUIDUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aspire.ums.cmdb.common.PageBean;
import com.aspire.ums.cmdb.maintenance.entity.MaintenSoftware;
import com.aspire.ums.cmdb.maintenance.mapper.MaintenSoftwareMapper;
import com.aspire.ums.cmdb.maintenance.payload.MaintenSoftPageRequest;
import com.aspire.ums.cmdb.maintenance.payload.MaintenSoftPageResp;
import com.aspire.ums.cmdb.maintenance.service.MaintenSoftService;

import lombok.extern.slf4j.Slf4j;

/**
 * 软件维保业务层接口
 * <p>
 * 项目名称:  mirror平台
 * 包:       com.aspire.ums.cmdb.maintenance.service.impl
 * 类名称:    MaintenSoftServiceImpl.java
 * 类描述:    软件维保业务层
 * 创建人:    JinSu
 * 创建时间:  2018/9/14 15:55
 * 版本:      v1.0
 */
@Slf4j
@Service
@Transactional
public class MaintenSoftServiceImpl implements MaintenSoftService {
	
    @Autowired
    private MaintenSoftwareMapper maintenSoftwareMapper;

	@Autowired
	private CmdbMaintenanceSoftwareRecordMapper softwareRecordMapper;
    
     
    /**
     * 插入软件维保
     *
     * @param maintenSoftware 软件维保
     */
    @Override
	public void insertMaintenSoftware(MaintenSoftware maintenSoftware) {
    	if (StringUtils.isEmpty(maintenSoftware.getId())) {
			maintenSoftware.setId(UUIDUtil.getUUID());
		}
    	maintenSoftwareMapper.insertMaintenSoftware(maintenSoftware);
	}

    
//    /**
//     * 修改软件维保
//     *
//     * @param maintenSoftware 软件维保
//     */
//    @Override
//	public int updateMaintenSoftware(MaintenSoftware maintenSoftware) {
//
//    	log.info("maintenSoftware is {} ",maintenSoftware);
//
//    	int count= maintenSoftwareMapper.updateMaintenSoftware(maintenSoftware);
//
//		return count;
//	}
   
//    /**
//     * 批量修改软件维保
//     *
//     * @param maintenSoftware 软件维保
//     */
//    @Override
//	public int batchUpdateMaintenSoftware(MaintenSoftware maintenSoftware) {
//
//
//    	log.info("maintenSoftware is {} ",maintenSoftware);
//
//    	Map<String, Object> hashMap=new HashMap<String, Object>();
//
//    	String[]  array = maintenSoftware.getId().split(",");
//
// 		hashMap.put("list", Arrays.asList(array));
//
// 		hashMap.put("classify", maintenSoftware.getClassify());
// 		hashMap.put("unit", maintenSoftware.getUnit());
// 		hashMap.put("number", maintenSoftware.getNumber());
// 		hashMap.put("userName", maintenSoftware.getUserName());
// 		hashMap.put("telephone", maintenSoftware.getTelephone());
// 		hashMap.put("maintenBeginDate", maintenSoftware.getMaintenBeginDate());
// 		hashMap.put("maintenEndDate", maintenSoftware.getMaintenEndDate());
// 		hashMap.put("admin", maintenSoftware.getAdmin());
// 		hashMap.put("remark", maintenSoftware.getRemark());
//
//		return maintenSoftwareMapper.batchUpdateMaintenSoftware(hashMap);
    	
     
//	}


    
    /**
     * 查询单个软件维保根据id
     *
     * @param id 软件维保
     */
    @Override
	public MaintenSoftware selectMaintenSoftwareById(String id) {
		
    	log.info("id is {} ",id);
    	
		return maintenSoftwareMapper.selectMaintenSoftwareById(id);
	}
    
    // 查询软件维保根据软件名称
    @Override
	public MaintenSoftware selectMaintenSoftwareBySoftNmae(String project ,String softwareName) {

    	Map<String, Object> hashMap=new HashMap<String, Object>();

// 		hashMap.put("company", company);
 		hashMap.put("project", project);
 		hashMap.put("softwareName", softwareName);

 		List<MaintenSoftware> maintenSoftwareList=maintenSoftwareMapper.selectMaintenSoftwareBySoftNmae(hashMap);

 		MaintenSoftware maintenSoftware= null;

 	    if (maintenSoftwareList!=null && maintenSoftwareList.size()>0) {
 	    	   maintenSoftware=maintenSoftwareList.get(0);
 		  }

 	    return maintenSoftware;

	}
      
    
   //删除软件维保
    @Override
	public void deleteMaintenSoftwareById(String ids) {
    	
    	log.info("ids is {} ",ids);
		String[] array=ids.split(",");
		List<String> idList=Arrays.asList(array);
		idList.forEach(id -> {
			CmdbMaintenanceSoftwareRecordQuery query = new CmdbMaintenanceSoftwareRecordQuery();
			query.setSoftwareId(id);
			int count = softwareRecordMapper.listCount(query);
			if (count > 0) {
				throw new RuntimeException("软件维保下还存在使用记录");
			} else {
				maintenSoftwareMapper.deleteMaintenSoftwareIds(idList);
			}
		});

	}

    
    //分页查询软件维保
	@Override
	public PageBean<MaintenSoftPageResp> selectMaintenSoftByPage(MaintenSoftPageRequest maintenSoftPageRequest) {
		log.info("maintenSoftPageRequest is {} ",maintenSoftPageRequest);
//		SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
		PageBean<MaintenSoftPageResp> pageBean=new PageBean<MaintenSoftPageResp>();
		Map<String, Object> queryMap = new HashMap<>();
		queryMap.put("pageNo", maintenSoftPageRequest.getPageNo());
		queryMap.put("pageSize", maintenSoftPageRequest.getPageSize());
		queryMap.put("maintenEndDateBefore", maintenSoftPageRequest.getMaintenEndDateBefore());
		queryMap.put("maintenEndDateAfter", maintenSoftPageRequest.getMaintenEndDateAfter());
		queryMap.put("project", maintenSoftPageRequest.getProject());
		queryMap.put("projectId", maintenSoftPageRequest.getProjectId());
		queryMap.put("softwareName", maintenSoftPageRequest.getSoftwareName());
		queryMap.put("company", maintenSoftPageRequest.getCompany());
        int count=maintenSoftwareMapper.getMaintenSoftwareCount(queryMap);
		List<MaintenSoftware> maintenSoftwareList=maintenSoftwareMapper.getMaintenSoftwareByPage(queryMap);
		
		List<MaintenSoftPageResp> maintenSoftPageList= new ArrayList<MaintenSoftPageResp>();
		
		for ( MaintenSoftware maintenSoftware : maintenSoftwareList ) {
			MaintenSoftPageResp maintenSoftPageResp=new MaintenSoftPageResp();
			BeanUtils.copyProperties(maintenSoftware, maintenSoftPageResp);
			maintenSoftPageList.add(maintenSoftPageResp);
		}
		pageBean.setCount(count);
		pageBean.setResult(maintenSoftPageList);
		return pageBean;
	}


//	@Override
//	public List<MaintenSoftPageResp> getMaintenSoftwareList(MaintenSoftPageRequest maintenSoftPageRequest) {
//
//		log.info("maintenSoftPageRequest is {} ",maintenSoftPageRequest);
//
//		Map<String, Object> hashMap=new HashMap<String, Object>();
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//
//
//		if ( maintenSoftPageRequest.getMaintenEndDateBefore()!=null ) {
//			hashMap.put("maintenEndDateBefore",sdf.format(maintenSoftPageRequest.getMaintenEndDateBefore()) );
//		}
//		if ( maintenSoftPageRequest.getMaintenEndDateAfter()!=null ) {
//			hashMap.put("maintenEndDateAfter",sdf.format(maintenSoftPageRequest.getMaintenEndDateAfter()) );
//		}
//
//		if ( maintenSoftPageRequest.getMaintenBeginDateBefore()!=null ) {
//			hashMap.put("maintenBeginDateBefore",sdf.format(maintenSoftPageRequest.getMaintenBeginDateBefore()) );
//		}
//		if ( maintenSoftPageRequest.getMaintenBeginDateAfter()!=null ) {
//			hashMap.put("maintenBeginDateAfter",sdf.format(maintenSoftPageRequest.getMaintenBeginDateAfter()) );
//		}
//
//		hashMap.put("project", maintenSoftPageRequest.getProject());
//		hashMap.put("softwareName", maintenSoftPageRequest.getSoftwareName());
//		hashMap.put("company", maintenSoftPageRequest.getCompany());
//		hashMap.put("classify", maintenSoftPageRequest.getClassify());
//		hashMap.put("userName", maintenSoftPageRequest.getUserName());
//		hashMap.put("telephone", maintenSoftPageRequest.getTelephone());
//		hashMap.put("admin", maintenSoftPageRequest.getAdmin());
//
//
//        List<MaintenSoftware> maintenSoftwareList=maintenSoftwareMapper.getMaintenSoftwareList(hashMap);
//
//		List<MaintenSoftPageResp> maintenSoftPageList= new ArrayList<MaintenSoftPageResp>();
//
//		for ( MaintenSoftware maintenSoftware : maintenSoftwareList ) {
//
//			MaintenSoftPageResp maintenSoftPageResp=new MaintenSoftPageResp();
//
//			BeanUtils.copyProperties(maintenSoftware, maintenSoftPageResp);
//
//			maintenSoftPageList.add(maintenSoftPageResp);
//		}
//
//
//		return maintenSoftPageList;
//	}


	@Override
	public void insertMaintenSoftwareList(List<MaintenSoftware> maintenSoftwareList) {
		 
		log.info("maintenSoftwareList is {} ",maintenSoftwareList);
		maintenSoftwareList.forEach(software -> {
			software.setId(UUIDUtil.getUUID());
		});
		maintenSoftwareMapper.insertMaintenSoftwareList(maintenSoftwareList);
		
		  
	}


	

	
	
}
