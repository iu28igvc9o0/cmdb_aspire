<template>
    <div>
        <table class="bordered mtop20">
            <tr>
                <td width="90">作业名称</td>
                <td width="20%">{{currentTaskInfo.pipeline_instance_name}}</td>
                <td width="90">启动人</td>
                <td width="20%">{{currentTaskInfo.operator}}</td>
                <td width="90">总时间(s)</td>
                <td>{{currentTaskInfo.total_time}}</td>
            </tr>
            <tr>
                <td>执行结果</td>
                <td>
                    <status-list :status="currentTaskInfo.status"></status-list>

                    <span v-if="fromHistory && currentTaskInfo.review_applicant"
                          class="blue mleft10 pointer"
                          @click="viewAuditList">详情</span>
                </td>
                <td>开始时间</td>
                <td>{{currentTaskInfo.start_time}}</td>
                <td>结束时间</td>
                <td>{{currentTaskInfo.end_time}}</td>
            </tr>
        </table>
        <section class="mtop20"
                 v-for="(stepBlock, index) in stepBlockList"
                 :key="stepBlock[0].pipeline_instance_id">
            <el-row type="flex"
                    :gutter="15"
                    align="top"
                    justify="center">
                <el-col :span="1">
                    <div class="split-num">{{index+1}}</div>
                    <div class="split-line"
                         v-if="index+1 !== stepBlockList.length"></div>
                </el-col>
                <el-col :span="23">
                    <div class="step-block relative">
                        <!-- // 操作类型  0 脚本  1 文件分发 -->
                        <div class="block-type"
                             :class="{'file' : stepBlock[0].ops_type === 1, 'download' : stepBlock[0].ops_type === 2}">
                            <div v-if="stepBlock[0].ops_type === 0">脚本</div>
                            <div v-if="stepBlock[0].ops_type === 1">文件分发</div>
                            <div v-if="stepBlock[0].ops_type === 2">结果保存</div>
                        </div>
                        <div class="step-title-box p020 border01">
                            步骤块名称：
                            <span class="title">{{stepBlock[0].block_name}}</span>
                        </div>
                        <div class="mtop10">
                            <el-table :data="stepBlock"
                                      class="yw-el-table"
                                      stripe
                                      tooltip-effect="dark"
                                      border>
                                <el-table-column prop="name"
                                                 show-overflow-tooltip
                                                 label="脚本名称"></el-table-column>
                                <el-table-column prop="total_hosts_num"
                                                 label="执行主机数"
                                                 width="120"></el-table-column>
                                <el-table-column prop="start_time"
                                                 show-overflow-tooltip
                                                 label="开始时间"></el-table-column>
                                <el-table-column prop="end_time"
                                                 show-overflow-tooltip
                                                 label="结束时间"></el-table-column>
                                <el-table-column prop="total_time"
                                                 label="总耗时(s)"
                                                 width="100"></el-table-column>
                                <el-table-column prop="status"
                                                 label="状态"
                                                 width="100">
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
                                <el-table-column label="操作"
                                                 min-width="140">
                                    <template slot-scope="scope">
                                        <div>
                                            <el-button type="text"
                                                       @click="viewScriptRunDetail(scope.row)">执行详情</el-button>
                                            <el-button type="text"
                                                       v-if="scope.row.status === 6"
                                                       @click="gotoNextStep(scope.row)">立即执行</el-button>
                                        </div>
                                    </template>
                                </el-table-column>
                            </el-table>
                        </div>
                    </div>
                </el-col>
            </el-row>
        </section>
        <div class="fixed-bottom-box t-center">
            <el-button @click="applyLog"
                       v-show="!hasApplyLog">日志打包申请</el-button>
            <el-button type="primary"
                       :disabled="!downloadActive"
                       @click="downloadLog">下载执行详情</el-button>
            <el-button type="primary"
                       @click="downloadSummaryOutput">下载执行输出文件</el-button>
        </div>
    </div>
