/**
 *
 * 项目名： composite-service 
 * <p/> 
 *
 * 文件名:  CompOpsYumManageController.java 
 * <p/>
 *
 * 功能描述: TODO 
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2019年11月27日 
 *
 * @version	V1.0
 * <p/>
 *
 *<b>Copyright(c)</b> 2019 卓望公司-版权所有<br/>
 *   
 */
package com.migu.tsg.microservice.atomicservice.composite.controller.opsmanage;

import com.aspire.mirror.composite.service.opsmanage.ICompYumManageService;
import com.aspire.mirror.ops.api.domain.GeneralResponse;
import com.aspire.mirror.ops.api.domain.OpsYumFileGroup;
import com.aspire.mirror.ops.api.domain.OpsYumFileGroupQueryModel;
import com.aspire.mirror.ops.api.domain.OpsYumFileModel;
import com.aspire.mirror.ops.api.domain.OpsYumFileQueryModel;
import com.aspire.mirror.ops.api.domain.OsDistributionModel;
import com.aspire.mirror.ops.api.domain.PageListQueryResult;
import com.aspire.mirror.ops.api.domain.YumFileServerSource;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.opsmanage.YumManageClient;
import com.migu.tsg.microservice.atomicservice.composite.config.CommonSftpServerConfig;
import com.migu.tsg.microservice.atomicservice.composite.controller.CommonResourceController;
import com.migu.tsg.microservice.atomicservice.composite.controller.authcontext.RequestAuthContext;
import com.migu.tsg.microservice.atomicservice.composite.controller.authcontext.ResAction;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.ResourceAuthHelper;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.SshUtil;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.SshUtil.SshResultWrap;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.SshUtil.SshdServer;
import com.migu.tsg.microservice.atomicservice.composite.helper.SshdHelper;
import com.migu.tsg.microservice.atomicservice.composite.vo.rbac.RbacResource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Set;

/** 
 *
 * 项目名称: composite-service 
 * <p/>
 * 
 * 类名: CompYumManageController
 * <p/>
 *
 * 类功能描述: TODO
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2019年11月27日  
 *
 * @version	V1.0 
 * <br/>
 *
 * <b>Copyright(c)</b> 2019 卓望公司-版权所有 
 *
 */
@Slf4j
@RestController
public class CompYumManageController  extends CommonResourceController implements ICompYumManageService {
	private static final String		OPS_YUM_LOCAL_DIR_PREFIX	= "yum_upload_local_";
	private static final String		OPS_YUM_TRANSFER_DIR_PREFIX	= "yum_transfer_local_";
	private static final String		OPS_YUM_DIR					= "/ops_yum/";
	@Autowired
	private YumManageClient			yumManageClient;
	@Autowired
	private CommonSftpServerConfig	sftpConfig;

	@Autowired
	protected ResourceAuthHelper resAuthHelper;

	@Autowired
	private SshdHelper sshdHelper;
	
	@Override
	@ResponseStatus(HttpStatus.OK)
	public List<OsDistributionModel> fetchOsDistributionList() {
		return yumManageClient.fetchOsDistributionList();
	}

	@Override
	@ResponseStatus(HttpStatus.OK)
	public List<OpsYumFileGroup> queryYumFileGroupTreeList() {
		return yumManageClient.queryYumFileGroupTreeList(new OpsYumFileGroupQueryModel());
	}

	@Override
	@ResponseStatus(HttpStatus.OK)
	public GeneralResponse createYumFileGroup(@RequestBody OpsYumFileGroup group) {
		return yumManageClient.createYumFileGroup(group);
	}

	@Override
	@ResponseStatus(HttpStatus.OK)
	public GeneralResponse updateYumFileGroup(@RequestBody OpsYumFileGroup group) {
		return yumManageClient.updateYumFileGroup(group);
	}

