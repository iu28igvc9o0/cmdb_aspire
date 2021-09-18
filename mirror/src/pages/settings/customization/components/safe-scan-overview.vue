<template>
    <div class="content-chart"
         style="width: 24.2%;">
        <section class="chart-title-wrap clearfix">
            <svg class="svg-icon svg-icon-24"
                 aria-hidden="true">
                <use xlink:href="#iconanquansaomiao"></use>
            </svg>
            <span class="chart-title">{{chartData.name}}</span>
        </section>

        <!-- 多图表 -->
        <section class="chart-section">
            <YwAlert :datas="chartData.chartList"
                     :alertOption="alertOption"></YwAlert>
        </section>
        <!-- 多图表 -->
    </div>
</template>

<script>
    import DrawChart from 'src/utils/drawCharts.js'
    export default {
        mixins: [DrawChart],
        components: {
            YwAlert: () => import('src/components/common/yw-alert.vue'),
        },
        data() {
            return {
                chartData: {
                    name: '安全扫描总览',
                    chartList: [{
                        name: '扫描总次数',
                        icon: require('src/assets/theme/dark/img/safe-scan.png'),
                        num: 0,
                    },
                    {
                        name: '扫描业务系统',
                        icon: require('src/assets/theme/dark/img/safe-bizSys.png'),
                        num: 0,
                    }],
                },
                alertOption: {
                    color: '#00F0FF'
                }
            }
        },
        methods: {
            // 获得数据
            getDatas() {

                let params = {
                    beginDate: this.conditionParams.dateRange[0],
                    endDate: this.conditionParams.dateRange[1],
                }

                this.$api.querySafeScanOverview(params).then((res) => {
                    this.chartData.chartList[0].num = res.scanCount || 0
                    this.chartData.chartList[1].num = res.bizCount || 0
                })

            },

        },
        mounted() {
            this.getDatas()
        }

    }
</script>

<style lang="scss" scoped>
</style>

