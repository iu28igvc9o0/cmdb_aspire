/*
* 自动化运维-系统巡检，基线巡检，交维巡检
*/
import rbHttp from 'assets/js/utility/rb-http.factory'

export default class rbAutoTargetServicesFactory {
    // 保存模板
    static async monitorItemPrototypeSave(req) {
        return rbHttp.sendRequest({
            method: 'POST',
            url: '/v1/items/monitorItemPrototype/save',
            data: req
        }).then(function (data) {
            return data
        })
    }
    // 导出
    static async exportMonitorItemPrototypeList(req) {
        return rbHttp.sendRequest({
            method: 'POST',
            responseType: 'arraybuffer',
            url: '/v1/items/monitorItemPrototype/exportMonitorItemPrototypeList',
            data: req
        }).then(function (data) {
            return data
        })
    }
    // 删除
    static async monitorItemPrototypeRemove(id) {
        return rbHttp.sendRequest({
            method: 'DELETE',
            url: `/v1/items/monitorItemPrototype/remove/${id}`,
        }).then(function (data) {
            return data
        })
    }
    // 批量删除
    static async monitorItemPrototypebatchRemove(id) {
        return rbHttp.sendRequest({
            method: 'DELETE',
            // params: id,
            url: `/v1/items/monitorItemPrototype/batchRemove/${id}`,
            // url: '/v1/items/monitorItemPrototype/batchRemove',
        }).then(function (data) {
            return data
        })
    }
    // 同步
    static async manualHandleApSchemeExecute(id) {
        return rbHttp.sendRequest({
            method: 'PUT',
            url: `/v1/items/monitorItemPrototype/syncRefreshItemsConfigByItemPrototype/${id}`,
        }).then(function (data) {
            return data
        })
    }

}