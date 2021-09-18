import rbHttp from 'assets/js/utility/rb-http.factory'

export default class bugManageService {
    /**
     * 漏洞管理
     * 漏洞分组规则
     */
    // 分组规则列表
    static async queryVulnerabilityGroupList(req) {
        return rbHttp.sendRequest({
            method: 'POST',
            params: req,
            url: '/v1/ops-service/vulnerabilityGroup/queryVulnerabilityGroupList',
        }).then(function (data) {
            return data
        })
    }
    // 分组规则详情 vulnerability_group_id
    static async getVulnerabilityGroup(req) {
        return rbHttp.sendRequest({
            method: 'GET',
            params: req,
            url: '/v1/ops-service/vulnerabilityGroup/getVulnerabilityGroup',
        }).then(function (data) {
            return data
        })
    }
    // 分组规则新增、修改
    static async saveVulnerabilityGroup(req) {
        return rbHttp.sendRequest({
            method: 'POST',
            url: '/v1/ops-service/vulnerabilityGroup/saveVulnerabilityGroup',
            params: req
        }).then(function (data) {
            return data
        })
    }
    // 分组规则删除 vulnerability_group_id
    static async removeVulnerabilityGroup(req) {
        return rbHttp.sendRequest({
            method: 'DELETE',
            url: '/v1/ops-service/vulnerabilityGroup/removeVulnerabilityGroup',
            params: req
        }).then(function (data) {
            return data
        })
    }
    /**
     * 漏洞管理
     * 漏洞列表
     */
    // 漏洞列表查询
    static async queryVulnerabilityList(req) {
        return rbHttp.sendRequest({
            method: 'POST',
            params: req,
            url: '/v1/ops-service/vulnerability/queryVulnerabilityList',
        }).then(function (data) {
            return data
        })
    }
    // 漏洞实例列表查询(漏洞下的主机列表)
    static async queryVulInstanceList(req) {
        return rbHttp.sendRequest({
            method: 'POST',
            params: req,
            url: '/v1/ops-service/vulnerability/queryVulInstanceList',
        }).then(function (data) {
            return data
        })
    }
    // 漏洞详情
    static async queryVulnerabilityById(req) {
        return rbHttp.sendRequest({
            method: 'GET',
            params: req,
            url: '/v1/ops-service/vulnerability/queryVulnerabilityById',
        }).then(function (data) {
            return data
        })
    }
    // 关联作业
    static async updateVulnerability(req) {
        return rbHttp.sendRequest({
            method: 'PUT',
            params: req,
            url: '/v1/ops-service/vulnerability/updateVulnerability',
        }).then(function (data) {
            return data
        })
    }
    // 漏洞实例信息修改
    static async updateVulInstance(req) {
        return rbHttp.sendRequest({
            method: 'PUT',
            params: req,
            url: '/v1/ops-service/vulnerability/updateVulInstance',
        }).then(function (data) {
            return data
        })
    }
    // 执行漏洞修复作业、返回作业实例id
    static async executeVulnerabilityRepair(req) {
        return rbHttp.sendRequest({
            method: 'PUT',
            params: req,
            url: `/v1/ops-service/vulnerability/executeVulnerabilityRepair/${req.vulInstanceId}/${req.pipelineId}`,
        }).then(function (data) {
            return data
        })
    }
    // 查询漏洞等级列表
    static async queryRiskLevelList(req) {
        return rbHttp.sendRequest({
            method: 'GET',
            params: req,
            url: '/v1/ops-service/vulnerability/riskLevelList',
        }).then(function (data) {
            return data
        })
    }

