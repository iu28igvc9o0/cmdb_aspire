<template>
    <div class="components-container">
        <div class="run-code-box mtop10">
            <el-form class="yw-form is-required"
                     ref="addForm"
                     :model="addForm"
                     :rules="addFormRules"
                     label-width="120px">
                <el-form-item label="目标机器"
                              prop="selectedData">
                    <!-- 选择服务器 -->
                    <select-servers :dataSelected="addForm.selectedData"
                                    :serversSelectShow="serversSelectShow"
                                    v-if="serversSelectShow"
                                    @setSelectedKey="setSelectedKey"></select-servers>
                </el-form-item>

            </el-form>
        </div>
    </div>
</template>

<script>
    import selectServers from '../../components/select-servers.vue'
    import rbAutoOperationMixin from 'src/services/auto_operation/rb-auto-operation-mixin.js'
    import rbAutoOperationWorkMixin from 'src/services/auto_operation/rb-auto-operation-work-mixin.js'
    export default {
        components: {
            selectServers,
        },
        props: ['rowData'],
        data() {
            return {
                addForm: {
                    // 目标服务器
                    selectedData: []
                },
                addFormRules: {
                    selectedData: [
                        {
                            required: false,
                            trigger: 'change',
                            validator: (rule, value, callback) => {
                                if (!value.length) {
                                    callback(new Error('请选择目标机器!'))
                                } else {
                                    callback()
                                }
                            }
                        }
                    ]
                },
            }
        },
        mixins: [rbAutoOperationMixin, rbAutoOperationWorkMixin],
        watch: {
            // 更新目标机器到当前行
            'addForm.selectedData'(newVal) {
                this.$emit('updateCurrentRow', this.rowData, 'target_host', newVal)
            },
        },
        mounted() {
            this.initPageInfo()
        },
        methods: {
            setSelectedKey(data) {
                this.addForm.selectedData = data
            },
            // 初始化页面信息
            initPageInfo() {
                this.addForm.selectedData = this.multipleSelection = this.rowData.rule_range.target_host
                this.serversSelectShow = true
            },
            // 校验表单
            validForm() {
                const addFormValid = new Promise((resolve, reject) => {
                    this.$refs.addForm.validate(valid => {
                        if (valid) {
                            resolve()
                        } else {
                            reject()
                        }
                    })
                })
                const codeFormValid = new Promise((resolve, reject) => {
                    this.$refs.codeEditor.$refs.codeForm.validate(valid => {
                        if (valid) {
                            resolve()
                        } else {
                            reject()
                        }
                    })
                })
                Promise.all([addFormValid, codeFormValid])
                    .then(() => {
                        this.$emit('updateScriptEditMoreValid')
                    })
                    .catch(() => {
                        this.$message('请先完善信息')
                    })
            },
        }
    }
</script>


<style lang="scss" scoped>
    .run-code-box {
        width: 70%;
        margin: 0 auto;
    }
    .account-box {
        padding: 15px;
        border: 1px solid $color-border;
        background: $color-bg;
        overflow: hidden;
    }
    .mleft10 {
        margin-left: 20px;
    }
    .red {
        color: $color-tip-red;
    }
    .split-box {
        padding: 10px;
    }
    .yw-dialog-main.no-scroll {
        overflow: hidden;
    }
</style>
