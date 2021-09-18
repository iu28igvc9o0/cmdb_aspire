package com.aspire.mirror.alert.server.controller.dashboard;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import com.aspire.mirror.alert.api.dto.dashboard.*;
import com.aspire.mirror.alert.api.service.dashboard.AlertRepPanelService;
import com.aspire.mirror.alert.server.biz.dashboard.AlertRepPanelBiz;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.aspire.mirror.alert.server.clientservice.DashboardEsServiceClient;
import com.aspire.mirror.alert.server.dao.dashboard.DeviceItemInfoDao;
import com.aspire.mirror.alert.server.dao.dashboard.po.AlertRepPanel;
import com.aspire.mirror.alert.server.dao.dashboard.po.AlertRepPanelItem;
import com.aspire.mirror.alert.server.dao.dashboard.po.AlertRepPanelMoniterItem;
import com.aspire.mirror.alert.server.dao.dashboard.po.DeviceItemInfo;
import com.aspire.mirror.elasticsearch.api.dto.DashboardResponse;
import com.aspire.mirror.elasticsearch.api.dto.DataSetsDto;
import com.google.common.collect.Lists;

import net.sf.json.JSONObject;

/**
 * 告警控制层
 * <p>
 * 项目名称: mirror平台 包: com.aspire.mirror.alert.server.controller 类名称:
 * AlertsHisController.java 类描述: 告警控制层 创建人: JinSu 创建时间: 2018/9/14 17:51 版本: v1.0
 */
@RestController
public class AlertRepPanelController implements AlertRepPanelService {

	private static final Logger LOGGER = LoggerFactory.getLogger(AlertRepPanelController.class);
	@Autowired
	private DashboardEsServiceClient dashboardEsServiceClient;
	@Autowired
	private AlertRepPanelBiz panelBiz;
	
	@Autowired
	private DeviceItemInfoDao deviceItemInfoDao;
	@Override
	public AlertRepPanelDTO findByPrimaryKey(@PathVariable("id") String id) {
		if (StringUtils.isEmpty(id)) {
			LOGGER.warn("findByPrimaryKey param id is null");
			return null;
		}
		AlertRepPanel panel = panelBiz.selectByPrimaryKey(id);
		if (null == panel) {
			return null;
		}
		AlertRepPanelDTO panelDTO = new AlertRepPanelDTO();
		BeanUtils.copyProperties(panel, panelDTO);
		return panelDTO;
	}
	@Override
	public AlertRepPanelDTO findByName(@PathVariable("name") String name) {
		if (StringUtils.isEmpty( name)) {
			LOGGER.warn("findByName param  name is null");
			return null;
		}
		AlertRepPanel panel = panelBiz.getByName(name);
		if (null == panel) {
			return null;
		}
		AlertRepPanelDTO panelDTO = new AlertRepPanelDTO();
		BeanUtils.copyProperties(panel, panelDTO);
		return panelDTO;
	}
	@Override
	public AlertRepPanelDTO create(@RequestBody AlertRepPanelDTO panel) throws Exception {
		if (panel == null) {
			LOGGER.error("create panel is null");
			throw new RuntimeException("create param is null");
		}
		AlertRepPanel panelDTO = new AlertRepPanel();
		BeanUtils.copyProperties(panel, panelDTO);
		changeDTO(panel,panelDTO);
		panelDTO.setCreate_time(new Date());
		AlertRepPanel p = panelBiz.insert(panelDTO);
		AlertRepPanelDTO pDTO = new AlertRepPanelDTO();
		BeanUtils.copyProperties(p, pDTO);
		return pDTO;
	}
	public void changeDTO(AlertRepPanelDTO panelDTO,AlertRepPanel panel){
		List<AlertRepPanelItem> iLst = Lists.newArrayList();
		List<AlertRepPanelItemDTO> items = panelDTO.getItems();
		if (null != items) {
			for(AlertRepPanelItemDTO i:items) {
				List<AlertRepPanelMoniterItemDTO> monitoers = i.getMoniterItems();
				List<AlertRepPanelMoniterItem> mList = Lists.newArrayList();
				for(AlertRepPanelMoniterItemDTO m:monitoers) {
					AlertRepPanelMoniterItem mm = new AlertRepPanelMoniterItem();
					BeanUtils.copyProperties(m, mm);
					mList.add(mm);
				}
				
				AlertRepPanelItem ii = new AlertRepPanelItem();
				BeanUtils.copyProperties(i, ii);
				ii.setMoniterItems(mList);
				iLst.add(ii);
			}
			panel.setItems(iLst);
		}
		
	}
	
