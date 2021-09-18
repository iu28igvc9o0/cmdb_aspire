/*
* @author huangzhijie
* @date   19.3.2
* @description 系统管理-资源请求接口
*/
import rbHttp from 'assets/js/utility/rb-http.factory'

export default class ResourceServiceFactory {
    static ROOT_ID = '000000'
    // 3.6.2 设备类型列表查询
    static getDevicetypeList() {
        let params = {}
        // if (ids) {
        //   params.ids = ids
        // }
        return rbHttp.sendRequest({
            method: 'GET',
            params: params,
            url: '/v1/cmdb/instance/getDeviceClassTree'
        })
    }
    static async getInstance(req) {
        return rbHttp.sendRequest({
            method: 'POST',
            data: req,
            url: '/v1/cmdb/instance/listInstanceBaseInfo'
        })
    }
    static getBizSysList() {
        let params = {}
        return rbHttp.sendRequest({
            method: 'GET',
            params: params,
            url: '/v1/cmdb/orgManager/loadTreeDepBiz'
        })
    }
    // 获取树
    static getDictTree(colName, ids) {
        let params = {}
        if (ids) {
            params.ids = ids
        }
        return rbHttp.sendRequest({
            method: 'GET',
            params: params,
            url: '/v1/cmdb/configDict/getTree/' + colName
        })
    }
    // 设备通用权限（区域）
    static getAreaTree() {
        let params = {}
        // if (ids) {
        //   params.ids = ids
        // }
        return rbHttp.sendRequest({
            method: 'GET',
            params: params,
            url: '/v1/cmdb/instance/getIdcTree'
        })
    }
    static async getAuthDevice(obj) {
        return rbHttp.sendRequest({
            method: 'POST',
            params: obj,
            url: '/v1/permissions/getAuthDeviceData'
        }).then(function (data) {
            return data
        })
    }
    // 3.6.4 查询资源角色列表
    static getResourceList() {
        return rbHttp.sendRequest({
            method: 'GET',
            url: '/v1/roles/rolesRerouceAllList'
        })
    }
    // 3.6.5 新增资源角色
    static addResource(params) {
        // TODO /v1/roles/resource/{namespace}
        return rbHttp.sendRequest({
            method: 'POST',
            data: params,
            url: '/v1/roles/resource/alauda'
        })
    }
    // 3.6.6 修改资源角色
    static updateResource(params) {
        // TODO url '/v1/roles/{namespace}'
        return rbHttp.sendRequest({
            method: 'PUT',
            data: params,
            url: `/v1/roles/alauda/${params.role_id}`
        })
    }
    // 3.6.7 删除资源角色
    static deleteResource(id) {
        // TODO url '/v1/roles/delete/{namespace}/{id}'
        return rbHttp.sendRequest({
            method: 'DELETE',
            url: `/v1/roles/delete/alauda/${id}`
        }).then(function (data) {
            return data
        })
    }
    // 3.6.8 查询资源角色信息
    static getResourceDetail(id) {
        return rbHttp.sendRequest({
            method: 'GET',
            url: `/v1/roles/detail/${id}`
        })
    }

    // 3.6.8 查询资源角色信息
    static getDict(ids) {
        return rbHttp.sendRequest({
            method: 'GET',
            params: { 'ids': ids },
            url: '/v1/cmdb/configDict/getByIds'
        })
    }

    // 3.13.3 查询分组树
    static querGroupTree() {
        return rbHttp.sendRequest({
            method: 'POST',
            url: '/v1/ops-service/opsGroup/querGroupTree'
        })
    }

    // 3.13.1 查询作业、脚本、yum资源树
    static querOpsResourceTree(req) {
        return rbHttp.sendRequest({
            method: 'POST',
            params: req,
            url: '/v1/ops-service/opsGroup/querOpsResourceTree'
        })
    }
}
