
<template>
    <!-- 创建、编辑变更计划 -->
    <div>
        <el-form>
            <el-form-item>
                <div class="stepsClass"
                     style="overflow-y: auto;max-height: 210px;padding:0 12px;">
                    <el-steps direction="vertical"
                              style="margin-bottom:10px"
                              :active="1"
                              v-for="(item,index) in steplist"
                              :key="index">
                        <span class="operate-text">
                            {{item.operate_time}}
                        </span>
                        <el-step :title="item.operate_type_desc"
                                 v-if="item.operate_type_desc === '执行成功' && item.operate_content !== '自动执行成功'"
                                 description="">
                        </el-step>
                        <el-step :title="item.operate_content"
                                 v-else-if="item.operate_content === '自动执行成功'"
                                 description="">
                        </el-step>
                        <el-step :title="item.operate_type_desc"
                                 v-else-if="item.operate_type_desc === '执行失败'"
                                 :description="'原因：' + taskDetailData.content">
                        </el-step>
                        <el-step :title="item.operate_type_desc"
                                 v-else-if="item.operate_type_desc === '执行开始'"
                                 :description="'工单号：' + taskDetailData.orderId">
                        </el-step>
                        <el-step :title="item.operate_type_desc"
                                 v-else
                                 :description="item.operater">
                        </el-step>
                    </el-steps>
                </div>
            </el-form-item>

            <el-form-item>
                <div class="box-title">
                    任务信息
                </div>
                <div class="box-title-content">
                    <div class="box-title-item">
                        <p>任务标题</p>
                        <p>{{taskDetailData.taskName}}</p>
                    </div>
                    <div class="box-title-item">
                        <p>操作类型</p>
                        <p>{{taskDetailData.taskType == '1'?'割接':'变更'}}</p>
                    </div>
                    <div class="box-title-item-describe"
                         style="line-height:25px">
                        <p>任务描述</p>
                        <p>{{taskDetailData.taskDescription}}</p>
                    </div>
                    <div class="box-title-item">
                        <p>资源池</p>
                        <p>{{taskDetailData.idcType}}</p>
                    </div>
                    <div class="box-title-item">
                        <p>计划执行开始时间</p>
                        <p>{{taskDetailData.taskStartTime}}</p>
                    </div>
                    <div class="box-title-item"
                         style="border-bottom:none">
                        <p>计划执行结束时间</p>
                        <p>{{taskDetailData.taskEndTime}}</p>
                    </div>
                </div>
            </el-form-item>
            <el-form-item style="margin-bottom:0">
                <div class="box-title">
                    反馈意见
                </div>
                <div style="margin:10px">
                    <div v-if="taskDetailData.taskStatus === '1'">
                        <el-input clearable="true"
                                  type="textarea"
                                  placeholder="请输入内容"
                                  v-model="feedbackContent"
                                  maxlength="100"
                                  rows="3"
                                  show-word-limit>
                        </el-input>
                        <section class="t-right">
                            <el-button type="primary"
                                       @click="feedbackContentSubmission">提交</el-button>
                        </section>
                    </div>
                    <div v-if="feedbackList.length>0"
                         class="box-title-content-opinion">
                        <div class="feedbackOpinion"
                             v-for="(item,index) in feedbackList"
                             :key="index">
                            <div class="feedback-text wp100">
                                <p class="wp100">{{item.operater}} <span class="f-right">{{item.operate_time | formatNoYear}}</span></p>
                                <p>{{item.operate_content}}</p>
                            </div>
                            <!-- <div class="t-right feedback-time">
                                {{item.operate_time | formatNoYear}}
                            </div> -->
                        </div>
                    </div>
                    <div v-else
                         class="box-title-content-opinion"
                         style="text-align:center">
                        暂无反馈
                    </div>
                    <div v-if="feedbackList.length>0"
                         style="margin-top:10px">
                        <el-pagination small
                                       @size-change="handleSizeChange"
                                       @current-change="handleCurrentChange"
                                       :current-page.sync="currentPage1"
                                       :page-size="10"
                                       layout="total, prev, pager, next"
                                       :pager-count="5"
                                       :total="countNum">
                        </el-pagination>
                    </div>

                </div>
            </el-form-item>
        </el-form>
        <section v-if="taskDetailData.taskStatus ==='1'"
                 class="btn-wrap t-right t-flex"
                 style="margin-bottom:10px">
            <el-button @click="deleteTask">删除</el-button>
            <el-button @click="editTask">编辑</el-button>
            <el-button type="primary"
                       @click="startTask">开始执行</el-button>
        </section>
        <section v-else-if="taskDetailData.taskStatus ==='2'"
                 class="btn-wrap t-right t-flex"
                 style="margin-bottom:10px">
            <el-button type="primary"
                       @click="endTaskConfirmB">结束执行</el-button>
        </section>
        <section v-else-if="taskDetailData.taskStatus ==='4'"
                 class="btn-wrap t-right t-flex"
                 style="margin-bottom:10px">
            <el-button type="primary"
                       @click="confirmExecutionResults(taskDetailData)">确认执行结果</el-button>
        </section>
    </div>
</template>
 
