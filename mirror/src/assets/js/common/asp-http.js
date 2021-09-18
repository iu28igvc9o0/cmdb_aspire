/**
 * http 请求入口
 * Modify by TurboC on 2012/02/17.
 * ================================================================ */
import axios from 'axios'

const aspHttps = {}

// 封装fetch方法
aspHttps.asp_Fetch = (url, params = null) => {
  if (params !== null) {
    params = Object.keys(params).map(function (key) {
      return encodeURIComponent(key) + '=' + encodeURIComponent(params[key])
    }).join('&')
    url = url + '?' + params
  }
  return new Promise((resolve, reject) => {
    axios.get(url)
      .then(response => {
        resolve(response.data)
      })
      .catch(err => {
        reject(err)
      })
  })
}
// 封装get方法
aspHttps.asp_Get = (url, params = null) => {
  if (params !== undefined && params !== null && Object.keys(params).length > 0) {
    params = Object.keys(params).map(function (key) {
      return encodeURIComponent(key) + '=' + encodeURIComponent(params[key])
    }).join('&')
    url = url + '?' + params
  }
  return new Promise((resolve, reject) => {
    axios.get(url)
      .then(response => {
        resolve(response.data)
      })
      .catch(err => {
        reject(err)
      })
  })
}
// 封装post请求(json类型)
aspHttps.asp_Post = (url, data) => {
  return new Promise((resolve, reject) => {
    axios.post(url, data)
      .then(response => {
        resolve(response.data)
      }, err => {
        reject(err)
      })
  })
}
// 封装post请求(form类型)
aspHttps.asp_PostForm = (url, data) => {
  const headers = { 'Content-type': 'application/x-www-form-urlencoded' }
  return new Promise((resolve, reject) => {
    axios.post(url, data, { headers: headers })
      .then(response => {
        resolve(response.data)
      }, err => {
        reject(err)
      })
  })
}
// 封装delete请求
aspHttps.asp_RequestDelete = (url, data) => {
  return new Promise((resolve, reject) => {
    axios.delete(url, data)
      .then(response => {
        resolve(response.data)
      }, err => {
        reject(err)
      })
  })
}
// 文件上传
aspHttps.asp_FileUpload = (url, data) => {
  return new Promise((resolve, reject) => {
    axios.create({
      headers: { 'Content-Type': 'multipart/form-data' }
    }).post(url, data).then(response => {
      resolve(response.data)
    }).catch(err => {
      reject(err)
    })
  })
}
// 文件下载
aspHttps.asp_FileDownload = (url, data, fileName) => {
  axios({
    method: 'post',
    url: url,
    data: data,
    responseType: 'blob'
  }).then(response => {
    if (parseInt(response.status) === 200) {
      const url = window.URL.createObjectURL(new Blob([response.data]))
      const link = document.createElement('a')
      link.style.display = 'none'
      link.href = url
      link.setAttribute('download', fileName)
      document.body.appendChild(link)
      link.click()
    }
  }).catch()
}
// 封装导出方法
aspHttps.asp_ExportGet = (exportUrl) => {
  window.location.href = encodeURI(exportUrl)
}

export default aspHttps