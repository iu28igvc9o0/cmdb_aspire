<template>
    <!-- 告警：最新热点告警 -->
    <div class="content-chart">
        <section class="chart-title-wrap clearfix">
            <span class="chart-title">{{chartData.name}}</span>
        </section>

        <!-- 多图表 -->
        <section class="chart-section">
            <YwAlertHot :datas="chartData.chartList"></YwAlertHot>
        </section>
        <!-- 多图表 -->
    </div>
</template>

<script>
    import { alert_level_obj } from 'src/pages/mirror_alert/alert_his/config/options.js'
    import { formatDate } from 'src/assets/js/utility/rb-filters.js'


    export default {
        components: {
            YwAlertHot: () => import('./sub_components/yw-alert-hot.vue'),

        },
        data() {
            return {
                chartData: {
                    name: '最新热点告警',
                    filter: [{ name: '物理机', label: 'X86服务器' }, { name: '虚拟机', label: '云主机' }],
                    activeFilter: 'X86服务器',
                    chartList: [],
                }
            }
        },
        methods: {
            // 获得数据
            getDatas() {

                let params = {
                    'limit': 20,
                }
                this.$api.queryAlertHot(params).then((res) => {

                    this.chartData.chartList = res.map((item) => {

                        return {
                            status: alert_level_obj[item.alert_level].color,
                            statusName: alert_level_obj[item.alert_level].name,
                            name: item.device_ip,
                            desc: item.moni_index,
                            date: formatDate(item.cur_moni_time),
                        }
                    })

                })
            },

        },
        mounted() {
            this.getDatas()
        }

    }
</script>

<style lang="scss" scoped>
    .chart-section {
        padding: 0 0 0 2px;
    }
</style>

