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
            <el-form-item label="指令内容"
                          prop="commandLike">
                <el-input v-model="formSearch.commandLike"
                          placeholder="请输入指令内容"
                          clearable></el-input>
            </el-form-item>
            <el-form-item label="脚本类型"
                          prop="content_type">
                <el-select v-model="formSearch.content_type"
                           placeholder="请选择"
                           filterable
                           clearable>
                    <el-option v-for="val in languageTypeList"
                               :key="val.id"
                               :label="val.label"
                               :value="val.id"></el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="指令级别"
                          prop="level_id">
                <el-select v-model="formSearch.level_id"
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
                <el-select v-model="formSearch.label"
                           placeholder="请选择"
                           filterable
                           clearable>
                    <el-option v-for="val in sensitiveList"
                               :key="val.key"
                               :label="val.value"
                               :value="val.key"></el-option>
                </el-select>
            </el-form-item>

            <el-form-item label="创建时间">
                <el-col :span="11">
                    <el-form-item prop="createTimeStart">
                        <el-date-picker value-format="yyyy-MM-dd HH:mm:ss"
                                        v-model="formSearch.createTimeStart"
                                        type="datetime"
                                        placeholder="开始时间"></el-date-picker>
                    </el-form-item>
                </el-col>
                <el-col class="line"
                        :span="1">-</el-col>
                <el-col :span="11">
                    <el-form-item prop="createTimeEnd">
                        <el-date-picker value-format="yyyy-MM-dd HH:mm:ss"
                                        v-model="formSearch.createTimeEnd"
                                        type="datetime"
                                        placeholder="结束时间"></el-date-picker>
                    </el-form-item>
                </el-col>
            </el-form-item>
            <el-form-item label="修改时间">
                <el-col :span="11">
                    <el-form-item prop="updateTimeStart">
                        <el-date-picker value-format="yyyy-MM-dd HH:mm:ss"
                                        v-model="formSearch.updateTimeStart"
                                        type="datetime"
                                        placeholder="开始时间"></el-date-picker>
                    </el-form-item>
                </el-col>
                <el-col class="line"
                        :span="1">-</el-col>
                <el-col :span="11">
                    <el-form-item prop="updateTimeEnd">
                        <el-date-picker value-format="yyyy-MM-dd HH:mm:ss"
                                        v-model="formSearch.updateTimeEnd"
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
                           icon="el-icon-plus"
                           @click="newInstruction">新建敏感指令</el-button>
            </div>
            <div class="yw-el-table-wrap">
                <el-table :data="dataList"
                          class="yw-el-table"
                          stripe
                          highlight-current-row
                          tooltip-effect="dark"
                          border
                          height="calc(100vh - 282px)"
                          @selection-change="handleSelectionChange"
                          v-loading="loading">
                    <el-table-column prop="command"
                                     label="指令内容"
                                     min-width="140"
                                     sortable
                                     show-overflow-tooltip></el-table-column>
                    <el-table-column prop="level_name"
                                     label="指令级别"
                                     width="100"
                                     sortable></el-table-column>
                    <el-table-column prop="label"
                                     label="指令分组"
                                     width="100"
                                     sortable>
                        <template slot-scope="scope">
                            <span v-if="scope.row.label === '1'">用户权限管理</span>
                            <span v-if="scope.row.label === '2'">字符文件处理</span>
                            <span v-if="scope.row.label === '3'">包管理及故障排查</span>
                            <span v-if="scope.row.label === '4'">资源服务管理</span>
                            <span v-if="scope.row.label === '5'">磁盘文件系统管理</span>
                            <span v-if="scope.row.label === '6'">网络管理</span>
                            <span v-if="scope.row.label === '7'">风险权限</span>
                        </template>
                    </el-table-column>
                    <el-table-column prop="content_type"
                                     label="脚本类型"
                                     sortable
                                     width="100">
                        <template slot-scope="scope">
                            <span v-if="scope.row.content_type === 1">shell</span>
                            <span v-if="scope.row.content_type === 2">bat</span>
                            <span v-if="scope.row.content_type === 3">python</span>
                        </template>
                    </el-table-column>
                    <el-table-column prop="rule_num"
                                     label="匹配规则数"
                                     sortable
                                     width="110"></el-table-column>
                    <el-table-column prop="review_num"
                                     label="匹配历史数"
                                     sortable
                                     width="110"></el-table-column>
                    <el-table-column prop="create_time"
                                     label="创建时间"
                                     sortable
                                     width="140"></el-table-column>
                    <el-table-column prop="creater"
                                     label="创建人"
                                     sortable
                                     width="140"></el-table-column>
                    <el-table-column prop="update_time"
                                     label="修改时间"
                                     sortable
                                     width="140"></el-table-column>
                    <el-table-column prop="updater"
                                     label="修改人"
                                     sortable
                                     width="140"></el-table-column>
                    <el-table-column label="操作"
                                     fixed="right"
                                     width="120">
                        <template slot-scope="scope">
                            <div>
                                <el-button type="text"
                                           title="赋权审核"
                                           icon="el-icon-finished"
                                           @click="gotoAudit(scope.row, 'audit')">
                                    <span v-if="scope.row.pending_review_num">({{scope.row.pending_review_num}})</span>
                                </el-button>
                                <el-button type="text"
                                           title="编辑"
                                           icon="el-icon-edit"
                                           @click="editRow(scope.row)"></el-button>
                                <el-button type="text"
                                           title="匹配查看"
                                           icon="el-icon-view"
                                           @click="gotoAudit(scope.row, 'view')"></el-button>
                                <el-button type="text"
                                           title="删除"
                                           icon="el-icon-delete"
                                           @click="deleteRow(scope.row)"></el-button>
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
        <!-- <run-work-dialog></run-work-dialog> -->

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
                           @click="saveCommand()">保存</el-button>
                <el-button @click="addCancel()">取消</el-button>
            </section>
        </el-dialog>
    </div>
