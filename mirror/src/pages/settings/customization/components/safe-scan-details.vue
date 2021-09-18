<template>

    <div class="content-chart"
         style="width: 24.2%;">
        <section class="chart-title-wrap clearfix">
            <svg class="svg-icon svg-icon-24"
                 aria-hidden="true">
                <use xlink:href="#iconanquansaomiaojieguomingxi"></use>
            </svg>
            <span class="chart-title">{{chartData.name}}</span>
            <div class="chart-filter">
                <el-button class="chart-filter-item yw-chart-button-wrap"
                           type="primary"
                           @click="exportDatas">
                    <svg class="svg-icon svg-icon-20"
                         style="margin:0 5px 0 0;"
                         aria-hidden="true">
                        <use xlink:href="#icondaochu1"></use>
                    </svg><span class="inline-block-middle">导出</span>
                </el-button>
            </div>
        </section>

        <!-- 多图表 -->
        <section class="chart-section">

            <div class="yw-rank-table-wrap has-pagination">
                <vue-scroll style="width:100%;height:100%;">
                    <el-table class="yw-rank-table"
                              style="min-width:100%;width:430px;"
                              :default-sort="{prop: 'scan_date',order: 'descending'}"
                              :data="chartData.chartList[0].tableDatas">
                        <el-table-column label="扫描时间"
                                         prop="scan_date"
                                         sortable
                                         :width="160">
                        </el-table-column>
                        <el-table-column label="业务系统数">
                            <template slot-scope="scope">
                                <el-tooltip effect="dark"
                                            :content="scope.row.bizCount"
                                            placement="top-start">
                                    <span class="text-ellipse">
                                        {{ scope.row.bizCount}}
                                    </span>
                                </el-tooltip>
                            </template>
                        </el-table-column>
                        <el-table-column label="漏洞总数">
                            <template slot-scope="scope">
                                <el-tooltip effect="dark"
                                            :content="scope.row.count"
                                            placement="top-start">
                                    <span class="text-ellipse">
                                        {{ scope.row.count}}
                                    </span>
                                </el-tooltip>
                            </template>
                        </el-table-column>
                        <el-table-column label="已修复漏洞">
                            <template slot-scope="scope">
                                <el-tooltip effect="dark"
                                            :content="scope.row.rCount"
                                            placement="top-start">
                                    <span class="text-ellipse">
                                        {{ scope.row.rCount}}
                                    </span>
                                </el-tooltip>
                            </template>
                        </el-table-column>
                    </el-table>
                </vue-scroll>
            </div>

            <YwPagination class="page-chart"
                          layout='total, prev, pager, next, jumper'
                          :pager-count="3"
                          @handleSizeChange="handleSizeChange"
                          @handleCurrentChange="handleCurrentChange"
                          :current-page="currentPage"
                          :page-sizes="pageSizes"
                          :page-size="pageSize"
                          :total="total"></YwPagination>
        </section>
        <!-- 多图表 -->
    </div>
</template>

<script>
    import QueryObject from 'src/utils/queryObject.js'

    export default {
        mixins: [QueryObject],
        components: {
            YwPagination: () => import('src/components/common/yw-pagination.vue'),
        },
        data() {
            return {
                // 查询条件
                queryParams: {

                },
                chartData: {
                    name: '安全扫描结果明细',
                    chartList: [{ tableTitle: [], tableDatas: [] }],
                }
            }
        },
        methods: {
            // 设置参数
            setParams(activePagination) {

                if (activePagination) {
                    this.queryParams['begin'] = this.currentPage
                    this.queryParams['pageSize'] = this.pageSize
                } else {
                    this.queryParams = {
                        'beginDate': this.conditionParams.dateRange[0],
                        'endDate': this.conditionParams.dateRange[1],
                        'begin': this.initPageChange(),
                        'pageSize': this.pageSize,
                    }
                }

            },
            // 查询
            query(activePagination = false) {

                this.setParams(activePagination)

                this.$api.querySafeScanTrend(this.queryParams).then((res) => {
                    this.total = res.count
                    this.chartData.chartList[0].tableDatas = res.result
                })
            },

            // 导出
            exportDatas() {
                let params = {
                    beginDate: this.conditionParams.dateRange[0],
                    endDate: this.conditionParams.dateRange[1],
                }
                this.rbHttp.sendRequest({
                    method: 'POST',
                    params: params,
                    binary: true,
                    url: '/v1/alerts/leak-scan/exportScanResult'
                }).then((res) => {
                    this.exportFiles({
                        data: res,
                        fileType: 'application/vnd.ms-excel',
                        fileName: '安全扫描结果明细.xlsx'
                    })
                    return res
                })
            },

        },
        mounted() {
            this.pageSize = 6
            this.query()
        }

    }
</script>

<style lang="scss" scoped>
    /deep/.chart-section {
        padding-top: 15px;
        padding-bottom: 15px;
    }
</style>

