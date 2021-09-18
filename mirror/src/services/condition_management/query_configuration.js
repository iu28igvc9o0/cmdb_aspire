
import rbHttp from 'assets/js/utility/rb-http.factory'

export default class queryConfiguration {
    // 条件查询配置列表 /v1/v3/cmdb/condication/list
    static async getCondicationList(params) {
        return rbHttp.sendRequest({
            method: 'POST',
            data: params,
            url: '/v1/v3/cmdb/condication/list'
        }).then(function (data) {
            return data
        })
    }
    // 新增查询配置列表 /v1/v3/cmdb/condication/save
    static async getCondicationSave(params) {
        return rbHttp.sendRequest({
            method: 'POST',
            data: params,
            url: '/v1/v3/cmdb/condication/save'
        }).then(function (data) {
            return data
        })
    }
    // 删除查询配置列表 /v1/v3/cmdb/condication/delete
    static async getCondicationDELETE(params) {
        return rbHttp.sendRequest({
            method: 'DELETE',
            params: params,
            url: '/v1/v3/cmdb/condication/delete'
        }).then(function (data) {
            return data
        })
    }
    // 修改查询配置列表 /v1/v3/cmdb/condication/update
    static async getCondicationUpdate(params) {
        return rbHttp.sendRequest({
            method: 'PUT',
            data: params,
            url: '/v1/v3/cmdb/condication/update'
        }).then(function (data) {
            return data
        })
    }
    // 码表名称 /v1/cmdb/code/getDistinctCodeList
    static async getDistinctCodeList() {
        return rbHttp.sendRequest({
            method: 'GET',
            url: '/v1/cmdb/code/getDistinctCodeList'
        }).then(function (data) {
            return data
        })
    }

    // 业务视图所需要的 查询条件 /v1/v3/cmdb/condication/get
    static async getCondicationGet(params) {
        return rbHttp.sendRequest({
            method: 'POST',
            data: params,
            url: '/v1/v3/cmdb/condication/get'
        }).then(function (data) {
            return data
        })
    }
    // 业务根据sql查询字典值 查询条件 /cmdb/module/queryTableData
    static async queryTableData(data) {
        return rbHttp.sendRequest({
            method: 'POST',
            data: data,
            url: '/v1/cmdb/module/queryTableData'
        }).then(function (data) {
            return data
        })
    }
    // 业务根据sql查询字典值 查询条件 /v1/cmdb/instance/list
    static async getInstanceList() {
        return rbHttp.sendRequest({
            method: 'POST',
            url: '/v1/cmdb/instance/list'
        }).then(function (data) {
            return data
        })
    }
    // 验证编码 或者 编码和地址 的唯一性 查询条件 /v1/v3/cmdb/condication/validate/unique
    static async validDataUnique(params) {
        return rbHttp.sendRequest({
            method: 'GET',
            params: params,
            url: '/v1/v3/cmdb/condication/validate/unique'
        }).then(function (data) {
            return data
        })
    }
}
