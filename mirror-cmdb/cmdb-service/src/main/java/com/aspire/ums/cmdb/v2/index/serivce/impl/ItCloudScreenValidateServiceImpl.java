package com.aspire.ums.cmdb.v2.index.serivce.impl;

import com.alibaba.fastjson.JSONObject;
import com.aspire.ums.cmdb.ftp.service.FtpService;
import com.aspire.ums.cmdb.index.payload.*;
import com.aspire.ums.cmdb.util.HttpUtil;
import com.aspire.ums.cmdb.util.UUIDUtil;
import com.aspire.ums.cmdb.v2.index.mapper.ItCloudScreenExportLogoMapper;
import com.aspire.ums.cmdb.v2.index.mapper.ItCloudScreenExportMapper;
import com.aspire.ums.cmdb.v2.index.mapper.ItCloudScreenValidateMapper;
import com.aspire.ums.cmdb.v2.index.serivce.ItCloudScreenValidateService;
import com.aspire.ums.cmdb.v2.index.util.ExportUtil;
import com.aspire.ums.cmdb.v2.index.util.ScreenDataUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.util.*;

/**
 * @ClassName ItCloudScreenValidateServiceImpl
 * @Description 大屏验证服务类
 * @Author luowenbo
 * @Date 2020/5/6 14:30
 * @Version 1.0
 */
@Service
public class ItCloudScreenValidateServiceImpl implements ItCloudScreenValidateService {

    @Autowired
    private ItCloudScreenValidateMapper validateMapper;

    @Autowired
    private ItCloudScreenExportMapper exportMapper;

    @Autowired
    private ItCloudScreenExportLogoMapper exportLogoMapper;

    @Autowired
    private FtpService ftpService;

    @Override
    public void insert(ScreenValidate obj) {
        if(null!=obj) {
            obj.setId(UUIDUtil.getUUID());
            obj.setIsDelete("0");
            validateMapper.insert(obj);
        }
    }

    @Override
    public List<ScreenValidate> listAll() {
        List<ScreenValidate> rsList = validateMapper.listAll();
        // 时间格式转换
        for(ScreenValidate item : rsList) {
            String it = item.getInsertTime();
            item.setInsertTime(it.substring(0,it.lastIndexOf(".")));
        }
        return rsList;
    }

    @Override
    public JSONObject validateIsImport(ItCloudScreenValidateRequest req) {
        JSONObject jsonObject = new JSONObject();
        ScreenValidate sv = new ScreenValidate();
        sv.setSystemTitleId(req.getSystemTitle());
        sv.setValidateType(req.getValidateType());
        sv.setMonthlyTime(req.getMonthlyTime());
        // 需要验证9个数据
        boolean flag = true;
        String[] tableNames = new String[]{"screen_max_utilization","screen_avg_utilization"};
        String[] deviceTypes = new String[]{"物理机","虚拟机"};
        String[] hardwareTypes = new String[]{"CPU","内存"};
        // 验证资源分配总量数据
        req.setTableName("screen_resource_allocation");
        StringBuilder sb = new StringBuilder();
        int one = validateMapper.countListData(req);
        if(one == 0) {
            sb.append("资源分配总量没有导入数据；");
            flag = false;
        }
        // 验证均峰值和均值数据
        for (int k=0;k<tableNames.length;k++) {
            for(int i=0;i<deviceTypes.length;i++) {
                for(int j=0;j<hardwareTypes.length;j++) {
                    req.setTableName(tableNames[k]);
                    req.setDeviceType(deviceTypes[i]);
                    req.setHardwareType(hardwareTypes[j]);
                    int tmpCnt = validateMapper.countListData(req);
                    if(tmpCnt == 0) {
                        String tmpRs = "screen_max_utilization".equals(tableNames[k]) ? "均峰值":"均值";
                        sb.append(tmpRs + "-" + deviceTypes[i] + "-" + hardwareTypes[j] +"没有导入数据；");
                        flag = false;
                    }
                }
            }
        }
        // 如果flag 是true 则表示所有数据都校验通过
        if(flag) {
            sv.setValidateResult( "【"+ req.getMonthlyTime() + "】数据是否导入-校验通过");
            jsonObject.put("flag",true);
            jsonObject.put("msg","校验通过");
        } else {
            sv.setValidateResult( "【"+ req.getMonthlyTime() + "】数据是否导入-校验不通过：" + sb.toString());
            jsonObject.put("flag",false);
            jsonObject.put("msg","校验不通过");
        }
        // 保存校验记录
        insert(sv);
        return jsonObject;
    }

