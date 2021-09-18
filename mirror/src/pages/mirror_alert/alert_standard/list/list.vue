<template>
    <div class='components-container yw-dashboard'
         v-loading.fullscreen.lock="loading"
         :element-loading-text="loading_text">
        <el-form class='components-condition yw-form'
                 :inline='true'
                 label-width='90px'>
            <el-form-item label='设备分类'>
                <el-select v-model='searchForm.deviceClass'
                           placeholder='请选择'
                           @change="changeDeviceType">
                    <el-option v-for='item in deviceClassList'
                               :key='item.value'
                               :label='item.name'
                               :value='item.value'></el-option>
                </el-select>
            </el-form-item>
            <el-form-item label='设备类型'>
                <el-select v-model='searchForm.deviceType'
                           placeholder='请选择'>
                    <el-option v-for='item in deviceTypeList'
                               :key='item.value'
                               :label='item.name'
                               :value='item.value'></el-option>
                </el-select>
            </el-form-item>
            <el-form-item label='监控指标Key'>
                <el-input v-model='searchForm.monitorKey'></el-input>
            </el-form-item>
            <section class='btn-wrap'>
                <el-button type='primary'
                           @click='search(1)'>查询</el-button>
                <el-button @click="cancel()">重置</el-button>
            </section>
        </el-form>

        <el-form class='yw-form'>
            <div class='table-operate-wrap clearfix'>
                <el-button type='text'
                           icon='el-icon-plus'
                           @click='addAS()'>新增</el-button>
                <el-button type='text'
                           icon='el-icon-delete'
                           @click='deleteMultiRow()'>批量删除</el-button>
                <el-button type='text'
                           icon='el-icon-circle-check'
                           @click="setStatus('1')">启用</el-button>
                <el-button type='text'
                           icon='el-icon-circle-close'
                           @click="setStatus('0')">禁用</el-button>
                <el-button type='text'
                           icon='el-icon-refresh'
                           @click='syncHistory()'>全量更新历史告警</el-button>
                <el-button type='text'
                           icon='el-icon-upload2'
                           @click='uploadAS()'>导入</el-button>
                <el-button type='text'
                           icon='el-icon-download'
                           @click='downloadAS()'>导出</el-button>
            </div>
            <div class='yw-el-table-wrap'>
                <el-table class='yw-el-table'
                          border
                          :data='tableData'
                          style='cursor: pointer;'
                          stripe
                          tooltip-effect='dark'
                          height='calc(100vh - 260px)'
                          @selection-change='handleSelectionChange'>
                    <el-table-column type='selection'></el-table-column>
                    <el-table-column prop='deviceClass'
                                     label='设备分类'
                                     width='150px'
                                     :show-overflow-tooltip='true'></el-table-column>
                    <el-table-column prop='deviceType'
                                     label='设备类型'
                                     width='150px'></el-table-column>
                    <el-table-column prop='monitorKey'
                                     label='监控指标Key'
                                     width='150px'
                                     :show-overflow-tooltip='true'></el-table-column>
                    <el-table-column prop='standardName'
                                     label='标准名称'
                                     width='150px'
                                     :show-overflow-tooltip='true'></el-table-column>
                    <el-table-column prop='alertDesc'
                                     label='告警描述'
                                     :show-overflow-tooltip='true'></el-table-column>
                    <el-table-column prop='status'
                                     label='状态'
                                     width='150px'
                                     :formatter="statusFormatter"></el-table-column>
                    <el-table-column prop='alertLevel'
                                     label='告警等级'
                                     width='150px'
                                     :formatter="alertLevelFormatter"></el-table-column>
                    <el-table-column fixed="right"
                                     label="操作"
                                     width="120">
                        <template slot-scope="scope">
                            <el-button type="text"
                                       title="编辑"
                                       @click="updateRow(scope.row)">
                                <i style="color: #269BE0;"
                                   class="el-icon-edit"></i>
                            </el-button>
                            <el-button type="text"
                                       title="更新历史告警"
                                       @click="updateHistoryRow(scope.row)">
                                <i style="color: #269BE0;"
                                   class="el-icon-refresh"></i>
                            </el-button>
                            <el-button type="text"
                                       title="操作日志"
                                       @click="showOperateRowLog(scope.row.id)">
                                <i style="color: #269BE0;"
                                   class="el-icon-document"></i>
                            </el-button>
                            <el-button type="text"
                                       title="删除"
                                       @click="deleteRow(scope.row.id)">
                                <i style="color: #269BE0;"
                                   class="el-icon-delete"></i>
                            </el-button>
                        </template>
                    </el-table-column>
                </el-table>
            </div>
            <div class='yw-page-wrap'>
                <el-pagination @size-change='handleSizeChange'
                               @current-change='handleCurrentChange'
                               :current-page='currentPage'
                               :page-sizes='pageSizes'
                               :page-size='pageSize'
                               :total='total'
                               layout='total, sizes, prev, pager, next, jumper'></el-pagination>
            </div>
        </el-form>

        <!-- 新增、修改 拟态框 -->
        <el-dialog class="yw-dialog"
                   :title="operate.dialogTitle"
                   :visible.sync="operate.dialogShow"
                   width="650px">
            <section class="yw-dialog-main">
                <el-form class="yw-form"
                         ref="submitForm"
                         label-width="90px"
                         :model="formData"
                         :inline="true"
                         :rules="rule">
                    <el-form-item label="设备分类"
                                  prop="deviceClass">
                        <el-select v-model="formData.deviceClass"
                                   placeholder="请选择"
                                   @change="changeDeviceType">
                            <el-option v-for="item in deviceClassList"
                                       :key="item.value"
                                       :label="item.name"
                                       :value="item.value"></el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="设备类型"
                                  prop="deviceType">
                        <el-select v-model="formData.deviceType"
                                   placeholder="请选择">
                            <el-option v-for="item in deviceTypeList"
                                       :key="item.value"
                                       :label="item.name"
                                       :value="item.value"></el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="监控指标key"
                                  prop="monitorKey">
                        <el-input style="width:172px;"
                                  v-model="formData.monitorKey"
                                  placeholder="请输入内容"></el-input>
                    </el-form-item>
                    <el-form-item label="标准名称"
                                  prop="standardName">
                        <el-input style="width:172px;"
                                  v-model="formData.standardName"
                                  placeholder="请输入内容"></el-input>
                    </el-form-item>
                    <el-form-item label="告警描述"
                                  prop="alertDesc">
                        <el-input style="width:172px;"
                                  v-model="formData.alertDesc"
                                  type="textarea"
                                  :rows="2"
                                  placeholder="请输入内容"></el-input>
                    </el-form-item>
                    <el-form-item label="告警等级"
                                  prop="alertLevel">
                        <el-select v-model="multiAlertLevel"
                                   multiple
                                   collapse-tags
                                   placeholder="请选择"
                                   @change="multiChooseAlertLevel">
                            <el-option v-for="item in alertLevelList"
                                       :key="item.value"
                                       :label="item.name"
                                       :value="item.value"></el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="启用状态"
                                  prop="status">
                        <el-select v-model="formData.status"
                                   placeholder="请选择">
                            <el-option v-for="item in statusList"
                                       :key="item.name"
                                       :label="item.value"
                                       :value="item.name"></el-option>
                        </el-select>
                    </el-form-item>
                </el-form>
            </section>
            <section class="btn-wrap">
                <el-button type="primary"
                           @click="submitForm()">确定</el-button>
                <el-button @click="calcelSubmit()">取消</el-button>
            </section>
        </el-dialog>
        <!-- 同步历史数据拟态框 -->
        <el-dialog class="yw-dialog"
                   title="更新历史告警"
                   :visible.sync="historyDialog.dialogShow"
                   width="450px">
            <section>
                <el-form class="yw-form"
                         label-width="90px">
                    <el-form-item label="时间范围">
                        <el-date-picker v-model="dateRange"
                                        type="datetimerange"
                                        value-format="yyyy-MM-dd HH:mm:ss"
                                        range-separator="至"
                                        start-placeholder="开始日期"
                                        end-placeholder="结束日期">
                        </el-date-picker>
                    </el-form-item>
                </el-form>
            </section>
            <section class="btn-wrap">
                <el-button type="primary"
                           @click="updateHistory()">确定更新</el-button>
                <el-button @click="calcelSync()">取消</el-button>
            </section>
        </el-dialog>

        <!-- 操作日志显示拟态框 -->
        <alertOperatrLog v-if="logDialog.visiable"
                         :logDialog="logDialog"
                         @setOperateDialogClose="operateDialogClose"></alertOperatrLog>

        <!-- 导入拟态框 -->
        <el-dialog class="yw-dialog pbottom10"
                   title="导入"
                   :visible.sync="uploadDialog.visiable"
                   width="400px">
            <el-upload class="upload-demo"
                       :headers="uploadDialog.headers"
                       drag
                       ref="uploadForm"
                       :on-success="uploadSuccess"
                       :on-error="uploadError"
                       :data="uploadDialog.data"
                       :action="uploadDialog.actionUrl">
                <i class="el-icon-upload"></i>
                <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
            </el-upload>
            <div align="center"
                 class="p10">
                <el-button type="primary"
                           @click="downLoadTemp">还没有Excel模版?请下载模版</el-button>
            </div>
        </el-dialog>
    </div>
