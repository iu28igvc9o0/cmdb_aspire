
export default {
    data() {
        return {
            allCodeList: [],
            accountList: [],
            roleList: [],
            addForm: {
                selectedData: []
            },
            addFormRules: {
                selectedData: [
                    {
                        required: this.isRequire,
                        trigger: 'change',
                        validator: (rule, value, callback) => {
                            if (!value.length) {
                                callback(new Error(`请选择${this.tipsTxt}!`))
                            } else {
                                callback()
                            }
                        }
                    }
                ]
            },

            accountBoxShow: false, // 登记账户
            serversBoxShow: false, // 主机选择弹框
        }
    },
    watch: {
    // toggleRowSelection 配合 row-key reserve-selection 属性，页面加载时还原已选中数据、记录翻页已选中数据
    // 表格数据更新时，勾选已选中数据
        dataList(val) {
            let self = this
            setTimeout(() => {
                if(val.length) {
                    self.handleRowSelect()
                    self.loading = false
                }
            }, 0)
        }
    },
    computed: {
        tipsTxt() {
            if(this.selectType === 'target_ops_user') {
                return '可执行设备账户'
            }else if(this.selectType === 'exec_role_ids') {
                return '可执行系统角色'
            }else if(this.selectType === 'review_role_ids') {
                return '执行审核赋权角色'
            }
        }
    },
    methods: {
    // 翻页等操作触发搜索
        search(pageSize, currentPage) {
            let searchType
            if(this.selectType === 'target_ops_user') {
                searchType = 'getAccountlist'
            }else{
                searchType = 'getRolelist'
            }
            if(currentPage !== undefined) {
                this.currentPage = currentPage
            }
            this.loading = true
            this.$emit('search', searchType, pageSize || this.pageSize, currentPage || this.currentPage)
        },
        // 显示服务器弹框
        showServersDialog() {
            this.serversBoxShow = true
            this.search(null, 1)
        },
        // 拷贝增加可执行角色
        cloneRole() {
            let exec_role_ids = this.rowData.rule_range['exec_role_ids']
            if(exec_role_ids.length) {
                this.addForm.selectedData = JSON.parse(
                    JSON.stringify(exec_role_ids)
                )
                this.$message.success('拷贝成功！')
            }else{
                this.$message.warning('请先选择可执行系统角色！')
            }
        },
        // 添加选中服务器/关闭弹框
        addToTargetList() {
            this.addForm.selectedData = this.multipleSelection
            this.serversBoxShow = false
            this.$refs.addForm.validateField('selectedData')
        },
        // 删除选中服务器
        clearSelectedData(index) {
            this.addForm.selectedData.splice(index, 1)
            // 先清空multipleSelection，再同步multipleSelection
            this.$refs.serverTable && this.$refs.serverTable.clearSelection()
            this.multipleSelection = this.addForm.selectedData
        },
        // 选中表格行
        toggleRow(row) {
            this.$refs.serverTable && this.$refs.serverTable.toggleRowSelection(row, true)
        },
        // 判断是否需要勾选
        handleRowSelect() {
            this.addForm.selectedData.forEach((item) => {
                this.dataList.forEach((row) => {
                    if(this.selectType === 'target_ops_user' && item.accountName && item.accountName === row.accountName) {
                        this.toggleRow(row)
                    }else if(item.uuid && item.uuid === row.uuid) {
                        this.toggleRow(row)
                    }
                })
            })
        },

    }
}
