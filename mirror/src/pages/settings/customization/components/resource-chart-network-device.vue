<template>
    <!-- 资源： 服务器业务使用占比 -->
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
                <div class="chart-box-item pleft20 pright10">

                    <div class="chart-filter">
                        <el-radio-group class="yw-radio-button-wrap chart-filter-item"
                                        v-model="tableTab.activeFilter"
                                        @change="changeBusinessTab">
                            <el-radio-button :label="tabItem.label"
                                             v-for="(tabItem,tabIndex) in tableTab.filter"
                                             :key="tabIndex">{{tabItem.name}}</el-radio-button>
                        </el-radio-group>
                    </div>
                    <el-table :data="businessList"
                              height="calc(100% - 46px)"
                              class="yw-rank-table mtop10">
                        <el-table-column prop="bizSystem"
                                         label="业务系统"
                                         min-width="200"
                                         show-overflow-tooltip></el-table-column>
                        <el-table-column prop="d_count"
                                         label="使用量"
                                         min-width="80"></el-table-column>
                        <el-table-column prop="rate"
                                         label="占比"
                                         min-width="90"></el-table-column>
                    </el-table>
                </div>
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
                    name: '服务器业务使用占比',
                    filter: [
                        { label: 'all', name: '全部' },
                        { label: 'X86服务器', name: '物理机' },
                        { label: '虚拟机', name: '虚拟机' }
                    ],
                    activeFilter: 'all',
                    chartList: [{ id: 'resource-chart-device-type-distribute', chartOption: 'pie-option', chartDatas: [] }]
                },
                tableTab: {
                    filter: [
                        { label: 'leadSystem', name: '核心业务使用量' },
                        { label: 'otherSystem', name: '其他业务使用量' }
                    ],
                    activeFilter: 'leadSystem',
                },
                businessList: []
            }
        },

        methods: {
            // 网段用途设备总量分布
            getDeviceCountBySegmentUse() {
                let params = {
                    deviceType: this.chartData.activeFilter,
                }
                this.chartData.chartList[0].chartDatas = []
                rbKeguanServices.getDeviceCountBySegmentUse(params).then((res) => {
                    this.chartData.chartList[0].chartDatas = res
                    this.drawChartsPie()
                })
            },
            // 服务器业务使用量占比
            getDeviceCountBySystem() {
                let params = {
                    deviceType: this.chartData.activeFilter,
                    systemType: this.tableTab.activeFilter,
                }
                rbKeguanServices.getDeviceCountBySystem(params).then((res) => {
                    this.businessList = res
                })
            },
            changeTab() {
                this.getDeviceCountBySegmentUse()
                this.getDeviceCountBySystem()
            },
            changeBusinessTab() {
                this.getDeviceCountBySystem()
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
                    option.title.show = true
                    option.title.text = ''
                    option.title.subtext = '设备量分布'
                    option.title.subtextStyle.fontSize = 10
                    option.title.x = '49%'
                    option.title.y = '44%'
                    option.series[0] = JSON.parse(JSON.stringify(option.series[0]))

                    if (subItem.chartDatas.length > 0) {
                        option.series[0].data = subItem.chartDatas.map((item, index) => {
                            let colorIndex = (index) % (Object.values(colors).length)
                            let num = ''
                            if (index < 2) {
                                num = fixedNumber(item.total, 1)
                            }
                            return {
                                name: item.segment_use,
                                value: item.segment_use_count,
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
        },
        mounted() {
            this.getDeviceCountBySegmentUse()
            this.getDeviceCountBySystem()
        },
        beforeDestroy() {
        }
    }
</script>

<style lang="scss" scoped>
</style>

