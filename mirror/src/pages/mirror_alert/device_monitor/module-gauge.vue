<template>
    <!-- 模块:仪表盘 -->
    <section class="chart-part">
        <div class="chart-title">{{moduleData.title}}</div>
        <div class="chart-content">
            <!-- 图表 -->
            <YwGauge :gaugeDatas="moduleData.data[0]"
                     class="chart-box"
                     v-if="moduleData.data[0].show">no data</YwGauge>
            <!-- 图表 -->

        </div>
    </section>
</template>

<script>
    import QueryObject from 'src/utils/queryObject.js'

    export default {
        mixins: [QueryObject],
        props: ['moduleData'],
        components: {
            YwGauge: () => import('src/components/common/yw-gauge.vue'),
        },
        data() {
            return {


            }
        },
        methods: {
            // 获得仪表盘数据
            getGaugeDatas() {

            },
        },

        mounted() {
            this.getGaugeDatas()
        },

    }
</script>

<style lang="scss" scoped>
    @import "./device-monitor.scss";
</style>
