<template>
    <!-- 资源：资源池网段-业务分布 -->
    <div class="content-chart"
         style="width:100%">
        <section class="chart-title-wrap clearfix">
            <svg class="svg-icon svg-icon-24"
                 aria-hidden="true">
                <use xlink:href="#iconziyuan"></use>
            </svg>
            <span class="chart-title">{{chartData.name}}</span>
        </section>

        <!-- 多图表 -->
        <section class="chart-section">
            <div class="chart-box-wrap"
                 style="width:24.2%;float:left;position: relative;z-index:100">
                <div class="chart-filter"
                     style="position: absolute;right:10px;z-index:101">
                    <el-radio-group class="yw-radio-button-wrap chart-filter-item"
                                    v-model="chartData.activeFilter"
                                    @change="changeTab">
                        <el-radio-button :label="tabItem.label"
                                         v-for="(tabItem,tabIndex) in chartData.filter"
                                         :key="tabIndex">{{tabItem.name}}</el-radio-button>
                    </el-radio-group>
                </div>
                <div class="chart-box-item"
                     v-for="(subItem,subIndex) in chartData.chartList"
                     :key="subIndex"
                     :id="subItem.id"></div>
            </div>
            <div class="chart-box-wrap"
                 style="position: relative;width:calc(75.8% - 1px);float:right;border-left: 1px solid #0B4D77;">
                <div class="chart-box-item"
                     style="margin-left:1%"
                     v-for="(subItem,subIndex) in chartData1.chartList"
                     :key="subIndex"
                     :id="subItem.id"></div>
            </div>
        </section>
        <!-- 多图表 -->

    </div>
</template>

