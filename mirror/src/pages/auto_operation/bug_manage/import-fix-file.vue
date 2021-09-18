<template>
    <el-form class="yw-form is-required"
             ref="yumForm"
             :model="addForm"
             label-width="100px">
        <el-form-item>
            还没有Excel模版?请
            <el-button type="text"
                       @click="downLoadTemp">下载模版</el-button>
        </el-form-item>
        <el-form-item label="文件名称"
                      prop="filename">
            <el-input class="middle-size-input"
                      v-model="filename"
                      placeholder="请上传修复手册"
                      readonly></el-input>
            <el-upload class="upload-demo"
                       action=""
                       :show-file-list="false"
                       :http-request="UploadFile">
                <el-button size="small"
                           type="primary">上传</el-button>
            </el-upload>
        </el-form-item>
    </el-form>
</template>

<script>
    import bugManageService from 'src/services/auto_operation/rb-auto-operation-bug-manage-services.js'
    import { createDownloadFile, validFile } from 'src/utils/utils.js'

    export default {
        name: 'AutoOperationCodeStatusList',
        components: {},
        data() {
            return {
                filename: '',
                poolList: [],
                addForm: {
                    poolName: '',
                },
            }
        },
        methods: {
            // 下载漏洞修复手册模板
            downLoadTemp() {
                this.$message('请稍候')
                bugManageService.downloadVulnerabilityTemplate(this.formSearch).then(res => {
                    if (res) {
                        this.$message.success('下载成功')
                        createDownloadFile(res, '漏洞修复手册模版.xlsx')
                    }
                })
            },
            UploadFile(param) {
                this.filename = param.file.name
                const formData = new FormData()
                if (!validFile(this, param.file, 'xls')) {
                    return
                }
                this.$message('请稍候')
                formData.append('file', param.file)
                bugManageService
                    .importVulnerabilityFixedFile(formData)
                    .then(res => {
                        if (res.includes('成功')) {
                            this.$message.success(res)
                            this.$emit('uploadSuccess')
                        } else {
                            this.$message.error(res)
                        }
                    })
                    .catch(err => {
                        this.$message.error(err.data)
                    })
            },
        }
    }
</script>
