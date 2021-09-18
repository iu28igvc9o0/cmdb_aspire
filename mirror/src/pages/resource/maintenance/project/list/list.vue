<template>
    <div class="components-container yw-dashboard"
         v-loading="loading">
        <!-- 查询条件开始 -->
        <el-form class="components-condition yw-form"
                 :inline="true"
                 :model="formData"
                 label-width="75px">
            <el-form-item label="开始时间">
                <el-date-picker v-model="formData.date"
                                style="width: 260px"
                                type="daterange"
                                range-separator="至"
                                start-placeholder="开始日期"
                                end-placeholder="结束日期"
                                value-format="yyyy-MM-dd">
                </el-date-picker>
            </el-form-item>
            <el-form-item label="项目名称">
                <el-input v-model="formData.projectName"
                          placeholder="项目名称"
                          style="width: 130px"></el-input>
            </el-form-item>
            <el-form-item label="合同编号">
                <el-input v-model="formData.projectNo"
                          placeholder="合同编号"
                          style="width: 130px"></el-input>
            </el-form-item>
            <el-form-item label="服务供应商">
                <el-select v-model="formData.maintenProduce"
                           style="width: 130px;"
                           clearable
                           filterable>
                    <el-option v-for="(item, index) in companyList"
                               :key="index"
                               :label="item"
                               :value="item">
                    </el-option>
                </el-select>
            </el-form-item>
            <section class="btn-wrap">
                <el-button type="primary"
                           @click="query()">查询</el-button>
                <el-button @click="reset()">重置</el-button>
            </section>
        </el-form>
        <!-- 查询条件结束 -->
        <!-- 结果开始 -->
        <el-form class="yw-form">
            <div class="table-operate-wrap clearfix">
                <el-button class="btn-icons-wrap"
                           type="text"
                           icon="el-icon-plus"
                           @click="create">新增</el-button>
                <el-button class="btn-icons-wrap"
                           type="text"
                           icon="el-icon-edit"
                           @click="modify">修改</el-button>
                <el-button class="btn-icons-wrap"
                           type="text"
                           icon="el-icon-delete"
                           @click="deleteProject">删除</el-button>
                <el-button class="btn-icons-wrap"
                           type="text"
                           icon="el-icon-download"
                           @click="importMaintenanceProject">导入维保项目</el-button>
                <el-button class="btn-icons-wrap"
                           type="text"
                           icon="el-icon-upload2"
                           @click="exportOut"
                           :disabled="can_export">导出</el-button>
            </div>
            <div class="yw-el-table-wrap">
                <el-table class="yw-el-table"
                        :data="result"
                        height="calc(100vh - 50px - 210px)"
                        :element-loading-text="loading_text"
                        stripe
                        border
                        size="mini"
                        style="width: 100%"
                        @selection-change="handleSelectionChange">
                    <el-table-column type="selection"
                                    min-width="20px"
                                    align="center"></el-table-column>
                    <el-table-column label="项目名称"
                                    align="left"
                                    width="180"
                                    prop="projectName"
                                    fixed="left"
                                    :show-overflow-tooltip="true"></el-table-column>
                    <el-table-column label="合同编号"
                                    align="left"
                                    width="150"
                                    prop="projectNo"
                                    fixed="left"
                                    :show-overflow-tooltip="true"></el-table-column>
                    <el-table-column label="合同供应商"
                                    align="left"
                                    width="100"
                                    prop="contractProduce"
                                    :show-overflow-tooltip="true"></el-table-column>
                    <el-table-column label="合同联系人"
                                    align="left"
                                    width="100"
                                    :show-overflow-tooltip="true"
                                    prop="contractProduceName"></el-table-column>
                    <el-table-column label="合同联系人电话"
                                    align="left"
                                    width="180"
                                    :show-overflow-tooltip="true"
                                    prop="contractProducePhone"></el-table-column>
                    <el-table-column label="合同联系人邮箱"
                                    align="left"
                                    width="180"
                                    :show-overflow-tooltip="true"
                                    prop="contractProduceEmail"></el-table-column>
                    <el-table-column label="维保类型"
                                    align="center"
                                    width="100"
                                    prop="maintenanceType"
                                    :show-overflow-tooltip="true"></el-table-column>
                    <el-table-column label="服务形式"
                                    align="center"
                                    width="100"
                                    prop="serviceType"
                                    :show-overflow-tooltip="true"></el-table-column>
                    <el-table-column label="开始时间"
                                    align="center"
                                    width="110"
                                    sortable
                                    :formatter="dateFormat"
                                    prop="serviceStartTime"
                                    :show-overflow-tooltip="true"></el-table-column>
                    <el-table-column label="结束时间"
                                    align="center"
                                    sortable
                                    width="110"
                                    :formatter="dateFormat"
                                    prop="serviceEndTime"
                                    :show-overflow-tooltip="true"></el-table-column>
                    <el-table-column label="服务供应商"
                                    align="center"
                                    width="100"
                                    prop="maintenProduce"
                                    :show-overflow-tooltip="true"></el-table-column>
                    <el-table-column label="服务联系人"
                                    align="left"
                                    width="180"
                                    :show-overflow-tooltip="true"
                                    prop="maintenProduceName"></el-table-column>
                    <el-table-column label="服务联系人电话"
                                    align="left"
                                    width="180"
                                    :show-overflow-tooltip="true"
                                    prop="maintenProducePhone"></el-table-column>
                    <el-table-column label="服务联系人邮箱"
                                    align="left"
                                    width="180"
                                    :show-overflow-tooltip="true"
                                    prop="maintenProduceEmail"></el-table-column>
                    <el-table-column label="设备区域"
                                    align="center"
                                    width="120"
                                    :show-overflow-tooltip="true"
                                    prop="deviceArea"></el-table-column>
                    <el-table-column label="维保对象类型"
                                    align="center"
                                    width="120"
                                    :show-overflow-tooltip="true"
                                    prop="maintenanceProjectType"></el-table-column>
                    <el-table-column label="采购类型"
                                    align="center"
                                    width="100"
                                    :show-overflow-tooltip="true"
                                    prop="procureType"></el-table-column>
                    <el-table-column label="设备类型"
                                    align="center"
                                    width="100"
                                    :show-overflow-tooltip="true"
                                    prop="deviceType"></el-table-column>
                    <el-table-column label="金额(万)"
                                    align="center"
                                    width="80"
                                    :show-overflow-tooltip="true"
                                    prop="money">
                        <el-template slot-scope="scope">
                            {{!isNaN(parseFloat(scope.row.money)) ? parseFloat(scope.row.money).toFixed(2) : ''}}
                        </el-template>
                    </el-table-column>
                    <el-table-column label="税前(万)"
                                    align="center"
                                    width="80"
                                    :show-overflow-tooltip="true"
                                    prop="preTax">
                        <el-template slot-scope="scope">
                            {{!isNaN(parseFloat(scope.row.preTax)) ? parseFloat(scope.row.preTax).toFixed(2) : ''}}
                        </el-template>
                    </el-table-column>
                    <el-table-column label="税率(%)"
                                    align="center"
                                    width="80"
                                    :show-overflow-tooltip="true"
                                    prop="taxRate">
                    </el-table-column>
                    <el-table-column label="税后(万)"
                                    align="center"
                                    width="80"
                                    :show-overflow-tooltip="true"
                                    prop="afterTax">
                        <el-template slot-scope="scope">
                            {{!isNaN(parseFloat(scope.row.afterTax)) ? parseFloat(scope.row.afterTax).toFixed(2) : ''}}
                        </el-template>
                    </el-table-column>
                    <el-table-column label="单价(万)"
                                    align="center"
                                    width="80"
                                    :show-overflow-tooltip="true"
                                    prop="unitPrice">
                        <el-template slot-scope="scope">
                            {{!isNaN(parseFloat(scope.row.unitPrice)) ? parseFloat(scope.row.unitPrice).toFixed(2) : ''}}
                        </el-template>
                    </el-table-column>
                    <el-table-column label="总价(万)"
                                    align="center"
                                    width="80"
                                    :show-overflow-tooltip="true"
                                    prop="totalPrice">
                        <el-template slot-scope="scope">
                            {{!isNaN(parseFloat(scope.row.totalPrice)) ? parseFloat(scope.row.totalPrice).toFixed(2) : ''}}
                        </el-template>
                    </el-table-column>
                    <el-table-column label="结算方式"
                                    align="center"
                                    width="100"
                                    :show-overflow-tooltip="true"
                                    prop="payMethod">
                    </el-table-column>
                    <el-table-column label="折扣后金额(万)"
                                    align="center"
                                    width="150"
                                    :show-overflow-tooltip="true"
                                    prop="discountAmount">
                        <el-template slot-scope="scope">
                            {{!isNaN(parseFloat(scope.row.discountAmount)) ? parseFloat(scope.row.discountAmount).toFixed(2) : ''}}
                        </el-template>
                    </el-table-column>
                    <el-table-column label="折扣率(%)"
                                    align="center"
                                    width="100"
                                    :show-overflow-tooltip="true"
                                    prop="discountRate"></el-table-column>
                    <el-table-column label="维保合同"
                                    align="center"
                                    width="100"
                                    :show-overflow-tooltip="true"
                                    fixed="right">
                        <template slot-scope="scope">
                            <a class="table-link"
                            @click="dealMaintenFile(scope.row)">
                                <el-button class="btn-icons-wrap"
                                        type="text"
                                        icon="el-icon-paperclip"></el-button>
                            </a>
                        </template>
                    </el-table-column>
                    <el-table-column label="服务数量"
                                    align="center"
                                    width="100"
                                    :show-overflow-tooltip="true"
                                    fixed="right">
                        <template slot-scope="scope">
                            <a class="table-link"
                            @click="dealServiceNum(scope.row)">
                                {{ scope.row.serviceNums && scope.row.serviceNums.length > 0 ? scope.row.serviceNumName : '未关联' }}
                            </a>
                        </template>
                    </el-table-column>
                    <el-table-column label="关联设备"
                                    align="center"
                                    width="100"
                                    fixed="right">
                        <template slot-scope="scope">
                            <a class="table-link"
                            @click="selectInstance(scope.row,new Date())">
                                {{ scope.row.projectInstanceList.length > 0 ? scope.row.projectInstanceList.length  : '未关联' }}
                            </a>
                        </template>
                    </el-table-column>
                    <el-table-column label="设备详情"
                                    align="center"
                                    width="100"
                                    fixed="right">
                        <template slot-scope="scope">
                            <a class="table-link"
                            @click="viewDetail(scope.row)">
                                详情
                            </a>
                        </template>
                    </el-table-column>
                </el-table>
                <YwPagination @handleSizeChange="handleSizeChange"
                            @handleCurrentChange="handleCurrentChange"
                            :current-page="currentPage"
                            :page-sizes="pageSizes"
                            :page-size="pageSize"
                            :total="total"></YwPagination>
            </div>
        </el-form>
        <DialogProjectAdd v-if="addDialogMsg.dialogVisible"
                          :dialogMsg="addDialogMsg"></DialogProjectAdd>
        <DialogSelectInstance v-if="selectInstanceDialogMsg.dialogVisible"
                              :dialogMsg="selectInstanceDialogMsg"
                              @setShowSelectDialog="showSelectDialog"
                              @setShowImportDeviceSnDialog="importProjectDeviceSn"></DialogSelectInstance>
        <DialogBindInstance v-if="bindInstanceDialogMsg.dialogVisible"
                            :dialogMsg="bindInstanceDialogMsg"
                            @setCloseHandler="closeHandler"
                            @setShowSelectDialog="showSelectDialog"
                            @setShowSelectProjectId="showSelectProjectId"></DialogBindInstance>
        <DealServiceNum v-if="dealServiceNumDialogMsg.dialogVisible"
                        :dialogMsg="dealServiceNumDialogMsg"
                        @setCloseHandler="closeHandler"
                        @setAddServiceNum="addServiceNumAfterQuery"></DealServiceNum>
        <DialogMaintenFile v-if="maintenFileDialogMsg.dialogVisible"
                           :dialogMsg="maintenFileDialogMsg"
                           @setCloseHandler="closeHandler"
                           @setMaintenAddFile="dealMaintenAddFile"></DialogMaintenFile>
        <DialogFileAdd v-if="maintenFileAddDialogMsg.dialogVisible"
                       :dialogMsg="maintenFileAddDialogMsg"
                       @setCloseHandler="closeFileAddHandler"></DialogFileAdd>
        <YwImport v-if="importProjectData.isImport"
                  :showImport="importProjectData.isImport"
                  :dataStart="importProjectData.dataStart"
                  @setImportDisplay="setShowImportProject"
                  :importType="importProjectData.importType"></YwImport>
        <YwImport v-if="importProjectBindDeviceData.isImport"
                  :showImport="importProjectBindDeviceData.isImport"
                  :dataStart="importProjectBindDeviceData.dataStart"
                  @setImportDisplay="setShowImportProjectDeviceSn"
                  :importType="importProjectBindDeviceData.importType"
                  :params="importProjectBindDeviceData.data"></YwImport>

        <!-- 维保设备详情 -->
        <el-dialog class="yw-dialog"
                   width="90%"
                   title="维保设备详情"
                   :visible.sync="detailBoxShow">
            <DeviceDetail :deviceId="curDeviceId"></DeviceDetail>
        </el-dialog>

    </div>
