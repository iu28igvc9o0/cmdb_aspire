import rbHttp from 'assets/js/utility/rb-http.factory'

// IP采集
const IpCollection = '/v1/cmdb/ipCollect'
const IpClash = '/v1/cmdb/ipClash'
const IPAudit = '/v1/cmdb/ipAudit'
export default class rbIpCollectionServiceFactory {
    //   IP采集功能接口开始
    // 存活IP查询
    static async survivalIpSearch(data) {
        return rbHttp.sendRequest({
            method: 'POST',
            data: data,
            url: IpCollection + '/survival/findPage'
        })
    }
    // 存活Ip获取所属资源池下拉数据
    static async getResourcePool(data) {
        return rbHttp.sendRequest({
            method: 'GET',
            params: data,
            url: IpCollection + '/survival/getResource'
        })
    }
    // 存活Ip获取数据来源下拉数据
    static async getSourcePool(data) {
        return rbHttp.sendRequest({
            method: 'GET',
            params: data,
            url: IpCollection + '/survival/getSource'
        })
    }
    // 存活Ip中导出接口
    static async survialIpExport(data) {
        return rbHttp.sendRequest({
            method: 'POST',
            binary: true,
            data: data,
            url: IpCollection + '/survival/export'
        })
    }
    // 虚拟IP查询
    static async fictitiousIpSearch(data) {
        return rbHttp.sendRequest({
            method: 'POST',
            data: data,
            url: IpCollection + '/fictitious/findPage'
        })
    }
    // 虚拟IP获取所属资源池下拉数据
    static async fictitiousResourcePool(data) {
        return rbHttp.sendRequest({
            method: 'GET',
            params: data,
            url: IpCollection + '/fictitious/getResource'
        })
    }
    // 虚拟IP获取使用类型
    static async fictitiousGetUserType(data) {
        return rbHttp.sendRequest({
            method: 'GET',
            data: data,
            url: IpCollection + '/fictitious/getUserType'
        })
    }
    // 虚拟IP中导出接口
    static async fictitiousIpExport(data) {
        return rbHttp.sendRequest({
            method: 'POST',
            binary: true,
            data: data,
            url: IpCollection + '/fictitious/export'
        })
    }

    // 冲突IP查询
    static async clashIpSearch(data) {
        return rbHttp.sendRequest({
            method: 'POST',
            data: data,
            url: IpClash + '/findPageAndTotal'
        })
    }

    // 冲突Ip中导出接口
    static async clashIpExport(data) {
        return rbHttp.sendRequest({
            method: 'POST',
            binary: true,
            data: data,
            url: IpClash + '/export'
        })
    }
    // 冲突ip更新状态接口
    static async clashIpStatus(data) {
        return rbHttp.sendRequest({
            method: 'POST',
            data: data,
            url: IpClash + '/updateHandleStatus'
        })
    }
    //   IP采集功能接口结束

    // IP稽核报表接口开始
    // 存活未录入CMDB查询列表
    static async survialCmdbSearch(data) {
        return rbHttp.sendRequest({
            method: 'POST',
            data: data,
            url: IPAudit + '/intranetIp/getSurvivingUnrecordIntranetIpList'
        })
    }
    // 存活未录入CMDB导出接口
    static async survialCmdbExport(data) {
        return rbHttp.sendRequest({
            method: 'POST',
            binary: true,
            data: data,
            url: IPAudit + '/intranetIp/exportSurvivingUnrecordIntranetIpList'
        })
    }
    // 存活未录入CMDB更新状态接口
    static async survialCmdbUpdate(data) {
        return rbHttp.sendRequest({
            method: 'POST',
            data: data,
            url: IPAudit + '/intranetIp/updateSurvivingUnrecordIntranetIpProcessStatus'
        })
    }
    // 已录入CMDB未存活IP查询列表
    static async recordedCmdbSearch(data) {
        return rbHttp.sendRequest({
            method: 'POST',
            data: data,
            url: IPAudit + '/intranetIp/getRecordedUnsurvivingIntranetIpList'
        })
    }
    // 已录入CMDB未存活IP导出接口
    static async recordedCmdbExport(data) {
        return rbHttp.sendRequest({
            method: 'POST',
            binary: true,
            data: data,
            url: IPAudit + '/intranetIp/exportRecordedUnsurvivingIntranetIpList'
        })
    }
    // 已录入CMDB未存活IP更新状态接口
    static async recordedCmdbUpdate(data) {
        return rbHttp.sendRequest({
            method: 'POST',
            data: data,
            url: IPAudit + '/intranetIp/updateRecordedUnsurvivingIntranetIpProcessStatus'
        })
    }
    // 已分配未存活IP查询列表
    static async unsavedIpSearch(data) {
        return rbHttp.sendRequest({
            method: 'POST',
            data: data,
            url: IPAudit + '/intranetIp/getAssignedUnsurvivingIntranetIpList'
        })
    }
    // 已分配未存活IP导出接口
    static async unsavedIpExport(data) {
        return rbHttp.sendRequest({
            method: 'POST',
            binary: true,
            data: data,
            url: IPAudit + '/intranetIp/exportAssignedUnsurvivingIntranetIpList'
        })
    }
    // 已分配未存活IP更新状态接口
    static async unsavedIpUpdate(data) {
        return rbHttp.sendRequest({
            method: 'POST',
            data: data,
            url: IPAudit + '/intranetIp/updateAssignedUnsurvivingIntranetIpProcessStatus'
        })
    }

