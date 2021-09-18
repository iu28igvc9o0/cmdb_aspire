export const MIRROR_TYPE_MAP = {
    '1': '硬件',
    '2': '网络',
    '3': '主机操作系统',
    '4': '应用'
}

export const MIRROR_FUN_TYPE = {
    '1': '监控指标项',
    '2': '巡检指标项'
}

export const TASK_TYPE_MAP = {
    '0': '全部',
    '1': '手动',
    '2': '自动'
}

export const TASK_TYPE_MAP1 = {
    '1': '手动',
    '2': '自动'
}

export const CYCLE_TYPE_MAP = {
    'MIN': '分钟',
    'HOUR': '小时',
    'MON': '月',
    'WEEK': '周',
    'DAY': '日',
    'DEFINE': '自定义'
}

export const TASK_STATUS = {
    'RUNNING': '运行中',
    'FINNISHED': '已完成'
}

export const ZHOU = {
    '1': '周日',
    '2': '周一',
    '3': '周二',
    '4': '周三',
    '5': '周四',
    '6': '周五',
    '7': '周六'
}

export const EXPRESSION = {
    '=': 0,
    '>': 0,
    '<': 0,
    '!=': 1,
    'In': 1
}

export const ZBXTYPE = {
    '0': '浮点数',
    '1': '字符串',
    '2': '日志',
    '3': '整数',
    '4': '文本'
}

// 告警
export const ALERTLEVEL = {
    'name': 'ALERTLEVEL',
    // '1': '提示',
    '2': '低',
    '3': '中',
    '4': '高',
    '5': '重大'
}
export const CONTENT_TYPE = {
    'name': 'CONTENT_TYPE',
    // '1': '提示',
    1: 'sh',
    2: 'bat',
    3: 'python'
}
export const ORDERSTATUS = {
    'name': 'ORDERSTATUS',
    '1': '未派单',
    '2': '处理中',
    '3': '已完成'
}

export const OBJECTTYPE = {
    'name': 'OBJECTTYPE',
    '1': '设备',
    '2': '业务系统'
}

export const MONTYPE = {
    'name': 'MONTYPE',
    '1': '设备',
    '2': '业务系统',
    '3': 'Agent设备'
}

export const SYSTYPE = {
    'name': 'SYSTYPE',
    'ZABBIX': 'ZABBIX',
    '普罗米修斯': 'PROMETHEUS',
    '主题': 'THEME',
    'MIRROR': 'MIRROR'
}

// 资源管理
export const RESOURCESTATUS = {
    'name': 'RESOURCESTATUS',
    '-1': '全部',
    '0': '已关闭',
    '1': '已预估',
    '2': '建设中'
}
// 通知方式
export const REPORTTYPE = {
    'name': 'REPORTTYPE',
    '0': '短信',
    '1': '邮件',
    '2': '全部',
    '3': '其他途径'
}

// 通知发送状态
export const REPORTSTATUS = {
    'name': 'REPORTSTATUS',
    '0': '失败',
    '1': '成功'
}

// 转派状态
export const TRANSSTATUS = {
    'name': 'TRANSSTATUS',
    '0': '失败',
    '1': '成功'
}

// 操作类型 0-转派, 1-确认,2-派发工单, 3-清除, 4-通知, 5-过滤, 6-工程, 7-维护模式'
export const OPERATION_TYPE = {
    'name': 'OPERATION_TYPE',
    '0': '转派',
    '1': '确认',
    '2': '派发工单',
    '3': '清除',
    '4': '通知',
    '5': '过滤',
    '6': '工程',
    '7': '维护模式'
}

// 工单类型
export const ORDERTYPE = {
    'name': 'ORDERTYPE',
    '1': '告警工单',
    '2': '故障工单',
    '3': '维保工单'
}

export const ISOLATE_STATUS = {
    'name': 'ISOLATE_STATUS',
    '1': '启用',
    '0': '停用'
}

