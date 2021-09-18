<template>
    <!-- 部门闭单率 -->
    <component :is="componentsRequire.default"
               :moduleData="moduleData"
               @changeComponent="changeComponent"></component>
</template>

<script>
    export default {
        components: {

        },
        data() {
            return {
                // 组件
                currentComponent: 'bpm-orderCloseRate-overview',
                // 数据
                moduleData: {
                    conditions: '',// 查询条件
                    datas: ''// 数据
                }
            }
        },
        computed: {
            // 动态引入组件
            componentsRequire() {
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

