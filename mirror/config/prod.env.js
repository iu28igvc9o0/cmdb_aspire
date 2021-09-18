'use strict'
const testEnvData = {
    NODE_ENV: '"production"',
    PORT: 8080,
    // API_SERVER_URL: 'http://10.1.5.120:5566',
    API_SERVER_URL: 'http://10.12.8.193:5566', // cmdb环境
    PRODUCT_ADDRESS: 'http://10.181.12.146:2122',
    // CMDB_SERVER_URL: 'http://10.12.9.238:2222',
    CMDB_SERVER_URL: 'http://10.12.9.232:2222', // cmdb环境
    FTP_SERVER_URL: 'http://10.12.12.139:59090',
    DEVELOP_HELP_SERVER_URL: 'http://10.12.9.232:4044',
    BIZ_LOG_URL: 'http://10.1.5.120:28302',
    REALM: 'demo_realm',
    // AUTH_SERVER_URL: 'http://10.12.70.40:8180/auth',
    AUTH_SERVER_URL: 'https://10.1.5.125:28180/auth', //cmdb环境
    SSL_REQUIRED: 'external',
    RESOURCE: 'prod_vue',
    PUBLIC_CLIENT: true
}
const prodEnvData = {
    NODE_ENV: '"production"',
    PORT: 8080,
    // API_SERVER_URL: 'http://10.181.12.112:5566',
    // API_SERVER_URL: 'http://10.181.12.121:30003',
    // API_SERVER_URL: 'http://10.1.123.49:8129',
    // API_SERVER_URL: 'http://10.12.8.191:8129',
    // API_SERVER_URL: 'http://10.12.70.39:29090',
    API_SERVER_URL: 'http://10.252.24.150:5566',
    BIZ_LOG_URL: 'http://10.181.12.121:30056',
    REALM: 'demo_realm',
    // CMDB_SERVER_URL: 'http://10.12.9.238:2222',
    CMDB_SERVER_URL: 'http://10.252.24.150:2222',
    FTP_SERVER_URL: 'http://10.12.12.139:59090',
    // AUTH_SERVER_URL: 'http://10.12.70.38:8180/auth',
    //AUTH_SERVER_URL: 'https://117.132.183.206:28080/auth',
    AUTH_SERVER_URL: '/auth/',
    // AUTH_SERVER_URL: 'http://10.1.5.104:8180/auth',
    // AUTH_SERVER_URL: 'http://10.1.5.104:8080/auth',
    BPMX_SERVER_URL: 'http://172.16.20.208:8081/bpmx/platform/console/main.ht',
    X7_SERVER_URL: 'http://172.16.20.221:8085',
    report_url: 'http://172.16.20.230:8075',
    DEVELOP_HELP_SERVER_URL: 'http://10.12.9.232:4044',
    OPERATE_SERVER_URL: 'http://117.132.183.206:8075',
    GRAFANA_SERVER_URL: 'http://172.16.10.33:3002',
    RESOURCE_SERVER_URL: 'http://10.252.24.150:2223',
    SSL_REQUIRED: 'external',
    RESOURCE: 'prod_vue',
    PUBLIC_CLIENT: true
}
// 默认读取 prodEnvData
const envData = process.argv[2] === 'test' ? testEnvData : prodEnvData
module.exports = envData