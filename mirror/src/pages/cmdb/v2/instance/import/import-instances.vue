<!--suppress ALL -->
<template>
    <div>
        <el-dialog class="yw-dialog"
                   :title="title"
                   :visible.sync="showImportDialog"
                   width="500px"
                   @close="closeImportDialog()"
                   v-loading="loading"
                   :element-loading-text="loading_text">
            <el-form ref="importPanel"
                     class="demo-ruleForm"
                     v-if="showImportPanel">
                <el-form-item align="center">
                    <el-upload ref="import-upload"
                               class="upload-demo"
                               :headers="headers"
                               drag
                               :action="'/v1/cmdb/process/import?importType=' + importType"
                               :data="actionParams"
                               :on-success="handleSuccess"
                               :before-upload="confirmSubmit"
                               :http-request="UploadFile">
                        <i class="el-icon-upload"></i>
                        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
                    </el-upload>
                </el-form-item>
                <el-form-item align="center">
                    <el-button type="primary"
                               @click="downloadTem">还没有Excel模版?请下载模版</el-button>
                </el-form-item>
                <el-form-item v-if="isShowItem"
                              align="center">
                    <el-col :span="4">设备模型</el-col>
                    <el-col :span="12">
                        <treeselect class="yw-treeselect"
                                    v-model="newModuleId"
                                    :options="moduleLists"
                                    :multiple="false"
                                    :auto-load-root-options="false"
                                    :limit="1"
                                    :flat="true"
                                    noChildrenText="暂无子节点"
                                    noOptionsText="暂无数据"
                                    placeholder="请选择设备模型">
                        </treeselect>
                        <form id="frmHostInfo"
                              name="frmHostInfo"
                              method="post"
                              fit="true"></form>
                    </el-col>
                    <el-col :span="4">
                        <el-button type="primary"
                                   @click="onSubmit">下载模板</el-button>
                    </el-col>
                    <!--设备分类-->
                    <!--<el-select placeholder="请选择设备分类" filterable v-model="deviceClass" @change="changeDeivceType">-->
                    <!--<el-option v-for="item in selectData"-->
                    <!--:key="item.id"-->
                    <!--:label="item.name"-->
                    <!--:value="item.id">-->
                    <!--</el-option>-->
                    <!--</el-select>-->
                    <!--</el-form-item>-->
                    <!--<el-form-item v-if="isShowItem" align="center">-->
                    <!--设备类型-->
                    <!--<el-select placeholder="请选择设备类型" v-model="moduleId" filterable>-->
                    <!--<el-option v-for="item in childData"-->
                    <!--:key="item.id"-->
                    <!--:label="item.name"-->
                    <!--:value="item.id">-->
                    <!--</el-option>-->
                    <!--</el-select>-->
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
                <div style="font: 14px small;"
                     v-if="showErrorList">
                    <el-button style="text-decoration:none; color: #F56C6C; font-weight: bold;"
                               type="text"
                               align="left"
                               @click="importErrorList">
                        失败列表
                    </el-button>
                </div>
            </el-form>
        </el-dialog>

        <el-dialog class="yw-dialog"
                   title="失败列表"
                   :visible.sync="showError"
                   width="500px">
            <section>
                <el-table ref="serviceTable"
                          :data="errorList"
                          class="yw-el-table mtop10"
                          stripe
                          border
                          width="100%"
                          height="200">
                    <el-table-column label="资源池"
                                     prop="pool">
                    </el-table-column>
                    <el-table-column label="ip"
                                     prop="agent_ip">
                    </el-table-column>
                    <el-table-column label="操作系统"
                                     prop="host_os_type">
                    </el-table-column>
                    <el-table-column label="失败原因"
                                     prop="error_reason">
                    </el-table-column>
                </el-table>
            </section>
        </el-dialog>
    </div>
