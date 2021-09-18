<template>
    <div class="components-container yw-dashboard"
         v-loading.fullscreen.lock="pageLoading"
         :element-loading-text="loading_text">
        <!-- 作业基础信息 -->
        <div class="relative">
            <table class="bordered">
                <tr>
                    <td width="90">作业名称</td>
                    <td>{{basicForm.pipeline_name}}</td>
                    <td width="90">分类标签</td>
                    <td>{{basicForm.label_id}}</td>
                </tr>
            </table>
            <!-- <el-button class="global-variable" disabled>全局变量设置</el-button> -->
        </div>
        <!-- 作业步骤设置 -->
        <div>
            <section class="mtop20"
                     v-for="(stepBlock, stepIndex) in stepBlockListHandle"
                     :key="stepBlock[0] && stepBlock[0].pipeline_instance_id">
                <!-- // 操作类型  0 脚本  1 文件分发 -->
                <el-row v-if="stepBlock[0].ops_type === 0"
                        type="flex"
                        :gutter="15"
                        align="top"
                        justify="center">
                    <el-col :span="1">
                        <div class="split-num">{{stepIndex+1}}</div>
                        <!-- <div class="split-line" :class="{'last-child' : stepIndex+1 === stepBlockList.length}"></div> -->
                        <div class="split-line"
                             v-if="stepIndex+1 < stepBlockList.length"></div>
                    </el-col>
                    <el-col :span="23">
                        <div class="step-block relative">
                            <div class="block-type">
                                <div>脚本</div>
                            </div>

                            <el-form class="yw-form wp100"
                                     :model="stepFormList[stepIndex]"
                                     :ref="'stepForm' + stepIndex"
                                     :inline="true"
                                     label-width="65px">
                                <div class="step-title-box relative">
                                    <el-form-item label="步骤块名称："
                                                  prop="block_name"
                                                  label-width="90px">
                                        <span>{{stepFormList[stepIndex].block_name}}</span>
                                    </el-form-item>
                                </div>
                            </el-form>
                            <div class="mtop10">
                                <el-table :data="stepBlock"
                                          :ref="'stepTable' + stepIndex"
                                          :row-key="getRowKeys"
                                          :expand-row-keys="expands"
                                          @expand-change="expandSelect"
                                          @selection-change="(val) => {
                                              return handleSelectionChange(val, stepIndex)
                                          }"
                                          class="yw-el-table"
                                          stripe
                                          tooltip-effect="dark"
                                          border>
                                    <el-table-column label="选择"
                                                     type="selection"></el-table-column>
                                    <el-table-column label="序号"
                                                     type="index"></el-table-column>
                                    <el-table-column prop="name"
                                                     width="260"
                                                     show-overflow-tooltip
                                                     label="脚本名称">
                                    </el-table-column>
                                    <el-table-column prop="target_ops_user"
                                                     label="服务器账户">
                                    </el-table-column>
                                    <el-table-column prop="target_hosts"
                                                     label="服务器数">
                                        <template slot-scope="scope">
                                            {{scope.row.target_hosts.length}}
                                        </template>
                                    </el-table-column>
                                    <el-table-column prop="script_param"
                                                     label="脚本参数"
                                                     min-width="110">
                                        <template slot-scope="scope">
                                            {{scope.row.script_param}}
                                            <span class="red mleft10"
                                                  v-if="scope.row.param_sensive_flag">敏感参数</span>
                                        </template>
                                    </el-table-column>
                                    <el-table-column label="操作"
                                                     min-width="90">
                                        <template slot-scope="scope">
                                            <div>
                                                <el-button type="text"
                                                           @click="expandRow(scope, stepIndex)">
                                                    <span v-if="activeStepIndex === stepIndex && activeRowIndex === scope.$index">收起更多</span>
                                                    <span v-else>展开更多</span>
                                                </el-button>
                                            </div>
                                        </template>
                                    </el-table-column>
                                    <!-- 展开更多信息 -->
                                    <el-table-column type="expand"
                                                     width="1">
                                        <template slot-scope="props">
                                            <!-- 脚本 -->
                                            <step-info-script ref="scriptCom"
                                                              :stepMoreData="stepMoreData"
                                                              :rowData="props.row"
                                                              :isPreview="true"></step-info-script>
                                        </template>
                                    </el-table-column>
                                </el-table>
                            </div>

                        </div>
                    </el-col>
                </el-row>
                <el-row v-else-if="stepBlock[0].ops_type === 1"
                        type="flex"
                        :gutter="15"
                        align="top"
                        justify="center">
                    <el-col :span="1">
                        <div class="split-num">{{stepIndex+1}}</div>
                        <div class="split-line"
                             v-if="stepIndex+1 !== stepBlockList.length"></div>
                    </el-col>
                    <el-col :span="23">
                        <div class="step-block relative">
                            <!-- // 操作类型  0 脚本  1 文件分发 -->
                            <div class="block-type file">
                                <div>文件分发</div>
                            </div>

                            <el-form class="yw-form wp100"
                                     :model="stepFormList[stepIndex]"
                                     :ref="'stepForm' + stepIndex"
                                     :inline="true"
                                     label-width="65px">
                                <div class="step-title-box relative">
                                    <el-form-item label="步骤块名称："
                                                  prop="block_name"
                                                  label-width="90px">
                                        <span>{{stepFormList[stepIndex].block_name}}</span>
                                    </el-form-item>
                                </div>
                            </el-form>
                            <div class="mtop10">
                                <el-table :data="stepBlock"
                                          :ref="'stepTable' + stepIndex"
                                          :row-key="getRowKeys"
                                          :expand-row-keys="expands"
                                          @expand-change="expandSelect"
                                          @selection-change="(val) => {
                                              return handleSelectionChange(val, stepIndex)
                                          }"
                                          class="yw-el-table"
                                          stripe
                                          tooltip-effect="dark"
                                          border>
                                    <el-table-column label="选择"
                                                     type="selection"></el-table-column>
                                    <el-table-column label="序号"
                                                     type="index"></el-table-column>
                                    <el-table-column prop="name"
                                                     show-overflow-tooltip
                                                     width="260"
                                                     label="任务名称">
                                        <template slot="header">
                                            <span class="required-option">任务名称</span>
                                        </template>
                                    </el-table-column>
                                    <el-table-column prop="target_ops_user"
                                                     label="服务器账户">
                                        <template slot="header">
                                            <span class="required-option">服务器账户</span>
                                        </template>
                                    </el-table-column>
                                    <el-table-column prop="target_hosts"
                                                     label="服务器数">
                                        <template slot-scope="scope">
                                            {{scope.row.target_hosts.length}}
                                        </template>
                                    </el-table-column>
                                    <el-table-column prop="file_target_path"
                                                     label="目标路径"
                                                     show-overflow-tooltip
                                                     min-width="160">
                                    </el-table-column>
                                    <el-table-column label="操作"
                                                     min-width="90">
                                        <template slot-scope="scope">
                                            <div>
                                                <el-button type="text"
                                                           @click="expandRow(scope, stepIndex)">
                                                    <span v-if="activeStepIndex === stepIndex && activeRowIndex === scope.$index">收起更多</span>
                                                    <span v-else>展开更多</span>
                                                </el-button>
                                            </div>
                                        </template>
                                    </el-table-column>
                                    <!-- 展开更多信息 -->
                                    <el-table-column type="expand"
                                                     width="1">
                                        <template slot-scope="props">
                                            <!-- 文件分发 -->
                                            <step-info-file :stepMoreData="stepMoreData"
                                                            :rowData="props.row"
                                                            :isPreview="true"
                                                            :isFromWorkEdit="true"></step-info-file>
                                        </template>
                                    </el-table-column>
                                </el-table>
                            </div>

                        </div>
                    </el-col>
                </el-row>
                <el-row v-else-if="stepBlock[0].ops_type === 2"
                        type="flex"
                        :gutter="15"
                        align="top"
                        justify="center">
                    <el-col :span="1">
                        <div class="split-num">{{stepIndex+1}}</div>
                        <div class="split-line"
                             v-if="stepIndex+1 !== stepBlockList.length"></div>
                    </el-col>
                    <el-col :span="23">
                        <div class="step-block relative">
                            <!-- // 操作类型  0 脚本  1 文件分发  2 结果文件保存 -->
                            <div class="block-type download">
                                <div>结果保存</div>
                            </div>

                            <el-form class="yw-form wp100"
                                     :model="stepFormList[stepIndex]"
                                     :ref="'stepForm' + stepIndex"
                                     :inline="true"
                                     label-width="65px">
                                <div class="step-title-box relative">
                                    <el-form-item label="步骤块名称："
                                                  prop="block_name"
                                                  label-width="90px">
                                        <span>{{stepFormList[stepIndex].block_name}}</span>
                                    </el-form-item>
                                </div>
                            </el-form>
                            <div class="mtop10">
                                <el-table :data="stepBlock"
                                          :ref="'stepTable' + stepIndex"
                                          :row-key="getRowKeys"
                                          :expand-row-keys="expands"
                                          @expand-change="expandSelect"
                                          @selection-change="(val) => {
                                              return handleSelectionChange(val, stepIndex)
                                          }"
                                          class="yw-el-table"
                                          stripe
                                          tooltip-effect="dark"
                                          border>
                                    <el-table-column label="选择"
                                                     type="selection"></el-table-column>
                                    <el-table-column label="序号"
                                                     type="index"></el-table-column>
                                    <el-table-column prop="name"
                                                     show-overflow-tooltip
                                                     width="260"
                                                     label="任务名称">
                                        <template slot="header">
                                            <span class="required-option">任务名称</span>
                                        </template>
                                    </el-table-column>
                                    <el-table-column prop="target_ops_user"
                                                     label="服务器账户">
                                        <template slot="header">
                                            <span class="required-option">服务器账户</span>
                                        </template>
                                    </el-table-column>
                                    <el-table-column prop="target_hosts"
                                                     label="服务器数">
                                        <template slot-scope="scope">
                                            {{scope.row.target_hosts.length}}
                                        </template>
                                    </el-table-column>
                                    <el-table-column prop="file_store_converge_type"
                                                 label="汇聚类型"
                                                 min-width="110">
                                        <template slot-scope="scope">
                                          
                                                      <el-select v-model="scope.row.file_store_converge_type"
                                                               placeholder="请选择汇聚类型"
                                                               filterable
                                                               style="width:180px;">
                                                        <el-option v-for="val in convergeType"
                                                                   :key="val.key"
                                                                   :label="val.value"
                                                                   :value="val.key"></el-option>
                                                    </el-select>
                                            <el-checkbox v-model="scope.row.file_store_safety">是否安全</el-checkbox>
                                        </template>
                                    </el-table-column>
                                    <el-table-column label="操作"
                                                     min-width="90">
                                        <template slot-scope="scope">
                                            <div>
                                                <el-button type="text"
                                                           @click="expandRow(scope, stepIndex)">
                                                    <span v-if="activeStepIndex === stepIndex && activeRowIndex === scope.$index">收起更多</span>
                                                    <span v-else>展开更多</span>
                                                </el-button>
                                            </div>
                                        </template>
                                    </el-table-column>
                                    <!-- 展开更多信息 -->
                                    <el-table-column type="expand"
                                                     width="1">
                                        <template slot-scope="props">
                                            <!-- 文件分发 -->
                                            <step-info-download :stepMoreData="stepMoreData"
                                                                :rowData="props.row"
                                                                :isPreview="true"
                                                                :isFromWorkEdit="true"></step-info-download>
                                        </template>
                                    </el-table-column>
                                </el-table>
                            </div>

                        </div>
                    </el-col>
                </el-row>
            </section>
        </div>
        <div class="fixed-bottom-box t-center">
            <el-button @click="closeBox">取消</el-button>
            <el-button type="primary"
                       @click="runWorkNow">立即执行</el-button>
        </div>
    </div>
