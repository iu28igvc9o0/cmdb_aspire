/*
 * 监控
 */
import rbHttp from 'assets/js/utility/rb-http.factory'

export default class monitorService {
    // 设备列表
    static async deviceList(params, moduleType = '') {
        return rbHttp.sendRequest({
            method: 'POST',
            data: params,
            url: `/v1/kpi/restful/instanceKpi/list?moduleType=${moduleType}`
        }).then(function (data) {
            return data
        })
    }
    // 连通性列表
    static async instancePing(params, moduleType = '') {
        return rbHttp.sendRequest({
            method: 'POST',
            data: params,
            // url: `/v1/kpi/restful/instancePing/list?moduleType=${moduleType}`
            url: `/v1/cmdb/instance/listV3?moduleType=${moduleType}`
        }).then(function (data) {
            return data
        })
    }
    // 告警设备列表
    static async deviceAlertList(params, moduleType = '') {
        return rbHttp.sendRequest({
            method: 'POST',
            data: params,
            url: `/v1/kpi/restful/alertKpi/list?moduleType=${moduleType}`
        }).then(function (data) {
            return data
        })
    }
    // 指标列表
    static async zabbixList(params) {
        return rbHttp.sendRequest({
            method: 'GET',
            params: params,
            url: '/v1/alerts/zabbix/itemByResourceId'
        }).then(function (data) {
            return data
        })
    }
    // 设备性能分布by资源池
    static async deviceTrendByPool(params) {
        return rbHttp.sendRequest({
            method: 'POST',
            data: params,
            url: '/v1/alerts/restful/getIdcTypePerformanceData'
        }).then(function (data) {
            return data
        })
    }
    // 告警所属设备数
    static async deviceByAlert(params, moduleType = '') {
        return rbHttp.sendRequest({
            method: 'POST',
            data: params,
            url: `/v2/alerts/alert/summaryDeviceAlertsByLevel?moduleType=${moduleType}`
        }).then(function (data) {
            return data
        })
    }

    static async autoSendInfo(query_data) {
        return rbHttp.sendRequest({
            method: 'POST',
            data: query_data,
            url: '/v1/alerts/auto/autoSendInfo'
        }).then(function (data) {
            return data
        })
    }

    static async insertAutoAlarmConfig(query_data) {
        return rbHttp.sendRequest({
            method: 'POST',
            data: query_data,
            // useFormData: true,
            url: '/v1/alerts/auto/insertAutoAlarmConfig'
        }).then(function (data) {
            return data
        })
    }

    // 资源性能展示
    static async getInstanceMonitorValueList(params) {
        return rbHttp.sendRequest({
            method: 'POST',
            params: params,
            url: '/v1/alerts/restful/getInstanceMonitorValueList'
        }).then(function (data) {
            return data
        })
    }
    // 资源性能导出
    static async exportInstanceMonitorValueList(params) {
        return rbHttp.sendRequest({
            method: 'POST',
            binary: true,
            params: params,
            url: '/v1/alerts/restful/exportInstanceMonitorValueList'
        }).then(function (data) {
            return data
        })
    }

}