</template>
<style scoped>
</style>
<script>
    import $ from 'jquery'
    import rbCmdbModuleServiceFactory from 'src/services/cmdb/rb-cmdb-module-service.factory.js'
    import rbCmdbServiceFactory from 'src/services/cmdb/rb-cmdb-service.factory.js'
    import { filterVueTree } from 'src/assets/js/utility/rb-filters.js'

    import Treeselect from '@riophae/vue-treeselect'
    import '@riophae/vue-treeselect/dist/vue-treeselect.css'
    export default {
        props: ['showImport', 'importType', 'moduleType', 'moduleId', 'catalogId'],
        components: {
            Treeselect
        },
        data() {
            return {
                loading: false,
                loading_text: '',
                showImportDialog: this.showImport,
                showImportPanel: this.showImport,
                showProcessPanel: false,
                deviceClass: '',
                newModuleId: null,
                selectData: [],
                childData: [],
                isShowItem: false,
                showErrorList: false,
                showError: false,
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
                showErrorDown: false,
                moduleLists: [],
                actionParams: {},
                useModuleName: '',
                errorList: []
            }
        },
        mounted: function () {
            $(document).find('input[type=file]:eq(0)').hide()
            this.headers.Authorization = 'Bearer ' + sessionStorage.getItem('token')
            if (this.importType !== 'targetHost') {
                this.initData()
            }
            this.actionParams.moduleId = this.moduleId ? this.moduleId : ''
            this.actionParams.moduleType = this.moduleType ? this.moduleType : ''
            this.actionParams.operatorUser = sessionStorage.getItem('username')
            console.log('module_id -------->' + this.moduleId)
            if (this.moduleId) {
                // 提示当前使用的导入模板
                rbCmdbModuleServiceFactory.getModuleDetail({ 'moduleId': this.moduleId }).then((data) => {
                    if (data) {
                        this.$confirm('系统将使用<span style="color:red;">' + data.name + '</span>模板导入Excel数据, 请确认模板是否正确?',
                            '确认提示',
                            {
                                confirmButtonText: '确定',
                                cancelButtonText: '取消',
                                dangerouslyUseHTMLString: true
                            }).then(() => { }).catch(() => {
                                this.closeImportDialog()
                            })
                    }
                })
            } else {
                if (this.moduleType) {
                    if (this.moduleType === 'host') {
                        this.useModuleName = '主机资源'
                    }
                    if (this.moduleType === 'business') {
                        this.useModuleName = '业务系统'
                    }
                    if (this.moduleType === 'dict') {
                        this.useModuleName = '数据模型'
                    }
                    this.$confirm('系统将使用<span style="color:red;">' + this.useModuleName + '</span>模板导入Excel数据, 请确认模板是否正确?',
                        '确认提示',
                        {
                            confirmButtonText: '确定',
                            cancelButtonText: '取消',
                            dangerouslyUseHTMLString: true
                        }).then(() => { }).catch(() => {
                            this.closeImportDialog()
                        })
                }
            }

        },
        beforeDestroy: function () {
            clearInterval(this.timeSetter)
            this.timeSetter = null
            this.closeImportDialog()
        },
        methods: {
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
            downloadTem() {
                if (this.importType === 'targetHost') {
                    rbCmdbServiceFactory.downloadTargetHostTemplate().then(res => {
                        let blob = new Blob([res], { type: 'application/msword' })
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
                } else {
                    this.isShowItem = true
                }
            },
            UploadFile(param) {
                if (this.importType === 'targetHost') {
                    const formData = new FormData()
                    formData.append('file', param.file)
                    rbCmdbServiceFactory.importTargetHost(formData).then(res => {
                        if (res.flag === 'true') {
                            if (res.errorHostList.length > 0) {
                                this.failCount = res.errorHostList.length
                                this.showErrorList = true
                                this.errorList = []
                                this.errorList = res.errorHostList
                            }
                            if (res.successHostList.length > 0) {
                                this.successCount = res.successHostList.length
                                this.$emit('successHostList', res.successHostList)
                            }
                            this.totalCount = this.successCount + this.failCount
                            this.showProcessPanel = true
                            this.showCancel = false
                            this.showFinish = true
                        }
                    })

                }
            },
            importErrorList() {
                this.showError = true
            },
            // 提交表单
            onSubmit() {
                let _this = this
                _this.loading = true
                _this.loading_text = '正在下载模板, 请耐心等待...'
                rbCmdbServiceFactory.downloadExcel(this.newModuleId).then((data) => {
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
                }).finally(() => {
                    _this.loading = false
                    _this.loading_text = ''
                })
            },
            initData() {
                this.moduleLists = []
                // 获取默认的主机分类列表
                let _this = this
                _this.loading = true
                _this.loading_text = '正在加载下载模板列表, 请耐心等待...'
                rbCmdbModuleServiceFactory.getModuleTreeByCatalogIdOrModuleId({ 'catalogId': this.catalogId, 'moduleId': this.moduleId }).then((data) => {
                    if (data && data.length > 0) {
                        this.moduleLists = filterVueTree(data, {
                            id: 'id',
                            label: 'name',
                            children: 'childModules',
                            isDisabled: (item) => this.filterDisabled(item),
                            isDisplay: 'isDisplay',
                            autoAddRoot: false
                        })
                    }
                }).catch((e) => {
                    console.error(e)
                }).finally(() => {
                    _this.loading = false
                    _this.loading_text = ''
                })
            },
            filterDisabled(item) {
                if (item.isDisplay && item.isDisplay === 1) {
                    return true
                }
                if (item.childModules && item.childModules.length > 0) {
                    return true
                }
                return false
            },
            resetForm(formName) {
                this.$refs[formName].resetFields()
            },
            handleSuccess(response) {
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
            confirmSubmit(file) {
                return this.validFile(file)
            },
            validFile(file) {
                console.log(file)
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
                        if (this.$parent.viewCodeName) {
                            let queryDatas = {
                                type: 'CMDB_VIEW',
                                key: this.$parent.viewCodeName
                            }
                            rbCmdbServiceFactory.refreshViewsTree(queryDatas).then((res) => {
                                if (res.success) {
                                    this.$parent.query()
                                    this.$emit('handleRefresh')
                                    this.$parent.$parent.initTree()
                                } else {
                                    this.$message.error(res.message)
                                }
                            })
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
                                _t.closeImportDialog()
                            }
                        }).catch(() => {
                            _t.$message.error('移除导入进程' + _t.processId + '失败')
                        })
                    }
                })
            },
            downImportErrorRecord() {
                let _this = this
                _this.loading = true
                _this.loading_text = '正在导出失败记录, 请耐心等待...'
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
                }).finally(() => {
                    _this.loading = false
                    _this.loading_text = ''
                })
            }
        }
    }
</script>
<style lang="scss" scoped>
</style>
