import rbHttp from 'assets/js/utility/rb-http.factory'

const ALTER_STANDARD_URL = '/v1/alerts/standard'

export default class rbAlertStandardServiceFactory {

  static async insert(data) {
    return rbHttp.sendRequest({
        method: 'POST',
        data: data,
        url: ALTER_STANDARD_URL + '/insert'
    }).then(function(data) {
        return data
    })
  }

  static async update(data) {
    return rbHttp.sendRequest({
        method: 'PUT',
        data: data,
        url: ALTER_STANDARD_URL + '/update'
    }).then(function(data) {
        return data
    })
  }

  static async deleteByIds(data) {
    return rbHttp.sendRequest({
        method: 'DELETE',
        data: data,
        url: ALTER_STANDARD_URL + '/delete'
    }).then(function(data) {
        return data
    })
  }

  static async list(params) {
    return rbHttp.sendRequest({
        method: 'GET',
        params: params,
        url: ALTER_STANDARD_URL + '/list'
    }).then(function(data) {
        return data
    })
  }

  static async updateStatus(params) {
    return rbHttp.sendRequest({
        method: 'PUT',
        data: params,
        url: ALTER_STANDARD_URL + '/update/status'
    }).then(function(data) {
        return data
    })
  }

  static async updateHistory(params) {
    return rbHttp.sendRequest({
        method: 'PUT',
        data: params,
        url: ALTER_STANDARD_URL + '/update/history'
    }).then(function(data) {
        return data
    })
  }

  static async updateHistoryOneRow(id,params) {
    return rbHttp.sendRequest({
        method: 'PUT',
        data: params,
        url: ALTER_STANDARD_URL + '/update/history/' + id
    }).then(function(data) {
        return data
    })
  }

  static async exportAll(data) {
    return rbHttp.sendRequest({
        method: 'GET',
        params: data,
        binary: true,
        url: ALTER_STANDARD_URL + '/export'
    }).then(function(data) {
        return data
    })
  }

  // 告警标准化下载导入模板
  static async downloadImportTemplate(templateName, downloadFileName) {
    return rbHttp.sendRequest({
        method: 'GET',
        params: {
          fileName: templateName
        },
        url: ALTER_STANDARD_URL + '/downloadTemp',
        binary: true
    }).then(function (data) {
        let blob = new Blob([data], {
            type: 'application/vnd.ms-excel'
        })
        // 创建下载链接
        let objectUrl = URL.createObjectURL(blob)
        let downLoadElement = document.createElement('a')
        downLoadElement.href = objectUrl
        downLoadElement.download = downloadFileName ? downloadFileName : templateName
        document.body.appendChild(downLoadElement)
        downLoadElement.click()
        document.body.removeChild(downLoadElement)
        URL.revokeObjectURL(objectUrl)
        return data
    })
  }
}