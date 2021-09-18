
<template>
    <!-- 创建、编辑变更计划 -->
    <div class="components-container">
        <el-form class="yw-form"
                 :model="addForm"
                 :rules="addFormRules"
                 ref="addForm"
                 :inline="true"
                 label-width="160px">
            <el-form-item label="任务标题"
                          prop="taskName">
                <el-input v-model="addForm.taskName"
                          placeholder="请输入任务标题"
                          clearable></el-input>
            </el-form-item>
            <el-form-item label="操作类型"
                          prop="taskType">
                <el-radio-group v-model="addForm.taskType">
                    <el-radio label="1"
                              name="type">割接</el-radio>
                    <el-radio label="2"
                              name="type">变更</el-radio>
                </el-radio-group>
            </el-form-item>
            <el-form-item label="任务描述"
                          prop="taskDescription">
                <el-input type="textarea"
                          class="wp100"
                          rows="4"
                          v-model="addForm.taskDescription"
                          placeholder="请输入任务描述"
                          clearable></el-input>
            </el-form-item>
            <el-form-item label="资源池"
                          prop="idcType">
                <el-select v-model="addForm.idcType"
                           class="wp100"
                           placeholder="请选择"
                           filterable
                           clearable>
                    <el-option v-for="val in idcTypeList"
                               :key="val.id"
                               :label="val.name"
                               :value="val.name"></el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="计划执行开始时间"
                          prop="taskStartTime">
                <el-date-picker v-model="addForm.taskStartTime"
                                :disabled="boxTitle === '开始执行' ? true : false"
                                value-format="yyyy-MM-dd HH:mm:ss"
                                type="datetime"
                                placeholder="请选择"></el-date-picker>
            </el-form-item>
            <el-form-item label="计划执行结束时间"
                          prop="taskEndTime">
                <el-date-picker v-model="addForm.taskEndTime"
                                value-format="yyyy-MM-dd HH:mm:ss"
                                type="datetime"
                                placeholder="请选择"></el-date-picker>
            </el-form-item>

            <el-form-item v-if="boxTitle ==='开始执行'"
                          label="工单号"
                          prop="orderId">
                <el-input v-model="addForm.orderId"
                          placeholder="请输入工单号"
                          clearable></el-input>
            </el-form-item>
        </el-form>

        <section v-if="boxTitle === '开始执行'"
                 class="btn-wrap mtop20 t-right">
            <el-button @click="addCancel">取消</el-button>
            <el-button type="primary"
                       @click="startTaskSubmit">开始执行</el-button>
        </section>
        <section v-else
                 class="btn-wrap mtop20 t-right">
            <el-button @click="addCancel">取消</el-button>
            <el-button type="primary"
                       @click="submitTask">提交</el-button>
        </section>

    </div>
</template>
 
<script>
    import moment from 'moment'
    import rbPlanServicesFactory from 'src/services/door/rb-plan-services.factory.js'
    export default {
        name: 'DoorPlanListTaskEdit',
        components: {
        },
        props: {
            boxTitle: {
                type: String,
                default: ''
            },
            startTime: {
                type: String,
                default: ''
            },
            taskDetailData: {
                type: Object,
                default: {}
            },
        },
        data() {
            return {
                page: 0,
                startTask: false, // 开始执行会会多出一个工单号
                addForm: {
                    taskName: '',
                    taskType: '',
                    taskDescription: '',
                    idcType: '',
                    taskStartTime: '',
                    taskEndTime: '',
                    orderId: '', // 工单号
                },
                // 信息校验规则
                addFormRules: {
                    taskName: [
                        {
                            required: true,
                            message: '请先输入任务标题!',
                            trigger: ['blur', 'change']
                        },
                        {
                            min: 2,
                            max: 100,
                            message: '长度在 2 到 100 个字符!',
                            trigger: ['blur', 'change']
                        }
                    ],
                    taskType: [
                        {
                            required: true,
                            message: '请选择操作类型!',
                            trigger: ['blur', 'change']
                        },
                    ],
                    taskDescription: [
                        {
                            required: true,
                            message: '请先输入任务描述!',
                            trigger: ['blur', 'change']
                        },
                        {
                            min: 2,
                            max: 100,
                            message: '长度在 2 到 100 个字符!',
                            trigger: ['blur', 'change']
                        }
                    ],
                    idcType: [
                        {
                            required: true,
                            message: '请选择资源池!',
                            trigger: ['blur', 'change']
                        },
                    ],
                    taskStartTime: [
                        {
                            required: true,
                            message: '请选择计划执行开始时间!',
                            trigger: ['blur', 'change']
                        },
                    ],
                    taskEndTime: [
                        {
                            required: true,
                            message: '请选择计划执行结束时间!',
                            trigger: ['blur', 'change']
                        },
                    ],
                },
                idcTypeList: [],
                isEditTask: false
            }
        },
        watch: {
            taskDetailData: {
                handler(val) {
                    if (this.boxTitle === '创建任务') {
                        this.addForm.taskStartTime = this.startTime
                    } else {
                        this.addForm = this.$utils.deepClone(val)
                    }
                },
                immediate: true, // 初始化传值
                deep: true // 深度监听
            },
        },
        computed: {
        },
        created() {
            this.getConfigDictByType()
        },
        methods: {
            // 资源池
            getConfigDictByType() {
                let req = {
                    type: 'idcType'
                }
                rbPlanServicesFactory
                    .getConfigDictByType(req)
                    .then(res => {
                        this.idcTypeList = res
                    })
            },
            // 创建、编辑任务
            submitTask() {
                let reqType = 'addTask', uuid = ''
                if (this.boxTitle === '编辑任务') {
                    reqType = 'updateTask'
                }
                this.$refs.addForm.validate(valid => {
                    if (!valid) {
                        this.$message.warning('请先完善信息')
                        return
                    }
                    if (moment(this.addForm.taskStartTime) >= moment(this.addForm.taskEndTime)) {
                        this.$message.warning('结束时间须大于开始时间')
                        return
                    }
                    rbPlanServicesFactory[reqType](this.addForm)
                        .then(res => {
                            uuid = res
                            this.$message.success('操作成功')
                            this.$emit('search')
                            this.isEditTask = true
                            if (this.boxTitle === '编辑任务') {
                                uuid = this.taskDetailData.uuid
                            }
                            this.getTaskDetail(uuid)
                        })
                })
            },
            // 编辑任务，获取任务信息
            getTaskDetail(uuid) {
                rbPlanServicesFactory
                    .getTaskDetail({ uuid: uuid })
                    .then(res => {
                        this.addForm = res
                        if (this.isEditTask) {
                            this.$emit('showTaskDetail', res)
                        }
                    })
            },
            addCancel() {
                this.$emit('showTaskDetail', this.addForm)
            },
            // 开始执行
            startTaskSubmit() {
                this.$refs.addForm.validate(valid => {
                    if (this.addForm.orderId) {
                        if (!valid) {
                            this.$message.warning('请先完善信息')
                            return
                        } else {
                            this.rbHttp.sendRequest({ // 反馈意见列表
                                method: 'POST',
                                data: this.addForm,
                                url: '/v1/alerts/task/startTask'
                            }).then(() => {
                                this.addForm.taskStatus = '2'
                                this.$emit('startTask', this.addForm)
                            })
                        }
                    } else {
                        this.$message.warning('工单号不能为空')

                    }

                })
            }
        },
    }
</script>
 
<style  lang="scss" scoped>
</style>
