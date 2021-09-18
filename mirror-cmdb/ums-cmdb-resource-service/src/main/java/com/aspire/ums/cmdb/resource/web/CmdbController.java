package com.aspire.ums.cmdb.resource.web;

import com.aspire.ums.cmdb.resource.entity.*;
import com.aspire.ums.cmdb.resource.entity.AdvancedCmdbSearchRequest;
import com.aspire.ums.cmdb.resource.entity.Tree;
import com.aspire.ums.cmdb.resource.entity.TreeAttribute;
import com.aspire.ums.cmdb.resource.mapper.AssetsMapper;
import com.aspire.ums.cmdb.resource.mapper.CmdbBusinessMapper;
import com.aspire.ums.cmdb.resource.mapper.CmdbMaintenanceInfoMapper;
import com.aspire.ums.cmdb.resource.mapper.ConfigDictMapper;
import com.aspire.ums.cmdb.sync.entity.Dict;
import com.aspire.ums.cmdb.util.ExcelReaderUtils;
import com.aspire.ums.cmdb.util.ModuleUtils;
import com.aspire.ums.cmdb.util.StringUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.FileUtils;
import org.apache.ibatis.javassist.tools.rmi.RemoteException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping(value = "/resource/cmdb")
@Api("CMDB 数据查询")
public class CmdbController {

	private static final Logger logger = Logger.getLogger(CmdbController.class);

	@Autowired
	private ConfigDictMapper configDictMapper;

	@Autowired
	private CmdbMaintenanceInfoMapper cmdbMaintenanceInfoMapper;

	@Autowired
	private CmdbBusinessMapper cmdbBusinessMapper;

	@Autowired
	private AssetsMapper assetsMapper;

	/**
	 * 根据父ID查询Bussines信息
	 *
	 * @return
	 */
	@RequestMapping("/getBrand")
	@ResponseBody
	@ApiOperation(value = "获取厂商品牌", notes = "获取厂商品牌")
	public List<ComboBox> getBrand() {
		List<ComboBox> dataList = new ArrayList<ComboBox>();
		List<Map<String, Object>> brandList = new ArrayList<Map<String, Object>>();
		try {

			brandList = cmdbBusinessMapper.getBrand();

			if (brandList.size() > 0) {
				for (Map<String, Object> map : brandList) {
					ComboBox comboBox = new ComboBox();
					comboBox.setId(map.get("DEVICE_VENDER").toString().trim());
					comboBox.setText(map.get("DEVICE_VENDER").toString().trim());
					dataList.add(comboBox);
				}
			}
		} catch (Exception e) {
			logger.error("获取厂商品牌失败", e);
		}
		return dataList;
	}

