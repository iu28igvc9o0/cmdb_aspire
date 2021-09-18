<template>
    <div class="components-container yw-dashboard"
         v-loading.fullscreen.lock="pageLoading">
        <el-form class="components-condition yw-form"
                 :inline="true"
                 label-width="60px">
            <el-form-item label="规则名称">
                <el-input v-model="checkedData.scheme_name_like"
                          placeholder="请输入内容"></el-input>
            </el-form-item>
            <el-form-item label="任务名称">
                <el-input v-model="checkedData.pipe_inst_name_like"
                          placeholder="请输入内容"></el-input>
            </el-form-item>
            <el-form-item label="开始时间">
                <el-date-picker v-model="checkedData.time_range"
                                type="datetimerange"
                                range-separator="至"
                                start-placeholder="开始日期"
                                end-placeholder="结束日期"
                                value-format="yyyy-MM-dd HH:mm:ss"></el-date-picker>
            </el-form-item>
            <el-form-item label="任务状态">
                <el-select v-model="checkedData.status"
                           placeholder="请选择"
                           clearable>
                    <el-option v-for="item in selectoptions"
                               :key="item.value"
                               :label="item.label"
                               :value="item.value"></el-option>
                </el-select>
            </el-form-item>

            <section class="btn-wrap">
                <el-button type="primary"
                           @click="search(1)">查询</el-button>
                <el-button @click="reset()">重置</el-button>
            </section>
        </el-form>
        <div class="yw-el-table-wrap">
            <el-table :data="tableData"
                      class="yw-el-table"
                      stripe
                      highlight-current-row
                      tooltip-effect="dark"
                      border
                      height="calc(100vh - 270px)"
                      :default-sort="{prop: 'create_time', order: 'descending'}">
                <el-table-column prop="_executedPipeInstNameList"
                                 label="任务名称"
                                 sortable
                                 min-width="140px"
                                 show-overflow-tooltip></el-table-column>
                <el-table-column prop="schemeName"
                                 label="自愈模板"
                                 min-width="140px"
                                 show-overflow-tooltip></el-table-column>
                <el-table-column prop="triggerTime"
                                 show-overflow-tooltip
                                 label="开始时间"
                                 width="140"></el-table-column>
                <el-table-column prop="endTime"
                                 show-overflow-tooltip
                                 label="结束时间"
                                 width="140"></el-table-column>
                <el-table-column prop="status"
                                 label="状态"
                                 min-width="100">
                    <template slot-scope="scope">
                        <span v-if="scope.row.status === 6">
                            <el-button type="text"
                                       @click="clickStatus(1,scope.row)">继续执行</el-button>
                            <el-button type="text"
                                       @click="clickStatus(0,scope.row)">终止执行</el-button>
                        </span>
                        <span v-else>
                            <status-list :status="scope.row.status"></status-list>
                        </span>
                    </template>
                </el-table-column>
                <el-table-column prop="aspNodeResult"
                                 label="执行结果">
                    <template slot-scope="scope">{{scope.row.aspNodeResult === 0 ? '正常' : '异常'}}</template>
                </el-table-column>
                <el-table-column prop="aspNodeMessage"
                                 label="执行详情"
                                 min-width="200px"
                                 show-overflow-tooltip></el-table-column>
                <el-table-column prop="operation"
                                 width="60"
                                 label="操作">
                    <template slot-scope="scope">
                        <el-button type="text"
                                   title="查看详情"
                                   icon="el-icon-view"
                                   @click="viewLogDetail(scope.row)"></el-button>
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
                           layout="total, sizes, prev, pager, next, jumper"
                           :total="total"></el-pagination>
        </div>

        <!-- 自愈日志详情 -->
        <el-dialog class="yw-dialog"
                   title="自愈日志详情"
                   :visible.sync="logDetailShow"
                   width="90%">
            <div>
                <table class="bordered mtop20">
                    <tr>
                        <td width="90">自愈模版</td>
                        <td width="20%">{{currentLogInfo.schemeName}}</td>
                        <td width="90">任务数</td>
                        <td width="20%">{{historicalTaskList.length}}</td>
                        <td width="90">状态</td>
                        <td>
                            <span v-if="currentLogInfo.status === 6">
                                <el-button type="text"
                                           @click="clickStatus(1,currentLogInfo)">继续执行</el-button>
                                <el-button type="text"
                                           @click="clickStatus(0,currentLogInfo)">终止执行</el-button>
                            </span>
                            <span v-else>{{statusDescMap[currentLogInfo.status]}}</span>
                        </td>
                    </tr>
                    <tr>
                        <td>开始时间</td>
                        <td>{{currentLogInfo.triggerTime}}</td>
                        <td>结束时间</td>
                        <td>{{currentLogInfo.endTime}}</td>
                        <td></td>
                        <td></td>
                    </tr>
                </table>

                <div class="yw-el-table-wrap mtop20">
                    <el-table :data="historicalTaskList"
                              height="50vh"
                              class="yw-el-table"
                              stripe
                              tooltip-effect="dark"
                              border
                              v-loading="loading">
                        <el-table-column prop="pipeline_instance_name"
                                         label="任务名称"
                                         min-width="200"
                                         show-overflow-tooltip></el-table-column>
                        <el-table-column prop="status"
                                         label="任务状态"
                                         width="100">
                            <!-- 状态  9 成功   101 执行失败  102 执行超时 -->
                            <template slot-scope="scope">
                                <status-list :status="scope.row.status"></status-list>
                            </template>
                        </el-table-column>
                        <el-table-column sortable
                                         prop="start_time"
                                         label="开始时间"
                                         min-width="140"></el-table-column>
                        <el-table-column sortable
                                         prop="end_time"
                                         label="结束时间"
                                         min-width="140"></el-table-column>
                        <el-table-column sortable
                                         prop="total_time"
                                         label="总耗时(s)"
                                         min-width="100"></el-table-column>
                        <el-table-column label="操作">
                            <template slot-scope="scope">
                                <div>
                                    <el-button type="text"
                                               @click="viewDetail(scope.row)">详情</el-button>
                                </div>
                            </template>
                        </el-table-column>
                    </el-table>
                </div>
            </div>
        </el-dialog>

        <!-- 任务执行详情 -->
        <el-dialog class="yw-dialog"
                   title="执行详情"
                   :visible.sync="detailBoxShow"
                   width="90%">
            <task-run-result :fromHistory="true"
                             :currentTaskInfo="currentTaskInfo"
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

    </div>
