<template>
    <div class="component-container"
         style="padding: 0px 0px 0px;">
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
        data() {
            return {
                keepAliveIframe: ''
            }
        },
        created() {

        },
        methods: {},



    }
</script>
<style lang="scss" scoped>
</style>
