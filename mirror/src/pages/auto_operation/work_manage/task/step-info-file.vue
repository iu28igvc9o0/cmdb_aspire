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
                <el-form-item label="文件类型"
                              prop="file_type"
                              v-if="!isPreview">
                    <el-radio-group v-model="addForm.file_type">
                        <el-radio :label="0"
                                  name="type">普通文件
                        </el-radio>
                        <el-radio :label="1"
                                  name="type">4A账号文件
                        </el-radio>
                    </el-radio-group>
                </el-form-item>
                <el-form-item label="选择文件"
                              prop="fileList"
                              v-if="!isPreview">
                    <el-upload class="upload-demo"
                               :http-request="UploadFile"
                               action=""
                               :show-file-list="false">
                        <el-button size="small"
                                   type="primary">使用本地文件</el-button>
                    </el-upload>
                </el-form-item>
                <el-form-item label="文件列表">
                    <el-table :data="addForm.fileList"
                              class="yw-el-table"
                              stripe
                              tooltip-effect="dark"
                              border
                              v-loading="loading">
                        <el-table-column sortable
                                         prop="file_path"
                                         label="文件列表"></el-table-column>
                        <el-table-column sortable
                                         prop="address"
                                         label="服务器地址"></el-table-column>
                        <el-table-column sortable
                                         prop="notifyType"
                                         label="服务器账户"
                                         width="120"></el-table-column>
                        <el-table-column label="操作"
                                         width="80"
                                         v-if="!isPreview">
                            <template slot-scope="scope">
                                <div class="yw-table-operator">
                                    <el-button type="text"
                                               title="删除"
                                               icon="el-icon-delete"
                                               @click="clearFile(scope.$index)"></el-button>
                                </div>
                            </template>
                        </el-table-column>
                    </el-table>
                </el-form-item>

                <!-- 选择服务器 -->
                <el-form-item label="目标机器"
                              prop="targetMachines">
                    <!-- 选择服务器组件 -->
                    <select-servers :dataSelected="addForm.targetMachines"
                                    :serversSelectShow="serversSelectShow"
                                    v-if="serversSelectShow"
                                    @setSelectedKey="setSelectedKey"
                                    @setSelectedService="setSelectedService"></select-servers>
                </el-form-item>
            </el-form>
        </div>
    </div>
</template>

<script>
    import selectServers from '../../components/select-servers.vue'
    import rbAutoOperationMixin from 'src/services/auto_operation/rb-auto-operation-mixin.js'
    import rbAutoOperationWorkMixin from 'src/services/auto_operation/rb-auto-operation-work-mixin.js'
    import rbAutoOperationWorkServicesFactory from 'src/services/auto_operation/rb-auto-operation-work-services.factory.js'

    export default {
        components: {
            selectServers
        },
        props: ['stepMoreData', 'rowData', 'isPreview', 'isFromWorkEdit'],
        data() {
            return {
                selectedServiceList: [],
                addForm: {
                    codeTime: '600',
                    // 目标服务器
                    targetMachines: [],
                    fileList: [],
                    file_type: 0,
                },
                addFormRules: {
                    fileList: [
                        {
                            required: true,
                            trigger: 'change',
                            validator: (rule, value, callback) => {
                                if (!value.length) {
                                    callback(new Error('请上传文件!'))
                                } else {
                                    callback()
                                }
                            }
                        }
                    ],
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
                    ]
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
            'addForm.fileList'(val) {
                this.$emit('updateCurrentRow', this.rowData, 'file_source', val)
            },
            // 更新文件类型到当前行
            'addForm.file_type'(val) {
                this.$emit('updateCurrentRow', this.rowData, 'file_type', val)
            },
            'stepMoreData.serverList': {
                deep: true,
                handler(val) {
                    this.serverList = val
                    this.loading = false
                }
            }
        },
        mounted() {
            this.initPageInfo()
        },
        methods: {
            setSelectedService(data) {
                this.$emit('setSelectedService', data)
                // this.selectedServiceList = []
                // this.selectedServiceList = data
            },

            // 上传本地文件
            UploadFile(param) {
                this.$message('请稍候')
                const formData = new FormData()
                formData.append('file', param.file)
                let obj = {
                    file_type: 'upload',
                    address: `本地文件(${Math.ceil(param.file.size / 1024)}k)`,
                }
                obj.name = param.file.name
                rbAutoOperationWorkServicesFactory
                    .uploadFile4Transfer(formData)
                    .then(res => {
                        if (res.flag) {
                            this.$message('上传成功')
                            obj.file_path = res.biz_data
                            this.addForm.fileList.push(obj)
                        } else {
                            this.$message.error(res.error_tip)
                        }
                    })
            },
            // 初始化页面信息
            initPageInfo() {
                this.serverList = this.stepMoreData.serverList
                this.target_hosts = this.rowData.target_hosts || [] // 同步已选中的目标服务器
                this.addForm.targetMachines = this.rowData.target_host_list || [] // 同步已选中的目标服务器
                // 从漏掉修复进入，获取修复的主机
                if (this.serverListFromBugFix && this.serverListFromBugFix.length) {
                    this.addForm.targetMachines = this.serverListFromBugFix
                }
                this.addForm.fileList = this.rowData.file_source || []  // 同步已上传的文件路径
                this.addForm.file_type = this.rowData.file_type || 0  // 同步已上传的文件类型
                this.serversSelectShow = true
            },
            // 删除文件
            clearFile(index) {
                this.addForm.fileList.splice(index, 1)
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
                        this.$emit('updateFileEditMoreValid')
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
