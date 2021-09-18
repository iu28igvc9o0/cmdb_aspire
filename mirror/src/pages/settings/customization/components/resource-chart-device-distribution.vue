<template>
    <!-- 资源：各资源池设备分配 -->
    <div class="content-chart"
         style="width:100%;"
         v-if="(projectName!='集中网管'&&projectName!='客响-集中运维平台')">
        <section class="chart-title-wrap clearfix">
            <svg class="svg-icon svg-icon-24"
                 aria-hidden="true">
                <use xlink:href="#iconziyuan"></use>
            </svg>
            <span class="chart-title">{{chartData.name}}</span>
        </section>

        <!-- 多图表 -->
        <section class="chart-section">
            <div class="chart-box-wrap"
                 style="width:24.2%;float:left;position: relative;z-index:100">
                <div class="chart-box-item"
                     v-for="(subItem,subIndex) in chartData.chartList"
                     :key="subIndex"
                     :id="subItem.id"></div>
            </div>
            <div class="chart-box-wrap"
                 style="width:calc(75.8% - 1px);float:right;border-left: 1px solid #0B4D77;">
                <div class="chart-box-item"
                     v-for="(subItem,subIndex) in chartData1.chartList"
                     :key="subIndex"
                     :id="subItem.id"></div>
            </div>
        </section>
        <!-- 多图表 -->

    </div>
</template>

