<template>
    <!-- 资源： 各设备类型状态分布 -->
    <div class="content-chart"
         style="width:100%">
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

    import Bus from '../utils/bus.js'

    import iframe from 'src/services/iframe/iframe.js'

    export default {
        mixins: [DrawChart],
        components: {

        },
        data() {
            return {
                poolOne: true,
                poolOneS: false,
                pieList: [],
                pieTooltip: '',
                QueryType: '',
                legendData1: '',
                legendData2: '',
                legendData3: '',
                legendData4: '',
                legendData5: '',
                Port: {
                    IdcType: [], // 资源池名字
                    NetworkNumber: [], // 网络设备
                    PhyNumber: [], // 物理机
                    SafeNumber: [], // 安全设备
                    StorageNumber: [], // 存储设备
                    Total: [], // 总数
                    VmNumber: [], // 虚拟机
                    legendData: ['物理服务器', '虚拟服务器', '网络', '安全', '存储'],
                },
                chartData: {
                    name: '各设备类型状态分布',
                    // filter: [{ name: '全部设备', label: '0' }, { name: '服务器', label: '1' }],
                    activeFilter: '0',
                    chartList: [{ id: 'resource-chart-device-pie', chartOption: 'pie-option', chartDatas: '' }]
                },
                chartData1: {
                    name: '',
                    chartList: [{ id: 'resource-chart-device-bar', chartOption: 'bar-option', chartDatas: '' }]
                }
            }
        },

        methods: {
            changeTab(val) {
                Bus.$emit('poolFlagS', this.poolOneS)
                if (val === '1') {
                    this.getCountByIdcDevCT('服务器')
                    this.QueryType = '服务器'
                    this.legendData1 = 'X86服务器'
                    this.legendData2 = '云主机'
                    this.legendData3 = ''
                    this.legendData4 = ''
                    this.legendData5 = ''
                } else {
                    this.getCountByIdcDevCT('')
                    this.QueryType = ''
                }
                this.pieList = []
                this.Port.NetworkNumber = []
                this.Port.PhyNumber = []
                this.Port.SafeNumber = []
                this.Port.StorageNumber = []
                this.Port.VmNumber = []
                this.Port.IdcType = []
            },
            getCountByIdcDevCT(data) {
                var params
                if (data === '服务器') {
                    params = {
                        queryType: '服务器'
                    }
                } else {
                    params = {
                        queryType: ''
                    }
                    this.legendData1 = 'X86服务器'
                    this.legendData2 = '云主机'
                    this.legendData3 = '网络'
                    this.legendData4 = '安全'
                    this.legendData5 = '存储'
                }

                iframe.getCountByIdcDevCT(params).then((res) => {
                    this.chartData.chartList[0].chartDatas = res
                    res.forEach(item => {
                        this.Port.NetworkNumber.push(item.networkNumber)
                        this.Port.PhyNumber.push(item.phyNumber)
                        this.Port.SafeNumber.push(item.safeNumber)
                        this.Port.StorageNumber.push(item.storageNumber)
                        this.Port.VmNumber.push(item.vmNumber)
                        this.Port.IdcType.push(item.idcType)
                    })
                    for (let item in res) {
                        this.pieList.push({
                            value: res[item].total,
                            name: res[item].idcType ? res[item].idcType : '未知'
                        })
                    }
                    this.drawChartsPie()
                    this.drawChartsBar()
                })
            },

            // 绘制饼图表
            drawChartsPie() {
                let _this = this
                _this.$nextTick(() => {
                    let subItem = this.chartData.chartList[0]
                    let myChart = _this.initMyChart(subItem.id)
                    myChart.clear()
                    // 数据格式处理
                    let option = JSON.parse(JSON.stringify(ChartOption[subItem.chartOption]))
                    let chartDatas = JSON.parse(JSON.stringify(subItem.chartDatas))
                    // 环形图颜色
                    let colorSource = ChartOption['color-option']['linearColor']
                    let colors = [
                        colorSource.blue, colorSource.blueLight, colorSource.green, colorSource.greenLight,
                        colorSource.yellow, colorSource.purple
                    ]
                    option.title.show = true
                    option.title.text = ''
                    option.title.subtext = '设备量分布'
                    option.title.subtextStyle.fontSize = 10
                    option.title.x = '49%'
                    option.title.y = '44%'
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
                                        stype: 'linear',
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
                    myChart.setOption(option, true)
                    // resize自适应
                    _this.setResizeFun(myChart)
                    myChart.off('click')
                    // myChart.on('click', function (params) {
                    //     Bus.$emit('poolFlag', _this.poolOne, params.name, _this.QueryType)
                    //     _this.selectedPie(myChart, option.series[0].data, params.dataIndex)
                    // })
                })
            },
            selectedPie(myChart, data, dataIndex) {
                // 刷新数据
                let option = myChart.getOption()
                let datas = option.series[0].data
                let datas2 = option.series[1].data

                datas.forEach((item, index) => {
                    item.selected = false
                    if (index === dataIndex) {
                        item.selected = true
                    }
                })
                datas2.forEach((item, index) => {
                    item.selected = false
                    if (index === dataIndex) {
                        item.selected = true
                    }
                })
                option.series[0].data = datas
                option.series[1].data = datas2
                myChart.setOption(option)
            },
            // 绘制柱图表
            drawChartsBar() {
                let _this = this
                _this.$nextTick(() => {
                    _this.chartData1.chartList.forEach((subItem) => {
                        let myChart = _this.initMyChart(subItem.id)
                        myChart.clear()
                        // 数据格式处理
                        let option = JSON.parse(JSON.stringify(ChartOption[subItem.chartOption]))
                        // let chartDatas = JSON.parse(JSON.stringify(subItem.chartDatas))

                        let colors = ChartOption['color-option']['linearColor']
                        // option.dataZoom[1].end = 100
                        for (let i = 0; i < _this.Port.IdcType.length - 1; i++) {
                            option.series[i] = JSON.parse(JSON.stringify(option.series[0]))
                            option.series[i].label.show = true
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

                        option.yAxis[0].name = '设备量（台）'
                        option.yAxis[0].nameTextStyle.padding = [0, 0, 0, -50]

                        option.series[0].data = _this.Port.PhyNumber
                        option.series[1].data = _this.Port.VmNumber //
                        option.series[2].data = _this.Port.NetworkNumber
                        option.series[3].data = _this.Port.SafeNumber
                        option.series[4].data = _this.Port.StorageNumber //

                        option.series[0].name = _this.legendData1
                        option.series[1].name = _this.legendData2
                        option.series[2].name = _this.legendData3
                        option.series[3].name = _this.legendData4
                        option.series[4].name = _this.legendData5

                        option.legend.data = [option.series[0].name, option.series[1].name, option.series[2].name, option.series[3].name, option.series[4].name]

                        option.xAxis.data = _this.Port.IdcType
                        option.series[0].label.fontSize = option.series[1].label.fontSize = option.series[2].label.fontSize = option.series[3].label.fontSize = option.series[4].label.fontSize = '10'

                        option.series[0].itemStyle.color.colorStops = colors.blue
                        option.series[1].itemStyle.color.colorStops = colors.blueLight
                        option.series[2].itemStyle.color.colorStops = colors.green
                        option.series[3].itemStyle.color.colorStops = colors.greenLight
                        option.series[4].itemStyle.color.colorStops = colors.yellow

                        myChart.setOption(option, false)
                        // resize自适应
                        _this.setResizeFun(myChart)
                        myChart.on('click', function () {
                        })
                    })
                })
            }
        },
        mounted() {
            this.getCountByIdcDevCT()
        },
    }
</script>

<style lang="scss" scoped>
</style>

