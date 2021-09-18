<template>
    <!-- 定制化组件： 资源利用率 -->
    <div class="content-chart">
        <section class="chart-title-wrap clearfix">
            <span class="chart-title">{{chartData.name}}</span>
            <div class="chart-filter">
                <el-radio-group class="yw-radio-button-wrap chart-filter-item"
                                v-model="tabData.activeFilter"
                                @change="changeTab">
                    <el-radio-button :label="tabItem.label"
                                     v-for="(tabItem,tabIndex) in tabData.filter"
                                     :key="tabIndex">{{tabItem.name}}</el-radio-button>
                </el-radio-group>
            </div>
        </section>

        <!-- 多图表 -->
        <section class="chart-section">
            <div class="chart-box-wrap">
                <div class="chart-box-item"
                     style="width:49%">
                    <div class="chart-filter wp100 t-center">
                        <el-radio-group class="yw-radio-button-wrap chart-filter-item"
                                        v-model="chartData.activeFilter"
                                        @change="changeTab">
                            <el-radio-button :label="tabItem.label"
                                             v-for="(tabItem,tabIndex) in chartData.filter"
                                             :key="tabIndex">{{tabItem.name}}</el-radio-button>
                        </el-radio-group>
                    </div>
                    <div class="chart-box-main">
                        <template v-for="(subItem,subIndex) in chartData.chartList">
                            <div v-if="subIndex < 2"
                                 style="width:49%"
                                 :key="subIndex">
                                <div :id="subItem.id"
                                     style="width:100%;height:96%"></div>
                                <!-- <p class="chart-box-title">{{subItem.chartDatas.title}}<br /><span class="num">{{subItem.chartDatas.num}}</span><span class="cell">{{subItem.chartDatas.cell}}</span></p> -->
                            </div>
                        </template>
                    </div>
                </div>

                <div class="chart-box-item"
                     style="width:49%">
                    <div class="chart-filter wp100 t-center">
                        <el-radio-group class="yw-radio-button-wrap chart-filter-item"
                                        v-model="chartData.activeFilter2"
                                        @change="changeTab">
                            <el-radio-button :label="tabItem.label"
                                             v-for="(tabItem,tabIndex) in chartData.filter2"
                                             :key="tabIndex">{{tabItem.name}}</el-radio-button>
                        </el-radio-group>
                    </div>
                    <div class="chart-box-main">
                        <template v-for="(subItem,subIndex) in chartData.chartList">
                            <div v-if="subIndex > 1"
                                 style="width:49%"
                                 :key="subIndex">
                                <div :id="subItem.id"
                                     style="width:100%;height:96%"></div>
                                <!-- <p class="chart-box-title">{{subItem.chartDatas.title}}<br /><span class="num">{{subItem.chartDatas.num}}</span><span class="cell">{{subItem.chartDatas.cell}}</span></p> -->
                            </div>
                        </template>
                    </div>

                </div>
            </div>
        </section>
        <!-- 多图表 -->
    </div>
</template>

