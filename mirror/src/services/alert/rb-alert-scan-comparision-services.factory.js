/*
* Mirror 项目
*/
import rbHttp from 'assets/js/utility/rb-http.factory'

export default class rbAlertKanBanServiceFactory {
    // 列表
    static async getScanComparisionList (obj) {
        return rbHttp.sendRequest({
            method: 'POST',
            data: obj,
            url: '/v1/alerts/scanComparision/getScanComparisionList'
        }).then(function (data) {
            return data
        })
    }
    // 删除
    static async deleteScanComparisionById (obj) {
        return rbHttp.sendRequest({
            method: 'DELETE',
            url: '/v1/alerts/scanComparision/deleteScanComparisionById',
            data: obj,
        }).then(function (data) {
            return data
        })
    }
    // 同步
    static async synScanComparision (obj) {
        return rbHttp.sendRequest({
            method: 'POST',
            data: obj,
            url: '/v1/alerts/scanComparision/synScanComparision'
        }).then(function (data) {
            return data
        })
    }
    // 导出
    static async exportScanComparision (data) {
        return rbHttp.sendRequest({
            method: 'POST',
            data: data,
            binary: true,
            url: '/v1/alerts/scanComparision/exportScanComparision'
        }).then(function (data) {
            return data
        })
    }
}
