<template>
    <div class="components-container yw-dashboard"
         v-loading="loading"
         :element-loading-text="loading_text">
        <el-collapse v-model="activeName"
                     accordion>
            <el-collapse-item title="数据导入模块"
                              name="1">
                <el-form class="components-condition yw-form"
                         :inline="true"
                         label-position="left"
                         label-width="85px">
                    <el-form-item label="模板名称">
                        <el-select v-model="search.templateName"
                                   @change="selectChanged"
                                   placeholder="请选择"
                                   filterable
                                   clearable>
                            <el-option v-for="(item, index) in templateList"
                                       :key="index"
                                       :label="item.value"
                                       :value="item.name"></el-option>
                        </el-select>
                    </el-form-item><br>
                    <el-form-item label="系统标题"
                                  v-show="visable.systemTitle">
                        <el-select v-model="search.systemTitle"
                                   placeholder="请选择"
                                   filterable
                                   clearable>
                            <el-option v-for="(item, index) in systemTitleList"
                                       :key="index"
                                       :label="item.value"
                                       :value="item.name"></el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="设备类型"
                                  v-show="visable.deviceType">
                        <el-select v-model="search.deviceType"
                                   placeholder="请选择"
                                   filterable
                                   clearable>
                            <el-option v-for="(item, index) in deviceTypeList"
                                       :key="index"
                                       :label="item.name"
                                       :value="item.value"></el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="硬件类型"
                                  v-show="visable.hardwareType">
                        <el-select v-model="search.hardwareType"
                                   placeholder="请选择"
                                   filterable
                                   clearable>
                            <el-option v-for="(item, index) in hardwareTypeList"
                                       :key="index"
                                       :label="item.name"
                                       :value="item.value"></el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="月报时间"
                                  v-show="visable.monthlyTime">
                        <el-date-picker v-model="search.monthlyTime"
                                        style="width: 120px"
                                        type="month"
                                        value-format="yyyy-MM"
                                        :clearable="false"
                                        placeholder="年-月"></el-date-picker>
                    </el-form-item>
                </el-form>
                <section class="btn-wrap">
                    <el-button type="primary"
                               @click="dataImport()">导入</el-button>
                    <el-button @click="dataReset()">重置</el-button>
                </section>
            </el-collapse-item>
            <el-collapse-item title="数据校验模板"
                              name="2">
                <el-form class="components-condition yw-form"
                         :inline="true"
                         label-width="85px">
                    <el-form-item label="校验类型">
                        <el-select v-model="validate.validateType"
                                   placeholder="请选择"
                                   filterable>
                            <el-option v-for="(item, index) in validateTypeList"
                                       :key="index"
                                       :label="item.name"
                                       :value="item.value"></el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="系统标题">
                        <el-select v-model="validate.systemTitle"
                                   placeholder="请选择"
                                   disabled>
                            <el-option v-for="(item, index) in systemTitleList"
                                       :key="index"
                                       :label="item.value"
                                       :value="item.name"></el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="月报时间">
                        <el-date-picker v-model="validate.monthlyTime"
                                        style="width: 120px"
                                        type="month"
                                        value-format="yyyy-MM"
                                        :clearable="false"
                                        placeholder="年-月"></el-date-picker>
                    </el-form-item>
                    <el-form-item>
                        <el-button type="primary"
                                   @click="validateData">检验</el-button>
                    </el-form-item>
                </el-form>
                <el-table :data="tableData"
                          stripe
                          style="width: 100%">
                    <el-table-column prop="insertTime"
                                     label="校验时间"
                                     width="180"
                                     show-overflow-tooltip> </el-table-column>
                    <el-table-column prop="validateResult"
                                     label="校验结果"> </el-table-column>
                </el-table>
            </el-collapse-item>
            <el-collapse-item title="数据导出模块"
                              name="3">
                <el-form class="components-condition yw-form"
                         :inline="true"
                         label-width="85px">
                    <el-form-item label="导出页面">
                        <el-select v-model="exportObj.exportType"
                                   @change="exportTypeChangeSelect"
                                   placeholder="请选择"
                                   filterable>
                            <el-option v-for="(item, index) in exportTypeList"
                                       :key="index"
                                       :label="item.name"
                                       :value="item.value"></el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="租户类型"
                                  v-show="exportVisable.department1">
                        <el-select v-model="exportObj.department1"
                                   @change="changeTenantType"
                                   placeholder="请选择"
                                   filterable>
                            <el-option v-for="(item, index) in tenantTypeList"
                                       :key="index"
                                       :label="item.name"
                                       :value="item.value"></el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="部门名称"
                                  v-show="exportVisable.department2">
                        <el-select v-model="exportObj.department2"
                                   placeholder="请选择"
                                   filterable>
                            <el-option v-for="(item, index) in departmentList"
                                       :key="index"
                                       :label="item.name"
                                       :value="item.value"></el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="资源池"
                                  v-show="exportVisable.idcType">
                        <el-select v-model="exportObj.idcType"
                                   placeholder="请选择"
                                   filterable>
                            <el-option v-for="(item, index) in idcTypeList"
                                       :key="index"
                                       :label="item.name"
                                       :value="item.value"></el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="是否排除[基础平台部]数据"
                                  v-show="exportVisable.isExclude"
                                  label-width="170px">
                        <el-switch v-model="exportObj.isExclude"
                                   active-text="是"
                                   inactive-text="否">
                        </el-switch>
                    </el-form-item>
                    <el-form-item label="月报时间">
                        <el-date-picker v-model="exportObj.monthlyTime"
                                        style="width: 120px"
                                        type="month"
                                        value-format="yyyy-MM"
                                        :clearable="false"
                                        placeholder="年-月"></el-date-picker>
                    </el-form-item>
                    <el-form-item>
                        <el-button type="primary"
                                   @click="createFile">生成</el-button>
                        <el-button type="primary"
                                   @click="exportFile">导出</el-button>
                    </el-form-item>
                </el-form>
            </el-collapse-item>
        </el-collapse>
        <YwImport v-if="importObject.isVisable"
                  :showImport="importObject.isVisable"
                  @setImportDisplay="setShowImportProject"
                  :importType="importObject.importType"
                  :screenParams="importObject.screenParams"></YwImport>
    </div>
