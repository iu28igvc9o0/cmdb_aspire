<!--上传文件 -->
<template>
    <el-dialog :title="title"
               :visible.sync="showImportDialog"
               :append-to-body="true"
               width="500px"
               @close="closeDialog()">
        <el-form ref="importPanel"
                 label-position="top"
                 class="demo-ruleForm"
                 label-width="100px"
                 style="padding-right:30px;"
                 v-if="showImportPanel">
            <el-form-item align="center">
                <el-upload ref="import-upload"
                           class="upload-demo"
                           :headers="headers"
                           drag
                           :action="actionUrl"
                           :data="actionParams"
                           :on-success="handleSuccess"
                           :before-upload="validFile">
                    <i class="el-icon-upload"></i>
                    <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
                </el-upload>
            </el-form-item>
            <el-form-item align="center">
                <el-button type="primary"
                           @click="downLoadTemp">还没有Excel模版?请下载模版</el-button>
            </el-form-item>
            <el-form-item class="template-form-item"
                          v-if="isShowItem"
                          label="请选择导出类型:">
                <el-select placeholder="配置类型"
                           v-model="moduleId"
                           filterable>
                    <el-option-group v-for="group in selectData"
                                     :key="group.id"
                                     :label="group.name">
                        <el-option v-for="item in group.childModules"
                                   :key="item.id"
                                   :label="item.name"
                                   :value="item.id">
                        </el-option>
                    </el-option-group>
                </el-select>
                <el-button type="primary"
                           @click="downLoadTemp">导出</el-button>
            </el-form-item>
        </el-form>
        <el-form ref="processPanel"
                 label-position="top"
                 class="demo-ruleForm"
                 label-width="100px"
                 style="padding-right:30px;"
                 v-if="showProcessPanel">
            <div style="width: 100%; padding-bottom: 50px;"
                 v-loading="true"
                 v-if="showCancel"></div>
            <div style="font: 14px Small"
                 v-if="showCancel">
                正在导入Excel数据, 共<span style="color: #409EFF; font-weight: bold;"> {{totalCount}} </span>条记录,
                已处理<span style="color: #409EFF; font-weight: bold;"> {{processCount}} </span>条,
                成功<span style="color: #67C23A; font-weight: bold;"> {{successCount}} </span>条,
                失败<span style="color: #F56C6C; font-weight: bold;"> {{failCount}} </span>条.
                预计剩余处理时长<span style="color: #E6A23C; font-weight: bold;"> {{leaveTime}} </span>秒, 请稍等...
                <div style="font: 14px small;">
                    <el-button style="text-decoration:none; color: #F56C6C; font-weight: bold;"
                               type="text"
                               align="left"
                               @click="stopProcess">
                        中止上传
                    </el-button>
                </div>
            </div>
            <div style="font: 14px Small"
                 v-if="showFinish">
                导入Excel数据结束, 共<span style="color: #409EFF; font-weight: bold;"> {{totalCount}} </span>条记录,
                已处理<span style="color: #409EFF; font-weight: bold;"> {{processCount}} </span>条,
                成功<span style="color: #67C23A; font-weight: bold;"> {{successCount}} </span>条,
                失败<span style="color: #F56C6C; font-weight: bold;"> {{failCount}} </span>条.
                处理时长<span style="color: #E6A23C; font-weight: bold;"> {{leaveTime}} </span>秒。
            </div>
            <div style="font: 14px small;"
                 v-if="showErrorDown">
                <el-button style="text-decoration:none; color: #F56C6C; font-weight: bold;"
                           type="text"
                           align="left"
                           @click="downImportErrorRecord">
                    下载失败列表
                </el-button>
            </div>
        </el-form>
    </el-dialog>
</template>

