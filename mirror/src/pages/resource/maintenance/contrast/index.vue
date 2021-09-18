<template>
    <div class="wp100"
         v-loading.fullscreen.lock="pageLoading"
         :element-loading-text="loading_text">

        <!-- 第一层表格 -->
        <div class="p10">
            <el-card class="box-card mtop15">
                <div slot="header">
                    <span style="font-weight: bold">维保设备与资产设备对比</span>
                    <div class="clearfix fr">
                        <el-button class="btn-icons-wrap"
                                   type="text"
                                   icon="el-icon-upload2"
                                   @click="exportOut('exportInventoryFirst', '维保设备与资产设备对比')"
                                   :disabled="can_export">导出</el-button>
                    </div>
                </div>
                <div class="yw-el-table-wrap">
                    <el-table :data="inventoryFirst"
                              class="yw-el-table"
                              stripe
                              tooltip-effect="dark"
                              border
                              max-height="400px"
                              show-summary
                              sum-text="总数"
                              v-loading="firstLoading">
                        <el-table-column sortable
                                         prop="idcType"
                                         label="资源池"
                                         min-width="100"
                                         show-overflow-tooltip>
                            <template slot-scope="scope">
                                <span class="blue pointer"
                                      @click="showSecondTable(scope.row)">{{scope.row.idcType}}</span>
                            </template>
                        </el-table-column>
                        <el-table-column sortable
                                         prop="successCount"
                                         label="关联成功设备数量"
                                         width="150"></el-table-column>
                        <el-table-column sortable
                                         prop="failCount"
                                         label="维保项目未关联设备数量"
                                         width="180"></el-table-column>
                        <el-table-column sortable
                                         prop="inventory"
                                         label="资产管理未关联设备数量"
                                         width="180"></el-table-column>
                    </el-table>
                </div>
            </el-card>
        </div>

        <!-- 第二层表格 -->
        <div class="p10"
             v-if="inventorySecond.length">
            <el-card class="box-card mtop15">
                <div slot="header">
                    <span style="font-weight: bold">维保设备与资产设备比对表-{{curIdcType}}</span>
                    <div class="clearfix fr">
                        <el-button class="btn-icons-wrap"
                                   type="text"
                                   icon="el-icon-upload2"
                                   @click="exportOut('exportInventorySecond', `维保设备与资产设备比对表-${curIdcType}`)">导出</el-button>
                    </div>
                </div>
                <div class="yw-el-table-wrap">
                    <el-table :data="inventorySecond"
                              :span-method="objectSpanMethod"
                              :cell-style="{'vertical-align':'bottom'}"
                              class="yw-el-table"
                              stripe
                              tooltip-effect="dark"
                              border
                              height="400px">
                        <el-table-column sortable
                                         prop="projectName"
                                         label="维保项目名称"
                                         min-width="100"
                                         show-overflow-tooltip></el-table-column>
                        <el-table-column sortable
                                         prop="successCount"
                                         label="关联成功设备数量"
                                         width="150"></el-table-column>
                        <el-table-column sortable
                                         prop="failCount"
                                         label="维保项目未关联设备数量"
                                         width="180">
                            <template slot-scope="scope">
                                <span class="blue pointer"
                                      @click="showThirdTable('maintenance', scope.row)">{{scope.row.failCount}}</span>
                            </template>
                        </el-table-column>
                        <el-table-column sortable
                                         prop="inventory"
                                         label="资产管理未关联设备数量"
                                         width="180">
                            <template slot-scope="scope">
                                <span class="blue pointer"
                                      @click="showThirdTable('instance', scope.row)">{{scope.row.inventory}}</span>
                            </template>
                        </el-table-column>
                    </el-table>
                </div>

                <div class="yw-page-wrap">
                    <el-pagination @size-change="handleSizeChange"
                                   @current-change="handleCurrentChange"
                                   :current-page="currentPage"
                                   :page-sizes="pageSizes"
                                   :page-size="pageSize"
                                   :total="total"
                                   small
                                   layout="total, sizes, prev, pager, next, jumper"></el-pagination>
                </div>
            </el-card>
        </div>

        <!-- 第三层表格 -->
        <div class="p10"
             v-if="inventoryThird.length && inventorySecond.length">
            <el-card class="box-card mtop15">
                <div slot="header">
                    <span style="font-weight: bold">{{curProjectName}}-{{curThirdTableName}}</span>
                    <div class="clearfix fr">
                        <el-button class="btn-icons-wrap"
                                   type="text"
                                   icon="el-icon-upload2"
                                   @click="exportOut('exportInventoryThird', `${curProjectName}-${curThirdTableName}`)">导出</el-button>
                    </div>
                </div>
                <div class="yw-el-table-wrap">

                    <!-- 点击“维保项目未关联设备数量” -->
                    <el-table v-if="searchType === 'maintenance'"
                              class="yw-el-table"
                              :data="inventoryThird"
                              height="400px"
                              v-loading="loading"
                              stripe
                              border>
                        <el-table-column label="设备序列号"
                                         align="left"
                                         min-width="120"
                                         prop="device_sn"
                                         show-overflow-tooltip></el-table-column>
                        <el-table-column label="设备名称"
                                         align="left"
                                         min-width="120"
                                         prop="device_name"
                                         show-overflow-tooltip></el-table-column>
                        <el-table-column label="设备IP"
                                         align="left"
                                         min-width="120"
                                         prop="ip"
                                         show-overflow-tooltip></el-table-column>
                        <el-table-column label="资源池"
                                         align="left"
                                         min-width="180"
                                         prop="idcType"
                                         show-overflow-tooltip></el-table-column>
                        <el-table-column label="一级部门"
                                         align="left"
                                         min-width="100"
                                         prop="department1"
                                         show-overflow-tooltip></el-table-column>
                        <el-table-column label="二级部门"
                                         align="left"
                                         min-width="100"
                                         prop="department2"
                                         show-overflow-tooltip></el-table-column>
                        <el-table-column label="业务系统"
                                         align="left"
                                         min-width="120"
                                         prop="bizSystem"
                                         show-overflow-tooltip></el-table-column>
                    </el-table>

                    <!-- 点击“资产管理未关联设备数量” -->
                    <el-table v-if="searchType === 'instance'"
                              class="yw-el-table"
                              :data="inventoryThird"
                              height="400px"
                              stripe
                              border>
                        <el-table-column prop="ip"
                                         label="管理IP"
                                         width="120"
                                         show-overflow-tooltip></el-table-column>
                        <el-table-column prop="device_name"
                                         min-width="200"
                                         label="设备名称"
                                         show-overflow-tooltip></el-table-column>
                        <el-table-column prop="insert_time"
                                         label="创建时间"
                                         width="140"
                                         show-overflow-tooltip></el-table-column>
                        <el-table-column prop="update_time"
                                         label="最后更新时间"
                                         width="140"
                                         show-overflow-tooltip></el-table-column>

                        <el-table-column prop="device_type"
                                         label="设备类型"
                                         width="100"
                                         show-overflow-tooltip></el-table-column>
                        <el-table-column prop="department2"
                                         label="二级部门"
                                         width="100"
                                         show-overflow-tooltip></el-table-column>
                        <el-table-column prop="dept_operation"
                                         label="维护部门"
                                         width="100"
                                         show-overflow-tooltip></el-table-column>
                        <el-table-column prop="is_ipmi_monitor"
                                         label="管理网是否监控"
                                         width="130"
                                         show-overflow-tooltip></el-table-column>
                        <el-table-column prop="device_sn"
                                         label="设备序列号"
                                         width="160"
                                         show-overflow-tooltip></el-table-column>
                        <el-table-column prop="pod_name"
                                         label="POD名称"
                                         width="100"
                                         show-overflow-tooltip></el-table-column>
                        <el-table-column prop="device_class"
                                         label="设备分类"
                                         width="100"
                                         show-overflow-tooltip></el-table-column>
                        <el-table-column prop="bizSystem"
                                         label="业务系统"
                                         width="100"
                                         show-overflow-tooltip></el-table-column>
                        <el-table-column prop="project_name"
                                         label="项目名称"
                                         width="100"
                                         show-overflow-tooltip></el-table-column>
                        <el-table-column prop="ops"
                                         label="维护人员"
                                         width="100"
                                         show-overflow-tooltip></el-table-column>
                        <el-table-column prop="device_mfrs"
                                         label="制造厂商"
                                         width="100"
                                         show-overflow-tooltip></el-table-column>
                        <el-table-column prop="device_status"
                                         label="设备状态"
                                         width="100"
                                         show-overflow-tooltip></el-table-column>
                        <el-table-column prop="device_model"
                                         label="设备型号"
                                         width="160"
                                         show-overflow-tooltip></el-table-column>
                        <el-table-column prop="idcType"
                                         label="资源池名称"
                                         width="110"
                                         show-overflow-tooltip></el-table-column>
                        <el-table-column prop="idc_cabinet"
                                         label="机柜名称"
                                         width="100"
                                         show-overflow-tooltip></el-table-column>
                        <el-table-column prop="department1"
                                         label="一级部门"
                                         width="110"
                                         show-overflow-tooltip></el-table-column>
                        <el-table-column prop="mainten_factory"
                                         label="维保厂家"
                                         width="110"
                                         show-overflow-tooltip></el-table-column>
                    </el-table>
                </div>

                <div class="yw-page-wrap">
                    <el-pagination @size-change="handleThirdTableSizeChange"
                                   @current-change="handleThirdTableCurrentChange"
                                   :current-page="thirdTableCurrentPage"
                                   :page-sizes="pageSizes"
                                   :page-size="pageSize"
                                   :total="thirdTableTotal"
                                   small
                                   layout="total, sizes, prev, pager, next, jumper"></el-pagination>
                </div>
            </el-card>
        </div>

    </div>
