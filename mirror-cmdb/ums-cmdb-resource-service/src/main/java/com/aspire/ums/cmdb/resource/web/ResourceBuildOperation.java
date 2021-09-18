package com.aspire.ums.cmdb.resource.web;//package com.aspire.ums.cmdb.resource.web;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aspire.ums.cmdb.resource.entity.*;
import com.aspire.ums.cmdb.resource.mapper.CmdbBusinessMapper;
import com.aspire.ums.cmdb.resource.mapper.CmdbMaintenanceInfoMapper;
import com.aspire.ums.cmdb.resource.mapper.ConfigDictMapper;
import com.aspire.ums.cmdb.resource.service.ResBuildDetailService;
import com.aspire.ums.cmdb.resource.service.ResBuildService;
import com.aspire.ums.cmdb.resource.service.ResourceEstimateService;
import com.aspire.ums.cmdb.util.POICascadeUtils2;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.*;

@RestController
@RequestMapping(value = "/resource/resourceBuildManage")
@Api("资源建设API")
public class ResourceBuildOperation {

    private static final Logger LOGGER = Logger.getLogger(ResourceBuildOperation.class);

    @Autowired
    private ResBuildService resBuildService;

    @Autowired
    private ResBuildDetailService resBuildDetailService;

    @Autowired
    private ResourceEstimateService resourceEstimateService;

    @Autowired
    private ConfigDictMapper configDictMapper;

    @Autowired
    private CmdbBusinessMapper cmdbBusinessMapper;

    @Autowired
    private CmdbMaintenanceInfoMapper cmdbMaintenanceInfoMapper;
//    @RequestMapping(value = "/build")
//    public String index(Model model) {
//        return "/modules/resourceManage/resourceBuildManage";
//    }


    @RequestMapping(value = "/getResouceBuildManageData", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "资源建设管理数据查询", notes = "资源建设管理数据查询")
    public ResBuildMaDataResponse getResouceBuildManageData(@RequestBody ResBuildMaDataRequest request) {
        LOGGER.info("[Request]>>>" + request.toString());
        ResBuildMaDataResponse response = new ResBuildMaDataResponse();
        List<ResBuildMaData> selectData = resBuildService.selectResBuildMaData(request);
        if (null != selectData && selectData.size() > 0) {
            int selectDataCount = resBuildService.selectResBuildMaCount(request);
            response.setFlag(true);
            response.setSum(selectDataCount);
            response.setResult(selectData);
            LOGGER.info("[Response]>>>" + selectData.toString());
        } else {
            response.setFlag(false);
            LOGGER.info("[Response]>>>" + "null");
        }
        return response;
    }

    @RequestMapping(value = "/getResourceEstimate", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "获取资源预估数据", notes = "获取资源预估数据")
    public List<ComboBox> getResourceEstimate() {
        List<ComboBox> res = new ArrayList<>();
        // 获取资源预估
        List<Map<String, String>> selectList = resBuildService.getResourceEstimate();
        if (selectList.size() > 0) {
            for (Map<String, String> map : selectList) {
                ComboBox response = new ComboBox();
                response.setId(String.valueOf(map.get("id")));
                response.setText(map.get("resourcePool"));
                res.add(response);
            }
        }
        return res;
    }

    @RequestMapping("/getResourceBuildName")
    @ResponseBody
    @ApiOperation(value = "获取资源建设名称", notes = "获取资源建设名称")
    public List<ResBuildNameResponse> getResourceBuildName(@RequestBody String request) {
        LOGGER.info("[Start]>>>" + request);
        JSONObject json = JSON.parseObject(request);
        Integer id = Integer.valueOf(json.get("id").toString());
        List<ResBuildNameResponse> selectList = resBuildService.getResourceBuildName(id);
        LOGGER.info("[End]>>>" + selectList.toString());
        return selectList;
    }

