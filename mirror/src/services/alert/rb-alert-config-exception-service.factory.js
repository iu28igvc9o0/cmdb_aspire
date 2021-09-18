/*
* Mirror 项目
*/
import rbHttp from 'assets/js/utility/rb-http.factory'

export default class rbAlertConfigExceptionServiceFactory {
    static async getConfigExceptionList(req) {
        return rbHttp.sendRequest({
            method: 'GET',
            url: '/v1/alerts/config-exception/list',
            params: req
        }).then(function (data) {
            return data
        })
    }
    static async getDetail(id) {
        return rbHttp.sendRequest({
            method: 'GET',
            url: `/v1/alerts/config-exception/detail/${id}`,
        }).then(function (data) {
            return data
        })
    }
    static async insert(req) {
        return rbHttp.sendRequest({
            method: 'POST',
            url: '/v1/alerts/config-exception/insert',
            data: req
        }).then(function (data) {
            return data
        })
    }
    static async update(req) {
        return rbHttp.sendRequest({
            method: 'PUT',
            url: '/v1/alerts/config-exception/update',
            data: req
        }).then(function (data) {
            return data
        })
    }
    static async delete(req) {
        return rbHttp.sendRequest({
            method: 'DELETE',
            url: '/v1/alerts/config-exception/delete',
            data: req
        }).then(function (data) {
            return data
        })
    }

    static async start(req) {
        return rbHttp.sendRequest({
            method: 'PUT',
            url: '/v1/alerts/config-exception/status/1',
            data: req
        }).then(function (data) {
            return data
        })
    }

    static async stop(req) {
        return rbHttp.sendRequest({
            method: 'PUT',
            url: '/v1/alerts/config-exception/status/0',
            data: req
        }).then(function (data) {
            return data
        })
    }
}
