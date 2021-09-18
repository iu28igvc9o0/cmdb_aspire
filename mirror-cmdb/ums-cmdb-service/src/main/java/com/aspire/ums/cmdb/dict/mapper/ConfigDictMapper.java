package com.aspire.ums.cmdb.dict.mapper;

import com.aspire.ums.cmdb.dict.entity.ConfigDict;
import com.aspire.ums.cmdb.dict.entity.Dict;
import com.aspire.ums.cmdb.dict.entity.DictListReq;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: CollectService
 * Author:   ws
 * Date:     2019/4/1
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Mapper
public interface ConfigDictMapper {
    
    int getConfigDictDataCount(DictListReq request);
    /**
     * 分页按条件查询
     * @param request
     * @return
     */
    List<Dict> getConfigDictData(DictListReq request);
    
    /**
     * 所有字典表(除了本身)
     * @return
     */
    List<Dict> getDictAll(@Param(value = "dictId") String dictId);
    
    /**
     * 根据code查询
     * @param code
     * @return
     */
    List<ConfigDict> getDictByCode(@Param(value = "code") String code);
    
    /**
     * 新增数据
     *
     * @param dict 动作PO对象
     * @return int 新增数据条数
     */
    int insert(Dict dict);
    
    /**
     * 根据主键删除数据
     *
     * @param dictId 主键
     * @return int 删除数据条数
     */
    int deleteByPrimaryKey(@Param(value = "dictId") String dictId);
    
    /**
     * 根据主键更新数据
     *
     * @param dict 动作PO对象
     * @return int 数据条数
     */
    int updateByPrimaryKey(Dict dict);
    
    
    Dict getDictById(@Param(value = "dictId") String dictId);
    
    List<Map<String,Object>> getDictExportData(@Param(value = "pcode") String pcode,
                                               @Param(value = "dictCode") String dictCode,
                                               @Param(value = "dictNote") String dictNote,
                                               @Param(value = "colName") String colName);
    
    List <ConfigDict> selectDicts();
    
    List <ConfigDict> selectDictsByType(@Param(value = "colName") String colName,@Param(value = "pid") String pid);

    List<ConfigDict> selectDictsByUpDict(Integer id);

    List<ConfigDict> selectDictDataByValue(@Param(value = "pValue") String pValue,
                                           @Param(value = "pType") String pType,
                                           @Param(value = "type") String type);

    //操作日志
    void insertAddLog(@Param(value = "id") String id);
    void insertEditLog(@Param(value = "id") String id);
    void insertDeleteLog(@Param(value = "id") String id);
    
    List<String> getDictType();
	
	
    List<ConfigDict> selectDictsByTypeAndIds(@Param(value = "colName") String colName, @Param(value = "ids") String[] idArray);


    List<ConfigDict> selectDictsByUpDictAndIds(@Param(value = "pid") Integer id, @Param(value = "ids") String[] ids);

    List<Dict> getDictByIds(@Param(value = "dictIds") List<String> dictIds);
}
