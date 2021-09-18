package com.aspire.fileCheck.service.impl;

import com.aspire.fileCheck.dao.IFileIndicationDAO;
import com.aspire.fileCheck.dao.IFileIndicationPeriodConfigDAO;
import com.aspire.fileCheck.dao.IMirrorDictDAO;
import com.aspire.fileCheck.entity.FileIndicationEntity;
import com.aspire.fileCheck.entity.FileIndicationPeriodConfigEntity;
import com.aspire.fileCheck.service.IFileIndicationPeriodConfigService;
import com.aspire.mirror.dao.IIndicationProvinceDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jw.zhu
 * Date: 2018/10/24
 * 软探针异常指标监控系统
 * com.aspire.mirror.service.impl.IndicationDataServiceImpl
 */
@Service
@Slf4j
public class FileIndicationPeriodConfigServiceImpl implements IFileIndicationPeriodConfigService {

    @Autowired
    private IFileIndicationPeriodConfigDAO fileIndicationPeriodConfigDAO;
    
    @Autowired
    private IFileIndicationDAO fileIndicationDAO;

    @Autowired
    private IMirrorDictDAO mirrorDictDAO;

    @Autowired
	private IIndicationProvinceDao iIndicationProvinceDao;

	@Override
	public List<FileIndicationPeriodConfigEntity> getConfigsByIndicationId(Integer indicationId) {
		return fileIndicationPeriodConfigDAO.getConfigsByIndicationId(indicationId);
	}
    
	@Override
	public List<FileIndicationEntity> getFileIndication(String catalog) {
		return fileIndicationDAO.getFileIndication(catalog);
	}



	@Override
	public List<FileIndicationPeriodConfigEntity> getConfigsByPeriod(String calcHour) {
		return fileIndicationPeriodConfigDAO.getConfigsByPeriod(calcHour);
	}
}
