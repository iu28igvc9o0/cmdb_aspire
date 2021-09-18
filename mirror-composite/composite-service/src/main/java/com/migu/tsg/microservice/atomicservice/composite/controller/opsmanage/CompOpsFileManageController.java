package com.migu.tsg.microservice.atomicservice.composite.controller.opsmanage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.aspire.mirror.common.util.ZipUtil;
import com.aspire.mirror.ops.api.domain.GeneralResponse;
import com.aspire.mirror.ops.api.domain.OpsFile;
import com.aspire.mirror.ops.api.domain.OpsFileQueryModel;
import com.aspire.mirror.ops.api.domain.PageListQueryResult;
import com.google.common.collect.Lists;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.opsmanage.OpsFileClient;
import com.migu.tsg.microservice.atomicservice.composite.controller.CommonResourceController;
import com.migu.tsg.microservice.atomicservice.composite.controller.authcontext.RequestAuthContext;
import com.migu.tsg.microservice.atomicservice.composite.controller.authcontext.ResAction;
import com.migu.tsg.microservice.atomicservice.composite.helper.SshdHelper;
import com.migu.tsg.microservice.atomicservice.composite.vo.rbac.RbacResource;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.aspire.mirror.composite.service.opsmanage.ICompOpsFileManageService;
import com.migu.tsg.microservice.atomicservice.composite.config.CommonSftpServerConfig;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.SshUtil;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

/**
 * 项目名称: composite-service
 * <p/>
 * <p>
 * 类名: CompOpsFileManageController
 * <p/>
 * <p>
 * 类功能描述: TODO
 * <p/>
 *
 * @author pengguihua
 * @version V1.0 <br/>
 *
 * <b>Copyright(c)</b> 2020 卓望公司-版权所有
 * @date 2020年4月29日
 */
@Slf4j
@RestController
public class CompOpsFileManageController extends CommonResourceController implements ICompOpsFileManageService {
    //    @Value("${CONVERGE_DOWNLOAD_DIR:D:/temp}")
//    private String downloadTempDir;
    @Autowired
    private CommonSftpServerConfig sftpConfig;

    @Autowired
    private OpsFileClient opsFileClient;

    @Override
    @ResponseStatus(HttpStatus.OK)
    @ResAction(action = "download", resType = "opsFile")
    public void downloadFile(@RequestBody Map<String, String> downParam) {
        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        resAuthHelper.resourceActionVerify(authCtx.getUser(), new RbacResource(), authCtx.getResAction(), authCtx.getFlattenConstraints());
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletResponse response = servletRequestAttributes.getResponse();
        sshdHelper.downloadFile(downParam, response, "application/octet-stream", "report_file_down");
    }

    @Override
    @ResAction(action = "download", resType = "opsFile")
    public void convergeDownloadFile(@RequestBody Map<String, String> downParam) {
        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        resAuthHelper.resourceActionVerify(authCtx.getUser(), new RbacResource(), authCtx.getResAction(), authCtx.getFlattenConstraints());
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletResponse response = servletRequestAttributes.getResponse();
        String filePath = downParam.get("file_path");
        String isRelativePath = downParam.get("is_relative");
        String zipPassword = downParam.get("password");
        String fileName = downParam.get("file_name");
        String[] filePathArray = filePath.split(",");
        List<String> fullPathList = Lists.newArrayList();

        for (String filePathTemp : filePathArray) {
            String fullDownFilePath = isRelativePath != null && "N".equalsIgnoreCase(isRelativePath) ? filePathTemp : sftpConfig.getRootDirectory() + filePathTemp;
            fullPathList.add(fullDownFilePath);
        }
        // step 1 下载文件到本地
//        Path localDownDir = Files.createTempDirectory("converge_");
//        + File.separator + fileName
        Pair<String, List<File>> pair = sshdHelper.downloadFile(fullPathList, "converge_", fileName);
//        String fileName = Paths.get(filePath).getFileName().toFile().getName();
        // step 2 加密打包
        String convergeDownloadDir = pair.getLeft() + File.separator + fileName + ".zip";
        ZipUtil.zipFileAndEncrypt(pair.getRight(), convergeDownloadDir, zipPassword);
        // 返回文件流
        FileInputStream fileInput = null;
        try (OutputStream os = response.getOutputStream()) {
            response.setHeader("Content-Disposition",
                    "attachment;filename*=utf-8'zh_cn'".concat(URLEncoder.encode(fileName + ".zip", "UTF-8")));
            response.setHeader("Connection", "close");
            response.setContentType("application/octet-stream".concat("; charset=UTF-8"));
            // luowenbo 2020-07-24 修改： 代码审计——存储型XSS缺陷
            response.setHeader("Set-Cookie","cookiename=cookievalue; path=/; Domain=domainvaule; Max- age=seconds; HttpOnly");
            fileInput = new FileInputStream(Paths.get(convergeDownloadDir).toFile());
            byte[] buff = new byte[20480];
            int read = 0;
            while ((read = fileInput.read(buff)) > 0) {
                os.write(buff, 0, read);
            }
            os.flush();
        } catch (Exception e) {
            log.error("OpsFileManageController download file {} failed!", convergeDownloadDir, e);
        } finally {
            IOUtils.closeQuietly(fileInput);
        }
    }


