
import rbHttp from 'assets/js/utility/rb-http.factory'

export default class CmdbFactory {

    // 1. 获取agent主机信息加载方式   
    static getAgentHostInfoLoadSource () {
        return rbHttp.sendRequest({
            method: 'GET',
            addNamespace: false,
            url: '/v1/ops-service/opsManage/getAgentHostInfoLoadSource '
        })
    }
    // 2. 获取CMDB页面动态查询条件 
    static async getCmdbCondition () {
        return rbHttp.sendRequest({
            method: 'POST',
            url: '/v1/v3/cmdb/condication/get',
            data: { 'condicationCode': 'automatic_agent_search' }
        }).then(function (data) {
            return data
        })
    }
    // 3.  执行sql加载下拉数据 
    static async queryTableData (req) {
        return rbHttp.sendRequest({
            method: 'POST',
            url: '/v1/cmdb/module/queryTableData',
            data: req
        }).then(function (data) {
            return data
        })
    }
    // 5.  查询cdmb设备列表 
    static async fetchUserAuthedAgentHostList (req) {
        return rbHttp.sendRequest({
            method: 'POST',
            url: '/v1/ops-service/opsManage/fetchUserAuthedAgentHostList',
            data: req
        }).then(function (data) {
            return data
        })
    }
}