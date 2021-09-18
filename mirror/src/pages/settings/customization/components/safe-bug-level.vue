<template>

    <div class="content-chart"
         style="width: 24.2%;">
        <section class="chart-title-wrap clearfix">
            <svg class="svg-icon svg-icon-24"
                 aria-hidden="true">
                <use xlink:href="#iconloudongdengjifenbu"></use>
            </svg>
            <span class="chart-title">{{chartData.name}}</span>
        </section>

        <!-- 多图表 -->
        <section class="chart-section">
            <div class="chart-box-wrap">
                <div class="chart-box-item"
                     style="width:100%"
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
        data() {
            return {
                chartData: {
                    name: '漏洞等级分布',
                    filter: [{ name: '物理机', label: 'X86服务器' }, { name: '虚拟机', label: '云主机' }],
                    activeFilter: 'X86服务器',
                    chartList: [{ id: 'safe-bug-level-1', chartOption: 'pie-option', chartDatas: '' },
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
                this.$api.querySafeBugLevel(params).then((res) => {
                    this.chartData.chartList[0].chartDatas = res
                    this.drawCharts()
                })

            },

            // 绘制图表
            drawCharts() {
                this.$nextTick(() => {
                    this.chartData.chartList.forEach((subItem) => {
                        // 非空判断，避免报错
                        let myChartDom = document.getElementById(subItem.id)
                        if (!myChartDom) {
                            return
                        }
                        let myChart = echarts.init(myChartDom)
                        myChart.clear()
                        // 数据格式处理
                        let option = JSON.parse(JSON.stringify(ChartOption[subItem.chartOption]))
                        let chartDatas = JSON.parse(JSON.stringify(subItem.chartDatas))
                        let colorSource = ChartOption['color-option']['linearColor']
                        let colors = [colorSource.red, colorSource.orange, colorSource.yellow]

                        option.legend.show = true
                        option.legend.data = ['高危漏洞', '中危漏洞', '低危漏洞']

                        option.series[0] = JSON.parse(JSON.stringify(option.series[0]))
                        option.series[0].name = ['漏洞等级分布']
                        option.series[0].radius = ['0%', '40%']
                        option.series[0].center = ['50%', '55%']
                        option.series[0].itemStyle.color = function (item) {
                            let linearSetting = {
                                type: 'linear',
                                x: 0,
                                y: 0, // 由上至下
                                x2: 0,
                                y2: 1,
                                colorStops: Object.values(colors)[item.dataIndex]
                            }
                            return linearSetting
                        }


                        if (chartDatas && Object.keys(chartDatas).length > 0) {
                            // option.series[0].data = chartDatas;
                            let list = [
                                {
                                    name: '高危漏洞',
                                    value: '60',
                                    label: {
                                        color: Object.values(colors)[0][1].color
                                    },
                                    itemStyle: {
                                        color: {
                                            type: 'linear',
                                            x: 0,
                                            y: 0, // 由上至下
                                            x2: 0,
                                            y2: 1,
                                            colorStops: Object.values(colors)[0]
                                        }
                                    }
                                },
                                {
                                    name: '中危漏洞',
                                    value: '20',
                                    label: {
                                        color: Object.values(colors)[1][1].color
                                    },
                                    itemStyle: {
                                        color: {
                                            type: 'linear',
                                            x: 0,
                                            y: 0, // 由上至下
                                            x2: 0,
                                            y2: 1,
                                            colorStops: Object.values(colors)[1]
                                        }
                                    }
                                },
                                {
                                    name: '低危漏洞',
                                    value: '20',
                                    label: {
                                        color: Object.values(colors)[2][1].color
                                    },
                                    itemStyle: {
                                        color: {
                                            type: 'linear',
                                            x: 0,
                                            y: 0, // 由上至下
                                            x2: 0,
                                            y2: 1,
                                            colorStops: Object.values(colors)[2]
                                        }
                                    }
                                }
                            ]
                            if (chartDatas.sCount > 0) {
                                list[0].value = chartDatas.highLeaks
                                list[1].value = chartDatas.mediumLeaks
                                list[2].value = chartDatas.lowLeaks
                            }
                            option.series[0].data = list

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

