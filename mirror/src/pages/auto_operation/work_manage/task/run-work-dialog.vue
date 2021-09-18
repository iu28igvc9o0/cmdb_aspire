<template>
    <div>

        <!-- 执行作业 -->
        <el-dialog width="90%"
                   class="yw-dialog"
                   title="执行作业"
                   @close="closeBox"
                   v-if="runWorkBoxShow"
                   :visible.sync="runWorkBoxShow"
                   :append-to-body="true"
                   :close-on-click-modal="true">
            <div class="heightp100-box">
                <work-preview :pipelineId="pipeline_id"
                              @closeBox="closeBox"
                              @runWorkNow="runWorkNow"></work-preview>
            </div>
        </el-dialog>

        <!-- 任务执行结果 -->
        <el-dialog class="yw-dialog"
                   title="执行结果"
                   v-if="workResultBoxShow"
                   :visible.sync="workResultBoxShow"
                   :close-on-click-modal="true"
                   width="90%">
            <div class="heightp100-box">
                <task-run-result :currentTaskInfo="currentTaskInfo"
                                 :stepBlockList="stepBlockList"
                                 :pipelineInstanceId="curPipelineInstanceId"
                                 @viewScriptRunDetail="viewScriptRunDetail"
                                 @gotoNextStep="gotoNextStep"></task-run-result>
            </div>
        </el-dialog>

        <!-- 步骤节点执行详情 -->
        <el-drawer ref="drawer"
                   :show-close="false"
                   v-if="resultBoxShow"
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
    import workPreview from './work-preview.vue'
    import taskRunResult from '../../components/task-run-result.vue'
    import codeRunResult from '../../components/code-run-result.vue'
    import rbAutoOperationMixin from 'src/services/auto_operation/rb-auto-operation-mixin.js'
    import rbAutoOperationServicesFactory from 'src/services/auto_operation/rb-auto-operation-services.factory.js'
    import rbAutoOperationWorkServicesFactory from 'src/services/auto_operation/rb-auto-operation-work-services.factory.js'

    export default {
        components: {
            workPreview,
            taskRunResult,
            codeRunResult
        },
        data() {
            return {
                // 作业预览数据
                pipeline_id: '',
                // 步骤块列表
                stepBlockList: [],
                // 当前任务详情信息
                currentTaskInfo: {},
                // 执行作业弹框
                runWorkBoxShow: false,
                // 执行结果弹框
                workResultBoxShow: false,
                // 单条脚本执行详情弹框
                resultBoxShow: false,
                // 步骤实例id
                stepInstId: '',
                timer: null,
                curPipelineInstanceId: '',
            }
        },
        mixins: [rbAutoOperationMixin],
        computed: {},
        watch: {
            workResultBoxShow(val) {
                if (!val) {
                    clearTimeout(this.timer)
                }
            }
        },
        mounted() {
            // 接收打开查看作业实例结果弹框指令
            this.onShowRunWorkResult()
            // 接收打开执行弹框指令
            this.onShowRunWork()
            // 接收关闭执行弹框指令
            this.onHideRunWork()
        },
        methods: {
            // 通知eventBus关闭其他页面的弹框
            closeBox() {
                this.$bus.emit('hideRunWork')
            },
            // 接收打开查看作业实例结果弹框指令
            onShowRunWorkResult() {
                this.$bus.on('showRunWorkResult', (pipelineInstanceId) => {
                    this.runWorkNow(pipelineInstanceId)
                })
            },
            // 接收打开执行弹框指令
            onShowRunWork() {
                this.$bus.on('showRunWork', (pipeline_id) => {
                    this.pipeline_id = pipeline_id
                    this.runWorkBoxShow = true
                })
            },
            // 接收关闭执行弹框指令
            onHideRunWork() {
                this.$bus.on('hideRunWork', () => {
                    this.runWorkBoxShow = false
                })
            },
            // 执行结果弹框
            runWorkNow(pipelineInstanceId) {
                this.curPipelineInstanceId = pipelineInstanceId
                // 清除之前的轮询
                let time = +sessionStorage.getItem('timeStorage')
                if (time) {
                    clearTimeout(time)
                }

                this.queryOpsPipelineInstanceById(pipelineInstanceId)
                this.queryStepInstListByPipelineInstId(pipelineInstanceId)
                this.workResultBoxShow = true
                this.runWorkBoxShow = false
            },
            // 查询执行历史作业详情
            queryOpsPipelineInstanceById(pipelineInstanceId) {
                rbAutoOperationWorkServicesFactory
                    .queryOpsPipelineInstanceById(pipelineInstanceId)
                    .then(res => {
                        this.currentTaskInfo = res
                    })
            },
            // 根据ops历史实例查询ops步骤实例列表
            queryStepInstListByPipelineInstId(pipelineInstanceId) {
                rbAutoOperationServicesFactory.queryStepInstListByPipelineInstId(pipelineInstanceId).then(res => {
                    this.stepBlockList = []
                    res.forEach(item => {
                        let status = item.status
                        // 执行状态：  9 执行成功 101 执行失败 102 执行超时
                        // 100 执行中  5 等待执行  6 暂停待确认
                        // 只要有一个节点执行失败/执行超时，则不再执行，不需再轮询
                        // 暂停待确认状态，用户点击【立即执行】，则继续触发轮询
                        if ((status === 100 || status === 5) && status !== 101 && status !== 102) {
                            if (this.timer) {
                                clearTimeout(this.timer)
                            }
                            this.timer = setTimeout(() => {
                                this.queryStepInstListByPipelineInstId(pipelineInstanceId)
                            }, 1500)
                            // 将轮询id保存到sessionStorage
                            sessionStorage.setItem('timeStorage', this.timer)
                        } else {
                            this.queryOpsPipelineInstanceById(pipelineInstanceId)
                        }
                        if (!this.stepBlockList[item.block_ord - 1]) {
                            this.stepBlockList[item.block_ord - 1] = []
                        }
                        this.stepBlockList[item.block_ord - 1].push(item)
                    })
                    this.stepBlockList = this.stepBlockList.filter(item => {
                        return item
                    })
                })
            },
            // 查看脚本执行详情
            viewScriptRunDetail(row) {
                // 弹出执行结果弹框
                this.stepInstId = row.step_instance_id
                this.resultBoxShow = true
            },
            // 完成后暂停，进入下一步
            gotoNextStep(row) {
                rbAutoOperationWorkServicesFactory.manualHandleOpsStepInstance(row.step_instance_id).then(res => {
                    if (res.flag) {
                        this.$message.success('操作成功')
                        this.queryStepInstListByPipelineInstId(this.curPipelineInstanceId)
                    } else {
                        this.$message.error(res.error_tip)
                    }
                })
            },
        }
    }
</script>


<style lang="scss" scoped>
</style>
