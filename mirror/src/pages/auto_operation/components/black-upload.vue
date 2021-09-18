<!--  -->
<template>
    <div>
        <el-upload action=""
                   class="upload-demo"
                   :file-list="fileList"
                   :http-request="UploadFile"
                   :on-remove="handleRemove">
            <el-button size="small"
                       type="primary">上传</el-button>
        </el-upload>

    </div>
</template>

<script>
    import fileManageService from 'src/services/auto_operation/rb-auto-operation-file-manage-services.js'

    export default {
        data() {
            return {
                fileList: [],

            }
        },
        props: ['filePathVal'],
        methods: {
            handleRemove(file, fileList) {
                console.log('this.fileList', fileList)
                console.log('this.file', file)

                this.$emit('deleteFilePath', file.name)
            },
            // 上传文件
            UploadFile(param) {
                const formData = new FormData()
                formData.append('file', param.file)
                fileManageService
                    .uploadFile(formData)
                    .then(res => {
                        if (res.flag) {
                            this.$message('上传成功')
                            this.$emit('filePath', res.biz_data)
                        }
                    })
            },
        },
        watch: {
            filePathVal: {
                handler(val) {
                    this.fileList = val
                    console.log('this.fileList', this.fileList)
                },
                immediate: true,
                deep: true
            }
        }
    }
</script>
<style lang='scss' scoped>
</style>