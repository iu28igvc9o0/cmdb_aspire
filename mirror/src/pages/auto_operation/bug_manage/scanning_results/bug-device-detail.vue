<!-- 漏洞管理：漏洞相关的设备详情 -->
<template>
    <div v-loading.fullscreen.lock="pageLoading"
         :element-loading-text="loading_text">

        <table class="bordered mtop20">
            <tr>
                <td class="bold"
                    width="90">一级部门</td>
                <td width="20%">{{deviceDetailInfo.department1}}</td>
                <td class="bold"
                    width="110">二级部门</td>
                <td width="20%">{{deviceDetailInfo.department2}}</td>
                <td class="bold"
                    width="110">业务系统</td>
                <td width="20%">{{deviceDetailInfo.bizSystem}}</td>
                <td class="bold"
                    width="110">操作系统</td>
                <td>{{deviceDetailInfo.osType}}</td>
            </tr>
            <tr>
                <td class="bold">IP地址</td>
                <td>{{deviceDetailInfo.hostIp}}</td>
                <td class="bold">高危漏洞数量</td>
                <td>{{deviceDetailInfo.highNum}}</td>
                <td class="bold">中危漏洞数量</td>
                <td>{{deviceDetailInfo.midNum}}</td>
                <td class="bold">低危漏洞数量</td>
                <td>{{deviceDetailInfo.lowNum}}</td>
            </tr>
        </table>

        <el-form class="yw-form mtop10">
            <el-button type="text"
                       :disabled="loading"
                       icon="el-icon-download"
                       @click="exportList">
                导出
            </el-button>

            <div class="yw-el-table-wrap">
                <el-table :data="dataList"
                          class="yw-el-table"
                          stripe
                          tooltip-effect="dark"
                          border
                          height="calc(100vh - 340px)"
                          v-loading="loading">
                    <el-table-column prop="name"
                                     label="漏洞名称"
                                     min-width="160"
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
                    <el-table-column prop="status"
                                     label="修复状态"
                                     min-width="70"
                                     show-overflow-tooltip></el-table-column>
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

    </div>
</template>

<script>
    import bugManageService from 'src/services/auto_operation/rb-auto-operation-bug-manage-services.js'
    import rbAutoOperationMixin from 'src/services/auto_operation/rb-auto-operation-mixin.js'
    import { createDownloadFile } from 'src/utils/utils.js'

    export default {
        name: 'ScanningResultsBugDeviceDetail',
        props: {
            deviceData: {
                type: Object,
                default() {
                    return {}
                }
            },
        },
        data() {
            return {
                pageLoading: false,
                loading_text: '请稍候...',
                dataList: [], // 作业列表
                deviceDetailInfo: {}
            }
        },
        mixins: [rbAutoOperationMixin],
        created() {
            this.search()
            this.getVulnerabilityInstanceDetailById()
        },
        methods: {
            // 导出漏洞列表
            exportList() {
                let req = {
                    hostIp: this.deviceData.hostIp,
                    poolName: this.deviceData.poolName
                }
                this.$message('请稍候')
                bugManageService.exportVulnerability(req).then(res => {
                    if (res) {
                        this.$message.success('下载成功')
                        createDownloadFile(res, '漏洞列表文件.xls')
                    }
                })
            },

            // 查询列表
            search() {
                let req = {
                    pageNo: this.currentPage,
                    pageSize: this.pageSize,
                    hostIp: this.deviceData.hostIp,
                    poolName: this.deviceData.poolName
                }
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
            // 查询设备详情
            getVulnerabilityInstanceDetailById() {
                let req = {
                    vulInstanceId: this.deviceData.id
                }
                bugManageService
                    .getVulnerabilityInstanceDetailById(req)
                    .then(res => {
                        this.deviceDetailInfo = res
                    })
                    .catch(error => {
                        this.showErrorTip(error)
                    })
            },
        }
    }
</script>

<style lang="scss" scoped>
</style>
