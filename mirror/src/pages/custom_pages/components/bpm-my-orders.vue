<template>
    <!-- 服务台： 我发起的工单 -->
    <div class="hp100">
        <orderList orderType="myList"></orderList>
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

