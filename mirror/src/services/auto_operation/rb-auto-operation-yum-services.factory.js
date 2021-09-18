/*
* 自动化运维-yum源管理
*/
import rbHttp from 'assets/js/utility/rb-http.factory'

export default class rbAutoOperationHomeServicesFactory {
    /**
   * yum源管理
   */
    // 获取操作系统发行版本列表
    static async fetchOsDistributionList() {
        return rbHttp.sendRequest({
            method: 'GET',
            url: '/v1/ops-service/yumManage/fetchOsDistributionList',
        }).then(function(data) {
            return data
        })
    }
    // 查询yum文件分组树
    static async queryYumFileGroupTreeList() {
        return rbHttp.sendRequest({
            method: 'POST',
            url: '/v1/ops-service/yumManage/queryYumFileGroupTreeList',
        }).then(function(data) {
            return data
        })
    }
    // 创建yum文件分组节点
    static async createYumFileGroup(req) {
        return rbHttp.sendRequest({
            method: 'POST',
            url: '/v1/ops-service/yumManage/createYumFileGroup',
            params: req
        }).then(function(data) {
            return data
        })
    }
    // 修改yum文件分组节点
    static async updateYumFileGroup(req) {
        return rbHttp.sendRequest({
            method: 'PUT',
            url: '/v1/ops-service/yumManage/updateYumFileGroup',
            data: req
        }).then(function(data) {
            return data
        })
    }
    // 删除yum文件分组节点
    static async removeYumFileGroup(yumFileGroupId) {
        return rbHttp.sendRequest({
            method: 'DELETE',
            url: `/v1/ops-service/yumManage/removeYumFileGroup/${yumFileGroupId}`,
        }).then(function(data) {
            return data
        })
    }
    // 查询yum源文件列表
    static async queryYumSourceList(req) {
        return rbHttp.sendRequest({
            method: 'POST',
            url: '/v1/ops-service/yumManage/queryYumSourceList',
            data: req
        }).then(function(data) {
            return data
        })
    }
    // 上传本地yum文件
    static async uploadYumLocalFile(req) {
        return rbHttp.sendRequest({
            method: 'POST',
            url: '/v1/ops-service/yumManage/uploadYumLocalFile',
            data: req
        }).then(function(data) {
            return data
        })
    }
    // 上传远程主机yum文件
    static async transferYumFile(req) {
        return rbHttp.sendRequest({
            method: 'POST',
            url: '/v1/ops-service/yumManage/transferYumFile',
            data: req
        }).then(function(data) {
            return data
        })
    }
    // 创建yum源记录
    static async createYumSourceModel(req) {
        return rbHttp.sendRequest({
            method: 'POST',
            url: '/v1/ops-service/yumManage/createYumSourceModel',
            data: req
        }).then(function(data) {
            return data
        })
    }
    // 更新yum源记录
    static async updateYumSourceModel(req) {
        return rbHttp.sendRequest({
            method: 'PUT',
            url: '/v1/ops-service/yumManage/updateYumSourceModel',
            data: req
        }).then(function(data) {
            return data
        })
    }
    // 删除yum源记录
    static async removeYumSource(yumSourceId) {
        return rbHttp.sendRequest({
            method: 'DELETE',
            url: `/v1/ops-service/yumManage/removeYumSource/${yumSourceId}`,
        }).then(function(data) {
            return data
        })
    }
    // 下载yum源记录
    static async downloadYumFile(req) {
        return rbHttp.sendRequest({
            method: 'POST',
            url: '/v1/ops-service/yumManage/downloadYumFile',
            data: req
        }).then(function(data) {
            return data
        })
    }
    // 查询yum配置文件列表
    static async queryYumConfigList(req) {
        return rbHttp.sendRequest({
            method: 'POST',
            url: '/v1/ops-service/yumManage/queryYumConfigList',
            data: req
        }).then(function(data) {
            return data
        })
    }
    // 创建yum配置记录
    static async createYumConfigModel(req) {
        return rbHttp.sendRequest({
            method: 'POST',
            url: '/v1/ops-service/yumManage/createYumConfigModel',
            data: req
        }).then(function(data) {
            return data
        })
    }
    // 更新yum配置记录
    static async updateYumConfigModel(req) {
        return rbHttp.sendRequest({
            method: 'PUT',
            url: '/v1/ops-service/yumManage/updateYumConfigModel',
            data: req
        }).then(function(data) {
            return data
        })
    }
    // 删除yum配置记录
    static async removeYumConfig(yumConfigId) {
        return rbHttp.sendRequest({
            method: 'DELETE',
            url: `/v1/ops-service/yumManage/removeYumConfig/${yumConfigId}`,
        }).then(function(data) {
            return data
        })
    }
}
