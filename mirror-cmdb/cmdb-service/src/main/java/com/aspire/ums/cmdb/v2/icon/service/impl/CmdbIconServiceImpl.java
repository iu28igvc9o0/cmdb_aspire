package com.aspire.ums.cmdb.v2.icon.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.aspire.ums.cmdb.icon.payload.CmdbIcon;
import com.aspire.ums.cmdb.util.BusinessException;
import com.aspire.ums.cmdb.util.FtpUtls;
import com.aspire.ums.cmdb.util.UUIDUtil;
import com.aspire.ums.cmdb.v2.icon.mapper.CmdbIconMapper;
import com.aspire.ums.cmdb.v2.icon.service.CmdbIconSerivce;

@Service
@Transactional
public class CmdbIconServiceImpl implements CmdbIconSerivce {

    @Autowired
    private CmdbIconMapper iconMapper;

    private static Logger logger = LoggerFactory.getLogger(CmdbIconServiceImpl.class);

    @Value("${ftp.host}")
    private String host;

    @Value("${ftp.port}")
    private String port;

    @Value("${ftp.username}")
    private String user;

    @Value("${ftp.password}")
    private String password;

    @Value("${iconSize}")
    private String iconSize;

    @Override
    public void uploadIcon(HttpServletRequest request) throws BusinessException, Exception {
        List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles("file");
        MultipartFile file = null;
        InputStream is = null;
        // 获取配置文件中的上传图标文件的最大大小
        int ftpPort = Integer.valueOf(port);
        Long iconMaxsize = Long.valueOf(iconSize);
        CmdbIcon icon = null;
        for (int i = 0; i < files.size(); i++) {
            file = files.get(i);
            String filename = file.getOriginalFilename();
            System.out.println(file.getSize());
            if (file.getSize() > iconMaxsize) {
                throw new BusinessException("上传的图标文件过大");
            }
            if (!file.isEmpty()) {
                is = file.getInputStream();
                String result = FtpUtls.uploadImage(host, ftpPort, user, password, filename, is);
                Integer sortindex = iconMapper.selectMaxSortIndex();
                if (null != result && !"".equals(result)) {
                    icon = new CmdbIcon(UUIDUtil.getUUID(), result, 1, sortindex + 1, 0);
                    iconMapper.insertSelective(icon);
                } else {
                    logger.error("图标文件[" + filename + "]上传失败");
                    throw new BusinessException("图标文件[" + filename + "]上传失败");
                }
            }
        }
        if(null != is) {
            // hangfang 2020.07.30 资源未释放：流
            try {
                is.close();
            } catch (IOException e) {
                e.getStackTrace();
            }
        }
    }
}