	public void changePanel(AlertRepPanelDTO panelDTO,AlertRepPanel panel){
		List<AlertRepPanelItemDTO> iLst = Lists.newArrayList();
		List<AlertRepPanelItem> items = panel.getItems();
		for(AlertRepPanelItem i:items) {
			List<AlertRepPanelMoniterItem> monitoers = i.getMoniterItems();
			List<AlertRepPanelMoniterItemDTO> mList = Lists.newArrayList();
			for(AlertRepPanelMoniterItem m:monitoers) {
				AlertRepPanelMoniterItemDTO mm = new AlertRepPanelMoniterItemDTO();
				BeanUtils.copyProperties(m, mm);
				mList.add(mm);
			}
			
			AlertRepPanelItemDTO ii = new AlertRepPanelItemDTO();
			BeanUtils.copyProperties(i, ii);
			ii.setMoniterItems(mList);
			iLst.add(ii);
		}
		panelDTO.setItems(iLst);
	}
	@Override
	public ResponseEntity<String> update(@RequestBody AlertRepPanelDTO panel) throws Exception {
		if (panel == null) {
			LOGGER.error("update panel is null");
			throw new RuntimeException("update param is null");
		}
		AlertRepPanel panelDTO = new AlertRepPanel();
		BeanUtils.copyProperties(panel, panelDTO);
		changeDTO(panel,panelDTO);
		panelBiz.update(panelDTO);
		return new ResponseEntity<String>("success", HttpStatus.OK);
	}
	@Override
	public ResponseEntity<String> delete(@PathVariable("id") String id) throws Exception {
		if (StringUtils.isEmpty(id)) {
			LOGGER.error("delete id is null");
			throw new RuntimeException("delete id is null");
		}
		panelBiz.delete(id);
		return new ResponseEntity<String>("success", HttpStatus.OK);
	}
	@Override
	public List<AlertRepPanelDTO> getAllPanel() {
		List<AlertRepPanel> all = panelBiz.getAllPanel();
		List<AlertRepPanelDTO> panels = Lists.newArrayList();
		for(AlertRepPanel p:all) {
			AlertRepPanelDTO panel = new AlertRepPanelDTO();
			BeanUtils.copyProperties(p, panel);
			panels.add(panel);
		}
		return panels;
	}
	@Override
	public AlertDashboardResponse getEsData(@RequestBody AlertDataSetsDto dataSetsDto) throws Exception {
		DataSetsDto dsdd = new DataSetsDto();
				BeanUtils.copyProperties(dataSetsDto, dsdd);
				LOGGER.info("输入getDashboardJsonList--dataSetsDto:" + JSONObject.fromObject(dsdd).toString());
		/*
		 * for (AlertDataSet ds : dataSetsDto.getDataSets()) { DataSet d = new
		 * DataSet(); BeanUtils.copyProperties(ds, d); dsList.add(d); //dsdd.set }
		 */
		//dsdd.setDataSets(dsList);
		DashboardResponse dr = dashboardEsServiceClient.getDashboardJsonList(dsdd);
		
		AlertDashboardResponse rs = new AlertDashboardResponse();
				BeanUtils.copyProperties(dr, rs);
		return rs;
	}
	
	@Override
	public List<String> getDeviceClass() {
		return deviceItemInfoDao.getDeviceClass();
	}
	@Override
	public List<String> getDeviceType(@RequestParam("deviceClass") String deviceClass) {
		return deviceItemInfoDao.getDeviceType(deviceClass);
	}
	@Override
	public List<String> getSubclass(@RequestParam("deviceClass") String deviceClass,@RequestParam("deviceType") String deviceType) {
		return deviceItemInfoDao.getSubclass(deviceClass, deviceType);
	}
	@Override
	public List<AlertDeviceItemInfo> getMonitor(@RequestParam("deviceClass") String deviceClass,
			@RequestParam("deviceType") String deviceType,@RequestParam("subclass") String subclass) {
		
		List<DeviceItemInfo> list = deviceItemInfoDao.getMonitor(deviceClass, deviceType, subclass);
		List<AlertDeviceItemInfo> returnList = Lists.newArrayList();
		for(DeviceItemInfo d:list) {
			AlertDeviceItemInfo dd = new AlertDeviceItemInfo();
			BeanUtils.copyProperties(d, dd);
			returnList.add(dd);
		}
		return returnList;
	}
	
