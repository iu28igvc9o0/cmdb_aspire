package com.migu.tsg.microservice.atomicservice.composite.controller.common;

import com.aspire.mirror.composite.service.common.ICompDictService;
import com.aspire.mirror.composite.service.common.payload.CompCodeDictResponse;
import com.migu.tsg.microservice.atomicservice.composite.biz.DictBiz;
import com.migu.tsg.microservice.atomicservice.composite.controller.authcontext.Authentication;
import com.migu.tsg.microservice.atomicservice.composite.service.dict.DictDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import static com.migu.tsg.microservice.atomicservice.composite.controller.util.PayloadParseUtil.jacksonBaseParse;

/**
 * 字典控制层
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.migu.tsg.microservice.atomicservice.composite.controller.common
 * 类名称:    CompDictController.java
 * 类描述:    字典控制层
 * 创建人:    JinSu
 * 创建时间:  2018/11/9 17:14
 * 版本:      v1.0
 */
@RestController
public class CompDictController implements ICompDictService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CompDictController.class);

    @Autowired
    private DictBiz dictService;

    @Override
    @Authentication(anonymous = true)
    public CompCodeDictResponse listAll() {
        DictDTO dictResponse = dictService.findAll();
        return jacksonBaseParse(CompCodeDictResponse.class, dictResponse);
    }
}
