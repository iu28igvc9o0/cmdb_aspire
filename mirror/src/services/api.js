/** 接口文档 **/
import http from 'assets/js/utility/rb-http.factory'
// import { formatTreeSelectData } from 'src/utils/globalUtils.js'
let api = {}

// 查询资源池
api.queryPool = (params = {}) => {
    return http.sendRequest({
        method: 'GET',
        url: '/v1/cmdb/configDict/getDictsByType',
        params: params
    }).then((data) => {
        return data
    })
}
// 查询业务系统
api.queryBizSys = () => {
    return http.sendRequest({
        method: 'GET',
        url: '/v1/cmdb/orgManager/loadTreeDepBiz?namespace=alauda',
        // params: params
    }).then((data) => {
        return data
        // let biz_tree = []
        // let root = {
        //     id: '',
        //     label: '所有',
        //     children: []
        // }
        // data.forEach(item => {
        //     root.children.push(formatTreeSelectData(item, { id: item.uuid, label: item.name }))
        // })
        // biz_tree.push(root)

        // return biz_tree
    })
}

// 创建首页模块
api.addModuleInfo = (params = {}) => {
    return http.sendRequest({
        method: 'POST',
        url: '/v1/moduleInfo/insert',
        params: params
    }).then((data) => {
        return data
    })
}

// 查询首页模块
api.queryModuleInfo = (params = {}) => {
    return http.sendRequest({
        method: 'GET',
        url: '/v1/moduleInfo/getModuleInfoByCode',
        params: params
    }).then((data) => {
        return data
    })
}
// 告警：查询条件
api.queryAlertByOperateStatus = (params = {}) => {
    return http.sendRequest({
        method: 'GET',
        url: '/v1/alerts/indexPage/selectAlertsByOperateStatus',
        params: params
    }).then((data) => {
        return data
    })
}
// 查询待解决告警和已解决告警
api.queryAlertStatus = (params = {}) => {
    return http.sendRequest({
        method: 'GET',
        url: '/v1/alerts/indexPage/summary',
        params: params
    }).then((data) => {
        return data
    })
}
// 查询告警级别分布
api.queryAlertLevel = (params = {}) => {
    return http.sendRequest({
        method: 'GET',
        url: '/v1/alerts/indexPage/level-span',
        params: params
    }).then((data) => {
        return data
    })
}
// 查询设备分类
api.queryDeviceClass = (params = {}) => {
    return http.sendRequest({
        method: 'GET',
        url: '/v1/alerts/indexPage/classify-span',
        params: params
    }).then((data) => {
        return data
    })
}

// 查询告警趋势
api.queryAlertTrend = (params = {}) => {
    return http.sendRequest({
        method: 'GET',
        url: '/v1/alerts/indexPage/trend',
        params: params
    }).then((data) => {
        return data
    })
}

// 告警设备相关top10
api.queryAlertDevice = (params = {}) => {
    return http.sendRequest({
        method: 'GET',
        url: '/v1/alerts/indexPage/alertDeviceTop10',
        params: params
    }).then((data) => {
        return data
    })
}

// 告警内容相关top10
api.queryAlertContent = (params = {}) => {
    return http.sendRequest({
        method: 'GET',
        url: '/v1/alerts/indexPage/alertContentTop10',
        params: params
    }).then((data) => {
        return data
    })
}
// 告警：处理时长
api.queryAlertSolveTime = (params = {}) => {
    return http.sendRequest({
        method: 'GET',
        url: '/v1/alerts/indexPage/alertIdcDoHours',
        params: params
    }).then((data) => {
        return data
    })
}

// 资源池-资源利用率分布
api.queryPoolRate = (params = {}) => {
    return http.sendRequest({
        method: 'POST',
        url: '/v1/alerts/indexPage/idcTypeDeviceUsedRate',
        params: params
    }).then((data) => {
        return data
    })
}

// 租户资源利用率分布
api.queryUserRate = (params = {}) => {
    return http.sendRequest({
        method: 'POST',
        url: '/v1/alerts/indexPage/bizSystemDeviceUsedRate',
        params: params
    }).then((data) => {
        return data
    })
}

// 资源利用率趋势图
api.queryPoolTrend = (params = {}) => {
    return http.sendRequest({
        method: 'POST',
        url: '/v1/alerts/indexPage/deviceUsedRateTrends',
        params: params
    }).then((data) => {
        return data
    })
}