    @RequestMapping(value = "/addResourceBuild")
    @ResponseBody
    @ApiOperation(value = "添加资源建设数据", notes = "添加资源建设数据")
    public AddOrUpdateResponse addResourceBuild(@RequestBody List<AddResBuildRequest> request) {

        LOGGER.info("[Request]>>>" + request.toString());

        AddOrUpdateResponse response = new AddOrUpdateResponse();
        String username = "admin";
        try {
            for(AddResBuildRequest req : request) {
                ResBuild resBuild = new ResBuild();
                ResBuildDetail resBuildDetail = new ResBuildDetail();
                resBuild.setEstimateId(Integer.valueOf(req.getResourceEstimateId()));
                resBuild.setBuildName(req.getAddBuildName());
                resBuild.setResourcePool(req.getAddResourcePool());
                resBuild.setStatus("1");
                resBuild.setCreateId(username);
                resBuild.setUpdateId(username);
                resBuildService.addResourceBuild(resBuild);
                int id = resBuild.getId();
                resBuildDetail.setBuildId(id);
                resBuildDetail.setServerType(req.getAddServerType());
                resBuildDetail.setServerName(req.getAddServerName());
                resBuildDetail.setCount(req.getAddCount());
                resBuildDetail.setUnit(req.getAddUnit());
                resBuildDetail.setConfigDetail(req.getAddConfigDetail());
                resBuildDetail.setImports(1);
                resBuildDetailService.addResourceBuildDetail(resBuildDetail);
                // 修改资源预估状态
                resBuildService.updateResourceEstimate(resBuild);
                response.setFlag(true);
                response.setMsg("添加成功！");
                LOGGER.info("[Response]>>>资源配置添加成功");

            }
        } catch (Exception e) {
            LOGGER.error(e);
            response.setFlag(false);
            response.setMsg("添加失败！");
        }
        return response;
    }

    /**
    * request
    * {"buildId":"1054"}
    *
    */
    @RequestMapping(value = "/getResourceBuildData")
    @ResponseBody
    @ApiOperation(value = "获取资源建设数据", notes = "获取资源建设数据")
    public ResBuildMaData getResourceBuildData(@RequestBody String request) {
        LOGGER.info("[Request]>>>" + request);
        JSONObject json = JSON.parseObject(request);
        Integer buildId = Integer.valueOf(json.get("buildId").toString());
        ResBuildMaData response = resBuildService.getResourceBuildData(buildId);
        return response;
    }
//
    @RequestMapping(value = "/updateResourceBuild")
    @ResponseBody
    @ApiOperation(value = "更新资源建设数据", notes = "更新资源建设数据")
    public AddOrUpdateResponse updateResourceBuild(@RequestBody UpdateResBuildRequest request) {
        LOGGER.info("[Request]>>>" + request.toString());
        String username = "admin";
        AddOrUpdateResponse response = new AddOrUpdateResponse();
        try {
            // 表resource_build
            ResBuild resBuild = new ResBuild();
            resBuild.setId(request.getBuildId());
            resBuild.setBuildName(request.getBuildName());
            resBuild.setUpdateId(username);
            resBuildService.updateResourceBuild(resBuild);
            // 表resource_build_detail
            ResBuildDetail resBuildDetail = new ResBuildDetail();
            resBuildDetail.setBuildId(request.getBuildId());
            resBuildDetail.setServerType(request.getServerType());
            resBuildDetail.setServerName(request.getServerName());
            resBuildDetail.setCount(Integer.valueOf(request.getCount()));
            resBuildDetail.setUnit(request.getAddUnit());
            resBuildDetail.setConfigDetail(request.getConfigDetail());
            resBuildDetailService.updateResourceBuildDetail(resBuildDetail);
            // 修改资源预估状态

            response.setFlag(true);
            response.setMsg("修改成功！");
            LOGGER.info("[Response]>>>资源配置修改成功");
        } catch (Exception e) {
            LOGGER.error(e);
            response.setFlag(false);
            response.setMsg("修改失败！");
        }
        return response;
    }

    @RequestMapping(value = "/importOpen")
    @ResponseBody
    @ApiOperation(value = "启用资源数据", notes = "启用资源数据")
    public AddOrUpdateResponse importOpen(@RequestBody Map<String, Object> param) {
        LOGGER.info("[Request]>>>" + param.get("id"));
        AddOrUpdateResponse response = new AddOrUpdateResponse();
        param.put("importStatus", 0);
        try {
            resBuildService.updateImportStatus(param);
            response.setFlag(true);
            response.setMsg("修改成功！");
        } catch (Exception e) {
            response.setFlag(false);
            response.setMsg("修改失败！");
        }

        return response;
    }

