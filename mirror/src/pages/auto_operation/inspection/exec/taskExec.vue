<template>
    <div class="components-container yw-dashboard">
        <el-form class="components-condition yw-form"
                 :inline="true"
                 label-width="65px">
            <el-form-item label="任务名称">
                <el-input v-model="name"
                          placeholder="请输入内容"></el-input>
            </el-form-item>

            <el-form-item label="开始时间">
                <el-date-picker v-model="time_range"
                                type="datetimerange"
                                range-separator="至"
                                start-placeholder="开始日期"
                                end-placeholder="结束日期"
                                value-format="yyyy-MM-dd HH:mm:ss">
                </el-date-picker>
            </el-form-item>
            <el-form-item label="任务状态">
                <el-select v-model="task_status"
                           placeholder="请选择"
                           clearable>
                    <el-option v-for="item in reportStatusList"
                               :key="item.value"
                               :label="item.label"
                               :value="item.value">
                    </el-option>
                </el-select>
            </el-form-item>
            <section class="btn-wrap">
                <el-button type="primary"
                           @click="search()">查询
                </el-button>
                <el-button @click="reset()">重置</el-button>
            </section>
        </el-form>

        <el-form class="yw-form">
            <div class="table-operate-wrap clearfix">
            </div>
            <div class="yw-el-table-wrap">
                <el-table :data="tableData"
                          class="yw-el-table"
                          stripe
                          tooltip-effect="dark"
                          border
                          height="calc(100vh - 275px)"
                          @selection-change="handleSelectionChange"
                          :default-sort="{prop: 'finish_time', order: 'descending'}">
                    <el-table-column prop="task_name"
                                     show-overflow-tooltip
                                     label="任务名称"
                                     sortable>
                    </el-table-column>
                    <el-table-column prop="template_names"
                                     show-overflow-tooltip
                                     label="巡检模板">
                    </el-table-column>
                    <el-table-column prop="create_time"
                                     show-overflow-tooltip
                                     label="开始时间"
                                     sortable>
                        <template slot-scope="scope">
                            {{ scope.row.create_time | formatDate}}
                        </template>
                    </el-table-column>
                    <el-table-column prop="finish_time"
                                     show-overflow-tooltip
                                     label="结束时间"
                                     sortable>
                        <template slot-scope="scope">
                            {{ scope.row.finish_time | formatDate}}
                        </template>
                    </el-table-column>
                    <el-table-column prop="status"
                                     label="状态"
                                     sortable>
                    </el-table-column>
                    <el-table-column prop="result"
                                     show-overflow-tooltip
                                     min-width="300px"
                                     label="巡检结果">
                        <template slot-scope="scope">
                            <div v-html="scope.row.result"></div>
                        </template>
                    </el-table-column>
                    <el-table-column prop="report"
                                     width="80"
                                     label="巡检报告">
                        <template slot-scope="scope">
                            <el-button type="text"
                                       title="日志详情"
                                       icon="el-icon-view"
                                       @click="reportDetails(scope.row)">
                            </el-button>
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
                               :total="total">
                </el-pagination>
            </div>
            <el-dialog class="yw-dialog"
                       :title="dialogName"
                       :visible.sync="execDetailShow"
                       width="1050px"
                       :close-on-click-modal="false">
                <exec-detail ref="execDetail"
                             :reportId="reportId"
                             :taskId="taskId"></exec-detail>
            </el-dialog>
        </el-form>

        <!-- dialog -->

        <!-- dialog -->
    </div>

</template>

<script>
    // import rbHttp from '../../../../assets/js/utility/rb-http.factory.js'
    import { taskState, reportStatus } from './config/options.js'
    import rbProjectDataServiceFactory from 'src/services/template/rb-template-services.factory.js'
    import rbMirrorCommonService from 'src/services/mirror/rb-mirror-common-services.factory.js'
    import { formatDate } from 'src/assets/js/utility/rb-filters.js'
    import execDetail from './detail/exec-detail.vue'
    export default {
        // props: ['task_name'],
        components: {
            execDetail
        },
        data() {
            return {
                name: '',
                taskId: '',
                reportId: '',
                execDetailShow: false,
                dialogName: '',
                time_range: [],
                task_state: [],
                task_status: '',
                reportStatusList: reportStatus,
                // 多选框模板存放的值
                multipleSelection: [],
                tableData: [],
                // 当前页
                currentPage: 1,
                // 分页每页多少行数据
                pageSize: 10,
                // 每页多少行数组
                pageSizes: [10, 20, 50, 100],
                // 总共多少行数据
                total: 0
            }
        },
        watch: {
            task_name(val) {
                this.name = val

                //
            }
        },
        methods: {
            // 多选框
            handleSelectionChange(val) {
                this.multipleSelection = val
            },
            // 分页改变尺寸
            handleSizeChange(val) {
                this.pageSize = val
                this.search(1)
            },
            // 分页改变当前页
            handleCurrentChange(val) {
                this.currentPage = val
                this.search(1)
            },
            // 业务逻辑
            reset() {
                this.task_status = ''
                this.name = ''
                this.time_range = []
            },
            search(num) {
                if (num !== 1) {
                    // 搜索前将当前页置为1
                    this.currentPage = 1
                }
                // let status1 = ''
                // if (this.task_status !== '') {
                //     status1 = rbMirrorCommonService.getDaoTaskStatus(this.task_status)
                // }
                let obj = {
                    inspection_time_start: this.time_range ? formatDate(this.time_range[0]) : '',
                    inspection_time_end: this.time_range ? formatDate(this.time_range[1]) : '',
                    name: this.name,
                    status: this.task_status,
                    page_no: this.currentPage,
                    page_size: this.pageSize
                }
                rbProjectDataServiceFactory.getTaskReport(obj).then((res) => {
                    this.tableData = this.packData(res.result)
                    this.total = res.count
                })
            },
            reportDetails(obj) {
                // this.$store.commit('reportDetail', obj.report_id)
                // this.$router.push('/mirror/report/reportDetails')
                this.dialogName = '运行日志详情'
                this.taskId = obj.task_id
                this.reportId = obj.report_id
                this.execDetailShow = true
            },
            // 封装数据
            packData(arr) {
                arr.forEach((item) => {
                    item.status = rbMirrorCommonService.getTaskStatus(item.status)
                    // item.create_time = formatTime2(item.create_time)
                    // item.finish_time = formatTime2(item.finish_time)
                })
                return arr
            },
            // 数据来源
            getTableData() {
                let obj = {
                    page_no: this.currentPage,
                    page_size: this.pageSize
                }
                rbProjectDataServiceFactory.getTaskReport(obj).then((res) => {
                    this.tableData = this.packData(res.result)
                    this.total = res.count
                })
            },
            selectData() {
                if (this.$store.state.homeStore.task_state) {
                    let id = Object.assign(this.$store.state.homeStore.task_id, {})
                    let obj = {
                        task_id: id,
                        page_no: this.currentPage,
                        page_size: this.pageSize
                    }
                    rbProjectDataServiceFactory.getTaskReport(obj).then((res) => {
                        this.tableData = this.packData(res.result)
                        this.total = res.count
                        this.$store.commit('taskState', false)
                    })
                } else {
                    this.tableData = this.getTableData()
                }
            }
        },
        mounted() {
            this.name = this.$route.query.task_name
            this.search()
            this.task_state = taskState
            // this.task_status = reportStatus

        }
    }
</script>

<style lang="scss" scoped>
</style>
