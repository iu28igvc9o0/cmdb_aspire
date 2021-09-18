
<template>
    <!-- 创建、编辑变更计划 -->
    <div>
        <!-- 任务详情页面=={{taskDetailData}} 任务信息数据 -->
        <el-form>

            <el-form-item>
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
                    <div class="box-title-item">
                        <p>计划执行结束时间</p>
                        <p>{{taskDetailData.taskEndTime}}</p>
                    </div>
                    <div class="box-title-item">
                        <p>工单号</p>
                        <p>{{taskDetailData.orderId}}</p>
                    </div>
                    <div class="box-title-item"
                         :class="(taskSetResult === '1')?borderBottom:''">
                        <el-form-item label="执行结果"
                                      prop="taskSetResult">
                            <el-radio-group v-model="taskSetResult">
                                <el-radio label="1"
                                          name="type">成功</el-radio>
                                <el-radio label="2"
                                          name="type">失败</el-radio>
                            </el-radio-group>
                        </el-form-item>
                    </div>

                    <div v-show="taskSetResult === '2'"
                         class="box-title-item-describe"
                         style="border-bottom:none">

                        <p>失败原因</p>
                        <p style="margin-bottom:10px;">
                            <el-input clearable="true"
                                      type="textarea"
                                      placeholder="请输入内容"
                                      v-model="message"
                                      maxlength="100"
                                      show-word-limit>
                            </el-input>
                        </p>
                    </div>
                </div>
            </el-form-item>

        </el-form>
        <section v-if="taskDetailData.taskStatus ==='4'"
                 class="btn-wrap t-right"
                 style="margin-bottom:10px">
            <el-button @click="addCancel">取消</el-button>
            <el-button type="primary"
                       @click="endTaskBsuu(taskDetailData)">确认</el-button>
        </section>
        <section v-else-if="taskDetailData.taskStatus ==='2'"
                 class="btn-wrap t-right"
                 style="margin-bottom:10px">
            <el-button @click="addCancel">取消</el-button>
            <el-button type="primary"
                       @click="endTaskB">结束执行</el-button>
        </section>
    </div>
</template>
 
<script>
    import rbPlanServicesFactory from 'src/services/door/rb-plan-services.factory.js'
    export default {
        name: 'DoorPlanListTaskDetail',
        components: {
        },
        props: {
            taskDetailData: {
                type: Object,
                default: {}
            },
            endTask: {
                type: Boolean,
                default: {}
            },
        },
        data() {
            return {
                message: '',
                steplist: [], // 步奏条列表
                feedbackList: [], // 反馈意见列表
                editEask: true,
                endTask: true,
                taskDetailData: {},
                startEask: {
                    startEask: true,
                    startEasklist: {}
                },
                taskType: '',
                uuid: '', // uuid

                taskSetResult: '1', // 执行结果
                // endExecutionSuccessful: true // 结束执行成功
                endExecutionSuccessful: {
                    end: true, // 结束执行成功
                }


            }
        },
        computed: {
        },
        watch: {
            endTask: {
                handler(val) {
                    this.endTask = val
                }
            },
            taskDetailData: {
                handler(val) {
                    this.uuid = val.uuid
                    this.message = ''
                }
            }
        },
        created() {
        },
        methods: {
            // 取消
            addCancel() {
                this.$emit('closeBox')
            },
            // 结束执行
            endTaskB() {
                let taskData = this.$utils.deepClone(this.taskDetailData)
                if (this.taskSetResult === '1' || (this.taskSetResult === '2' && this.message)) {
                    taskData.taskStatus = '3'
                    taskData.content = this.message
                    this.rbHttp.sendRequest({ // 反馈意见列表
                        method: 'POST',
                        data: { uuid: this.uuid, massage: this.message, taskResult: this.taskSetResult },
                        url: '/v1/alerts/task/stopTask'
                    }).then((res) => {
                        if (res === 'success') {
                            this.$message.success('结束执行成功')
                            this.$emit('search')
                            this.$emit('showTaskDetail', taskData)
                        } else {
                            this.$message.error('结束执行失败失败')
                        }
                    })
                } else {
                    this.$message.error('失败原因！不能为空！')
                }
            },
            // 确认执行状态，把taskResult设置为'3'
            endTaskBsuu(data) {
                let req = {
                    uuid: data.uuid,
                    taskResult: this.taskSetResult,
                    message: this.message
                }
                let taskData = this.$utils.deepClone(data)
                taskData.taskResult = '3'
                if (this.taskSetResult === '1' || (this.taskSetResult === '2' && this.message)) {
                    rbPlanServicesFactory.stopTask(req)
                        .then(() => {
                            this.$message.success('确认成功')
                            this.$emit('search')
                            this.$emit('showTaskDetail', taskData)
                        })
                } else {
                    this.$message.error('失败原因不能为空！')
                }
            }
        },
    }
</script>
 
<style  lang="scss" scoped>
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
        .feedbackOpinion {
            display: flex;
            justify-content: space-between;
            padding: 0 12px;
            border-bottom: 1px solid rgba(223, 233, 252, 1);
            line-height: 25px;
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
    .borderBottom {
        border-bottom: none;
    }
</style>
