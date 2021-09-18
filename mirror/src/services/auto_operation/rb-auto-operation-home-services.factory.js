/**
 * 自动化运维-首页 项目
 */
import rbHttp from 'assets/js/utility/rb-http.factory'

export default class rbAutoOperationHomeServicesFactory {
    /**
     * 首页
     */
    // 首页主机统计
    static async queryIndexHostStatistic() {
        return rbHttp.sendRequest({
            method: 'GET',
            url: '/v1/ops-service/opsReport/indexHostStatistic',
        }).then(function (data) {
            return data
        })
    }
    // 首页常用作业统计
    static async queryIndexPipelineStatistic() {
        return rbHttp.sendRequest({
            method: 'GET',
            url: '/v1/ops-service/opsReport/indexPipelineStatistic',
        }).then(function (data) {
            return data
        })
    }
    // 近30天任务数统计
    static async queryIndexRecent30DaysRunStatistic() {
        return rbHttp.sendRequest({
            method: 'GET',
            url: '/v1/ops-service/opsReport/indexRecent30DaysRunStatistic',
        }).then(function (data) {
            return data
        })
    }
    // 定时任务数统计
    static async queryIndexPipelineJobStatistic() {
        return rbHttp.sendRequest({
            method: 'GET',
            url: '/v1/ops-service/opsReport/indexPipelineJobStatistic',
        }).then(function (data) {
            return data
        })
    }
    // 近30天任务执行趋势统计(近30天任务执行趋势统计)
    static async queryIndexRecent30DaysRunTrend() {
        return rbHttp.sendRequest({
            method: 'GET',
            url: '/v1/ops-service/opsReport/indexRecent30DaysRunTrend'
        }).then(function (data) {
            return data
        })
    }
    // 任务耗时区间统计
    static async queryIndexRunTimeSpanStatistic() {
        return rbHttp.sendRequest({
            method: 'GET',
            url: '/v1/ops-service/opsReport/indexRunTimeSpanStatistic',
        }).then(function (data) {
            return data
        })
    }
    /**
     * ops执行历史查询
     */
    // 查询ops操作历史列表
    static async queryOpsPipelineInstanceList(req) {
        return rbHttp.sendRequest({
            method: 'POST',
            url: '/v1/ops-service/opsManage/queryOpsPipelineInstanceList',
            params: req
        }).then(function (data) {
            return data
        })
    }
    /**
     * 首页改版20200413
     */
    // 自动化对象数量
    static async queryNewIndexAgentStatistic() {
        return rbHttp.sendRequest({
            method: 'GET',
            url: '/v1/ops-service/opsReport/newIndexAgentStatistic',
        }).then(function (data) {
            return data
        })
    }
    // 7日自动化任务
    static async queryNewIndexPipeInstStatistic7Days() {
        return rbHttp.sendRequest({
            method: 'GET',
            url: '/v1/ops-service/opsReport/newIndexPipeInstStatistic7Days',
        }).then(function (data) {
            return data
        })
    }
    // 7日自动巡检任务
    static async queryNewIndexInspectionTaskStatistic7Days() {
        return rbHttp.sendRequest({
            method: 'GET',
            url: '/v1/ops-service/opsReport/newIndexInspectionTaskStatistic7Days',
        }).then(function (data) {
            return data
        })
    }
    // 7日故障自愈任务
    static async queryNewIndexAutoRepairInstanceStatistic7Days() {
        return rbHttp.sendRequest({
            method: 'GET',
            url: '/v1/ops-service/opsReport/newIndexAutoRepairInstanceStatistic7Days',
        }).then(function (data) {
            return data
        })
    }
    // 30日内所有任务
    static async queryNewIndexAllTasksStatistic30Days() {
        return rbHttp.sendRequest({
            method: 'GET',
            url: '/v1/ops-service/opsReport/newIndexAllTasksStatistic30Days',
        }).then(function (data) {
            return data
        })
    }
    // 7天任务执行情况
    static async queryNewIndexPipeInstStatisticByLine7Days() {
        return rbHttp.sendRequest({
            method: 'GET',
            url: '/v1/ops-service/opsReport/newIndexPipeInstStatisticByLine7Days',
        }).then(function (data) {
            return data
        })
    }
    // 7天各类型任务分布
    static async queryNewIndexAllTaskTypeStatisticByPie7Days() {
        return rbHttp.sendRequest({
            method: 'GET',
            url: '/v1/ops-service/opsReport/newIndexAllTaskTypeStatisticByPie7Days',
        }).then(function (data) {
            return data
        })
    }
    // 7天任务执行时长分布
    static async queryNewIndexPipeInstCostTimeStatistic7Days() {
        return rbHttp.sendRequest({
            method: 'GET',
            url: '/v1/ops-service/opsReport/newIndexPipeInstCostTimeStatistic7Days',
        }).then(function (data) {
            return data
        })
    }
    // 任务执行异常列表
    static async queryNewIndexExceptionPipeInstPageList(req) {
        return rbHttp.sendRequest({
            method: 'POST',
            data: req,
            url: '/v1/ops-service/opsReport/newIndexExceptionPipeInstPageList',
        }).then(function (data) {
            return data
        })
    }
    // 场景快捷键
    static async queryNormalScenesStatistic() {
        return rbHttp.sendRequest({
            method: 'GET',
            url: '/v1/ops-service/opsReport/normalScenesStatistic',
        }).then(function (data) {
            return data
        })
    }
    // 今日任务
    static async queryTodayOpsTaskStatistic() {
        return rbHttp.sendRequest({
            method: 'GET',
            url: '/v1/ops-service/opsReport/todayOpsTaskStatistic',
        }).then(function (data) {
            return data
        })
    }
    // 今日任务执行状态
    static async queryTodayOpsTaskStatusStatistic() {
        return rbHttp.sendRequest({
            method: 'GET',
            url: '/v1/ops-service/opsReport/todayOpsTaskStatusStatistic',
        }).then(function (data) {
            return data
        })
    }
    // 待处理任务
    static async queryToBeProcessedTaskList(req) {
        return rbHttp.sendRequest({
            method: 'POST',
            data: req,
            url: '/v1/ops-service/opsReport/toBeProcessedTaskList',
        }).then(function (data) {
            return data
        })
    }
    // 七日任务执行成功率
    static async queryTaskExec7DaySuccessedRate() {
        return rbHttp.sendRequest({
            method: 'GET',
            url: '/v1/ops-service/opsReport/taskExec7DaySuccessedRate',
        }).then(function (data) {
            return data
        })
    }
}
