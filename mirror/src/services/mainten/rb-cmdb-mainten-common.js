import rbHttp from 'assets/js/utility/rb-http.factory'

const MODULE_BASE = '/v1/cmdb/module'

// CMDB 维保相关功能的通用工具类
export default class rbCmdbMaintenCommon {

    // 根据厂家类型获取厂家信息 服务供应商||合同供应商
    static async getProducesByType (data) {
        return rbHttp.sendRequest({
            method: 'POST',
            data: data,
            url: MODULE_BASE + '/module/data?moduleType=default_produce_module_id'
        }).then(function (data) {
            return data
        })
    }

    // 获取业务系统列表
    static async getBizSystemList (data) {
        return rbHttp.sendRequest({
            method: 'POST',
            data: data,
            url: MODULE_BASE + '/module/data?moduleType=default_business_system_module_id'
        }).then(function (data) {
            return data
        })
    }
}
