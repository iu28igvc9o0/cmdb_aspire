// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import store from './store/index'
import ElementUI from 'element-ui'

import './assets/iconfont/iconfont.css'

// flexible
import 'static/js/flexible'

// filters
import * as filters from './assets/js/utility/rb-filters'
import * as globalUtils from './utils/globalUtils'
import * as sessionDictsData from 'src/assets/js/constant/rb-mirror-mirror.constant.js'
import VueBus from './store/vue-bus'
import 'element-ui/lib/theme-chalk/index.css'
import 'font-awesome/css/font-awesome.css'
import './assets/iconfont/iconfont.js'
import rbHttp from './assets/js/utility/rb-http.factory'
import * as ProdEnv from 'assets/js/config/prod.env'
import * as DevEnv from '../config/dev.env'
import Keycloak from './assets/js/common/keycloak'
import aspHttp from './assets/js/common/asp-http.js'
import cookieService from 'src/services/cookie.service.js'
import { getQueryValue, getQueryToken } from './utils/utils.js'
import { handleVueRouter } from './router/index.js'

import vuescroll from 'vuescroll/dist/vuescroll-native'

import { table, form } from 'asp-smart-layout'
Vue.use(table)
Vue.use(form)
Vue.use(vuescroll, {
    // 在这里设置全局默认配置
    ops: {
        vuescroll: {},
        scrollPanel: {

        },
        rail: {
            background: '#01a99a',
            opacity: 0,
            size: '6px',
            specifyBorderRadius: false,
            gutterOfEnds: null,    // 轨道距 x 和 y 轴两端的距离
            gutterOfSide: '0',   // 距离容器的距离
            keepShow: false,   // 是否即使 bar 不存在的情况下也保持显示
            border: 'none'    // 边框
        },
        bar: {
            keepShow: true,
            size: '6px',
            // hoverStyle: true,
            onlyShowBarOnScroll: false, // 是否只有滚动的时候才显示滚动条
            background: '#0B5BB5',   // 颜色
        }
    },

    name: 'vueScroll' // 在这里自定义组件名字，默认是vueScroll
})

import VueRouter from 'vue-router'
Vue.use(VueRouter)

import cmdbFactory from 'src/services/auto_operation/rb-auto-operation-cmdb.factory.js'
import {
    api
} from 'src/services/api.js'

require('static/js/jtopo-0.4.8-min.js')
Vue.prototype.$api = api
// 注册全局方法集
Vue.prototype.$utils = globalUtils

Vue.prototype.$http = rbHttp
Vue.prototype.$aspHttps = aspHttp  // smartLayout webbas 原型链请求
Vue.use(VueBus) // 中央总线
Vue.use(ElementUI)

Vue.config.productionTip = false
Vue.prototype.rbHttp = rbHttp
if (process.env.NODE_ENV === 'development') {
    init(DevEnv)
} else {
    init(ProdEnv)
}
const originalPush = VueRouter.prototype.push
VueRouter.prototype.push = function push(location) {
    return originalPush.call(this, location).catch(err => err)
}

/**
 * 不同项目菜单 platForm
 * 服务台： it_devops_receptionp_platform
 * IT云运维管理平台： it_devops_manage_platform
 */
async function getMenu(platForm) {
    return rbHttp.sendRequest({
        method: 'GET',
        url: `/v1/sys_menu/menu/${platForm || 'it_devops_manage_platform'}`
    }).then((res) => {
        return res
    }).catch((e) => {
        console.error('getMenu Failed===', e)
        return {}
    })
}

// 登出日志记录
async function saveDeskLogs(loginType) {
    const text = loginType === 'iamcaspticket' ? 'OA单点登录' : '系统登录'
    let params = {
        appType: text,   // 广州4A：OA单点登录/系统登录、系统登出
        opText: text,  // 广州4A：OA单点登录/系统登录、系统登出
        opType: 'Login'  // Login  Logout
    }
    return rbHttp.sendRequest({
        method: 'POST',
        url: '/v1/desk/saveDeskLogs',
        data: params
    })
        .then(data => data)
        .catch((e) => {
            console.error('saveDeskLogs Failed===', e)
            return {}
        })
}

