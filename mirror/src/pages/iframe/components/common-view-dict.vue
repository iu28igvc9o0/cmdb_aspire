<template>
    <div class="iframe-wrap">
        <iframe :src="page"
                v-if="page"
                class="iframe"
                scrolling="no"></iframe>
        <div v-else>没找到可用视图</div>
    </div>
</template>
<script>
    import rbHttp from 'assets/js/utility/rb-http.factory'
    export default {
        data() {
            return {
                page: ''
            }
        },
        created() {
            let path = this.$route.path
            rbHttp.sendRequest({
                method: 'GET',
                url: '/v1/cmdb/configDict/getDictsByType?type=iframe_url'
            }).then((res) => {
                res.forEach((item) => {
                    if (path === item.name) {
                        this.page = item.value
                        return
                    }
                })
            })
        },
        methods: {}
    }
</script>
<style>
    .iframe {
        width: 100%;
        border-width: 0;
        height: calc(100vh + 150px);
    }
</style>
