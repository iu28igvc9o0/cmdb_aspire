package com.aspire.code.generator.controller;

import com.aspire.code.generator.api.ICodeGeneratorAPI;
import com.aspire.code.generator.config.ZipUtils;
import com.aspire.code.generator.service.ICodeGeneratorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: CodeGeneratorController
 * Author:   zhu.juwang
 * Date:     2019/4/28 15:04
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@RestController
@Slf4j
public class CodeGeneratorController implements ICodeGeneratorAPI {
    @Autowired
    private ICodeGeneratorService generatorService;
    @Value("${generatr.path}")
    private String zipPath;

    @Override
    public List<String> connection(@RequestBody Map<String, Object> connectionRequest) throws Exception {
        String databaseUrl = connectionRequest.get("databaseUrl").toString();
        String databaseUser = connectionRequest.get("databaseUser").toString();
        String databasePwd = connectionRequest.get("databasePwd").toString();
        return generatorService.connection(databaseUrl, databaseUser, databasePwd);
    }

    @Override
    public void generatorCode(@RequestBody Map<String, Object> connectionRequest,
                                      HttpServletResponse response) throws Exception {
        String databaseUrl = connectionRequest.getOrDefault("databaseUrl", "").toString();
        String databaseUser = connectionRequest.getOrDefault("databaseUser", "").toString();
        String databasePwd = connectionRequest.getOrDefault("databasePwd", "").toString();
        List<String> tableNames = (List<String>) connectionRequest.getOrDefault("tableNames", new String[]{});
        String packagePath = connectionRequest.getOrDefault("packagePath", "").toString();
        generatorService.generatorCode(databaseUrl, databaseUser, databasePwd, tableNames, packagePath, zipPath);
        String path = zipPath + File.separator + packagePath;
        if (packagePath.contains(".")) {
            path = zipPath + File.separator + packagePath.split("\\.")[0];
        }
        File file = new File(zipPath+File.separator+packagePath+".zip");
        FileOutputStream fos1 = new FileOutputStream(file);
        ZipUtils.toZip(path, fos1, true);
        BufferedInputStream fis = null;
        OutputStream outputStream = null;
        try {
            File zipFile = new File(zipPath+File.separator+packagePath+".zip");
            fis = new BufferedInputStream(new FileInputStream(zipFile));
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            // 清空response
            response.reset();
            outputStream = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment;filename=" + new String(zipFile.getName().getBytes("UTF-8"),"ISO-8859-1"));
            // luowenbo 2020.07.23 代码审计——存储型XSS 输出到前端的内容可能被加塞js可执行代码
            response.setHeader("Set-Cookie","cookiename=cookievalue; path=/; Domain=domainvaule; Max- age=seconds; HttpOnly");
            outputStream.write(buffer);
            outputStream.flush();
        } catch (FileNotFoundException e) {
            log.error("文件未找到错误:", e);
        } catch (IOException e) {
            log.error("输入输出错误:", e);
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    log.error("关闭文件输入流错误:", e);
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    log.error("关闭文件输出流错误:", e);
                }
            }
            //删除目录
//            File cleanFile = new File(path);
//            if (cleanFile.exists()) {
//                cleanFile.delete();
//            }
        }
    }

    /**
     * 生成 CURD
     * @param connectionRequest databaseUrl 数据库连接
     * @param connectionRequest databaseUser 数据库用户名
     * @param connectionRequest databasePwd 数据库密码
     * @param connectionRequest tableName 需要生成的表名
     * @param connectionRequest operator 操作方法
     * @return 生成CURD语句
     */
    public String generatorCURD(@RequestBody Map<String, Object> connectionRequest) {
        String databaseUrl = connectionRequest.getOrDefault("databaseUrl", "").toString();
        String databaseUser = connectionRequest.getOrDefault("databaseUser", "").toString();
        String databasePwd = connectionRequest.getOrDefault("databasePwd", "").toString();
        List<String> tableNames = (List<String>) connectionRequest.get("tableNames");
        String operator = connectionRequest.getOrDefault("operator", "").toString();
        return generatorService.generatorCURD(databaseUrl, databaseUser, databasePwd, tableNames, operator);
    }

}
