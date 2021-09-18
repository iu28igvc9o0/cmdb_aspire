<template>
    <!-- 资源：各资源池租户分布 -->
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
                idcTypeId: '',
                QueryType: '',
                Number1List: [],
                Department1List: [],
                department1IdList: [],
                newArr: [],
                point: 0,

                chartData: (window.projectName == '集中网管' || window.projectName == '客响-集中运维平台') ? {
                    name: '各资源池业务线分布',
                    filter: [{ name: '全部设备', label: '0' }, { name: '服务器', label: '1' }],
                    activeFilter: '0',
                    chartList: [{ id: 'resource-chart-user-pie', chartOption: 'pie-option', chartDatas: '' }]
                } : {
                        name: '各资源池租户分布',
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
                    this.idcTypeId = ''
                    this.IdcType = ''
                    this.getCountBizByIdc('9561')
                    this.getCountBizByIdcDep1('9561')
                    // 服务器
                    this.QueryType = '9561'
                } else {
                    this.idcTypeId = ''
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
                if (window.projectName == '集中网管' || window.projectName == '客响-集中运维平台') {
                    let queryData = {
                        queryName: 'ICountInstByCdtAPI_internet_countBusinessAssetByIdcPieChart',
                        deviceClass: data ? '服务器' : '',
                        queryType: 'code'
                    }
                    iframe.businessCountList(queryData).then((res) => {
                        _this.chartData.chartList[0].chartDatas = res
                        for (let item in res) {
                            _this.pieList.push({
                                businessCount: res[item].businessCount,
                                businessName: res[item].businessName,
                                name: res[item].idcType ? res[item].idcType : '未知'
                            })
                        }
                        // _this.newArr = _this.pieList.map(obj => { return obj.value })

                        // var arr = _this.newArr
                        // function array2() {
                        //     var temp
                        //     for (var i = 0; i < arr.length - 1; i++) {
                        //         for (var j = i + 1; j < arr.length; j++) {
                        //             if (arr[j] > arr[i]) {
                        //                 temp = arr[i]
                        //                 arr[i] = arr[j]
                        //                 arr[j] = temp
                        //             }
                        //         }
                        //     }
                        //     _this.point = arr[2]
                        // }
                        // array2()
                        _this.drawCharts()
                    })
                } else {
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
                }

            },
            getCountBizByIdcDep1(data) {
                let _this = this

                let parms = {
                    idcType: _this.idcTypeId,
                    queryType: data
                }
                if (window.projectName == '集中网管' || window.projectName == '客响-集中运维平台') {
                    let queryData = {
                        queryName: 'ICountInstByCdtAPI_internet_countBusinessAssetByIdcBarChart',
                        idcType: _this.IdcType,
                        deviceClass: data ? '服务器' : ''
                    }
                    iframe.businessCountList(queryData).then((res) => {
                        _this.Number1List = []
                        _this.Department1List = []
                        for (let i = 0; i < res.length; i++) {
                            _this.Number1List.push(res[i].businessCount)
                            _this.Department1List.push(res[i].businessName1 ? res[i].businessName1 : '未知')
                        }
                        _this.drawCharts1()
                    })
                } else {
                    iframe.getCountBizByIdcDep1(parms).then((res) => {
                        _this.Number1List = []
                        _this.Department1List = []
                        for (let i = 0; i < res.length; i++) {
                            _this.Number1List.push(res[i].number)
                            _this.Department1List.push(res[i].department1 ? res[i].department1 : '未知')
                            _this.department1IdList.push(res[i].department1_id ? res[i].department1_id : '')
                        }
                        _this.drawCharts1()
                    })
                }

            },
            // 绘制饼图表
            drawCharts() {
                let _this = this
                _this.$nextTick(() => {
                    // _this.chartData.chartList.forEach((subItem, subIndex) => {
                    let subItem = this.chartData.chartList[0]
                    let myChart = echarts.init(document.getElementById(subItem.id))
                    myChart.clear()
                    // 数据格式处理
                    let option = JSON.parse(JSON.stringify(ChartOption[subItem.chartOption]))
                    let chartDatas = JSON.parse(JSON.stringify(subItem.chartDatas))
                    let colorSource = ChartOption['color-option']['linearColor']
                    let colors = [colorSource.blue, colorSource.blueLight, colorSource.green, colorSource.greenLight, colorSource.yellow, colorSource.purple]

                    if (window.projectName == '集中网管' || window.projectName == '客响-集中运维平台') {
                        option.series[0] = JSON.parse(JSON.stringify(option.series[0]))
                        option.series[0].radius = ['0%', '40%']
                        option.series[0].label.fontSize = 10
                        if (chartDatas && chartDatas.length > 0) {
                            option.series[0].data = chartDatas.map((item, index) => {
                                let colorIndex = (index) % (Object.values(colors).length)
                                return {
                                    name: item.idcType ? item.idcType : '未知',
                                    businessCount: item.businessCount,
                                    businessName: item.businessName,
                                    value: item.total,
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

                            // option.series[1].data = chartDatas.map((item, index) => {
                            //     let colorIndex = (index) % (Object.values(colors).length)
                            //     return {
                            //         name: item.idcType ? item.idcType : '未知',
                            //         businessCount: item.businessCount,
                            //         businessName: item.businessName,
                            //         value: index + 1,
                            //         // selected: index === 0,
                            //         label: {
                            //             color: '#fff',
                            //             textShadowColor: '#000',
                            //             textShadowBlur: 2,
                            //             textShadowOffsetX: 2,
                            //             textShadowOffsetY: 2,
                            //             show: false
                            //         },
                            //         itemStyle: {
                            //             color: {
                            //                 type: 'linear',
                            //                 x: 0,
                            //                 y: 0, // 由上至下
                            //                 x2: 0,
                            //                 y2: 1,
                            //                 colorStops: (Object.values(colors)[colorIndex])
                            //             }
                            //         }
                            //     }
                            // })
                        } else {
                            option.series[0].data = [{
                                name: '暂无数据',
                                value: 0
                            }]
                        }
                        // option.series[1].label.formatter = function (param) {
                        //     if (param.dataIndex > 1) {
                        //         return ''
                        //     }

                        //     if (param.data.department1) {
                        //         let text = '{row1|一级部门' + param.data.department1 + '\n}{row2|二级部门' +
                        //             param.data.department2 + '\n}{row3|业务系统' +
                        //             param.data.bizSys + '}'
                        //         return text
                        //     }
                        // }
                        option.tooltip.formatter = (param) => {
                            let text = param.name + '<br/>'
                            text += '总数:' + param.value + '<br/>'
                            for (let i = 0; i < param.data.businessName.length; i++) {
                                text += param.data.businessName[i] + ':' +
                                    param.data.businessCount[i] + '<br/>'
                            }
                            return text
                        }

                    } else {
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
                                    idcTypeId: item.idcTyp_id,
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
                    }

                    myChart.setOption(option, true)
                    // resize自适应
                    _this.setResizeFun(myChart)

                    myChart.off('click')
                    myChart.on('click', function (params) {
                        _this.selectedPie(myChart, option.series[0].data, params.dataIndex)
                        console.info('ceshiDemo:')
                        console.info(params)
                        _this.IdcType = params.name
                        _this.paramsName = params.name
                        _this.idcTypeId = params.data.idcTypeId
                        if (_this.QueryType === '9561') {
                            if (window.projectName == '集中网管' || window.projectName == '客响-集中运维平台') {
                                _this.getCountBizByIdcDep1('服务器')
                            } else {
                                _this.getCountBizByIdcDep1('9561')
                            }

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
                datas.forEach((item, index) => {
                    item.selected = false
                    if (index === dataIndex) {
                        item.selected = true
                    }
                })
                if (window.projectName != '集中网管' && window.projectName != '客响-集中运维平台') {
                    let datas2 = option.series[1].data
                    datas2.forEach((item, index) => {
                        item.selected = false
                        if (index === dataIndex) {
                            item.selected = true
                        }
                    })
                    option.series[1].data = datas2
                }
                option.series[0].data = datas
                myChart.setOption(option)
            },
            // 绘制柱图表
            drawCharts1() {
                let _this = this
                _this.$nextTick(() => {
                    _this.chartData1.chartList.forEach((subItem) => {
                        let myChart = echarts.init(document.getElementById(subItem.id))
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
                        if (window.projectName == '集中网管' || window.projectName == '客响-集中运维平台') {
                            option.title.text = _this.IdcType + '独立业务线分布'
                            option.title.subtext = '独立业务线设备数量 (个)'
                        } else {
                            option.title.text = _this.IdcType + '一级租户业务系统分布'
                            option.title.subtext = '业务系统数 (个)'
                        }
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
                            console.info('业务系统Top10:')
                            console.info(params)
                            if (window.projectName == '集中网管' || window.projectName == '客响-集中运维平台') {
                                Bus.$emit('User', _this.user, params.name, _this.paramsName, _this.QueryType ? '服务器' : '', _this.department1IdList[params.dataIndex], _this.idcTypeId)
                            } else {
                                Bus.$emit('User', _this.user, params.name, _this.paramsName, _this.QueryType, _this.department1IdList[params.dataIndex], _this.idcTypeId)
                            }

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

