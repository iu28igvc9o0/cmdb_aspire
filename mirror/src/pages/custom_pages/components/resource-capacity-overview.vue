<template>
    <!-- 定制化组件： 资源容量总览 -->
    <div class="content-chart">
        <section class="chart-title-wrap clearfix">
            <svg class="svg-icon svg-icon-24"
                 aria-hidden="true">
                <use xlink:href="#iconliyongshuai"></use>
            </svg>
            <span class="chart-title">{{chartData.name}}</span>
        </section>

        <section class="chart-section">
            <!-- 计算资源 -->
            <div class="chart-box-wrap">
                <div class="chart-box-item p010"
                     style="width:74.5%">
                    <div class="chart-box-main">
                        <template v-for="(subItem,subIndex) in chartData.chartList">
                            <div v-if="subIndex < 3"
                                 style="width:32%"
                                 :key="subIndex">
                                <p class="chart-box-title">{{subItem.chartDatas.title}}计算资源</p>
                                <div :id="subItem.id"
                                     class="content-box-chart"></div>
                                <yw-resource-progress :data="subItem.chartDatas"></yw-resource-progress>
                            </div>
                        </template>
                    </div>
                </div>
                <!-- 存储设备 -->
                <div class="chart-box-item"
                     style="width:24.5%">
                    <div class="chart-box-main">
                        <div style="width:calc(100% - 15px)">
                            <p class="chart-box-title">存储设备</p>
                            <div class="content-box">
                                <yw-progress :datas="storageData"></yw-progress>
                            </div>
                        </div>
                    </div>

                </div>
            </div>
        </section>
        <!-- 多图表 -->
    </div>
</template>

