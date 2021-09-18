<template>
    <!-- 告警：当日告警总览 -->
    <div class="content-chart"
         style="width: 24.2%;">
        <section class="chart-title-wrap clearfix">
            <svg class="svg-icon svg-icon-24"
                 aria-hidden="true">
                <use xlink:href="#icongaojing"></use>
            </svg>
            <span class="chart-title">{{chartData.name}}</span>
        </section>

        <!-- 多图表 -->
        <section class="chart-section">
            <YwAlertStatus :datas="chartData.chartList"></YwAlertStatus>
        </section>
        <!-- 多图表 -->
    </div>
</template>

<script>

    export default {
        components: {
            YwAlertStatus: () => import('src/components/common/yw-alert-status.vue'),
        },
        data() {
            return {
                chartData: {
                    name: '当日告警总览',
                    filter: [{ name: '物理机', label: 'X86服务器' }, { name: '虚拟机', label: '云主机' }],
                    activeFilter: 'X86服务器',
                    chartList: [
                        {
                            name: '待处理', status: 0, icon: require('src/assets/theme/dark/img/alert-wait-confirm.png'), num: 0,
                            numList: [
                                { status: 'red', num: 0 },
                                { status: 'orange', num: 0 },
                                { status: 'yellow', num: 0 },
                                { status: 'blue', num: 0 },
                            ]
                        },
                        {
                            name: '处理中', status: 1,
                            icon: require('src/assets/theme/dark/img/alert-finish-confirm.png'),
                            num: 0,
                            numList: [
                                { status: 'red', num: '0' },
                                { status: 'orange', num: '0' },
                                { status: 'yellow', num: '0' },
                                { status: 'blue', num: '0' },
                            ]
                        },
                        {
                            name: '已清除', status: 4,
                            icon: require('src/assets/theme/dark/img/alert-finish-clean.png'),
                            num: 0,
                            numList: [
                                { status: 'red', num: '0' },
                                { status: 'orange', num: '0' },
                                { status: 'yellow', num: '0' },
                                { status: 'blue', num: '0' },
                            ]
                        },
                        {
                            name: '已关闭', status: 3,
                            icon: require('src/assets/theme/dark/img/alert-finish-release.png'),
                            num: 0,
                            numList: [
                                { status: 'red', num: '0' },
                                { status: 'orange', num: '0' },
                                { status: 'yellow', num: '0' },
                                { status: 'blue', num: '0' },
                            ]
                        }],
                }
            }
        },
        methods: {
            // 获得数据
            getDatas() {

                let params = {

                }
                this.$api.queryTodayAlertOverview(params).then((res) => {
                    this.chartData.chartList[0].num = this.validData(res.toBeConfirmed.summary) || 0
                    this.chartData.chartList[0].numList[0].num = this.validData(res.toBeConfirmed.serious) || 0
                    this.chartData.chartList[0].numList[1].num = this.validData(res.toBeConfirmed.high) || 0
                    this.chartData.chartList[0].numList[2].num = this.validData(res.toBeConfirmed.medium) || 0
                    this.chartData.chartList[0].numList[3].num = this.validData(res.toBeConfirmed.low) || 0

                    this.chartData.chartList[1].num = this.validData(res.confirmed.summary) || 0
                    this.chartData.chartList[1].numList[0].num = this.validData(res.confirmed.serious) || 0
                    this.chartData.chartList[1].numList[1].num = this.validData(res.confirmed.high) || 0
                    this.chartData.chartList[1].numList[2].num = this.validData(res.confirmed.medium) || 0
                    this.chartData.chartList[1].numList[3].num = this.validData(res.confirmed.low) || 0

                    this.chartData.chartList[2].num = this.validData(res.cleared && res.cleared.summary) || 0
                    this.chartData.chartList[2].numList[0].num = this.validData(res.cleared && res.cleared.serious) || 0
                    this.chartData.chartList[2].numList[1].num = this.validData(res.cleared && res.cleared.high) || 0
                    this.chartData.chartList[2].numList[2].num = this.validData(res.cleared && res.cleared.medium) || 0
                    this.chartData.chartList[2].numList[3].num = this.validData(res.cleared && res.cleared.low) || 0

                    this.chartData.chartList[3].num = this.validData(res.resolved.summary) || 0
                    this.chartData.chartList[3].numList[0].num = this.validData(res.resolved.serious) || 0
                    this.chartData.chartList[3].numList[1].num = this.validData(res.resolved.high) || 0
                    this.chartData.chartList[3].numList[2].num = this.validData(res.resolved.medium) || 0
                    this.chartData.chartList[3].numList[3].num = this.validData(res.resolved.low) || 0
                })


            },
            validData(num) {
                num = Number(num)
                if (num > 10000) {
                    return (num / 10000).toFixed(1) + 'w'
                } else {
                    return num
                }
            },

            changeTab() {
                this.getDatas()
            },
        },
        mounted() {
            this.getDatas()
        }

    }
</script>

<style lang="scss" scoped>
    .chart-section {
        padding: 0;
    }
</style>

