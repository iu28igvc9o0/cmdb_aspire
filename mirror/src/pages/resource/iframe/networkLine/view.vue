<template>
    <!-- 机柜管理 -->
    <div class="network-line-management">
        <nav-tab :tabsType="tabsType"
                 @changeTabs="changeTabs"></nav-tab>
        <div class="network-line-management-search">
            <el-form ref="computerForm"
                     v-show="isServerCabinet"
                     :model="computerForm"
                     inline
                     label-width="100px">
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
                <el-form-item label="网络类型"
                              prop="server_network_type">
                    <el-select v-model="computerForm.server_network_type"
                               clearable
                               filterable
                               placeholder="请选择">
                        <el-option v-for="(item,index) in networkOptions"
                                   :key="index"
                                   :label="item.value"
                                   :value="item.id">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="计费方式"
                              prop="server_bill_type">
                    <el-select v-model="computerForm.server_bill_type"
                               clearable
                               filterable
                               placeholder="请选择">
                        <el-option v-for="(item,index) in billOptions"
                                   :key="index"
                                   :label="item.value"
                                   :value="item.id">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item prop="server_line_create_date"
                              label="新增线路时间"
                              class="time-range">
                    <el-date-picker v-model="computerForm.server_line_create_date"
                                    type="daterange"
                                    format="yyyy-MM-dd"
                                    value-format="yyyy-MM-dd"
                                    clearable
                                    range-separator="至"
                                    start-placeholder="开始日期"
                                    end-placeholder="结束日期">
                    </el-date-picker>
                </el-form-item>
                <el-form-item label="线路名称"
                              prop="server_line">
                    <el-input v-model="computerForm.server_line"
                              clearable
                              placeholder="请输入"></el-input>
                </el-form-item>
                <el-form-item label="A端"
                              prop="server_line_a">
                    <el-input v-model="computerForm.server_line_a"
                              clearable
                              placeholder="请输入"></el-input>
                </el-form-item>
                <el-form-item label="Z端"
                              prop="server_line_b">
                    <el-input v-model="computerForm.server_line_b"
                              clearable
                              placeholder="请输入"></el-input>
                </el-form-item>
                <el-form-item prop="server_line_update_date"
                              label="线路调整时间"
                              class="time-range">
                    <el-date-picker v-model="computerForm.server_line_update_date"
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
                <el-form-item label="所属业务"
                              prop="business_belong_to">
                    <el-input v-model="computerForm.business_belong_to"
                              clearable
                              placeholder="请输入"></el-input>
                </el-form-item>
                <el-form-item label="规格"
                              prop="server_line_standard">
                    <el-input v-model="computerForm.server_line_standard"
                              clearable
                              placeholder="请输入"></el-input>
                </el-form-item>
                <el-form-item prop="server_line_cancal_date"
                              label="线路取消时间"
                              class="time-range">
                    <el-date-picker v-model="computerForm.server_line_cancal_date"
                                    type="daterange"
                                    format="yyyy-MM-dd"
                                    value-format="yyyy-MM-dd"
                                    clearable
                                    range-separator="至"
                                    start-placeholder="开始日期"
                                    end-placeholder="结束日期">
                    </el-date-picker>
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
                <el-form-item label="网络类型"
                              prop="server_network_type">
                    <el-select v-model="cabinetForm.server_network_type"
                               clearable
                               filterable
                               placeholder="请选择">
                        <el-option v-for="(item,index) in networkOptions"
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
                <el-form-item label="计费方式"
                              prop="server_bill_type">
                    <el-select v-model="cabinetForm.server_bill_type"
                               clearable
                               filterable
                               placeholder="请选择">
                        <el-option v-for="(item,index) in billOptions"
                                   :key="index"
                                   :label="item.value"
                                   :value="item.id">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="A端"
                              prop="server_line_a">
                    <el-input v-model="cabinetForm.server_line_a"
                              clearable
                              placeholder="请输入"></el-input>
                </el-form-item>
                <el-form-item label="Z端"
                              prop="server_line_b">
                    <el-input v-model="cabinetForm.server_line_b"
                              clearable
                              placeholder="请输入"></el-input>
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
                <el-form-item label="线路名称"
                              prop="server_line">
                    <el-input v-model="cabinetForm.server_line"
                              clearable
                              placeholder="请输入"></el-input>
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
                        <el-table-column label="历史数据"
                                         v-if="isServerCabinet"
                                         width="100"
                                         fixed="right"
                                         align="center">
                            <template slot-scope="scope">
                                <el-button type="text"
                                           @click="seeHistory(scope.row)"
                                           size="mini">查看</el-button>
                            </template>
                        </el-table-column>
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
        <importInstances ref="importInstances"
                         v-if="display.isImport"
                         :showImport="display.isImport"
                         @setImportDisplay="setImportDisplay"
                         :importType="importType"
                         :moduleType="moduleType"
                         :moduleId="moduleId"></importInstances>
        <!-- 网络线路历史数据 -->
        <history-dialog v-if="isHistory"
                        @closeHistoryDialog="closeHistoryDialog"
                        :historyParams='historyParams'></history-dialog>
    </div>

