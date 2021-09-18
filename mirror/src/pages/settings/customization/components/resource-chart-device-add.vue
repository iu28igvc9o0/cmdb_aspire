<template>

    <!-- 资源：各资源池设备品牌分布 -->
    <div class="content-chart"
         style="width: 74.8%">
        <section class="chart-title-wrap clearfix">
            <svg class="svg-icon svg-icon-24"
                 aria-hidden="true">
                <use xlink:href="#iconziyuan"></use>
            </svg>
            <span class="chart-title">{{chartData.name}}</span>
            <div class="chart-filter">
            </div>
        </section>

        <!-- 多图表 -->
        <section class="chart-section">
            <div class="chart-box-wrap"
                 style="position: relative;">
                <div class="chart-box-item"
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
    import iframe from 'src/services/iframe/iframe.js'

    export default {
        mixins: [DrawChart],
        data() {
            return {
                pieList: [],
                Produce: '',
                idcType: [],
                networkNumber: [],
                phyNumber: [],
                safeNumber: [],
                storageNumber: [],
                total: [],
                vmNumber: [],
                chartData: (window.projectName == '集中网管' || window.projectName == '客响-集中运维平台') ? {
                    name: '各资源池设备厂家分布',
                    chartList: [{ id: 'resource-chart-device-add-pie', chartOption: 'pie-option', chartDatas: '' }, { id: 'resource-chart-device-add-bar', chartOption: 'bar-option', chartDatas: '' }]
                } : {
                        name: '各资源池设备品牌分布',
                        chartList: [{ id: 'resource-chart-device-add-pie', chartOption: 'pie-option', chartDatas: '' }, { id: 'resource-chart-device-add-bar', chartOption: 'bar-option', chartDatas: '' }]
                    }
            }
        },
        methods: {
            // 获得数据
            countByProduceAll() {
                if (window.projectName == '集中网管' || window.projectName == '客响-集中运维平台') {
                    let queryData = {
                        queryName: 'ICountInstByCdtAPI_internet_countProducePieChart'
                    }
                    iframe.businessCountList(queryData).then((res) => {
                        this.chartData.chartList[0].chartDatas = res
                        for (let item in res) {
                            this.pieList.push({
                                value: res[item].COUNT,
                                name: res[item].device_mfrs ? res[item].device_mfrs : '未知'
                            })
                        }
                        this.drawPie()
                    })
                } else {
                    iframe.countByProduceAll().then((res) => {
                        this.chartData.chartList[0].chartDatas = res
                        for (let item in res) {
                            this.pieList.push({
                                value: res[item].COUNT,
                                name: res[item].device_mfrs ? res[item].device_mfrs : '未知'
                            })
                        }
                        this.drawPie()
                    })
                }

            },
            countByProduce(produce) {
                console.log(produce, 'produce')
                if (window.projectName == '集中网管' || window.projectName == '客响-集中运维平台') {
                    let params = {
                        queryName: 'ICountInstByCdtAPI_internet_countAssetByIdc',
                        factoryName: produce
                    }
                    iframe.businessCountList(params).then((res) => {
                        this.idcType = []
                        this.networkNumber = []
                        this.phyNumber = []
                        this.safeNumber = []
                        this.storageNumber = []
                        this.vmNumber = []
                        this.total = []
                        for (let item = 0; item < res.length; item++) {
                            this.idcType.push(res[item].idcType)
                            this.networkNumber.push(res[item].networkNumber)
                            this.total.push(res[item].total)
                            this.phyNumber.push(res[item].phyNumber)
                            this.safeNumber.push(res[item].safeNumber)
                            this.storageNumber.push(res[item].storageNumber)
                            this.vmNumber.push(res[item].vmNumber)
                        }
                        this.drawBar()
                    })
                } else {
                    let params = {
                        'produce': produce
                    }
                    iframe.countByProduce(params).then((res) => {
                        this.idcType = []
                        this.networkNumber = []
                        this.phyNumber = []
                        this.safeNumber = []
                        this.storageNumber = []
                        this.vmNumber = []
                        this.total = []
                        for (let item = 0; item < res.length; item++) {
                            this.idcType.push(res[item].idcType)
                            this.networkNumber.push(res[item].networkNumber)
                            this.total.push(res[item].total)
                            this.phyNumber.push(res[item].phyNumber)
                            this.safeNumber.push(res[item].safeNumber)
                            this.storageNumber.push(res[item].storageNumber)
                            this.vmNumber.push(res[item].vmNumber)
                        }
                        this.drawBar()
                    })
                }

            },
            drawPie() {
                let _this = this
                // 数据格式处理
                let subItem = this.chartData.chartList[0]
                let myChart = echarts.init(document.getElementById(subItem.id))
                let option = JSON.parse(JSON.stringify(ChartOption[subItem.chartOption]))
                let chartDatas = JSON.parse(JSON.stringify(subItem.chartDatas))
                let colorSource = ChartOption['color-option']['linearColor']

                let colors = [colorSource.blue, colorSource.blueLight, colorSource.green, colorSource.greenLight, colorSource.yellow, colorSource.purple, colorSource.orange, colorSource.red, colorSource.cyant, colorSource.cyanm]

                option.title.show = true
                option.title.text = ''
                option.title.subtext = '设备量分布'
                option.title.subtextStyle.fontSize = 10
                option.title.x = '49%'
                option.title.y = '44%'
                option.series[0] = JSON.parse(JSON.stringify(option.series[0]))
                option.series[1] = JSON.parse(JSON.stringify(option.series[0]))
                option.series[1].name = option.series[0].name = (window.projectName == '集中网管' || window.projectName == '客响-集中运维平台') ? '各资源池设备厂家分布' : '各资源池设备品牌分布'
                option.series[1].radius = option.series[0].radius = ['20%', '40%']
                option.series[1].label.position = 'inside'
                option.series[0].label.formatter = '{b} {d}%'
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
                            name: item.device_mfrs ? item.device_mfrs : '未知',
                            device_mfrs_id: item.device_mfrs_id,
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
                                    colorStops: (Object.values(colors)[colorIndex])
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
                            name: item.device_mfrs ? item.device_mfrs : '未知',
                            device_mfrs_id: item.device_mfrs_id,
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
                _this.setResizeFun(myChart)
                myChart.off('click')
                myChart.on('click', function (params) {
                    _this.Produce = params.name
                    if (window.projectName == '集中网管' || window.projectName == '客响-集中运维平台') {
                        _this.countByProduce(params.name)
                    } else {
                        _this.countByProduce(params.data.device_mfrs_id)
                    }
                    _this.selectedPie(myChart, option.series[0].data, params.dataIndex)
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
            drawBar() {
                let _this = this
                let subItem = _this.chartData.chartList[1]
                let myChart = echarts.init(document.getElementById(subItem.id))
                let option = JSON.parse(JSON.stringify(ChartOption[subItem.chartOption]))
                let colors = ChartOption['color-option']['linearColor']

                option.series[0] = JSON.parse(JSON.stringify(option.series[0]))
                option.series[1] = JSON.parse(JSON.stringify(option.series[0]))
                option.series[2] = JSON.parse(JSON.stringify(option.series[0]))
                option.series[3] = JSON.parse(JSON.stringify(option.series[0]))
                option.series[4] = JSON.parse(JSON.stringify(option.series[0]))

                if (window.projectName == '集中网管' || window.projectName == '客响-集中运维平台') {
                    option.series[0].name = '服务器'
                    option.series[1].name = '磁带库'
                    option.series[2].name = '网络设备'
                    option.series[3].name = '安全设备'
                    option.series[4].name = '存储设备'
                } else {
                    option.series[0].name = 'X86服务器'
                    option.series[1].name = '云主机'
                    option.series[2].name = '网络'
                    option.series[3].name = '安全'
                    option.series[4].name = '存储'
                }

                option.legend.data = [option.series[0].name, option.series[1].name, option.series[2].name, option.series[3].name, option.series[4].name]
                option.series[0].itemStyle.color.colorStops = colors.blue
                option.series[1].itemStyle.color.colorStops = colors.blueLight
                option.series[2].itemStyle.color.colorStops = colors.yellow
                option.series[3].itemStyle.color.colorStops = colors.greenLight
                option.series[4].itemStyle.color.colorStops = colors.green

                option.series[0].stack = option.series[1].stack = option.series[2].stack =
                    option.series[3].stack = option.series[4].stack = 'chart-stack'
                // option.series[0].itemStyle.barBorderRadius = option.series[1].itemStyle.barBorderRadius =
                //   option.series[2].itemStyle.barBorderRadius = option.series[3].itemStyle.barBorderRadius = [0, 0, 0, 0]
                // option.series[4].itemStyle.barBorderRadius = [4, 4, 0, 0]

                option.xAxis.axisLabel.rotate = 20
                option.title.show = true
                option.title.text = _this.Produce + '设备类型分布'
                option.title.subtext = '设备量 (台)'
                option.grid.top = 50

                option.series[4].label.show = true
                option.series[4].label.data = _this.total
                option.series[4].label.fontSize = '10'
                option.series[4].label.formatter = function (params) {
                    // if (_this.total[params.dataIndex]) {
                    //     return fixedNumber(_this.total[params.dataIndex], 1)
                    // }
                    if (_this.total[params.dataIndex] > 1000 || _this.total[params.dataIndex] === 1000) {
                        return fixedNumber(_this.total[params.dataIndex], 1)
                    } if (_this.total[params.dataIndex] < 1000 && _this.total[params.dataIndex] > 0) {
                        return _this.total[params.dataIndex]
                    } else {
                        return ''
                    }
                }

                if (_this.idcType && _this.idcType.length > 0) {
                    option.series[2].data = _this.networkNumber
                    option.series[0].data = _this.phyNumber
                    option.series[3].data = _this.safeNumber
                    option.series[4].data = _this.storageNumber
                    option.series[1].data = _this.vmNumber

                    option.xAxis.data = _this.idcType
                } else {
                    option.series[0].data = [0]
                    option.xAxis.data = ['暂无']
                }

                myChart.setOption(option)

                // resize自适应
                _this.setResizeFun(myChart)
            }
        },
        mounted() {
            this.countByProduceAll()
            this.countByProduce('')
        }

    }
</script>

<style lang="scss">
    #resource-chart-device-add-pie {
        width: 33% !important;
    }
    #resource-chart-device-add-bar {
        width: calc(67% - 1px) !important;
    }
</style>

