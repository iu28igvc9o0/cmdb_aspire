// 模型配置：配置步骤数据缓存
export default {
    state: {
        // 模型模块常用对象
        commonObj: {
            topCatalogId: '', // 模型分组根节点id
            rowDetails: '', // 根据模型表格中每行数据id查询处的详情数据
            moduleStatus: 'add', // 模型操作状态(新增add、编辑edit、复制copy、删除delete)
        },

        // 模型字段数据(第二步和第三步的模型详情)
        moduleField: {
            origin: '', // 初始数据
            new: '' // 新增数据
        },
        // 模型对象(提交数据)
        moduleObj: {
            // 步骤1
            'id': null, // 模型id
            'name': '', // 模型分组名称
            'code': '', // 模型编码
            'catalogId': '', // 分组id
            'iconUrl': '', // 图标地址
            // "sortIndex": 1,//排序
            'auths': [], // 权限列表
            'refModules': [], // 引用模型列表(格式：[{}])
            'tags': [], // 标签列表
            'isVice': '',
            'enableApprove': '',
            // 步骤2、步骤3
            'groupList': [], // 分组字段列表(格式：[{}])
            // 步骤3
            // "codeSettingList": [] //字段显示隐藏(格式：[{}])
        }
    },
    mutations: {
        // 模型模块常用对象
        setCommonObj(state, data) {
            let allKeys = Object.keys(state.commonObj)
            for (let key in data) {
                if (allKeys.indexOf(key) > -1) {
                    state.commonObj[key] = data[key]
                }
            }
        },

        // 模型字段
        setModuleField(state, data) {
            state.moduleField.origin = data.origin
            state.moduleField.new = data.new
        },
        // 模型对象
        setModuleObj(state, data) {
            let allKeys = Object.keys(state.moduleObj)
            for (let key in data) {
                if (allKeys.indexOf(key) > -1) {
                    state.moduleObj[key] = data[key]
                }
            }
        },
    },

    actions: {
        // setComponent(context, data = []) {
        //   context.commit('setComponenet', data);
        // }
    }


}
