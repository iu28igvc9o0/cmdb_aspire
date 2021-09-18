<template>
    <!-- 资源： 各设备厂商型号分布 -->
    <div class="content-chart"
         style="width:100%">
        <section class="chart-title-wrap clearfix">
            <svg class="svg-icon svg-icon-24"
                 aria-hidden="true">
                <use xlink:href="#iconziyuan"></use>
            </svg>
            <span class="chart-title">{{chartData.name}}</span>
            <div class="chart-filter">
                <el-radio-group class="yw-radio-button-wrap chart-filter-item"
                                v-model="chartData.activeFilter"
                                @change="changeTab">
                    <el-radio-button :label="tabItem.label"
                                     v-for="(tabItem,tabIndex) in chartData.filter"
                                     :key="tabIndex">{{tabItem.name}}</el-radio-button>
                </el-radio-group>
            </div>
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
                     v-for="(subItem,subIndex) in barChartData.chartList"
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

    // import Bus from '../utils/bus.js'

    import rbKeguanServices from 'src/services/iframe/rb-keguan-services.js'

    export default {
        mixins: [DrawChart],
        components: {

        },
        data() {
            return {
                chartData: {
                    name: '各设备厂商型号分布',
                    filter: [
                        { label: 'all', name: '全部' },
                        { label: 'phyServer', name: '物理机' },
                        { label: 'netWork', name: '网络' },
                        { label: 'storage', name: '存储' },
                        { label: 'security', name: '安全' }
                    ],
                    activeFilter: 'all',
                    chartList: [{ id: 'resource-chart-produce-device', chartOption: 'pie-option', chartDatas: [] }]
                },
                barChartData: {
                    name: '',
                    chartList: [{ id: 'resource-chart-produce-device-bar', chartOption: 'bar-option', chartDatas: [], xAxisData: [] }]
                }
            }
        },

        methods: {
            changeTab(val) {
                this.getDeviceCountByProduceAll(val)
            },
            // 各设备厂商型号分布
            getDeviceCountByProduceAll(data) {
                let params = {
                    deviceClass: data || 'all'
                }
                rbKeguanServices.getDeviceCountByProduceAll(params).then((res) => {
                    this.chartData.chartList[0].chartDatas = res
                    this.drawChartsPie()
                    this.getModelCountByProduce(res[0].device_mfrs)
                })
            },
            // 厂商-型号设备量分布
            getModelCountByProduce(produce) {
                let params = {
                    deviceClass: this.chartData.activeFilter || 'all',
                    produce: produce
                }
                rbKeguanServices.getModelCountByProduce(params).then((res) => {
                    this.barChartData.chartList[0].chartDatas = []
                    this.barChartData.chartList[0].xAxisData = []
                    res.forEach(item => {
                        this.barChartData.chartList[0].chartDatas.push(item.m_count)
                        this.barChartData.chartList[0].xAxisData.push(item.device_model)
                    })
                    this.drawChartsBar()
                })
            },

            // 绘制饼图表
            drawChartsPie() {
                let _this = this
                _this.$nextTick(() => {
                    let subItem = _this.chartData.chartList[0]
                    let myChart = _this.initMyChart(subItem.id)
                    myChart.clear()
                    // 数据格式处理
                    let option = this.$utils.deepClone(ChartOption[subItem.chartOption])
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

                    if (subItem.chartDatas.length > 0) {
                        option.series[0].data = subItem.chartDatas.map((item, index) => {
                            let colorIndex = (index) % (Object.values(colors).length)
                            let num = ''
                            if (index < 2) {
                                num = fixedNumber(item.total, 1)
                            }
                            if (index === 0) {
                                item.selected = true
                            }
                            return Object.assign(item, {
                                name: item.device_mfrs,
                                value: item.device_count,
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

                            })
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
                    myChart.on('click', function (params) {
                        _this.selectedPie(myChart, params.dataIndex)
                    })
                })
            },
            selectedPie(myChart, dataIndex) {
                // 刷新数据
                let option = myChart.getOption()

                option.series[0].data.forEach((item, index) => {
                    item.selected = false
                    if (index === dataIndex) {
                        item.selected = true
                    }
                })
                myChart.setOption(option)

                this.getModelCountByProduce(option.series[0].data[dataIndex].name)
            },
            // 绘制柱图表
            drawChartsBar() {
                let _this = this
                _this.$nextTick(() => {
                    let subItem = _this.barChartData.chartList[0]
                    let myChart = _this.initMyChart(subItem.id)
                    myChart.clear()
                    // 数据格式处理
                    let option = _this.$utils.deepClone(ChartOption[subItem.chartOption])

                    // 默认展示全部数据
                    option.dataZoom[0].start = 0
                    option.dataZoom[0].end = 100

                    option.xAxis.axisLabel.rotate = 30
                    option.xAxis.data = subItem.xAxisData

                    option.series[0].name = '数量'
                    option.series[0].label.show = true
                    option.series[0].data = subItem.chartDatas

                    option.yAxis[0].name = '设备量（个）'

                    myChart.setOption(option, false)
                    // resize自适应
                    _this.setResizeFun(myChart)
                    myChart.on('click', function () {
                    })
                })
            }
        },
        mounted() {
            this.getDeviceCountByProduceAll()
        },
        beforeDestroy() {

        }
    }
</script>

<style lang="scss" scoped>
</style>

