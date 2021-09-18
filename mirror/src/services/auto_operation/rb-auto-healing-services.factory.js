/*
* 自动化运维-故障自愈规则
*/
import rbHttp from 'assets/js/utility/rb-http.factory'

export default class rbAutoHealingServicesFactory {
    /**
     * 故障自愈规则
     */
    // 查询自愈方案列表
    static async queryOpsAutoRepairSchemeList (req) {
        return rbHttp.sendRequest({
            method: 'POST',
            url: '/v1/ops-service/opsAutoRepair/queryOpsAutoRepairSchemeList',
            data: req
        }).then(function (data) {
            return data
        })
    }
    // 根据id删除自愈方案
    static async removeOpsAutoRepairScheme (req) {
        return rbHttp.sendRequest({
            method: 'DELETE',
            url: `/v1/ops-service/opsAutoRepair/removeOpsAutoRepairScheme/${req.schemeId}`,
            data: req
        }).then(function (data) {
            return data
        })
    }
    // 查询自愈指标类型列表
    static async queryOpsAutoRepairItemTypeList (req) {
        return rbHttp.sendRequest({
            method: 'POST',
            url: '/v1/ops-service/opsAutoRepair/queryOpsAutoRepairItemTypeList',
            data: req
        }).then(function (data) {
            return data
        })
    }
    // 根据指标类型id查询自愈指标列表
    static async queryOpsAutoRepairItemListByItemType (req) {
        return rbHttp.sendRequest({
            method: 'POST',
            url: `/v1/ops-service/opsAutoRepair/queryOpsAutoRepairItemListByItemType/${req.itemTypeId}`,
            data: req
        }).then(function (data) {
            return data
        })
    }
    //
    // 保存自愈方案
    static async saveOpsAutoRepairScheme (req) {
        return rbHttp.sendRequest({
            method: 'POST',
            url: '/v1/ops-service/opsAutoRepair/saveOpsAutoRepairScheme',
            data: req
        }).then(function (data) {
            return data
        })
    }
    // 获取自愈作业完毕动作列表 
    static async loadApSchemePipeFinishActionList () {
        return rbHttp.sendRequest({
            method: 'GET',
            url: '/v1/ops-service/opsAutoRepair/loadApSchemePipeFinishActionList',
        }).then(function (data) {
            return data
        })
    }
    // 获取自愈作业完成判断类型列表 
    static async loadApPipeFinishJudgeTypeList () {
        return rbHttp.sendRequest({
            method: 'GET',
            url: '/v1/ops-service/opsAutoRepair/loadApPipeFinishJudgeTypeList',
        }).then(function (data) {
            return data
        })
    }

    // 获取自愈作业判断类型下拉列表
    static async loadApPipeFinishJudgeDropdownList (req) {
        return rbHttp.sendRequest({
            method: 'GET',
            url: `/v1/ops-service/opsAutoRepair/loadApPipeFinishJudgeDropdownList/${req.judgeType}`,
            data: req
        }).then(function (data) {
            return data
        })
    }
    // 查询自愈方案执行历史列表
    static async queryOpsAutoRepairExecHistory (req) {
        return rbHttp.sendRequest({
            method: 'POST',
            url: '/v1/ops-service/opsAutoRepair/queryOpsAutoRepairExecHistory',
            data: req
        }).then(function (data) {
            return data
        })
    }
    // 人工干预自愈执行流程
    static async manualHandleApSchemeExecute (req) {
        return rbHttp.sendRequest({
            method: 'PUT',
            url: `/v1/ops-service/opsAutoRepair/manualHandleApSchemeExecute/${req.schemeExecLogId}/${req.manualStatus}`,
            data: req
        }).then(function (data) {
            return data
        })
    }
    // 获取自愈状态列表
    static async loadApStatusList () {
        return rbHttp.sendRequest({
            method: 'GET',
            url: '/v1/ops-service/opsAutoRepair/loadApStatusList'
        }).then(function (data) {
            return data
        })
    }
    // 查询分组树 
    static async queryTree (req) {
        return rbHttp.sendRequest({
            method: 'POST',
            url: '/v1/ops-service/opsGroup/querGroupTree',
            data: req
        }).then(function (data) {
            return data
        })
    }
}