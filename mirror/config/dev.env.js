'use strict'
const merge = require('webpack-merge')
const prodEnv = require('./prod.env')

module.exports = merge(prodEnv, {
    NODE_ENV: '"development"',
    PORT: 8080,

    //40环境
    API_SERVER_URL: 'http://10.12.70.39:5566', // 开发环境
    API_COMPSITE_URL: 'http://10.12.70.39:5566', // 开发环境 composite地址，用于单点登录跳过zuul配置
    AUTH_SERVER_URL: 'https://10.12.70.40:28080/auth',  // 开发环境

    //64环境
    // API_SERVER_URL: 'http://10.1.203.64:8080', // 测试环境
    // API_COMPSITE_URL: 'http://10.1.203.64:8080', // 测试环境 composite地址，用于单点登录跳过zuul配置
    // AUTH_SERVER_URL: 'http://10.1.203.64:8080/auth',  // 测试环境

    PRODUCT_ADDRESS: 'http://10.181.12.146:2122',
    BIZ_LOG_URL: 'http://10.181.12.121:30056',
    REALM: 'demo_realm',
    CMDB_SERVER_URL: 'http://10.12.70.39:2222',
    FTP_SERVER_URL: 'http://10.12.12.139:59090',
    BPMX_SERVER_URL: 'http://10.12.70.37:8066/front/home',
    X7_SERVER_URL: 'http://10.12.70.37:8066',
    X7_MANAGE_URL: 'http://10.12.70.37:8066',
    GRAFANA_SERVER_URL: 'http://10.12.70.41:3000',
    DEVELOP_HELP_SERVER_URL: 'http://10.12.9.232:4044',
    RESOURCE_SERVER_URL: 'http://localhost:2223',
    SSL_REQUIRED: 'external',
    RESOURCE: 'prod_vue',
    PUBLIC_CLIENT: true,
    OPERATE_SERVER_URL: 'http://117.132.183.206:8075',

    // ��Դ���Ӹ澯���ӣ��������
    ALERT_INFO_SERVER_URL: 'http://10.12.70.39:28130'
})

