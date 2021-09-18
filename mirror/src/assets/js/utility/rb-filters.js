/* eslint-disable no-sparse-arrays */
import moment from 'moment'
import marked from 'marked'
import parser from 'cron-parser' //  https://github.com/harrisiirak/cron-parser
import CN from 'src/assets/js/constant/rb_zh_cn.i18n.constant.js'
let translate = str => {
    return CN[str] || str
}
let lowercase = str => {
    return str.toLowerCase()
}
let formatDate = (timestamp, fractional) => {
    if (!timestamp) {
        return ''
    }
    if (fractional) {
        return moment(timestamp).format('YYYY-MM-DD HH:mm:ss.SSS')
    } else {
        return moment(timestamp).format('YYYY-MM-DD HH:mm:ss')
    }
}

let formatToTime = (timestamp, fractional) => {
    if (!timestamp) {
        return ''
    }
    if (fractional) {
        return moment(timestamp).format('HH:mm:ss.SSS')
    } else {
        return moment(timestamp).format('HH:mm:ss')
    }
}

let formatDate2 = (timestamp, fractional) => {
    if (!timestamp) {
        return ''
    }
    if (fractional) {
        return moment(timestamp).format('YYYY-MM-DD')
    } else {
        return moment(timestamp).format('YYYY-MM-DD')
    }
}
let formatDateByFilter = (timestamp, formate = 'YYYY-MM-DD HH:mm:ss') => {
    if (!timestamp) {
        return ''
    }
    return moment(timestamp).format(formate)
}
let formatUtcStr = str => {
    if (!str) {
        return '-'
    }
    return moment(moment.utc(str).valueOf()).format('YYYY-MM-DD HH:mm:ss')
}

let rbDuration = milliseconds => {
    let message = ''
    if (milliseconds >= 1000) {
        const duration = moment.duration(milliseconds)

        const days = duration.days()
        const hours = duration.hours()
        const minutes = duration.minutes()
        const seconds = duration.seconds()
        message += days ? days + '天' : ''
        message += hours ? hours + '小时' : ''
        message += minutes ? minutes + '分钟' : ''
        message += seconds ? seconds + '秒' : ''
    } else if (milliseconds > 0) {
        message = '少于1秒'
    } else {
        message = '-'
    }
    return message
}

let markdown2html = str => {
    return marked(str)
}
let rbCrontabNext = (
    rule,
    step = 1,
    options = {
        utc: false,
        format: 'YYYY-MM-DD HH:mm:ss',
        tz: 'Asia/Shanghai'
    }
) => {
    const INPUT_FORMAT = 'ddd MMM DD YYYY HH:mm:ss GMTZ'
    let dateString = 'N/A'
    if (!rule || !isFiledCountCorrect(rule)) {
        return dateString
    }
    try {
        const interval = parser.parseExpression(rule, options)
        for (let i = 0; i < step; i++) {
            dateString = interval.next().toString()
        }
        const momentInstance = moment(dateString, INPUT_FORMAT, 'en')
        if (options.utc) {
            dateString =
                momentInstance.utcOffset(0).format(options.format) + ' UTC'
        } else if (options.tz === 'Asia/Shanghai') {
            dateString =
                momentInstance.utcOffset(480).format(options.format) + ' GMT+8'
        } else {
            dateString = momentInstance.format(options.format)
        }
    } catch (err) {
        dateString = 'N/A'
    }
    return dateString
}

function isFiledCountCorrect(exp) {
    const spaceCount = exp.split('').filter(val => val === ' ').length
    const filedCount = exp.split(' ').filter(val => val).length
    return spaceCount === 4 && filedCount === 5
}

function rbCapitalizeAll(field) {
    if (!field) {
        return ''
    }
    const tokens = field.split(' ')
    const formatField = tokens
        .map(
            token =>
                token.charAt(0).toUpperCase() + token.slice(1).toLowerCase()
        )
        .join(' ')
    return formatField
}

/*
 *字符串型数字、数字型数字保留小数
 *num：当前数字
 *pointNum：保留位数
 *keepMinNum：true小于1千的数字保留原位数
 */
function fixedNumber(num = 0, pointNum = 0, keepMinNum = false) {
    num = Number(num)
    if (num > 1000) {
        num = (num / 1000).toFixed(pointNum) + 'K'
    } else {
        if (!keepMinNum) {
            num = num.toFixed(pointNum)
        }
    }
    return num
}
/*
 *字符串型数字、数字型数字保留两位小数
 *num：当前数字
 */
function fixedNumberPointTwo(num) {
    let isNum = typeof num === 'number'
    if (isNum && num % 1 === 0) {
        return num
    }
    if (isNum) {
        return num.toFixed(2)
    } else {
        return ''
    }
}