</template>

<script>
    import configDictService from '../../../../services/cmdb/rb-configDict-service.factory'
    import alertStandardService from '../../../../services/alert/rb-alert-standard-service.factory'
    import rbAutoOperationMixin from 'src/services/auto_operation/rb-auto-operation-mixin.js'

    export default {
        name: 'MirrorAlertStandardList',
        mixins: [rbAutoOperationMixin],
        components: {
            alertOperatrLog: () => import('src/pages/mirror_alert/alert_standard/list/operate_log_list.vue'),
            YwImport: () => import('src/components/common/yw-import.vue')
        },
        data() {
            return {
                loading: false,
                loading_text: '请稍候...',
                dateRange: [], // 更新历史告警的时间范围
                multiAlertLevel: [], // 多选告警等级
                multiSelect: [], // 选择数据
                tableData: [],
                deviceClassList: [],
                deviceTypeList: [],
                alertLevelList: [],
                statusList: [],
                searchForm: {
                    deviceClass: '',
                    deviceType: '',
                    monitorKey: ''
                },
                formData: {
                    deviceClass: '',
                    deviceType: '',
                    monitorKey: '',
                    standardName: '',
                    alertDesc: '',
                    alertLevel: '',
                    status: ''
                },
                operate: {
                    dialogType: '',
                    dialogTitle: '',
                    dialogShow: false
                },
                historyDialog: {
                    type: null,
                    dialogShow: false,
                    id: null
                },
                rule: {
                    deviceClass: [{ required: true, message: '请选择设备分类', trigger: ['blur', 'change'] }],
                    deviceType: [{ required: true, message: '请选择设备类型', trigger: ['blur', 'change'] }],
                    monitorKey: [{ required: true, message: '请输入监控指标Key', trigger: ['blur'] }],
                    standardName: [{ required: true, message: '请输入标准名称', trigger: ['blur'] }],
                    alertDesc: [{ required: true, message: '请输入告警描述', trigger: ['blur'] }],
                    alertLevel: [{ required: true, message: '请选择告警等级', trigger: ['blur', 'change'] }],
                    status: [{ required: true, message: '请选择状态', trigger: ['blur', 'change'] }]
                },
                logDialog: {
                    visiable: false,
                    id: null
                },
                uploadDialog: {
                    visiable: false,
                    actionUrl: '',
                    data: {}
                },
            }
        },
        mounted() {
            this.search()
            this.getDeviceTypeList()
            this.getDeviceClassList()
            this.getAlertLevelList()
            this.getMonitorEnableList()
        },
        methods: {
            // 导出
            downloadAS() {
                this.loading = true
                this.loading_text = '正在导出数据，请稍后...'
                alertStandardService.exportAll(this.searchForm).then((data) => {
                    let blob = new Blob([data], {
                        type: 'application/vnd.ms-excel'
                    })
                    // 创建下载链接
                    let objectUrl = URL.createObjectURL(blob)
                    let downLoadElement = document.createElement('a')
                    downLoadElement.href = objectUrl
                    downLoadElement.download = '告警标准化列表.xlsx'
                    document.body.appendChild(downLoadElement)
                    downLoadElement.click()
                    document.body.removeChild(downLoadElement)
                    URL.revokeObjectURL(objectUrl)
                }).finally(() => {
                    this.loading = false
                })
            },
            // 下载模板
            downLoadTemp() {
                this.loading = true
                this.loading_text = '正在下载模板,请稍后...'
                alertStandardService.downloadImportTemplate('告警标准化导入模板.xlsx').finally(() => {
                    this.loading = false
                })
            },
            // 导入失败
            uploadError() {
                this.$message.error('导入失败')
                this.$refs['uploadForm'].clearFiles()
            },
            // 导入成功
            uploadSuccess() {
                this.$message.success('导入成功')
                this.$refs['uploadForm'].clearFiles()
                this.uploadDialog.visiable = false
                this.search(1)
            },
            // 导入
            uploadAS() {
                var headers = {
                    Authorization: 'Bearer ' + sessionStorage.getItem('token'),
                    head_orgAccount: 'alauda',
                    head_userName: 'alauda'
                }
                var operator = sessionStorage.getItem('username')
                this.uploadDialog.headers = headers
                this.uploadDialog.visiable = true
                this.uploadDialog.data = {
                    operator: operator
                }
                this.uploadDialog.actionUrl = '/v1/alerts/standard/import'
            },
            // 设备分类级联设备类型
            changeDeviceType(row) {
                var item = this.deviceClassList.find(p => p.value == row)
                this.getDeviceTypeList(item.id, item.type)
                this.resetDeviceType()
            },
            // 重置前一步选择的设备类型
            resetDeviceType() {
                this.searchForm.deviceType = ''
                this.formData.deviceType = ''
            },
            // 当行同步历史告警
            updateHistoryRow(row) {
                if (row.status == '0') {
                    this.$message.info('该数据状态为禁用')
                    return
                }
                this.historyDialog.dialogShow = true
                this.historyDialog.type = 'ONE'
                this.historyDialog.id = row.id
            },
            // 单行数据的操作日志,显示操作日志拟态框
            showOperateRowLog(id) {
                this.logDialog.id = id
                this.logDialog.visiable = true
            },
            // 单个数据删除
            deleteRow(id) {
                this.$confirm('确认是否删除', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'info'
                }).then(() => {
                    var operator = sessionStorage.getItem('username')
                    var param = {
                        ids: id,
                        operator: operator
                    }
                    this.deleteMethod(param)
                })
            },
            // 更新告警历史数据
            updateHistory() {
                if (this.dateRange.length == 0) {
                    this.$message.info('请选择时间范围')
                    return
                }
                this.loading = true
                this.loading_text = '正在同步历史告警数据，请稍等...'
                var operator = sessionStorage.getItem('username')
                var param = {
                    operator: operator,
                    startTime: this.dateRange[0],
                    endTime: this.dateRange[1]
                }
                if (this.historyDialog.type == 'ONE') {
                    var id = this.historyDialog.id
                    alertStandardService.updateHistoryOneRow(id, param).then(() => {
                        this.$message.success('更新告警历史数据成功')
                    }).catch(() => {
                        this.$message.error('更新告警历史数据失败')
                    }).finally(() => {
                        this.calcelSync()
                        this.loading = false
                    })
                } else {
                    alertStandardService.updateHistory(param).then(() => {
                        this.$message.success('更新告警历史数据成功')
                    }).catch(() => {
                        this.$message.error('更新告警历史数据失败')
                    }).finally(() => {
                        this.calcelSync()
                        this.loading = false
                    })
                }
            },
            // 取消更新历史数据
            calcelSync() {
                this.dateRange = []
                this.historyDialog.dialogShow = false
            },
            // 全量更新——历史告警拟态框
            syncHistory() {
                this.historyDialog.dialogShow = true
            },
            // 更新状态
            setStatus(status) {
                if (this.multiSelect.length == 0) {
                    this.$message.error('请选择数据')
                    return
                }
                const crtData = this.multiSelect
                console.info(crtData)
                const ids = []
                var flag = false
                for (var i = 0; i < crtData.length; i++) {
                    ids.push(crtData[i].id)
                    if (crtData[i].status == status) {
                        flag = true
                    }
                }
                if (flag) {
                    var statusStr = status == '0' ? '禁用' : '启用'
                    this.$message.error('选中的数据中存在' + statusStr + '数据')
                    return
                }
                var operator = sessionStorage.getItem('username')
                var data = {
                    operator: operator,
                    ids: ids.toString()
                }
                alertStandardService.updateStatus(data).then(() => {
                    this.$message.success('状态更新成功')
                }).catch(() => {
                    this.$message.error('状态更新失败')
                }).finally(() => {
                    this.search(1)
                })
            },
            // 多选删除
            deleteMultiRow() {
                this.$confirm('确认是否删除所选数据', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'info'
                }).then(() => {
                    var ids = []
                    this.multiSelect.forEach((item) => {
                        ids.push(item.id)
                    })
                    var operator = sessionStorage.getItem('username')
                    var param = {
                        ids: ids.toString(),
                        operator: operator
                    }
                    this.deleteMethod(param)
                })
            },
            // 删除方法
            deleteMethod(param) {
                alertStandardService.deleteByIds(param).then(() => {
                    this.$message.success('删除成功')
                }).catch(() => {
                    this.$message.error('删除失败')
                }).finally(() => {
                    this.search(1)
                })
            },
            // 多选告警等级
            multiChooseAlertLevel(row) {
                this.formData.alertLevel = row.toString()
            },
            // 更新拟态框
            updateRow(row) {
                this.operate.dialogType = 'UPDATE'
                this.operate.dialogTitle = '编辑'
                this.operate.dialogShow = true
                const ctdData = row
                var formData = {
                    id: ctdData.id,
                    deviceClass: ctdData.deviceClass,
                    deviceType: ctdData.deviceType,
                    monitorKey: ctdData.monitorKey,
                    standardName: ctdData.standardName,
                    alertDesc: ctdData.alertDesc,
                    alertLevel: ctdData.alertLevel,
                    status: ctdData.status
                }
                this.formData = formData
                this.multiAlertLevel = formData.alertLevel.split(',')
                this.$refs['submitForm'].clearValidate()
            },
            // 取消提交表单
            calcelSubmit() {
                var operate = {
                    dialogType: '',
                    dialogTitle: '',
                    dialogShow: false
                }
                this.operate = operate
                this.dataInit()
                this.search(1)
            },
            // 数据重置
            dataInit() {
                var formData = {
                    deviceClass: '',
                    deviceType: '',
                    monitorKey: '',
                    standardName: '',
                    alertDesc: '',
                    alertLevel: '',
                    status: ''
                }
                this.formData = formData
                this.multiAlertLevel = []
            },
            // 提交表单数据
            submitForm() {
                this.$refs['submitForm'].validate((valid) => {
                    if (valid) {
                        this.formData.insertPerson = sessionStorage.getItem('username')
                        if (this.operate.dialogType === 'ADD') {
                            alertStandardService.insert(this.formData).then(() => {
                                this.$message.success('新增成功')
                            }).catch(() => {
                                this.$message.error('新增失败')
                            }).finally(() => {
                                this.calcelSubmit()
                            })
                        } else {
                            alertStandardService.update(this.formData).then(() => {
                                this.$message.success('更新成功')
                            }).catch(() => {
                                this.$message.error('更新失败')
                            }).finally(() => {
                                this.calcelSubmit()
                            })
                        }
                    }
                })
            },
            // 新增拟态框
            addAS() {
                this.operate.dialogType = 'ADD'
                this.operate.dialogTitle = '新增'
                this.operate.dialogShow = true
                this.$refs['submitForm'].clearValidate()
            },
            statusFormatter(row) {
                return row.status === '1' ? '启用' : '禁用'
            },
            alertLevelFormatter(row) {
                if (this.alertLevelList.length == 0) {
                    return row.alertLevel
                }
                var levels = row.alertLevel.split(',')
                var copyLevels = []
                for (var i = 0; i < levels.length; i++) {
                    var kv = this.alertLevelList.find(item => item.value === levels[i])
                    copyLevels.push(kv.name)
                }
                return copyLevels.toString()
            },
            // 重置搜索框
            cancel() {
                var searchForm = {
                    deviceClass: '',
                    deviceType: '',
                    monitorKey: ''
                }
                this.searchForm = searchForm
                this.search(1)
                this.getDeviceTypeList()
            },
            // 处理多选
            handleSelectionChange(row) {
                this.multiSelect = row
            },
            // 获取TableList
            search(pageNum) {
                this.loading = true
                this.currentPage = pageNum ? pageNum : this.currentPage
                let req = {
                    pageNo: this.currentPage,
                    pageSize: this.pageSize,
                }
                req = Object.assign(req, this.searchForm)
                alertStandardService.list(req).then((res) => {
                    this.tableData = res.result
                    this.total = res.count
                }).finally(() => {
                    this.loading = false
                })
            },
            // 获取告警等级
            getAlertLevelList() {
                configDictService
                    .getDictsByType({
                        type: 'alert_level'
                    })
                    .then(data => {
                        this.alertLevelList = data
                    })
            },
            // 获取设备类型列表
            getDeviceTypeList(pid, ptype) {
                configDictService
                    .getDictsByType({
                        type: 'device_type',
                        pid: pid,
                        pType: ptype
                    })
                    .then(data => {
                        this.deviceTypeList = data
                    })
            },
            // 获取设备分类列表
            getDeviceClassList() {
                configDictService
                    .getDictsByType({
                        type: 'device_class'
                    })
                    .then(data => {
                        this.deviceClassList = data
                    })
            },
            // 获取监控状态列表
            getMonitorEnableList() {
                configDictService
                    .getDictsByType({
                        type: 'alert_standard_status'
                    })
                    .then(data => {
                        this.statusList = data
                    })
            }
        }
    }
</script>

<style lang='scss' scoped>
</style>
