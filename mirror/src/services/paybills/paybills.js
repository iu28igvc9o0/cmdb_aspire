import rbHttp from 'assets/js/utility/rb-http.factory'
export default class rbAlertPaybillsServiceFactory {
    static async getPaybillsData(params) {
        return rbHttp.sendRequest({
            method: 'GET',
            url: '/v1/cmdb/bill/monthRecord/list',
            params: params
        }).then(function (data) {
            return data
        })
    }

    static async updatePaybillsService(params) {
        return rbHttp.sendRequest({
            method: 'PUT',
            url: '/v1/cmdb/bill/monthRecord/update',
            params: params
        }).then(function (data) {
            return data
        })
    }

    static async getTenantData(params) {
        return rbHttp.sendRequest({
            method: 'GET',
            url: '/v1/cmdb/bill/record/payList',
            params: params
        }).then(function (data) {
            return data
        })
    }
    static async getTenantDetail(params) {
        return rbHttp.sendRequest({
            method: 'GET',
            url: '/v1/cmdb/bill/record/payList/detail',
            params: params
        }).then(function (data) {
            return data
        })
    }

    static async editPaybillsService(data) {
        return rbHttp.sendRequest({
            method: 'PUT',
            url: '/v1/cmdb/bill/record/payList',
            data: data
        }).then(function (data) {
            return data
        })
    }

}