<template>
    <!-- 柱状图、折线图(浅色系) -->
    <div class="yw-chart-barLine"
         :ref="barLineDatas.id"
         style="width: 100%;height: 100%;">
    </div>
</template>

<script>
    import ChartOption from 'src/utils/chartOption.js'
    import DrawChart from 'src/utils/drawCharts.js'
    export default {
        mixins: [DrawChart],
        props: ['barLineDatas'],
        data() {
            return {
                chartOption: {
                    // 颜色
                    'color-option': {
                        linearColor: {
                            blueLight: [{
                                offset: 0,
                                color: '#01A3FF' // 0% 处的颜色
                            },
                            {
                                offset: 1,
                                color: '#3FD5FF' // 100% 处的颜色
                            }
                            ],
                        }
                    },
                    // 柱状图-线图
                    'bar-line-option': {
                        color: ['#0249CB', '#02BD6C'],
                        textStyle: {
                            color: '#606d80',
                            fontSize: 12
                        },
                        title: {
                            show: false,
                            text: '',
                            padding: [0, 0, 0, 0],
                            textStyle: {
                                fontSize: 14,
                                color: '#CAF4FF'
                            },
                            subtext: '',
                            subtextStyle: {
                                fontSize: 14,
                                color: '#fff'
                            }
                        },
                        tooltip: {
                            trigger: 'axis',
                            axisPointer: { // 坐标轴指示器，坐标轴触发有效
                                type: 'shadow' // 默认为直线，可选为：'line' | 'shadow'
                            },
                            confine: true
                        },
                        legend: {
                            itemWidth: 14,
                            itemHeight: 6,
                            textStyle: {
                                // color: '#606d80',
                                // fontSize: '12px'
                            },
                            // data: ['名称']
                        },
                        grid: {
                            left: 10,
                            right: 10,
                            top: 50,
                            bottom: 20,
                            containLabel: true
                        },
                        xAxis: {
                            data: ['暂无数据'],
                            axisTick: {
                                show: false
                            },
                            axisLine: {
                                lineStyle: {
                                    color: '#606d80'
                                }
                            },
                            axisLabel: {
                                // rotate: 20,
                                // interval: 0, //显示所有标签
                            }

                        },
                        yAxis: [{
                            name: '名称1',
                            // min: 0,
                            // max: 100,
                            // interval: 5,
                            splitNumber: 5,
                            nameTextStyle: {
                                // fontSize: 14,
                                padding: [0, 0, 0, 20]
                            },
                            axisLine: {
                                show: false
                            },
                            axisTick: {
                                show: false
                            },
                            splitLine: {
                                lineStyle: {
                                    type: 'dashed',
                                    color: '#8fa3cc'
                                }
                            },
                            axisLabel: {
                                fontSize: 14,
                            }
                        },
                        {
                            name: '名称2',
                            nameTextStyle: {
                                // fontSize: 14,
                                padding: [0, 0, 0, 0]
                            },
                            splitNumber: 5,
                            // min: 0,
                            // max: 100,
                            // interval: 20,
                            axisLabel: {
                                formatter: '{value}%',
                                fontSize: 14,
                            },
                            axisLine: {
                                show: false
                            },
                            axisTick: {
                                show: false
                            },
                            splitLine: {
                                lineStyle: {
                                    type: 'dashed',
                                    color: '#E8ECF2'
                                }
                            },
                        }

                        ],
                        series: [{
                            name: '名称',
                            type: 'bar',
                            itemStyle: {
                                color: {
                                    type: 'linear',
                                    x: 0,
                                    y: 1, // 由下至上
                                    x2: 0,
                                    y2: 0,
                                    colorStops: [{
                                        offset: 0,
                                        color: '#3377FF' // 0% 处的颜色
                                    },
                                    {
                                        offset: 1,
                                        color: '#80E6FF' // 100% 处的颜色
                                    }
                                    ],
                                    global: false // 缺省为 false
                                }
                                // barBorderRadius: [4, 4, 0, 0] // 边角弧度
                            },
                            barMaxWidth: 20,
                            // barWidth: 20,
                            // barGap: 20, //不同系列的柱间距离
                            // barCategoryGap: 20, //同一系列的柱间距离
                            data: [0]
                        }, {
                            name: '名称',
                            type: 'line',

                            yAxisIndex: 1, // 使用的 y 轴的 index，在单个图表实例中存在多个 y轴的时候有用
                            // smooth: true, //平滑曲线显示
                            showSymbol: false,
                            symbol: 'circle', // 标记的图形为实心圆
                            symbolSize: 6, // 标记的大小
                            itemStyle: {
                                borderColor: '#fff',
                                // 折线拐点标志的样式
                                color: '#EFC311'
                            },
                            lineStyle: {
                                width: 2,
                                color: {
                                    type: 'linear',
                                    x: 0,
                                    y: 0,
                                    x2: 0,
                                    y2: 1,
                                    colorStops: [{
                                        offset: 0,
                                        color: '#EFC311' // 0% 处的颜色
                                    },
                                    {
                                        offset: 1,
                                        color: '#FFE221' // 100% 处的颜色
                                    }
                                    ],
                                    global: false // 缺省为 false
                                }
                            },
                            pointer: {
                                width: 4
                            },
                            data: [0]
                        }]
                    }
                }
            }
        },

        methods: {
            // 绘制图表
            drawCharts(subItem) {
                this.$nextTick(() => {
                    let myChart = echarts.init(this.$refs[subItem.id])
                    myChart.clear()

                    // 数据格式处理
                    this.drawBarLine(myChart, subItem)

                })

            },

            drawBarLine(myChart, subItem) {
                // 数据格式处理
                let chartOption = this.chartOption ? this.chartOption[subItem.chartOption] : ChartOption[subItem.chartOption]
                let option = JSON.parse(JSON.stringify(chartOption))
                let chartDatas = JSON.parse(JSON.stringify(subItem.chartDatas))
                let colors = this.chartOption ? this.chartOption['color-option']['linearColor'] : ChartOption['color-option']['linearColor']

                // 绘制
                let parentChartDatas = this.barLineDatas
                option.legend.data = []
                parentChartDatas.option.series.forEach((item, index) => {

                    // 坐标轴
                    option.series[index] = JSON.parse(JSON.stringify(item.chartType === 'line' ? option.series[1] : option.series[0]))
                    option.series[index].name = item.seriesName
                    option.legend.data.push(option.series[index].name)
                    option.yAxis[index].name = item.yAxisName

                    // 数据
                    option.series[index].data = []
                    option.xAxis.data = []
                    if (chartDatas && chartDatas.length > 0) {
                        chartDatas.forEach((dataItem) => {
                            option.series[index].data.push(dataItem[item.yData])
                            option.xAxis.data.push(dataItem[item.xData])
                        })
                    } else {
                        option.series[0].data = [0]
                        option.xAxis.data = ['暂无']
                    }


                })

                option.series[0].itemStyle.color.colorStops = colors.blueLight

                myChart.setOption(option)

                // resize自适应
                this.setResizeFun(myChart)
            },
        },
        mounted() {
            this.drawCharts(this.barLineDatas)
        }

    }
</script>

<style lang="scss" scoped>
</style>
