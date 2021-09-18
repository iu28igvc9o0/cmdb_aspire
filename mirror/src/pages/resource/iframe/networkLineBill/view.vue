<template>
    <!--  网络线路结算管理 -->
    <div class="network-line-settlement-management">
        <div class="network-line-settlement-management-search">
            <el-form ref="cabinetForm"
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
                <el-form-item prop="server_line_create_date"
                              label="新增线路时间"
                              class="time-range">
                    <el-date-picker v-model="cabinetForm.server_line_create_date"
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
                    <el-input v-model="cabinetForm.server_line"
                              clearable
                              placeholder="请输入"></el-input>
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
                <el-form-item prop="server_line_update_date"
                              label="线路调整时间"
                              class="time-range">
                    <el-date-picker v-model="cabinetForm.server_line_update_date"
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
                <el-form-item label="规格"
                              prop="server_line_standard">
                    <el-input v-model="cabinetForm.server_line_standard"
                              clearable
                              placeholder="请输入"></el-input>
                </el-form-item>
                <el-form-item prop="server_line_cancal_date"
                              label="线路取消时间"
                              class="time-range">
                    <el-date-picker v-model="cabinetForm.server_line_cancal_date"
                                    type="daterange"
                                    format="yyyy-MM-dd"
                                    value-format="yyyy-MM-dd"
                                    clearable
                                    range-separator="至"
                                    start-placeholder="开始日期"
                                    end-placeholder="结束日期">
                    </el-date-picker>
                </el-form-item>
                <el-form-item prop="bill_year"
                              class="bill-year"
                              label="网络结算年份">
                    <el-date-picker v-model="cabinetForm.bill_year"
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
                <div class="computer-table-button">
                    <el-button icon="el-icon-edit"
                               type="primary"
                               @click="editOperation"
                               v-if="buttonPermission.update">修改</el-button>
                    <el-button icon="el-icon-upload"
                               type="primary"
                               @click="uploadFile"
                               v-if="buttonPermission.import">导入</el-button>
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
        name: 'ResourceIframeNetworkLineBill',
        mixins: [myMixin],
        components: {
            operateDialog: () => import('src/pages/resource/iframe/computerManagement/networkLineBillDialog.vue'),
            importInstances: () => import('src/pages/cmdb/v2/instance/import/import-instance.vue')
        },
        data() {
            return {
                cabinetForm: {
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
                    server_line_cancal_date: '',
                    server_bill_year: '',
                },
                idcTypeOptions: [],
                networkOptions: [],
                billOptions: [],
                unitOptions: [],
                projectOptions: [],
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
                        value: 'bill_year',
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
                    },
                    {
                        label: '1月单价',
                        value: 'bill_month_unit1',
                        sortable: false
                    },
                    {
                        label: '1月最新容量',
                        value: 'capacity_month1',
                        sortable: false,
                        width: '100'
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
                        label: '2月最新容量',
                        value: 'capacity_month2',
                        sortable: false,
                        width: '100'
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
                        label: '3月最新容量',
                        value: 'capacity_month3',
                        sortable: false,
                        width: '100'
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
                        label: '4月最新容量',
                        value: 'capacity_month4',
                        sortable: false,
                        width: '100'
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
                        label: '5月最新容量',
                        value: 'capacity_month5',
                        sortable: false,
                        width: '100'
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
                        label: '6月最新容量',
                        value: 'capacity_month6',
                        sortable: false,
                        width: '100'
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
                        label: '7月最新容量',
                        value: 'capacity_month7',
                        sortable: false,
                        width: '100'
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
                        label: '8月最新容量',
                        value: 'capacity_month8',
                        sortable: false,
                        width: '100'
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
                        label: '9月最新容量',
                        value: 'capacity_month9',
                        sortable: false,
                        width: '100'
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
                        label: '10月最新容量',
                        value: 'capacity_month10',
                        sortable: false,
                        width: '100'
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
                        label: '11月最新容量',
                        value: 'capacity_month11',
                        sortable: false,
                        width: '100'
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
                        label: '12月最新容量',
                        value: 'capacity_month12',
                        sortable: false,
                        width: '100'
                    },
                    {
                        label: '12月计费',
                        value: 'bill_month_total12',
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
                moduleId: this.$route.query.module_id || 'ce4e68d1e95b4eb893703dedd160e0c3'
            }
        },
        created() {
            this.$set(this.collectionData, 'tableHeader', this.tableHeader1)
            this.getSelectCodeId()
            this.getTableList()
        },
        methods: {
            getSelectCodeId() {
                let moduleId = this.moduleId
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
            async getTableList(type) {
                this.loading = true
                let queryData = {}
                if (type) {
                    this.collectionData.pagination.currentPage = 1
                }
                for (let prop in this.cabinetForm) {
                    if (this.cabinetForm[prop] && this.cabinetForm[prop] != 0) {
                        if (prop === 'server_line_create_date' || prop === 'server_line_update_date' || prop === 'server_line_cancal_date') {
                            queryData[prop] = this.cabinetForm[prop].join(',')
                        } else {
                            queryData[prop] = this.cabinetForm[prop]
                        }
                    }

                }
                queryData.condicationCode = this.$route.query.condicationCode || 'server_network_line_bill'
                queryData.module_id = this.moduleId
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
            editOperation() {
                if (this.multipleSelection.length === 1) {
                    this.$set(this.operationParams, 'isEdit', true)
                    this.$set(this.operationParams, 'editDatas', { instance_id: this.multipleSelection[0].id })
                    this.showOperation = true
                } else {
                    this.$message.warning('请勾选一条需要修改的数据')
                }
            },
            uploadFile() {
                this.setImportDisplay(true)
            },
            setImportDisplay(val) {
                this.display.isImport = val
            },
            exportData() {
                let queryData = {}
                let moduleType = ''
                for (let prop in this.cabinetForm) {
                    if (this.cabinetForm[prop] && this.cabinetForm[prop] != 0) {
                        if (prop === 'server_line_create_date' || prop === 'server_line_update_date' || prop === 'server_line_cancal_date') {
                            queryData[prop] = this.cabinetForm[prop].join(',')
                        } else {
                            queryData[prop] = this.cabinetForm[prop]
                        }
                    }

                }
                queryData.condicationCode = this.$route.query.condicationCode || 'server_network_line_bill'
                queryData.module_id = this.moduleId
                moduleType = this.$route.query.condicationCode || 'server_network_line_bill'
                queryData.pageSize = this.collectionData.pagination.pageSize
                queryData.pageNo = this.collectionData.pagination.currentPage
                rbCmdbServiceFactory.exportInstanceListStream(queryData, moduleType).then((data) => {
                    let blob = new Blob([data], { type: 'application/msword' })
                    // 创建下载链接
                    let objectUrl = URL.createObjectURL(blob)
                    let downLoadElement = document.createElement('a')
                    downLoadElement.href = objectUrl
                    downLoadElement.download = '网络线路结算.xlsx'
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
    .network-line-settlement-management {
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