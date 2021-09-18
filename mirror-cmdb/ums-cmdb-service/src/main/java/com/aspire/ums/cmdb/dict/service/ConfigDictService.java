package com.aspire.ums.cmdb.dict.service;

import com.aspire.ums.cmdb.allocate.entity.Result;
import com.aspire.ums.cmdb.dict.entity.ConfigDict;
import com.aspire.ums.cmdb.dict.entity.Dict;

import java.util.List;
import java.util.Map;

public interface ConfigDictService {
    
    Result<Dict> getAllConfigDictData(int pageNum, int startPageNum, int pageSize, String pname, String name, String value, String type);
    
    List<Dict> getDictAll(String dictId);
    
    String insert(Dict dict);
    
    String update(Dict dict);
    
    String delete(String dictId);
    
    Dict getDictById(String dictId);
    
    List<Map<String,Object>> getDictExportData(String pcode,String dictCode,String dictNote,String colName);
    
    List <ConfigDict> selectDicts();
    
    List <ConfigDict> selectDictsByType(String colName,String pid,String pValue,String pType);

    List<ConfigDict> getDictTree(String colName, String ids);

    List<Dict> getDictByIds(List<String> dictIds);
    
    List<String> getDictType();
}
