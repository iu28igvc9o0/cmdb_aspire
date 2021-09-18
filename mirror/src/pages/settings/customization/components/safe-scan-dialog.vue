<template>
    <!-- 扫描报告弹窗 -->
    <el-dialog class="yw-dialog"
               :visible.sync="dialogMsg.dialogVisible"
               width="800px"
               title="业务报告信息"
               :close-on-click-modal="false"
               :close-on-press-escape="false">
        <section class="yw-dialog-main">
            <el-form class="yw-form"
                     label-width="90px">
                <el-form-item label="归属业务"
                              class="text-left">
                    {{bizLine}}
                </el-form-item>
                <el-form-item label="最新扫描时间"
                              class="text-left">
                    {{scanDate}}
                </el-form-item>
            </el-form>
            <el-form class="yw-form">
                <div class="table-operate-wrap clearfix">

                </div>
                <div class="yw-el-table-wrap">
                    <el-table :data="reportData"
                              class="yw-el-table"
                              stripe
                              tooltip-effect="dark"
                              border>
                        <el-table-column prop="ip"
                                         label="IP地址">
                            <template slot-scope="scope">
                                <a :href="scope.row.reportPath"
                                   target="_blank">{{scope.row.ip}}</a>
                            </template>
                        </el-table-column>
                        <el-table-column prop="highLeaks"
                                         label="高危漏洞数量">
                        </el-table-column>
                        <el-table-column prop="mediumLeaks"
                                         label="中危漏洞数量">
                        </el-table-column>
                        <el-table-column prop="lowLeaks"
                                         label="低微漏洞">
                        </el-table-column>
                        <el-table-column prop="riskVal"
                                         label="风险值">
                        </el-table-column>
                    </el-table>
                </div>
                <YwPagination @handleSizeChange="handleSizeChange"
                              @handleCurrentChange="handleCurrentChange"
                              :current-page="currentPage"
                              :page-sizes="pageSizes"
                              :page-size="pageSize"
                              :total="total"></YwPagination>
            </el-form>
        </section>
    </el-dialog>
</template>

<script>
    import QueryObject from 'src/utils/queryObject.js'
    import rbAlertServiceFactory from 'src/services/alert/rb-alert-services.factory.js'
    import { formatDate2 } from 'src/assets/js/utility/rb-filters.js'

    export default {
        mixins: [QueryObject],
        props: ['dialogMsg'],
        components: {
            YwPagination: () => import('src/components/common/yw-pagination.vue'),
        },
        data () {
            return {
                // 查询参数
                queryParams: {},
                bizLine: '',
                scanDate: '',

                // 查询结果
                reportData: []
            }
        },
        methods: {
            // 设置参数
            setParams (activePagination) {

                if (activePagination) {
                    this.queryParams['page_no'] = this.currentPage
                    this.queryParams['page_size'] = this.pageSize
                } else {
                    this.queryParams = {
                        'scan_id': this.dialogMsg.id,
                        'page_no': this.initPageChange(),
                        'page_size': this.pageSize,
                    }
                }

            },
            query (activePagination = false) {

                this.setParams(activePagination)

                rbAlertServiceFactory.getSecurityLeakScanReport(this.queryParams).then(res => {
                    if (res.count > 0) {
                        this.total = res.count
                        this.reportData = res.result
                    }
                })

                rbAlertServiceFactory.getSecurityLeakScanById(this.dialogMsg.id).then(res => {
                    this.bizLine = res.bizLine
                    this.scanDate = formatDate2(res.scanDate)
                })

            },


        },
        created () {
            this.pageSize = 10
            this.query()
        }

    }
</script>

<style lang="scss" scoped>
</style>