</template>

<script>
    import taskRunResult from '../../components/task-run-result.vue'
    import codeRunResult from '../../components/code-run-result.vue'
    import statusList from 'src/pages/auto_operation/components/status-list.vue'
    import rbAutoHealingServicesFactory from 'src/services/auto_operation/rb-auto-healing-services.factory.js'
    import rbAutoOperationServicesFactory from 'src/services/auto_operation/rb-auto-operation-services.factory.js'
    import rbAutoOperationMixin from 'src/services/auto_operation/rb-auto-operation-mixin.js'

    export default {
        components: {
            taskRunResult,
            codeRunResult,
            statusList
        },
        data() {
            return {
                pageLoading: false,
                tableData: [],
                statusDescMap: {},
                selectoptions: [{
                    label: '全部',
                    value: '',
                    key: ''
                }],
                checkedData: {
                    scheme_name_like: '',
                    time_range: [],
                    status: '',
                    pipe_inst_name_like: ''
                },

                currentLogInfo: {},
                logDetailShow: false, // 日志详情
                detailBoxShow: false, // 任务执行详情
                currentTaskInfo: {}, // 当前任务详情信息
                historicalTaskList: [], // 任务节点列表
                resultBoxShow: false, // 步骤节点执行结果弹框
                stepBlockList: [],  // 步骤块列表
                stepInstId: '', // 步骤实例ID
            }
        },
        mixins: [rbAutoOperationMixin],
        methods: {
            search(pageNum) {
                this.currentPage = pageNum ? pageNum : this.currentPage
                this.getList()
            },
            reset() {
                this.checkedData = {
                    scheme_name_like: '',
                    time_range: [],
                    status: '',
                    pipe_inst_name_like: ''
                }
            },
            getList() {
                let obj = {
                    scheme_name_like: this.checkedData.scheme_name_like || '',
                    pipe_inst_name_like: this.checkedData.pipe_inst_name_like || '',
                    status: this.checkedData.status || '',
                    create_time_start: this.checkedData.time_range ? this.checkedData.time_range[0] : null,
                    create_time_end: this.checkedData.time_range ? this.checkedData.time_range[1] : null,
                    page_no: this.currentPage,
                    page_size: this.pageSize,
                }
                rbAutoHealingServicesFactory.queryOpsAutoRepairExecHistory(obj).then((data) => {
                    this.total = data.totalCount
                    if (data.dataList) {
                        // executedPipeInstNameList
                        data.dataList.forEach(v => {
                            if (v.executedPipeInstNameList && v.executedPipeInstNameList.length) {
                                v._executedPipeInstNameList = v.executedPipeInstNameList.join(',')
                            }
                        })
                        this.tableData = data.dataList
                    } else {
                        this.tableData = []
                    }
                })
            },
            clickStatus(v, rwo) {
                let obj = {
                    schemeExecLogId: rwo.id,
                    manualStatus: v
                }
                this.pageLoading = true
                rbAutoHealingServicesFactory.manualHandleApSchemeExecute(obj).then((res) => {
                    if (res.flag === true) {
                        this.$message({
                            message: '执行成功',
                            type: 'success'
                        })
                        this.getList()
                        this.pageLoading = false
                    } else {
                        this.pageLoading = false
                        this.$message('执行失败!')
                    }
                }).catch(() => {
                    this.pageLoading = false
                    this.$message('执行失败!')
                })
            },
            getStatusList() {
                rbAutoHealingServicesFactory.loadApStatusList().then(res => {
                    if (res && res.length) {
                        res.forEach(v => {
                            this.statusDescMap = { ...this.statusDescMap, ...v }
                            this.selectoptions.push({
                                label: Object.values(v)[0],
                                value: Object.keys(v)[0],
                                key: Object.keys(v)[0]
                            })
                        })
                    }
                })
            },
            // 查看日志详情
            viewLogDetail(row) {
                let req = {
                    pipeline_instance_id_list: row.executedPipeInstIdList,
                }

                this.logDetailShow = true
                this.loading = true
                this.currentLogInfo = row
                this.historicalTaskList = []
                rbAutoOperationServicesFactory
                    .queryOpsPipelineInstanceList(req)
                    .then(res => {
                        this.loading = false
                        this.historicalTaskList = res.dataList
                    })
                    .catch(() => {
                        this.loading = false
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
        },
        mounted() {
            let query = this.$route.query
            // 作业预览跳转进入,查询人工干预状态
            if (query.status !== undefined) {
                this.checkedData.status = query.status
            }
            this.getList()
            this.getStatusList()
        }
    }
</script>