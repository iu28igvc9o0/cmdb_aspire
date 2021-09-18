package com.aspire.mirror.common.server.biz.impl;

import com.aspire.mirror.common.api.dto.model.DictDTO;
import com.aspire.mirror.common.api.dto.model.DictValue;
import com.aspire.mirror.common.server.biz.DictBiz;
import com.aspire.mirror.common.server.dao.CodeDictDao;
import com.aspire.mirror.common.server.dao.po.CodeDict;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 字典业务层实现
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.common.server.biz.impl
 * 类名称:    DictBizImpl.java
 * 类描述:    字典业务层实现
 * 创建人:    JinSu
 * 创建时间:  2018/11/12 10:49
 * 版本:      v1.0
 */
@Service
public class DictBizImpl implements DictBiz {
    @Override
    public DictDTO findList() {
        DictDTO response = new DictDTO();
        try {
            List<CodeDict> lists = dictDao.fetchCodeDict();
            Map<String, Map<String, String>> codeDictMap = new HashMap<>();
            for (CodeDict codeDict : lists) {
                Map<String, String> codeDicts = codeDictMap.get(codeDict
                        .getCodeType());
                if (codeDicts == null) {
                    codeDicts = new HashMap<>();
                    codeDictMap.put(codeDict.getCodeType(), codeDicts);
                }
                codeDicts.put(codeDict.getKey(), codeDict.getValue());
            }
            response.setCode("0");
            response.setDesc("SUCCESS");
            response.setResult(gson.toJson(codeDictMap));

        } catch (Exception e) {
            e.printStackTrace();
            response.setCode("1");
            response.setDesc("SQL FAIL");
        }
        return response;
    }

    @Override
    public List<CodeDict> getCodeDictByCodeType(String codeType) {
       return dictDao.getCodeDictByCodeType(codeType);
    }

    @Override
    public DictDTO findAll() {
        DictDTO response = new DictDTO();
        try {
            List<CodeDict> lists = dictDao.fetchCodeDict();
//            Map<String, Map<String, String>> codeDictMap = new HashMap<String, Map<String, String>>();
//            for (CodeDict codeDict : lists) {
//                Map<String, String> codeDicts = codeDictMap.get(codeDict
//                        .getCodeType());
//                if (codeDicts == null) {
//                    codeDicts = new HashMap<String, String>();
//                    codeDictMap.put(codeDict.getCodeType(), codeDicts);
//                }
//                codeDicts.put(codeDict.getKey(), codeDict.getValue());
//            }
            // 支持子集
            Map<String, Map<String, DictValue>> codeDictMap = new HashMap<>();
            for (CodeDict codeDict : lists) {
                Map<String, DictValue> codeDicts = codeDictMap.get(codeDict
                        .getCodeType());
                if (codeDicts == null) {
                    codeDicts = new HashMap<>();
                    codeDictMap.put(codeDict.getCodeType(), codeDicts);
                }
                if (codeDict.getParentId() == null || codeDict.getParentId() == 0) {
                    Map<String, DictValue> childs = new HashMap<>();
                    if (existChild(lists, codeDict.getId())) {
                        getChilds(lists, codeDict.getId(), childs);
                    }
                    codeDicts.put(codeDict.getKey(), new DictValue(codeDict.getValue(), childs));
                }
            }
            response.setCode("0");
            response.setDesc("SUCCESS");
            response.setResult(gson.toJson(codeDictMap));

        } catch (Exception e) {
            e.printStackTrace();
            response.setCode("1");
            response.setDesc("SQL FAIL");
        }
        return response;
    }

    private void getChilds(List<CodeDict> lists, Integer id, Map<String, DictValue> childs) {
        for (CodeDict codeDict : lists) {
            if (codeDict.getParentId() != null && codeDict.getParentId().equals(id)) {
                Map<String, DictValue> subChilds = new HashMap<>();
                if (existChild(lists, codeDict.getId())) {
                    getChilds(lists, codeDict.getId(), subChilds);
                }
                childs.put(codeDict.getKey(), new DictValue(codeDict.getValue(), subChilds));
            }
        }
    }

    private boolean existChild(List<CodeDict> lists, Integer id) {
        for (CodeDict codeDict : lists) {
            if (codeDict.getParentId() != null && codeDict.getParentId().equals(id)) {
                return true;
            }
        }
        return false;
    }

    @Autowired
    private CodeDictDao dictDao;

    private final Gson gson = new Gson();
}
