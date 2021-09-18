/*
* 自动化运维-作业管理 项目
*/
import rbHttp from 'assets/js/utility/rb-http.factory'

export default class rbAutoOperationServicesFactory {
    /**
   * 定时作业
   */
    // 查询执行账户/ops用户列表
    static async queryOpsPipelineRunJobList(req) {
        return rbHttp.sendRequest({
            method: 'POST',
            url: '/v1/ops-service/opsManage/queryOpsPipelineRunJobList',
            data: req
        }).then(function (data) {
            return data
        })
    }
    // 新增定时作业
    static async saveOpsPipelineRunJob(req) {
        return rbHttp.sendRequest({
            method: 'POST',
            url: '/v1/ops-service/opsManage/saveOpsPipelineRunJob',
            data: req
        }).then(function (data) {
            return data
        })
    }
    // 删除定时作业
    static async removeOpsPipelineRunJob(jobId) {
        return rbHttp.sendRequest({
            method: 'DELETE',
            url: `/v1/ops-service/opsManage/removeOpsPipelineRunJob/${jobId}`
        }).then(function (data) {
            return data
        })
    }

    // 启动定时作业
    static async schedulePipelineCronJob(jobId) {
        return rbHttp.sendRequest({
            method: 'PUT',
            url: `/v1/ops-service/opsManage/schedulePipelineCronJob/${jobId}`
        }).then(function (data) {
            return data
        })
    }
    // 停止定时作业
    static async unSchedulePipelineCronJob(jobId) {
        return rbHttp.sendRequest({
            method: 'PUT',
            url: `/v1/ops-service/opsManage/unSchedulePipelineCronJob/${jobId}`
        }).then(function (data) {
            return data
        })
    }
    /**
   * 作业管理
   */
    // 获取作业列表
    static async queryOpsPipelineList(req) {
        return rbHttp.sendRequest({
            method: 'POST',
            url: '/v1/ops-service/opsManage/queryOpsPipelineList',
            data: req
        }).then(function (data) {
            return data
        })
    }
    // 根据id查询作业
    static async queryOpsPipelineById(pipelineId) {
        return rbHttp.sendRequest({
            method: 'GET',
            url: `/v1/ops-service/opsManage/queryOpsPipelineById/${pipelineId}`,
        }).then(function (data) {
            return data
        })
    }
    // 新增、修改作业
    static async saveOpsPipeline(req) {
        return rbHttp.sendRequest({
            method: 'POST',
            url: '/v1/ops-service/opsManage/saveOpsPipeline',
            data: req
        }).then(function (data) {
            return data
        })
    }
    // 删除作业
    static async removePipeline(pipelineId) {
        return rbHttp.sendRequest({
            method: 'DELETE',
            url: `/v1/ops-service/opsManage/removePipeline/${pipelineId}`
        }).then(function (data) {
            return data
        })
    }
    static async auditOpsPipeline(req) {
        return rbHttp.sendRequest({
            method: 'PUT',
            url: '/v1/ops-service/opsManage/auditPipeline',
            params: req
        }).then(function (data) {
            return data
        })
    }
    // 根据作业id查询步骤定义列表
    static async queryOpsStepListByPipelineId(pipelineId) {
        return rbHttp.sendRequest({
            method: 'GET',
            url: `/v1/ops-service/opsManage/queryOpsStepListByPipelineId/${pipelineId}`
        }).then(function (data) {
            return data
        })
    }
    // 执行作业
    static async pipelineExecute(req) {
        return rbHttp.sendRequest({
            method: 'PUT',
            url: `/v1/ops-service/opsManage/pipelineExecute/${req.pipelineId}`,
            data: req
        }).then(function (data) {
            return data
        })
    }
    // 作业步骤暂停后手工处理
    static async manualHandleOpsStepInstance(stepInstanceId) {
        return rbHttp.sendRequest({
            method: 'PUT',
            url: `/v1/ops-service/opsManage/manualHandleOpsStepInstance/${stepInstanceId}/100`,
        }).then(function (data) {
            return data
        })
    }
    // 根据作业运行实例id，查询实例对象
    static async queryOpsPipelineInstanceById(pipelineInstanceId) {
        return rbHttp.sendRequest({
            method: 'GET',
            url: `/v1/ops-service/opsManage/queryOpsPipelineInstanceById/${pipelineInstanceId}`,
        }).then(function (data) {
            return data
        })
    }
    // 查询分类标签
    static async queryOpsLabelList() {
        return rbHttp.sendRequest({
            method: 'POST',
            url: '/v1/ops-service/opsManage/queryOpsLabelList',
        }).then(function (data) {
            return data
        })
    }
    // 本地上传分发文件
    static async uploadFile4Transfer(params) {
        return rbHttp.sendRequest({
            method: 'POST',
            data: params,
            url: '/v1/ops-service/opsManage/uploadFile4Transfer',
        }).then(function (data) {
            return data
        })
    }
    // 执行实时文件分发
    static async realtimeFileTransfer(params) {
        return rbHttp.sendRequest({
            method: 'PUT',
            data: params,
            url: '/v1/ops-service/opsManage/realtimeFileTransfer',
        }).then(function (data) {
            return data
        })
    }
    /**
     * 作业执行日志下载
     */