<script>

    import ChartOption from 'src/utils/chartOption'
    import DrawChart from 'src/utils/drawCharts.js'

    import { fixedNumber } from 'src/assets/js/utility/rb-filters.js'

    import iframe from 'src/services/iframe/iframe.js'

    export default {
        mixins: [DrawChart],
        components: {

        },
        data() {
            return {
                poolOne: true,
                legendName: [],
                pieList: [],
                pieTooltip: '',
                columnChart: {},
                xList: [],
                legendList: [],
                seriesList: [],
                barYName: [],
                barXValue: [],
                colors: [
                    ['#0249CB', '#006CFF'],
                    ['#05A7EA', '#3AEFFF'],
                    ['#02BD6C', '#4BFF9B'],
                    ['#84BE29', '#CCFF72'],
                    ['#F8D755', '#FFF072'],
                    ['#E24E1A', '#FF8400'],
                    ['#FF0528', '#FF3790'],
                    ['#9630FF', '#C73FFF'],
                    ['#FFE823', '#F4C22B'],
                    ['#607FF1', '#1C56D9']
                ], // 配置颜色
                seriesData: {
                    name: '',
                    type: 'bar',
                    data: null,
                    label: {
                        show: true,
                        position: 'left'
                    },
                    barWidth: 20, // 柱图宽度
                    itemStyle: {
                        normal: {
                            color: {
                                type: 'linear',
                                x: 0,
                                y: 0,
                                x2: 0,
                                y2: 1,
                                colorStops: [{
                                    offset: 1, color: '#305BFF' // 0% 处的颜色
                                }, {
                                    offset: 0, color: '#5B94FF' // 100% 处的颜色
                                }]
                            },
                            barBorderRadius: [4, 4, 0, 0], // 边角弧度
                            label: {
                                show: true, // 开启显示
                                position: 'top', // 在上方显示
                                // formatter: '',
                                textStyle: { // 数值样式
                                    color: '#C9CCED',
                                    fontSize: 10
                                }
                            }
                        }
                    }
                },
                Port: {
                    IdcType: [], // 资源池名字
                    NetworkNumber: [], // 网络设备
                    PhyNumber: [], // 物理机
                    SafeNumber: [], // 安全设备
                    StorageNumber: [], // 存储设备
                    Total: [], // 总数
                    VmNumber: [], // 虚拟机
                    data: ['物理服务器', '虚拟服务器', '网络', '安全', '存储']
                },
                chartData: {
                    name: '各资源池设备分配',
                    chartList: [{ id: 'resource-chart-distribution-pie', chartOption: 'pie-option', chartDatas: '' }]
                },
                chartData1: {
                    name: '',
                    chartList: [{ id: 'resource-chart-distribution-bar', chartOption: 'bar-option', chartDatas: '' }]
                },
                projectName: window.projectName
            }
        },

        methods: {
            // 获得数据
            getCountByIdcDevCT() {
                iframe.countStatusAll().then((res) => {
                    this.chartData.chartList[0].chartDatas = res
                    for (let i = 0; i < res.length; i++) {
                        this.pieList.push({
                            value: Object.values(res)[i].COUNT,
                            name: Object.values(res)[i].device_status ? Object.values(res)[i].device_status : '未知'
                        })
                    }

                    this.drawChartsPie()
                })
            },
            getCountStatusByIdc() {
                iframe.countStatusByIdc().then((res) => {
                    this.columnChart = {}
                    this.xList = []
                    this.legendList = []
                    this.barXValue = []
                    this.barYName = []
                    res.forEach(item => {
                        for (let key in item) {
                            if (key === '') {
                                key = '未知'
                                item[key] = item['']
                            }
                            if (key === 'idcType') {
                                this.xList.push(item[key] ? item[key] : '未知')
                            } else {
                                if (this.columnChart[key]) {
                                    this.columnChart[key].push(item[key])
                                } else {
                                    this.legendList.push(key)
                                    this.columnChart[key] = []
                                    this.columnChart[key].push(item[key])
                                }
                            }
                        }
                    })
                    this.seriesList = []
                    this.legendList.forEach((item) => {
                        let series = JSON.parse(JSON.stringify(this.seriesData))
                        series.name = item
                        series.data = this.columnChart[item]
                        // series.itemStyle.normal.color.colorStops[1].color = this.colors[index][0]
                        // series.itemStyle.normal.color.colorStops[0].color = this.colors[index][1]
                        this.seriesList.push(series)
                        this.barYName.push(series.name)
                        this.barXValue.push(series.data)
                    })
                    this.echartsTypeColumn()
                })
            },

            // 绘制饼图表
            drawChartsPie() {
                let _this = this
                _this.$nextTick(() => {
                    // _this.chartData.chartList.forEach((subItem, subIndex) => {
                    let subItem = this.chartData.chartList[0]
                    let myChart = echarts.init(document.getElementById(subItem.id))
                    // 清空画布
                    myChart.clear()
                    // 数据格式处理
                    let option = JSON.parse(JSON.stringify(ChartOption[subItem.chartOption]))
                    let chartDatas = JSON.parse(JSON.stringify(subItem.chartDatas))
                    // 环形图颜色
                    let colorSource = ChartOption['color-option']['linearColor']
                    let colors = [colorSource.blue, colorSource.blueLight, colorSource.green, colorSource.greenLight, colorSource.yellow, colorSource.purple]

                    option.title.show = true
                    option.title.text = ''
                    option.title.subtext = '设备量分布'
                    option.title.subtextStyle.fontSize = 10
                    option.title.x = '49%'
                    option.title.y = '44%'
                    option.series[0] = JSON.parse(JSON.stringify(option.series[0]))
                    option.series[1] = JSON.parse(JSON.stringify(option.series[0]))
                    option.series[1].name = option.series[0].name = '各资源池设备分配'
                    option.series[0].radius = option.series[1].radius = ['20%', '40%']
                    option.series[1].label.position = 'inside'
                    option.tooltip.formatter = '{b} <br/>设备数: {c} ({d}%)'
                    option.series[0].label.formatter = function (param) {
                        if (param.percent === 0) {
                            let text = param.name + '\n0.01%'
                            return text
                        } else {
                            let text = param.name + '\n' + param.percent + '%'
                            return text
                        }
                    }
                    option.series[1].label.formatter = function (param) {
                        let text = '{num|' + param.data.num + '}'
                        return text
                    }

                    if (chartDatas && chartDatas.length > 0) {
                        option.series[0].data = chartDatas.map((item, index) => {
                            let colorIndex = (index) % (Object.values(colors).length)
                            let num = ''
                            if (index < 2) {
                                num = fixedNumber(item.COUNT, 1)
                            }
                            return {
                                name: item.device_status ? item.device_status : '未知',
                                value: item.COUNT,
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
                                        colorStops: (Object.values(colors)[colorIndex] || Object.values(colors)[0])
                                    }
                                }

                            }
                        })

                        option.series[1].data = chartDatas.map((item, index) => {
                            let colorIndex = (index) % (Object.values(colors).length)
                            let num = ''
                            if (index < 2) {
                                num = fixedNumber(item.COUNT, 1)
                            }
                            return {
                                name: item.device_status ? item.device_status : '未知',
                                value: item.COUNT,
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
                                        stype: 'linear',
                                        x: 0,
                                        y: 0, // 由上至下
                                        x2: 0,
                                        y2: 1,
                                        colorStops: (Object.values(colors)[colorIndex] || Object.values(colors)[0])
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
                    myChart.setOption(option, true)
                    // resize自适应
                    _this.setResizeFun(myChart)
                    myChart.on('click', function (params) {
                        _this.selectedPie(myChart, option.series[0].data, params.dataIndex)
                    })
                    // })
                })
            },
            selectedPie(myChart, data, dataIndex) {
                // 刷新数据
                let option = myChart.getOption()
                let datas = option.series[0].data

                datas.forEach((item, index) => {
                    item.selected = false
                    if (index === dataIndex) {
                        item.selected = true
                    }
                })
                option.series[1].data = option.series[0].data = datas
                myChart.setOption(option)
            },
            // 绘制柱图表
            // echartsTypeColumn() {
            //     let that = this
            //     let myChart = echarts.init(document.getElementById('eqAnColumn0d'))
            //     myChart.clear()
            //     myChart.setOption({
            //         title: {
            //             text: ''
            //         },
            //         tooltip: {},
            //         grid: {
            //             top: '50',
            //             bottom: '30',
            //             right: '30',
            //             left: '70'
            //         },
            //         legend: {
            //             data: that.legendList,
            //             icon: 'circle',
            //             right: '20',
            //             itemHeight: 9, // 改变圆圈大小
            //             textStyle: { // 图例文字的样式
            //                 color: '#fff'
            //             },
            //             y: '0'
            //         },
            //         xAxis: {
            //             data: that.xList,
            //             axisLabel: {
            //                 textStyle: { // 改变刻度字体样式
            //                     color: '#9597AB'
            //                 }
            //             },
            //             axisLine: {
            //                 show: true,
            //                 lineStyle: {
            //                     color: '#2D315F',
            //                     width: 1
            //                 }
            //             },
            //             axisTick: {
            //                 show: false
            //             }
            //         },
            //         yAxis: {
            //             name: '设备量（个）',
            //             axisLabel: {
            //                 textStyle: { // 改变刻度字体样式
            //                     color: '#9597AB'
            //                 }
            //             },
            //             nameTextStyle: {
            //                 fontSize: 14,
            //                 color: '#fff',
            //             // align: 'left'
            //                 padding: [0, 0, 0, 10]
            //             },
            //             splitLine: {    // 网格线
            //                 lineStyle: {
            //                     type: 'dashed',    // 设置网格线类型 dotted：虚线   solid:实线
            //                     color: '#2D315F'
            //                 },
            //                 show: true // 隐藏或显示
            //             },
            //             axisLine: {
            //                 show: false
            //             },
            //             axisTick: {
            //                 show: false
            //             }
            //         },
            //         series: that.seriesList
            //     })
            //     myChart.on('click', function(params) {
            //     })
            // }
            echartsTypeColumn() {
                let _this = this
                _this.$nextTick(() => {
                    _this.chartData1.chartList.forEach((subItem) => {
                        var myChart = echarts.init(document.getElementById(subItem.id))
                        myChart.clear()
                        // 数据格式处理
                        let option = JSON.parse(JSON.stringify(ChartOption[subItem.chartOption]))
                        let color01 = ['#0249CB', '#05A7EA', '#02BD6C', '#84BE29', '#F8D755', '#E24E1A', '#FF0528', '#9630FF', '#0249CB', '#05A7EA', '#02BD6C', '#84BE29']
                        let color02 = ['#006CFF', '#3AEFFF', '#4BFF9B', '#CCFF72', '#FFF072', '#FF8400', '#FF3790', '#C73FFF', '#006CFF', '#3AEFFF', '#4BFF9B', '#CCFF72']

                        for (let i = 0; i < _this.barYName.length; i++) {
                            option.series[i] = JSON.parse(JSON.stringify(option.series[0]))
                            option.series[i].data = _this.barXValue[i]
                            option.series[i].name = _this.barYName[i]
                            option.series[i].label.show = true

                            _this.legendName.push(option.series[i].name)
                            option.series[i].itemStyle.color.colorStops[0].color = color01[i] || color01[0]
                            option.series[i].itemStyle.color.colorStops[1].color = color02[i] || color02[0]
                            // 柱状图 width调整
                            option.series[i].barWidth = 10
                            option.series[i].label.formatter = (param) => {
                                if (param.data > 1000 || param.data === 1000) {
                                    return fixedNumber(param.data, 1)
                                } if (param.data < 1000 && param.data > 0) {
                                    return param.data
                                } else {
                                    return ''
                                }
                            }
                        }
                        option.legend.data = _this.legendName

                        option.yAxis[0].name = '设备量（台）'
                        option.yAxis[0].nameTextStyle.padding = [0, 0, 0, -50]
                        option.xAxis.data = _this.xList
                        option.grid.top = '60'
                        option.legend.left = '80'
                        console.log(option)
                        myChart.setOption(option, false)
                        // resize自适应
                        _this.setResizeFun(myChart)
                    })
                })
            }
        },
        mounted() {
            this.getCountByIdcDevCT()
            this.getCountStatusByIdc()
        },
        beforeDestroy() {

        }
    }
</script>

<style lang="scss" scoped>
</style>

