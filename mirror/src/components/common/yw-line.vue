<template>
    <!-- 折线图 -->
    <div class="yw-chart-line"
         :ref="lineDatas.id"
         style="width: 100%;height: 100%;">
    </div>
</template>

<script>
    import ChartOption from 'src/utils/chartOption.js'
    import DrawChart from 'src/utils/drawCharts.js'
    export default {
        mixins: [DrawChart],
        props: ['lineDatas'],
        data() {
            return {

            }
        },

        methods: {
            // 绘制图表
            drawCharts(subItem) {
                this.$nextTick(() => {
                    let myChart = echarts.init(this.$refs[subItem.id])
                    myChart.clear()

                    // 数据格式处理
                    this.drawLine(myChart, subItem)

                })

            },

            drawLine(myChart, subItem) {
                // 数据格式处理
                let option = JSON.parse(JSON.stringify(ChartOption[subItem.chartOption]))
                let chartDatas = JSON.parse(JSON.stringify(subItem.chartDatas))
                let colors = ChartOption['color-option']['linearColor']
                let colorsList = [colors.blueLight, colors.green]

                option.legend.show = this.lineDatas.details.legend ? this.lineDatas.details.legend.show : true
                option.legend.data = []
                this.lineDatas.details.seriesName.forEach((item, index) => {
                    option.series[index] = JSON.parse(JSON.stringify(option.series[0]))
                    option.series[index].name = this.lineDatas.details.seriesName[index].name
                    option.legend.data.push(option.series[index].name)

                    // 线条颜色设置
                    let countIndex = index % (colorsList.length)
                    if (this.lineDatas.details.seriesName[index].lineColor) {
                        option.series[index].lineStyle.color = this.lineDatas.details.seriesName[index].lineColor
                        option.series[index].areaStyle.color.colorStops[0].color = this.lineDatas.details.seriesName[index].lineColor
                        option.series[index].areaStyle.color.colorStops[1].color = this.lineDatas.details.seriesName[index].lineColor
                    } else {
                        option.series[index].lineStyle.color.colorStops = colorsList[countIndex]
                        option.series[index].areaStyle.color.colorStops[0].color = colorsList[countIndex]
                        option.series[index].areaStyle.color.colorStops[1].color = colorsList[countIndex]
                    }
                })

                // 坐标轴名称
                if (this.lineDatas.details.seriesName[0].yLabelName) {
                    option.yAxis.name = this.lineDatas.details.seriesName[0].yLabelName
                } else {
                    option.yAxis.name = ''
                }
                option.grid.left = 30
                option.legend.left = 0
                option.tooltip.formatter = (params) => {
                    let flag = '<span style="display:inline-block;vertical-align:middle;margin-right:3px;width:4px;height:4px;border-radius:100%;background:#fff;"></span>'
                    let text = `${flag}${params[0] ? params[0].name : 0}：`
                    let res = ''

                    params.forEach((item, index) => {
                        if (this.lineDatas.details.seriesName[index]['xLabelDatasTooltip']) {
                            text = `${flag}${this.lineDatas.details.seriesName[index]['xLabelDatasTooltip'][item.dataIndex]}`
                        }

                        let nameItem = `<span style="margin-left:7px;">${item.seriesName}：${item.value ? item.value : 0} ${option.yAxis.name}</span>`
                        res += `<br/>${nameItem}`
                    })
                    return text + res
                }

                // y轴数据
                let maxLlist = []
                let minLlist = []
                this.lineDatas.details.seriesName.forEach((item, index) => {
                    if (this.lineDatas.details.seriesName[index].yLabelDatas) {
                        option.series[index].data = this.lineDatas.details.seriesName[index].yLabelDatas
                    } else {
                        option.series[index].data = chartDatas.map((item) => {
                            return item[this.lineDatas.details.seriesName[index].yLabel]
                        })
                    }
                    maxLlist.push(Math.max(...option.series[index].data))
                    minLlist.push(Math.min(...option.series[index].data))
                })

                // x轴数据
                if (this.lineDatas.details.seriesName[0].xLabelDatas) {
                    option.xAxis.data = this.lineDatas.details.seriesName[0].xLabelDatas
                } else {
                    option.xAxis.data = chartDatas.map((item) => {
                        return item[this.lineDatas.details.seriesName[0].xLabel]
                    })
                }

                // 最大值设置
                // 根据数据设置最大最小值
                let maxValue = Math.max(...maxLlist)
                let minValue = Math.max(...minLlist)
                option.yAxis.max = maxValue
                option.yAxis.min = minValue

                // 0%~100%区间设置最大最小值
                if (this.lineDatas.details && this.lineDatas.details.seriesName && this.lineDatas.details.seriesName.length > 0 && this.lineDatas.details.seriesName[0].yLabelName === '%') {
                    option.yAxis.max = 100
                    option.yAxis.min = 0
                }

                // 无数据时
                if (!chartDatas || chartDatas.length < 1) {
                    this.lineDatas.details.seriesName.forEach((item, index) => {
                        option.series[index].data = [0]
                    })

                    option.xAxis.data = ['暂无数据']
                }

                myChart.setOption(option, true)

                // resize自适应
                this.setResizeFun(myChart)
            },
        },
        mounted() {
            this.drawCharts(this.lineDatas)
        }

    }
</script>

<style lang="scss" scoped>
</style>
