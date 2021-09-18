<template>

    <div class="content-chart"
         style="width: 24.2%;">
        <section class="chart-title-wrap clearfix">
            <svg class="svg-icon svg-icon-24"
                 aria-hidden="true">
                <use xlink:href="#icongongdanfenbu"></use>
            </svg>
            <span class="chart-title">7天各类型任务分布</span>
        </section>

        <!-- 图表 -->
        <section class="chart-section">
            <div class="chart-box-wrap">
                <div class="chart-box-item"
                     style="width:100%"
                     v-for="(subItem,subIndex) in chartData.chartList"
                     :key="subIndex"
                     :id="subItem.id"></div>
            </div>
        </section>
    </div>
</template>

<script>
    import rbAutoOperationHomeServicesFactory from 'src/services/auto_operation/rb-auto-operation-home-services.factory.js'
    import ChartOption from 'src/utils/chartOption.js'
    import DrawChart from 'src/utils/drawCharts.js'

    export default {
        mixins: [DrawChart],
        data() {
            return {
                chartData: {
                    name: '7天各类型任务分布',
                    chartList: [
                        { id: 'orderDistribution', chartOption: 'pie-option', chartDatas: {} },
                    ],
                }
            }
        },
        created() {
            this.queryNewIndexAllTaskTypeStatisticByPie7Days()
        },
        methods: {
            // 7天各类型任务分布
            queryNewIndexAllTaskTypeStatisticByPie7Days() {
                rbAutoOperationHomeServicesFactory
                    .queryNewIndexAllTaskTypeStatisticByPie7Days()
                    .then(res => {
                        this.chartData.chartList[0].chartDatas = res
                        this.drawCharts()
                    })
            },

            // 绘制图表
            drawCharts() {
                this.$nextTick(() => {
                    this.chartData.chartList.forEach((subItem) => {
                        let myChart = this.initMyChart(subItem.id)
                        myChart.clear()
                        // 数据格式处理
                        let option = JSON.parse(JSON.stringify(ChartOption[subItem.chartOption]))
                        let chartDatas = JSON.parse(JSON.stringify(subItem.chartDatas))
                        let colorSource = ChartOption['color-option']['linearColor']
                        // let colors = [colorSource.red, colorSource.orange, colorSource.yellow]
                        let colors = [colorSource.blue, colorSource.green, colorSource.yellow, colorSource.blueLight, colorSource.greenLight, colorSource.purple]
                        let valueArr = ['故障自愈任务', '手工任务', '自动任务', '巡检任务'], dataList = []

                        option.series[0] = JSON.parse(JSON.stringify(option.series[0]))
                        option.series[0].radius = [0, '60%']
                        option.series[0].center = ['50%', '60%']

                        let keys = Object.keys(chartDatas)
                        if (chartDatas && keys.length > 0) {
                            valueArr.forEach((valueName, index) => {
                                let obj = {
                                    name: valueName,
                                    value: chartDatas[keys[index]],
                                    label: {
                                        color: Object.values(colors)[index][1].color
                                    },
                                    itemStyle: {
                                        color: {
                                            type: 'linear',
                                            x: 0,
                                            y: 0, // 由上至下
                                            x2: 0,
                                            y2: 1,
                                            colorStops: Object.values(colors)[index]
                                        }
                                    }
                                }
                                dataList.push(obj)
                            })
                            option.series[0].data = dataList

                        } else {
                            option.series[0].data = [{
                                name: '暂无数据',
                                value: 0
                            }]
                        }

                        myChart.setOption(option)

                        // resize自适应
                        this.setResizeFun(myChart)
                    })

                })
            },
        }

    }
</script>

<style lang="scss" scoped>
</style>

