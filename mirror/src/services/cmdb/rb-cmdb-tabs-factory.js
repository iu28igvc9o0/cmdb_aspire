import rbHttp from 'assets/js/utility/rb-http.factory'

const tabsManage = '/v1/cmdb/modelTabs'
export default class rbTabsServiceFactory {
    // 更新模型tab标签
    static async saveModelTabs(data) {
        return rbHttp.sendRequest({
            method: 'POST',
            data: data,
            url: tabsManage + '/saveModelTabs'
        }).then(function (data) {
            return data
        })
    }
    // 获取模型tab标签列表
    static async getModelTabsList(data) {
        return rbHttp.sendRequest({
            method: 'POST',
            data: data,
            url: tabsManage + '/getModelTabsList'
        }).then(function (data) {
            return data
        })
    }
    // 根据ID获取模型tab标签
    static async getModelTabsById(data) {
        return rbHttp.sendRequest({
            method: 'GET',
            params: data,
            url: tabsManage + '/getModelTabsById'
        }).then(function (data) {
            return data
        })
    }
    // 根据tabsId删除模型tab标签
    static async deleteModelTabsById(data) {
        return rbHttp.sendRequest({
            method: 'GET',
            params: data,
            url: tabsManage + '/deleteModelTabsById'
        }).then(function (data) {
            return data
        })
    }


}