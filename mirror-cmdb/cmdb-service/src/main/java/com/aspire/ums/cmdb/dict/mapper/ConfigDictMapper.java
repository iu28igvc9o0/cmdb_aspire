package com.aspire.ums.cmdb.dict.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.aspire.ums.cmdb.dict.payload.ConfigDict;
import com.aspire.ums.cmdb.dict.payload.Dict;
import com.aspire.ums.cmdb.dict.payload.DictListReq;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司 FileName: CollectService Author: ws Date: 2019/4/1 Description: ${DESCRIPTION} History:
 * <author> <time> <version> <desc> 作者姓名 修改时间 版本号 描述
 */
@Mapper
public interface ConfigDictMapper {

    int getConfigDictDataCount(DictListReq request);

    /**
     * 分页按条件查询
     *
     * @param request
     * @return
     */
    List<Dict> getConfigDictData(DictListReq request);

    /**
     * 所有字典表(除了本身)
     *
     * @return
     */
    List<Dict> getDictAll(@Param(value = "dictId") String dictId);

    /**
     * 根据code查询
     *
     * @param code
     * @return
     */
    List<ConfigDict> getDictByCode(@Param(value = "code") String code);

    /**
     * 新增数据
     *
     * @param dict
     *            动作PO对象
     * @return int 新增数据条数
     */
    int insert(Dict dict);

    /**
     * 根据主键删除数据
     *
     * @param dictId
     *            主键
     * @return int 删除数据条数
     */
    int deleteByPrimaryKey(@Param(value = "dictId") String dictId);

    /**
     * 根据主键更新数据
     *
     * @param dict
     *            动作PO对象
     * @return int 数据条数
     */
    int updateByPrimaryKey(Dict dict);

    Dict getDictById(@Param(value = "dictId") String dictId);

    String getValueById(@Param(value = "dictId") String dictId);

    String getIdByNoteAndCol(@Param(value = "dictNote") String dictNote, @Param(value = "colName") String colName);

    List<Map<String, Object>> getDictExportData(@Param(value = "pcode") String pcode, @Param(value = "dictCode") String dictCode,
            @Param(value = "dictNote") String dictNote, @Param(value = "colName") String colName);

    List<ConfigDict> selectDicts();

    List<ConfigDict> selectDictsByType(@Param(value = "colName") String colName, @Param(value = "pid") String pid,
            @Param(value = "pValue") String pValue, @Param(value = "pType") String pType);

    List<ConfigDict> selectDictsByUpDict(String id);

    // 操作日志
    void insertAddLog(@Param(value = "id") String id);

    void insertEditLog(@Param(value = "id") String id);

    void insertDeleteLog(@Param(value = "id") String id);

    List<String> getDictType();

    List<ConfigDict> selectDictsByTypeAndIds(@Param(value = "colName") String colName, @Param(value = "ids") String[] idArray);

    List<ConfigDict> selectDictsByUpDictAndIds(@Param(value = "pid") Integer id, @Param(value = "ids") String[] ids);

    List<Dict> getDictByIds(@Param(value = "dictIds") List<String> dictIds);

    /**
     * 查询所有的字典类型及描述
     *
     * @return
     */
    List<Map> getDistinctDictType();

    /**
     * 是否存在 标签名、值、类型相同的数据
     *
     * @param dict
     * @return
     */
    int getByDict(Dict dict);

    /**
     * 获取一级部门列表
     *
     * @return 部门列表
     */
    List<ConfigDict> getDepartment1();

    /**
     * 获取二级部门列表
     *
     * @param parentId
     *            父部门ID
     * @return
     */
    List<ConfigDict> getDepartment2(@Param("parentId") String parentId);

    /**
     * 获取业务系统列表
     *
     * @param departmentId
     *            部门ID
     * @return 业务系统列表
     */
    List<ConfigDict> getBizSystemList(@Param("departmentId") String departmentId);

    /**
     * 获取资源池列表
     *
     * @return
     */
    List<ConfigDict> getIdcTypeList();

    /**
     * 根据名称获取资源池
     *
     * @return
     */
    Map<String, String> getIdcTypeByName(@Param("idcName") String idcName);

    /**
     * 获取资源池下的项目名称
     *
     * @param idcId
     *            资源池ID
     * @return
     */
    List<ConfigDict> getProjectNameList(@Param("idcId") String idcId);

    /**
     * 获取项目下的所有POD名称
     *
     * @param idcId
     *            项目ID
     * @return
     */
    List<ConfigDict> getPodNameList(@Param("idcId") String idcId);

    /**
     * 获取POD下的所有机房位置
     *
     * @param podId
     *            pod ID
     * @return
     */
    List<ConfigDict> getRoomList(@Param("idcId") String idcId);

    /**
     * 获取所有的维保厂家
     *
     * @return
     */
    List<ConfigDict> getMaintenFactory();

    /**
     * 获取所有的制造厂商
     *
     * @return
     */
    List<ConfigDict> getDeviceMfrs();

    /**
     * 根据字典类型和字段编码 获取数据字典信息
     *
     * @param colName
     *            字典类型
     * @param dictCode
     *            字典编码
     * @return
     */
    ConfigDict getDictByColNameAndDictCode(@Param("colName") String colName, @Param("dictCode") String dictCode);

    /**
     * 通过字典表类型和父级ID获取对应字典数据
     * @param colName 类型
     * @param pid 父级ID
     */
    List<ConfigDict> selectDictsByTypeOrPid(@Param(value = "colName") String colName, @Param(value = "pid") String pid);
}
