<template>
    <div id="app">
        <router-view />
    </div>
</template>

<script>
    export default {
        data() {
            return {
                name: 'app'
            }
        }
    }
</script>

<style  lang="scss">
    @import "assets/css/elementui.scss";
    @import "src/pages/auto_operation/assets/global.scss";
</style>
