import rbHttp from 'assets/js/utility/rb-http.factory'

const CMDBCONFIGDICT_BASE_URL = '/v1/v3/cmdb/module/config'

export default class rbConfigDictServiceFactory {

  // 新增
  static async save(data) {
    return rbHttp.sendRequest({
        method: 'POST',
        data: data,
        url: CMDBCONFIGDICT_BASE_URL + '/save'
    }).then(function (data) {
        return data
    })
  }

  // 修改
  static async update(data) {
    return rbHttp.sendRequest({
        method: 'PUT',
        data: data,
        url: CMDBCONFIGDICT_BASE_URL + '/update'
    }).then(function (data) {
        return data
    })
  }

  // 删除
  static async delete(data) {
    return rbHttp.sendRequest({
        method: 'DELETE',
        data: data,
        url: CMDBCONFIGDICT_BASE_URL + '/delete'
    }).then(function (data) {
        return data
    })
  }

  // 分页列表
  static async listWithPage(data) {
    return rbHttp.sendRequest({
        method: 'POST',
        data: data,
        url: CMDBCONFIGDICT_BASE_URL + '/list'
    }).then(function (data) {
        return data
    })
  }

  // 单例查询
  static async getOne(data) {
    return rbHttp.sendRequest({
        method: 'GET',
        params: data,
        url: CMDBCONFIGDICT_BASE_URL + '/getOne'
    }).then(function (data) {
      return data
    })
  }
}
