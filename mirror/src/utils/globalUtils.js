
/* 全局公共方法集 */
// 表格数据处理： smartLayout 表格数据格式
export function smartTableDataFormat(tableItem, responseBody, dataKey, countKey) {
    Object.assign(responseBody, {
        page: (tableItem.pagination && tableItem.pagination.currentPage) || tableItem.pageNum, // 当前页数
        data: (dataKey && responseBody[dataKey]) || responseBody.dataList,    // 表格列表
        total: (countKey && responseBody[countKey]) || responseBody.totalCount, // 列表总数
        status: 200
    })
}
// 表单 重置对象属性值为空： 
export function smartFormReset(object) {
    for (const key in object) {
        object[key] = ''
    }
}
// 表单 下拉框请求数据处理： 
export function smartFormSelectDataFormat(callback, responseBody, data) {
    callback(Object.assign(responseBody, {
        data: data || responseBody, // 下拉框列表数据
        status: 200
    }))
}
// 表格 下拉框请求数据处理： 
export function smartTableSelectDataFormat(responseBody, data) {
    Object.assign(responseBody, {
        data: data || responseBody, // 下拉框列表数据
        status: 200
    })
}
// 统一处理接口响应消息
export function handleSmartResponse(_this, flag, msg, boxName) {
    if (flag) {
        _this.$message.success(msg || '操作成功')
        boxName && (_this[boxName] = false)
    } else {
        _this.$message.error(msg || '操作失败')
    }
}

// 深拷贝对象
export function deepClone(obj) {
    if (!obj) {
        return
    }
    return JSON.parse(JSON.stringify(obj))
}
// 数组对象去重
export function reduceArrObj(arr, key) {
    let hash = {}
    return arr.reduce(function (newArr, cur) {
        hash[cur[key || 'ip']] ? '' : hash[cur[key || 'ip']] = true && newArr.push(cur)
        return newArr
    }, [])
}
// 数组对象去除空值
export function filterEmptyArr(arr) {
    let r = arr.filter(item => item)
    return r
}

// 动态设置对象属性
export function setObjKeys(_this, obj, key, value) {
    if (!(key in obj)) {
        _this.$set(obj, key, value)
    } else {
        obj[key] = value
    }
}

// 数组转为对象(list：数组,children:子级节点,key：转换后的key)
export function transformListToObj(list = [], children = 'children', key = 'key') {
    let obj = {}
    function listToObj(list, children) {
        list.forEach((item) => {
            if (item[children] && item[children].length > 0) {
                listToObj(item[children], children)
            } else {
                // obj[item[key]] = item
                // 转为空
                obj[item[key]] = ''
            }
        })
    }
    listToObj(list, children)
    return obj

}

// 5分钟定时更新数据，关闭页面/切换tab自动销毁
export function creatInterval(_this, callback, time) {
    callback()
    let timer = setInterval(() => {
        callback()
    }, time)
    _this.$once('hook:deactivated', function () {
        clearInterval(timer)
    })
    _this.$once('hook:beforeDestroy', function () {
        clearInterval(timer)
    })
}

// 文件下载
export function createDownloadFileBlob(res, filename) {
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

// 处理vue treeselect数据 
// 懒加载子节点：children设置为null，并且需要设置:load-options="loadOptions"方法，否则控制台会报错；非懒加载，没有children属性
export function formatTreeSelectData(item, element = {
    id: item.name,
    label: item.name,
}) {
    if (item.subList && item.subList.length > 0) {
        element.children = []
        _(item.subList).forEach(subItem => {
            element.children.push(formatTreeSelectData(subItem))
        })
    }
    return element
}   