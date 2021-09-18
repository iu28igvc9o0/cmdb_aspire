package com.migu.tsg.microservice.atomicservice.composite.controller.alert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aspire.mirror.common.entity.PageResult;
import com.aspire.mirror.composite.service.alert.ICompNetworkStrategyService;
import com.aspire.mirror.composite.service.alert.payload.CompNetworkLoadBalancerDto;
import com.aspire.mirror.composite.service.alert.payload.CompNetworkPublicNetDto;
import com.aspire.mirror.elasticsearch.api.dto.NetworkLoadBalancerDto;
import com.aspire.mirror.elasticsearch.api.dto.NetworkPublicNetDto;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.alert.INetworkStrategyServiceClient;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.ExportExcelUtil;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.PayloadParseUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.*;

/**
 * @author baiwp
 * @title: ICompNetworkStrategyController
 * @projectName msp-composite
 * @description: TODO
 * @date 2019/7/2910:26
 */
@RestController
public class CompNetworkStrategyController implements ICompNetworkStrategyService {

    @Autowired
    private INetworkStrategyServiceClient iNetworkStrategyServiceClient;

    public PageResult<Map<String, Object>> queryFillWall(@RequestParam(value = "keyword", required = false) String
                                                                 keyword,
                                                         @RequestParam(value = "name", required = false) String name,
                                                         @RequestParam(value = "sourceZone", required = false)
                                                                 String sourceZone,
                                                         @RequestParam(value = "destinationZone", required = false)
                                                                 String destinationZone,
                                                         @RequestParam(value = "action", required = false) String
                                                                 action,
                                                         @RequestParam(value = "destinationAddress", required =
                                                                 false) String destinationAddress,
                                                         @RequestParam(value = "sourceAddress", required = false)
                                                                 String sourceAddress,
                                                         @RequestParam(value = "servicePort", required = false)
                                                                 String servicePort,
                                                         @RequestParam(value = "pageNum", defaultValue = "1") int
                                                                 pageNum,
                                                         @RequestParam(value = "pageSize", defaultValue = "50") int
                                                                 pageSize) {
//        PageResult<Map<String, Object>> networkFillWallDtoPageResult = iNetworkStrategyServiceClient.queryFillWall(keyword, pageNum, pageSize);
//        List<CompNetworkFillWallDto> compNetworkStrategyDtoList = PayloadParseUtil.jacksonBaseParse(CompNetworkFillWallDto.class, networkFillWallDtoPageResult.getResult());
//        PageResult<CompNetworkFillWallDto> page = new PageResult<>();
//        page.setPageSize(networkFillWallDtoPageResult.getPageSize());
//        page.setCurPage(networkFillWallDtoPageResult.getCurPage());
//        page.setCount(networkFillWallDtoPageResult.getCount());
//        page.setPageCount(page.getPageCount());
//        page.setResult(compNetworkStrategyDtoList);
        return iNetworkStrategyServiceClient.queryFillWall(keyword, name, sourceZone, destinationZone, action,
                destinationAddress, sourceAddress, servicePort, pageNum, pageSize);
    }

