<template>
    <div class="content-chart"
         style="width: 49.4%;">
        <section class="chart-title-wrap clearfix">
            <svg class="svg-icon svg-icon-24"
                 aria-hidden="true">
                <use xlink:href="#iconliyongshuai"></use>
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
                    name: '业务系统资源利用率Top10',
                    filter: [{ name: '从高到低', label: 'desc' }, { name: '从低到高', label: 'asc' }],
                    activeFilter: 'desc',
                    chartList: [{ id: 'resource-bizsys-rate-top-1', chartOption: 'bar-option', chartDatas: '' },
                    ],
                }
            }
        },
        methods: {
            // 获得数据
            getDatas() {

                let params = {
                    'startDate': '',
                    'endDate': '',
                    'size': 10,
                    'order': this.chartData.activeFilter,
                }
                this.$api.queryBizUseRate(params).then((res) => {
                    this.chartData.chartList[0].chartDatas = res// 对象
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
                        let colors = ChartOption['color-option']['linearColor']

                        // option.yAxis[0].name = '分布占比(%)';
                        option.title.show = true
                        option.title.subtext = '分布占比(%)'
                        option.yAxis[0].min = 0
                        option.yAxis[0].max = 100

                        option.series[0] = JSON.parse(JSON.stringify(option.series[0]))
                        option.series[1] = JSON.parse(JSON.stringify(option.series[0]))
                        option.series[2] = JSON.parse(JSON.stringify(option.series[0]))
                        option.series[3] = JSON.parse(JSON.stringify(option.series[0]))

                        option.series[3].label.show = option.series[2].label.show = option.series[1].label.show = option.series[0].label.show = true
                        option.series[3].label.formatter = option.series[2].label.formatter = option.series[1].label.formatter = option.series[0].label.formatter = (param) => {
                            return fixedNumber(param.data, 1, true) ? fixedNumber(param.data, 1, true) : ''
                        }

                        let legendName = '利用率'
                        option.series[0].name = `${legendName}≥80%`
                        option.series[1].name = `80%>${legendName}≥40%`
                        option.series[2].name = `40%>${legendName}≥15%`
                        option.series[3].name = `${legendName}<15%`

                        option.legend.data = [option.series[0].name, option.series[1].name, option.series[2].name, option.series[3].name]
                        option.series[0].itemStyle.color.colorStops = colors.greenLight
                        option.series[1].itemStyle.color.colorStops = colors.green
                        option.series[2].itemStyle.color.colorStops = colors.blueLight
                        option.series[3].itemStyle.color.colorStops = colors.blue

                        let dataList = Object.values(chartDatas)
                        if (dataList && dataList.length > 0) {
                            option.series[0].data = []
                            option.series[1].data = []
                            option.series[2].data = []
                            option.series[3].data = []
                            dataList.forEach((item) => {
                                option.series[0].data.push(item.count ? fixedNumber(item['80'] / item.count * 100, 0) : 0)
                                option.series[1].data.push(item.count ? fixedNumber(item['40-80'] / item.count * 100, 0) : 0)
                                option.series[2].data.push(item.count ? fixedNumber(item['15-40'] / item.count * 100, 0) : 0)
                                option.series[3].data.push(item.count ? fixedNumber(item['15'] / item.count * 100, 0) : 0)
                            })
                            option.xAxis.data = Object.keys(chartDatas)
                        } else {
                            option.series[0].data = [0]
                            option.xAxis.data = ['暂无']
                        }

                        option.tooltip.formatter = (param) => {

                            let axisValue = param[0].name
                            let valuesObj = chartDatas[axisValue]
                            let percent1 = 0, percent2 = 0, percent3 = 0, percent4 = 0
                            if (!valuesObj) {
                                return
                            }
                            if (valuesObj['count']) {
                                percent1 = fixedNumber(valuesObj['80'] / valuesObj['count'] * 100, 0)
                                percent2 = fixedNumber(valuesObj['40-80'] / valuesObj['count'] * 100, 0)
                                percent3 = fixedNumber(valuesObj['15-40'] / valuesObj['count'] * 100, 0)
                                percent4 = fixedNumber(valuesObj['15'] / valuesObj['count'] * 100, 0)

                            }
                            let title = `${axisValue} (总设备${valuesObj['deviceCount']}/监控采集${valuesObj['count']})<br/>`

                            let text1 = `1. ${param[0].seriesName}：设备数${valuesObj['80']} (${percent1}%)<br/>`
                            let text2 = `2. ${param[1].seriesName}：设备数${valuesObj['40-80']} (${percent2}%)<br/>`
                            let text3 = `3. ${param[2].seriesName}：设备数${valuesObj['15-40']}(${percent3}%)<br/>`
                            let text4 = `4. ${param[3].seriesName}：设备数${valuesObj['15']} (${percent4}%)<br/>`

                            return title + text1 + text2 + text3 + text4
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

