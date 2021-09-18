<template>
    <div class="minheightp100 pbottom30"
         v-loading.fullscreen.lock="pageLoading"
         :element-loading-text="loading_text">
        <div class="components-container yw-dashboard">
            <!-- 基础信息 -->
            <el-form class="components-condition yw-form"
                     :model="basicForm"
                     :rules="basicFormRules"
                     ref="basicForm"
                     :inline="true"
                     label-width="75px">
                <el-form-item label="指令内容"
                              prop="command"
                              label-width="75px">
                    <el-input v-if="!isRunWork"
                              v-model="basicForm.command"
                              placeholder="请输入指令内容"
                              clearable></el-input>
                    <span v-if="isRunWork">{{basicForm.command}}</span>
                </el-form-item>
                <el-form-item label="指令参数"
                              label-width="75px">
                    <el-input v-if="!isRunWork"
                              v-model="basicForm.params"
                              placeholder="请输入指令参数"
                              clearable></el-input>
                    <span v-if="isRunWork">{{basicForm.params}}</span>
                </el-form-item>
                <el-form-item label="指令路径"
                              prop="pathList">
                    <el-tag :key="tag"
                            v-for="tag in basicForm.pathList"
                            :closable="!isRunWork"
                            :disable-transitions="false"
                            @click="handleTagClick(tag)"
                            @close="handleClose(tag)">
                        {{tag}}
                    </el-tag>
                    <el-input class="input-new-tag"
                              v-show="inputVisible"
                              v-model="inputValue"
                              ref="saveTagInput"
                              size="small"
                              @keyup.enter.native="handleInputConfirm"
                              @blur="handleInputConfirm">
                    </el-input>
                    <el-button v-show="!inputVisible && !isRunWork"
                               class="button-new-tag"
                               size="small"
                               @click="showInput">+ 增加指令路径</el-button>
                </el-form-item>
                <el-form-item label="脚本类型"
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
                <el-form-item label="匹配方式"
                              prop="match_type">
                    <el-select v-model="basicForm.match_type"
                               placeholder="请选择"
                               filterable
                               clearable>
                        <el-option v-for="val in matchTypeList"
                                   :key="val.id"
                                   :label="val.label"
                                   :value="val.id"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="响应方式"
                              prop="deal_type">
                    <el-select v-model="basicForm.deal_type"
                               placeholder="请选择"
                               filterable
                               clearable>
                        <el-option v-for="val in dealTypeList"
                                   :key="val.id"
                                   :label="val.label"
                                   :value="val.id"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="指令级别"
                              prop="level_id">
                    <el-select v-model="basicForm.level_id"
                               placeholder="请选择"
                               filterable
                               clearable>
                        <el-option v-for="val in levelList"
                                   :key="val.id"
                                   :label="val.name"
                                   :value="val.id"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="指令分组"
                              prop="label">
                    <el-select v-model="basicForm.label"
                               placeholder="请选择"
                               filterable
                               clearable>
                        <el-option v-for="val in sensitiveList"
                                   :key="val.key"
                                   :label="val.value"
                                   :value="val.key"></el-option>
                    </el-select>
                </el-form-item>

            </el-form>
            <!-- <el-button class="global-variable" disabled>全局变量设置</el-button> -->
            <!-- 匹配规则设置 -->
            <div class="yw-form">
                <section class="mtop20">
                    <div class="table-operate-wrap clearfix">
                        <el-button type="text"
                                   icon="el-icon-plus"
                                   @click="addRow">新建匹配规则</el-button>
                    </div>
                    <div class="mtop10">
                        <el-table :data="stepBlockListHandle"
                                  ref="stepTable"
                                  :row-key="getRowKeys"
                                  :expand-row-keys="expands"
                                  @expand-change="expandSelect"
                                  class="yw-el-table"
                                  stripe
                                  tooltip-effect="dark"
                                  border>
                            <el-table-column label="序号"
                                             type="index"></el-table-column>
                            <el-table-column prop="rule_name"
                                             label="规则名称">
                                <template slot="header">
                                    <span class="required-option">规则名称</span>
                                </template>
                                <template slot-scope="scope">
                                    <el-form class="wp100"
                                             :model="scope.row"
                                             :rules="stepBlockRules"
                                             :ref="'stepBlock' + scope.$index">
                                        <el-form-item prop="rule_name">
                                            <el-input v-model="scope.row.rule_name"
                                                      placeholder="请输入规则名称"
                                                      clearable></el-input>
                                        </el-form-item>
                                    </el-form>
                                </template>
                            </el-table-column>
                            <el-table-column prop="target_host"
                                             label="目标设备数"
                                             width="200">
                                <template slot-scope="scope">
                                    <el-input v-model="scope.row.rule_range.target_host.length"
                                              placeholder="请选择服务器"
                                              readonly></el-input>
                                </template>
                            </el-table-column>
                            <el-table-column label="操作"
                                             fixed="right"
                                             width="240">
                                <template slot-scope="scope">
                                    <div>
                                        <el-button type="text"
                                                   @click="updateStatus(scope.row)">
                                            <span v-if="scope.row.status === 2">启动</span>
                                            <span v-if="scope.row.status === 1">暂停</span>
                                        </el-button>
                                        <el-button type="text"
                                                   @click="cloneRow(scope)">克隆</el-button>
                                        <el-button type="text"
                                                   @click="expandRow(scope)"
                                                   class="mleft10">
                                            <span v-if="activeRowIndex === scope.$index">收起更多</span>
                                            <span v-else>编辑更多</span>
                                        </el-button>
                                        <el-button type="text"
                                                   @click="deleteRow(scope)">删除</el-button>
                                    </div>
                                </template>
                            </el-table-column>
                            <!-- 展开更多信息 -->
                            <el-table-column type="expand"
                                             width="1">
                                <template slot-scope="props">

                                    <!-- 目标机器 -->
                                    <server-info ref="serverForm"
                                                 :rowData="props.row"
                                                 @updateCurrentRow="updateCurrentRow"></server-info>

                                    <!-- 可执行设备账户 -->
                                    <ohter-info v-if="accountList.length"
                                                ref="userForm"
                                                selectType="target_ops_user"
                                                :dataList="accountList"
                                                :listTotal="accountList.length"
                                                :rowData="props.row"
                                                @updateCurrentRow="updateCurrentRow"
                                                @search="search"></ohter-info>

                                    <!-- 可执行系统角色 -->
                                    <!-- <ohter-info v-if="roleList.length"
                            ref="execForm"
                            selectType="exec_role_ids"
                            :dataList="roleList"
                            :listTotal="roleTotal"
                            :rowData="props.row"
                            @updateCurrentRow="updateCurrentRow"
                            @search="search"></ohter-info>
 -->
                                    <!-- 执行审核赋权角色 -->
                                    <!-- <ohter-info v-if="roleList.length"
                            ref="reviewForm"
                            selectType="review_role_ids"
                            :isRequire="isReviewRoleRequire"
                            :dataList="roleList"
                            :listTotal="roleTotal"
                            :rowData="props.row"
                            @updateCurrentRow="updateCurrentRow"
                            @search="search"></ohter-info> -->

                                    <!-- 白名单管理 -->
                                    <white-list ref="serverForm"
                                                :rowData="props.row"
                                                @updateCurrentRow="updateCurrentRow"></white-list>

                                </template>
                            </el-table-column>
                        </el-table>
                    </div>
                </section>
            </div>
            <div class="fixed-bottom-box t-center">
                <el-button @click="save">保存</el-button>
                <el-button type="primary"
                           @click="cancel">取消</el-button>
            </div>
        </div>
    </div>