// 租户-top5最低的排序
api.queryUserTop5 = (params = {}) => {
    return http.sendRequest({
        method: 'POST',
        url: '/v1/alerts/indexPage/bizSystemDeviceUsedRateLow',
        params: params
    }).then((data) => {
        return data
    })
}

// 我的告警
api.queryMyAlert = (params = {}) => {
    return http.sendRequest({
        method: 'GET',
        url: '/v1/alerts/statistic/getAlertSummary',
        params: params
    }).then((data) => {
        return data
    })
}
// 当日告警总览
api.queryTodayAlertOverview = (params = {}) => {
    return http.sendRequest({
        method: 'GET',
        url: '/v1/alerts/statistic/summary',
        params: params
    }).then((data) => {
        return data
    })
}
// 告警总览
api.queryAlertOverview = (params = {}) => {
    return http.sendRequest({
        method: 'GET',
        // url: `/v1/alerts/statistic/summary`,
        url: '/v1/alerts/indexPage/summary',
        params: params
    }).then((data) => {
        return data
    })
}
// 告警设备分类
api.queryAlertClass = (params = {}) => {
    return http.sendRequest({
        method: 'GET',
        url: '/v1/alerts/indexPage/classify-span',
        params: params
    }).then((data) => {
        return data
    })
}
// 最新热点告警
api.queryAlertHot = (params = {}) => {
    return http.sendRequest({
        method: 'GET',
        url: '/v1/alerts/statistic/latest',
        params: params
    }).then((data) => {
        return data
    })
}
// 漏洞等级分布
api.querySafeBugLevel = (params = {}) => {
    return http.sendRequest({
        method: 'GET',
        url: '/v1/alerts/leak-scan/leaksRankDistribute',
        params: params
    }).then((data) => {
        return data
    })
}
// 业务系统漏洞数Top5
api.querySafeBizBug = (params = {}) => {
    return http.sendRequest({
        method: 'GET',
        url: '/v1/alerts/leak-scan/getLeakByBizSystem',
        params: params
    }).then((data) => {
        return data
    })
}

// 设备漏洞数TOP5
api.querySafeDeviceBug = (params = {}) => {
    return http.sendRequest({
        method: 'GET',
        url: '/v1/alerts/leak-scan/getLeakByIp',
        params: params
    }).then((data) => {
        return data
    })
}
// 资源池漏洞数
api.querySafePoolBug = (params = {}) => {
    return http.sendRequest({
        method: 'GET',
        url: '/v1/alerts/leak-scan/getLeakByIdcType',
        params: params
    }).then((data) => {
        return data
    })
}

// 租户资源利用率Top10
api.queryUserResourceRate = (params = {}) => {
    return http.sendRequest({
        method: 'GET',
        url: '/v1/alerts/indexPage/department1DeviceUsedRateTopN',
        params: params
    }).then((data) => {
        return data
    })
}

// 业务系统利用率topN
api.queryBizUseRate = (params = {}) => {
    return http.sendRequest({
        method: 'GET',
        url: '/v1/alerts/indexPage/bizSystemDeviceUsedRateTopN',
        params: params
    }).then((data) => {
        return data
    })
}

// 工单分类处理时长分析
api.queryOrderSolveTime = (params = {}) => {
    return http.sendRequest({
        method: 'GET',
        url: '/v1/order/instRuntimeAnalysis',
        params: params
    }).then((data) => {
        return data
    })
}
// 工单分析
api.queryOrderAnalysis = (params = {}) => {
    return http.sendRequest({
        method: 'POST',
        url: '/v1/order/homepageProInstAnalysis',
        params: params
    }).then((data) => {
        return data
    })
}
// 工单统计
api.queryOrderStatistics = (params = {}) => {
    return http.sendRequest({
        method: 'GET',
        url: '/v1/order/instStatisticsNew',
        params: params
    }).then((data) => {
        return data
    })
}
// 工单类型分布
api.queryOrderType = (params = {}) => {
    return http.sendRequest({
        method: 'GET',
        url: '/v1/order/instDistributionNew',
        params: params
    }).then((data) => {
        return data
    })
}
// 工单总览
api.queryOrderOverview = (params = {}) => {
    return http.sendRequest({
        method: 'GET',
        url: '/v1/order/instOverview',
        params: params
    }).then((data) => {
        return data
    })
}

