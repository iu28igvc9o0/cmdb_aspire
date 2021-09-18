
<template>
    <div class="mtop15">
        <el-card class="box-card">
            <div slot="header">
                <span style="font-weight: bold">任务执行时长统计</span>
            </div>
            <div id="taskDuration"
                 style="width: 100%; height: 250px;"></div>
        </el-card>
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
        mounted() {
            this.queryChartData()
        },
        mixins: [DrawChart],
        methods: {
            initChart(chartData) {
                this.$nextTick(() => {
                    let myChart = this.initMyChart('taskDuration')
                    myChart.clear()
                    let option = JSON.parse(JSON.stringify(ChartOption['bar-option']))
                    let colors = ChartOption['color-option']['linearColor']

                    option.textStyle.color = '#666'
                    option.legend.textStyle.color = '#9c9c9c'

                    option.yAxis[0].name = '任务数(个)'
                    option.yAxis[0].nameTextStyle.fontSize = '14px'
                    option.yAxis[0].nameTextStyle.color = '#9c9c9c'
                    option.yAxis[0].nameTextStyle.padding = [0, 0, 0, -40]
                    option.yAxis[0].splitLine.lineStyle.color = '#DCDFE6'

                    option.xAxis.axisLabel.rotate = '30'
                    option.xAxis.axisLine.lineStyle.color = '#ccc'
                    option.xAxis.data = ['1分钟内', '1到3分钟内', '3到5分钟内', '5到10分钟内', '10到30分钟内', '30分钟以上']

                    option.series[0].itemStyle.color.colorStops = colors.blue
                    option.series[0].label.show = true
                    option.series[0].name = '完成个数'
                    option.series[0].data = chartData

                    // 默认展示全部数据
                    option.dataZoom[0].start = 0
                    option.dataZoom[0].end = 100
                    option.dataZoom[0].show = false

                    option.grid = {
                        left: 10,
                        right: 5,
                        top: 30,
                        bottom: -8,
                        containLabel: true
                    }
                    // 绘制图表
                    myChart.setOption(option, false)
                    this.setResizeFun(myChart)
                })
            },
            queryChartData() {
                rbAutoOperationHomeServicesFactory
                    .queryIndexRunTimeSpanStatistic()
                    .then(res => {
                        let chartData = [
                            res.inOneMinuteCount,
                            res.amongOne2ThreeMinutesCount,
                            res.amongThree2FiveMinutesCount,
                            res.amongFive2TenMinutesCount,
                            res.amongTen2ThirtyMinutesCount,
                            res.moreThanThirtyMinutesCount,
                        ]
                        this.initChart(chartData)
                    })
            }
        }
    }
</script>
 
<style  lang="scss" scoped>
    .mtop15 {
        margin-top: 15px;
    }
</style>
