package com.aspire.ums.cmdb.service.impl;

import com.aspire.ums.cmdb.mapper.OrgSyncManager;
import com.aspire.ums.cmdb.sync.entity.User;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import net.sf.json.JSONArray;
import com.aspire.ums.cmdb.common.StringUtils;


import com.aspire.ums.cmdb.service.OrgSyncInterfaceService;
import com.aspire.ums.cmdb.sync.entity.CommonResult;
import net.sf.json.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.*;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 同步接口信息表(OrgSyncInterface)表服务实现类
 *
 * @author makejava
 * @since 2020-12-03 15:44:10
 */
@Service
public class OrgSyncInterfaceServiceImpl implements OrgSyncInterfaceService {


    @Value("${sync.yunGuan.tokenUrl:http://10.144.91.163:30110/oauthserver/oauth/token}")
    private String sendUrl;
    @Value("${sync.yunGuan.userName:IWbqqFuqHw7GIqlWZRevR1NUothJR4VDMpO0NfGcv1r+RyIlWoaamJ+d/mL/bcrunv3B5D7y6hsIza6KmEct6Fa3hMbU4VLuA6mAaiRhFNUCKCiJd/renkZV2ddhTH7EeqHXy2g808c7HnXLWKsA7JgkYHx/sQ6UGV+lN+EoTZM=}")
    private String userName;
    @Value("${sync.yunGuan.password:YOeEctAnr2qXR/3sl1vlPyI3ox1GjRLE+NhacmUEjkDBgwgr1R8HO+/d9lLRmNs27Kn966YYfs/AUYmgZZekjmZqN0GiUON6/mhPbkAruIgn95QEI4fh5npYxWIvbTtFjvPbM+QN1fNNpZX/up0e1QV/BT6yIkxrREJo3vcvt9s=}")
    private String password;
    @Value("${sync.yunGuan.orgUrl:http://10.144.91.163:30110/madrids/v1/external-platform/organizations}")
    private String getSuYanOrgsUrl;
    @Value("${sync.yunGuan.userUrl:http://10.144.91.163:30110/madrids/v1/external-platform/users?&platformName=BPM工单}")
    private String getSuYanUsersUrl;
    @Value("${sync.bpm.token:Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImV4cCI6MTYwOTIzNTI4MSwiaWF0IjoxNjA5MTQ4ODgxfQ.4O7N09mHVYgjyc1jQx9xB3Xg7bWLHR9g1Y8JR7DOHoO1t4PY69DpTjbWigbGADMTVCin32z6vR3keTO8BQbUkg}")
    private String bpmToken;
    @Value("${sync.bpm.org:http://10.12.70.39:28129/v1/cmdb/restful/bpm/resource/syncOrgSystem}")
    private String bpmOrgUrl;
    @Value("30110")
    private String port;
    @Resource
    private OrgSyncManager orgSyncManager;

    private static final String SUZHOU_SYNC="BOMC";





    protected static Logger logger = LoggerFactory.getLogger(OrgSyncInterfaceServiceImpl.class);



    /**
     * 全量同步云管组织架构到UMS用户管理
     * 1 获取数据    2 orgnization->department  3 分类增删改
     * 1 验证+地址                                  获取三方数据
     * 2 数据+sql(数据源)->字段名                   封装成目标对象增删改{
     * 1) Json转目标对象 获取同步时间 过滤掉 已同步数据(单独封装一个方法)
     * 2) 目标对象.setDestName(数据.get(sourceName))
     * 3) 比对目标对象{
     *     目标对象.get
     *     null-insert
     *     存在-update
     *     Deletestatus-delete
     *     }
     * 4) 同步完成 更新信息表的updateTime
     */
    @Override
    public void syncOrgData(String syncData) throws Exception {
        CommonResult res = new CommonResult();
        //>>>>>>>>>>>>>>>>>>>>>>测试阶段本地获取>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
        //String path2 = "D:/fileStorage/download/json/31orgless.json";
        String path2 = "D:/fileStorage/download/json/31orgjson.json";
        String String = readJsonFile(path2);
        net.sf.json.JSONObject allData = net.sf.json.JSONObject.fromObject(String);
        net.sf.json.JSONArray entity =  allData.getJSONObject("entity").getJSONArray("list");
        //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

        // 1 从三方接口获取json数据,filter过滤未更新数据
        // net.sf.json.JSONArray entity = getSuYanData(getSuYanOrgsUrl);
        // 2.1
        List<Map<String,String>> depts = JSON2Dept(entity,SUZHOU_SYNC,"department");
        // 3 遍历比对分类 INSET UPDATE DELETE
        // 3.1 不指定时间全部INSERT
        // insertOrg(depts);
        System.out.println();
        orgSyncManager.insertOrg(depts,SUZHOU_SYNC);


    }