<script>
    export default {
        name: 'DoorPlanListTaskDetail',
        components: {
        },
        props: {
            uuid: {
                type: String,
                default: ''
            },
            taskDetailData: {
                type: Object,
                default: {}
            },
            endExecutionSuccessfulnotice: {
                type: Object,
                default: {}
            },
            status3: {
                type: Boolean,
                default: {}

            }
        },
        data() {
            return {
                status3: true,
                uuidList: '',
                feedbackContent: '',
                steplist: [], // 步奏条列表
                feedbackList: [], // 反馈意见列表
                countNum: 0,
                pageSize: 10,
                pageNum: 1,

                startEaskS: {
                    start: true,
                    startlist: {}
                },
                endTaskConfirm: { // 结束执行
                    endTaskList: {}
                }
            }
        },
        computed: {
        },
        watch: {
            endExecutionSuccessfulnotice(val) {
                this.taskDetailData.taskStatus = '0'
                this.taskDetailData = val

            },
            taskDetailData: {
                handler(val) {
                    this.feedbackContent = ''
                    this.startEaskS.startlist = val
                    this.endTaskConfirm.endTaskList = val
                    this.rbHttp.sendRequest({ // 步奏条
                        method: 'GET',
                        url: '/v1/alerts/task/getTaskActionList?uuid=' + val.uuid,
                    }).then((res) => {
                        this.steplist = res
                    })

                    this.rbHttp.sendRequest({ // 反馈意见列表
                        method: 'GET',
                        url: '/v1/alerts/task/getTaskMessageList?uuid=' + val.uuid + '&pageNum=' + this.pageNum + '&pageSize=' + this.pageSize,
                    }).then((res) => {
                        this.feedbackList = res.result
                        this.countNum = res.count
                    })
                },
                immediate: true, // 初始化传值
                deep: true // 深度监听
            }
        },
        created() {
        },
        methods: {
            // 分页
            handleCurrentChange(val) {
                this.rbHttp.sendRequest({ // 反馈意见列表
                    method: 'GET',
                    url: '/v1/alerts/task/getTaskMessageList?uuid=' + (this.uuid || this.taskDetailData.uuid) + '&pageNum=' + val + '&pageSize=' + this.pageSize,
                }).then((res) => {
                    this.feedbackList = res.result
                    this.countNum = res.count
                })
            },
            deleteTask() { // 删除
                this.$confirm('您确认删除此任务？', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.rbHttp.sendRequest({ // 反馈意见列表
                        method: 'DELETE',
                        url: '/v1/alerts/task/deleteTask?uuid=' + this.uuid,
                    }).then(() => {
                        this.$message.success('删除成功')
                        this.$emit('search')
                        this.$emit('closeBox')
                    })
                }).catch(() => {
                    this.$message.info('已取消删除')
                })
            },
            // 编辑
            editTask() {
                this.$emit('editTask', this.taskDetailData)
            },
            // 开始执行
            startTask() {
                this.$emit('startExecution', this.uuid)
            },
            // 结束执行
            endTaskConfirmB() {
                this.$emit('endTaskConfirm', this.taskDetailData)
            },
            // 确认执行结果
            confirmExecutionResults(data) {
                this.$emit('confirmExecutionR', data)
            },
            // 反馈意见提交
            feedbackContentSubmission() {
                if (this.feedbackContent) {
                    this.rbHttp.sendRequest({ // 反馈意见列表
                        method: 'POST',
                        data: { uuid: this.uuid || this.taskDetailData.uuid, content: this.feedbackContent },
                        url: '/v1/alerts/task/editTaskMessage'
                    }).then((res) => {
                        if (res === 'success') {
                            this.$message.success('提交成功')
                            this.rbHttp.sendRequest({ // 反馈意见列表
                                method: 'GET',
                                url: '/v1/alerts/task/getTaskMessageList?uuid=' + (this.uuid || this.taskDetailData.uuid) + '&pageNum=' + this.pageNum + '&pageSize=' + this.pageSize,
                            }).then((res) => {
                                this.feedbackList = res.result
                                this.countNum = res.count
                            })
                        } else {
                            this.$message.error('提交失败')
                        }
                    })
                }
            },
        },
    }
</script>
 
<style lang="scss" scoped>
    .box-title {
        padding: 0px 10px;
        line-height: 37px;
        font-size: 12px;
        font-weight: bold;
        background: rgba(244, 244, 246, 1);
    }

    .box-title-content {
        margin: 10px;
        border: 1px solid rgba(223, 233, 252, 1);
        .box-title-item {
            display: flex;
            justify-content: space-between;
            padding: 0 12px;
            border-bottom: 1px solid rgba(223, 233, 252, 1);
            p:nth-child(1) {
                font-size: 10px;
                color: #53607e;
            }
            p:nth-child(2) {
                font-size: 12px;
                color: #333333;
            }
        }
        .box-title-item-describe {
            border-bottom: 1px solid rgba(223, 233, 252, 1);
            padding: 0 12px;
            p:nth-child(1) {
                font-size: 10px;
                color: #53607e;
            }
            p:nth-child(2) {
                font-size: 12px;
                color: #333333;
            }
        }
    }
    .box-title-content-opinion {
        border: 1px solid rgba(223, 233, 252, 1);
        max-height: 400px;
        overflow-y: auto;
        .feedbackOpinion {
            display: flex;
            justify-content: space-between;
            padding: 5px 10px;
            border-bottom: 1px solid rgba(223, 233, 252, 1);
            line-height: 20px;
            div:nth-child(2),
            div > p:nth-child(1) {
                color: #53607e;
                font-size: 10px;
            }
            div:nth-child(1) {
                color: #333333;
                font-size: 12px;
            }
        }
    }
    .box-title-content-opinion > div:last-child {
        border-bottom: none;
    }
    .feedback-text {
        word-break: break-all;
    }
    .feedback-time {
        width: 80px;
    }
</style>
