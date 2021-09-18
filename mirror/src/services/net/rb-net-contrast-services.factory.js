import rbHttp from 'assets/js/utility/rb-http.factory'

export default class rbNetContrastServicesFactory {

    /**
     * 主备对比
     */
    // 导出配置比对列表接口
    static async exportList(req) {
        return rbHttp.sendRequest({
            method: 'POST',
            responseType: 'arraybuffer',
            url: '/v1/configCompare/export',
            data: req
        }).then(function (data) {
            return data
        })
    }
    // 导出配置比对清单
    static async exportDetail(req) {
        return rbHttp.sendRequest({
            method: 'POST',
            responseType: 'arraybuffer',
            url: '/v1/configCompare/exportDetail',
            data: req
        }).then(function (data) {
            return data
        })
    }
    // 下载模板
    static async downloadTemplate() {
        return rbHttp.sendRequest({
            method: 'POST',
            url: '/v1/configCompare/downloadTemplate',
            binary: true
        }).then(function (data) {
            let blob = new Blob([data], {
                type: 'application/msword'
            })
            // 创建下载链接
            let objectUrl = URL.createObjectURL(blob)
            let downLoadElement = document.createElement('a')
            downLoadElement.href = objectUrl
            downLoadElement.download = '主备自动对比.xls'
            document.body.appendChild(downLoadElement)
            downLoadElement.click()
            document.body.removeChild(downLoadElement)
            URL.revokeObjectURL(objectUrl)
            return data
        })
    }
    // 导入主备清单
    static async importList(req) {
        return rbHttp.sendRequest({
            method: 'POST',
            url: '/v1/configCompare/import',
            data: req
        }).then(function (data) {
            return data
        })
    }
    //
    // 新增比对接口
    static async insertAdd(req) {
        return rbHttp.sendRequest({
            method: 'POST',
            url: '/v1/configCompare/import',
            data: req
        }).then(function (data) {
            return data
        })
    }
    // 比对 
    static async compareData(id, masterIndex, backupIndex) {
        return rbHttp.sendRequest({
            method: 'POST',
            url: `/v1/configCompare/compare/${id}?masterIndex=${masterIndex}&backupIndex=${backupIndex}`,
        }).then(function (data) {
            return data
        })
    }
    // 查询比对记录列表接口 
    static async logsDataList(id) {
        return rbHttp.sendRequest({
            method: 'GET',
            url: `/v1/configCompare/logs/${id}`,
        }).then(function (data) {
            return data
        })
    }

    // 导出比对记录
    static async exportLogs(id) {
        return rbHttp.sendRequest({
            method: 'GET',
            // url: `/v1/ops-service/opsAutoRepair/loadApPipeFinishJudgeDropdownList/${req.judgeType}`,
            url: `/v1/configCompare/exportLogs/${id}`,
            responseType: 'arraybuffer',
            // data: req
        }).then(function (data) {
            return data
        })
    }
    // 索引列表
    static async indexList(id) {
        return rbHttp.sendRequest({
            method: 'GET',
            url: `/v1/configCompare/index/${id}`,
            // data: req
        }).then(function (data) {
            return data
        })
    }
}