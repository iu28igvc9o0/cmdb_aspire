package com.migu.tsg.microservice.atomicservice.composite.controller.opsmanage;

import com.aspire.mirror.composite.service.opsmanage.ICompOpsGroupService;
import com.aspire.mirror.composite.service.opsmanage.payload.GroupRelationExport;
import com.aspire.mirror.ops.api.domain.GeneralResponse;
import com.aspire.mirror.ops.api.domain.GroupObjectDetail;
import com.aspire.mirror.ops.api.domain.GroupRelationBatchAddReq;
import com.aspire.mirror.ops.api.domain.GroupRelationDetail;
import com.aspire.mirror.ops.api.domain.GroupRelationQueryModel;
import com.aspire.mirror.ops.api.domain.OpsGroup;
import com.aspire.mirror.ops.api.domain.OpsResource;
import com.aspire.mirror.ops.api.domain.PageListQueryResult;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.opsmanage.OpsGroupClient;
import com.migu.tsg.microservice.atomicservice.composite.controller.CommonResourceController;
import com.migu.tsg.microservice.atomicservice.composite.controller.authcontext.RequestAuthContext;
import com.migu.tsg.microservice.atomicservice.composite.controller.authcontext.ResAction;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.EasyPoiUtil;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.PayloadParseUtil;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.ResourceAuthHelper;
import com.migu.tsg.microservice.atomicservice.composite.vo.rbac.RbacResource;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * TODO
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.migu.tsg.microservice.atomicservice.composite.controller.opsmanage
 * 类名称:    CompOpsGroupController.java
 * 类描述:    TODO
 * 创建人:    JinSu
 * 创建时间:  2020/3/9 17:23
 * 版本:      v1.0
 */
@RestController
public class CompOpsGroupController extends CommonResourceController implements ICompOpsGroupService {
    @Autowired
    private OpsGroupClient opsGroupClient;

    @Autowired
    protected ResourceAuthHelper resAuthHelper;

    @Override
    @ResAction(action = "view", resType = "group")
    public List<OpsGroup> queryGroupTree(@RequestBody OpsGroup opsGroup) {
        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        if (!authCtx.getUser().isAdmin() && !authCtx.getUser().isSuperUser()) {
            Map<String, Set<String>> totalConstraints = resAuthHelper.verifyActionAndGetResource(
                    authCtx.getUser(), authCtx.getResAction(), authCtx.getFlattenConstraints());

            if (!super.applyGeneralAuthConstraints(totalConstraints, opsGroup)) {
                return Lists.newArrayList();
            }
        }
        return opsGroupClient.queryGroupTree(opsGroup);
    }

    @Override
    @ResAction(action = "create", resType = "group")
    public GeneralResponse saveGroup(@RequestBody OpsGroup opsGroup) {
        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        resAuthHelper.resourceActionVerify(authCtx.getUser(), new RbacResource(), authCtx.getResAction(), authCtx.getFlattenConstraints());
        return opsGroupClient.saveGroup(opsGroup);
    }

    @Override
    @ResAction(action = "delete", resType = "group")
    public GeneralResponse deleteGroup(@RequestParam("group_id") Long groupId) {
        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        resAuthHelper.resourceActionVerify(authCtx.getUser(), new RbacResource(), authCtx.getResAction(), authCtx.getFlattenConstraints());
        return opsGroupClient.deleteGroup(groupId);
    }

    @Override
    @ResAction(action = "create", resType = "group")
    public GeneralResponse saveBatchGroupRelation(@RequestBody GroupRelationBatchAddReq groupRelationBatchAddReq) {
        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        resAuthHelper.resourceActionVerify(authCtx.getUser(), new RbacResource(), authCtx.getResAction(), authCtx.getFlattenConstraints());
        return opsGroupClient.saveBatchGroupRelation(groupRelationBatchAddReq);
    }

    @Override
    @ResAction(action = "view", resType = "group")
    public PageListQueryResult<GroupRelationDetail> querGroupRelationList(@RequestBody GroupRelationQueryModel groupRelationQueryModel) {
        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        resAuthHelper.resourceActionVerify(authCtx.getUser(), new RbacResource(), authCtx.getResAction(), authCtx.getFlattenConstraints());
        return opsGroupClient.querGroupRelationList(groupRelationQueryModel);
    }

    @Override
    @ResAction(action = "export", resType = "group")
    public void exportGroupRelation(@RequestBody GroupRelationQueryModel groupRelationQueryModel, HttpServletResponse response) {
        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        resAuthHelper.resourceActionVerify(authCtx.getUser(), new RbacResource(), authCtx.getResAction(), authCtx.getFlattenConstraints());

        groupRelationQueryModel.setPageNo(null);
        groupRelationQueryModel.setPageSize(null);
        PageListQueryResult<GroupRelationDetail> pageList = opsGroupClient.querGroupRelationList(groupRelationQueryModel);
        List<GroupRelationExport> exportList =  PayloadParseUtil.jacksonBaseParse(GroupRelationExport.class, pageList.getDataList());
        String fileName = "group_relation_export";
        EasyPoiUtil.exportExcel(exportList, "分组关联对象数据", "分组对象数据", GroupRelationExport.class, fileName, true, response);
    }

    @Override
    @ResAction(action = "view", resType = "group")
    public PageListQueryResult<GroupObjectDetail> queryObjectList(@RequestBody GroupRelationQueryModel groupRelationQueryModel) {
        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        resAuthHelper.resourceActionVerify(authCtx.getUser(), new RbacResource(), authCtx.getResAction(), authCtx.getFlattenConstraints());

        return opsGroupClient.queryObjectList(groupRelationQueryModel);
    }

    @Override
    @ResAction(action = "delete", resType = "group")
    public GeneralResponse deleteGroupRelation(@RequestParam("group_relation_ids") String groupRelationIds) {
        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        resAuthHelper.resourceActionVerify(authCtx.getUser(), new RbacResource(), authCtx.getResAction(), authCtx.getFlattenConstraints());

        return opsGroupClient.deleteGroupRelation(groupRelationIds);
    }

    @Override
    public List<OpsResource> querOpsResourceTree(@RequestParam("objectType") String objectType) {
        return opsGroupClient.querOpsResourceTree(objectType);
    }
}
