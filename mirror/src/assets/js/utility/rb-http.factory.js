import axios from 'axios'
import Message from 'element-ui/packages/message'
import cookieService from 'src/services/cookie.service.js'
import common from './common.js'
const FormData = window.FormData


// let pending = [] //声明一个数组用于存储每个请求的取消函数和axios标识
// let cancelToken = axios.CancelToken
// let removePending = (config) => {
//     for (let i in pending) {
//         if (pending[i].url === axios.defaults.baseURL + config.url) { //在当前请求在数组中存在时执行取消函数
//             pending[i].fn() //执行取消操作
//             //pending.splice(i, 1); 根据具体情况决定是否在这里就把pending去掉
//         }
//     }
// }
// 超时时间

axios.defaults.timeout = 50000
// http请求拦截器
// let loadinginstace
axios.interceptors.request.use(config => {
    const token = sessionStorage.getItem('token')
    const namespace = sessionStorage.getItem('namespace')
    const project = cookieService.getCookie('project')
    // 在拦截器中处理请求头，兼容smartLayout请求
    // Most rubick ajax calls are subject to namespace
    config.params = config.params || {}

    if (!config.params.namespace && config.addNamespace !== false) {
        config.params.namespace = namespace
    }

    if (project) {
        config.params.project_name = project
    }

    // // combine params & data and assign to data
    if (!(config.data instanceof FormData) && !(config.data instanceof Array)) {
        config.data = Object.assign({}, config.data, config.params)
    }

    config.headers = config.headers || {}
    config.headers['RUBICK-AJAX-REQUEST'] = true
    config.headers['X-REQUESTED-WITH'] = 'XMLHttpRequest'
    config.headers['Content-Type'] = 'application/json;charset=UTF-8'
    config.headers.head_orgAccount = sessionStorage.getItem('namespace')
    config.headers.head_userName = sessionStorage.getItem('username')
    config.headers.head_isAdmin = sessionStorage.getItem('isAdmin')
    config.headers.Accept = 'application/json, text/plain, */*'
    config.headers.Authorization = 'Bearer ' + token

    if (config.useFormData) {
        if (!(config.data instanceof FormData)) {
            config.data = this._buildFormData(config.data)
        }
        config.headers['Content-Type'] = 'multipart/form-data'
    }
    if (config.binary) {
        config.responseType = 'arraybuffer'
    }
    if (config.Authorization) {
        config.headers.Authorization = config.Authorization
    }

    // return config
    // removePending(config)
    // //在一个axios发送前执行一下判定操作，在removePending中执行取消操作
    // config.cancelToken = new cancelToken(function executor(cancelFn){
    //     //本次axios请求的配置添加cancelToken
    //   pending.push({
    //       url: axios.defaults.baseURL + config.url,
    //       fn: cancelFn
    //   })
    //   // 将本次的url添加到pending中，因此对于某个url第一次发起的请求不会被取消，因为还没有配置取消函数
    // })
    return config
}, error => {
    // loadinginstace.close()
    Message.error({
        message: '加载超时'
    })
    return Promise.reject(error)
})
// http响应拦截器
axios.interceptors.response.use(data => {
    // removePending(data.config); //在一个axios响应后再执行一下取消操作，把已经完成的请求从pending中移除
    return data
}, error => {
    // error.response.data
    error.response.config && error.response.config.client === 'rbHttp' && common.ajaxGenericOnError(error.response)
    return Promise.reject(error.response)
})

export default class rbHttp {
    /**
     * Simply a migration from legacy ajax.sendRequest method, but with new ES6 signature
     *
     * @param method HTTP method (e.g. 'GET', 'POST', etc)
     * @param url Absolute or relative URL of the resource that is being requested.
     * @param params Map of strings or objects which will be serialized with the paramSerializer and appended as GET parameters.
     * @param data Data to be sent as the request message data.
     * @param timeout timeout in milliseconds, or promise that should abort the request when resolved.
     * @param binary Whether or not to expect the response is binary
     * @param cache A boolean value or object created with $cacheFactory to enable or disable caching of the HTTP response.
     *        Typically, use this only if you know this value will not be updated.
     * @param useFormData Indicates that the data passed in should be transformed to FormData
     * @param addNamespace add namespace to params by default
     */
    static sendRequest({
        method = 'GET',
        url,
        data,
        params = {},
        timeout = 1000 * 300,
        binary = false,
        cache = false,
        useFormData = false,
        addNamespace = true,
        Authorization,
        responseType
    }) {

        const config = {
            method,
            url,
            timeout,
            cache,
            data,
            params,
            xsrfHeaderName: 'X-CSRFToken',
            xsrfCookieName: '294f62ecd0',
            client: 'rbHttp',
            responseType,
            useFormData,
            Authorization,
            binary,
            addNamespace
        }
        return axios(config).then(function (response) {
            return response.data
        })
    }

    static _buildFormData(data) {
        const formData = new FormData()
        for (const key in data) {
            const value = data[key]
            if (value instanceof Array) {
                for (let i = 0; i < value.length; i++) {
                    formData.append(key, value[i])
                }
            } else {
                formData.append(key, value)
            }
        }
        return formData
    }

    static getList(url) {
        return new Promise((resolve, reject) => {
            axios.get(url).then(response => {
                resolve(response.data)
            }).catch((error) => {
                reject(error)
            })
        })
    }

}