// 请求分类
export const REQUEST_TYPE = [
    {
        label: '服务咨询',
        value: '1'
    },
    {
        label: '技术支持',
        value: '2'
    },
    {
        label: '问题上报',
        value: '3'
    }
]
// 请求子类 主机，存储，网络，数据库，中间件，机房配套，OS，其他平台，其他,安全，云管平台，4A/VPN
export const REQUEST_SUB_TYPE = [
    {
        label: '主机',
        value: '1'
    },
    {
        label: '存储',
        value: '2'
    },
    {
        label: '网络',
        value: '3'
    },
    {
        label: '数据库',
        value: '4'
    },
    {
        label: '中间件',
        value: '5'
    },
    {
        label: '机房配套',
        value: '6'
    },
    {
        label: 'OS',
        value: '7'
    },
    {
        label: '其他平台',
        value: '8'
    },
    {
        label: '安全',
        value: '9'
    },
    {
        label: '云管平台',
        value: '10'
    },
    {
        label: '4A/VPN',
        value: '11'
    },
]
// 请求来源
export const REQUEST_SOURCE = [
    {
        label: '自服务',
        value: '1'
    },
    {
        label: '云鸽在线',
        value: '2'
    },
    {
        label: '微信',
        value: '3'
    },
    {
        label: '电话',
        value: '4'
    },
    {
        label: 'APP',
        value: '5'
    }
]
// 故障列表动态列
export const FAULT_TABLE_HEAD = [
    {
        columnCode: 'id',
        columnName: 'id',
        isSelected: true
    }, {
        columnCode: 'faultId',
        columnName: '故障id',
        isSelected: true
    }, {
        columnCode: 'faultReportUser',
        columnName: '上报人',
        isSelected: true
    }, {
        columnCode: 'faultReporMobile',
        columnName: '上报人电话',
        isSelected: true
    }, {
        columnCode: 'faultReportEmail',
        columnName: '上报人邮箱',
        isSelected: true
    }, {
        columnCode: 'faultReportBizsys',
        columnName: '上报人业务组',
        isSelected: true
    }, {
        columnCode: 'faultReportTime',
        columnName: '上报时间',
        isSelected: true
    }, {
        columnCode: 'faultReporMobile',
        columnName: '上报人电话',
        isSelected: true
    }, {
        columnCode: 'faultReportEmail',
        columnName: '上报人邮箱',
        isSelected: true
    }, {
        columnCode: 'faultReportBizsys',
        columnName: '上报人业务组',
        isSelected: true
    }, {
        columnCode: 'faultReportTime',
        columnName: '上报时间',
        isSelected: true
    }, {
        columnCode: 'faultReporMobile',
        columnName: '上报人电话',
        isSelected: true
    }, {
        columnCode: 'faultReportEmail',
        columnName: '上报人邮箱',
        isSelected: true
    }, {
        columnCode: 'faultReportBizsys',
        columnName: '上报人业务组',
        isSelected: true
    }, {
        columnCode: 'faultReportTime',
        columnName: '上报时间',
        isSelected: true
    }, {
        columnCode: 'faultHappenTime',
        columnName: '故障发生时间',
        isSelected: true
    }, {
        columnCode: 'faultReportTimely',
        columnName: '上报是否及时',
        isSelected: true
    }, {
        columnCode: 'faultIdcType',
        columnName: '资源池',
        isSelected: true
    }, {
        columnCode: 'faultOrderId',
        columnName: '上报工单id',
        isSelected: true
    }, {
        columnCode: 'faultContent',
        columnName: '上报内容简述',
        isSelected: true
    }, {
        columnCode: 'faultRegtime',
        columnName: '接收故障时间',
        isSelected: true
    }, {
        columnCode: 'reportTitle',
        columnName: '故障报告名称',
        isSelected: true
    }, {
        columnCode: 'reportUser',
        columnName: '报告编写人',
        isSelected: true
    }, {
        columnCode: 'reportMobile',
        columnName: '编写人电话',
        isSelected: true
    }, {
        columnCode: 'reportEmail',
        columnName: '编写人邮箱',
        isSelected: true
    }, {
        columnCode: 'reportBizsys',
        columnName: '编写人业务组',
        isSelected: true
    }, {
        columnCode: 'reportWnId',
        columnName: '全网id',
        isSelected: true
    }, {
        columnCode: 'reportOrderId',
        columnName: '报告工单id',
        isSelected: true
    }, {
        columnCode: 'reportCreateTime',
        columnName: '事件单下发时间',
        isSelected: true
    }, {
        columnCode: 'reportPlainFinish',
        columnName: '事件要求完成时间',
        isSelected: true
    }, {
        columnCode: 'reportFinishTime',
        columnName: '事件实际完成时间',
        isSelected: true
    }, {
        columnCode: 'reportTimely',
        columnName: '故障上报及时性',
        isSelected: true
    }, {
        columnCode: 'reportPlatformRecoverytime',
        columnName: '平台恢复时间',
        isSelected: true
    }, {
        columnCode: 'reportPlatformRecoveryhours',
        columnName: '平台恢复时长',
        isSelected: true
    }, {
        columnCode: 'reportBizsysRecoverytime',
        columnName: '业务恢复时间',
        isSelected: true
    }, {
        columnCode: 'reportBizsysRecoveryhours',
        columnName: '业务恢复时长',
        isSelected: true
    }, {
        columnCode: 'reportAffectDescribe',
        columnName: '故障影响描述',
        isSelected: true
    }, {
        columnCode: 'reportType',
        columnName: '故障分类',
        isSelected: true
    }, {
        columnCode: 'reportResson',
        columnName: '故障原因描述',
        isSelected: true
    }, {
        columnCode: 'reportProducer',
        columnName: '故障厂家',
        isSelected: true
    }, {
        columnCode: 'reportNature',
        columnName: '事件性质',
        isSelected: true
    }, {
        columnCode: 'reportDeductPoints',
        columnName: '扣分',
        isSelected: true
    }, {
        columnCode: 'reportEnclosure',
        columnName: '故障报告附件',
        isSelected: true
    }, {
        columnCode: 'reportFollowPlan',
        columnName: '后续计划',
        isSelected: true
    }, {
        columnCode: 'reportFollowPlanExplain',
        columnName: '后续计划说明',
        isSelected: true
    }, {
        columnCode: 'reportRemark',
        columnName: '其它附注信息',
        isSelected: true
    }]