    @RequestMapping(value = "/importClose")
    @ResponseBody
    @ApiOperation(value = "关闭资源数据", notes = "关闭资源数据")
    public AddOrUpdateResponse importClose(@RequestBody Map<String, Object> param) {
        LOGGER.info("[Request]>>>" + param.get("id"));
        AddOrUpdateResponse response = new AddOrUpdateResponse();
        param.put("importStatus", 1);
        try {
            resBuildService.updateImportStatus(param);
            response.setFlag(true);
            response.setMsg("修改成功！");
        } catch (Exception e) {
            response.setFlag(false);
            response.setMsg("修改失败！");
        }

        return response;
    }

//    // 导入excel数据到 cmdb-device-assets
    @RequestMapping("/saveCmdbHostAssetsExcelData")
    @ResponseBody
    @Transactional
    @ApiOperation(value = "保存excel导入的数据", notes = "保存excel导入的数据")
    public Object saveCmdbHostAssetsExcelData(HttpServletRequest request) {

        String result = "";
        String ens = request.getParameter("entities");
        ens = ens.replace("}{", "},{");
        ens = "[" + ens.toString() + "]";
        List<CmdbHostAssetsExcelData> cmdbHostAssetsExcelList = JSONArray.parseArray(ens,
                CmdbHostAssetsExcelData.class);
        String build_detail_id = request.getParameter("resourceBuildDetailId");
        if (cmdbHostAssetsExcelList == null) {
            result = "数据为空,保存失败";
        } else {
            for (CmdbHostAssetsExcelData cmdbHostAssetsExcelData : cmdbHostAssetsExcelList) {
                if ("".equals(cmdbHostAssetsExcelData.getHost_backup())) {
                    cmdbHostAssetsExcelData.setHost_backup("主");
                }
                if ("".equals(cmdbHostAssetsExcelData.getResource_plan())) {
                    cmdbHostAssetsExcelData.setResource_plan(null);
                }

                cmdbHostAssetsExcelData.setBuild_detail_id(build_detail_id);

            }
            try {
                resBuildService.saveCmdbHostAssetsExcelData(cmdbHostAssetsExcelList);
                Map<String, Object> paramer = Maps.newHashMap();
                paramer.put("id", build_detail_id);
                paramer.put("importStatus", 2);
                resBuildService.updateImportStatus(paramer);
                cmdbHostAssetsExcelList = null;
                result = "保存成功";
            } catch (Exception e) {
                LOGGER.error("设备资产信息导入数据失败", e);
                result = "保存失败";
            }
        }
        Map<String, Object> hashMap = new HashMap<>();
        hashMap.put("result", result);
        return hashMap;
    }

    @RequestMapping(value = "/getComboboxData")
    @ResponseBody
    @ApiOperation(value = "获取控件数据", notes = "获取控件数据")
    public ComboBoxDataResponse getComboboxData() {
        ComboBoxDataResponse response = new ComboBoxDataResponse();
        List<ComboBox> names = new ArrayList<>();
        List<Map<String, String>> namesList = resBuildService.getReBuildName();
        if (null != namesList && namesList.size() > 0) {
            for (Map<String, String> map : namesList) {
                ComboBox name = new ComboBox();
                name.setId(UUID.randomUUID().toString());
                name.setText(map.get("text"));
                names.add(name);
            }
        }
        List<ComboBox> types = new ArrayList<>();
        List<Map<String, String>> typesList = resBuildService.getResourceBuildReType();
        if (null != typesList && typesList.size() > 0) {
            for (Map<String, String> map : typesList) {
                ComboBox type = new ComboBox();
                type.setId(map.get("id"));
                type.setText(map.get("text"));
                types.add(type);
            }
        }
        List<ComboBox> pools = new ArrayList<>();
        List<Map<String, String>> poolsList = resBuildService.getReBuildResourPool();
        if (null != namesList && namesList.size() > 0) {
            for (Map<String, String> map : poolsList) {
                ComboBox pool = new ComboBox();
                pool.setId(UUID.randomUUID().toString());
                pool.setText(map.get("text"));
                pools.add(pool);
            }
        }
        response.setNames(names);
        response.setPools(pools);
        response.setTypes(types);
        return response;
    }

