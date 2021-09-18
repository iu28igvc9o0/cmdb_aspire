<template>
    <div class="components-container yw-dashboard"
         v-loading.fullscreen.lock="pageLoading"
         :element-loading-text="loading_text">
        <el-form class="components-condition yw-form"
                 :model="formSearch"
                 @keyup.enter.native="search(1)"
                 ref="formSearch"
                 :inline="true"
                 label-width="65px">
            <el-form-item label="作业名称"
                          prop="pipeline_name_like"
                          label-width="75px">
                <el-input v-model="formSearch.pipeline_name_like"
                          placeholder="请输入作业名称"
                          clearable></el-input>
            </el-form-item>
            <el-form-item label="创建时间">
                <el-col :span="11">
                    <el-form-item prop="create_time_start">
                        <el-date-picker value-format="yyyy-MM-dd HH:mm:ss"
                                        v-model="formSearch.create_time_start"
                                        type="datetime"
                                        placeholder="开始时间"></el-date-picker>
                    </el-form-item>
                </el-col>
                <el-col class="line"
                        :span="1">-</el-col>
                <el-col :span="11">
                    <el-form-item prop="create_time_end">
                        <el-date-picker value-format="yyyy-MM-dd HH:mm:ss"
                                        v-model="formSearch.create_time_end"
                                        type="datetime"
                                        placeholder="结束时间"></el-date-picker>
                    </el-form-item>
                </el-col>
            </el-form-item>
            <el-form-item label="创建人"
                          prop="creater">
                <el-input v-model="formSearch.creater"
                          placeholder="请输入创建人"
                          clearable></el-input>
            </el-form-item>
            <!-- <el-form-item label="最后修改人"
                          prop="updater"
                          label-width="75px">
                <el-input v-model="formSearch.updater"
                          placeholder="请输入最后修改人"
                          clearable></el-input>
            </el-form-item> -->
            <el-form-item label="审核状态"
                          prop="updater"
                          label-width="75px">
                <el-select v-model="formSearch.audit_status"
                           placeholder="请选择审核状态"
                           filterable
                           clearable>
                    <el-option v-for="val in auditStatusList"
                               :key="val.code"
                               :label="val.label"
                               :value="val.code"></el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="修改时间">
                <el-col :span="11">
                    <el-form-item prop="update_time_start">
                        <el-date-picker value-format="yyyy-MM-dd HH:mm:ss"
                                        v-model="formSearch.update_time_start"
                                        type="datetime"
                                        placeholder="开始时间"></el-date-picker>
                    </el-form-item>
                </el-col>
                <el-col class="line"
                        :span="1">-</el-col>
                <el-col :span="11">
                    <el-form-item prop="update_time_end">
                        <el-date-picker value-format="yyyy-MM-dd HH:mm:ss"
                                        v-model="formSearch.update_time_end"
                                        type="datetime"
                                        placeholder="结束时间"></el-date-picker>
                    </el-form-item>
                </el-col>
            </el-form-item>
            <el-form-item label="分类标签"
                          prop="labelId">
                <el-select v-model="formSearch.labelId"
                           placeholder="请选择分类标签"
                           filterable
                           clearable>
                    <el-option v-for="val in labelList"
                               :key="val.code"
                               :label="val.label"
                               :value="val.code"></el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="审核状态"
                          prop="updater"
                          label-width="75px">
                <el-select v-model="formSearch.audit_status"
                           placeholder="请选择审核状态"
                           filterable
                           clearable>
                    <el-option v-for="val in auditStatusList"
                               :key="val.code"
                               :label="val.label"
                               :value="val.code"></el-option>
                </el-select>
            </el-form-item>
            <section class="btn-wrap">
                <el-button type="primary"
                           @click="search(1)">查询</el-button>
                <el-button @click="reset()">重置</el-button>
            </section>
        </el-form>

        <el-form class="yw-form">
            <div ref="operateBox"
                 class="table-operate-wrap clearfix">
                <el-button type="text"
                           icon="el-icon-plus"
                           @click="newWork">新建作业</el-button>
            </div>
            <div class="yw-el-table-wrap">
                <el-table :data="workList"
                          class="yw-el-table"
                          stripe
                          highlight-current-row
                          tooltip-effect="dark"
                          border
                          :height="tableHeight"
                          @selection-change="handleSelectionChange"
                          v-loading="loading">
                    <el-table-column prop="pipeline_name"
                                     label="作业名称"
                                     min-width="200"
                                     show-overflow-tooltip></el-table-column>
                    <el-table-column prop="step_count"
                                     label="步骤"
                                     width="80"></el-table-column>
                    <el-table-column prop="creater"
                                     label="创建人"
                                     width="90"></el-table-column>
                    <el-table-column prop="updater"
                                     label="最后修改人"
                                     width="110"></el-table-column>
                    <el-table-column prop="reviewer"
                                     label="审核人"
                                     width="110"></el-table-column>
                    <el-table-column prop="create_time"
                                     label="创建时间"
                                     width="140"></el-table-column>
                    <el-table-column prop="update_time"
                                     label="最后修改时间"
                                     width="140"></el-table-column>
                    <el-table-column prop="review_time"
                                     label="审核时间"
                                     width="140"></el-table-column>
                    <el-table-column prop="pipeline_id"
                                     label="ID"
                                     width="60"></el-table-column>
                    <el-table-column prop="label_id"
                                     label="分类"
                                     width="150"></el-table-column>
                    <el-table-column label="审核状态">
                        <template slot-scope="scope">
                            <span v-if="scope.row.audit_status == 0">待审核</span>
                            <span v-if="scope.row.audit_status == 1">审核通过</span>
                            <span v-if="scope.row.audit_status == 2">审核拒绝</span>
                        </template>
                    </el-table-column>
                    <el-table-column prop="audit_desc"
                                     label="审核描述"
                                     width="120"></el-table-column>
                    <el-table-column prop="current_version"
                                     label="当前版本"
                                     width="140"></el-table-column>
                    <el-table-column label="操作"
                                     fixed="right"
                                     width="200">
                        <template slot-scope="scope">
                            <div>
                                <el-button type="text"
                                           title="立即执行"
                                           icon="el-icon-video-play"
                                           @click="showRunWork(scope.row.pipeline_id)"
                                           :disabled="scope.row.label_id === 'autoRepair' || scope.row.audit_status != '1'"></el-button>
                                <el-button type="text"
                                           title="定时启动"
                                           icon="el-icon-time"
                                           @click="setTimeTask(scope.row)"
                                           :disabled="scope.row.label_id === 'autoRepair'"></el-button>
                                <span class="split-line"></span>
                                <el-button type="text"
                                           title="编辑"
                                           icon="el-icon-edit"
                                           @click="goEdit(scope.row)"></el-button>
                                <el-button type="text"
                                           title="克隆"
                                           icon="el-icon-copy-document"
                                           @click="gotoWork(scope.row, 'clone')"></el-button>
                                <el-button type="text"
                                           title="删除"
                                           icon="el-icon-delete"
                                           @click="deleteWork(scope.row)"></el-button>
                                <el-button type="text"
                                           :disabled="scope.row.audit_status != '0'"
                                           title="审核"
                                           icon="el-icon-thumb"
                                           @click="gotoAuditWork(scope.row)"></el-button>
                            </div>
                        </template>
                    </el-table-column>
                </el-table>
            </div>
            <div class="yw-page-wrap">
                <el-pagination @size-change="handleSizeChange"
                               @current-change="handleCurrentChange"
                               :current-page="currentPage"
                               :page-sizes="pageSizes"
                               :page-size="pageSize"
                               :total="total"
                               layout="total, sizes, prev, pager, next, jumper"></el-pagination>
            </div>
        </el-form>

        <!-- 执行作业 -->
        <run-work-dialog></run-work-dialog>

        <!-- 定时任务 -->
        <el-dialog class="yw-dialog"
                   :title="dialogName"
                   :visible.sync="codeEditorShow"
                   :destroy-on-close="true"
                   width="850px">
            <section class="yw-dialog-main">
                <timed-job-editor ref="codeEditor"
                                  :codeNameShow="true"
                                  :isFromWorkList="true"
                                  :currentCodeInfo="currentCodeInfo"
                                  @passCodeContent="passCodeContent"></timed-job-editor>
            </section>
            <section class="btn-wrap">
                <el-button type="primary"
                           @click="saveCode()">保存</el-button>
                <el-button @click="addCancel()">取消</el-button>
            </section>
        </el-dialog>
        <!-- 审核作业 -->
        <el-dialog width="50%"
                   class="yw-dialog"
                   title="编辑审核"
                   :visible.sync="auditBoxShow"
                   :append-to-body="true"
                   :destroy-on-close="true"
                   :close-on-click-modal="false">
            <section class="yw-dialog-main">
                <el-form class="yw-form is-required"
                         ref="auditForm"
                         :model="auditForm"
                         label-width="100px">
                    <el-form-item label="审核状态"
                                  prop="auditStatus">
                        <el-radio v-model="auditForm.auditStatus"
                                  label="1">通过</el-radio>
                        <el-radio v-model="auditForm.auditStatus"
                                  label="2">拒绝</el-radio>
                        <el-tooltip class="item"
                                    effect="dark"
                                    :content="tipContent"
                                    placement="right">
                            <i class="el-icon-question"></i>
                        </el-tooltip>
                    </el-form-item>
                    <el-form-item label="审核描述"
                                  prop="auditDesc">
                        <el-input type="textarea"
                                  :rows="2"
                                  placeholder="请输入内容"
                                  v-model="auditForm.auditDesc">
                        </el-input>
                    </el-form-item>
                </el-form>
            </section>
            <section class="btn-wrap">
                <el-button type="primary"
                           @click="auditPipleine">确定</el-button>
                <el-button @click="auditBoxShow = false">取消</el-button>
            </section>
        </el-dialog>
        <!-- 编辑 -->
        <el-dialog class="yw-dialog"
                   title="编辑"
                   width="800px"
                   :visible.sync="editDialog"
                   :append-to-body="true"
                   :close-on-click-modal="false">
            <section class="yw-dialog-main">
                <el-form class="yw-form is-required">
                    <el-form-item label="作业名称">
                        <div class="item-style">
                            <span>{{workName}}</span>
                            <el-button type="text"
                                       @click="gotoWork(pipelineId, 'edit')">编辑</el-button>
                        </div>
                    </el-form-item>
                    <el-form-item>
                        <el-table class="yw-el-table"
                                  style="cursor:pointer"
                                  :data="pipelineHisList"
                                  stripe
                                  height="calc(100vh - 400px)"
                                  tooltip-effect="dark"
                                  border
                                  size="samll"
                                  v-loading="loading">
                            <el-table-column prop="current_version"
                                             label="版本号"
                                             sortable></el-table-column>
                            <el-table-column prop="create_time"
                                             label="创建时间"
                                             sortable></el-table-column>
                            <el-table-column prop="creater"
                                             label="创建人"
                                             sortable></el-table-column>
                            <el-table-column label="状态"
                                             sortable>
                                <template slot-scope="scope">
                                    <p v-if="scope.row.current_version == currentVersion">使用</p>
                                    <p v-else>未使用</p>
                                </template>
                            </el-table-column>
                            <el-table-column prop="pool_name"
                                             label="操作"
                                             sortable>
                                <template slot-scope="scope">

                                    <el-button type="text"
                                               :disabled="scope.row.current_version == currentVersion"
                                               @click="updatePipelineVersion(scope.row)">启用</el-button>
                                    <el-button type="text"
                                               @click="viewTaskDetail(scope.row)">查看</el-button>
                                </template>
                            </el-table-column>

                        </el-table>
                    </el-form-item>
                </el-form>
                <whiteList :pipelineId="pipelineId"></whiteList>

            </section>

        </el-dialog>

    </div>
