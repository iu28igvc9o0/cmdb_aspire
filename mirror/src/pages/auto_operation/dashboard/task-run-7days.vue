
<template>
    <div class="content-chart"
         style="width:49.4%;">

        <section class="chart-title-wrap clearfix">
            <svg class="svg-icon svg-icon-24"
                 aria-hidden="true">
                <use xlink:href="#iconrenwuzhihang"></use>
            </svg>
            <span class="chart-title">7天任务执行情况</span>
        </section>

        <!-- 图表 -->
        <section class="chart-section">
            <div class="chart-box-wrap">
                <div id="historicalTaskLine"
                     class="chart-box-item"
                     style="width: 100%; height: 100%;">
                </div>
            </div>
        </section>
    </div>
</template>
 
<script>
    import rbAutoOperationHomeServicesFactory from 'src/services/auto_operation/rb-auto-operation-home-services.factory.js'
    import ChartOption from 'src/utils/chartOption'
    import DrawChart from 'src/utils/drawCharts'
    export default {
        components: {},
        data() {
            return {}
        },
        computed: {},
        created() {
            this.queryChartData()
        },
        mixins: [DrawChart],
        methods: {
            initChart(chartData) {
                this.$nextTick(() => {
                    let myChart = this.initMyChart('historicalTaskLine')
                    myChart.clear()
                    let option = JSON.parse(JSON.stringify(ChartOption['line-option']))
                    // let colors = ChartOption['color-option']['linearColor']

                    option.legend.data = ['执行成功', '总数']
                    option.textStyle.color = '#9597AB'

                    option.title.show = true
                    option.title.text = ''
                    option.title.subtext = '任务数(个)'
                    option.title.subtextStyle.color = '#fff'

                    // 默认展示全部数据
                    option.dataZoom[0].start = 0
                    option.dataZoom[0].end = 100

                    option.xAxis.data = chartData.days
                    option.xAxis.axisLabel.rotate = 0

                    option.color = ['blue', 'green']

                    option.series = [
                        {
                            name: '执行成功',
                            type: 'line',
                            data: chartData.successCounts,
                            smooth: true,
                            lineStyle: {
                                width: 4,
                                color: {
                                    type: 'linear',
                                    x: 0,
                                    y: 0,
                                    x2: 1,
                                    y2: 0,
                                    colorStops: [
                                        {
                                            offset: 0,
                                            color: '#004CD8' // 0% 处的颜色
                                        },
                                        {
                                            offset: 1,
                                            color: '#006CFF' // 100% 处的颜色
                                        }
                                    ],
                                    global: false // 缺省为 false
                                }

                            }
                        },
                        {
                            name: '总数',
                            type: 'line',
                            data: chartData.totalCounts,
                            smooth: true,
                            lineStyle: {
                                width: 4,
                                color: {
                                    type: 'linear',
                                    x: 0,
                                    y: 0,
                                    x2: 1,
                                    y2: 0,
                                    colorStops: [
                                        {
                                            offset: 0,
                                            color: '#02BD6C' // 0% 处的颜色
                                        },
                                        {
                                            offset: 1,
                                            color: '#4BFF9B' // 100% 处的颜色
                                        }
                                    ],
                                    global: false // 缺省为 false
                                }

                            }
                        }
                    ]
                    // 绘制图表
                    myChart.setOption(option, false)
                    this.setResizeFun(myChart)
                })
            },
            // 7天任务执行情况
            queryChartData() {
                rbAutoOperationHomeServicesFactory
                    .queryNewIndexPipeInstStatisticByLine7Days()
                    .then(res => {
                        let chartData = {
                            days: [],
                            successCounts: [],
                            totalCounts: []
                        }
                        res.forEach(item => {
                            chartData.days.push(item.day.substring(5))
                            chartData.successCounts.push(item.successCount)
                            chartData.totalCounts.push(item.totalCount)
                        })

                        this.initChart(chartData)
                    })
            },
        }
    }
</script>
 
<style  lang="scss" scoped>
    .mtop15 {
        margin-top: 15px;
    }
</style>
