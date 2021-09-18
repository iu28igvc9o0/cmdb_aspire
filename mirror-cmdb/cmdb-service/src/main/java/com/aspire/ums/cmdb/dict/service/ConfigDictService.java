package com.aspire.ums.cmdb.dict.service;

import com.aspire.ums.cmdb.allocate.payload.Result;
import com.aspire.ums.cmdb.dict.payload.ConfigDict;
import com.aspire.ums.cmdb.dict.payload.Dict;

import java.util.List;
import java.util.Map;

public interface ConfigDictService {

    Result<Dict> getAllConfigDictData(int pageNum, int startPageNum, int pageSize, String pname, String name, String value,
            String type);

    List<Dict> getDictAll(String dictId);

    String insert(Dict dict);

    String update(Dict dict);

    String delete(String dictId);

    String delete(Dict dict);

    Dict getDictById(String dictId);

    String getValueById(String dictId);

    String getIdByNoteAndCol(String dictNote, String colName);

    List<Map<String, Object>> getDictExportData(String pcode, String dictCode, String dictNote, String colName);

    List<ConfigDict> selectDicts();

    /**
     * 获取字典值列表
     * 
     * @param type
     *            字典类型
     * @param pid
     *            父字典ID
     * @param pValue
     *            父字典值
     * @param pType
     *            父字典类型
     * @return 字典值列表
     */
    List<ConfigDict> selectDictsByType(String type, String pid, String pValue, String pType);

    List<ConfigDict> getDictTree(String colName);

    List<Dict> getDictByIds(List<String> dictIds);

    List<String> getDictType();

    List<Map> getDistinctDictType();

    Map<String, String> getIdcTypeByName(String idcName);

    /**
     * 根据字典类型和字段编码 获取数据字典信息
     * 
     * @param colName
     *            字典类型
     * @param dictCode
     *            字典编码
     * @return
     */
    ConfigDict getDictByColNameAndDictCode(String colName, String dictCode);
}
