<template>
    <div class="components-container yw-dashboard"
         v-loading.fullscreen.lock="pageLoading"
         :element-loading-text="loading_text">
        <el-form class="components-condition yw-form"
                 :model="formSearch"
                 @keyup.enter.native="search(1)"
                 ref="formSearch"
                 :inline="true"
                 label-width="85px">
            <el-form-item label="指令内容"
                          prop="commandLike">
                <el-input v-model="formSearch.commandLike"
                          placeholder="请输入指令内容"
                          clearable></el-input>
            </el-form-item>
            <el-form-item label="任务ID"
                          prop="pipeline_instance_id">
                <el-input v-model="formSearch.pipeline_instance_id"
                          placeholder="请输入指令ID"
                          clearable></el-input>
            </el-form-item>
            <el-form-item label="规则名称"
                          prop="ruleNameLike">
                <el-input v-model="formSearch.ruleNameLike"
                          placeholder="请输入规则名称"
                          clearable></el-input>
            </el-form-item>
            <el-form-item label="脚本类型"
                          prop="contentType">
                <el-select v-model="formSearch.contentType"
                           placeholder="请选择"
                           filterable
                           clearable>
                    <el-option v-for="val in languageTypeList"
                               :key="val.id"
                               :label="val.label"
                               :value="val.id"></el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="审核状态"
                          prop="deal_type">
                <el-select multiple
                           collapse-tags
                           v-model="formSearch.deal_type"
                           placeholder="请选择"
                           clearable>
                    <el-option v-for="val in dealTypeList"
                               :key="val.id"
                               :label="val.label"
                               :value="val.id"></el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="执行对象"
                          prop="instanceClassify">
                <el-select v-model="formSearch.instanceClassify"
                           placeholder="请选择"
                           filterable
                           clearable>
                    <el-option v-for="val in instanceClassifyList"
                               :key="val.id"
                               :label="val.label"
                               :value="val.id"></el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="执行对象名称"
                          prop="instanceNameLike">
                <el-input v-model="formSearch.instanceNameLike"
                          placeholder="请输入执行对象名称"
                          clearable></el-input>
            </el-form-item>
            <el-form-item label="响应时间">
                <el-col :span="11">
                    <el-form-item prop="applyTimeStart">
                        <el-date-picker value-format="yyyy-MM-dd HH:mm:ss"
                                        v-model="formSearch.applyTimeStart"
                                        type="datetime"
                                        placeholder="开始时间"></el-date-picker>
                    </el-form-item>
                </el-col>
                <el-col class="line"
                        :span="1">-</el-col>
                <el-col :span="11">
                    <el-form-item prop="applyTimeEnd">
                        <el-date-picker value-format="yyyy-MM-dd HH:mm:ss"
                                        v-model="formSearch.applyTimeEnd"
                                        type="datetime"
                                        placeholder="结束时间"></el-date-picker>
                    </el-form-item>
                </el-col>
            </el-form-item>
            <section class="btn-wrap">
                <el-button type="primary"
                           @click="search(1)">查询</el-button>
                <el-button @click="reset()">重置</el-button>
            </section>
        </el-form>

        <el-form class="yw-form">
            <div class="table-operate-wrap clearfix">
                <el-button type="text"
                           icon="el-icon-check"
                           @click="reviewInstance('batch', 2)">通过</el-button>
                <el-button type="text"
                           icon="el-icon-close"
                           @click="reviewInstance('batch', 3)">拒绝</el-button>
            </div>
            <div class="yw-el-table-wrap">
                <el-table :data="dataList"
                          ref="table"
                          class="yw-el-table"
                          stripe
                          highlight-current-row
                          tooltip-effect="dark"
                          border
                          height="calc(100vh - 282px)"
                          row-key="review_id"
                          @selection-change="handleSelectionChange"
                          v-loading="loading">
                    <el-table-column type="selection"
                                     :selectable="handleSelectable"
                                     :reserve-selection="true"
                                     width="40"></el-table-column>
                    <el-table-column prop="command"
                                     label="指令内容"
                                     min-width="100"
                                     show-overflow-tooltip></el-table-column>
                    <el-table-column prop="pipeline_instance_id"
                                     label="任务ID"
                                     min-width="100"
                                     show-overflow-tooltip></el-table-column>
                    <el-table-column prop="content_type"
                                     label="脚本类型"
                                     width="80">
                        <template slot-scope="scope">
                            <span v-if="scope.row.content_type === 1">shell</span>
                            <span v-if="scope.row.content_type === 2">bat</span>
                            <span v-if="scope.row.content_type === 3">python</span>
                        </template>
                    </el-table-column>
                    <el-table-column prop="rule_name"
                                     label="规则名称"
                                     width="110"></el-table-column>
                    <el-table-column prop="instance_classify"
                                     label="执行对象类型"
                                     width="100"
                                     show-overflow-tooltip>
                        <template slot-scope="scope">
                            <span v-if="scope.row.instance_classify === 1">快速脚本执行</span>
                            <span v-if="scope.row.instance_classify === 9">作业执行</span>
                        </template>
                    </el-table-column>
                    <el-table-column prop="instance_name"
                                     label="执行对象名称"
                                     width="220"
                                     show-overflow-tooltip>
                        <template slot-scope="scope">
                            <span class="blue pointer"
                                  @click="viewTaskDetail(scope.row)">{{scope.row.instance_name}}</span>
                        </template>
                    </el-table-column>
                    <el-table-column prop="applicant"
                                     label="执行人"
                                     width="80"
                                     show-overflow-tooltip></el-table-column>
                    <el-table-column prop="apply_time"
                                     label="执行响应时间"
                                     width="140"></el-table-column>
                    <el-table-column prop="review_content"
                                     label="执行匹配内容"
                                     min-width="120"
                                     show-overflow-tooltip></el-table-column>
                    <el-table-column prop="review_status"
                                     label="审核状态"
                                     width="80">
                        <template slot-scope="scope">
                            <span v-if="scope.row.review_status === 0">待确认</span>
                            <span v-if="scope.row.review_status === 1">待审核</span>
                            <span v-if="scope.row.review_status === 2">审核通过</span>
                            <span v-if="scope.row.review_status === 3">审核拒绝</span>
                            <span v-if="scope.row.review_status === 9">阻断</span>
                        </template>
                    </el-table-column>
                    <el-table-column label="审核操作"
                                     width="90">
                        <template slot-scope="scope">
                            <div>
                                <el-button v-if="scope.row.review_status === 1"
                                           type="text"
                                           title="通过"
                                           icon="el-icon-check"
                                           @click="reviewInstance(scope.row.review_id, 2)"></el-button>
                                <el-button v-if="scope.row.review_status === 1"
                                           type="text"
                                           title="拒绝"
                                           icon="el-icon-close"
                                           @click="reviewInstance(scope.row.review_id, 3)"></el-button>
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
            </section>
            <section class="btn-wrap">
                <el-button type="primary"
                           @click="saveCode()">保存</el-button>
                <el-button @click="addCancel()">取消</el-button>
            </section>
        </el-dialog>
    </div>
