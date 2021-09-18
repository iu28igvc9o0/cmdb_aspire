<template>
    <div>
        <div class="mtop10 run-code-box">
            <el-form class="yw-form is-required"
                     ref="addForm"
                     @submit.native.prevent
                     :model="addForm"
                     :rules="addFormRules"
                     label-width="100px">

                <!-- 选择服务器 -->
                <el-form-item label="目标机器"
                              prop="targetMachines">
                    <!-- 选择服务器组件 -->
                    <select-servers :dataSelected="addForm.targetMachines"
                                    :targetObjectList="target_exec_object"
                                    :isAutoRepair="isAutoRepair"
                                    :serversSelectShow="serversSelectShow"
                                    :rowData="rowData"
                                    v-if="serversSelectShow"
                                    @updateCustomTargetHosts="updateCustomTargetHosts"
                                    @updateCustomTargetHostsValue="updateCustomTargetHostsValue"
                                    @setSelectedKey="setSelectedKey"
                                    @setSelectedService="setSelectedService"></select-servers>
                </el-form-item>

                <code-editor ref="codeEditor"
                             :currentCodeInfo="currentCodeInfo"
                             :codeCloneList="allCodeList"
                             :isFromWorkManage="true"
                             :isAddCode="true"
                             :isPreview="isPreview"
                             :paramType="paramType"
                             @passCodeContent="passCodeContent"></code-editor>

                <el-form-item label="脚本时间(s):"
                              prop="codeTime">
                    <el-input v-model="addForm.codeTime"
                              :readonly="isPreview"
                              clearable></el-input>
                </el-form-item>
            </el-form>
        </div>
    </div>
</template>

<script>
    import codeEditor from '../../components/code-editor.vue'
    import selectServers from '../../components/select-servers.vue'
    import rbAutoOperationMixin from 'src/services/auto_operation/rb-auto-operation-mixin.js'
    import rbAutoOperationWorkMixin from 'src/services/auto_operation/rb-auto-operation-work-mixin.js'

    export default {
        components: {
            codeEditor,
            selectServers
        },
        props: ['stepMoreData', 'rowData', 'isPreview', 'isAutoRepair'],
        data() {
            return {
                replace_attrs: [],
                target_exec_object: [],
                replace_attrs_obj: {},
                serverListFromBugFix: [],
                paramType: ''
            }
        },
        mixins: [rbAutoOperationMixin, rbAutoOperationWorkMixin],
        watch: {
            // 更新目标机器模版参数key到当前行
            'addForm.custom_target_hosts'(newVal) {
                this.handleAttrs(newVal, 'target_hosts', 'check')
            },
            // 更新目标机器模版参数value到当前行
            'addForm.custom_target_hosts_value'(newVal) {
                this.handleAttrs(newVal, 'target_hosts')
            },
            // 更新目标机器到当前行
            // 'addForm.targetMachines'(newVal) {
            //     this.$emit('updateCurrentRow', this.rowData, 'target_hosts', newVal)
            // },
            // 更新超时时间到当前行
            'addForm.codeTime'(newVal) {
                this.$emit('updateCurrentRow', this.rowData, 'ops_timeout', newVal)
            },
            // 更新目标机器到当前行
            'addForm.targetMachines': {
                handler(newVal) {
                    this.$emit('updateCurrentRow', this.rowData, 'target_hosts', newVal)
                },
                deep: true,
                immediate: false
            },
            // 更新脚本信息到当前行
            codeInfo: {
                handler(newVal) {
                    this.$emit('updateCurrentRow', this.rowData, 'embed_script', newVal)
                },
                deep: true
            },
        },
        created() {
            if (this.$route.query) {
                console.log('route', this.$route.query)
                if (this.$route.query.currentTitle === '新建作业') {
                    this.paramType = 'add'
                } else {
                    this.paramType = 'edit'
                }
            }
        },
        mounted() {
            this.initPageInfo()
        },
        methods: {
            updateCustomTargetHosts(val) {
                this.addForm.custom_target_hosts = val
            },
            updateCustomTargetHostsValue(val) {
                this.addForm.custom_target_hosts_value = val
            },
            handleAttrs(newVal, key, type) {
                let obj = {}
                if ((newVal && type === 'check') || !type) {
                    obj.replace_attr = key
                    obj.replace_type = this.addForm[`custom_${key}_value`]
                } else if (type === 'check') {
                    obj = {}
                }

                this.replace_attrs_obj[key] = obj
                this.replace_attrs = Object.values(this.replace_attrs_obj)
                this.$emit('updateCurrentRow', this.rowData, 'replace_attrs', this.replace_attrs)

                this.addFormRules.targetMachines[0].required = !newVal
                this.$refs.addForm.validateField('targetMachines')
            },
            // 初始化页面信息
            initPageInfo() {
                console.log('this.rowData', this.rowData)
                // 可编辑状态
                if (!this.isPreview) {
                    this.serverList = this.stepMoreData.serverList
                    this.allCodeList = this.stepMoreData.allCodeList
                }
                this.target_exec_object = this.rowData.target_exec_object || []
                this.target_hosts = this.rowData.target_hosts || [] // 同步已选中的目标服务器
                this.addForm.targetMachines = this.rowData.target_host_list || [] // 同步已选中的目标服务器
                // 从漏掉修复进入，获取修复的主机
                if (this.serverListFromBugFix.length) {
                    this.addForm.targetMachines = this.serverListFromBugFix
                }
                this.addForm.codeTime = this.rowData.ops_timeout || '30'  // 同步已设置的超时时间
                this.rowData.embed_script && this.rowData.embed_script.content_type && (this.currentCodeInfo.languageType = this.rowData.embed_script.content_type)  // 同步已设置的脚本类型
                this.rowData.embed_script && (this.currentCodeInfo.codeContent = this.rowData.embed_script.script_content)  // 同步已设置的脚本内容
                // 合并其他字段
                this.currentCodeInfo = this.$utils.deepClone(Object.assign(this.currentCodeInfo, this.rowData.embed_script))
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
            close(type) {
                if (type) {
                    this.serversCmdbBoxShow = false
                }
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
