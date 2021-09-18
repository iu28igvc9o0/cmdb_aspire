<template>
    <!-- 资源：各资源池租户分布 -->
    <div class="content-chart"
         v-if="userBoolean"
         style="width:100%;">
        <section class="chart-title-wrap clearfix">
            <svg class="svg-icon svg-icon-24"
                 aria-hidden="true">
                <use xlink:href="#iconziyuan"></use>
            </svg>
            <span class="chart-title">{{titleName}}{{chartData.name}}</span>
        </section>

        <!-- 多图表 -->
        <section class="chart-section">
            <div class="chart-box-wrap"
                 style="width:24.2%;float:left">
                <div class="chart-box-item"
                     v-for="(subItem,subIndex) in chartData.chartList"
                     :key="subIndex"
                     :id="subItem.id"></div>
            </div>
            <div class="chart-box-wrap"
                 style="width:calc(75.8% - 1px);float:right;border-left: 1px solid #0B4D77;padding-left:10px;">
                <div class="chart-box-item"
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
                userBoolean: false,
                titleName: '',
                department1Id: '',
                idtypeName: '',
                idcTypeId: '',
                annularName: '',
                bizSystemName: '',
                pieList: [],
                IdcType: '',
                QueryType: '',
                Number1List: [],
                Department1List: [],
                Zu2: [],
                Zu1: [{ name: '暂无', value: '0' }],

                bizSystemList: [],
                numberList: [],

                chartData: (window.projectName == '集中网管' || window.projectName == '客响-集中运维平台') ? {
                    name: '独立业务设备在各资源池分布',
                    chartList: [{ id: 'resource-chart-user-two-pie', chartOption: 'pie-option', chartDatas: '' }]
                } : {
                        name: '二级租户业务系统分布TOP10',
                        chartList: [{ id: 'resource-chart-user-two-pie', chartOption: 'pie-option', chartDatas: '' }]
                    },
                chartData1: {
                    name: '',
                    chartList: [{ id: 'resource-chart-user-two-bar', chartOption: 'bar-option', chartDatas: '' }]
                }
            }
        },

        methods: {
            bbtn: function () {
                Bus.$on('User', (newV1, newv2, newv3, newv4, newv5, newv6) => {   // 这里最好用箭头函数，不然this指向有问题
                    this.userBoolean = newV1
                    this.titleName = newv2 // 一级部门
                    this.idtypeName = newv3 // 资源池
                    this.QueryType = newv4 // 设备类型
                    this.department1Id = newv5
                    this.idcTypeId = newv6
                    this.bizSystemList = []
                    this.numberList = []
                    this.Zu2 = []
                    this.getCountBizByIdcDep2()
                    this.getCountByIdcDep2Biz()
                })
            },
            bbtnFalse: function () {
                Bus.$on('UserFlase', (newV1) => {   // 这里最好用箭头函数，不然this指向有问题
                    this.userBoolean = newV1
                })
            },
            // 柱状图数据
            getCountByIdcDep2Biz() {
                let data = {
                    department2: this.annularName,
                    department1: this.department1Id,
                    idcType: this.idcTypeId,
                    queryType: this.QueryType
                }
                if (window.projectName == '集中网管' || window.projectName == '客响-集中运维平台') {
                    let queryData = {
                        queryName: 'ICountInstByCdtAPI_internet_countAssetByBusinessBarChart',
                        businessName1: this.titleName,
                        deviceClass: this.QueryType
                    }
                    iframe.businessCountList(queryData).then((res) => {
                        this.bizSystemList = []
                        this.numberList = []
                        for (let i = 0; i < res.length; i++) {
                            this.bizSystemList.push(res[i].businessName2)
                            this.numberList.push(res[i].businessCount)
                        }
                        this.drawCharts1()
                    })
                } else {
                    iframe.getCountByIdcDep2Biz(data).then((res) => {
                        this.bizSystemList = []
                        this.numberList = []
                        for (let i = 0; i < res.length; i++) {
                            this.bizSystemList.push(res[i].bizSystem)
                            this.numberList.push(res[i].number)
                        }
                        this.drawCharts1()
                    })
                }

            },
            // 环形图数据
            getCountBizByIdcDep2() {
                let data = {
                    department1: this.department1Id,
                    idcType: this.idcTypeId,
                    queryType: this.QueryType
                }
                if (window.projectName == '集中网管' || window.projectName == '客响-集中运维平台') {
                    let queryData = {
                        queryName: 'ICountInstByCdtAPI_internet_countAssetByBusinessPieChart',
                        businessName1: this.titleName,
                        deviceClass: this.QueryType,
                        queryType: 'code'
                    }
                    iframe.countObject(queryData).then((res) => {
                        let dataList = res.dataList
                        this.Zu2 = []
                        this.Total = ''
                        this.Total = res.total
                        for (let item in dataList) {
                            this.Zu2.push({
                                name: dataList[item].idcType ? dataList[item].idcType : '未知',
                                value: dataList[item].idcCount
                            })
                        }
                        this.drawCharts()
                    })
                } else {
                    iframe.getCountBizByIdcDep2(data).then((res) => {
                        let dataList = res.dataList
                        this.Zu2 = []
                        this.Total = ''
                        this.Total = res.total
                        for (let item in dataList) {
                            this.Zu2.push({
                                name: dataList[item].department2 ? dataList[item].department2 : '未知',
                                value: dataList[item].number
                            })
                        }
                        this.drawCharts()
                    })
                }

            },
            // 绘制环形图表
            drawCharts() {
                let _this = this
                _this.$nextTick(() => {
                    _this.chartData.chartList.forEach((subItem) => {
                        let myChart = echarts.init(document.getElementById(subItem.id))
                        myChart.clear()

                        // 数据格式处理
                        let option = JSON.parse(JSON.stringify(ChartOption[subItem.chartOption]))
                        // let chartDatas = JSON.parse(JSON.stringify(subItem.chartDatas))
                        // 环形图颜色
                        let colorSource = ChartOption['color-option']['linearColor']
                        let colors = [colorSource.blue, colorSource.blueLight, colorSource.green, colorSource.greenLight, colorSource.yellow, colorSource.purple]
                        if (window.projectName == '集中网管' || window.projectName == '客响-集中运维平台') {
                            colors = [colorSource.blue, colorSource.blueLight, colorSource.green, colorSource.greenLight, colorSource.yellow, colorSource.purple, colorSource.orange, colorSource.red, colorSource.cyan, colorSource.cyanLight, colorSource.blueSingle, colorSource.orangeSingle, colorSource.greenSingle]
                        }
                        option.series[0] = JSON.parse(JSON.stringify(option.series[0]))
                        option.series[0].radius = ['20%', '40%']
                        option.series[0].center = ['35%', '50%']
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
                        option.title.show = true

                        option.title.y = '44%'
                        option.title.x = '33%'
                        option.title.text = _this.titleName
                        option.title.subtext = _this.Total
                        option.title.subtextStyle.fontSize = 10
                        option.title.textStyle.color = '#53D8FF'
                        option.title.textStyle.fontSize = 10

                        option.legend.show = true
                        option.legend.type = 'scroll'
                        option.legend.x = '63%'
                        option.legend.orient = 'vertical'

                        option.legend.data = _this.Zu2.length > 0 ? _this.Zu2 : _this.Zu1
                        option.series[0].label.show = false
                        option.series[0].data = _this.Zu2.length > 0 ? _this.Zu2 : _this.Zu1
                        if (window.projectName == '集中网管' || window.projectName == '客响-集中运维平台') {
                            option.tooltip.formatter = '{b} <br/>独立业务设备数: {c} ({d}%)'
                        } else {
                            option.tooltip.formatter = '{b} <br/>业务系统数: {c} ({d}%)'
                        }
                        myChart.setOption(option, true)
                        // resize自适应
                        _this.setResizeFun(myChart)
                        myChart.off('click')
                        if (window.projectName != '集中网管' && window.projectName != '客响-集中运维平台') {
                            myChart.on('click', function (params) {
                                _this.annularName = params.name
                                _this.getCountByIdcDep2Biz()
                            })
                        }
                    })
                })
            },
            // 绘制柱图表
            drawCharts1() {
                let _this = this
                _this.$nextTick(() => {
                    _this.chartData1.chartList.forEach((subItem) => {
                        let myChart = echarts.init(document.getElementById(subItem.id))
                        myChart.clear()

                        let option = JSON.parse(JSON.stringify(ChartOption[subItem.chartOption]))
                        // let chartDatas = JSON.parse(JSON.stringify(subItem.chartDatas))

                        let colors = ChartOption['color-option']['linearColor']

                        option.series[0] = JSON.parse(JSON.stringify(option.series[0]))

                        option.series[0].itemStyle.color.colorStops = colors.blueLight
                        option.legend.data = []
                        option.xAxis.data = _this.bizSystemList.length > 0 ? _this.bizSystemList : '暂无数据'
                        option.series[0].data = _this.numberList
                        // option.yAxis[0].name = _this.annularName + '业务系统设备分布'
                        option.title.show = true
                        if (window.projectName == '集中网管' || window.projectName == '客响-集中运维平台') {
                            option.title.text = '独立业务子模块设备分配'
                            option.title.subtext = '设备量 (台)'
                        } else {
                            option.title.text = _this.annularName + '业务系统设备分布TOP10'
                            option.title.subtext = '设备量 (台)'
                        }
                        option.grid.top = 50

                        option.yAxis[0].nameTextStyle.padding = [0, 0, 0, -40]
                        option.tooltip.formatter = '{b} <br/>设备数: {c}'
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
                        myChart.setOption(option, true)
                        // resize自适应
                        _this.setResizeFun(myChart)

                        if (window.projectName != '集中网管' && window.projectName != '客响-集中运维平台') {
                            myChart.on('click', function (params) {
                                _this.bizSystemName = params.name
                                _this.$router.push({
                                    path: '/resource/iframe/list',
                                    query: {
                                        parentParams: {
                                            idcType: _this.idtypeName,
                                            department1: _this.titleName,
                                            department2: _this.annularName,
                                            bizSystem: _this.bizSystemName
                                        },
                                        condicationCode: 'instance_list'
                                    }
                                })
                            })
                        }
                    })
                })
            }

        },

        mounted() {
            this.bbtn()
            this.bbtnFalse()
        },
        beforeDestroy() {

        }
    }
</script>

<style lang="scss" scoped>
</style>

