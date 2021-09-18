
<template>
    <!-- 7天任务执行时长分布 -->
    <div class="content-chart"
         style="width: 24.2%;">
        <section class="chart-title-wrap clearfix">
            <svg class="svg-icon svg-icon-24"
                 aria-hidden="true">
                <use xlink:href="#iconrenwuzhihangshichang"></use>
            </svg>
            <span class="chart-title">7天任务执行时长分布</span>
        </section>

        <!-- 图表 -->
        <section class="chart-section">
            <div class="chart-box-wrap">
                <div class="chart-box-item"
                     style="width:100%;"
                     id="taskDuration"></div>
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
                    let myChart = this.initMyChart('taskDuration')
                    myChart.clear()
                    let option = JSON.parse(JSON.stringify(ChartOption['bar-option']))

                    option.title.show = true
                    option.title.text = '任务数(个)'
                    option.title.textStyle.fontWeight = 'normal'
                    option.title.textStyle.color = '#fff'

                    // 默认展示全部数据
                    option.dataZoom[0].start = 0
                    option.dataZoom[0].end = 100

                    option.xAxis.axisLabel.rotate = 30
                    option.xAxis.data = ['1分钟内', '1到3分钟内', '3到5分钟内', '5到10分钟内', '10到30分钟内', '30分钟以上']

                    option.dataZoom[0].show = false
                    option.series[0].label.show = true
                    option.series[0].name = '完成个数'
                    option.series[0].data = chartData

                    option.grid = {
                        left: 10,
                        right: 5,
                        top: 30,
                        bottom: -8,
                        containLabel: true
                    }
                    // 绘制图表
                    myChart.setOption(option)
                    this.setResizeFun(myChart)
                })
            },
            // 7天任务执行时长分布
            queryChartData() {
                rbAutoOperationHomeServicesFactory
                    .queryNewIndexPipeInstCostTimeStatistic7Days()
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
            },
        }
    }
</script>
 
<style  lang="scss" scoped>
    .mtop15 {
        margin-top: 15px;
    }
</style>
