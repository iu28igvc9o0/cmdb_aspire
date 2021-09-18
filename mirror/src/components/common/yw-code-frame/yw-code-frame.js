// 级联下拉框事件
import rbConfigService from 'src/services/condition_management/query_configuration.js'
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
            getCondicationGetList: [],
            // 数据框类型
            selectTypes: ['cascader', 'select', 'listSel'],
            inputTypes: ['ip', 'singleRowText', 'int', 'input']
        }
    },
    computed: {
        // 获得conditionList表单所有值
        getConditionListForm() {
            let obj = {}
            this.conditionList.forEach((item) => {
                if (item.frameDatas.select && this.selectTypes.includes(item.frameOptions.type) && typeof item.frameDatas.select === 'object') {
                    obj[item.key] = item.frameDatas.select && item.frameDatas.select.id || null
                } else {
                    obj[item.key] = item.frameDatas.select
                }
            })
            return obj
        }
    },
    methods: {
        // 查询字段列表(通过condicationCode)
        queryConditionList(obj = {}) {
            let params = {
                condicationCode: obj.condicationCode
            }
            return rbConfigService.getCondicationGet(params).then((res) => {
                if (res && res.settingRelationList && res.settingRelationList.length > 0) {
                    this.getCondicationGetList = res.settingRelationList
                    this.conditionList = res.settingRelationList.map((item) => {
                        return {
                            key: item.cmdbCode.filedCode,
                            name: item.cmdbCode.filedName,
                            frameDatas: {
                                show: true,
                                select: '',
                                codeObj: item.cmdbCode,
                                parentCode: '',
                                parentSelect: '',
                                cascadeList: item.cmdbCode.cascadeList && item.cmdbCode.cascadeList.map((cascadeItem) => {
                                    return cascadeItem.subFiledCode
                                })
                            },
                            frameOptions: {
                                type: item.cmdbCode.controlType.controlCode
                            }
                        }
                    })
                }
                return res
            })
        },

        /*
        **根据key获得当前框数据
        **key:(如fieldCode)
        **valueType：字段类型(如id,name等等)
        */
        getSelectValueByKey(key, valueType = '') {
            let temp = ''
            this.conditionList.some((item) => {
                if (item.key === key) {
                    if (item.frameDatas.select && this.selectTypes.includes(item.frameOptions.type) && typeof item.frameDatas.select === 'object') {
                        temp = valueType ? item.frameDatas.select[valueType] : item.frameDatas.select['id']
                    } else {
                        temp = item.frameDatas.select
                    }

                    return true
                } else {
                    return false
                }
            })
            return temp
        },

        /*
       **下拉框选中赋值
       **key:(如fieldCode)
       **value:值(支持id、name、下拉框数据对应的对象)
       **valueType：字段类型(如id,name等等)
       */
        // 下拉框赋值
        setSelectValue(key, value, valueType = '') {
            this.conditionList.some((item) => {
                if (item.key === key) {
                    if (value && typeof value === 'object') {
                        item.frameDatas.select = valueType ? value[valueType] : value['id']
                    } else {
                        item.frameDatas.select = value
                    }

                    this.freshCodeFrame(item)
                    return true
                } else {
                    return false
                }
            })
        },


        // 刷新当前下拉框等组件
        freshCodeFrame(item) {
            item.frameDatas.show = false
            this.$nextTick(() => {
                item.frameDatas.show = true
            })
        },
        // 切换下拉框
        changeSelect(frameDatas, frameOptions, select, codeObj) {
            this.conditionList.some((item) => {
                if (item.key === frameDatas.codeObj.filedCode) {

                    if (this.selectTypes.indexOf(frameOptions.type) > -1) {
                        // 下拉框
                        // item.frameDatas.select = select && select.id || ''
                        item.frameDatas.select = select || {}

                    } else {
                        item.frameDatas.select = select
                    }

                    return true
                } else {
                    return false
                }
            })

            // 级联查询
            if (frameDatas.cascadeList) {
                this.conditionList.forEach((item) => {
                    if (frameDatas.cascadeList.indexOf(item.key) > -1) {
                        // 被级联框数据入参
                        item.frameDatas.select = ''
                        item.frameDatas.parentCode = codeObj
                        item.frameDatas.parentSelect = select

                        // 刷新当前下拉框组件
                        // this.freshCodeFrame(item)
                    }
                })
            }
        },

        // 清空下拉框
        clear() {

        },
        // 重置
        resetCondition() {
            this.conditionList.forEach((item) => {
                item.frameDatas.select = ''
                item.frameDatas.parentCode = ''
                item.frameDatas.parentSelect = ''
                this.freshCodeFrame(item)
            })
        }
    },


}