/*
 *vue-selecttree封装(https://vue-treeselect.js.org)
 *data：源数据
 *options：转化为vueTree能识别的id|label|children
 */
function filterVueTree(data, options = {}) {
    let id = options.id || 'id'
    let label = options.label || 'label'
    let children = options.children || 'children'
    let isDisabled = options.isDisabled || 'isDisabled'
    // let autoAddRoot = options.autoAddRoot || 'autoAddRoot'

    // if (typeof options.isDisabled === 'function') {
    //     //如果是自定义方法
    //     isDisabled = Object.keys(options.isDisabled())[0]
    // }

    let subElement = function (item) {
        let element = {
            id: item[id],
            label: item[label],
            isDisabled:
                typeof options.isDisabled === 'function'
                    ? options.isDisabled(item) : item[isDisabled],
            selfDatas: item
        }

        if (item[children] && item[children].length > 0) {
            element.children = []
            item[children].forEach(subItem => {
                element.children.push(subElement(subItem))
            })
        }
        return element
    }
    let biz_tree = []
    let root = {
        id: '',
        label: '所有',
        children: [],
        isDisabled: false,
        selfDatas: ''
    }

    // 判断是否需要自动追加根节点
    function handleRootChildren(data, root) {
        if (data.length) {
            data.forEach(item => {
                root.children.push(subElement(item))
            })
        } else {
            root.children = ''
        }
        return root
    }

    if (options.autoAddRoot) {
        // 如果已经有根节点
        if (data.length === 1 && ['全部', '所有'].indexOf(data[0][label]) > -1) {
            root = {
                id: data[0][id],
                label: data[0][label],
                children: [],
                isDisabled:
                    typeof options.isDisabled === 'function'
                        ? options.isDisabled(data[0]) : data[0][isDisabled],
                selfDatas: data[0]
            }
            root = handleRootChildren(data[0][children], root)
        } else {
            root = handleRootChildren(data, root)
        }
    } else {
        root = {
            id: data[0][id],
            label: data[0][label],
            children: [],
            isDisabled:
                typeof options.isDisabled === 'function'
                    ? options.isDisabled(data[0]) : data[0][isDisabled],
            selfDatas: data[0]
        }
        root = handleRootChildren(data[0][children], root)
    }

    biz_tree.push(root)

    return biz_tree
}

/** 地址栏信息**/
function getUrlKey(name) {
    return (
        decodeURIComponent(
            (new RegExp('[?|&]' + name + '=' + '([^&;]+?)(&|#|;|$)').exec(
                location.href
            ) || [, ''])[1].replace(/\+/g, '%20')
        ) || null
    )
}

/*
 *xMonth：根据当前月获得某一个月份
 *num：获得当前月往前后推的月份（默认下个月，负数上月）
 *yearFormat：年、月之间的格式
 *monthFormat：月格式
 */
function xMonth(num = 0, yearFormat = '-', monthFormat = '') {
    let date = new Date()
    let year = date.getFullYear() // 当年
    let curMonth = date.getMonth() + 1 // 当月(getMonth0~11)
    let month = curMonth + num
    if (month === 0) {
        month = 12
        year = year - 1
    } else if (month < 0) {
        month = 12 + month
        year = year - 1
    } else if (month > 12) {
        month = month - 12
        year = year + 1
    }

    if (month <= 9) {
        month = '0' + month
    }

    let result = year + yearFormat + month
    if (monthFormat) {
        result = result + monthFormat
    }
    return result
}
/*
 *xDay：根据当前时间获得某天时间
 *timestamp：当前时间
 *num：获得当前时间往前后推的天数（默认往后推,负数往前推）
 *fractional：时间格式
 */
function xDay(timestamp = '', num = 0, fractional = '') {
    let format = fractional ? fractional : 'YYYY-MM-DD'
    let currentTime = timestamp ? timestamp : new Date()
    let result = moment(currentTime).subtract(num, 'days').format(format)
    return result
}

function formatNoYear(time) {
    return moment(time).format('MM-DD HH:mm:ss')
}

function getFilename(str) {
    let arr = str.split('/')
    let last = arr[arr.length - 1]
    return last
}

export {
    lowercase,
    formatDate,
    formatToTime,
    formatDate2,
    formatDateByFilter,
    formatUtcStr,
    rbDuration,
    markdown2html,
    rbCrontabNext,
    rbCapitalizeAll,
    translate,
    fixedNumber,
    filterVueTree,
    getUrlKey,
    xMonth,
    xDay,
    fixedNumberPointTwo,
    formatNoYear,
    getFilename
}
