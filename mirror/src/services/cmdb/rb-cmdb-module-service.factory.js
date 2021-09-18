import rbHttp from 'assets/js/utility/rb-http.factory'
// const ModuleEndPoint = '/cmdb/module'
const ModuleEndPoint = '/v1/cmdb/module'
// const IconEndPoint = '/cmdb/icon'
const IconEndPoint = '/v1/cmdb/icon'
const DictEndPoint = '/v1/cmdb/configDict'
const ValidEndPoint = '/v1/cmdb/code/validate'
const versionV3 = '/v1/v3/cmdb'

export default class cmdbModueService {
    static async getModuleIdByDeviceType(params) {
        return rbHttp.sendRequest({
            method: 'GET',
            params: params,
            url: '/v1/cmdb/module/getModuleIdByDeviceType'
        }).then(function (data) {
            return data
        })
    }
    /* icon */
    static async getIcons(params, body) {
        return rbHttp.sendRequest({
            method: 'POST',
            params: params,
            data: body,
            url: IconEndPoint + '/getIcons'
        }).then(function (data) {
            return data
        })
    }
    /* dict */
    static async getDictByColName(col_name) {
        return rbHttp.sendRequest({
            method: 'GET',
            url: DictEndPoint + `/getTree/${col_name}`
        }).then(function (data) {
            return data
        })
    }
    /* icon */
    static async listValid() {
        return rbHttp.sendRequest({
            method: 'GET',
            url: ValidEndPoint + '/list'
        }).then(function (data) {
            return data
        })
    }
    /* 模型类 */
    // 查询模型事件
    static async getModuleEvent(params = {}) {
        return rbHttp.sendRequest({
            method: 'GET',
            url: '/v1/cmdb/module/event/getModuleEventList',
            params: params
        }).then(function (data) {
            return data
        })
    }
    // 查询事件类
    static async getEventClass(params = {}) {
        return rbHttp.sendRequest({
            method: 'GET',
            url: '/v1/cmdb/module/handleClass/getModuleHandlerClassList',
            params: params
        }).then(function (data) {
            return data
        })
    }
    // 保存模型事件
    static async saveModuleEvent(params = {}, data = {}) {
        return rbHttp.sendRequest({
            method: 'POST',
            params: params,
            url: '/v1/cmdb/module/event/save',
            data: data
        }).then(function (data) {
            return data
        })
    }
    // 模型分组根树
    static async getRootTree(params = {}) {
        return rbHttp.sendRequest({
            method: 'GET',
            url: versionV3 + '/module/catalog/getRootLevel',
            params: params
        }).then(function (data) {
            return data
        })
    }
    // 模型分组所有树
    static async getAllTree(params = {}) {
        return rbHttp.sendRequest({
            method: 'GET',
            url: versionV3 + '/module/catalog/getAllLevelTree',
            params: params
        }).then(function (data) {
            return data
        })
    }
    // 校验模型分组编码/名称是否存在
    static async validTreeRepeat(params = {}) {
        return rbHttp.sendRequest({
            method: 'GET',
            url: versionV3 + '/module/catalog/valid',
            params: params
        }).then(function (data) {
            return data
        })
    }
    // 添加模型分组树
    static async addTree(params = {}) {
        return rbHttp.sendRequest({
            method: 'POST',
            url: versionV3 + '/module/catalog/save',
            data: params
        }).then(function (data) {
            return data
        })
    }
    // 修改模型分组树
    static async updateTree(params = {}) {
        return rbHttp.sendRequest({
            method: 'PUT',
            url: versionV3 + '/module/catalog/update',
            data: params
        }).then(function (data) {
            return data
        })
    }
    // 删除模型分组树
    static async deleteTree(params = {}) {
        return rbHttp.sendRequest({
            method: 'DELETE',
            url: versionV3 + '/module/catalog/delete',
            data: params
        }).then(function (data) {
            return data
        })
    }
    // 模型分组树排序
    static async sortTree(params = {}) {
        return rbHttp.sendRequest({
            method: 'PUT',
            url: versionV3 + '/module/catalog/update/sort',
            params: params
        }).then(function (data) {
            return data
        })
    }
    // 模型列表
    static async getModuleTree(params) {
        return rbHttp.sendRequest({
            method: 'GET',
            url: ModuleEndPoint + '/getTree',
            params: params
        }).then(function (data) {
            return data
        })
    }

    // 模型列表
    static async getModuleTreeByCatalogIdOrModuleId(params) {
        return rbHttp.sendRequest({
            method: 'GET',
            url: ModuleEndPoint + '/getModuleTreeByCatalogIdOrModuleId',
            params: params
        }).then(function (data) {
            return data
        })
    }

    // 模型列表
    static async getModuleByCatalogId(params = {}) {
        return rbHttp.sendRequest({
            method: 'GET',
            url: ModuleEndPoint + '/getModuleByCatalogId',
            params: params
        }).then(function (data) {
            return data
        })
    }
    // 模型排序
    static async sortModuleTree(params = {}) {
        return rbHttp.sendRequest({
            method: 'PUT',
            url: ModuleEndPoint + '/updateModuleSort',
            params: params
        }).then(function (data) {
            return data
        })
    }
    // 模型权限
    static async authModule(params = {}) {
        return rbHttp.sendRequest({
            method: 'GET',
            url: versionV3 + '/authorization/list',
            params: params
        }).then(function (data) {
            return data
        })
    }
    // 新增模型
    static async addModule(params = {}, data = {}) {
        return rbHttp.sendRequest({
            method: 'POST',
            data: data,
            // params: params,
            timeout: 600000,
            url: ModuleEndPoint + `/addModule?topCatalogId=${params.topCatalogId}`
        }).then(function (data) {
            return data
        })
    }