    /**
     * 同步用户数据
     * @param syncData
     * @throws Exception
     */
    @Override
    public void syncUserData(String syncData) throws Exception {
        // 1 从三方接口获取json数据,filter过滤未更新数据 VING
        // net.sf.json.JSONArray entity = getSuYanData(getSuYanUsersUrl);

        //>>>>>>>>>>>>>>>>>>>>>>测试阶段本地获取>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
        String path2 = "D:/fileStorage/download/json/31userjson.json";
        String String = readJsonFile(path2);
        net.sf.json.JSONObject allData = net.sf.json.JSONObject.fromObject(String);
        net.sf.json.JSONArray entity =  allData.getJSONObject("entity").getJSONArray("list");
        //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
        // 2.1
        List<Map<String,String>> users = JSON2Dept(entity,SUZHOU_SYNC,"user");
        // 3 遍历比对分类 INSET UPDATE DELETE
        // 3.1 不指定时间全部INSERT
        // insertOrg(depts);

        orgSyncManager.insertUser(users,SUZHOU_SYNC);
        orgSyncManager.insertUserDept(users,SUZHOU_SYNC);
        orgSyncManager.setLastSyncTimeString(SUZHOU_SYNC,"user");


    }
    /**
     * 增量同步用户数据
     * @param syncDate
     * @throws Exception
     */
    @Override
    @Transactional
    public void syncAddUserData(String syncDate) throws Exception {
        // 1 从三方接口获取json数据
        net.sf.json.JSONArray entity = getSuYanData(getSuYanUsersUrl);
        // 2.1UVING 本地测试数据
    /*    String path2 = "D:/fileStorage/download/json/31userjson.json";
        String String = readJsonFile(path2);

        net.sf.json.JSONObject allData = net.sf.json.JSONObject.fromObject(String);
        net.sf.json.JSONArray entity =  allData.getJSONObject("entity").getJSONArray("list");*/

        //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


        List<Map<String,String>> users = JSON2Dept(entity,SUZHOU_SYNC,"user");
     /*
     //此处过滤 会导致 下面过滤删除list的数据误增, 过滤数据会变成删除数据
     if(null!=syncDate){
            users = users.parallelStream().filter(org -> compareTime((String) JSONObject.fromObject(org).get("createdAt"), syncDate) || compareTime((String) JSONObject.fromObject(org).get("updatedAt"), syncDate)).collect(Collectors.toList());
        }*/
        // 3 遍历比对分类 INSET UPDATE DELETE
        // 查出从目标源同步过来的所有UUID, 移除能和云管匹配上的, 剩余的就是 被云管删除的
        List<String> uuids = orgSyncManager.getUuidsByYyncSource(SUZHOU_SYNC);
        //List<Map<String, Object>> udMapList = orgSyncManager.getUuidsAndDeptIdByYyncSource(SUZHOU_SYNC);

        HashMap<String,String> uuMap = new HashMap();
        HashMap<String,String> accountMap = new HashMap();
        //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
        for(int i = 0; i<uuids.size(); i++){
            uuMap.put(uuids.get(i),"1");
        }
        List<Map<String,String>> insertList = new ArrayList<>();
        List<Map<String,String>> updateList = new ArrayList<>();
        List<String> delList = new ArrayList<>();
        List<String> delAccountList = new ArrayList<>();
        for (Map m:
                users) {
            if("1".equals(uuMap.get(m.get("uuid")))){
                updateList.add(m);
                m.put("delStatus",0);
                uuMap.remove(m.get("uuid"));
            } else {
                insertList.add(m);
            }

        }
        if(uuMap.size()!=0){
            uuMap.entrySet()
                    .stream()
                    .forEach(entry ->delList.add(entry.getKey()));
        }
        if(uuMap.size()!=0){
            uuMap.entrySet()
                    .stream()
                    .forEach(entry ->delAccountList.add(orgSyncManager.getCodeById(entry.getKey()).get(0)));
        }

        //过滤update数据
        updateList = updateList.parallelStream().filter(org -> compareTime((String) JSONObject.fromObject(org).get("createdAt"), syncDate) || compareTime((String) JSONObject.fromObject(org).get("updatedAt"), syncDate)).collect(Collectors.toList());

        if("pushOnly".equals(syncDate)) {
            orgSyncManager.insertUser(insertList, SUZHOU_SYNC);
            orgSyncManager.insertUserDept(insertList, SUZHOU_SYNC);

            orgSyncManager.updateUser(updateList, SUZHOU_SYNC);
            orgSyncManager.updateUserDept(updateList, SUZHOU_SYNC);

            orgSyncManager.delUser(delList, SUZHOU_SYNC);
            orgSyncManager.delUserDept(delList);
            //   orgSyncManager.modiUserDept(modiUDMap);
            orgSyncManager.setLastSyncTimeString(SUZHOU_SYNC, "user");
        }
        if(insertList.size()!=0) {
            HashMap<String, List> postInMap = new HashMap<>();
            postInMap.put("insertUser", insertList);
            String insertJson = com.alibaba.fastjson.JSON.toJSONString(postInMap);
            sendPostData2BPM("http://localhost:8088/api/YunGuanSync/v1/getSuYanData", insertJson);
        }
        if(updateList.size()!=0) {
            HashMap<String, List> postUpMap = new HashMap<>();
            postUpMap.put("upUser", updateList);
            String upJson = com.alibaba.fastjson.JSON.toJSONString(postUpMap);
            sendPostData2BPM("http://localhost:8088/api/YunGuanSync/v1/getSuYanData", upJson);
        }
        if(delList.size()!=0) {
            HashMap<String, List> postDelMap = new HashMap<>();
            postDelMap.put("delUser", delAccountList);
            String delJson = com.alibaba.fastjson.JSON.toJSONString(postDelMap);
            sendPostData2BPM("http://localhost:8088/api/YunGuanSync/v1/getSuYanData", delJson);
        }
    }


