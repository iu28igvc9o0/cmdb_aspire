<template>
    <!-- 告警：新增告警趋势 -->
    <div class="content-chart"
         style="width: 100%;height:310px">
        <section class="chart-title-wrap clearfix">
            <svg class="svg-icon svg-icon-24"
                 aria-hidden="true">
                <use xlink:href="#iconxinzenggaojingqushi"></use>
            </svg>
            <span class="chart-title">{{chartData.name}}</span>
            <div class="chart-filter">
                <el-radio-group class="yw-radio-button-wrap chart-filter-item"
                                v-model="chartData.activeFilter2"
                                @change="changeTab">
                    <el-radio-button :label="tabItem.label"
                                     v-for="(tabItem,tabIndex) in chartData.filter2"
                                     :key="tabIndex">{{tabItem.name}}</el-radio-button>
                </el-radio-group>
                <!-- <el-radio-group class="yw-radio-button-wrap chart-filter-item"
                        v-model="chartData.activeFilter3"
                        @change="changeTab">
          <el-radio-button :label="tabItem.label"
                           v-for="(tabItem,tabIndex) in chartData.filter3"
                           :key="tabIndex">{{tabItem.name}}</el-radio-button>
        </el-radio-group> -->
                <el-radio-group class="yw-radio-button-wrap chart-filter-item"
                                v-model="chartData.activeFilter4"
                                @change="changeTab">
                    <el-radio-button :label="tabItem.label"
                                     v-for="(tabItem,tabIndex) in chartData.filter4"
                                     :key="tabIndex">{{tabItem.name}}</el-radio-button>
                </el-radio-group>
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

    export default {
        mixins: [DrawChart],
        data() {
            return {
                chartData: {
                    name: '新增告警趋势',
                    filter: [{ name: '周', label: 'week' }, { name: '月', label: 'month' }, { name: '季', label: 'season' }, { name: '年', label: 'year' }],
                    activeFilter: 'week',
                    filter2: [{ name: '全部', label: '' }, { name: '服务器', label: '服务器' }, { name: '防火墙', label: '防火墙' }, { name: '路由器', label: '路由器' }, { name: '交换机', label: '交换机' }],
                    activeFilter2: '',
                    filter3: [{ name: '严重', label: '5' }, { name: '高', label: '4' }, { name: '中', label: '3' }, { name: '低', label: '2' }],
                    activeFilter3: '5',
                    filter4: [{ name: '全部', label: '' }, { name: 'zabbix', label: 'zabbix' }, { name: 'IPMI', label: 'IPMI' }, { name: '日志', label: '日志' }, { name: '云管', label: '云管' }],
                    activeFilter4: '',
                    chartList: [{ id: 'alert-chart-trend-1', chartOption: 'line-option', chartDatas: '' }],
                }
            }
        },
        methods: {
            // 获得数据
            getDatas() {

                let params = {
                    'idcType': this.conditionParams.poolActive,
                    'span': this.chartData.activeFilter,
                    'deviceType': this.chartData.activeFilter2,
                    'alertLevel': '',

                    // 'alertLevel': this.chartData.activeFilter3,
                    'source': this.chartData.activeFilter4,
                }
                this.$api.queryAlertTrend(params).then((res) => {
                    this.chartData.chartList[0].chartDatas = res// 对象
                    this.drawCharts()
                })


            },

            // 绘制图表
            drawCharts() {
                this.$nextTick(() => {
                    this.chartData.chartList.forEach((subItem) => {
                        let myChart = echarts.init(document.getElementById(subItem.id))
                        myChart.clear()
                        // 数据格式处理
                        let option = JSON.parse(JSON.stringify(ChartOption[subItem.chartOption]))
                        let chartDatas = JSON.parse(JSON.stringify(subItem.chartDatas))
                        let colors = ChartOption['color-option']['linearColor']

                        // option.yAxis.name = '告警量（个）';
                        option.title.show = true
                        option.title.text = ''
                        option.title.subtext = '告警量（个）'

                        option.series[0] = JSON.parse(JSON.stringify(option.series[0]))
                        option.series[1] = JSON.parse(JSON.stringify(option.series[0]))
                        option.series[2] = JSON.parse(JSON.stringify(option.series[0]))
                        option.series[3] = JSON.parse(JSON.stringify(option.series[0]))

                        option.series[0].name = '严重'
                        option.series[1].name = '高'
                        option.series[2].name = '中'
                        option.series[3].name = '低'
                        option.legend.data = [option.series[0].name, option.series[1].name, option.series[2].name, option.series[3].name]

                        option.color = ['red', 'orange', 'yellow', 'blue']
                        option.series[0].lineStyle.color.colorStops = colors.red
                        option.series[1].lineStyle.color.colorStops = colors.orange
                        option.series[2].lineStyle.color.colorStops = colors.yellow
                        option.series[3].lineStyle.color.colorStops = colors.blue

                        if (chartDatas.series && Object.keys(chartDatas.series).length > 0) {
                            option.series[0].data = chartDatas.series['5']
                            option.series[1].data = chartDatas.series['4']
                            option.series[2].data = chartDatas.series['3']
                            option.series[3].data = chartDatas.series['2']
                        } else {
                            option.series[0].data = [0]
                        }

                        if (chartDatas.xAxis && chartDatas.xAxis.length > 0) {
                            option.xAxis.data = chartDatas.xAxis
                            // 新增
                            option.xAxis.boundaryGap = false
                            option.grid.left = '50px'
                        } else {
                            option.xAxis.data = ['暂无']
                        }
                        myChart.setOption(option, true)

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

