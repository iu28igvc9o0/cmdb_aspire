<template>

    <div class="content-chart"
         style="width: 24.2%;">
        <section class="chart-title-wrap clearfix">
            <svg class="svg-icon svg-icon-24"
                 aria-hidden="true">
                <use xlink:href="#iconloudongdengjifenbu"></use>
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

    export default {
        mixins: [DrawChart],
        data() {
            return {
                chartData: {
                    name: '资源池漏洞',
                    filter: [{ name: '物理机', label: 'X86服务器' }, { name: '虚拟机', label: '云主机' }],
                    activeFilter: 'X86服务器',
                    chartList: [{ id: 'safe-pool-bug-1', chartOption: 'bar-option', chartDatas: '' },
                    ],
                }
            }
        },
        methods: {
            // 获得数据
            getDatas() {

                let params = {

                }
                this.$api.querySafePoolBug(params).then((res) => {
                    this.chartData.chartList[0].chartDatas = res
                    this.drawCharts()
                })

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
                        let colors = ChartOption['color-option']['linearColorToDeep']

                        option.series[0] = JSON.parse(JSON.stringify(option.series[0]))
                        option.series[1] = JSON.parse(JSON.stringify(option.series[0]))
                        option.series[2] = JSON.parse(JSON.stringify(option.series[0]))

                        option.series[0].name = '高危'
                        option.series[1].name = '中危'
                        option.series[2].name = '低危'

                        option.legend.data = [option.series[0].name, option.series[1].name, option.series[2].name]
                        option.series[0].itemStyle.color.colorStops = colors.red
                        option.series[1].itemStyle.color.colorStops = colors.orange
                        option.series[2].itemStyle.color.colorStops = colors.yellow

                        option.series[0].stack = option.series[1].stack = option.series[2].stack = 'chart-stack'
                        // option.series[0].itemStyle.barBorderRadius = option.series[1].itemStyle.barBorderRadius = 0;
                        // option.series[2].itemStyle.barBorderRadius = [4, 4, 0, 0];

                        // option.xAxis.axisLabel.rotate = 20;
                        // option.yAxis[0].name = '漏洞数（个）';
                        option.title.show = true
                        option.title.subtext = '漏洞数（个）'


                        if (chartDatas && chartDatas.length > 0) {
                            option.series[0].data = chartDatas.map((item) => {
                                return item.hCount
                            })
                            option.series[1].data = chartDatas.map((item) => {
                                return item.mCount
                            })
                            option.series[2].data = chartDatas.map((item) => {
                                return item.lCount
                            })

                            option.xAxis.data = chartDatas.map((item) => {
                                return item.idcType
                            })
                        } else {
                            option.series[0].data = [0]
                            option.xAxis.data = ['暂无']
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

