
<template>
    <div class="mtop15">
        <el-card class="box-card">
            <div slot="header">
                <span style="font-weight: bold">历史任务执行情况</span>
            </div>
            <div id="historicalTaskLine"
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
                    let myChart = this.initMyChart('historicalTaskLine')
                    myChart.clear()
                    let option = JSON.parse(JSON.stringify(ChartOption['line-option']))

                    option.textStyle.color = '#666'
                    option.legend.textStyle.color = '#9c9c9c'
                    option.legend.data = ['执行成功', '总数']

                    option.yAxis.name = '任务数(个)'
                    option.yAxis.nameTextStyle.fontSize = '14px'
                    option.yAxis.nameTextStyle.color = '#9c9c9c'
                    option.yAxis.nameTextStyle.padding = [0, 0, 0, -35]
                    option.yAxis.splitLine.lineStyle.color = '#DCDFE6'

                    option.xAxis.axisLine.lineStyle.color = '#ccc'
                    option.xAxis.data = chartData.days
                    // 默认展示全部数据
                    option.dataZoom[0].start = 0
                    option.dataZoom[0].end = 100

                    option.series = [
                        {
                            name: '执行成功',
                            type: 'line',
                            data: chartData.successCounts
                        },
                        {
                            name: '总数',
                            type: 'line',
                            data: chartData.totalCounts
                        }
                    ]
                    // 绘制图表
                    myChart.setOption(option, false)
                    this.setResizeFun(myChart)
                })
            },
            queryChartData() {
                rbAutoOperationHomeServicesFactory
                    .queryIndexRecent30DaysRunTrend()
                    .then(res => {
                        let chartData = {
                            days: [],
                            successCounts: [],
                            totalCounts: []
                        }
                        res.forEach(item => {
                            chartData.days.push(item.day)
                            chartData.successCounts.push(item.successCount)
                            chartData.totalCounts.push(item.totalCount)
                        })

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
