<template>
    <el-dialog class="yw-dialog"
               :title="dialogMsg.data.title"
               width="640px"
               top="10vh"
               @close="handleClosed"
               :visible.sync="dialogMsg.dialogVisible">
        <section class="yw-dialog-main middle-size-form">
            <el-form class="yw-form is-required"
                     ref="fileForm"
                     :model="submitInfo"
                     :rules="formRules"
                     label-width="100px">
                <el-form-item label="文件"
                              prop="fileName">
                    <el-input class="middle-size-input"
                              v-model="submitInfo.fileName"
                              placeholder="请选取文件"
                              readonly></el-input>
                    <el-upload class="upload-demo"
                               :show-file-list="false"
                               :http-request="UploadFile">
                        <el-button size="small"
                                   type="primary">选取文件</el-button>
                    </el-upload>
                </el-form-item>
                <el-form-item label="文件别名"
                              prop="fileNameAlias">
                    <el-input class="middle-size-input"
                              v-model="submitInfo.fileNameAlias"
                              placeholder="请输入文件别名"></el-input>
                </el-form-item>
            </el-form>
        </section>
        <section class="btn-wrap">
            <el-button type="primary"
                       @click="save()">保存</el-button>
            <el-button @click="addCancel()">取消</el-button>
        </section>
    </el-dialog>
</template>

<script>
    import rbDocManageServicesFactory from '../../../../../services/doc_manage/rb-doc-manage-services.factory.js'
    export default {
        props: ['dialogMsg'],
        components: {},
        data() {
            return {
                loading: false,
                pageLoading: false,
                submitInfo: {
                    fileName: '',
                    fileType: '',
                    fileObject: '',
                    fileNameAlias: '',
                    fileData: ''
                },
                formRules: {
                    fileName: [
                        {
                            required: true,
                            message: '请上传文件!',
                            trigger: ['blur', 'change']
                        },
                    ],
                    fileNameAlias: [
                        {
                            required: true,
                            message: '请输入文件别名!',
                            trigger: ['blur', 'change']
                        },
                        {
                            min: 2,
                            max: 50,
                            message: '长度在 2 到 50 个字符!',
                            trigger: ['blur', 'change']
                        }
                    ],
                },
            }
        },
        watch: {
        },
        created() {
        },
        mounted() {
            console.info(this.dialogMsg.data)
        },
        methods: {
            // 上传yum源文件
            UploadFile(param) {
                let nameWithSuffix = param.file.name
                this.submitInfo.fileName = param.file.name
                this.submitInfo.fileData = param.file
                if (nameWithSuffix.indexOf('.') != -1) {
                    let names = nameWithSuffix.split('.')
                    // 去掉后缀
                    this.submitInfo.fileNameAlias = names[0]
                }
            },
            addCancel() {
                this.dialogMsg.dialogVisible = false
            },
            handleClosed() {
                this.$emit('refreshList')
                this.dialogMsg.dialogVisible = false
            },
            // 保存文件
            save() {
                this.$refs.fileForm.validate(valid => {
                    if (!valid) {
                        // this.$message("请先完善信息");
                        return
                    }
                    // 补充实体的文件对象
                    this.submitInfo.fileObject = this.dialogMsg.data.fileObject
                    this.submitInfo.fileType = this.dialogMsg.data.fileType
                    let req = this.submitInfo
                    this.saveFile(req, 'saveFile')
                })
            },
            // 提交文件
            saveFile(req, type) {
                this.pageLoading = true
                rbDocManageServicesFactory[type](req).then(res => {
                    if (res.flag === 'success') {
                        this.$message.success('保存成功')
                        this.handleClosed()
                        this.pageLoading = false
                    } else {
                        this.$message.error(res.error_tip)
                        this.pageLoading = false
                    }
                })
            },
        }
    }
</script>

<style lang="scss" scoped>
</style>