	@Override
	@ResponseStatus(HttpStatus.OK)
	public GeneralResponse removeYumFileGroup(@PathVariable("yumFileGroupId") Long yumFileGroupId) {
		return yumManageClient.removeYumFileGroup(yumFileGroupId);
	}

	@Override
	@ResponseStatus(HttpStatus.OK)
	public GeneralResponse uploadYumLocalFile(@RequestParam("file") MultipartFile file) {
		try {
			String fileName = file.getOriginalFilename();
			Path tempDir = Files.createTempDirectory(OPS_YUM_LOCAL_DIR_PREFIX);
			Path tempFile = tempDir.resolve(fileName);
			file.transferTo(tempFile.toFile());
			
			Pair<SshResultWrap, String> uploadResult = uploadYumFile(tempFile);
			Files.deleteIfExists(tempFile);
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

//		SshdServer sftpServer = new SshdServer();
//		sftpServer.setIpAddress(sftpConfig.getIpAddress());
//		sftpServer.setPort(sftpConfig.getPort());
//		sftpServer.setLoginUser(sftpConfig.getLoginUser());
//		sftpServer.setLoginPass(sftpConfig.getLoginPass());
//
//		String subDir = OPS_YUM_DIR + localFile.getParent().toFile().getName();
//		String remotePath = sftpConfig.getRootDirectory() + subDir;
//		SshUtil.executeShellCmd(sftpServer, 5, "mkdir -p " + remotePath + " || ls " + remotePath);
//
//		Pair<SshResultWrap, String> uploadResult = SshUtil.sftpUpload(sftpServer, localFile, remotePath);
//		String relativePath = subDir + "/" + localFile.toFile().getName(); // 使用相对路径
		return sshdHelper.uploadFile(localFile, OPS_YUM_DIR);
	}

	@Override
	@ResponseStatus(HttpStatus.OK)
	public GeneralResponse transferYumFile(@RequestBody YumFileServerSource serverSource) {
		try {
			Path localDir = Files.createTempDirectory(OPS_YUM_TRANSFER_DIR_PREFIX);
			SshdServer sftp = retriveSshdServer(serverSource);
			Pair<SshResultWrap, Path> sftpResult = SshUtil.sftpDownload(
					sftp, serverSource.getSourceFilePath(), localDir.toFile().getAbsolutePath());
			if (!sftpResult.getLeft().isSuccess()) {
				return new GeneralResponse(false, sftpResult.getLeft().getBizLog());
			}
			
			Pair<SshResultWrap, String> uploadResult = uploadYumFile(sftpResult.getRight());
			Files.deleteIfExists(sftpResult.getRight());
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

	@Override
	@ResponseStatus(HttpStatus.OK)
	@ResAction(action = "view", resType = "yum")
	public PageListQueryResult<OpsYumFileModel> queryYumSourceList(@RequestBody OpsYumFileQueryModel param) {
		RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
		if (!authCtx.getUser().isAdmin() && !authCtx.getUser().isSuperUser()) {
			Map<String, Set<String>> totalConstraints = resAuthHelper.verifyActionAndGetResource(
					authCtx.getUser(), authCtx.getResAction(), authCtx.getFlattenConstraints());

			if (!super.applyGeneralAuthConstraints(totalConstraints, param)) {
				return new PageListQueryResult<>();
			}
		}

		return yumManageClient.queryYumSourceList(param);
	}

	@Override
	@ResponseStatus(HttpStatus.OK)
	@ResAction(action = "create", resType = "yum")
	public GeneralResponse createYumSourceModel(@RequestBody OpsYumFileModel yumSource) {
		RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
		resAuthHelper.resourceActionVerify(authCtx.getUser(), new RbacResource(), authCtx.getResAction(), authCtx.getFlattenConstraints());

		return yumManageClient.createYumSourceModel(yumSource);
	}

	@Override
	@ResponseStatus(HttpStatus.OK)
	@ResAction(action = "update", resType = "yum")
	public GeneralResponse updateYumSourceModel(@RequestBody OpsYumFileModel yumSource) {
		RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
		resAuthHelper.resourceActionVerify(authCtx.getUser(), new RbacResource(), authCtx.getResAction(), authCtx.getFlattenConstraints());

		return yumManageClient.updateYumSourceModel(yumSource);
	}

	@Override
	@ResponseStatus(HttpStatus.OK)
	@ResAction(action = "delete", resType = "yum")
	public GeneralResponse removeYumSourceModel(@PathVariable("yumSourceId") Long yumSourceId) {
		RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
		resAuthHelper.resourceActionVerify(authCtx.getUser(), new RbacResource(), authCtx.getResAction(), authCtx.getFlattenConstraints());

		return yumManageClient.removeYumSourceModel(yumSourceId);
	}

	@Override
	@ResponseStatus(HttpStatus.OK)
	@ResAction(action = "view", resType = "yum")
	public PageListQueryResult<OpsYumFileModel> queryYumConfigList(@RequestBody OpsYumFileQueryModel param) {
		RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
		if (!authCtx.getUser().isAdmin() && !authCtx.getUser().isSuperUser()) {
			Map<String, Set<String>> totalConstraints = resAuthHelper.verifyActionAndGetResource(
					authCtx.getUser(), authCtx.getResAction(), authCtx.getFlattenConstraints());

			if (!super.applyGeneralAuthConstraints(totalConstraints, param)) {
				return new PageListQueryResult<>();
			}
		}

		return yumManageClient.queryYumConfigList(param);
	}

	@Override
	@ResponseStatus(HttpStatus.OK)
	@ResAction(action = "create", resType = "yum")
	public GeneralResponse createYumConfigModel(@RequestBody OpsYumFileModel yumConfig) {
		RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
		resAuthHelper.resourceActionVerify(authCtx.getUser(), new RbacResource(), authCtx.getResAction(), authCtx.getFlattenConstraints());

		return yumManageClient.createYumConfigModel(yumConfig);
	}

	@Override
	@ResponseStatus(HttpStatus.OK)
	@ResAction(action = "update", resType = "yum")
	public GeneralResponse updateYumConfigModel(@RequestBody OpsYumFileModel yumConfig) {
		return yumManageClient.updateYumConfigModel(yumConfig);
	}

	@Override
	@ResponseStatus(HttpStatus.OK)
	@ResAction(action = "delete", resType = "yum")
	public GeneralResponse removeYumConfigModel(@PathVariable("yumConfigId") Long yumConfigId) {
		RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
		resAuthHelper.resourceActionVerify(authCtx.getUser(), new RbacResource(), authCtx.getResAction(), authCtx.getFlattenConstraints());

		return yumManageClient.removeYumConfigModel(yumConfigId);
	}
	
	@Override
	@ResponseStatus(HttpStatus.OK)
	@ResAction(action = "download", resType = "yum")
	public void downloadYumFile(@RequestBody Map<String, String> downParam) {
		RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
		resAuthHelper.resourceActionVerify(authCtx.getUser(), new RbacResource(), authCtx.getResAction(), authCtx.getFlattenConstraints());

		ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletResponse response = servletRequestAttributes.getResponse();
        sshdHelper.downloadFile(downParam, response, "text/plain", "yum_file_down");
//        String relativeFilePath = downParam.get("file_path");
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
//    			throw new RuntimeException(wrap.getBizLog());
//    		}
//
//            response.setHeader("Content-Disposition",
//            		"attachment;filename=".concat(String.valueOf(URLEncoder.encode(fileName, "UTF-8"))));
//            response.setHeader("Connection", "close");
//            response.setHeader("Content-Type", "text/plain");
//            os.write(Files.readAllBytes(downResult.getRight()));
//            os.flush();
//        } catch (Exception e) {
//       	 	log.error("DownYumFile failed!", e);
//        }

	}
}
