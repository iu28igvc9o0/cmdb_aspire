<template>
    <!-- 监控：租户资源利用率分布 -->
    <div class="content-chart"
         style="width: 100%;">
        <section class="chart-title-wrap clearfix">
            <svg class="svg-icon svg-icon-24"
                 aria-hidden="true">
                <use xlink:href="#iconliyongshuai"></use>
            </svg>
            <span class="chart-title">{{this.moduleName || chartData.name}}</span>
            <div class="chart-filter">
                <el-radio-group class="yw-radio-button-wrap chart-filter-item"
                                v-model="chartData.activeFilter"
                                @change="changeTab">
                    <el-radio-button :label="tabItem.label"
                                     v-for="(tabItem,tabIndex) in chartData.filter"
                                     :key="tabIndex">{{tabItem.name}}</el-radio-button>
                </el-radio-group>
                <el-radio-group class="yw-radio-button-wrap chart-filter-item"
                                v-model="chartData.activeFilter2"
                                @change="changeTab">
                    <el-radio-button :label="tabItem.label"
                                     v-for="(tabItem,tabIndex) in chartData.filter2"
                                     :key="tabIndex">{{tabItem.name}}</el-radio-button>
                </el-radio-group>
                <YwSelectTree class="chart-filter-item"
                              @changeTree="changeTree"></YwSelectTree>
                <!-- <treeselect class="yw-treeselect chart-filter-item"
                            style="height:26px;"
                            v-model="chartData.activeFilter3"
                            :options="chartData.filter3"
                            :multiple="true"
                            :auto-load-root-options="false"
                            :limit="1"
                            @input="changeTab"
                            placeholder="请选择业务系统">
                </treeselect> -->
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
    // import Treeselect from '@riophae/vue-treeselect'
    // import '@riophae/vue-treeselect/dist/vue-treeselect.css'

    export default {
        mixins: [DrawChart],
        components: {
            // Treeselect,
            YwSelectTree: () => import('src/components/common/yw-selectTree-bizSystem.vue'),
        },
        data() {
            return {
                chartData: {
                    name: '租户资源利用率分布',
                    filter: [{ name: 'X86服务器', label: 'X86服务器' }, { name: '云主机', label: '云主机' }],
                    activeFilter: 'X86服务器',
                    filter2: [{ name: 'CPU', label: 'cpu' }, { name: '内存', label: 'memory' }],
                    activeFilter2: 'cpu',
                    filter3: [],
                    activeFilter3: null,
                    chartList: [{ id: 'monitor-chart-user-1', chartOption: 'bar-option', chartDatas: '' },
                    ],
                }
            }
        },
        methods: {
            // 获得查询条件
            getParams() {
                this.$api.queryBizSys().then((res) => {
                    this.chartData.filter3 = res
                    this.chartData.activeFilter3 = null
                })
                this.chartData.activeFilter3 = null

            },

            // 获得数据
            getDatas() {

                let params = {
                    'idcType': this.conditionParams.poolActive,
                    'startDate': this.conditionParams.dateRange[0],
                    'endDate': this.conditionParams.dateRange[1],
                    'deviceType': this.chartData.activeFilter,
                    'sourceType': this.chartData.activeFilter2,
                    'bizSystem': this.chartData.activeFilter3,
                }
                this.$api.queryUserRate(params).then((res) => {
                    this.chartData.chartList[0].chartDatas = res// 对象
                    this.drawCharts(this.chartData.chartList[0])
                })

            },

            // 绘制图表
            drawCharts(subItem) {
                this.$nextTick(() => {
                    console.log(777)
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
                    let title1 = this.chartData.activeFilter, title2 = 'CPU'

                    if (this.chartData.activeFilter2 == 'cpu') {
                        title2 = 'CPU'
                    } else {
                        title2 = '内存'
                    }

                    option.title.text = `各业务系统${title1}${title2}峰值利用率分布`
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

                    let legendName = `${title2}峰值`
                    option.series[0].name = `${legendName}≥80%`
                    option.series[1].name = `80%>${legendName}≥40%`
                    option.series[2].name = `40%>${legendName}≥15%`
                    option.series[3].name = `${legendName}<15%`

                    option.legend.data = [option.series[0].name, option.series[1].name, option.series[2].name, option.series[3].name]
                    // 新增距离
                    option.legend.top = '13px'

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

                        // let dataList = Object.values(chartDatas);
                        // option.series[0].data = dataList.map((item) => {
                        //   return item.count ? fixedNumber(item['80'] / item.count * 100, 0) : 0
                        // });
                        // option.series[1].data = dataList.map((item) => {
                        //   return item.count ? fixedNumber(item['40-80'] / item.count * 100, 0) : 0
                        // });
                        // option.series[2].data = dataList.map((item) => {
                        //   return item.count ? fixedNumber(item['15-40'] / item.count * 100, 0) : 0
                        // });
                        // option.series[3].data = dataList.map((item) => {
                        //   return item.count ? fixedNumber(item['15'] / item.count * 100, 0) : 0
                        // });
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
            changeTree(val = '') {
                this.chartData.activeFilter3 = val
                this.changeTab()
            }
        },
        created() {
            // this.getParams()
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

