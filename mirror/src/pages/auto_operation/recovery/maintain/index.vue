<template>
    <div class="components-container yw-form pbottom"
         v-loading.fullscreen.lock="pageLoading"
         :element-loading-text="loading_text">
        <!-- 基础信息 -->
        <div class="relative">
            <el-form class="mbottom0"
                     :model="basicForm"
                     :rules="basicFormRules"
                     ref="basicForm"
                     :inline="true"
                     label-width="75px">
                <el-form-item label="规则名称"
                              prop="command"
                              label-width="75px">
                    <el-input v-model="basicForm.command"
                              placeholder="请输入规则名称"
                              clearable></el-input>
                </el-form-item>
                <el-form-item label="规则类型"
                              prop="content_type">
                    <el-select v-model="basicForm.content_type"
                               placeholder="请选择"
                               filterable
                               clearable>
                        <el-option v-for="val in languageTypeList"
                                   :key="val.id"
                                   :label="val.label"
                                   :value="val.id"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="规则描述"
                              prop="command"
                              label-width="75px">
                    <el-input v-model="basicForm.command"
                              placeholder="请输入规则描述"
                              clearable></el-input>
                </el-form-item>
            </el-form>
            <!-- 步骤1： 故障告警指标项 -->
            <div class="mtop10">
                步骤1：
                <transfer-box :dataList="dataList"></transfer-box>

                <el-row :gutter="10"
                        type="flex"
                        align="middle"
                        class="recovery-container mtop10 hide">
                    <el-col :span="8">
                        <!-- 左容器 -->
                        <div class="border-container">
                            <el-row :gutter="10"
                                    type="flex"
                                    align="middle"
                                    class="recovery-container">
                                <el-col :span="12">
                                    故障告警指标项
                                </el-col>
                                <el-col :span="12">
                                    <el-input v-model="basicForm.command"
                                              placeholder="请输入搜索内容">
                                        <i slot="suffix"
                                           class="el-input__icon el-icon-search"
                                           @click="searchList"></i>
                                    </el-input>
                                </el-col>
                            </el-row>
                            <div class="yw-el-table-wrap mtop20">
                                <el-table :data="dataList"
                                          row-key="command"
                                          class="yw-el-table"
                                          stripe
                                          tooltip-effect="dark"
                                          height="200px"
                                          @selection-change="handleSelectionChange"
                                          v-loading="loading">
                                    <el-table-column type="selection"
                                                     :selectable="handleSelectable"
                                                     :reserve-selection="true"
                                                     width="42"></el-table-column>
                                    <el-table-column type="index"
                                                     label="序号"
                                                     width="45"></el-table-column>
                                    <el-table-column prop="command"
                                                     label="告警指标"
                                                     min-width="130"
                                                     show-overflow-tooltip></el-table-column>
                                    <el-table-column prop="creater"
                                                     label="指标来源"
                                                     show-overflow-tooltip></el-table-column>
                                </el-table>
                            </div>
                        </div>
                    </el-col>
                    <el-col :span="1">
                        <!-- 箭头按钮 -->
                        <div class="arrow-right">
                            <i class="el-icon-right"></i>
                        </div>
                    </el-col>
                    <el-col :span="15">
                        <!-- 右容器 -->
                        <div class="border-container">
                            <el-row :gutter="10"
                                    type="flex"
                                    align="middle"
                                    class="recovery-container">
                                <el-col :span="12">
                                    已选
                                    <el-button type="text"
                                               class="mleft5"
                                               @click="editRow(scope.row)">清空已选</el-button>
                                </el-col>
                                <el-col :span="12">
                                    <el-input v-model="basicForm.command"
                                              placeholder="请输入搜索内容">
                                        <i slot="suffix"
                                           class="el-input__icon el-icon-search"
                                           @click="searchList"></i>
                                    </el-input>
                                </el-col>
                            </el-row>
                            <div class="yw-el-table-wrap mtop20">
                                <el-table :data="dataList"
                                          row-key="command"
                                          class="yw-el-table"
                                          stripe
                                          tooltip-effect="dark"
                                          height="200px"
                                          @selection-change="handleSelectionChange"
                                          v-loading="loading">
                                    <el-table-column type="selection"
                                                     :selectable="handleSelectable"
                                                     :reserve-selection="true"
                                                     width="42"></el-table-column>
                                    <el-table-column type="index"
                                                     label="序号"
                                                     width="45"></el-table-column>
                                    <el-table-column prop="command"
                                                     label="告警指标"
                                                     min-width="130"
                                                     show-overflow-tooltip></el-table-column>
                                    <el-table-column prop="creater"
                                                     label="指标来源"
                                                     show-overflow-tooltip></el-table-column>
                                </el-table>
                            </div>
                        </div>
                    </el-col>
                </el-row>
            </div>
            <!-- 步骤2： 故障自愈作业 -->
            <div class="mtop10">
                步骤2：
                <el-row :gutter="10"
                        type="flex"
                        align="middle"
                        class="recovery-container mtop10 hide">
                    <el-col :span="8">
                        <!-- 左容器 -->
                        <div class="border-container">
                            <el-row :gutter="10"
                                    type="flex"
                                    align="middle"
                                    class="recovery-container">
                                <el-col :span="12">
                                    故障告警指标项
                                </el-col>
                                <el-col :span="12">
                                    <el-input v-model="basicForm.command"
                                              placeholder="请输入搜索内容">
                                        <i slot="suffix"
                                           class="el-input__icon el-icon-search"
                                           @click="searchList"></i>
                                    </el-input>
                                </el-col>
                            </el-row>
                            <div class="yw-el-table-wrap mtop20">
                                <el-table :data="dataList"
                                          row-key="command"
                                          class="yw-el-table"
                                          stripe
                                          tooltip-effect="dark"
                                          height="200px"
                                          @selection-change="handleSelectionChange"
                                          v-loading="loading">
                                    <el-table-column type="selection"
                                                     :selectable="handleSelectable"
                                                     :reserve-selection="true"
                                                     width="42"></el-table-column>
                                    <el-table-column type="index"
                                                     label="序号"
                                                     width="45"></el-table-column>
                                    <el-table-column prop="command"
                                                     label="告警指标"
                                                     min-width="130"
                                                     show-overflow-tooltip></el-table-column>
                                    <el-table-column prop="creater"
                                                     label="指标来源"
                                                     show-overflow-tooltip></el-table-column>
                                </el-table>
                            </div>
                        </div>
                    </el-col>
                    <el-col :span="1">
                        <div class="arrow-right">
                            <i class="el-icon-right"></i>
                        </div>
                    </el-col>
                    <el-col :span="15">
                        <!-- 右容器 -->
                        <div class="border-container">
                            <el-row :gutter="10"
                                    type="flex"
                                    align="middle"
                                    class="recovery-container">
                                <el-col :span="12">
                                    已选
                                    <el-button type="text"
                                               class="mleft5"
                                               @click="editRow(scope.row)">清空已选</el-button>
                                </el-col>
                                <el-col :span="12">
                                    <el-input v-model="basicForm.command"
                                              placeholder="请输入搜索内容">
                                        <i slot="suffix"
                                           class="el-input__icon el-icon-search"
                                           @click="searchList"></i>
                                    </el-input>
                                </el-col>
                            </el-row>
                            <div class="yw-el-table-wrap mtop20">
                                <el-table :data="dataList"
                                          row-key="command"
                                          class="yw-el-table"
                                          stripe
                                          tooltip-effect="dark"
                                          height="200px"
                                          @selection-change="handleSelectionChange"
                                          v-loading="loading">
                                    <el-table-column type="selection"
                                                     :selectable="handleSelectable"
                                                     :reserve-selection="true"
                                                     width="42"></el-table-column>
                                    <el-table-column type="index"
                                                     label="序号"
                                                     width="45"></el-table-column>
                                    <el-table-column prop="command"
                                                     label="告警指标"
                                                     min-width="130"
                                                     show-overflow-tooltip></el-table-column>
                                    <el-table-column prop="creater"
                                                     label="指标来源"
                                                     show-overflow-tooltip></el-table-column>
                                </el-table>
                            </div>
                        </div>
                    </el-col>
                </el-row>
            </div>
            <!-- 步骤3： 故障自愈过程 -->
        </div>
        <!-- 规则设置 -->
        <div class="fixed-bottom-box t-center">
            <el-button @click="save">保存</el-button>
            <el-button type="primary"
                       @click="cancel">取消</el-button>
        </div>
    </div>
