<template>
    <!--  机柜结算管理 -->
    <div class="cabinet-settlement-management">
        <nav-tab :tabsType="tabsType"
                 @changeTabs="changeTabs"></nav-tab>
        <div class="cabinet-settlement-management-search">
            <el-form ref="computerForm"
                     v-show="!isServerCabinet"
                     :model="computerForm"
                     inline
                     label-width="100px">
                <el-form-item prop="billing_start_date"
                              label="计费时间"
                              class="time-range">
                    <el-date-picker v-model="computerForm.billing_start_date"
                                    type="daterange"
                                    format="yyyy-MM-dd"
                                    value-format="yyyy-MM-dd"
                                    clearable
                                    range-separator="至"
                                    start-placeholder="开始日期"
                                    end-placeholder="结束日期">
                    </el-date-picker>
                </el-form-item>
                <el-form-item label="资源池"
                              prop="idcType">
                    <el-select v-model="computerForm.idcType"
                               clearable
                               filterable
                               placeholder="请选择">
                        <el-option v-for="(item,index) in idcTypeOptions"
                                   :key="index"
                                   :label="item.value"
                                   :value="item.id">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="机房"
                              prop="server_room_location">
                    <el-select v-model="computerForm.server_room_location"
                               clearable
                               filterable
                               placeholder="请选择">
                        <el-option v-for="(item,index) in locationOptions"
                                   :key="index"
                                   :label="item.value"
                                   :value="item.id">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="机柜规格"
                              prop="server_cabinet_standard">
                    <el-select v-model="computerForm.server_cabinet_standard"
                               clearable
                               filterable
                               placeholder="请选择">
                        <el-option v-for="(item,index) in standardOptions"
                                   :key="index"
                                   :label="item.value"
                                   :value="item.id">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label=" "
                              class="time-range">
                    <el-button type="primary"
                               icon="el-icon-search"
                               @click="computerSearch">查询</el-button>
                    <el-button icon="el-icon-refresh-right"
                               @click="computerReset">重置</el-button>
                </el-form-item>
            </el-form>
            <el-form ref="cabinetForm"
                     v-show="isServerCabinet"
                     :model="cabinetForm"
                     inline
                     label-width="100px">
                <el-form-item label="所属项目"
                              prop="project_belong_to">
                    <el-select v-model="cabinetForm.project_belong_to"
                               clearable
                               filterable
                               placeholder="请选择">
                        <el-option v-for="(item,index) in projectOptions"
                                   :key="index"
                                   :label="item.value"
                                   :value="item.id">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="资源池"
                              prop="idcType">
                    <el-select v-model="cabinetForm.idcType"
                               clearable
                               filterable
                               placeholder="请选择">
                        <el-option v-for="(item,index) in idcTypeOptions"
                                   :key="index"
                                   :label="item.value"
                                   :value="item.id">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item prop="first_online_date"
                              label="首次加电时间"
                              class="time-range">
                    <el-date-picker v-model="cabinetForm.first_online_date"
                                    type="daterange"
                                    format="yyyy-MM-dd"
                                    value-format="yyyy-MM-dd"
                                    clearable
                                    range-separator="至"
                                    start-placeholder="开始日期"
                                    end-placeholder="结束日期">
                    </el-date-picker>
                </el-form-item>
                <el-form-item label="机房"
                              prop="server_room_location">
                    <el-select v-model="cabinetForm.server_room_location"
                               clearable
                               filterable
                               placeholder="请选择">
                        <el-option v-for="(item,index) in locationOptions"
                                   :key="index"
                                   :label="item.value"
                                   :value="item.id">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="机柜编码"
                              prop="server_cabinet_code">
                    <el-input v-model="cabinetForm.server_cabinet_code"
                              clearable
                              placeholder="请输入"></el-input>
                </el-form-item>
                <el-form-item label="机柜规格"
                              prop="server_cabinet_standard">
                    <el-select v-model="cabinetForm.server_cabinet_standard"
                               clearable
                               filterable
                               placeholder="请选择">
                        <el-option v-for="(item,index) in standardOptions"
                                   :key="index"
                                   :label="item.value"
                                   :value="item.id">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item prop="offline_date"
                              label="下架时间"
                              class="time-range">
                    <el-date-picker v-model="cabinetForm.offline_date"
                                    type="daterange"
                                    format="yyyy-MM-dd"
                                    value-format="yyyy-MM-dd"
                                    clearable
                                    range-separator="至"
                                    start-placeholder="开始日期"
                                    end-placeholder="结束日期">
                    </el-date-picker>
                </el-form-item>
                <el-form-item label="是否已加电"
                              prop="server_online_status">
                    <el-select v-model="cabinetForm.server_online_status"
                               clearable
                               placeholder="请选择">
                        <el-option v-for="(item,index) in statusOptions"
                                   :key="index"
                                   :label="item.value"
                                   :value="item.id">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item prop="server_bill_year"
                              class="bill-year"
                              label="机柜结算年份">
                    <el-date-picker v-model="cabinetForm.server_bill_year"
                                    type="year"
                                    format="yyyy"
                                    value-format="yyyy"
                                    clearable
                                    placeholder="请选择年份">
                    </el-date-picker>
                </el-form-item>
                <el-form-item label=" "
                              class="time-range">
                    <el-button type="primary"
                               icon="el-icon-search"
                               @click="cabinetSearch">查询</el-button>
                    <el-button icon="el-icon-refresh-right"
                               @click="cabinetReset">重置</el-button>
                </el-form-item>
            </el-form>
        </div>
        <div class="computer-table">
            <div class="yw-table">
                <div class="computer-table-button"
                     v-show="!isServerCabinet">
                    <el-button icon="el-icon-plus"
                               type="primary"
                               @click="addOperation"
                               v-if="buttonPermission.create">新增</el-button>
                    <el-button icon="el-icon-edit"
                               type="primary"
                               @click="editOperation"
                               v-if="buttonPermission.update">修改</el-button>
                    <el-button icon="el-icon-delete"
                               type="primary"
                               @click="toDelete"
                               v-if="buttonPermission.delete">删除</el-button>
                    <el-button icon="el-icon-upload"
                               type="primary"
                               @click="uploadFile"
                               v-if="buttonPermission.import">导入</el-button>
                    <el-button icon="el-icon-download"
                               @click="exportData"
                               v-if="buttonPermission.export">导出</el-button>
                </div>
                <div class="computer-table-button"
                     v-show="isServerCabinet">
                    <el-button icon="el-icon-download"
                               @click="exportData"
                               v-if="buttonPermission.export">导出</el-button>
                </div>
                <div class="yw-el-table-wrap"
                     v-loading="loading"
                     style="margin-top:10px;">
                    <el-table class="yw-el-table"
                              :data="collectionData.tableBody"
                              style="width:100%;cursor: pointer;"
                              stripe
                              border
                              tooltip-effect="dark"
                              height="calc(100vh - 340px)"
                              @selection-change="handleSelectionChange">
                        <el-table-column type="selection"
                                         width="55">
                        </el-table-column>
                        <el-table-column v-for="(item,index) in collectionData.tableHeader"
                                         :prop="item.value"
                                         :sortable="item.sortable"
                                         :label="item.label"
                                         :width="item.width"
                                         :show-overflow-tooltip="true"
                                         :key="index"> </el-table-column>
                    </el-table>
                </div>
                <div class="yw-page-wrap"
                     v-if="collectionData.pagination.total>0">
                    <el-pagination @size-change="handleSizeChange"
                                   @current-change="handleCurrentChange"
                                   :current-page="collectionData.pagination.currentPage"
                                   :page-sizes="collectionData.pageSizes"
                                   :page-size="collectionData.pagination.pageSize"
                                   :total="collectionData.pagination.total"
                                   layout="total, sizes, prev, pager, next, jumper">
                    </el-pagination>
                </div>
            </div>
        </div>
        <!-- 新增操作 -->
        <operate-dialog v-if="showOperation"
                        :operationParams="operationParams"
                        @closeOperateDialog="closeOperateDialog"></operate-dialog>
        <importInstances ref="importInstances"
                         v-if="display.isImport"
                         :showImport="display.isImport"
                         @setImportDisplay="setImportDisplay"
                         :importType="importType"
                         :moduleType="moduleType"
                         :moduleId="moduleId"></importInstances>
    </div>