async function init(envConfig) {
    // 第三方平台单点登录
    let tokenKey = getQueryValue('iamcaspticket') ? 'iamcaspticket' : 'token'
    let map = getQueryToken(tokenKey)

    // 不同项目标识
    let pageFrom = getQueryValue('pageFrom') || sessionStorage.getItem('pageFrom')
    let platForm = getQueryValue('platForm') || sessionStorage.getItem('platForm')
    let cityName = getQueryValue('cityName') || sessionStorage.getItem('cityName')

    // 设置项目名称
    if (platForm === 'it_devops_receptionp_platform') {
        document.title = 'IT云服务台'
    } else {
        document.title = window.projectName
    }
    // 引入中移服务台项目样式文件
    if (platForm === 'it_devops_receptionp_platform' && cityName === 'master') {
        import('./assets/css/server-system.scss')
    }

    // 记录跳转页面url
    let currentMenuPath = getQueryValue('toPage')

    if (currentMenuPath) {
        sessionStorage.setItem('currentMenuPath', currentMenuPath)
        sessionStorage.setItem('updatePersonStorage', getQueryValue('updatePerson'))
        sessionStorage.setItem('provinceStorage', getQueryValue('province'))
        sessionStorage.setItem('dateStorage', getQueryValue('date'))
        sessionStorage.setItem('mirrorTokenStorage', getQueryValue('mirrorToken'))
        sessionStorage.setItem('usernameScreen', getQueryValue('userName'))
        sessionStorage.setItem('department1Screen', getQueryValue('departmentOne'))
        sessionStorage.setItem('department2Screen', getQueryValue('departmentTwo'))
        sessionStorage.setItem('deviceIP', getQueryValue('ip'))
        sessionStorage.setItem('deviceIdByIp', getQueryValue('resourceId'))
    }

    sessionStorage.setItem('sessionDictsData', JSON.stringify(sessionDictsData))  // 缓存静态字典表数据
    sessionStorage.setItem('BPMX_SERVER_URL', envConfig['BPMX_SERVER_URL'])
    sessionStorage.setItem('OPERATE_SERVER_URL', envConfig['OPERATE_SERVER_URL'])
    sessionStorage.setItem('X7_SERVER_URL', envConfig['X7_SERVER_URL'])
    sessionStorage.setItem('X7_MANAGE_URL', envConfig['X7_MANAGE_URL'])
    sessionStorage.setItem('GRAFANA_SERVER_URL', envConfig['GRAFANA_SERVER_URL'])
    sessionStorage.setItem('platForm', platForm ? platForm : '')
    sessionStorage.setItem('cityName', cityName ? cityName : '')
    sessionStorage.setItem('pageFrom', pageFrom ? pageFrom : '')
    Vue.prototype.env = envConfig

    var keycloak = Keycloak({
        'realm': envConfig['REALM'],
        'url': envConfig['AUTH_SERVER_URL'],
        'ssl-required': envConfig['SSL_REQUIRED'],
        'clientId': envConfig['RESOURCE'],
        'public-client': envConfig['PUBLIC_CLIENT']
    })
    keycloak.onTokenExpired = function () {
        const redirectUri = window.location.protocol + '//' + window.location.hostname + (window.location.port ? ':' + window.location.port : '')
        const logoutUrl = keycloak.createLogoutUrl({
            redirectUri: redirectUri
        })
        window.location.href = logoutUrl
    }

    if (map.code !== '0000') {
        let type = cookieService.getCookie('sso_t_name')
        if (type && type !== '') {
            map.code = '0000'
            map.type = type
            map.token = cookieService.getCookie('sso_t_value')
        }
    }
    if (map.code === '0000') {
        // 临时添加报文头
        keycloak.initConfig()
        await rbHttp.sendRequest({
            method: 'GET',
            url: '/sso/token',
            addNamespace: true,
            params: map
        }).then((res) => {
            if (res.code === '0000') {
                sessionStorage.setItem('namespace', res.userName)
                sessionStorage.setItem('username', res.userName)
                sessionStorage.setItem('isAdmin', res.isAdmin)
                cookieService.setCookie('sso_t_name', map.type)
                cookieService.setCookie('sso_t_value', map.token)
                keycloak.setTokens(res.access_token, res.refresh_token, res.id_token)
                initUser(keycloak, platForm, map.type)
            } else {
                cookieService.setCookie('sso_t_name', '')
                cookieService.setCookie('sso_t_value', '')
                keycloak.onTokenExpired()
            }
        }).catch(() => {
            cookieService.setCookie('sso_t_name', '')
            cookieService.setCookie('sso_t_value', '')
            keycloak.onTokenExpired()
        })
    } else {
        keycloak.init({
            onLoad: 'login-required'
        }).success(function () {
            initUser(keycloak, platForm)
        })
    }
}

