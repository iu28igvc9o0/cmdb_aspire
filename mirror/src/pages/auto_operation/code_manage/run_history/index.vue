<template>
    <div class="components-container yw-dashboard">
        <el-form class="components-condition yw-form"
                 :model="formSearch"
                 @keyup.enter.native="search(1)"
                 ref="formSearch"
                 :inline="true"
                 label-width="65px">
            <el-form-item label="任务名称"
                          prop="pipeline_instance_name">
                <el-input v-model="formSearch.pipeline_instance_name"
                          placeholder="请输入任务名称"
                          clearable></el-input>
            </el-form-item>
            <el-form-item label="任务ID"
                          prop="pipeline_instance_id">
                <el-input v-model="formSearch.pipeline_instance_id"
                          placeholder="请输入任务ID"
                          clearable></el-input>
            </el-form-item>
            <el-form-item label="启动人"
                          prop="operator">
                <el-input v-model="formSearch.operator"
                          placeholder="请输入启动人"
                          clearable></el-input>
            </el-form-item>
            <el-form-item label="执行时间">
                <el-col :span="11">
                    <el-form-item prop="start_time">
                        <el-date-picker v-model="formSearch.start_time"
                                        value-format="yyyy-MM-dd HH:mm:ss"
                                        type="datetime"
                                        placeholder="开始时间"></el-date-picker>
                    </el-form-item>
                </el-col>
                <el-col class="line"
                        :span="1">-</el-col>
                <el-col :span="11">
                    <el-form-item prop="end_time">
                        <el-date-picker v-model="formSearch.end_time"
                                        value-format="yyyy-MM-dd HH:mm:ss"
                                        type="datetime"
                                        placeholder="结束时间"></el-date-picker>
                    </el-form-item>
                </el-col>
            </el-form-item>
            <el-form-item label="任务状态"
                          prop="status">
                <el-select v-model="formSearch.status"
                           placeholder="请选择"
                           filterable
                           clearable>
                    <el-option v-for="val in workStatusList"
                               :key="val.status"
                               :label="val.statusText"
                               :value="val.status"></el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="启动方式"
                          prop="trigger_way">
                <el-select v-model="formSearch.trigger_way"
                           placeholder="请选择"
                           filterable
                           clearable>
                    <el-option v-for="val in triggerWayList"
                               :key="val.status"
                               :label="val.statusText"
                               :value="val.status"></el-option>
                </el-select>
            </el-form-item>
            <section class="btn-wrap">
                <el-button type="primary"
                           @click="search(1)">查询</el-button>
                <el-button @click="reset()">重置</el-button>
            </section>
        </el-form>

        <el-form class="yw-form">
            <div class="yw-el-table-wrap">
                <el-table :data="historicalTaskList"
                          class="yw-el-table"
                          stripe
                          highlight-current-row
                          tooltip-effect="dark"
                          border
                          :height="tableHeight"
                          @selection-change="handleSelectionChange"
                          v-loading="loading">
                    <el-table-column prop="pipeline_instance_name"
                                     label="任务名称"
                                     min-width="200"
                                     show-overflow-tooltip></el-table-column>
                    <el-table-column prop="pipeline_instance_id"
                                     label="任务ID"
                                     min-width="100"
                                     show-overflow-tooltip></el-table-column>
                    <el-table-column prop="operator"
                                     label="启动人"
                                     width="100"></el-table-column>
                    <el-table-column prop="status"
                                     label="任务状态"
                                     show-overflow-tooltip
                                     width="120">
                        <!-- 状态  9 成功   101 执行失败  102 执行超时 -->
                        <template slot-scope="scope">
                            <status-list :status="scope.row.status"></status-list>
                        </template>
                    </el-table-column>
                    <el-table-column prop="aspnode_result"
                                     label="结果状态"
                                     width="100">
                        <template slot-scope="scope">
                            <aspnode-result-list :status="scope.row.aspnode_result"></aspnode-result-list>
                        </template>
                    </el-table-column>
                    <el-table-column sortable
                                     prop="start_time"
                                     label="开始时间"
                                     show-overflow-tooltip
                                     width="140"></el-table-column>
                    <el-table-column sortable
                                     prop="end_time"
                                     label="结束时间"
                                     show-overflow-tooltip
                                     width="140"></el-table-column>
                    <el-table-column prop="trigger_way"
                                     label="启动方式"
                                     show-overflow-tooltip
                                     width="120">
                        <!-- 触发方式  0页面触发 1定时作业触发 2第三方调用触发 -->
                        <template slot-scope="scope">
                            <span v-if="scope.row.trigger_way === 0">页面触发</span>
                            <span v-else-if="scope.row.trigger_way === 1">定时作业触发</span>
                            <span v-else-if="scope.row.trigger_way === 2">第三方调用触发</span>
                        </template>
                    </el-table-column>
                    <el-table-column sortable
                                     prop="total_time"
                                     label="总耗时(s)"
                                     width="100"></el-table-column>
                    <el-table-column prop="review_applicant"
                                     label="审核申请人"
                                     width="100"></el-table-column>
                    <el-table-column prop="review_apply_time"
                                     label="审核发起时间"
                                     show-overflow-tooltip
                                     width="140"></el-table-column>
                    <el-table-column label="操作"
                                     fixed="right"
                                     width="120">
                        <template slot-scope="scope">
                            <div>
                                <el-button type="text"
                                           title="详情"
                                           icon="el-icon-view"
                                           @click="viewDetail(scope.row)"></el-button>
                                <el-button type="text"
                                           title="克隆"
                                           icon="el-icon-copy-document"
                                           :disabled="scope.row.instance_classify === 2"
                                           @click="cloneTask(scope.row)"></el-button>

                                <!-- 状态  7 执行阻断   8 执行待审核 状态为8 才能发起指令审核申请 -->
                                <el-button v-if="scope.row.status === 8 && !scope.row.review_applicant"
                                           type="text"
                                           title="指令审核申请"
                                           icon="el-icon-thumb"
                                           @click="instructionAudit(scope.row)"></el-button>

                                <el-button v-if="scope.row.status === 11"
                                           type="text"
                                           title="执行"
                                           icon="el-icon-s-promotion"
                                           @click="continueExec(scope.row)"></el-button>
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

        <!-- 任务执行详情 -->
        <el-dialog class="yw-dialog"
                   title="执行详情"
                   :visible.sync="detailBoxShow"
                   width="90%">
            <task-run-result :fromHistory="true"
                             :currentTaskInfo="currentTaskInfo"
                             :pipelineInstanceId="currentTaskInfo.pipeline_instance_id"
                             :stepBlockList="stepBlockList"
                             @viewScriptRunDetail="viewScriptRunDetail"
                             @viewAuditList="viewAuditList">
            </task-run-result>
        </el-dialog>

        <!-- 步骤节点执行详情 -->
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

        <!-- 根据作业实例ID查询审核列表 -->
        <el-dialog width="90%"
                   class="yw-dialog"
                   title="敏感指令审核日志"
                   :visible.sync="auditListBoxShow"
                   :destroy-on-close="true"
                   :close-on-click-modal="false">
            <div class="yw-el-table-wrap">
                <div>
                    <span class="bold">审核申请人：</span>{{currentTaskInfo.review_applicant}}
                    <span class="mleft10"><span class="bold">审核发起时间：</span>{{currentTaskInfo.review_apply_time}}</span>
                </div>
                <el-table :data="auditList"
                          class="yw-el-table mtop10"
                          stripe
                          tooltip-effect="dark"
                          border
                          @selection-change="handleSelectionChange"
                          v-loading="loading">
                    <el-table-column prop="command"
                                     label="敏感指令"
                                     min-width="100"
                                     show-overflow-tooltip></el-table-column>
                    <el-table-column prop="rule_name"
                                     label="匹配规则"
                                     width="100"
                                     show-overflow-tooltip></el-table-column>
                    <el-table-column prop="review_content"
                                     label="违规内容描述"
                                     min-width="280"
                                     show-overflow-tooltip></el-table-column>
                    <el-table-column prop="reviewer"
                                     label="审核人/角色"
                                     width="140"></el-table-column>
                    <el-table-column prop="review_status"
                                     label="审核状态"
                                     width="100">
                        <template slot-scope="scope">
                            <span v-if="scope.row.review_status === 0">待确认</span>
                            <span v-if="scope.row.review_status === 1">待审核</span>
                            <span v-if="scope.row.review_status === 2">审核通过</span>
                            <span v-if="scope.row.review_status === 3">审核拒绝</span>
                            <span v-if="scope.row.review_status === 9">阻断</span>
                        </template>
                    </el-table-column>
                </el-table>
            </div>
        </el-dialog>
    </div>
