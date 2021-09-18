<template>
    <!-- 深圳服务台首页：我的工单+公告 -->
    <div class="flex-column-space-between">
        <bpm-shenzhen-myOrders style="height:330px;"></bpm-shenzhen-myOrders>
        <bpm-table-announcement style="height:330px;"></bpm-table-announcement>
    </div>
</template>

<script>
    export default {
        components: {
            'bpm-shenzhen-myOrders': () => import('./bpm-shenzhen-myOrders.vue'),
            'bpm-table-announcement': () => import('./bpm-table-announcement.vue'),
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