    /**
     * 20200827
     * 漏洞列表调整
     * 漏洞扫描结果
     * 漏洞扫描报告
     */
    // 导入漏洞报告
    static async importVulnerabilityReport(req) {
        return rbHttp.sendRequest({
            method: 'POST',
            data: req,
            url: '/v1/ops-service/vulnerability/importVulnerabilityReport',
            // url: `/v1/ops-service/vulnerability/importVulnerabilityReport/idcType=${req.idcType}&reportType=${req.reportType}&scanCycle=${req.scanCycle}`,

        }).then(function (data) {
            return data
        })
    }
    // 下载漏洞修复手册模板
    static async downloadVulnerabilityTemplate(req) {
        return rbHttp.sendRequest({
            method: 'GET',
            data: req,
            responseType: 'arraybuffer',
            url: '/v1/ops-service/vulnerability/downloadVulnerabilityTemplate',
        }).then(function (data) {
            return data
        })
    }
    // 导入漏洞修复手册
    static async importVulnerabilityFixedFile(req) {
        return rbHttp.sendRequest({
            method: 'POST',
            data: req,
            url: '/v1/ops-service/vulnerability/importVulnerability',
        }).then(function (data) {
            return data
        })
    }
    // 导出漏洞列表
    static async exportVulnerability(req) {
        return rbHttp.sendRequest({
            method: 'POST',
            data: req,
            responseType: 'arraybuffer',
            url: '/v1/ops-service/vulnerability/exportVulnerability',
        }).then(function (data) {
            return data
        })
    }
    // 创建漏洞
    static async saveVulnerability(req) {
        return rbHttp.sendRequest({
            method: 'POST',
            data: req,
            url: '/v1/ops-service/vulnerability/saveVulnerability',
        }).then(function (data) {
            return data
        })
    }
    // 查询漏洞报告
    static async queryVulnerabilityReport(req) {
        return rbHttp.sendRequest({
            method: 'POST',
            data: req,
            url: '/v1/ops-service/vulnerability/vulnerabilityReport',
        }).then(function (data) {
            return data
        })
    }
    // 导出漏洞报告
    static async exportVulnerabilityReport(req) {
        return rbHttp.sendRequest({
            method: 'POST',
            data: req,
            responseType: 'arraybuffer',
            url: '/v1/ops-service/vulnerability/exportVulnerabilityReport',
        }).then(function (data) {
            return data
        })
    }
    // 获取扫描周期列表
    static async getScanCycleList() {
        return rbHttp.sendRequest({
            method: 'GET',
            url: '/v1/ops-service/vulnerability/getScanCycleList',
        }).then(function (data) {
            return data
        })
    }
    // 查询漏洞实例详情
    static async getVulnerabilityInstanceDetailById(req) {
        return rbHttp.sendRequest({
            method: 'GET',
            params: req,
            url: '/v1/ops-service/vulnerability/getVulnerabilityInstanceDetailById',
        }).then(function (data) {
            return data
        })
    }
    // 批量修复漏洞实例
    static async batchExecuteVulnerabilityRepair(req) {
        return rbHttp.sendRequest({
            method: 'PUT',
            data: req,
            url: '/v1/ops-service/vulnerability/batchExecuteVulnerabilityRepair',
        }).then(function (data) {
            return data
        })
    }

    // 回退
    static async executeVulnerabilityGoBack(req) {
        return rbHttp.sendRequest({
            method: 'PUT',
            params: req,
            url: `/v1/ops-service/vulnerability/executeVulnerabilityGoBack/${req.vulInstanceId}/${req.pipelineId}`,
        }).then(function (data) {
            return data
        })
    }
    // 复查
    static async executeVulnerabilityRecheck(req) {
        return rbHttp.sendRequest({
            method: 'PUT',
            params: req,
            url: `/v1/ops-service/vulnerability/executeVulnerabilityRecheck/${req.vulInstanceId}/${req.pipelineId}`,
        }).then(function (data) {
            return data
        })
    }
    // 新增漏洞周期
    static async saveVulScanCycle(req) {
        return rbHttp.sendRequest({
            method: 'POST',
            data: req,
            url: '/v1/ops-service/vulnerability/saveVulScanCycle',
        }).then(function (data) {
            return data
        })
    }
    // 漏洞报告详情
    static async queryVulReportItemList(req) {
        return rbHttp.sendRequest({
            method: 'POST',
            data: req,
            url: '/v1/ops-service/vulnerability/queryVulReportItemList',
        }).then(function (data) {
            return data
        })
    }
    // 漏洞报告详情统计
    static async queryVulReportDetailStatistics(req) {
        return rbHttp.sendRequest({
            method: 'POST',
            data: req,
            url: '/v1/ops-service/vulnerability/queryVulReportDetailStatistics',
        }).then(function (data) {
            return data
        })
    }
    // 下载漏洞报告
    static async downloadSourceReport(req) {
        return rbHttp.sendRequest({
            method: 'POST',
            data: req,
            responseType: 'arraybuffer',
            url: '/v1/ops-service/vulnerability/downloadSourceReport',
        }).then(function (data) {
            return data
        })
    }

    // 漏洞报告导出
    static async exportVulNewReport(req) {
        return rbHttp.sendRequest({
            method: 'POST',
            data: req,
            responseType: 'arraybuffer',
            url: '/v1/ops-service/vulnerability/exportVulNewReport',
        }).then(function (data) {
            return data
        })
    }

}