</template>

<script>
    import rbAutoOperationMixin from 'src/services/auto_operation/rb-auto-operation-mixin.js'
    import rbAutoOperationServicesFactory from 'src/services/auto_operation/rb-auto-operation-services.factory.js'
    import rbAutoOperationWorkServicesFactory from 'src/services/auto_operation/rb-auto-operation-work-services.factory.js'
    import rbAutoOperationInstructionServicesFactory from 'src/services/auto_operation/rb-auto-operation-instruction-services.factory.js'

    export default {
        props: ['dataList'],
        components: {
            transferBox: () => import('./transfer-box.vue')
        },
        data() {
            return {
                dataStep01: {},
                valueStep01: [],

                // 分类标签
                labelList: [],
                expands: [],

                // 作业基础信息
                basicForm: {
                    command: '',
                    content_type: '',
                    match_type: '',
                    deal_type: '',
                },
                // 基本信息校验规则
                basicFormRules: {
                    command: [
                        {
                            required: true,
                            message: '请先输入作业名称!',
                            trigger: ['blur', 'change']
                        },
                        {
                            min: 2,
                            max: 100,
                            message: '长度在 2 到 100 个字符!',
                            trigger: ['blur', 'change']
                        }
                    ],
                    content_type: [
                        {
                            required: true,
                            message: '请选择脚本类型!',
                            trigger: ['blur', 'change']
                        },
                    ],
                    match_type: [
                        {
                            required: true,
                            message: '请选择匹配类型!',
                            trigger: ['blur', 'change']
                        },
                    ],
                    deal_type: [
                        {
                            required: true,
                            message: '请选择响应类型!',
                            trigger: ['blur', 'change']
                        },
                    ],
                },
                accountList: [],  // 服务器账户
                roleList: [],  // 角色列表
                roleTotal: 0,

                // 列表
                stepBlockList: [
                    {
                        rule_name: '',
                        rule_range: {
                            target_host: [],
                            target_ops_user: [],
                            exec_role_ids: [],
                            review_role_ids: [],
                        }
                    }
                ],
                // 新增步骤参数
                defaultStepInfo: {
                    rule_name: '',
                    rule_range: {
                        target_host: [],
                        target_ops_user: [],
                        exec_role_ids: [],
                        review_role_ids: [],
                    }
                },
                activeRowIndex: '', // 当前激活行下的激活信息

                pageLoading: false,
                loading_text: '请稍候...',
                allCodeList: [],
                // 当前脚本内容
                currentCodeInfo: {
                    codeContent: '',
                    languageType: ''
                },
                isSensitivity: false, // 是否敏感参数
                addForm: {
                    name: '',
                },
                addFormRules: {
                    name: [
                        {
                            required: true,
                            message: '请先输入规则名称!',
                            trigger: ['blur', 'change']
                        },
                        {
                            min: 2,
                            max: 100,
                            message: '长度在 2 到 100 个字符!',
                            trigger: ['blur', 'change']
                        }
                    ],
                },

                codeCloneList: [], // 脚本克隆列表
                publicCodeList: [], // 公共脚本列表
                codeInfo: {}, // 脚本弹框最新内容
                accountBoxShow: false, // 登记账户
                newAccountName: '', // 新登记账户名称
                chooseMachineType: 1, // 选择主机方式
                serverList: [], // 主机列表
                serverTotal: 0,
                serversBoxShow: false, // 主机选择弹框
                resultBoxShow: false, // 执行结果弹框
                hasRunCode: false, // 显示执行结果按钮
                stepInstId: '', // 步骤实例ID
                searchWord: '',

                isEditMoreValid: true, // 脚本类型【编辑更多】信息已全部校验通过
                isFileEditMoreValid: true, // 文件分发类型【编辑更多】信息已全部校验通过
                save_pipeline_id: '',  // 保存后，缓存作业id
                gotoRunWork: false, // 去执行作业
                remove_step_list: [], // 删除的步骤ids

                // 指令内容
                languageTypeList: [
                    {
                        id: 1,
                        label: 'shell'
                    },
                    {
                        id: 2,
                        label: 'bat'
                    },
                    {
                        id: 3,
                        label: 'python'
                    },
                ],
                matchTypeList: [
                    {
                        id: 1,
                        label: '完全匹配'
                    },
                    {
                        id: 2,
                        label: '正则匹配'
                    },
                ],
                dealTypeList: [
                    {
                        id: 1,
                        label: '审核'
                    },
                    {
                        id: 2,
                        label: '阻断'
                    },
                ],
            }
        },
        mixins: [rbAutoOperationMixin],
        computed: {
            // 为每行增加rowIndex属性，新增行时需用到
            stepBlockListHandle() {
                this.stepBlockList.forEach((item, index) => {
                    // 添加唯一 rowIndex
                    item.rowIndex = index
                })
                return this.stepBlockList
            },
            // 当前id
            configId() {
                return this.$route.query.id || ''
            },
            // 响应方式为审核时，执行审核赋权角色必选
            isReviewRoleRequire() {
                return this.basicForm.deal_type === 1
            }
        },
        created() {
        },
        methods: {
            getRolelist(pageSize, currentPage) {
                let req = {
                    pageNo: currentPage || this.currentPage,  // 此处参数pageNo须为驼峰，否则报错，其他为下划线。。。
                    pageSize: pageSize || this.pageSize,
                    roleType: 0 // 获取功能角色
                }
                this.loading = true
                rbAutoOperationInstructionServicesFactory
                    .queryRoleList(req)
                    .then(res => {
                        this.loading = false
                        this.roleTotal = res.count
                        this.roleList = res.result
                    })
                    .catch(() => {
                        this.loading = false
                    })
            },


            // 脚本【编辑更多】信息已全部校验通过
            updateScriptEditMoreValid() {
                this.isEditMoreValid = true
            },
            // 增加脚本
            addRow() {
                this.stepBlockList.push(this.defaultStepInfo)
            },
            // 更新当前行的信息
            updateCurrentRow(target, key, value) {
                let obj = target.rule_range
                obj[key] = value
                this.$set(target, 'rule_range', obj)
            },
            // 折叠面板每次只能展开一行
            expandSelect(row, expandedRows) {
                if (expandedRows.length) {
                    this.expands = []
                    if (row) {
                        this.activeRowIndex = row.rowIndex
                        this.expands.push(row.rowIndex)
                    }
                } else {
                    this.activeRowIndex = ''
                    this.expands = []
                }
            },
            // 展开行
            expandRow(scope) {
                // 没有激活行 || 不同行
                let needToSetIndex = this.activeRowIndex === '' || this.activeRowIndex !== scope.$index
                if (needToSetIndex) {
                    this.activeRowIndex = scope.$index
                } else {
                    this.activeRowIndex = ''
                }

                this.toggleRowExpansion(scope.row)

            },
            toggleRowExpansion(row) {
                this.$refs['stepTable'].toggleRowExpansion(row)
            },

            // 敏感指令规则状态更新
            updateStatus(row) {
                // 1：启动 2：禁用
                let req = {
                    status: row.status === 1 ? 2 : 1,
                    sensitiveRuleId: row.sensitive_rule_id
                }
                this.$confirm('确定更新操作？').then(() => {
                    this.pageLoading = true
                    rbAutoOperationInstructionServicesFactory.updateStatusByRuleId(req).then(res => {
                        if (res.flag) {
                            this.$message.success('更新成功！')
                            row.status = req.status
                        } else {
                            this.$message.error(res.error_tip)
                        }
                        this.pageLoading = false
                    })
                })
            },

            // 获取详情
            getDetail(id) {
                this.pageLoading = true
                rbAutoOperationInstructionServicesFactory
                    .getSensitiveConfig({ sensitiveConfigId: id })
                    .then(res => {
                        this.basicForm.command = res.command
                        this.basicForm.content_type = res.content_type
                        this.basicForm.match_type = res.match_type
                        this.basicForm.deal_type = res.deal_type

                        let ruleList = res.rule_list
                        ruleList.map(item => {
                            return item.rule_range = JSON.parse(item.rule_range)
                        })
                        this.stepBlockList = ruleList
                        this.pageLoading = false
                    })
                    .catch(error => {
                        this.showErrorTip(error)
                        this.pageLoading = false
                    })
            },

            searchList() {
                console.log('search')
            },
            // 关闭弹框
            cancel() {
                this.$emit('cancel')
            },
            // 保存作业
            save() {
                this.gotoRunWork = false
                this.validForm(this.sendSaveWorkReq)
            },
            // 处理保存参数数据
            handleRangeToString() {
                let arr = JSON.parse(JSON.stringify(this.stepBlockListHandle))
                arr.forEach(item => {
                    item.rule_range = JSON.stringify(item.rule_range)
                })
                return arr
            },
            // 发起保存的请求
            sendSaveWorkReq() {
                this.pageLoading = true
                let req = {
                    command: this.basicForm.command,
                    content_type: this.basicForm.content_type,
                    match_type: this.basicForm.match_type,
                    deal_type: this.basicForm.deal_type,
                    // 需要保存的规则列表
                    rule_list: this.handleRangeToString(),
                }
                this.configId && (req.sensitive_config_id = this.configId)
                rbAutoOperationInstructionServicesFactory
                    .saveSensitiveConfig(req)
                    .then(res => {
                        if (res.flag) {
                            this.$message('保存成功')
                            this.$router.push('/auto_operation/instruction_manage/list')
                        } else {
                            this.$message.error(res.error_tip || '保存失败')
                        }
                        this.pageLoading = false
                    })
                    .catch(error => {
                        this.pageLoading = false
                        this.showErrorTip(error)
                    })
            },
            // 校验表单
            validForm(callback) {
                let promiseArr = []
                // 基本信息
                const basicFormValid = new Promise((resolve, reject) => {
                    this.$refs.basicForm.validate(valid => {
                        if (valid) {
                            resolve()
                        } else {
                            reject()
                        }
                    })
                })
                promiseArr.push(basicFormValid)

                // 校验每行规则名称
                this.stepBlockList.forEach((item, index) => {
                    const formValid = new Promise((resolve, reject) => {
                        this.$refs['stepBlock' + index].validate(valid => {
                            if (valid) {
                                resolve()
                            } else {
                                reject()
                            }
                        })
                    })
                    promiseArr.push(formValid)
                })

                Promise.all(promiseArr)
                    .then(() => {
                        // 【编辑更多】默认为非必选项，只有【执行审核赋权角色】在响应方式为审核时候，才需要选择
                        if (this.isReviewRoleRequire) {
                            this.validRowMore(callback)
                        } else {
                            callback && callback()
                        }
                    })
                    .catch(() => {
                        this.$message.warning('请先完善信息')
                    })
            },
            triggerValidMore() {
                let promiseArr = []
                setTimeout(() => {
                    // let formArr = ['serverForm', 'userForm', 'execForm', 'reviewForm']
                    let formArr = ['reviewForm']
                    formArr.forEach(item => {
                        const formValid = new Promise((resolve, reject) => {
                            this.$refs[item].$refs['addForm'].validate(valid => {
                                if (valid) {
                                    resolve()
                                } else {
                                    reject()
                                }
                            })
                        })
                        promiseArr.push(formValid)
                    })

                    Promise.all(promiseArr)
                        .then(() => {
                            // this.$message("已完善信息")
                        })
                        .catch(() => {
                            this.$message.warning('请先完善信息')
                        })
                }, 0)
            },
            // 校验每行的【编辑更多】信息
            validRowMore(callback) {
                let self = this
                self.isEditMoreValid = true
                // self.stepBlockListHandle.forEach((stepBlock, stepIndex) => {
                for (let block of self.stepBlockListHandle) {
                    if (this.isReviewRoleRequire && !block.rule_range.review_role_ids.length) {
                        // 如果有脚本类型，且没有填写、设置为校验不通过。
                        self.isEditMoreValid = false
                        self.expands = []
                        self.expands.push(block.rowIndex)

                        // 校验目标服务器、脚本内容
                        self.triggerValidMore()
                        break
                    }
                }
                setTimeout(() => {
                    if (self.isEditMoreValid) {
                        callback && callback()
                    }
                }, 100)
            },
            // 查询分类标签
            queryOpsLabelList() {
                rbAutoOperationWorkServicesFactory
                    .queryOpsLabelList()
                    .then(res => {
                        this.labelList = res
                    })
            },
            // 查询主机列表
            getServerslist(pageSize, currentPage) {
                let req = {
                    page_no: currentPage || this.currentPage,
                    page_size: pageSize || this.pageSize
                }
                this.loading = true
                rbAutoOperationServicesFactory
                    .fetchUserAuthedAgentHostList(req)
                    .then(res => {
                        this.loading = false
                        this.serverTotal = res.totalCount
                        this.serverList = res.dataList
                    })
            },
            // 查询执行账户列表
            queryOpsAccountList() {
                rbAutoOperationServicesFactory.queryOpsAccountList().then(res => {
                    this.accountList = res
                    // 暂时伪造一个root账户来执行脚本
                    this.accountList.push({
                        accountName: 'root',
                        create_time: '2019-12-03 16:08:19',
                        creater: 'alauda'
                    })
                })
            },
            // 翻页触发
            search(searchType, pageSize, currentPage) {
                this[searchType](pageSize, currentPage)
            },
            // 选中表格行
            toggleRow(row) {
                this.$refs.serverTable.toggleRowSelection(row, true)
            },
        }
    }
</script>


<style lang="scss" scoped>
    @import "../../assets/global.scss";

    .global-variable {
        position: absolute;
        top: 50%;
        right: 0;
        transform: translate(0, -50%);
    }

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
    .pbottom {
        padding-bottom: 110px;
    }
    .yw-dialog-main.no-scroll {
        overflow: hidden;
    }
    .drawer-close-btn {
        float: right;
        margin: -20px 10px 12px 0;
    }

    // 故障自愈
    .border-container {
        border: 1px solid $color-border;
        padding: 10px 5px;
    }
    .arrow-right {
        width: 30px;
        height: 30px;
        line-height: 30px;
        border: 1px solid rgb(83, 96, 128);
        border-radius: 50%;
        text-align: center;
        cursor: pointer;
        .el-icon-right {
            font-size: 18px;
        }
        &:hover {
            border: 1px solid #46bafe;
            .el-icon-right {
                color: #46bafe;
            }
        }
    }
    .recovery-container .el-input .el-input__icon {
        line-height: 26px;
    }
</style>
