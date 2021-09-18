<template>
    <div class="components-container yw-dashboard run-code-box"
         v-loading.fullscreen.lock="pageLoading"
         :element-loading-text="loading_text">
        <!-- <div v-if="routerQuery.type === 'detail'"
             class="full-mask"></div> -->
        <el-form class="yw-form is-required pbottom50"
                 ref="addForm"
                 :model="addForm"
                 :rules="addFormRules"
                 label-width="100px">
            <el-form-item label="脚本名称"
                          prop="name">
                <el-input v-model="addForm.name"
                          clearable></el-input>
            </el-form-item>
            <el-form-item label="执行账户"
                          v-if="$route.query.currentTitle!=='脚本详情'"
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

            <!-- 选择服务器 -->
            <el-form-item label="目标机器"
                          v-if="$route.query.currentTitle!=='脚本详情'"
                          prop="targetMachines">
                <!-- 选择服务器组件 -->
                <select-servers :dataSelected="addForm.targetMachines"
                                :targetObjectList="target_exec_object"
                                @setSelectedKey="setSelectedKey"
                                @setSelectedService="setSelectedService"></select-servers>
            </el-form-item>

            <!-- 该版本暂不做公共脚本功能20191202  -->
            <!-- :publicCodeShow="true"
      :publicCodeList="publicCodeList"-->
            <code-editor ref="codeEditor"
                         :currentCodeInfo="currentCodeInfo"
                         :fromCodeList="fromCodeList"
                         :codeCloneList="allCodeList"
                         @passCodeContent="passCodeContent"></code-editor>

            <!-- 脚本执行时，填写脚本参数和脚本时间 -->
            <el-form-item label="脚本参数"
                          prop="codeParam">
                <el-input v-model="addForm.codeParam"
                          :type="isSensitivity?'password':'text'"
                          clearable></el-input>
                <el-checkbox v-model="isSensitivity">
                    <el-tooltip class="item"
                                effect="dark"
                                :content="tipContent"
                                placement="right">
                        <i class="el-icon-question"></i>
                    </el-tooltip>
                    <span class="red">*敏感参数</span>
                </el-checkbox>
            </el-form-item>
            <el-form-item label="脚本时间(s)"
                          prop="codeTime">
                <el-input v-model="addForm.codeTime"
                          clearable></el-input>
            </el-form-item>
            <el-form-item label
                          v-if="routerQuery.type !== 'detail'"
                          align="center"
                          class="fixed-bottom-box">
                <el-button @click="saveCode">保存</el-button>
                <el-button type="primary"
                           @click="runCode">执行脚本</el-button>
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
    import codeEditor from '../../components/code-editor.vue'
    import codeRunResult from '../../components/code-run-result.vue'
    import rbAutoOperationMixin from 'src/services/auto_operation/rb-auto-operation-mixin.js'
    import rbAutoOperationServicesFactory from 'src/services/auto_operation/rb-auto-operation-services.factory.js'
    import selectServers from '../../components/select-servers.vue'

    export default {
        name: 'AutoOperationCodeManageRun',
        components: {
            codeEditor,
            codeRunResult,
            selectServers
        },
        data() {
            return {
                target_exec_object: [],
                pageLoading: false,
                loading_text: '请稍候...',
                allCodeList: [],
                // 当前脚本内容
                currentCodeInfo: {
                    codeContent: '',
                    languageType: '',
                    base64Encode: ''
                },
                isSensitivity: false, // 是否敏感参数
                addForm: {
                    name: '',
                    accountSelected: 'aspire',
                    codeParam: '',
                    codeTime: '600',
                    // 目标服务器
                    targetMachines: []
                },
                addFormRules: {
                    name: [
                        {
                            required: true,
                            message: '请先输入脚本名称!',
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
                    // targetMachines: [
                    //     {
                    //         required: true,
                    //         trigger: 'change',
                    //         validator: (rule, value, callback) => {
                    //             if (!value.length) {
                    //                 callback(new Error('请选择目标机器!'))
                    //             } else {
                    //                 callback()
                    //             }
                    //         }
                    //     }
                    // ]
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
                tipContent: '',
                selectedServiceList: []
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
            routerQuery() {
                return this.$route.query
            },
            fromCodeList() {
                return this.routerQuery.type === 'fromCodeList'
            },
        },
        watch: {
            routerQuery: {
                handler() {
                    this.initPageInfo()
                },
                deep: true,
                immediate: true
            },
            'addForm.codeParam': {
                handler(val) {
                    if (val) {
                        let newVal = val.split(' ')
                        let num = 0
                        this.tipContent = ''
                        newVal.forEach(item => {
                            num++
                            this.tipContent += '第' + num + '个参数：' + item + ';'
                        })
                    }
                },
                deep: true,
                immediate: true

            }
        },
        methods: {
            setSelectedKey(data) {
                this.addForm.targetMachines = data
                console.log('this.addForm.targetMachines===', this.addForm.targetMachines)
            },
            setSelectedService(data) {
                this.selectedServiceList = []
                this.selectedServiceList = data
            },
            // 初始化页面信息
            initPageInfo() {
                let query = this.$route.query

                // 来源：脚本管理 => 去执行
                if (query.scriptId) {
                    if (query.historyVal === '1') {
                        this.queryOpsScriptHisById(query.scriptId)
                    } else {
                        this.queryOpsScriptById(query.scriptId)
                    }
                    // 来源：脚本执行历史 => 克隆
                } else if (query.pipelineInstanceId) {
                    this.queryStepInstListByPipelineInstId(query.pipelineInstanceId)
                } else {
                    this.currentCodeInfo = {
                        codeContent: '',
                        languageType: '',
                        base64Encode: ''
                    }
                    this.addForm = {
                        name: '',
                        accountSelected: 'aspire',
                        codeParam: '',
                        codeTime: '600',
                        // 目标服务器
                        targetMachines: []
                    }
                }

                // this.getServerlist()
                this.queryOpsAccountList()
                this.getAllCodeList()

            },
            // 根据ops历史实例查询ops步骤实例列表
            queryStepInstListByPipelineInstId(pipelineInstanceId) {
                rbAutoOperationServicesFactory.queryStepInstListByPipelineInstId(pipelineInstanceId).then(res => {
                    let cloneData = res[0]
                    this.currentCodeInfo.codeContent = cloneData.script_content
                    this.currentCodeInfo.languageType = cloneData.script_content_type
                    this.addForm.name = cloneData.name
                    this.addForm.accountSelected = cloneData.target_ops_user
                    // 初始化目标机器
                    this.addForm.targetMachines = cloneData.target_host_list

                })
            },
            // 登记账户
            registAccount() {
                if (!this.newAccountName) {
                    this.$message('请填写账户名称！')
                    return
                }
            },
            // 传递最新脚本内容
            passCodeContent(data) {
                this.codeInfo = data
            },
            // 保存脚本
            saveCode() {
                if (!this.addForm.name) {
                    this.$message('请输入脚本名称')
                    return
                }
                if (!this.codeInfo.codeContent) {
                    this.$message('请输入脚本内容')
                    return
                }
                this.pageLoading = true
                let req = {
                    step_id: '',
                    label_id: this.codeInfo.labelId,
                    script_id: this.currentCodeInfo.codeId,
                    content_type: this.currentCodeInfo.languageType,
                    script_content: this.codeInfo.codeContent,
                    script_name: this.addForm.name,
                    base64_encode: true,
                    ops_param_reference_list: this.codeInfo.custompParameterList,
                    target_exec_object: this.selectedServiceList
                }
                req = Object.assign(this.codeInfo, req)
                rbAutoOperationServicesFactory
                    .copyScript(req)
                    .then(() => {
                        this.$message('保存成功')
                        this.pageLoading = false
                    })
                    .catch(error => {
                        this.pageLoading = false
                        this.showErrorTip(error)
                    })
            },
            // 执行脚本
            runCode() {
                this.validForm(this.sendRunCodeReq)
            },
            // 校验表单
            validForm(callback) {
                const addFormValid = new Promise((resolve, reject) => {
                    this.$refs.addForm.validate(valid => {
                        if (valid) {
                            resolve()
                        } else {
                            reject()
                        }
                    })
                })
                const codeFormValid = new Promise((resolve, reject) => {
                    this.$refs.codeEditor.$refs.codeForm.validate(valid => {
                        if (valid) {
                            resolve()
                        } else {
                            reject()
                        }
                    })
                })
                Promise.all([addFormValid, codeFormValid])
                    .then(() => {
                        callback && callback()
                    })
                    .catch(() => {
                        this.$message('请先完善信息')
                    })
            },
            // 发起执行脚本请求
            sendRunCodeReq() {
                this.hasRunCode = false
                this.pageLoading = true
                let req = {
                    run_name: this.addForm.name,
                    script: {
                        script_params: this.addForm.codeParam,
                        content_type: this.codeInfo.languageType,
                        script_content: this.codeInfo.codeContent,
                        timeout: this.addForm.codeTime,
                        base64_encode: true
                    },
                    target_host_list: this.targetHostList,
                    target_ops_user: this.addForm.accountSelected,
                    target_exec_object: this.selectedServiceList

                }
                req.script = Object.assign(this.codeInfo, req.script)

                rbAutoOperationServicesFactory.realtimeScriptExecute(req).then(res => {
                    this.pageLoading = false
                    if (res.flag) {
                        this.$message('执行成功')
                        // 弹出执行结果弹框
                        this.stepInstId = res.biz_data
                        this.hasRunCode = true
                        this.resultBoxShow = true
                    } else {
                        this.pageLoading = false
                        this.showErrorTip(res)
                    }
                })
                    .catch(error => {
                        this.pageLoading = false
                        this.showErrorTip(error)
                    })
            },
            // 查询执行账户列表
            queryOpsAccountList() {
                rbAutoOperationServicesFactory.queryOpsAccountList().then(res => {
                    this.accountList = res
                    // 暂时伪造一个root账户来执行脚本
                    // this.accountList.push({
                    //     accountName: 'root',
                    //     create_time: '2019-12-03 16:08:19',
                    //     creater: 'alauda'
                    // })
                })
            },
            // 根据id查询脚本内容
            queryOpsScriptById(scriptId) {
                rbAutoOperationServicesFactory.queryOpsScriptById(scriptId).then(res => {
                    this.currentCodeInfo.codeContent = res.script_content
                    this.currentCodeInfo.languageType = res.content_type
                    this.currentCodeInfo.ops_param_reference_list = res.ops_param_reference_list
                    this.currentCodeInfo.codeId = res.script_id
                    this.currentCodeInfo = this.$utils.deepClone(Object.assign(this.currentCodeInfo, res))
                    this.addForm.name = res.script_name
                })
            },
            // 根据id查询脚本历史内容
            queryOpsScriptHisById(scriptId) {
                rbAutoOperationServicesFactory.queryOpsScriptHisById(scriptId).then(res => {
                    this.currentCodeInfo.codeContent = res.script_content
                    this.currentCodeInfo.languageType = res.content_type
                    this.currentCodeInfo.codeId = res.script_id
                    this.currentCodeInfo.base64Encode = res.base64_encode
                    this.currentCodeInfo = this.$utils.deepClone(Object.assign(this.currentCodeInfo, res))
                    this.addForm.name = res.script_name
                })
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
    .split-box {
        padding: 10px;
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
        padding-bottom: 80px;
    }
    .yw-dialog-main.no-scroll {
        overflow: hidden;
    }
    .drawer-close-btn {
        float: right;
        margin: -20px 10px 12px 0;
    }
    .full-mask {
        width: 100%;
        height: 100%;
        background: rgba(0, 0, 0, 0);
        z-index: 9999;
        position: absolute;
    }
</style>
