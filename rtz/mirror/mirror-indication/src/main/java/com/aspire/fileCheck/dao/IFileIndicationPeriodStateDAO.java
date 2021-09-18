package com.aspire.fileCheck.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.aspire.fileCheck.entity.FileIndicationPeriodStateEntity;
import com.aspire.fileCheck.entity.FileStateReport;

@Mapper
public interface IFileIndicationPeriodStateDAO {

	List<FileIndicationPeriodStateEntity> getDataByDate(String uploadDate);

	List<FileIndicationPeriodStateEntity> getMinUpdateIntegrity(
			@Param("startDate") String startDate,
			@Param("endDate") String endDate);

	int batchUpdateStates(List<FileIndicationPeriodStateEntity> list);

	int batchInsertStates(List<FileIndicationPeriodStateEntity> list);

	List<FileStateReport> getReport(@Param("catolog") String catolog,
			@Param("provinceCode") String provinceCode,
			@Param("period") String period, @Param("thisDate") String thisDate,
			@Param("lastDate") String lastDate,
			@Param("fileIndicationId") String fileIndicationId);

}
