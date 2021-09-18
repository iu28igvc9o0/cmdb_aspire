package com.aspire.mirror.indexadapt.adapt.inspectiondb.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.aspire.mirror.indexadapt.adapt.inspectiondb.model.InpsectionRawIndex;

@Mapper
public interface InspectionIndexDao {
	
	public List<InpsectionRawIndex> fetchInspectionRawIndexList(@Param(value = "indexSeq") int startIdxSeq);
	
}
