<template>
    <div ref="test"
         style="width:100%;height:100%;"></div>

</template>

<script>
    import CommonOption from 'src/utils/commonOption.js'
    import rbMonitorService from 'src/services/monitor/rb-monitor-service.factory.js'
    import DrawChart from 'src/utils/drawCharts.js'
    export default {
        mixins: [DrawChart, CommonOption],
        props: ['moduleData', 'filterData'],
        components: {

        },
        data() {
            return {
                datax: [],
                data1: [],
                data2: [],
                data3: [],
                data4: [],
                data5: []

            }
        },

        methods: {
            getDatas() {
                this.datax = ['暂无数据']
                this.data1 = []
                this.data2 = []
                this.data3 = []
                this.data4 = []
                this.data5 = []
                this.showFullScreenLoading({ text: '正在查询数据, 请稍等...' })
                let params = {
                    idcType: this.moduleData.conditionParams.idcType,
                    item: this.moduleData.tabParams.name,
                    deviceType: this.filterData.label,
                    startTime: this.moduleData.conditionParams.date ? this.moduleData.conditionParams.date[0] : '',
                    endTime: this.moduleData.conditionParams.date ? this.moduleData.conditionParams.date[1] : ''

                }

                return rbMonitorService.deviceTrendByPool(params).then((res) => {
                    this.datax = []
                    this.data1 = []
                    this.data2 = []
                    this.data3 = []
                    this.data4 = []
                    this.data5 = []
                    res.forEach((item) => {
                        this.datax.push(item['day'])
                        this.data1.push(item['fifteen_ratio'])
                        this.data2.push(item['thirty_ratio'])
                        this.data3.push(item['sixty_ratio'])
                        this.data4.push(item['eighty_five_ratio'])
                        this.data5.push(item['hundred_ratio'])
                    })
                    return res
                }).finally(() => {
                    this.closeFullScreenLoading()
                })
            },
            draw() {

                let option = {
                    // 灰色、蓝色、绿色、橙色、红色
                    color: ['gray', 'blue', 'green', 'orange', 'red'],
                    title: {
                        text: `资源性能分布：${this.filterData.name}`,
                        subtext: ''
                    },
                    tooltip: {
                        trigger: 'axis'
                    },
                    legend: {
                        show: true,
                        icon: 'rect',
                        data: ['0%-15%', '15%-30%', '30%-60%', '60%-85%', '85%-100%']
                    },
                    toolbox: {
                        show: false,
                        feature: {
                            magicType: { show: true, type: ['stack', 'tiled'] },
                            saveAsImage: { show: true }
                        }
                    },
                    xAxis: {
                        type: 'category',
                        boundaryGap: false,
                        data: this.datax
                    },
                    yAxis: {
                        type: 'value',
                        name: '占比(%)',
                        max: 100,
                    },
                    series: [
                        //     {
                        //     name: '0%-15%',
                        //     type: 'line',
                        //     smooth: true,
                        //     areaStyle: {},
                        //     stack: 'trend',
                        //     data: this.data1
                        // },
                        // {
                        //     name: '15%-30%',
                        //     type: 'line',
                        //     smooth: true,
                        //     areaStyle: {},
                        //     stack: 'trend',
                        //     data: this.data2
                        // }
                    ]
                }

                option.series = option.legend.data.map((item, index) => {
                    let obj = {
                        name: item,
                        type: 'line',
                        smooth: true,
                        symbol: 'none',
                        areaStyle: {},
                        stack: 'trend',
                        data: this['data' + (index + 1)]
                    }
                    return obj
                })

                this.$nextTick(() => {
                    let myChart = echarts.init(this.$refs['test'])
                    myChart.clear()

                    // 数据格式处理
                    myChart.setOption(option, true)
                    // resize自适应
                    this.setResizeFun(myChart)

                })

            },
            async init() {
                await this.getDatas()
                this.draw()
            }
        },

        mounted() {
            this.init()
        },
    }
</script>

<style lang="scss" scoped>
</style>
