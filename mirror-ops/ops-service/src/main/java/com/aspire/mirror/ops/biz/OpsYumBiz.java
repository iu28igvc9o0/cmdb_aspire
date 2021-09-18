/**
 *
 * 项目名： ops-service 
 * <p/> 
 *
 * 文件名:  YumManageBiz.java 
 * <p/>
 *
 * 功能描述: TODO 
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2019年11月22日 
 *
 * @version	V1.0
 * <p/>
 *
 *<b>Copyright(c)</b> 2019 卓望公司-版权所有<br/>
 *   
 */
package com.aspire.mirror.ops.biz;

import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.aspire.mirror.ops.api.domain.GeneralResponse;
import com.aspire.mirror.ops.api.domain.OpsGroupObjectTypeEnum;
import com.aspire.mirror.ops.api.domain.OpsYumFileGroup;
import com.aspire.mirror.ops.api.domain.OpsYumFileGroupQueryModel;
import com.aspire.mirror.ops.api.domain.OpsYumFileModel;
import com.aspire.mirror.ops.api.domain.OpsYumFileQueryModel;
import com.aspire.mirror.ops.api.domain.OsDistributionModel;
import com.aspire.mirror.ops.api.domain.PageListQueryResult;
import com.aspire.mirror.ops.api.domain.YumFileServerSource;
import com.aspire.mirror.ops.biz.model.OpsGroupObjectHandler;
import com.aspire.mirror.ops.controller.authcontext.RequestAuthContext;
import com.aspire.mirror.ops.dao.OpsGroupDao;
import com.aspire.mirror.ops.dao.OpsYumDataDao;
import com.aspire.mirror.ops.exception.OpsServiceException;
import com.aspire.mirror.ops.util.SshUtil;
import com.aspire.mirror.ops.util.SshUtil.SshResultWrap;
import com.aspire.mirror.ops.util.SshUtil.SshdServer;

import lombok.extern.slf4j.Slf4j;

/** 
 *
 * 项目名称: ops-service 
 * <p/>
 * 
 * 类名: YumManageBiz
 * <p/>
 *
 * 类功能描述: TODO
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2019年11月22日  
 *
 * @version	V1.0 
 * <br/>
 *
 * <b>Copyright(c)</b> 2019 卓望公司-版权所有 
 *
 */
@Slf4j
@Service
@Transactional
public class OpsYumBiz {
	private static final String		OPS_YUM_LOCAL_DIR_PREFIX	= "yum_upload_local_";
	private static final String		OPS_YUM_TRANSFER_DIR_PREFIX	= "yum_transfer_local_";
	private static final String		OPS_YUM_DIR					= "/ops_yum/";
	@Autowired
	private CommonSftpServerConfig	sftpConfig;
	@Autowired
	private OpsYumDataDao			yumDataDao;

	@Autowired
	private OpsGroupDao opsGroupDao;

	@Autowired
	private OpsGroupObjectHandler opsGroupObjectHandler;


	public List<OsDistributionModel> fetchOsDistributionList() {
		return yumDataDao.fetchOsDistributionList();
	}
	
	public List<OpsYumFileGroup> queryYumFileGroupList(OpsYumFileGroupQueryModel param) {
//		List<OpsYumFileGroup> resultList = yumDataDao.queryYumFileGroupList(param);
//		if (CollectionUtils.isEmpty(resultList)) {
//			return new PageListQueryResult<OpsYumFileGroup>(0, resultList);
//		}
//		Integer totalCount = yumDataDao.queryTotalYumFileGroupCount(param);
//		return new PageListQueryResult<OpsYumFileGroup>(totalCount, resultList);
		return yumDataDao.queryYumFileGroupList(param);
	}
	
	@Transactional(isolation=Isolation.REPEATABLE_READ)
	public GeneralResponse createYumFileGroup(OpsYumFileGroup group) {
		try {
			OpsYumFileGroupQueryModel param = new OpsYumFileGroupQueryModel();
			param.setParentGroupId(group.getParentGroupId());
			param.setGroupName(group.getGroupName());
			List<OpsYumFileGroup> queryList = queryYumFileGroupList(param);
			if (CollectionUtils.isNotEmpty(queryList)) {
				return new GeneralResponse(false, "Already exists the group name under the same parent group.");
			}
			group.setCreater(RequestAuthContext.getRequestHeadUserName());
			group.setCreateTime(new Date());
			this.yumDataDao.createYumFileGroup(group);

			return new GeneralResponse(true, null, group);
		} catch(Exception e) {
			log.error("Exception when createYumFileGroup().", e);
			return new GeneralResponse(false, e.toString());
		}
	}
	