</template>
<script>
    import rbCmdbServiceFactory from 'src/services/cmdb/rb-cmdb-service.factory.js'
    import rbCmdbModuleServiceFactory from 'src/services/cmdb/rb-cmdb-module-service.factory'
    import myMixin from 'src/pages/resource/iframe/computerManagement/buttonPermission.js'
    export default {
        name: 'ResourceIframeServerCabinetBill',
        mixins: [myMixin],
        components: {
            navTab: () => import('src/pages/resource/iframe/computerManagement/navTab.vue'),
            operateDialog: () => import('src/pages/resource/iframe/computerManagement/cabinetBillDialog.vue'),
            importInstances: () => import('src/pages/cmdb/v2/instance/import/import-instance.vue')
        },
        data() {
            return {
                tabsType: {
                    name1: '机柜结算',
                    name2: '机柜结算配置',
                    index1: 3,
                    index2: 4
                },
                isServerCabinet: true,
                computerForm: {
                    billing_start_date: '',
                    idcType: '',
                    server_room_location: '',
                    server_cabinet_standard: ''
                },
                cabinetForm: {
                    server_bill_year: '',
                    idcType: '',
                    first_online_date: '',
                    server_room_location: '',
                    server_cabinet_code: '',
                    server_cabinet_standard: '',
                    offline_date: '',
                    server_online_status: '',
                    project_belong_to: ''
                },
                idcTypeOptions: [],
                locationOptions: [],
                standardOptions: [],
                statusOptions: [],
                projectOptions: [],
                optTypeOptions: [],
                collectionData: {
                    tableHeader: [],
                    tableBody: [],
                    pagination: {
                        currentPage: 1,
                        pageSize: 20,
                        total: 0
                    },
                    pageSizes: [10, 20, 50, 100]
                },
                tableHeader1: [
                    {
                        label: '结算年份',
                        value: 'server_bill_year',
                        sortable: false
                    },
                    {
                        label: '资源池',
                        value: 'idcType_dict_note_name',
                        sortable: false,
                        width: '140'
                    },
                    {
                        label: '机房',
                        value: 'server_room_location_dict_note_name',
                        sortable: false
                    },
                    {
                        label: '机柜编码',
                        value: 'server_cabinet_code',
                        sortable: false
                    },
                    {
                        label: '机柜规格',
                        value: 'server_cabinet_standard_dict_note_name',
                        sortable: false
                    },
                    {
                        label: '是否已加电',
                        value: 'server_online_status_dict_note_name',
                        sortable: false,
                        width: '100'
                    },
                    {
                        label: '加电设备台数',
                        value: 'online_count',
                        sortable: false,
                        width: '120'
                    },
                    {
                        label: '所属项目',
                        value: 'project_belong_to_dict_note_name',
                        sortable: false,
                        width: '120'
                    },
                    {
                        label: '所属业务',
                        value: 'business_belong_to',
                        sortable: false
                    },
                    {
                        label: '1月单价',
                        value: 'bill_month_unit1',
                        sortable: false
                    },
                    {
                        label: '1月计费',
                        value: 'bill_month_total1',
                        sortable: false
                    },
                    {
                        label: '2月单价',
                        value: 'bill_month_unit2',
                        sortable: false
                    },
                    {
                        label: '2月计费',
                        value: 'bill_month_total2',
                        sortable: false
                    },
                    {
                        label: '3月单价',
                        value: 'bill_month_unit3',
                        sortable: false
                    },
                    {
                        label: '3月计费',
                        value: 'bill_month_total3',
                        sortable: false
                    },
                    {
                        label: '4月单价',
                        value: 'bill_month_unit4',
                        sortable: false
                    },
                    {
                        label: '4月计费',
                        value: 'bill_month_total4',
                        sortable: false
                    },
                    {
                        label: '5月单价',
                        value: 'bill_month_unit5',
                        sortable: false
                    },
                    {
                        label: '5月计费',
                        value: 'bill_month_total5',
                        sortable: false
                    },
                    {
                        label: '6月单价',
                        value: 'bill_month_unit6',
                        sortable: false
                    },
                    {
                        label: '6月计费',
                        value: 'bill_month_total6',
                        sortable: false
                    },
                    {
                        label: '7月单价',
                        value: 'bill_month_unit7',
                        sortable: false
                    },
                    {
                        label: '7月计费',
                        value: 'bill_month_total7',
                        sortable: false
                    },
                    {
                        label: '8月单价',
                        value: 'bill_month_unit8',
                        sortable: false
                    },
                    {
                        label: '8月计费',
                        value: 'bill_month_total8',
                        sortable: false
                    },
                    {
                        label: '9月单价',
                        value: 'bill_month_unit9',
                        sortable: false
                    },
                    {
                        label: '9月计费',
                        value: 'bill_month_total9',
                        sortable: false
                    },
                    {
                        label: '10月单价',
                        value: 'bill_month_unit10',
                        sortable: false
                    },
                    {
                        label: '10月计费',
                        value: 'bill_month_total10',
                        sortable: false
                    },
                    {
                        label: '11月单价',
                        value: 'bill_month_unit11',
                        sortable: false
                    },
                    {
                        label: '11月计费',
                        value: 'bill_month_total11',
                        sortable: false
                    },
                    {
                        label: '12月单价',
                        value: 'bill_month_unit12',
                        sortable: false
                    },
                    {
                        label: '12月计费',
                        value: 'bill_month_total12',
                        sortable: false
                    }
                ],
                tableHeader2: [
                    {
                        label: '合同',
                        value: 'contract_no',
                        sortable: false,
                        width: '140'
                    },
                    {
                        label: '资源池',
                        value: 'idcType_dict_note_name',
                        sortable: false,
                        width: '140'
                    },
                    {
                        label: '机房',
                        value: 'server_room_location_dict_note_name',
                        sortable: false
                    },
                    {
                        label: '机柜规格',
                        value: 'server_cabinet_standard_dict_note_name',
                        sortable: false
                    },
                    {
                        label: '单价（不含税）',
                        value: 'contractor_price',
                        sortable: false
                    },
                    {
                        label: '计费开始日期',
                        value: 'billing_start_date',
                        sortable: false,
                        width: '140'
                    },
                    {
                        label: '计费截止时间',
                        value: 'bill_end_time',
                        sortable: false,
                        width: '140'
                    },
                    {
                        label: '备注',
                        value: 'remark',
                        sortable: false
                    }
                ],
                multipleSelection: [],
                showOperation: false,
                operationParams: {
                    isEdit: false,
                    editDatas: null
                },
                loading: false,
                display: {
                    isImport: false,
                    isEdit: false,
                    isSearchPlane: false,
                    isBatchUpdate: false
                },
                importType: 'ip_repository',
                moduleType: 'host',
                moduleId: this.$route.query.module_id || 'b1d6fd38c19c46d6bf9168c3db4c1e26'
            }
        },
        created() {
            this.$set(this.collectionData, 'tableHeader', this.tableHeader1)
            this.getSelectCodeId()
            this.getTableList()
        },
        methods: {
            getSelectCodeId() {
                let moduleId = '73361bce181545f59edcd529366fadf7'
                rbCmdbServiceFactory.getInstanceHeader(moduleId).then((res) => {
                    let selectObj = {
                        idcType: '',
                        server_room_location: '',
                        server_cabinet_standard: '',
                        server_online_status: '',
                        project_belong_to: '',
                    }
                    for (let prop in selectObj) {
                        selectObj[prop] = (res.filter(item => item.filedCode == prop))[0].codeId
                    }
                    for (let item in selectObj) {
                        let params = {
                            codeId: selectObj[item]
                        }
                        rbCmdbModuleServiceFactory.getRefModuleDict({ params: params }).then((res) => {
                            switch (item) {
                                case 'idcType':
                                    this.idcTypeOptions = res
                                    this.$set(this.operationParams, 'idcTypeOptions', res)
                                    break
                                case 'server_room_location':
                                    this.locationOptions = res
                                    this.$set(this.operationParams, 'locationOptions', res)
                                    break
                                case 'server_cabinet_standard':
                                    this.standardOptions = res
                                    this.$set(this.operationParams, 'standardOptions', res)
                                    break
                                case 'server_online_status':
                                    this.statusOptions = res
                                    break
                                case 'project_belong_to':
                                    this.projectOptions = res
                                    break
                            }
                        })
                    }
                })
            },
            async getTableList(type) {
                this.loading = true
                let queryData = {}
                if (type) {
                    this.collectionData.pagination.currentPage = 1
                }
                if (!this.isServerCabinet) {
                    for (let prop in this.computerForm) {
                        if (this.computerForm[prop] && this.computerForm[prop] != 0) {
                            if (prop === 'billing_start_date') {
                                queryData[prop] = this.computerForm[prop].join(',')
                            } else {
                                queryData[prop] = this.computerForm[prop]
                            }
                        }

                    }
                    queryData.condicationCode = this.$route.query.condicationCode || 'server_cabinet_bill_conf'
                    queryData.module_id = this.$route.query.module_id || 'b1d6fd38c19c46d6bf9168c3db4c1e26'
                } else {
                    for (let prop in this.cabinetForm) {
                        if (this.cabinetForm[prop] && this.cabinetForm[prop] != 0) {
                            if (prop === 'first_online_date' || prop === 'offline_date') {
                                queryData[prop] = this.cabinetForm[prop].join(',')
                            } else {
                                queryData[prop] = this.cabinetForm[prop]
                            }
                        }

                    }
                    queryData.condicationCode = 'server_cabinet_bill'
                    queryData.module_id = '73361bce181545f59edcd529366fadf7'
                }
                queryData.pageSize = this.collectionData.pagination.pageSize
                queryData.pageNo = this.collectionData.pagination.currentPage
                this.collectionData.tableBody = []
                this.collectionData.pagination.total = 0
                try {
                    let res = await rbCmdbServiceFactory.getInstanceList(queryData)
                    this.collectionData.tableBody = res.data || []
                    this.collectionData.pagination.total = res.totalSize
                } catch (error) {
                    this.$message({
                        message: error.data.errors[0].message,
                        type: 'error'
                    })
                } finally {
                    this.loading = false
                }
            },
            changeTabs(data) {
                if (data === 3) {
                    this.isServerCabinet = true
                    this.$set(this.collectionData, 'tableHeader', this.tableHeader1)
                } else {
                    this.isServerCabinet = false
                    this.$set(this.collectionData, 'tableHeader', this.tableHeader2)
                }
                this.collectionData.pagination.pageSize = 20
                this.collectionData.pagination.currentPage = 1
                this.getTableList()
            },
            computerSearch() {
                this.$refs['computerForm'].validate((valid) => {
                    if (valid) {
                        this.getTableList('find')
                    } else {
                        return false
                    }
                })
            },
            computerReset() {
                this.$refs['computerForm'].resetFields()
                this.getTableList('find')
            },
            cabinetSearch() {
                this.$refs['cabinetForm'].validate((valid) => {
                    if (valid) {
                        this.getTableList('find')
                    } else {
                        return false
                    }
                })
            },
            cabinetReset() {
                this.$refs['cabinetForm'].resetFields()
                this.getTableList('find')
            },
            addOperation() {
                this.showOperation = true
                this.$set(this.operationParams, 'isEdit', false)
                this.$set(this.operationParams, 'editDatas', null)
            },
            editOperation() {
                if (this.multipleSelection.length === 1) {
                    this.$set(this.operationParams, 'isEdit', true)
                    this.$set(this.operationParams, 'editDatas', { instance_id: this.multipleSelection[0].id })
                    this.showOperation = true
                } else {
                    this.$message.warning('请勾选一条需要修改的数据')
                }
            },
            toDelete() {
                if (this.multipleSelection.length <= 0) {
                    this.$alert('请选择需要删除的数据', '通知', {
                        confirmButtonText: '确定'
                    })
                    return
                }
                let _this = this
                this.$confirm('确定要删除选中的' + this.multipleSelection.length + '条记录吗?').then(() => {
                    rbCmdbServiceFactory.deleteInstance({ 'instanceList': _this.multipleSelection }).then((data) => {
                        if (data && data.success === true) {
                            this.$message({ type: 'success', message: data.message })
                            this.getTableList()
                        } else {
                            this.$message({ type: 'error', message: data.message })
                        }
                    })
                })
            },
            uploadFile() {
                this.setImportDisplay(true)
            },
            // 导入功能
            // importInstance() {
            //     this.setImportDisplay(true)
            // },
            // 设置导入窗口是否显示 true:显示 false:不显示
            setImportDisplay(val) {
                this.display.isImport = val
            },
            exportData() {
                let queryData = {}
                let moduleType = ''
                if (!this.isServerCabinet) {
                    for (let prop in this.computerForm) {
                        if (this.computerForm[prop] && this.computerForm[prop] != 0) {
                            if (prop === 'billing_start_date') {
                                queryData[prop] = this.computerForm[prop].join(',')
                            } else {
                                queryData[prop] = this.computerForm[prop]
                            }
                        }

                    }
                    queryData.condicationCode = this.$route.query.condicationCode || 'server_cabinet_bill_conf'
                    queryData.module_id = this.$route.query.module_id || 'b1d6fd38c19c46d6bf9168c3db4c1e26'
                    moduleType = this.$route.query.condicationCode || 'server_cabinet_bill_conf'
                } else {
                    for (let prop in this.cabinetForm) {
                        if (this.cabinetForm[prop] && this.cabinetForm[prop] != 0) {
                            if (prop === 'first_online_date' || prop === 'offline_date') {
                                queryData[prop] = this.cabinetForm[prop].join(',')
                            } else {
                                queryData[prop] = this.cabinetForm[prop]
                            }
                        }

                    }
                    queryData.condicationCode = 'server_cabinet_bill'
                    queryData.module_id = '73361bce181545f59edcd529366fadf7'
                    moduleType = 'server_cabinet_bill'
                }
                queryData.pageSize = this.collectionData.pagination.pageSize
                queryData.pageNo = this.collectionData.pagination.currentPage
                rbCmdbServiceFactory.exportInstanceListStream(queryData, moduleType).then((data) => {
                    console.log(data, 'data')
                    let blob = new Blob([data], { type: 'application/msword' })
                    // 创建下载链接
                    let objectUrl = URL.createObjectURL(blob)
                    let downLoadElement = document.createElement('a')
                    downLoadElement.href = objectUrl
                    if (!this.isServerCabinet) {
                        downLoadElement.download = '机柜结算配置.xlsx'
                    } else {
                        downLoadElement.download = '机柜结算汇总.xlsx'
                    }
                    document.body.appendChild(downLoadElement)
                    downLoadElement.click()
                    document.body.removeChild(downLoadElement)
                    URL.revokeObjectURL(objectUrl)
                }).catch((e) => {
                    this.$message.error('导出数量过大，请重新选择!')
                })
            },
            closeOperateDialog(status) {
                this.showOperation = status
            },
            // element相关方法 多选框
            handleSelectionChange(val) {
                this.multipleSelection = val
            },
            // 分页改变尺寸
            handleSizeChange(val) {
                this.collectionData.pagination.pageSize = val
                this.getTableList('find')
            },
            // 分页改变当前页
            handleCurrentChange(val) {
                this.collectionData.pagination.currentPage = val
                this.getTableList()
            },
        }
    }
</script>
<style lang="scss" scoped>
    .cabinet-settlement-management {
        &-search {
            background: #fff;
            box-shadow: 0px 0px 8px 0px rgba(4, 0, 0, 0.13);
            border-radius: 6px;
            padding: 10px;
            margin: 10px;
            .el-form--inline /deep/ .el-form-item__content {
                width: 170px;
            }
            .el-form--inline .time-range /deep/ .el-form-item__content {
                width: initial;
            }
        }
        .computer-table {
            background: #fff;
            box-shadow: 0px 0px 8px 0px rgba(4, 0, 0, 0.13);
            border-radius: 6px;
            padding: 10px;
            margin: 10px;
        }
    }
</style>