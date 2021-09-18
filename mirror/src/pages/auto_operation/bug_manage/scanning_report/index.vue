<!-- 漏洞管理：漏洞扫描报告 -->
<template>
    <div class="components-container yw-dashboard"
         v-loading.fullscreen.lock="pageLoading"
         :element-loading-text="loading_text">

        <!-- 搜索组件 -->
        <search-box @search="search"
                    @updateFormSearch="updateFormSearch"></search-box>

        <el-form class="yw-form">
            <div ref="operateBox"
                 class="table-operate-wrap clearfix">
                <el-button type="text"
                           icon="el-icon-download"
                           @click="exportList">导出</el-button>
            </div>
            <div class="yw-el-table-wrap">
                <el-table :data="dataList"
                          class="yw-el-table"
                          stripe
                          highlight-current-row
                          tooltip-effect="dark"
                          border
                          :height="tableHeight"
                          v-loading="loading">
                    <el-table-column prop="groupName"
                                     label="漏洞分组"
                                     min-width="90"
                                     show-overflow-tooltip>
                        <template slot-scope="scope">
                            <span class="blue pointer"
                                  @click="gotoDeviceList(scope)">{{scope.row.groupName}}</span>
                        </template>
                    </el-table-column>
                    <el-table-column prop="department1"
                                     label="一级部门"
                                     show-overflow-tooltip
                                     min-width="90">
                        <template slot-scope="scope">
                            <span class="blue pointer"
                                  @click="gotoDeviceList(scope)">{{scope.row.department1}}</span>
                        </template>
                    </el-table-column>
                    <el-table-column prop="department2"
                                     label="二级部门"
                                     show-overflow-tooltip
                                     min-width="90">
                        <template slot-scope="scope">
                            <span class="blue pointer"
                                  @click="gotoDeviceList(scope)">{{scope.row.department2}}</span>
                        </template>
                    </el-table-column>
                    <el-table-column prop="bizSystem"
                                     label="业务系统"
                                     show-overflow-tooltip
                                     min-width="90">
                        <template slot-scope="scope">
                            <span class="blue pointer"
                                  @click="gotoDeviceList(scope)">{{scope.row.bizSystem}}</span>
                        </template>
                    </el-table-column>
                    <el-table-column prop="highNum"
                                     label="高危漏洞级别设备数量"
                                     show-overflow-tooltip
                                     width="150">
                        <template slot-scope="scope">
                            <span class="blue pointer"
                                  @click="gotoDeviceList(scope)">{{scope.row.highNum}}</span>
                        </template>
                    </el-table-column>
                    <el-table-column prop="midNum"
                                     label="中危漏洞级别设备数量"
                                     show-overflow-tooltip
                                     width="150">
                        <template slot-scope="scope">
                            <span class="blue pointer"
                                  @click="gotoDeviceList(scope)">{{scope.row.midNum}}</span>
                        </template>
                    </el-table-column>
                    <el-table-column prop="lowNum"
                                     label="低危漏洞级别设备数量"
                                     show-overflow-tooltip
                                     width="150">
                        <template slot-scope="scope">
                            <span class="blue pointer"
                                  @click="gotoDeviceList(scope)">{{scope.row.lowNum}}</span>
                        </template>
                    </el-table-column>
                    <el-table-column prop="totalNum"
                                     label="发现漏洞设备总数量"
                                     min-width="110"
                                     show-overflow-tooltip>
                        <template slot-scope="scope">
                            <span class="blue pointer"
                                  @click="gotoDeviceList(scope)">{{scope.row.totalNum}}</span>
                        </template>
                    </el-table-column>
                    <el-table-column prop="processedNum"
                                     label="已修复漏洞设备数量"
                                     min-width="110"
                                     show-overflow-tooltip>
                        <template slot-scope="scope">
                            <span class="blue pointer"
                                  @click="gotoDeviceList(scope)">{{scope.row.processedNum}}</span>
                        </template>
                    </el-table-column>
                    <el-table-column prop="beyondRepairNum"
                                     label="不可修复设备数量"
                                     min-width="110"
                                     show-overflow-tooltip>
                        <template slot-scope="scope">
                            <span class="blue pointer"
                                  @click="gotoDeviceList(scope)">{{scope.row.beyondRepairNum}}</span>
                        </template>
                    </el-table-column>
                    <el-table-column prop="waitRepairNum"
                                     label="未修复设备数量"
                                     min-width="100"
                                     show-overflow-tooltip>
                        <template slot-scope="scope">
                            <span class="blue pointer"
                                  @click="gotoDeviceList(scope)">{{scope.row.waitRepairNum}}</span>
                        </template>
                    </el-table-column>
                </el-table>
            </div>
        </el-form>

        <!-- 设备列表弹框 -->
        <el-dialog width="90%"
                   class="yw-dialog"
                   title="设备列表"
                   v-if="deviceListBoxShow"
                   :visible.sync="deviceListBoxShow"
                   :append-to-body="true"
                   :destroy-on-close="true"
                   :close-on-click-modal="false">
            <div class="yw-dialog-main">
                <bug-device-list :currentRowData="currentRowData"
                                 :currentProp="currentProp"
                                 :isReadonly="true"
                                 :originWorkData="originWorkData"></bug-device-list>
            </div>
        </el-dialog>

    </div>
