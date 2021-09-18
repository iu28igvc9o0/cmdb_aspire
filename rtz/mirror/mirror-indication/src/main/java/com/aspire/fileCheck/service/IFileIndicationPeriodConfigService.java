package com.aspire.fileCheck.service;

import java.util.List;

import com.aspire.fileCheck.entity.FileIndicationEntity;
import com.aspire.fileCheck.entity.FileIndicationPeriodConfigEntity;


public interface IFileIndicationPeriodConfigService {
	
	List<FileIndicationPeriodConfigEntity> getConfigsByIndicationId(Integer indicationId);

	List<FileIndicationEntity> getFileIndication(String catalog);

	/**
	 * 根据时间段, 获取需要检测的指标配置信息
	 * @param calcHour
	 * @return
	 */
    List<FileIndicationPeriodConfigEntity> getConfigsByPeriod(String calcHour);
}