    public void exportFillWall(@RequestParam(value = "keyword", required = false) String keyword, HttpServletResponse response) throws Exception {
        String[] headerList = {"防火墙IP", "策略编号", "源区域", "目标区域", "执行动作", "源地址", "目的地址",  "服务端口", "所属机房", "创建日期"};
        String[] keyList = {"ip", "name", "source_zone", "destination_zone", "action", "source", "target",  "port", "roomId", "create_time"};
        String title = "网络策略-防火墙";
        String fileName = title + ".xlsx";
        List<Map<String, Object>> networkFillWallDtoList = iNetworkStrategyServiceClient.exportFillWall(keyword);
        List<Map<String, Object>> list = new ArrayList<>();
        for (Map<String, Object> map: networkFillWallDtoList) {
            JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(map));
            Map<String, Object> netMap = new HashMap<>();
            netMap.put("ip", jsonObject.getString("ip"));
            netMap.put("roomId", jsonObject.getString("roomId"));
            netMap.put("create_time", jsonObject.getString("create_time"));
            JSONObject securityRule = jsonObject.getJSONObject("security_rule");
            if (securityRule == null) {
                list.add(netMap);
                continue;
            }
            netMap.put("destination_zone", securityRule.getString("destination_zone"));
            netMap.put("source_zone", securityRule.getString("source_zone"));
            netMap.put("name", securityRule.getString("name"));
            netMap.put("action", securityRule.getString("action"));
            JSONObject sourceAddress = securityRule.getJSONObject("source_address");
            StringBuilder sourceSb = new StringBuilder();
            if (sourceAddress != null) {
                JSONArray aDefault = sourceAddress.getJSONArray("default");
                if (aDefault != null) {
                    for (int i = 0; i < aDefault.size(); i++) {
                        JSONObject defaultDetail = aDefault.getJSONObject(i);
                        sourceSb.append(defaultDetail.getString("network")).append("/").append(defaultDetail.getString("mask")).append(",");
                    }
                }
                JSONArray range = sourceAddress.getJSONArray("range");
                if (range != null) {
                    for (int i = 0; i < range.size(); i++) {
                        JSONObject rangeDetail = range.getJSONObject(i);
                        sourceSb.append(rangeDetail.getString("from")).append("-").append(rangeDetail.getString("to")).append(",");
                    }
                }
                JSONObject addressSets = sourceAddress.getJSONObject("address_sets");
                if (addressSets != null) {
                    JSONArray addressList = addressSets.getJSONArray("list");
                    if (addressList != null) {
                        for (int i = 0; i < addressList.size(); i++) {
                            JSONObject address = addressList.getJSONObject(i);
                            sourceSb.append(address.getString("ip")).append("/").append(address.getString("mask")).append(",");
                        }
                    }
                }
            }
            if (sourceSb.length() > 0) {
                netMap.put("source", sourceSb.substring(0, sourceSb.length()-1));
            }

            JSONObject destinationAddress = securityRule.getJSONObject("destination_address");
            StringBuilder targetSb = new StringBuilder();
            if (destinationAddress != null) {
                JSONArray aDefault = destinationAddress.getJSONArray("default");
                if (aDefault != null) {
                    for (int i = 0; i < aDefault.size(); i++) {
                        JSONObject defaultDetail = aDefault.getJSONObject(i);
                        targetSb.append(defaultDetail.getString("network")).append("/").append(defaultDetail.getString("mask")).append(",");
                    }
                }
                JSONArray range = destinationAddress.getJSONArray("range");
                if (range != null) {
                    for (int i = 0; i < range.size(); i++) {
                        JSONObject rangeDetail = range.getJSONObject(i);
                        targetSb.append(rangeDetail.getString("from")).append("-").append(rangeDetail.getString("to")).append(",");
                    }
                }
                JSONObject addressSets = destinationAddress.getJSONObject("address_sets");
                if (addressSets != null) {
                    JSONArray addressList = addressSets.getJSONArray("list");
                    if (addressList != null) {
                        for (int i = 0; i < addressList.size(); i++) {
                            JSONObject address = addressList.getJSONObject(i);
                            targetSb.append(address.getString("ip")).append("/").append(address.getString("mask")).append(",");
                        }
                    }
                }
            }
            if (targetSb.length() > 0) {
                netMap.put("target", targetSb.substring(0, targetSb.length()-1));
            }

            JSONObject service = securityRule.getJSONObject("service");
            StringBuilder portSb = new StringBuilder();
            if (service != null) {
                JSONArray serviceSet = service.getJSONArray("service_set");
                if (serviceSet != null) {
                    for (int i = 0; i < serviceSet.size(); i++) {
                        JSONObject serviceSetJSONObject = serviceSet.getJSONObject(i);
                        if (serviceSetJSONObject != null) {
                            JSONArray serviceList = serviceSetJSONObject.getJSONArray("list");
                            if (serviceList != null) {
                                for (int j = 0; j < serviceList.size(); j++) {
                                    JSONObject serviceDetail = serviceList.getJSONObject(j);
                                    if (serviceDetail != null) {
                                        String protocol = serviceDetail.getString("protocol");
                                        JSONObject servicePorts = serviceDetail.getJSONObject("service_ports");
                                        if (servicePorts != null) {
                                            JSONArray rangePorts = servicePorts.getJSONArray("range_ports");
                                            if (rangePorts != null) {
                                                for (int k = 0; k < rangePorts.size(); k++) {
                                                    JSONObject rangeDetail = rangePorts.getJSONObject(k);
                                                    if (StringUtils.isEmpty(protocol)) {
                                                        portSb.append(rangeDetail.getString("from")).append("-").append(rangeDetail.getString("to")).append(",");
                                                    } else {
                                                        portSb.append(rangeDetail.getString("from")).append("-").append(rangeDetail.getString("to")).append("/").append(protocol).append(",");
                                                    }

                                                }
                                            }
                                            JSONArray ports = servicePorts.getJSONArray("ports");
                                            if (ports != null) {
                                                for (int k = 0; k < ports.size(); k++) {
                                                    String portDetail = ports.getString(k);
                                                    if (StringUtils.isEmpty(protocol)) {
                                                        portSb.append(portDetail).append(",");
                                                    } else {
                                                        portSb.append(portDetail).append("/").append(protocol).append(",");
                                                    }

                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if (portSb.length() > 0) {
                netMap.put("port", portSb.substring(0, portSb.length()-1));
            }
            list.add(netMap);
        }
        OutputStream os = response.getOutputStream();// 取得输出流
        response.setHeader("Content-Disposition", "attachment;filename=".concat(String.valueOf(URLEncoder.encode(fileName, "UTF-8"))));
        response.setHeader("Connection", "close");
        response.setHeader("Content-Type", "application/vnd.ms-excel");
        //excel constuct
        ExportExcelUtil eeu = new ExportExcelUtil();
        Workbook book = new SXSSFWorkbook(128);
        eeu.exportExcel(book, 0, title, headerList, list, keyList);
        book.write(os);
    }

    public PageResult<CompNetworkLoadBalancerDto> queryLoadBalancer(@RequestParam(value = "keyword", required = false) String keyword,
                                                                    @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                                                    @RequestParam(value = "pageSize", defaultValue = "50") int pageSize) {
        PageResult<NetworkLoadBalancerDto> networkLoadBalancerDtoPageResult = iNetworkStrategyServiceClient.queryLoadBalancer(keyword, pageNum, pageSize);
        List<CompNetworkLoadBalancerDto> compNetworkStrategyDtoList = PayloadParseUtil.jacksonBaseParse(CompNetworkLoadBalancerDto.class, networkLoadBalancerDtoPageResult.getResult());
        PageResult<CompNetworkLoadBalancerDto> page = new PageResult<>();
        page.setPageSize(networkLoadBalancerDtoPageResult.getPageSize());
        page.setCurPage(networkLoadBalancerDtoPageResult.getCurPage());
        page.setCount(networkLoadBalancerDtoPageResult.getCount());
        page.setPageCount(page.getPageCount());
        page.setResult(compNetworkStrategyDtoList);
        return page;
    }

    public void exportLoadBalancer(@RequestParam(value = "keyword", required = false) String keyword, HttpServletResponse response) throws Exception {
        String[] headerList = {"负载IP", "负载端口", "负载模式", "负载协议", "负载pool", "内网地址", "内网端口", "Snat类型", "设备IP", "所属机房", "创建日期"};
        String[] keyList = {"loadIp", "loadPort", "loadMode", "loadProtocol", "loadPool", "IntranetAddr", "IntranetPort", "snatType", "deviceIp", "roomId", "createTime"};
        String title = "网络策略-负载均衡";
        String fileName = title + ".xlsx";
        List<NetworkLoadBalancerDto> networkLoadBalancerDtoList = iNetworkStrategyServiceClient.exportLoadBalancer(keyword);
        OutputStream os = response.getOutputStream();// 取得输出流
        response.setHeader("Content-Disposition", "attachment;filename=".concat(String.valueOf(URLEncoder.encode(fileName, "UTF-8"))));
        response.setHeader("Connection", "close");
        response.setHeader("Content-Type", "application/vnd.ms-excel");
        //excel constuct
        ExportExcelUtil eeu = new ExportExcelUtil();
        Workbook book = new SXSSFWorkbook(128);
        eeu.exportExcel(book, 0, title, headerList, PayloadParseUtil.objectToMap(networkLoadBalancerDtoList), keyList);
        book.write(os);
    }

    public PageResult<CompNetworkPublicNetDto> queryPublicNet(@RequestParam(value = "keyword", required = false) String keyword,
                                                              @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                                              @RequestParam(value = "pageSize", defaultValue = "50") int pageSize) {
        PageResult<NetworkPublicNetDto> networkPublicNetDtoPageResult = iNetworkStrategyServiceClient.queryPublicNet(keyword, pageNum, pageSize);
        List<CompNetworkPublicNetDto> compNetworkStrategyDtoList = PayloadParseUtil.jacksonBaseParse(CompNetworkPublicNetDto.class, networkPublicNetDtoPageResult.getResult());
        PageResult<CompNetworkPublicNetDto> page = new PageResult<>();
        page.setPageSize(networkPublicNetDtoPageResult.getPageSize());
        page.setCurPage(networkPublicNetDtoPageResult.getCurPage());
        page.setCount(networkPublicNetDtoPageResult.getCount());
        page.setPageCount(page.getPageCount());
        page.setResult(compNetworkStrategyDtoList);
        return page;
    }

    public void exportPublicNet(@RequestParam(value = "keyword", required = false) String keyword, HttpServletResponse response) throws Exception {
        String[] headerList = {"防火墙IP", "公网IP", "公网端口", "负载IP", "负载端口", "内网IP", "内网端口", "所属机房", "创建日期"};
        String[] keyList = {"fillWallIp", "publicNetIp", "publicNetPort", "loadIp", "loadPort", "targetIntranetIp", "targetIntranetPort", "createTime"};
        String title = "网络策略-公网";
        String fileName = title + ".xlsx";
        List<NetworkPublicNetDto> networkPublicNetDtoList = iNetworkStrategyServiceClient.exportPublicNet(keyword);
        OutputStream os = response.getOutputStream();// 取得输出流
        response.setHeader("Content-Disposition", "attachment;filename=".concat(String.valueOf(URLEncoder.encode(fileName, "UTF-8"))));
        response.setHeader("Connection", "close");
        response.setHeader("Content-Type", "application/vnd.ms-excel");
        //excel constuct
        ExportExcelUtil eeu = new ExportExcelUtil();
        Workbook book = new SXSSFWorkbook(128);
        eeu.exportExcel(book, 0, title, headerList, PayloadParseUtil.objectToMap(networkPublicNetDtoList), keyList);
        book.write(os);
    }

}
