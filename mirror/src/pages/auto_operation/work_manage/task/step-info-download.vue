<template>
    <div :class="{'components-container' : isFromWorkEdit}">
        <div class="mtop10"
             :class="{'run-code-box' : isFromWorkEdit}">
            <el-form class="yw-form is-required"
                     ref="addForm"
                     @submit.native.prevent
                     :model="addForm"
                     :rules="addFormRules"
                     label-width="100px">
                <el-form-item label="目标路径"
                              prop="filePathList">
                    <el-tag :key="tag"
                            v-for="tag in addForm.filePathList"
                            :closable="!isPreview"
                            :disable-transitions="false"
                            @click="handleTagClick(tag)"
                            @close="handleClose(tag)">
                        {{tag}}
                    </el-tag>
                    <el-input class="input-new-tag"
                              v-show="inputVisible"
                              v-model="inputValue"
                              ref="saveTagInput"
                              size="small"
                              @keyup.enter.native="handleInputConfirm"
                              @blur="handleInputConfirm">
                    </el-input>
                    <el-button v-show="!inputVisible && !isPreview"
                               class="button-new-tag"
                               size="small"
                               @click="showInput">+ 增加目标路径</el-button>
                </el-form-item>

                <!-- 选择服务器 -->
                <el-form-item label="目标机器"
                              prop="targetMachines">
                    <!-- 选择服务器组件 -->
                    <select-servers :dataSelected="addForm.targetMachines"
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
            selectServers
        },
        props: ['stepMoreData', 'rowData', 'isPreview', 'isFromWorkEdit'],
        data() {
            return {
                addForm: {
                    codeTime: '600',
                    // 目标服务器
                    targetMachines: [],
                    filePathList: [],
                },
                inputVisible: false,
                inputValue: '',
                addFormRules: {
                    targetMachines: [
                        {
                            required: true,
                            trigger: 'change',
                            validator: (rule, value, callback) => {
                                if (!value.length) {
                                    callback(new Error('请选择目标机器!'))
                                } else {
                                    callback()
                                }
                            }
                        }
                    ],
                    filePathList: [
                        {
                            required: true,
                            trigger: 'change',
                            validator: (rule, value, callback) => {
                                if (!value.length) {
                                    callback(new Error('请填写目标路径!'))
                                } else {
                                    callback()
                                }
                            }
                        }
                    ],
                },

            }
        },
        mixins: [rbAutoOperationMixin, rbAutoOperationWorkMixin],
        watch: {
            // 更新目标机器到当前行
            'addForm.targetMachines'(val) {
                if (val) {
                    this.$emit('updateCurrentRow', this.rowData, 'target_hosts', val)
                }
            },
            // 更新文件列表到当前行
            'addForm.filePathList'(val) {
                this.$emit('updateCurrentRow', this.rowData, 'file_store_source', val)
            },
            'stepMoreData.serverList': {
                deep: true,
                handler(val) {
                    this.serverList = val
                }
            }
        },
        mounted() {
            this.initPageInfo()
        },
        methods: {
            handleClose(tag) {
                this.addForm.filePathList.splice(this.addForm.filePathList.indexOf(tag), 1)
            },
            handleTagClick(tag) {
                this.inputValue = tag
                this.handleClose(tag)
                this.showInput()
            },
            showInput() {
                this.inputVisible = true
                this.$nextTick(() => {
                    this.$refs.saveTagInput.$refs.input.focus()
                })
            },
            handleInputConfirm() {
                let inputValue = this.inputValue
                if (inputValue) {
                    this.addForm.filePathList.push(inputValue)
                }
                this.inputVisible = false
                this.inputValue = ''
            },
            // 初始化页面信息
            initPageInfo() {
                this.serverList = this.stepMoreData.serverList
                this.target_hosts = this.rowData.target_hosts || [] // 同步已选中的目标服务器
                this.addForm.targetMachines = this.rowData.target_host_list || [] // 同步已选中的目标服务器
                this.addForm.filePathList = this.rowData.file_store_source || []  // 同步已上传的文件路径
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
                let codeFormValid
                if (this.isFromWorkEdit) {
                    codeFormValid = new Promise((resolve, reject) => {
                        this.$refs.codeEditor.$refs.codeForm.validate(valid => {
                            if (valid) {
                                resolve()
                            } else {
                                reject()
                            }
                        })
                    })
                }
                Promise.all([addFormValid, codeFormValid])
                    .then(() => {
                        this.$emit('updateDownloadEditMoreValid')
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
    .el-tag {
        display: block;
        cursor: pointer;
        height: 24px;
        line-height: 24px;
        + .el-tag {
            margin-top: 7px;
        }
    }
    .button-new-tag {
        margin-left: 0;
        padding-top: 0;
        padding-bottom: 0;
    }
    .input-new-tag {
        width: 90px;
        vertical-align: bottom;
    }
</style>
