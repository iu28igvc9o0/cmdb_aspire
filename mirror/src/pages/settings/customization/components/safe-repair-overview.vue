<template>
    <div class="content-chart"
         style="width: 24.2%;">
        <section class="chart-title-wrap clearfix">
            <svg class="svg-icon svg-icon-24"
                 aria-hidden="true">
                <use xlink:href="#iconanquanxiufuzonglan"></use>
            </svg>
            <span class="chart-title">{{chartData.name}}</span>
        </section>

        <!-- 多图表 -->
        <section class="chart-section">
            <div class="chart-box-wrap">
                <div class="chart-box-item"
                     style="width:50%"
                     v-for="(subItem,subIndex) in chartData.chartList"
                     :key="subIndex"
                     :id="subItem.id"></div>
                <div class="chart-box-item"
                     style="width:50%">
                    <div class="legend-box">
                        <div class="legend-wrap"
                             v-for="(item,index) in chartData.tableDatas"
                             :key="index">
                            <p class="legend-name">{{item.name}}</p>
                            <p class="legend-value">{{item.value}}</p>
                        </div>
                    </div>
                </div>
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
        components: {

        },
        data() {
            return {
                chartData: {
                    name: '安全修复总览',
                    tableDatas: [],
                    chartList: [
                        { id: 'order-chart-overview-1', chartOption: 'pie-option', chartDatas: '' }]
                }
            }
        },
        methods: {
            // 获得数据
            getDatas() {
                let datas = {
                    title: '已修复',
                    rate: '0%',
                    list: [
                        {
                            name: '漏洞总数',
                            value: 0,
                        },
                        {
                            name: '已修复',
                            value: 0,
                        },
                        {
                            name: '未修复',
                            value: 0,
                        },

                    ]
                }

                let params = {
                    beginDate: this.conditionParams.dateRange[0],
                    endDate: this.conditionParams.dateRange[1],
                }

                this.$api.querySafeScanOverview(params).then((res) => {
                    let total = res.leakCount || 0
                    let repairNum = res.repairLeakCount || 0
                    let rate = 0

                    datas.list[0].value = total
                    datas.list[1].value = repairNum
                    datas.list[2].value = total - repairNum
                    if (total) {
                        rate = fixedNumber(repairNum / total * 100, 0)
                    }
                    datas.rate = rate + '%'

                    this.chartData.tableDatas = datas.list.slice(0, 2)
                    this.chartData.chartList[0].chartDatas = datas
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
                        this.drawPie(myChart, subItem)
                    })
                })
            },
            drawPie(myChart, subItem) {
                // 数据格式处理
                let option = JSON.parse(JSON.stringify(ChartOption[subItem.chartOption]))
                let chartDatas = JSON.parse(JSON.stringify(subItem.chartDatas))
                // let colorSource = ChartOption['color-option']['linearColor']

                let selfBlue = [[{ offset: 0, color: '#29d8f8' }, { offset: 1, color: '#05a7ea' }
                ], [{ offset: 0, color: '#1b2054' }, { offset: 1, color: '#1b2054' }
                ]]

                option.series[0] = JSON.parse(JSON.stringify(option.series[0]))
                option.series[0].name = ['安全修复总览']
                option.series[0].radius = ['60%', '85%']
                option.series[0].label.show = false

                option.title.show = true
                option.title.text = chartDatas.title
                option.title.subtext = chartDatas.rate
                option.title.x = '48%'
                option.title.y = '42%'
                option.title.textStyle.fontSize = 16
                option.title.textStyle.color = '#CAF4FF'
                option.title.subtextStyle.fontSize = 20
                option.title.subtextStyle.color = '#31DBF2'

                option.series[0].itemStyle.color = function (item) {

                    let linearSetting = {
                        type: 'linear',
                        x: 0,
                        y: 0, // 由上至下
                        x2: 0,
                        y2: 1,
                        colorStops: selfBlue[item.dataIndex]
                    }
                    return linearSetting
                }

                if (chartDatas.list && chartDatas.list.length > 0) {
                    let filterData = chartDatas.list.slice(1)
                    option.series[0].data = filterData.map((item) => {
                        return {
                            name: item.name,
                            value: item.value,
                            itemStyle: {
                                shadowColor: '#05A7EA',
                                shadowBlur: 10,
                                shadowOffsetX: 1,
                                shadowOffsetY: 0
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
            }
        },
        mounted() {
            this.getDatas()
        }

    }
</script>

<style lang="scss" scoped>
    /deep/.chart-section {
        .chart-box-wrap {
            .chart-box-item {
                & + .chart-box-item {
                    display: flex;
                    align-items: center;
                    &:before {
                        background: transparent;
                    }
                }
            }
        }
        .legend-box {
            width: 90%;
            height: 145px;
            .legend-wrap {
                // font-size: $font-16;
                font-size: 16px;
                color: $color-base;
                padding: 10px 0 10px 20px;
                .legend-value {
                    // font-size: $font-24;
                    font-size: 24px;
                }
                &:nth-of-type(1) {
                    border-bottom: 1px solid rgba(11, 77, 119, 1);
                    .legend-value {
                        color: #ff7113;
                    }
                }
                &:nth-of-type(2) {
                    .legend-value {
                        color: #33e5fc;
                    }
                }
            }
        }
    }
</style>

