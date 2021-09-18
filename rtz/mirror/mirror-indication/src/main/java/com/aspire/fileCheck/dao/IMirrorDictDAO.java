package com.aspire.fileCheck.dao;

import com.aspire.fileCheck.entity.MirrorDictEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface IMirrorDictDAO {

  /**
   * 根据字典名称 获取字典列表
   * @param dictName
   * @return
   */
  List<MirrorDictEntity> getMirrorDictByDictName(@Param("dictName") String dictName);

  /**
   * 根据字典ID, 获取字典信息
   * @param dictId
   * @return
   */
  MirrorDictEntity getMirrorDictByDictId(@Param("dictId") Integer dictId);
}
