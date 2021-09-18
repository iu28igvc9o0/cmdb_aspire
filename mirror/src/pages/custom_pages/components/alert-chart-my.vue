<template>
    <!-- 告警：我的告警 -->
    <div class="content-chart">
        <section class="chart-title-wrap clearfix">
            <span class="chart-title">{{chartData.name}}</span>
        </section>

        <!-- 多图表 -->
        <section class="chart-section">
            <YwAlert :datas="chartData.chartList"></YwAlert>
        </section>
        <!-- 多图表 -->
    </div>
</template>

<script>

    export default {
        components: {
            YwAlert: () => import('./sub_components/yw-alert.vue'),
        },
        data() {
            return {
                chartData: {
                    name: '我的告警',
                    filter: [{ name: '物理机', label: 'X86服务器' }, { name: '虚拟机', label: '云主机' }],
                    activeFilter: 'X86服务器',
                    chartList: [{
                        name: '活动告警',
                        icon: require('src/assets/theme/dark/img/alert-active.png'),
                        num: 0,
                    },
                    {
                        name: '确认告警',
                        icon: require('src/assets/theme/dark/img/alert-confirm.png'),
                        num: 0,
                    }],
                }
            }
        },
        methods: {
            // 获得数据
            getDatas() {

                let params = {

                }
                this.$api.queryMyAlert(params).then((res) => {
                    this.chartData.chartList[0].num = res.toBeConfirm || 0
                    this.chartData.chartList[1].num = res.confirmed || 0
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