</template>

<script>
    import { createDownloadFile } from 'src/utils/utils.js'
    import rbAutoOperationWorkServicesFactory from 'src/services/auto_operation/rb-auto-operation-work-services.factory.js'
    import statusList from 'src/pages/auto_operation/components/status-list.vue'
    import aspnodeResultList from 'src/pages/auto_operation/components/aspnode-result-list'
    export default {
        name: 'AutoOperationTaskRunResult',
        components: {
            statusList,
            aspnodeResultList
        },
        props: ['currentTaskInfo', 'stepBlockList', 'fromHistory', 'pipelineInstanceId'],
        data() {
            return {
                hasApplyLog: false,
                downloadActive: false,
                file_path: '',
                timer: null
            }
        },
        computed: {
        },
        watch: {
            // 新的实例，重置状态
            pipelineInstanceId() {
                this.hasApplyLog = false
                this.downloadActive = false
                this.getPipelineInstanceLog()
            }
        },
        created() {
            this.getPipelineInstanceLog()
        },
        destroyed() {
            clearTimeout(this.timer)
        },
        methods: {
            viewScriptRunDetail(row) {
                this.$emit('viewScriptRunDetail', row)
            },
            viewAuditList() {
                this.$emit('viewAuditList')
            },
            gotoNextStep(row) {
                this.$emit('gotoNextStep', row)
            },

            getPipelineInstanceLog() {
                // 清除之前的轮询
                let time = +sessionStorage.getItem('timeStorageResultDownload')
                if (time) {
                    clearTimeout(time)
                }

                let req = {
                    pipelineInstanceId: this.pipelineInstanceId
                }
                rbAutoOperationWorkServicesFactory.getPipelineInstanceLog(req).then(res => {
                    if (this.timer) {
                        clearTimeout(this.timer)
                    }

                    if (res && res.status === 1) {
                        this.$message.success('日志已打包成功，可下载执行详情')
                        this.file_path = res.log_path
                        this.downloadActive = true
                        this.hasApplyLog = true

                        // 点击申请后，res为空或者res.status === 0，继续轮询
                    } else if (this.hasApplyLog && (res || (res && res.status === 0))) {
                        this.timer = setTimeout(() => {
                            this.getPipelineInstanceLog()
                        }, 1000)
                        // 将轮询id保存到sessionStorage
                        sessionStorage.setItem('timeStorageResultDownload', this.timer)

                    } else {
                        this.downloadActive = false
                    }
                })
            },
            applyLog() {
                let req = {
                    pipelineInstanceId: this.pipelineInstanceId
                }
                rbAutoOperationWorkServicesFactory.logPackageApply(req).then(res => {
                    if (res.flag) {
                        this.$message.success('已申请日志打包，请稍候')
                        this.hasApplyLog = true
                        this.getPipelineInstanceLog()
                    }
                })
            },
            downloadLog() {
                let req = {
                    file_path: this.file_path
                }
                let pathArr = this.file_path.split('/')
                let filename = pathArr[pathArr.length - 1]
                rbAutoOperationWorkServicesFactory.downloadLogFile(req).then(res => {
                    if (res) {
                        this.$message.success('下载成功')
                        createDownloadFile(res, filename)
                    }
                })
            },
            downloadSummaryOutput() {
                let req = {
                    pipelineInstanceId: this.pipelineInstanceId
                }
                rbAutoOperationWorkServicesFactory.downloadSummaryOutput(req).then(res => {
                    if (res && res.byteLength > 0) {
                        this.$message.success('下载成功')
                        createDownloadFile(res, this.pipelineInstanceId + '_产出汇总.zip')
                    } else {
                        this.$message.error('该作业实例没有可正常合并的产出')
                    }
                })
            }
        }
    }
</script>

<style lang="scss" scoped>
    @import "../assets/global.scss";
</style>
