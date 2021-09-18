/**
 * 
 */
package com.aspire.ums.cmdb.schedule;

import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.aspire.ums.cmdb.sync.client.RbacServiceClient;
import com.google.common.collect.Lists;
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
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import com.aspire.ums.cmdb.common.StringUtils;
import com.aspire.ums.cmdb.sync.client.LdapServiceClient;
import com.aspire.ums.cmdb.sync.entity.Office;
import com.aspire.ums.cmdb.sync.entity.OfficeUser;
import com.aspire.ums.cmdb.sync.entity.User;
import com.aspire.ums.cmdb.sync.util.ClassConvertUtil;
import com.aspire.ums.cmdb.sync.util.UmsWebServiceUtils;
import com.migu.tsg.microservice.atomicservice.ldap.dto.GetLdapUserResponse;
import com.migu.tsg.microservice.atomicservice.ldap.dto.InsertLdapMemberRequest;
import com.migu.tsg.microservice.atomicservice.ldap.dto.ListPagenationResponse;
import com.migu.tsg.microservice.atomicservice.ldap.dto.UpdateLdapMemberRequest;
import org.springframework.util.CollectionUtils;

/**
 * @author lupeng
 *
 */
@Component
@ConditionalOnExpression("${schedule.epcUser.flag:false}")
public class EpcUserSyncTaskService {

	protected static Logger logger = LoggerFactory.getLogger(EpcUserSyncTaskService.class);
	@Value("${ldapconfig.namespace:alauda}")
	private String namespace;

	@Value("${sysdata.Token.url}")
	private String sendUrl;
	@Value("${sysdata.Token.username}")
	private String userName;

	@Value("${sysdata.Token.password}")
	private String password;

	@Value("${sysdata.Epcuser.url}")
	private String sysUrl;

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
	// private final static int pagesize = 200000;

	// private String md5 = "{MD5}";

	@Autowired
	private LdapServiceClient ldapServiceClient;

	@Autowired
	private RbacServiceClient rbacServiceClient;
	@Autowired
	private UmsWebServiceUtils umsWebServiceUtils;
	//"0 0/30 * * *  ?"

