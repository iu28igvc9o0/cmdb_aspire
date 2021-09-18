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

import com.aspire.ums.cmdb.allocate.util.PageBean;
import com.aspire.ums.cmdb.maintenance.entity.MaintenHardware;
import com.aspire.ums.cmdb.maintenance.entity.MaintenSoftware;
import com.aspire.ums.cmdb.maintenance.mapper.MaintenHardwareMapper;
import com.aspire.ums.cmdb.maintenance.service.MaintenHardService;
import com.aspire.ums.cmdb.maintenance.util.MaintenHardPageRequest;
import com.aspire.ums.cmdb.maintenance.util.MaintenHardPageResp;

import lombok.extern.slf4j.Slf4j;

/**
 * 硬件维保业务层接口
 * <p>
 * 项目名称:  mirror平台
 * 包:       com.aspire.ums.cmdb.maintenance.service.impl
 * 类名称:    MaintenHardServiceImpl.java
 * 类描述:    硬件维保业务层
 * 创建人:    JinSu
 * 创建时间:  2018/9/14 15:55
 * 版本:      v1.0
 */
@Slf4j
@Service
@Transactional
public class MaintenHardServiceImpl implements MaintenHardService {
	
    @Autowired
    private MaintenHardwareMapper maintenHardwareMapper;
    
     
    /**
     * 插入硬件维保
     *
     * @param maintenHardware 硬件维保
     */
    @Override
	public int insertMaintenHardware(MaintenHardware maintenHardware) {
		 
    	log.info("maintenHardware is {} ",maintenHardware);
    	
    	int count= maintenHardwareMapper.insertMaintenHardware(maintenHardware); 
    	
		return count;
	}

    
    /**
     * 修改硬件维保
     *
     * @param maintenHardware 硬件维保
     */
    @Override
	public int updateMaintenHardware(MaintenHardware maintenHardware) {
    	
    	log.info("maintenHardware is {} ",maintenHardware);
		 
    	int count= maintenHardwareMapper.updateMaintenHardware(maintenHardware);
    	
		return count;
	}
     
    
    /**
     * 批量修改硬件维保
     *
     * @param maintenSoftware 软件维保
     */
	@Override
	public int batchUpdateMaintenHardware(MaintenHardware maintenHardware) {
		 
        log.info("maintenHardware is {} ",maintenHardware);
    	
    	Map<String, Object> hashMap=new HashMap<String, Object>();
		
    	String[]  array = maintenHardware.getId().split(",");
    	  	
 		hashMap.put("list", Arrays.asList(array));
 	       
 		
 		hashMap.put("province", maintenHardware.getProvince());
 		hashMap.put("city", maintenHardware.getCity());
 		hashMap.put("resourcePool", maintenHardware.getResourcePool());
 		hashMap.put("systemName", maintenHardware.getSystemName());
 		hashMap.put("deviceClassify", maintenHardware.getDeviceClassify());
 		hashMap.put("deviceType", maintenHardware.getDeviceType());
 		hashMap.put("deviceModel", maintenHardware.getDeviceModel());
 		hashMap.put("deviceName", maintenHardware.getDeviceName());
 		hashMap.put("deviceSerialNumber", maintenHardware.getDeviceSerialNumber());
 		hashMap.put("assetsNumber", maintenHardware.getAssetsNumber());
 		hashMap.put("warrantyDate", maintenHardware.getWarrantyDate());
 		hashMap.put("buyMainten", maintenHardware.getBuyMainten());
 		hashMap.put("originBuy", maintenHardware.getOriginBuy());
 		hashMap.put("originBuyExplain", maintenHardware.getOriginBuyExplain());
 		hashMap.put("adviceMaintenFactory", maintenHardware.getAdviceMaintenFactory());
 		hashMap.put("maintenFactory", maintenHardware.getMaintenFactory());
 		hashMap.put("maintenSupplyContact", maintenHardware.getMaintenSupplyContact());
 		hashMap.put("maintenFactoryContact", maintenHardware.getMaintenFactoryContact());
 		hashMap.put("maintenBeginDate", maintenHardware.getMaintenBeginDate() );
 		hashMap.put("maintenEndDate", maintenHardware.getMaintenEndDate() );
 		hashMap.put("realMaintenType", maintenHardware.getRealMaintenType() );
 		hashMap.put("admin", maintenHardware.getAdmin() );
 		   
 		
		return maintenHardwareMapper.batchUpdateMaintenHardware(hashMap);
		
	}

    
    
