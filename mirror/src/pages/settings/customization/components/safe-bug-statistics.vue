<template>

    <div class="content-chart"
         style="width: 100%;">
        <section class="chart-title-wrap clearfix">
            <svg class="svg-icon svg-icon-24"
                 aria-hidden="true">
                <use xlink:href="#iconyewutongji"></use>
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
                <el-radio-group class="yw-radio-button-wrap chart-filter-item"
                                v-model="chartData.activeFilter"
                                @change="changeTab">
                    <el-radio-button :label="tabItem.label"
                                     v-for="(tabItem,tabIndex) in chartData.filter"
                                     :key="tabIndex">{{tabItem.name}}</el-radio-button>
                </el-radio-group>
            </div>
        </section>

        <!-- 多图表 -->
        <section class="chart-section">
            <div class="chart-box-wrap">
                <div class="chart-box-item"
                     style="width:75%"
                     v-for="(subItem,subIndex) in chartData.chartList"
                     :key="subIndex"
                     :id="subItem.id"></div>
                <div class="chart-box-item"
                     style="width:24.2%;padding:15px 0 15px 15px;">
                    <!-- 表格 -->
                    <div class="yw-rank-table-wrap has-pagination">
                        <el-table class="yw-rank-table"
                                  style="width:100%"
                                  :data="chartData.tableDatas">
                            <el-table-column label="业务系统">
                                <template slot-scope="scope">
                                    <el-tooltip effect="dark"
                                                :content="scope.row.biz_line"
                                                placement="top-start">
                                        <span class="text-ellipse">
                                            {{ scope.row.biz_line}}
                                        </span>
                                    </el-tooltip>
                                </template>
                            </el-table-column>
                            <el-table-column label="总漏洞">
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
                    <!-- 表格 -->
                </div>
            </div>
        </section>
        <!-- 多图表 -->
    </div>
</template>

<script>
    import QueryObject from 'src/utils/queryObject.js'
    import ChartOption from 'src/utils/chartOption.js'
    import DrawChart from 'src/utils/drawCharts.js'
    import { fixedNumber } from 'src/assets/js/utility/rb-filters.js'

    export default {
        mixins: [DrawChart, QueryObject],
        components: {
            YwPagination: () => import('src/components/common/yw-pagination.vue'),
        },
        data() {
            return {
                // 查询条件
                queryParams: {

                },
                chartData: {
                    name: '业务系统漏洞统计',
                    filter: [{ name: '高危', label: 'high' }, { name: '中危', label: 'middle' }, { name: '低危', label: 'low' }],
                    activeFilter: 'high',
                    tableDatas: [],
                    chartList: [{ id: 'safe-bug-statistics-1', chartOption: 'bar-option', chartDatas: '' }]
                }
            }
        },
        methods: {
            // 获得数据
            getDatas() {

                let params = {
                    'beginDate': this.conditionParams.dateRange[0],
                    'endDate': this.conditionParams.dateRange[1],
                    'rankType': this.chartData.activeFilter
                }

                this.$api.querySafeBizStatistics(params).then((res) => {
                    this.chartData.chartList[0].chartDatas = res.result
                    this.drawCharts()
                })

            },

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
                        'rankType': this.chartData.activeFilter
                    }
                }

            },
            query(activePagination = false) {

                this.setParams(activePagination)

                this.$api.querySafeBizStatistics(this.queryParams).then((res) => {
                    this.total = res.count
                    this.chartData.tableDatas = res.result
                })

            },

            // 绘制图表
            drawCharts() {
                this.$nextTick(() => {
                    this.chartData.chartList.forEach((subItem) => {
                        let myChart = echarts.init(document.getElementById(subItem.id))
                        myChart.clear()
                        // 数据格式处理
                        switch (subItem.chartOption) {
                            case 'bar-option':
                                this.drawBar(myChart, subItem)
                                break
                        }
                    })
                })
            },
            drawBar(myChart, subItem) {

                let option = JSON.parse(JSON.stringify(ChartOption[subItem.chartOption]))
                let chartDatas = JSON.parse(JSON.stringify(subItem.chartDatas))
                let colors = ChartOption['color-option']['linearColor']
                option.dataZoom[0].show = true

                option.series[0] = JSON.parse(JSON.stringify(option.series[0]))
                option.series[1] = JSON.parse(JSON.stringify(option.series[0]))

                option.series[0].name = '总漏洞'
                option.series[1].name = '已修复漏洞'

                option.legend.data = [option.series[0].name, option.series[1].name]
                option.series[0].itemStyle.color.colorStops = colors.blue
                option.series[1].itemStyle.color.colorStops = colors.green

                option.title.show = true
                option.title.subtext = '漏洞数（个）'
                option.series[1].label.show = option.series[0].label.show = true
                option.series[1].label.formatter = option.series[0].label.formatter = (param) => {
                    return fixedNumber(param.data, 1, true) ? fixedNumber(param.data, 1, true) : ''
                }

                let dataList = chartDatas
                if (dataList && dataList.length > 0) {
                    option.series[0].data = []
                    option.series[1].data = []
                    option.xAxis.data = []
                    dataList.forEach((item) => {
                        option.series[0].data.push(item.count)
                        option.series[1].data.push(item.rCount)
                        option.xAxis.data.push(item.biz_line)
                    })
                } else {
                    option.series[0].data = [0]
                    option.xAxis.data = ['暂无']
                }

                myChart.setOption(option)

                // resize自适应
                this.setResizeFun(myChart)
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
                    url: '/v1/alerts/leak-scan/exportleakStatListByBizResult'
                }).then((res) => {
                    this.exportFiles({
                        data: res,
                        fileType: 'application/vnd.ms-excel',
                        fileName: '业务系统漏洞统计.xlsx'
                    })
                    return res
                })
            },
            changeTab() {
                this.query()
                this.getDatas()
            }
        },
        mounted() {
            this.pageSize = 6
            this.query()
            this.getDatas()
        }

    }
</script>

<style lang="scss" scoped>
    /deep/.chart-section {
        padding-left: 15px;
        padding-right: 15px;
    }
</style>