    // 存活未分配内网IP查询列表
    static async unassignedIpSearch(data) {
        return rbHttp.sendRequest({
            method: 'POST',
            data: data,
            url: IPAudit + '/intranetIp/getSurvivingUnassignIntranetIpList'
        })
    }
    // 存活未分配内网IP导出接口
    static async unassignedIpExport(data) {
        return rbHttp.sendRequest({
            method: 'POST',
            binary: true,
            data: data,
            url: IPAudit + '/intranetIp/exportSurvivingUnassignIntranetIpList'
        })
    }
    // 存活未分配内网IP更新状态接口
    static async unassignedIpUpdate(data) {
        return rbHttp.sendRequest({
            method: 'POST',
            data: data,
            url: IPAudit + '/intranetIp/updateSurvivingUnassignIntranetIpProcessStatus'
        })
    }
    // 存活未规划内网IP查询列表
    static async survialUnplanInnerIpSearch(data) {
        return rbHttp.sendRequest({
            method: 'POST',
            data: data,
            url: IPAudit + '/intranetIp/getSurvivingUnplannedIntranetIpList'

        })
    }
    // 存活未规划内网IP导出接口
    static async survialUnplanInnerIpExport(data) {
        return rbHttp.sendRequest({
            method: 'POST',
            binary: true,
            data: data,
            url: IPAudit + '/intranetIp/exportSurvivingUnplannedIntranetIpList'

        })
    }
    // 存活未规划内网IP更新状态接口
    static async survialUnplanInnerIpUpdate(data) {
        return rbHttp.sendRequest({
            method: 'POST',
            data: data,
            url: IPAudit + '/intranetIp/updateSurvivingUnplannedIntranetIpProcessStatus'
        })
    }
    // 登记已过期IP查询列表（内网、公网、IPV6公用）
    static async loginOverdueIpSearch(data) {
        return rbHttp.sendRequest({
            method: 'POST',
            data: data,
            url: IPAudit + '/getRegistrationExpiredIpList'


        })
    }
    // 登记已过期IP导出接口（内网、公网、IPV6公用）
    static async loginOverdueIpExport(data) {
        return rbHttp.sendRequest({
            method: 'POST',
            binary: true,
            data: data,
            url: IPAudit + '/exportRegistrationExpiredIpList'


        })
    }
    // 登记已过期IP（内网、公网、IPV6公用）更新状态接口
    static async loginOverdueIpUpdate(data) {
        return rbHttp.sendRequest({
            method: 'POST',
            data: data,
            url: IPAudit + '/intranetIp/updateRegistrationExpiredIpProcessStatus'
        })
    }
    // 存活未规划公网IP查询列表
    static async survialUnplanPublicIpSearch(data) {
        return rbHttp.sendRequest({
            method: 'POST',
            data: data,
            url: IPAudit + '/intranetIp/getUnPlanPublicIpList'
        })
    }
    // 存活未规划公网IP导出接口
    static async survialUnplanPublicIpExport(data) {
        return rbHttp.sendRequest({
            method: 'POST',
            binary: true,
            data: data,
            url: IPAudit + '/intranetIp/exportUnPlanPublicIpList'

        })
    }