    /**
     * 查询单个硬件维保根据id
     *
     * @param maintenHardware 硬件维保
     */
    @Override
	public MaintenHardware selectMaintenHardwareById(String id) {
		
    	log.info("id is {} ",id);
    	
		return maintenHardwareMapper.selectMaintenHardwareById(id);
	}
     
    
    // 查询硬件维保根据软件名称
	@Override
	public MaintenHardware selectMaintenHardwareBySoftNmae(String deviceSerialNumber) {
		 
		Map<String, Object> hashMap=new HashMap<String, Object>();
		 
 		hashMap.put("deviceSerialNumber", deviceSerialNumber);
 		 
    	
 		List<MaintenHardware> maintenHardwareList=maintenHardwareMapper.selectMaintenHardwareBySoftNmae(hashMap);
 			
 		MaintenHardware maintenHardware= null;
 				   
 	    if (maintenHardwareList!=null && maintenHardwareList.size()>0) {   	
 	    	maintenHardware=maintenHardwareList.get(0);		 
 		  }
 		   		     
 	    return maintenHardware; 
		
 	    
	}

 
 
   //删除硬件维保
    @Override
	public int deleteMaintenHardwareById(String ids) {
    	
    	log.info("ids is {} ",ids);
    	
    	String[] array=ids.split(",");
    	
    	List<String> idList=Arrays.asList(array);
    	
    	
		return maintenHardwareMapper.deleteMaintenHardwareIds(idList);
		
	}

    
    //分页查询硬件维保
	@Override
	public PageBean<MaintenHardPageResp> selectMaintenHardByPage( MaintenHardPageRequest maintenHardPageRequest) {
		 
		log.info("maintenHardPageRequest is {} ",maintenHardPageRequest);
		
		PageBean<MaintenHardPageResp> pageBean=new PageBean<MaintenHardPageResp>();
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	         
        Map<String, Object> hashMap=new HashMap<String, Object>();
		 
        int pageSize=Integer.valueOf(maintenHardPageRequest.getPageSize());
		int pageNo=Integer.valueOf(maintenHardPageRequest.getPageNo());
		 
		hashMap.put("pageNo", (pageNo - 1) * pageSize);
		hashMap.put("pageSize", pageSize);
				
		
		if ( maintenHardPageRequest.getWarrantyDateBefore()!=null ) {
			hashMap.put("warrantyDateBefore",sdf.format(maintenHardPageRequest.getWarrantyDateBefore()) );
		}	 
		if ( maintenHardPageRequest.getWarrantyDateAfter()!=null ) {
			hashMap.put("warrantyDateAfter",sdf.format(maintenHardPageRequest.getWarrantyDateAfter()) );
		}
		
		if ( maintenHardPageRequest.getMaintenBeginDateBefore()!=null ) {
			hashMap.put("maintenBeginDateBefore",sdf.format(maintenHardPageRequest.getMaintenBeginDateBefore()) );
		}
		if ( maintenHardPageRequest.getMaintenBeginDateAfter()!=null ) {
			hashMap.put("maintenBeginDateAfter",sdf.format(maintenHardPageRequest.getMaintenBeginDateAfter()) );
		}	
		if ( maintenHardPageRequest.getMaintenEndDateBefore()!=null ) {
			hashMap.put("maintenEndDateBefore",sdf.format(maintenHardPageRequest.getMaintenEndDateBefore()) );
		}
		if ( maintenHardPageRequest.getMaintenEndDateAfter()!=null ) {
			hashMap.put("maintenEndDateAfter",sdf.format(maintenHardPageRequest.getMaintenEndDateAfter()) );
		}
		 
		hashMap.put("systemName", maintenHardPageRequest.getSystemName());
		hashMap.put("deviceName", maintenHardPageRequest.getDeviceName());
		hashMap.put("deviceSerialNumber", maintenHardPageRequest.getDeviceSerialNumber());
		hashMap.put("assetsNumber", maintenHardPageRequest.getAssetsNumber());
		hashMap.put("resourcePool", maintenHardPageRequest.getResourcePool());
		hashMap.put("deviceClassify", maintenHardPageRequest.getDeviceClassify());
		hashMap.put("deviceType", maintenHardPageRequest.getDeviceType()); 
		hashMap.put("deviceModel", maintenHardPageRequest.getDeviceModel()); 
		hashMap.put("buyMainten", maintenHardPageRequest.getBuyMainten()); 
		hashMap.put("originBuy", maintenHardPageRequest.getOriginBuy());
		hashMap.put("maintenFactory", maintenHardPageRequest.getMaintenFactory()); 
		hashMap.put("realMaintenType", maintenHardPageRequest.getRealMaintenType());
		hashMap.put("admin", maintenHardPageRequest.getAdmin());
			 
		
        int count=maintenHardwareMapper.getMaintenHardwareCount(hashMap);
		
		List<MaintenHardware> maintenHardwareList=maintenHardwareMapper.getMaintenHardwareByPage(hashMap); 
		
		List<MaintenHardPageResp> maintenHardPageList= new ArrayList<MaintenHardPageResp>();
		
		for ( MaintenHardware maintenHardware : maintenHardwareList ) {
			
			MaintenHardPageResp maintenHardPageResp=new MaintenHardPageResp();
			
			BeanUtils.copyProperties(maintenHardware, maintenHardPageResp);
			
			maintenHardPageList.add(maintenHardPageResp);
		}
		
		pageBean.setCount(count);
		pageBean.setResult(maintenHardPageList);
		
		
		return pageBean;
	}


