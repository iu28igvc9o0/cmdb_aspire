<template>
    <div class="components-container yw-dashboard"
         v-loading.fullscreen.lock="loading"
         :element-loading-text="loading_text">
        <el-form class="components-condition yw-form"
                 :model="formSearch"
                 @keyup.enter.native="search(1)"
                 ref="formSearch"
                 :inline="true"
                 label-width="65px">
            <el-form-item label="定时任务"
                          prop="nameLike">
                <el-input v-model="formSearch.nameLike"
                          placeholder="请输入定时任务名称"
                          clearable></el-input>
            </el-form-item>
            <el-form-item label="创建时间">
                <el-col :span="11">
                    <el-form-item prop="createTimeStart">
                        <el-date-picker v-model="formSearch.createTimeStart"
                                        type="datetime"
                                        placeholder="开始时间"
                                        value-format="yyyy-MM-dd HH:mm:ss"></el-date-picker>
                    </el-form-item>
                </el-col>
                <el-col class="line"
                        :span="1">-</el-col>
                <el-col :span="11">
                    <el-form-item prop="createTimeEnd">
                        <el-date-picker v-model="formSearch.createTimeEnd"
                                        type="datetime"
                                        placeholder="结束时间"
                                        value-format="yyyy-MM-dd HH:mm:ss"></el-date-picker>
                    </el-form-item>
                </el-col>
            </el-form-item>
            <el-form-item label="最后修改人"
                          prop="updaterLike"
                          label-width="75px">
                <el-input v-model="formSearch.updaterLike"
                          placeholder="请输入最后修改人"
                          clearable></el-input>
            </el-form-item>
            <el-form-item label="创建人"
                          prop="createrLike">
                <el-input v-model="formSearch.createrLike"
                          placeholder="请输入创建人"
                          clearable></el-input>
            </el-form-item>

            <el-form-item label="修改时间">
                <el-col :span="11">
                    <el-form-item prop="updateTimeStart">
                        <el-date-picker v-model="formSearch.updateTimeStart"
                                        type="datetime"
                                        placeholder="开始时间"
                                        value-format="yyyy-MM-dd HH:mm:ss"></el-date-picker>
                    </el-form-item>
                </el-col>
                <el-col class="line"
                        :span="1">-</el-col>
                <el-col :span="11">
                    <el-form-item prop="updateTimeEnd">
                        <el-date-picker v-model="formSearch.updateTimeEnd"
                                        type="datetime"
                                        placeholder="结束时间"
                                        value-format="yyyy-MM-dd HH:mm:ss"></el-date-picker>
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
                           icon="el-icon-plus"
                           @click="addCode">新建任务</el-button>
            </div>
            <div class="yw-el-table-wrap">
                <el-table :data="codeList"
                          class="yw-el-table"
                          stripe
                          highlight-current-row
                          tooltip-effect="dark"
                          border
                          height="calc(100vh - 282px)"
                          @selection-change="handleSelectionChange"
                          v-loading="loading">
                    <el-table-column sortable
                                     prop="name"
                                     label="定时任务"></el-table-column>
                    <el-table-column sortable
                                     prop="pipeline_name"
                                     label="作业名称"></el-table-column>
                    <el-table-column sortable
                                     prop="cron_expression"
                                     label="定时表达式"
                                     min-width="180"></el-table-column>
                    <el-table-column sortable
                                     prop="creater"
                                     label="创建人"
                                     width="120"></el-table-column>
                    <el-table-column sortable
                                     prop="create_time"
                                     label="创建时间"
                                     min-width="120"></el-table-column>
                    <el-table-column sortable
                                     prop="updater"
                                     label="最后修改人"></el-table-column>
                    <el-table-column sortable
                                     prop="update_time"
                                     label="最后修改时间"
                                     min-width="120"></el-table-column>
                    <el-table-column sortable
                                     prop="statusDesc"
                                     label="当前状态"></el-table-column>
                    <el-table-column label="操作"
                                     width="120">
                        <template slot-scope="scope">
                            <div>
                                <el-button type="text"
                                           title="启动"
                                           icon="el-icon-video-play"
                                           @click="start(scope.row)"
                                           v-if="scope.row.status != 100">
                                </el-button>
                                <el-button type="text"
                                           title="停止"
                                           icon="el-icon-video-pause"
                                           @click="stop(scope.row)"
                                           v-if="scope.row.status == 100">
                                </el-button>
                                <span class="split-line"></span>
                                <el-button type="text"
                                           title="修改"
                                           icon="el-icon-edit"
                                           @click="updateCode(scope.row)"></el-button>
                                <el-button type="text"
                                           title="删除"
                                           icon="el-icon-delete"
                                           @click="deleteCode(scope.row)"></el-button>
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
        <!-- dialog -->
        <el-dialog class="yw-dialog"
                   :title="dialogName"
                   :visible.sync="codeEditorShow"
                   width="850px">
            <section class="yw-dialog-main">
                <timed-job-editor ref="codeEditor"
                                  :codeNameShow="true"
                                  :currentCodeInfo="currentCodeInfo"
                                  @passCodeContent="passCodeContent"></timed-job-editor>
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
    import timedJobEditor from './timed-job-editor.vue'
    import rbAutoOperationMixin from 'src/services/auto_operation/rb-auto-operation-mixin.js'
    import rbAutoOperationWorkServicesFactory from 'src/services/auto_operation/rb-auto-operation-work-services.factory.js'
    import _ from 'lodash'
    const statusDescMap = { '100': '执行中', '101': '执行失败', '102': '执行超时', '9': '执行成功', '5': '执行等待', '6': '暂停执行' }

    export default {
        name: 'TimedJob',
        components: {
            timedJobEditor
        },
        data() {
            return {
                loading: false,
                loading_text: '请稍候...',
                codeList: [], // 作业列表
                codeEditorShow: false,
                formSearch: {
                    nameLike: '',
                    createrLike: '',
                    updaterLike: '',
                    createTimeStart: '',
                    createTimeEnd: '',
                    updateTimeStart: '',
                    updateTimeEnd: ''
                },
                codeInfo: {}, // 定时任务
                currentCodeInfo: {},  // 当前定时任务内容
                dialogName: '新建任务'  // 弹框title
            }
        },
        mixins: [rbAutoOperationMixin],
        created() {
            this.search()
        },
        methods: {
            // 查询作业列表
            search(pageNum) {
                this.currentPage = pageNum ? pageNum : this.currentPage
                let req = {
                    nameLike: this.formSearch.nameLike,
                    createrLike: this.formSearch.createrLike,
                    updaterLike: this.formSearch.updaterLike,
                    createTimeStart: this.formSearch.createTimeStart,
                    createTimeEnd: this.formSearch.createTimeEnd,
                    updateTimeStart: this.formSearch.updateTimeStart,
                    updateTimeEnd: this.formSearch.updateTimeEnd,
                    page_no: this.currentPage,
                    page_size: this.pageSize
                }
                this.loading = true
                rbAutoOperationWorkServicesFactory
                    .queryOpsPipelineRunJobList(req)
                    .then(res => {
                        if (res.totalCount) {
                            this.loading = false
                            this.total = res.totalCount
                            this.codeList = res.dataList
                            _.map(this.codeList, function (itemRes) {
                                itemRes.statusDesc = statusDescMap[itemRes.status + '']
                            })
                        } else {
                            this.showErrorTip(res)
                        }
                        this.loading = false
                    })
                    .catch(error => {
                        this.loading = false
                        this.showErrorTip(error)
                    })
            },
            reset() {
                this.$refs['formSearch'].resetFields()
            },
            // 保存作业
            saveCode() {
                this.$refs.codeEditor.$refs.timerForm.validate((valid) => {
                    if (!valid) {
                        this.$message('请先完善信息')
                        return
                    }

                    this.pageLoading = true

                    let req = {
                        job_id: this.currentCodeInfo.job_id,
                        name: this.currentCodeInfo.name,
                        pipeline_id: this.currentCodeInfo.pipeline_id,
                        cron_expression: this.currentCodeInfo.cron_expression
                    }
                    rbAutoOperationWorkServicesFactory
                        .saveOpsPipelineRunJob(req)
                        .then(res => {
                            if (res.flag) {
                                this.$message('保存成功')
                                this.search()
                                this.codeEditorShow = false
                            } else {
                                this.showErrorTip(res)
                            }
                            this.pageLoading = false
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
            // 显示新建作业弹框
            addCode() {
                this.dialogName = '新建定时任务'
                this.currentCodeInfo = {
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
                this.codeEditorShow = true
            },
            // 编辑作业
            updateCode(row) {
                this.dialogName = '编辑任务'
                this.currentCodeInfo = {
                    job_id: row.job_id,
                    name: row.name,
                    pipeline_id: row.pipeline_id,
                    cron_expression: row.cron_expression
                }
                this.codeEditorShow = true
            },
            // 删除定时作业
            deleteCode(row) {
                this.$confirm('确认删除?').then(() => {
                    rbAutoOperationWorkServicesFactory.removeOpsPipelineRunJob(row.job_id).then(res => {
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
            // 启动定时作业
            start(row) {

                rbAutoOperationWorkServicesFactory.schedulePipelineCronJob(row.job_id).then(res => {
                    if (res.flag) {
                        this.$message({
                            message: '启动成功!',
                            type: 'success'
                        })
                        this.search()
                    } else {
                        // console.log(res)
                        this.showErrorTip(res)
                    }
                }).catch(error => {
                    this.showErrorTip(error)
                })
            },
            // 启动定时作业
            stop(row) {
                rbAutoOperationWorkServicesFactory.unSchedulePipelineCronJob(row.job_id).then(res => {
                    if (res.flag) {
                        this.$message({
                            message: '停止成功!',
                            type: 'success'
                        })
                        this.search()
                    } else {
                        // console.log(res)
                        this.showErrorTip(res)
                    }
                }).catch(error => {
                    this.showErrorTip(error)
                })
            },
            // 传递作业内容
            passCodeContent(data) {
                this.codeInfo = data
            }
        }
    }
</script>

<style scoped>
</style>