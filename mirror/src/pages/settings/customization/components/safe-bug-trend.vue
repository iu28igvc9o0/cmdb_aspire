<template>
    <div class="content-chart"
         style="width: 24.2%;">
        <section class="chart-title-wrap clearfix">
            <svg class="svg-icon svg-icon-24"
                 aria-hidden="true">
                <use xlink:href="#iconanquanloudongqushi"></use>
            </svg>
            <span class="chart-title">{{chartData.name}}</span>
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

    export default {
        mixins: [DrawChart],
        components: {
        },
        data() {
            return {
                chartData: {
                    name: '安全漏洞趋势',
                    chartList: [{ id: 'safe-bug-trend-1', chartOption: 'bar-line-option', chartDatas: '' },
                    ],
                }
            }
        },
        methods: {
            // 获得数据
            getDatas() {

                let params = {
                    beginDate: this.conditionParams.dateRange[0],
                    endDate: this.conditionParams.dateRange[1],
                }

                this.$api.querySafeScanTrend(params).then((res) => {
                    this.chartData.chartList[0].chartDatas = res.result
                    this.drawCharts()
                })

            },

            // 绘制图表
            drawCharts() {
                this.$nextTick(() => {

                    this.chartData.chartList.forEach((subItem) => {
                        let myChart = echarts.init(document.getElementById(subItem.id))
                        myChart.clear()
                        // 数据格式处理
                        let option = JSON.parse(JSON.stringify(ChartOption[subItem.chartOption]))
                        let chartDatas = JSON.parse(JSON.stringify(subItem.chartDatas))
                        let colors = ChartOption['color-option']['linearColor']

                        option.series[0] = JSON.parse(JSON.stringify(option.series[0]))
                        option.series[1] = JSON.parse(JSON.stringify(option.series[1]))
                        option.series[2] = JSON.parse(JSON.stringify(option.series[1]))

                        option.series[0].name = '业务系统'
                        option.series[1].name = '总漏洞'
                        option.series[2].name = '已修复漏洞'
                        option.legend.data = [option.series[0].name, option.series[1].name, option.series[2].name]

                        option.series[0].itemStyle.color.colorStops = colors.blueSingle
                        option.series[1].lineStyle.color.colorStops = colors.orangeSingle
                        option.series[2].lineStyle.color.colorStops = colors.greenSingle
                        option.series[2].itemStyle.color = '#B1FF2B'

                        option.yAxis[0].name = '业务系统数(个)'
                        option.yAxis[1].name = '漏洞数(个)'
                        option.yAxis[0].nameTextStyle.padding = [0, 0, 0, 30]
                        option.yAxis[1].axisLabel.formatter = '{value}'

                        let dataList = chartDatas
                        if (dataList && dataList.length > 0) {
                            option.series[0].data = []
                            option.series[1].data = []
                            option.series[2].data = []
                            option.xAxis.data = []
                            dataList.reverse().forEach((item) => {
                                option.series[0].data.push(item.bizCount)
                                option.series[1].data.push(item.count)
                                option.series[2].data.push(item.rCount)
                                option.xAxis.data.push(item['scan_date'])
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

