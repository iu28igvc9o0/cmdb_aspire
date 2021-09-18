/**
 * 故障管理
 */
import rbHttp from 'assets/js/utility/rb-http.factory'

export default class rbFaultServicesFactory {
    // 故障详情
    static async editFaultData(data) {
        return rbHttp.sendRequest({
            method: 'GET',
            url: '/v1/alerts/fault_manage/detail/' + data
        }).then(function (data) {
            return data
        })
    }
    // 故障编辑
    static async editFault(params) {
        return rbHttp.sendRequest({
            method: 'PUT',
            params: params,
            url: '/v1/alerts/fault_manage/update'
        }).then(function (data) {
            return data
        })
    }
    // 故障数据导出
    static async exportFaultData(params) {
        return rbHttp.sendRequest({
            method: 'GET',
            params: params,
            url: '/v1/alerts/fault_manage/export'
        }).then(function (data) {
            return data
        })
    }
    // 故障报告导出
    static async exportFaultReport(params) {
        return rbHttp.sendRequest({
            method: 'POST',
            data: params,
            url: '/v1/alerts/fault_manage/exportAnnex'
        }).then(function (data) {
            return data
        })
    }
    // 故障附件导入
    static async importFaultReport(data) {
        return rbHttp.sendRequest({
            method: 'GET',
            url: '/v1/alerts/fault_manage/import/' + data
        }).then(function (data) {
            return data
        })
    }
    // 动态列头部
    static async getOrInsertColumnFilter() {
        return rbHttp.sendRequest({
            method: 'GET',
            url: '/v1/cmdb/search/getOrInsertColumnFilter/fault/fault'
        }).then(function (data) {
            return data
        })
    }
}