    // 存活未分配公网IP查询列表
    static async survialUnassignPublicIpSearch(data) {
        return rbHttp.sendRequest({
            method: 'POST',
            data: data,
            url: IPAudit + '/intranetIp/getUnAssignPublicIpList'
        })
    }
    // 存活未分配公网IP导出接口
    static async survialUnassignPublicIpExport(data) {
        return rbHttp.sendRequest({
            method: 'POST',
            binary: true,
            data: data,
            url: IPAudit + '/intranetIp/exportUnAssignPublicIpList'
        })
    }
    // 登记已过期公网IP查询列表
    static async loginOverduePublicIpSearch(data) {
        return rbHttp.sendRequest({
            method: 'POST',
            data: data,
            url: IPAudit + '/intranetIp/getRegistrationExpiredPublicIpList'
        })
    }
    // 登记已过期公网IP导出接口
    static async loginOverduePublicIpExport(data) {
        return rbHttp.sendRequest({
            method: 'POST',
            binary: true,
            data: data,
            url: IPAudit + '/intranetIp/exportRegistrationExpiredPublicIpList'
        })
    }
    // 存活未规划、未分配公网IP状态更新接口
    static async survialPublicIpUpdate(data) {
        return rbHttp.sendRequest({
            method: 'POST',
            data: data,
            url: IPAudit + '/intranetIp/updatePublicIpProcessStatus'
        })
    }
    // 存活未规划IPV6 IP查询列表
    static async survialUnplanIpV6IpSearch(data) {
        return rbHttp.sendRequest({
            method: 'POST',
            data: data,
            url: IPAudit + '/intranetIp/getUnPlanIPv6List'
        })
    }
    // 存活未规划IPV6 IP导出接口
    static async survialUnplanIpV6IpExport(data) {
        return rbHttp.sendRequest({
            method: 'POST',
            binary: true,
            data: data,
            url: IPAudit + '/intranetIp/exportUnPlanIPv6List'
        })
    }
    // 存活未分配IPV6 IP查询列表
    static async survialUnassignIpV6IpSearch(data) {
        return rbHttp.sendRequest({
            method: 'POST',
            data: data,
            url: IPAudit + '/intranetIp/getUnAssignIPv6List'
        })
    }
    // 存活未分配IPV6 IP导出接口
    static async survialUnassignIpV6IpExport(data) {
        return rbHttp.sendRequest({
            method: 'POST',
            binary: true,
            data: data,
            url: IPAudit + '/intranetIp/exportUnAssignIPv6List'
        })
    }
    // 存活未规划、存活未分配IP更新状态接口
    static async survialIpV6IpUpdate(data) {
        return rbHttp.sendRequest({
            method: 'POST',
            data: data,
            url: IPAudit + '/intranetIp/updateIPv6ProcessStatus'
        })
    }

    // 登记已过期IPV6查询列表
    static async loginOverdueIpV6Search(data) {
        return rbHttp.sendRequest({
            method: 'POST',
            data: data,
            url: IPAudit + '/intranetIp/getRegistrationExpiredIpv6List'
        })
    }
    // 登记已过期IPV6导出接口
    static async loginOverdueIpV6Export(data) {
        return rbHttp.sendRequest({
            method: 'POST',
            binary: true,
            data: data,
            url: IPAudit + '/intranetIp/exportRegistrationExpiredIpv6List'
        })
    }
    // IP稽核报表接口结束
    // 获取公共下拉取值接口
    static async getCommonSelectValue(data) {
        return rbHttp.sendRequest({
            method: 'GET',
            url: '/v1/cmdb/configDict/getDictsByType?type=' + data
        })
    }
    // 获取一二级业务线、网络类型下拉值
    static async getResource(data) {
        return rbHttp.sendRequest({
            method: 'GET',
            url: '/v1/cmdb/ipAudit/getResource?type=' + data.type + ((data.type === 'financialBusiness' || data.type === 'business1'|| data.type==='deviceStatus') ? '' : '&pid=' + data.pid)
        })
    }
    // IP登记
    static async updateIpRepositoryInfo(data) {
        return rbHttp.sendRequest({
            method: 'POST',
            data: data,
            url: '/v1/cmdb/ipAudit/updateIpRepositoryInfo'
        })
    }

}
