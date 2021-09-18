/*
* Mirror 项目
*/
import rbHttp from 'assets/js/utility/rb-http.factory'

export default class rbSysMenuServiceFactory {
    static async getSystemList() {
        return rbHttp.sendRequest({
            method: 'GET',
            url: '/v1/sys_manage/list',
        }).then(function (data) {
            return data
        })
    }
    static async getMenuListById(sysId) {
        return rbHttp.sendRequest({
            method: 'GET',
            url: `/v1/sys_menu/list/${sysId}`,
        }).then(function (data) {
            console.log(data)
            return data
        })
    }
    static async getMenuListByName(sysName) {
        return rbHttp.sendRequest({
            method: 'GET',
            url: `/v1/sys_menu/menu/${sysName}`,
        }).then(function (data) {
            return data
        })
    }
    static async getDetail(id) {
        return rbHttp.sendRequest({
            method: 'GET',
            url: `/v1/sys_menu/detail/${id}`,
        }).then(function (data) {
            return data
        })
    }
    static async insert(req) {
        return rbHttp.sendRequest({
            method: 'POST',
            url: '/v1/sys_menu/insert',
            data: req
        }).then(function (data) {
            return data
        })
    }
    static async update(req) {
        return rbHttp.sendRequest({
            method: 'PUT',
            url: '/v1/sys_menu/update',
            data: req
        }).then(function (data) {
            return data
        })
    }
    static async delete(id) {
        return rbHttp.sendRequest({
            method: 'DELETE',
            url: `/v1/sys_menu/delete/${id}`,
        }).then(function (data) {
            return data
        })
    }
}
