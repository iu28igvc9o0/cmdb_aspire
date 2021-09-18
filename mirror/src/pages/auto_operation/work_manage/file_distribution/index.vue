<template>
    <div class="components-container yw-dashboard run-code-box"
         v-loading.fullscreen.lock="pageLoading"
         :element-loading-text="loading_text">
        <el-form class="yw-form is-required"
                 ref="addForm"
                 :model="addForm"
                 :rules="addFormRules"
                 label-width="100px">
            <el-form-item label="作业名称"
                          prop="name">
                <el-input v-model="addForm.name"
                          clearable></el-input>
            </el-form-item>
            <el-form-item label="目标路径"
                          prop="target_file_path">
                <el-input v-model="addForm.target_file_path"
                          clearable></el-input>
            </el-form-item>
            <el-form-item label="执行账户"
                          prop="accountSelected">
                <el-select v-model="addForm.accountSelected"
                           placeholder="请选择"
                           filterable
                           clearable>
                    <el-option v-for="val in accountList"
                               :key="val.accountName"
                               :label="val.accountName"
                               :value="val.accountName"></el-option>
                </el-select>
            </el-form-item>
            <el-form-item label
                          v-if="accountBoxShow">
                <div class="account-box">
                    <el-col :span="3">
                        <span class="red">*</span> 账户名称：
                    </el-col>
                    <el-col :span="8">
                        <el-input v-model="newAccountName"></el-input>
                    </el-col>
                    <el-col :span="2">
                        <el-button type="primary"
                                   class="mleft10"
                                   @click="registAccount()">登记账户</el-button>
                    </el-col>
                </div>
            </el-form-item>

            <!-- 文件分发 -->
            <step-info-file :stepMoreData="stepMoreData"
                            :rowData="rowData"
                            ref="fileForm"
                            @setSelectedService="setSelectedService"
                            @updateFileEditMoreValid="updateFileEditMoreValid"
                            @updateCurrentRow="updateCurrentRow"></step-info-file>

            <el-form-item label
                          align="center"
                          class="fixed-bottom-box">
                <!-- <el-button @click="saveWork">保存</el-button> -->
                <el-button type="primary"
                           @click="toRunTask">开始执行</el-button>
            </el-form-item>
        </el-form>

        <!-- 执行结果 -->
        <el-drawer ref="drawer"
                   :show-close="false"
                   :visible.sync="resultBoxShow"
                   direction="rtl"
                   size="100%">
            <el-button class="drawer-close-btn"
                       type="primary"
                       @click="$refs.drawer.closeDrawer()">返回</el-button>
            <code-run-result :stepInstId="stepInstId"></code-run-result>
        </el-drawer>
        <div class="view-result-btn"
             v-if="hasRunCode"
             @click="resultBoxShow=true">查看执行结果</div>
    </div>
</template>