    /**
     * 推送用户数据到bpm
     * @param url
     * @param poMap
     * @throws Exception
     */
    public void postUserData(String url,Map<String,List<User>> poMap) throws Exception {
        List insertList=poMap.get("addList");
        if(null!=insertList&&insertList.size()!=0) {
            HashMap<String, List> postInMap = new HashMap<>();
            logger.info("sync>> insertListSize:"+insertList.size());
            postInMap.put("insertUser", insertList);
            String insertJson = com.alibaba.fastjson.JSON.toJSONString(postInMap);
            sendPostData2BPM(url, insertJson);
        }
        List updateList = poMap.get("modiList");
        if(null!=updateList&&updateList.size()!=0) {
            HashMap<String, List> postUpMap = new HashMap<>();
            postUpMap.put("upUser", updateList);
            logger.info("sync>> updatelListSize:"+updateList.size());
            String upJson = com.alibaba.fastjson.JSON.toJSONString(postUpMap);
            sendPostData2BPM(url, upJson);
        }
        List<User> delList = poMap.get("delList");
        if(null!=delList&&delList.size()!=0) {
            List<String> delAccountList = new ArrayList<>();
            for (User u:
                    delList) {
                delAccountList.add(u.getLoginName());
            }
            HashMap<String, List> postDelMap = new HashMap<>();
            postDelMap.put("delUser", delAccountList);
            logger.info("sync>> bpmDelList:"+delAccountList.toString());
            String delJson = com.alibaba.fastjson.JSON.toJSONString(postDelMap);
            sendPostData2BPM(url, delJson);
        }
    }
    @Scheduled(cron = "${syncEpcOrgData.cron}")
    public void syncOrgAddData() throws Exception {
        this.syncAddOrgData(null);
    }

