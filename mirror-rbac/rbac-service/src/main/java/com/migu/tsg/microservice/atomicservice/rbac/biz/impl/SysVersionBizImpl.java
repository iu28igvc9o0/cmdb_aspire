package com.migu.tsg.microservice.atomicservice.rbac.biz.impl;

import com.google.common.io.ByteStreams;
import com.migu.tsg.microservice.atomicservice.rbac.biz.SysVersionBiz;
import com.migu.tsg.microservice.atomicservice.rbac.dao.SysVersionDao;
import com.migu.tsg.microservice.atomicservice.rbac.dao.po.SysVersion;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * @author menglinjie
 */
@Service
public class SysVersionBizImpl implements SysVersionBiz {
    @Resource
    SysVersionDao sysVersionDao;
    @Override
    public Map<Boolean, Object> importSysVersion(MultipartFile multipartFile) throws IOException, DocumentException {
        ZipInputStream zipInputStream = new ZipInputStream(multipartFile.getInputStream(), Charset.forName("GBK"));
        try {

            BufferedInputStream bs = new BufferedInputStream(zipInputStream);
            ZipEntry zipEntry = null;
            byte[] bytes = null;
            SysVersion sysVersion = new SysVersion();
            while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                if (!zipEntry.isDirectory()) {
                    String[] split = zipEntry.getName().split("/");
                    String fileName = split[split.length - 1];
                    //解析xml文件
                    if (fileName.endsWith("information.xml")) {
                        //把zipEntry转换为流的形式，然后进行解析
                        bytes = new byte[(int) zipEntry.getSize()];
                        bs.read(bytes, 0, (int) zipEntry.getSize());
                        InputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
                        //解析xml--dom4j的形式
                        SAXReader saxReader = new SAXReader();
                        Document document = saxReader.read(byteArrayInputStream);
                        Element rootElement = document.getRootElement();//获得根元素
                        Element name = rootElement.element("name");
                        sysVersion.setName(name.getText().trim());
                        sysVersion.setSystemVersion(rootElement.element("system_version").getText().trim());
                        sysVersion.setDatabaseVersion(rootElement.element("database_version").getText().trim());
                        sysVersion.setVersionDate(rootElement.element("version_date").getText().trim());
                        sysVersion.setVersionInfo(rootElement.element("version_info").getText().trim());
                    }
                    //图片文件
                    //首页logo图片
                    if (fileName.equals("main_logo.png")) {
                        bytes = new byte[(int) zipEntry.getSize()];
                        bs.read(bytes, 0, (int) zipEntry.getSize());
                        InputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
                        byte[] outLogo = ByteStreams.toByteArray(byteArrayInputStream);
                        String date1 = new String(Base64.getEncoder().encode(outLogo));
                        sysVersion.setOutLogo(date1);
                    }
                    //内页logo图片
                    if (fileName.equals("mini_logo.png")) {
                        bytes = new byte[(int) zipEntry.getSize()];
                        bs.read(bytes, 0, (int) zipEntry.getSize());
                        InputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
                        byte[] inLogo = ByteStreams.toByteArray(byteArrayInputStream);
                        String date = new String(Base64.getEncoder().encode(inLogo));
                        sysVersion.setInLogo(date);
                    }
                    //logo图片
                    if (fileName.equals("inTitleLogo.png")) {
                        bytes = new byte[(int) zipEntry.getSize()];
                        bs.read(bytes, 0, (int) zipEntry.getSize());
                        InputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
                        byte[] inLogo = ByteStreams.toByteArray(byteArrayInputStream);
                        String date3 = new String(Base64.getEncoder().encode(inLogo));
                        sysVersion.setInTitleLogo(date3);
                    }
                    //登录logo图片
                    if (fileName.equals("loginLogo.png")) {
                        bytes = new byte[(int) zipEntry.getSize()];
                        bs.read(bytes, 0, (int) zipEntry.getSize());
                        InputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
                        byte[] inLogo = ByteStreams.toByteArray(byteArrayInputStream);
                        String date4 = new String(Base64.getEncoder().encode(inLogo));
                        sysVersion.setLoginLogo(date4);
                    }

                }
            }
            sysVersionDao.importSysVersion(sysVersion);
        } finally {
            zipInputStream.close();
        }

        Map<Boolean, Object> map = new HashMap<>();
        map.put(true, "导入平台信息成功！");
        return map;
    }

    @Override
    public SysVersion selectSysVersion() {
        return sysVersionDao.selectSysVersion();
    }
}