	@Transactional(isolation=Isolation.REPEATABLE_READ)
	public GeneralResponse updateYumFileGroup(OpsYumFileGroup group) {
		try {
			OpsYumFileGroupQueryModel param = new OpsYumFileGroupQueryModel();
			param.setParentGroupId(group.getParentGroupId());
			param.setGroupName(group.getGroupName());
			List<OpsYumFileGroup> queryList = queryYumFileGroupList(param);
			for (OpsYumFileGroup node : queryList) {
				if (!node.getGroupId().equals(group.getGroupId())) {
					return new GeneralResponse(false, "Already exists the group name under the same parent group.");
				}
			}
			group.setLastUpdater(RequestAuthContext.getRequestHeadUserName());
			group.setLastUpdateTime(new Date());
			this.yumDataDao.updateYumFileGroup(group);
			return new GeneralResponse();
		} catch(Exception e) {
			log.error("Exception when updateYumFileGroup().", e);
			return new GeneralResponse(false, e.toString());
		}
	}
	
	public GeneralResponse removeYumFileGroup(Long yumFileGroupId) {
		OpsYumFileGroupQueryModel param = new OpsYumFileGroupQueryModel();
		param.setParentGroupId(yumFileGroupId);
		List<OpsYumFileGroup> childList = yumDataDao.queryYumFileGroupList(param);
		if (CollectionUtils.isNotEmpty(childList)) {
			return new GeneralResponse(false, "Can't remove the yum file group with id '" + yumFileGroupId 
					+ "', as there are children file groups.") ;
		}
		OpsYumFileQueryModel yumFileParam = new OpsYumFileQueryModel();
		yumFileParam.setYumFileGroupId(yumFileGroupId);
		List<OpsYumFileModel> yumFileList = yumDataDao.queryYumFileList(yumFileParam);
		if (CollectionUtils.isNotEmpty(yumFileList)) {
			return new GeneralResponse(false, "Can't remove the yum file group with id '" + yumFileGroupId 
					+ "', as it was referenced by the yum files.") ;
		}
		this.yumDataDao.removeYumFileGroup(yumFileGroupId);
		return new GeneralResponse();
	}

	public GeneralResponse uploadYumLocalFile(MultipartFile file) {
		try {
			String fileName = file.getOriginalFilename();
			Path tempDir = Files.createTempDirectory(OPS_YUM_LOCAL_DIR_PREFIX);
			Path tempFile = tempDir.resolve(fileName);
			file.transferTo(tempFile.toFile());
			
			Pair<SshResultWrap, String> uploadResult = uploadYumFile(tempFile);
			if (!uploadResult.getLeft().isSuccess()) {
				return new GeneralResponse(false, uploadResult.getLeft().getBizLog());
			}
			return new GeneralResponse(true, null, uploadResult.getRight());
		} catch (Exception e) {
			log.error("Exception when uploadYumLocalFile().", e);
			return new GeneralResponse(false, e.toString());
		}
	}
	
	private Pair<SshResultWrap, String> uploadYumFile(Path localFile) {
		SshdServer sftpServer = new SshdServer();
		sftpServer.setIpAddress(sftpConfig.getIpAddress());
		sftpServer.setPort(sftpConfig.getPort());
		sftpServer.setLoginUser(sftpConfig.getLoginUser());
		sftpServer.setLoginPass(sftpConfig.getLoginPass());
		
		String subDir = OPS_YUM_DIR + localFile.getParent().toFile().getName();
		String remotePath = sftpConfig.getRootDirectory() + subDir;
		SshUtil.executeShellCmd(sftpServer, 5, "mkdir -p " + remotePath + " || ls " + remotePath);
		
		Pair<SshResultWrap, String> uploadResult = SshUtil.sftpUpload(sftpServer, localFile, remotePath);
		String relativePath = subDir + "/" + localFile.toFile().getName(); // 使用相对路径
		return Pair.of(uploadResult.getLeft(), relativePath);
	}
	
