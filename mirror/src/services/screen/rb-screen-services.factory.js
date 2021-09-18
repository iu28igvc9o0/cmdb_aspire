/*
* 租户大屏利用率导入方法
*/
import rbHttp from 'assets/js/utility/rb-http.factory'
const dataDictEndPoint = '/v1/cmdb/configDict'
const screenOverview = '/v1/cmdb/index/overview'

export default class rbScreenServicesFactory {
    //  维保统计中的 按照类型获取字典数据
    static async getDictDataByType (param) {
        return rbHttp.sendRequest({
            method: 'GET',
            params: param,
            url: dataDictEndPoint + '/getDictsByType'
        }).then(function (data) {
            return data
        })
    }

    // 依据选择的验证类型进行验证
    static async validateDataByType (data) {
        return rbHttp.sendRequest({
            method: 'POST',
            data: data,
            url: screenOverview + '/validate'
        }).then(function (data) {
            return data
        })
    }

    // 查询验证记录
    static async listValidateRecord (data) {
        return rbHttp.sendRequest({
            method: 'POST',
            data: data,
            url: screenOverview + '/validate/list'
        }).then(function (data) {
            return data
        })
    }

    // 生成Excel文件
    static async createFile (data) {
        return rbHttp.sendRequest({
            method: 'POST',
            data: data,
            url: screenOverview + '/excel/create'
        }).then(function (data) {
            return data
        })
    }

    // 生成Excel文件
    static async exportFile (data) {
        return rbHttp.sendRequest({
            method: 'POST',
            data: data,
            url: screenOverview + '/excel/export'
        }).then(function (data) {
            return data
        })
    }

    // 获取租户数据
    static async getTenantData (param) {
        return rbHttp.sendRequest({
            method: 'GET',
            params: param,
            url: '/v1/cmdb/orgManager/loadTree'
        }).then(function (data) {
            return data
        })
    }
}
