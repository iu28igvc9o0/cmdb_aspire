package com.aspire.ums.cmdb.bpmx.sendOrder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.aspire.ums.cmdb.ipCollect.entity.BpmAutoStartTaskOrderParam;
import com.aspire.ums.cmdb.ipCollect.entity.ExcelTemplateParam;
import com.aspire.ums.cmdb.util.Base64Util;
import com.aspire.ums.cmdb.util.DateUtils;
import com.aspire.ums.cmdb.util.ExportExcelUtils;
import com.aspire.ums.cmdb.util.HttpUtil;
import com.aspire.ums.cmdb.util.JsonUtil;
import com.aspire.ums.cmdb.util.SpringUtils;

/**
 * 派发工单抽象类,所有的派发工单都继承该类.
 *
 * @author jiangxuwen
 * @date 2021/1/4 12:55
 */
public abstract class AbstractBpmAutoOrderTask {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    private BpmConfiguration bpmConfiguration;

    protected BpmConfiguration getBpmConfiguration() {
        if (bpmConfiguration == null) {
            bpmConfiguration = SpringUtils.getBean("bpmConfiguration", BpmConfiguration.class);
        }
        return bpmConfiguration;
    }

    public final String execute() {
        String taskName = getTaskName();
        logger.info("================{},派发工单任务开始========================", taskName);
        List<Map<String, Object>> resultList = getPendingSendOrderList();
        try {
            logger.info("pending autoSendOrder list.size==={}", resultList.size());
            handleAutoOrder(resultList);
            logger.info("================{},派发工单任务完成========================", taskName);
            return "success";
        } catch (Exception e) {
            logger.error("派单失败", e);
            return "error";
        }
    }

    /**
     * 派发工单+更新数据库工单派发状态.
     * 
     * @param
     * @return
     */
    protected void handleAutoOrder(List<Map<String, Object>> list) throws Exception {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        String result = doSendAutoOrder(list);
        logger.info("autoStartTaskOrder response==={}", result);
        Map<String, Object> resultMap = JsonUtil.parseJSONstr2Map(result);
        if (MapUtils.isEmpty(resultMap)) {
            return;
        }
        updateOrderSendStatus(list, resultMap);
    }

    /**
     * 执行发送派发工单到BPM.
     * 
     * @param
     * @return
     */
    protected String doSendAutoOrder(List<Map<String, Object>> resultList) throws Exception {
        BpmAutoStartTaskOrderParam param = buildBpmAutoTaskParam();
        Object[] objects = getAttachmentContent(resultList);
        String fileName = (String) objects[0];
        byte[] content = (byte[]) objects[1];
        String json = JSON.toJSONString(param);
        return httpPostSend(json, content, fileName);

    }

    private String httpPostSend(String json, byte[] content, String fileName) {
        CloseableHttpResponse resp = null;
        String respondBody = null;
        try {
            final CloseableHttpClient httpClient = HttpClients.createDefault();
            final HttpPost httpPost = new HttpPost(getBpmConfiguration().getBpmDispatchOrderUrl());
            Map<String, String> adminHeader = getBpmUserToken("dw_jzwgxt");
            httpPost.addHeader("Authorization", adminHeader.get("Authorization"));
            // 附件参数需要用到的请求参数实体构造器
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.addBinaryBody("file", content, ContentType.MULTIPART_FORM_DATA, fileName);// 文件流
            ContentType contentType = ContentType.create(HTTP.PLAIN_TEXT_TYPE, HTTP.UTF_8);
            StringBody jsonBody = new StringBody(json, contentType);
            builder.addPart("param", jsonBody);
            StringBody fileNameBody = new StringBody(fileName, contentType);
            builder.addPart("xqfjmc", fileNameBody);
            logger.info("================派发工单任务参数：{},需求附件名称{}========================", json, fileName);
            HttpEntity httpEntity = builder.build();
            httpPost.setEntity(httpEntity);
            resp = httpClient.execute(httpPost);
            respondBody = EntityUtils.toString(resp.getEntity());
        } catch (IOException | ParseException e) {
            // 日志信息及异常处理
            logger.error("执行HTTP响应时抛出异常，需要关注", e);
        } finally {
            if (resp != null) {
                try {
                    // 关闭请求
                    resp.close();
                } catch (IOException e) {
                    logger.error("关闭HTTP响应时抛出异常，需要关注", e);
                }
            }
        }
        return respondBody;
    }

    public Map<String, String> getBpmUserToken(String userLoginName) {
        try {
            String name = Base64Util.getBase64(userLoginName);
            String tokenUrl = getBpmConfiguration().getBpmJwtTokenUrl() + "?name=" + name;
            String resultToken = HttpUtil.post(tokenUrl, "");
            com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(resultToken);
            String jwtToken = jsonObject.getString("token");
            Map<String, String> header = new HashMap<>();
            header.put("Authorization", "Bearer " + jwtToken);
            return header;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取要发送到BPM的附件内容.
     * 
     * @param resultList
     *            结果列表
     * @return
     */
    protected Object[] getAttachmentContent(List<Map<String, Object>> resultList) throws Exception {
        ExcelTemplateParam excelTemplateParam = buildExcelTemplateParam();
        Object[] obj = new Object[2];
        OutputStream out = null;
        String[] headerArray = excelTemplateParam.getHeaders();
        String[] keyArray = excelTemplateParam.getKeys();
        String date = DateUtils.format(new Date(), DateUtils.DATE_PLAIN_FORMAT);
        String title = excelTemplateParam.getSheetName() + "_" + date;
        obj[0] = title + ".xls";
        String fileName = excelTemplateParam.getFilePath() + obj[0];
        ExportExcelUtils eeu = new ExportExcelUtils();
        Workbook book = new SXSSFWorkbook(128);
        try {
            FileUtils.forceMkdir(new File(fileName).getParentFile());
            eeu.exportExcel(book, 0, title, headerArray, resultList, keyArray);
            out = new FileOutputStream(fileName);
            book.write(out);
        } finally {
            if (out != null) {
                out.flush();
                out.close();
            }
        }
        byte[] content = FileUtils.readFileToByteArray(new File(fileName));
        obj[1] = content;
        return obj;
    }

    /**
     * 获取执行派单任务的名称.
     * 
     * @param
     * @return
     */
    protected abstract String getTaskName();

    /**
     * 获取生成Excel的参数.
     * 
     * @param
     * @return
     */
    protected abstract ExcelTemplateParam buildExcelTemplateParam();

    /**
     * 获取执行派单附件的列表
     * 
     * @param
     * @return
     */
    protected abstract List<Map<String, Object>> getPendingSendOrderList();

    /**
     * 更新数据库中的派发工单状态.
     * 
     * @param resultList
     *            待更新列表
     * @param resultMap
     *            工单号
     * @return
     */
    protected abstract void updateOrderSendStatus(List<Map<String, Object>> resultList, Map<String, Object> resultMap);

    /**
     * 构建派单请求参数.
     * 
     * @param
     * @return
     */
    protected abstract BpmAutoStartTaskOrderParam buildBpmAutoTaskParam();
}