</template>

<script>
    import taskRunResult from '../../components/task-run-result.vue'
    import codeRunResult from '../../components/code-run-result.vue'
    import statusList from 'src/pages/auto_operation/components/status-list.vue'
    import aspnodeResultList from 'src/pages/auto_operation/components/aspnode-result-list'
    import rbAutoOperationServicesFactory from 'src/services/auto_operation/rb-auto-operation-services.factory.js'
    import rbAutoOperationMixin from 'src/services/auto_operation/rb-auto-operation-mixin.js'
    export default {
        name: 'AutoOperationRunHistory',
        components: {
            taskRunResult,
            codeRunResult,
            statusList,
            aspnodeResultList
        },
        data() {
            return {
                // 审核列表
                auditList: [],
                // 审核列表弹框
                auditListBoxShow: false,
                resultBoxShow: false, // 步骤节点执行结果弹框
                stepInstId: '', // 步骤实例ID

                stepBlockList: [],  // 步骤块列表
                historicalTaskList: [], // 最近任务执行记录
                currentTaskInfo: {}, // 当前任务详情信息
                workStatusList: [],
                triggerWayList: [],
                detailBoxShow: false,

                formSearch: {
                    pipeline_instance_name: '',
                    pipeline_instance_id: '',
                    operator: sessionStorage.getItem('username'),
                    start_time: '',
                    end_time: '',
                    trigger_way: '',
                    status: ''
                },
            }
        },
        mixins: [rbAutoOperationMixin],
        mounted() {
            this.search()
            this.loadOpsStatusList()
            this.loadOpsTriggerWayList()
        },
        methods: {
            // 根据作业实例ID查询审核列表
            viewAuditList() {
                this.loading = true
                // 弹出执行结果弹框
                rbAutoOperationServicesFactory
                    .getSensitiveReviewByPipelineInstanceId({ pipelineInstanceId: this.currentTaskInfo.pipeline_instance_id })
                    .then(res => {
                        this.auditList = res
                        this.loading = false
                    })
                this.auditListBoxShow = true
            },
            // 根据ops步骤实例查询主机执行详情列表
            queryOpsStepInstanceAgentRunResultList(step_instance_id) {
                let req = {
                    step_instance_id: step_instance_id,
                    page_no: 1,
                    page_size: 100
                }
                rbAutoOperationServicesFactory.queryOpsStepInstanceAgentRunResultList(req).then(() => {
                    this.detailBoxShow = true
                })
            },
            // 根据ops历史实例查询ops步骤实例列表
            queryStepInstListByPipelineInstId(pipelineInstanceId) {
                rbAutoOperationServicesFactory.queryStepInstListByPipelineInstId(pipelineInstanceId).then(res => {
                    this.stepBlockList = []
                    res.forEach(item => {
                        if (!this.stepBlockList[item.block_ord - 1]) {
                            this.stepBlockList[item.block_ord - 1] = []
                        }
                        this.stepBlockList[item.block_ord - 1].push(item)
                    })
                })
            },
            // 查看脚本执行详情
            viewScriptRunDetail(row) {
                // 弹出执行结果弹框
                this.stepInstId = row.step_instance_id
                this.resultBoxShow = true
            },
            // 查看详情
            viewDetail(row) {
                this.currentTaskInfo = row
                this.queryStepInstListByPipelineInstId(row.pipeline_instance_id)
                this.detailBoxShow = true
            },
            // 克隆任务
            cloneTask(row) {
                // instance_classify:1.实时脚本作业  2.实时文件分发 9. 作业
                if (row.instance_classify === 1) {
                    this.$router.push({
                        path: '/auto_operation/code_manage/run',
                        query: {
                            pipelineInstanceId: row.pipeline_instance_id
                        }
                    })
                } else if (row.instance_classify === 9) {
                    this.$router.push({
                        path: '/auto_operation/work_manage/task',
                        query: {
                            cloneId: row.pipeline_id,
                            type: 'clone',
                            currentTitle: '克隆作业'
                        }
                    })
                }
            },
            // 指令审核
            instructionAudit(row) {
                this.$confirm('确定进行指令审核申请？').then(() => {
                    this.loading = true
                    rbAutoOperationServicesFactory.reviewSensitiveApply(row.pipeline_instance_id).then(res => {
                        if (res.flag) {
                            this.$message.success('申请成功！')
                            this.search()
                        } else {
                            this.$message.error(res.error_tip)
                        }
                        this.loading = false
                    })
                })
            },
            // 转换数据格式 [{"0": "执行成功"}] => [{status: "0", statusText: "执行成功"}]
            handleData(list) {
                let arr = []
                list.forEach(item => {
                    let obj = {}
                    Object.keys(item).forEach(key => {
                        obj['status'] = +key
                        obj['statusText'] = item[key]
                    })
                    arr.push(obj)
                })
                return arr
            },
            // 查询执行状态列表
            loadOpsStatusList() {
                rbAutoOperationServicesFactory.loadOpsStatusList().then(res => {
                    this.workStatusList = this.handleData(res)
                })
            },
            // 查询触发方式列表
            loadOpsTriggerWayList() {
                rbAutoOperationServicesFactory.loadOpsTriggerWayList().then(res => {
                    this.triggerWayList = this.handleData(res)
                })
            },
            // 最近任务执行记录
            search(pageNum) {
                this.currentPage = pageNum ? pageNum : this.currentPage
                let req = {
                    page_no: this.currentPage,
                    page_size: this.pageSize
                }
                req = Object.assign(req, this.formSearch)
                this.loading = true
                this.historicalTaskList = []
                rbAutoOperationServicesFactory
                    .queryOpsPipelineInstanceList(req)
                    .then(res => {
                        this.loading = false
                        this.total = res.totalCount
                        this.historicalTaskList = res.dataList
                    })
                    .catch(() => {
                        this.loading = false
                    })
            },
            reset() {
                this.$refs['formSearch'].resetFields()
            },
            //  执行
            continueExec(row) {
                this.$confirm('确定进行执行？').then(() => {
                    this.loading = true
                    rbAutoOperationServicesFactory.continueExecInstance({ pipelineInstanceId: row.pipeline_instance_id }).then(res => {
                        if (res.flag) {
                            this.$message.success('执行成功！')
                            this.search()
                        } else {
                            this.$message.error(res.error_tip)
                        }
                        this.loading = false
                    })
                })
            }
        }
    }
</script>

<style lang="scss" scoped>
    .drawer-close-btn {
        float: right;
        margin: -20px 10px 12px 0;
    }
</style>