</template>

<script>
    import whiteList from './white-list.vue'
    import runWorkDialog from '../task/run-work-dialog.vue'
    import timedJobEditor from '../timed/timed-job-editor.vue'
    import rbAutoOperationWorkServicesFactory from 'src/services/auto_operation/rb-auto-operation-work-services.factory.js'
    import rbAutoOperationMixin from 'src/services/auto_operation/rb-auto-operation-mixin.js'
    import rbAutoOperationServicesFactory from 'src/services/auto_operation/rb-auto-operation-services.factory.js'

    // import opsAudit from 'src/pages/auto_operation/components/ops-audit.vue'
    export default {
        name: 'AutoOperationWorkManageList',
        components: {
            runWorkDialog,
            timedJobEditor,
            whiteList
            // ,
            // opsAudit
        },
        data() {
            return {
                scriptList: [],
                auditStatusList: [{ code: '0', label: '待审核' }, { code: '1', label: '审核通过' }, { code: '2', label: '审核拒绝' }],
                tipContent: '',
                editDialog: false,
                pipelineId: '',
                workName: '',
                currentVersion: '',
                pipelineHisList: [],
                labelList: [],
                auditBoxShow: false,
                pageLoading: false,
                loading_text: '请稍候...',
                workList: [], // 作业列表
                codeEditorShow: false,
                auditForm: {
                    piepeline: null,
                    auditStatus: null,
                    auditDesc: ''
                },
                formSearch: {
                    pipeline_name_like: '',
                    creater: sessionStorage.getItem('username'),
                    updater: '',
                    update_time_start: '',
                    update_time_end: '',
                    create_time_start: '',
                    create_time_end: ''
                },
                codeCloneList: [], // 作业克隆列表
                codeInfo: {}, // 作业弹框最新内容
                currentCodeInfo: {}, // 当前行作业内容
                dialogName: '新建作业', // 弹框title
                runWorkBoxShow: false, // 执行作业弹框
                pipeline_id: '', // 点击行的作业id
                // 步骤块列表
                stepBlockList: [
                    [
                        {
                            ops_type: 0,
                            block_name: '',
                            name: '', // 脚本名称
                            target_ops_user: '', // 执行账户
                            target_hosts: [], // 目标服务器
                            embed_script: {},
                            script_param: '', // 脚本参数
                            param_sensive_flag: false, // 是否敏感参数
                            pause_flag: false,  // 是否完成后暂停
                            replace_attrs: [],   // 模版参数
                            ile_store_converge_type: 0,  // 无汇聚
                            file_store_safety: false, // 非安全
                        }
                    ],
                ],
            }
        },
        mixins: [rbAutoOperationMixin],
        computed: {
            // 为每行增加rowIndex属性，新增行时需用到
            stepBlockListHandle() {
                this.stepBlockList.forEach((table, tableIndex) => {
                    // 步骤字段
                    console.log('table===', table)
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

                    // 步骤字段校验
                    this.stepFormRulesList.push(this.stepFormRules)

                    // 添加唯一 rowIndex
                    table.forEach((row, index) => {
                        console.log(row)
                        row.rowIndex = tableIndex + '' + index
                        if (this.pageType === 'clone') {
                            row.name = row.name + '_' + this.username
                        }
                    })
                })
                return this.stepBlockList
            },

        },
        created() {
            this.queryOpsLabelList()
            this.search()
        },
        methods: {
            // 编辑
            goEdit(row) {
                this.editDialog = true
                this.pipelineId = row.pipeline_id
                this.workName = row.pipeline_name
                this.currentVersion = row.current_version
                this.getHistoryList()
            },
            // 启用历史
            updatePipelineVersion(row) {
                rbAutoOperationServicesFactory.updatePipelineVersion({ pipelineHisId: row.id }).then(res => {
                    if (res.flag) {
                        this.$message.success('启用成功')
                        this.getHistoryList()
                    } else {
                        this.$message.error(res.error_tip)
                    }
                })
            },
            // 历史作业
            getHistoryList() {
                rbAutoOperationServicesFactory.getPipelineHisListByPipelineId({ pipelineId: this.pipelineId }).then(res => {
                    this.pipelineHisList = res
                })
            },
            // 查看脚本/作业详情
            viewTaskDetail(row) {
                this.editDialog = false
                this.$router.push({
                    path: '/auto_operation/work_manage/task',
                    query: {
                        cloneId: row.id,
                        type: 'detail',
                        historyVal: '1',
                        currentTitle: '作业详情'
                    }
                })
            },
            // 查询作业列表
            search(pageNum) {
                this.currentPage = pageNum ? pageNum : this.currentPage
                let req = {
                    page_no: this.currentPage,
                    page_size: this.pageSize
                }
                req = Object.assign(req, this.formSearch)
                this.loading = true
                this.codeCloneList = []
                rbAutoOperationWorkServicesFactory
                    .queryOpsPipelineList(req)
                    .then(res => {
                        this.loading = false
                        this.total = res.totalCount
                        this.workList = res.dataList
                    })
                    .catch(error => {
                        this.loading = false
                        this.showErrorTip(error)
                    })
            },
            reset() {
                this.$refs['formSearch'].resetFields()
            },
            // 保存定时作业
            saveCode() {
                this.$refs.codeEditor.$refs.timerForm.validate((valid) => {
                    if (!valid) {
                        this.$message('请先完善信息')
                        return
                    }

                    this.pageLoading = true

                    let req = {
                        job_id: '',
                        name: this.currentCodeInfo.pipeline_name,
                        pipeline_id: this.currentCodeInfo.pipeline_id,
                        cron_expression: this.currentCodeInfo.cron_expression
                    }
                    rbAutoOperationWorkServicesFactory
                        .saveOpsPipelineRunJob(req)
                        .then(() => {
                            this.$message('保存成功')
                            this.search()
                            this.pageLoading = false
                            this.codeEditorShow = false
                        })
                        .catch(error => {
                            this.pageLoading = false
                            this.showErrorTip(error)
                        })
                })
            },
            queryOpsLabelList() {
                rbAutoOperationWorkServicesFactory
                    .queryOpsLabelList()
                    .then(res => {
                        this.labelList = res
                    })
            },
            // 取消关闭弹框
            addCancel() {
                this.codeEditorShow = false
            },
            // 新建作业
            newWork() {
                this.$router.push({
                    path: '/auto_operation/work_manage/task',
                    query: {
                        currentTitle: '新建作业'
                    }
                })
            },
            // 执行作业弹框
            showRunWork(pipelineId) {
                this.$bus.emit('showRunWork', pipelineId)
            },
            // 编辑作业
            setTimeTask(row) {
                let obj = {
                    job_id: '',
                    name: '',
                    pipeline_id: '',
                    cron_expression: '',
                    status: '',
                    creater: '',
                    create_time: '',
                    updater: '',
                    update_time: ''
                }
                this.currentCodeInfo = Object.assign(obj, JSON.parse(JSON.stringify(row)))
                this.currentCodeInfo.name = this.currentCodeInfo.pipeline_name
                console.log('row==', this.currentCodeInfo)
                this.codeEditorShow = true
            },
            // 编辑作业-更新
            // 克隆作业-带上内容新增-需去除id
            gotoWork(row, type) {
                let query = {
                    type: type
                }
                if (type === 'clone') {
                    query.cloneId = row.pipeline_id
                    query.currentTitle = '克隆作业'
                } else {
                    query.pipeline_id = this.pipelineId
                    query.currentTitle = '编辑作业'
                }
                this.editDialog = false

                this.$router.push({
                    path: '/auto_operation/work_manage/task',
                    query: query
                })
            },
            // 审核作业
            gotoAuditWork(row) {
                this.auditForm.piepeline = row.pipeline_id
                this.auditForm.auditStatus = '1'
                this.auditForm.auditDesc = ''
                this.scriptList = []
                this.auditBoxShow = true
                this.getStepDetail(row.pipeline_id)
            },
            // 获取步骤详情
            getStepDetail(workId) {
                rbAutoOperationWorkServicesFactory
                    .queryOpsStepListByPipelineId(workId)
                    .then(res => {
                        if (res.length) {
                            this.handleList(res)
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
            async handleList(list) {
                let arr = []
                for (let item of list) {
                    let stepOrd = item.block_ord - 1
                    arr[stepOrd] = arr[stepOrd] ? arr[stepOrd] : []
                    if (item.ops_type === 0) {
                        this.queryOpsScriptById(item)
                        item.param_sensive_flag = item.param_sensive_flag === 1 ? true : false
                        item.pause_flag = item.pause_flag === 1 ? true : false
                        arr[stepOrd].push(item)
                    } else {
                        item.pause_flag = item.pause_flag === 1 ? true : false
                        item.file_store_safety = item.file_store_safety === 1 ? true : false
                        arr[stepOrd].push(item)
                    }
                }
                this.stepBlockList = arr
            },
            // 根据id查询脚本内容
            async queryOpsScriptById(row) {
                await rbAutoOperationServicesFactory.queryOpsScriptById(row.script_id).then(res => {
                    row.embed_script = res
                    this.scriptList.push(res)
                })
                this.checkScriptSensitivity(this.scriptList)
            },
            checkScriptSensitivity(arrNew) {
                rbAutoOperationServicesFactory
                    .checkScriptSensitivity({ script_list: arrNew })
                    .then(res => {
                        if (res.flag) {
                            this.tipContent = '暂无敏感指令'
                        } else {
                            this.tipContent = res.error_tip
                        }
                    })
            },
            // 处理保存作业参数数据
            handleStepList() {
                let list = []
                let deepCloneList = JSON.parse(JSON.stringify(this.stepBlockListHandle))
                let ordCounter = 0
                deepCloneList.forEach((stepBlock, stepIndex) => {
                    stepBlock.forEach((item) => {
                        ordCounter++
                        if (this.type === 'clone') {
                            item.block_name = this.stepFormList[stepIndex].block_name + '_' + this.username
                        } else {
                            item.block_name = this.stepFormList[stepIndex].block_name
                        }
                        item.block_ord = stepIndex + 1  // 步骤块顺序
                        item.ord = ordCounter  // 脚本顺序

                        // 编辑状态或已保存，带id，克隆不带
                        item.step_id = this.hasSaved ? item.step_id : ''
                        item.script_id = this.hasSaved ? item.script_id : ''

                        // 脚本类型
                        if (item.ops_type === 0) {
                            item.ops_timeout = item.ops_timeout || '600'
                            item.embed_script.script_name = item.name
                            // 编辑状态带id，克隆不带
                            item.embed_script.script_id = this.hasSaved ? item.script_id : ''
                            // 是否敏感参数
                            item.param_sensive_flag = item.param_sensive_flag === true ? 1 : 0
                            item.file_store_safety = item.file_store_safety === true ? 1 : 0
                            // 文件分发类型
                        } else {
                            item.file_store_safety = item.file_store_safety === true ? 1 : 0
                            // item.file_source = item.file_source
                            item.param_sensive_flag = 0
                        }
                        item.pause_flag = item.pause_flag === true ? 1 : 0
                        list.push(item)
                    })
                })
                return list
            },

            auditPipleine() {
                if (this.auditForm.auditStatus == '2' && this.auditForm.auditDesc == '') {
                    this.$message.error('审核拒绝必须填写审核描述')
                    return
                }
                rbAutoOperationWorkServicesFactory.auditOpsPipeline(this.auditForm).then(res => {
                    if (res.flag) {
                        this.$message({
                            message: '审核成功!',
                            type: 'success'
                        })
                    } else {
                        this.$message.error(res.error_tip)
                    }
                    this.auditBoxShow = false
                    this.search()
                })
                    .catch(error => {
                        this.showErrorTip(error)
                    })
            },
            // 删除作业
            deleteWork(row) {
                this.$confirm('确认删除?').then(() => {
                    rbAutoOperationWorkServicesFactory
                        .removePipeline(row.pipeline_id)
                        .then(res => {
                            if (res.flag) {
                                this.$message({
                                    message: '删除成功!',
                                    type: 'success'
                                })
                                this.search()
                            } else {
                                // console.log(res)
                                this.showErrorTip(res)
                            }
                        })
                        .catch(error => {
                            this.showErrorTip(error)
                        })
                })
            },
            // 传递作业内容
            passCodeContent(data) {
                this.currentCodeInfo = Object.assign(this.currentCodeInfo, data)
                console.log('this.currentCodeInfo===', this.currentCodeInfo)
            }
        }
    }
</script>

<style lang="scss" scoped>
    .split-line {
        width: 10px;
        height: 20px;
        margin: 0 10px;
        border-left: 1px solid $color-border;
    }
    .item-style {
        display: flex;
        justify-content: space-between;
    }
</style>
