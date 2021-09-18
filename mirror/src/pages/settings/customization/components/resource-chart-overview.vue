<template>
    <!-- 资源：总览 -->
    <div class="content-chart"
         :style="{width: moduleName === 'guangxi' ? '74.4%' : '49.4%'}">
        <section class="chart-title-wrap clearfix">
            <svg class="svg-icon svg-icon-24"
                 aria-hidden="true">
                <use xlink:href="#iconziyuanzonglan"></use>
            </svg>
            <span class="chart-title">{{chartData.name}}</span>
        </section>

        <!-- 多图表 -->
        <section class="chart-section">
            <div class="chart-box-wrap">
                <div class="chart-box-item"
                     style="width:24.2%"
                     v-for="(item,index) in chartData.chartList"
                     :key="index">
                    <div class="progress-rank-wrap">
                        <div class="progress-rank-header"
                             :class="item.status">
                            <span class="progress-rank-title">{{item.name}}</span>
                        </div>
                        <div class="progress-rank-main">
                            <YwProgress :datas="item"></YwProgress>
                        </div>
                    </div>
                </div>
            </div>
        </section>
        <!-- 多图表 -->
    </div>
</template>

<script>

    export default {
        props: ['moduleName'],
        components: {
            YwProgress: () => import('src/components/common/yw-progress.vue'),
        },
        data() {
            return {
                chartData: {
                    name: '资源总览',
                    filter: [{ name: '物理机', label: 'X86服务器' }, { name: '虚拟机', label: '云主机' }],
                    activeFilter: 'X86服务器',
                    chartList: [{ name: '服务器', list: [], status: 'blue' }, { name: '网络设备', list: [], status: 'green' }, { name: '存储设备', list: [], status: 'green' }, { name: '安全设备', list: [], status: 'purple' }],
                }
            }
        },
        methods: {
            // 获得数据
            getDatas() {
                this.chartData.chartList[0].list = []
                this.chartData.chartList[1].list = []
                this.chartData.chartList[2].list = []
                this.chartData.chartList[3].list = []

                let params = {

                }
                this.$api.queryResourceOverview(params).then((res) => {
                    res.forEach((item) => {
                        let rate = 0
                        let tip = null
                        if (item.total) {
                            rate = item.assigned_count / item.type_count * 100
                        }
                        if (item.device_type == '物理机') {
                            tip = '物理机包含裸金属和宿主机'
                        }
                        let obj = {
                            name: item.device_type,
                            num: item.assigned_count,
                            total: item.type_count,
                            rate: rate,
                            tip: tip
                        }
                        if (item.device_class == '服务器') {
                            this.chartData.chartList[0].list.push(
                                obj
                            )
                        }
                        if (item.device_class == '网络设备') {
                            this.chartData.chartList[1].list.push(
                                obj
                            )
                        }
                        if (item.device_class == '存储设备') {
                            this.chartData.chartList[2].list.push(
                                obj
                            )
                        }
                        if (item.device_class == '安全设备') {
                            this.chartData.chartList[3].list.push(
                                obj
                            )
                        }
                    })

                    this.chartData.chartList[0].list = this.chartData.chartList[0].list.slice(0, 5)
                    this.chartData.chartList[1].list = this.chartData.chartList[1].list.slice(0, 5)
                    this.chartData.chartList[2].list = this.chartData.chartList[2].list.slice(0, 5)
                    this.chartData.chartList[3].list = this.chartData.chartList[3].list.slice(0, 5)

                })

            },
        },
        mounted() {
            this.getDatas()
        }

    }
</script>

<style lang="scss" scoped>
    /deep/.chart-section {
        .chart-box-wrap {
            .chart-box-item + .chart-box-item {
                padding-left: 10px;
            }
        }
    }
</style>

