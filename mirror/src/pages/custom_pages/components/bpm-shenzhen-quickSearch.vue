<template>
    <!-- 快速搜索(深圳) -->
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
                currentComponent: 'bpm-quick-search',
                // 数据
                moduleData: {
                    // 工单
                    order: {
                        // 获得列表服务
                        getListService: 'getOrdersByShenzhen',
                        // 字段（组件key:当前key）
                        queryParamsKeys: {
                            pageNo: 'pageNo',
                            pageSize: 'pageSize',
                            subject: 'instQueryParam'// 搜索字段
                        },
                        // 搜索提示
                        placeholders: '请输入标题、工单号搜索'
                    }
                }
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
                this.moduleData = params.datas
            }
        }

    }
</script>

<style lang="scss" scoped>
</style>

