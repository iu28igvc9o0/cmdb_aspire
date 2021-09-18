package com.migu.tsg.microservice.atomicservice.composite.controller.cmdb;

import com.aspire.mirror.composite.service.cmdb.ICompAllocateIpConfigService;
import com.aspire.mirror.composite.service.inspection.payload.CompAllocateIpConfigDetail;
import com.aspire.mirror.composite.service.inspection.payload.CompAllocateIpConfigListReq;
import com.aspire.ums.cmdb.allocate.payload.AllocateIpConfigDetail;
import com.aspire.ums.cmdb.allocate.payload.AllocateIpConfigListReq;
import com.aspire.ums.cmdb.allocate.payload.AllocateIpConfigRes;
import com.aspire.ums.cmdb.allocate.payload.Result;
import com.google.common.collect.Lists;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.CmdbAllocateIpConfigClient;
import com.migu.tsg.microservice.atomicservice.composite.controller.authcontext.RequestAuthContext;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.ExportExcelUtil;
import com.migu.tsg.microservice.atomicservice.composite.helper.CmdbHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import static com.migu.tsg.microservice.atomicservice.composite.controller.util.PayloadParseUtil.jacksonBaseParse;

@RestController
@Slf4j
public class CompAllocateIpConfigController implements ICompAllocateIpConfigService {

    private static final SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

    @Autowired
    private CmdbAllocateIpConfigClient cmdbAllocateIpConfigClient;
    @Autowired
    private CmdbHelper cmdbHelper;

    @Override
    public Object getAllocateIpConfigData(@RequestParam(value = "pageNum",required = false) int pageNum,
                                                                   @RequestParam(value = "startPageNum",required = false) int startPageNum,
                                                                   @RequestParam(value = "pageSize",required = false) int pageSize,
                                                                   @RequestParam(value = "vpnId",required = false) int vpnId,
                                                                   @RequestParam(value = "networkId",required = false) int networkId,
                                                                   @RequestParam(value = "bizSystem",required = false) String bizSystem,
                                                                   @RequestParam(value = "ip",required = false) String ip,
                                                                   @RequestParam(value = "privateIp",required = false) String privateIp,
                                                                   @RequestParam(value = "isAdd",required = false) boolean isAdd) {
        Result<AllocateIpConfigRes> allocateIpConfigData =
                cmdbAllocateIpConfigClient.getAllocateIpConfigData( pageNum, startPageNum, pageSize, vpnId, networkId, bizSystem, ip, privateIp, isAdd );
        List<AllocateIpConfigRes> data = Lists.newLinkedList();
        if (allocateIpConfigData.getCount() > 0) {
            for (AllocateIpConfigRes compAllocateIpConfigRes : allocateIpConfigData.getData()) {
                String bizSys = cmdbHelper.getBizSysName(compAllocateIpConfigRes.getBizSystem());
                compAllocateIpConfigRes.setBizSystem(bizSys);
                data.add(compAllocateIpConfigRes);
            }
        }
        allocateIpConfigData.setData(data);
        return allocateIpConfigData;
    }

    @Override
    public String insertAllocateIpConfig(@RequestBody CompAllocateIpConfigDetail request) {
        AllocateIpConfigDetail allocateIpConfigDetail = jacksonBaseParse(AllocateIpConfigDetail.class, request );
        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        List<AllocateIpConfigDetail> list = Lists.newLinkedList();
        List<String> ips = request.getIps();
        if (null == ips ||ips.isEmpty()) {
            list.add(allocateIpConfigDetail);
        } else {
            for (String str : ips) {
                AllocateIpConfigDetail compAllocateIpConfigDetail = new AllocateIpConfigDetail();
                compAllocateIpConfigDetail.setVpnId(request.getVpnId());
                compAllocateIpConfigDetail.setNetworkId(request.getNetworkId());
                compAllocateIpConfigDetail.setBizSystem(request.getBizSystem());
                compAllocateIpConfigDetail.setStartTime(request.getStartTime());
                compAllocateIpConfigDetail.setEndTime(request.getEndTime());
                compAllocateIpConfigDetail.setUserName(request.getUserName());
                compAllocateIpConfigDetail.setUserT(request.getUserT());
                compAllocateIpConfigDetail.setIp(str);
                compAllocateIpConfigDetail.setPrivateIp(request.getPrivateIp());
                compAllocateIpConfigDetail.setFlag(0);
                list.add(compAllocateIpConfigDetail);
            }
        }
        log.info("[composite request] >>> {}", list);
        return cmdbAllocateIpConfigClient.insertAllocateIpConfig(list, authCtx.getUser().getUsername());
    }

    @Override
    public String deleteAllocateIpConfigById(@RequestParam("ids") String ids) {
        log.info("[composite request] >>> {}", ids);
        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        log.info("[user name] >>> {}", authCtx.getUser().getUsername());
        return cmdbAllocateIpConfigClient.deleteAllocateIpConfigById(ids, authCtx.getUser().getUsername());
    }

    @Override
    public void exportAllocateIpConfig(@RequestBody CompAllocateIpConfigListReq request) {
        log.info("[composite request] >>> {}", request);
        AllocateIpConfigListReq allocateIpConfigDetail = jacksonBaseParse(AllocateIpConfigListReq.class, request );
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletResponse response = servletRequestAttributes.getResponse();
        try {
            List<Map<String, Object>> compAllocateIpConfigRes = cmdbAllocateIpConfigClient.exportAllocateIpConfig(allocateIpConfigDetail);
            log.info("[cdmb allocataIpConfig Data] >>> {}", compAllocateIpConfigRes );
            String[] headerList = {"域名","网段","业务系统","分配ip","私有云ip","生效时间","用户"};
            String[] keyList = {"vpnName","network","bizSystem","allocateIp","cloudsIp","useTime","userInfo"};
            String title = "IP分配配置表";
            String fileName = title+".xlsx";
            OutputStream os = response.getOutputStream();// 取得输出流
            response.setHeader("Content-Disposition", "attachment;filename=".concat(String.valueOf( URLEncoder.encode(fileName, "UTF-8"))));
            response.setHeader("Connection", "close");
            response.setHeader("Content-Type", "application/vnd.ms-excel");
            //excel constuct
            ExportExcelUtil eeu = new ExportExcelUtil();
            Workbook book = new SXSSFWorkbook(128);
            eeu.exportExcel(book, 0, title, headerList, compAllocateIpConfigRes, keyList);
            book.write(os);
        } catch (Exception e) {
            log.error("[export allocateIpConfigData is error] >>> " + e);
        }
    }

    @Override
    public List<Map<String, Object>> getVpnData() {
        return cmdbAllocateIpConfigClient.getVpnData();
    }

    @Override
    public List<Map<String, Object>> getNetworkById(@RequestParam long id) {
        return cmdbAllocateIpConfigClient.getNetworkById(id);
    }
}