</template>
<script>
    import screenService from 'src/services/screen/rb-screen-services.factory.js'
    export default {
        components: {
            YwImport: () => import('src/components/common/yw-import.vue'),
        },
        data() {
            return {
                activeName: '1',
                tableData: [],
                loading: false,
                loading_text: '加载中',
                // 资源池
                idcTypeList: [],
                // IT租户
                itData: [],
                // 非IT租户
                noItData: [],
                departmentList: [],
                // 租户类型
                tenantTypeList: [{
                    name: '内部租户',
                    value: 'it'
                }, {
                    name: '外部租户',
                    value: 'noIt'
                }],
                templateList: [{
                    name: 'screen_resource_allocate',
                    value: '资源分配总量导入模板'
                }, {
                    name: 'screen_max_utilization',
                    value: '资源均峰值导入模板'
                }, {
                    name: 'screen_avg_utilization',
                    value: '资源平均值导入模板'
                }, {
                    name: 'bs_not_inspect',
                    value: '业务系统详细信息导入模板'
                }, {
                    name: 'store_resource_low_utilization',
                    value: '存储资源低利用率导入模板'
                }, {
                    name: 'bs_online_info',
                    value: '业务系统上线信息导入模板'
                }],
                systemTitleList: [{
                    name: '1',
                    value: 'IT云资源利用情况'
                }],
                deviceTypeList: [{
                    name: '物理机',
                    value: '物理机'
                }, {
                    name: '虚拟机',
                    value: '虚拟机'
                }],
                hardwareTypeList: [{
                    name: 'CPU',
                    value: 'CPU'
                }, {
                    name: '内存',
                    value: '内存'
                }],
                // 导入参数
                search: {
                    templateName: '',
                    systemTitle: '',
                    deviceType: '',
                    hardwareType: '',
                    monthlyTime: ''
                },
                // 校验参数
                validate: {
                    systemTitle: '1',
                    validateType: 'IsImport',
                    monthlyTime: ''
                },
                // 导出参数
                exportObj: {
                    exportType: 'overview',
                    department1: 'it',
                    department2: '',
                    idcType: '',
                    isExclude: false,
                    monthlyTime: ''
                },
                // 导出模块的可视化
                exportVisable: {
                    department1: false,
                    department2: false,
                    idcType: false,
                    isExclude: true
                },
                visable: {
                    systemTitle: false,
                    deviceType: false,
                    hardwareType: false,
                    monthlyTime: false
                },
                importObject: {
                    isVisable: false,
                    importType: '',
                    screenParams: {}
                },
                validateTypeList: [{
                    name: '1.数据是否导入',
                    value: 'IsImport'
                }, {
                    name: '2.数据是否完整',
                    value: 'IsComplete'
                }, {
                    name: '3.数据是否正确',
                    value: 'IsCorrect'
                }],
                exportTypeList: [{
                    name: '租户运营月报',
                    value: 'tenant'
                }, {
                    name: '租户运营总览',
                    value: 'overview'
                }, {
                    name: '一级资源池运营',
                    value: 'idcType'
                }],
                exportFileName: '导出文件'
            }
        },
        mounted: function () {
            this.getCurrentMonthlyTime()
            this.listRecordList()
            this.getIdcTypeList()
            this.getTenantData()
        },
        methods: {
            // 文件导出
            exportFile() {
                const reqBody = this.transExportReqParam(this.exportObj)
                screenService.exportFile(reqBody).then((res) => {
                    if (res.flag) {
                        let downLoadElement = document.createElement('a')
                        downLoadElement.href = res.msg
                        downLoadElement.setAttribute('download', this.exportFileName + '.xlsx')
                        document.body.appendChild(downLoadElement)
                        downLoadElement.click()
                        document.body.removeChild(downLoadElement)
                    } else {
                        this.$message.error(res.msg)
                    }
                })
            },
            // 租户类型切换
            changeTenantType(val) {
                this.exportObj.department2 = ''
                if (val == 'it') {
                    this.departmentList = this.itData
                    this.exportObj.department2 = this.itData[0].value
                } else if (val == 'noIt') {
                    this.departmentList = this.noItData
                    this.exportObj.department2 = this.noItData[0].value
                } else {
                    this.departmentList = []
                }
            },
            // 导出选中不同模板做不同参数处理，以及不同的选择框
            exportTypeChangeSelect(val) {
                var visable = {}
                if (val == 'tenant') {
                    visable = {
                        department1: true,
                        department2: true,
                        idcType: false,
                        isExclude: false
                    }
                } else if (val == 'idcType') {
                    visable = {
                        department1: false,
                        department2: false,
                        idcType: true,
                        isExclude: false
                    }
                } else {
                    visable = {
                        isExclude: true
                    }
                }
                this.exportVisable = visable
            },
            // 处理导出模块的参数
            transExportReqParam(exportObj) {
                var param = {}
                if (exportObj.exportType == 'tenant') {
                    var department1, department2
                    if (exportObj.department1 == 'it') {
                        department1 = '中移信息公司'
                        department2 = exportObj.department2
                        this.exportFileName = '租户运营月报-' + department1 + '-' + department2 + '-' + exportObj.monthlyTime
                    } else {
                        department1 = exportObj.department2
                        department2 = null
                        this.exportFileName = '租户运营月报-' + department1 + '-' + exportObj.monthlyTime
                    }
                    param = {
                        exportType: exportObj.exportType,
                        department1: department1,
                        department2: department2,
                        monthlyTime: exportObj.monthlyTime
                    }
                } else if (exportObj.exportType == 'overview') {
                    param = {
                        exportType: exportObj.exportType,
                        monthlyTime: exportObj.monthlyTime
                    }
                    if (exportObj.isExclude) {
                        param.excludeDep = '基础平台部'
                        this.exportFileName = '租户运营(总览)-' + '排除[基础平台部]-' + param.monthlyTime
                    } else {
                        this.exportFileName = '租户运营(总览)-' + param.monthlyTime
                    }

                } else if (exportObj.exportType == 'idcType') {
                    param = {
                        exportType: exportObj.exportType,
                        idcType: exportObj.idcType,
                        monthlyTime: exportObj.monthlyTime
                    }
                    this.exportFileName = '一级资源池运营-' + param.idcType + '-' + param.monthlyTime
                }
                return param
            },
            // 生成文件
            createFile() {
                const reqBody = this.transExportReqParam(this.exportObj)
                screenService.createFile(reqBody).then((res) => {
                    console.info(res)
                    var type = res.flag ? 'success' : 'error'
                    this.$message({
                        message: res.msg,
                        type: type
                    })
                })
            },
            // 获取当前月报时间2020-03格式
            getCurrentMonthlyTime() {
                const today = new Date()
                var year = today.getFullYear()
                var month = (today.getMonth() + 1) < 10 ? '0' + (today.getMonth() + 1) : (today.getMonth() + 1) + ''
                this.validate.monthlyTime = year + '-' + month
                this.exportObj.monthlyTime = year + '-' + month
            },
            // 数据验证
            validateData() {
                this.loading = true
                this.loading_text = '正在验证中...请稍后'
                screenService.validateDataByType(this.validate).then((res) => {
                    var type = res.flag ? 'success' : 'error'
                    this.$message({
                        message: res.msg,
                        type: type
                    })
                    this.listRecordList()
                    this.loading = false
                })
            },
            // 获取验证记录列表
            listRecordList() {
                this.loading = true
                this.loading_text = '正在查询数据...'
                screenService.listValidateRecord({}).then((res) => {
                    this.tableData = res
                    this.loading = false
                })
            },
            setShowImportProject(val) {
                this.importObject.isVisable = val
            },
            // 导入方法
            dataImport() {
                this.importObject.isVisable = true
                this.importObject.importType = this.search.templateName
                this.importObject.screenParams = this.search
            },
            // 重置
            dataReset() {
                let search = {
                    templateName: this.search.templateName,
                    systemTitle: '',
                    deviceType: '',
                    hardwareType: '',
                    monthlyTime: ''
                }
                this.search = search
            },
            // select 值改变触发的方法
            selectChanged(val) {
                this.dataReset()
                let visable = {}
                if (val == 'screen_resource_allocate' || val == 'bs_online_info' || val == 'store_resource_low_utilization') {
                    visable = {
                        systemTitle: true,
                        deviceType: false,
                        hardwareType: false,
                        monthlyTime: true
                    }
                } else if (val == 'screen_max_utilization' || val == 'screen_avg_utilization') {
                    visable = {
                        systemTitle: true,
                        deviceType: true,
                        hardwareType: true,
                        monthlyTime: true
                    }
                } else if (val == 'bs_not_inspect') {
                    visable = {
                        systemTitle: true,
                        deviceType: true,
                        hardwareType: false,
                        monthlyTime: true
                    }
                }
                this.visable = visable
            },
            // 获取资源池数据
            getIdcTypeList() {
                screenService.getDictDataByType({ type: 'month_report_idctype' }).then((res) => {
                    var idcList = []
                    for (var i = 0; i < res.length; i++) {
                        if (res[i].name == '总览') {
                            idcList.push({
                                name: res[i].name,
                                value: ''
                            })
                        } else {
                            idcList.push({
                                name: res[i].name,
                                value: res[i].value
                            })
                        }
                    }
                    this.idcTypeList = idcList
                })
            },
            // 获取租户名称
            getTenantData() {
                var it = []
                var notIt = []
                screenService.getTenantData({}).then((res) => {
                    if (res[0].children) {
                        res[0].children.forEach((item) => {
                            if (item.orgName.trim() === '中移信息公司') {
                                item.children.forEach((item2) => {
                                    it.push({
                                        name: item2.orgName,
                                        value: item2.orgName
                                    })
                                })
                            } else {
                                notIt.push({
                                    name: item.orgName,
                                    value: item.orgName
                                })
                            }
                        })
                    }
                    this.itData = it
                    this.noItData = notIt
                    this.departmentList = it
                    this.exportObj.department2 = it[0].value
                })
            }
        }
    }
</script>
<style scoped>
</style>