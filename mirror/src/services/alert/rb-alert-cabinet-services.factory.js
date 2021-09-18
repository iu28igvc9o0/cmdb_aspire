/*
* Mirror 项目
*/
import rbHttp from 'assets/js/utility/rb-http.factory'

export default class rbAlertIntelligentServicesFactory {
    // 列头柜导出
    static async exportCabinetColumnAlert(params) {
        return rbHttp.sendRequest({
            method: 'POST',
            data: params,
            responseType: 'arraybuffer',
            url: '/v1/alerts/CabinetColumn/exportCabinetColumnAlert'
        }).then(function (data) {
            return data
        })
    }

    // 影响导出
    static async exportBizSystemList(params) {
        return rbHttp.sendRequest({
            method: 'POST',
            data: params,
            responseType: 'arraybuffer',
            url: '/v1/alerts/CabinetColumn/exportBizSystemList'
        }).then(function (data) {
            return data
        })
    }

    // 开启禁用
    static async updateStatus(params) {
        return rbHttp.sendRequest({
            method: 'POST',
            params: params,
            url: '/v1/alerts/CabinetColumn/updateStatus'
        }).then(function (data) {
            return data
        })
    }

    // 配置保存
    static async manageConfig(params) {
        return rbHttp.sendRequest({
            method: 'POST',
            data: params,
            url: '/v1/alerts/CabinetColumn/manageConfig'
        }).then(function (data) {
            return data
        })
    }

    // 配置查询
    static async getConfig(params) {
        return rbHttp.sendRequest({
            method: 'POST',
            params: params,
            url: '/v1/alerts/CabinetColumn/getConfig'
        }).then(function (data) {
            return data
        })
    }

    // 配置数据
    static async getScheduleConfig(params) {
        return rbHttp.sendRequest({
            method: 'GET',
            params: params,
            url: '/v1/alerts/CabinetColumn/getScheduleConfig'
        }).then(function (data) {
            return data
        })
    }

}