	@RequestMapping("GetCmdbHostAssetsExcelData")
	@ResponseBody
	@ApiOperation(value = "解析上传的EXCEL数据", notes = "解析上传的EXCEL数据")
	public Map<String, Object> getCmdbHostAssetsExcelData(@RequestParam Map<String, String> param,
			HttpServletRequest request, HttpServletResponse response) {
		DataGrid cmdbHostAssetsDataGrid = null;
		List<CmdbHostAssetsExcelData> cmdbHostAssetsExcelList = null;

		// LogWatch logWatch = new LogWatch();
		// logWatch.start();
		// String flag = LogWatch.FLAG_SUCCESS;

		if (cmdbHostAssetsDataGrid == null) {
			cmdbHostAssetsDataGrid = new DataGrid();
		}
		if (cmdbHostAssetsExcelList == null) {
			cmdbHostAssetsExcelList = new ArrayList<CmdbHostAssetsExcelData>();
		}
		String message = null;
		Boolean result = Boolean.FALSE;
		String extName = "";
		String newFileName = "";
		response.setCharacterEncoding("utf-8");
		// 服务器目录
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		MultipartFile file = multipartRequest.getFile("myFile");
		String targetDirectory = request.getRealPath("/upload");
		String uploadFileFileName = file.getOriginalFilename();
		// 获取扩展名
		if (uploadFileFileName.lastIndexOf(".") >= 0) {
			extName = uploadFileFileName.substring(uploadFileFileName.lastIndexOf("."));
		}
		// 设置上传文件的新文件名
		String nowTime = new SimpleDateFormat("yyyymmddHHmmss").format(new Date());// 当前时间
		newFileName = nowTime + extName;
		// 生成上传的文件对象
		File targetFile = new File(targetDirectory, newFileName);
		// 文件已经存在删除原有文件
		if (targetFile.exists()) {
			targetFile.delete();
		}
		try {
			ExcelReaderUtils excelReader = new ExcelReaderUtils();
			FileUtils.writeByteArrayToFile(targetFile, file.getBytes());// 服务器中生成文件
			// 上传数据到数据库
			cmdbHostAssetsExcelList = excelReader.doUploadCmdbHostAssetsExcelData(targetFile);

			// 数据再处理 把中文字段转相应的ID字段存到对象中
			// business_level device_maintenance deviceType deviceOsType
			// deviceModel deviceClass 新增属性给cmdbHostAssetsExcelData对象

			// 设备系统类型
			Map<String, Object> deviceOsType = new HashMap<String, Object>();
			deviceOsType.put("type", "deviceOsType");
			// 设备分类
			Map<String, Object> deviceClass = new HashMap<String, Object>();
			deviceClass.put("type", "deviceClass");
			// 设备型号
			Map<String, Object> deviceModel = new HashMap<String, Object>();
			deviceModel.put("type", "deviceModel");
			// 设备类型
			Map<String, Object> deviceType = new HashMap<String, Object>();
			deviceType.put("type", "deviceType");
			// 所属位置
			Map<String, Object> idcType = new HashMap<String, Object>();
			idcType.put("type", "idcType");
			// 机房位置
			Map<String, Object> idcLocationType = new HashMap<String, Object>();
			idcLocationType.put("type", "idcLocationType");
			List<Map> idcTypeList = configDictMapper.getDictConfig(idcType);
			List<Map> idcLocationTypeList = configDictMapper.getDictConfig(idcLocationType);
			List<Map> deviceClassList = configDictMapper.getDictConfig(deviceClass);
			List<Map> deviceModelList = configDictMapper.getDictConfig(deviceModel);
			List<Map> deviceTypeList = configDictMapper.getDictConfig(deviceType);
			List<Map> deviceOsTypeList = configDictMapper.getDictConfig(deviceOsType);
			List<Map> ipList = assetsMapper.getAssets();
			List<CmdbMaintenanceInfo> maintenanceInfoList = cmdbMaintenanceInfoMapper.getMaintenanceInfo();
			List<CmdbBusiness> businesses = cmdbBusinessMapper.getBusinessByPId(null);
			List<CmdbBusiness> allBusinessesList = cmdbBusinessMapper.getAllBusinesses();

			String idc_Value = null;
			String idc_label = null;
			String device_maintenance = null;
			String vender = null;
			String business_level1 = null;
			String business_level2 = null;
			String businessId1 = null;
			String businessId2 = null;
			String device_ip = null;
			String idc = null;

			for (CmdbHostAssetsExcelData cmdbHostAssetsExcelData : cmdbHostAssetsExcelList) {
				Boolean ipFlag = false;
				StringBuffer ids = new StringBuffer();

				// 经过md5加密生成 ID
				device_ip = cmdbHostAssetsExcelData.getDevice_ip();
				if (device_ip != null && !device_ip.trim().equals("")) { // 业务IP
																			// 是否存在
																			// 存在则走下去，否则直接跳过

					idc = cmdbHostAssetsExcelData.getIdc();
					if (device_ip != null && idc != null) {
						ids.append(device_ip);
						ids.append(idc);
					}
					if (StringUtils.isEmpty(cmdbHostAssetsExcelData.getHost_backup())
							|| cmdbHostAssetsExcelData.getHost_backup().equals("")) {
						cmdbHostAssetsExcelData.setHost_backup("主");
						ids.append("主");
					} else {
						ids.append(cmdbHostAssetsExcelData.getHost_backup());
					}
					String MDid = MD5(ids.toString());
					for (Map<String, Object> deviceIp : ipList) {
						if (deviceIp.get("ID").equals(MDid)) {
							ipFlag = true;
							break;
						}
					}
					if (ipFlag) { // 业务IP 是否重复
						cmdbHostAssetsExcelData.setDevice_ip("已存在");
					} else {
						cmdbHostAssetsExcelData.setID(MD5(ids.toString()));
						ids = null;

						// 获得一级业务名，根据业务名获取其ID
						business_level1 = cmdbHostAssetsExcelData.getBusiness_level1();
						if (business_level1 == null || business_level1.equals("")) {// 一级业务没填
							cmdbHostAssetsExcelData.setBusiness_level2(null);
							cmdbHostAssetsExcelData.setBusinessId(null);

						} else { // 一级业务有填
							Boolean bussinessFlag1 = false;
							for (CmdbBusiness cmdbBusiness : businesses) {
								if (cmdbBusiness.getBusinessName().equals(business_level1)) {
									businessId1 = cmdbBusiness.getId();
									bussinessFlag1 = true;
									break;
								}
							}
							if (!bussinessFlag1) {
								cmdbHostAssetsExcelData.setBusiness_level1("数据填写有误");
								cmdbHostAssetsExcelData.setBusiness_level2(null);
								cmdbHostAssetsExcelData.setBusinessId(null);
							} else {
								if (businessId1 != null) { // 在一级业务正常基础上 判断二级业务
									business_level2 = cmdbHostAssetsExcelData.getBusiness_level2();
									if (business_level2 == null || business_level2.equals("")) { // 二级业务
																									// 没填
										cmdbHostAssetsExcelData.setBusinessId(businessId1);
									} else {
										Boolean bussinessFlag2 = false;
										for (CmdbBusiness cmdbBusiness : allBusinessesList) {
											if (cmdbBusiness.getParentId() != null) {
												if (cmdbBusiness.getBusinessName().equals(business_level2)
														&& cmdbBusiness.getParentId().equals(businessId1)) {
													businessId2 = cmdbBusiness.getId();
													bussinessFlag2 = true;
													break;
												}
											}

										}
										if (!bussinessFlag2) {
											cmdbHostAssetsExcelData.setBusiness_level2("数据填写有误");
											cmdbHostAssetsExcelData.setBusinessId(null);
										} else {
											cmdbHostAssetsExcelData.setBusinessId(businessId2);
										}
									}

								}

							}
						}

						device_maintenance = cmdbHostAssetsExcelData.getDevice_maintenance();
						vender = cmdbHostAssetsExcelData.getDevice_maintenance_vender();
						if (device_maintenance != null && !device_maintenance.trim().equals("") && vender != null
								&& !vender.equals("")) {
							String MaintenanceId = null;
							for (CmdbMaintenanceInfo cmdbMaintenanceInfo : maintenanceInfoList) {
								if (cmdbMaintenanceInfo.getMode().equals(device_maintenance)
										&& cmdbMaintenanceInfo.getVender().equals(vender)) {
									MaintenanceId = cmdbMaintenanceInfo.getId();
									break;
								}
							}
							if (MaintenanceId != null) {
								cmdbHostAssetsExcelData.setDevice_maintenance_id(MaintenanceId);
							} else {
								cmdbHostAssetsExcelData.setDevice_maintenance_id(null);
							}
						} else {
							cmdbHostAssetsExcelData.setDevice_maintenance_id(null);
						}

						String device_class = cmdbHostAssetsExcelData.getDevice_class();
						// 如果填写的内容为空！连带关系的字段 直接跳过
						if (device_class != null) {

							String device_class_id = null;
							for (Map map : deviceClassList) {
								if (map.get("VALUE").equals(device_class)) {
									device_class_id = (String) map.get("ID");
									break;
								}
							}
							if (device_class_id != null) {
								cmdbHostAssetsExcelData.setDevice_class_id(device_class_id);
								String device_model = cmdbHostAssetsExcelData.getDevice_model();

								if (device_model != null) {
									String device_model_id = null;
									for (Map map : deviceModelList) {
										if (map.get("PARENT_ID").equals(device_class_id)
												&& map.get("VALUE").equals(device_model)) {
											device_model_id = (String) map.get("ID");
											break;
										}
									}
									if (device_model_id != null) {
										cmdbHostAssetsExcelData.setDevice_model_id(device_model_id);
									} else {
										cmdbHostAssetsExcelData.setDevice_model_id(null);
									}
								}

								String device_type = cmdbHostAssetsExcelData.getDevice_type();
								if (device_type != null) {
									String device_type_id = null;
									for (Map map : deviceTypeList) {
										if (map.get("PARENT_ID").equals(device_class_id)
												&& map.get("VALUE").equals(device_type)) {
											device_type_id = (String) map.get("ID");
											break;
										}
									}
									if (device_type_id != null) {
										cmdbHostAssetsExcelData.setDevice_type_id(device_type_id);
									} else {
										cmdbHostAssetsExcelData.setDevice_type_id(null);
									}
								}

								String device_os_type = cmdbHostAssetsExcelData.getDevice_os_type();
								if (device_os_type != null) {
									String device_os_type_id = null;
									for (Map map : deviceOsTypeList) {
										if (map.get("VALUE").equals(device_os_type)) {
											device_os_type_id = (String) map.get("ID");
											break;
										}
									}
									if (device_os_type_id != null) {
										cmdbHostAssetsExcelData.setDevice_os_type_id(device_os_type_id);
									} else {
										cmdbHostAssetsExcelData.setDevice_os_type_id(null);
									}
								}

							} else {
								cmdbHostAssetsExcelData.setDevice_class_id(null);
							}

						}

						idc_Value = cmdbHostAssetsExcelData.getIdc();
						idc_label = cmdbHostAssetsExcelData.getIdc_label();
						// 任一者为空则不走下面的校验 字段正确性
						if ((idc_Value == null || idc_label == null) == false) {
							Boolean flagIdc = false;
							for (Map map : idcTypeList) {
								String VALUE = (String) map.get("VALUE");
								String LABEL = (String) map.get("LABEL");
								if (idc_Value.equals(VALUE) && idc_label.equals(LABEL)) {
									flagIdc = true;
									break;
								}
							}
							if (!flagIdc) {
								cmdbHostAssetsExcelData.setIdc_check("Flase");
							}
						}

						/*
						 * idc_location =
						 * cmdbHostAssetsExcelData.getIdc_location(); Boolean
						 * flagLocation=false; for (Map map :
						 * idcLocationTypeList) { String LOCATION = (String)
						 * map.get("VALUE"); if (idc_location.equals(LOCATION))
						 * { flagLocation=true; break; } } if (!flagLocation) {
						 * cmdbHostAssetsExcelData.setIdc_location("数据填写有误"); }
						 */

					}
				}

			}

			// 删除上传数据
			targetFile.delete();
			cmdbHostAssetsDataGrid.setRows(cmdbHostAssetsExcelList);

			if (cmdbHostAssetsExcelList == null) {
				result = Boolean.FALSE;
			} else {
				result = Boolean.TRUE;
			}
		} catch (Exception e) {
			logger.error("保存操作日志失败", e);
			if (e.getMessage().contains("列数")) {
				message = "抱歉，已超出文件的列数限制，请重新填写！";
			} else if (e.getMessage().contains("行数")) {
				message = "抱歉，已超出文件的行数限制，请分批导入！";
			}
			result = Boolean.FALSE;
			// flag = LogWatch.FLAG_FAILURE;
		}
		String modules = ModuleUtils.getModule().get("cmdb_hostAssetsMaintenance_import");
		StringBuilder content = new StringBuilder();
		content.append("执行操作：" + modules + "<br>");
		// logWatch.end(modules, content.toString(),
		// LogWatch.LOG_TYPE_OPERATION, flag);
		Map<String, Object> hashMap = new HashMap<>();
		hashMap.put("message", message);
		hashMap.put("result", result);
		hashMap.put("DataGrid", cmdbHostAssetsDataGrid);
		return hashMap;
	}