</template>

<script>
    import rbmaintenanceServiceFactory from 'src/services/cmdb/rb-maintenance-services.factory.js'
    import rbAutoOperationMixin from 'src/services/auto_operation/rb-auto-operation-mixin.js'
    import QueryObject from 'src/utils/queryObject.js'
    export default {
        name: 'DocManageList',
        components: {},
        data() {
            return {
                inventoryFirst: [],
                inventorySecond: [],
                // 第三层表格
                inventoryThird: [],
                searchType: '',
                curIdcType: '',
                curProjectName: '',

                thirdTableCurrentPage: 1, // 当前页
                thirdTableTotal: 0, // 总共多少条数据

                firstLoading: false,
                pageLoading: false,
                loading_text: '请稍候...',

            }
        },
        mixins: [QueryObject, rbAutoOperationMixin],
        computed: {
            curThirdTableName() {
                if (this.searchType === 'maintenance') {
                    return '维保项目未关联设备数量'
                } else {
                    return '资产管理未关联设备数量'
                }
            }
        },
        mounted() {
            this.getInventoryFirst()
        },
        methods: {
            objectSpanMethod({ column, rowIndex }) {
                if (column.label === '资产管理未关联设备数量') {
                    if (rowIndex === 0) {
                        return {
                            rowspan: this.inventorySecond.length,
                            colspan: 1
                        }
                    } else {
                        return {
                            rowspan: 0,
                            colspan: 0
                        }
                    }
                }
            },
            // 改变每页条数
            handleThirdTableSizeChange(val) {
                this.pageSize = val
                this.thirdTableCurrentPage = 1
                this.search()
            },
            // 跳转到第n页
            handleThirdTableCurrentChange(val) {
                this.thirdTableCurrentPage = val
                this.getInventoryThird()
            },
            // 导出
            exportOut(methodType, fileName) {
                let params = {}
                if (methodType === 'exportInventorySecond') {
                    params.resourcePool = this.curIdcType
                } else if (methodType === 'exportInventoryThird') {

                    params = this.handleSearchTypeParams(this.searchType, params)

                }
                this.pageLoading = true
                rbmaintenanceServiceFactory[methodType](params)
                    .then(res => {
                        this.pageLoading = false
                        this.$message.success('下载完成')
                        this.exportFiles({
                            data: res,
                            fileType: 'application/vnd.ms-excel',
                            fileName: `${fileName}.xls`
                        })
                    })
                    .catch(() => {
                        this.pageLoading = false
                    })
            },
            showSecondTable(row) {
                this.currentPage = 1
                this.curIdcType = row.idcType
                this.inventoryThird = []
                this.getInventorySecond(row.idcType)
            },
            handleSearchTypeParams(searchType, params = {}) {
                // 查询维保未关联设备数量
                if (searchType === 'maintenance') {
                    params.searchType = 'maintenance'
                    params.projectName = this.curProjectName
                    // 查询资产管理未关联设备数量
                } else if (searchType === 'instance') {
                    params.searchType = 'instance'
                    params.resourcePool = this.curIdcType
                }
                return params
            },
            showThirdTable(searchType, row) {
                this.searchType = searchType
                this.curProjectName = row.projectName
                this.thirdTableCurrentPage = 1
                this.getInventoryThird()
            },
            // 第一层查询
            getInventoryFirst() {
                this.firstLoading = true
                rbmaintenanceServiceFactory
                    .getInventoryFirst()
                    .then(res => {
                        this.inventoryFirst = res
                        this.firstLoading = false
                    })
                    .catch(error => {
                        if (error.data && error.data.message) {
                            this.$message.error(error.data.message)
                            this.firstLoading = false
                        }
                    })
            },
            // 第二层查询
            async getInventorySecond(idcType) {
                let req = {
                    pageNo: this.currentPage,
                    pageSize: this.pageSize,
                    resourcePool: idcType
                }

                this.inventorySecond = []
                this.pageLoading = true
                await rbmaintenanceServiceFactory
                    .getInventorySecond(req)
                    .then(res => {
                        this.total = res.totalSize
                        this.inventorySecond = res.data
                        this.pageLoading = false
                        if (!res.totalSize) {
                            this.$message.warning('该资源池暂无信息')
                        }
                    })
                    .catch(error => {
                        if (error.data && error.data.message) {
                            this.$message.error(error.data.message)
                            this.pageLoading = false
                        }
                    })
            },
            // 第三层查询
            async getInventoryThird() {
                let req = {
                    pageNo: this.thirdTableCurrentPage,
                    pageSize: this.pageSize,
                }
                req = this.handleSearchTypeParams(this.searchType, req)

                this.pageLoading = true
                await rbmaintenanceServiceFactory
                    .getInventoryThird(req)
                    .then(res => {
                        this.thirdTableTotal = res.totalSize
                        this.inventoryThird = res.data
                        this.pageLoading = false
                        if (!res.totalSize) {
                            this.$message.warning('暂无信息')
                        }
                    })
                    .catch(error => {
                        if (error.data && error.data.message) {
                            this.$message.error(error.data.message)
                            this.pageLoading = false
                        }
                    })
            },
            search() {
                this.getInventorySecond(this.curIdcType)
            }

        }
    }
</script>

<style lang="scss" scoped>
    @import "src/pages/auto_operation/assets/global.scss";
</style>
