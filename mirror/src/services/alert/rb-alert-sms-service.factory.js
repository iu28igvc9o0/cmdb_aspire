import rbHttp from 'assets/js/utility/rb-http.factory'
export default class rbAlertSmsServiceFactory {

    // 发送短信
    static async sendSms(params) {
        return rbHttp.sendRequest({
            method: 'POST',
            url: '/v1/alerts/sms/sendSms',
            data: params
        }).then(function (data) {
            return data
        })
    }
    // 查询短信记录
    static async querySmsRecords(params) {
        return rbHttp.sendRequest({
            method: 'POST',
            url: '/v1/alerts/sms/findSmsList',
            // addNamespace: false,
            data: params
        }).then(function (data) {
            return data
        })
    }
    // 导出短信记录
    static async exportSmsRecords(params) {
        return rbHttp.sendRequest({
            method: 'POST',
            url: '/v1/alerts/sms/exportSmsRecord',
            // addNamespace: false,
            data: params
        }).then(function (data) {
            return data
        })
    }
    // 删除短信记录
    static async deleteSmsRecords(params) {
        return rbHttp.sendRequest({
            method: 'POST',
            url: '/v1/alerts/sms/deleteSmsRecord',
            addNamespace: false,
            data: params
        }).then(function (data) {
            return data
        })
    }

    // 查询模板分类
    static async querySmsTags(params) {
        return rbHttp.sendRequest({
            method: 'GET',
            url: '/v1/alerts/sms/findSmsTemplateList',
            params: params
        }).then(function (data) {
            return data
        })
    }
    // 根据模板分类查询模板内容
    static async querySmsTemplatesByTag(params) {
        return rbHttp.sendRequest({
            method: 'GET',
            url: '/v1/alerts/sms/findDetailListByCondition',
            params: params
        }).then(function (data) {
            return data
        })
    }
    // 编辑模板内容
    static async editSmsTemplates(params) {
        return rbHttp.sendRequest({
            method: 'POST',
            url: '/v1/alerts/sms/editTemplateContent',
            data: params
        }).then(function (data) {
            return data
        })
    }
    // 删除模板内容
    static async deleteSmsTemplates(params) {
        return rbHttp.sendRequest({
            method: 'POST',
            url: '/v1/alerts/sms/deleteTemplateContent',
            data: params
        }).then(function (data) {
            return data
        })
    }

    // 编辑模板分类
    static async editSmsTemplatesTags(params) {
        return rbHttp.sendRequest({
            method: 'POST',
            url: '/v1/alerts/sms/addSmsTemplate',
            data: params
        }).then(function (data) {
            return data
        })
    }
    // 删除模板分类
    static async deleteSmsTemplatesTags(params) {
        return rbHttp.sendRequest({
            method: 'POST',
            url: '/v1/alerts/sms/xxxxxxxxxx',
            params: params
        }).then(function (data) {
            return data
        })
    }

    // 保存短信内容到模板分类
    static async editSmsContentToTags(params) {
        return rbHttp.sendRequest({
            method: 'POST',
            url: '/v1/alerts/sms/addContentToTemplate',
            data: params
        }).then(function (data) {
            return data
        })
    }

}