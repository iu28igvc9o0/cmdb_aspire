export function getQueryValue(queryName) {
    var reg = new RegExp('(^|&)' + queryName + '=([^&]*)(&|$)', 'i')
    var r = window.location.search.substr(1).match(reg)
    if (r != null) {
        return decodeURIComponent(decodeURI(r[2]))
    } else {
        return null
    }
}

// 单点登录token、type处理
export function getQueryToken(tokenKey) {
    var query = window.location.search.substring(1)
    var vars = query.split('&')
    var map = {}
    for (var i = 0; i < vars.length; i++) {
        var pair = vars[i].split('=')
        if (pair[0].toLocaleLowerCase().indexOf(tokenKey) > -1) {
            map.code = '0000'
            map.type = pair[0]
            map.token = pair[1]
            return map
        }
    }
    map.code = '0001'
    return map
}


export function createDownloadFile(res, filename) {
    let blob = new Blob([res], { type: 'application/vnd.ms-excel' })
    let objectUrl = URL.createObjectURL(blob)
    let downLoadElement = document.createElement('a')
    downLoadElement.href = objectUrl
    downLoadElement.download = filename
    document.body.appendChild(downLoadElement)
    downLoadElement.click()
    document.body.removeChild(downLoadElement)
    URL.revokeObjectURL(objectUrl)
}

/**
 * 校验文件类型及大小
 * type string-单个类型；array-多个类型
 * size 文件大小，缺省不校验
 */
export function validFile(self, file, type, size) {
    let isTheType
    if (typeof type === 'string') {
        isTheType = file.name.includes(`.${type}`)
    } else if (typeof type === 'object') {
        type.forEach(item => {
            if (file.name.includes(item)) {
                isTheType = true
            }
        })
    }
    const maxSize = file.size / 1024 / 1024 < size
    if (!isTheType) {
        self.$message.error(`上传文件格式不正确, 请选择[${type}]格式文件`)
        return false
    }
    if (size && !maxSize) {
        self.$message.error(`上传文件大小不能超过${size}MB`)
        return false
    }
    return true
}