    // 克隆作业
    static async copyOpsPipeline(req) {
        return rbHttp.sendRequest({
            method: 'POST',
            url: '/v1/ops-service/opsManage/copyOpsPipeline',
            data: req
        }).then(function (data) {
            return data
        })
    }
    // 删除作业

    /**
     * 作业执行日志下载
     */
    // 作业历史日志查询接口
    static async getPipelineInstanceLog(req) {
        return rbHttp.sendRequest({
            method: 'GET',
            params: req,
            url: '/v1/ops-service/opsManage/getPipelineInstanceLog',
        }).then(function (data) {
            return data
        })
    }
    // 执行历史日志打包申请
    static async logPackageApply(req) {
        return rbHttp.sendRequest({
            method: 'GET',
            params: req,
            url: '/v1/ops-service/opsManage/logPackageApply',
        }).then(function (data) {
            return data
        })
    }
    // 作业历史日志包下载
    static async downloadLogFile(req) {
        return rbHttp.sendRequest({
            method: 'POST',
            params: req,
            responseType: 'arraybuffer',
            url: '/v1/ops-service/opsManage/downloadLogFile',
        }).then(function (data) {
            return data
        })
    }
    // 作业历史日志产出
    static async downloadSummaryOutput(req) {
        return rbHttp.sendRequest({
            method: 'POST',
            params: req,
            responseType: 'arraybuffer',
            url: '/v1/ops-service/opsManage/downloadSummaryOutput',
        }).then(function (data) {
            return data
        })
    }
    // 获取场景列表
    static async pipelineScenesAllList() {
        return rbHttp.sendRequest({
            method: 'GET',
            url: '/v1/ops-service/opsManage/pipelineScenesAllList',
        }).then(function (data) {
            return data
        })
    }
    // 场景详情
    static async pipelineScenesById(req) {
        return rbHttp.sendRequest({
            method: 'GET',
            params: req,
            url: '/v1/ops-service/opsManage/pipelineScenesById',
        }).then(function (data) {
            return data
        })
    }
    // 保存场景
    static async savePipelineScenes(params, pictureData) {
        const formData = new FormData()
        formData.append('scenes_picture', pictureData)
        console.log(formData)
        return rbHttp.sendRequest({
            method: 'POST',
            params: params,
            data: formData,
            url: '/v1/ops-service/opsManage/savePipelineScenes',
        }).then(function (data) {
            return data
        })
    }
    // 删除场景
    static async deletePipelineScenes(req) {
        return rbHttp.sendRequest({
            method: 'DELETE',
            params: req,
            url: '/v1/ops-service/opsManage/deletePipelineScenes',
        }).then(function (data) {
            return data
        })
    }
    // 查询分组树
    static async querGroupTree(params) {
        return rbHttp.sendRequest({
            method: 'POST',
            params: params,
            url: '/v1/ops-service/opsGroup/querGroupTree',
        }).then(function (data) {
            return data
        })
    }
}
