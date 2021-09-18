import rbHttp from 'assets/js/utility/rb-http.factory'

// 数据上报类接口
export default class cmdbService {
    // 表头
    static async getTableHeader(params) {
        return rbHttp.sendRequest({
            method: 'GET',
            params: params,
            url: '/v1/v3/cmdb/moduleGroup/header'
        }).then(function (data) {
            return data
        })
    }
    // 表数据
    static async getTableDatas(params) {
        return rbHttp.sendRequest({
            method: 'POST',
            data: params,
            url: '/v1/cmdb/instance/listV3'
        }).then(function (data) {
            return data
        })
    }
    // 数据保存接口
    static async saveDatas(params) {
        return rbHttp.sendRequest({
            method: 'POST',
            data: params,
            url: '/v1/cmdb/gvSafeFault/save'
        }).then(function (data) {
            return data
        })
    }
    // 字段值校验
    static async validDatas(params) {
        return rbHttp.sendRequest({
            method: 'POST',
            data: params,
            url: '/v1/cmdb/code/value/valid'
        }).then(function (data) {
            return data
        })
    }
    // 下载模板
    static async downloadTemplate(params = {}) {
        return rbHttp.sendRequest({
            method: 'POST',
            params: params,
            binary: true,
            url: `/v1/cmdb/module/download/import/template/${params.module_id}`
        }).then(function (data) {
            return data
        })
    }

    // 导出
    static async exportOut(params = {}) {
        return rbHttp.sendRequest({
            method: 'POST',
            data: {},
            url: '/v1/xxxxx'
        }).then(function (data) {
            return data
        })
    }
}
