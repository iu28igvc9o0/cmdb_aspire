package com.aspire.ums.cmdb.v2.search.mapper;


import com.aspire.ums.cmdb.search.payload.ColumnFilter;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SearchMapper {
	/**
	 * 查询过滤项
	 * @param columnFilter
	 * @return
	 */
    ColumnFilter getOne(ColumnFilter columnFilter);

	/**
	 * 新增过滤项
	 * @param columnFilter
	 */
	void insert(ColumnFilter columnFilter);

	/**
	 * 修改过滤项
	 * @param columnFilter
	 */
	void update(ColumnFilter columnFilter);

	/**
	 * 删除过滤项
	 * @param id 过滤项ID
	 */
	void delete(String id);

}
