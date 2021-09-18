
<template>
    <!-- 自动化：作业概览 -->
    <div class="p10 pbottom30">
        <!-- 数据面板 -->
        <el-row :gutter="10"
                type="flex">
            <el-col :span="6">
                <el-card class="box-card">
                    <el-row type="flex"
                            class="panel-box"
                            align="middle"
                            justify="start">
                        <el-col :span="7"
                                align="center">
                            <img src="/static/img/alert/A.png" />
                        </el-col>
                        <el-col :span="9">
                            <div>业务主机</div>
                            <div class="target-num">{{hostStatistic.agentCount}}</div>
                        </el-col>
                        <el-col :span="8">
                            <!-- <div>Agent正常：3</div> -->
                        </el-col>
                    </el-row>
                </el-card>
            </el-col>
            <el-col :span="6">
                <el-card class="box-card">
                    <el-row type="flex"
                            class="panel-box"
                            align="middle"
                            justify="start">
                        <el-col :span="7"
                                align="center">
                            <img src="/static/img/alert/A.png" />
                        </el-col>
                        <el-col :span="9">
                            <div>常用作业</div>
                            <div class="target-num">{{pipelineStatistic.pipelineCount}}</div>
                        </el-col>
                        <el-col :span="8">
                        </el-col>
                    </el-row>
                </el-card>
            </el-col>
            <el-col :span="6">
                <el-card class="box-card">
                    <el-row type="flex"
                            class="panel-box"
                            align="middle"
                            justify="start">
                        <el-col :span="7"
                                align="center">
                            <img src="/static/img/alert/B.png" />
                        </el-col>
                        <el-col :span="9">
                            <div>近30天任务</div>
                            <div class="target-num">{{recent30DaysRunStatistic.totalCount}}</div>
                        </el-col>
                        <el-col :span="8"
                                class="f16">
                            <div>执行中：{{recent30DaysRunStatistic.runCount}}</div>
                            <div>成功：{{recent30DaysRunStatistic.successCount}}</div>
                            <div>失败：{{recent30DaysRunStatistic.failCount}}</div>
                            <div>超时：{{recent30DaysRunStatistic.timeoutCount}}</div>
                        </el-col>
                    </el-row>
                </el-card>
            </el-col>
            <el-col :span="6">
                <el-card class="box-card">
                    <el-row type="flex"
                            class="panel-box"
                            align="middle"
                            justify="start">
                        <el-col :span="7"
                                align="center">
                            <img src="/static/img/alert/C.png" />
                        </el-col>
                        <el-col :span="9">
                            <div>定时任务</div>
                            <div class="target-num">{{+pipelineJobStatistic.runJobCount + +pipelineJobStatistic.pauseJobCount}}</div>
                        </el-col>
                        <el-col :span="8">
                            <div>启动：{{+pipelineJobStatistic.runJobCount}}</div>
                            <div>暂停：{{+pipelineJobStatistic.pauseJobCount}}</div>
                        </el-col>
                    </el-row>
                </el-card>
            </el-col>
        </el-row>
        <!-- 历史任务执行情况 -->
        <historical-task-line></historical-task-line>
        <el-row :gutter="10">
            <!-- 任务执行时长统计 -->
            <el-col :span="12">
                <task-duration></task-duration>
            </el-col>
            <!-- 最近任务执行记录 -->
            <el-col :span="12">
                <el-card class="box-card mtop15">
                    <div slot="header">
                        <span style="font-weight: bold">最近任务执行记录</span>
                        <div class="clearfix fr">
                            <el-button type="text"
                                       @click="getMore">更多</el-button>
                        </div>
                    </div>

                    <div class="yw-el-table-wrap">
                        <el-table :data="historicalTaskList"
                                  class="yw-el-table"
                                  stripe
                                  tooltip-effect="dark"
                                  height="214"
                                  v-loading="loading">
                            <el-table-column prop="pipeline_instance_name"
                                             label="任务名称"></el-table-column>
                            <el-table-column prop="operator"
                                             label="启动人"
                                             width="120"></el-table-column>
                            <el-table-column prop="status"
                                             label="任务状态"
                                             width="120">
                                <!-- 状态  9 成功   101 执行失败  102 执行超时 -->
                                <template slot-scope="scope">
                                    <status-list :status="scope.row.status"></status-list>
                                </template>
                            </el-table-column>
                            <el-table-column prop="updater"
                                             label="最后修改人"
                                             width="120"></el-table-column>
                        </el-table>
                    </div>
                    <div class="yw-page-wrap">
                        <el-pagination @size-change="handleSizeChange"
                                       @current-change="handleCurrentChange"
                                       :current-page="currentPage"
                                       :page-sizes="pageSizes"
                                       :page-size="pageSize"
                                       :total="total"
                                       small
                                       layout="total, sizes, prev, pager, next, jumper"></el-pagination>
                    </div>
                </el-card>
            </el-col>
        </el-row>
    </div>