// 资源总览
api.queryResourceOverview = (params = {}) => {
    return http.sendRequest({
        method: 'GET',
        url: '/v1/cmdb/index/countOverview',
        params: params
    }).then((data) => {
        return data
    })
}
// 各资源池设备分布
api.queryPoolDevice = (params = {}) => {
    return http.sendRequest({
        method: 'GET',
        url: '/v1/cmdb/index/countByIdcDevCT',
        params: params
    }).then((data) => {
        return data
    })
}
// 各资源池租户分布
api.queryPoolUser = (params = {}) => {
    return http.sendRequest({
        method: 'GET',
        url: '/v1/cmdb/index/countBizByIdc',
        params: params
    }).then((data) => {
        return data
    })
}
// 各资源池租户业务系统
api.queryPoolBiz = (params = {}) => {
    return http.sendRequest({
        method: 'GET',
        url: '/v1/cmdb/index/countBizByIdcDep1',
        params: params
    }).then((data) => {
        return data
    })
}

// 告警热点
api.queryAlertHotNew = (params = {}) => {
    return http.sendRequest({
        method: 'GET',
        url: '/v1/alerts/indexPage/latest',
        params: params
    }).then((data) => {
        return data
    })
}

// 资源利用率:物理设备
api.queryResourceDeviceRate = (params = {}) => {
    return http.sendRequest({
        method: 'GET',
        url: '/v1/alerts/zabbix/deviceUsedRateByMonitor',
        params: params
    }).then((data) => {
        return data
    })
}

// 资源利用率:物理机虚拟机总数
api.queryResourceDeviceTotal = (params = {}) => {
    return http.sendRequest({
        method: 'GET',
        url: '/v1/cmdb/instance/queryServiceCount',
        params: params
    }).then((data) => {
        return data
    })
}
// 资源利用率:磁盘
api.queryResourceDeviceDisk = (params = {}) => {
    return http.sendRequest({
        method: 'GET',
        url: '/v1/alerts/zabbix/storageUsedRate',
        params: params
    }).then((data) => {
        return data
    })
}

// 安全扫描总览
api.querySafeScanOverview = (params = {}) => {
    return http.sendRequest({
        method: 'GET',
        url: '/v1/alerts/leak-scan/leakSummary',
        params: params
    }).then((data) => {
        return data
    })
}

// 安全漏洞趋势
api.querySafeScanTrend = (params = {}) => {
    return http.sendRequest({
        method: 'GET',
        url: '/v1/alerts/leak-scan/leakTrend',
        params: params
    }).then((data) => {
        return data
    })
}

// 业务系统漏洞数统计
api.querySafeBizStatistics = (params = {}) => {
    return http.sendRequest({
        method: 'GET',
        url: '/v1/alerts/leak-scan/leakStatListByBiz',
        params: params
    }).then((data) => {
        return data
    })
}

// 漏洞扫描清单
api.querySafeScanList = (params = {}) => {
    return http.sendRequest({
        method: 'POST',
        url: '/v1/alerts/leak-scan/detailList',
        data: params
    }).then((data) => {
        return data
    })
}

// 安全漏洞通告
api.querySafeNotice = (params = {}) => {
    return http.sendRequest({
        method: 'GET',
        url: '/v1/order/getInstListByDefKey',
        params: params
    }).then((data) => {
        return data
    })
}

// 安全漏洞整改任务
api.querySafeTask = (params = {}) => {
    return http.sendRequest({
        method: 'GET',
        url: '/v1/order/getInstListByDefKeyAndStatus',
        params: params
    }).then((data) => {
        return data
    })
}

// 监控：服务设备信息
api.queryServerInfo = (params = {}) => {
    return http.sendRequest({
        method: 'GET',
        url: '/v1/alerts/restful/queryServerInfo',
        params: params
    }).then((data) => {
        return data
    })
}
// 监控：网络设备信息
api.queryNetInfo = (params = {}) => {
    return http.sendRequest({
        method: 'GET',
        url: '/v1/alerts/restful/queryNetInfo',
        params: params
    }).then((data) => {
        return data
    })
}
// 监控：设备趋势

api.queryServerData = (params = {}) => {
    return http.sendRequest({
        method: 'GET',
        url: '/v1/alerts/restful/queryServerData',
        params: params
    }).then((data) => {
        return data
    })
}
// 监控：根据Ip查询instanceID
api.queryIdByIp = (params = {}) => {
    return http.sendRequest({
        method: 'POST',
        url: '/v1/cmdb/instance/query/detail',
        data: params
    }).then((data) => {
        return data
    })
}

// 集中网管-资源总览
api.countList = (params = {}) => {
    return http.sendRequest({
        method: 'POST',
        url: '/v1/cmdb/index/countList',
        data: params
    }).then((data) => {
        return data
    })
}
export {
    api
}
