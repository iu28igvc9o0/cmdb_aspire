<template>
    <!-- 机柜管理 -->
    <div class="computer-room-management">
        <nav-tab :tabsType="tabsType"
                 @changeTabs="changeTabs"></nav-tab>
        <div class="computer-room-management-search">
            <el-form ref="computerForm"
                     v-show="isServerCabinet"
                     :model="computerForm"
                     inline
                     label-width="100px">
                <!-- <el-form-item prop="history"
                              label="历史数据">
                    <el-date-picker v-model="computerForm.history"
                                    type="date"
                                    format="yyyy-MM-dd"
                                    value-format="yyyy-MM-dd"
                                    clearable
                                    placeholder="选择日期">
                    </el-date-picker>
                </el-form-item> -->
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
                <el-form-item prop="first_online_date"
                              label="首次加电时间"
                              class="time-range">
                    <el-date-picker v-model="computerForm.first_online_date"
                                    type="daterange"
                                    format="yyyy-MM-dd"
                                    value-format="yyyy-MM-dd"
                                    clearable
                                    range-separator="至"
                                    start-placeholder="开始日期"
                                    end-placeholder="结束日期">
                    </el-date-picker>
                </el-form-item>
                <el-form-item label="机柜编码"
                              prop="server_cabinet_code">
                    <el-input v-model="computerForm.server_cabinet_code"
                              clearable
                              placeholder="请输入"></el-input>
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
                <el-form-item label="是否已加电"
                              prop="server_online_status">
                    <el-select v-model="computerForm.server_online_status"
                               clearable
                               placeholder="请选择">
                        <el-option v-for="(item,index) in statusOptions"
                                   :key="index"
                                   :label="item.value"
                                   :value="item.id">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item prop="offline_date"
                              label="下架时间"
                              class="time-range">
                    <el-date-picker v-model="computerForm.offline_date"
                                    type="daterange"
                                    format="yyyy-MM-dd"
                                    value-format="yyyy-MM-dd"
                                    clearable
                                    range-separator="至"
                                    start-placeholder="开始日期"
                                    end-placeholder="结束日期">
                    </el-date-picker>
                </el-form-item>
                <el-form-item label="所属项目"
                              prop="project_belong_to">
                    <el-select v-model="computerForm.project_belong_to"
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
                     v-show="!isServerCabinet"
                     :model="cabinetForm"
                     inline
                     label-width="100px">
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
                <el-form-item label="操作类型"
                              prop="opt_type">
                    <el-select v-model="cabinetForm.opt_type"
                               clearable
                               filterable
                               placeholder="请选择">
                        <el-option v-for="(item,index) in optTypeOptions"
                                   :key="index"
                                   :label="item.value"
                                   :value="item.id">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item prop="update_time"
                              label="更新时间"
                              class="time-range">
                    <el-date-picker v-model="cabinetForm.update_time"
                                    type="datetimerange"
                                    format="yyyy-MM-dd HH:mm:ss"
                                    value-format="yyyy-MM-dd HH:mm:ss"
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
                <el-form-item prop="insert_time"
                              label="操作时间"
                              class="time-range">
                    <el-date-picker v-model="cabinetForm.insert_time"
                                    type="datetimerange"
                                    format="yyyy-MM-dd HH:mm:ss"
                                    value-format="yyyy-MM-dd HH:mm:ss"
                                    clearable
                                    range-separator="至"
                                    start-placeholder="开始日期"
                                    end-placeholder="结束日期">
                    </el-date-picker>
                </el-form-item>
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
                <el-form-item label="所属业务"
                              prop="business_belong_to">
                    <el-input v-model="cabinetForm.business_belong_to"
                              clearable
                              placeholder="请输入"></el-input>
                </el-form-item>
                <el-form-item label="负责人"
                              prop="owner_person">
                    <el-input v-model="cabinetForm.owner_person"
                              clearable
                              placeholder="请输入"></el-input>
                </el-form-item>
                <el-form-item label="工单号"
                              prop="order_no">
                    <el-input v-model="cabinetForm.order_no"
                              clearable
                              placeholder="请输入"></el-input>
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
                     v-show="isServerCabinet">
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
                     v-show="!isServerCabinet">
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
        <!-- 导入操作 -->
        <upload-dialog v-if="uploadOperate"
                       @closeUploadDialog="closeUploadDialog"></upload-dialog>
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
        name: 'ResourceIframeServerCabinet',
        mixins: [myMixin],
        components: {
            navTab: () => import('src/pages/resource/iframe/computerManagement/navTab.vue'),
            operateDialog: () => import('src/pages/resource/iframe/computerManagement/operateDialog.vue'),
            uploadDialog: () => import('src/pages/resource/iframe/computerManagement/uploadDialog.vue'),
            importInstances: () => import('src/pages/cmdb/v2/instance/import/import-instance.vue')
        },
        data() {
            return {
                tabsType: {
                    name1: '机柜设备汇总',
                    name2: '机柜申请记录',
                    index1: 1,
                    index2: 2
                },
                isServerCabinet: true,
                computerForm: {
                    idcType: '',
                    server_room_location: '',
                    server_cabinet_standard: '',
                    server_online_status: '',
                    first_online_date: '',
                    offline_date: '',
                    project_belong_to: '',
                    server_cabinet_code: ''
                },
                cabinetForm: {
                    idcType: '',
                    opt_type: '',
                    server_room_location: '',
                    update_time: '',
                    server_cabinet_standard: '',
                    project_belong_to: '',
                    server_cabinet_code: '',
                    insert_time: '',
                    business_belong_to: '',
                    owner_person: '',
                    order_no: ''
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
                tableHeader1: [{
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
                    sortable: false
                },
                {
                    label: '加电设备台数',
                    value: 'online_count',
                    sortable: false
                },
                {
                    label: '首次加电日期',
                    value: 'first_online_date',
                    sortable: false,
                    width: '140'
                }, {
                    label: '计费开始日期',
                    value: 'bill_start_date',
                    sortable: false,
                    width: '140'
                },
                {
                    label: '下架日期',
                    value: 'offline_date',
                    sortable: false,
                    width: '140'
                },
                {
                    label: '所属项目',
                    value: 'project_belong_to_dict_note_name',
                    sortable: false
                },
                {
                    label: '所属业务',
                    value: 'business_belong_to',
                    sortable: false
                }],
                tableHeader2: [
                    {
                        label: '更新时间',
                        value: 'update_time',
                        sortable: false,
                        width: '140'
                    },
                    {
                        label: '操作类型',
                        value: 'opt_type_dict_note_name',
                        sortable: false
                    },
                    {
                        label: '申请时间',
                        value: 'insert_time',
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
                        label: '机柜编码',
                        value: 'server_cabinet_code',
                        sortable: false,
                        width: '140'
                    },
                    {
                        label: '机柜规格',
                        value: 'server_cabinet_standard_dict_note_name',
                        sortable: false
                    }, {
                        label: '设备台数',
                        value: 'server_device_count',
                        sortable: false
                    },
                    {
                        label: '首次加电日期',
                        value: 'first_online_date',
                        sortable: false,
                        width: '140'
                    },
                    {
                        label: '计费开始日期',
                        value: 'bill_start_date',
                        sortable: false,
                        width: '140'
                    },
                    {
                        label: '下架日期',
                        value: 'offline_date',
                        sortable: false,
                        width: '140'
                    },
                    {
                        label: '所属项目',
                        value: 'project_belong_to_dict_note_name',
                        sortable: false
                    },
                    {
                        label: '所属业务',
                        value: 'business_belong_to',
                        sortable: false
                    },
                    {
                        label: '负责人',
                        value: 'owner_person',
                        sortable: false
                    },
                    {
                        label: '工单号',
                        value: 'order_no',
                        sortable: false,
                        width: '140'
                    }
                ],
                multipleSelection: [],
                showOperation: false,
                operationParams: {
                    isEdit: false,
                    editDatas: null
                },
                uploadOperate: false,
                loading: false,
                display: {
                    isImport: false,
                    isEdit: false,
                    isSearchPlane: false,
                    isBatchUpdate: false
                },
                importType: 'ip_repository',
                moduleType: 'host',
                moduleId: this.$route.query.module_id || '161c8169f40b4f5fa0c8f583e5ae8d41'
            }
        },
        created() {
            this.$set(this.collectionData, 'tableHeader', this.tableHeader1)
            this.getSelectCodeId()
            this.getSelectRecordCode()
            this.getTableList()
        },
        methods: {
            getSelectCodeId() {
                let moduleId = this.$route.query.module_id || '161c8169f40b4f5fa0c8f583e5ae8d41'
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
                                    this.$set(this.operationParams, 'projectOptions', res)
                                    break
                            }
                        })
                    }
                })
            },
            getSelectRecordCode() {
                let moduleId = '6749da0e8d2d498e83d6cc5fa8231c77'
                rbCmdbServiceFactory.getInstanceHeader(moduleId).then((res) => {
                    let params = {
                        codeId: (res.filter(item => item.filedCode == 'opt_type'))[0].codeId
                    }
                    rbCmdbModuleServiceFactory.getRefModuleDict({ params: params }).then((res) => {
                        this.optTypeOptions = res
                    })
                })
            },
            async getTableList(type) {
                this.loading = true
                let queryData = {}
                if (type) {
                    this.collectionData.pagination.currentPage = 1
                }
                if (this.isServerCabinet) {
                    for (let prop in this.computerForm) {
                        if (this.computerForm[prop] && this.computerForm[prop] != 0) {
                            if (prop === 'first_online_date' || prop === 'offline_date') {
                                queryData[prop] = this.computerForm[prop].join(',')
                            } else {
                                queryData[prop] = this.computerForm[prop]
                            }

                        }

                    }
                    queryData.condicationCode = this.$route.query.condicationCode || 'server_cabinet_list'
                    queryData.module_id = this.$route.query.module_id || '161c8169f40b4f5fa0c8f583e5ae8d41'
                } else {
                    for (let prop in this.cabinetForm) {
                        if (this.cabinetForm[prop] && this.cabinetForm[prop] != 0) {
                            if (prop === 'insert_time' || prop === 'update_time') {
                                queryData[prop] = this.cabinetForm[prop].join(',')
                            } else {
                                queryData[prop] = this.cabinetForm[prop]
                            }
                        }

                    }
                    queryData.condicationCode = 'server_cabinet_record_list'
                    queryData.module_id = '6749da0e8d2d498e83d6cc5fa8231c77'
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
                if (data === 1) {
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
                // this.uploadOperate = true
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
                if (this.isServerCabinet) {
                    for (let prop in this.computerForm) {
                        if (this.computerForm[prop] && this.computerForm[prop] != 0) {
                            if (prop === 'first_online_date' || prop === 'offline_date') {
                                queryData[prop] = this.computerForm[prop].join(',')
                            } else {
                                queryData[prop] = this.computerForm[prop]
                            }

                        }

                    }
                    queryData.condicationCode = this.$route.query.condicationCode || 'server_cabinet_list'
                    queryData.module_id = this.$route.query.module_id || '161c8169f40b4f5fa0c8f583e5ae8d41'
                    moduleType = this.$route.query.condicationCode || 'server_cabinet_list'
                } else {
                    for (let prop in this.cabinetForm) {
                        if (this.cabinetForm[prop] && this.cabinetForm[prop] != 0) {
                            if (prop === 'insert_time' || prop === 'update_time') {
                                queryData[prop] = this.cabinetForm[prop].join(',')
                            } else {
                                queryData[prop] = this.cabinetForm[prop]
                            }
                        }

                    }
                    queryData.condicationCode = 'server_cabinet_record_list'
                    queryData.module_id = '6749da0e8d2d498e83d6cc5fa8231c77'
                    moduleType = 'server_cabinet_record_list'
                }
                queryData.pageSize = this.collectionData.pagination.pageSize
                queryData.pageNo = this.collectionData.pagination.currentPage
                rbCmdbServiceFactory.exportInstanceListStream(queryData, moduleType).then((data) => {
                    let blob = new Blob([data], { type: 'application/msword' })
                    // 创建下载链接
                    let objectUrl = URL.createObjectURL(blob)
                    let downLoadElement = document.createElement('a')
                    downLoadElement.href = objectUrl
                    if (this.isServerCabinet) {
                        downLoadElement.download = '机柜设备汇总.xlsx'
                    } else {
                        downLoadElement.download = '机柜申请记录.xlsx'
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
            closeUploadDialog(status) {
                this.uploadOperate = status
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
    .computer-room-management {
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