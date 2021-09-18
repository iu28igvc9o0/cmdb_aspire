package com.aspire.ums.cmdb.module.service.impl;

import java.io.InputStream;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.aspire.ums.cmdb.module.entity.Icon;
import com.aspire.ums.cmdb.module.mapper.IconMapper;
import com.aspire.ums.cmdb.module.service.IconService;
import com.aspire.ums.cmdb.util.BusinessException;
import com.aspire.ums.cmdb.util.FtpUtls;
import com.aspire.ums.cmdb.util.UUIDUtil;

@Service("iconService")
@Transactional
public class IconServiceImpl implements IconService {
	@Autowired
	private IconMapper iconMapper;
	
	private static Logger logger = Logger.getLogger(IconServiceImpl.class);
	
	@Value("${ftp.address}")
	private String host;
	@Value("${ftp.port}")
	private String port;
	@Value("${ftp.user}")
	private String user;
	@Value("${ftp.password}")
	private String password;
	@Value("${iconSize}")
	private String iconSize;
	
	@Override
	public void uploadIcon(HttpServletRequest request) throws BusinessException,Exception {
		List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles("file");
		MultipartFile file = null;
		InputStream is = null;
		//获取配置文件中的上传图标文件的最大大小
		int ftpPort = Integer.valueOf(port);
		Long iconMaxsize = Long.valueOf(iconSize);
		Icon icon = null;
		for(int i=0;i<files.size();i++){
			file = files.get(i);
			String filename = file.getOriginalFilename();
			System.out.println(file.getSize());
			if(file.getSize()>iconMaxsize){
				throw new BusinessException("上传的图标文件过大");
			}
			if (!file.isEmpty()) {
				is = file.getInputStream();
				String result = FtpUtls.uploadImage(host, ftpPort, user, password, filename, is);
				Integer sortindex = iconMapper.selectMaxSortIndex();
				if(null !=result && !"".equals(result)){
					icon = new Icon(UUIDUtil.getUUID(),result,1,sortindex+1,0);
					iconMapper.insertSelective(icon);
				}else{
					logger.error("图标文件["+filename+"]上传失败");
					throw new BusinessException("图标文件["+filename+"]上传失败");
				}
			}
		}
	}
}
