<template>
    <div class="code-mirror-box">
        <el-form class="yw-form is-required"
                 ref="timerForm"
                 :model="codeInfo"
                 :rules="codeFormRules"
                 label-width="100px">
            <el-form-item label="定时任务"
                          prop="name"
                          v-if="codeNameShow">
                <el-input v-model="codeInfo.name"
                          placeholder="请输入定时任务名称"
                          clearable></el-input>
            </el-form-item>
            <el-form-item label="作业名称"
                          prop="pipeline_id">
                <el-select v-model="codeInfo.pipeline_id"
                           placeholder="请选择"
                           class="list-sel"
                           :disabled="isFromWorkList">
                    <el-option v-for="item in pipelineList"
                               :key="item.pipeline_id"
                               :label="item.pipeline_name"
                               :value="item.pipeline_id">
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="定时规则">
                <cron v-if="showCronBox"
                      v-model="codeInfo.cron_expression"></cron>
                <span style="color: #E6A23C; font-size: 12px;">cron从左到右（用空格隔开）：秒 分 小时 月份中的日期 月份 星期中的日期 年份</span>
            </el-form-item>
            <el-form-item label="Cron"
                          prop="cron_expression">
                <el-input v-model="codeInfo.cron_expression"
                          auto-complete="off"
                          class="cron-input">
                    <el-button slot="append"
                               v-if="!showCronBox"
                               icon="el-icon-arrow-up"
                               @click="showCronBox = true"
                               title="打开图形配置"
                               style="border: 0px;"></el-button>
                    <el-button slot="append"
                               v-else
                               icon="el-icon-arrow-down"
                               @click="showCronBox = false"
                               title="关闭图形配置"
                               style="border: 0px;"></el-button>
                </el-input>
            </el-form-item>
        </el-form>
    </div>
</template>

<script>
    import cron from './components/cron'
    import { cronValidate } from 'src/assets/js/utility/cron.js'
    import rbAutoOperationWorkServicesFactory from 'src/services/auto_operation/rb-auto-operation-work-services.factory.js'
    export default {
        name: 'TimedJobEditor',
        components: {
            cron
        },
        props: ['currentCodeInfo', 'isFromWorkList'],
        watch: {
            currentCodeInfo(val) {
                this.codeInfo = val
            }
        },
        data() {
            var cronVal = (rule, value, callback) => {
                let result = cronValidate(value)
                if (!result) {
                    callback(new Error('cron表达式错误'))
                } else {
                    callback()
                }
            }
            return {
                codeFormRules: {
                    name: [
                        {
                            required: true,
                            message: '请输入定时任务名称!',
                            trigger: 'blur'
                        },
                        {
                            min: 4,
                            max: 100,
                            message: '长度在 4 到 100 个字符!',
                            trigger: 'blur'
                        }
                    ],
                    pipeline_id: [
                        {
                            required: true,
                            message: '请选择作业!',
                            trigger: 'blur'
                        }
                    ],
                    cron_expression: [
                        {
                            required: true,
                            message: '请输入正确的定时规则!',
                            trigger: 'blur'
                        },
                        { validator: cronVal, trigger: 'blur' }
                    ]
                },
                showCronBox: true,
                codeNameShow: true,
                pipelineList: [],
                codeInfo: this.currentCodeInfo
            }
        },
        mounted() {
            this.searchPipelineList()
        },
        methods: {
            searchPipelineList() {
                let param = { page_no: 1, page_size: 1000 }
                rbAutoOperationWorkServicesFactory.queryOpsPipelineList(param).then(res => {
                    // this.total = res.totalCount;
                    this.pipelineList = res.dataList
                })
            }
        }

    }
</script>

<style lang="scss" scoped>
    .code-mirror-box .el-textarea {
        width: 100% !important;
        max-height: 300px;
    }
    /*.cron-input {*/
    /*.el-button {*/
    /**/
    /*}*/
    /*}*/
</style>