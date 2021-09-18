<template>
    <!-- 服务台： 待办工单 -->
    <div class="hp100">
        <orderList orderType="todoList"></orderList>
    </div>
</template>

<script>
    import orderList from 'src/pages/custom_pages/components/sub_components/bpm-orders-list.vue'

    export default {
        components: {
            orderList
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