	public GeneralResponse transferYumFile(YumFileServerSource serverSource) {
		try {
			Path localDir = Files.createTempDirectory(OPS_YUM_TRANSFER_DIR_PREFIX);
			SshdServer sftp = retriveSshdServer(serverSource);
			Pair<SshResultWrap, Path> sftpResult = SshUtil.sftpDownload(
					sftp, serverSource.getSourceFilePath(), localDir.toFile().getAbsolutePath());
			if (!sftpResult.getLeft().isSuccess()) {
				return new GeneralResponse(false, sftpResult.getLeft().getBizLog());
			}
			
			Pair<SshResultWrap, String> uploadResult = uploadYumFile(sftpResult.getRight());
			if (!uploadResult.getLeft().isSuccess()) {
				return new GeneralResponse(false, uploadResult.getLeft().getBizLog());
			}
			return new GeneralResponse(true, null, uploadResult.getRight());
		} catch (Exception e) {
			log.error("Exception when transferYumFile().", e);
			return new GeneralResponse(false, e.toString());
		}
	}
	
	private SshdServer retriveSshdServer(YumFileServerSource serverSource) {
		SshdServer sshServer = new SshdServer();
		sshServer.setIpAddress(serverSource.getServerIP());
		sshServer.setPort(serverSource.getServerPort());
		sshServer.setLoginUser(serverSource.getLoginUser());
		sshServer.setLoginPass(serverSource.getLoginPass());
		return sshServer;
	}
	
	@Transactional(readOnly=true)
	public PageListQueryResult<OpsYumFileModel> queryYumFileList(OpsYumFileQueryModel param) {
		List<OpsYumFileModel> yumFileList = yumDataDao.queryYumFileList(param);
		if (CollectionUtils.isEmpty(yumFileList)) {
			return new PageListQueryResult<OpsYumFileModel>(0, yumFileList);
		}
		Integer totalCount = yumDataDao.queryTotalYumFileCount(param);
		return new PageListQueryResult<OpsYumFileModel>(totalCount, yumFileList);
	}
	
	public GeneralResponse createYumFileModel(OpsYumFileModel yumFile) {
		try {
			OpsYumFileQueryModel param = new OpsYumFileQueryModel();
			param.setFileType(yumFile.getFileType());
			param.setName(yumFile.getName());
			param.setVersion(yumFile.getVersion());
			Integer existCount = yumDataDao.queryTotalYumFileCount(param);
			if (existCount > 0) {
				return new GeneralResponse(false, "Already exists the record with the same name and version.");
			}
			
			yumFile.setCreater(RequestAuthContext.getRequestHeadUserName());
			yumFile.setCreateTime(new Date());
			yumDataDao.createYumFileModel(yumFile);

			// 添加分组列表
			opsGroupObjectHandler.saveOpsGroup(yumFile, yumFile.getId(), OpsGroupObjectTypeEnum.YUM.getStatusCode());
			return new GeneralResponse(true, null, yumDataDao.queryYumFileModelById(yumFile.getId()));
		} catch(Exception e) {
			log.error("Exception when createYumSourceModel().", e);
			return new GeneralResponse(false, e.toString());
		}
	}
	