	//@Scheduled(cron = "${syncEpcUserData.cron}")
	public void syncEpcUserData() {
		logger.info("定时任务：同步用户执行开始...");
		try {
			sysEpcUserData();
		} catch (Exception e) {
			e.printStackTrace();
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

	public JSONArray getEpcUserData() throws Exception {
		// 获取token结果数据
		JSONObject token = this.getToken();
		logger.info("+++++++++获取Token:" + token);
		if (token == null) {
			throw new Exception("获取的Token为NULL");
		}
		String access_token = (String) token.get("access_token");
		if (StringUtils.isEmpty(access_token)) {
			throw new Exception("未获取到access_token异常");
		}
		String applicationName = "ZWN_卓望新工单";
		StringBuffer sendUrl = new StringBuffer(sysUrl).append("?" +"applicationName="+ applicationName+"&&token=" + access_token);
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpGet getMethod = new HttpGet(sendUrl.toString());
		// String value = token_type + " " + access_token;
		// logger.info("请求头的信息value：" + value);
		// getMethod.addHeader("Authorization", value);
		HttpResponse response = null;
		JSONArray entity = null;
		try {
			response = httpClient.execute(getMethod);
			// 数据解析处理
			String responseStr = EntityUtils.toString(response.getEntity());
			logger.info("call sync users interface return is : " + sendUrl);
			logger.info("call sync users interface status is : " + response.getStatusLine().getStatusCode());
			logger.info("call sync users interface return is : " + responseStr);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				JSONObject jsonObject = JSONObject.parseObject(responseStr);
				entity = jsonObject.getJSONArray("entity");
				return entity;
			} else {
				logger.error("同步用户数据失败:" + sendUrl);
			}
		} catch (Exception e) {
			logger.error("同步用户数据失败:" + sendUrl, e);
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
		JSONArray entity = this.getEpcUserData();
		if (entity == null) {
			throw new Exception("获取的用户为NULL");
		}
		// 录入数据到网管平台
		Map<String, List<User>> userMap = syncEpcUserToOsa(entity);
		// 同步数据到BPM
		syncPortalUserToBpm(userMap, true);

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
		List<User> onlyDelList = new ArrayList<>();
		List<User> addListProcess = new ArrayList<>();
		List<OfficeUser> ouAddList = new ArrayList<>();
		List<User> ouDelList = new ArrayList<>();

		// add jisnu 新增用户 修改用户
		List<UserCreateRequest> userCreateRequestList = Lists.newArrayList();

//		List<UserUpdateRequest> userUpdateRequestList = Lists.newArrayList();
		try {

			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			Date createDate = new Date();
			String remark = sdf.format(createDate);
			String markAdd = remark + "_add";
			String markUpdate = remark + "_update";
			String markSuspend = remark + "_suspend";
			String markDelete = remark + "_delete";
			ListPagenationResponse response = ldapServiceClient.listLdapMember(namespace, null, null, projectss, orderBy,
					pageSize, currentPage);
			List<GetLdapUserResponse> results = response.getResults();
			logger.info("SyncCmicUserData listLdapMember");
			if (results.size() > 0) {
				List<User> list = new ArrayList<>();

				for (GetLdapUserResponse result : results) {
					// list.add((User) ClassConvertUtil.convertClass(result,
					// GetLdapUserResponse.class));
					User user = new User();
					user.setLoginName(result.getUsername());
					user.setMobile(result.getMobile());
					user.setName(result.getName());
					user.setEmail(result.getMail());
					user.setPassword(result.getPassword());
					user.setCreateDate(sdf.parse(result.getCreateTime()));
					list.add(user);
				}
				Map<String, User> cnmsUserMap = new HashMap<>(list.size());
				for (User user : list) {
					cnmsUserMap.put(user.getLoginName(), user);
				}
				boolean isDel;
				for (int i = 0; i < entity.size(); i++) {
					JSONObject object = (JSONObject) entity.get(i);
					String id = (String) object.get("id");
					// String organizationId = (String) object.get("organizationId");
					// String applicationName = (String) object.get("applicationName");
					// String roleId = (String) object.get("roleId");
					String status = (String) object.get("status");
					// String effectTime = (String) object.get("effectTime");
					String name = (String) object.get("name");
					String username = (String) object.get("username");
					String password = (String) object.get("password");
					String email = (String) object.get("email");
					String mobile = (String) object.get("phone");
					User cmnsUser = cnmsUserMap.get(username);
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
								username, password, name, email, mobile, DEFAULT_USER_TYPE, markAdd, createDate);
						addList.add(cmnsUser);
						// logger.info("add: "+cmicUser.getEmail());

					} else {
						switch (status.toUpperCase()) {
							case "DELETED":// 删除
								cmnsUser.setDelFlag("1");
								cmnsUser.setRemarks(markDelete);
								delList.add(cmnsUser);
								isDel = true;
								// logger.info("del: "+cmicUser.getEmail());
								break;
							case "SUSPEND":// 挂起
								cmnsUser.setRemarks(markSuspend);
								cmnsUser.setSuspendFlag("1");
								break;
							default:// 修改
								cmnsUser.setSuspendFlag("0");
								break;
						}
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
								// cmnsUser.setUpdateDate(createDate);
								modiList.add(cmnsUser);
							}
						}
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
					rbacUser.setDeptId(DEPARTMENT_ID);
					rbacUser.setRoles(ROLE_ID);
					rbacUser.setCode(user.getLoginName());
					userCreateRequestList.add(rbacUser);

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
						e.printStackTrace();
					}
				}
				logger.info("after add ldap menbers , userlist is :" + userCreateRequestList.size());
				// userDao.save(addList);
				// userDao.save(addListProcess); 第三方暂时不同步
				// contactsService.addOrgUsers(ouAddList);

				// 修改用户信息
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
					rbacUser.setDeptId(DEPARTMENT_ID);
					rbacUser.setRoles(ROLE_ID);
					rbacUser.setCode(user.getLoginName());
					userCreateRequestList.add(rbacUser);
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
						e.printStackTrace();
					}

				}
				logger.info("after mod ldap menbers , userlist is :" + userCreateRequestList.size());
				// userDao.save(modiList);
				// contactsService.delOrgUsers(ouDelList);
				// 删除用户
				for (User user : delList) {
					ldapServiceClient.deleteLdapMember(namespace, user.getLoginName());
				}

				// userDao.save(onlyDelList);
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

					if (!"ACTIVE".equalsIgnoreCase(status)) {
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
							username, password, name, email, moile, DEFAULT_USER_TYPE, markAdd, createDate);
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
						rbacUser.setDeptId(DEPARTMENT_ID);
						rbacUser.setRoles(ROLE_ID);
						rbacUser.setCode(addUser.getLoginName());

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
			// 测试注释
			if (!CollectionUtils.isEmpty(userCreateRequestList)) {
				UserBatchCreateRequest userBatchCreateRequest = new UserBatchCreateRequest();
				userBatchCreateRequest.setListUser(userCreateRequestList);
				rbacServiceClient.batchCreatedUser(userBatchCreateRequest);
			}
			returnMap.put("addList", addList);
			// returnMap.put("addListProcess", addListProcess); 第三方暂时不同步
			returnMap.put("addListProcess", new ArrayList<>());
			returnMap.put("modiList", modiList);
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

	
}
