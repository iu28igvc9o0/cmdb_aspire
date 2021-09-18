<template>

    <div class="content-chart"
         style="width: 49.4%;">
        <section class="chart-title-wrap clearfix">
            <svg class="svg-icon svg-icon-24"
                 aria-hidden="true">
                <use xlink:href="#iconziyuan"></use>
            </svg>
            <span class="chart-title">{{chartData.name}}</span>
        </section>

        <!-- 多图表 -->
        <section class="chart-section">
            <div class="chart-box-wrap">
                <div class="chart-box-item"
                     style="width:49%"
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
    import { fixedNumber } from 'src/assets/js/utility/rb-filters.js'

    export default {
        mixins: [DrawChart],
        data() {
            return {
                chartData: {
                    name: '各资源池设备分布',
                    filter: [{ name: '物理机', label: 'X86服务器' }, { name: '虚拟机', label: '云主机' }],
                    activeFilter: 'X86服务器',
                    chartList: [{ id: 'resource-pool-device-1', chartOption: 'pie-option', chartDatas: '' }, { id: 'resource-pool-device-2', chartOption: 'bar-option', chartDatas: '' }]
                }
            }
        },
        methods: {
            // 获得数据
            getDatas() {
                let params = {
                    'idcType': '',
                    'queryType': ''
                }
                this.$api.queryPoolDevice(params).then((res) => {
                    this.chartData.chartList[0].chartDatas = res
                    this.chartData.chartList[1].chartDatas = res
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
                        switch (subItem.chartOption) {
                            case 'bar-option':
                                this.drawBar(myChart, subItem)
                                break
                            case 'pie-option':
                                this.drawPie(myChart, subItem)
                                break
                        }
                    })
                })
            },
            drawPie(myChart, subItem) {
                // 数据格式处理
                let option = JSON.parse(JSON.stringify(ChartOption[subItem.chartOption]))
                let chartDatas = JSON.parse(JSON.stringify(subItem.chartDatas))
                let colorSource = ChartOption['color-option']['linearColor']
                let colors = [colorSource.blue, colorSource.blueLight, colorSource.green, colorSource.greenLight, colorSource.yellow, colorSource.purple]

                option.title.show = true
                option.title.text = ''
                option.title.subtext = '设备量分布'
                option.title.x = '49%'
                option.title.y = '43%'
                option.series[0] = JSON.parse(JSON.stringify(option.series[0]))
                option.series[1] = JSON.parse(JSON.stringify(option.series[0]))
                option.series[1].name = option.series[0].name = '各资源池设备分布'
                option.series[1].radius = option.series[0].radius = ['20%', '40%']
                option.series[1].label.position = 'inside'

                option.series[1].label.formatter = function (param) {
                    let text = '{num|' + param.data.num + '}'
                    return text
                }

                if (chartDatas && chartDatas.length > 0) {
                    option.series[0].data = chartDatas.map((item, index) => {
                        let colorIndex = (index) % (Object.values(colors).length)
                        let num = ''
                        if (index < 2) {
                            num = fixedNumber(item.total, 1)
                        }
                        return {
                            name: item.idcType,
                            value: item.total,
                            num: num,
                            label: {
                                color: (Object.values(colors)[colorIndex][1].color)
                            },
                            itemStyle: {
                                color: {
                                    type: 'linear',
                                    x: 0,
                                    y: 0, // 由上至下
                                    x2: 0,
                                    y2: 1,
                                    colorStops: (Object.values(colors)[colorIndex])
                                }
                            }

                        }
                    })

                    option.series[1].data = chartDatas.map((item, index) => {
                        let colorIndex = (index) % (Object.values(colors).length)
                        let num = ''
                        if (index < 2) {
                            num = fixedNumber(item.total, 1)
                        }
                        return {
                            name: item.idcType,
                            value: item.total,
                            num: num,
                            label: {
                                color: '#fff',
                                textShadowColor: '#000',
                                textShadowBlur: 2,
                                textShadowOffsetX: 2,
                                textShadowOffsetY: 2
                            },
                            itemStyle: {
                                color: {
                                    type: 'linear',
                                    x: 0,
                                    y: 0, // 由上至下
                                    x2: 0,
                                    y2: 1,
                                    colorStops: (Object.values(colors)[colorIndex])
                                }
                            }
                        }
                    })
                } else {
                    option.series[0].data = [{
                        name: '暂无数据',
                        value: 0
                    }]
                }

                myChart.setOption(option)

                // resize自适应
                this.setResizeFun(myChart)
            },
            drawBar(myChart, subItem) {
                let option = JSON.parse(JSON.stringify(ChartOption[subItem.chartOption]))
                let chartDatas = JSON.parse(JSON.stringify(subItem.chartDatas))
                let colors = ChartOption['color-option']['linearColor']
                option.dataZoom[0].show = true

                option.series[0] = JSON.parse(JSON.stringify(option.series[0]))
                option.series[1] = JSON.parse(JSON.stringify(option.series[0]))
                option.series[2] = JSON.parse(JSON.stringify(option.series[0]))
                option.series[3] = JSON.parse(JSON.stringify(option.series[0]))
                option.series[4] = JSON.parse(JSON.stringify(option.series[0]))

                option.series[0].name = 'X86服务器'
                option.series[1].name = '云主机'
                option.series[2].name = '网络'
                option.series[3].name = '安全'
                option.series[4].name = '存储'

                option.legend.data = [option.series[0].name, option.series[1].name, option.series[2].name, option.series[3].name, option.series[4].name]
                option.series[0].itemStyle.color.colorStops = colors.blue
                option.series[1].itemStyle.color.colorStops = colors.blueLight
                option.series[2].itemStyle.color.colorStops = colors.yellow
                option.series[3].itemStyle.color.colorStops = colors.greenLight
                option.series[4].itemStyle.color.colorStops = colors.green

                option.series[0].stack = option.series[1].stack = option.series[2].stack =
                    option.series[3].stack = option.series[4].stack = 'chart-stack'
                // option.series[0].itemStyle.barBorderRadius = option.series[1].itemStyle.barBorderRadius =
                //   option.series[2].itemStyle.barBorderRadius = option.series[3].itemStyle.barBorderRadius = 0
                // option.series[4].itemStyle.barBorderRadius = [4, 4, 0, 0]

                // option.xAxis.axisLabel.rotate = 20;

                option.title.show = true
                option.title.text = '设备类型分布'
                option.title.subtext = '设备量(个)'
                option.title.padding = [18, 0, 0, 0]
                option.grid.top = 80
                option.series[4].label.show = true
                option.series[4].label.formatter = (param) => {
                    if (param.name == chartDatas[param.dataIndex].idcType) {
                        return fixedNumber(chartDatas[param.dataIndex].total, 1, true)
                    }
                }

                if (chartDatas && chartDatas.length > 0) {
                    option.series[0].data = chartDatas.map((item) => {
                        return item.phyNumber
                    })
                    option.series[1].data = chartDatas.map((item) => {
                        return item.vmNumber
                    })
                    option.series[2].data = chartDatas.map((item) => {
                        return item.networkNumber
                    })
                    option.series[3].data = chartDatas.map((item) => {
                        return item.safeNumber
                    })
                    option.series[4].data = chartDatas.map((item) => {
                        return item.storageNumber
                    })

                    option.xAxis.data = chartDatas.map((item) => {
                        return item.idcType
                    })
                } else {
                    option.series[0].data = [0]
                    option.xAxis.data = ['暂无']
                }

                myChart.setOption(option)

                // resize自适应
                this.setResizeFun(myChart)
            },
            changeTab() {
                this.getDatas()
            }
        },
        mounted() {
            this.getDatas()
        }

    }
</script>

<style lang="scss" scoped>
</style>