    // cmdb设备资产信息--模板，参考字段的导出
    @RequestMapping("/exportGridDataCMDB2")
    @ApiOperation(value = "下载cmdb设备资产信息--模板", notes = "下载cmdb设备资产信息--模板")
    public void exportGridData3(@RequestParam Map<String, Object> params, HttpServletRequest request,
                                HttpServletResponse response) {
        try {
            if (params != null) {
                /* 第一步 */
                String[] headerList = { "主IP地址(必填)", "一级业务", "二级业务", "IDC位置(必填)", "所属位置(必填)", "机房位置", "设备分类(必填)",
                        "设备类型(必填)", "设备型号(必填)", "设备系统类型(必填)", "设备状态(必填)", "是否ansible管理(必填)", "是否资源池管理设备(必填)", "主备关系",
                        "维保型号", "维保厂家", "资源计划性", "分布式存储类型", "项目归属", "块存储(GB)(必填)", "分布式存储(GB)", "其它IP地址", "逻辑名", "机柜号",
                        "刀箱号", "槽位号", "刀箱管理IP", "所在宿主机IP", "承载虚拟机名称", "承载虚拟机IP", "设备规格", "序列号", "板卡序列号", "资产编号",
                        "分布式存储挂载目录", "维保时间(格式：xxxx/xx/xx)", "上线时间(格式：xxxx/xx/xx)", "console IP", "console vlan",
                        "console 掩码", "console 网关", "console 账号", "console 密码", "业务 vlan", "本地磁盘大小", "初始外挂磁盘", "网络区域",
                        "备注", "转资成本", "单价", "按比例分摊日期(格式：xxxx/xx/xx)", "使用年限" };

                // 所属位置
                Map<String, Object> idcType = new HashMap<String, Object>();
                idcType.put("type", "idcType");
                // 机房位置
                Map<String, Object> idcLocationType = new HashMap<String, Object>();
                idcLocationType.put("type", "idcLocationType");
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

                List<CmdbBusiness> businesses = cmdbBusinessMapper.getBusinessByPId(null);
                List<Map> idcTypeList = configDictMapper.getDictConfig(idcType);

                List<Map> idcLocationTypeList = configDictMapper.getDictConfig(idcLocationType);
                List<Map> deviceClassList = configDictMapper.getDictConfig(deviceClass);
                List<Map> deviceModelList = configDictMapper.getDictConfig(deviceModel);
                List<Map> deviceTypeList = configDictMapper.getDictConfig(deviceType);
                List<Map> deviceOsTypeList = configDictMapper.getDictConfig(deviceOsType);
                List<CmdbMaintenanceInfo> maintenanceInfoList = cmdbMaintenanceInfoMapper.getMaintenanceInfo();
                /* 第二步 */
                /* 主IP地址(必填) */
                String[] DEVICE_IP = {};
                List<Object> deviceIpList = new ArrayList<Object>();
                deviceIpList.add(DEVICE_IP);
                deviceIpList.add("DEVICE_IP");

                /* 一级业务(必填),二级业务 */
                List<Map<String, Object>> businessesList = new ArrayList<Map<String, Object>>();
                List<String> businessArray = new ArrayList<>();
                for (CmdbBusiness cmdbBusiness : businesses) {
                    ArrayList<String> business2List = new ArrayList<>();
                    Map<String, Object> hashMap = new HashMap<String, Object>();
                    String pID = cmdbBusiness.getId();
                    String businessName1 = cmdbBusiness.getBusinessName();
                    List<CmdbBusiness> businesses2 = cmdbBusinessMapper.getBusinessByPId(pID);
                    if (businesses2.isEmpty()) {// 如何没有二级业务的话，保留一级业务
                        businessArray.add(businessName1);
                        business2List.add(null);
                    } else {
                        for (CmdbBusiness cmdbBusiness2 : businesses2) {
                            businessArray.add(businessName1);
                            business2List.add(cmdbBusiness2.getBusinessName());
                        }
                    }
                    hashMap.put("BUSINESS_LEVEL1", businessName1);
                    hashMap.put("BUSINESS_LEVEL2", business2List);
                    businessesList.add(hashMap);
                }
                // 去重业务一
                List<String> newList = new ArrayList<String>();
                for (String cd : businessArray) {
                    if (!newList.contains(cd)) {
                        newList.add(cd);
                    }
                }
                String[] business1Lists = (String[]) newList.toArray(new String[0]);
                List<Object> business1 = new ArrayList<Object>();
                business1.add(business1Lists);
                business1.add("business1");

                List<Object> business2 = new ArrayList<Object>();
                business2.add(businessesList);
                business2.add("business2");

                /* IDC位置(必填),所属位置(必填)，机房位置 */
                List<Object> idcTypeLists = null;
                List<Object> idcLists = null;
                List<Object> idcLocationTypeLists = null;

                String[] tempList = null;
                List<String> stringList1 = new ArrayList<String>();
                List<String> stringList2 = new ArrayList<String>();

                if (!idcTypeList.isEmpty()) {
                    for (Map map : idcTypeList) {
                        stringList1.add((String) map.get("LABEL"));
                        stringList2.add((String) map.get("VALUE"));
                    }
                    tempList = (String[]) stringList1.toArray(new String[0]);
                    idcTypeLists = new ArrayList<Object>();
                    idcTypeLists.add(tempList);
                    idcTypeLists.add("IDC_LABEL");
                    stringList1.clear();
                    tempList = null;

                    tempList = (String[]) stringList2.toArray(new String[0]);
                    idcLists = new ArrayList<Object>();
                    idcLists.add(tempList);
                    idcLists.add("IDC");
                    stringList2.clear();
                    tempList = null;
                }
                if (!idcLocationTypeList.isEmpty()) {
                    for (Map map : idcLocationTypeList) {
                        stringList1.add((String) map.get("LABEL"));
                    }
                    tempList = (String[]) stringList1.toArray(new String[0]);
                    idcLocationTypeLists = new ArrayList<Object>();
                    idcLocationTypeLists.add(tempList);
                    idcLocationTypeLists.add("IDC_LOCATION");
                    stringList1.clear();
                    tempList = null;
                }

                /* 设备分类(必填),设备类型(必填),设备型号(必填), */
                List<String> deviceClassArray = new ArrayList<>();

                LinkedList<Map<String, Object>> modelLinkedList = new LinkedList<Map<String, Object>>();
                for (Map map1 : deviceClassList) {
                    Map<String, Object> hashMap = new HashMap<String, Object>();
                    List<String> modelList = new ArrayList<>();
                    List<String> typeList = new ArrayList<>();
                    String ID = (String) map1.get("ID");
                    for (Map map2 : deviceModelList) {
                        String PID2 = (String) map2.get("PARENT_ID");
                        if (PID2.equals(ID)) {
                            deviceClassArray.add((String) map1.get("VALUE"));
                            modelList.add((String) map2.get("VALUE"));

                        }
                    }

                    for (Map map3 : deviceTypeList) {

                        String PID = (String) map3.get("PARENT_ID");
                        if (PID.equals(ID)) {
                            String object = (String) map3.get("VALUE");
                            if (deviceClassArray.contains(object)) {
                                object = object + "re";
                            }
                            String replace2 = object.replace('（', 'l');
                            String replace = replace2.replace('）', 'r');
                            typeList.add(replace);
                            // typeList.add((String)map3.get("VALUE"));
                        }
                    }
                    hashMap.put("DEVICE_CLASS", (String) map1.get("VALUE"));
                    hashMap.put("DEVICE_MODEL", modelList);
                    hashMap.put("DEVICE_TYPE", typeList);
                    modelLinkedList.add(hashMap);
                }
                // 去重设备分类
                List<String> newList1 = new ArrayList<String>();
                for (String cd : deviceClassArray) {
                    if (!newList1.contains(cd)) {
                        newList1.add(cd);
                    }
                }

                tempList = (String[]) newList1.toArray(new String[0]);
                List<Object> deviceClassLists = new ArrayList<Object>();
                deviceClassLists.add(tempList);
                deviceClassLists.add("deviceClassLists");

                List<Object> deviceTypeLists = new ArrayList<Object>();
                deviceTypeLists.add(modelLinkedList);
                deviceTypeLists.add("deviceTypeLists");

                List<Object> deviceModelLists = new ArrayList<Object>();
                deviceModelLists.add(modelLinkedList);
                deviceModelLists.add("deviceModelLists");

                stringList1.clear();
                tempList = null;

                /*
                 * 设备系统类型(必填),设备状态(上电/下电)(必填),是否ansible管理(1=是/0=否)(必填),是否资源池管理设备(1=是/0=否)(必填),
                 * 主备关系,维保型号(请参考字段/选填),维保厂家,资源计划性 ,
                 */
                List<Object> deviceOsTypeLists = null;
                if (!deviceOsTypeList.isEmpty()) {
                    for (Map map : deviceOsTypeList) {
                        stringList1.add((String) map.get("LABEL"));
                    }
                    tempList = (String[]) stringList1.toArray(new String[0]);
                    deviceOsTypeLists = new ArrayList<Object>();
                    deviceOsTypeLists.add(tempList);
                    deviceOsTypeLists.add("device_os_type");
                    stringList1.clear();
                    tempList = null;
                }

                List<Object> deviceStatus = new ArrayList<Object>();
                stringList1.add("上电");
                stringList1.add("下电");
                tempList = (String[]) stringList1.toArray(new String[0]);
                deviceStatus.add(tempList);
                deviceStatus.add("deviceStatus");
                stringList1.clear();
                tempList = null;

                String[] managed_by_ansible = { "是", "否" };
                List<Object> managed_by_ansibleList = new ArrayList<Object>();
                managed_by_ansibleList.add(managed_by_ansible);
                managed_by_ansibleList.add("managed_by_ansible");

                String[] mgd_by_pool = { "是", "否" };
                List<Object> mgd_by_poolList = new ArrayList<Object>();
                mgd_by_poolList.add(mgd_by_pool);
                mgd_by_poolList.add("mgd_by_pool");

                String[] host_backup = { "主", "备1", "备2", "备3", "备4", "备5", "备6", "备7", "备8", "备9", "备10", "备11",
                        "备12" };
                List<Object> host_backupList = new ArrayList<Object>();
                host_backupList.add(host_backup);
                host_backupList.add("host_backup");

                List<Object> device_maintenanceList = null;
                List<Object> device_maintenance_venderList = null;
                if (!maintenanceInfoList.isEmpty()) {
                    for (CmdbMaintenanceInfo cmdbMaintenanceInfo : maintenanceInfoList) {
                        stringList1.add(cmdbMaintenanceInfo.getMode());
                        stringList2.add(cmdbMaintenanceInfo.getVender());
                    }
                    tempList = (String[]) stringList1.toArray(new String[0]);
                    device_maintenanceList = new ArrayList<Object>();
                    device_maintenanceList.add(tempList);
                    device_maintenanceList.add("device_maintenance");
                    stringList1.clear();
                    tempList = null;
                    tempList = (String[]) stringList2.toArray(new String[0]);
                    device_maintenance_venderList = new ArrayList<Object>();
                    device_maintenance_venderList.add(tempList);
                    device_maintenance_venderList.add("device_maintenance_vender");
                    stringList2.clear();
                    tempList = null;
                }
                String[] resource_plan = { "计划内", "计划外" };
                List<Object> resource_planList = new ArrayList<Object>();
                resource_planList.add(resource_plan);
                resource_planList.add("resource_plan");

                /*
                 * 分布式存储类型,项目归属,块存储(GB)(必填),分布式存储（GB）
                 * ,其它IP地址,逻辑名,机柜号,刀箱号,槽位号,刀箱管理IP,所在宿主机IP,承载虚拟机名称,承载虚拟机IP,设备规格,序列号,板卡序列号,资产编号,
                 * 分布式存储挂载目录, 维保时间,上线时间,console IP,console vlan,console 掩码 ,console 网关,console
                 * 账号,console 密码,业务 vlan,本地磁盘大小,初始外挂磁盘,网络区域,备注';
                 */
                String[] dis_st_type = {};
                List<Object> dis_st_typeList = new ArrayList<Object>();
                dis_st_typeList.add(dis_st_type);
                dis_st_typeList.add("dis_st_type");

                String[] blong_to = {};
                List<Object> blong_toList = new ArrayList<Object>();
                blong_toList.add(blong_to);
                blong_toList.add("blong_to");

                String[] blockSize = {};
                List<Object> blockSizeList = new ArrayList<Object>();
                blockSizeList.add(blockSize);
                blockSizeList.add("blockSize");

                String[] disStorage = {};
                List<Object> disStorageList = new ArrayList<Object>();
                disStorageList.add(disStorage);
                disStorageList.add("disStorage");

                String[] other_ip = {};
                List<Object> other_ipList = new ArrayList<Object>();
                other_ipList.add(other_ip);
                other_ipList.add("other_ip");

                String[] device_log_name = {};
                List<Object> device_log_nameList = new ArrayList<Object>();
                device_log_nameList.add(device_log_name);
                device_log_nameList.add("device_log_name");

                String[] device_cell = {};
                List<Object> device_cellList = new ArrayList<Object>();
                device_cellList.add(device_cell);
                device_cellList.add("device_cell");

                String[] box_num = {};
                List<Object> box_numList = new ArrayList<Object>();
                box_numList.add(box_num);
                box_numList.add("box_num");

                String[] slot_num = {};
                List<Object> slot_numList = new ArrayList<Object>();
                slot_numList.add(slot_num);
                slot_numList.add("slot_num");

                String[] box_mgd_ip = {};
                List<Object> box_mgd_ipList = new ArrayList<Object>();
                box_mgd_ipList.add(box_mgd_ip);
                box_mgd_ipList.add("box_mgd_ip");

                String[] exsi_ip = {};
                List<Object> exsi_ipList = new ArrayList<Object>();
                exsi_ipList.add(exsi_ip);
                exsi_ipList.add("exsi_ip");

                String[] vm_name = {};
                List<Object> vm_nameList = new ArrayList<Object>();
                vm_nameList.add(vm_name);
                vm_nameList.add("vm_name");

                String[] vm_ip = {};
                List<Object> vm_ipList = new ArrayList<Object>();
                vm_ipList.add(vm_ip);
                vm_ipList.add("vm_ip");

                String[] device_standard = {};
                List<Object> device_standardList = new ArrayList<Object>();
                device_standardList.add(device_standard);
                device_standardList.add("device_standard");

                String[] device_sn = {};
                List<Object> device_snList = new ArrayList<Object>();
                device_snList.add(device_sn);
                device_snList.add("device_sn");

                String[] b_card_sn = {};
                List<Object> b_card_snList = new ArrayList<Object>();
                b_card_snList.add(b_card_sn);
                b_card_snList.add("b_card_sn");

                String[] asset_number = {};
                List<Object> asset_numberList = new ArrayList<Object>();
                asset_numberList.add(asset_number);
                asset_numberList.add("asset_number");

                String[] dis_st_dir = {};
                List<Object> dis_st_dirList = new ArrayList<Object>();
                dis_st_dirList.add(dis_st_dir);
                dis_st_dirList.add("dis_st_dir");

                String[] maintenance_time = {};
                List<Object> maintenance_timeList = new ArrayList<Object>();
                maintenance_timeList.add(maintenance_time);
                maintenance_timeList.add("maintenance_time");

                String[] online_time = {};
                List<Object> online_timeList = new ArrayList<Object>();
                online_timeList.add(online_time);
                online_timeList.add("online_time");

                String[] console_ip = {};
                List<Object> console_ipList = new ArrayList<Object>();
                console_ipList.add(console_ip);
                console_ipList.add("console_ip");

                String[] console_vlan = {};
                List<Object> console_vlanList = new ArrayList<Object>();
                console_vlanList.add(console_vlan);
                console_vlanList.add("console_vlan");

                String[] console_mask = {};
                List<Object> console_maskList = new ArrayList<Object>();
                console_maskList.add(console_mask);
                console_maskList.add("console_mask");

                String[] console_gw = {};
                List<Object> console_gwList = new ArrayList<Object>();
                console_gwList.add(console_gw);
                console_gwList.add("console_gw");

                String[] console_user = {};
                List<Object> console_userList = new ArrayList<Object>();
                console_userList.add(console_user);
                console_userList.add("console_user");

                String[] console_password = {};
                List<Object> console_passwordList = new ArrayList<Object>();
                console_passwordList.add(console_password);
                console_passwordList.add("console_password");

                String[] business_vlan = {};
                List<Object> business_vlanList = new ArrayList<Object>();
                business_vlanList.add(business_vlan);
                business_vlanList.add("business_vlan");

                String[] local_disk = {};
                List<Object> local_diskList = new ArrayList<Object>();
                local_diskList.add(local_disk);
                local_diskList.add("local_disk");

                String[] mount_disk = {};
                List<Object> mount_diskList = new ArrayList<Object>();
                mount_diskList.add(mount_disk);
                mount_diskList.add("mount_disk");

                String[] network_area = {};
                List<Object> network_areaList = new ArrayList<Object>();
                network_areaList.add(network_area);
                network_areaList.add("network_area");

                String[] remark = {};
                List<Object> remarkList = new ArrayList<Object>();
                remarkList.add(remark);
                remarkList.add("remark");

                /*
                 * 转资成本,单价,按比例分摊日期,使用年限 trans_cost,unit_price,prorate_date,service_life
                 */
                String[] trans_cost = {};
                List<Object> trans_costList = new ArrayList<Object>();
                trans_costList.add(trans_cost);
                trans_costList.add("trans_cost");

                String[] unit_price = {};
                List<Object> unit_priceList = new ArrayList<Object>();
                unit_priceList.add(unit_price);
                unit_priceList.add("unit_price");

                String[] prorate_date = {};
                List<Object> prorate_dateList = new ArrayList<Object>();
                prorate_dateList.add(prorate_date);
                prorate_dateList.add("prorate_date");

                String[] service_life = {};
                List<Object> service_lifeList = new ArrayList<Object>();
                service_lifeList.add(service_life);
                service_lifeList.add("service_life");

                /*
                 * '主IP地址(必填),一级业务(必填),二级业务,IDC位置(必填),所属位置(必填),机房位置,设备分类(必填),设备类型(必填),设备型号(必填),
                 * 设备系统类型(必填),设备状态
                 * (上电/下电)(必填),块存储(GB)(必填),分布式存储（GB）,是否ansible管理(1=是/0=否)(必填),是否资源池管理设备(1=是/0=否)
                 * (必填),维保型号 (请参考字段/选填),维保厂家 (请参考字段/选填),其它IP地址,逻辑名,主备关系
                 * (主/备1/备2/...),机柜号,刀箱号,槽位号,刀箱管理IP,所在宿主机IP,承载虚拟机名称,承载虚拟机IP,设备规格,序列号,板卡序列号,资产编号,
                 * 分布式存储挂载目录,维保时间,上线时间,分布式存储类型,资源计划性 (计划内/计划外),项目归属,console IP,console
                 * vlan,console 掩码,console 网关,console 账号,console 密码,业务
                 * vlan,本地磁盘大小,初始外挂磁盘,网络区域,备注';
                 * 'DEVICE_IP,BUSINESS_LEVEL1,BUSINESS_LEVEL2,IDC,IDC_LABEL,IDC_LOCATION,
                 * DEVICE_CLASS,DEVICE_TYPE,DEVICE_MODEL,DEVICE_OS_TYPE,DEVICE_STATUS,BLOCK_SIZE
                 * ,DIS_STORAGE,MANAGED_BY_ANSIBLE,FOR_CHECK,MGD_BY_POOL,DEVICE_MAINTENANCE,
                 * DEVICE_MAINTENANCE_VENDER,OTHER_IP,DEVICE_LOG_NAME,HOST_BACKUP,DEVICE_CELL,
                 * BOX_NUM,SLOT_NUM,BOX_MGD_IP,EXSI_IP,VM_NAME,VM_IP,DEVICE_STANDARD,DEVICE_SN,
                 * B_CARD_SN,ASSET_NUMBER,DIS_ST_DIR,MAINTENANCE_TIME,ONLINE_TIME,DIS_ST_TYPE,
                 * RESOURCE_PLAN,BLONG_TO,CONSOLE_IP,CONSOLE_VLAN,CONSOLE_MASK,CONSOLE_GW,
                 * CONSOLE_USER,CONSOLE_PASSWORD,BUSINESS_VLAN,LOCAL_DISK,MOUNT_DISK,
                 * NETWORK_AREA,REMARK';
                 */
                /* 第三步 */
                List<List<Object>> result = new ArrayList<>();
                result.add(deviceIpList);
                result.add(business1);
                result.add(business2);
                result.add(idcLists);
                result.add(idcTypeLists);
                result.add(idcLocationTypeLists);
                result.add(deviceClassLists);
                result.add(deviceModelLists);
                result.add(deviceTypeLists);
                result.add(deviceOsTypeLists);// 9
                result.add(deviceStatus);
                result.add(managed_by_ansibleList);
                result.add(mgd_by_poolList);
                result.add(host_backupList);
                result.add(device_maintenanceList);
                result.add(device_maintenance_venderList);
                result.add(resource_planList);// 16

                result.add(dis_st_typeList);// 17-47没有下拉
                result.add(blong_toList);
                result.add(blockSizeList);
                result.add(disStorageList);
                result.add(other_ipList);
                result.add(device_log_nameList);
                result.add(device_cellList);
                result.add(box_numList);
                result.add(slot_numList);
                result.add(box_mgd_ipList);
                result.add(exsi_ipList);
                result.add(vm_nameList);
                result.add(vm_ipList);
                result.add(device_standardList);
                result.add(device_snList);
                result.add(b_card_snList);
                result.add(asset_numberList);
                result.add(dis_st_dirList);
                result.add(maintenance_timeList);
                result.add(online_timeList);
                result.add(console_ipList);
                result.add(console_vlanList);
                result.add(console_maskList);
                result.add(console_gwList);
                result.add(console_userList);
                result.add(console_passwordList);
                result.add(business_vlanList);
                result.add(local_diskList);
                result.add(mount_diskList);
                result.add(network_areaList);
                result.add(remarkList);// 47

                result.add(trans_costList);
                result.add(unit_priceList);
                result.add(prorate_dateList);
                result.add(service_lifeList);// 51

                OutputStream os = response.getOutputStream();// 取得输出流
                try {
                    String title = params.get("title").toString();
                    String fileName = title + ".xls";
                    response.setHeader("Content-Disposition",
                            "attachment;filename=".concat(String.valueOf(URLEncoder.encode(fileName, "UTF-8"))));
                    response.setHeader("Connection", "close");
                    response.setHeader("Content-Type", "application/vnd.ms-excel");

                    /*
                     * POICascadeUtils POICascadeUtils = new POICascade(); Workbook wb = new
                     * HSSFWorkbook(); poiCascadeTest2.utilss(wb, result,headerList); wb.write(os);
                     */

                    POICascadeUtils2 poiCascade = new POICascadeUtils2();
                    Workbook wb = new HSSFWorkbook();
                    poiCascade.cmdbUtilss(wb, result, headerList, title);
                    wb.write(os);

                } catch (Exception e) {
                    LOGGER.error("导出excel失败", e);
                } finally {
                    os.flush();
                    os.close();
                }
            } else {
                throw new NullPointerException("导出excel失败，excel文件不能为空");
            }
        } catch (Exception e) {
            LOGGER.error("导出excel,查询数据失败", e);
        }
    }
}
