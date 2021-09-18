/** 常用校验规则 */
// 英文、下划线(英文开头、下划线不连续)
export function isEnUnderLine(rule, value, callback) {
    // let pattern = /^([a-zA-Z]+[0-9]*[_]?[a-zA-Z0-9]*)*$/
    let pattern = /^[a-zA-Z]([_]|([a-zA-Z0-9]+[_]?)*)$/
    if (value && !pattern.test(value)) {
        callback(new Error('只支持英文数字和下划线（不连续）且以英文字母开头'))
    } else {
        callback()
    }
}

// 是否是数组型json格式(如'[{'a':'123'}]')
export function isJsonArray(rule, value, callback) {
    let valueParse = ''
    try {
        if (value) {
            valueParse = JSON.parse(value)
        }

    } catch (error) {
        callback(new Error('必须json数组格式，请检查值'))
    }

    if (value && !(typeof value === 'string' && Array.isArray(valueParse))) {
        callback(new Error('必须json数组格式，请检查值'))
    } else {
        callback()
    }
}
// 手机号判断
export function isEmptyOrPhone(rule, value, callback) {
    const pattern = /^((0\d{2,3}-\d{7,8})|(1[34578]\d{9}))$/
    if (value && value !== '' && !pattern.test(value)) {
        return callback(new Error('输入的手机号错误'))
    }
    return callback()
}