<script>

    import ChartOption from 'src/utils/chartOption'
    import DrawChart from 'src/utils/drawCharts.js'
    import Bus from '../utils/bus.js'
    import { fixedNumber } from 'src/assets/js/utility/rb-filters.js'

    import iframe from 'src/services/iframe/iframe.js'

    export default {
        mixins: [DrawChart],
        components: {

        },
        data() {
            return {
                user: true,
                userFlase: false,
                paramsName: '',
                pieList: [],
                IdcType: '',
                QueryType: '',
                Number1List: [],
                Department1List: [],
                newArr: [],
                point: 0,

                chartData: {
                    name: '资源池网段-业务分布',
                    filter: [{ name: '全部设备', label: '0' }, { name: '服务器', label: '1' }],
                    activeFilter: '0',
                    chartList: [{ id: 'resource-chart-user-pie', chartOption: 'pie-option', chartDatas: '' }]
                },
                chartData1: {
                    name: '',
                    chartList: [{ id: 'resource-chart-user-bar', chartOption: 'bar-option', chartDatas: '' }]
                }
            }
        },

        methods: {
            changeTab(val) {
                this.pieList = []
                if (val === '1') {
                    this.IdcType = ''
                    this.getCountBizByIdc('服务器')
                    this.getCountBizByIdcDep1('服务器')
                    this.QueryType = '服务器'
                } else {
                    this.IdcType = ''
                    this.getCountBizByIdc('')
                    this.getCountBizByIdcDep1('')
                    this.QueryType = ''
                }
            },
            // 获得数据
            getCountBizByIdc(data) {
                let _this = this
                let parms = {
                    queryType: data
                }
                iframe.getCountBizByIdc(parms).then((res) => {
                    _this.chartData.chartList[0].chartDatas = res
                    for (let item in res) {
                        _this.pieList.push({
                            department1: res[item].department1,
                            department2: res[item].department2,
                            value: res[item].bizSystem,
                            name: res[item].idcType ? res[item].idcType : '未知'
                        })
                    }

                    _this.newArr = _this.pieList.map(obj => { return obj.value })

                    var arr = _this.newArr
                    function array2() {
                        var temp
                        for (var i = 0; i < arr.length - 1; i++) {
                            for (var j = i + 1; j < arr.length; j++) {
                                if (arr[j] > arr[i]) {
                                    temp = arr[i]
                                    arr[i] = arr[j]
                                    arr[j] = temp
                                }
                            }
                        }
                        _this.point = arr[2]
                    }
                    array2()
                    _this.drawCharts()
                })
            },
            getCountBizByIdcDep1(data) {
                let _this = this

                let parms = {
                    idcType: _this.IdcType,
                    queryType: data
                }
                iframe.getCountBizByIdcDep1(parms).then((res) => {
                    _this.Number1List = []
                    _this.Department1List = []
                    for (let i = 0; i < res.length; i++) {
                        _this.Number1List.push(res[i].number)
                        _this.Department1List.push(res[i].department1 ? res[i].department1 : '未知')
                    }
                    _this.drawCharts1()
                })
            },
            // 绘制饼图表
            drawCharts() {
                let _this = this
                _this.$nextTick(() => {
                    // _this.chartData.chartList.forEach((subItem, subIndex) => {
                    let subItem = this.chartData.chartList[0]
                    let myChart = _this.initMyChart(subItem.id)
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
                    option.series[1].label.fontSize = 10

                    if (chartDatas && chartDatas.length > 0) {
                        option.series[0].data = chartDatas.map((item, index) => {
                            let colorIndex = (index) % (Object.values(colors).length)
                            return {
                                name: item.idcType ? item.idcType : '未知',
                                value: item.bizSystem,
                                department1: item.department1,
                                department2: item.department2,
                                bizSys: item.bizSystem,
                                // selected: false,
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
                                name: item.idcType ? item.idcType : '未知',
                                value: item.bizSystem,
                                department1: item.department1,
                                department2: item.department2,
                                bizSys: item.bizSystem,
                                // selected: index === 0,
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
                    myChart.setOption(option, true)
                    // resize自适应
                    _this.setResizeFun(myChart)

                    myChart.off('click')
                    myChart.on('click', function (params) {
                        _this.selectedPie(myChart, option.series[0].data, params.dataIndex)
                        _this.IdcType = params.name
                        _this.paramsName = params.name
                        if (_this.QueryType === '服务器') {
                            _this.getCountBizByIdcDep1('服务器')
                        } else {
                            _this.getCountBizByIdcDep1('')
                        }

                        Bus.$emit('UserFlase', _this.userFlase)
                    })
                    // })
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
            // 绘制柱图表
            drawCharts1() {
                let _this = this
                _this.$nextTick(() => {
                    _this.chartData1.chartList.forEach((subItem) => {
                        let myChart = _this.initMyChart(subItem.id)
                        myChart.clear()
                        // 数据格式处理

                        let option = JSON.parse(JSON.stringify(ChartOption[subItem.chartOption]))
                        // let chartDatas = JSON.parse(JSON.stringify(subItem.chartDatas))

                        let colors = ChartOption['color-option']['linearColor']

                        option.series[0] = JSON.parse(JSON.stringify(option.series[0]))

                        option.legend.data = []
                        option.series[0].itemStyle.color.colorStops = colors.blueLight

                        if (_this.Department1List && _this.Department1List.length > 0) {
                            option.xAxis.data = _this.Department1List
                            option.series[0].data = _this.Number1List
                        } else {
                            option.series[0].data = [0]
                            option.xAxis.data = ['暂无']
                        }
                        option.title.show = true
                        option.title.text = _this.IdcType + '一级租户业务系统分布'
                        option.title.subtext = '业务系统数 (个)'
                        option.grid.top = 50

                        option.series[0].label.show = true
                        option.series[0].label.fontSize = '10'
                        option.series[0].label.formatter = (param) => {
                            if (param.data > 1000 || param.data === 1000) {
                                return fixedNumber(param.data, 1)
                            } if (param.data < 1000 && param.data > 0) {
                                return param.data
                            } else {
                                return ''
                            }
                        }
                        option.tooltip.formatter = '{b} <br/>业务系统数: {c}'
                        myChart.setOption(option, true)
                        // resize自适应
                        _this.setResizeFun(myChart)

                        myChart.off('click')
                        myChart.on('click', function (params) {
                            Bus.$emit('User', _this.user, params.name, _this.paramsName, _this.QueryType)
                        })
                    })
                })
            }

        },

        mounted() {
            this.getCountBizByIdc('')
            this.getCountBizByIdcDep1('')
        },
        beforeDestroy() {

        }
    }
</script>

<style lang="scss" scoped>
</style>