    @Override
    public JSONObject validateIsComplete(ItCloudScreenValidateRequest req) {
        JSONObject jsonObject = new JSONObject();
        ScreenValidate sv = new ScreenValidate();
        sv.setSystemTitleId(req.getSystemTitle());
        sv.setValidateType(req.getValidateType());
        sv.setMonthlyTime(req.getMonthlyTime());
        boolean flag = true;
        String[] deviceTypes = new String[]{"物理机","虚拟机"};
        StringBuilder sb = new StringBuilder();
        for(int i=0;i<deviceTypes.length;i++) {
            req.setDeviceType(deviceTypes[i]);
            // 获取数据完整性数据，如果数据为空，则表示数据完整
            List<Map<String,Object>> data = validateMapper.validateDataIsComplete(req);
            if(null != data && !data.isEmpty()) {
                for(Map<String,Object> item : data) {
                    String trs = "(" + deviceTypes[i] + "-" + item.get("department1")+ "-" + item.get("department2") + "-"
                            + item.get("bizSystem")+ "-" + item.get("resourcePool") + "-" + item.get("pod") + ")";
                    String cpuMaxRs = item.get("cpuMax") == null ? ScreenDataUtil.getValidateMsgByKey("cpuMax") : "";
                    String storeMaxRs = item.get("storeMax") == null ? ScreenDataUtil.getValidateMsgByKey("storeMax") : "";
                    String cpuAvgRs = item.get("cpuAvg") == null ? ScreenDataUtil.getValidateMsgByKey("cpuAvg") : "";
                    String storeAvgRs = item.get("storeAvg") == null ? ScreenDataUtil.getValidateMsgByKey("storeAvg") : "";
                    sb.append(trs+"缺少" + cpuMaxRs + storeMaxRs + cpuAvgRs + storeAvgRs + "数据；");
                }
                flag = false;
            }
        }
        if(flag) {
            sv.setValidateResult( "【"+ req.getMonthlyTime() + "】数据是否完整-校验通过");
            jsonObject.put("flag",true);
            jsonObject.put("msg","校验通过");
        } else {
            jsonObject.put("flag",false);
            jsonObject.put("msg","校验不通过");
            sv.setValidateResult( "【"+ req.getMonthlyTime() + "】数据是否完整-校验不通过：" + sb.toString());
        }
        // 保存校验记录
        insert(sv);
        return jsonObject;
    }

    @Override
    public JSONObject validateDataIsCorrect(ItCloudScreenValidateRequest req) {
        JSONObject jsonObject = new JSONObject();
        ScreenValidate sv = new ScreenValidate();
        sv.setSystemTitleId(req.getSystemTitle());
        sv.setValidateType(req.getValidateType());
        sv.setMonthlyTime(req.getMonthlyTime());
        boolean flag = true;
        String[] deviceTypes = new String[]{"物理机","虚拟机"};
        String[] hardwareTypes = new String[]{"内存","CPU"};
        StringBuilder sb = new StringBuilder();
        for(int i=0;i<deviceTypes.length;i++) {
            for (int j=0;j<hardwareTypes.length;j++) {
                req.setDeviceType(deviceTypes[i]);
                req.setHardwareType(hardwareTypes[j]);
                // 获取数据完整性数据，如果数据为空，则表示数据完整
                List<Map<String,Object>> data = validateMapper.validateDataIsCorrect(req);
                if(null != data && !data.isEmpty()) {
                    for(Map<String,Object> item : data) {
                        String trs = "(" + deviceTypes[i] + "-" + hardwareTypes[j] + "-" + item.get("department1")+ "-" + item.get("department2") + "-"
                                + item.get("bizSystem")+ "-" + item.get("resourcePool") + "-" + item.get("pod") + ")";
                        sb.append(trs + "数据有误；");
                    }
                    flag = false;
                }
            }
        }
        if(flag) {
            sv.setValidateResult( "【"+ req.getMonthlyTime() + "】数据是否准确-校验通过");
            jsonObject.put("flag",true);
            jsonObject.put("msg","校验通过");
        } else {
            jsonObject.put("flag",false);
            jsonObject.put("msg","校验不通过");
            sv.setValidateResult( "【"+ req.getMonthlyTime() + "】数据是否准确-校验不通过：" + sb.toString());
        }
        // 保存校验记录
        insert(sv);
        return jsonObject;
    }

