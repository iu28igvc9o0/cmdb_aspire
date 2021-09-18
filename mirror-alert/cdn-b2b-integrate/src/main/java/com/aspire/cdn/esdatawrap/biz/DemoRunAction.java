package com.aspire.cdn.esdatawrap.biz;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.aspire.cdn.esdatawrap.biz.metricalert.MetricAlert;
import com.aspire.cdn.esdatawrap.config.LabelContentHolder;
import com.aspire.cdn.esdatawrap.util.JsonUtil;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.TypeRef;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class DemoRunAction implements IElasticSearchBizRunAction {
	@Autowired
	private WeixinAcessHelper	weixinHelper;
	
//	@Autowired
//	private TestDao	testDao;
	
	@Autowired
	private RestHighLevelClient			restClient;
	
	@Autowired
	private LabelContentHolder 			labelHolder;
	
//	@Autowired
//	private DomainMapCpNameHolder		mapHolder;
	
	private static final String			ALERT_READ_INDEX		= "metric_alert";
	private static final String			DOC_TYPE				= "doc";
	
	
	@Override
	public void doAction() {
//		readFromCdn();
//		write2LocalEs();
		log.info("Started success ....");
	}
	
	private void write2LocalEs() {
		try {
			Path readFile = Paths.get("D:\\metric_alert.txt");
			List<String> allLines = Files.readAllLines(readFile);
			
			Request request = new Request("Post", "_bulk");
			request.setJsonEntity(String.join("\n", allLines));
			Response response = restClient.getLowLevelClient().performRequest(request);
			String respJson = EntityUtils.toString(response.getEntity());
			if (StringUtils.isNotBlank(respJson) && respJson.contains("\"errors\":true")) {
				System.out.println(respJson);
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
	
	private void readFromCdn() {
		try {
			String reqJson = "{\r\n" + 
					"  \"size\": 100, \r\n" + 
					"  \"query\": {\"match_all\": {}}\r\n" + 
					"}";
			
			Request request = new Request("Post", "/" + ALERT_READ_INDEX + "/_search");
			request.setJsonEntity(reqJson);
			Response response = restClient.getLowLevelClient().performRequest(request);
			String respJson = EntityUtils.toString(response.getEntity());
			DocumentContext ctx = JsonUtil.buildDefaultJsonPathContext(respJson);
			
			TypeRef<List<MetricAlert>> typeRef = new TypeRef<List<MetricAlert>>() {};
			List<MetricAlert> alertList = ctx.read("$.hits.hits[*]['_source']", typeRef);
			
			List<String> lines = new ArrayList<>();
			String BULK_CREATE_META	= "{ \"create\" : { \"_index\" : \"%s\", \"_type\" : \"doc\", \"_id\" : \"%s\" } }";
			alertList.stream().forEach(alert -> {
				lines.add(String.format(BULK_CREATE_META, ALERT_READ_INDEX, alert.generateDocId()));
				lines.add(JsonUtil.toJacksonJson(alert));
			});
			lines.add("\n");
			
			Path writeFile = Paths.get("D:\\metric_alert.txt");
			Files.write(writeFile, lines);
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
}
 