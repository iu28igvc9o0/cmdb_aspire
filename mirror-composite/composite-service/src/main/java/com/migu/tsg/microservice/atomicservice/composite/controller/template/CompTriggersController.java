package com.migu.tsg.microservice.atomicservice.composite.controller.template;

import com.aspire.mirror.composite.service.template.ICompTriggersService;
import com.aspire.mirror.composite.service.template.payload.CompTriggersDetailResponse;
import com.aspire.mirror.template.api.dto.TriggersDetailResponse;
import com.google.common.collect.Lists;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.template.TriggersServiceClient;
import com.migu.tsg.microservice.atomicservice.composite.controller.CommonResourceController;
import com.migu.tsg.microservice.atomicservice.composite.controller.authcontext.ResAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.migu.tsg.microservice.atomicservice.composite.controller.util.PayloadParseUtil.jacksonBaseParse;

/**
 * 触发器控制层
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.migu.tsg.microservice.atomicservice.composite.controller.template
 * 类名称:    CompTriggersController.java
 * 类描述:    触发器控制层
 * 创建人:    JinSu
 * 创建时间:  2018/11/14 14:10
 * 版本:      v1.0
 */
@RestController
public class CompTriggersController extends CommonResourceController implements ICompTriggersService {
    private final Logger logger = LoggerFactory.getLogger(CompTriggersController.class);

    @Autowired
    private TriggersServiceClient triggersService;

    @Override
    @ResAction(action = "view", resType = "template")
    public List<CompTriggersDetailResponse> listByTemplateId(@PathVariable("template_id") String templateId) {
        logger.info("method[listByTemplateId] request templateId is {}.", templateId);
//        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
//        logger.info("[findByPrimaryKey]Username is {},namespace is {}.", authCtx.getUser().getUsername(), authCtx.getUser().getNamespace());
//        RbacResource rbacData = new RbacResource();
//        rbacData.setResTypeAction(authCtx.getResAction());
//        logger.info("[findByPrimaryKey]The rbacResource is {}.",rbacData);
//        resAuthHelper.resourceActionVerify(
//                authCtx.getUser(), rbacData, authCtx.getResAction(), authCtx.getFlattenConstraints());
        List<TriggersDetailResponse> list = triggersService.listByTemplateId(templateId);
        List<CompTriggersDetailResponse> responseList = Lists.newArrayList();
        for (TriggersDetailResponse triggersDetailResponse : list) {
            responseList.add(jacksonBaseParse(CompTriggersDetailResponse.class, triggersDetailResponse));
        }
        return responseList;
    }
}
