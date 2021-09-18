import rbHttp from 'assets/js/utility/rb-http.factory'
export default class rbAlertSubscribeServiceFactory {
    static async getSubscribeRulesData(params) {
        return rbHttp.sendRequest({
            method: 'POST',
            url: '/v1/alerts/SubscribeRules/queryRules',
            params: params
        }).then(function (data) {
            return data
        })
    }

    static async updataSubscribeService(req) {
        return rbHttp.sendRequest({
            method: 'POST',
            url: '/v1/alerts/SubscribeRules/updateRules',
            params: req
        }).then(function (data) {
            return data
        })
    }

    static async deteleSubscribeService(req) {
        return rbHttp.sendRequest({
            method: 'GET',
            url: '/v1/alerts/SubscribeRules/deteleRules',
            params: req
        }).then(function (data) {
            return data
        })
    }

    static async createSubscribeService(params) {
        return rbHttp.sendRequest({
            method: 'POST',
            url: '/v1/alerts/SubscribeRules/CreateSubscribeRules',
            params: params
        }).then(function (data) {
            return data
        })
    }
    static async getSubscribeRulesById(params) {
        return rbHttp.sendRequest({
            method: 'GET',
            url: '/v1/alerts/SubscribeRules/GetSubscribeRulesById',
            params: params
        }).then(function (data) {
            return data
        })
    }

    static async updateSubscribeService(params) {
        return rbHttp.sendRequest({
            method: 'POST',
            url: '/v1/alerts/SubscribeRules/UpdateSubscribeRules',
            params: params
        }).then(function (data) {
            return data
        })
    }

    static async emailNotify(params) {
        return rbHttp.sendRequest({
            method: 'POST',
            url: '/v1/alerts/SubscribeRules/emailNotify',
            data: params
        }).then(function (data) {
            return data
        })
    }


    static async getSubscribeData(params) {
        return rbHttp.sendRequest({
            method: 'POST',
            url: '/v1/alerts/SubscribeRules/query',
            params: params
        }).then(function (data) {
            return data
        })
    }

    static async querySubscribeRules() {
        return rbHttp.sendRequest({
            method: 'GET',
            url: '/v1/alerts/SubscribeRules/queryAlertSubscribeRules'
        }).then(function (data) {
            return data
        })
    }

    static async exportSubscribeRules(params) {
        return rbHttp.sendRequest({
            method: 'POST',
            binary: true,
            url: '/v1/alerts/SubscribeRules/export',
            params: params
        }).then(function (data) {
            return data
        })
    }

}