/**
 * Bpm-服务台首页
 */
import rbHttp from 'assets/js/utility/rb-http.factory'

export default class rbBpmHomeServices {
    /**
     * 首页
     */
    // 获取工单起草列表
    static async getUserDraftList() {
        return rbHttp.sendRequest({
            method: 'GET',
            url: '/v1/desk/workDraft/getUserDraftList',
        }).then(function (data) {
            return data
        })
    }
    // 获取用户常用操作列表
    static async getUserOperationsList() {
        return rbHttp.sendRequest({
            method: 'GET',
            url: '/v1/desk/commonOperations/getUserOperationsList',
        }).then(function (data) {
            return data
        })
    }
    // 获取各类型订单列表、数量
    static async getOrderList(req) {
        return rbHttp.sendRequest({
            method: 'POST',
            data: req,
            url: '/v1/bpm/runTime/instance/getOrderList',
        }).then(function (data) {
            return data
        })
    }
    // 催办工单列表
    static async getCuibanList(req) {
        return rbHttp.sendRequest({
            method: 'POST',
            data: req,
            url: '/v1/desk/remind/list',
        }).then(function (data) {
            return data
        })
    }
    // 催办订单
    static async reminderOrder(req) {
        return rbHttp.sendRequest({
            method: 'POST',
            data: req,
            url: '/v1/bpm/runTime/instance/reminder',
        }).then(function (data) {
            return data
        })
    }
    // 服务台人员首页 20201029
    // 快速搜索-搜人
    static async getUsers(req) {
        return rbHttp.sendRequest({
            method: 'POST',
            params: req,
            url: '/v1/user/pageList',
        }).then(function (data) {
            return data
        })
    }
    // 快速搜索-搜工单
    static async getOrders(req) {
        return rbHttp.sendRequest({
            method: 'POST',
            data: req,
            url: '/v1/desk/runtime/getInstDetailListForDesk',
        }).then(function (data) {
            return data
        })
    }
    // 快速搜索-搜工单（深圳环境）
    static async getOrdersByShenzhen(req) {
        return rbHttp.sendRequest({
            method: 'POST',
            data: req,
            url: '/v1/bpm/runtime/umsHome/instSearch',
        }).then(function (data) {
            return data
        })
    }
    // 首页公告走马灯-无需分页，后台根据时间获取
    static async getHomePageNotice() {
        return rbHttp.sendRequest({
            method: 'POST',
            url: '/v1/desk/publicNotice/findListHomePage',
        }).then(function (data) {
            return data
        })
    }
    // 服务台接入云客服系统，获取token
    static async getHomeYunToken(req) {
        return rbHttp.sendRequest({
            method: 'GET',
            url: '/v1/desk/ccs/getToken?module=' + req.module,
        }).then(function (data) {
            return data
        })
    }
    // 首页服务事件工单统计
    static async getHomePageStatist() {
        return rbHttp.sendRequest({
            method: 'GET',
            url: '/v1/desk/zxgllc/statist',
        }).then(function (data) {
            return data
        })
    }
    // 首页催办列表导出
    static async exportRemindList(req) {
        return rbHttp.sendRequest({
            method: 'POST',
            data: req,
            responseType: 'arraybuffer',
            url: '/v1/desk/remind/export',
        }).then(function (data) {
            return data
        })
    }
    // 公告
    static async getAnnouncement(req) {
        return rbHttp.sendRequest({
            method: 'POST',
            data: req,
            url: '/v1/bpm/runtime/umsHome/getOfficialDocumentList',
        }).then(function (data) {
            return data
        })
    }
    // 效能展示-总览
    static async getInstEfficiencyShow(req) {
        return rbHttp.sendRequest({
            method: 'POST',
            data: req,
            url: '/v1/bpm/runtime/umsHome/getInstEfficiencyShow',
        }).then(function (data) {
            return data
        })
    }
    // 效能展示-详情
    static async getInstEfficiencyReport(req) {
        return rbHttp.sendRequest({
            method: 'POST',
            data: req,
            url: '/v1/bpm/runtime/umsHome/getInstEfficiencyReport',
        }).then(function (data) {
            return data
        })
    }
    // 工单考核
    static async getWorkAssessmentReport(req) {
        return rbHttp.sendRequest({
            method: 'POST',
            data: req,
            url: '/v1/bpm/runtime/umsHome/getWorkAssessmentReport',
        }).then(function (data) {
            return data
        })
    }
    // 工单top
    static async getWorkTop(req) {
        return rbHttp.sendRequest({
            method: 'POST',
            data: req,
            url: '/v1/bpm/runtime/umsHome/getWorkTop',
        }).then(function (data) {
            return data
        })
    }
    // 搜知识
    static async knowledge(req) {
        return rbHttp.sendRequest({
            method: 'POST',
            data: req,
            url: '/v1/desk/ccs/knowledge/list',
        }).then(function (data) {
            return data
        })
    }
    // 知识详情
    static async getKnowledgeDetail(id) {
        return rbHttp.sendRequest({
            method: 'GET',
            url: '/v1/desk/ccs/knowledge/' + id + '?kmType=inner',
        }).then(function (data) {
            return data
        })
    }

}
