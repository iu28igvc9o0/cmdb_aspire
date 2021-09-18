<template>
    <div class="component-container">
        <commonview></commonview>

    </div>
</template>
<script>
    import commonview from 'src/pages/iframe/components/common-view.vue'

    export default {
        // name: '/iframe/report/big-screen',
        components: {
            commonview,

        },
        data () {
            return {
                keepAliveIframe: ''
            }
        },
        created () {

        },
        methods: {},



    }
</script>
<style lang="scss" scoped>
.iframe {
  width: 100%;
  border-width: 0;
  height: calc(100vh);
}
</style>
