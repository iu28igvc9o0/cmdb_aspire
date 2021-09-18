<!--suppress ALL -->
<template>
    <el-dialog :title="title"
               :visible.sync="showImportDialog"
               :append-to-body="true"
               width="500px"
               @close="closeImportDialog()">
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
            <el-form-item align="center"
                          v-if="importType!=='fault'">
                <el-button type="primary"
                           @click="downLoadTemp">还没有Excel模版?请下载模版</el-button>
            </el-form-item>
            <el-form-item class="template-form-item"
                          v-if="isShowItem && importType == 'instance'"
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
                           @click="onSubmit">导出</el-button>
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
<style scoped>
</style>
<script>
    import $ from 'jquery'
    import rbCmdbServiceFactory from 'src/services/cmdb/rb-cmdb-service.factory.js'
    import rbNetContrastServicesFactory from 'src/services/net/rb-net-contrast-services.factory.js'

    export default {
        props: ['showImport', 'importType', 'importId', 'dataStart', 'SelectProps', 'params', 'instanceId', 'maintenanceId', 'screenParams'],
        data() {
            return {
                showImportDialog: this.showImport,
                showImportPanel: this.showImport,
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
                title: '导入Excel新增或更新数据',
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
            $(document).find('input[type=file]:eq(0)').hide()
            this.headers.Authorization = 'Bearer ' + sessionStorage.getItem('token')
            this.actionUrl = '/v1/cmdb/process/import'

            // 变更计划导入接口
            if (this.importType === 'task_list_template') {
                this.actionUrl = '/v1/alerts/task/importTaskTemplate'
            }

            this.actionParams.importType = this.importType
            // 故障报告导入
            if (this.importType === 'fault') {
                this.actionUrl = '/v1/alerts/fault_manage/import/' + this.importId
                this.actionParams.id = this.importId
                console.log('this.actionParams===', this.actionParams)
            }
            if (this.importType === 'contrast') {
                this.actionUrl = '/v1/configCompare/import'
            }

            // 租户大屏利用率导入参数
            if (this.screenParams) {
                this.actionParams.systemTitle = this.screenParams.systemTitle
                this.actionParams.deviceType = this.screenParams.deviceType
                this.actionParams.hardwareType = this.screenParams.hardwareType
                this.actionParams.monthlyTime = this.screenParams.monthlyTime
            }
            // 维保部件导入参数
            if (this.maintenanceId) {
                this.actionParams.maintenanceId = this.maintenanceId
            }

            if (this.instanceId) {
                this.actionParams.instanceId = this.instanceId
            }
            if (this.dataStart) {
                this.actionParams.dataStart = this.dataStart
            }
            if (this.SelectProps) {
                this.actionParams.createUsername = this.SelectProps.person
                this.actionParams.createUserPhone = this.SelectProps.mobile
                this.actionParams.quarter = this.SelectProps.year + this.SelectProps.quarter
                this.actionParams.province = this.SelectProps.province
            }
            if (this.importType === 'maintenance_project_bind_device') {
                this.actionParams.maintenanceProjectName = this.params.projectName
            }
            console.log(this.actionParams)
            this.initData()
        },
        beforeDestroy: function () {
            clearInterval(this.timeSetter)
            this.timeSetter = null
            this.closeImportDialog()
        },
        methods: {
            downLoadTemp() {
                this.isShowItem = !this.isShowItem
                if (this.importType === 'repairevent') {
                    rbCmdbServiceFactory.downloadImportTemplate('repairevent.xlsx', '维修事件模板.xlsx')
                }
                if (this.importType === 'equipmentproblem') {
                    rbCmdbServiceFactory.downloadImportTemplate('equipmentproblem.xlsx', '设备问题模板.xlsx')
                }
                if (this.importType === 'problemevent') {
                    rbCmdbServiceFactory.downloadImportTemplate('problemevent.xlsx', '故障事件模板.xlsx')
                }
                if (this.importType === 'maintensoftware') {
                    rbCmdbServiceFactory.downloadImportTemplate('软件维保管理导入模板.xlsx')
                }
                if (this.importType === 'maintensoftwarerecord') {
                    rbCmdbServiceFactory.downloadImportTemplate('软件维保使用管理导入模板.xlsx')
                }
                if (this.importType === 'maintenance_project') {
                    rbCmdbServiceFactory.downloadImportTemplate('维保项目导入模板.xlsx')
                }
                if (this.importType === 'maintenance_project_bind_device') {
                    rbCmdbServiceFactory.downloadImportTemplate('维保项目导入设备序列号模板.xlsx')
                }
                if (this.importType === 'hardware') {
                    rbCmdbServiceFactory.downloadImportTemplate('硬件维保管理导入模板.xlsx')
                }
                if (this.importType === 'hardwareuse') {
                    rbCmdbServiceFactory.downloadImportTemplate('硬件维保使用管理导入模板.xlsx')
                }
                if (this.importType === 'invalidresource') {
                    rbCmdbServiceFactory.downloadImportTemplate('低效无效资源统计.xlsx')
                }
                if (this.importType === 'bizresourcephy') {
                    rbCmdbServiceFactory.downloadImportTemplate('业务物理资源导入模板.xlsx')
                }
                if (this.importType === 'bizresourcevm') {
                    rbCmdbServiceFactory.downloadImportTemplate('业务虚拟资源导入模板.xlsx')
                }
                if (this.importType === 'produce') {
                    rbCmdbServiceFactory.downloadImportTemplate('维保厂商导入模板.xlsx')
                }
                if (this.importType === 'portRelation') {
                    rbCmdbServiceFactory.downloadImportTemplate('资源关联设备端口.xlsx')
                }
                if (this.importType === 'networkCard') {
                    rbCmdbServiceFactory.downloadImportTemplate('信息导入模板.xlsx')
                }
                if (this.importType === 'evaluate') {
                    var data = {
                        province: this.actionParams.province,
                        quarter: this.actionParams.quarter
                    }
                    this.$emit('setExportTemplate', data)
                }
                if (this.importType === 'deviceInfo') {
                    rbCmdbServiceFactory.downloadImportTemplate('IT设备量信息导入模板.xlsx')
                }
                if (this.importType === 'resourceassign') {
                    rbCmdbServiceFactory.downloadImportTemplate('资源分配分析报表模板.xlsx')
                }
                if (this.importType === 'mainten_component') {
                    rbCmdbServiceFactory.downloadImportTemplate('维保部件清单导入模板.xlsx')
                }
                if (this.importType === 'screen_resource_allocate') {
                    rbCmdbServiceFactory.downloadImportTemplate('资源分配总量导入模板.xlsx')
                }
                if (this.importType === 'screen_max_utilization') {
                    rbCmdbServiceFactory.downloadImportTemplate('资源利用率均峰值导入模板.xlsx')
                }
                if (this.importType === 'screen_avg_utilization') {
                    rbCmdbServiceFactory.downloadImportTemplate('资源利用率平均值导入模板.xlsx')
                }
                if (this.importType === 'bs_not_inspect') {
                    rbCmdbServiceFactory.downloadImportTemplate('业务系统详细信息导入模板.xlsx')
                }
                if (this.importType == 'store_resource_low_utilization') {
                    rbCmdbServiceFactory.downloadImportTemplate('存储资源低利用率导入模板.xlsx')
                }
                if (this.importType == 'bs_online_info') {
                    rbCmdbServiceFactory.downloadImportTemplate('业务系统上线信息导入模板.xlsx')
                }
                // 主备对比导入模板
                if (this.importType == 'contrast') {
                    rbNetContrastServicesFactory.downloadTemplate()
                }
                if (this.importType == 'task_list_template') {
                    this.$emit('downloadTaskTemplate')
                }
            },
            closeImportDialog() {
                this.$emit('setImportDisplay', false)
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
            submitUpload() {
                this.$refs.upload.submit()
            },
            // 提交表单
            onSubmit() {
                rbCmdbServiceFactory.downloadExcel(this.moduleId).then((data) => {
                    let blob = new Blob([data], { type: 'application/msword' })
                    // 创建下载链接
                    let objectUrl = URL.createObjectURL(blob)
                    let downLoadElement = document.createElement('a')
                    downLoadElement.href = objectUrl
                    downLoadElement.download = '配置模板.xlsx'
                    document.body.appendChild(downLoadElement)
                    downLoadElement.click()
                    document.body.removeChild(downLoadElement)
                    URL.revokeObjectURL(objectUrl)
                })
            },
            initData() {
                // this.userData.createUsername= this.SelectProps ? this.SelectProps.person : '',
                //   this.userData.createUserPhone= this.SelectProps ? this.SelectProps.mobile : ''
                if (this.importType === 'instance')
                    rbCmdbServiceFactory.getModuleTree('').then((data) => {
                        this.selectData = data
                    })
            },
            resetForm(formName) {
                this.$refs[formName].resetFields()
            },
            handleError(response) {
                this.$message.error(response.message)
                this.$refs['import-upload'].clearFiles()
            },
            handleSuccess(response) {
                // 变更计划导入
                if (this.importType === 'task_list_template') {
                    if (response === 'success') {
                        this.showImportPanel = false
                        this.$message.success('导入成功')
                    } else {
                        this.$message.error('导入失败')
                        this.$refs['import-upload'].clearFiles()
                    }
                }
                if (this.importType === 'fault') {
                    if (response.code === '0000') {
                        this.showImportDialog = false
                        this.$emit('setImportPath', response.path)
                    } else {
                        this.$message.error(response.message)
                        this.$refs['import-upload'].clearFiles()
                    }
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
            validFile(file) {
                // 文件类型不做严格校验：windows server 2012系统下获取到的file.type为空
                const isExcel = file.name.includes('.xls')
                const fileType = file.type === 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' || isExcel
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

                        // 导入成功，更新列表
                        this.$emit('getSearch')
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
                                _t.closeImportDialog()
                            }
                        }).catch(() => {
                            _t.$message.error('移除导入进程' + _t.processId + '失败')
                        })
                    }
                })
            },
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
    .el-dialog__body {
        padding-left: 30px !important;
        padding-bottom: 0px !important;
        padding-right: 30px !important;
        padding-top: 0px !important;
        color: #606266;
        font-size: 14px;
        word-break: break-all;
        .template-form-item {
            /deep/ .el-form-item__content {
                display: inline-block;
                margin-left: 10px;
            }
        }
    }
</style>