</template>
 
<script>
    import rbAutoOperationHomeServicesFactory from 'src/services/auto_operation/rb-auto-operation-home-services.factory.js'
    import rbAutoOperationMixin from 'src/services/auto_operation/rb-auto-operation-mixin.js'
    import historicalTaskLine from './historical-task-line'
    import taskDuration from './task-duration'
    import statusList from 'src/pages/auto_operation/components/status-list.vue'
    export default {
        name: 'AutoOperationHome',
        components: {
            historicalTaskLine,
            taskDuration,
            statusList
        },
        data() {
            return {
                historicalTaskList: [], // 最近任务执行记录
                hostStatistic: {}, // 业务主机
                pipelineStatistic: {}, // 常用作业
                recent30DaysRunStatistic: {}, // 近30天任务
                pipelineJobStatistic: {
                    runJobCount: 0,
                    pauseJobCount: 0
                }, // 定时任务
            }
        },
        mixins: [rbAutoOperationMixin],
        computed: {},
        created() {
            this.queryIndexHostStatistic()
            this.queryIndexPipelineStatistic()
            this.queryIndexRecent30DaysRunStatistic()
            this.queryIndexPipelineJobStatistic()
            this.search()
        },
        methods: {
            // 业务主机
            queryIndexHostStatistic() {
                rbAutoOperationHomeServicesFactory
                    .queryIndexHostStatistic()
                    .then(res => {
                        this.hostStatistic = res
                    })
            },
            // 常用作业
            queryIndexPipelineStatistic() {
                rbAutoOperationHomeServicesFactory
                    .queryIndexPipelineStatistic()
                    .then(res => {
                        this.pipelineStatistic = res
                    })
            },
            // 近30天任务
            queryIndexRecent30DaysRunStatistic() {
                rbAutoOperationHomeServicesFactory
                    .queryIndexRecent30DaysRunStatistic()
                    .then(res => {
                        this.recent30DaysRunStatistic = res
                    })
            },
            // 定时任务
            queryIndexPipelineJobStatistic() {
                rbAutoOperationHomeServicesFactory
                    .queryIndexPipelineJobStatistic()
                    .then(res => {
                        this.pipelineJobStatistic = res
                    })
            },
            // 最近任务执行记录
            search() {
                let req = {
                    page_no: this.currentPage,
                    page_size: this.pageSize
                }
                this.loading = true
                rbAutoOperationHomeServicesFactory
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
            getMore() {
                this.$router.push({
                    path: '/auto_operation/code_manage/run_history'
                })
            }
        },
    }
</script>
 
<style  lang="scss" scoped>
    @import "../assets/global.scss";
    .panel-box {
        padding: 10px 0;
        font-size: $font-20;
        img {
            width: 0.45rem;
            height: 0.45rem;
        }
        .target-num {
            font-size: $font-30;
            color: $color-tip-orange;
        }
    }
    .mtop15 {
        margin-top: 15px;
    }
</style>