<script>
    import stepInfoFile from '../task/step-info-file.vue'
    import codeRunResult from '../../components/code-run-result.vue'
    import rbAutoOperationMixin from 'src/services/auto_operation/rb-auto-operation-mixin.js'
    import rbAutoOperationServicesFactory from 'src/services/auto_operation/rb-auto-operation-services.factory.js'
    import rbAutoOperationWorkServicesFactory from 'src/services/auto_operation/rb-auto-operation-work-services.factory.js'

    export default {
        components: {
            stepInfoFile,
            codeRunResult
        },
        data() {
            return {
                selectedServiceList: [],
                pageLoading: false,
                loading_text: '请稍候...',
                addForm: {
                    name: '',
                    accountSelected: 'aspire',
                    // 目标服务器
                    targetMachines: []
                },
                addFormRules: {
                    name: [
                        {
                            required: true,
                            message: '请先输入作业名称!',
                            trigger: 'blur'
                        },
                        {
                            min: 2,
                            max: 100,
                            message: '长度在 2 到 100 个字符!',
                            trigger: 'blur'
                        }
                    ],
                    target_file_path: [
                        {
                            required: true,
                            message: '请先输入目标路径!',
                            trigger: 'blur'
                        },
                        {
                            min: 2,
                            max: 100,
                            message: '长度在 2 到 100 个字符!',
                            trigger: 'blur'
                        }
                    ],
                    accountSelected: [
                        {
                            required: true,
                            message: '请选择执行账户!',
                            trigger: ['blur', 'change']
                        }
                    ],
                    targetMachines: [
                        {
                            required: true,
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
                accountBoxShow: false, // 登记账户
                newAccountName: '', // 新登记账户名称
                serverList: [], // 主机列表
                serversBoxShow: false, // 主机选择弹框
                resultBoxShow: false, // 执行结果弹框
                hasRunCode: false, // 显示执行结果按钮
                stepInstId: '', // 步骤实例ID
                searchWord: '',

                // 更多脚本信息，传递到子组件
                stepMoreData: {
                    serverList: [],
                },
                // 
                rowData: {
                    target_hosts: [],
                    file_source: []
                },
                isFileEditMoreValid: false,
                pipeline_id: '',  // 保存后，缓存作业id
                gotoRunWork: false, // 去执行作业
                remove_step_list: [], // 删除的步骤ids
            }
        },
        mixins: [rbAutoOperationMixin],
        computed: {
            // 目标主机列表，格式为 proxy_id:host_ip
            targetHostList() {
                return this.addForm.targetMachines.map(item => {
                    return `${item.proxy_id}:${item.agent_ip}`
                })
            },
        },
        watch: {
        },
        mounted() {
            this.initPageInfo()
        },
        methods: {
            setSelectedService(data) {
                console.log('data=======', data)
                this.selectedServiceList = []
                this.selectedServiceList = data
            },

            // 打开执行作业弹框
            toRunTask() {
                this.validForm()
            },
            // 保存作业
            saveWork() {
                this.gotoRunWork = false
                this.validForm()
            },
            // 发起执行文件分发的请求
            realtimeFileTransfer() {
                this.pageLoading = true
                let req = {
                    run_name: this.addForm.name,
                    target_ops_user: this.addForm.accountSelected,
                    target_file_path: this.addForm.target_file_path,
                    target_host_list: this.rowData.target_hosts,
                    transfer_files: this.rowData.file_source,
                    file_type: this.rowData.file_type,
                    target_exec_object: this.selectedServiceList
                    // label_id: this.addForm.label_id,
                }
                rbAutoOperationWorkServicesFactory
                    .realtimeFileTransfer(req)
                    .then(res => {
                        if (res.flag) {
                            this.$message('执行成功')
                            // 弹出执行结果弹框
                            this.stepInstId = res.biz_data
                            this.hasRunCode = true
                            this.resultBoxShow = true
                        } else if (!res.flag) {
                            this.$message.error(res.error_tip || '执行失败')
                        }
                        this.pageLoading = false
                    })
                    .catch(error => {
                        this.pageLoading = false
                        this.showErrorTip(error)
                    })
            },
            // 文件分发【编辑更多】信息已全部校验通过
            updateFileEditMoreValid() {
                this.realtimeFileTransfer()
            },
            // 更新当前行的信息
            updateCurrentRow(target, key, value) {
                if (key === 'target_hosts') {
                    let arr = value.map(item => {
                        return `${item.proxy_id}:${item.agent_ip}`
                    })
                    value = arr
                }
                this.$set(target, key, value)
            },
            // 初始化页面信息
            initPageInfo() {
                this.getServerlist()
                this.queryOpsAccountList()
            },
            // 登记账户
            registAccount() {
                if (!this.newAccountName) {
                    this.$message('请填写账户名称！')
                    return
                }
            },
            // 校验表单
            validForm() {
                const addFormValid = new Promise((resolve, reject) => {
                    this.$refs.addForm.validate(valid => {
                        if (valid) {
                            resolve()
                        } else {
                            reject()
                        }
                    })
                })

                Promise.all([addFormValid])
                    .then(() => {
                        this.$refs.fileForm.validForm()
                    })
                    .catch(() => {
                        this.$message('请先完善信息')
                    })
            },
            // 查询执行账户列表
            queryOpsAccountList() {
                rbAutoOperationServicesFactory.queryOpsAccountList().then(res => {
                    this.accountList = res
                })
            },
            // 翻页触发 
            search() {
                this.getServerlist()
            },
            // 查询主机列表
            getServerlist() {
                let req = {
                    page_no: this.currentPage,
                    page_size: 200
                }
                if (this.searchWord) {
                    req.agent_ip = this.searchWord
                }
                this.loading = true
                rbAutoOperationServicesFactory
                    .fetchUserAuthedAgentHostList(req)
                    .then(res => {
                        this.loading = false
                        this.total = res.totalCount
                        this.stepMoreData.serverList = res.dataList
                    })
            },
        }
    }
</script>


<style lang="scss" scoped>
    .icon-input-add {
        font-size: 24px;
        vertical-align: middle;
    }
    .run-code-box {
        width: 70%;
        margin: 0 auto;
    }
    .account-box {
        padding: 15px;
        border: 1px solid $color-border;
        background: $color-bg;
        overflow: hidden;
    }
    .mleft10 {
        margin-left: 20px;
    }
    .red {
        color: $color-tip-red;
    }
    .view-result-btn {
        cursor: pointer;
        width: 25px;
        padding: 5px;
        border: 1px solid $color-common;
        background: $color-bg;
        position: fixed;
        right: 10px;
        top: 45%;
    }
    .fixed-bottom-box {
        position: fixed;
        bottom: 0;
        left: 0;
        right: 0;
        z-index: 999;
        margin-bottom: 0;
        border-top: 1px solid $color-border;
        background: $color-bg;
        box-shadow: 0px 0px 5px 4px rgba(0, 0, 0, 0.1);
    }
    .pbottom50 {
        padding-bottom: 50px;
    }
    .yw-dialog-main.no-scroll {
        overflow: hidden;
    }
    .drawer-close-btn {
        float: right;
        margin: -20px 10px 12px 0;
    }
</style>
