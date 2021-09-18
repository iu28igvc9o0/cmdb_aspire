
export default {
    data() {
        return {
            allCodeList: [],
            roleList: [],
            // 当前脚本内容
            currentCodeInfo: {
                codeContent: '',
                languageType: 1
            },
            isSensitivity: false, // 是否敏感参数
            addForm: {
                codeTime: '600',
                // 目标服务器
                selectedData: []
            },
            addFormRules: {
                selectedData: [
                    {
                        required: false,
                        trigger: 'change',
                        validator: (rule, value, callback) => {
                            if (!value.length) {
                                callback(new Error('请选择目标机器!'))
                            } else {
                                callback()
                            }
                        }
                    }
                ]
            },

            accountList: [], // 账户列表
            codeCloneList: [], // 脚本克隆列表
            publicCodeList: [], // 公共脚本列表
            codeInfo: {}, // 脚本弹框最新内容
            accountBoxShow: false, // 登记账户
            newAccountName: '', // 新登记账户名称
            chooseMachineType: 1, // 选择主机方式
            serverList: [], // 主机列表
            serversBoxShow: false, // 主机选择弹框
            resultBoxShow: false, // 执行结果弹框
            hasRunCode: false, // 显示执行结果按钮
            stepInstId: '', // 步骤实例ID
            searchWord: '',
            serversCmdbBoxShow: false,
            target_hosts: [], // 克隆主机列表
        }
    },
    watch: {
        dataList(val) {
            let self = this
            setTimeout(() => {
                if (val.length) {
                    self.handleRowSelect()
                    self.loading = false
                }
            }, 0)
        }
    },
    computed: {
        // 目标主机列表，格式为 proxy_id:host_ip
        targetHostList() {
            return this.addForm.selectedData.map(item => {
                return `${item.proxy_id}:${item.agent_ip}`
            })
        },
        cloneSelectedData() {
            let cloneHosts = []
            this.target_hosts.forEach(host => {
                let hostArr = host.split(':')
                this.serverList.forEach(server => {
                    if (+hostArr[0] === server.proxy_id && hostArr[1] === server.agent_ip) {
                        cloneHosts.push(server)
                    }
                })
            })
            this.addForm.selectedData = cloneHosts
            return cloneHosts
        }
    },
    methods: {
        close(type) {
            if (type) {
                this.serversCmdbBoxShow = false
            }
        },
        addCMDB(data) {
            console.log(data)
            this.close(true)
            this.addForm.selectedData = JSON.parse(
                JSON.stringify(data)
            )
            this.$refs.addForm.validateField('targetMachines')
        },
        search(pageSize, currentPage) {
            if (currentPage !== undefined) {
                this.currentPage = currentPage
            }
            this.loading = true
            this.$emit('search', 'getServerslist', pageSize || this.pageSize, currentPage || this.currentPage)
        },
        // 显示服务器弹框
        showServersDialog() {
            if (sessionStorage.getItem('ops_agent_loadfrom') && sessionStorage.getItem('ops_agent_loadfrom') == 'cmdb') {
                this.serversCmdbBoxShow = true
            } else {
                this.serversBoxShow = true
                this.search(null, 1)
            }
        },
        // 添加选中服务器/关闭弹框
        addToSelectedData() {
            this.addForm.selectedData = this.multipleSelection
            this.serversBoxShow = false
            this.$refs.addForm.validateField('selectedData')
        },
        // 删除选中服务器
        clearMachine(index) {
            this.addForm.selectedData.splice(index, 1)
            this.$refs.serverTable && this.$refs.serverTable.clearSelection()
            this.multipleSelection = this.addForm.selectedData
        },
        // 选中表格行
        toggleRow(row) {
            this.$refs.serverTable.toggleRowSelection(row, true)
        },
        // 判断主机是否需要勾选
        handleRowSelect() {
            this.addForm.selectedData.forEach((item) => {
                this.dataList.forEach((row) => {
                    if (item.agent_ip === row.agent_ip) {
                        this.toggleRow(row)
                    }
                })
            })
        },
    }
}
