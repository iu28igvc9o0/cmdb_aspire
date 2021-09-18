import rbHttp from 'assets/js/utility/rb-http.factory'

const ASSIGNENDPOIN = '/v1/cmdb/assign'

export default class report {
    static async listAssign (data) {
        return rbHttp.sendRequest({
            method: 'POST',
            data: data,
            url: ASSIGNENDPOIN + '/list'
        }).then(function (data) {
            return data
        })
    }
    static async exportAssign (data) {
        return rbHttp.sendRequest({
            method: 'POST',
            data: data,
            binary: true,
            url: ASSIGNENDPOIN + '/export'
        }).then(function (data) {
            return data
        })
    }
    static async deleteAssign (data) {
        return rbHttp.sendRequest({
            method: 'DELETE',
            params: data,
            url: ASSIGNENDPOIN + '/delete'
        }).then(function (data) {
            return data
        })
    }
}
