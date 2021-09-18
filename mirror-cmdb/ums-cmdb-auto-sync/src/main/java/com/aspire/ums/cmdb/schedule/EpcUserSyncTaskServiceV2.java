/**
 *
 */
package com.aspire.ums.cmdb.schedule;

import com.migu.tsg.microservice.atomicservice.rbac.dto.vo.UserVO;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aspire.ums.cmdb.common.StringUtils;
import com.aspire.ums.cmdb.service.impl.OrgSyncInterfaceServiceImpl;
import com.aspire.ums.cmdb.mapper.OrgSyncManager;
//import com.aspire.ums.cmdb.mapper.UserSyncManager;
import com.aspire.ums.cmdb.sync.client.LdapServiceClient;
import com.aspire.ums.cmdb.sync.client.RbacServiceClient;
import com.aspire.ums.cmdb.sync.entity.Office;
import com.aspire.ums.cmdb.sync.entity.User;
import com.aspire.ums.cmdb.sync.util.UmsWebServiceUtils;
import com.google.common.collect.Lists;
import com.migu.tsg.microservice.atomicservice.ldap.dto.GetLdapUserResponse;
import com.migu.tsg.microservice.atomicservice.ldap.dto.InsertLdapMemberRequest;
import com.migu.tsg.microservice.atomicservice.ldap.dto.ListPagenationResponse;
import com.migu.tsg.microservice.atomicservice.ldap.dto.UpdateLdapMemberRequest;
import com.migu.tsg.microservice.atomicservice.rbac.dto.UserBatchCreateRequest;
import com.migu.tsg.microservice.atomicservice.rbac.dto.UserCreateRequest;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;


import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author lupeng
 *
 */
@Component
//@ConditionalOnExpression("${schedule.epcUser.flag:false}")
public class EpcUserSyncTaskServiceV2 {

	protected static Logger logger = LoggerFactory.getLogger(EpcUserSyncTaskServiceV2.class);
	@Value("${ldapconfig.namespace:alauda}")
	private String namespace;

	@Value("${sync.yunGuan.tokenUrl:http://10.144.91.163:30110/oauthserver/oauth/token}")
	private String sendUrl;
	@Value("${sync.yunGuan.userName:IWbqqFuqHw7GIqlWZRevR1NUothJR4VDMpO0NfGcv1r+RyIlWoaamJ+d/mL/bcrunv3B5D7y6hsIza6KmEct6Fa3hMbU4VLuA6mAaiRhFNUCKCiJd/renkZV2ddhTH7EeqHXy2g808c7HnXLWKsA7JgkYHx/sQ6UGV+lN+EoTZM=}")
	private String userName;
	@Value("${sync.yunGuan.password:YOeEctAnr2qXR/3sl1vlPyI3ox1GjRLE+NhacmUEjkDBgwgr1R8HO+/d9lLRmNs27Kn966YYfs/AUYmgZZekjmZqN0GiUON6/mhPbkAruIgn95QEI4fh5npYxWIvbTtFjvPbM+QN1fNNpZX/up0e1QV/BT6yIkxrREJo3vcvt9s=}")
	private String password;
	@Value("${sysdata.Token.username}")
	private String userNameForOld;
	@Value("${sysdata.Token.password}")
	private String passwordForOld;
	@Value("${sync.yunGuan.userUrl:http://10.144.91.163:30110/madrids/v1/external-platform/users?&platformName=BPM工单}")
	private String sysUrl;

	@Value("${sync.bpm.user:http://localhost:8088/api/YunGuanSync/v1/getSuYanData}")
	private String bpmUserUrl;

	//sysdata.Epcuser.url
	@Value("${sysdata.Epcuser.url}")
	private String sysUrlForOld;

	@Value("${sysdata.Token.url}")
	private String tokenUrlForOld;

	@Value("${server.port}")
	private String port;

	@Value("${cmic.org.user.default.role.id}")
	private static String DEFAULT_ROLE_ID;

	@Value("${cmic.org.user.default.user.type}")
	private static String DEFAULT_USER_TYPE;

	@Value("${rabc.sysadmin.roleid}")
	private String ROLE_ID;

	@Value("${rabc.root.departmentid}")
	private String DEPARTMENT_ID;
	//同步来源标识, 之前设计, 是把标识存在descr字段,故 沿用
	@Value("EPC")
	private String descr;
	@Value("${sysdata.Epcuser.applicationName}")
	private String applicationName;
	// private final static int pagesize = 200000;

