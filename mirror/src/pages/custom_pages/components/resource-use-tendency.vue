<template>
    <!-- 定制化组件： 资源利用率趋势 -->
    <div class="content-chart">
        <section class="chart-title-wrap clearfix">
            <span class="chart-title">资源利用率趋势</span>
            <div class="chart-filter">
                <el-radio-group class="yw-radio-button-wrap chart-filter-item"
                                v-model="tabData.activeFilter2"
                                @change="changeTab">
                    <el-radio-button :label="tabItem.label"
                                     v-for="(tabItem,tabIndex) in tabData.filter2"
                                     :key="tabIndex">{{tabItem.name}}</el-radio-button>
                </el-radio-group>
                <el-radio-group class="yw-radio-button-wrap chart-filter-item mleft10"
                                v-model="tabData.activeFilter"
                                @change="changeTab">
                    <el-radio-button :label="tabItem.label"
                                     v-for="(tabItem,tabIndex) in tabData.filter"
                                     :key="tabIndex">{{tabItem.name}}</el-radio-button>
                </el-radio-group>
            </div>
        </section>

        <!-- 图表 -->
        <section class="chart-section">
            <div class="chart-box-wrap">
                <div id="custom-resource-use-tendency"
                     class="chart-box-item"
                     style="width: 100%; height: 100%;">
                </div>
            </div>
        </section>
    </div>
</template>
 
<script>
    import rbKeguanServices from 'src/services/iframe/rb-keguan-services.js'
    import ChartOption from 'src/utils/chartOption'
    import DrawChart from 'src/utils/drawCharts'
    export default {
        components: {},
        data() {
            return {
                tabData: {
                    filter: [
                        { label: '云主机', name: '虚拟机' },
                        { label: 'X86服务器', name: '物理机' },
                    ],
                    activeFilter: '云主机',
                    filter2: [
                        { name: 'CPU', label: 'cpu' },
                        { name: '内存', label: 'memory' },
                    ],
                    activeFilter2: 'cpu',
                    filter3: [
                        { name: '峰值趋势', label: '峰值趋势' },
                        { name: '均值趋势', label: '均值趋势' },
                    ],
                    activeFilter3: '峰值趋势',
                },
                segmentUseList: [
                    {
                        field_name: '全部用途',
                        field_value: ''
                    }
                ],
                segmentUseSelected: '',
                // 业务列表
                systemList: [
                    {
                        bizSystem: '全部业务'
                    }
                ],
                systemSelected: '全部业务'
            }
        },
        computed: {},
        mounted() {
            // this.getSystemList()
            // this.getSegmentUseList()
            this.getChartData()
        },
        mixins: [DrawChart],
        methods: {
            initChart(chartData) {
                this.$nextTick(() => {
                    let myChart = this.initMyChart('custom-resource-use-tendency')
                    myChart && myChart.clear()
                    let option = JSON.parse(JSON.stringify(ChartOption['line-option']))

                    option.legend.data = ['峰值', '平均值']
                    option.textStyle.color = '#9597AB'

                    option.title.show = true
                    option.title.text = ''
                    option.title.subtext = '分布占比(%)'
                    option.title.subtextStyle.color = '#fff'

                    // 默认展示全部数据
                    option.dataZoom[0].start = 0
                    option.dataZoom[0].end = 100

                    option.xAxis.data = chartData.xAxis
                    option.xAxis.axisLabel.rotate = 0

                    option.color = ['blue', 'green']

                    option.series = [
                        {
                            name: '峰值',
                            type: 'line',
                            data: chartData.series.max,
                            smooth: true,
                            lineStyle: {
                                width: 4,
                                color: {
                                    type: 'linear',
                                    x: 0,
                                    y: 0,
                                    x2: 1,
                                    y2: 0,
                                    colorStops: [
                                        {
                                            offset: 0,
                                            color: '#004CD8' // 0% 处的颜色
                                        },
                                        {
                                            offset: 1,
                                            color: '#006CFF' // 100% 处的颜色
                                        }
                                    ],
                                    global: false // 缺省为 false
                                }

                            }
                        },
                        {
                            name: '平均值',
                            type: 'line',
                            data: chartData.series.avg,
                            smooth: true,
                            lineStyle: {
                                width: 4,
                                color: {
                                    type: 'linear',
                                    x: 0,
                                    y: 0,
                                    x2: 1,
                                    y2: 0,
                                    colorStops: [
                                        {
                                            offset: 0,
                                            color: '#02BD6C' // 0% 处的颜色
                                        },
                                        {
                                            offset: 1,
                                            color: '#4BFF9B' // 100% 处的颜色
                                        }
                                    ],
                                    global: false // 缺省为 false
                                }

                            }
                        }
                    ]
                    if (!option.xAxis.data.length) {
                        option.xAxis.data = ['暂无数据']
                        option.series[0].data = [0]
                        option.series[1].data = [0]
                    }

                    // 绘制图表
                    myChart && myChart.setOption(option, false)
                    this.setResizeFun(myChart)
                })
            },
            // 获取业务系统列表
            getSystemList() {
                rbKeguanServices
                    .getSystemList()
                    .then(res => {
                        this.systemList = [].concat(this.systemList, res)
                    })
            },
            // 获取网段用途列表
            getSegmentUseList() {
                rbKeguanServices
                    .getSegmentUseList()
                    .then(res => {
                        this.segmentUseList = [].concat(this.segmentUseList, res)
                    })
            },
            changeTab() {
                this.getChartData()
            },
            // 资源利用率趋势
            getChartData() {
                let req = {
                    deviceType: this.tabData.activeFilter,
                    sourceType: this.tabData.activeFilter2,
                    indexType: 1
                }

                this.$api.queryPoolTrend(req)
                    .then(res => {
                        res = res || { 'xAxis': [], 'series': { 'avg': [], 'max': [] } }
                        this.initChart(res)
                    })
            },
        }
    }
</script>
 
<style  lang="scss" scoped>
    .mtop15 {
        margin-top: 15px;
    }
</style>
