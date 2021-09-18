/*
 * Mirror 项目
 */
import rbHttp from 'assets/js/utility/rb-http.factory'

export default class rbAlertModelFieldServicesFactory {
    // 根据模型ID获取告警模型字段列表
    static async getAlertFieldListByModelId(obj) {
        return rbHttp.sendRequest({
            method: 'POST',
            url: '/v2/alerts/model/field/getAlertFieldListByModelId',
            data: obj
        }).then(function (data) {
            return data
        })
    }
    // 新增告警模型字段数据
    static async insertAlertField(data) {
        return rbHttp.sendRequest({
            method: 'POST',
            url: '/v2/alerts/model/field/insertAlertField',
            data: data
        }).then(function (data) {
            return data
        })
    }
    // 根据ID获取告警模型字段详情
    static async getAlertFieldDetailById(id) {
        return rbHttp.sendRequest({
            method: 'GET',
            url: '/v2/alerts/model/field/getAlertFieldDetailById',
            params: {
                'id': id
            }
        }).then(function (data) {
            return data
        })
    }
    // 更新告警模型字段数据
    static async updateAlertField(data) {
        return rbHttp.sendRequest({
            method: 'POST',
            url: '/v2/alerts/model/field/updateAlertField',
            data: data
        }).then(function (data) {
            return data
        })
    }

    // 删除告警模型字段数据
    static async deleteAlertFieldDetailById(id, modelId) {
        return rbHttp.sendRequest({
            method: 'DELETE',
            url: '/v2/alerts/model/field/deleteAlertFieldDetailById',
            params: {
                'id': id,
                'modelId': modelId
            }
        }).then(function (data) {
            return data
        })
    }

    // 修改锁定状态
    static async updateLockStatus(id, modelId,isLock) {
        return rbHttp.sendRequest({
            method: 'POST',
            url: '/v2/alerts/model/field/updateLockStatus',
            params: {
                'id': id,
                'modelId': modelId,
                'isLock': isLock
            }
        }).then(function (data) {
            return data
        })
    }
    // 同步告警字段
    static async synchronizeField(modelId) {
        return rbHttp.sendRequest({
            method: 'POST',
            url: '/v2/alerts/model/field/synchronizeField',
            params: {
                'modelId': modelId
            }
        }).then(function (data) {
            return data
        })
    }
}