	// private String md5 = "{MD5}";
	@Autowired
	private OrgSyncInterfaceServiceImpl orgservice;

	@Autowired
	private LdapServiceClient ldapServiceClient;

	@Autowired
	private RbacServiceClient rbacServiceClient;
	@Autowired
	private UmsWebServiceUtils umsWebServiceUtils;
	@Autowired
	private OrgSyncManager orgSyncManager;
	@Value("${bomc.enable.sync.status:false}")
	private boolean enableSyncStatus;
	/*@Autowired
	private UmsWebServiceUtils umsWebServiceUtils;*/
	//"0 0/30 * * *  ?"
	//@Scheduled(cron = "${syncEpcUserData.cron}")
	public void syncEpcUserData() {
		logger.info("定时任务：同步用户执行开始...");
		try {
			sysEpcUserData();
			if (enableSyncStatus) {
				System.out.println("ZWN_卓望新工单开始测试");
				this.syncStatus("ZWN_卓望新工单");
				this.syncStatus(this.applicationName);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void syncStatus(String applicationName) throws Exception {
		// 获取token结果数据
		JSONObject token = this.getTokenWithOldUrl();
		logger.info("+++++++++获取Token:" + token);
		if (token == null) {
			throw new Exception("获取的Token为NULL");
		}
		String access_token = (String) token.get("access_token");
		if (StringUtils.isEmpty(access_token)) {
			throw new Exception("未获取到access_token异常");
		}
		StringBuffer sendUrl = new StringBuffer(sysUrlForOld).append("?" +"applicationName="+ applicationName+"&&token=" + access_token);
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpGet getMethod = new HttpGet(sendUrl.toString());
		try {
			httpClient.execute(getMethod);
		} catch (Exception e) {
			logger.error("同步用户数据失败:" + sendUrl, e);
			throw new Exception("同步用户数据失败");
		}
	}

	/**
	 * 获取token
	 *
	 * @return
	 * @throws Exception
	 */
	public JSONObject getToken() throws Exception {
		JSONObject responseEntity = null;
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
				JSONObject jsonObject = JSONObject.parseObject(entityStr);
				responseEntity = jsonObject.getJSONObject("entity");
				return responseEntity;
			}
		} catch (Exception e) {
			logger.error("请求获取Token失败:", e);
			throw e;
		}
		return responseEntity;
	}

	/**
	 * 获取token
	 *
	 * @return
	 * @throws Exception
	 */
	public JSONObject getTokenWithOldUrl() throws Exception {
		JSONObject responseEntity = null;
		logger.info("************", port);

		logger.info("sysdata.Token.url is : {}", tokenUrlForOld);
		logger.info("用户名" + userNameForOld);
		logger.info("密码" + passwordForOld);

		// 准备请求相关信息
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpPost postMethod = new HttpPost(tokenUrlForOld);
		postMethod.addHeader("Content_Type", "application/x-www-form-urlencoded");
		postMethod.addHeader("Authorization", "Basic Q0xJRU5UOlNFQ1JFVA==");
		List<NameValuePair> params = new ArrayList<>();
		params.add(new BasicNameValuePair("username", userNameForOld));
		params.add(new BasicNameValuePair("password", passwordForOld));
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
				JSONObject jsonObject = JSONObject.parseObject(entityStr);
				responseEntity = jsonObject.getJSONObject("entity");
				return responseEntity;
			}
		} catch (Exception e) {
			logger.error("请求获取Token失败:", e);
			throw e;
		}
		return responseEntity;
	}
	public JSONArray getEpcUserData() throws Exception {

		// 获取token结果数据
		com.alibaba.fastjson.JSONObject token = this.getToken();
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
		HttpGet getMethod = new HttpGet(sysUrl);
		String value =  access_token;
		logger.info("请求头的信息value：" + value);
		getMethod.addHeader("Authorization", "bearer "+access_token);
		HttpResponse response = null;
		JSONArray entity = null;
		try {
			response = httpClient.execute(getMethod);
			// 数据解析处理
			String responseStr = EntityUtils.toString(response.getEntity());
			logger.info("call sync users interface return is : " + sysUrl);
			logger.info("call sync users interface status is : " + response.getStatusLine().getStatusCode());
			//logger.info("call sync users interface return is : " + responseStr);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				JSONObject jsonObject = JSONObject.parseObject(responseStr).getJSONObject("entity");
				entity = jsonObject.getJSONArray("list");

				return entity;
			} else {
				logger.error("同步用户数据失败:" + sysUrl);
			}
		} catch (Exception e) {
			logger.error("同步用户数据失败:" + sysUrl, e);
			throw new Exception("同步用户数据失败");
		}

		return entity;
	}

	/**
	 * 同步用户
	 *
	 * @param
	 * @return
	 */

	public void sysEpcUserData() throws Exception {
		//JSONArray entity = this.getEpcUserData();

		JSONArray entity = this.getUserDataByTestFile();
		if (entity == null) {
			throw new Exception("获取的用户为NULL");
		}//UV0001
		// 录入数据到网管平台
		Map<String, List<User>> userMap = syncEpcUserToOsa(entity);
		// 同步数据到BPM
		orgservice.postUserData(bpmUserUrl,userMap);

	}

	private JSONArray getUserDataByTestFile() {
		String path2 = "D:json/user106.json";
		String String = readJsonFile(path2);
		JSONArray entity = JSONObject.parseArray(String);
		return entity;
	}

	/**
	 * 过滤portal ws 接口返回的无用user
	 *
	 * @param entity
	 * @return
	 */
	private Map<String, List<User>> syncEpcUserToOsa(JSONArray entity) {

//		String namespace = "alauda";
		String projects = null;
		List<String> usernames = new ArrayList();
		List<String> projectss = new ArrayList();
		List<String> orderBy = new ArrayList();
		int pageSize = 200000;
		int currentPage = 1;

		logger.info("SyncCmicUserData: filter cmic users data start");
		Map<String, List<User>> returnMap = new HashMap<>();
		List<User> addList = new ArrayList<>();
		List<User> modiList = new ArrayList<>();
		List<User> delList = new ArrayList<>();
		List<User> addListProcess = new ArrayList<>();

		List<String> delSyncAccounts = new ArrayList<>();
		boolean hasDel = false;

		// add jisnu 新增用户 修改用户
		List<UserCreateRequest> userCreateRequestList = Lists.newArrayList();

//		List<UserUpdateRequest> userUpdateRequestList = Lists.newArrayList();
		try {
			// 一 从ldap获取全量用户 封装成 key为login Name的 cnmsUserMap
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			Date createDate = new Date();
			String remark = sdf.format(createDate);
			String markAdd = remark + "_add";
			String markUpdate = remark + "_update";
			String markSuspend = remark + "_suspend";
			String markDelete = remark + "_delete";
			List<User> userList = orgSyncManager.getMysqlUserList();
//			ldapServiceClient.listLdapMember(namespace, null, null, projectss, orderBy,
//					pageSize, currentPage);
//			List<GetLdapUserResponse> results = response.getResults();
			logger.info("SyncCmicUserData listLdapMember");
			if (userList.size() > 0) {
				logger.info("Find mysql user size -> {}", userList.size());
				logger.info("Find BOMC user size -> {}", entity.size());
//				List<User> list = new ArrayList<>();
//
//				for (GetLdapUserResponse result : results) {
//					// list.add((User) ClassConvertUtil.convertClass(result,
//					// GetLdapUserResponse.class));
//					User user = new User();
//					user.setLoginName(result.getUsername());
//					user.setMobile(result.getMobile());
//					user.setName(result.getName());
//					user.setEmail(result.getMail());
//					user.setPassword(result.getPassword());
//					user.setCreateDate(sdf.parse(result.getCreateTime()));
//					user.setFrom("EPC");
//					// 增加source_uuid属性
//					list.add(user);
//				}
				Map<String, User> cnmsUserMap = new HashMap<>(userList.size());
				List<String> interfaceUser = new ArrayList<>(userList.size());
				for (User user : userList) {
					cnmsUserMap.put(user.getLoginName(), user);
				}

				// 二  遍历 云管数据 取出遍历元素的 值,  用云管的username 去cnmsUserMap中匹配loginName
				// 2.1 ldap中没有 将 元素的值 放入new User中 存入addList
				//2.2 匹配到了  378 delList 逻辑待修改
				boolean isDel;
				for (int i = 0; i < entity.size(); i++) {
					JSONObject object = (JSONObject) entity.get(i);
					String id = (String) object.get("code");
					// String organizationId = (String) object.get("organizationId");
					// String applicationName = (String) object.get("applicationName");
					// String roleId = (String) object.get("roleId");
					//TODO 新接口此处不返回删除状态
					String status = object.get("status").equals("OFFLINE")?"SUSPEND":"";
					String suspendFlag = ((String) object.get("status"));
					// String effectTime = (String) object.get("effectTime");
					String name = (String) object.get("name");
					String username = (String) object.get("username");
					String password = (String) object.get("password"); //new User
					String email = (String) object.get("email");
					String mobile = (String) object.get("phone");
					//同步接口提供的组织编码
					String deptId = (String) object.get("organizationCode");
					User cmnsUser = cnmsUserMap.get(username);
					//汇总所有接口提供的用户账号
					interfaceUser.add(username);
					// Pattern loginNamePattern1 = Pattern.compile("^dw", Pattern.CASE_INSENSITIVE);
					// Pattern loginNamePattern2 = Pattern.compile("^dw_",
					// Pattern.CASE_INSENSITIVE);
					// String loginName = cmicUser.getPortalUserId();
					if (StringUtils.isEmpty(name)) {
						continue;
					}
					if (StringUtils.isEmpty(email)) {
						continue;
					}
//					String emailReg = "^([a-zA-Z0-9_\\-\\.]+)@\\w+(\\.\\w+)+";
//					if (!email.matches(emailReg)) {
//						continue;
//					}
					isDel = false;
					// 新增
					if (null == cmnsUser) {
						if ("DELETED".equalsIgnoreCase(status)) {
							continue;
						}
						// Matcher matcher1=loginNamePattern1.matcher(username);
						// //检查是否dw开头
						// if(matcher1.find()) {
						// //检查是否为dw_开头, dw_开头不修改账号名, dw开头自动加上下划线
						// if(!loginNamePattern2.matcher(username).find()) {
						// username = matcher1.replaceFirst("dw_");
						// }
						// }
						Office o = new Office();
						o.setId("1");
						cmnsUser = new User(id, o, // 物理公司
								o, // 物理部门
								username, password, name, email, mobile, DEFAULT_USER_TYPE, markAdd, createDate, deptId,descr,suspendFlag);
						addList.add(cmnsUser);
						// logger.info("add: "+cmicUser.getEmail());

					} else {
						switch (status.toUpperCase()) {
							case "SUSPEND":// 挂起
								cmnsUser.setRemarks(markSuspend);
								cmnsUser.setSuspendFlag("1");
								break;
							default:// 修改
								cmnsUser.setSuspendFlag("0");
								break;
						}
						// 二 2.3匹配到了 且云管status 不为DELETE , 比较云管和cmnsUser(ldap的值) 不相等的进入modilist
						if (!isDel) {
							// ldap中选择MD5的加密策略
							// StringBuffer sb = new StringBuffer();
							// sb.append(md5);
							// sb.append(EncryptUtil.encryptMd5(password));
							//
							// ldap中选择明文存入
							if (!password.equals(cmnsUser.getPassword()) || !mobile.equals(cmnsUser.getMobile())
									|| !email.equals(cmnsUser.getEmail()) || !name.equals(cmnsUser.getName())
							) {
								cmnsUser.setEmail(email);
								cmnsUser.setMobile(mobile);
								cmnsUser.setPassword(password);
								cmnsUser.setName(name);
								cmnsUser.setRemarks(markUpdate);
								cmnsUser.setDeptId(cmnsUser.getDeptId());
								//cmnsUser.setSuspendFlag(suspendFlag);
								// cmnsUser.setUpdateDate(createDate);
								modiList.add(cmnsUser);
							}
						}
					//	cnmsUserMap.remove(username);  //cnmsUserMap 中的ldap数据 有自有数据不能判断
					}


					// for (User tempUser : cnmsUserMap.values()) {
					// String loginName = tempUser.getLoginName();
					// if (!isDel&& StringUtils.isNotEmpty(loginName) &&
					// loginName.equalsIgnoreCase(username)) {
					// tempUser.setDelFlag("1");
					// tempUser.setRemarks("重复用户" + markDelete);
					// onlyDelList.add(tempUser);
					// }
					// }
				}

              //  cnmsUserMap.entrySet().iterator().forEachRemaining(item -> delList.add(item.getValue()));
				logger.info("Need to add user size -> {}", addList.size());
				logger.info("Need to update user size -> {}", modiList.size());


				// 三 遍历addList 封装UserCreateRequest 放入 rbac 插入list
				//    3.2 封装到insert, ldap 新增数据
				// 新增用户
				for (User user : addList) {
					// add jinsu 拼装用户数据
					UserCreateRequest rbacUser = new UserCreateRequest();
					rbacUser.setLdapId(user.getLoginName());
					rbacUser.setNamespace(namespace);
					rbacUser.setName(user.getName());
					// 设置用户类型为正式用户
					rbacUser.setUserType(1);
					rbacUser.setMail(user.getEmail());
					rbacUser.setMobile(user.getMobile());
					rbacUser.setDeptId(user.getDeptId());
					rbacUser.setRoles(ROLE_ID);
					rbacUser.setCode(user.getLoginName());
					rbacUser.setDescr("EPC");
					userCreateRequestList.add(rbacUser);
					logger.info("Add user info data. -> {}", JSONObject.toJSON(user));
					if (StringUtils.isEmpty(user.getEmail())) {
						continue;
					}

					if (StringUtils.isEmpty(user.getMobile())) {
						continue;
					}

					if (StringUtils.isEmpty(user.getName())) {
						continue;
					}


					List<InsertLdapMemberRequest> request = new ArrayList<>();
					InsertLdapMemberRequest insert = new InsertLdapMemberRequest();
					// request.add((InsertLdapMemberRequest)ClassConvertUtil.convertClass(user,InsertLdapMemberRequest.class));
					insert.setMobile(user.getMobile());
					insert.setName(user.getName());
					insert.setUsername(user.getLoginName());
					insert.setMail(user.getEmail());
					insert.setPassword(user.getPassword());
					request.add(insert);
					try {
						ldapServiceClient.insertLdapMembers(namespace, request);
					} catch (Exception e) {
						logger.error("Insert ldap error. ", e);
					}
				}
				logger.info("after add ldap menbers , userlist is :" + userCreateRequestList.size());
				// userDao.save(addList);
				// userDao.save(addListProcess); 第三方暂时不同步
				// contactsService.addOrgUsers(ouAddList);

				// 修改用户信息
				// 三 3.3 封装 请求 ldap
				//    放入rbaclist
				UpdateLdapMemberRequest req;
				for (User user : modiList) {
					// add jinsu 拼装用户数据
					UserCreateRequest rbacUser = new UserCreateRequest();
					rbacUser.setLdapId(user.getLoginName());
					rbacUser.setNamespace(namespace);
					rbacUser.setName(user.getName());
					// 设置用户类型为正式用户
					rbacUser.setUserType(1);
					rbacUser.setMail(user.getEmail());
					rbacUser.setMobile(user.getMobile());
					rbacUser.setDeptId(user.getDeptId());
					rbacUser.setRoles(ROLE_ID);
					rbacUser.setCode(user.getLoginName());
					rbacUser.setDescr("EPC");
					List<String> ds = Arrays.asList(user.getDeptId());
					rbacUser.setDeptIds(ds);
					userCreateRequestList.add(rbacUser);
					logger.info("Update user info data. -> {}", JSONObject.toJSON(user));
					if (StringUtils.isEmpty(user.getEmail())) {
						continue;
					}

					if (StringUtils.isEmpty(user.getMobile())) {
						continue;
					}

					if (StringUtils.isEmpty(user.getName())) {
						continue;
					}


					req = new UpdateLdapMemberRequest();
					req.setName(user.getName());
					req.setNewPassword(user.getPassword());
					req.setMail(user.getEmail());
					req.setMobile(user.getMobile());
					try {
						ldapServiceClient.updateLdapMember(namespace, user.getLoginName(), req);
					} catch (Exception e) {
						logger.error("Update ldap error.", e);
					}
				}
				logger.info("after mod ldap menbers , userlist is :" + userCreateRequestList.size());
				// userDao.save(modiList);
				// contactsService.delOrgUsers(ouDelList);
				// 删除用户
			/*	for (User user : delList) {
					ldapServiceClient.deleteLdapMember(namespace, user.getLoginName());
				}*/

				//UV删除ldap账号

				delSyncAccounts = getDelAccountList(interfaceUser);
				hasDel = (null != delSyncAccounts && delSyncAccounts.size() != 0);
				if (hasDel) {
					logger.info("sync>> start del user in ldap and rbac");
					for (String ac : delSyncAccounts) {
						try {
							ldapServiceClient.deleteLdapMember(namespace, ac);
							logger.info("sync>> Del ldapUser info data. -> {}", ac);
						}catch (Exception e){
							logger.error("del ldap user error.{}",e);
						}
						UserVO user = rbacServiceClient.findByLdapId(ac);
						if (null != user&&null!=user.getUuid()) {
							rbacServiceClient.deleteByPrimaryKey(user.getUuid());
							logger.info("sync>> Del rbacUser info ldapId:"+ac+"  uuid"+user.getUuid());
						}
					}
				}

			} else {
				// 当查出ldap数据列表为空时，直接进行新增用户操作
				for (int i = 0; i < entity.size(); i++) {
					JSONObject object = (JSONObject) entity.get(i);
					String id = (String) object.get("id");
					String status = (String) object.get("status");
					// String effectTime = (String) object.get("effectTime");
					String name = (String) object.get("name");
					String username = (String) object.get("username");
					String password = (String) object.get("password");
					String email = (String) object.get("email");
					String moile = (String) object.get("phone");
					String deptpId = (String) object.get("organizationCode");
					String suspendFlag = (String) object.get("status");
					if (StringUtils.isEmpty(name)) {
						continue;
					}
					if (StringUtils.isEmpty(email)) {
						continue;
					}
					String emailReg = "^([a-zA-Z0-9_\\-\\.]+)@\\w+(\\.\\w+)+";
					if (!email.matches(emailReg)) {
						continue;
					}

					User addUser;
					// 新增

					if (!"ONLINE".equalsIgnoreCase(status)) {
						continue;
					}
					// Matcher matcher1=loginNamePattern1.matcher(username);
					// //检查是否dw开头
					// if(matcher1.find()) {
					// //检查是否为dw_开头, dw_开头不修改账号名, dw开头自动加上下划线
					// if(!loginNamePattern2.matcher(username).find()) {
					// username = matcher1.replaceFirst("dw_");
					// }
					// }
					Office o = new Office();
					o.setId("1");
					addUser = new User(id, o, // 物理公司
							o, // 物理部门
							username, password, name, email, moile, DEFAULT_USER_TYPE, markAdd, createDate,deptpId,descr,suspendFlag);
					addList.add(addUser);
					List<InsertLdapMemberRequest> request = new ArrayList<>();
					if (StringUtils.isEmpty(addUser.getMobile())) {
						continue;
					}
					// add jinsu 拼装用户数据
					UserCreateRequest rbacUser = new UserCreateRequest();
					rbacUser.setLdapId(addUser.getLoginName());
					rbacUser.setNamespace(namespace);
					rbacUser.setName(addUser.getName());
					// 设置用户类型为正式用户
					rbacUser.setUserType(1);
					rbacUser.setMail(addUser.getEmail());
					rbacUser.setMobile(addUser.getMobile());
					rbacUser.setDeptId(deptpId);
					rbacUser.setRoles(ROLE_ID);
					rbacUser.setCode(addUser.getLoginName());
					rbacUser.setDescr(descr);
					List<String> ds = Arrays.asList(addUser.getDeptId());
					rbacUser.setDeptIds(ds);
					userCreateRequestList.add(rbacUser);

					// request.add((InsertLdapMemberRequest)ClassConvertUtil.convertClass(user,InsertLdapMemberRequest.class));
					InsertLdapMemberRequest insert = new InsertLdapMemberRequest();
					insert.setMail(addUser.getEmail());
					insert.setMobile(addUser.getMobile());
					insert.setName(addUser.getName());
					insert.setUsername(addUser.getLoginName());
					insert.setPassword(addUser.getPassword());
					request.add(insert);
					try {
						ldapServiceClient.insertLdapMembers(namespace, request);
					} catch (Exception e) {
						logger.error("insert ldap member error: {}", e.getMessage());
					}
				}

//				if (!CollectionUtils.isEmpty(userUpdateRequestList)) {
//					rbacServiceClient.modifyArrayByCode(userUpdateRequestList);
//				}
			}
			logger.info("already sync user to database:" + userCreateRequestList.size());
			// 四 发起rbac请求
			if (!CollectionUtils.isEmpty(userCreateRequestList)) {
				UserBatchCreateRequest userBatchCreateRequest = new UserBatchCreateRequest();
				int loop = 0;
				List<UserCreateRequest> tempList = Lists.newArrayList();
				for (UserCreateRequest userCreateRequest : userCreateRequestList) {
					tempList.add(userCreateRequest);
					if (loop % 100 == 0 || loop >= userCreateRequestList.size()) {
						try{
						logger.info("Current loop -> {} UserCreateRequestList Size -> {}", loop, userCreateRequestList.size());
						userBatchCreateRequest.setListUser(tempList);
						rbacServiceClient.batchCreatedUser(userBatchCreateRequest);
						tempList.clear();}catch (Exception e){
							logger.error("rbac - batchCreatedUser exception -> {}",e);
						}
					}
					loop++;
				}
			}
			// 五 封装成bpm map
			returnMap.put("addList", addList);
			// returnMap.put("addListProcess", addListProcess); 第三方暂时不同步
			returnMap.put("addListProcess", new ArrayList<>());
			returnMap.put("modiList", modiList);
			if(hasDel) {
				for (String s :
						delSyncAccounts) {
					delList.add(new User(s));
				}
			}
			logger.info("Del bpmUser info delList:"+delList.toString());
			returnMap.put("delList", delList);
			logger.info("SyncCmicUserData: sync cmic users to osa completed, added users :" + addList.size());
			logger.info("SyncCmicUserData: sync cmic 3rd party users to osa completed, added users :"
					+ addListProcess.size());
			logger.info("SyncCmicUserData: sync cmic users to osa completed, modified users :" + modiList.size());
			logger.info("SyncCmicUserData: sync cmic users to osa completed, deleted users :" + delList.size());
		} catch (Exception e) {
			logger.error("SyncCmicUserData: sync cmic users to osa failed", e);
		}
		return returnMap;
	}



	/**
	 * 同步user到bpm,返回同步成功的数据
	 *
	 * @param syncToBpmMap
	 * @return
	 */
	private void syncPortalUserToBpm(Map<String, List<User>> syncToBpmMap, Boolean initFlag) {
		List<User> addList = syncToBpmMap.get("addList");
		List<User> modiList = syncToBpmMap.get("modiList");
		List<User> delList = syncToBpmMap.get("delList");
		List<User> addListProcess = syncToBpmMap.get("addListProcess");
		try {
			if (!CollectionUtils.isEmpty(modiList)) {
				for (User user : modiList) {
					String operatorType = "1".equals(user.getSuspendFlag()) ?
							UmsWebServiceUtils.UMS_USER_OPERATE_TYPE__SUSPEND :
							UmsWebServiceUtils.UMS_USER_OPERATE_TYPE_MODI;
					Map<String, Object> syscUserResult = umsWebServiceUtils.syscUserData(user, operatorType);
					if ((Boolean) syscUserResult.get("result")) {
						logger.info("update portal user to bpm success, user name:" + user.getLoginName());
					} else {
						logger.error("update portal user to bpm failed, user name:" + user.getLoginName());
						if (null != syscUserResult.get("message")) {
							String msg = syscUserResult.get("message").toString();

							if (msg.contains("账号不存在")) {
								logger.info("try re-add portal user to bpm , user name:" + user.getLoginName());
								addList.add(user);
							}
						}
					}
				}
			}

	/*		// 添加用户, 并发起工单
			for (User user : addListProcess) {
				// 同步新增用户
				Map<String, Object> syscUserResult = UmsWebServiceUtils.syscUserData(user,
						// 初始化为true, 使用普通添加
						(initFlag ? UmsWebServiceUtils.UMS_USER_OPERATE_TYPE_ADD_FOR_SYNC
								: UmsWebServiceUtils.UMS_USER_OPERATE_TYPE_ADD_FOR_PROCEDURE));
				if ((Boolean) syscUserResult.get("result")) {
					logger.info("sync portal user to bpm success, user name:" + user.getLoginName());
					// 同步新增用户业务关系
//					Map<String, Object> syscRelationResult = UmsWebServiceUtils
//							.syscBusinessUserRelationData(user.getLoginName(), "8000|8000");
//					if ((Boolean) syscRelationResult.get("result")) {
//						logger.info("sync portal user relationship to bpm success, user name:" + user.getLoginName());
//					} else {
//						logger.error("sync portal user relationship to bpm failed, user name:" + user.getLoginName());
//					}
				} else {
					logger.error("sync portal user to bpm failed, user name:" + user.getLoginName());
				}

			}*/
			if (!CollectionUtils.isEmpty(addList)) {
				for (User user : addList) {
					// 同步新增用户
					Map<String, Object> syscUserResult = umsWebServiceUtils.syscUserData(user,
							UmsWebServiceUtils.UMS_USER_OPERATE_TYPE_ADD);
					if ((Boolean) syscUserResult.get("result")) {
						logger.info("sync portal user to bpm success, user name:" + user.getLoginName());
						// 同步新增用户业务关系
//						Map<String, Object> syscRelationResult = UmsWebServiceUtils
//								.syscBusinessUserRelationData(user.getLoginName(), "8000|8000");
//						if ((Boolean) syscRelationResult.get("result")) {
//							logger.info(
//									"sync portal user relationship to bpm success, user name:" + user.getLoginName());
//						} else {
//							logger.error(
//									"sync portal user relationship to bpm failed, user name:" + user.getLoginName());
//						}
					} else {
						logger.error("sync portal user to bpm failed, user name:" + user.getLoginName());
					}

				}
			}

			if (!CollectionUtils.isEmpty(delList)) {
				for (User user : delList) {
					Map<String, Object> deleteResult = umsWebServiceUtils.syscUserData(user,
							UmsWebServiceUtils.UMS_USER_OPERATE_TYPE_DEL);
					if ((Boolean) deleteResult.get("result")) {
						logger.info("delete portal user to bpm success, user name:" + user.getLoginName());
					} else {
						logger.error("delete portal user to bpm failed, user name:" + user.getLoginName());
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	/**
	 * 比对记录表和 获取的全量同步用户  用于 提取需要删除的账号
	 * 分类 删除  记录对全的补集
	 * 新增  全对记录的补集
	 *
	 * @param interfaceUser 三方接口提供的全量用户数据 Key 为账号 value 为空
	 * @return
	 */
	public List<String> getDelAccountList(List<String> interfaceUser) {

		//已经同步过的账号记录
		List<String> syncAccounts = orgSyncManager.getAccountsBySyncSrc(descr);
		if (null != syncAccounts && syncAccounts.size() != 0) {
			/* 保留interface的 Map
			1 遍历接口账号
			2 将接口数据拆分为
			dellist  只要在inter中找不到的
			和新增数据 在接口用户中有, 而库中没有的-> 接口中 剔除掉 同时存在于库中的
			 */
			HashMap<String, Integer> acRecordMap = new HashMap();
			for (String s :
					syncAccounts) {
				acRecordMap.put(s, 1);
			}
			//补集
			List<String> interfaceUserForRemove = interfaceUser.parallelStream().filter(entry-> null!=acRecordMap.get(entry)).collect(Collectors.toList());
			//syncAccounts 更新为删除list
			syncAccounts.removeAll(interfaceUserForRemove);
			logger.info("sync>> sync_account need to del size{}",syncAccounts.size());
			//interfaceUser 更新为新增list
			interfaceUser.removeAll(interfaceUserForRemove);
			logger.info("sync>> sync_account need to insert size{}",interfaceUser.size());
			//更新同步记录表
			if (null != syncAccounts && syncAccounts.size() != 0) {
				orgSyncManager.delAccountsInSyncAccounts(syncAccounts, descr);
			}
			if (null != interfaceUser && interfaceUser.size() != 0) {
				orgSyncManager.insertAccounts(interfaceUser, descr);
			}
		}else {
					//当记录数为0, 全量插入
					orgSyncManager.insertAccounts(interfaceUser, descr);
				}
				List<String> userDels =	orgSyncManager.getUserDelList(descr);
		return userDels;
	}


	public void testSuyanAddUserByFile(String jsonData) throws Exception {
		JSONArray entity = JSONObject.parseArray(jsonData);
		if (entity == null) {
			throw new Exception("获取的用户为NULL");
		}
		// 录入数据到网管平台
		Map<String, List<User>> userMap = syncEpcUserToOsa(entity);
		// 同步数据到BPM
		orgservice.postUserData(bpmUserUrl, userMap);
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



}
