/*
* 自动化运维-黑名单管理
*/
import rbHttp from 'assets/js/utility/rb-http.factory'

export default class rbAutoOperationServicesFactory {
    /**
   * 主机黑名单管理
   */
    // 保存单个主机白名单
    static async saveWhitelistHost(req) {
        return rbHttp.sendRequest({
            method: 'POST',
            url: '/v1/ops-service/whitelist/saveWhitelistHost',
            data: req
        }).then(function (data) {
            return data
        })
    }
    // 下载主机白名单中主机导入模板
    static async downloadTargetHostTemplate() {
        return rbHttp.sendRequest({
            method: 'GET',
            url: '/v1/ops-service/opsManage/downloadTargetHostTemplate',
        }).then(function (data) {
            return data
        })
    }
    // 主机白名单新增时导入主机列表
    static async importWhitelistHost(req) {
        return rbHttp.sendRequest({
            method: 'POST',
            url: '/v1/ops-service/whitelist/importWhitelistHost',
            data: req
        }).then(function (data) {
            return data
        })
    }
    // 批量保存主机白名单
    static async batchSaveWhitelistHost(req) {
        return rbHttp.sendRequest({
            method: 'POST',
            url: '/v1/ops-service/whitelist/batchSaveWhitelistHost',
            data: req
        }).then(function (data) {
            return data
        })
    }
    // 巡检白名单新增时加载巡检指标列表
    static async itemsPageList(req) {
        return rbHttp.sendRequest({
            method: 'POST',
            url: '/v1/items/pageList',
            data: req
        }).then(function (data) {
            return data
        })
    }
    // 保存巡检黑名单
    static async saveWhitelistCruiseCheck(req) {
        return rbHttp.sendRequest({
            method: 'POST',
            url: '/v1/ops-service/whitelist/saveWhitelistCruiseCheck',
            data: req
        }).then(function (data) {
            return data
        })
    }
    // 根据关键属性查询巡检黑名单
    static async queryOpsWhitelistCruiseCheckByKeys(req) {
        return rbHttp.sendRequest({
            method: 'POST',
            url: '/v1/ops-service/whitelist/queryOpsWhitelistCruiseCheckByKeys',
            data: req
        }).then(function (data) {
            return data
        })
    }
    // 根据ID移除巡检黑名单
    static async removeWhitelistCruiseCheckById(id) {
        return rbHttp.sendRequest({
            method: 'DELETE',
            url: `/v1/ops-service/whitelist/removeWhitelistCruiseCheckById/${id}`
        }).then(function (data) {
            return data
        })
    }
    // 保存漏洞黑名单
    static async saveWhitelistVulnerability(req) {
        return rbHttp.sendRequest({
            method: 'POST',
            url: '/v1/ops-service/whitelist/saveWhitelistVulnerability ',
            data: req
        }).then(function (data) {
            return data
        })
    }
    // 根据关键属性查询漏洞黑名单
    static async queryOpsWhitelistVulnerabilityByKeys(req) {
        return rbHttp.sendRequest({
            method: 'POST',
            url: '/v1/ops-service/whitelist/queryOpsWhitelistVulnerabilityByKeys',
            data: req
        }).then(function (data) {
            return data
        })
    }
    // 根据ID移除漏洞黑名单
    static async removeWhitelistVulnerabilityById(id) {
        return rbHttp.sendRequest({
            method: 'PUT',
            url: `/v1/ops-service/whitelist/removeWhitelistVulnerabilityById/${id}`
        }).then(function (data) {
            return data
        })
    }
    // 保存白名单关联的约束
    static async saveWhitelistLinkConstraints(req) {
        return rbHttp.sendRequest({
            method: 'POST',
            url: '/v1/ops-service/whitelist/saveWhitelistLinkConstraints',
            data: req
        }).then(function (data) {
            return data
        })
    }
    // 根据白名单类型和id查询关联的约束列表
    static async queryWhitelistConstraintListByTypeAndId(type, id) {
        return rbHttp.sendRequest({
            method: 'POST',
            url: `/v1/ops-service/whitelist/queryWhitelistConstraintListByTypeAndId/${type}/${id}`
        }).then(function (data) {
            return data
        })
    }




}
