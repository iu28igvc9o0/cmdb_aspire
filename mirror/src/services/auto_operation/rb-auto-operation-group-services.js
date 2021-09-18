/*
* @author huangzhijie
* @date   19.3.14
* @description 系统管理-人员、部门请求接口
*/
import rbHttp from 'assets/js/utility/rb-http.factory'

export default class PersonServiceFactory {

    // 3.11.1.5 批量新增分组对象关联
    static saveBatchGroupRelation(params) {
        return rbHttp.sendRequest({
            method: 'POST',
            params: params,
            addNamespace: false,
            url: '/v1/ops-service/opsGroup/saveBatchGroupRelation'
        })
    }
    // 3.11.1.6 保存分组
    static saveGroup(params) {
        return rbHttp.sendRequest({
            method: 'POST',
            params: params,
            addNamespace: false,
            url: '/v1/ops-service/opsGroup/saveGroup'
        })
    }
    // 3.11.1.1 删除分组信息
    static deleteGroup(id) {
        return rbHttp.sendRequest({
            method: 'DELETE',
            params: {
                group_id: id
            },
            url: '/v1/ops-service/opsGroup/deleteGroup'
        })
    }
    // 3.11.1.2 删除分组关联
    static deleteGroupRelation(params) {
        return rbHttp.sendRequest({
            method: 'DELETE',
            params: params,
            addNamespace: false,
            url: '/v1/ops-service/opsGroup/deleteGroupRelation'
        })
    }
    // 3.11.1 查询分组数
    static getQueryGroupTree() {
        const params = {}
        return rbHttp.sendRequest({
            method: 'POST',
            params: params,
            addNamespace: false,
            url: '/v1/ops-service/opsGroup/querGroupTree'
        })
    }
    // 3.11.1.3查询分组对象关联
    static querGroupRelationList(group_id, pageSize = 10, pageNo = 1, search, search1, valueObject, group_parent_id) {
        const params = {
            page_size: pageSize,
            page_no: pageNo
        }
        if (group_id) {
            params.group_id = group_id
        }
        if (search) {
            params.object_name_like = search
        }
        if (search1) {
            params.group_name_like = search1 // 分组名称
        }
        if (valueObject) {
            params.object_type = valueObject // 对象类型
        }
        if (group_parent_id) {
            params.group_parent_id = group_parent_id
        }
        return rbHttp.sendRequest({
            method: 'POST',
            params: params,
            addNamespace: false,
            url: '/v1/ops-service/opsGroup/querGroupRelationList'
        })
    }
    // 3.11.1.3查询分组对象关联  新增
    static queryObjectList(group_id, pageSize = 10, pageNo = 1, search, valueObject) {
        const params = {
            page_size: pageSize,
            page_no: pageNo
        }
        if (group_id) {
            params.group_id = group_id
        }
        if (search) {
            params.object_name_like = search
        }
        if (valueObject) {
            params.object_type = valueObject // 对象类型
        }
        return rbHttp.sendRequest({
            method: 'POST',
            params: params,
            addNamespace: false,
            url: '/v1/ops-service/opsGroup/queryObjectList'
        })
    }
    // 3.11.8 人员批量导出
    static exportGroupRelation(groupId, searchText, searchTextGroup, valueObject) {
        let param = {}
        if (groupId) {
            param.group_id = groupId
        }
        if (searchText) {
            param.object_name_like = searchText
        }
        if (searchTextGroup) {
            param.group_name_like = searchTextGroup // 分组名称
        }
        if (valueObject) {
            param.object_type = valueObject // 对象类型
        }
        return rbHttp.sendRequest({
            method: 'POST',
            params: param,
            binary: true,
            url: '/v1/ops-service/opsGroup/exportGroupRelation'
        })
    }

}
