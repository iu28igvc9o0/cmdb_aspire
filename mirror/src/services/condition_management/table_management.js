import rbHttp from 'assets/js/utility/rb-http.factory'

export default class tableManagement {
    // 查询相关信息
    static async getCasParentCodes(data) {
        return rbHttp.sendRequest({
            method: 'POST',
            data: data,
            addNamespace: false,
            url: '/v1/cmdb/code/getCasParentCodes'
        }).then(function (data) {
            return data
        })
    }
    // 码表查询列表 /v1/cmdb/code/list
    static async getCodeList(params) {
        return rbHttp.sendRequest({
            method: 'POST',
            data: params,
            url: '/v1/cmdb/code/list'
        }).then(function (data) {
            return data
        })
    }
    // 新增码表查询列表 /v1/cmdb/code/saveCode
    static async getSaveCodeList(params) {
        return rbHttp.sendRequest({
            method: 'POST',
            data: params,
            url: '/v1/cmdb/code/saveCode'
        }).then(function (data) {
            return data
        })
    }
    // 修改码表查询列表 /v1/cmdb/code/updateCode
    static async getUpdateCodeList(params) {
        return rbHttp.sendRequest({
            method: 'PUT',
            params: params,
            url: '/v1/cmdb/code/updateCode'
        }).then(function (data) {
            return data
        })
    }
    // 修改码表查询列表 /v1/cmdb/code/deleteCode
    static async getDeleteCodeList(params) {
        return rbHttp.sendRequest({
            method: 'DELETE',
            params: params,
            url: '/v1/cmdb/code/deleteCode'
        }).then(function (data) {
            return data
        })
    }
    // 查询模型分组列表 下拉框/v1/v3/cmdb/module/catalog/getRootLevel
    static async getcontrolList() {
        return rbHttp.sendRequest({
            method: 'GET',
            url: '/v1/v3/cmdb/module/catalog/getRootLevel'
        }).then(function (data) {
            return data
        })
    }
    // 模型分组引用模型 下拉框/v1/cmdb/module/getTree?catalogId=1
    static async getTreeList(parms) {
        return rbHttp.sendRequest({
            method: 'GET',
            url: '/v1/cmdb/module/getTree?catalogId=' + parms
        }).then(function (data) {
            return data
        })
    }
    // 模型分组模型字段 下拉框/v1/cmdb/module/getModuleDetail?moduleId=xxx
    static async getModuleDetailList(parms) {
        return rbHttp.sendRequest({
            method: 'GET',
            url: '/v1/cmdb/code/list/' + parms
        }).then(function (data) {
            return data
        })
    }

    // 批量修正CI数量 /v1/cmdb/instance/batchUpdateCount
    static async getBatchUpdateCount(xsxsx, xsId) {
        return rbHttp.sendRequest({
            method: 'POST',
            data: xsxsx,
            url: '/v1/cmdb/instance/batchUpdateCount?moduleId=' + xsId
        }).then(function (data) {
            return data
        })
    }
    // 批量修正CI数量 /v1/cmdb/process/batchUpdateInstance
    static async getBatchUpdateInstance(vf, vfId) {
        return rbHttp.sendRequest({
            method: 'POST',
            data: vf,
            url: '/v1/cmdb/process/batchUpdateInstance?moduleId=' + vfId
        }).then(function (data) {
            return data
        })
    }
}
