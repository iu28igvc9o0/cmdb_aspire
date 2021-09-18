/*
* 文档管理
*/
import rbHttp from 'assets/js/utility/rb-http.factory'

export default class rbDocManageServicesFactory {
    // 保存文件
    static async saveFile(req) {
        return rbHttp.sendRequest({
            useFormData: true,
            method: 'POST',
            data: req,
            url: '/v1/cmdb/file/manage/save',
        }).then(function(data) {
            return data
        })
    }
    // 删除文件
    static async deleteFile(req) {
        return rbHttp.sendRequest({
            method: 'POST',
            data: req,
            url: '/v1/cmdb/file/manage/delete',
        }).then(function(data) {
            return data
        })
    }
    // 修改文件
    static async updateFile(req) {
        return rbHttp.sendRequest({
            method: 'POST',
            url: '/v1/cmdb/file/manage/update',
            params: req,
        }).then(function(data) {
            return data
        })
    }
    // 查询文件列表
    static async queryFileList(req) {
        return rbHttp.sendRequest({
            method: 'POST',
            url: '/v1/cmdb/file/manage/list',
            data: req
        }).then(function(data) {
            return data
        })
    }
    // 查询文件类型
    static async getDictsByType(req) {
        return rbHttp.sendRequest({
            method: 'GET',
            params: req,
            url: '/v1/cmdb/configDict/getDictsByType',
        }).then(function(data) {
            return data
        })
    }
    // 查询文件对象
    static async queryFileObj(req) {
        return rbHttp.sendRequest({
            method: 'POST',
            url: '/v1/cmdb/file/manage/listFileObj',
            data: req
        }).then(function(data) {
            return data
        })
    }
    // 下载文件
    static async downloadFile(id) {
        return rbHttp.sendRequest({
            binary: true,
            method: 'GET',
            url: `/v1/cmdb/file/manage/download/${id}`,
        }).then(function(data) {
            return data
        })
    }
}
