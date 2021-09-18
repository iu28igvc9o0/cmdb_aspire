// package com.migu.tsg.microservice.atomicservice.composite.controller.desk;
//
// import cn.hutool.http.HttpRequest;
// import com.alibaba.fastjson.JSON;
// import com.aspire.mirror.common.entity.PageResult;
// import com.aspire.mirror.composite.service.desk.CloudCustomerServiceAPI;
// import com.migu.tsg.microservice.atomicservice.composite.controller.rbac.RbacUserController;
// import com.migu.tsg.microservice.atomicservice.composite.service.rbac.payload.UserQueryPagePayload;
// import com.migu.tsg.microservice.atomicservice.composite.service.rbac.payload.UserResponse;
// import net.sf.json.JSONObject;
// import org.apache.commons.collections.CollectionUtils;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.http.HttpEntity;
// import org.springframework.http.HttpHeaders;
// import org.springframework.http.HttpMethod;
// import org.springframework.web.bind.annotation.RequestParam;
// import org.springframework.web.bind.annotation.RestController;
// import org.springframework.web.client.RestTemplate;
// import org.springframework.web.util.UriComponentsBuilder;
//
// import java.security.MessageDigest;
// import java.text.SimpleDateFormat;
// import java.util.*;
//
/// **
// * @projectName: DeskStaffAPI
// * @description: 接口
// * @author: menglinjie
// * @create: 2020-10-28 10:37
// **/
// @RestController
// public class CloudCustomerServiceController implements CloudCustomerServiceAPI {
//
//
// @Autowired
// private RbacUserController rbacUserController;
//
// @Value("${ccs.accountId:N00000052707}")
// protected String accountId;
// @Value("${ccs.exten:8000}")
// protected String exten;
// @Value("${ccs.password:GJ!peHoAR8000}")
// protected String ccsPassword;
// @Value("${ccs.host:https://apis.7moor.com}")
// protected String ccsHost;
// @Value("${ccs.url:/v20160818/sso/getToken/}")
// protected String ccsUrl;
// @Value("${ccs.accountAPISecret:48dd39d0-1a91-11eb-92e5-cbff6b7100bd}")
// protected String accountAPISecret;
// @Value("${ccs.str:7moor}")
// protected String ccsStr;
// @Value("${ccs.getTemplateUrl:/v20170418/customer/getTemplate/}")
// protected String getTemplateUrl;
// @Value("${ccs.customerInsertUrl:/v20170418/customer/insert/}")
// protected String customerInsertUrl;
//
// @Override
// public Object getToken(@RequestParam("module")String module) {
// Date date = new Date();
// SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
// String temp = sf.format(date);
//
//
// //生成请求参数（sig）
// String sig = getSig(temp);
//
// //生成登录密码（加密）
// String encryptPw = getEncryptPw(temp);
//
// //封装请求体
// Map<String, Object> params = new HashMap<>();
// params.put("account",accountId);
// params.put("exten",exten);
// params.put("password",encryptPw);
// params.put("extentype","null");
// params.put("timeStamp",temp);
// params.put("module",module);
//
// String urlStr = ccsHost + ccsUrl + accountId +"?sig=" + sig;
// return this.httpPost(temp,urlStr,params);
//
// }
//
// @Override
// public Object getTemplate() {
// Date date = new Date();
// SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
// String temp = sf.format(date);
// String sig = getSig(temp);
// String urlStr = ccsHost + getTemplateUrl + accountId +"?sig=" + sig;
// return httpGet(temp,urlStr,new HashMap<>(8));
// }
//
// @Override
// public Object insert() {
// Map<String,Object> resultMap = new HashMap<>(8);
//
// Date date = new Date();
// SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
// String temp = sf.format(date);
// String sig = getSig(temp);
//
// //获取数据库版本，客户资料的字段（因为composite没有Redis工具，暂时没有将数据库版本放Redis）
// String templateUrlStr = ccsHost + getTemplateUrl + accountId +"?sig=" + sig;
// Object result = httpGet(temp,templateUrlStr,new HashMap<>(8));
// net.sf.json.JSONObject jsonObject = net.sf.json.JSONObject.fromObject(result);
// net.sf.json.JSONObject jsonObject1 = (JSONObject) jsonObject.get("body");
// Integer code = (Integer) jsonObject1.get("code");
// int total = 0;
// if (!code.equals(200)){
// resultMap.put("code",500);
// resultMap.put("message","get version fail");
// return resultMap;
// }else {
// net.sf.json.JSONObject jsonObject2 = (JSONObject) jsonObject1.get("data");
// //数据库版本
// String version = (String) jsonObject2.get("version");
// //获取用户
// for (int i = 1; i < 100; i++) {
// UserQueryPagePayload request = new UserQueryPagePayload();
// request.setPageSize(500);
// request.setPageNo(i);
// PageResult<UserResponse> userResponsePageResult = findUserList(request);
// total = userResponsePageResult.getCount();
// List<UserResponse> userResponseList = userResponsePageResult.getResult();
//
// List<Map<String,Object>> mapList = new ArrayList<>();
// if (CollectionUtils.isNotEmpty(userResponseList)){
// for (UserResponse userResponse : userResponseList){
//
// Map<String,Object> map = new HashMap<>();
// map.put("status","status0");
// map.put("source","ums");
// map.put("name",userResponse.getCode());
// map.put("title",userResponse.getName());
// map.put("email",userResponse.getMail());
// map.put("phone",userResponse.getMobile());
// map.put("owner",exten);
// map.put("真实姓名",userResponse.getName());
// map.put("部门",userResponse.getDeptId());
// map.put("性别",userResponse.getSex());
// if (userResponse.getUserType().equals(1)){
// map.put("用户类型","正式用户");
// }else {
// map.put("用户类型","临时用户");
// }
// map.put("工号",userResponse.getNo());
// map.put("办公电话",userResponse.getPhone());
// map.put("address",userResponse.getAddress());
// map.put("传真",userResponse.getFax());
// map.put("职责",userResponse.getPost());
// mapList.add(map);
// }
// //封装请求体
// Map<String, Object> params = new HashMap<>(8);
// params.put("version",version);
// params.put("customers",JSON.toJSONString(mapList));
// String urlStr = ccsHost + customerInsertUrl + accountId +"?sig=" + sig;
// Object object = httpPost2(temp,urlStr,params);
// net.sf.json.JSONObject jsonObject3 = net.sf.json.JSONObject.fromObject(object);
// Integer code2 = (Integer) jsonObject3.get("code");
// if (!code2.equals(200)) {
// resultMap.put("code",500);
// resultMap.put("message","Insert data fail");
// return resultMap;
// }
// if (userResponseList.size()<500){
// break;
// }
// }
// }
// }
// resultMap.put("code",200);
// resultMap.put("message","Insert customer "+ total+" success！");
// return resultMap;
// }
//
// private PageResult<UserResponse> findUserList(UserQueryPagePayload request) {
// return rbacUserController.pageList(request);
// }
//
// private Object httpPost(String temp, String urlStr, Map<String, Object> params) {
// //生成请求头
// String authorization = getAuthorization(temp);
//
// HttpHeaders requestHeaders = new HttpHeaders();
// requestHeaders.add("Authorization", authorization);
// requestHeaders.add("Content-Type", "application/json;charset=utf-8");
// RestTemplate restTemplate = new RestTemplate();
// UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(urlStr);
// params.entrySet().stream().forEach(o -> builder.queryParam(o.getKey(), o.getValue()));
// String url = builder.build().encode().toString();
// @SuppressWarnings("rawtypes")
// HttpEntity formErntity = new HttpEntity<>(params, requestHeaders);
// Object result = restTemplate.exchange(url, HttpMethod.POST, formErntity, Object.class, params);
// return result;
// }
//
// public Object httpGet(String temp, String urlStr, Map<String, Object> params) {
// //生成请求头
// String authorization = getAuthorization(temp);
//
// HttpHeaders requestHeaders = new HttpHeaders();
// requestHeaders.add("Authorization", authorization);
// requestHeaders.add("Content-Type", "application/json;charset=utf-8");
// RestTemplate restTemplate = new RestTemplate();
// UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(urlStr);
// params.entrySet().stream().forEach(o -> builder.queryParam(o.getKey(), o.getValue()));
// String url = builder.build().encode().toString();
// @SuppressWarnings("rawtypes")
// HttpEntity formErntity = new HttpEntity<>(params, requestHeaders);
// Object result = restTemplate.exchange(url, HttpMethod.POST, formErntity, Object.class, params);
// return result;
// }
//
//
// private String getEncryptPw(String temp) {
// String str = ccsStr + accountId + exten + ccsPassword + temp;
// return angelinaMD5(str);
// }
//
// private String getSig(String temp) {
// String str = accountId + accountAPISecret + temp;
// return capitalMD5(str);
// }
//
// private String getAuthorization(String temp) {
// String str = accountId + ":" + temp;
// final Base64.Encoder encoder = Base64.getEncoder();
// return encoder.encodeToString(str.getBytes());
// }
//
// //MD5算法生成32位小写
// public String angelinaMD5(String str) {
// try {
// MessageDigest md = MessageDigest.getInstance("MD5");
//
// md.update(str.getBytes());
//
// byte b[] = md.digest();
//
// int i=0;
//
// StringBuffer buf = new StringBuffer("");
// for (int offset = 0; offset < b.length; offset++) {
// i = b[offset];
// if (i < 0)
// i += 256;
// if (i < 16)
// buf.append("0");
// buf.append(Integer.toHexString(i));
// }
// str = buf.toString();
// } catch (Exception e) {
// e.printStackTrace();
//
// }
// return str;
// }
//
// //MD5算法生成32位大写
// public String capitalMD5(String str)
// {
// String result=null;
//
// result=angelinaMD5(str).toUpperCase();
//
// return result;
// }
//
// public Object httpPost2(String temp, String urlStr, Map<String, Object> params) {
// //生成请求头
// String authorization = getAuthorization(temp);
//
// Object result = null;
// try {
// String version = (String) params.get("version");
// String customers = (String) params.get("customers");
// String str = "{\"customers\":"+customers+",\"version\":"+"\""+version+"\"}";
// //头信息，多个头信息多次调用此方法即可
// result = HttpRequest.post(urlStr)
// .header("Authorization", authorization)
// .header("Content-Type", "application/json;charset=utf-8")
// //表单内容
// .body(str)
// //超时，毫秒
// .timeout(10000)
// .execute().body();
// }catch (Exception e){
// // 查询失败
// e.printStackTrace();
// }
// return result;
// }
// }
