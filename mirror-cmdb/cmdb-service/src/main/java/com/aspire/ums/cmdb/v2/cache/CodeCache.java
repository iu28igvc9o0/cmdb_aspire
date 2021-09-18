package com.aspire.ums.cmdb.v2.cache;

import com.aspire.ums.cmdb.code.payload.CmdbCode;
import com.aspire.ums.cmdb.code.payload.FullCode;
import com.aspire.ums.cmdb.v2.code.service.ICmdbCodeService;
import com.aspire.ums.cmdb.v2.code.service.ICmdbCodeValidateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: CodeCache
 * Author:   zhu.juwang
 * Date:     2019/5/20 21:58
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Component
@Slf4j
public class CodeCache {

//    @Autowired
//    private ICmdbCodeService codeService;
//    @Autowired
//    private ICmdbCodeValidateService validateService;
//
//    /**
//     * 刷新Code List缓存
//     */
//    public void refreshCache() {
//        List<String> groupList = codeService.listForGroup();
//        List<FullCode> fullCodeList = new LinkedList<>();
//        log.info("开始计算码表缓存...");
//        for (String group : groupList) {
//            CmdbCode queryEntity = new CmdbCode();
//            queryEntity.setCodeGroup(group);
//            List<CmdbCode> codeList = codeService.listByEntity(queryEntity);
//            List<FullCode.CmdbCode> fCodeList = new LinkedList<>();
//            for (CmdbCode cmdbCode : codeList) {
//                FullCode.CmdbCode fCode = new FullCode.CmdbCode();
//                fCode.setControlType(cmdbCode.getControlType());
//                fCode.setCmdbCode(cmdbCode);
//                fCode.setValidateList(validateService.getValidateListByCodeId(cmdbCode.getCodeId()));
//                fCodeList.add(fCode);
//            }
//            FullCode fullCode = new FullCode();
//            fullCode.setGroupName(group);
//            fullCode.setCodeList(fCodeList);
//            fullCodeList.add(fullCode);
//        }
//        CacheConst.CACHE_CODE_LIST = fullCodeList;
//        log.info("计算码表缓存结束.");
//    }
}
