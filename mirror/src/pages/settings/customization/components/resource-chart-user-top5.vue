<template>
    <!-- 各一级租户服务器分布 -->
    <div class="content-chart"
         style="width:24.2%;">
        <section class="chart-title-wrap clearfix">
            <svg class="svg-icon svg-icon-24"
                 aria-hidden="true">
                <use xlink:href="#iconyewutongji"></use>
            </svg>
            <span class="chart-title">{{chartData.name}}</span>
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

    import ChartOption from 'src/utils/chartOption.js'
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
                xAxis: [],
                phyNumberList: [],
                vmNumberList: [],
                totalNumberList: [],
                chartData: (window.projectName == '集中网管' || window.projectName == '客响-集中运维平台') ? {
                    name: '独立业务服务器分布TOP5',
                    chartList: [{ id: 'resource-chart-user-top5-bar', chartOption: 'bar-option', chartDatas: '' }]
                } : {
                        name: '各一级租户服务器分布TOP5',
                        chartList: [{ id: 'resource-chart-user-top5-bar', chartOption: 'bar-option', chartDatas: '' }]
                    }
            }
        },

        methods: {
            getDatas() {
                this.chartData.chartList.chartDatas = []
            },
            // 各一级租户设备类型分布
            countDeviceClassByDepartment1() {
                if (window.projectName == '集中网管' || window.projectName == '客响-集中运维平台') {
                    let queryData = {
                        queryName: 'ICountInstByCdtAPI_internet_countProduceBarChartTOP5'
                    }
                    iframe.businessCountList(queryData).then((res) => {
                        // console.log('0-0-0-0-', res)
                        var xAxis = []
                        var virtualCount = [] // 虚拟服务器
                        var virtualVMwareCount = []// 虚拟机-Vmware
                        var rackServerCount = []// 机架服务器
                        var bladeServerCount = []// 刀片服务器
                        var physicalCount = [] // 物理服务器
                        var knifeDeviceCount = []// 刀框相关设备
                        var properDeviceCount = []// 专有硬件设备
                        var virtualKVMCount = []// 虚拟机-KVM
                        var totalNumber = [] // 虚拟服务器
                        for (var i = 0; i < res.length; i++) {
                            xAxis.push(res[i].businessName)
                            virtualCount.push(res[i].virtualCount)
                            virtualVMwareCount.push(res[i].virtualVMwareCount)
                            rackServerCount.push(res[i].rackServerCount)
                            bladeServerCount.push(res[i].bladeServerCount)
                            physicalCount.push(res[i].physicalCount)
                            knifeDeviceCount.push(res[i].knifeDeviceCount)
                            properDeviceCount.push(res[i].properDeviceCount)
                            virtualKVMCount.push(res[i].virtualKVMCount)
                            totalNumber.push(res[i].total)
                        }
                        this.xAxis = xAxis
                        this.virtualCount = virtualCount
                        this.virtualVMwareCount = virtualVMwareCount
                        this.rackServerCount = rackServerCount
                        this.bladeServerCount = bladeServerCount
                        this.physicalCount = physicalCount
                        this.knifeDeviceCount = knifeDeviceCount
                        this.properDeviceCount = properDeviceCount
                        this.virtualKVMCount = virtualKVMCount
                        this.totalNumberList = totalNumber
                        this.drawCharts()
                    })
                } else {
                    rbHomeService.countDeviceClassByDepartment1().then((res) => {
                        // console.log('0-0-0-0-', res)
                        var xAxis = []
                        var phyNumber = [] // 物理服务器
                        var vmNumber = [] // 虚拟服务器
                        var totalNumber = [] // 虚拟服务器
                        for (var i = 0; i < res.length; i++) {
                            xAxis.push(res[i].orgName)
                            phyNumber.push(res[i].phyNumber)
                            vmNumber.push(res[i].vmNumber)
                            totalNumber.push(res[i].totalNumber)
                        }
                        this.xAxis = xAxis
                        this.phyNumberList = phyNumber
                        this.vmNumberList = vmNumber
                        this.totalNumberList = totalNumber
                        this.drawCharts()
                    })
                }

            },
            // 绘制柱子图表
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

                        if (window.projectName == '集中网管' || window.projectName == '客响-集中运维平台') {
                            option.series[0] = JSON.parse(JSON.stringify(option.series[0]))
                            option.series[1] = JSON.parse(JSON.stringify(option.series[0]))
                            option.series[2] = JSON.parse(JSON.stringify(option.series[0]))
                            option.series[3] = JSON.parse(JSON.stringify(option.series[0]))
                            option.series[4] = JSON.parse(JSON.stringify(option.series[0]))
                            option.series[5] = JSON.parse(JSON.stringify(option.series[0]))
                            option.series[6] = JSON.parse(JSON.stringify(option.series[0]))
                            option.series[7] = JSON.parse(JSON.stringify(option.series[0]))
                            option.series[0].name = '虚拟机-Vmware'
                            option.series[1].name = '虚拟机'
                            option.series[2].name = '机架服务器'
                            option.series[3].name = '刀片服务器'
                            option.series[4].name = '物理机'
                            option.series[5].name = '刀框相关设备'
                            option.series[6].name = '专有硬件设备'
                            option.series[7].name = '虚拟机-KVM'
                            // option.legend.data = [option.series[0].name, option.series[1].name, option.series[2].name, option.series[3].name, option.series[4].name, option.series[5].name, option.series[6].name, option.series[7].name]
                            option.series[0].itemStyle.color.colorStops = colors.blue
                            option.series[1].itemStyle.color.colorStops = colors.blueLight
                            option.series[3].itemStyle.color.colorStops = colors.green
                            option.series[4].itemStyle.color.colorStops = colors.greenLight
                            option.series[5].itemStyle.color.colorStops = colors.yellow
                            option.series[6].itemStyle.color.colorStops = colors.orange
                            option.series[7].itemStyle.color.colorStops = colors.red
                            option.series[0].stack = option.series[1].stack = option.series[2].stack = option.series[3].stack = option.series[4].stack = option.series[5].stack = option.series[6].stack = option.series[7].stack = 'chart-stack'
                        } else {
                            option.series[0] = JSON.parse(JSON.stringify(option.series[0]))
                            option.series[1] = JSON.parse(JSON.stringify(option.series[0]))

                            option.series[0].name = 'X86服务器'
                            option.series[1].name = '云主机'

                            option.legend.data = [option.series[0].name, option.series[1].name]
                            option.series[0].itemStyle.color.colorStops = colors.blue
                            option.series[1].itemStyle.color.colorStops = colors.blueLight

                            option.series[0].stack = option.series[1].stack = 'chart-stack'
                        }

                        // option.series[0].itemStyle.barBorderRadius = option.series[1].itemStyle.barBorderRadius = 0
                        // option.series[1].itemStyle.barBorderRadius = [4, 4, 0, 0]

                        // option.grid.left = 50
                        option.xAxis.axisLabel.rotate = 20
                        option.yAxis[0].name = '设备量 (台)'
                        if (window.projectName == '集中网管' || window.projectName == '客响-集中运维平台') {
                            option.yAxis[0].nameTextStyle.padding = [0, 0, 0, -30]
                            option.series[7].label.show = true
                            option.series[7].label.data = _this.totalNumberList
                            option.series[7].label.fontSize = '10'
                            // console.log(option.series[7])
                            option.series[7].label.formatter = function (params) {
                                if (_this.totalNumberList[params.dataIndex] > 1000 || _this.totalNumberList[params.dataIndex] === 1000) {
                                    return fixedNumber(_this.totalNumberList[params.dataIndex], 1)
                                } if (_this.totalNumberList[params.dataIndex] < 1000 && _this.totalNumberList[params.dataIndex] > 0) {
                                    return _this.totalNumberList[params.dataIndex]
                                } else {
                                    return ''
                                }
                            }
                        } else {
                            option.yAxis[0].nameTextStyle.padding = [0, 0, 0, -50]
                            option.series[1].label.show = true

                            option.series[1].label.data = _this.totalNumberList
                            option.series[1].label.fontSize = '10'
                            option.series[1].label.formatter = function (params) {
                                if (_this.totalNumberList[params.dataIndex] > 1000 || _this.totalNumberList[params.dataIndex] === 1000) {
                                    return fixedNumber(_this.totalNumberList[params.dataIndex], 1)
                                } if (_this.totalNumberList[params.dataIndex] < 1000 && _this.totalNumberList[params.dataIndex] > 0) {
                                    return _this.totalNumberList[params.dataIndex]
                                } else {
                                    return ''
                                }
                            }
                        }
                        // option.yAxis[0].nameTextStyle.padding = [0,0,0,30]
                        option.xAxis.data = _this.xAxis
                        if (window.projectName == '集中网管' || window.projectName == '客响-集中运维平台') {
                            option.series[0].data = _this.virtualVMwareCount
                            option.series[1].data = _this.virtualCount
                            option.series[2].data = _this.rackServerCount
                            option.series[3].data = _this.bladeServerCount
                            option.series[4].data = _this.physicalCount
                            option.series[5].data = _this.knifeDeviceCount
                            option.series[6].data = _this.properDeviceCount
                            option.series[7].data = _this.virtualKVMCount
                        } else {
                            option.series[0].data = _this.phyNumberList
                            option.series[1].data = _this.vmNumberList
                        }
                        myChart.setOption(option)

                        // resize自适应
                        _this.setResizeFun(myChart)
                        if (window.projectName != '集中网管' && window.projectName != '客响-集中运维平台') {
                            myChart.on('click', function (params) {
                                console.log('params.namemmmm', params.seriesName)
                                _this.$router.push({
                                    path: '/resource/iframe/list',
                                    query: {
                                        parentParams: {
                                            'department1': params.name,
                                            'device_type': params.seriesName,
                                            'device_class': '服务器'
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
            this.countDeviceClassByDepartment1()
        },
        beforeDestroy() {

        }
    }
</script>

<style lang="scss" scoped>
</style>

