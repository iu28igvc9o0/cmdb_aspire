/*
* 自动化运维-脚本管理 项目
*/
import rbHttp from 'assets/js/utility/rb-http.factory'

export default class rbAutoOperationServicesFactory {
    /**
   * 脚本管理
   */
    // 脚本列表查询
    static async queryOpsScriptList(req) {
        return rbHttp.sendRequest({
            method: 'POST',
            url: '/v1/ops-service/opsManage/queryOpsScriptList',
            data: req
        }).then(function (data) {
            return data
        })
    }
    // 根据脚本id查询脚本内容
    static async queryOpsScriptById(id) {
        return rbHttp.sendRequest({
            method: 'GET',
            url: `/v1/ops-service/opsManage/queryOpsScriptById?scriptId=${id}`,
            data: id
        }).then(function (data) {
            return data
        })
    }
    // 新增、修改脚本
    static async saveScript(req) {
        return rbHttp.sendRequest({
            method: 'POST',
            url: '/v1/ops-service/opsManage/saveScript',
            data: req
        }).then(function (data) {
            return data
        })
    }
    // 克隆脚本
    static async copyScript(req) {
        return rbHttp.sendRequest({
            method: 'POST',
            url: '/v1/ops-service/opsManage/copyScript',
            data: req
        }).then(function (data) {
            return data
        })
    }
    // 审核脚本
    static async auditScript(req) {
        return rbHttp.sendRequest({
            method: 'PUT',
            url: '/v1/ops-service/opsManage/auditScript',
            params: req
        }).then(function (data) {
            return data
        })
    }
    // 执行
    static async continueExecInstance(req) {
        return rbHttp.sendRequest({
            method: 'PUT',
            url: '/v1/ops-service/opsManage/continueExecInstance',
            params: req
        }).then(function (data) {
            return data
        })
    }
    // 上传脚本文件
    static async readLocalScriptFile(req) {
        return rbHttp.sendRequest({
            method: 'POST',
            url: '/v1/ops-service/opsManage/readLocalScriptFile',
            data: req
        }).then(function (data) {
            return data
        })
    }
    // 删除脚本
    static async removeScript(scriptId) {
        return rbHttp.sendRequest({
            method: 'DELETE',
            url: `/v1/ops-service/opsManage/removeScript/${scriptId}`
        }).then(function (data) {
            return data
        })
    }
    // 执行脚本
    static async realtimeScriptExecute(req) {
        return rbHttp.sendRequest({
            method: 'PUT',
            url: '/v1/ops-service/opsManage/realtimeScriptExecute',
            data: req
        }).then(function (data) {
            return data
        })
    }
    // 校验脚本是否包含敏感指令
    static async checkScriptSensitivity(req) {
        return rbHttp.sendRequest({
            method: 'POST',
            data: req,
            url: '/v1/ops-service/sensitive/checkScriptSensitivity'
        }).then(function (data) {
            return data
        })
    }
    // 根据作业实例ID查询审核列表
    static async getSensitiveReviewByPipelineInstanceId(req) {
        return rbHttp.sendRequest({
            method: 'GET',
            url: '/v1/ops-service/sensitive/getSensitiveReviewByPipelineInstanceId',
            params: req
        }).then(function (data) {
            return data
        })
    }
    /**
   * 基本数据查询
   */
    // 查询执行账户/ops用户列表
    static async queryOpsAccountList(req) {
        return rbHttp.sendRequest({
            method: 'POST',
            url: '/v1/ops-service/opsManage/queryOpsAccountList',
            data: req
        }).then(function (data) {
            return data
        })
    }
    // 查询agent主机列表
    static async fetchUserAuthedAgentHostList(req) {
        return rbHttp.sendRequest({
            method: 'POST',
            url: '/v1/ops-service/opsManage/fetchUserAuthedAgentHostList',
            data: req
        }).then(function (data) {
            return data
        })
    }
    // 查询执行状态列表
    static async loadOpsStatusList() {
        return rbHttp.sendRequest({
            method: 'GET',
            url: '/v1/ops-service/opsManage/loadOpsStatusList'
        }).then(function (data) {
            return data
        })
    }
    // 查询触发方式列表
    static async loadOpsTriggerWayList() {
        return rbHttp.sendRequest({
            method: 'GET',
            url: '/v1/ops-service/opsManage/loadOpsTriggerWayList'
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
    // 根据ops历史实例查询ops步骤实例列表
    static async queryStepInstListByPipelineInstId(pipelineInstanceId) {
        return rbHttp.sendRequest({
            method: 'POST',
            url: `/v1/ops-service/opsManage/queryStepInstListByPipelineInstId/${pipelineInstanceId}`
        }).then(function (data) {
            return data
        })
    }
    // 根据ops步骤实例查询主机执行详情列表
    static async queryOpsStepInstanceAgentRunResultList(req) {
        return rbHttp.sendRequest({
            method: 'POST',
            url: '/v1/ops-service/opsManage/queryOpsStepInstanceAgentRunResultList',
            params: req
        }).then(function (data) {
            return data
        })
    }
    // 根据ops步骤实例id查询实例详情
    static async queryStepInstanceById(stepInstId) {
        return rbHttp.sendRequest({
            method: 'GET',
            url: `/v1/ops-service/opsManage/queryStepInstanceById?stepInstId=${stepInstId}`,
        }).then(function (data) {
            return data
        })
    }
    // 作业实例指令审核申请
    static async reviewSensitiveApply(pipelineInstanceId) {
        return rbHttp.sendRequest({
            method: 'PUT',
            url: `/v1/ops-service/opsManage/reviewSensitiveApply/${pipelineInstanceId}`,
        }).then(function (data) {
            return data
        })
    }
    // 查询自定义参数列表
    static async queryCustomParamsList() {
        return rbHttp.sendRequest({
            method: 'GET',
            url: '/v1/ops-service/opsManage/getParamAllList',
        }).then(function (data) {
            return data
        })
    }

    // 下载文件
    //     {
    //     "file_path": "/home/sudoroot/temp/demo/sftpRoot/ops_file_store/pipeInst_1000/stepInst_55/10.12.70.39/SftpServer.jar",  // 下载路径，可以为全路径，也可以为相对于sftp根目录的相对路径; 
    //         "is_relative": "N"   // Y|N  Y: 下载路径为全路径  N：下载路径为SFTP相对路径
    // }
    static async downloadFile(req) {
        return rbHttp.sendRequest({
            method: 'POST',
            params: req,
            responseType: 'arraybuffer',
            url: '/v1/ops-service/opsFileManage/downloadFile',
        }).then(function (data) {
            return data
        })
    }

    // 获取作业历史列表
    static async getPipelineHisListByPipelineId(req) {
        return rbHttp.sendRequest({
            method: 'GET',
            params: req,
            url: '/v1/ops-service/opsManage/getPipelineHisListByPipelineId',
        }).then(function (data) {
            return data
        })
    }
    // 作业版本变更
    static async updatePipelineVersion(req) {
        return rbHttp.sendRequest({
            method: 'PUT',
            url: '/v1/ops-service/opsManage/updatePipelineVersion',
            params: req
        }).then(function (data) {
            return data
        })
    }
    // 获取脚本历史列表
    static async getScriptHisListByScriptId(req) {
        return rbHttp.sendRequest({
            method: 'GET',
            params: req,
            url: '/v1/ops-service/opsManage/getScriptHisListByScriptId',
        }).then(function (data) {
            return data
        })
    }
    // 脚本版本变更
    static async updateScriptVersion(req) {
        return rbHttp.sendRequest({
            method: 'PUT',
            url: '/v1/ops-service/opsManage/updateScriptVersion',
            params: req
        }).then(function (data) {
            return data
        })
    }
    // 敏感指令分级列表
    static async querySensitiveLevelList(req) {
        return rbHttp.sendRequest({
            method: 'POST',
            url: '/v1/ops-service/sensitive/querySensitiveLevelList',
            params: req
        }).then(function (data) {
            return data
        })
    }
    // 获取自动化详细agent设备列表
    static async getUsernameListByAgentIp(req) {
        return rbHttp.sendRequest({
            method: 'GET',
            params: req,
            url: '/v1/ops-service/opsManage/getUsernameListByAgentIp',
        }).then(function (data) {
            return data
        })
    }
    //  用户密码列表    
    static async getNormalAgentHostList(req) {
        return rbHttp.sendRequest({
            method: 'POST',
            params: req,
            url: '/v1/ops-service/opsManage/getNormalAgentHostList',
        }).then(function (data) {
            return data
        })
    }
    // 密码导出
    static async exportUserPassword(req, downloadPassword) {
        return rbHttp.sendRequest({
            method: 'POST',
            data: req,
            responseType: 'arraybuffer',
            url: `/v1/ops-service/opsManage/exportUserPassword/${downloadPassword}`,
        }).then(function (data) {
            return data
        })
    }
    // 密码导出
    static async exportStepTargetIp(id) {
        return rbHttp.sendRequest({
            method: 'GET',
            responseType: 'arraybuffer',
            url: `/v1/ops-service/opsManage/exportStepTargetIp/${id}`,
        }).then(function (data) {
            return data
        })
    }
    // 根据作业历史id查询历史作业
    static async queryOpsPipelineHisById(id) {
        return rbHttp.sendRequest({
            method: 'GET',
            url: `/v1/ops-service/opsManage/queryOpsPipelineHisById?pipelineHisId=${id}`,
            data: id
        }).then(function (data) {
            return data
        })
    }
    // 根据作业历史id查询历史步骤
    static async queryOpsStepHisListByPipelineHisId(id) {
        return rbHttp.sendRequest({
            method: 'GET',
            url: `/v1/ops-service/opsManage/queryOpsStepHisListByPipelineHisId?pipelineHisId=${id}`,
            data: id
        }).then(function (data) {
            return data
        })
    }
    // 根据脚本历史id查询脚本
    static async queryOpsScriptHisById(id) {
        return rbHttp.sendRequest({
            method: 'GET',
            url: `/v1/ops-service/opsManage/queryOpsScriptHisById?scriptHisId=${id}`,
            data: id
        }).then(function (data) {
            return data
        })
    }
}
