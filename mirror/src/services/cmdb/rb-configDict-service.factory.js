import rbHttp from 'assets/js/utility/rb-http.factory'

const ConfigDictManage = '/v1/cmdb/configDict'

export default class rbConfigDictServiceFactory {
    // 获取配置项事件
    static async getHaveEventCodeList(data) {
        return rbHttp.sendRequest({
            method: 'GET',
            params: data,
            url: '/v1/cmdb/module/event/getHaveEventCodeList'
        }).then(function (data) {
            return data
        })
    }
    // 获取配置项事件回调
    static async getCodeEventData(params, data) {
        return rbHttp.sendRequest({
            method: 'POST',
            params: params,
            data: data,
            url: '/v1/cmdb/module/event/getCodeEventData'
        }).then(function (data) {
            return data
        })
    }
    // 分页查询
    static async listDictByPage(data) {
        return rbHttp.sendRequest({
            method: 'GET',
            params: data,
            url: ConfigDictManage + '/list'
        }).then(function (data) {
            return data
        })
    }

    // 插入
    static async insertDict(data) {
        return rbHttp.sendRequest({
            method: 'POST',
            data: data,
            url: ConfigDictManage + '/add'
        }).then(function (data) {
            return data
        })
    }

    // 根据id删除
    static async deleteDictById(data) {
        return rbHttp.sendRequest({
            method: 'DELETE',
            params: data,
            url: ConfigDictManage + '/delete'
        }).then(function (data) {
            return data
        })
    }

    // 父标签
    static async getParentDict(data) {
        return rbHttp.sendRequest({
            method: 'GET',
            params: data,
            url: ConfigDictManage + '/getAll'
        }).then(function (data) {
            return data
        })
    }

    // 根据id查询
    static async getDictById(data) {
        return rbHttp.sendRequest({
            method: 'GET',
            params: data,
            url: ConfigDictManage + '/getById'
        }).then(function (data) {
            return data
        })
    }

    // 更新
    static async updateDict(data) {
        return rbHttp.sendRequest({
            method: 'POST',
            params: data,
            url: ConfigDictManage + '/update'
        }).then(function (data) {
            return data
        })
    }

    // 导出
    static async exportDict(data) {
        return rbHttp.sendRequest({
            method: 'POST',
            params: data,
            binary: true,
            url: ConfigDictManage + '/export'
        }).then(function (data) {
            return data
        })
    }

    static async getDicts() {
        return rbHttp.sendRequest({
            method: 'GET',
            url: ConfigDictManage + '/getDicts'
        }).then(function (data) {
            return data
        })
    }

    // 根据type查询
    static async getDictsByType(data) {
        return rbHttp.sendRequest({
            method: 'GET',
            params: data,
            url: ConfigDictManage + '/getDictsByType'
        }).then(function (data) {
            return data
        })
    }

    // 根据type查询
    static async getDistinctDictType() {
        return rbHttp.sendRequest({
            method: 'GET',
            url: ConfigDictManage + '/getDistinctDictType'
        }).then(function (data) {
            return data
        })
    }

    // 获取所有业务系统
    static async getBizSystem(data) {
        return rbHttp.sendRequest({
            method: 'POST',
            url: '/v1/cmdb/module/module/data?moduleType=default_business_system_module_id',
            data: { 'params': data ? data : {} }
        }).then(function (data) {
            return data
        })
    }

    // 验证码表编码和模型分组是否唯一
    static async validateIfUnique(params) {
        return rbHttp.sendRequest({
            method: 'GET',
            url: '/v1/cmdb/code/validate/unique',
            params: params,
        }).then(function (data) {
            return data
        })
    }

    static async getProduce(data) {
        return rbHttp.sendRequest({
            method: 'POST',
            data: data,
            url: '/v1/cmdb/module/module/data?moduleType=default_produce_module_id'
        }).then(function (data) {
            return data
        })
    }
}