    // 修改模型
    static async editModule(params = {}, data = {}) {
        return rbHttp.sendRequest({
            method: 'POST',
            data: data,
            // params: params,
            timeout: 600000,
            url: ModuleEndPoint + `/updateModule?topCatalogId=${params.topCatalogId}`
        }).then(function (data) {
            return data
        })
    }

    // 模型校验
    static async validModule(params = {}) {
        return rbHttp.sendRequest({
            method: 'GET',
            params: params,
            url: ModuleEndPoint + '/getModuleExist'
        }).then(function (data) {
            return data
        })
    }


    static async getModuleSelective(data) {
        return rbHttp.sendRequest({
            method: 'POST',
            params: data,
            url: ModuleEndPoint + '/getModuleSelective'
        }).then(function (data) {
            return data
        })
    }
    // 模型详情
    static async getModuleDetail(params = {}) {
        return rbHttp.sendRequest({
            method: 'GET',
            url: '/v1/cmdb/module/getModuleDetail',
            params: params,
        }).then(function (data) {
            return data
        })
    }
    // 根据模型ID,获取父模型信息
    static async getParentInfo(params = {}) {
        return rbHttp.sendRequest({
            method: 'GET',
            url: '/v1/cmdb/module/getParentInfo',
            params: params,
        }).then(function (data) {
            return data
        })
    }
    // 引用模型详情（新增状态下）
    static async getModuleSelfDetail(params = {}) {
        return rbHttp.sendRequest({
            method: 'GET',
            url: '/v1/cmdb/code/getSelfCodeListByModuleId',
            params: params,
        }).then(function (data) {
            return data
        })
    }

    // 码表字段校验规则
    static async getCodeValids(params = {}) {
        return rbHttp.sendRequest({
            method: 'POST',
            url: '/v1/cmdb/code/value/valid',
            data: params,
        }).then(function (data) {
            return data
        })
    }





    static async addModuleCode(data) {
        return rbHttp.sendRequest({
            method: 'POST',
            data: data,
            timeout: 600000,
            url: ModuleEndPoint + '/addModuleCode'
        }).then(function (data) {
            return data
        })
    }

    static async updateModule(data) {
        return rbHttp.sendRequest({
            method: 'POST',
            data: data,
            timeout: 600000,
            url: ModuleEndPoint + '/updateModule'
        }).then(function (data) {
            return data
        })
    }
    static async getModuleTags(moduleId) {
        return rbHttp.sendRequest({
            method: 'GET',
            url: ModuleEndPoint + `/getModuleTag/${moduleId}`
        }).then(function (data) {
            return data
        })
    }
    static async deleteModule(moduleId) {
        return rbHttp.sendRequest({
            method: 'DELETE',
            timeout: 600000,
            url: ModuleEndPoint + `/deleteModule/${moduleId}`
        }).then(function (data) {
            return data
        })
    }
    static async queryTableData(data) {
        return rbHttp.sendRequest({
            method: 'POST',
            timeout: 600000,
            data: data,
            url: ModuleEndPoint + '/queryTableData/'
        }).then(function (data) {
            return data
        })
    }

    // 新增模型关系
    static async addModuleRelation(data) {
        return rbHttp.sendRequest({
            method: 'POST',
            data: data,
            url: versionV3 + '/module/relation/save'
        }).then(function (data) {
            return data
        })
    }

    // 删除模型关系
    static async deleteModuleRelation(param) {
        return rbHttp.sendRequest({
            method: 'DELETE',
            params: param,
            url: versionV3 + '/module/relation/deleteById'
        }).then(function (data) {
            return data
        })
    }

    // 修改模型关系
    static async updateModuleRelation(data) {
        return rbHttp.sendRequest({
            method: 'POST',
            data: data,
            url: versionV3 + '/module/relation/update'
        }).then(function (data) {
            return data
        })
    }


    // 查询模型关系
    static async getModuleRelationList(param) {
        return rbHttp.sendRequest({
            method: 'GET',
            params: param,
            url: versionV3 + '/module/relation/listRDetailByModuleId'
        }).then(function (data) {
            return data
        })
    }
    // 查询引用模型字典数据
    static async getRefModuleDict(data) {
        return rbHttp.sendRequest({
            method: 'POST',
            params: data.params,
            data: data.payload,
            url: ModuleEndPoint + '/getRefModuleDict'
        }).then(function (data) {
            return data
        })
    }

    // 获取引用模型数据
    static async getRefModuleData(req) {
        return rbHttp.sendRequest({
            method: 'POST',
            url: '/v1/cmdb/module/module/data',
            params: req.params,
            data: req.payload
        }).then(function (data) {
            return data
        })
    }

    // 获取引用模型数据
    static async getModuleColumns(params) {
        return rbHttp.sendRequest({
            method: 'GET',
            url: '/v1/cmdb/module/getModuleColumns',
            params: params
        }).then(function (data) {
            return data
        })
    }

}
