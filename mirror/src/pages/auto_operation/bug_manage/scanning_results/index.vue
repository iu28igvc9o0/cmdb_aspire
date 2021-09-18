<!-- 漏洞管理：漏洞扫描结果 -->
<template>
    <div class="components-container yw-dashboard"
         v-loading.fullscreen.lock="pageLoading"
         :element-loading-text="loading_text">

        <!-- 搜索组件 -->
        <search-box :isScanningResult="true"
                    @search="search"
                    @updateFormSearch="updateFormSearch"></search-box>

        <el-form class="yw-form">
            <div ref="operateBox"
                 class="table-operate-wrap clearfix">
                <el-button type="text"
                           icon="el-icon-download"
                           @click="exportList">导出</el-button>
                <el-button type="text"
                           icon="el-icon-download"
                           @click="exampleList">导出漏洞实例列表</el-button>
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
                    <el-table-column prop="name"
                                     label="漏洞名称"
                                     min-width="180"
                                     show-overflow-tooltip></el-table-column>
                    <el-table-column prop="id"
                                     label="漏洞编号"
                                     show-overflow-tooltip
                                     width="100"></el-table-column>
                    <el-table-column prop="cveNumber"
                                     label="CVE编码"
                                     show-overflow-tooltip
                                     width="90"></el-table-column>
                    <el-table-column prop="cncveNumber"
                                     label="CNCVE编码"
                                     show-overflow-tooltip
                                     width="90"></el-table-column>
                    <el-table-column prop="cnvdNumber"
                                     label="CNVD编码"
                                     show-overflow-tooltip
                                     width="90"></el-table-column>
                    <el-table-column prop="cnnvdNumber"
                                     label="CNNVD编码"
                                     show-overflow-tooltip
                                     width="100"></el-table-column>
                    <el-table-column prop="port"
                                     label="端口"
                                     show-overflow-tooltip
                                     width="60"></el-table-column>
                    <el-table-column prop="protocol"
                                     label="协议"
                                     show-overflow-tooltip
                                     width="90"></el-table-column>
                    <el-table-column prop="service"
                                     label="服务"
                                     show-overflow-tooltip
                                     width="90"></el-table-column>
                    <el-table-column prop="riskLevelDesc"
                                     label="漏洞级别"
                                     show-overflow-tooltip
                                     width="90"></el-table-column>
                    <el-table-column prop="describe"
                                     label="漏洞描述"
                                     min-width="180"
                                     show-overflow-tooltip></el-table-column>
                    <el-table-column prop="deviceNum"
                                     label="设备数量"
                                     width="80"
                                     show-overflow-tooltip></el-table-column>
                    <el-table-column label="操作"
                                     fixed="right"
                                     width="80">
                        <template slot-scope="scope">
                            <el-button type="text"
                                       title="关联作业"
                                       icon="el-icon-connection"
                                       @click="chooseWork(scope.row)"></el-button>
                            <el-button type="text"
                                       title="查看漏洞设备列表"
                                       icon="el-icon-view"
                                       @click="gotoDeviceList(scope.row)"></el-button>
                            <!-- <el-button type="text"
                                       title="修复"
                                       @click="bugFix(scope.row)">
                                <i class="icon iconfont f14">&#xe62b;</i>
                            </el-button> -->
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
                               layout="total, sizes, prev, pager, next, jumper"></el-pagination>
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
                                 :formSearchParams="formSearch"
                                 :originWorkData="originWorkData"
                                 :originWorkBackData="originWorkBackData"
                                 :originWorkRecheckData="originWorkRecheckData"></bug-device-list>
            </div>
        </el-dialog>

        <!-- 关联作业弹框 -->
        <el-dialog width="90%"
                   class="yw-dialog"
                   title="关联作业"
                   v-if="chooseWorkBoxShow"
                   :visible.sync="chooseWorkBoxShow"
                   :append-to-body="true"
                   :destroy-on-close="true"
                   :close-on-click-modal="false">
            <section class="yw-dialog-main">
                <works-choose :currentRowData="currentRowData"
                              :originWorkData="originWorkData"
                              propKey="pipelineIdList"
                              workType="选择修复作业"
                              @setSelectedKey="setSelectedKey"></works-choose>
                <works-choose :currentRowData="currentRowData"
                              :originWorkData="originWorkBackData"
                              propKey="goBackPipelineIdList"
                              workType="选择回退作业"
                              @setSelectedKey="setSelectedBackKey"></works-choose>
                <works-choose :currentRowData="currentRowData"
                              :originWorkData="originWorkRecheckData"
                              propKey="recheckPipelineIdList"
                              workType="选择复查作业"
                              @setSelectedKey="setSelectedRecheckKey"></works-choose>
                <div class="fixed-bottom-box t-center">
                    <el-button type="primary"
                               @click="checkSelectedList">确定
                    </el-button>
                    <el-button @click="hideChooseBox">取消</el-button>
                </div>
            </section>
        </el-dialog>

        <!-- 执行作业 -->
        <run-work-dialog></run-work-dialog>

    </div>
</template>

