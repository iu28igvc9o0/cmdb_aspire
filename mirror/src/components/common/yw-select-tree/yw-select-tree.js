// 级联下拉框事件
export default {
    data() {
        return {
            // 表单字段
            conditionList: [
                // {
                //     key: 'idcType',
                //     name: '资源池',
                //     frameDatas: {
                //         show: true,
                //         // 当前选中值
                //         select: '',
                //         // 当前code对象
                //         codeObj: { codeId: '1923b99c568011e998180242ac110001', filedCode: 'idcType' },// 暂时写死，后面看cmdb后端字段优化
                //         // 父级code对象
                //         parentCode: '',
                //         // 父级选中的值
                //         parentSelect: '',
                //         // 级联的子级key
                //         cascadeList: ['pod_name', 'roomId'],
                //     },
                //     frameOptions: {
                //         type: 'select',
                //     }
                // },
            ],
            // 数据框类型
            selectTypes: ['cascader', 'select', 'listSel'],
            inputTypes: ['ip', 'singleRowText', 'int', 'input']
        }
    },
    methods: {

    },


}
