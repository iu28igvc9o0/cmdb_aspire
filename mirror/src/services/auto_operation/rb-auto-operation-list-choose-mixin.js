// 列表选择弹框公共方法
export default {
    data() {
        return {
            curSelection: [], // 待选列表当前页已选中行
            curPageSelected: [], // 待选列表当前页已选中行的idList
            selectedDataList: [], // 已选中的列表
            oldList: [],  // 缓存已选中的列表
            curKey: 'param_code',   // 比对是否选中该行的key
            curPropKey: 'paramCode',   // 接口返回已选中的列表
            initPage: true,
        }
    },
    computed: {
        // 参数列表、接口返回的已选中的参数 串
        paramData() {
            return {
                appointProp: this[this.curPropKey], // 已选中的值
                dataList: this.dataList
            }
        }
    },
    watch: {
        // 更新已选中行，接口提交
        selectedDataList(val) {
            // 选择主机列表，直接传递选中的数组列表
            if (this.passDataType === 'Array') {
                this.$emit('setSelectedKey', val)
            } else {
                let valueArr = []
                let paramType = [], saveValue
                val.forEach(item => {
                    valueArr.push(`${item[this.curKey]}`)
                    paramType.push(`${item.param_type}`)
                })

                let curPropValue = this[this.curPropKey]
                // 更新已选中的值:数组类型
                if (Array.isArray(curPropValue)) {
                    curPropValue = Array.from(new Set([].concat(curPropValue, valueArr)))
                    saveValue = curPropValue
                    // 字符串类型
                } else {
                    if (curPropValue) {
                        curPropValue = curPropValue + ',' + valueArr.toString()
                    } else {
                        curPropValue = valueArr.toString()
                    }
                    saveValue = this.$utils.filterEmptyArr(curPropValue.split(',')).toString()
                }
                this.$emit('setSelectedKey', saveValue, paramType)
            }
        },
        // 接口返回数据，设置已选中行
        paramData: {
            handler(newVal) {
                // 已选中的列表原始数据，接口返回的格式，需要处理为table
                let selectedDataList
                if (!newVal.appointProp) {
                    selectedDataList = this.selectedDataList
                } else {
                    if (Array.isArray(newVal.appointProp)) {
                        selectedDataList = newVal.appointProp
                    } else {
                        selectedDataList = newVal.appointProp.split(',')
                    }
                }
                // 有已选中的主机，已请求cmdb主机列表
                selectedDataList.forEach(item => {
                    if (!newVal.appointProp) {
                        item = item[this.curKey]
                    }
                    newVal.dataList.forEach(row => {
                        if (item == row[this.curKey]) {
                            this.pushNewItem(row, this.selectedDataList, this.curKey)
                            this.$nextTick(() => {
                                this.$refs.dataTable && this.toggleRow(row, this.$refs.dataTable)
                            })
                        }
                    })
                })
            },
            immediate: true,
            deep: true
        },
    },
    mounted() {
    },
    methods: {
        getRowKeys(row) {
            return row[this.curKey]
        },
        // 确认选中列表
        checkSelectedList() {
            this.hideChooseBox()
            this.$emit('checkSelectedList')
        },
        // 关闭选择弹框
        hideChooseBox() {
            this.$emit('hideChooseBox')
        },
        selectRow(selection) {
            if (this.initPage) {
                return
            }
            // 缓存当前页选中值
            this.curSelection = this.$utils.deepClone(selection)
            // 缓存所有已选中的值
            this.oldList = this.$utils.deepClone(this.selectedDataList)
        },
        // 勾选动作变化时触发
        handleSelectionChange(selection) {
            this.initPage = false
            this.selectRow(selection)
        },
        // 判断item是否已存在列表list中，不存在，则加入list；curKey为比对的key值
        pushNewItem(item, list, curKey) {
            let hasExist = list.filter(oldItem => {
                return item[curKey] == (oldItem.agentIp || oldItem[curKey])
            })
            if (!hasExist.length) {
                list.push(item)
            }
            return hasExist.length
        },
        // 添加选中/关闭弹框
        addToSelectedList() {
            // 合并旧的已选和新的已选，数组去重
            this.selectedDataList = this.$utils.reduceArrObj([].concat(this.curSelection, this.oldList), this.curKey)
            this.dialogShow = false

            // 巡检模版
            if (this.from === 'inspectionTask') {
                this.$emit('close', '1')
                this.$emit('addCMDB', this.curSelection)
                return
            }

            // 其他页面
            this.$refs.addForm && this.$refs.addForm.validateField('selectedDataList')
            this.$emit('setSelectedService', this.serviceList)
            this.activeName = 'first'
        },
        // 打开待选择列表
        showDialog() {
            this.dialogShow = true
            setTimeout(() => {
                this.handleRowSelect()
            }, 0)
        },
        addList() {

        },
        // 关闭待选择列表
        hideDialog() {
            this.activeName = 'first'
            this.dialogShow = false
            if (this.from === 'inspectionTask') {
                this.$emit('close', '1')
            }
        },
        // 删除选中行
        removeRow(index) {
            this.selectedDataList.splice(index, 1)
        },
        // 选中表格行
        toggleRow(row, table) {
            table.toggleRowSelection(row, true)
        },
        // 通过指定key判断行是否需要勾选
        handleRowSelect() {
            let self = this
            self.curPageSelected = []
            self.curSelection = []
            setTimeout(() => {
                self.$refs.dataTable && self.$refs.dataTable.clearSelection()
                self.selectedDataList.forEach(item => {
                    self.dataList.forEach(row => {
                        if ((item[self.curKey] || item.agentIp) === row[self.curKey]) {
                            // 缓存当前页已选中值
                            self.curPageSelected.push(item[self.curKey])
                            self.curSelection.push(item)
                            self.toggleRow(row, self.$refs.dataTable)
                        }
                    })
                })
            }, 0)
        },

    }
}