</template>

<script>
    import stepInfoScript from './step-info-script.vue'
    import stepInfoFile from './step-info-file.vue'
    import stepInfoDownload from './step-info-download.vue'
    import rbAutoOperationMixin from 'src/services/auto_operation/rb-auto-operation-mixin.js'
    import rbAutoOperationServicesFactory from 'src/services/auto_operation/rb-auto-operation-services.factory.js'
    import rbAutoOperationWorkServicesFactory from 'src/services/auto_operation/rb-auto-operation-work-services.factory.js'

    export default {
        components: {
            stepInfoScript,
            stepInfoFile,
            stepInfoDownload
        },
        props: ['pipelineId'],
        data() {
            return {
                convergeType: [
                  {
                    key: 0,
                    value: '无汇聚'
                  },
                  {
                    key: 1,
                    value: '追加汇聚'
                  },
                  {
                    key: 2,
                    value: '分类汇聚'
                  }
                ],
                // 步骤类型
                stepTypes: [
                    {
                        type: 'script',
                        label: '添加脚本'
                    },
                    {
                        type: 'file',
                        label: '添加文件分发'
                    },
                ],
                // 分类标签
                labelList: [],
                // 获取row的key值
                getRowKeys(row) {
                    return row.rowIndex
                },
                expands: [],

                // 作业基础信息
                basicForm: {
                    pipeline_name: '',
                    label_id: ''
                },

                // 步骤块名称
                stepFormList: [
                ],
                // 步骤块校验列表
                stepFormRulesList: [
                ],
                // 步骤块列表
                stepBlockList: [],
                // 新增步骤参数
                defaultStepInfo: {
                    block_name: '', // 步骤块名称
                    name: '', // 脚本名称
                    target_ops_user: '', // 执行账户
                    target_hosts: '', // 目标服务器
                    script_param: '', // 脚本参数
                    param_sensive_flag: false, // 是否敏感参数
                    file_store_converge_type: 0,  // 无汇聚
                    file_store_safety: false, // 非安全
                },
                // 新增步骤类型
                stepType: 'script',
                // 更多脚本信息，传递到子组件
                stepMoreData: {
                    serverList: [],
                    allCodeList: [],
                },
                activeStepIndex: 0, // 当前激活步骤
                activeRowIndex: '', // 当前激活步骤下的激活脚本信息

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
                    accountSelected: '',
                    codeParam: '',
                    codeTime: '30',
                    // 目标服务器
                    targetMachines: []
                },

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

                target_hosts: [], // 克隆主机列表
                previewData: {
                    basicForm: {},
                    stepBlockList: [],
                },
                allTableSelects: [],
                hasToggle: false, // 是否已展开行
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
            // 为每行增加rowIndex属性，新增行时需用到
            stepBlockListHandle() {
                this.stepBlockList.length && this.stepBlockList.forEach((table, tableIndex) => {
                    // 步骤字段
                    let rowBlockName = this.stepFormList[tableIndex] && this.stepFormList[tableIndex].block_name
                    if (!rowBlockName) {
                        this.stepFormList.splice(
                            tableIndex,
                            1,
                            {
                                block_name: table[0].block_name || ''
                            }
                        )
                    }

                    // 添加唯一 rowIndex
                    table.forEach((row, index) => {
                        row.rowIndex = tableIndex + '' + index
                    })

                    // 默认全选，执行全部节点
                    setTimeout(() => {
                        if (this.$refs && this.$refs['stepTable0']) {
                            this.$refs['stepTable' + tableIndex][0].toggleAllSelection()
                        }
                    })
                })
                return this.stepBlockList
            },
        },
        watch: {
            pipelineId(val) {
                this.getWorkDetail(val)
            }

        },
        mounted() {
            this.getWorkDetail(this.pipelineId)
            this.getServerslist()
        },
        methods: {
            // 复选框 已勾选数据
            handleSelectionChange(val, index) {
                this.allTableSelects[index] = val
            },
            // 获取作业详情
            getWorkDetail(workId) {
                this.pageLoading = true
                // 作业基本信息
                rbAutoOperationWorkServicesFactory
                    .queryOpsPipelineById(workId)
                    .then(res => {
                        if (res.pipeline_id) {
                            this.previewData.basicForm.pipeline_name = res.pipeline_name
                            this.previewData.basicForm.label_id = res.label_id
                            this.getStepDetail(workId)
                        } else {
                            this.$message.error(res.error_tip)
                            this.pageLoading = false
                        }
                    })
                    .catch(error => {
                        this.pageLoading = false
                        this.showErrorTip(error)
                    })
            },
            // 获取步骤详情
            getStepDetail(workId) {
                rbAutoOperationWorkServicesFactory
                    .queryOpsStepListByPipelineId(workId)
                    .then(res => {
                        if (res.length) {

                            this.previewData.stepBlockList = this.handleList(res)
                            this.setPreviewData()
                        } else {
                            this.$message.error(res.error_tip)
                        }
                        this.pageLoading = false
                    })
                    .catch(error => {
                        this.pageLoading = false
                        this.showErrorTip(error)
                    })
            },
            handleList(list) {
                let arr = []
                list.forEach(item => {
                    item.file_store_safety = item.file_store_safety === 1 ? true: false
                    if (item.ops_type === 0) {
                        arr[0] = arr[0] ? arr[0] : []
                        arr[0].push(item)
                    } else {
                        arr[1] = arr[1] ? arr[1] : []
                        arr[1].push(item)
                    }
                })
                return arr
            },
            // 查询主机列表 
            getServerslist() {
                let req = {
                    page_no: this.currentPage,
                    page_size: this.pageSize
                }
                if (this.searchWord) {
                    req.agent_ip = this.searchWord
                }
                this.loading = true
                rbAutoOperationServicesFactory
                    .fetchUserAuthedAgentHostList(req)
                    .then(res => {
                        this.loading = false
                        this.serverList = res.dataList
                    })
            },
            setPreviewData() {
                this.basicForm = this.previewData.basicForm
                this.stepBlockList = this.previewData.stepBlockList.filter(item => {
                    return item
                })
            },
            getSelectedSteps() {
                let arr = []
                this.allTableSelects.forEach(singleTableSelects => {
                    singleTableSelects.forEach(item => {
                        arr.push(item.step_id)
                    })
                })
                return arr
            },
            // 执行作业
            runWorkNow() {
                let req = {
                    pipelineId: this.pipelineId,
                    select_step_list: this.getSelectedSteps()
                }
                this.pageLoading = true
                rbAutoOperationWorkServicesFactory
                    .pipelineExecute(req)
                    .then(res => {
                        if (res.flag) {
                            this.$message('作业执行中')
                            // 返回作业执行实例id
                            // 跳转至执行结果
                            this.$emit('runWorkNow', res.biz_data)

                        } else {
                            this.$message.error(res.error_tip)
                        }
                        this.pageLoading = false
                    })
                    .catch(error => {
                        this.pageLoading = false
                        this.showErrorTip(error)
                    })
            },
            closeBox() {
                this.$emit('closeBox')
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
            expandRow(scope, stepIndex) {
                // 没有激活行 || 不同步骤 || 同一步骤下，不同行
                let needToSetIndex = this.activeRowIndex === '' || this.activeStepIndex !== stepIndex || (this.activeStepIndex === stepIndex && this.activeRowIndex !== scope.$index)
                if (needToSetIndex) {
                    this.activeRowIndex = scope.$index
                } else {
                    this.activeRowIndex = ''
                }
                this.activeStepIndex = stepIndex
                this.setActiveRowData(scope, stepIndex)
            },
            // 设置当前行信息
            setActiveRowData(scope, stepIndex) {
                let row = scope.row
                this.target_hosts = row.target_hosts
                if (row.ops_type === 0) {
                    this.queryOpsScriptById(row, stepIndex)
                } else {
                    this.toggleRowExpansion(row, stepIndex)
                }
            },
            toggleRowExpansion(row, stepIndex) {
                this.$refs['stepTable' + stepIndex][0].toggleRowExpansion(row)
            },

            // 根据id查询脚本内容
            queryOpsScriptById(row, stepIndex) {
                rbAutoOperationServicesFactory.queryOpsScriptById(row.script_id).then(res => {
                    row.embed_script = res
                    this.$refs['stepTable' + stepIndex][0].toggleRowExpansion(row)
                })
            },
            // 翻页触发 
            search() {
            },
            // 选中表格行
            toggleRow(row) {
                this.$refs.serverTable.toggleRowSelection(row, true)
            },
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
    .yw-dialog-main.no-scroll {
        overflow: hidden;
    }
    .drawer-close-btn {
        float: right;
        margin: -20px 10px 12px 0;
    }
</style>
