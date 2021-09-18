<template>
    <!-- 科管部资源首页 -->
    <div class="components-container container-view">
        <div class="component-list">
            <template v-if="resetComponent">
                <component :is="item.componentName.default"
                           class="component-item"
                           v-for="(item,index) in componentsRequire"
                           :moduleName="item.moduleName"
                           :key="index"></component>
            </template>
        </div>
    </div>
</template>
 
<script>
    import updateComponent from 'src/utils/updateComponent.js'

    export default {
        mixins: [updateComponent],
        data() {
            return {
                // 组件数据
                componentsData: []
            }
        },
        computed: {
            // 动态引入组件
            componentsRequire() {
                let componentsRequire = []
                this.componentsData.forEach(item => {
                    componentsRequire.push({
                        componentName: require(`src/pages/settings/customization/components/${item.componentName}.vue`),
                        moduleName: item.name
                    })

                })
                return componentsRequire
            }
        },
        methods: {
            // 获得模块数据
            getComponents() {
                this.componentsData = [
                    { componentName: 'resource-order-device-keguan' },    // 科管部模块： 资源总览
                    { componentName: 'resource-chart-produce-device' },  // 科管部模块： 各设备厂商型号分布
                    { componentName: 'resource-chart-resource-distribute' },  // 科管部模块： 资源使用情况分布
                    { componentName: 'resource-chart-network-device' },  // 科管部模块： 服务器业务使用占比
                ]
            },
        },
        created() {
            this.getComponents()
        }
    }
</script>
 
<style  lang="scss" scoped>
</style>
