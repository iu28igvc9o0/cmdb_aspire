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
            <el-form-item label="规则名称"
                          prop="commandLike">
                <el-input v-model="formSearch.commandLike"
                          placeholder="请输入规则名称"
                          clearable></el-input>
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
            <el-form-item label="规则分组"
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
                           @click="newAdd">新增</el-button>
                <el-button type="text"
                           icon="el-icon-delete"
                           @click="deleteRow">删除</el-button>
            </div>
            <div class="yw-el-table-wrap">
                <el-table :data="dataList"
                          row-key="command"
                          class="yw-el-table"
                          stripe
                          tooltip-effect="dark"
                          border
                          height="calc(100vh - 282px)"
                          @selection-change="handleSelectionChange"
                          v-loading="loading">
                    <el-table-column type="selection"
                                     :selectable="handleSelectable"
                                     :reserve-selection="true"
                                     width="40"></el-table-column>
                    <el-table-column prop="command"
                                     label="规则名称"
                                     min-width="140"
                                     show-overflow-tooltip></el-table-column>
                    <el-table-column prop="content_type"
                                     label="规则分组"
                                     width="100">
                        <template slot-scope="scope">
                            <span v-if="scope.row.content_type === 1">shell</span>
                            <span v-if="scope.row.content_type === 2">bat</span>
                            <span v-if="scope.row.content_type === 3">python</span>
                        </template>
                    </el-table-column>
                    <el-table-column prop="rule_num"
                                     label="步骤数"
                                     width="110"></el-table-column>
                    <el-table-column prop="review_num"
                                     label="关联作业"
                                     width="110"></el-table-column>
                    <el-table-column prop="creater"
                                     label="创建人员"
                                     width="140"></el-table-column>
                    <el-table-column prop="create_time"
                                     label="创建时间"
                                     width="140"></el-table-column>
                    <el-table-column prop="update_time"
                                     label="规则描述"
                                     min-width="140"
                                     show-overflow-tooltip></el-table-column>
                    <el-table-column label="操作"
                                     width="140">
                        <template slot-scope="scope">
                            <div>
                                <el-button type="text"
                                           @click="editRow(scope.row)">编辑</el-button>
                                <el-button type="text"
                                           @click="deleteRow(scope.row)">删除</el-button>
                                <el-button type="text"
                                           @click="gotoAudit(scope.row)">复制</el-button>
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

        <!-- 新增、编辑 -->
        <el-dialog class="yw-dialog"
                   :title="dialogName"
                   :visible.sync="newAddShow"
                   :append-to-body="true"
                   :destroy-on-close="true"
                   width="1100px">
            <div class="heightp100-box">
                <main-tain :dataList="dataList"
                           @cancel="cancel">
                </main-tain>
            </div>
        </el-dialog>
    </div>
</template>

<script>
    import rbAutoOperationInstructionServicesFactory from 'src/services/auto_operation/rb-auto-operation-instruction-services.factory.js'
    import rbAutoOperationMixin from 'src/services/auto_operation/rb-auto-operation-mixin.js'

    export default {
        name: 'AutoOperationWorkManageList',
        components: {
            mainTain: () => import('../maintain/index.vue'),
        },
        data() {
            return {
                pageLoading: false,
                loading_text: '请稍候...',
                dataList: [], // 作业列表
                newAddShow: false,
                formSearch: {
                    commandLike: '',
                    creater_like: '',
                    content_type: '',
                    updateTimeStart: '',
                    updateTimeEnd: '',
                    createTimeStart: '',
                    createTimeEnd: ''
                },
                codeCloneList: [], // 作业克隆列表
                codeInfo: {}, // 作业弹框最新内容
                currentCodeInfo: {}, // 当前行作业内容
                dialogName: '新建规则', // 弹框title
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
                ]
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
                    page_no: this.currentPage,
                    page_size: this.pageSize
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
            // 新建
            newAdd() {
                this.newAddShow = true
            },
            cancel() {
                this.newAddShow = false
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
                this.newAddShow = true
            },
            gotoAudit(row) {
                let query
                if (row) {
                    query = {
                        id: row.sensitive_config_id,
                        commandLike: row.command,
                        contentType: row.content_type,
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
                    id: row.sensitive_config_id
                }
                this.$router.push({
                    path: '/auto_operation/instruction_manage/instruction',
                    query: query
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
    @import "../../assets/global.scss";
    .split-line {
        width: 10px;
        height: 20px;
        margin: 0 10px;
        border-left: 1px solid $color-border;
    }
</style>