</template>

<script>
    import runWorkDialog from 'src/pages/auto_operation/work_manage/task/run-work-dialog.vue'
    import rbAutoOperationInstructionServicesFactory from 'src/services/auto_operation/rb-auto-operation-instruction-services.factory.js'
    import rbAutoOperationMixin from 'src/services/auto_operation/rb-auto-operation-mixin.js'

    export default {
        name: 'AutoOperationWorkManageList',
        components: {
            runWorkDialog
        },
        data() {
            return {
                pageLoading: false,
                loading_text: '请稍候...',
                dataList: [], // 作业列表
                codeEditorShow: false,
                formSearch: {
                    deal_type: [],
                    commandLike: '',
                    pipeline_instance_id: '',
                    ruleNameLike: '',
                    contentType: '',
                    reviewStatusString: '',
                    instanceClassify: '',
                    instanceNameLike: '',
                    applyTimeStart: '',
                    applyTimeEnd: '',
                },
                codeCloneList: [], // 作业克隆列表
                codeInfo: {}, // 作业弹框最新内容
                currentCodeInfo: {}, // 当前行作业内容
                dialogName: '新建作业', // 弹框title
                runWorkBoxShow: false, // 执行作业弹框
                pipeline_id: '', // 点击行的作业id

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
                dealTypeList: [
                    {
                        id: 0,
                        label: '待确认'
                    },
                    {
                        id: 1,
                        label: '待审核'
                    },
                    {
                        id: 2,
                        label: '审核通过'
                    },
                    {
                        id: 3,
                        label: '审核拒绝'
                    },
                    {
                        id: 9,
                        label: '阻断'
                    },
                ],
                instanceClassifyList: [
                    {
                        id: 1,
                        label: '快速脚本执行'
                    },
                    {
                        id: 9,
                        label: '作业执行'
                    },
                ],
            }
        },
        mixins: [rbAutoOperationMixin],
        watch: {
            'formSearch.deal_type'(val) {
                this.formSearch.reviewStatusString = val.toString()
            }
        },
        mounted() {
            this.initPage()
            setTimeout(() => {
                this.search()
            }, 0)
        },
        methods: {
            initPage() {
                let query = this.$route.query
                if (query.type === 'audit') {
                    this.formSearch.commandLike = query.commandLike
                    this.formSearch.deal_type.push(1) // 待审核
                } else if (query.type === 'view') {
                    this.formSearch.commandLike = query.commandLike
                }
                // 作业预览跳转进入,查询待审核状态
                if (query.deal_type !== undefined) {
                    this.formSearch.deal_type.push(+query.deal_type)
                }
            },
            handleSelectable(row) {
                if (row.review_status === 1) {
                    return true
                } else {
                    return false
                }
            },
            reviewInstance(param, status) {
                let req = {
                    review_ids: param,
                    review_status: status
                }
                let arr = []

                if (param === 'batch') {
                    this.multipleSelection.forEach(item => {
                        arr.push(item.review_id)
                    })
                    req.review_ids = arr.toString()
                }
                if (!req.review_ids) {
                    this.$message.warning('请先选择行！')
                    return
                }
                this.loading = true
                rbAutoOperationInstructionServicesFactory
                    .reviewInstance(req)
                    .then(() => {
                        this.loading = false
                        this.$message.success('操作成功')
                        this.$refs.table.clearSelection()
                        this.search()
                    })
                    .catch(error => {
                        this.loading = false
                        this.showErrorTip(error)
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
                rbAutoOperationInstructionServicesFactory
                    .querySensitiveReviewList(req)
                    .then(res => {
                        this.loading = false
                        this.total = res.totalCount
                        this.dataList = res.dataList
                    })
                    .catch(error => {
                        this.loading = false
                        this.showErrorTip(error)
                    })
            },
            reset() {
                console.log(this.$refs)
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
                    rbAutoOperationInstructionServicesFactory
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
            // 取消关闭弹框
            addCancel() {
                this.codeEditorShow = false
            },
            // 查看脚本/作业详情
            viewTaskDetail(row) {
                if (row.pipeline_id === '-1') {
                    this.$router.push({
                        path: '/auto_operation/code_manage/run',
                        query: {
                            pipelineInstanceId: row.pipeline_instance_id,
                            type: 'detail',
                            currentTitle: '脚本详情'
                        }
                    })
                } else {
                    this.$router.push({
                        path: '/auto_operation/work_manage/task',
                        query: {
                            cloneId: row.pipeline_id,
                            type: 'detail',
                            currentTitle: '作业详情'
                        }
                    })
                }
            },
            // 新建作业
            newInstruction() {
                this.$router.push('/auto_operation/instruction_manage/instruction')
            },
            // 执行作业弹框
            showRunWork(row) {
                if (row.pipeline_id !== '-1') {
                    this.$bus.emit('showRunWork', row.pipeline_id)
                }
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
                } else {
                    query.pipeline_id = row.pipeline_id
                }
                this.$router.push({
                    path: '/auto_operation/work_manage/task',
                    query: query
                })
            },
            // 删除作业
            deleteWork(row) {
                this.$confirm('确认删除?').then(() => {
                    rbAutoOperationInstructionServicesFactory
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
</style>
