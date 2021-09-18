<template>
    <!-- 资源：各业务系统趋势 -->
    <div class="content-chart"
         style="width:74.8%;">
        <section class="chart-title-wrap clearfix">
            <svg class="svg-icon svg-icon-24"
                 aria-hidden="true">
                <use xlink:href="#iconyewutongji"></use>
            </svg>
            <span class="chart-title">{{chartData.name}}</span>
            <div class="chart-filter">
                <el-radio-group class="yw-radio-button-wrap chart-filter-item"
                                v-model="chartData.activeFilter"
                                @change="changeTab">
                    <el-radio-button :label="tabItem.label"
                                     v-for="(tabItem,tabIndex) in chartData.filter"
                                     :key="tabIndex">{{tabItem.label}}</el-radio-button>
                </el-radio-group>
            </div>
        </section>

        <!-- 多图表 -->
        <section class="chart-section">
            <div class="chart-box-wrap">
                <div class="chart-box-item"
                     v-for="(subItem,subIndex) in chartData.chartList"
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
    import { fixedNumber } from 'src/assets/js/utility/rb-filters.js'
    import rbHomeService from 'src/services/iframe/rb-home-service.js'
    import iframe from 'src/services/iframe/iframe.js'
    export default {
        mixins: [DrawChart],
        components: {

        },
        data() {
            return {
                tabPosition: 'right',
                p1: false,
                p2: true,
                p3: false,
                FirstList: [],  // 第一条柱子
                SecondList: [], // 第二条柱子
                ThirdList: [],  // 第三条柱子
                depList: [],
                dateFlag: [],   // 日和月的标识
                chartData: (window.projectName == '集中网管' || window.projectName == '客响-集中运维平台') ? {
                    name: '独立业务新增设备量趋势分布TOP10',
                    filter: [{ name: '-1', label: '日' }, { name: '0', label: '月' }, { name: '1', label: '年' }],
                    activeFilter: '月',
                    chartList: [{ id: '123', chartOption: 'bar-option', chartDatas: '' }]
                } : {
                        name: '各一级租户新增设备量趋势分布TOP10',
                        filter: [{ name: '-1', label: '日' }, { name: '0', label: '月' }, { name: '1', label: '年' }],
                        activeFilter: '月',
                        chartList: [{ id: '123', chartOption: 'bar-option', chartDatas: '' }]
                    }
            }
        },

        methods: {
            getDatas() {
                this.chartData.chartList.chartDatas = []
            },
            changeTab(val) {
                if (val === '日') {
                    this.p1 = true
                    this.p2 = false
                    this.p3 = false
                    if (window.projectName == '集中网管' || window.projectName == '客响-集中运维平台') {
                        let queryData = {
                            queryName: 'ICountInstByCdtAPI_internet_countByBusinessWithBarChartTOP10_DAY',
                            queryType: 'newTrendDay'
                        }
                        iframe.businessCountList(queryData).then((res) => {
                            var flag = this.getLastThreeDay()
                            this.packageData(flag, res)
                            this.setDateFlag(flag, 'day')
                            this.drawCharts()
                        })
                    } else {
                        rbHomeService.countDevClsByDepWithDay().then((res) => {
                            var flag = this.getLastThreeDay()
                            this.packageData(flag, res)
                            this.setDateFlag(flag, 'day')
                            this.drawCharts()
                        })
                    }

                } else if (val === '月') {
                    this.p1 = false
                    this.p2 = true
                    this.p3 = false
                    if (window.projectName == '集中网管' || window.projectName == '客响-集中运维平台') {
                        let queryData = {
                            queryName: 'ICountInstByCdtAPI_internet_countByBusinessWithBarChartTOP10_Month',
                            queryType: 'newTrendMonth'
                        }
                        iframe.businessCountList(queryData).then((res) => {
                            var flag = this.getLastThreemMonth()
                            this.packageData(flag, res)
                            this.setDateFlag(flag, 'month')
                            this.drawCharts()
                        })
                    } else {
                        rbHomeService.countDevClsByDepWithMonth().then((res) => {
                            var flag = this.getLastThreemMonth()
                            this.packageData(flag, res)
                            this.setDateFlag(flag, 'month')
                            this.drawCharts()
                        })
                    }

                } else {
                    this.p1 = false
                    this.p2 = false
                    this.p3 = true
                    if (window.projectName == '集中网管' || window.projectName == '客响-集中运维平台') {
                        let queryData = {
                            queryName: 'ICountInstByCdtAPI_internet_countByBusinessWithBarChartTOP10_YEAR',
                            queryType: 'newTrendYear'
                        }
                        iframe.businessCountList(queryData).then((res) => {
                            var flag = this.getLastThreemYear()
                            this.packageData(flag, res)
                            this.setDateFlag(flag, 'Year')
                            this.drawCharts()
                        })
                    } else {
                        rbHomeService.countDevByDepWithYear().then((res) => {
                            var flag = this.getLastThreemYear()
                            this.packageData(flag, res)
                            this.setDateFlag(flag, 'Year')
                            this.drawCharts()
                        })
                    }

                }
            },
            // 封装数据
            packageData(flag, res) {
                var FirstList = []
                var SecondList = []
                var ThirdList = []
                var depList = []
                for (var i = 0; i < res.length; i++) {
                    var crtObj = res[i]
                    var zero = flag[0]
                    var one = flag[1]
                    var two = flag[2]
                    depList.push(crtObj.department)
                    FirstList.push(crtObj[zero])
                    SecondList.push(crtObj[one])
                    ThirdList.push(crtObj[two])
                }
                this.depList = depList
                this.FirstList = FirstList
                this.SecondList = SecondList
                this.ThirdList = ThirdList
            },
            // 获取时间分类标识
            setDateFlag(flag, date) {
                var dateFlag = []
                if (date === 'day') {
                    for (let i = 0; i < flag.length; i++) {
                        dateFlag.push(flag[i])
                    }
                } else if (date === 'month') {
                    var monthName = ['一', '二', '三', '四', '五', '六', '七', '八', '九', '十', '十一', '十二']
                    for (let i = 0; i < flag.length; i++) {
                        var monthIndex = flag[i].split('-')[1]
                        dateFlag.push(monthName[parseInt(monthIndex) - 1] + '月份')
                    }
                } else {
                    for (let i = 0; i < flag.length; i++) {
                        dateFlag.push(flag[i])
                    }
                }
                this.dateFlag = dateFlag
            },
            // 获取最近的三天日期
            getLastThreeDay() {
                var rs = []
                for (var i = -2; i <= 0; i++) {
                    var today = new Date()
                    let targetday_milliseconds = today.getTime() + 1000 * 60 * 60 * 24 * i
                    today.setTime(targetday_milliseconds)
                    let tYear = today.getFullYear()
                    let tMonth = today.getMonth()
                    let tDate = today.getDate()
                    tMonth = tMonth + 1 >= 10 ? tMonth + 1 : '0' + (tMonth + 1)
                    tDate = tDate.toString().length == 1 ? '0' + tDate : tDate
                    rs.push(tYear + '-' + tMonth + '-' + tDate)
                }
                return rs
            },
            // 获取最近三月的月份
            getLastThreemMonth() {
                var rs = []
                for (var i = -2; i <= 0; i++) {
                    let today = new Date()
                    let tYear = today.getFullYear()
                    let tMonth = today.getMonth()
                    today.setMonth(tMonth + i)
                    tMonth = today.getMonth()
                    tMonth = tMonth + 1 >= 10 ? tMonth + 1 : '0' + (tMonth + 1)
                    rs.push(tYear + '-' + tMonth)
                }
                return rs
            },
            getLastThreemYear() {
                let today = new Date()
                var rs = [String(today.getFullYear()), String(today.getFullYear() - 1), String(today.getFullYear() - 2)]
                return rs
            },

            // 绘制柱子表
            drawCharts() {
                let _this = this
                _this.$nextTick(() => {
                    _this.chartData.chartList.forEach((subItem) => {
                        let myChart = echarts.init(document.getElementById(subItem.id))
                        myChart.clear()
                        // 数据格式处理
                        let option = JSON.parse(JSON.stringify(ChartOption[subItem.chartOption]))
                        // let chartDatas = JSON.parse(JSON.stringify(subItem.chartDatas))

                        let colors = ChartOption['color-option']['linearColor']

                        option.series[0] = JSON.parse(JSON.stringify(option.series[0]))
                        option.series[1] = JSON.parse(JSON.stringify(option.series[0]))
                        option.series[2] = JSON.parse(JSON.stringify(option.series[0]))

                        option.yAxis[0].name = '设备量（台）'
                        option.yAxis[0].nameTextStyle.padding = [0, 0, 0, 0]
                        option.series[0].label.show = true
                        option.series[1].label.show = true
                        option.series[2].label.show = true

                        option.series[0].label.fontSize = option.series[1].label.fontSize = option.series[2].label.fontSize = '10'
                        option.series[0].label.formatter = (param) => {
                            if (param.data > 1000 || param.data === 1000) {
                                return fixedNumber(param.data, 1)
                            } if (param.data < 1000 && param.data > 0) {
                                return param.data
                            } else {
                                return ''
                            }
                        }
                        option.series[1].label.formatter = (param) => {
                            if (param.data > 1000 || param.data === 1000) {
                                return fixedNumber(param.data, 1)
                            } if (param.data < 1000 && param.data > 0) {
                                return param.data
                            } else {
                                return ''
                            }
                        }
                        option.series[2].label.formatter = (param) => {
                            if (param.data > 1000 || param.data === 1000) {
                                return fixedNumber(param.data, 1)
                            } if (param.data < 1000 && param.data > 0) {
                                return param.data
                            } else {
                                return ''
                            }
                        }

                        option.series[0].data = _this.FirstList
                        option.series[1].data = _this.SecondList
                        option.series[2].data = _this.ThirdList

                        option.series[0].name = _this.dateFlag[0]
                        option.series[1].name = _this.dateFlag[1]
                        option.series[2].name = _this.dateFlag[2]

                        option.legend.data = [option.series[0].name, option.series[1].name, option.series[2].name]

                        option.xAxis.data = _this.depList

                        option.series[0].itemStyle.color.colorStops = colors.blue
                        option.series[1].itemStyle.color.colorStops = colors.blueLight
                        option.series[2].itemStyle.color.colorStops = colors.green

                        myChart.setOption(option, true)
                        // resize自适应
                        this.setResizeFun(myChart)
                        if (window.projectName != '集中网管' && window.projectName != '客响-集中运维平台') {
                            myChart.on('click', function (params) {
                                var monthList = ['一', '二', '三', '四', '五', '六', '七', '八', '九', '十', '十一', '十二']
                                var monthFlag = ['31', '28', '31', '30', '31', '30', '31', '31', '30', '31', '30', '31']
                                let start = ''
                                var end = ''
                                // 日
                                var flagName = params.seriesName

                                if (_this.p1) {
                                    start = flagName
                                    end = flagName
                                } else if (_this.p2) {
                                    var index = -1
                                    for (; index < monthList.length; index++) {
                                        var c = monthList[index] + '月份'
                                        if (c === flagName) {
                                            break
                                        }
                                    }
                                    var today = new Date()
                                    var tYear = today.getFullYear()
                                    var tickMonth = (index + 1) > 9 ? (index + 1) : '0' + (index + 1)
                                    var flag = parseInt(tickMonth)
                                    var monthEndDay = monthFlag[flag - 1]
                                    if (flag === 2) {
                                        if (tYear % 4 === 0 && tYear % 100 !== 0) {
                                            monthEndDay = monthEndDay + 1
                                        } else if (tYear % 4 === 0 && tYear % 400 === 0) {
                                            monthEndDay = monthEndDay + 1
                                        }
                                    }
                                    start = tYear + '-' + tickMonth + '-01'
                                    end = tYear + '-' + tickMonth + '-' + monthEndDay
                                } else {
                                    start = flagName + '-01-01'
                                    end = flagName + '-12-31'
                                }
                                _this.$router.push({
                                    path: '/resource/iframe/list',
                                    query: {
                                        parentParams: {
                                            'department1': params.name,
                                            // 'startInsertTime': start,
                                            // 'endInsertTime': end
                                            'insert_time': start + ',' + end
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
            this.getDatas()
            this.changeTab('月')
        },
        beforeDestroy() {

        }
    }
</script>

<style lang="scss" scoped>
    .poolTenantAnnularBotton {
        float: right;
        line-height: rem(48);
        margin-right: 10px;
        div {
            float: left;
            cursor: pointer;
            width: 55px;
            height: rem(20);
            line-height: rem(20);
            text-align: center;
            font-size: rem(14);
            p {
                display: inline-block;
                margin-top: 14px;
            }
        }
        .activeAnnular {
            border-bottom: 2px solid #028ded;
            color: #028ded;
        }
    }
</style>

