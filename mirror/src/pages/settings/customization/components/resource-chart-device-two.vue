<template>
    <!-- 资源：各资源池设备量分布 -->
    <div class="content-chart"
         v-if='poolTwo'
         style="width:100%;">
        <section class="chart-title-wrap clearfix">
            <svg class="svg-icon svg-icon-24"
                 aria-hidden="true">
                <use xlink:href="#iconziyuan"></use>
            </svg>
            <span class="chart-title">{{poolTwoName}}{{chartData.name}}</span>
        </section>

        <!-- 多图表 -->
        <section class="chart-section">
            <div class="chart-box-wrap"
                 style="width:24.2%;float:left">
                <div class="chart-box-item"
                     v-for="(subItem,subIndex) in chartData.chartList"
                     :key="subIndex"
                     :id="subItem.id"></div>
            </div>
            <div class="chart-box-wrap"
                 style="width:calc(75.8% - 1px);float:right;border-left: 1px solid #0B4D77;padding-left:10px">
                <div class="chart-box-item"
                     v-for="(subItem,subIndex) in chartData1.chartList"
                     :key="subIndex"
                     :id="subItem.id"></div>
            </div>
        </section>

    </div>
</template>

<script>

    import ChartOption from 'src/utils/chartOption.js'
    import DrawChart from 'src/utils/drawCharts.js'
    import Bus from '../utils/bus.js'
    import { fixedNumber } from 'src/assets/js/utility/rb-filters.js'

    import iframe from 'src/services/iframe/iframe.js'

    export default {
        mixins: [DrawChart],
        components: {

        },
        data() {
            return {
                legendName: [],
                poolTwo: false,
                poolThree: true,
                idcTypeId: '', // 传进来的资源池ID
                poolTwoName: '',
                pieList: [],
                barYName: [],
                barXValue: [],
                pieTooltip: '',
                QueryType: '',
                nameList: {
                    phyNumber: 'X86服务器',
                    vmNumber: '云主机',
                    networkNumber: '网络设备',
                    storageNumber: '存储设备',
                    safeNumber: '安全设备'
                },
                columnChart: {},
                xList: [],
                xListId: [],
                legendList: [],
                seriesList: [],
                sendAnnular0: true,
                Device_type: [],
                deviceClass: ['安全设备', '网络设备', '存储设备'],
                deviceType: ['X86服务器', '云主机'],
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
                    ['#607FF1', '#1C56D9'],
                    // 备用colors
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
                            // barBorderRadius: [4, 4, 0, 0], // 边角弧度
                            label: {
                                show: true, // 开启显示
                                position: 'top', // 在上方显示
                                textStyle: { // 数值样式
                                    color: '#C9CCED',
                                    fontSize: 14
                                }
                            }
                        }
                    }
                },
                chartData: {
                    name: '设备分类分布',
                    chartList: [{ id: 'resource-chart-device-two-pie', chartOption: 'pie-option', chartDatas: '' }]
                },
                chartData1: {
                    name: '',
                    chartList: [{ id: 'resource-chart-device-two-bar', chartOption: 'bar-option', chartDatas: '' }]
                }
            }
        },

        methods: {
            bbtnFalg: function () {
                Bus.$on('poolFlag', (newV1, newV2, newV3, newV4) => { // 这里最好用箭头函数，不然this指向有问题
                    this.pieList = []
                    this.poolTwo = ''
                    this.QueryType = ''
                    this.poolTwo = newV1
                    this.poolTwoName = newV2
                    this.QueryType = newV3
                    this.idcTypeId = newV4
                    this.getCountByIdcDevCT()
                    this.getCountByIdcPro()
                })
                Bus.$on('poolFlagS', (newV1) => { // 这里最好用箭头函数，不然this指向有问题
                    this.pieList = []
                    this.poolTwo = ''
                    this.poolTwoName = ''
                    this.QueryType = ''
                    this.poolTwo = newV1
                    this.getCountByIdcDevCT()
                    // this.getCountByIdcPro()
                })
            },
            getDatas() {
                this.chartData.chartList.chartDatas = []
            },
            resetData() {
                this.Port = {
                    IdcType: [], // 资源池名字
                    NetworkNumber: [], // 网络设备
                    PhyNumber: [], // 物理机
                    SafeNumber: [], // 安全设备
                    StorageNumber: [], // 存储设备
                    Total: [], // 总数
                    VmNumber: [], // 虚拟机
                    data: ['物理服务器', '虚拟服务器', '网络', '安全', '存储']
                }
            },

            getCountByIdcDevCT() {
                let data = {
                    idcType: this.idcTypeId,
                    queryType: this.QueryType
                }
                iframe.getCountByIdcDevCT(data).then((res) => {
                    delete res[0].idcType
                    delete res[0].total
                    this.pieList = []
                    res.forEach(item => {
                        for (let key in item) {
                            this.pieList.push({
                                value: item[key],
                                name: this.nameList[key] ? this.nameList[key] : '未知'
                            })
                        }
                    })
                    this.drawCharts()
                })
            },
            getCountByIdcPro() {
                let data = {
                    idcType: this.idcTypeId,
                    queryType: this.QueryType
                }
                iframe.getCountByIdcPro(data).then((res) => {
                    this.barXValue = []
                    this.barYName = []
                    this.barXValue = []
                    this.columnChart = {}
                    this.xList = []
                    this.xListId = []
                    this.legendList = []
                    res.forEach(item => {
                        for (let key in item) {
                            if (key === '') {
                                key = '未知'
                                item[key] = item['']
                            }
                            if (key === 'device_type') {
                                this.xList.push(item[key] ? item[key] : '未知')
                            } else if (key.endsWith('Id')) {
                                this.xListId.push(item[key])
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
                    if (this.legendList.length) {
                        this.legendList.forEach((item, index) => {
                            let series = JSON.parse(JSON.stringify(this.seriesData))
                            series.name = item
                            series.data = this.columnChart[item]
                            series.itemStyle.normal.color.colorStops[1].color = (this.colors[index] && this.colors[index][0]) || this.colors[0][0]
                            series.itemStyle.normal.color.colorStops[0].color = (this.colors[index] && this.colors[index][1]) || this.colors[0][1]
                            this.seriesList.push(series)
                            this.barYName.push(series.name)
                            this.barXValue.push(series.data)
                        })
                    }
                    this.echartsTypeColumn()
                })
            },
            // 绘制饼图表
            drawCharts() {
                let _this = this
                _this.$nextTick(() => {
                    _this.chartData.chartList.forEach((subItem) => {
                        let myChart = _this.initMyChart(subItem.id)
                        myChart.clear()
                        // 数据格式处理
                        let option = JSON.parse(JSON.stringify(ChartOption[subItem.chartOption]))
                        // let chartDatas = JSON.parse(JSON.stringify(subItem.chartDatas))
                        // 环形图颜色
                        let colorSource = ChartOption['color-option']['linearColor']
                        let colors = [colorSource.blue, colorSource.blueLight, colorSource.green, colorSource.greenLight, colorSource.yellow, colorSource.purple]

                        option.series[0] = JSON.parse(JSON.stringify(option.series[0]))
                        option.series[1] = JSON.parse(JSON.stringify(option.series[0]))
                        option.series[1].radius = option.series[0].radius = ['20%', '40%']
                        option.title.show = true
                        option.title.text = ''
                        option.title.subtext = '设备量分布'
                        option.title.subtextStyle.fontSize = 10
                        option.title.x = '49%'
                        option.title.y = '44%'
                        option.series[0].label.color = '#fff'

                        option.series[1].label.position = 'inside'
                        option.series[1].label.formatter = '{c}'
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

                        option.series[0].data = _this.pieList.map((item, index) => {
                            let colorIndex = (index) % (colors.length)
                            let num = ''
                            if (index < 2) {
                                num = fixedNumber(item.value, 1)
                            }
                            return {
                                name: item.name,
                                value: item.value,
                                num: num,
                                label: {
                                    color: (colors[colorIndex] && colors[colorIndex][1].color) || colors[0][1].color
                                },
                                itemStyle: {
                                    color: {
                                        type: 'linear',
                                        x: 0,
                                        y: 0, // 由上至下
                                        x2: 0,
                                        y2: 1,
                                        colorStops: colors[colorIndex] || colors[0]
                                    }
                                }
                            }
                        })
                        option.series[1].data = _this.pieList.map((item, index) => {
                            let colorIndex = (index) % (colors.length)
                            let num = ''
                            if (index < 2) {
                                num = fixedNumber(item.value, 1)
                            }
                            return {
                                name: item.name,
                                value: item.value,
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
                                        colorStops: colors[colorIndex] || colors[0]
                                    }
                                }
                            }
                        })
                        option.tooltip.formatter = '{b} <br/>设备数: {c} ({d}%)'
                        myChart.setOption(option, true)
                        // resize自适应
                        _this.setResizeFun(myChart)
                    })
                })
            },
            echartsTypeColumn() {
                let _this = this
                _this.$nextTick(() => {
                    _this.chartData1.chartList.forEach((subItem) => {
                        let myChart = _this.initMyChart(subItem.id)
                        myChart.clear()
                        // 数据格式处理
                        let option = JSON.parse(JSON.stringify(ChartOption[subItem.chartOption]))
                        // let chartDatas = JSON.parse(JSON.stringify(subItem.chartDatas))

                        // let colors = ChartOption['color-option']['linearColor']
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

                        myChart.setOption(option, false)
                        // resize自适应
                        _this.setResizeFun(myChart)
                        myChart.off('click')
                        myChart.on('click', function (params) {
                            if (_this.poolTwoName === '哈尔滨资源池' || _this.poolTwoName === '呼和浩特资源池') {
                                Bus.$emit('poolFlagThree', _this.poolThree, params.name, _this.poolTwoName, _this.QueryType, _this.xListId[params.dataIndex], _this.idcTypeId)
                            } else {
                                _this.$router.push({
                                    path: '/resource/iframe/list',
                                    query: {
                                        parentParams: {
                                            device_project: params.seriesName,
                                            device_type: _this.deviceType.indexOf(params.name) > -1 ? params.name : undefined,
                                            device_class: _this.deviceClass.indexOf(params.name) > -1 ? params.name : _this.deviceType.indexOf(params.name) > -1 ? '服务器' : undefined,
                                            device_class_3: params.name === 'X86服务器' ? 'X86服务器' : undefined,
                                            idcType: _this.poolTwoName
                                        },
                                        condicationCode: 'instance_list'
                                    }
                                })
                            }
                        })
                    })
                })
            }
        },
        mounted() {
            this.bbtnFalg()
            this.getDatas()
        },
        beforeDestroy() {

        }
    }
</script>

<style lang="scss" scoped>
</style>