<script>
    import rbCmdbServiceFactory from 'src/services/cmdb/rb-cmdb-service.factory.js'
    export default {
        props: ['uploadData'],
        data() {
            return {
                fileTypes: ['application/vnd.ms-excel', 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'],// 文件类型
                showImportDialog: this.uploadData.show,
                showImportPanel: this.uploadData.show,
                showProcessPanel: false,
                actionUrl: '',
                actionParams: {},
                moduleId: '',
                selectData: [],
                isShowItem: false,
                fileList: [],
                headers: {
                    'head_isAdmin': true,
                    'head_orgAccount': 'alauda',
                    'head_userName': 'alauda'
                },
                title: '上传',
                processId: '',
                totalCount: 0,
                processCount: 0,
                successCount: 0,
                failCount: 0,
                leaveTime: 0,
                timeSetter: null,
                showCancel: true,
                showFinish: false,
                showErrorDown: false
            }
        },
        mounted: function () {
            this.headers.Authorization = 'Bearer ' + sessionStorage.getItem('token')
            this.actionUrl = this.uploadData.actionUrl || '/v1/cmdb/process/import'
            this.actionParams = this.uploadData.actionParams || {}
        },
        beforeDestroy: function () {
            clearInterval(this.timeSetter)
            this.timeSetter = null
            this.closeDialog()
        },
        methods: {
            // 下载模板
            downLoadTemp() {
                // this.isShowItem = !this.isShowItem;
                this.$emit('uploadEvent', { eventType: 'downLoadTemp' })
            },
            // 关闭弹窗
            closeDialog() {
                this.$emit('uploadEvent', { eventType: 'closeDialog' })
                if (this.processId !== '') {
                    let _t = this
                    rbCmdbServiceFactory.removeImportProcess(this.processId).then(() => {
                        clearInterval(this.timeSetter)
                        _t.timeSetter = null
                    }).catch(() => {
                        _t.$message.error('移除导入进程' + _t.processId + '失败')
                    })
                }
            },
            // 上传成功
            handleSuccess(response) {
                // 龙凤故障导入接口直接返回了数据(后面得改成进度，此块代码得隐藏)
                if (response.flag === 'true' && response.data && Array.isArray(response.data)) {
                    this.$emit('uploadEvent', { eventType: 'uploadFinish', datas: response.data })
                    return
                }
                if (response.flag === 'true') {
                    this.title = '文件导入进度'
                    this.showImportPanel = false
                    this.showProcessPanel = true
                    let _t = this
                    _t.processId = response.processId
                    this.timeSetter = setInterval(() => {
                        _t.getImportProcess(_t.processId)
                    }, 2000)
                } else {
                    this.$message.error(response.message)
                    this.$refs['import-upload'].clearFiles()
                }
            },
            // 上传校验
            validFile(file) {

                // 文件类型不做严格校验：windows server 2012系统下获取到的file.type为空
                const isExcel = file.name.includes('.xls')
                const fileType = this.fileTypes.indexOf(file.type) > -1 || isExcel
                const maxSize = file.size / 1024 / 1024 < 1
                if (!fileType) {
                    this.$message.error('上传文件格式不正确, 请选择[xlsx、xls]格式文件.')
                    return false
                }
                if (!maxSize) {
                    this.$message.error('上传文件大小不能超过1MB.')
                    return false
                }
                return true
            },
            // 上传进度
            getImportProcess(proccessId) {
                let _t = this
                rbCmdbServiceFactory.getImportProcess(proccessId).then((data) => {
                    if (data.flag === 'true') {
                        _t.totalCount = data.data.totalRecord
                        _t.processCount = data.data.processCount
                        _t.successCount = data.data.successCount
                        _t.failCount = data.data.errorCount
                        _t.leaveTime = data.data.leaveTime
                        if (data.data.endTime) {
                            clearInterval(_t.timeSetter)
                            _t.timeSetter = null
                            _t.showCancel = false
                            _t.showFinish = true
                            _t.leaveTime = (data.data.endTime - data.data.startTime) / 1000
                            _t.showErrorDown = data.data.errorCount > 0
                        }
                    }
                })
            },
            // 终止上传
            stopProcess() {
                this.$confirm('确认要中止文件导入?').then(() => {
                    if (this.processId !== '') {
                        let _t = this
                        rbCmdbServiceFactory.removeImportProcess(this.processId).then((item) => {
                            if (item.flag === 'false') {
                                _t.$message.error('中止失败')
                            } else {
                                _t.$message.success('中止成功')
                                clearInterval(_t.timeSetter)
                                _t.timeSetter = null
                                _t.processId = ''
                                _t.closeDialog()
                            }
                        }).catch(() => {
                            _t.$message.error('移除导入进程' + _t.processId + '失败')
                        })
                    }
                })
            },
            // 失败记录
            downImportErrorRecord() {
                rbCmdbServiceFactory.downloadProcessErrorRecord(this.processId).then((data) => {
                    let blob = new Blob([data], { type: 'application/msword' })
                    // 创建下载链接
                    let objectUrl = URL.createObjectURL(blob)
                    let downLoadElement = document.createElement('a')
                    downLoadElement.href = objectUrl
                    downLoadElement.download = '导入失败记录.xlsx'
                    document.body.appendChild(downLoadElement)
                    downLoadElement.click()
                    document.body.removeChild(downLoadElement)
                    URL.revokeObjectURL(objectUrl)
                })
            }
        }
    }
</script>
<style lang="scss" scoped>
</style>
