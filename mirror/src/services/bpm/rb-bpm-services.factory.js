/*
* bpm 项目
*/
import rbHttp from 'assets/js/utility/rb-http.factory'

export default class rbBpmServiceFactory {
    // 公告管理-导出
    static async exportNotice(req) {
        return rbHttp.sendRequest({
            method: 'POST',
            url: '/v1/desk/publicNotice/export',
            binary: true,
            data: req
        }).then(function (data) {
            return data
        })
    }
    // 公告管理-启用
    static async runNotice(data, params) {
        return rbHttp.sendRequest({
            method: 'POST',
            url: `/v1/desk/operateStatus/${params.status}`,
            addNamespace: false,
            data: data
        }).then(function (data) {
            return data
        })
    }
    // 公告管理-停用
    static async stopNotice(data, params) {
        return rbHttp.sendRequest({
            method: 'POST',
            url: `/v1/desk/operateStatus/${params.status}`,
            addNamespace: false,
            data: data
        }).then(function (data) {
            return data
        })
    }
    // 公告管理-通知推送
    static async pushNotice(req) {
        return rbHttp.sendRequest({
            method: 'GET',
            url: `/v1/desk/publicNotice/sendNotice/${req.id}`,
            // data: req
        }).then(function (data) {
            return data
        })
    }
    // 通过defKey获得流程id
    static async getBpmDefId(params) {
        return rbHttp.sendRequest({
            method: 'GET',
            params: params,
            url: '/v1/desk/getBpmDefId'
        }).then(function (data) {
            return data
        })
    }
    static async getAccountByParam() {
        return rbHttp.sendRequest({
            method: 'GET',
            url: '/v1/order/getAccountByParam'
        }).then(function (data) {
            return data
        })
    }
    static async instDistribution(type, isWhole) {
        return rbHttp.sendRequest({
            method: 'GET',
            url: '/v1/order/instDistribution',
            params: {
                'type': type,
                'isWhole': isWhole
            }
        }).then(function (data) {
            return data
        })
    }
    static async instTrend(startDate, endDate, isWhole) {
        return rbHttp.sendRequest({
            method: 'GET',
            url: '/v1/order/instTrend',
            params: {
                'startDate': startDate,
                'endDate': endDate,
                'isWhole': isWhole
            }
        }).then(function (data) {
            return data
        })
    }
    static async listJson(param) {
        return rbHttp.sendRequest({
            method: 'POST',
            url: '/v1/order/listJson',
            params: param
        }).then(function (data) {
            return data
        })
    }
    static async instStatistics(defKey, startDate, endDate, isWhole) {
        return rbHttp.sendRequest({
            method: 'GET',
            url: '/v1/order/instStatistics',
            params: {
                'defKey': defKey,
                'startDate': startDate,
                'endDate': endDate,
                'isWhole': isWhole
            }
        }).then(function (data) {
            return data
        })
    }

}
