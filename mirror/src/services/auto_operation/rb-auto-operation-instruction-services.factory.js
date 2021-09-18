/*
* 自动化运维-敏感指令管理 项目
*/
import rbHttp from 'assets/js/utility/rb-http.factory'

export default class rbAutoOperationServicesFactory {
    /**
   * 敏感指令
   */
    // 查询敏感指令列表
    static async querySensitiveConfigList(req) {
        return rbHttp.sendRequest({
            method: 'POST',
            url: '/v1/ops-service/sensitive/querySensitiveConfigList',
            data: req
        }).then(function (data) {
            return data
        })
    }
    // 新增敏感指令
    static async saveSensitiveConfig(req) {
        return rbHttp.sendRequest({
            method: 'POST',
            url: '/v1/ops-service/sensitive/saveSensitiveConfig',
            data: req
        }).then(function (data) {
            return data
        })
    }
    // 删除敏感指令
    static async removeSensitiveConfig(req) {
        return rbHttp.sendRequest({
            method: 'DELETE',
            params: req,
            url: '/v1/ops-service/sensitive/removeSensitiveConfig'
        }).then(function (data) {
            return data
        })
    }
    // 根据id查询敏感指令详情
    static async getSensitiveConfig(req) {
        return rbHttp.sendRequest({
            method: 'GET',
            params: req,
            url: '/v1/ops-service/sensitive/getSensitiveConfig',
        }).then(function (data) {
            return data
        })
    }
    // 查询角色列表
    static async queryRoleList(req) {
        return rbHttp.sendRequest({
            method: 'POST',
            url: '/v1/roles/pageList',
            params: req
        }).then(function (data) {
            return data
        })
    }
    // 敏感指令规则状态更新
    static async updateStatusByRuleId(req) {
        return rbHttp.sendRequest({
            method: 'PUT',
            url: '/v1/ops-service/sensitive/updateStatusByRuleId',
            params: req
        }).then(function (data) {
            return data
        })
    }
    // 规则白名单分页查询
    static async querySensitiveRuleWhiteList(req) {
        return rbHttp.sendRequest({
            method: 'POST',
            url: '/v1/ops-service/sensitive/querySensitiveRuleWhiteList',
            params: req
        }).then(function (data) {
            return data
        })
    }
    // 规则白名单状态更新
    static async updateObjectStatusByWhiteId(req) {
        return rbHttp.sendRequest({
            method: 'PUT',
            url: '/v1/ops-service/sensitive/updateObjectStatusByWhiteId',
            params: req
        }).then(function (data) {
            return data
        })
    }

    /**
   * 敏感指令赋权审核历史管理
   */
    // 敏感指令审核列表
    static async querySensitiveReviewList(req) {
        return rbHttp.sendRequest({
            method: 'POST',
            url: '/v1/ops-service/sensitive/querySensitiveReviewList',
            data: req
        }).then(function (data) {
            return data
        })
    }
    // 审核匹配历史
    static async reviewInstance(req) {
        return rbHttp.sendRequest({
            method: 'PUT',
            params: req,
            url: '/v1/ops-service/sensitive/reviewInstance',
        }).then(function (data) {
            return data
        })
    }
    // 作业实例指令审核申请
    static async reviewSensitiveApply(pipelineInstanceId) {
        return rbHttp.sendRequest({
            method: 'PUT',
            url: `/v1/ops-service/opsManage/reviewSensitiveApply/${pipelineInstanceId}`,
        }).then(function (data) {
            return data
        })
    }
    // 敏感指令分级修改
    static async updateSensitiveLevelById(req) {
        return rbHttp.sendRequest({
            method: 'PUT',
            params: req,
            url: '/v1/ops-service/sensitive/updateSensitiveLevelById',
        }).then(function (data) {
            return data
        })
    }


    // 白名单
    static async querySensitiveRuleList(req) {
        return rbHttp.sendRequest({
            method: 'POST',
            url: '/v1/ops-service/sensitive/querySensitiveRuleList',
            data: req
        }).then(function (data) {
            return data
        })
    }

}
