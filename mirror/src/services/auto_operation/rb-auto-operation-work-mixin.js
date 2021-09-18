
import rbAutoOperationServicesFactory from 'src/services/auto_operation/rb-auto-operation-services.factory.js'
import store from 'src/store/index'
export default {
    data() {
        return {
            allCodeList: [],
            // 当前脚本内容
            currentCodeInfo: {
                codeContent: '',
                languageType: 1
            },
            isSensitivity: false, // 是否敏感参数
            addForm: {
                codeTime: '600',
                // 目标服务器
                targetMachines: [],
                custom_target_hosts: false,
                custom_target_hosts_value: 'A',
            },
            addFormRules: {
                targetMachines: [
                    {
                        required: true,
                        trigger: 'change',
                        validator: (rule, value, callback) => {
                            // 动态切换校验
                            if (rule.required && !value.length) {
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
            serversSelectShow: false, // 主机选择组件
            resultBoxShow: false, // 执行结果弹框
            hasRunCode: false, // 显示执行结果按钮
            stepInstId: '', // 步骤实例ID
            searchWord: '',
            serversCmdbBoxShow: false,
            target_hosts: [], // 克隆主机列表
            ops_agent_loadfrom: sessionStorage.getItem('ops_agent_loadfrom'),   // 主机类型
            hasClickServerBtn: false,   // 是否已点击按钮

        }
    },
    computed: {
        // store中的cmdb主机列表
        serverListRes() {
            return this.$store.state.homeStore.serverListRes
        },
        // 目标主机列表，格式为 proxy_id:host_ip
        targetHostList() {
            return this.addForm.targetMachines.map(item => {
                return `${item.proxy_id}:${item.agent_ip}`
            })
        },
        // 显示服务器弹框前，把弹框设置为透明，避免展开行时，弹框闪一下
        isOpacity0() {
            return !this.hasClickServerBtn ? 'opacity0' : ''
        }
    },
    mounted() {
        let hasInitWorkMixin = this.$store.state.homeStore.hasInitWorkMixin
        if (!hasInitWorkMixin) {
            // 触发获取cmdb主机列表，还原已选中主机
            if (this.ops_agent_loadfrom == 'cmdb') {
                this.serverList = []
                this.serversCmdbBoxShow = true
                let self = this
                setTimeout(() => {
                    self.serversCmdbBoxShow = false
                }, 0)
                store.commit('setHasInitWorkMixin', true)
                this.loading = true
            }
        }
    },
    methods: {
        setSelectedKey(data) {
            this.addForm.targetMachines = data
            this.$emit('updateCurrentRow', this.rowData, 'target_host_list', data)
        },
        setSelectedService(data) {
            this.$emit('upSelectedService', data)
        },
        // 传递最新脚本内容
        passCodeContent(data) {
            this.codeInfo = data
            this.$emit('updateCurrentRow', this.rowData, 'embed_script', data)
        },
        // 获取克隆脚本列表
        getAllCodeList() {
            let req = {
                pageNum: 1,
                pageSize: 200
            }
            rbAutoOperationServicesFactory.queryOpsScriptList(req).then(res => {
                this.allCodeList = JSON.parse(JSON.stringify(res.dataList))
            })
        }
    }
}
