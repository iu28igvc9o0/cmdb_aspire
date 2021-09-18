<template>
    <div>
        <deviceData v-if="JSON.parse($route.query.queryParams).deviceName==='网络设备'"></deviceData>
        <serverData v-else-if="JSON.parse($route.query.queryParams).deviceName==='服务器'"></serverData>
        <p v-else>正在建设中</p>
    </div>

</template>

<script>
    export default {
        components: {
            deviceData: () => import('src/pages/resource/iframe/detail/deviceData.vue'),
            serverData: () => import('src/pages/resource/iframe/detail/serverData.vue')
        },
        mounted() {
            console.log(JSON.parse(this.$route.query.queryParams).deviceName)
        }
    }
</script>