	public GeneralResponse updateYumFileModel(OpsYumFileModel yumFile) {
		try {
			OpsYumFileQueryModel param = new OpsYumFileQueryModel();
			param.setFileType(yumFile.getFileType());
			param.setName(yumFile.getName());
			param.setVersion(yumFile.getVersion());
			List<OpsYumFileModel> existList = yumDataDao.queryYumFileList(param);
			for (OpsYumFileModel file : existList) {
				if (!file.getId().equals(yumFile.getId())) {
					return new GeneralResponse(false, "Already exists the record with the same name and version.");
				}
			}
			
			yumFile.setLastUpdater(RequestAuthContext.getRequestHeadUserName());
			yumFile.setLastUpdateTime(new Date());
			yumDataDao.updateYumFileModel(yumFile);
//			添加分组列表
			opsGroupObjectHandler.saveOpsGroup(yumFile, yumFile.getId(), OpsGroupObjectTypeEnum.YUM.getStatusCode());

			return new GeneralResponse(true, null, yumDataDao.queryYumFileModelById(yumFile.getId()));
		} catch(Exception e) {
			log.error("Exception when updateYumSourceModel().", e);
			return new GeneralResponse(false, e.toString());
		}	
	}

//	private void saveOpsGroup(OpsYumFileModel yumFile) {
//		// 添加分组列表
//		if (!CollectionUtils.isEmpty(yumFile.getGroupIdList())) {
//			List<OpsGroupRelation> relationList = Lists.newArrayList();
//			GroupRelationQueryModel queryModel = new GroupRelationQueryModel();
//			queryModel.setObjectId(yumFile.getId());
//			queryModel.setObjectType(OpsGroupObjectTypeEnum.YUM.getStatusCode());
//			List<GroupRelationDetail> groupRelationDetails = opsGroupDao.querGroupRelationList(queryModel);
//			List<Long> existGroupId = groupRelationDetails.stream().map(GroupRelationDetail::getGroupId).collect
//					(Collectors.toList());
//			for (Long groupId : yumFile.getGroupIdList()) {
//				if (!existGroupId.contains(groupId)) {
//					OpsGroupRelation opsGroupRelation = new OpsGroupRelation(groupId, OpsGroupObjectTypeEnum.YUM
//							.getStatusCode(), yumFile.getId());
//					relationList.add(opsGroupRelation);
//				}
//			}
//			if (CollectionUtils.isNotEmpty(relationList)) {
//				opsGroupDao.saveBatchGroupRelation(relationList);
//			}
//
//			//删除
//			List<Long> deleteGroupIdList = existGroupId.stream().filter(groupId -> !yumFile.getGroupIdList()
//					.contains(groupId)).collect(Collectors.toList());
//			if (!CollectionUtils.isEmpty(deleteGroupIdList)) {
//				Long objectId = yumFile.getId();
//				opsGroupDao.deleteGroupRelationByGroupIdListAndObjectId(deleteGroupIdList, objectId);
//			}
//		}
//	}

	public GeneralResponse removeYumFileModel(Long yumFileId) {
		yumDataDao.removeYumFileModel(yumFileId);
		opsGroupDao.deleteGroupRelationByObjectIdAndType(yumFileId, OpsGroupObjectTypeEnum.YUM.getStatusCode());
		return new GeneralResponse();
	}
	
	public void downYumFile(String relativeFilePath) {
//		ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
//        HttpServletResponse response = servletRequestAttributes.getResponse();
//
//        try (OutputStream os = response.getOutputStream()){
//        	String fileName = Paths.get(relativeFilePath).getFileName().toFile().getName();
//        	String fullDownFilePath = sftpConfig.getRootDirectory() + relativeFilePath;
//
//        	SshdServer sftpServer = new SshdServer();
//    		sftpServer.setIpAddress(sftpConfig.getIpAddress());
//    		sftpServer.setPort(sftpConfig.getPort());
//    		sftpServer.setLoginUser(sftpConfig.getLoginUser());
//    		sftpServer.setLoginPass(sftpConfig.getLoginPass());
//
//    		Path localDownDir = Files.createTempDirectory("yum_file_down");
//    		Pair<SshResultWrap, Path> downResult
//    			= SshUtil.sftpDownload(sftpServer, fullDownFilePath, localDownDir.toFile().getCanonicalPath());
//    		SshResultWrap wrap = downResult.getLeft();
//    		if (!wrap.isSuccess()) {
//    			throw new OpsServiceException(wrap.getBizLog());
//    		}
//
//            response.setHeader("Content-Disposition",
//            		"attachment;filename=".concat(String.valueOf(URLEncoder.encode(fileName, "UTF-8"))));
//            response.setHeader("Connection", "close");
//            response.setHeader("Content-Type", "text/plain");
//
//            os.write(FileCopyUtils.copyToByteArray(downResult.getRight().toFile()));
//            os.flush();
//        } catch (Exception e) {
//       	 	log.error("DownYumFile failed!", e);
//        }
	}
}
