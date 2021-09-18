/*
* 自动化运维-自定义参数管理 项目
*/
import rbHttp from 'assets/js/utility/rb-http.factory'

export default class rbAutoOperationServicesFactory {
    // 参数类型
    static async loadAllParamTypeList() {
        return rbHttp.sendRequest({
            method: 'GET',
            url: '/v1/ops-service/opsManage/loadAllParamTypeList',
        }).then(function (data) {
            return data
        })
    }
    // 根据脚本ID查询参数关联
    static async queryReferParamListByEntityId(entityId) {
        return rbHttp.sendRequest({
            method: 'GET',
            url: `/v1/ops-service/opsManage/queryReferParamListByEntityId/${entityId}`
        }).then(function (data) {
            return data
        })
    }
    // 审核脚本
    static async auditScript(req) {
        return rbHttp.sendRequest({
            method: 'PUT',
            url: '/v1/ops-service/opsManage/auditScript',
            params: req
        }).then(function (data) {
            return data
        })
    }
    // 新增
    static async createOpsParam(req) {
        return rbHttp.sendRequest({
            method: 'POST',
            url: '/v1/ops-service/opsManage/createOpsParam',
            data: req
        }).then(function (data) {
            return data
        })
    }
    // 修改
    static async updateOpsParam(req) {
        return rbHttp.sendRequest({
            method: 'PUT',
            url: '/v1/ops-service/opsManage/updateOpsParam',
            data: req
        }).then(function (data) {
            return data
        })
    }
    // 删除
    static async removeParam(paramId) {
        return rbHttp.sendRequest({
            method: 'DELETE',
            url: `/v1/ops-service/opsManage/deleteOpsParamById/${paramId}`
        }).then(function (data) {
            return data
        })
    }

    // 查询所有敏感指令规则列表
    static async querySensitiveRuleList(req) {
        return rbHttp.sendRequest({
            method: 'POST',
            url: '/v1/ops-service/sensitive/querySensitiveRuleList',
            data: req
        }).then(function (data) {
            return data
        })
    }

    // 批量新增敏感指令白名单
    static async batchCreateSensitiveRuleWhite(req) {
        return rbHttp.sendRequest({
            method: 'POST',
            url: '/v1/ops-service/sensitive/batchCreateSensitiveRuleWhite',
            data: req
        }).then(function (data) {
            return data
        })
    }

    // 导出漏洞实例列表
    static async exportVulInstance(req) {
        return rbHttp.sendRequest({
            method: 'POST',
            responseType: 'arraybuffer',
            url: '/v1/ops-service/vulnerability/exportVulInstance',
            data: req
        }).then(function (data) {
            return data
        })
    }

}