</template>

<script>
    import rbAutoOperationInstructionServicesFactory from 'src/services/auto_operation/rb-auto-operation-instruction-services.factory.js'
    import rbAutoOperationMixin from 'src/services/auto_operation/rb-auto-operation-mixin.js'
    import rbAutoOperationServicesFactory from 'src/services/auto_operation/rb-auto-operation-services.factory.js'

    export default {
        name: 'AutoOperationWorkManageList',
        components: {
        },
        data() {
            return {
                pageLoading: false,
                loading_text: '请稍候...',
                dataList: [], // 作业列表
                codeEditorShow: false,
                formSearch: {
                    commandLike: '',
                    creater_like: '',
                    content_type: '',
                    updateTimeStart: '',
                    updateTimeEnd: '',
                    createTimeStart: '',
                    createTimeEnd: '',
                    level_id: '',
                    label: ''
                },
                codeCloneList: [], // 作业克隆列表
                codeInfo: {}, // 作业弹框最新内容
                currentCodeInfo: {}, // 当前行作业内容
                dialogName: '新建作业', // 弹框title
                runWorkBoxShow: false, // 执行作业弹框
                pipeline_id: '', // 点击行的作业id
                levelList: [],
                sensitiveList: [],// 指令分组
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
                ]
            }
        },
        mixins: [rbAutoOperationMixin],
        created() {
            this.search()
            this.queryLevelList()
            this.getSensitiveList()

        },
        methods: {
            // 查询作业列表
            search(pageNum) {
                this.currentPage = pageNum ? pageNum : this.currentPage
                let req = {
                    page_no: this.currentPage,
                    page_size: this.pageSize,
                    orderType: 'desc'
                }
                req = Object.assign(req, this.formSearch)
                this.loading = true
                this.codeCloneList = []
                rbAutoOperationInstructionServicesFactory
                    .querySensitiveConfigList(req)
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
                this.$refs['formSearch'].resetFields()
            },
            // 保存
            saveCommand() {
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
            // 新建作业
            newInstruction() {
                this.$router.push('/auto_operation/instruction_manage/instruction')
            },
            // 执行作业弹框
            showRunWork(pipelineId) {
                this.$bus.emit('showRunWork', pipelineId)
            },
            // 敏感指令分级列表
            queryLevelList() {
                rbAutoOperationServicesFactory.querySensitiveLevelList().then(res => {
                    if (res.dataList) {
                        this.levelList = res.dataList
                    }
                })
            },
            getSensitiveList() {
                let settingObj = JSON.parse(sessionStorage.getItem('sensitiveLabel'))
                if (settingObj) {
                    Object.keys(settingObj).forEach(item => {
                        this.sensitiveList.push({ 'key': item, 'value': settingObj[item].name })
                    })
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
            gotoAudit(row, type) {
                let query
                if (row) {
                    query = {
                        type: type,
                        id: row.sensitive_config_id,
                        commandLike: row.command,
                    }
                }
                this.$router.push({
                    path: '/auto_operation/instruction_manage/audit',
                    query: query
                })
            },
            // 编辑作业-更新
            editRow(row) {
                let query = {
                    id: row.sensitive_config_id,
                    currentTitle: '编辑敏感指令'
                }
                this.$router.push({
                    path: '/auto_operation/instruction_manage/instruction',
                    query: query,
                })
            },
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
            deleteRow(row) {
                this.$confirm('确认删除?').then(() => {
                    rbAutoOperationInstructionServicesFactory
                        .removeSensitiveConfig({ sensitiveConfigId: row.sensitive_config_id })
                        .then(res => {
                            if (res.flag) {
                                this.$message({
                                    message: '删除成功!',
                                    type: 'success'
                                })
                                this.search()
                            } else {
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
