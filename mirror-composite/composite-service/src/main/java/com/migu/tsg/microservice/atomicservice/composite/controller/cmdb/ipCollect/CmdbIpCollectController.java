package com.migu.tsg.microservice.atomicservice.composite.controller.cmdb.ipCollect;

import com.aspire.mirror.composite.service.cmdb.ipCollect.IIpCollectAPI;
import com.aspire.ums.cmdb.common.Result;
import com.aspire.ums.cmdb.common.ResultVo;
import com.aspire.ums.cmdb.ipCollect.payload.entity.*;
import com.google.common.collect.Lists;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.ipCollect.CmdbIpCollectClient;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.ExportExcelUtil;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.JavaBeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

/**
 * @Author: fanshenquan
 * @Datetime: 2020/5/20 16:57
 */
@Slf4j
@RestController
public class CmdbIpCollectController implements IIpCollectAPI {

    @Autowired
    private CmdbIpCollectClient cmdbIpCollectClient;


    @Override
    public CmdbIpCollectResult findPageS(@RequestBody CmdbIpCollectRequest cmdbIpCollectRequest) {
        return cmdbIpCollectClient.findPageS(cmdbIpCollectRequest);
    }

    @Override
    public ResultVo getResourceS() {
        return cmdbIpCollectClient.getResourceS();
    }

    @Override
    public ResultVo getSourceS() {
        return cmdbIpCollectClient.getSourceS();
    }

    @Override
    public void exportS(@RequestBody CmdbIpCollectRequest cmdbIpCollectRequest, HttpServletResponse response) {
        log.info("cmdbIpCollectRequest is {}", cmdbIpCollectRequest);
        String[] headerList = {"\t\t\t检测时间\t\t\t", "\t\t\t\t\tIP\t\t\t\t\t", "\t\tMAC地址\t\t", "IP类型", "\t\t数据来源\t\t", "网关设备IP", "\t所属资源池\t"};
        String[] keyList = {"time", "ip", "mac", "iptype", "source", "gateway", "resource"};
        String title = "存活IP列表";
        String fileName = title + ".xlsx";

        OutputStream os = null;
        List<Map<String, Object>> dataLists = Lists.newArrayList();
        try {
            ResultVo<List<CmdbIpCollectResponse>> result = cmdbIpCollectClient.findListS(cmdbIpCollectRequest);
            List<CmdbIpCollectResponse> entityList = result.getData();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            for (CmdbIpCollectResponse entity : entityList) {
                Map<String, Object> map = JavaBeanUtil.convertBeanToMap(entity);
                map.put("time", sdf.format(entity.getTime()));
                dataLists.add(map);
            }

            os = response.getOutputStream();// 取得输出流
            response.setHeader("Content-Disposition",
                    "attachment;filename=".concat(String.valueOf(URLEncoder.encode(fileName, "UTF-8"))));
            response.setHeader("Connection", "close");
            response.setContentType("application/vnd.ms-excel");
            // excel constuct
            ExportExcelUtil eeu = new ExportExcelUtil();
            Workbook book = new SXSSFWorkbook(128);
            eeu.exportExcel(book, 0, title, headerList, dataLists, keyList);
            book.write(os);
            os.flush();
            log.info("导出/生成文件: {} 成功!", fileName);
        } catch (Exception e) {
            log.error("导出Excel数据失败!", e);
        } finally {
            IOUtils.closeQuietly(os);
        }
    }

    @Override
    public Result<CmdbVipCollectEntity> findPageF(@RequestBody CmdbVipCollectRequest cmdbVipCollectRequest) {
        return cmdbIpCollectClient.findPageF(cmdbVipCollectRequest);
    }

    @Override
    public ResultVo getResourceF() {
        return cmdbIpCollectClient.getResourceF();
    }

    @Override
    public ResultVo getUserTypeF() {
        return cmdbIpCollectClient.getUserTypeF();
    }

    @Override
    public void exportF(@RequestBody CmdbVipCollectRequest cmdbVipCollectRequest, HttpServletResponse response) {
        log.info("cmdbVipCollectRequest is {}", cmdbVipCollectRequest);
        String[] headerList = {"\t\t\t检测时间\t\t\t", "\t\t\t虚拟IP\t\t\t", "\t当前绑定IP\t", "\t漂移IP列表\t", "虚拟IP使用类型", "\t所属资源池\t"};
        String[] keyList = {"time", "vip", "bindip", "iplist", "usetype", "resource"};
        String title = "虚拟IP列表";
        String fileName = title + ".xlsx";

        OutputStream os = null;
        List<Map<String, Object>> dataLists = Lists.newArrayList();
        try {
            ResultVo<List<CmdbVipCollectEntity>> result = cmdbIpCollectClient.findListF(cmdbVipCollectRequest);
            List<CmdbVipCollectEntity> entityList = result.getData();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            for (CmdbVipCollectEntity entity : entityList) {
                Map<String, Object> map = JavaBeanUtil.convertBeanToMap(entity);
                map.put("time", sdf.format(entity.getTime()));
                dataLists.add(map);
            }

            os = response.getOutputStream();// 取得输出流
            response.setHeader("Content-Disposition",
                    "attachment;filename=".concat(String.valueOf(URLEncoder.encode(fileName, "UTF-8"))));
            response.setHeader("Connection", "close");
            response.setContentType("application/vnd.ms-excel");
            // excel constuct
            ExportExcelUtil eeu = new ExportExcelUtil();
            Workbook book = new SXSSFWorkbook(128);
            eeu.exportExcel(book, 0, title, headerList, dataLists, keyList);
            book.write(os);
            os.flush();
            log.info("导出/生成文件: {} 成功!", fileName);
        } catch (Exception e) {
            log.error("导出Excel数据失败!", e);
        } finally {
            IOUtils.closeQuietly(os);
        }
    }
}