	@Override
	public List<AlertDeviceItemInfo> getSubMonitorList(@RequestParam("deviceClass") String deviceClass,
			@RequestParam("deviceType") String deviceType) {
		
		List<DeviceItemInfo> list = deviceItemInfoDao.getSunMonitors(deviceClass, deviceType);
		List<AlertDeviceItemInfo> rs = Lists.newArrayList();
		for(DeviceItemInfo d:list) {
			AlertDeviceItemInfo dd = new AlertDeviceItemInfo();
			BeanUtils.copyProperties(d, dd);
			rs.add(dd);
			/*
			 * if(StringUtils.isEmpty(d.getSubclass())) { continue; } String subclass =
			 * d.getSubclass(); AlertDeviceItemInfo dd = new AlertDeviceItemInfo();
			 * BeanUtils.copyProperties(d, dd);
			 * 
			 * if(map.containsKey(d.getSubclass())) { map.get(subclass).add(dd); }else {
			 * List<AlertDeviceItemInfo> temp = Lists.newArrayList(); temp.add(dd);
			 * map.put(subclass,temp); }
			 */
		}
		return rs;
	}
	
	/**
	 * 导入标准化
	 */
	@Override
	public void importMonitorItem(@RequestParam(value="filePath") String filePath,@RequestParam(value="sheetNum", required = false) int sheetNum) throws Exception {
		try {
            // 创建对Excel工作簿文件的引用
			XSSFWorkbook wookbook = new XSSFWorkbook(new FileInputStream(filePath)); 
            // 在Excel文档中，第一张工作表的缺省索引是0
            // 其语句为：HSSFSheet sheet = workbook.getSheetAt(0);
			XSSFSheet sheet = wookbook.getSheetAt(sheetNum);
            //获取到Excel文件中的所有行数
            int rows = sheet.getPhysicalNumberOfRows();
            List<DeviceItemInfo> list = Lists.newArrayList();
            //遍历行
            for (int i = 1; i < rows; i++) {
                  // 读取左上端单元格
            	XSSFRow row = sheet.getRow(i);
            	//List<String> more = Lists.newArrayList();
            	DeviceItemInfo info = new DeviceItemInfo();
                  // 行不为空
                  if (row != null) {
                        //获取到Excel文件中的所有的列
                        int cells = row.getPhysicalNumberOfCells();
                        
                        String value = "";     
                        //遍历列
                        System.out.println();
                        for (int j = 0; j < cells; j++) {
                        	//String value = "";
                        	if((j==0 || j==1 ||j==2)&& isMergedRegion(sheet,i,j)){
                        		value = getMergedRegionValue(sheet,i,j);
                               //System.out.print(getMergedRegionValue(sheet,i,j)+"\t");
                            }else{
                                row = sheet.getRow(i);
                                value = getCellValue(row.getCell(j));//row.getCell(j);
                                //System.out.print(row.getCell(j)+"\t");
                            }
						/*
						 * if(value.contains("\\") && j==2) { //value = value.replace("\\","/");
						 * String[] values = value.split("\\\\"); value = values[0]; if(values.length>1)
						 * { for(int m=1;m<values.length;m++) { more.add(values[m]); } } } else
						 * if(value.contains("/") && j==2) { //value = value.replace("\\","/"); String[]
						 * values = value.split("/"); value = values[0]; if(values.length>1) { for(int
						 * m=1;m<values.length;m++) { more.add(values[m]); } } }
						 */
                        	value = value.trim();
                        	switch(j) {
                        	case 0:
                        		info.setDevice_class(value);
                        		break;
                        	case 1:
                        		info.setDevice_type(value);
                        		break;
                        	case 2:
                        		info.setSubclass(value);
                        		break;
                        	case 3:
                        		info.setMoniter_item_name(value);
                        		break;
                        	case 4:
                        		info.setMoniter_item_key(value);
                        		break;
                        	case 5:
                        		info.setComment(value);
                        		break;
                        	case 6:
                        		info.setAlert_level(value);
                        		break;
                        	case 7:
                        		info.setIs_create_alert(value);
                        		break;
                        	case 8:
                        		info.setAlert_tips(value);
                        		break;
                        	case 9:
                        		info.setObject_type(value);
                        		break;
                        	case 10:
                        		info.setIs_create_order(value);
                        		break;
                        	case 11:
                        		info.setDefault_value(value);
                        		break;
                        	case 12:
                        		info.setMonitor_rate(value);
                        		break;
                        	case 13:
                        		info.setProtocol(value);
                        		break;
                        	case 14:
                        		info.setIs_show_idcType(value);
                        		break;
                        	case 15:
                        		info.setIs_show_room(value);
                        		break;
                        	case 16:
                        		info.setIs_show_frame(value);
                        		break;
                        	case 17:
                        		info.setNote(value);
                        		break;
                        		default:
                        			break;
                        	}
						/*
						 * //获取到列的值 XSSFCell cell = row.getCell(j); cell.getColumnIndex();
						 * cell.getRowIndex(); CellRangeAddress dd = cell.getArrayFormulaRange(); if
						 * (cell != null) { switch (cell.getCellType()) { case
						 * HSSFCell.CELL_TYPE_FORMULA: break; case HSSFCell.CELL_TYPE_NUMERIC: value +=
						 * cell.getNumericCellValue() + ","; break; case HSSFCell.CELL_TYPE_STRING:
						 * value += cell.getStringCellValue() + ","; break; default: value += "0";
						 * break; } }
						 */      
                  }
                  
            }
                  list.add(info);
				/*
				 * if(more.size()>0) { for(String m:more) { DeviceItemInfo dd = new
				 * DeviceItemInfo(); BeanUtils.copyProperties(info, dd); dd.setSubclass(m);
				 * list.add(dd); } }
				 */
       }
         deviceItemInfoDao.batchInsert(list);
} catch (FileNotFoundException e) {
      e.printStackTrace();
} catch (IOException e) {
      e.printStackTrace();
}
	}
	
