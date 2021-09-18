<template>
    <!-- 告警：告警设备分类 -->
    <div class="content-chart">
        <section class="chart-title-wrap clearfix">
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

    export default {
        mixins: [DrawChart],
        data() {
            return {
                chartData: {
                    name: '告警设备分类',
                    filter: [{ name: '日', label: 'day' }, { name: '周', label: 'week' }, { name: '月', label: 'month' }, { name: '季', label: 'season' }, { name: '年', label: 'year' }],
                    activeFilter: 'day',
                    chartList: [{ id: 'alert-device-class-1', chartOption: 'pie-option', chartDatas: '' },
                    ],
                }
            }
        },
        methods: {
            // 获得数据
            getDatas() {

                let params = {
                    'span': this.chartData.activeFilter,
                }
                this.$api.queryAlertClass(params).then((res) => {
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
                        let option = this.$utils.deepClone(ChartOption[subItem.chartOption])
                        let chartDatas = this.$utils.deepClone(subItem.chartDatas)
                        let colors = ChartOption['color-option']['linearColor']

                        option.series[0] = this.$utils.deepClone(option.series[0])
                        option.series[0].name = ['告警设备分类']
                        option.series[0].radius = ['40%', '60%']
                        option.series[0].center = ['50%', '50%']

                        if (chartDatas && chartDatas.length > 0) {
                            option.series[0].data = chartDatas.map((item, index) => {
                                let colorIndex = (index) % (Object.values(colors).length)
                                return {
                                    name: item.deviceType,
                                    value: item.alertNum,
                                    label: {
                                        color: (Object.values(colors)[colorIndex][1].color)
                                    },
                                    itemStyle: {
                                        color: {
                                            type: 'linear',
                                            x: 0,
                                            y: 0, // 由上至下
                                            x2: 0,
                                            y2: 1,
                                            colorStops: (Object.values(colors)[colorIndex])
                                        }
                                    }
                                }
                            })
                        } else {
                            option.series[0].data = [{
                                name: '暂无数据',
                                value: 0
                            }]
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
            this.drawCharts()// 避免数据返回前页面图表为空
            this.getDatas()
        }

    }
</script>

<style lang="scss" scoped>
</style>

