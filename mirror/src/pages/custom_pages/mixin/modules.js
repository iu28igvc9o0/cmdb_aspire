// 定制页模块(目前是静态配置)
export default [
    // 定制化综合首页模块
    {
        code: 'index',
        name: '综合首页',
        pageType: 'dark',
        data: [
            {
                name: '行',
                type: 'row',
                childrenHasTitle: true,
                children: [
                    {
                        name: 'resource-capacity-overview',
                        type: 'module',
                        minWidth: '74.5%',
                        label: '资源容量总览',
                    },
                    {
                        name: 'order-chart-overview',
                        type: 'module',
                        minWidth: '24.5%',
                        maxWidth: '24.5%',
                        label: '工单总览',
                    }
                ]
            },
            {
                name: '告警总览',
                type: 'row',
                childrenHasTitle: false,
                children: [
                    {
                        name: 'alert-chart-my',
                        type: 'module',
                        minWidth: '24%',
                        maxWidth: '49.75%',
                        label: '我的告警'
                    },
                    {
                        name: 'alert-today-overview',
                        type: 'module',
                        minWidth: '24%',
                        maxWidth: '49.75%',
                        label: '当日告警总览'
                    },
                    {
                        name: 'alert-device-class',
                        type: 'module',
                        minWidth: '24%',
                        maxWidth: '49.75%',
                        label: '告警设备分类'
                    },
                    {
                        name: 'alert-new-hot',
                        type: 'module',
                        minWidth: '24%',
                        maxWidth: '49.75%',
                        label: '最新热点告警'
                    },
                ]
            },
            {
                name: '资源运营总览',
                type: 'row',
                childrenHasTitle: false,
                children: [
                    {
                        name: 'resource-use-rate',
                        type: 'module',
                        minWidth: '49.75%',
                        label: '资源利用率'
                    },
                    {
                        name: 'resource-bizsys-rate-top',
                        type: 'module',
                        minWidth: '49.75%',
                        label: '业务系统资源利用率Top10'
                    },
                    {
                        name: 'resource-use-tendency',
                        type: 'module',
                        minWidth: '100%',
                        label: '资源利用率趋势'
                    }
                ]
            }
        ]
    },
    // 定制化监控首页模块
    {
        code: 'mirrorAlertIndex',
        name: '监控首页',
        pageType: 'dark',
        data: [
            {
                name: '行',
                type: 'row',
                childrenHasTitle: true,
                children: [
                    {
                        name: 'resource-capacity-overview',
                        type: 'module',
                        minWidth: '74.5%',
                        label: '资源容量总览-监控首页',
                    },
                    {
                        name: 'order-chart-overview',
                        type: 'module',
                        minWidth: '24.5%',
                        maxWidth: '24.5%',
                        label: '工单总览-监控首页',
                    }
                ]
            },
            {
                name: '告警总览-监控首页',
                type: 'row',
                childrenHasTitle: false,
                children: [
                    {
                        name: 'alert-chart-my',
                        type: 'module',
                        minWidth: '24%',
                        maxWidth: '49.75%',
                        label: '我的告警'
                    },
                    {
                        name: 'alert-today-overview',
                        type: 'module',
                        minWidth: '24%',
                        maxWidth: '49.75%',
                        label: '当日告警总览'
                    },
                    {
                        name: 'alert-device-class',
                        type: 'module',
                        minWidth: '24%',
                        maxWidth: '49.75%',
                        label: '告警设备分类'
                    },
                    {
                        name: 'alert-new-hot',
                        type: 'module',
                        minWidth: '24%',
                        maxWidth: '49.75%',
                        label: '最新热点告警'
                    },
                ]
            },
            {
                name: '资源运营总览',
                type: 'row',
                childrenHasTitle: false,
                children: [
                    {
                        name: 'resource-use-rate',
                        type: 'module',
                        minWidth: '49.75%',
                        label: '资源利用率'
                    },
                    {
                        name: 'resource-bizsys-rate-top',
                        type: 'module',
                        minWidth: '49.75%',
                        label: '业务系统资源利用率Top10'
                    },
                    {
                        name: 'resource-use-tendency',
                        type: 'module',
                        minWidth: '100%',
                        label: '资源利用率趋势'
                    }
                ]
            }
        ],
    },
    // 定制化服务台值班人员首页模块
    {
        code: 'bpmDutyIndex',
        name: '值班人员首页',
        pageType: 'light',
        data: [
            // 服务台值班人员首页
            {
                name: '行',
                type: 'row',
                childrenHasTitle: true,
                minHeight: '36px',
                children: [
                    {
                        name: 'bpm-announcement',
                        type: 'module',
                        minWidth: '100%',
                        height: '36px',
                        label: '公告'
                    }
                ]
            },
            {
                name: '行',
                type: 'row',
                childrenHasTitle: true,
                children: [
                    {
                        name: 'bpm-quick-search',
                        thumbnail: 'bpm-order-types',
                        type: 'module',
                        minWidth: '24%',
                        maxWidth: '24.75%',
                        height: '680px',
                        label: '快速搜索'
                    },
                    {
                        name: 'bpm-new-order',
                        thumbnail: 'bpm-my-orders',
                        type: 'module',
                        minWidth: '24%',
                        maxWidth: '24.75%',
                        height: '680px',
                        label: '创建服务事件工单'
                    },
                    {
                        name: 'bpm-orders-statistics',
                        thumbnail: 'bpm-my-orders',
                        type: 'module',
                        minWidth: '49.75%',
                        height: '680px',
                        label: '今日服务事件工单统计',
                    }
                ]
            },
            {
                name: '行',
                type: 'row',
                childrenHasTitle: true,
                children: [
                    {
                        name: 'bpm-order-types01',
                        type: 'module',
                        minWidth: '12%',
                        maxWidth: '12.75%',
                        label: '工单起草'
                    },
                    {
                        name: 'bpm-reminder-orders',
                        thumbnail: 'bpm-my-orders',
                        type: 'module',
                        minWidth: '49.75%',
                        label: '催办工单',
                    }
                ]
            },
            {
                name: '行',
                type: 'row',
                childrenHasTitle: true,
                children: [
                    {
                        name: 'bpm-my-orders01',
                        thumbnail: 'bpm-my-orders',
                        type: 'module',
                        minWidth: '49.75%',
                        label: '我的工单',
                    }
                ]
            },

            // 通用首页
            // {
            //     name: '行',
            //     type: 'row',
            //     childrenHasTitle: true,
            //     children: [
            //         {
            //             name: 'bpm-order-types',
            //             type: 'module',
            //             minWidth: '24%',
            //             maxWidth: '24.75%',
            //             label: '工单起草'
            //         },
            //         {
            //             name: 'bpm-todo-orders',
            //             type: 'module',
            //             minWidth: '49.75%',
            //             label: '待办工单',
            //         }
            //     ]
            // },
            // {
            //     name: '行',
            //     type: 'row',
            //     childrenHasTitle: true,
            //     children: [
            //         {
            //             name: 'bpm-daily-use',
            //             type: 'module',
            //             minWidth: '24%',
            //             maxWidth: '24.75%',
            //             label: '常用操作'
            //         },
            //         {
            //             name: 'bpm-my-orders',
            //             type: 'module',
            //             minWidth: '49.75%',
            //             label: '我发起的工单',
            //         },
            //     ]
            // },
        ],
    },
    // 定制化服务台通用首页模块
    {
        code: 'bpmIndex',
        name: '通用首页',
        pageType: 'light',
        data: [
            // 通用首页
            {
                name: '行',
                type: 'row',
                childrenHasTitle: true,
                children: [
                    {
                        name: 'bpm-order-types',
                        type: 'module',
                        minWidth: '24%',
                        maxWidth: '24.75%',
                        label: '工单起草'
                    },
                    {
                        name: 'bpm-todo-orders',
                        type: 'module',
                        minWidth: '49.75%',
                        label: '待办工单',
                    }
                ]
            },
            {
                name: '行',
                type: 'row',
                childrenHasTitle: true,
                children: [
                    {
                        name: 'bpm-daily-use',
                        type: 'module',
                        minWidth: '24%',
                        maxWidth: '24.75%',
                        label: '常用操作'
                    },
                    {
                        name: 'bpm-my-orders',
                        type: 'module',
                        minWidth: '49.75%',
                        label: '我发起的工单',
                    },
                ]
            },
        ],
    },
    // 定制化深圳统一门户模块
    {
        code: 'bpmIndexByShenZhen',
        name: '统一门户',
        pageType: 'light',
        data: [
            {
                name: '行',
                type: 'row',
                childrenHasTitle: true,
                children: [
                    {
                        name: 'bpm-draftOrders-dailyUse',
                        thumbnail: 'bpm-order-types',
                        type: 'module',
                        minWidth: '24%',
                        maxWidth: '24.75%',
                        height: '680px',
                        label: '工单起草-常用操作(深圳)'
                    },
                    {
                        name: 'bpm-myOrders-announcement',
                        thumbnail: 'bpm-shenzhen-myOrders',
                        type: 'module',
                        minWidth: '49.75%',
                        height: '680px',
                        label: '我的工单-公告(深圳)'
                    },
                    {
                        name: 'bpm-effectStatistics',
                        type: 'module',
                        minWidth: '49.75%',
                        label: '效能展示(深圳)'
                    },
                    {
                        name: 'bpm-orderCheck',
                        type: 'module',
                        minWidth: '49.75%',
                        label: '工单考核(深圳)'
                    },
                    {
                        name: 'bpm-orderTop',
                        type: 'module',
                        minWidth: '49.75%',
                        label: '工单TOP(深圳)'
                    },
                    {
                        name: 'bpm-orderCloseRate',
                        type: 'module',
                        minWidth: '24%',
                        maxWidth: '24.75%',
                        label: '部门闭单率(深圳)'
                    },
                    {
                        name: 'bpm-alertStatistics',
                        type: 'module',
                        minWidth: '24%',
                        maxWidth: '24.75%',
                        label: '告警统计(深圳)'
                    },
                    {
                        name: 'bpm-shenzhen-quickSearch',
                        thumbnail: 'bpm-order-types',
                        type: 'module',
                        minWidth: '24%',
                        maxWidth: '24.75%',
                        height: '680px',
                        label: '快速搜索(深圳)'
                    },
                ]
            },
        ],
    },

]
