/*
 * 告警看板--历史告警new
 */
import rbHttp from 'assets/js/utility/rb-http.factory'

export default class rbAlertHistoryAlertServicesFactory {
    // 1.1.13查询模型字段接口
    static async getModelFields() {
        return rbHttp.sendRequest({
            method: 'GET',
            url: '/v2/alerts/model/field/listByTableName/alert_alerts'
        }).then(function(data) {
            return data
        })
    }

    // 获得具体模型字段的数据源
    static async getFieldSource(url) {
        let sourceUrl = url
        return rbHttp.sendRequest({
            method: 'GET',
            url: sourceUrl
        }).then(function(data) {
            return data
        })
    }





    // 获取过滤条件下的告警列表
    // static async getHomeAlertList(obj, alertType) {
    //     return rbHttp.sendRequest({
    //         method: 'POST',
    //         data: obj,
    //         params: {
    //             'alertType': alertType
    //         },
    //         url: `/v1/alerts/home/query`
    //     }).then(function(data) {
    //         return data
    //     })
    // }
}