	/**
	 *
	 * @param s
	 *            被加密的字符串
	 * @return
	 */
	public final static String MD5(String s) {
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
		try {
			byte[] btInput = s.getBytes();
			// 获得MD5摘要算法的 MessageDigest 对象
			MessageDigest mdInst = MessageDigest.getInstance("MD5");
			// 使用指定的字节更新摘要
			mdInst.update(btInput);
			// 获得密文
			byte[] md = mdInst.digest();
			// 把密文转换成十六进制的字符串形式
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(str);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@RequestMapping(value = "/findAlarmIsHostedType", method = RequestMethod.GET)
	@ApiOperation(value = "", notes = "")
	public List<Object> findAlarmIsHostedType(@RequestParam Map<String, Object> params) {
		String deviceIp = "";
		List<Object> dicts = null;
		if (null != params.get("deviceIp")) {
			deviceIp = params.get("deviceIp").toString();
			logger.info("!!!!!!!!!findAlarmIsHostedType!!!!!!!!!参数" + deviceIp);
			dicts = cmdbBusinessMapper.findAlarmIsHostedType(deviceIp);
		}
		return dicts;
	}

	@RequestMapping(value = "/findBizByBiz", method = RequestMethod.GET)
	@ApiOperation(value = "", notes = "")
	public List<Object> findBizByBiz(@RequestParam Map<String, Object> params) {
		String biz1 = "";
		List<Object> dicts = null;
		if (null != params.get("biz1")) {
			biz1 = params.get("biz1").toString();
			logger.info("!!!!!!!!findBizByBiz!!!!!!!!!!!参数" + biz1);
			dicts = cmdbBusinessMapper.findBiz2ByBiz1(biz1);
		}
		return dicts;
	}

	@RequestMapping(value = "/findContactByBiz", method = RequestMethod.GET)
	@ApiOperation(value = "", notes = "")
	public List<Map<String, String>> findContactByBiz(@RequestParam Map<String, Object> params) {
		String Business_code1 = "";
		String Business_code2 = "";
		List<Map<String, String>> dicts = null;
		if (null != params.get("Business_code1") && null != params.get("Business_code2")) {
			Business_code1 = params.get("Business_code1").toString();
			Business_code2 = params.get("Business_code2").toString();
			logger.info("!!!!!!!!!findContactByBiz!!!!!!!!!!参数" + Business_code1 + Business_code2);
			dicts = cmdbBusinessMapper.findContactByBiz(Business_code1, Business_code2);
		}
		return dicts;
	}

	@RequestMapping("/cmdbBusinessTreeOneLevel1")
	@ResponseBody
	@ApiOperation(value = "", notes = "")
	public Map<String, Object> cmdbBusinessTreeOneLevel1(Tree tree) {
		Map<String, Object> map = Maps.newHashMap();
		List<Tree> result = Lists.newArrayList();
		Tree root = new Tree();
		root.setId("1");
		root.setName("根业务");
		root.setNocheck(false);
		root.setIconSkin("ico_tree2");
		root.setIsParent(true);
		TreeAttribute rootta = new TreeAttribute();
		rootta.setId("1");
		rootta.setType("root");
		root.setAttributes(rootta.toMap());

		List<CmdbBusiness> businesses = cmdbBusinessMapper.getBusinessByPId(null);
		List<Tree> list = Lists.newArrayList();
		if (businesses != null) {
			for (CmdbBusiness b : businesses) {
				/*
				 * try { if
				 * (!UserUtils.getBusinessCodeList().contains(b.getBusinessCode(
				 * ))) { continue; } } catch (RemoteException e) {
				 * logger.error("加载业务权限失败", e); continue; }
				 */
				Tree tn = new Tree();
				tn.setIsParent(false);
				tn.setId(b.getId());
				tn.setName(b.getBusinessName());
				tn.setNocheck(false);
				tn.setIconSkin("icon-service");
				TreeAttribute ta = new TreeAttribute();
				ta.setId(b.getId());
				ta.setType("business");
				tn.setAttributes(ta.toMap());
				list.add(tn);
			}
		}
		root.setChildren(list);
		result.add(root);

		map.put("data", result);
		return map;
	}

	@RequestMapping(value = "/findBusinessCodeAndName/{id}", method = RequestMethod.GET)
	@ApiOperation(value = "", notes = "")
	public List<Map<String, String>> findBusinessCodeAndName(@PathVariable("id") String id) {
		logger.info("!!!!!!!!findBusinessCodeAndName!!!!!!!!!!!参数" + id);
		List<Map<String, String>> result = new ArrayList<>();
		if (null != id) {
			String[] idArr = id.split(",");
			List<String> ids = Arrays.asList(idArr);
			result = cmdbBusinessMapper.findBusinessCodeAndName(ids);
		}
		return result;
	}

	@RequestMapping(value = "/getFirstBusiness/{businessCodes}", method = RequestMethod.GET)
	public List<Map<String, String>> getFirstBusiness(@PathVariable("businessCodes") String businessCodes) {
		logger.info("!!!!!!!!getFirstBusiness!!!!!!!!!!!参数" + businessCodes);
		List<Map<String, String>> result = new ArrayList<>();
		if (null != businessCodes) {
			String[] idArr = businessCodes.split(",");
			List<String> ids = Arrays.asList(idArr);
			result = cmdbBusinessMapper.getFirstBusiness(ids);
		}
		return result;
	}

	/**
	 * <p>
	 * Description: cmdb高级查询
	 * </p>
	 * 
	 * @param request
	 *            AdvancedCmdbSearchRequest
	 * @return
	 */
	@RequestMapping(value = "/cmdbAdvancedSearchDeviceIdList", method = RequestMethod.POST)
	@ApiOperation(value = "cmdb高级查询", notes = "cmdb高级查询")
	public @ResponseBody List<String> cmdbAdvancedSearchDeviceIdList(@RequestBody AdvancedCmdbSearchRequest request) {
		// LogWatch logWatch = new LogWatch();
		// logWatch.start();
		List<String> basisList = new ArrayList<>();
		List<String> advanceList = new ArrayList<>();
		List<String> resultList = new ArrayList<>();
		Map<String, Object> mainParams_map = request.getMainParams();
		// List<ModelColumnCondition> modelParams_list =
		// request.getModelParamList();
		if (mainParams_map != null && !mainParams_map.isEmpty()) {
			List<Map<String, Object>> deviceList = assetsMapper.cmdbAdvancedSearchDeviceIdList(mainParams_map);
			if (deviceList != null) {
				for (Map<String, Object> map : deviceList) {
					basisList.add(map.get("ID").toString().trim());
				}
			}
		}
		if (mainParams_map != null && !mainParams_map.isEmpty()) {
			/*
			 * basisList.retainAll(advanceList); resultList.addAll(basisList);
			 */
			if (basisList.size() >= advanceList.size()) {
				resultList = receiveCollectionList(basisList, advanceList);
			} else {
				resultList = receiveCollectionList(advanceList, basisList);
			}
		} else if (mainParams_map != null && !mainParams_map.isEmpty()) {
			resultList.addAll(basisList);
		} else if ((mainParams_map == null || mainParams_map.isEmpty())) {
			resultList.addAll(advanceList);
		}
		String modules = ModuleUtils.getModule().get("cmdb_advancedSearch_query");
		StringBuilder content = new StringBuilder();
		content.append("执行操作：" + modules + "<br>");
		// logWatch.end(modules, content.toString(), LogWatch.LOG_TYPE_VISIT,
		// LogWatch.FLAG_SUCCESS);
		return resultList;
	}

  

    /**
     * @方法描述：获取两个ArrayList的交集
     * @param firstArrayList 第一个ArrayList
     * @param secondArrayList 第二个ArrayList
     * @return resultList 交集ArrayList
     */
    public List<String> receiveCollectionList(List<String> firstArrayList, List<String> secondArrayList) {
        List<String> resultList = new ArrayList<String>();
        LinkedList<String> result = new LinkedList<String>(firstArrayList);// 大集合用linkedlist
        HashSet<String> othHash = new HashSet<String>(secondArrayList);// 小集合用hashset
        Iterator<String> iter = result.iterator();// 采用Iterator迭代器进行数据的操作
        while(iter.hasNext()) {
            if(!othHash.contains(iter.next())) {
                iter.remove();
            }
        }
        resultList = new ArrayList<String>(result);
        return resultList;
    }
	
	@RequestMapping(value = "/findAlarmByDiffValue/{deviceIps}/{bussinessCodes}", method = RequestMethod.GET)
	public List<Map<String, String>> findAlarmByDiffValue(@PathVariable("deviceIps") String deviceIps, @PathVariable("bussinessCodes") String bussinessCodes) {
		logger.info("!!!!!!!!findAlarmByDiffValue!!!!!!!!!!!参数" + deviceIps);
		logger.info("!!!!!!!!findAlarmByDiffValue!!!!!!!!!!!参数" + bussinessCodes);
		List<Map<String, String>> result = new ArrayList<>();
		if (null != deviceIps) {
			String[] idArr = deviceIps.split(",");
			String[] bCodes = bussinessCodes.split(",");
			List<String> ips = Arrays.asList(idArr);
			List<String> codes = Arrays.asList(bCodes);
			result = cmdbBusinessMapper.findAlarmByDiffValue(ips,codes);
		}
		return result;
	}
	
	@RequestMapping(value = "/getBussCode/{moniObject}", method = RequestMethod.GET)
	public String getBussCode(@PathVariable("moniObject") String moniObject){
		logger.info("!!!!!!!!getBussCode!!!!!!!!!!!参数" + moniObject);
		String rs = "";
		List<Map<String, String>> result = cmdbBusinessMapper.getBussCodeByMoniObject(moniObject);
		if(result.size() > 0){
			rs = result.get(0).get("MONI_BUSI_CODE");
		}
		logger.info("!!!!!!!!getBussCode!!!!!!!!!!!返回参数" + rs);
		return rs;
	}
}
