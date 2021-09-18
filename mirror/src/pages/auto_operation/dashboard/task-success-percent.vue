<template>
    <div class="content-chart"
         style="width: 24.2%;">
        <section class="chart-title-wrap clearfix">
            <svg class="svg-icon svg-icon-24"
                 aria-hidden="true">
                <use xlink:href="#iconzhihangchenggongshuai"></use>
            </svg>
            <span class="chart-title">{{chartData.name}}</span>
        </section>

        <!-- 多图表 -->
        <section class="chart-section">
            <div class="chart-box-wrap flex-wrap">
                <div class="chart-box-item"
                     style="width:50%; height: 50%"
                     v-for="(subItem,subIndex) in chartData.chartList"
                     :key="subIndex"
                     :id="subItem.id"></div>
            </div>
        </section>
        <!-- 多图表 -->
    </div>
</template>

<script>
    import rbAutoOperationHomeServicesFactory from 'src/services/auto_operation/rb-auto-operation-home-services.factory.js'
    import ChartOption from 'src/utils/chartOption.js'
    import DrawChart from 'src/utils/drawCharts.js'

    export default {
        mixins: [DrawChart],
        data() {
            return {
                chartData: {
                    name: '7天任务执行成功率',
                    chartList: [
                        { id: 'alert-device-class-1', chartOption: 'pie-option', chartDatas: '' },
                        { id: 'alert-device-class-2', chartOption: 'pie-option', chartDatas: '' },
                        { id: 'alert-device-class-3', chartOption: 'pie-option', chartDatas: '' },
                        { id: 'alert-device-class-4', chartOption: 'pie-option', chartDatas: '' },
                    ]
                },
                chartDatas: [
                    [
                        {
                            name: '手工任务',
                            value: '',
                            key: 'handle_success_rate'
                        },
                    ],
                    [
                        {
                            name: '自动任务',
                            value: '',
                            key: 'auto_success_rate'
                        },
                    ],
                    [
                        {
                            name: '巡检任务',
                            value: '',
                            key: 'inspection_success_rate'
                        },
                    ],
                    [
                        {
                            name: '故障自愈任务',
                            value: '',
                            key: 'self_repair_success_rate'
                        },
                    ],
                ]
            }
        },
        created() {
            this.queryChartData()
        },
        methods: {
            // 七日任务执行成功率
            queryChartData() {
                rbAutoOperationHomeServicesFactory
                    .queryTaskExec7DaySuccessedRate()
                    .then(res => {
                        this.chartDatas.forEach(item => {
                            let obj = JSON.parse(JSON.stringify(item[0]))
                            let curPercentNum = this.handleNum(res[obj.key])
                            item[0].value = curPercentNum   // 需要显示的数据百分比
                            obj.value = 100 - curPercentNum // 不需显示的百分比
                            item.push(obj)
                        })
                        this.drawCharts()
                    })
            },
            handleNum(n) {
                return (n * 100).toFixed(0)
            },

            // 绘制图表
            drawCharts() {
                this.$nextTick(() => {
                    this.chartData.chartList.forEach((subItem, subIndex) => {
                        let myChart = this.initMyChart(subItem.id)
                        myChart.clear()
                        // 数据格式处理
                        let option = JSON.parse(JSON.stringify(ChartOption[subItem.chartOption]))
                        let chartDatas = this.chartDatas
                        let colors = ChartOption['color-option']['linearColor']

                        // 设置圆环标题
                        option.title = {
                            text: chartDatas[subIndex][0].name,
                            x: 'left',
                            y: 'center',
                            textStyle: {
                                fontSize: 12,
                                fontWeight: 'normal',
                                color: ['#fff']
                            },
                            top: '40%',   // 标题文字距离顶部距离
                            left: '50%'
                        }

                        option.series[0] = JSON.parse(JSON.stringify(option.series[0]))

                        // 隐藏牵引线
                        option.series[0].labelLine = {
                            show: false
                        }
                        option.series[0].radius = ['50%', '70%']    // 圆环大小
                        option.series[0].center = ['30%', '50%']    // 圆环位置

                        chartDatas.forEach((item) => {
                            // let colorIndex = (index) % (Object.values(colors).length)
                            item.forEach((singleData, singleIndex) => {
                                singleData.label = {
                                    position: 'center',
                                    color: '#fff',
                                    formatter: '{d}%',
                                }
                                singleData.itemStyle = {
                                    color: {
                                        type: 'linear',
                                        x: 0,
                                        y: 0, // 由上至下
                                        x2: 0,
                                        y2: 1,
                                        colorStops: (Object.values(colors)[1])
                                    }
                                }
                                singleData.tooltip = {
                                    show: false
                                }
                                if (singleIndex === 1) {
                                    singleData.label = {
                                        show: false
                                    }
                                    singleData.itemStyle = {
                                        color: '#1B2054'
                                    }
                                    // singleData.label.emphasis = {
                                    //     show: false
                                    // }
                                }
                            })
                        })
                        option.series[0].data = chartDatas[subIndex]

                        myChart.setOption(option)

                        // resize自适应
                        this.setResizeFun(myChart)
                    })

                })
            },
        },

    }
</script>

<style lang="scss" scoped>
</style>

