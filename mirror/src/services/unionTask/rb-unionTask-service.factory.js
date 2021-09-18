import rbHttp from 'assets/js/utility/rb-http.factory'

// 任务协同接口
export default class unionTaskService {

    // 创建协同作战 
    static async addUnionFight(params) {
        return rbHttp.sendRequest({
            method: 'POST',
            data: params,
            url: '/v1/cmdb/teamword/createTeamwork'
        }).then(function (data) {
            return data
        })
    }
    // 编辑协同作战 
    static async editUnionFight(params) {
        return rbHttp.sendRequest({
            method: 'PUT',
            data: params,
            url: '/v1/cmdb/teamword/updateTeamwork'
        }).then(function (data) {
            return data
        })
    }
    // 删除协同作战
    static async deleteUnionFight(params) {
        return rbHttp.sendRequest({
            method: 'DELETE',
            params: params,
            url: '/v1/cmdb/teamword/deleteTeamwork'
        }).then(function (data) {
            return data
        })
    }
}
