package com.aspire.ums.cmdb.maintenance.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aspire.ums.cmdb.allocate.entity.AllocateDomain;
import com.aspire.ums.cmdb.allocate.util.PageBean;
import com.aspire.ums.cmdb.maintenance.entity.MaintenSoftware;
import com.aspire.ums.cmdb.maintenance.mapper.MaintenSoftwareMapper;
import com.aspire.ums.cmdb.maintenance.service.MaintenSoftService;
import com.aspire.ums.cmdb.maintenance.util.ExportExcelUtil;
import com.aspire.ums.cmdb.maintenance.util.MaintenSoftPageRequest;
import com.aspire.ums.cmdb.maintenance.util.MaintenSoftPageResp;
import com.aspire.ums.cmdb.maintenance.util.MaintenSoftwareResp;

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
    
     
    /**
     * 插入软件维保
     *
     * @param maintenSoftware 软件维保
     */
    @Override
	public int insertMaintenSoftware(MaintenSoftware maintenSoftware) {
		 
    	log.info("maintenSoftware is {} ",maintenSoftware);
    	
    	int count= maintenSoftwareMapper.insertMaintenSoftware(maintenSoftware); 
    	
		return count;
	}

    
    /**
     * 修改软件维保
     *
     * @param maintenSoftware 软件维保
     */
    @Override
	public int updateMaintenSoftware(MaintenSoftware maintenSoftware) {
    	
    	log.info("maintenSoftware is {} ",maintenSoftware);
		 
    	int count= maintenSoftwareMapper.updateMaintenSoftware(maintenSoftware);
    	
		return count;
	}
   
    /**
     * 批量修改软件维保
     *
     * @param maintenSoftware 软件维保
     */
    @Override
	public int batchUpdateMaintenSoftware(MaintenSoftware maintenSoftware) {
		 
    	
    	log.info("maintenSoftware is {} ",maintenSoftware);
    	
    	Map<String, Object> hashMap=new HashMap<String, Object>();
		
    	String[]  array = maintenSoftware.getId().split(",");
    	
    	
 		hashMap.put("list", Arrays.asList(array));
 			
 		hashMap.put("project", maintenSoftware.getProject());
 		hashMap.put("classify", maintenSoftware.getClassify());
 		hashMap.put("softwareName", maintenSoftware.getSoftwareName());
 		hashMap.put("unit", maintenSoftware.getUnit());
 		hashMap.put("number", maintenSoftware.getNumber());
 		hashMap.put("company", maintenSoftware.getCompany());
 		hashMap.put("userName", maintenSoftware.getUserName());
 		hashMap.put("telephone", maintenSoftware.getTelephone());
 		hashMap.put("maintenBeginDate", maintenSoftware.getMaintenBeginDate());
 		hashMap.put("maintenEndDate", maintenSoftware.getMaintenEndDate());
 		hashMap.put("admin", maintenSoftware.getAdmin());
 		hashMap.put("remark", maintenSoftware.getRemark());
		  
    	
		return maintenSoftwareMapper.batchUpdateMaintenSoftware(hashMap);
    	
     
	}


    
    /**
     * 查询单个软件维保根据id
     *
     * @param maintenSoftware 软件维保
     */
    @Override
	public MaintenSoftware selectMaintenSoftwareById(String id) {
		
    	log.info("id is {} ",id);
    	
		return maintenSoftwareMapper.selectMaintenSoftwareById(id);
	}
    
    // 查询软件维保根据软件名称
    @Override
	public MaintenSoftware selectMaintenSoftwareBySoftNmae( String company ,String project ,String softwareName) {
		 
    	Map<String, Object> hashMap=new HashMap<String, Object>();
		 
 		hashMap.put("company", company);
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
	public int deleteMaintenSoftwareById(String ids) {
    	
    	log.info("ids is {} ",ids);
    	
    	String[] array=ids.split(",");
    	
    	List<String> idList=Arrays.asList(array);
    	
    	
		return maintenSoftwareMapper.deleteMaintenSoftwareIds(idList);
		
	}

    
    //分页查询软件维保
	@Override
	public PageBean<MaintenSoftPageResp> selectMaintenSoftByPage( MaintenSoftPageRequest maintenSoftPageRequest) {
		 
		log.info("maintenSoftPageRequest is {} ",maintenSoftPageRequest);
		
		PageBean<MaintenSoftPageResp> pageBean=new PageBean<MaintenSoftPageResp>();
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	         
        Map<String, Object> hashMap=new HashMap<String, Object>();
		 
        int pageSize=Integer.valueOf(maintenSoftPageRequest.getPageSize());
		int pageNo=Integer.valueOf(maintenSoftPageRequest.getPageNo());
		 
		
		hashMap.put("pageNo", (pageNo - 1) * pageSize);
		hashMap.put("pageSize", pageSize);
		
		
		if ( maintenSoftPageRequest.getMaintenEndDateBefore()!=null ) {
			hashMap.put("maintenEndDateBefore",sdf.format(maintenSoftPageRequest.getMaintenEndDateBefore()) );
		}
		if ( maintenSoftPageRequest.getMaintenEndDateAfter()!=null ) {
			hashMap.put("maintenEndDateAfter",sdf.format(maintenSoftPageRequest.getMaintenEndDateAfter()) );
		}
		
		if ( maintenSoftPageRequest.getMaintenBeginDateBefore()!=null ) {
			hashMap.put("maintenBeginDateBefore",sdf.format(maintenSoftPageRequest.getMaintenBeginDateBefore()) );
		}
		if ( maintenSoftPageRequest.getMaintenBeginDateAfter()!=null ) {
			hashMap.put("maintenBeginDateAfter",sdf.format(maintenSoftPageRequest.getMaintenBeginDateAfter()) );
		}	
		 
		hashMap.put("project", maintenSoftPageRequest.getProject());
		hashMap.put("softwareName", maintenSoftPageRequest.getSoftwareName());
		hashMap.put("company", maintenSoftPageRequest.getCompany());
		hashMap.put("classify", maintenSoftPageRequest.getClassify());
		hashMap.put("userName", maintenSoftPageRequest.getUserName());
		hashMap.put("telephone", maintenSoftPageRequest.getTelephone()); 
		hashMap.put("admin", maintenSoftPageRequest.getAdmin()); 
		 
        int count=maintenSoftwareMapper.getMaintenSoftwareCount(hashMap);
		
		List<MaintenSoftware> maintenSoftwareList=maintenSoftwareMapper.getMaintenSoftwareByPage(hashMap); 
		
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


	@Override
	public List<MaintenSoftPageResp> getMaintenSoftwareExcelData(MaintenSoftPageRequest maintenSoftPageRequest) {
		 
		log.info("maintenSoftPageRequest is {} ",maintenSoftPageRequest);
		
		Map<String, Object> hashMap=new HashMap<String, Object>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
	 
		
		if ( maintenSoftPageRequest.getMaintenEndDateBefore()!=null ) {
			hashMap.put("maintenEndDateBefore",sdf.format(maintenSoftPageRequest.getMaintenEndDateBefore()) );
		}
		if ( maintenSoftPageRequest.getMaintenEndDateAfter()!=null ) {
			hashMap.put("maintenEndDateAfter",sdf.format(maintenSoftPageRequest.getMaintenEndDateAfter()) );
		}
		
		if ( maintenSoftPageRequest.getMaintenBeginDateBefore()!=null ) {
			hashMap.put("maintenBeginDateBefore",sdf.format(maintenSoftPageRequest.getMaintenBeginDateBefore()) );
		}
		if ( maintenSoftPageRequest.getMaintenBeginDateAfter()!=null ) {
			hashMap.put("maintenBeginDateAfter",sdf.format(maintenSoftPageRequest.getMaintenBeginDateAfter()) );
		}	
		 
		hashMap.put("project", maintenSoftPageRequest.getProject());
		hashMap.put("softwareName", maintenSoftPageRequest.getSoftwareName());
		hashMap.put("company", maintenSoftPageRequest.getCompany());
		hashMap.put("classify", maintenSoftPageRequest.getClassify());
		hashMap.put("userName", maintenSoftPageRequest.getUserName());
		hashMap.put("telephone", maintenSoftPageRequest.getTelephone()); 
		hashMap.put("admin", maintenSoftPageRequest.getAdmin());
		
		
        List<MaintenSoftware> maintenSoftwareList=maintenSoftwareMapper.getMaintenSoftwareList(hashMap); 
		
		List<MaintenSoftPageResp> maintenSoftPageList= new ArrayList<MaintenSoftPageResp>();
		
		for ( MaintenSoftware maintenSoftware : maintenSoftwareList ) {
			
			MaintenSoftPageResp maintenSoftPageResp=new MaintenSoftPageResp();
			
			BeanUtils.copyProperties(maintenSoftware, maintenSoftPageResp);
			
			maintenSoftPageList.add(maintenSoftPageResp);
		}
		
		
		return maintenSoftPageList;
	}


	@Override
	public int insertMaintenSoftwareList(List<MaintenSoftware> maintenSoftwareList) {
		 
		log.info("maintenSoftwareList is {} ",maintenSoftwareList);
		
		
		return  maintenSoftwareMapper.saveMaintenSoftwareList(maintenSoftwareList);
		
		  
	}


	

	
	
}
