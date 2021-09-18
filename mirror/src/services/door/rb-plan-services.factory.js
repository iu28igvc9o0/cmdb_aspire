/**
 * 变更计划
 */
import rbHttp from 'assets/js/utility/rb-http.factory'

export default class rbPlanServicesFactory {
    /**
     * 创建任务
     */
    // 资源池
    static async getConfigDictByType(req) {
        return rbHttp.sendRequest({
            method: 'GET',
            params: req,
            url: '/v1/cmdb/configDict/getDictsByType'
        }).then(function (data) {
            return data
        })
    }
    // 创建任务
    static async addTask(req) {
        return rbHttp.sendRequest({
            method: 'POST',
            params: req,
            url: '/v1/alerts/task/addTask'
        }).then(function (data) {
            return data
        })
    }
    // 获取任务列表
    static async getTaskList(req) {
        return rbHttp.sendRequest({
            method: 'POST',
            params: req,
            url: '/v1/alerts/task/getTaskList'
        }).then(function (data) {
            return data
        })
    }
    // 获取任务详情
    static async getTaskDetail(req) {
        return rbHttp.sendRequest({
            method: 'GET',
            params: req,
            url: '/v1/alerts/task/getTaskDetail'
        }).then(function (data) {
            return data
        })
    }
    // 编辑任务
    static async updateTask(req) {
        return rbHttp.sendRequest({
            method: 'POST',
            params: req,
            url: '/v1/alerts/task/updateTask'
        }).then(function (data) {
            return data
        })
    }
    // 删除任务
    static async deleteTask(req) {
        return rbHttp.sendRequest({
            method: 'DELETE',
            params: req,
            url: '/v1/alerts/task/deleteTask'
        }).then(function (data) {
            return data
        })
    }
    // 编辑任务反馈意见
    static async editTaskMessage(req) {
        return rbHttp.sendRequest({
            method: 'POST',
            params: req,
            url: '/v1/alerts/task/editTaskMessage'
        }).then(function (data) {
            return data
        })
    }
    // 开始任务
    static async startTask(req) {
        return rbHttp.sendRequest({
            method: 'POST',
            params: req,
            url: '/v1/alerts/task/startTask'
        }).then(function (data) {
            return data
        })
    }
    // 结束任务
    static async stopTask(req) {
        return rbHttp.sendRequest({
            method: 'POST',
            params: req,
            url: '/v1/alerts/task/stopTask'
        }).then(function (data) {
            return data
        })
    }
    // 获取任务操作列表
    static async getTaskActionList(req) {
        return rbHttp.sendRequest({
            method: 'POST',
            params: req,
            url: '/v1/alerts/task/getTaskActionList'
        }).then(function (data) {
            return data
        })
    }
    // 获取任务反馈意见列表
    static async getTaskMessageList(req) {
        return rbHttp.sendRequest({
            method: 'GET',
            params: req,
            url: '/v1/alerts/task/getTaskMessageList'
        }).then(function (data) {
            return data
        })
    }
    // 导出任务
    static async exportTask(req) {
        return rbHttp.sendRequest({
            method: 'POST',
            params: req,
            responseType: 'arraybuffer',
            url: '/v1/alerts/task/exportTask'
        }).then(function (data) {
            return data
        })
    }
    // 任务通知
    static async taskNotify(req) {
        return rbHttp.sendRequest({
            method: 'GET',
            params: req,
            url: '/v1/alerts/task/taskNotify'
        }).then(function (data) {
            return data
        })
    }
    // 下载任务模板
    static async downloadTaskTemplate() {
        return rbHttp.sendRequest({
            method: 'POST',
            responseType: 'arraybuffer',
            url: '/v1/alerts/task/downloadTaskTemplate'
        }).then(function (data) {
            return data
        })
    }
    // 导入任务
    static async importTaskTemplate(req) {
        return rbHttp.sendRequest({
            method: 'POST',
            params: req,
            url: '/v1/alerts/task/importTaskTemplate'
        }).then(function (data) {
            return data
        })
    }
}
