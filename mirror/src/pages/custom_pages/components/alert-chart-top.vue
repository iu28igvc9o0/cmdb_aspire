<template>
    <!-- 告警：告警Top10 -->
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
    import { fixedNumber } from 'src/assets/js/utility/rb-filters.js'

    export default {
        mixins: [DrawChart],
        data() {
            return {
                chartData: {
                    name: '告警Top10',
                    filter: [{ name: 'POD池', label: 'podName' }, { name: '设备品牌', label: 'mfrs' }, { name: '设备类型', label: 'deviceType' }],
                    activeFilter: 'podName',
                    chartList: [{ id: 'alert-chart-top10-1', chartOption: 'bar-option', chartDatas: '' },
                    ],
                }
            }
        },
        methods: {
            // 获得数据
            getDatas() {

                let params = {
                    'idcType': '',
                    'deviceType': '',
                    'alertLevel': '',
                    'colName': this.chartData.activeFilter,
                }
                this.$api.queryAlertDevice(params).then((res) => {
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

                        option.legend.show = false
                        // option.yAxis[0].name = '告警量（个）';
                        option.xAxis.axisLabel.rotate = 20
                        option.title.show = true
                        option.title.text = ''
                        option.title.subtext = '告警量（个）'
                        // option.grid.top = 80;

                        option.series[0].label.show = true
                        option.series[0].label.formatter = (param) => {
                            return fixedNumber(param.data, 1, true) ? fixedNumber(param.data, 1, true) : ''
                        }

                        option.series[0] = JSON.parse(JSON.stringify(option.series[0]))
                        option.series[0].name = '告警'

                        if (chartDatas && chartDatas.length > 0) {
                            option.series[0].data = chartDatas.map((item) => {
                                return item.count
                            })
                            option.xAxis.data = chartDatas.map((item) => {
                                return item.colName
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