<script>
    import worksChoose from 'src/pages/auto_operation/bug_manage/works-choose.vue'

    import bugDeviceList from 'src/pages/auto_operation/bug_manage/scanning_results/bug-device-list.vue'
    import searchBox from 'src/pages/auto_operation/bug_manage/scanning_results/search-box.vue'
    import bugFix from 'src/pages/auto_operation/bug_manage/bug-fix.vue'
    import runWorkDialog from 'src/pages/auto_operation/work_manage/task/run-work-dialog.vue'
    import rbAutoOperationWorkServicesFactory from 'src/services/auto_operation/rb-auto-operation-work-services.factory.js'
    import bugManageService from 'src/services/auto_operation/rb-auto-operation-bug-manage-services.js'
    import rbAutoOperationMixin from 'src/services/auto_operation/rb-auto-operation-mixin.js'
    import { createDownloadFile } from 'src/utils/utils.js'
    import rbAutoOperationCustomParameterFactory from 'src/services/auto_operation/rb-auto-operation-custom-parameter.factory.js'

    export default {
        name: 'ScanningResultsIndex',
        components: {
            worksChoose,
            bugDeviceList,
            searchBox,
            bugFix,
            runWorkDialog,
        },
        data() {
            return {
                pageLoading: false,
                loading_text: '请稍候...',
                dataList: [], // 作业列表
                goBackPipelineIdList: [], // 关联的作业id列表
                recheckPipelineIdList: [],// 复查作业
                formSearch: {
                    name: '',
                    canFixed: '',
                    riskLevel: '',
                    id: '',
                    protocol: '',
                    service: '',
                    department1Index: '',
                    department1: '',
                    department2: '',
                    bizSystem: '',
                    osType: '',
                },
                currentRowData: {}, // 当前行内容
                chooseWorkBoxShow: false, // 关联作业弹框
                deviceListBoxShow: false, // 关联作业弹框
                bugFixBoxShow: false, // 修复漏洞弹框
                pipelineIdList: [], // 关联的作业id列表
                // 漏洞类型作业数据
                originWorkData: {
                    dataList: [],
                    backList: [],
                    totalCount: 0
                },
                // 漏洞类型作业数据
                originWorkBackData: {
                    dataList: [],
                    totalCount: 0
                },
                // 漏洞类型作业数据
                originWorkRecheckData: {
                    dataList: [],
                    totalCount: 0
                },
            }
        },
        mixins: [rbAutoOperationMixin],
        created() {
            this.searchWorkList()
        },
        methods: {
            exportList() {
                this.$message('请稍候')
                bugManageService.exportVulnerability(this.formSearch).then(res => {
                    if (res) {
                        this.$message.success('下载成功')
                        createDownloadFile(res, '漏洞列表文件.xls')
                    }
                })
            },
            exampleList() {
                this.$message('请稍候')

                rbAutoOperationCustomParameterFactory.exportVulInstance(this.formSearch).then(res => {
                    if (res) {
                        this.$message.success('下载成功')
                        createDownloadFile(res, '漏洞实例列表文件.xls')
                    }
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
                    pageSize: this.pageSize,
                    orderColumn: 'risk_level',
                    orderType: 'asc'

                }
                req = Object.assign(req, this.formSearch)
                this.loading = true
                bugManageService
                    .queryVulnerabilityList(req)
                    .then(res => {
                        this.loading = false
                        this.total = res.totalCount
                        this.dataList = res.dataList
                    })
                    .catch(error => {
                        this.loading = false
                        this.showErrorTip(error)
                    })
            },
            setSelectedKey(data) {
                this.pipelineIdList = data
            },
            setSelectedBackKey(data) {
                this.goBackPipelineIdList = data
            },
            setSelectedRecheckKey(data) {
                this.recheckPipelineIdList = data
            },
            // 取消关闭弹框
            hideChooseBox() {
                this.chooseWorkBoxShow = false
            },
            // 打开关联作业弹框
            chooseWork(row) {
                this.currentRowData = row
                this.chooseWorkBoxShow = true
            },
            // 触发查询漏洞类型作业列表
            searchWorkList() {
                this.queryOpsPipelineList('vulnerability')
                this.queryOpsPipelineList('vulnerability_goback')
                this.queryOpsPipelineList('vulnerability_recheck')
            },
            // 查询漏洞类型作业列表
            queryOpsPipelineList(type) {
                let req = {
                    page_no: this.currentPage,
                    page_size: this.pageSize,
                    labelId: type  // 漏洞类型： vulnerability
                }
                rbAutoOperationWorkServicesFactory
                    .queryOpsPipelineList(req)
                    .then(res => {
                        if (type === 'vulnerability') {
                            this.originWorkData = res
                            this.originWorkData.dataList = res.dataList || []
                        } else if (type === 'vulnerability_goback') {
                            this.originWorkBackData = res
                            this.originWorkBackData.dataList = res.dataList || []
                        } else {
                            this.originWorkRecheckData = res
                            this.originWorkRecheckData.dataList = res.dataList || []
                        }
                    })
                    .catch(error => {
                        this.showErrorTip(error)
                    })
            },
            // 关联作业
            checkSelectedList() {
                if (!this.pipelineIdList.length && !this.goBackPipelineIdList.length && !this.recheckPipelineIdList.length) {
                    this.$message.warning('请先选择作业！')
                    return
                }
                this.pageLoading = true
                let req = {
                    id: this.currentRowData.id, // id
                    pipelineIdList: this.pipelineIdList,    // 关联的作业id列表
                    goBackPipelineIdList: this.goBackPipelineIdList,    // 关联的作业id列表
                    recheckPipelineIdList: this.recheckPipelineIdList,

                }
                this.updateVulnerability(req)
            },
            // 更新漏洞 接口
            updateVulnerability(req) {
                bugManageService
                    .updateVulnerability(req)
                    .then(() => {
                        this.$message.success('操作成功')
                        this.search()
                        this.pageLoading = false
                        this.hideChooseBox()
                    })
                    .catch(error => {
                        this.pageLoading = false
                        this.hideChooseBox()
                        this.showErrorTip(error)
                    })
            },
            // 打开设备列表弹框
            gotoDeviceList(row) {
                this.currentRowData = row
                this.deviceListBoxShow = true
            },
            // 打开漏洞详情，选中主机进行修复
            bugFix(row) {
                this.currentRowData = row
                this.bugFixBoxShow = true
            },
        }
    }
</script>

<style lang="scss" scoped>
</style>
