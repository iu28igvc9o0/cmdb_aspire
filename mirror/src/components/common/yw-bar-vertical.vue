<template>
    <!-- 柱状图：竖向 -->
    <div class="yw-chart-bar"
         :ref="barDatas.id"
         style="width: 100%;height: 100%;">
    </div>
</template>

<script>
    import ChartOption from 'src/utils/chartOption.js'
    import DrawChart from 'src/utils/drawCharts.js'
    import { fixedNumber } from 'src/assets/js/utility/rb-filters.js'
    export default {
        mixins: [DrawChart],
        props: ['barDatas'],
        data() {
            return {

            }
        },

        methods: {
            // 绘制图表
            drawCharts(subItem) {
                this.$nextTick(() => {

                    let myChart = echarts.init(this.$refs[subItem.id], null, { renderer: 'svg' })
                    myChart.clear()

                    // 数据格式处理
                    this.drawBar(myChart, subItem)

                })

            },

            drawBar(myChart, subItem) {

                let option = JSON.parse(JSON.stringify(ChartOption[subItem.chartOption]))
                let chartDatas = JSON.parse(JSON.stringify(subItem.chartDatas))

                // series
                this.barDatas.details.seriesName.forEach((seriesItem, index) => {
                    // 坐标轴
                    option.series[index] = JSON.parse(JSON.stringify(option.series[0]))
                    option.series[index].name = seriesItem.yLabelName
                    option.legend.show = false
                    option.series[index].label.show = true
                    option.series[index].label.formatter = (param) => {
                        return fixedNumber(param.data, 1, true) ? fixedNumber(param.data, 1, true) : ''
                    }

                    // 数据
                    option.series[index].data = chartDatas.map((item) => {
                        return item[seriesItem.yLabel]
                    })
                    option.xAxis.data = chartDatas.map((item) => {
                        return item[seriesItem.xLabel]
                    })

                })

                // 常用设置
                option.legend.show = false
                option.title.show = true
                option.title.subtext = this.barDatas.details.title

                // 空处理
                if (!chartDatas || (chartDatas && chartDatas.length < 1)) {
                    option.series[0].data = ['']
                    option.yAxis[0].data = ['暂无数据']

                }

                myChart.setOption(option)

                // resize自适应
                this.setResizeFun(myChart)
            },
        },
        mounted() {
            this.drawCharts(this.barDatas)
        }

    }
</script>

<style lang="scss" scoped>
</style>
