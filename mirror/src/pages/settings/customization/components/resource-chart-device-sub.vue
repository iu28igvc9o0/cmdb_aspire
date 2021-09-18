<template>
    <!-- 资源：设备子类型分布 -->
    <div class="content-chart"
         style="width:24.2%;">
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

    import rbHomeService from 'src/services/iframe/rb-home-service.js'
    import iframe from 'src/services/iframe/iframe.js'
    export default {
        mixins: [DrawChart],
        components: {

        },
        data() {
            return {
                tabPosition: 'right',
                p1: true,
                p2: false,
                p3: false,
                p4: false,
                title: '服务器',
                titleID: '',
                subTotal: '',
                showData: [],
                chartData: {
                    name: '设备子类型分布',
                    filter: [{ name: '-1', label: '服务器' }, { name: '0', label: '网络' }, { name: '1', label: '安全' }, { name: '3', label: '存储' }, { name: '4', label: '磁带库' }],
                    activeFilter: '服务器',
                    chartList: [{ id: 'equipment', chartOption: 'pie-option', chartDatas: '' }]
                }
            }
        },

        methods: {
            getDatas() {
                this.chartData.chartList.chartDatas = []
            },
            changeTab(val) {
                var valName = ''
                if (val === '服务器') {
                    valName = '9561'
                    this.title = '服务器'
                    this.titleID = '9561'
                } else if (val === '网络') {
                    valName = '9564'
                    this.title = '网络设备'
                    this.titleID = '9564'
                } else if (val === '安全') {
                    valName = '9560'
                    this.title = '安全设备'
                    this.titleID = '9560'
                } else if (val === '存储') {
                    valName = '9559'
                    this.title = '存储设备'
                    this.titleID = '9559'
                } else if (val == '磁带库' && (window.projectName == '集中网管' || window.projectName == '客响-集中运维平台')) {
                    valName = '磁带库'
                    this.title = '磁带库'
                }
                var params = {
                    deviceClass: valName
                }
                if (window.projectName == '集中网管' || window.projectName == '客响-集中运维平台') {
                    switch (valName) {
                        case '9561':
                            params.deviceClass = '服务器'
                            break
                        case '9564':
                            params.deviceClass = '网络设备'
                            break
                        case '9560':
                            params.deviceClass = '安全设备'
                            break
                        case '9559':
                            params.deviceClass = '存储设备'
                            break
                        case '磁带库':
                            params.deviceClass = '磁带库'
                            break
                    }
                    params.queryName = 'ICountInstByCdtAPI_internet_countDeviceTypeByDeviceClass'
                    iframe.businessCountList(params).then((res) => {
                        var rs = []
                        var sum = 0
                        for (var i = 0; i < res.length; i++) {
                            var obj = {
                                name: (res[i].device_type == null ? '未知' : res[i].device_type),
                                value: res[i].num
                            }
                            sum += res[i].num
                            rs.push(obj)
                        }
                        this.subTotal = sum
                        this.showData = rs
                        this.drawCharts()
                    })
                } else {
                    rbHomeService.countDeviceTypeByDeviceClass(params).then((res) => {
                        var rs = []
                        var sum = 0
                        for (var i = 0; i < res.length; i++) {
                            var obj = {
                                name: (res[i].device_type == null ? '未知' : res[i].device_type),
                                value: res[i].num
                            }
                            sum += res[i].num
                            rs.push(obj)
                        }
                        this.subTotal = sum
                        this.showData = rs
                        this.drawCharts()
                    })
                }

            },
            // 绘制饼图表
            drawCharts() {
                let _this = this
                _this.$nextTick(() => {
                    _this.chartData.chartList.forEach((subItem) => {
                        let myChart = echarts.init(document.getElementById(subItem.id))
                        myChart.clear()
                        let option = JSON.parse(JSON.stringify(ChartOption[subItem.chartOption]))
                        // let chartDatas = JSON.parse(JSON.stringify(subItem.chartDatas))
                        // 环形图颜色
                        let colorSource = ChartOption['color-option']['linearColor']
                        let colors = [colorSource.blue, colorSource.blueLight, colorSource.green, colorSource.greenLight, colorSource.yellow, colorSource.purple]
                        if (window.projectName == '集中网管' || window.projectName == '客响-集中运维平台') {
                            colors = [colorSource.blue, colorSource.blueLight, colorSource.green, colorSource.greenLight, colorSource.yellow, colorSource.purple, colorSource.orange, colorSource.red, colorSource.cyan, colorSource.cyanLight, colorSource.blueSingle, colorSource.orangeSingle, colorSource.greenSingle]
                        }
                        option.series[0] = JSON.parse(JSON.stringify(option.series[0]))

                        option.series[0].radius = ['25%', '50%']
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
                        option.legend.show = true
                        option.legend.data = _this.showData
                        option.legend.top = 'center'
                        // option.legend.y = 'center' // 设置y轴居中不起作用
                        option.legend.x = '60%'
                        option.legend.formatter = function (param) {
                            let index = 0
                            let datas = _this.showData
                            datas.forEach(function (item, i) {
                                if (item.name === param) {
                                    index = i
                                }
                            })
                            let text = _this.showData[index].name + '   ' + _this.showData[index].value + '   '

                            return text
                        }

                        option.title.y = '44%'
                        option.title.x = '33%'
                        option.title.text = _this.title
                        option.title.subtext = _this.subTotal
                        option.title.textStyle.color = '#53D8FF'
                        option.title.subtextStyle.color = '#fff'
                        option.title.subtextStyle.fontSize = 14
                        option.title.textStyle.fontSize = 16
                        option.series[0].data = _this.showData
                        option.series[0].label.show = false
                        option.tooltip.formatter = '{b} <br/>设备数: {c} ({d}%)'
                        myChart.setOption(option, true)
                        // resize自适应
                        _this.setResizeFun(myChart)
                        myChart.off('click')
                        if (window.projectName != '集中网管' && window.projectName != '客响-集中运维平台') {
                            myChart.on('click', function (params) {
                                var deviceType = params.name
                                _this.$router.push({
                                    path: '/resource/iframe/list',
                                    query: {
                                        parentParams: {
                                            'device_type': deviceType,
                                            'device_class': _this.title
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
            // this.countDeviceTypeByDeviceClass('服务器')
            this.changeTab('服务器')
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

