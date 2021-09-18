package com.aspire.ums.cmdb.common.mapper;

import com.aspire.ums.cmdb.common.entity.DictEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DictMapper {
    /**
     * 根据字典值编码，查询字典值列表
     * @param code 字典编码
     * @return 字典值列表
     */
	List<DictEntity> getDictByCode(@Param("code") String code);
}