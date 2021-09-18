<template>

    <div class="content-chart"
         style="width:49.4%;">
        <section class="chart-title-wrap clearfix">
            <svg class="svg-icon svg-icon-24"
                 aria-hidden="true">
                <use xlink:href="#iconziyuan"></use>
            </svg>
            <span class="chart-title">{{chartData.name}}</span>
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
                    name: '各资源池租户分布',
                    filter: [{ name: '物理机', label: 'X86服务器' }, { name: '虚拟机', label: '云主机' }],
                    activeFilter: 'X86服务器',
                    idcType: '',
                    idcTypeId: '',
                    chartList: [{ id: 'resource-pool-user-1', chartOption: 'pie-option', chartDatas: '' }, { id: 'resource-pool-user-2', chartOption: 'bar-option', chartDatas: '' }]
                },
                listdata: []
            }
        },
        methods: {
            // 获得数据
            getDatas() {
                this.getUserDatas()
            },

            // 获得租户分布数据
            getUserDatas() {
                let params = {
                    'queryType': ''
                }
                this.$api.queryPoolUser(params).then((res) => {
                    console.log(res)
                    this.listdata = res
                    this.chartData.chartList[0].chartDatas = res
                    this.drawPie()
                    this.chartData.idcType = res[0].idcType
                    this.chartData.idcTypeId = res[0].idcTyp_id
                    this.getBizDatas()
                })
            },

            // 获得业务系统数据
            getBizDatas() {
                console.log(this.chartData)
                let listdata = this.listdata
                let str = ''
                for (let i = 0; i < listdata.length; i++) {
                    if (listdata[i].idcType == this.chartData.idcType) {
                        str = listdata[i].idcTyp_id
                    }
                }
                let params = {
                    'idcType': str,
                    'queryType': ''
                }
                this.$api.queryPoolBiz(params).then((res) => {
                    console.log(res)
                    for (let i = 0; i < res.length; i++) {
                        if (res[i].department1 == null || res[i].department1 == 'null') {
                            res[i].department1 = '未知'
                        }
                    }
                    this.chartData.chartList[1].chartDatas = res
                    this.drawBar()
                })
            },
            drawPie() {
                this.$nextTick(() => {
                    let subItem = this.chartData.chartList[0]
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
                    let colorSource = ChartOption['color-option']['linearColor']
                    let colors = [colorSource.blue, colorSource.blueLight, colorSource.green, colorSource.greenLight, colorSource.yellow, colorSource.purple]

                    option.series[0] = JSON.parse(JSON.stringify(option.series[0]))
                    option.series[1] = JSON.parse(JSON.stringify(option.series[0]))
                    option.series[1].name = option.series[0].name = '各资源池租户分布'
                    option.series[1].label.position = 'inside'
                    option.series[1].radius = option.series[0].radius = ['0%', '40%']
                    // option.series[1].itemStyle.color = option.series[0].itemStyle.color = function (item) {
                    //   let linearSetting = {
                    //     type: 'linear',
                    //     x: 0,
                    //     y: 0, // 由上至下
                    //     x2: 0,
                    //     y2: 1,
                    //     colorStops: Object.values(colors)[item.dataIndex]
                    //   }
                    //   return linearSetting
                    // }
                    option.series[1].label.fontSize = 10

                    if (chartDatas && chartDatas.length > 0) {
                        option.series[0].data = chartDatas.map((item, index) => {
                            let colorIndex = (index) % (Object.values(colors).length)
                            return {
                                name: item.idcType,
                                value: item.bizSystem,
                                department1: item.department1,
                                department2: item.department2,
                                bizSys: item.bizSystem,
                                selected: index === 0,
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

                        option.series[1].data = chartDatas.map((item, index) => {
                            let colorIndex = (index) % (Object.values(colors).length)
                            return {
                                name: item.idcType,
                                value: item.bizSystem,
                                department1: item.department1,
                                department2: item.department2,
                                bizSys: item.bizSystem,
                                selected: index === 0,
                                label: {
                                    color: '#fff',
                                    textShadowColor: '#000',
                                    textShadowBlur: 2,
                                    textShadowOffsetX: 2,
                                    textShadowOffsetY: 2
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
                    option.series[1].label.formatter = function (param) {
                        if (param.dataIndex > 1) {
                            return ''
                        }

                        if (param.data.department1) {
                            let text = '{row1|一级部门' + param.data.department1 + '\n}{row2|二级部门' +
                                param.data.department2 + '\n}{row3|业务系统' +
                                param.data.bizSys + '}'
                            return text
                        }
                    }
                    option.tooltip.formatter = (param) => {
                        let text = param.name + '<br/>' + '一级部门:' + param.data.department1 + '<br/>二级部门:' +
                            param.data.department2 + '<br/>业务系统:' +
                            param.data.bizSys + ''
                        return text
                    }
                    myChart.setOption(option)

                    // resize自适应
                    this.setResizeFun(myChart)

                    // 点击事件
                    myChart.on('click', (params) => {
                        this.changeTab(params)
                        this.selectedPie(myChart, option.series[0].data, params.dataIndex)
                    })
                })
            },
            selectedPie(myChart, data, dataIndex) {
                // 刷新数据
                let option = myChart.getOption()
                let datas = option.series[0].data
                let datas2 = option.series[1].data

                datas.forEach((item, index) => {
                    item.selected = false
                    if (index === dataIndex) {
                        item.selected = true
                    }
                })
                datas2.forEach((item, index) => {
                    item.selected = false
                    if (index === dataIndex) {
                        item.selected = true
                    }
                })
                option.series[0].data = datas
                option.series[1].data = datas2
                myChart.setOption(option)
            },
            drawBar() {
                this.$nextTick(() => {
                    let subItem = this.chartData.chartList[1]
                    // 非空判断，避免报错
                    let myChartDom = document.getElementById(subItem.id)
                    if (!myChartDom) {
                        return
                    }
                    let myChart = echarts.init(myChartDom)
                    myChart.clear()

                    let option = JSON.parse(JSON.stringify(ChartOption[subItem.chartOption]))
                    let chartDatas = JSON.parse(JSON.stringify(subItem.chartDatas))
                    let colors = ChartOption['color-option']['linearColor']
                    option.dataZoom[0].show = true

                    option.series[0] = JSON.parse(JSON.stringify(option.series[0]))
                    option.series[0].name = ['业务系统数']
                    option.legend.data = []
                    option.series[0].itemStyle.color.colorStops = colors.blueSingle
                    // option.xAxis.axisLabel.rotate = 20;
                    option.title.show = true
                    option.title.text = `${this.chartData.idcType}一级部门分布`
                    option.title.subtext = '业务系统数（个）'
                    option.title.padding = [18, 0, 0, 0]
                    option.grid.top = 80
                    option.series[0].label.show = true
                    option.series[0].label.formatter = (param) => {
                        return fixedNumber(param.data, 1, true)
                    }

                    if (chartDatas && chartDatas.length > 0) {
                        option.series[0].data = chartDatas.map((item) => {
                            return item.number
                        })

                        option.xAxis.data = chartDatas.map((item) => {
                            return item.department1
                        })
                    } else {
                        option.series[0].data = [0]
                        option.xAxis.data = ['暂无']
                    }

                    myChart.setOption(option)

                    // resize自适应
                    this.setResizeFun(myChart)
                })
            },
            changeTab(val) {
                console.log(val)
                this.chartData.idcType = val.data.name
                this.getBizDatas()
            }
        },
        mounted() {
            this.getDatas()
        }

    }
</script>

<style lang="scss" scoped>
</style>