    /**UV123
     * 增量同步组织数据
     * @param syncDate
     * @throws Exception
     */
    @Async
    @Override
    public void syncAddOrgData(String syncDate) throws Exception {

        //>>>>>>>>>>>>>>>>>>>>>>测试阶段本地获取>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
        //String path2 = "D:/fileStorage/download/json/31orgjson.json";
        // String path2 = "D:/fileStorage/download/json/31orgless.json";
        //  String String = readJsonFile(path2);
        //net.sf.json.JSONObject allData = net.sf.json.JSONObject.fromObject(String);
        //  net.sf.json.JSONArray entity =  allData.getJSONObject("entity").getJSONArray("list");
        //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


        // 1 从三方接口获取json数据,filter过滤未更新数据
        net.sf.json.JSONArray entity = getSuYanData(getSuYanOrgsUrl);
        // 2.1 全量获取
        List<Map<String,String>> depts = JSON2Dept(entity,SUZHOU_SYNC,"department");
        //List<Map<String,String>> f = new ArrayList<>();
        //如果同步时间不为空,则过滤陈旧数据
        if(null!=syncDate){
            depts = depts.parallelStream().filter(org ->
                    compareTime((String) JSONObject.fromObject(org).get("createdAt"), syncDate)
                            || compareTime((String) JSONObject.fromObject(org).get("updatedAt"), syncDate)
            ).collect(Collectors.toList());
        }
        // 3 遍历比对分类 INSET UPDATE DELETE
        List<Map<String,String>> insertList = new ArrayList<>();
        List<Map<String,String>> updateList = new ArrayList<>();

        List<String> uuids = orgSyncManager.getOrgidsByYyncSource(SUZHOU_SYNC);
        HashMap<String,String> orgMap = new HashMap();
        for(int i = 0; i<uuids.size(); i++){
            orgMap.put(uuids.get(i),"1");
        }

        for (Map m:
                depts) {
            if("1".equals(orgMap.get(m.get("uuid")))){
                updateList.add(m);
                orgMap.remove(m.get("uuid"));
            } else {
                insertList.add(m);
            }

        }

        orgSyncManager.insertOrg(insertList,SUZHOU_SYNC);
        //orgSyncManager.updateOrg(updateList);
        orgSyncManager.setLastSyncTimeString(SUZHOU_SYNC,"department");
        logger.debug("同步到UMS完成");
        HashMap<String,List> postInMap = new HashMap<>();
        postInMap.put("insertOrg",insertList);
        postInMap.put("updateOrg",updateList);

        String postJson=com.alibaba.fastjson.JSON.toJSONString(postInMap);


        logger.debug("推送新增组织数量:"+insertList.size());
        logger.debug("推送更新组织数量:"+updateList.size());


        Thread post = new Thread(()->{unirestPostOrgs2Bpm(bpmOrgUrl,postJson);});
        post.start();
    }

    public void unirestPostOrgs2Bpm(String url,String data){
        kong.unirest.HttpResponse<JsonNode> jsonResponse =
                Unirest.post(url)
                        .header("head_orgAccount", "alauda")
                        .header("head_userName", "alauda")
                        .header("Content-Type", "application/json")
                        .body(data).asJson();


        int status = jsonResponse.getStatus();
        String s = jsonResponse.getBody().getArray().get(0)+"";
        logger.debug("同步状态"+status+"同步结果:"+s);
        System.out.println("同步状态"+status);
    }