<script>

    import rbCustomServices from 'src/services/custom_pages/rb-services.js'
    import ChartOption from 'src/utils/chartOption.js'
    import DrawChart from 'src/utils/drawCharts.js'
    import YwProgress from './sub_components/yw-progress.vue'
    import YwResourceProgress from './sub_components/yw-resource-progress'

    export default {
        mixins: [DrawChart],
        components: {
            YwProgress,
            YwResourceProgress
        },
        data() {
            return {
                chartData: {
                    name: '资源容量总览',
                    chartList: [
                        {
                            id: 'resource-capacity-view-1', chartOption: 'gauge-option',
                            chartDatas: {
                                title: '虚拟机',
                                assign_count: 0,// 已分配数量
                                total_count: 0,   // 已使用数量
                                un_install_count: 0
                            }
                        },
                        {
                            id: 'resource-capacity-view-2', chartOption: 'gauge-option',
                            chartDatas: {
                                title: '物理机',
                                assign_count: 0,
                                total_count: 0,
                                un_install_count: 0
                            }
                        },
                        {
                            id: 'resource-capacity-view-3', chartOption: 'gauge-option',
                            chartDatas: {
                                title: 'GPU',
                                assign_count: 0,
                                total_count: 0,
                                un_install_count: 0
                            }
                        },
                    ]
                },
                storageData: [
                    {
                        'device_type': 'FC_SAN',
                        'use_count': 0,
                        'total_count': 0
                    },
                    {
                        'device_type': 'IP_SAN',
                        'use_count': 0,
                        'total_count': 0
                    },
                    {
                        'device_type': '对象存储',
                        'use_count': 0,
                        'total_count': 0
                    },
                    {
                        'device_type': '分布式块存储',
                        'use_count': 0,
                        'total_count': 0
                    },
                    {
                        'device_type': '文件存储',
                        'use_count': 0,
                        'total_count': 0
                    }
                ],
            }
        },
        methods: {
            // 容量总览统计
            getInstanceStatistics(curName, map) {
                let req = {
                    // 该值取决于cmdb_sql_source表name字段, 由CMDB统一分配. 需要确保唯一.
                    name: curName,
                    // 根据实际的查询条件进行填写. 该值需要与cmdb_sql_source表中配置的<if> #{} ${}中配置的变量一致.
                    params: {
                        department1: '',
                        department2: '',
                    },
                    // 如果接口需要返回单条记录, 则设置为map即可. 如需返回集合, 则可以不设置或设置为list. 不区分大小写. 系统默认为list.
                    responseType: map
                }
                rbCustomServices
                    .getInstanceStatistics(req)
                    .then(res => {
                        let resData = res
                        // 虚拟机
                        if (curName === 'bpm_department_index_vm_agent_statistic') {
                            this.chartData.chartList[0].chartDatas = Object.assign(this.chartData.chartList[0].chartDatas, resData)
                            this.drawCharts(this.chartData.chartList[0])
                        }
                        // 物理机
                        if (curName === 'bpm_department_index_ph_agent_statistic') {
                            this.chartData.chartList[1].chartDatas = Object.assign(this.chartData.chartList[1].chartDatas, resData)
                            this.drawCharts(this.chartData.chartList[1])
                        }
                        // GPU
                        if (curName === 'bpm_department_index_gpu_agent_statistic') {
                            this.chartData.chartList[2].chartDatas = Object.assign(this.chartData.chartList[2].chartDatas, resData)
                            this.drawCharts(this.chartData.chartList[2])
                        }
                        // 存储设备
                        if (curName === 'bpm_department_index_storage_stats') {
                            if (resData.length) {
                                this.storageData = resData
                            }
                        }
                    })
            },
            // 储存设备
            getStorageUsedView() {
                this.getInstanceStatistics('bpm_department_index_storage_stats', 'list')
            },
            // 获得数据
            getDatas() {
                // 虚拟机的入参 name 是: bpm_department_index_vm_agent_statistic
                // 物理机的入参 name 是: bpm_department_index_ph_agent_statistic
                // GPU的入参 name 是: bpm_department_index_gpu_agent_statistic
                this.getInstanceStatistics('bpm_department_index_vm_agent_statistic', 'map')
                this.getInstanceStatistics('bpm_department_index_ph_agent_statistic', 'map')
                this.getInstanceStatistics('bpm_department_index_gpu_agent_statistic', 'map')
                this.getStorageUsedView()
            },
            // 绘制图表
            drawCharts(subItem) {
                this.$nextTick(() => {
                    let myChart = this.initMyChart(subItem.id)
                    myChart && myChart.clear()
                    myChart && this.drawGauge(myChart, subItem)
                })
            },
            handleRate(num01 = 0, num02 = 0) {
                if (!num01 && !num02) {
                    return 0
                }
                return (num01 / num02).toFixed(4) * 100
            },
            drawGauge(myChart, subItem) {

                // 数据格式处理
                let option = JSON.parse(JSON.stringify(ChartOption[subItem.chartOption]))
                let chartDatas = JSON.parse(JSON.stringify(subItem.chartDatas))
                let colors = ChartOption['color-option']['linearColor']
                option.series[0] = JSON.parse(JSON.stringify(option.series[0]))

                option.series[0].axisLine.lineStyle.color[0] =
                    [this.handleRate(chartDatas.assign_count, chartDatas.total_count) / 100, new echarts.graphic.LinearGradient(0, 0, 1, 0, colors.blue)]// 右/下/左/上
                // if (['resource-capacity-view-1', 'resource-capacity-view-3'].indexOf(subItem.id) > -1) {
                //     option.series[0].axisLine.lineStyle.color[0] = [chartDatas.assign_count / 100, new echarts.graphic.LinearGradient(0, 0, 1, 0, [{ // 右/下/左/上
                //         offset: 0, color: '#007643'                    }, {
                //         offset: 1,
                //         color: '#00DB62'
                //     }
                //     ])]
                // }

                option.series[0].name = '已分配'
                option.series[0].data[0].name = ''
                option.series[0].data[0].value = this.handleRate(chartDatas.assign_count, chartDatas.total_count)

                option.series[0].detail.formatter = function () {

                    return '{detailName|' + chartDatas.title + '已分配(台)：' + (chartDatas.assign_count || 0)
                        + '\n}{assign_countName|使用量(台)：' + (chartDatas.total_count || 0) + '}'
                }
                option.series[0].detail.padding = [100, 0, 0, 0]
                option.series[0].detail.offsetCenter = [0, '110%']
                option.series[0].detail.lineHeight = 14
                myChart.setOption(option)

                // resize自适应
                this.setResizeFun(myChart)
            },
        },
        mounted() {
            this.getDatas()
        }

    }
</script>

<style lang="scss" scoped>
    .chart-section {
        padding-top: 30px;
        padding-bottom: 30px;
    }
    .chart-filter {
        width: 100%;
        text-align: center;
        margin-bottom: 5px;
    }
    .chart-box-main {
        width: 100%;
        height: calc(100% - 36px);
        display: flex;
        justify-content: space-between;
        .chart-box-title {
            height: 36px;
            line-height: 36px;
            text-align: left;
            font-size: $font-16;
            color: $color-base;
            .num {
                font-size: $font-20;
                color: #52c4ff;
            }
            .cell {
                font-size: $font-14;
            }
        }
        .content-box-chart {
            width: 100%;
            height: calc(100% - 50px);
        }
        .content-box {
            width: 100%;
            height: 100%;
        }
    }
</style>

