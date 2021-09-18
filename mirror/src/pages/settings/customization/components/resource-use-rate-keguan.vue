<template>
    <!-- 监控： 资源利用率 科管部 -->
    <div class="content-chart"
         style="width: 100%;">
        <section class="chart-title-wrap clearfix">
            <svg class="svg-icon svg-icon-24"
                 aria-hidden="true">
                <use xlink:href="#iconliyongshuai"></use>
            </svg>
            <span class="chart-title">{{chartData.name}}</span>
        </section>

        <!-- 多图表 -->
        <section class="chart-section">
            <div class="chart-box-wrap">
                <div class="chart-box-item"
                     style="width:49%">
                    <div class="chart-filter">
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
                            <div v-if="subIndex < 3"
                                 style="width:32%"
                                 :key="subIndex">
                                <p class="chart-box-title">{{subItem.chartDatas.title}}<br /><span class="num">{{subItem.chartDatas.num}}</span><span class="cell">{{subItem.chartDatas.cell}}</span></p>
                                <div :id="subItem.id"
                                     style="width:100%;height:calc(100% - 40px)"></div>
                            </div>
                        </template>
                    </div>
                </div>

                <div class="chart-box-item"
                     style="width:49%">
                    <div class="chart-filter">
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
                            <div v-if="subIndex > 2"
                                 style="width:49%"
                                 :key="subIndex">
                                <p class="chart-box-title">{{subItem.chartDatas.title}}<br /><span class="num">{{subItem.chartDatas.num}}</span><span class="cell">{{subItem.chartDatas.cell}}</span></p>
                                <div :id="subItem.id"
                                     style="width:100%;height:calc(100% - 40px)"></div>
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

    import rbKeguanServices from 'src/services/iframe/rb-keguan-services.js'
    import ChartOption from 'src/utils/chartOption.js'
    import DrawChart from 'src/utils/drawCharts.js'
    import echarts from 'echarts'

    export default {
        mixins: [DrawChart],
        data() {
            return {
                chartData: {
                    name: '资源利用率',
                    filter: [{ name: 'CPU', label: 'cpu' }, { name: '内存', label: 'memory' }],
                    activeFilter: 'cpu',
                    filter2: [{ name: '磁盘', label: '磁盘' }],
                    activeFilter2: '磁盘',
                    chartList: [
                        {
                            id: 'resource-use-rate-1', chartOption: 'gauge-option',
                            chartDatas: {
                                title: '物理机',
                                num: '0',
                                cell: '核',
                                name: 'CPU',
                                maxRate: '0',// 0%
                                avgRate: '0'
                            }
                        },
                        {
                            id: 'resource-use-rate-2', chartOption: 'gauge-option',
                            chartDatas: {
                                title: '宿主机',
                                num: '0',
                                cell: '核',
                                name: 'CPU',
                                maxRate: '0',
                                avgRate: '0'
                            }
                        },
                        {
                            id: 'resource-use-rate-3', chartOption: 'gauge-option',
                            chartDatas: {
                                title: '虚拟机',
                                num: '0',
                                cell: '核',
                                name: 'CPU',
                                maxRate: '0',
                                avgRate: '0'
                            }
                        },
                        {
                            id: 'resource-use-rate-4', chartOption: 'gauge-option',
                            chartDatas: {
                                title: '块存储',
                                num: '0',
                                cell: 'TB',
                                name: '磁盘',
                                maxRate: '0',
                                avgRate: '0'
                            }
                        },
                        {
                            id: 'resource-use-rate-5', chartOption: 'gauge-option',
                            chartDatas: {
                                title: 'SAN存储',
                                num: '0',
                                cell: 'TB',
                                name: '磁盘',
                                maxRate: '0',
                                avgRate: '0'
                            }
                        }
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
            // 资源利用率：查询服务器数量(内存/CPU)
            queryServiceCount() {
                rbKeguanServices
                    .queryServiceCount()
                    .then(res => {
                        this.chartData.chartList.forEach((item, index) => {
                            if (!res[index]) {
                                return
                            }
                            if (this.chartData.activeFilter === 'cpu') {
                                item.chartDatas.num = (Number(res[index].cpuCoreNumber)).toFixed(0)
                            } else {
                                item.chartDatas.num = (Number(res[index].memorySize)).toFixed(0)
                            }
                        })
                    })
            },
            // cpu、内存利用率
            queryDeviceUsedRate() {
                let params = {
                    startDate: this.conditionParams.dateRange[0],
                    endDate: this.conditionParams.dateRange[1],
                    monitor: this.chartData.activeFilter,
                }
                rbKeguanServices
                    .queryDeviceUsedRate(params).then((res) => {

                        let obj1 = res['X86服务器']
                        let obj2 = res['宿主机']
                        let obj3 = res['虚拟机']
                        if (obj1) {
                            this.chartData.chartList[0].chartDatas.maxRate = (obj1.max + '').indexOf('.') > -1 ? obj1.max.toFixed(2) : (obj1.max || 0)
                            this.chartData.chartList[0].chartDatas.avgRate = (obj1.avg + '').indexOf('.') > -1 ? obj1.avg.toFixed(2) : (obj1.avg || 0)
                        }
                        if (obj2) {
                            this.chartData.chartList[1].chartDatas.maxRate = (obj2.max + '').indexOf('.') > -1 ? obj2.max.toFixed(2) : (obj2.max || 0)
                            this.chartData.chartList[1].chartDatas.avgRate = (obj2.avg + '').indexOf('.') > -1 ? obj2.avg.toFixed(2) : (obj2.avg || 0)
                        }
                        if (obj3) {
                            this.chartData.chartList[2].chartDatas.maxRate = (obj3.max + '').indexOf('.') > -1 ? obj3.max.toFixed(2) : (obj3.max || 0)
                            this.chartData.chartList[2].chartDatas.avgRate = (obj3.avg + '').indexOf('.') > -1 ? obj3.avg.toFixed(2) : (obj3.avg || 0)
                        }

                        this.drawCharts(this.chartData.chartList[0])
                        this.drawCharts(this.chartData.chartList[1])
                        this.drawCharts(this.chartData.chartList[2])

                    })
            },
            // 磁盘利用率
            getStorageUsedRate() {
                let req = {
                    startDate: this.conditionParams.dateRange[0],
                    endDate: this.conditionParams.dateRange[1]
                }
                rbKeguanServices
                    .getStorageUsedRate(req)
                    .then(res => {
                        this.chartData.chartList[3].chartDatas.num = (Number(res.storage)).toFixed(0)
                        this.chartData.chartList[3].chartDatas.maxRate = (res.blockMax + '').indexOf('.') > -1 ? res.blockMax.toFixed(2) : (res.blockMax || 0)
                        this.chartData.chartList[3].chartDatas.avgRate = (res.blockAvg + '').indexOf('.') > -1 ? res.blockAvg.toFixed(2) : (res.blockAvg || 0)

                        this.chartData.chartList[4].chartDatas.num = (Number(res.san)).toFixed(0)
                        this.chartData.chartList[4].chartDatas.maxRate = (res.sanMax + '').indexOf('.') > -1 ? res.sanMax.toFixed(2) : (res.sanMax || 0)
                        this.chartData.chartList[4].chartDatas.avgRate = (res.sanAvg + '').indexOf('.') > -1 ? res.sanAvg.toFixed(2) : (res.sanAvg || 0)

                        this.drawCharts(this.chartData.chartList[3])
                        this.drawCharts(this.chartData.chartList[4])
                    })
            },
            // 获得数据
            getDatas() {

                if (this.chartData.activeFilter === 'cpu') {
                    this.chartData.chartList[0].chartDatas.name = 'CPU'
                    this.chartData.chartList[1].chartDatas.name = 'CPU'
                    this.chartData.chartList[2].chartDatas.name = 'CPU'
                    this.chartData.chartList[0].chartDatas.cell = '核'
                    this.chartData.chartList[1].chartDatas.cell = '核'
                    this.chartData.chartList[2].chartDatas.cell = '核'
                } else {
                    this.chartData.chartList[0].chartDatas.name = '内存'
                    this.chartData.chartList[1].chartDatas.name = '内存'
                    this.chartData.chartList[2].chartDatas.name = '内存'
                    this.chartData.chartList[0].chartDatas.cell = 'GB'
                    this.chartData.chartList[1].chartDatas.cell = 'GB'
                    this.chartData.chartList[2].chartDatas.cell = 'GB'

                }

                this.queryServiceCount()
                this.queryDeviceUsedRate()
                this.getStorageUsedRate()

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

                option.series[0].axisLine.lineStyle.color[0] = [chartDatas.maxRate / 100, new echarts.graphic.LinearGradient(0, 0, 1, 0, colors.blue)]// 右/下/左/上
                if (['resource-use-rate-1', 'resource-use-rate-3'].indexOf(subItem.id) > -1) {
                    option.series[0].axisLine.lineStyle.color[0] = [chartDatas.maxRate / 100, new echarts.graphic.LinearGradient(0, 0, 1, 0, [{ // 右/下/左/上
                        offset: 0, color: '#007643'                    }, {
                        offset: 1,
                        color: '#00DB62'
                    }
                    ])]
                }

                if (chartDatas && Object.keys(chartDatas).length > 0) {
                    option.series[0].data[0].name = ''
                    option.series[0].data[0].value = chartDatas.maxRate

                } else {
                    option.series[0].data[0].name = '占比'
                    option.series[0].data[0].value = '0'
                }
                option.series[0].detail.formatter = function () {

                    return '{detailName|' + chartDatas.name + '\n}{maxRateName|最大利用率\n}{maxRate|' +
                        chartDatas.maxRate + '}{rateCell|%\n}{avgRateName|平均}{avgRate|' +
                        chartDatas.avgRate + '}{rateCell|%\n}'
                }
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
    .chart-filter {
        width: 100%;
        text-align: center;
        margin-bottom: 5px;
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