    @Override
    @ResAction(action = "create", resType = "opsFile")
    public GeneralResponse saveFile(@RequestBody OpsFile opsFile) {
        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        resAuthHelper.resourceActionVerify(authCtx.getUser(), new RbacResource(), authCtx.getResAction(), authCtx.getFlattenConstraints());
        return opsFileClient.saveFile(opsFile);
    }

    @Override
    @ResAction(action = "delete", resType = "opsFile")
    public GeneralResponse deleteFile(@RequestParam("file_id") Long fileId) {
        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        resAuthHelper.resourceActionVerify(authCtx.getUser(), new RbacResource(), authCtx.getResAction(), authCtx.getFlattenConstraints());
        OpsFile opsFile = opsFileClient.getFileDetail(fileId);
        SshUtil.SshdServer sftpServer = sshdHelper.getSshdServer();
        String subDir = opsFile.getFilePath();
        String remotePath = sftpConfig.getRootDirectory() + subDir;
        SshUtil.SshResultWrap sshResultWrap = SshUtil.executeShellCmd(sftpServer, 5, "rm -rf " + remotePath);
        if (!sshResultWrap.isSuccess()) {
            log.error("删除文件失败！" + sshResultWrap.getBizLog());
        }
        return opsFileClient.deleteFile(fileId);
//		if (sshResultWrap.isSuccess()) {
//
//		} else {
//			log.error("删除文件出错！{0}", sshResultWrap.getBizLog());
//			return new GeneralResponse(false, sshResultWrap.getBizLog());
//		}
    }

    @Override
    @ResAction(action = "view", resType = "opsFile")
    public OpsFile getFileDetail(@RequestParam("file_id") Long fileId) {
        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        resAuthHelper.resourceActionVerify(authCtx.getUser(), new RbacResource(), authCtx.getResAction(), authCtx.getFlattenConstraints());
        return opsFileClient.getFileDetail(fileId);
    }

    @Override
    @ResAction(action = "view", resType = "opsFile")
    public PageListQueryResult<OpsFile> pageList(@RequestBody OpsFileQueryModel opsFileQueryModel) {
        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        resAuthHelper.resourceActionVerify(authCtx.getUser(), new RbacResource(), authCtx.getResAction(), authCtx.getFlattenConstraints());
        return opsFileClient.pageList(opsFileQueryModel);
    }

    @Override
    @ResAction(action = "upload", resType = "opsFile")
    public GeneralResponse uploadFile(@RequestBody MultipartFile file) {
        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        resAuthHelper.resourceActionVerify(authCtx.getUser(), new RbacResource(), authCtx.getResAction(), authCtx.getFlattenConstraints());
        String fileName = file.getOriginalFilename();
        Path tempDir = null;
        try {
            tempDir = Files.createTempDirectory(OPS_FILE_LOCAL_DIR_PREFIX);
            Path tempFile = tempDir.resolve(fileName);
            file.transferTo(tempFile.toFile());
            Pair<SshUtil.SshResultWrap, String> uploadResult = sshdHelper.uploadFile(tempFile, OPS_FILE_MODEL_PATH);
            Files.deleteIfExists(tempFile);
            if (uploadResult.getLeft().isSuccess()) {
                return new GeneralResponse(true, null, uploadResult.getRight());
            } else {
                return new GeneralResponse(false, uploadResult.getLeft().getBizLog());
            }

        } catch (IOException e) {
            log.error("CompOpsFileManageController[uploadFile] is error!");
            return new GeneralResponse(false, e.toString());
        }
    }

    private static final String OPS_FILE_LOCAL_DIR_PREFIX = "file_manage_local_";
    private static final String OPS_FILE_MODEL_PATH = "/file_manage/";
    @Autowired
    private SshdHelper sshdHelper;
}