</template>

<script>
    import serverInfo from './server-info.vue'
    import ohterInfo from './ohter-info.vue'
    import whiteList from './white-list.vue'
    import rbAutoOperationMixin from 'src/services/auto_operation/rb-auto-operation-mixin.js'
    import rbAutoOperationServicesFactory from 'src/services/auto_operation/rb-auto-operation-services.factory.js'
    import rbAutoOperationWorkServicesFactory from 'src/services/auto_operation/rb-auto-operation-work-services.factory.js'
    import rbAutoOperationInstructionServicesFactory from 'src/services/auto_operation/rb-auto-operation-instruction-services.factory.js'

    export default {
        components: {
            serverInfo,
            ohterInfo,
            whiteList,
        },
        props: ['isRunWork'],
        data() {
            return {
                // 分类标签
                labelList: [],
                sensitiveList: [],// 指令分组
                // 获取row的key值
                getRowKeys(row) {
                    return row.rowIndex
                },
                expands: [],
                inputVisible: false,
                inputValue: '',
                // 作业基础信息
                basicForm: {
                    command: '',
                    params: '',
                    content_type: '',
                    match_type: '',
                    deal_type: '',
                    level_id: '',
                    label: '',
                    pathList: [
                    ]
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
                    level_id: [
                        {
                            required: true,
                            message: '请选择指令分级!',
                            trigger: ['blur', 'change']
                        },
                    ],
                    label: [
                        {
                            required: true,
                            message: '请选择指令分组!',
                            trigger: ['blur', 'change']
                        },
                    ],

                    pathList: [
                        {
                            required: true,
                            trigger: 'change',
                            validator: (rule, value, callback) => {
                                if (!value.length) {
                                    callback(new Error('请填写指令路径!'))
                                } else {
                                    callback()
                                }
                            }
                        }
                    ]
                },
                accountList: [],  // 服务器账户
                roleList: [],  // 角色列表
                roleTotal: 0,
                // 校验规则
                stepBlockRules: {
                    rule_name: [
                        {
                            required: true,
                            message: '请输入规则名称!',
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

                // 列表
                stepBlockList: [
                    {
                        rule_name: '',
                        rule_range: {
                            target_host: [],
                            target_ops_user: [],
                            exec_role_ids: [],
                            review_role_ids: [],
                        },
                        white_list: [],
                    }
                ],
                // 新增步骤参数
                defaultStepInfo: {
                    rule_name: '',
                    rule_range: {
                        target_host: [],
                        target_ops_user: [],
                        // exec_role_ids: [],
                        // review_role_ids: [],
                    },
                    white_list: [],
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
                levelList: []
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
                //      return this.basicForm.deal_type === 1
            }
        },
        created() {
            this.initPageInfo()
            this.queryLevelList()
            this.getSensitiveList()
        },
        methods: {
            getSensitiveList() {
                let settingObj = JSON.parse(sessionStorage.getItem('sensitiveLabel'))
                if (settingObj) {
                    Object.keys(settingObj).forEach(item => {
                        this.sensitiveList.push({ 'key': item, 'value': settingObj[item].name })
                    })
                }
            },
            showInput() {
                this.inputVisible = true
                this.$nextTick(() => {
                    this.$refs.saveTagInput.$refs.input.focus()
                })
            },
            handleInputConfirm() {
                let inputValue = this.inputValue
                if (inputValue) {
                    this.basicForm.pathList.push(inputValue)
                }
                this.inputVisible = false
                this.inputValue = ''
            },
            handleClose(tag) {
                this.basicForm.pathList.splice(this.basicForm.pathList.indexOf(tag), 1)
            },
            handleTagClick(tag) {
                this.inputValue = tag
                this.handleClose(tag)
                this.showInput()
            },
            // 获取账户、角色
            getAccountlist(pageSize, currentPage) {
                let req = {
                    page_no: currentPage || this.currentPage,
                    page_size: pageSize || this.pageSize
                }
                this.loading = true
                rbAutoOperationServicesFactory
                    .queryOpsAccountList(req)
                    .then(res => {
                        this.loading = false
                        this.accountList = res
                    })
                    .catch(() => {
                        this.loading = false
                    })
            },
            getRolelist(pageSize, currentPage) {
                let req = {
                    page_no: currentPage || this.currentPage,  // 此处参数pageNo须为驼峰，否则报错，其他为下划线。。。
                    page_size: pageSize || this.pageSize,
                    role_type: 0 // 获取功能角色
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
                this.stepBlockList.push(JSON.parse(JSON.stringify(this.defaultStepInfo)))
            },
            // 更新当前行的信息
            updateCurrentRow(target, key, value) {
                if (key === 'white_list') {
                    this.$set(target, key, value)
                } else {
                    let obj = target.rule_range
                    obj[key] = value
                    this.$set(target, 'rule_range', obj)
                }
            },
            // 折叠面板每次只能展开一行
            expandSelect(row, expandedRows) {
                if (expandedRows.length) {
                    this.expands = []
                    if (row) {
                        this.expands.push(row.rowIndex)
                    }
                } else {
                    this.expands = []
                }
            },
            // 展开行
            expandRow(scope) {
                let rowIndex = scope.$index
                // 没有激活行 || 不同行
                let needToSetIndex = this.activeRowIndex === '' || this.activeRowIndex !== scope.$index
                if (needToSetIndex) {
                    this.activeRowIndex = scope.$index
                } else {
                    this.activeRowIndex = ''
                }

                this.toggleRowExpansion(scope.row, rowIndex)

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
            // 克隆行
            cloneRow(scope) {
                let cloneRow = {
                    rule_name: ''
                }
                this.$confirm('确定克隆该行？').then(() => {
                    cloneRow.rule_range = JSON.parse(JSON.stringify(scope.row.rule_range))
                    this.stepBlockList.push(cloneRow)
                    console.log(this.stepBlockList)
                })
            },
            // 删除行
            deleteRow(scope) {
                let rowIndex = scope.$index
                this.$confirm('确定删除该行？').then(() => {
                    let length = this.stepBlockList.length
                    if (length > 1 && this.activeRowIndex === rowIndex) {
                        this.activeRowIndex = ''
                        this.toggleRowExpansion(scope.row, rowIndex)
                    } else if (length > 1 && this.activeRowIndex > rowIndex) {
                        this.activeRowIndex = this.activeRowIndex - 1
                    }
                    if (length > 1) {
                        this.remove_step_list.push(this.stepBlockList[rowIndex].sensitive_config_id)
                        this.stepBlockList.splice(rowIndex, 1)
                    } else {
                        this.$message.warning('至少填写一条数据！')
                    }
                })
            },

            // 初始化页面信息
            initPageInfo() {
                this.getAccountlist()
                this.getRolelist()
                this.configId && this.getDetail(this.configId)
            },
            // 获取详情
            getDetail(id) {
                this.pageLoading = true
                rbAutoOperationInstructionServicesFactory
                    .getSensitiveConfig({ sensitiveConfigId: id })
                    .then(res => {
                        this.basicForm.command = res.command
                        this.basicForm.params = res.params
                        this.basicForm.content_type = res.content_type
                        this.basicForm.match_type = res.match_type
                        this.basicForm.deal_type = res.deal_type
                        this.basicForm.level_id = res.level_id
                        this.basicForm.label = res.label
                        this.basicForm.pathList = res.path.split(',')
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
            // 打开执行作业弹框
            cancel() {
                this.$router.push('/auto_operation/instruction_manage/list')
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
                    params: this.basicForm.params,
                    content_type: this.basicForm.content_type,
                    match_type: this.basicForm.match_type,
                    deal_type: this.basicForm.deal_type,
                    level_id: this.basicForm.level_id,
                    label: this.basicForm.label,
                    path: this.basicForm.pathList.join(','),
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
                // let self = this, 
                let promiseArr = []
                setTimeout(() => {
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
                for (let block of self.stepBlockListHandle) {
                    // if (this.isReviewRoleRequire && !block.rule_range.review_role_ids.length) {
                    if (this.isReviewRoleRequire) {
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
            // 敏感指令分级列表
            queryLevelList() {
                rbAutoOperationServicesFactory.querySensitiveLevelList().then(res => {
                    if (res.dataList) {
                        this.levelList = res.dataList
                    }
                })
            }
        }
    }
</script>


<style lang="scss" scoped>
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
</style>