    @Async
    @Override
    public void createExcel(ItCloudScreenExportRequest req) {
        List<ScreenExport> data = exportMapper.listByType(req.getExportType());
        Map<String,Map<String,Object>> rsExcel = new HashMap<>();
        try {
            for(ScreenExport obj : data) {
                Object srcData = null;
                if("GET".equals(obj.getReqMethod())) {
                    String param = ExportUtil.joinParams(obj.getParam(),req);
                    JSONObject header = JSONObject.parseObject(obj.getReqHeader());
                    String URL = "http://" + obj.getIp() + ":" + obj.getPort() + obj.getUrl();
                    srcData = HttpUtil.getMethod(URL,header,param);
                } else {
                    // 封装请求参数
                    JSONObject param = ExportUtil.mergeParams(JSONObject.parseObject(obj.getParam()),req);
                    JSONObject header = JSONObject.parseObject(obj.getReqHeader());
                    String URL = "http://" + obj.getIp() + ":" + obj.getPort() + obj.getUrl();
                    // 请求后拿到的结果
                    srcData = HttpUtil.postMethod(URL,header,param);
                }
                // 1、解析请求返回的结果，统一封装成List<Map<String,String>>
                List<Map<String,String>> unifyData = ExportUtil.transferJSONObject(srcData,obj.getRspFlag(),obj.getRspType(),obj.getRspKey());
                if(unifyData.isEmpty()) {
                    continue;
                }
                // 2、判断是否属于同一个sheet页的数据
                if(rsExcel.containsKey(obj.getSheetName())) {
                    Map<String,Object> crtData = rsExcel.get(obj.getSheetName());
                    List<Map<String,String>> crtMpList = (List<Map<String, String>>) crtData.get("data");
                    crtMpList.addAll(unifyData);
                    crtData.put("data",crtMpList);
                    rsExcel.put(obj.getSheetName(),crtData);
                } else {
                    Map<String,Object> srcMp = new HashMap<>();
                    srcMp.put("excelHeader",obj.getExcelHeader());
                    srcMp.put("data",unifyData);
                    rsExcel.put(obj.getSheetName(),srcMp);
                }
            }
        } catch (Exception e) {
            ScreenExportLogo exportLogo = new ScreenExportLogo();
            exportLogo.setFileUnqiue(ExportUtil.createFileUnqiue(req));
            exportLogo.setStatus("失败");
            e.printStackTrace();
            exportLogo.setRemark(e.getMessage());
            exportLogoMapper.update(exportLogo);
            return;
        }
        // 3、将最终结果传到FTP服务器
        ByteArrayInputStream in = ExportUtil.uploadExcel(rsExcel);
        Map<String,String> resultMp = ftpService.uploadtoFTP("demo.xlsx",in);
        // 4、修改数据库中文件的状态
        if("success".equals(resultMp.get("code"))) {
            System.out.println("文件路径:" + resultMp.get("path"));
            ScreenExportLogo exportLogo = new ScreenExportLogo();
            exportLogo.setFileUnqiue(ExportUtil.createFileUnqiue(req));
            exportLogo.setPath(resultMp.get("path"));
            exportLogo.setStatus("已生成");
            exportLogoMapper.update(exportLogo);
        } else {
            ScreenExportLogo exportLogo = new ScreenExportLogo();
            exportLogo.setFileUnqiue(ExportUtil.createFileUnqiue(req));
            exportLogo.setStatus("失败");
            exportLogo.setRemark(resultMp.get("message"));
            exportLogoMapper.update(exportLogo);
        }
    }

    @Override
    public JSONObject judgeIfHasCreate(ItCloudScreenExportRequest req) {
        JSONObject rs = new JSONObject();
        String fileUnique = ExportUtil.createFileUnqiue(req);
        ScreenExportLogo exportLogo = exportLogoMapper.getOneByFileUnique(fileUnique);
        // 数据库中没有数据，表示可以进行生成
        if(null == exportLogo) {
            rs.put("flag",true);
            rs.put("msg","Excel文件正在生成，请稍等片刻");
        } else {
            String status = exportLogo.getStatus();
            if("生成中".equals(status)){
                rs.put("flag",false);
                rs.put("msg","Excel文件还正在生成中");
            } else if("已生成".equals(status)){
                rs.put("flag",false);
                rs.put("msg","Excel文件已经生成，请点击导出按钮");
            } else {
                rs.put("flag",false);
                rs.put("msg","Excel文件生成失败! 失败原因:" + exportLogo.getRemark());
            }
        }
        return rs;
    }

    @Override
    public JSONObject judgeIfHasExist(ItCloudScreenExportRequest req) {
        JSONObject rs = new JSONObject();
        String fileUnique = ExportUtil.createFileUnqiue(req);
        ScreenExportLogo exportLogo = exportLogoMapper.getOneByFileUnique(fileUnique);
        // 数据库中没有数据，表示可以进行生成
        if(null == exportLogo) {
            rs.put("flag",false);
            rs.put("msg","数据还未生成，请先点击生成按钮");
        } else {
            String status = exportLogo.getStatus();
            if("已生成".equals(status)){
                rs.put("flag",true);
                rs.put("msg",exportLogo.getPath());
            } else if("生成中".equals(status)){
                rs.put("flag",false);
                rs.put("msg","Excel文件还正在生成中");
            } else {
                rs.put("flag",false);
                rs.put("msg","Excel文件生成失败! 失败原因:" + exportLogo.getRemark());
            }
        }
        return rs;
    }

    @Override
    public void createFileUnique(ItCloudScreenExportRequest req) {
        ScreenExportLogo exportLogo = new ScreenExportLogo();
        exportLogo.setFileUnqiue(ExportUtil.createFileUnqiue(req));
        exportLogo.setStatus("生成中");
        exportLogoMapper.insert(exportLogo);
    }
}
