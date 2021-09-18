/*
 * Mirror 项目
 */
import rbHttp from 'assets/js/utility/rb-http.factory'

export default class rbAlertModelServicesFactory {
    // 获取告警模型数据列表
    static async getAlertModelList() {
        return rbHttp.sendRequest({
            method: 'POST',
            url: '/v2/alerts/model/getAlertModelList'
        }).then(function (data) {
            return data
        })
    }
    // 新增告警模型数据
    static async insertAlertModel(data) {
        return rbHttp.sendRequest({
            method: 'POST',
            url: '/v2/alerts/model/insertAlertModel',
            data: data
        }).then(function (data) {
            return data
        })
    }
    // 获取告警模型数据树
    static async getAlertModelTreeData() {
        return rbHttp.sendRequest({
            method: 'POST',
            url: '/v2/alerts/model/getAlertModelTreeData'
        }).then(function (data) {
            return data
        })
    }
    // 删除告警模型数据
    static async deleteAlertModel(data) {
        return rbHttp.sendRequest({
            method: 'DELETE',
            url: '/v2/alerts/model/deleteAlertModel',
            data: data
        }).then(function (data) {
            return data
        })
    }
    // 获取告警模型数据列表
    static async getAlertModelDetail(id) {
        return rbHttp.sendRequest({
            method: 'GET',
            url: '/v2/alerts/model/getAlertModelDetail',
            params: {
                'id': id
            }
        }).then(function (data) {
            return data
        })
    }
    // 编辑告警模型数据
    static async updateAlertModel(data) {
        return rbHttp.sendRequest({
            method: 'POST',
            url: '/v2/alerts/model/updateAlertModel',
            data: data
        }).then(function (data) {
            return data
        })
    }

}
