package com.aspire.ums.cmdb.resource.web;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aspire.ums.cmdb.resource.entity.ResourceBuildManageQueryEntity;
import com.aspire.ums.cmdb.resource.entity.ResourceDemandCollect;
import com.aspire.ums.cmdb.resource.entity.ResourceDemandColletExcel;
import com.aspire.ums.cmdb.resource.entity.ResourceEstimateResponse;
import com.aspire.ums.cmdb.resource.service.ResourceDemandCollectService;
import com.aspire.ums.cmdb.util.ExcelReaderUtils;
import com.aspire.ums.cmdb.util.ExportExcelUtils;
import com.aspire.ums.cmdb.util.ModuleUtils;
import com.aspire.ums.cmdb.util.POICascadeUtils2;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping(value = "/resource/resourceCollect")
@Api("资源收集管理")
public class ResourceDemandCollectController {

    private final Logger logger = Logger.getLogger(getClass());

    @Autowired
    private ResourceDemandCollectService resourceDemandCollectService;

    /**
     * 资源总览 -> 需求收集管理
     * @param entity
     * @return
     */
    @RequestMapping(value = "/getResouceBuildManageData", method = RequestMethod.POST)
    @ApiOperation(value = "获取资源建设管理数据", notes = "获取资源建设管理数据")
    public Map<String, Object> getResouceBuildManageData(@RequestBody ResourceBuildManageQueryEntity entity) {
        logger.info("[Request]>>> " + JSONObject.toJSON(entity).toString());
        Map<String, Object> result = resourceDemandCollectService.selectResBuildMaData(entity);
        logger.info("[Response]>>>" + JSONObject.toJSON(result).toString());
        return result;
    }

