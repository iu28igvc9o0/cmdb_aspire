<template>
    <!-- 定制化组件：工单总览 -->
    <div class="content-chart">
        <section class="chart-title-wrap clearfix">
            <svg class="svg-icon svg-icon-24"
                 aria-hidden="true">
                <use xlink:href="#icongongdan"></use>
            </svg>
            <span class="chart-title">{{chartData.name}}</span>
        </section>

        <!-- 多图表 -->
        <section class="chart-section">
            <div class="chart-box-wrap direction-column">
                <div class="chart-box-item"
                     style="width:100%;height:40%">
                    <YwOrder :datas="chartData.orderList"></YwOrder>
                </div>
                <div class="chart-box-item"
                     style="width:100%;height:60%"
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
        components: {
            YwOrder: () => import('./sub_components/yw-order.vue')
        },
        data() {
            return {
                chartData: {
                    name: '工单总览',
                    filter: [{ name: '物理机', label: 'X86服务器' }, { name: '虚拟机', label: '云主机' }],
                    activeFilter: 'X86服务器',
                    orderList: [{
                        name: '我的请求',
                        icon: require('src/assets/theme/dark/img/order-require.png'),
                        num: 0,
                        date: 0,
                        timeDesc: '逾期'
                    }, {
                        name: '我的待处理',
                        icon: require('src/assets/theme/dark/img/order-wait.png'),
                        num: 0,
                        date: 0,
                        timeDesc: '逾期'
                    }],
                    chartList: [
                        { id: 'order-chart-overview-1', chartOption: 'pie-option', chartDatas: '' }]
                }
            }
        },
        methods: {
            // 获得数据
            getDatas() {
                let params = {

                }
                this.$api.queryOrderOverview(params).then((res) => {
                    // 对象
                    this.chartData.orderList[0].num = res.myRequest.all
                    this.chartData.orderList[0].date = res.myRequest.overdue
                    this.chartData.orderList[1].num = res.todo.all
                    this.chartData.orderList[1].date = res.todo.overdue

                    this.chartData.chartList[0].chartDatas = {
                        title: '工单总数',
                        total: res.total.all,
                        list: [
                            {
                                name: '已完成',
                                value: res.finish.all,
                                num: res.finish.all,
                                date: res.finish.overdue
                            },
                            {
                                name: '处理中',
                                value: res.run.all,
                                num: res.run.all,
                                date: res.run.overdue
                            }
                        ]
                    }
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
                        this.drawPie(myChart, subItem)
                    })
                })
            },
            drawPie(myChart, subItem) {
                // 数据格式处理
                let option = this.$utils.deepClone(ChartOption[subItem.chartOption])
                let chartDatas = this.$utils.deepClone(subItem.chartDatas)
                let colorSource = ChartOption['color-option']['linearColor']
                let colors = [colorSource.blueLight, colorSource.blue]
                let selfBlue = [colorSource.blueLight, [{ offset: 0, color: '#09184B' }, { offset: 1, color: '#091344' } // 0% 处的颜色


                ]]

                option.series[0] = this.$utils.deepClone(option.series[0])
                option.series[0].name = ['工单总数']
                option.series[0].radius = ['50%', '70%']
                option.series[0].label.show = false
                option.series[0].center = ['18%', '50%']
                option.series[0].selectedOffset = 3

                option.series[1] = this.$utils.deepClone(option.series[0])
                option.series[0].name = ['工单总数']
                option.series[1].radius = ['78%', '80%']
                option.series[1].label.show = false
                option.series[1].center = ['18%', '50%']
                option.series[1].selectedOffset = 3

                option.title.show = true
                option.title.text = chartDatas.title
                option.title.subtext = chartDatas.total
                option.title.x = '16%'
                option.title.y = '32%'
                option.legend.show = true
                option.legend.orient = 'vertical'
                option.legend.top = 'middle'
                option.legend.left = '35%'
                option.legend.formatter = function (param) {

                    let index = 0
                    let datas = chartDatas.list
                    datas.forEach(function (item, i) {
                        if (item.name === param) {
                            index = i
                        }
                    })
                    let text = '{name|' + datas[index].name + '}{num|' + fixedNumber(datas[index].num, 1, true) + '}{name|/逾期}{date|' + fixedNumber(datas[index].date, 1, true) +
                        '}'

                    return text
                }
                option.legend.data = ['已完成', '处理中']
                option.legend.data.textStyle = { fontSize: 8 }

                option.series[0].itemStyle.color = function (item) {
                    let linearSetting = {
                        type: 'linear',
                        x: 0,
                        y: 0, // 由上至下
                        x2: 0,
                        y2: 1,
                        colorStops: Object.values(colors)[item.dataIndex]
                    }
                    return linearSetting
                }

                option.series[1].itemStyle.color = function (item) {
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
                    option.series[0].data = chartDatas.list

                    option.series[1].data = chartDatas.list.map((item) => {
                        return {
                            name: item.name,
                            value: item.value,
                            num: item.num,
                            date: item.date,
                            itemStyle: {
                                shadowColor: '#05A7EA',
                                shadowBlur: 6,
                                shadowOffsetX: 2,
                                shadowOffsetY: 0,
                                fontSize: 10
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
</style>

