/*
* 科管项目
*/
import rbHttp from 'assets/js/utility/rb-http.factory'

export default class rbKeguanServices {
    /**
     * 资源首页
     */
    // 资源总览
    static async getCountOverview() {
        return rbHttp.sendRequest({
            method: 'GET',
            url: '/v1/cmdb/index/countOverview',
        }).then(function (data) {
            return data
        })
    }
    // 统计各品牌设备分布（总）
    static async getDeviceCountByProduceAll(req) {
        return rbHttp.sendRequest({
            method: 'GET',
            url: '/v1/cmdb/kg/index/deviceCountByProduceAll',
            params: req
        }).then(function (data) {
            return data
        })
    }
    // 统计品牌下设备型号分布
    static async getModelCountByProduce(req) {
        return rbHttp.sendRequest({
            method: 'GET',
            url: '/v1/cmdb/kg/index/modelCountByProduce',
            params: req
        }).then(function (data) {
            return data
        })
    }
    // 获取网段地址列表
    static async getAllSegmentAddress() {
        return rbHttp.sendRequest({
            method: 'GET',
            url: '/v1/cmdb/kg/index/getAllSegmentAddress',
        }).then(function (data) {
            return data
        })
    }
    // 获取业务系统列表
    static async getSystemList(req) {
        return rbHttp.sendRequest({
            method: 'POST',
            url: '/v1/cmdb/module/module/data?moduleType=default_business_system_module_id',
            data: req,
        }).then(function (data) {
            return data
        })
    }
    // 资源使用状况分布
    static async getDeviceUseCount(req) {
        return rbHttp.sendRequest({
            method: 'GET',
            url: '/v1/cmdb/kg/index/getDeviceUseCount',
            params: req,
        }).then(function (data) {
            return data
        })
    }
    // (物理机/虚拟机)资源使用状况分布
    static async getDeviceUseCountByType(req) {
        return rbHttp.sendRequest({
            method: 'GET',
            url: '/v1/cmdb/kg/index/getDeviceUseCountByType',
            params: req
        }).then(function (data) {
            return data
        })
    }
    // 网段用途设备总量分布
    static async getDeviceCountBySegmentUse(req) {
        return rbHttp.sendRequest({
            method: 'GET',
            params: req,
            url: '/v1/cmdb/kg/index/deviceCountBySegmentUse'
        }).then(function (data) {
            return data
        })
    }
    // 服务器业务使用量占比
    static async getDeviceCountBySystem(req) {
        return rbHttp.sendRequest({
            method: 'GET',
            url: '/v1/cmdb/kg/index/deviceCountBySystem',
            params: req
        }).then(function (data) {
            return data
        })
    }

    /**
     * 监控首页
     */
    // 监控告警总览
    static async getAlertView(req) {
        return rbHttp.sendRequest({
            method: 'POST',
            url: '/v1/alerts/kg/monitor/index/getAlertView',
            params: req
        }).then(function (data) {
            return data
        })
    }
    // 资源利用率：查询服务器(内存/CPU)
    static async queryServiceCount() {
        return rbHttp.sendRequest({
            method: 'GET',
            url: '/v1/alerts/kg/monitor/index/queryServiceCount',
        }).then(function (data) {
            return data
        })
    }
    // 资源利用率：(内存/CPU)占比
    static async queryDeviceUsedRate(req) {
        return rbHttp.sendRequest({
            method: 'POST',
            url: '/v1/alerts/kg/monitor/index/deviceUsedRate',
            data: req
        }).then(function (data) {
            return data
        })
    }
    // 资源利用率：磁盘利用率
    static async getStorageUsedRate(req) {
        return rbHttp.sendRequest({
            method: 'POST',
            url: '/v1/alerts/kg/monitor/index/storageUsedRateForKG',
            params: req
        }).then(function (data) {
            return data
        })
    }
    // 获取网段用途列表
    static async getSegmentUseList() {
        return rbHttp.sendRequest({
            method: 'GET',
            url: '/v1/alerts/kg/monitor/index/getSegmentUseList',
        }).then(function (data) {
            return data
        })
    }
    // 资源利用率趋势
    static async getDeviceUsedRateTrends(req) {
        return rbHttp.sendRequest({
            method: 'POST',
            url: '/v1/alerts/kg/monitor/index/deviceUsedRateTrends',
            data: req
        }).then(function (data) {
            return data
        })
    }
    // 设备厂商列表
    static async getListProduceByPage(req = { params: { produce_type: '生产供应商' } }) {
        return rbHttp.sendRequest({
            method: 'POST',
            url: '/v1/cmdb/module/module/data?moduleType=default_produce_module_id',
            data: req,
        }).then(function (data) {
            return data
        })
    }
    // 存储资源使用总览
    static async getStorageUseView(req) {
        return rbHttp.sendRequest({
            method: 'POST',
            url: '/v1/alerts/kg/monitor/index/storageUseView',
            data: req,
        }).then(function (data) {
            return data
        })
    }
    // 业务资源利用率
    static async getBizSystemDeviceUsedRate(req) {
        return rbHttp.sendRequest({
            method: 'POST',
            url: '/v1/alerts/kg/monitor/index/bizSystemDeviceUsedRate',
            data: req
        }).then(function (data) {
            return data
        })
    }
}