    /**苏研通用方法 耦合处:jsonObject.getJSONObject("entity")
     * 获取苏研Token
     * @return
     */
    @Override
    //@Scheduled(cron = "0 0/8 * * * ?" )
    public com.alibaba.fastjson.JSONObject getSuZhouOautToken() throws IOException {
        com.alibaba.fastjson.JSONObject responseEntity = null;
        logger.info("************", port);
        logger.info("sysdata.Token.url is : {}", sendUrl);
        logger.info("用户名" + userName);
        logger.info("密码" + password);

        // 准备请求相关信息
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpPost postMethod = new HttpPost(sendUrl);
        postMethod.addHeader("Content_Type", "application/x-www-form-urlencoded");
        postMethod.addHeader("Authorization", "Basic Q0xJRU5UOlNFQ1JFVA==");
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("username", userName));
        params.add(new BasicNameValuePair("password", password));
        HttpEntity paramEntity = new UrlEncodedFormEntity(params, "UTF-8");
        postMethod.setEntity(paramEntity);
        HttpResponse response = null;
        try {
            response = httpClient.execute(postMethod);
            logger.info("请求信息" + postMethod);
            logger.info("请求信息" + postMethod.getEntity().getContent().toString());

            // 请求发送成功并得到响应
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                // 读取响应字符串
                HttpEntity httpEntity = response.getEntity();
                String entityStr = EntityUtils.toString(httpEntity);
                logger.info("call get token result is : {}", entityStr);
                // 将字符串转换成Json对象
                com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSONObject.parseObject(entityStr);
                responseEntity = jsonObject.getJSONObject("entity");
                return responseEntity;
            }
        } catch (Exception e) {
            logger.error("请求获取Token失败:", e);
            throw e;
        }
        return responseEntity;
    }

    /** TODO 此方法前提是, 对方更新参数的时候 没有漏更新updateAt的情况, 否则去除过滤 全量更新
     * 比较时间 前参 晚于 后参 返回 true
     * @param orgTime
     * @param sync
     * @return
     */
    boolean compareTime(String orgTime,String sync){
        boolean over = true;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格
        Date orgd = null;
        try {
            if(orgTime.equals("")){return true;}
            orgd = df.parse(orgTime);
            if(null==sync){return orgd.compareTo(new Date())==1?true:false;}
            over = orgd.compareTo(df.parse(sync))==1?true:false;;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return over;
    }
    /**
     * 解析json转成Map数据
     * @param srcList      三方接口获取的数据
     * @param syncSource 数据源
     *     1 根据数据源获取字段对应表
     *     2 json转dept
     *      1) 根据表中来源字段名取数据 存入dept对象中     *
     *     3 判断是否增量数据 过滤陈旧数据(新增和更新时间大于 上一次同步数据的时间)
     */
    public  List<Map<String,String>> JSON2Dept( net.sf.json.JSONArray srcList,String syncSource,String desTableName){
        //1.从数据库取到字段对应表
        List<Map<String, Object>> relationList = orgSyncManager.getFieldRelation(syncSource,desTableName);
        List<Map<String,String>> dlist = new ArrayList<>();
        logger.debug("获取到字段对应表");

        for (Object object : srcList) {
            net.sf.json.JSONObject o = JSONObject.fromObject(object);
            Map<String,String> map = new HashMap<>(9);
            for (Map relation:relationList) {
                map.put(relation.get("DESTINATION_FIELD_NAME")+"",(o.get(relation.get("SOURCE_FIELD_NAME")).equals("null")?"":o.get(relation.get("SOURCE_FIELD_NAME")))+"");   /*relation.get("SOURCE_FIELD_NAME")*/
            }
            dlist.add(map);
        }

        return dlist;
    }

    /**苏研通用方法:耦合处为解析JSON结构的层次和key
     * 获取苏研JSON数据  转为JSONArray
     * @param getUrl
     * @return
     * @throws Exception
     */
    @Override
    public JSONArray getSuYanData(String getUrl) throws Exception {
        // 获取token结果数据
        com.alibaba.fastjson.JSONObject token = this.getSuZhouOautToken();
        logger.info("+++++++++获取Token:" + token);
        if (token == null) {
            throw new Exception("获取的Token为NULL");
        }
        String access_token = (String) token.get("access_token");
        if (StringUtils.isEmpty(access_token)) {
            throw new Exception("未获取到access_token异常");
        }


       /*  .url("http://10.144.91.163:30110/madrids/v1/external-platform/organizations")
                .method("GET", null)
                .addHeader("Authorization", "bearer c26eebdc-edd8-40d8-89ad-8efe033ff94f")*/
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpGet getMethod = new HttpGet(getUrl);
        String value =  access_token;
        logger.info("请求头的信息value：" + value);
        getMethod.addHeader("Authorization", "bearer "+access_token);
        HttpResponse response = null;
        net.sf.json.JSONArray entity = null;
        try {
            response = httpClient.execute(getMethod);
            // 数据解析处理
            String responseStr = EntityUtils.toString(response.getEntity());
            logger.info("call sync users interface return is : " + getUrl);
            logger.info("call sync users interface status is : " + response.getStatusLine().getStatusCode());
            //logger.info("call sync users interface return is : " + responseStr);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                net.sf.json.JSONObject allData = net.sf.json.JSONObject.fromObject(responseStr);
                entity =  allData.getJSONObject("entity").getJSONArray("list");

                // JSONObject jsonObject = JSONObject.parseObject(responseStr);
                //  entity = jsonObject.getJSONArray("entity");
                ;
                return entity;
            } else {
                logger.error("同步用户数据失败:" + getUrl);
            }
        } catch (Exception e) {
            logger.error("同步用户数据失败:" + getUrl, e);
            throw new Exception("同步用户数据失败");
        }

        return entity;
    }





    //读取json文件
    public static String readJsonFile(String fileName) {
        String jsonStr = "";
        try {
            File jsonFile = new File(fileName);
            FileReader fileReader = new FileReader(jsonFile);
            Reader reader = new InputStreamReader(new FileInputStream(jsonFile),"utf-8");
            int ch = 0;
            StringBuffer sb = new StringBuffer();
            while ((ch = reader.read()) != -1) {
                sb.append((char) ch);
            }
            fileReader.close();
            reader.close();
            jsonStr = sb.toString();
            return jsonStr;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }



    /**同步到BPM Post 用户数据
     * @param postUrl
     * @return
     * @throws Exception
     */
    public void sendPostData2BPM(String postUrl,String data) throws Exception {




       /*  .url("http://10.144.91.163:30110/madrids/v1/external-platform/organizations")
                .method("GET", null)
                .addHeader("Authorization", "bearer c26eebdc-edd8-40d8-89ad-8efe033ff94f")*/
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpPost postMethod = new HttpPost(postUrl);



       // postMethod.addHeader("head_orgAccount","alauda");
        //postMethod.addHeader("head_userName","alauda");
        //postMethod.set
        StringEntity se = new StringEntity(data, Charset.forName("UTF-8"));
        postMethod.setEntity(se);
        HttpResponse response = null;
        net.sf.json.JSONArray entity = null;
        try {
            response = httpClient.execute(postMethod);
            // 数据解析处理
            String responseStr = EntityUtils.toString(response.getEntity());
            logger.info("call sync users interface return is : " + postUrl);
            logger.info("call sync users interface status is : " + response.getStatusLine().getStatusCode());
            //logger.info("call sync users interface return is : " + responseStr);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                logger.error("同步数据成功:" + postUrl);
            } else {
                logger.error("同步数据失败:" + postUrl);
            }
        } catch (Exception e) {
            logger.error("同步数据失败:" + postUrl, e);
            throw new Exception("同步数据失败");
        }


    }



    /**
     * 增量同步组织数据
     * @param syncDate
     * @throws Exception
     */

    public CommonResult insertData(String syncDate) throws Exception {
        CommonResult res = new CommonResult();
        //>>>>>>>>>>>>>>>>>>>>>>测试阶段本地获取>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
        // String path2 = "D:/fileStorage/download/json/31orgjson.json";
        String path2 = "D:/fileStorage/download/json/31orgless.json";
        String String = readJsonFile(path2);
        net.sf.json.JSONObject allData = net.sf.json.JSONObject.fromObject(String);
        net.sf.json.JSONArray entity =  allData.getJSONObject("entity").getJSONArray("list");
        //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


        // 1 从三方接口获取json数据,filter过滤未更新数据
        // net.sf.json.JSONArray entity = getSuYanData(getSuYanOrgsUrl);
        // 2.1 全量获取
        List<Map<String,String>> depts = JSON2Dept(entity,SUZHOU_SYNC,"department");
        List<Map<String,String>> f = new ArrayList<>();
        //如果同步时间不为空,则过滤陈旧数据
        if(null!=syncDate){
            depts = depts.parallelStream().filter(org ->
                    compareTime((String) JSONObject.fromObject(org).get("createdAt"), syncDate)
                            || compareTime((String) JSONObject.fromObject(org).get("updatedAt"), syncDate)
            ).collect(Collectors.toList());
        }
        // 3 遍历比对分类 INSET UPDATE DELETE
        List<Map<String,String>> insertList = new ArrayList<>();
        List<Map<String,String>> updateList = new ArrayList<>();
        /*for (Map m:
             depts) {
            if(orgSyncManager.getCountById(m.get("uuid")+"")){updateList.add(m);}
            else {insertList.add(m);}
        }*/
        List<String> uuids = orgSyncManager.getOrgidsByYyncSource(SUZHOU_SYNC);
        HashMap<String,String> orgMap = new HashMap();
        for(int i = 0; i<uuids.size(); i++){
            orgMap.put(uuids.get(i),"1");
        }

        for (Map m:
                depts) {
            if("1".equals(orgMap.get(m.get("uuid")))){
                updateList.add(m);
                orgMap.remove(m.get("uuid"));
            } else {
                insertList.add(m);
            }


        }

        //orgSyncManager.insertOrg(insertList,SUZHOU_SYNC);
        // orgSyncManager.updateOrg(updateList);
        //orgSyncManager.setLastSyncTimeString(SUZHOU_SYNC,"department");
        HashMap<String,List> postInMap = new HashMap<>();
        postInMap.put("insertOrg",insertList);
        postInMap.put("updateOrg",updateList);
        String insertJson=com.alibaba.fastjson.JSON.toJSONString(postInMap);
        res.setValue(insertJson);
        return res;
    }
    //>>>>>>>>>>>>>>>>>>>>>>>addUser>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
 /*     //一用户 对应多组织需要单独处理 先删除再添加
        for(int i = 0; i<udMapList.size(); i++){
            uuMap.put((String)udMapList.get(i).get("uuid"),(String)udMapList.get(i).get("dept_id"));
            //uuMap.put(udMapList.get(),"1");
        }
        List<Map<String,String>> insertList = new ArrayList<>();
        List<Map<String,String>> updateList = new ArrayList<>();
        List<String> delList = new ArrayList<>();

        HashMap<String,String> modiUDMap = new HashMap();
        for (Map m:
                users) {
            if(null!=uuMap.get(m.get("uuid"))){
                if(!uuMap.get(m.get("uuid")).equals(m.get("dept_Id"))){
                    modiUDMap.put((String)m.get("uuid"),(String)m.get("dept_Id"));
                }
                updateList.add(m);
                m.put("delStatus",1);
                uuMap.remove(m.get("uuid"));
            } else {
                insertList.add(m);
            }
        }

           for (Map m:
                users) {
            if("1".equals(uuMap.get(m.get("uuid")))){
                updateList.add(m);
                m.put("delStatus",1);
                uuMap.remove(m.get("uuid"));
            } else {
                insertList.add(m);
            }


        }
        if(uuMap.size()!=0){
            uuMap.entrySet()
                    .stream()
                    .forEach(entry ->delList.add(entry.getKey()));
        }
        */



}