    @RequestMapping(value = "/exportGridData", method = RequestMethod.POST)
    @ApiOperation(value = "获取资源数据导入Excel模板", notes = "获取资源数据导入Excel模板")
    public void exportGridData(@RequestParam Map<String, Object> params, HttpServletResponse response) {
        try {
            if (params != null) {

				/* 第一步 */
                String[] headerList = { "一级部门(必填)", "二级部门(必填)", "应用系统(必填)", "是否立项", "立项时间", "资源提出时间", "资源预期投资时间",
                        "系统描述", "16核、128G、200G系统磁盘", "8核、64G、200G系统磁盘", "8核、32G、200G系统磁盘", "8核、16G、200G系统磁盘",
                        "4核、32G、200G系统磁盘", "4核、16G、200G系统磁盘", "4核、8G、200G系统磁盘", "2核、4G、200G系统磁盘", "虚拟机宿主机低端应用服务器(台)",
                        "分析型服务器(MPP服务器)(台)", "分布式服务器(Hadoop服务器)(台)", "缓存型服务器(台)", "高端应用服务器(台)", "分布式文件存储(TB)",
                        "分布式块存储TB)", "对象存储(TB)", "FC-SAN存储(TB)", "IP-SAN存储(TB)", "FC-SAN裸盘(TB)", "IP-SAN存储(TB)",
                        "备份存储(TB)", "CMNET地址需求(个)", "CMNET带宽资源需求(Gbps)", "IP地址需求(个)", "至IP专网带宽(Gbps)", "mysql数据库",
                        "mongodb数据库", "postgresql数据库", "内存数据库", "其它数据库", "分布式缓存redis", "分布式缓存Memcached",
                        "消息中间件activemq", "消息中间件kafka", "消息中间件rockermq", "应用中间件apache", "应用中间件jboos", "应用中间件tomcat",
                        "负载均衡nginx", "负载均衡haproxy", "分布式协调服务zookeeper", "分布式协调服务etcd", "搜索中间件ES", "镜像仓库DockerRegistry",
                        "工作流JBPM", "工作流activity", "镜像openjdk", "镜像python", "镜像go", "镜像nodejs", "镜像ruby", "镜像net",
                        "CICDjenkins", "开发框架springcloud人", "其它开发框架", "与前期上报需求有无变化，如有，请说明原因", "物理局址要求", "部门负责人", "接口联系人",
                        "电话", "邮箱" };

                String[] resource_pool = {};
                List<Object> resource_poolList = new ArrayList<Object>();
                resource_poolList.add(resource_pool);
                resource_poolList.add("resource_pool");

                // Map<String, Object> resourcePool = new HashMap<String, Object>();
                // resourcePool.put("type", "resourcePool");

                // List<Map> resourcePoolList = componentDataService
                // .queryAllGridData("com.aspire.birp.modules.osa.dao.config.dict.getDictConfig",
                // resourcePool);
                // logger.info("++++++++++++++++++"+resourcePoolList);
                //
                // List<Object> resourcePoolLists = null;
                // List<String> strList= new ArrayList<>();
                // String[] tempList = null;
                //
                // if (!resourcePoolList.isEmpty()) {
                // for (Map map : resourcePoolList) {
                // strList.add((String) map.get("LABEL"));
                // }
                // tempList = (String[]) strList.toArray(new String[0]);
                // resourcePoolLists = new ArrayList<Object>();
                // resourcePoolLists.add(tempList);
                // resourcePoolLists.add("resource_pool");
                //
                // }

                String[] company_name = {};
                List<Object> company_nameList = new ArrayList<Object>();
                company_nameList.add(company_name);
                company_nameList.add("company_name");

                String[] department = {};
                List<Object> departmentList = new ArrayList<Object>();
                departmentList.add(department);
                departmentList.add("department");

                String[] business_name = {};
                List<Object> business_nameList = new ArrayList<Object>();
                business_nameList.add(business_name);
                business_nameList.add("business_name");

                String[] vm_kernel_num = {};
                List<Object> vm_kernel_numList = new ArrayList<Object>();
                vm_kernel_numList.add(vm_kernel_num);
                vm_kernel_numList.add("vm_kernel_num");

                String[] vm_momery = {};
                List<Object> vm_momeryList = new ArrayList<Object>();
                vm_momeryList.add(vm_momery);
                vm_momeryList.add("vm_momery");

                String[] vm_system_disk = {};
                List<Object> vm_system_diskList = new ArrayList<Object>();
                vm_system_diskList.add(vm_system_disk);
                vm_system_diskList.add("vm_system_disk");

                String[] pm_analytical_server_b1 = {};
                List<Object> pm_analytical_server_b1List = new ArrayList<Object>();
                pm_analytical_server_b1List.add(pm_analytical_server_b1);
                pm_analytical_server_b1List.add("pm_analytical_server_b1");

                String[] pm_distributed_server_b2 = {};
                List<Object> pm_distributed_server_b2List = new ArrayList<Object>();
                pm_distributed_server_b2List.add(pm_distributed_server_b2);
                pm_distributed_server_b2List.add("pm_distributed_server_b2");

                String[] pm_distributed_server_b3 = {};
                List<Object> pm_distributed_server_b3List = new ArrayList<Object>();
                pm_distributed_server_b3List.add(pm_distributed_server_b3);
                pm_distributed_server_b3List.add("pm_distributed_server_b3");

                String[] pm_node_server_b4 = {};
                List<Object> pm_node_server_b4List = new ArrayList<Object>();
                pm_node_server_b4List.add(pm_node_server_b4);
                pm_node_server_b4List.add("pm_node_server_b4");

                String[] pm_cache_server_s1 = {};
                List<Object> pm_cache_server_s1List = new ArrayList<Object>();
                pm_cache_server_s1List.add(pm_cache_server_s1);
                pm_cache_server_s1List.add("pm_cache_server_s1");

                String[] pm_storage_server_s3 = {};
                List<Object> pm_storage_server_s3List = new ArrayList<Object>();
                pm_storage_server_s3List.add(pm_storage_server_s3);
                pm_storage_server_s3List.add("pm_storage_server_s3");

                String[] pm_vedio_server_s4 = {};
                List<Object> pm_vedio_server_s4List = new ArrayList<Object>();
                pm_vedio_server_s4List.add(pm_vedio_server_s4);
                pm_vedio_server_s4List.add("pm_vedio_server_s4");

                String[] pm_virtual_server_c1 = {};
                List<Object> pm_virtual_server_c1List = new ArrayList<Object>();
                pm_virtual_server_c1List.add(pm_virtual_server_c1);
                pm_virtual_server_c1List.add("pm_virtual_server_c1");

                String[] pm_high_app_server_c3 = {};
                List<Object> pm_high_app_server_c3List = new ArrayList<Object>();
                pm_high_app_server_c3List.add(pm_high_app_server_c3);
                pm_high_app_server_c3List.add("pm_high_app_server_c3");

                String[] pm_high_app_server_c4 = {};
                List<Object> pm_high_app_server_c4List = new ArrayList<Object>();
                pm_high_app_server_c4List.add(pm_high_app_server_c4);
                pm_high_app_server_c4List.add("pm_high_app_server_c4");

                String[] pm_other_server = {};
                List<Object> pm_other_serverList = new ArrayList<Object>();
                pm_other_serverList.add(pm_other_server);
                pm_other_serverList.add("pm_other_server");

                String[] storage_distributd_file_capacity = {};
                List<Object> storage_distributd_file_capacityList = new ArrayList<Object>();
                storage_distributd_file_capacityList.add(storage_distributd_file_capacity);
                storage_distributd_file_capacityList.add("storage_distributd_file_capacity");

                String[] storage_distributd_file_performance = {};
                List<Object> storage_distributd_file_performanceList = new ArrayList<Object>();
                storage_distributd_file_performanceList.add(storage_distributd_file_performance);
                storage_distributd_file_performanceList.add("storage_distributd_file_performance");

                String[] storage_distributd_block_capacity = {};
                List<Object> storage_distributd_block_capacityList = new ArrayList<Object>();
                storage_distributd_block_capacityList.add(storage_distributd_block_capacity);
                storage_distributd_block_capacityList.add("storage_distributd_block_capacity");

                String[] storage_distributd_block_performance = {};
                List<Object> storage_distributd_block_performanceList = new ArrayList<Object>();
                storage_distributd_block_performanceList.add(storage_distributd_block_performance);
                storage_distributd_block_performanceList.add("storage_distributd_block_performance");

                String[] storage_object_capacity = {};
                List<Object> storage_object_capacityList = new ArrayList<Object>();
                storage_object_capacityList.add(storage_object_capacity);
                storage_object_capacityList.add("storage_object_capacity");

                String[] storage_fc_san_capacity = {};
                List<Object> storage_fc_san_capacityList = new ArrayList<Object>();
                storage_fc_san_capacityList.add(storage_fc_san_capacity);
                storage_fc_san_capacityList.add("storage_fc_san_capacity");

                String[] storage_fc_san_raid = {};
                List<Object> storage_fc_san_raidList = new ArrayList<Object>();
                storage_fc_san_raidList.add(storage_fc_san_raid);
                storage_fc_san_raidList.add("storage_fc_san_raid");

                String[] storage_back_up = {};
                List<Object> storage_back_upList = new ArrayList<Object>();
                storage_back_upList.add(storage_back_up);
                storage_back_upList.add("storage_back_up");

                String[] storage_nas = {};
                List<Object> storage_nasList = new ArrayList<Object>();
                storage_nasList.add(storage_nas);
                storage_nasList.add("storage_nas");

                String[] load_balancer_low = {};
                List<Object> load_balancer_lowList = new ArrayList<Object>();
                load_balancer_lowList.add(load_balancer_low);
                load_balancer_lowList.add("load_balancer_low");

                String[] load_balancer_middle = {};
                List<Object> load_balancer_middleList = new ArrayList<Object>();
                load_balancer_middleList.add(load_balancer_middle);
                load_balancer_middleList.add("load_balancer_middle");

                String[] load_balancer_high = {};
                List<Object> load_balancer_highList = new ArrayList<Object>();
                load_balancer_highList.add(load_balancer_high);
                load_balancer_highList.add("load_balancer_high");

                String[] firewall = {};
                List<Object> firewallList = new ArrayList<Object>();
                firewallList.add(firewall);
                firewallList.add("firewall");

                String[] switch_infiniband_gateway = {};
                List<Object> switch_infiniband_gatewayList = new ArrayList<Object>();
                switch_infiniband_gatewayList.add(switch_infiniband_gateway);
                switch_infiniband_gatewayList.add("switch_infiniband_gateway");

				/*
				 * 转资成本,单价,按比例分摊日期,使用年限
				 * switch_infiniband_conerge,bandwidth_gbps_cmnet,special_requirement,
				 * special_line
				 */
                String[] switch_infiniband_conerge = {};
                List<Object> switch_infiniband_conergeList = new ArrayList<Object>();
                switch_infiniband_conergeList.add(switch_infiniband_conerge);
                switch_infiniband_conergeList.add("switch_infiniband_conerge");

                String[] bandwidth_gbps_cmnet = {};
                List<Object> bandwidth_gbps_cmnetList = new ArrayList<Object>();
                bandwidth_gbps_cmnetList.add(bandwidth_gbps_cmnet);
                bandwidth_gbps_cmnetList.add("bandwidth_gbps_cmnet");

                String[] bandwidth_gbps_ip = {};
                List<Object> bandwidth_gbps_ipList = new ArrayList<Object>();
                bandwidth_gbps_ipList.add(bandwidth_gbps_ip);
                bandwidth_gbps_ipList.add("bandwidth_gbps_ip");

                String[] special_line = {};
                List<Object> special_lineList = new ArrayList<Object>();
                special_lineList.add(special_line);
                special_lineList.add("special_line");

                String[] bandwidth_gbps_special_line = {};
                List<Object> bandwidth_gbps_special_lineList = new ArrayList<Object>();
                bandwidth_gbps_special_lineList.add(bandwidth_gbps_special_line);
                bandwidth_gbps_special_lineList.add("bandwidth_gbps_special_line");

				/*
				 * 转资成本,单价,按比例分摊日期,使用年限
				 * resource_date,resource_date,special_requirement,create_time
				 */
                String[] other_resource = {};
                List<Object> other_resourceList = new ArrayList<Object>();
                other_resourceList.add(other_resource);
                other_resourceList.add("other_resource");

                String[] resource_date = {};
                List<Object> resource_dateList = new ArrayList<Object>();
                resource_dateList.add(resource_date);
                resource_dateList.add("resource_date");

                String[] special_requirement = {};
                List<Object> special_requirementList = new ArrayList<Object>();
                special_requirementList.add(special_requirement);
                special_requirementList.add("special_requirement");

                String[] contact = {};
                List<Object> contactList = new ArrayList<Object>();
                contactList.add(contact);
                contactList.add("contact");

                String[] remark = {};
                List<Object> remarkList = new ArrayList<Object>();
                remarkList.add(remark);
                remarkList.add("remark");

                String[] phone = {};
                List<Object> phoneList = new ArrayList<Object>();
                phoneList.add(phone);
                phoneList.add("phone");

                String[] create_time = {};
                List<Object> create_timeList = new ArrayList<Object>();
                create_timeList.add(create_time);
                create_timeList.add("create_time");

				/* 第三步 */
                List<List<Object>> result = new ArrayList<>();

                result.add(resource_poolList);// 17-47没有下拉
                result.add(company_nameList);
                result.add(departmentList);
                result.add(business_nameList);
                result.add(vm_kernel_numList);
                result.add(vm_momeryList);
                result.add(vm_system_diskList);
                result.add(pm_analytical_server_b1List);
                result.add(pm_distributed_server_b2List);
                result.add(pm_distributed_server_b3List);
                result.add(pm_node_server_b4List);
                result.add(pm_cache_server_s1List);
                result.add(pm_storage_server_s3List);
                result.add(pm_vedio_server_s4List);
                result.add(pm_virtual_server_c1List);
                result.add(pm_high_app_server_c3List);
                result.add(pm_high_app_server_c4List);
                result.add(pm_other_serverList);
                result.add(storage_distributd_file_capacityList);
                result.add(storage_distributd_file_performanceList);
                result.add(storage_distributd_block_capacityList);
                result.add(storage_distributd_block_performanceList);
                result.add(storage_object_capacityList);
                result.add(storage_fc_san_capacityList);
                result.add(storage_fc_san_raidList);
                result.add(storage_back_upList);
                result.add(storage_nasList);
                result.add(load_balancer_lowList);
                result.add(load_balancer_middleList);
                result.add(load_balancer_highList);
                result.add(firewallList);
                result.add(switch_infiniband_gatewayList);// 47

                result.add(switch_infiniband_conergeList);
                result.add(bandwidth_gbps_cmnetList);
                result.add(bandwidth_gbps_ipList);
                result.add(special_lineList);// 51

                result.add(bandwidth_gbps_special_lineList);
                result.add(remarkList);// 47

                result.add(other_resourceList);
                result.add(resource_dateList);
                result.add(special_requirementList);
                result.add(contactList);// 51
                result.add(phoneList);
                result.add(create_timeList);// 51

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
                    poiCascade.resourceUtilss(wb, result, headerList, title);
                    wb.write(os);

                } catch (Exception e) {
                    logger.error("导出excel失败", e);
                } finally {
                    os.flush();
                    os.close();
                }
            } else {
                throw new NullPointerException("导出excel失败，excel文件不能为空");
            }
        } catch (Exception e) {
            logger.error("导出excel,查询数据失败", e);
        }
    }

    /**
     * 上传并预览Excel
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/GetExcelData", method = RequestMethod.POST)
    @ApiOperation(value = "解析上传的Excel数据并返回结果", notes = "解析上传的Excel数据并返回结果")
    public Map<String, Object> getExcelData(HttpServletRequest request, HttpServletResponse response) {
        ResourceEstimateResponse dataGrid = new ResourceEstimateResponse();
        List<ResourceDemandColletExcel> resourceDemandColletExcelList;
//        LogWatch logWatch = new LogWatch();
//        logWatch.start();
//        String flag = LogWatch.FLAG_SUCCESS;
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
        ExcelReaderUtils excelReader = new ExcelReaderUtils();
        try {
            FileUtils.writeByteArrayToFile(targetFile, file.getBytes());// 服务器中生成文件
            // 上传数据到数据库
            resourceDemandColletExcelList = excelReader.doUploadResourceCollectData(targetFile);
            // 数据再处理 把中文字段转相应的ID字段存到对象中
            // business_level device_maintenance deviceType deviceOsType deviceModel
            // deviceClass 新增属性给cmdbHostAssetsExcelData对

            // 删除上传数据
            targetFile.delete();
            dataGrid.setRows(resourceDemandColletExcelList);
            if (CollectionUtils.isEmpty(resourceDemandColletExcelList)) {
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
            }else {
                message = e.getMessage();
            }
            result = Boolean.FALSE;
//            flag = LogWatch.FLAG_FAILURE;
        }
        String modules = ModuleUtils.getModule().get("resouceCollectExcelData_import");
        StringBuilder content = new StringBuilder();
        content.append("执行操作：" + modules + "<br>");
//        logWatch.end(modules, content.toString(), LogWatch.LOG_TYPE_OPERATION, flag);
        Map<String, Object> hashMap = new HashMap<>();
        hashMap.put("message", message);
        hashMap.put("result", result);
        hashMap.put("DataGrid", dataGrid);
        return hashMap;
    }

    @RequestMapping(value = "/saveExcelData", method = RequestMethod.POST)
    @Transactional
    @ApiOperation(value = "保存导入的Excel数据", notes = "保存导入的Excel数据")
    public HashMap<String, Object> saveExcelData(@RequestParam(value = "entities") String entities) {
        HashMap<String, Object> map = new HashMap<>();
        entities = entities.replace("}{", "},{");
        entities = "[" + entities.toString() + "]";
        List<ResourceDemandColletExcel> resourceDemandCollectExcelList = JSONArray.parseArray(entities, ResourceDemandColletExcel.class);
        List<ResourceDemandCollect> resourceDemandCollectList = new ArrayList<ResourceDemandCollect>();
        ResourceDemandCollect rdc;
        for (ResourceDemandColletExcel excel : resourceDemandCollectExcelList) {
            rdc = new ResourceDemandCollect();
            if (StringUtils.isNotEmpty(excel.getPrimary_department())) {
                rdc.setPrimary_department(excel.getPrimary_department());
            }
            if (StringUtils.isNotEmpty(excel.getSecondary_department())) {
                rdc.setSecondary_department(excel.getSecondary_department());
            }
            if (StringUtils.isNotEmpty(excel.getApp_system())) {
                rdc.setApp_system(excel.getApp_system());
            }
            if (StringUtils.isNotEmpty(excel.getIs_project())) {
                rdc.setIs_project(excel.getIs_project());
            }
            if (StringUtils.isNotEmpty(excel.getProject_time())) {
                rdc.setProject_time(excel.getProject_time());
            }
            if (StringUtils.isNotEmpty(excel.getResource_put_date())) {
                rdc.setResource_put_date(excel.getResource_put_date());
            }
            if (StringUtils.isNotEmpty(excel.getResource_produce_date())) {
                rdc.setResource_produce_date(excel.getResource_produce_date());
            }
            if (StringUtils.isNotEmpty(excel.getSystem_description())) {
                rdc.setSystem_description(excel.getSystem_description());
            }
            if (StringUtils.isNotEmpty(excel.getVm_model1())) {
                rdc.setVm_model1(Integer.parseInt(excel.getVm_model1()));
            }
            if (StringUtils.isNotEmpty(excel.getVm_model2())) {
                rdc.setVm_model2(Integer.parseInt(excel.getVm_model2()));
            }
            if (StringUtils.isNotEmpty(excel.getVm_model3())) {
                rdc.setVm_model3(Integer.parseInt(excel.getVm_model3()));
            }
            if (StringUtils.isNotEmpty(excel.getVm_model4())) {
                rdc.setVm_model4(Integer.parseInt(excel.getVm_model4()));
            }
            if (StringUtils.isNotEmpty(excel.getVm_model5())) {
                rdc.setVm_model5(Integer.parseInt(excel.getVm_model5()));
            }
            if (StringUtils.isNotEmpty(excel.getVm_model6())) {
                rdc.setVm_model6(Integer.parseInt(excel.getVm_model6()));
            }
            if (StringUtils.isNotEmpty(excel.getVm_model7())) {
                rdc.setVm_model7(Integer.parseInt(excel.getVm_model7()));
            }
            if (StringUtils.isNotEmpty(excel.getVm_model8())) {
                rdc.setVm_model8(Integer.parseInt(excel.getVm_model8()));
            }
            if (StringUtils.isNotEmpty(excel.getPm_low_app_server())) {
                rdc.setPm_low_app_server(Integer.parseInt(excel.getPm_low_app_server()));
            }
            if (StringUtils.isNotEmpty(excel.getPm_analytical_server())) {
                rdc.setPm_analytical_server(Integer.parseInt(excel.getPm_analytical_server()));
            }
            if (StringUtils.isNotEmpty(excel.getPm_distributed_server())) {
                rdc.setPm_distributed_server(Integer.parseInt(excel.getPm_distributed_server()));
            }
            if (StringUtils.isNotEmpty(excel.getPm_cache_server())) {
                rdc.setPm_cache_server(Integer.parseInt(excel.getPm_cache_server()));
            }
            if (StringUtils.isNotEmpty(excel.getPm_high_app_server())) {
                rdc.setPm_high_app_server(Integer.parseInt(excel.getPm_high_app_server()));
            }
            if (StringUtils.isNotEmpty(excel.getStorage_distributd_file_capacity())) {
                rdc.setStorage_distributd_file_capacity(Double.valueOf(excel.getStorage_distributd_file_capacity()));
            }
            if (StringUtils.isNotEmpty(excel.getStorage_distributd_block_performance())) {
                rdc.setStorage_distributd_block_performance(Double.valueOf(excel.getStorage_distributd_block_performance()));
            }
            if (StringUtils.isNotEmpty(excel.getStorage_object_capacity())) {
                rdc.setStorage_object_capacity(Double.valueOf(excel.getStorage_object_capacity()));
            }
            if (StringUtils.isNotEmpty(excel.getStorage_fc_san_capacity())) {
                rdc.setStorage_fc_san_capacity(Double.valueOf(excel.getStorage_fc_san_capacity()));
            }
            if (StringUtils.isNotEmpty(excel.getStorage_ip_san_capacity())) {
                rdc.setStorage_ip_san_capacity(Double.valueOf(excel.getStorage_ip_san_capacity()));
            }
            if (StringUtils.isNotEmpty(excel.getStorage_fc_san_naked())) {
                rdc.setStorage_fc_san_naked(Double.valueOf(excel.getStorage_fc_san_naked()));
            }
            if (StringUtils.isNotEmpty(excel.getStorage_ip_san_naked())) {
                rdc.setStorage_ip_san_naked(Double.valueOf(excel.getStorage_ip_san_naked()));
            }
            if (StringUtils.isNotEmpty(excel.getStorage_back_up())) {
                rdc.setStorage_back_up(Double.valueOf(excel.getStorage_back_up()));
            }
            if (StringUtils.isNotEmpty(excel.getCmnet_address())) {
                rdc.setCmnet_address(Integer.parseInt(excel.getCmnet_address()));
            }
            if (StringUtils.isNotEmpty(excel.getBandwidth_gbps_cmnet())){
                rdc.setBandwidth_gbps_cmnet(Double.valueOf(excel.getBandwidth_gbps_cmnet()));
            }
            if (StringUtils.isNotEmpty(excel.getIp_address())) {
                rdc.setIp_address(Integer.parseInt(excel.getIp_address()));
            }
            if (StringUtils.isNotEmpty(excel.getBandwidth_gbps_ip())){
                rdc.setBandwidth_gbps_ip(Double.valueOf(excel.getBandwidth_gbps_ip()));
            }
            if (StringUtils.isNotEmpty(excel.getDb_mysql())) {
                rdc.setDb_mysql(Integer.parseInt(excel.getDb_mysql()));
            }
            if (StringUtils.isNotEmpty(excel.getDb_mongodb())) {
                rdc.setDb_mongodb(Integer.parseInt(excel.getDb_mongodb()));
            }
            if (StringUtils.isNotEmpty(excel.getDb_postgresql())) {
                rdc.setDb_postgresql(Integer.parseInt(excel.getDb_postgresql()));
            }
            if (StringUtils.isNotEmpty(excel.getDb_memory())) {
                rdc.setDb_memory(Integer.parseInt(excel.getDb_memory()));
            }
            if (StringUtils.isNotEmpty(excel.getDb_other())) {
                rdc.setDb_other(Integer.parseInt(excel.getDb_other()));
            }
            if (StringUtils.isNotEmpty(excel.getCache_redis())) {
                rdc.setCache_redis(Integer.parseInt(excel.getCache_redis()));
            }
            if (StringUtils.isNotEmpty(excel.getCache_memcached())) {
                rdc.setCache_memcached(Integer.parseInt(excel.getCache_memcached()));
            }
            if (StringUtils.isNotEmpty(excel.getInfo_middleware_activemq())) {
                rdc.setInfo_middleware_activemq(Integer.parseInt(excel.getInfo_middleware_activemq()));
            }
            if (StringUtils.isNotEmpty(excel.getInfo_middleware_kafka())) {
                rdc.setInfo_middleware_kafka(Integer.parseInt(excel.getInfo_middleware_kafka()));
            }
            if (StringUtils.isNotEmpty(excel.getInfo_middleware_rocketmq())) {
                rdc.setInfo_middleware_rocketmq(Integer.parseInt(excel.getInfo_middleware_rocketmq()));
            }
            if (StringUtils.isNotEmpty(excel.getApp_middleware_apache())) {
                rdc.setApp_middleware_apache(Integer.parseInt(excel.getApp_middleware_apache()));
            }
            if (StringUtils.isNotEmpty(excel.getApp_middleware_jboos())) {
                rdc.setApp_middleware_jboos(Integer.parseInt(excel.getApp_middleware_jboos()));
            }
            if (StringUtils.isNotEmpty(excel.getApp_middleware_tomcat())) {
                rdc.setApp_middleware_tomcat(Integer.parseInt(excel.getApp_middleware_tomcat()));
            }
            if (StringUtils.isNotEmpty(excel.getBalancer_nginx())) {
                rdc.setBalancer_nginx(Integer.parseInt(excel.getBalancer_nginx()));
            }
            if (StringUtils.isNotEmpty(excel.getBalancer_haproxy())) {
                rdc.setBalancer_haproxy(Integer.parseInt(excel.getBalancer_haproxy()));
            }
            if (StringUtils.isNotEmpty(excel.getServer_zookeeper())) {
                rdc.setServer_zookeeper(Integer.parseInt(excel.getServer_zookeeper()));
            }
            if (StringUtils.isNotEmpty(excel.getServer_etcd())) {
                rdc.setServer_etcd(Integer.parseInt(excel.getServer_etcd()));
            }
            if (StringUtils.isNotEmpty(excel.getSearch_middleware_es())) {
                rdc.setSearch_middleware_es(Integer.parseInt(excel.getSearch_middleware_es()));
            }
            if (StringUtils.isNotEmpty(excel.getServer_docker_registry())) {
                rdc.setServer_docker_registry(Integer.parseInt(excel.getServer_docker_registry()));
            }
            if (StringUtils.isNotEmpty(excel.getStream_jbpm())) {
                rdc.setStream_jbpm(Integer.parseInt(excel.getStream_jbpm()));

            }
            if (StringUtils.isNotEmpty(excel.getStream_activity())) {
                rdc.setStream_activity(Integer.parseInt(excel.getStream_activity()));
            }
            if (StringUtils.isNotEmpty(excel.getImage_openjdk())) {
                rdc.setImage_openjdk(Integer.parseInt(excel.getImage_openjdk()));
            }
            if (StringUtils.isNotEmpty(excel.getImage_python())) {
                rdc.setImage_python(Integer.parseInt(excel.getImage_python()));
            }
            if (StringUtils.isNotEmpty(excel.getImage_go())) {
                rdc.setImage_go(Integer.parseInt(excel.getImage_go()));
            }
            if (StringUtils.isNotEmpty(excel.getImage_nodejs())) {
                rdc.setImage_nodejs(Integer.parseInt(excel.getImage_nodejs()));
            }
            if (StringUtils.isNotEmpty(excel.getImage_ruby())) {
                rdc.setImage_ruby(Integer.parseInt(excel.getImage_ruby()));
            }
            if (StringUtils.isNotEmpty(excel.getImage_net())) {
                rdc.setImage_net(Integer.parseInt(excel.getImage_net()));
            }
            if (StringUtils.isNotEmpty(excel.getCicd_jenkins())) {
                rdc.setCicd_jenkins(Integer.parseInt(excel.getCicd_jenkins()));
            }
            if (StringUtils.isNotEmpty(excel.getFramework_springcloud())) {
                rdc.setFramework_springcloud(Integer.parseInt(excel.getFramework_springcloud()));
            }
            if (StringUtils.isNotEmpty(excel.getFramework_other())) {
                rdc.setFramework_other(Integer.parseInt(excel.getFramework_other()));
            }
            if (StringUtils.isNotEmpty(excel.getSpecial_requirement())) {
                rdc.setSpecial_requirement(excel.getSpecial_requirement());
            }
            if (StringUtils.isNotEmpty(excel.getAddress_requirement())) {
                rdc.setAddress_requirement(excel.getAddress_requirement());
            }
            if (StringUtils.isNotEmpty(excel.getDepartment_head())) {
                rdc.setDepartment_head(excel.getDepartment_head());
            }
            if  (StringUtils.isNotEmpty(excel.getInter_contact())) {
                rdc.setInter_contact(excel.getInter_contact());
            }
            if (StringUtils.isNotEmpty(excel.getPhone())) {
                rdc.setPhone(excel.getPhone());
            }
            if (StringUtils.isNotEmpty(excel.getEmail())) {
                rdc.setEmail(excel.getEmail());
            }
            resourceDemandCollectList.add(rdc);
        }
        if (CollectionUtils.isEmpty(resourceDemandCollectList)) {
            map.put("result", "数据为空,保存失败");
        } else {
            try {
                resourceDemandCollectService.saveResourceCollectExcelData(resourceDemandCollectList);
                map.put("result", "保存成功");
            } catch (Exception e) {
                logger.error("高级维保导入数据失败", e);
                map.put("result", "保存失败");
            }
        }
        return map;
    }

    @RequestMapping(value = "/resourceCollectExportGridData", method = RequestMethod.POST)
    @ApiOperation(value = "导出资源收集数据", notes = "导出资源收集数据")
    public void resourceCollectExportGridData(@RequestParam Map<String, Object> params, HttpServletResponse response) {
        if (CollectionUtils.isEmpty(params)) {
            logger.error("导出参数为空!");
            return;
        }
        String[] keyList = params.get("keys")==null?null:params.get("keys").toString().split(",");
        String[] headerList = params.get("headers")==null?null:params.get("headers").toString().split(",");
        params.remove("keyList");
        params.remove("headerList");
        String [] whereStr = params.get("list")==null?null:params.get("list").toString().split(",");
        if(whereStr!=null) {
            String listStr = "(";
            for (int a = 0; a < whereStr.length; a++) {
                if(a<whereStr.length-1) {
                    listStr = listStr+"'"+whereStr[a]+"',";
                }else {
                    listStr = listStr+"'"+whereStr[a]+"')";
                }
            }
            params.put("list", listStr);
        }
        //权限标识相关

        String primaryDepartment = params.get("primaryDepartment") == null ? "" : String.valueOf(params.get("primaryDepartment"));
        String secondaryDepartment = params.get("secondaryDepartment") == null ? "" : String.valueOf(params.get("secondaryDepartment"));
        String appSystem = params.get("appSystem") == null ? "" : String.valueOf(params.get("appSystem"));

        ResourceBuildManageQueryEntity paramers = new ResourceBuildManageQueryEntity();
        paramers.setPrimaryDepartment(primaryDepartment);
        paramers.setSecondaryDepartment(secondaryDepartment);
        paramers.setAppSystem(appSystem);
        List<Map<String, Object>> dataLists = resourceDemandCollectService.getResourceDemandCollectList(paramers);
        logger.info("exportGridData params:"+params);
        OutputStream os = null;
        try {
            os = response.getOutputStream();// 取得输出流
            String title = params.get("title").toString();
            String fileName = title+".xlsx";
            response.setHeader("Content-Disposition", "attachment;filename=".concat(String.valueOf(URLEncoder.encode(fileName, "UTF-8"))));
            response.setHeader("Connection", "close");
            response.setHeader("Content-Type", "application/vnd.ms-excel");
            //excel constuct
            ExportExcelUtils eeu = new ExportExcelUtils();
            Workbook book = new SXSSFWorkbook(128);
            eeu.exportExcel(book, 0, title, headerList, dataLists, keyList);
            book.write(os);
//                flag= LogWatch.FLAG_SUCCESS;
        } catch (Exception e) {
            logger.error("导出excel,查询数据失败", e);
        } finally {
            if (os != null) {
                try {
                    os.flush();
                    os.close();
                } catch (IOException e) {
                    logger.error("关闭输出流失败", e);
                }
            }
        }
    }

    @RequestMapping(value = "/insertResourceCollect", method = RequestMethod.POST)
    @ApiOperation(value = "新增资源收集数据", notes = "新增资源收集数据")
    public Map<String, Object> insertResourceCollect(HttpServletResponse response, @RequestBody ResourceDemandCollect demandCollect) {
        Map<String, Object> returnMap = new HashMap<>();
        returnMap.put("code", "success");
        returnMap.put("msg", "");
        try {
            List<ResourceDemandCollect> resourceDemandCollectList = new ArrayList<>();
            resourceDemandCollectList.add(demandCollect);
            resourceDemandCollectService.saveResourceCollectExcelData(resourceDemandCollectList);
        } catch (Exception e) {
            returnMap.put("code", "error");
            returnMap.put("msg", e.getMessage());
            logger.error("新增资源收集数据错误,{}", e);
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return returnMap;
    }

}
