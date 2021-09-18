<template>
    <!-- 效能展示 -->
    <component :is="componentsRequire.default"
               :moduleData="moduleData"
               @changeComponent="changeComponent"></component>
</template>

<script>
    export default {
        components: {
            // 'bpm-effectStatistics-overview': () => import('./bpm-effectStatistics-overview.vue'),
        },
        data() {
            return {
                // 组件
                currentComponent: 'bpm-effectStatistics-overview',
                // 数据
                moduleData: ''
            }
        },
        computed: {
            // 动态引入组件
            componentsRequire() {
                // let componentsRequire = []
                // this.componentsData.forEach(item => {
                //     componentsRequire.push({ componentName: require(`src/pages/settings/customization/components/${item.componentName}.vue`) })
                // })
                let componentsRequire = require(`./${this.currentComponent}.vue`)
                return componentsRequire
            }
        },
        mounted() {

        },
        methods: {
            // 切换组件
            changeComponent(params = {}) {
                this.currentComponent = params.url
                this.moduleData = params
            }
        }

    }
</script>

<style lang="scss" scoped>
</style>

