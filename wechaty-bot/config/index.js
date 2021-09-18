/*
 * @Author: isboyjc
 * @Date: 2020-02-18 16:13:15
 * @LastEditors: isboyjc
 * @LastEditTime: 2020-03-01 02:14:53
 * @Description: 配置项
 */

module.exports = {
    appName: 'wechaty-bot',
    memorypath: '/opt/data/wechaty/robot',
    log: {
        replaceConsole: true,
        appenders: {
            console: { type: 'console' },
            error: {
                type: 'dateFile', //日志类型
                category: 'errLogger', //日志名称
                filename: '/opt/data/wechaty/logs/err', //日志输出位置，当目录文件或文件夹不存在时自动创建
                pattern: 'yyyy-MM-dd.log',
                alwaysIncludePattern: true,
                maxLogSize: 104800, // 文件最大存储空间
                backups: 100,//当文件内容超过文件存储空间时，备份文件的数量
                layout: { type: 'json', separator: ',' }
            },
            response: {
                type: 'dateFile',
                category: 'resLogger',
                filename: '/opt/data/wechaty/logs/log',
                pattern: 'yyyy-MM-dd.log', //日志输出模式
                alwaysIncludePattern: true,
                maxLogSize: 104800,
                backups: 100,
                layout: { type: 'json', separator: ',' },
            }
        },
        categories: {
            error: {
                appenders: ['error'],
                level: 'error'
            },
            response: {
                appenders: ['response'],
                level: 'info'
            },
            default: {
                appenders: ['response'],
                level: 'info'
            }
        }
    },
    //   mysql: {
    //     host : '127.0.0.1',
    //     user : 'wechaty',
    //     password : '9O0p-[=]',
    //     database: 'wechaty'
    //   },
    // mysql: {
    //     host: '10.12.70.40',
    //     user: 'root',
    //     password: '1234@qwer',
    //     database: 'wechaty' 
    // },
    mysql: {
        host: '10.12.66.107',
        user: 'wechaty',
        password: '9O0p-[=]',
        database: 'wechaty'
    },
    // puppet_padplus Token
    // token: "puppet_padplus_402d25b2efe05e79", // 杨凡token 有效期到7月25日
    token: "puppet_padplus_b5470b8bafe77956", // 韦鉴昌token 有效期到7月30日
    // 机器人名字
    name: "测试机器人",
    // 房间/群聊
    room: {
        // 大德管理群
        adminRoom: {
            name: "微信转工单",
            roomId: "22808783294@chatroom",
        },
        // 租户管理群
        adminRoom: {
            name: "微信机器人内部管理群",
            roomId: "22146294157@chatroom",
        },
        // 埚牛管理群
        // adminRoom: {
        //     name: "微信机器人-管理群",
        //     roomId: "17806922917@chatroom",
        // },
        // 加入房间回复
        roomJoinReply: `\n 你好，欢迎你的加入，请自觉遵守群规则，文明交流，最后，请向大家介绍你自己！ \n\n Hello, welcome to join, please consciously abide by the group rules, civilized communication, finally, please introduce yourself to everyone！😊`
    },
    // 私人
    personal: {
        // 好友验证自动通过关键字
        addFriendKeywords: ["加群", "前端"],
        // 是否开启加群
        addRoom: true
    }
}
