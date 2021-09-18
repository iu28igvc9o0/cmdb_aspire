export const alert_email_col = [{
    name: '当前监控值',
    code: 'cur_moni_value'
}, {
    name: '资源池',
    code: 'idc_type'
}, {
    name: '业务系统',
    code: 'biz_sys'
}, {
    name: '设备类型',
    code: 'device_class'
}, {
    name: '机房',
    code: 'source_room'
}, {
    name: '告警类型',
    code: 'object_type'
}]

export const alert_level = [
    {
        label: '全部',
        value: '',
        id: ''
    },
    {
        label: '低',
        value: '低',
        id: '2'
    }, {
        label: '中',
        value: '中',
        id: '3'
    }, {
        label: '高',
        value: '高',
        id: '4'
    }, {
        label: '重大',
        value: '重大',
        id: '5'
    }
]

export const alert_level_obj = {
    '1': {
        name: '未定义',
        status: 'low',
        color: 'blue'
    },
    '2': {
        name: '低',
        status: 'low',
        color: 'blue'
    },
    '3': {
        name: '中',
        status: 'mid',
        color: 'yellow'
    },
    '4': {
        name: '高',
        status: 'high',
        color: 'orange'
    },
    '5': {
        name: '重大',
        status: 'serious',
        color: 'red'
    }
}

export const order_status = [{
    label: '未派单',
    value: '1'
}, {
    label: '处理中',
    value: '2'
}, {
    label: '已完成',
    value: '3'
}]

export const alert_type = [{
    label: '全部',
    value: '',
    id: ''
}, {
    label: '设备',
    value: '设备',
    id: '1'
}, {
    label: '业务系统',
    value: '业务系统',
    id: '2'
}]

export const relation = [{
    label: '包含',
    value: 'include'
}, {
    label: '不包含',
    value: 'exclude'
}]

export const alert_notice_types = [{
    label: '全部',
    value: '',
    id: ''
}, {
    label: '邮件',
    value: '邮件',
    id: '1'
}, {
    label: '短信',
    value: '短信',
    id: '0'
}]

export const notice_status = [{
    label: '全部',
    value: '',
    id: ''
}, {
    label: '成功',
    value: '成功',
    id: '1'
}, {
    label: '失败',
    value: '失败',
    id: '0'
}]

export const alert_from = [{
    label: 'ZABBIX',
    value: 'ZABBIX'
}, {
    label: 'prometheus',
    value: 'prometheus'
}, {
    label: '内部告警',
    value: '内部告警'
}, {
    label: 'inside',
    value: 'inside'
}, {
    label: 'HTTP_MONITOR',
    value: 'HTTP_MONITOR'
}, {
    label: 'sysLog',
    value: 'sysLog'
}]

export const operateType = {
    '大于': '>',
    '小于': '<',
    '等于': '=',
    '不等于': '!=',
    '大于等于': '>=',
    '小于等于': '<=',
    '包含': 'in',
    '不包含': 'not in',
    '模糊匹配': 'like'
}

export const scavenging_type = [{
    label: '全部',
    value: '',
    id: ''
}, {
    label: '人工清除',
    value: '人工清除',
    id: '1'
}, {
    label: '自动清除',
    value: '自动清除',
    id: '2'
}]
