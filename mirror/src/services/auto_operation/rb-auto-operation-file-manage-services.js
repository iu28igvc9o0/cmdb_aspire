import rbHttp from 'assets/js/utility/rb-http.factory'

export default class fileManageService {
    /**
     * 首页
     */
    // 文件列表
    static async queryFileList(req) {
        return rbHttp.sendRequest({
            method: 'POST',
            params: req,
            url: '/v1/ops-service/opsFileManage/pageList',
        }).then(function (data) {
            return data
        })
    }
    // 文件详情
    static async getFileDetail(req) {
        return rbHttp.sendRequest({
            method: 'GET',
            params: req,
            url: '/v1/ops-service/opsFileManage/getFileDetail',
        }).then(function (data) {
            return data
        })
    }
    // 文件保存
    static async saveFile(req) {
        return rbHttp.sendRequest({
            method: 'POST',
            url: '/v1/ops-service/opsFileManage/saveFile',
            params: req
        }).then(function (data) {
            return data
        })
    }
    // 文件删除
    static async deleteFile(req) {
        return rbHttp.sendRequest({
            method: 'DELETE',
            url: '/v1/ops-service/opsFileManage/deleteFile',
            params: req
        }).then(function (data) {
            return data
        })
    }
    // 文件上传
    static async uploadFile(req) {
        return rbHttp.sendRequest({
            method: 'POST',
            url: '/v1/ops-service/opsFileManage/uploadFile',
            data: req
        }).then(function (data) {
            return data
        })
    }
    // 文件下载
    static async downloadFile(req) {
        return rbHttp.sendRequest({
            method: 'POST',
            url: '/v1/ops-service/opsFileManage/downloadFile',
            params: req,
            responseType: 'arraybuffer'
        }).then(function (data) {
            return data
        })
    }
    static async convergeDownloadFile(req) {
        return rbHttp.sendRequest({
            method: 'POST',
            url: '/v1/ops-service/opsFileManage/convergeDownloadFile',
            params: req,
            responseType: 'arraybuffer'
        }).then(function (data) {
            return data
        })
    }
}
