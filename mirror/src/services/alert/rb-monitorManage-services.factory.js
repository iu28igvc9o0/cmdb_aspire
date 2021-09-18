/*
* Mirror 项目
*/
import rbHttp from 'assets/js/utility/rb-http.factory'
import { formatTreeSelectData } from 'src/utils/globalUtils.js'
const URL = '/v1/alerts/monitorHttp'
export default class rbAlertFilterSceneServiceFactory {
    static async getList(page) {
        return rbHttp.sendRequest({
            method: 'POST',
            url: `${URL}/pageList`,
            data: page
        }).then(function (data) {
            return data
        })
    }
    static async create(object) {
        return rbHttp.sendRequest({
            method: 'POST',
            url: `${URL}/create`,
            data: object
        }).then(function (data) {
            return data
        })
    }
    static async update(object, oldName) {
        return rbHttp.sendRequest({
            method: 'PUT',
            url: `${URL}/update`,
            data: object,
            params: {
                oldName: oldName
            }
        }).then(function (data) {
            return data
        })
    }
    static async updateStatus(object) {
        return rbHttp.sendRequest({
            method: 'PUT',
            url: `${URL}/updateStatus`,
            data: object
        }).then(function (data) {
            return data
        })
    }
    static async delete(id) {
        return rbHttp.sendRequest({
            method: 'DELETE',
            url: `${URL}/delete/` + id,
            params: {
                id: id
            }
        }).then(function (data) {
            return data
        })
    }
    static async getByKey(id) {
        return rbHttp.sendRequest({
            method: 'GET',
            url: `${URL}/detail/` + id,
            params: {
                id: id
            }
        }).then(function (data) {
            return data
        })
    }

    static async getByName(name) {
        return rbHttp.sendRequest({
            method: 'GET',
            url: `${URL}/findByName/` + name,
            params: {
                name: name
            }
        }).then(function (data) {
            return data
        })
    }

    static async getHisList(page) {
        return rbHttp.sendRequest({
            method: 'POST',
            url: `${URL}/pageListHis`,
            data: page
        }).then(function (data) {
            return data
        })
    }

    static async getBizSystem() {
        return rbHttp.sendRequest({
            method: 'GET',
            url: '/v1/cmdb/orgManager/loadTreeDepBiz?namespace=alauda'
        }).then(function (data) {
            let biz_tree = []
            _(data).forEach(item => {
                biz_tree.push(formatTreeSelectData(item))
            })
            return biz_tree
        })
    }

    static async getHisById(id) {
        return rbHttp.sendRequest({
            method: 'GET',
            url: `${URL}/findHisByPrimaryKey/` + id,
            params: {
                id: id
            }
        }).then(function (data) {
            return data
        })
    }

    static async getIdcTypes() {
        return rbHttp.sendRequest({
            method: 'GET',
            url: `${URL}/getIdcTypes`
        }).then(function (data) {
            return data
        })
    }

    static async testHttp(object) {
        return rbHttp.sendRequest({
            method: 'POST',
            url: `${URL}/testHttp`,
            data: object
        }).then(function (data) {
            return data
        })
    }
}
