<template>
    <div class="content-chart"
         :style="{width: isCustomPage ? '100%' : '24.2%', height: isCustomPage ? '490px' : '310px'}">
        <section class="chart-title-wrap clearfix">
            <svg class="svg-icon svg-icon-24"
                 aria-hidden="true">
                <use xlink:href="#icongaojing"></use>
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
                    name: '厂家Top10',
                    filter: [{ name: '全部', label: '' }, { name: '服务器', label: '服务器' }, { name: '防火墙', label: '防火墙' }, { name: '路由器', label: '路由器' }, { name: '交换机', label: '交换机' }],
                    activeFilter: '',
                    chartList: [{ id: 'alert-company-top-1', chartOption: 'bar-option', chartDatas: '' },
                    ],
                }
            }
        },
        methods: {
            // 获得数据
            getDatas() {

                let params = {
                    'idcType': this.conditionParams.poolActive,
                    'colName': 'mfrs',
                    'deviceType': this.chartData.activeFilter,
                    'alertLevel': '',
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
                        let myChart = echarts.init(document.getElementById(subItem.id))
                        myChart.clear()
                        // 数据格式处理
                        let option = JSON.parse(JSON.stringify(ChartOption[subItem.chartOption]))
                        let chartDatas = JSON.parse(JSON.stringify(subItem.chartDatas))

                        option.series[0] = JSON.parse(JSON.stringify(option.series[0]))
                        option.series[0].label.show = true
                        option.series[0].label.formatter = (param) => {
                            return fixedNumber(param.data, 1, true) ? fixedNumber(param.data, 1, true) : ''
                        }

                        option.series[0].name = '告警量'
                        option.legend.show = false
                        // option.yAxis[0].name = '告警量（个）';
                        option.title.show = true
                        option.title.subtext = '告警量（个）'

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
            this.$utils.creatInterval(this, this.getDatas, 300000)
        },
        activated() {
            this.$utils.creatInterval(this, this.getDatas, 300000)
        }

    }
</script>

<style lang="scss" scoped>
</style>

