<template>
    <!-- 资源： 资源使用情况分布 -->
    <div class="content-chart"
         style="width:100%">
        <section class="chart-title-wrap clearfix">
            <svg class="svg-icon svg-icon-24"
                 aria-hidden="true">
                <use xlink:href="#iconziyuan"></use>
            </svg>
            <span class="chart-title">{{chartData.name}}</span>
            <div class="chart-filter">
                <el-select v-model="segmentAddressSelected"
                           @change="getChartData"
                           filterable
                           placeholder="请选择分类标签">
                    <el-option v-for="val in segmentAddressList"
                               :key="val.field_value"
                               :label="val.field_name"
                               :value="val.field_value"></el-option>
                </el-select>
                <el-select v-model="systemSelected"
                           @change="getChartData"
                           filterable
                           class="mleft10"
                           placeholder="请选择分类标签">
                    <el-option v-for="val in systemList"
                               :key="val.id"
                               :label="val.bizSystem"
                               :value="val.bizSystem"></el-option>
                </el-select>
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

    import rbKeguanServices from 'src/services/iframe/rb-keguan-services.js'

    export default {
        mixins: [DrawChart],
        components: {

        },
        data() {
            return {
                chartData: {
                    name: '资源使用情况分布',
                    filter: [
                        { label: 'all', name: '全部' },
                        { label: 'phyServer', name: '物理机' },
                        { label: 'netWork', name: '网络' },
                        { label: 'storage', name: '存储' },
                        { label: 'security', name: '安全' }
                    ],
                    activeFilter: 'all',
                    chartList: [{ id: 'resource-chart-resource-use-distribute', chartOption: 'pie-option', chartDatas: [] }]
                },
                barChartData: {
                    name: '',
                    chartList: [{ id: 'resource-chart-resource-use-distribute-bar', chartOption: 'bar-option', chartDatas: [] }]
                },

                // 网段列表
                segmentAddressList: [
                    {
                        field_name: '全部网段',
                        field_value: ''
                    }
                ],
                segmentAddressSelected: '',
                // 业务列表
                systemList: [
                    {
                        bizSystem: '全部业务'
                    }
                ],
                systemSelected: '全部业务'
            }
        },

        methods: {
            // 获取网段地址列表
            getAllSegmentAddress() {
                rbKeguanServices.getAllSegmentAddress().then((res) => {
                    this.segmentAddressList = [].concat(this.segmentAddressList, res)
                })
            },
            // 获取业务系统列表
            getSystemList() {
                rbKeguanServices.getSystemList().then((res) => {
                    this.systemList = [].concat(this.systemList, res)
                })
            },
            // 资源使用状况分布
            getDeviceUseCount() {
                let params = {
                    segmentAddr: this.segmentAddressSelected,
                    bizSystem: this.systemSelected === '全部业务' ? '' : this.systemSelected,
                }
                rbKeguanServices.getDeviceUseCount(params).then((res) => {
                    this.chartData.chartList[0].chartDatas = res
                    this.drawChartsPie()
                })
            },
            // (物理机/虚拟机)资源使用状况分布
            getDeviceUseCountByType() {
                let params = {
                    segmentAddr: this.segmentAddressSelected,
                    bizSystem: this.systemSelected === '全部业务' ? '' : this.systemSelected,
                }
                rbKeguanServices.getDeviceUseCountByType(params).then((res) => {
                    this.barChartData.chartList[0].chartDatas = res
                    this.barChartData.chartList[0].xAxisData = []
                    res.forEach(item => {
                        this.barChartData.chartList[0].xAxisData.push(this.handleDeviceType(item.deviceType))
                    })
                    this.drawChartsBar()
                })
            },
            handleDeviceType(deviceType) {
                if (deviceType === 'X86服务器') {
                    return '物理机'
                } else if (deviceType === '虚拟机') {
                    return '虚拟机'
                }
            },
            getChartData() {
                this.getDeviceUseCount()
                this.getDeviceUseCountByType()
            },

            // 绘制饼图表
            drawChartsPie() {
                let _this = this
                _this.$nextTick(() => {
                    let subItem = _this.chartData.chartList[0]
                    let myChart = _this.initMyChart(subItem.id)
                    myChart.clear()
                    // 数据格式处理
                    let option = JSON.parse(JSON.stringify(ChartOption[subItem.chartOption]))
                    // 环形图颜色
                    let colorSource = ChartOption['color-option']['linearColor']
                    let colors = [
                        colorSource.blue, colorSource.blueLight, colorSource.green, colorSource.greenLight,
                        colorSource.yellow, colorSource.purple
                    ]

                    if (subItem.chartDatas.length > 0) {
                        option.series[0].data = subItem.chartDatas.map((item, index) => {
                            let colorIndex = (index) % (Object.values(colors).length)
                            let num = ''
                            if (index < 2) {
                                num = fixedNumber(item.total_count, 1)
                            }
                            return {
                                name: item.deviceStatus,
                                value: item.d_count,
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
                        option.series[0].radius = ['0', '40%']
                    } else {
                        option.series[0].data = [{
                            name: '暂无数据',
                            value: 0
                        }]
                    }
                    myChart.setOption(option, true)
                    // resize自适应
                    _this.setResizeFun(myChart)
                })
            },
            // 绘制柱图表
            drawChartsBar() {
                let _this = this
                _this.$nextTick(() => {
                    let subItem = _this.barChartData.chartList[0]
                    let myChart = _this.initMyChart(subItem.id)
                    myChart.clear()
                    // 数据格式处理
                    let option = JSON.parse(JSON.stringify(ChartOption[subItem.chartOption]))
                    // 默认展示全部数据
                    option.dataZoom[0].start = 0
                    option.dataZoom[0].end = 100

                    option.xAxis.axisLabel.rotate = 30
                    option.xAxis.data = subItem.xAxisData

                    option.series[0].data = []
                    option.series[0].name = '数量'
                    option.series[0].label.show = true
                    option.series[1] = this.$utils.deepClone(option.series[0])

                    subItem.chartDatas.forEach(item => {
                        option.series[0].data.push(item.use || 0)
                        option.series[1].data.push(item.unUse || 0)
                    })
                    option.series[0].name = '使用'
                    option.series[1].name = '闲置'

                    myChart.setOption(option, false)
                    // resize自适应
                    _this.setResizeFun(myChart)
                })
            }
        },
        created() {
            this.getChartData()
            this.getAllSegmentAddress()
            this.getSystemList()
        },
        beforeDestroy() {
        }
    }
</script>

<style lang="scss" scoped>
</style>

