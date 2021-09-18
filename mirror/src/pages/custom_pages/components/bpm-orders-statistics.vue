<template>
    <!-- 服务台： 服务事件工单统计、列表 -->
    <div class="hp100">
        <!-- 统计数据 -->
        <order-statistics></order-statistics>
        <!-- 列表 -->
        <event-order-list></event-order-list>
    </div>
</template>

<script>
    import eventOrderList from 'src/pages/custom_pages/components/sub_components/bpm-server-event-orders-list.vue'
    import orderStatistics from 'src/pages/custom_pages/components/sub_components/bpm-server-event-orders-statistics.vue'

    export default {
        components: {
            eventOrderList,
            orderStatistics
        },
        data() {
            return {
            }
        },
        methods: {

        },
        mounted() {
        }

    }
</script>

<style lang="scss" scoped>
</style>

