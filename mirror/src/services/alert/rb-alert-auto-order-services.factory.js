/*
 * Mirror 项目
 */
import rbHttp from 'assets/js/utility/rb-http.factory'

export default class rbAlertAutoOrderServicesFactory {
    // 获取告警自动派单配置列表
    static async getAlertAutoOrderConfigList(params) {
        return rbHttp.sendRequest({
            method: 'GET',
            url: '/v1/alerts/autoOrderConfig/getAlertAutoOrderConfigList',
            params: params
        }).then(function (data) {
            return data
        })
    }
    // 创建告警自动派单配置
    static async createAlertAutoOrderConfig(data) {
        return rbHttp.sendRequest({
            method: 'POST',
            url: '/v1/alerts/autoOrderConfig/createAlertAutoOrderConfig',
            data: data
        }).then(function (data) {
            return data
        })
    }
    // 修改告警自动派单配置
    static async updateAlertAutoOrderConfig(data) {
        return rbHttp.sendRequest({
            method: 'PUT',
            url: '/v1/alerts/autoOrderConfig/updateAlertAutoOrderConfig',
            data: data
        }).then(function (data) {
            return data
        })
    }
    // 获取告警自动派单配置详情
    static async getAlertAutoOrderConfigDetail(uuid) {
        return rbHttp.sendRequest({
            method: 'GET',
            url: '/v1/alerts/autoOrderConfig/getAlertAutoOrderConfigDetail',
            params: {
                'uuid': uuid
            }
        }).then(function (data) {
            return data
        })
    }
    // 删除告警自动派单配置详情
    static async deleteAlertAutoOrderConfig(uuidList) {
        return rbHttp.sendRequest({
            method: 'DELETE',
            url: '/v1/alerts/autoOrderConfig/deleteAlertAutoOrderConfig',
            data: uuidList
        }).then(function (data) {
            return data
        })
    }
    // 更改告警自动派单配置状态
    static async updateAlertAutoOrderConfigStatus(uuidList, configStatus) {
        return rbHttp.sendRequest({
            method: 'PUT',
            url: '/v1/alerts/autoOrderConfig/updateAlertAutoOrderConfigStatus',
            params: {
                'configStatus': configStatus
            },
            data: uuidList
        }).then(function (data) {
            return data
        })
    }
    // 拷贝告警自动派单配置
    static async copyAlertAutoOrderConfig(uuid) {
        return rbHttp.sendRequest({
            method: 'POST',
            url: '/v1/alerts/autoOrderConfig/copyAlertAutoOrderConfig',
            params: {
                'uuid': uuid
            }
        }).then(function (data) {
            return data
        })
    }
    // 获取告警自动派单配置日志列表
    static async getAlertAutoOrderLogList(obj) {
        return rbHttp.sendRequest({
            method: 'POST',
            url: '/v1/alerts/autoOrderConfig/getAlertAutoOrderLogList',
            data: obj
        }).then(function (data) {
            return data
        })
    }
    // 导出告警自动派单配置日志
    static async exportAlertAutoOrderLogList(obj) {
        return rbHttp.sendRequest({
            method: 'POST',
            url: '/v1/alerts/autoOrderConfig/exportAlertAutoOrderLogList',
            data: obj,
            binary: true
        }).then(function (data) {
            return data
        })
    }
    // 校验配置名称
    static async checkName(configName) {
        return rbHttp.sendRequest({
            method: 'GET',
            url: '/v1/alerts/autoOrderConfig/checkName',
            params: {
                'configName': configName
            }
        }).then(function (data) {
            return data
        })
    }
    // 实时资源利用率topN
    /**
     * kpi 必填 String   指标 CPU_PUSED-cpu利用率；MEMORY_PUSED-内存利用率；DISK_PUSED_ROOT-磁盘根目录利用率
     * idcType 非必填 String   资源池  
     * size 非必填 String   条数 不填默认10，最大100
     */
    static async getDevicePusedTopN(req) {
        return rbHttp.sendRequest({
            method: 'GET',
            url: `/v1/alerts/indexPage/devicePusedTopN/${req.kpi}`,
            params: req
        }).then(function (data) {
            return data
        })
    }

}
