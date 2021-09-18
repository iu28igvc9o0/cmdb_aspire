<template>
    <!-- 在线虚拟机批量关机 -->
    <div>
        <dialog-model ref="dialogModel"
                      :dialogAttribute="dialogAttribute"
                      @handleSureDialog="handleSureDialog"
                      @handleCancelDialog="handleCancelDialog">
            <template slot="content">
                <div class="virtual-content">
                    <div class="virtual-header">
                        <span class="template-download"
                              @click="templateDownLoad">模板下载</span>
                        <el-upload class="upload-demo"
                                   ref="upload"
                                   :headers="headers"
                                   :limit="limitNum"
                                   :action="uploadAction"
                                   :on-exceed="exceedFile"
                                   accept=".xlsx"
                                   :before-upload="beforeUploadFile"
                                   :on-success="handleSuccess"
                                   :on-error="handleError"
                                   :file-list="fileList"
                                   :auto-upload="false">
                            <el-button slot="trigger"
                                       size="small"
                                       type="primary">选择文件</el-button>
                            <el-button style="margin-left: 10px;"
                                       size="small"
                                       type="primary"
                                       class="el-icon-upload"
                                       @click="submitUpload">上传并解析</el-button>
                            <div slot="tip"
                                 class="el-upload__tip">只能上传xlsx文件，且不超过10M</div>
                        </el-upload>
                    </div>
                </div>
            </template>
        </dialog-model>
    </div>
</template>
<script>
    import rbCmdbServiceFactory from 'src/services/cmdb/rb-cmdb-service.factory.js'
    export default {
        components: {
            DialogModel: () => import('src/pages/resource/iframe/computerManagement/modelDialog.vue')
        },
        data() {
            return {
                dialogAttribute: {
                    dialogWidth: '1000px',
                    dialogTitle: '机柜设备导入',
                    dialogSure: '确认',
                    dialogCancel: ''
                },
                fileList: [],
                uploadAction: '/cmdb/vmTool/uploadShutdownVmFile/' + this.$route.query['module_id'],
                headers: {
                    'head_isAdmin': true,
                    'head_orgAccount': 'alauda',
                    'head_userName': 'alauda'
                },
                limitNum: 1,
                vmList: []
            }
        },
        created() {
            this.headers.Authorization = 'Bearer ' + sessionStorage.getItem('token')
        },
        methods: {
            templateDownLoad() {
                let moduleId = this.$route.query.module_id
                rbCmdbServiceFactory.getTemplateFile(moduleId).then((res) => {
                    let index = res.headers['content-disposition'].lastIndexOf('=')
                    let fileName = decodeURI(res.headers['content-disposition'].substr(index + 1))
                    let downLoadName = ''
                    if (fileName) {
                        downLoadName = fileName
                    } else {
                        downLoadName = '关机虚机模版.xlsx'
                    }
                    let blob = new Blob([res.data], { type: 'application/msword' })
                    // 创建下载链接
                    let objectUrl = URL.createObjectURL(blob)
                    let downLoadElement = document.createElement('a')
                    downLoadElement.href = objectUrl
                    downLoadElement.download = downLoadName
                    document.body.appendChild(downLoadElement)
                    downLoadElement.click()
                    document.body.removeChild(downLoadElement)
                    URL.revokeObjectURL(objectUrl)
                }).catch((e) => {
                    this.$message.error('下载失败!' + e)
                })
            },
            handleSureDialog(status) {
                this.$emit('closeUploadDialog', status)
            },
            handleCancelDialog(status) {
                this.$emit('closeUploadDialog', status)
                this.vmList = []
            },
            //  文件超出个数限制时的钩子
            exceedFile(files, fileList) {
                this.$message.warning(`只能选择 ${this.limitNum} 个文件，当前共选择了 ${files.length + fileList.length} 个`)
            },
            // 上传文件之前的钩子, 参数为上传的文件,若返回 false 或者返回 Promise 且被 reject，则停止上传
            beforeUploadFile(file) {
                let extension = file.name.substring(file.name.lastIndexOf('.') + 1)
                let size = file.size / 1024 / 1024
                if (extension != 'xlsx') {
                    this.$message.warning('只能上传后缀是.xlsx的文件')
                    return false
                }
                if (size > 10) {
                    this.$message.warning('文件大小不得超过10M')
                    return false
                }
            },
            // 文件上传成功时的钩子
            handleSuccess(response) {
                this.$alert(response.msg, '提示', {
                    dangerouslyUseHTMLString: true,
                    customClass: 'upload-tips'
                })
            },
            // 文件上传失败时的钩子
            handleError() {
                this.$message.error('文件上传失败')
            },
            submitUpload() {
                this.$nextTick(() => {
                    this.$refs.upload.submit()
                })
            }
        }
    }
</script>
<style lang="scss" scoped>
    .template-download {
        display: inline-block;
        background: url("../../../../assets/img/excel.png") no-repeat left center;
        padding-left: 20px;
        cursor: pointer;
    }
    .upload-demo {
        margin-top: 10px;
    }
</style>