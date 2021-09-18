<template>
    <!-- 资源：各资源池设备量分布 -->
    <div class="content-chart"
         v-if='poolFlag'
         style="width:100%;">
        <section class="chart-title-wrap clearfix">
            <svg class="svg-icon svg-icon-24"
                 aria-hidden="true">
                <use xlink:href="#iconziyuan"></use>
            </svg>
            <span class="chart-title">{{poolName}}{{poolThreeName}}{{chartData.name}}</span>
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
                 style="width:calc(75.8% - 1px);float:right;border-left: 1px solid #0B4D77;">
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
                poolFlag: false,
                poolThreeName: '',
                deviceTypeId: '',
                idcTypeId: '',
                poolName: '',
                pieList: [],
                pieTooltip: '',
                projectData: '',
                tranentName: '',
                projectNameId: '', // 工期ID
                QueryType: '',
                deviceClass: ['安全设备', '网络设备', '存储设备'],
                deviceType: ['X86服务器', '云主机'],
                chartData: {
                    name: '工期分布',
                    chartList: [{ id: 'resource-chart-device-three-pie', chartOption: 'pie-option', chartDatas: '' }]
                },
                chartData1: {
                    name: '',
                    chartList: [{ id: 'resource-chart-device-three-bar', chartOption: 'bar-option', chartDatas: '' }]
                }
            }
        },

        methods: {
            bbtnFalg: function () {
                Bus.$on('poolFlagThree', (newV1, newV2, newV3, newV4, newV5, newV6) => {   // 这里最好用箭头函数，不然this指向有问题
                    this.poolFlag = newV1
                    this.poolThreeName = newV2  // 设备类型
                    this.poolName = newV3   // 资源池
                    this.QueryType = newV4
                    this.deviceTypeId = newV5
                    this.idcTypeId = newV6
                    this.getCountByIdcPro()
                    this.getCountByIdcDevPro()
                })
            },
            // 获得数据
            getDatas() {
                this.chartData.chartList.chartDatas = []
            },
            getDatas1() {
                this.chartData1.chartList.chartDatas = []
            },

            getCountByIdcDevPro() {
                let data = {
                    projectName: this.projectNameId,
                    idcType: this.idcTypeId,
                    deviceType: this.deviceTypeId,
                    queryType: this.QueryType
                }
                iframe.getCountByIdcDevPro(data).then((res) => {
                    this.podList = []
                    this.podData = []
                    res.forEach(item => {
                        this.podList.push(item.pod_name ? item.pod_name : '未知')
                        this.podData.push(item.number)
                    })
                    this.drawCharts1()
                })
            },
            getCountByIdcPro() {
                let data = {
                    idcType: this.idcTypeId,
                    deviceType: this.deviceTypeId,
                    queryType: this.QueryType
                }
                iframe.getCountByIdcPro(data).then((res) => {
                    this.projectData = []
                    res.forEach(item => {
                        for (let key in item) {
                            if (key !== 'device_type' && !key.endsWith('Id')) {
                                this.projectData.push({ 
                                    value: item[key], 
                                    name: key === '' ? '未知' : key,
                                    id: item[key+'Id']
                                })
                            }
                        }
                    })
                    this.drawCharts()
                })
            },
            // 绘制饼图表
            drawCharts() {
                let _this = this
                _this.$nextTick(() => {
                    _this.chartData.chartList.forEach((subItem) => {
                        let myChart = _this.initMyChart(subItem.id)
                        myChart.clear()
                        // 数据格式处理
                        let option = JSON.parse(JSON.stringify(ChartOption[subItem.chartOption]))
                        // let chartDatas = JSON.parse(JSON.stringify(subItem.chartDatas))
                        // 环形图颜色
                        let colorSource = ChartOption['color-option']['linearColor']
                        let colors = [colorSource.blue, colorSource.blueLight, colorSource.green, colorSource.greenLight, colorSource.yellow, colorSource.purple]

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

                        option.title.x = '33%'
                        option.title.y = '40%'
                        option.title.text = _this.titleName
                        option.title.subtext = '工期分布'
                        option.title.subtextStyle.fontSize = 10
                        option.title.textStyle.color = '#53D8FF'
                        option.title.textStyle.fontSize = 10

                        option.legend.show = true
                        option.legend.type = 'scroll'
                        option.legend.x = '63%'
                        option.legend.orient = 'vertical'

                        option.legend.data = _this.projectData.length > 0 ? _this.projectData : [{ name: '暂无', value: '0' }]
                        option.series[0].label.show = false
                        option.series[0].data = _this.projectData.length > 0 ? _this.projectData : [{ name: '暂无', value: '0' }]
                        option.tooltip.formatter = '{b} <br/>设备数: {c} ({d}%)'
                        myChart.setOption(option, true)
                        // resize自适应
                        _this.setResizeFun(myChart)
                        myChart.off('click')
                        myChart.on('click', function (params) {
                            _this.projectNameId = params.data.id
                            // 标题名称
                            _this.tranentName = params.name
                            _this.getCountByIdcDevPro()
                        })
                    })
                })
            },
            // 绘制柱图表
            drawCharts1() {
                let _this = this
                _this.$nextTick(() => {
                    _this.chartData1.chartList.forEach((subItem) => {
                        let myChart = _this.initMyChart(subItem.id)
                        myChart.clear()

                        let option = JSON.parse(JSON.stringify(ChartOption[subItem.chartOption]))
                        // let chartDatas = JSON.parse(JSON.stringify(subItem.chartDatas))

                        let colors = ChartOption['color-option']['linearColor']

                        // option.yAxis[0].nameTextStyle.padding = [0, 0, 0, 130]
                        option.series[0] = JSON.parse(JSON.stringify(option.series[0]))
                        option.series[0].label.show = true
                        option.series[0].itemStyle.color.colorStops = colors.blueLight
                        option.legend.data = []
                        option.xAxis.data = _this.podList.length > 0 ? _this.podList : '暂无数据'
                        option.series[0].data = _this.podData
                        option.yAxis[0].name = _this.poolName + _this.tranentName + 'POD池分布'
                        option.yAxis[0].nameTextStyle.padding = [0, 0, 0, -40]
                        option.tooltip.formatter = '{b} <br/>设备数: {c}'

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

                        myChart.off('click')
                        myChart.on('click', function (params) {
                            _this.$router.push({
                                path: '/resource/iframe/list',
                                query: {
                                    parentParams: {
                                        device_project: _this.projectNameId,
                                        pod_name: params.name,
                                        device_type: _this.deviceType.indexOf(_this.poolThreeName) > -1 ? _this.poolThreeName : undefined,
                                        // deviceClass: _this.deviceClass.indexOf(_this.poolThreeName) > -1 ? _this.poolThreeName : undefined,
                                        device_class: _this.deviceClass.indexOf(_this.poolThreeName) > -1 ? _this.poolThreeName : _this.deviceType.indexOf(_this.poolThreeName) > -1 ? '服务器' : undefined,
                                        device_class_3: _this.poolThreeName === 'X86服务器' ? 'X86服务器' : undefined,
                                        idcType: _this.poolName
                                    },
                                    condicationCode: 'instance_list'
                                }
                            })
                        })
                    })
                })
            }
        },
        mounted() {
            this.bbtnFalg()
            this.getDatas()
            this.getDatas1()
        },
        beforeDestroy() {

        }
    }
</script>

<style lang="scss" scoped>
</style>

