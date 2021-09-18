<template>
    <div class="content-chart"
         style="width: 24.2%;">
        <section class="chart-title-wrap clearfix">
            <svg class="svg-icon svg-icon-24"
                 aria-hidden="true">
                <use xlink:href="#icongongdan"></use>
            </svg>
            <span class="chart-title">{{chartData.name}}</span>
            <div class="chart-filter">
                <el-date-picker class="yw-date-editor chart-filter-item"
                                style="width:190px"
                                v-model="chartData.dateRange"
                                @change="changeTab"
                                format="yyyy-MM-dd"
                                value-format="yyyy-MM-dd"
                                type="daterange"
                                :clearable="false"
                                range-separator="至"
                                start-placeholder="开始日期"
                                end-placeholder="结束日期">
                </el-date-picker>
            </div>
        </section>

        <!-- 多图表 -->
        <section class="chart-section">
            <div class="chart-box-wrap">
                <div class="chart-box-item"
                     style="width:1000px;height:300px"
                     v-for="(subItem,subIndex) in chartData.chartList"
                     :key="subIndex"
                     :id="subItem.id"></div>
            </div>
        </section>
        <!-- 多图表 -->
    </div>
</template>

<script>

    import ChartOption from 'src/utils/chartOption.js'
    import DrawChart from 'src/utils/drawCharts.js'
    import { formatDate2 } from 'src/assets/js/utility/rb-filters.js'

    export default {
        mixins: [DrawChart],
        components: {
        },
        data() {
            return {
                chartData: {
                    name: '工单统计',
                    dateRange: [formatDate2(new Date().getTime() - 1000 * 60 * 60 * 24 * 7), formatDate2(new Date())],
                    chartList: [{ id: 'order-chart-statistics-1', chartOption: 'bar-line-option', chartDatas: '' },
                    ],
                }
            }
        },
        methods: {
            // 获得数据
            getDatas() {

                let params = {
                    'startDate': this.chartData.dateRange[0] || '',
                    'endDate': this.chartData.dateRange[1] || '',
                }
                this.$api.queryOrderStatistics(params).then((res) => {
                    this.chartData.chartList[0].chartDatas = res
                    this.drawCharts()
                })

            },

            // 绘制图表
            drawCharts() {
                this.$nextTick(() => {
                    this.chartData.chartList.forEach((subItem) => {
                        // 非空判断，避免报错
                        let myChartDom = document.getElementById(subItem.id)
                        if (!myChartDom) {
                            return
                        }
                        let myChart = echarts.init(myChartDom)
                        myChart.clear()
                        // 数据格式处理
                        let option = JSON.parse(JSON.stringify(ChartOption[subItem.chartOption]))
                        let chartDatas = JSON.parse(JSON.stringify(subItem.chartDatas))
                        let colors = ChartOption['color-option']['linearColor']

                        option.series[3] = JSON.parse(JSON.stringify(option.series[1]))
                        option.series[0] = JSON.parse(JSON.stringify(option.series[0]))
                        option.series[1] = JSON.parse(JSON.stringify(option.series[0]))
                        option.series[2] = JSON.parse(JSON.stringify(option.series[0]))

                        option.series[0].name = '今日逾期'
                        option.series[1].name = '今日新增'
                        option.series[2].name = '今日完成'
                        option.series[3].name = '闭单率(%)'

                        option.legend.data = [option.series[0].name, option.series[1].name, option.series[2].name]
                        option.series[0].itemStyle.color.colorStops = colors.blueLight
                        option.series[1].itemStyle.color.colorStops = colors.yellow
                        option.series[2].itemStyle.color.colorStops = colors.green

                        option.series[0].stack = option.series[1].stack = option.series[2].stack = 'chart-stack'
                        // option.series[0].itemStyle.barBorderRadius = option.series[1].itemStyle.barBorderRadius = 0;
                        // option.series[2].itemStyle.barBorderRadius = [4, 4, 0, 0];

                        option.yAxis[0].name = '工单数(个)'
                        option.yAxis[1].name = ''
                        option.yAxis[1].name = '闭单率(%)'


                        if (chartDatas && chartDatas.length > 0) {
                            option.series[0].data = chartDatas.map((item) => {
                                return item.todayOverdue
                            })
                            option.series[1].data = chartDatas.map((item) => {
                                return item.todayAll
                            })
                            option.series[2].data = chartDatas.map((item) => {
                                return item.todayEnd
                            })
                            option.series[3].data = chartDatas.map((item) => {
                                let rate = item.endNumberRate
                                let percentIndex = rate.indexOf('%')
                                return Number(rate.substring(0, percentIndex))
                            })

                            option.xAxis.data = chartDatas.map((item) => {
                                return item.dateStr
                            })
                        } else {
                            option.series[0].data = [0]
                            option.xAxis.data = ['暂无']
                        }


                        myChart.setOption(option)

                        // resize自适应
                        this.setResizeFun(myChart)
                    })

                })
            },
            changeTab() {
                this.getDatas()
            },
        },
        mounted() {
            this.getDatas()
        }

    }
</script>

<style lang="scss" scoped>
</style>

