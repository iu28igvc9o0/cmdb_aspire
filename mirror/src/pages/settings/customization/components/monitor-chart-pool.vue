<template>
    <!-- 监控：资源池资源利用率分布 -->
    <div class="content-chart"
         style="width: 100%;">
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
                     style="width:49%"
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
                    name: '资源池资源利用率分布',
                    filter: [{ name: 'X86服务器', label: 'X86服务器' }, { name: '云主机', label: '云主机' }],
                    activeFilter: 'X86服务器',
                    filter2: [{ name: 'CPU', label: 'cpu' }, { name: '内存', label: 'memory' }],
                    activeFilter2: 'cpu',
                    chartList: [{ id: 'monitor-chart-pool-1', chartOption: 'bar-option', chartDatas: '', title: '物理机CPU峰值利用率分布' }, { id: 'monitor-chart-pool-2', chartOption: 'bar-option', chartDatas: '', title: '物理机内存峰值利用率分布' }],
                }
            }
        },
        methods: {
            // 获得数据
            getDatas() {

                let params = {
                    'idcType': this.conditionParams.poolActive,
                    'startDate': this.conditionParams.dateRange[0],
                    'endDate': this.conditionParams.dateRange[1],
                    'deviceType': this.chartData.activeFilter,
                    'sourceType': 'cpu',
                }

                this.$api.queryPoolRate(params).then((res) => {
                    this.chartData.chartList[0].chartDatas = res// 对象
                    this.chartData.activeFilter2 = 'cpu'

                    this.drawCharts(this.chartData.chartList[0])
                })

                let params2 = {
                    'idcType': this.conditionParams.poolActive,
                    'startDate': this.conditionParams.dateRange[0],
                    'endDate': this.conditionParams.dateRange[1],
                    'deviceType': this.chartData.activeFilter,
                    'sourceType': 'memory',
                }
                this.$api.queryPoolRate(params2).then((res) => {
                    this.chartData.chartList[1].chartDatas = res
                    this.chartData.activeFilter2 = 'memory'
                    this.drawCharts(this.chartData.chartList[1])
                })

            },

            // 绘制图表
            drawCharts(subItem) {
                this.$nextTick(() => {
                    let myChart = echarts.init(document.getElementById(subItem.id))
                    myChart.clear()
                    // 数据格式处理
                    let option = JSON.parse(JSON.stringify(ChartOption[subItem.chartOption]))
                    let chartDatas = JSON.parse(JSON.stringify(subItem.chartDatas))
                    let colors = ChartOption['color-option']['linearColor']

                    // option.yAxis[0].name = '分布占比(%)';
                    option.yAxis[0].min = 0
                    option.yAxis[0].max = 100
                    option.title.show = true
                    option.title.text = subItem.title
                    option.title.subtext = '分布占比(%)'
                    option.title.padding = [18, 0, 0, 0]
                    option.grid.top = 80

                    option.series[0] = JSON.parse(JSON.stringify(option.series[0]))
                    option.series[1] = JSON.parse(JSON.stringify(option.series[0]))
                    option.series[2] = JSON.parse(JSON.stringify(option.series[0]))
                    option.series[3] = JSON.parse(JSON.stringify(option.series[0]))

                    option.series[3].label.show = option.series[2].label.show = option.series[1].label.show = option.series[0].label.show = true
                    option.series[3].label.formatter = option.series[2].label.formatter = option.series[1].label.formatter = option.series[0].label.formatter = (param) => {
                        return fixedNumber(param.data, 1, true) ? fixedNumber(param.data, 1, true) : ''
                    }

                    let title1 = this.chartData.activeFilter, title2 = 'CPU'

                    if (this.chartData.activeFilter2 == 'cpu') {
                        title2 = 'CPU'
                    } else {
                        title2 = '内存'
                    }

                    option.title.text = `${title1}${title2}峰值利用率分布`

                    let legendName = `${title2}峰值`
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
    /deep/.chart-section {
        padding-left: 15px;
        padding-right: 15px;
    }
</style>