</template>

<script>
    import searchBox from 'src/pages/auto_operation/bug_manage/scanning_results/search-box.vue'
    import bugDeviceList from 'src/pages/auto_operation/bug_manage/scanning_results/bug-device-list.vue'
    import bugManageService from 'src/services/auto_operation/rb-auto-operation-bug-manage-services.js'
    import rbAutoOperationWorkServicesFactory from 'src/services/auto_operation/rb-auto-operation-work-services.factory.js'
    import rbAutoOperationMixin from 'src/services/auto_operation/rb-auto-operation-mixin.js'
    import { createDownloadFile } from 'src/utils/utils.js'

    export default {
        name: 'ScanningResultsIndex',
        components: {
            searchBox,
            bugDeviceList,
        },
        data() {
            return {
                pageLoading: false,
                loading_text: '请稍候...',
                dataList: [], // 作业列表
                groupList: [], // 分组列表
                formSearch: {
                    groupName: '',
                    name: '',
                    canFixed: '',
                    riskLevel: '',
                },
                currentRowData: {}, // 当前行内容
                currentProp: '',
                deviceListBoxShow: false, // 设备列表弹框
                pipelineIdList: [], // 关联的作业id列表
                // 漏洞类型作业数据
                originWorkData: {
                    dataList: [],
                    totalCount: 0
                },
                riskLevelList: [], // 漏洞等级列表

                curDeivceListParams: {}
            }
        },
        mixins: [rbAutoOperationMixin],
        created() {
            this.queryOpsPipelineList()
        },
        methods: {
            // 导出漏洞列表
            exportList() {
                this.$message('请稍候')
                bugManageService.exportVulnerabilityReport(this.formSearch).then(res => {
                    if (res) {
                        this.$message.success('下载成功')
                        createDownloadFile(res, '设备列表文件.xls')
                    }
                })
            },
            // 触发查询漏洞类型作业列表
            searchList(req) {
                this.queryOpsPipelineList(req)
            },
            // 查询漏洞类型作业列表
            queryOpsPipelineList(req = {}) {
                req = Object.assign({
                    page_no: this.currentPage,
                    page_size: this.pageSize,
                    labelId: 'vulnerability'  // 漏洞类型： vulnerability
                }, req)
                rbAutoOperationWorkServicesFactory
                    .queryOpsPipelineList(req)
                    .then(res => {
                        this.originWorkData = res
                        this.originWorkData.dataList = res.dataList || []
                    })
                    .catch(error => {
                        this.showErrorTip(error)
                    })
            },
            updateFormSearch(params) {
                this.formSearch = this.$utils.deepClone(params)
            },
            // 查询列表
            search(pageNum) {
                this.currentPage = pageNum ? pageNum : this.currentPage
                let req = {
                    pageNo: this.currentPage,
                    pageSize: this.pageSize
                }
                req = Object.assign(req, this.formSearch)
                this.loading = true
                bugManageService
                    .queryVulnerabilityReport(req)
                    .then(res => {
                        this.loading = false
                        this.dataList = res
                    })
                    .catch(error => {
                        this.loading = false
                        this.showErrorTip(error)
                    })
            },
            reset() {
                this.$refs['formSearch'].resetFields()
                this.search()
            },
            getGroupName(groupId) {
                let groupName = ''
                this.groupList.forEach(item => {
                    if (item.vulnerability_group_id === groupId) {
                        groupName = item.name
                    }
                })
                return groupName
            },
            // 取消关闭弹框
            hideChooseBox() {
                this.deviceListBoxShow = false
            },
            // 打开设备列表弹框
            gotoDeviceList(scope) {
                this.currentRowData = scope.row
                this.currentProp = scope.column.property
                this.deviceListBoxShow = true
            },
        }
    }
</script>

<style lang="scss" scoped>
</style>