	@Override
	public List<MaintenHardPageResp> getMaintenHardwareExcelData(MaintenHardPageRequest maintenHardPageRequest) {
		 
		log.info("maintenHardPageRequest is {} ",maintenHardPageRequest);
		
		Map<String, Object> hashMap=new HashMap<String, Object>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
	 
		
		if ( maintenHardPageRequest.getWarrantyDateBefore()!=null ) {
			hashMap.put("warrantyDateBefore",sdf.format(maintenHardPageRequest.getWarrantyDateBefore()) );
		}	 
		if ( maintenHardPageRequest.getWarrantyDateAfter()!=null ) {
			hashMap.put("warrantyDateAfter",sdf.format(maintenHardPageRequest.getWarrantyDateAfter()) );
		}
		
		if ( maintenHardPageRequest.getMaintenBeginDateBefore()!=null ) {
			hashMap.put("maintenBeginDateBefore",sdf.format(maintenHardPageRequest.getMaintenBeginDateBefore()) );
		}
		if ( maintenHardPageRequest.getMaintenBeginDateAfter()!=null ) {
			hashMap.put("maintenBeginDateAfter",sdf.format(maintenHardPageRequest.getMaintenBeginDateAfter()) );
		}	
		if ( maintenHardPageRequest.getMaintenEndDateBefore()!=null ) {
			hashMap.put("maintenEndDateBefore",sdf.format(maintenHardPageRequest.getMaintenEndDateBefore()) );
		}
		if ( maintenHardPageRequest.getMaintenEndDateAfter()!=null ) {
			hashMap.put("maintenEndDateAfter",sdf.format(maintenHardPageRequest.getMaintenEndDateAfter()) );
		}
		 
		hashMap.put("systemName", maintenHardPageRequest.getSystemName());
		hashMap.put("deviceName", maintenHardPageRequest.getDeviceName());
		hashMap.put("deviceSerialNumber", maintenHardPageRequest.getDeviceSerialNumber());
		hashMap.put("assetsNumber", maintenHardPageRequest.getAssetsNumber());
		hashMap.put("resourcePool", maintenHardPageRequest.getResourcePool());
		hashMap.put("deviceClassify", maintenHardPageRequest.getDeviceClassify());
		hashMap.put("deviceType", maintenHardPageRequest.getDeviceType()); 
		hashMap.put("deviceModel", maintenHardPageRequest.getDeviceModel()); 
		hashMap.put("buyMainten", maintenHardPageRequest.getBuyMainten()); 
		hashMap.put("originBuy", maintenHardPageRequest.getOriginBuy());
		hashMap.put("maintenFactory", maintenHardPageRequest.getMaintenFactory()); 
		hashMap.put("realMaintenType", maintenHardPageRequest.getRealMaintenType());
		hashMap.put("admin", maintenHardPageRequest.getAdmin());
		
		
        List<MaintenHardware> maintenHardwareList=maintenHardwareMapper.getMaintenHardwareList(hashMap); 
		
		List<MaintenHardPageResp> maintenHardPageList= new ArrayList<MaintenHardPageResp>();
		
		for ( MaintenHardware maintenHardware : maintenHardwareList ) {
			
			MaintenHardPageResp maintenHardPageResp=new MaintenHardPageResp();
			
			BeanUtils.copyProperties(maintenHardware, maintenHardPageResp);
			
			maintenHardPageList.add(maintenHardPageResp);
		}
		
		
		return maintenHardPageList;
	}


	@Override
	public int insertMaintenHardwareList(List<MaintenHardware> maintenHardwareList) {
		 
		log.info("maintenHardwareList is {} ",maintenHardwareList);
		
		
		return  maintenHardwareMapper.saveMaintenHardwareList(maintenHardwareList);
		
		  
	}


	
}