</template>

<script>
    import QueryObject from 'src/utils/queryObject.js'
    import rbmaintenanceCommonUtil from 'src/services/mainten/rb-cmdb-mainten-common.js'
    import moment from 'moment'

    export default {
        name: 'ResourceMaintenanceProjectList',
        mixins: [QueryObject],
        components: {
            YwPagination: () => import('src/components/common/yw-pagination.vue'),
            DialogProjectAdd: () => import('src/pages/resource/maintenance/project/add/add.vue'),
            DialogSelectInstance: () => import('src/pages/resource/maintenance/project/instance/selectInstance.vue'),
            DialogBindInstance: () => import('src/pages/resource/maintenance/project/instance/bindInstance.vue'),
            YwImport: () => import('src/components/common/yw-import.vue'),
            DealServiceNum: () => import('src/pages/resource/maintenance/project/instance/dealServiceNum.vue'),
            DeviceDetail: () => import('src/pages/resource/maintenance/deviceDetail/index.vue'),
            DialogMaintenFile: () => import('src/pages/resource/maintenance/project/filemanage/list.vue'),
            DialogFileAdd: () => import('src/pages/resource/maintenance/project/filemanage/add.vue'),
        },
        data() {
            return {
                loading: false,
                loading_text: '正在查询数据, 请稍等...',
                can_export: false,
                // 表单数据
                formData: {
                    date: '',
                    projectName: '',
                    projectNo: '',
                    maintenProduce: '',
                },
                // 查询条件
                queryParams: {
                },
                companyList: [],
                // 查询结果
                result: [],
                // dialog
                dialogMsg: {
                    id: '',// 每个弹窗数据的唯一标识
                    dialogVisible: false,
                    data: {} // 数据(暂时没有详情接口，从列表数据携带)
                },
                addDialogMsg: {
                    dialogVisible: false,
                    data: {
                        dialogTitle: ''
                    }
                },
                selectInstanceDialogMsg: {
                    dialogVisible: false,
                    data: {}
                },
                bindInstanceDialogMsg: {
                    dialogVisible: false,
                    data: {}
                },
                // 服务数量拟态框
                dealServiceNumDialogMsg: {
                    dialogVisible: false,
                    data: {}
                },
                // 维保合同拟态框
                maintenFileDialogMsg: {
                    dialogVisible: false,
                    data: {}
                },
                // 维保合同新增拟态框
                maintenFileAddDialogMsg: {
                    dialogVisible: false,
                    data: {}
                },
                selectProjects: [],
                importProjectData: {
                    isImport: false,
                    importType: 'maintenance_project'
                },
                importProjectBindDeviceData: {
                    isImport: false,
                    importType: 'maintenance_project_bind_device',
                    data: {}
                },

                // 详情弹框
                deviceDetailInfo: {
                    dialogTitle: '设备详情',
                },
                detailBoxShow: false,
                curDeviceId: '',
            }
        },
        watch: {
            addDialogMsg: {
                handler(val) {
                    if (!val.dialogVisible) {
                        this.query()
                    }
                },
                deep: true
            },
            selectInstanceDialogMsg: {
                handler(val) {
                    if (!val.dialogVisible) {
                        this.query()
                    }
                },
                deep: true
            },
        },
        created() {
            this.query()
            this.getBrandList()
        },
        methods: {
            // 服务数量格式处理
            formatServiceNum(data) {
                data.forEach(item => {
                    if (item.serviceNums && item.serviceNums.length > 0) {
                        // 得到服务数量的列表
                        var serviceNums = item.serviceNums
                        var result = serviceNums[0].serviceType + '(' + serviceNums[0].serviceNum + ')'
                        for (var i = 1; i < serviceNums.length; i++) {
                            result += ';'
                            result += serviceNums[i].serviceType + '(' + serviceNums[i].serviceNum + ')'
                        }
                        item.serviceNumName = result
                    }
                })
            },
            // 服务数量处理
            dealServiceNum(row) {
                this.dealServiceNumDialogMsg.dialogVisible = true
                this.dealServiceNumDialogMsg.data = row
            },
            // 维保合同拟态框处理
            dealMaintenFile(row) {
                this.maintenFileDialogMsg.dialogVisible = true
                this.maintenFileDialogMsg.data = row.id
            },
            // 维保合同新增文件拟态框处理
            dealMaintenAddFile(row) {
                // 新增拟态框出现
                this.maintenFileAddDialogMsg.dialogVisible = true
                this.maintenFileAddDialogMsg.data = row
                // 列表拟态框隐藏
                this.maintenFileDialogMsg.dialogVisible = false
            },
            addServiceNumAfterQuery() {
                this.dealServiceNumDialogMsg.dialogVisible = false
                this.query()
            },
            // 维保合同新增文件拟态框管理时处理
            closeFileAddHandler(row) {
                // 列表拟态框出现
                this.maintenFileDialogMsg.dialogVisible = true
                this.maintenFileDialogMsg.data = row
                // 新增拟态框隐藏
                this.maintenFileAddDialogMsg.dialogVisible = false
            },
            // bindIstance关闭处理
            closeHandler() {
                this.query()
            },
            // 获取厂家详情
            gotoDetail(row) {
                this.$router.push({
                    path: '/resource/maintenance/produceDetail',
                    query: { id: row }
                })
            },
            // 获取服务(维保)供应商列表
            getBrandList() {
                let req = {
                    'params': { 'produce_type': '维保供应商' }
                }
                rbmaintenanceCommonUtil.getProducesByType(req).then((res) => {
                    const list = []
                    for (let i in res) {
                        list.push(res[i].produce_name)
                    }
                    this.companyList = list
                })
            },
            // 新增维保项目
            create() {
                this.addDialogMsg.dialogVisible = true
                this.addDialogMsg.dialogTitle = '新增维保项目'
                this.addDialogMsg.data.id = ''
            },
            // 修改维保项目
            modify() {
                if (this.selectProjects.length !== 1) {
                    this.$message.error('请选择一条维保项目进行修改.')
                    return false
                }
                this.addDialogMsg.dialogVisible = true
                this.addDialogMsg.dialogTitle = '修改维保项目'
                this.addDialogMsg.data = this.selectProjects[0]
            },
            // 导入维保项目
            importMaintenanceProject() {
                this.importProjectData.isImport = true
            },
            // 设置导入维保项目显示
            setShowImportProject(val) {
                this.importProjectData.isImport = val
            },
            // 导入设备序列号
            importProjectDeviceSn(val) {
                this.selectInstanceDialogMsg.dialogVisible = false
                this.importProjectBindDeviceData.isImport = true
                this.importProjectBindDeviceData.data = val
            },
            // 设置导入设备序列号显示
            setShowImportProjectDeviceSn(val) {
                this.importProjectBindDeviceData.isImport = val
                this.query()
            },
            showSelectDialog(val) {
                this.selectInstanceDialogMsg.dialogVisible = val
                this.query()
            },
            showSelectProjectId(val) {
                this.selectInstanceDialogMsg.data = val
            },
            // 删除维保项目
            deleteProject() {
                if (this.selectProjects.length !== 1) {
                    this.$message.error('请选择一个需要删除的项目，不支持批量删除操作.')
                    return false
                }
                const _t = this
                this.$confirm('确认要删除选中的维保项目吗?').then(() => {
                    this.rbHttp.sendRequest({
                        method: 'DELETE',
                        url: '/v1/cmdb/maintenance/project/delete',
                        params: { project_id: this.selectProjects[0].id }
                    }).then((res) => {
                        if (res && res.flag === 'success') {
                            this.$message.success('删除成功')
                            _t.query()
                        } else if (res && res.flag === 'error') {
                            this.$message.error(res.msg)
                        } else {
                            this.$message.error('删除失败')
                        }
                    })
                })
            },
            // 查询设备 绑定项目
            selectInstance(row) {
                // 查看已绑定设备列表
                if (row.projectInstanceList.length > 0) {
                    this.bindInstanceDialogMsg.dialogVisible = true
                    this.bindInstanceDialogMsg.data = row
                } else {
                    // 新增绑定设备列表
                    this.selectInstanceDialogMsg.dialogVisible = true
                    this.selectInstanceDialogMsg.data = row
                }
            },
            // 设置参数
            setParams(activePagination) {
                if (activePagination) {
                    this.queryParams['pageNo'] = this.currentPage
                    this.queryParams['pageSize'] = this.pageSize
                } else {
                    this.queryParams = {
                        'pageNo': this.initPageChange(),
                        'pageSize': this.pageSize,
                        'serviceStartTime': this.formData.date[0],
                        'serviceEndTime': this.formData.date[1],
                        'projectName': this.formData.projectName,
                        'projectNo': this.formData.projectNo,
                        'maintenProduce': this.formData.maintenProduce
                    }
                }
            },
            // 格式化日期为yyyy-MM-dd
            dateFormat(row, column) {
                var date = row[column.property]
                if (date === undefined) {
                    return ''
                }
                var dataStr = moment(date).format('YYYY-MM-DD')
                if (dataStr == '1970-10-01') {
                    return '----/--/--'
                }
                return dataStr
            },
            // 格式化供应商厂商联系人信息
            supplyFormat(row, column) {
                var produceInfo = row['contractProduceInfo']
                var concat = produceInfo == null ? null : produceInfo.concats
                var value = ''
                if (concat !== null && concat.length > 0) {
                    concat.forEach((item) => {
                        if (item[column.property] !== undefined) {
                            if (value !== '') {
                                value += ','
                            }
                            value += item[column.property]
                        }
                    })
                }
                return value
            },
            // 格式化服务厂商联系人信息
            serviceFormat(row, column) {
                var produceInfo = row['produceInfo']
                var concat = produceInfo == null ? null : produceInfo.concats
                var value = ''
                if (concat !== null && concat.length > 0) {
                    concat.forEach((item) => {
                        if (item[column.property] !== undefined) {
                            if (value !== '') {
                                value += ','
                            }
                            value += item[column.property]
                        }
                    })
                }
                return value
            },
            /** 查询
             * activePagination:分页活动下保持先前的查询条件
             */
            query(activePagination = false) {
                this.setParams(activePagination)
                this.loading = true
                this.rbHttp.sendRequest({
                    method: 'POST',
                    url: '/v1/cmdb/maintenance/project/list',
                    data: this.queryParams
                }).then((res) => {
                    this.total = res.totalSize
                    this.result = res.data
                    this.formatServiceNum(res.data)
                    return res
                }).finally(() => {
                    this.loading = false
                })
            },
            // 重置
            reset() {
                this.formData = {
                    ip: '',
                    date: '',
                }
                this.query()
            },
            // 复选框选择事件
            handleSelectionChange(val) {
                this.selectProjects = val
            },
            // 导出
            exportOut() {
                this.loading = true
                this.loading_text = '正在导出数据,请稍等...'
                this.can_export = true
                this.rbHttp.sendRequest({
                    method: 'POST',
                    url: '/v1/cmdb/maintenance/project/export',
                    data: this.queryParams,
                    binary: true
                }).then((res) => {
                    this.exportFiles({
                        data: res,
                        fileType: 'application/vnd.ms-excel',
                        fileName: '维保项目列表.xls'
                    })
                    return res
                }).finally(() => {
                    this.loading = false
                    this.loading_text = '正在查询数据,请稍等...'
                    this.can_export = false
                })
            },

            // 设备详情
            viewDetail(row) {
                this.detailBoxShow = true
                this.curDeviceId = row.id
            }
        }
    }
</script>

<style lang="scss" scoped>
    /deep/ .el-date-editor .el-range-separator {
        padding: 0px;
    }
</style>
