package com.aspire.mirror.alert.server.task.leakScan;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aspire.mirror.alert.server.biz.leakScan.SecurityLeakScanBiz;
import com.aspire.mirror.alert.server.clientservice.cmdb.InstanceSearchClient;
import com.aspire.mirror.alert.server.domain.GetLdapMemberRequest;
import com.aspire.mirror.alert.server.vo.leakScan.*;
import com.aspire.mirror.alert.server.clientservice.LdapClient;
import com.aspire.mirror.alert.server.clientservice.payload.GetLdapUserResponse;
import com.aspire.mirror.alert.server.clientservice.payload.ListPagenationResponse;
import com.aspire.mirror.alert.server.util.DateUtils;
import com.aspire.ums.cmdb.allocate.payload.BizSysRequestBody;
import com.google.common.collect.Lists;
import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.poi.util.IOUtils;
import org.dom4j.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.xml.namespace.QName;
import javax.xml.rpc.ServiceException;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

@Component
@Order(3)
public class SecurityLeakScanBpmSubmitTask implements ApplicationRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(SecurityLeakScanBpmSubmitTask.class);

    @Value("${sls.bpm.queue-size:1000}")
    private int SLS_BPM_QUEUE_SIZE;

    @Value("${sls.bpm.queue-consumer-num:1}")
    private int SLS_BPM_QUEUE_CONSUMER_NUM;

    @Value("${sls.bpm.queue-offer-timeout:5}")
    private int SLS_BPM_QUEUE_OFFER_TIMEOUT;

    @Value("${sls.bpm.upload.url}")
    private String BPM_UPLOAD_URL;

    @Value("${sls.ldap.namespace:alauda}")
    private String LDAP_NAMESPACE;

    /* ----------- webservice ------------------------------------------------------------------------------- */
    @Value(value = "${sls.bpm.webservice.url}")
    private String BPM_WEBSERVIE_URL;
    @Value(value = "${sls.bpm.webservice.flowkey}")
    private String BPM_WEBSERVIE_FLOWKEY;
    @Value(value = "${sls.bpm.webservice.account}")
    private String BPM_WEBSERVIE_ACOUNT;
    @Value(value = "${sls.bpm.webservice.namespace}")
    private String BPM_WEBSERVIE_NAMESPACE;
    @Value(value = "${sls.bpm.webservice.method}")
    private String BPM_WEBSERVIE_METHOD;
    @Value(value = "${sls.bpm.rest.url}")
    private String BPM_REST_URL;

    @Autowired
    private InstanceSearchClient instanceSearchClient;
    @Autowired
    private LdapClient ldapClient;


    private BlockingQueue<SecurityLeakSacnBpmSubstance> substanceBlockingQueue;

    @Autowired
    private SecurityLeakScanBiz securityLeakScanBiz;
    /**
     * Callback used to run the bean.
     *
     * @param args incoming application arguments
     * @throws Exception on error
     */
    @Override
    public void run(ApplicationArguments args) throws Exception {
        // 初始化阻塞队列
        substanceBlockingQueue = new LinkedBlockingDeque<>(SLS_BPM_QUEUE_SIZE);
        Executors.newSingleThreadExecutor().execute(new BpmCallHandler(substanceBlockingQueue));
    }

    /**
     * 向队列提交发起扫描工单处理的实体
     * @param substance
     */
    public void submitBpmSubstance(SecurityLeakSacnBpmSubstance substance) {
        String scanId = substance.getScanId();
        String attachFileName = substance.getAttachFile().getName();
        try {
            if (substanceBlockingQueue.offer(substance, SLS_BPM_QUEUE_OFFER_TIMEOUT, TimeUnit.SECONDS)) {
            	LOGGER.info("Successfully offered bpm task ! scanId: {} file: {} to bpm quue. current queue size: {}",
                        scanId, attachFileName, substanceBlockingQueue.size());
            } else {
            	LOGGER.error("Failed to offer bpm task ! scanId: {} file: {} to bpm quue.", scanId, attachFileName);
            }
        } catch (InterruptedException e) {
        	LOGGER.error("Interrupted, failed to offer bpm queue, scanId: {} file: {}", scanId, attachFileName, e);
        }
    }

    private String httpUpload (final File zipFile, final String url, final String token) {
        final String zipFileName = zipFile.getName();
        final CloseableHttpClient httpClient = HttpClients.createDefault();
        String fileId = "";
        try {
            final HttpPost httpPost = new HttpPost(url);
            if (StringUtils.isNotEmpty(token)) {
                httpPost.addHeader("Authorization", "Bearer " + token);
            }
//                httpPost.setHeader("Content-Type", "application/json"); // 此处应去除
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.setCharset(Charset.forName("UTF-8"));
            builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            FileBody fileBody = new FileBody(zipFile);
            builder.addPart("file", fileBody);
//                builder.addTextBody("filename", fileName,  ContentType.DEFAULT_BINARY);
            HttpEntity entity = builder.build();
            httpPost.setEntity(entity);
            final HttpResponse httpResponse = httpClient.execute(httpPost);
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            LOGGER.info("POST Response Status: {}", statusCode);
            HttpEntity responseEntity = httpResponse.getEntity();
            String result = "";
            if (responseEntity != null) {
                // 将响应内容转换为字符串
                result = EntityUtils.toString(responseEntity, Charset.forName("UTF-8"));
                LOGGER.info("POST 上传文件 {} 至 {} 回执: {}", zipFileName, url, result);
            }
            if (statusCode == 200) {
                if (StringUtils.isNotEmpty(result)) {
                    JSONObject map = JSON.parseObject(result);
                    boolean state = Boolean.valueOf(map.getString("state"));
                    if (state) {
                    	LOGGER.info("成功传输了文件: {}", zipFileName);
                        String value = map.getString("value");
                        JSONObject obj = JSON.parseObject(value);
                        fileId = obj.getString("fileId");
                    }
                }
            } else {
            	LOGGER.info("POST 上传文件 HttpCode: {}  Content: {}", statusCode, result);
            }
        } catch (UnsupportedCharsetException e) {
        	LOGGER.error("send post error:", e);
        } catch (ClientProtocolException e) {
        	LOGGER.error("send post error:{}", e);
        } catch (IOException e) {
        	LOGGER.error("send post error:{}", e);
        } finally {
            IOUtils.closeQuietly(httpClient);
        }
        return fileId;
    }

    private void callBpmRestful(String scanId, String content, String token) {
        ProcessLoadRequestVo request = new ProcessLoadRequestVo();
        request.setAccount(BPM_WEBSERVIE_ACOUNT);
        request.setFlowKey(BPM_WEBSERVIE_FLOWKEY);
        request.setSubject("");
        request.setBusinessKey("");
        request.setData(content);
        try {
            final CloseableHttpClient httpClient = HttpClients.createDefault();

            final HttpPost httpPost = new HttpPost(BPM_REST_URL);
            httpPost.addHeader("Content-type","application/json; charset=utf-8");
            if (StringUtils.isNotEmpty(token)) {
                httpPost.addHeader("Authorization", "Bearer " + token);
            }
            String body = JSON.toJSONString(request);
            StringEntity se = new StringEntity(body, Charset.forName("UTF-8"));
//            se.setContentType("application/json; charset=utf-8");
            httpPost.setEntity(se);
            final HttpResponse httpResponse = httpClient.execute(httpPost);
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            LOGGER.info("POST Response Status: {}", statusCode);
            HttpEntity responseEntity = httpResponse.getEntity();
            String result = "";
            if (responseEntity != null) {
                // 将响应内容转换为字符串
                result = EntityUtils.toString(responseEntity, Charset.forName("UTF-8"));
                LOGGER.info("POST {} 回执: {}", BPM_REST_URL, result);
            }
            if (statusCode == 200) {
                if (StringUtils.isNotEmpty(result)) {
                    ProcessLoadResponseVo response = JSONObject.parseObject(result, ProcessLoadResponseVo.class);
                    if (response == null) {
                    	LOGGER.error("BPM 工单派发失败! ...{}", result);
                        return;
                    }
                    String status = response.getStatus();
                    if ("1".equals(status)) {
                        String runId = response.getRunId();
                        securityLeakScanBiz.fillScanRecordBpmId(scanId, runId);
                        LOGGER.info("BPM 派单成功 ！ BPM ID: {}", runId);
                    } else {
                    	LOGGER.error("BPM 工单派发失败! ...返回码: {}", status);
                    }
                }
            } else {
            	LOGGER.error("BPM 工单派发失败! ...{}", result);
            }
        } catch (IOException e) {
        	LOGGER.error("", e);
        }
    }

    private void callWebService(String scanId, String content) {
//        logger.info("Bpm webservice call content: {}", content);
        StringBuilder builder = new StringBuilder();
        // head
        builder.append("<req flowKey=\""
                + BPM_WEBSERVIE_FLOWKEY + "\" subject=\"\" account=\"" + BPM_WEBSERVIE_ACOUNT
                + "\" businessKey=\"\" runId=\"\">");
        builder.append("<data>" + content + "</data>"); // body
        builder.append("</req>"); // foot
        String xml = builder.toString();
//        logger.info("Bpm webservice call xml: {}", xml);
        try {
            Service service = new Service();
            Call call = (Call) service.createCall();
            call.setTargetEndpointAddress(new java.net.URL(BPM_WEBSERVIE_URL));
            call.setOperationName(new QName(BPM_WEBSERVIE_NAMESPACE, BPM_WEBSERVIE_METHOD));// WSDL里面描述的接口名称
            call.addParameter(new QName("xml"), org.apache.axis.encoding.XMLType.XSD_STRING,
                    javax.xml.rpc.ParameterMode.IN);// 接口的参数
            call.setReturnType(org.apache.axis.encoding.XMLType.XSD_STRING);// 设置返回类型
            // 调用webservice请求
            String result = (String) call.invoke(new Object[]{xml});
            LOGGER.info("BPM webservice request body: {} call back：{}", xml, result);
            Document doc = null;
            String message = "";
            try {
                doc = DocumentHelper.parseText(result); // 将字符串转为XML
                Element rootElt = doc.getRootElement(); // 获取根节点
                Attribute messageAttr = rootElt.attribute("message");
                if (null != messageAttr) {
                    message = messageAttr.getValue();
                }
                Attribute attribute = rootElt.attribute("status");
                if (null == attribute) {
                	LOGGER.error("BPM 工单派发失败! ...{}", message);
                }
                String rs = attribute.getValue();
                if ("1".equals(rs)) {
                    Attribute runAttr = rootElt.attribute("runId");
                    String runId = runAttr.getValue();
                    if (StringUtils.isNotEmpty(runId)) {
                    	LOGGER.info("BPM 派单成功 ！ BPM ID: {}", runId);
                        securityLeakScanBiz.fillScanRecordBpmId(scanId, runId);
                    }
                } else {
                	LOGGER.error("BPM 工单派发失败! ...返回码: {}", rs);
                }
            } catch (DocumentException e) {
            	LOGGER.error("parse result error : ", e);
            }
        } catch (ServiceException e) {
        	LOGGER.error("Webservice调用异常: ", e);
        } catch (MalformedURLException e) {
        	LOGGER.error("Webservice调用异常: ", e);
        } catch (RemoteException e) {
        	LOGGER.error("Webservice调用异常: ", e);
        }
    }

    private void submitBpmTask(String scanId, String fileId, String token) throws RuntimeException {
        SecurityLeakScanRecordVo record = securityLeakScanBiz.getSecurityLeakScanRecordById(scanId);
        if (record == null) {
        	LOGGER.error("不存在 scanId: {} 的扫描记录!", scanId);
            return;
        }
        List<SecurityLeakScanReportVo> reoportList = securityLeakScanBiz.getReportListByScanId(scanId);
        String bizLine = record.getBizLine();
        SimpleDateFormat format = new SimpleDateFormat(DateUtils.SHORT_DATE_PATTERN, Locale.CHINA);
        String smsj = format.format(record.getScanDate()); // 扫描时间
        String aqldmc = format.format(record.getScanDate()) + "_" + bizLine + "_漏洞修复"; // 安全漏洞名称
        String gsbm = ""; // 归属部门
        List<Map<String, Object>> sub_ldxxxx = Lists.newArrayList();
        List<String> usernameList = Lists.newArrayList(); // 业务线接口人 中文
        List<String> usrnameList = Lists.newArrayList(); // 业务线接口人ID 拼音字母
        if (CollectionUtils.isNotEmpty(reoportList)) {
            BizSysRequestBody requestBody = new BizSysRequestBody();
            requestBody.setIp(reoportList.get(0).getIp());
            requestBody.setBizSystem(record.getBizLine());
            Map<String, Object> users = instanceSearchClient.selectDepartBizSystemInfo(requestBody);
            LOGGER.info("CMDB Request: {}, Response: {}", JSON.toJSONString(requestBody), JSON.toJSONString(users));
            List<String> deptList = Lists.newArrayList();
            String department1 = String.valueOf(users.get("department1"));
            LOGGER.info("BPM department 1: {}", department1);
            Locale.setDefault(Locale.ENGLISH);
            if (StringUtils.isNotEmpty(department1) && !"null".equals(department1.toLowerCase())) {
                deptList.add(department1);
            }
            String department2 = String.valueOf(users.get("department2"));
            LOGGER.info("BPM department 2: {}", department2);
            if (StringUtils.isNotEmpty(department2) && !"null".equals(department2.toLowerCase())) {
                deptList.add(department2);
            }
            gsbm = CollectionUtils.isEmpty(deptList) ? "" : StringUtils.join(deptList.toArray(), "_"); // 归属部门
            List<Map> contactList = JSON.parseArray(JSON.toJSONString(users.get("contactList")), Map.class);
            if (CollectionUtils.isNotEmpty(contactList)) {
                for (Map<String, String> user : contactList) {
                    String name = user.get("name");
                    String phone = user.get("phone");
                    String email = user.get("email");
                    usernameList.add(name);
                }
            }
            if (CollectionUtils.isNotEmpty(usernameList)) {
                GetLdapMemberRequest ldapMemberRequest = new GetLdapMemberRequest();
                ldapMemberRequest.setNamespace(LDAP_NAMESPACE);
                ldapMemberRequest.setUuids(Lists.newArrayList());
                ldapMemberRequest.setUsernames(Lists.newArrayList());
                ldapMemberRequest.setNames(usernameList);
                ldapMemberRequest.setProjects(Lists.newArrayList());
                ldapMemberRequest.setOrderBy(Lists.newArrayList());
                ListPagenationResponse listPagenationResponse = ldapClient.listLdapMemberInfo(ldapMemberRequest);
                LOGGER.info("LDAP Request: namespace: {}, body: {}, Response: {}", LDAP_NAMESPACE,
                        JSON.toJSONString(ldapMemberRequest), JSON.toJSONString(listPagenationResponse));
                List<GetLdapUserResponse> results = listPagenationResponse.getResults();
                if (CollectionUtils.isNotEmpty(results)) {
                    for (GetLdapUserResponse userInfo : results) {
                        usrnameList.add(userInfo.getUsername());
                    }
                }
            }
            // 漏洞详细信息列表
            for (SecurityLeakScanReportVo dto : reoportList) {
                Map<String, Object> map = new HashMap<>();
                map.put("zwldsl", dto.getMediumLeaks());
                map.put("dwldsl", dto.getLowLeaks());
                map.put("gwldsl", dto.getHighLeaks());
                map.put("fxz", dto.getRiskVal());
                map.put("IPdz", dto.getIp());
                sub_ldxxxx.add(map);
            }
        } else {
        	LOGGER.error("漏洞告警报告不存在设备IP信息! 无法发起工单! scanId: {}", scanId);
            return;
        }
        // 调用 webservice
        Map<String, Object> model = new HashMap<>();
        Map<String, Object> ldsmdx = new HashMap<>(); // 漏洞扫描对象
        ldsmdx.put("aqldmc", aqldmc); // 安全漏洞名称
        ldsmdx.put("xfsm", record.getRepairStat()); // 修复说明
        ldsmdx.put("zycjkr", ""); // 资源池接口人
        ldsmdx.put("zycjkrID", ""); // 资源接口人ID
        ldsmdx.put("ywxjkr", StringUtils.join(usernameList.toArray(), "||")); // 业务线接口人
        ldsmdx.put("ywxjkrID", StringUtils.join(usrnameList.toArray(), "||")); // 业务接口人ID
        ldsmdx.put("smsj", smsj); // 扫描时间
        ldsmdx.put("gsbm", gsbm); // 归属部门
        ldsmdx.put("fjxx", fileId); // 附件信息 fileId
        ldsmdx.put("sszyc", ""); // 所属资源池
        ldsmdx.put("gsywx", bizLine); // 归属业务线
        ldsmdx.put("sub_ldxxxx", sub_ldxxxx); // 未知 (漏洞详细信息)

        Map<String, Object> innerMap = new HashMap<>();
        innerMap.put("zwldsl", null); // 中危漏洞数量
        innerMap.put("dwldsl", null); // 低危漏洞数量
        innerMap.put("gwldsl", null); // 高危漏洞数量
        innerMap.put("fxz", null); // 风险值
        innerMap.put("IPdz", null); // IP地址

        Map<String, Object> initDataMap = new HashMap<>();
        initDataMap.put("ldxxxx", innerMap);
        // 漏洞详细信息
        ldsmdx.put("initData", initDataMap);
        model.put("ldsmdx", ldsmdx);
        // 调用 派单 BPM 接口
        long startTime = System.currentTimeMillis();
//        callWebService(scanId, JSON.toJSONString(model));
        callBpmRestful(scanId, JSON.toJSONString(model), token);
        long endTime = System.currentTimeMillis();
        LOGGER.info("BPM ======= 发起工单处理时长: {}s", (endTime - startTime)/1000);
    }

    private class BpmCallHandler implements Runnable {

        boolean interrupted = false;
        private BlockingQueue<SecurityLeakSacnBpmSubstance> queue;

        public BpmCallHandler(BlockingQueue<SecurityLeakSacnBpmSubstance> queue) {
            this.queue = queue;
        }

        @Override
        public void run() {
            try {
                while (true) {
                    final SecurityLeakSacnBpmSubstance substance = queue.take();
                    File attachment = substance.getAttachFile();
                    String scanId = substance.getScanId();
                    if (StringUtils.isEmpty(scanId)) {
                    	LOGGER.error("BPM 工单发起, 不存在 scanId: {} 的扫描记录!", scanId);
                        return;
                    }
                    String token = substance.getToken();
                    if (StringUtils.isEmpty(token)) {
                    	LOGGER.error("BPM file upload token is empty ! scanId: {} file: {}", scanId, attachment.getName());
                        return;
                    }
                    String fileId = httpUpload(attachment, BPM_UPLOAD_URL, token);
                    LOGGER.info("BPM file upload url: {} callback fileId: {}", BPM_UPLOAD_URL, fileId);
                    if (StringUtils.isNotEmpty(fileId)) {
                        securityLeakScanBiz.fillScanRecordBpmFileId(scanId, fileId);
                    } else {
                    	LOGGER.error("BPM file upload: 未获取到上传文件 {} 的回执 fileId", attachment.getName());
                        return;
                    }
                    try {
                    	LOGGER.info("BPM ===========================================START===============");
                        submitBpmTask(scanId, fileId, token);
                        LOGGER.info("BPM 发起工单队列->剩余未处理任务数: {}", queue.size());
                        LOGGER.info("BPM ============================================END================");
                    } catch (RuntimeException e) {
                    	LOGGER.error("", e);
                    }
                }
            } catch (InterruptedException e) {
                interrupted = true;
                LOGGER.error("FtpFileConsumer {} interrupted !", Thread.currentThread().getName(), e);
            } finally {
                if (interrupted) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
}
