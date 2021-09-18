<template>
    <div class="component-container"
         style="padding: 0px 0px 0px;">
        <commonview v-if="showIframe"></commonview>
        <!-- <div style="overflow:hidden;">
            <iframe src="http://localhost:8081/front/#/flow/retrieve?mirrorToken=YWxhdWRhfHxuYW1l&pageFrom=ums"
                    width="100%"
                    height="900"
                    frameborder="0"
                    scrolling="auto">

            </iframe>
        </div> -->
    </div>
</template>
<script>
    import commonview from 'src/pages/iframe/components/common-view.vue'
    import rbBpmHomeServices from 'src/services/bpm/rb-bpm-home-services.js'
    export default {
        // name: '/iframe/report/big-screen',
        components: {
            commonview,

        },
        data() {
            return {
                keepAliveIframe: '',
                showIframe: false
            }
        },
        created() {
            // 请求接口 
            let yg_routerHash = this.$route.query.yg_routerHash
            let req = {
                module: yg_routerHash.replace(/\/index\//, '')
            }
            if (!yg_routerHash) {
                this.showIframe = true
                sessionStorage.setItem('yunguanToken', '')
                return
            }
            rbBpmHomeServices
                .getHomeYunToken(req)
                .then(res => {
                    this.showIframe = true
                    sessionStorage.setItem('yunguanToken', res.token)
                }).catch(error => {
                    console.log(error)
                })
        },
        methods: {},



    }
</script>
<style lang="scss" scoped>
</style>
