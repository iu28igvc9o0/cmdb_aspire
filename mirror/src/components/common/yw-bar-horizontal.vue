<template>
    <!-- 柱状图：横向 -->
    <div class="yw-chart-bar"
         :ref="barDatas.id"
         style="width: 100%;height: 100%;">
    </div>
</template>

<script>
    import ChartOption from 'src/utils/chartOption.js'
    import DrawChart from 'src/utils/drawCharts.js'
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
                let colors = ChartOption['color-option']['linearColorToDeep']
                let colorsList = [colors.blue, colors.green]

                // series
                this.barDatas.details.seriesName.forEach((seriesItem, index) => {
                    // 坐标轴
                    option.series[index] = JSON.parse(JSON.stringify(option.series[0]))
                    option.series[index].itemStyle.color.colorStops = colorsList[index]
                    option.xAxis[0].name = seriesItem.xLabelName
                    option.series[index].label.formatter = (params) => {
                        if (params.value) {
                            return params.value
                        } else {
                            return ''
                        }

                    }
                    option.series[index].stack = 'chart-stack'

                    // legend
                    option.series[index].name = seriesItem.yLabelName
                    option.legend.data = this.barDatas.details.seriesName.map((seriesItem) => { return seriesItem.yLabelName })

                    // 数据
                    option.series[index].data = chartDatas.map((item) => {
                        return item[seriesItem.xLabel]
                    })
                    option.yAxis[0].data = chartDatas.map((item) => {
                        return item[seriesItem.yLabel]
                    })

                })

                // 常用设置
                option.series[this.barDatas.details.seriesName.length - 1].itemStyle.barBorderRadius = [0, 4, 4, 0]
                option.title.show = true
                option.title.text = ''
                option.title.subtext = ''
                option.grid.top = 60

                // tooltip
                option.tooltip.formatter = (params) => {
                    let flag = '<span style="display:inline-block;vertical-align:middle;margin-right:3px;width:4px;height:4px;border-radius:100%;background:#fff;"></span>'
                    let itemText = ''
                    params.forEach((item, index) => {
                        itemText += `<span style="margin-left:7px;">${params[index].seriesName}：${params[index].value || 0}个</span><br/>`

                    })
                    let text = `${flag}${params[0].name}<br/>${itemText}`
                    return text
                }

                // 空处理
                if (!chartDatas || (chartDatas && chartDatas.length < 1)) {
                    option.series[0].data = ['']
                    option.yAxis[0].data = ['暂无数据']

                }

                myChart.setOption(option)
                // resize自适应
                this.setResizeFun(myChart)

                // 点击事件
                myChart.on('click', (params) => {
                    this.$emit('clickChart', params)
                })
            },
        },
        mounted() {
            this.drawCharts(this.barDatas)
        }

    }
</script>

<style lang="scss" scoped>
</style>