<script>

    import rbCustomServices from 'src/services/custom_pages/rb-services.js'
    import ChartOption from 'src/utils/chartOption.js'
    import DrawChart from 'src/utils/drawCharts.js'

    export default {
        mixins: [DrawChart],
        data() {
            return {
                tabData: {
                    filter: [
                        { label: '云主机', name: '虚拟机' },
                        { label: 'X86服务器', name: '物理机' },
                    ],
                    activeFilter: '云主机',
                },
                chartData: {
                    name: '资源利用率',
                    filter: [{ name: 'CPU', label: 'cpu' }],
                    activeFilter: 'cpu',
                    filter2: [{ name: '内存', label: 'memory' }],
                    activeFilter2: 'memory',
                    chartList: [
                        {
                            id: 'custom-resource-use-rate-1', chartOption: 'gauge-option',
                            chartDatas: {
                                title: '峰值利用率',
                                num: '0',
                                rankName: '上升',
                                useRate: '0',// 0%
                                compareRate: '0'
                            }
                        },
                        {
                            id: 'custom-resource-use-rate-2', chartOption: 'gauge-option',
                            chartDatas: {
                                title: '均值利用率',
                                num: '0',
                                rankName: '上升',
                                useRate: '0',
                                compareRate: '0'
                            }
                        },
                        {
                            id: 'custom-resource-use-rate-3', chartOption: 'gauge-option',
                            chartDatas: {
                                title: '峰值利用率',
                                num: '0',
                                rankName: '下降',
                                useRate: '0',
                                compareRate: '0'
                            }
                        },
                        {
                            id: 'custom-resource-use-rate-4', chartOption: 'gauge-option',
                            chartDatas: {
                                title: '均值利用率',
                                num: '0',
                                rankName: '上升',
                                useRate: '0',
                                compareRate: '0'
                            }
                        },
                    ],
                    panelNames: {
                        'cpu': {
                            cell: '核',
                            name: 'CPU'
                        },
                        'memory': {
                            cell: 'GB',
                            name: '内存'
                        }
                    }

                }
            }
        },
        methods: {
            // cpu、内存利用率
            getUserRateForZH() {
                let params = {
                    deviceType: this.tabData.activeFilter,
                }
                rbCustomServices
                    .getUserRateForZH(params).then((res) => {
                        if (res) {
                            this.chartData.chartList[0].chartDatas.useRate = res.cpu_max
                            this.chartData.chartList[0].chartDatas.compareRate = res.compare_cpu_max
                            this.chartData.chartList[1].chartDatas.useRate = res.cpu_avg
                            this.chartData.chartList[1].chartDatas.compareRate = res.compare_cpu_avg
                            this.chartData.chartList[2].chartDatas.useRate = res.memory_max
                            this.chartData.chartList[2].chartDatas.compareRate = res.compare_memory_max
                            this.chartData.chartList[3].chartDatas.useRate = res.memory_avg
                            this.chartData.chartList[3].chartDatas.compareRate = res.compare_memory_avg
                        }

                        this.drawCharts(this.chartData.chartList[0])
                        this.drawCharts(this.chartData.chartList[1])
                        this.drawCharts(this.chartData.chartList[2])
                        this.drawCharts(this.chartData.chartList[3])

                    })
            },
            // 获得数据
            getDatas() {

                this.getUserRateForZH()

            },
            // 绘制图表
            drawCharts(subItem) {
                this.$nextTick(() => {
                    let myChart = this.initMyChart(subItem.id)
                    myChart.clear()
                    this.drawGauge(myChart, subItem)
                })
            },

            drawGauge(myChart, subItem) {

                // 数据格式处理
                let option = JSON.parse(JSON.stringify(ChartOption[subItem.chartOption]))
                let chartDatas = JSON.parse(JSON.stringify(subItem.chartDatas))
                let colors = ChartOption['color-option']['linearColor']
                option.series[0] = JSON.parse(JSON.stringify(option.series[0]))
                option.series[0].name = chartDatas.name

                option.series[0].axisLine.lineStyle.color[0] =
                    [chartDatas.useRate / 100, new echarts.graphic.LinearGradient(0, 0, 1, 0, colors.blue)]
                // 右/下/左/上

                // if (['custom-resource-use-rate-1', 'custom-resource-use-rate-3'].indexOf(subItem.id) > -1) {
                //     option.series[0].axisLine.lineStyle.color[0] =
                //         [chartDatas.useRate / 100, new echarts.graphic.LinearGradient(0, 0, 1, 0, [
                //             { // 右/下/左/上
                //                 offset: 0,
                //                 color: '#007643'
                //             },
                //             {
                //                 offset: 1,
                //                 color: '#00DB62'
                //             }
                //         ])]
                // }

                option.series[0].name = '利用率'
                option.series[0].data[0].name = ''
                option.series[0].data[0].value = chartDatas.useRate
                option.series[0].detail.formatter = function () {
                    return `{detailName|${chartDatas.title} ${chartDatas.useRate}%\n}{useRateName|同比上月份 }{compareRate|${chartDatas.compareRate}%}`
                }
                option.series[0].detail.fontSize = '10'
                option.series[0].detail.offsetCenter = [0, '200%']
                option.series[0].detail.lineHeight = 14
                myChart.setOption(option)

                // resize自适应
                this.setResizeFun(myChart)
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
    .chart-section {
        padding-top: 30px;
        padding-bottom: 30px;
    }
    .chart-box-main {
        width: 100%;
        height: calc(100% - 40px);
        display: flex;
        justify-content: space-between;
        .chart-box-title {
            text-align: center;
            font-size: $font-16;
            color: $color-base;
            margin-top: 20px;
            .num {
                font-size: $font-20;
                color: #52c4ff;
            }
            .cell {
                font-size: $font-14;
            }
        }
    }
</style>

