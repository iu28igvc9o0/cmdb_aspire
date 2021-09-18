package com.migu.tsg.microservice.atomicservice.composite.controller.alert;

import static com.migu.tsg.microservice.atomicservice.composite.controller.util.PayloadParseUtil.jacksonBaseParse;

import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aspire.mirror.composite.service.alert.IComStpInfoService;
import com.aspire.mirror.composite.service.alert.payload.ComRouteDeviceInfo;
import com.aspire.mirror.composite.service.alert.payload.ComRouteQueryRequest;
import com.aspire.mirror.composite.service.alert.payload.ComStpDeviceInfo;
import com.aspire.mirror.elasticsearch.api.dto.RouteDataInfo;
import com.aspire.mirror.elasticsearch.api.dto.RouteDeviceInfo;
import com.aspire.mirror.elasticsearch.api.dto.RouteQueryRequest;
import com.aspire.mirror.elasticsearch.api.dto.StpDeviceInfo;
import com.google.common.collect.Maps;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.alert.IStpInfoServiceClient;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.ExportExcelUtil;

/**
 * @author baiwp
 * @title: ICompNetworkStrategyController
 * @projectName msp-composite
 * @description: TODO
 * @date 2019/7/2910:26
 */
@RestController
public class ComStpInfoController implements IComStpInfoService {

	 @Autowired
	 private IStpInfoServiceClient iStpInfoServiceClient;
	 
	@Override
	public ComStpDeviceInfo getStpInfoList(@RequestParam(value = "ip")String ip
			,@RequestParam(value = "idcType") String idcType,@RequestParam(value = "indexDate",required=false) String indexDate) {
		StpDeviceInfo stpList = iStpInfoServiceClient.queryStpInfo(idcType, ip,indexDate);
		return jacksonBaseParse(ComStpDeviceInfo.class, stpList);
	}
	@Override
	public ComRouteDeviceInfo getRouteInfoList(@RequestBody ComRouteQueryRequest queryRequest) {
		RouteDeviceInfo routeInfo = iStpInfoServiceClient.queryRouteInfo(jacksonBaseParse(RouteQueryRequest.class, queryRequest));
		//routeInfo.setRouteDataList(Lists.newArrayList());
		return jacksonBaseParse(ComRouteDeviceInfo.class, routeInfo);
	}
	@Override
	public void exportRouteInfo(@RequestBody ComRouteQueryRequest queryReq, HttpServletResponse response) throws Exception {
		RouteDeviceInfo routeInfo = iStpInfoServiceClient.queryRouteInfo(jacksonBaseParse(RouteQueryRequest.class, queryReq));
		List<Map<String, Object>> valList = Lists.newArrayList();
		if(null!=routeInfo) {
			List<RouteDataInfo> routeList = routeInfo.getRouteDataList();
			for(RouteDataInfo r:routeList) {
				Map<String, Object> val = Maps.newHashMap();
				val.put("routeProto", r.getRouteProto());
				val.put("routeDest", r.getRouteDest());
				val.put("routeMask", r.getRouteMask());
				val.put("routeNextHop", r.getRouteNextHop());
				valList.add(val);
			}
		}
		
		String[] headerList = { "路由协议", "目的地址", "掩码", "路由的下一跳地址"};
		String[] keyList = { "routeProto", "routeDest", "routeMask", "routeNextHop" };
		//SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		String title = "路由信息表";
		String fileName = title + ".xlsx";
//       
		OutputStream os = response.getOutputStream();// 取得输出流
		response.setHeader("Content-Disposition",
				"attachment;filename=".concat(String.valueOf(URLEncoder.encode(fileName, "UTF-8"))));
		response.setHeader("Connection", "close");
		response.setHeader("Content-Type", "application/vnd.ms-excel");
		// excel constuct
		ExportExcelUtil eeu = new ExportExcelUtil();
		Workbook book = new SXSSFWorkbook(128);
		eeu.exportExcel(book, 0, title, headerList, valList, keyList);
		book.write(os);
	}

  
}
