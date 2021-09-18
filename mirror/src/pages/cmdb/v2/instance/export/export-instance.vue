<!--suppress ALL -->
<template>
    <div>
        <el-dialog class="yw-dialog"
                   :title="title"
                   :visible.sync="showExportDialog"
                   width="500px"
                   @close="closeExportDialog()"
                   v-loading="loading"
                   :element-loading-text="loading_text">
            <el-form ref="processPanel"
                     label-position="top"
                     class="demo-ruleForm"
                     label-width="100px"
                     style="padding-right:30px;">
                <div style="font: 14px Small"
                     v-if="showCancel">
                    正在数据, 共<span style="color: #409EFF; font-weight: bold;"> {{totalCount}} </span>条记录,
                    已导出<span style="color: #409EFF; font-weight: bold;"> {{processCount}} </span>条,
                    预计剩余处理时长<span style="color: #E6A23C; font-weight: bold;"> {{leaveTime}} </span>秒, 请稍等...
                    <div style="font: 14px small;">
                        <el-button style="text-decoration:none; color: #F56C6C; font-weight: bold;"
                                   type="text"
                                   align="left"
                                   @click="stopProcess">
                            中止下载
                        </el-button>
                    </div>
                </div>
                <div style="font: 14px Small"
                     v-if="!showCancel">
                    数据导出完成.
                </div>
                <div style="width: 100%; padding-bottom: 50px;"
                     v-loading="true"
                     v-if="showCancel"></div>
            </el-form>
        </el-dialog>
    </div>
</template>
<style scoped>
</style>
<script>
    import rbCmdbServiceFactory from 'src/services/cmdb/rb-cmdb-service.factory.js'
    import CommonOption from 'src/utils/commonOption.js'
    export default {
        mixins: [CommonOption],
        props: ['showExport', 'moduleType', 'moduleId', 'queryParams'],
        data() {
            return {
                loading: false,
                loading_text: '',
                showExportDialog: this.showExport,
                showExportPanel: this.showExport,
                showProcessPanel: false,
                isShowItem: false,
                headers: {
                    'head_isAdmin': true,
                    'head_orgAccount': 'alauda',
                    'head_userName': 'alauda'
                },
                title: 'Excel数据导出',
                processId: '',
                totalCount: 0,
                processCount: 0,
                leaveTime: 0,
                timeSetter: null,
                showCancel: true,
                showFinish: false,
                exportFilePath: ''
            }
        },
        mounted: function () {
            let resdata = this.$utils.deepClone(this.queryParams)
            resdata.pageSize = null
            resdata.currentPage = null
            let _t = this
            rbCmdbServiceFactory.exportInstanceList(resdata, this.moduleType).then((data) => {
                if (data.code === 'success') {
                    _t.processId = data.processId
                    this.timeSetter = setInterval(() => {
                        _t.getImportProcess(_t.processId)
                    }, 2000)
                } else {
                    this.$message.error(data.message)
                }
            }).catch((e) => {
                this.pageLoading = false
                this.$message.error('导出失败!' + e)
            }).finally(() => {
                // this.hideLoading()
            })
        },
        beforeDestroy: function () {
            clearInterval(this.timeSetter)
            this.timeSetter = null
            this.closeExportDialog()
        },
        methods: {
            closeExportDialog() {
                this.$emit('setExportDisplay', false)
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
                            _t.exportFilePath = data.data.exportFilePath
                            _t.downExportFile()
                            _t.closeExportDialog()
                        }
                    }
                })
            },
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
                                _t.closeExportDialog()
                            }
                        }).catch(() => {
                            _t.$message.error('移除导入进程' + _t.processId + '失败')
                        })
                    }
                })
            },
            downExportFile() {
                let _this = this
                this.exportFilesByLink({
                    path: _this.exportFilePath,
                    fileName: '资源列表.xlsx'
                })
            }
        }
    }
</script>
<style lang="scss" scoped>
</style>
