<template>
    <div class="content-chart"
         style="width: 24.2%;">
        <section class="chart-title-wrap clearfix">
            <svg class="svg-icon svg-icon-24"
                 aria-hidden="true">
                <use xlink:href="#icongongdan"></use>
            </svg>
            <span class="chart-title">{{chartData.name}}</span>
        </section>

        <!-- 多图表 -->
        <section class="chart-section">
            <div class="chart-box-wrap">
                <div class="chart-box-item"
                     style="width:100%"
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


    export default {
        mixins: [DrawChart],
        data() {
            return {
                chartData: {
                    name: '工单类型分布',
                    filter: [{ name: '物理机', label: 'X86服务器' }, { name: '虚拟机', label: '云主机' }],
                    activeFilter: 'X86服务器',
                    chartList: [{ id: 'order-type-distributed-1', chartOption: 'funnel-option', chartDatas: '' },
                    ],
                }
            }
        },
        methods: {
            // 获得数据
            getDatas() {

                let params = {

                }
                this.$api.queryOrderType(params).then((res) => {
                    this.chartData.chartList[0].chartDatas = res
                    this.drawCharts()
                })
                // this.chartData.chartList[0].chartDatas = [
                //   { name: '告警工单', value: 120 },
                //   { name: '维保管理', value: 100 },
                //   { name: '周报工单', value: 80 },
                //   { name: '网络策略', value: 60 },
                //   { name: '变更流程', value: 40 },
                //   { name: '账号申请', value: 20 },

                // ]
                // this.drawCharts();

            },

            // 绘制图表
            drawCharts() {
                this.$nextTick(() => {
                    this.chartData.chartList.forEach((subItem) => {
                        // 非空判断，避免报错
                        let myChartDom = document.getElementById(subItem.id)
                        if (!myChartDom) {
                            return
                        }
                        let myChart = echarts.init(myChartDom)
                        myChart.clear()
                        // 数据格式处理
                        let option = JSON.parse(JSON.stringify(ChartOption[subItem.chartOption]))
                        let chartDatas = JSON.parse(JSON.stringify(subItem.chartDatas))
                        let colorSource = ChartOption['color-option']['linearColor']
                        let green1 = [{ offset: 0, color: '#00E5C2' }, { offset: 1, color: '#4AFFBD' }]
                        let green2 = [{ offset: 0, color: '#47ED21' }, { offset: 1, color: '#8EFF82' }]
                        let yellow = [{ offset: 0, color: '#B2DF00' }, { offset: 1, color: '#E7FF85' }]
                        let colors = [colorSource.blue, colorSource.cyan, colorSource.cyanLight, green1, green2, yellow]

                        option.series[0] = JSON.parse(JSON.stringify(option.series[0]))
                        option.series[1] = JSON.parse(JSON.stringify(option.series[1]))
                        option.series[0].name = '工单类型分布'
                        option.series[1].name = '工单类型分布'

                        if (chartDatas && chartDatas.length > 0) {
                            chartDatas.forEach((item, index) => {
                                let itemStyle = {
                                    borderColor: 'transparent',
                                    color: {
                                        type: 'linear',
                                        x: 0,
                                        y: 0,
                                        x2: 0,
                                        y2: 1,
                                        colorStops: colors[index],
                                        global: false // 缺省为 false
                                    }
                                }
                                item.itemStyle = itemStyle
                            })

                            option.series[1].data = option.series[0].data = chartDatas.slice(0, 6).map((item, index) => {
                                return {
                                    name: item.procDefName,
                                    value: 120 - index * 20,
                                    num: fixedNumber(item.count, 1, true),
                                    numDetail: item.count,
                                    // rate: item.percent,
                                    itemStyle: item.itemStyle
                                }
                            })

                            option.series[1].label.formatter = (param) => {
                                return param.data.num
                            }
                            option.tooltip.formatter = (param) => {
                                return `${param.seriesName} <br/>${param.data.name} : ${param.data.numDetail}`
                            }

                        } else {
                            option.series[0].data = [
                                {
                                    value: 60,
                                    name: '暂无数据1'
                                },
                                {
                                    value: 40,
                                    name: '暂无数据2'
                                },
                                {
                                    value: 20,
                                    name: '暂无数据3'
                                },
                            ]
                            option.series[1].data = [
                                {
                                    value: 60,
                                    name: '暂无数据1'
                                },
                                {
                                    value: 40,
                                    name: '暂无数据2'
                                },
                                {
                                    value: 20,
                                    name: '暂无数据3'
                                },
                            ]
                        }

                        myChart.setOption(option)

                        // resize自适应
                        this.setResizeFun(myChart)
                    })

                })
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
</style>

