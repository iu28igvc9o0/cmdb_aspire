
<template>
    <!-- 监控： 存储资源使用总览 科管部 -->
    <div class="content-chart"
         style="width:49.4%;">

        <section class="chart-title-wrap clearfix">
            <svg class="svg-icon svg-icon-24"
                 aria-hidden="true">
                <use xlink:href="#iconrenwuzhihang"></use>
            </svg>
            <span class="chart-title">存储资源使用总览</span>
            <div class="chart-filter">
                <el-select v-model="produceSelected"
                           @change="getChartData"
                           filterable
                           class="mleft10"
                           placeholder="请选择分类">
                    <el-option v-for="val in produceList"
                               :key="val.produce_name"
                               :label="val.produce_name"
                               :value="val.produce_name"></el-option>
                </el-select>
            </div>
        </section>

        <!-- 图表 -->
        <section class="chart-section">
            <div class="chart-box-wrap">
                <div id="resource-use-view"
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
                produceList: [
                    {
                        produce_name: '全部厂商',
                    }
                ],
                produceSelected: '全部厂商',
            }
        },
        computed: {},
        created() {
            this.getListProduceByPage()
            this.getChartData()
        },
        mixins: [DrawChart],
        methods: {
            initChart(chartData) {
                this.$nextTick(() => {
                    let myChart = this.initMyChart('resource-use-view')
                    myChart.clear()
                    let option = JSON.parse(JSON.stringify(ChartOption['line-option']))

                    option.legend.data = ['已使用', '总数']
                    option.textStyle.color = '#9597AB'

                    option.title.show = true
                    option.title.text = ''
                    option.title.subtext = '容量(TB)'
                    option.title.subtextStyle.color = '#fff'

                    // 默认展示全部数据
                    option.dataZoom[0].start = 0
                    option.dataZoom[0].end = 100

                    option.xAxis.data = chartData.xAxis
                    option.xAxis.axisLabel.rotate = 0

                    option.color = ['blue', 'green']

                    option.series = [
                        {
                            name: '已使用',
                            type: 'line',
                            data: chartData.series.use_count,
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
                            name: '总数',
                            type: 'line',
                            data: chartData.series.sum_count,
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
                    myChart.setOption(option, false)
                    this.setResizeFun(myChart)
                })
            },
            // 设备厂商列表
            getListProduceByPage() {
                rbKeguanServices
                    .getListProduceByPage()
                    .then(res => {
                        this.produceList = [].concat(this.produceList, res)
                    })
            },
            // 存储资源使用总览
            getChartData() {
                let req = {
                    startDate: this.conditionParams.dateRange[0],
                    endDate: this.conditionParams.dateRange[1],
                    deviceMrfs: this.produceSelected === '全部厂商' ? '' : this.produceSelected
                }
                rbKeguanServices
                    .getStorageUseView(req)
                    .then(res => {
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
