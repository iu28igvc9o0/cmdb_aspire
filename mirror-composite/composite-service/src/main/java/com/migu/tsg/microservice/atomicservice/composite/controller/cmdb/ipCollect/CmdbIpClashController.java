package com.migu.tsg.microservice.atomicservice.composite.controller.cmdb.ipCollect;

import com.aspire.mirror.composite.service.cmdb.ipCollect.IIpClashAPI;
import com.aspire.ums.cmdb.common.ResultVo;
import com.aspire.ums.cmdb.ipCollect.payload.entity.CmdbIpClashFindPageRequest;
import com.aspire.ums.cmdb.ipCollect.payload.entity.CmdbIpClashFindPageResponse;
import com.aspire.ums.cmdb.ipCollect.payload.entity.CmdbIpClashResult;
import com.aspire.ums.cmdb.ipCollect.payload.entity.CmdbIpClashUpdateRequest;
import com.google.common.collect.Lists;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.ipCollect.CmdbIpClashClient;
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
 * @Datetime: 2020/5/28 11:38
 */
@Slf4j
@RestController
public class CmdbIpClashController implements IIpClashAPI {

    @Autowired
    private CmdbIpClashClient cmdbIpClashClient;

    @Override
    public CmdbIpClashResult findPage(@RequestBody CmdbIpClashFindPageRequest request) {
        return cmdbIpClashClient.findPage(request);
    }

    @Override
    public ResultVo updateHandleStatus(@RequestBody CmdbIpClashUpdateRequest request) {
        return cmdbIpClashClient.updateHandleStatus(request);
    }

    @Override
    public void export(@RequestBody CmdbIpClashFindPageRequest cmdbIpClashFindPageRequest, HttpServletResponse response) {
        log.info("cmdbIpClashFindPageRequest is {}", cmdbIpClashFindPageRequest);
        String[] headerList = {"\t\t\t检测时间\t\t\t", "\t\t\t\t冲突IP\t\t\t\t", "绑定MAC地址", "累计切换次数",
                "\t系统推测\t", "网关设备IP", "所属资源池", "处理状态", "原因说明",
                "操作人", "\t\t\t操作时间\t\t\t", "是否已通知", "\t\t\t工单号\t\t\t", "\t\t来源\t\t"};
        String[] keyList = {"checkTime", "clashIp", "nowMac", "changeTotal",
                "systemInfer", "gateway", "resource", "handleStatusText", "notHandleReason",
                "operator", "updateTime", "isNotify", "jobNumber", "collectType"};
        String title = "冲突IP列表";
        String fileName = title + ".xlsx";

        OutputStream os = null;
        List<Map<String, Object>> dataLists = Lists.newArrayList();
        try {
            ResultVo<List<CmdbIpClashFindPageResponse>> resultVo = cmdbIpClashClient.findPageList(cmdbIpClashFindPageRequest);
            if (!resultVo.isSuccess()) {
                throw new RuntimeException(resultVo.getMsg());
            }
            List<CmdbIpClashFindPageResponse> entityList = resultVo.getData();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            for (CmdbIpClashFindPageResponse entity : entityList) {
                Map<String, Object> map = JavaBeanUtil.convertBeanToMap(entity);
                map.put("checkTime", sdf.format(entity.getCheckTime()));
                map.put("updateTime", sdf.format(entity.getUpdateTime()));
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
