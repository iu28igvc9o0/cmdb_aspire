<template>
    <!-- 监控：资源利用率趋势 -->
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
                <el-radio-group class="yw-radio-button-wrap chart-filter-item"
                                v-model="chartData.activeFilter2"
                                @change="changeTab">
                    <el-radio-button :label="tabItem.label"
                                     v-for="(tabItem,tabIndex) in chartData.filter2"
                                     :key="tabIndex">{{tabItem.name}}</el-radio-button>
                </el-radio-group>
                <!-- <treeselect class="yw-treeselect chart-filter-item"
                            v-model="chartData.activeFilter3"
                            :options="chartData.filter3"
                            :multiple="true"
                            :limit="1"
                            @input="changeTab"
                            placeholder="请选择业务系统">
                </treeselect> -->
                <YwSelectTree class="chart-filter-item"
                              @changeTree="changeTree"></YwSelectTree>
                <el-radio-group class="yw-radio-button-wrap chart-filter-item"
                                v-model="chartData.activeFilter4"
                                @change="changeTab">
                    <el-radio-button :label="tabItem.label"
                                     v-for="(tabItem,tabIndex) in chartData.filter4"
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
        components: {
            YwSelectTree: () => import('src/components/common/yw-selectTree-bizSystem.vue'),
        },
        data() {
            return {
                chartData: {
                    name: '资源利用率趋势',
                    filter: [{ name: 'X86服务器', label: 'X86服务器' }, { name: '云主机', label: '云主机' }],
                    activeFilter: 'X86服务器',
                    filter2: [{ name: 'CPU', label: 'cpu' }, { name: '内存', label: 'memory' }],
                    activeFilter2: 'cpu',
                    filter3: [],
                    activeFilter3: null,
                    filter4: [{ name: '日', label: 'day' }, { name: '周', label: 'week' }, { name: '月', label: 'month' }],
                    activeFilter4: 'day',
                    chartList: [{ id: 'monitor-chart-trend-1', chartOption: 'line-option', chartDatas: '' },
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
                    'stateTimeType': this.chartData.activeFilter4,
                }
                this.$api.queryPoolTrend(params).then((res) => {
                    this.chartData.chartList[0].chartDatas = res// 对象
                    this.drawCharts(this.chartData.chartList[0])
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

                    option.series[0] = JSON.parse(JSON.stringify(option.series[0]))
                    option.series[1] = JSON.parse(JSON.stringify(option.series[0]))
                    option.series[0].name = '均值利用率'
                    option.series[1].name = '峰值利用率'
                    option.series[0].lineStyle.color.colorStops = colors.blue
                    option.series[1].lineStyle.color.colorStops = colors.green

                    // option.yAxis.name = '利用率%';
                    option.title.show = true

                    let title1 = this.chartData.activeFilter, title2 = 'CPU'

                    if (this.chartData.activeFilter2 == 'cpu') {
                        title2 = 'CPU'
                    } else {
                        title2 = '内存'
                    }

                    option.title.text = `${title1}${title2}资源利用率趋势`
                    option.title.subtext = '利用率%'
                    option.title.padding = [18, 0, 0, 0]
                    option.grid.top = 80
                    option.legend.data = [option.series[0].name, option.series[1].name]
                    // 新增距离
                    option.legend.top = '13px'
                    if (chartDatas['xAxis'] && chartDatas['xAxis'].length > 0) {

                        option.series[0].data = chartDatas['series']['avg']
                        option.series[1].data = chartDatas['series']['max']
                        option.xAxis.data = chartDatas['xAxis']
                    } else {
                        option.series[1].data = [0]
                        option.xAxis.data = ['暂无']
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