async function initUser(keycloak, platForm, loginType) {

    keycloak.loadUserInfo().success(async () => {
        const curToken = sessionStorage.getItem('token')
        sessionStorage.setItem('token', keycloak.token)
        sessionStorage.setItem('mobile', keycloak.userInfo.mobile)
        sessionStorage.setItem('username', keycloak.userInfo.userName)
        sessionStorage.setItem('isAdmin', keycloak.userInfo.employeeType === 'admin')
        let Base64 = require('js-base64').Base64
        sessionStorage.setItem('mirror', Base64.encode(keycloak.userInfo.userName + '||name'))
        sessionStorage.setItem('usermirror', Base64.encode(keycloak.userInfo.userName))
        let menus = await getMenu(platForm)
        let router
        try {
            router = handleVueRouter(menus, VueRouter)
        } catch (error) {
            console.error('handleVueRouter==', error)
        }

        const fields = keycloak.userInfo.employeeType.split('.')
        if (fields[0] === 'user' && fields.length === 2) {
            sessionStorage.setItem('namespace', fields[1])
        } else {
            sessionStorage.setItem('namespace', keycloak.userInfo.userName)
        }
        // 登录成功后，调用登录日志接口
        if (!curToken) {
            await saveDeskLogs(loginType)
        }
        Vue.prototype.keycloak = keycloak
        // 获取自动化是否关联cmdb
        cmdbFactory.getAgentHostInfoLoadSource().then((res) => {
            sessionStorage.setItem('ops_agent_loadfrom', res.biz_data)
        }).catch(error => {
            console.error(error)
            sessionStorage.setItem('ops_agent_loadfrom', 'local')
        })
        // 得到菜单操作权限
        if (keycloak.userInfo.employeeType !== 'admin') {
            rbHttp.sendRequest({
                method: 'GET',
                url: `/v1/roles/${sessionStorage.getItem('namespace')}/getRoleByUserName/${sessionStorage.getItem('username')}`
            }).then((res) => {
                let obj = {}
                res.forEach(function (item) {
                    Object.assign(obj, item.permissions[0] && item.permissions[0].resource)
                })
                let target = []
                for (let key in obj) {
                    target.push(key)
                }
                menus.categories.forEach(function (item) {
                    let deleteFlag0
                    if (target.includes(item.id)) {
                        deleteFlag0 = false
                    } else {
                        deleteFlag0 = true
                    }
                    if (item.children && item.children.length > 0) {
                        item.children.forEach((subitem) => {
                            let deleteFlag
                            if (target.includes(subitem.id)) {
                                deleteFlag0 = false
                                deleteFlag = false
                            } else {
                                deleteFlag = true
                            }
                            if (subitem.children && subitem.children.length > 0) {
                                subitem.children.forEach((threeItem) => {
                                    if (!target.includes(threeItem.id)) {
                                        subitem.children = subitem.children.del(threeItem)
                                    } else {
                                        deleteFlag0 = false
                                        deleteFlag = false
                                    }
                                })
                            }
                            if (deleteFlag) {
                                item.children = item.children.del(subitem)
                            }
                        })
                    }
                    if (deleteFlag0) {
                        menus.categories = menus.categories.del(item)
                    }
                })
                store.commit('modifyOperResource', menus)
                // 得到数据字典
                rbHttp.sendRequest({
                    method: 'GET',
                    url: '/v1/dict/listAll'
                }).then((res) => {
                    let obj = JSON.parse(res.result)
                    sessionStorage.setItem('sensitiveLabel', JSON.stringify(obj.sensitive_label))
                    store.commit('modifyDictObj', obj)
                    // 如果后台配置不需要短信认证则直接设置已校验通过
                    if (obj && obj.login_valid && obj.login_valid.need_sms_captcha.name && obj.login_valid.need_sms_captcha.name === 'false') {
                        sessionStorage.setItem('logValid', true)
                    }

                    new Vue({
                        el: '#app',
                        router: router,
                        store: store,
                        components: {
                            App
                        },
                        template: '<App/>'
                    })
                })
            })
        } else {
            store.commit('modifyOperResource', menus)
            sessionStorage.setItem('logValid', true)
            // 得到数据字典
            rbHttp.sendRequest({
                method: 'GET',
                url: '/v1/dict/listAll'
            }).then((res) => {
                let obj = JSON.parse(res.result)
                sessionStorage.setItem('sensitiveLabel', JSON.stringify(obj.sensitive_label))
                store.commit('modifyDictObj', obj)
                // 如果后台配置不需要短信认证则直接设置已校验通过

                new Vue({
                    el: '#app',
                    router,
                    store: store,
                    components: {
                        App
                    },
                    template: '<App/>'
                })
            })
        }
    }).error(() => {
        cookieService.setCookie('sso_t_name', '')
        cookieService.setCookie('sso_t_value', '')
        keycloak.onTokenExpired()
    })

}
// 注册全局过滤器
Object.keys(filters).forEach(key => {
    Vue.filter(key, filters[key])
})
