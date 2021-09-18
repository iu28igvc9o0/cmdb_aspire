import rbHttp from 'assets/js/utility/rb-http.factory'

const AgentCheckPoint = '/v1/cmdb/agent'

export default class agentCheckService {

  // 分页查询列表数据
  static async listByPage (data) {
    return rbHttp.sendRequest({
        method: 'POST',
        data: data,
        url: AgentCheckPoint + '/list'
    }).then(function (data) {
        return data
    })
  }

    // 批量删除数据
    static async batchDelete (data) {
      return rbHttp.sendRequest({
          method: 'POST',
          data: data,
          url: AgentCheckPoint + '/delete'
      }).then(function (data) {
          return data
      })
    }

    // 导出数据
    static async export (data) {
      return rbHttp.sendRequest({
          method: 'POST',
          data: data,
          binary: true,
          url: AgentCheckPoint + '/export'
      }).then(function (data) {
          return data
      })
    }
}