	 /**
     * 判断指定的单元格是否是合并单元格
     * @param sheet
     * @param row 行下标
     * @param column 列下标
     * @return
     */
    private  boolean isMergedRegion(XSSFSheet sheet,int row ,int column) {
        int sheetMergeCount = sheet.getNumMergedRegions();
        for (int i = 0; i < sheetMergeCount; i++) {
            CellRangeAddress range = sheet.getMergedRegion(i);
            int firstColumn = range.getFirstColumn();
            int lastColumn = range.getLastColumn();
            int firstRow = range.getFirstRow();
            int lastRow = range.getLastRow();
            if(row >= firstRow && row <= lastRow){
                if(column >= firstColumn && column <= lastColumn){
                    return true;
                }
            }
        }
        return false;
    }
    
	
	/**
	 * 判断单元格是否为合并单元格
	 *
	 * @param listCombineCell 存放合并单元格的list
	 * @param cell            需要判断的单元格
	 * @param sheet           sheet
	 * @return
	 */
	public static Boolean isCombineCell(List<CellRangeAddress> listCombineCell, XSSFCell cell,XSSFSheet sheet) {
		int firstC = 0;
		int lastC = 0;
		int firstR = 0;
		int lastR = 0;
		for (CellRangeAddress ca : listCombineCell) {
			// 获得合并单元格的起始行, 结束行, 起始列, 结束列
			firstC = ca.getFirstColumn();
			lastC = ca.getLastColumn();
			firstR = ca.getFirstRow();
			lastR = ca.getLastRow();
			if (cell.getColumnIndex() <= lastC && cell.getColumnIndex() >= firstC) {
				if (cell.getRowIndex() <= lastR && cell.getRowIndex() >= firstR) {
					return true;
				}
			}
		}
		return false;
	}
	
	
	public  String getMergedRegionValue(XSSFSheet sheet ,int row , int column){
        int sheetMergeCount = sheet.getNumMergedRegions();

        for(int i = 0 ; i < sheetMergeCount ; i++){
            CellRangeAddress ca = sheet.getMergedRegion(i);
            int firstColumn = ca.getFirstColumn();
            int lastColumn = ca.getLastColumn();
            int firstRow = ca.getFirstRow();
            int lastRow = ca.getLastRow();

            if(row >= firstRow && row <= lastRow){
                if(column >= firstColumn && column <= lastColumn){
                	XSSFRow fRow = sheet.getRow(firstRow);
                	XSSFCell fCell = fRow.getCell(firstColumn);
                    return getCellValue(fCell) ;
                }
            }
        }

        return null ;
    }
	
	public  String getCellValue(XSSFCell cell){
        if(cell == null) return "";
        return cell.getStringCellValue();
    }

}
