<template>
    <div class="component-container">
        <commonviewdict></commonviewdict>

    </div>
</template>
<script>
    import commonviewdict from 'src/pages/iframe/components/common-view-dict.vue'

    export default {
        components: {
          commonviewdict,
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