</template>
<script>
    import rbCmdbServiceFactory from 'src/services/cmdb/rb-cmdb-service.factory.js'
    import rbCmdbModuleServiceFactory from 'src/services/cmdb/rb-cmdb-module-service.factory'
    import myMixin from 'src/pages/resource/iframe/computerManagement/buttonPermission.js'
    export default {
        name: 'ResourceIframeNetworkLine',
        mixins: [myMixin],
        components: {
            navTab: () => import('src/pages/resource/iframe/computerManagement/navTab.vue'),
            operateDialog: () => import('src/pages/resource/iframe/computerManagement/networkLineDialog.vue'),
            importInstances: () => import('src/pages/cmdb/v2/instance/import/import-instance.vue'),
            historyDialog: () => import('src/pages/resource/iframe/computerManagement/seeHistory.vue')
        },
        data() {
            return {
                tabsType: {
                    name1: '网络线路汇总',
                    name2: '网路线路申请记录',
                    index1: 5,
                    index2: 6
                },
                isServerCabinet: true,
                computerForm: {
                    idcType: '',
                    server_network_type: '',
                    server_bill_type: '',
                    server_line_create_date: '',
                    server_line: '',
                    server_line_a: '',
                    server_line_b: '',
                    server_line_update_date: '',
                    project_belong_to: '',
                    business_belong_to: '',
                    server_line_standard: '',
                    server_line_cancal_date: ''
                },
                cabinetForm: {
                    idcType: '',
                    opt_type: '',
                    server_network_type: '',
                    update_time: '',
                    server_bill_type: '',
                    server_line: '',
                    server_line_a: '',
                    server_line_b: '',
                    insert_time: '',
                    project_belong_to: '',
                    business_belong_to: '',
                    owner_person: '',
                    order_no: ''

                },
                idcTypeOptions: [],
                networkOptions: [],
                billOptions: [],
                unitOptions: [],
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
                        label: '线路名称',
                        value: 'server_line',
                        sortable: false
                    },
                    {
                        label: '资源池',
                        value: 'idcType_dict_note_name',
                        sortable: false,
                        width: '140'
                    },
                    {
                        label: '网络类型',
                        value: 'server_network_type_dict_note_name',
                        sortable: false
                    },
                    {
                        label: '计费方式',
                        value: 'server_bill_type_dict_note_name',
                        sortable: false
                    },
                    {
                        label: '单位',
                        value: 'server_bill_unit_dict_note_name',
                        sortable: false
                    },
                    {
                        label: '数量',
                        value: 'server_line_count',
                        sortable: false
                    },
                    {
                        label: '规格',
                        value: 'server_line_standard',
                        sortable: false
                    },
                    {
                        label: 'A端',
                        value: 'server_line_a',
                        sortable: false
                    },
                    {
                        label: 'Z端',
                        value: 'server_line_b',
                        sortable: false
                    },
                    {
                        label: '线路新增时间',
                        value: 'server_line_create_date',
                        sortable: false,
                        width: '140'
                    },
                    {
                        label: '线路调整时间',
                        value: 'server_line_update_date',
                        sortable: false,
                        width: '140'
                    },
                    {
                        label: '线路取消时间',
                        value: 'server_line_cancal_date',
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
                    }
                ],
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
                        label: '申请操作时间',
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
                        label: '网络类型',
                        value: 'server_network_type_dict_note_name',
                        sortable: false
                    },
                    {
                        label: '计费方式',
                        value: 'server_bill_type_dict_note_name',
                        sortable: false
                    },
                    {
                        label: '规格',
                        value: 'server_line_standard',
                        sortable: false
                    },
                    {
                        label: '线路名称',
                        value: 'server_line',
                        sortable: false
                    },
                    {
                        label: 'A端',
                        value: 'server_line_a',
                        sortable: false
                    },
                    {
                        label: 'Z端',
                        value: 'server_line_b',
                        sortable: false
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
                moduleId: this.$route.query.module_id || 'a15eea62bf0c4872afe1c9630c3f5191',
                isHistory: false,
                historyParams: ''
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
                let moduleId = this.$route.query.module_id || 'a15eea62bf0c4872afe1c9630c3f5191'
                rbCmdbServiceFactory.getInstanceHeader(moduleId).then((res) => {
                    let selectObj = {
                        idcType: '',
                        project_belong_to: '',
                        server_network_type: '',
                        server_bill_type: '',
                        server_bill_unit: ''
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
                                case 'server_network_type':
                                    this.networkOptions = res
                                    this.$set(this.operationParams, 'networkOptions', res)
                                    break
                                case 'server_bill_type':
                                    this.billOptions = res
                                    this.$set(this.operationParams, 'billOptions', res)
                                    break
                                case 'server_bill_unit':
                                    this.unitOptions = res
                                    this.$set(this.operationParams, 'unitOptions', res)
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
                let moduleId = 'b43fb7edaaf943bcaff310d72b02102f'
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
                            if (prop === 'server_line_create_date' || prop === 'server_line_update_date' || prop === 'server_line_cancal_date') {
                                queryData[prop] = this.computerForm[prop].join(',')
                            } else {
                                queryData[prop] = this.computerForm[prop]
                            }

                        }

                    }
                    queryData.condicationCode = this.$route.query.condicationCode || 'server_network_line'
                    queryData.module_id = this.$route.query.module_id || 'a15eea62bf0c4872afe1c9630c3f5191'
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
                    queryData.condicationCode = 'server_network_line_record'
                    queryData.module_id = 'b43fb7edaaf943bcaff310d72b02102f'
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
                if (data === 5) {
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
                if (this.isServerCabinet) {
                    for (let prop in this.computerForm) {
                        if (this.computerForm[prop] && this.computerForm[prop] != 0) {
                            if (prop === 'server_line_create_date' || prop === 'server_line_update_date' || prop === 'server_line_cancal_date') {
                                queryData[prop] = this.computerForm[prop].join(',')
                            } else {
                                queryData[prop] = this.computerForm[prop]
                            }

                        }
                    }
                    queryData.condicationCode = this.$route.query.condicationCode || 'server_network_line'
                    queryData.module_id = this.$route.query.module_id || 'a15eea62bf0c4872afe1c9630c3f5191'
                    moduleType = this.$route.query.condicationCode || 'server_network_line'
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
                    queryData.condicationCode = 'server_network_line_record'
                    queryData.module_id = 'b43fb7edaaf943bcaff310d72b02102f'
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
                        downLoadElement.download = '网络线路汇总.xlsx'
                    } else {
                        downLoadElement.download = '网络线路申请记录.xlsx'
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
            // 查看历史数据
            seeHistory(row) {
                this.historyParams = row.id
                this.isHistory = true
            },
            // 关闭历史数据弹框
            closeHistoryDialog(status) {
                this.isHistory = status
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
    .network-line-management {
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