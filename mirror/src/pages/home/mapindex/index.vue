
<template>
    <!-- 综合首页 -->
    <div class="components-container container-view">
        <!-- <div class="component-edit-button"
         @click="goEdit">
      <i class="el-icon-edit-outline"></i>
    </div> -->
        <div class="component-list">
            <template v-if="resetComponent">
                <component :is="item.componentName.default"
                           class="component-item"
                           :conditionParams="conditionParams"
                           :moduleName="item.moduleName"
                           v-for="(item,index) in componentsRequire"
                           :key="index"></component>
            </template>
        </div>
    </div>
</template>
 
<script>
    import updateComponent from 'src/utils/updateComponent.js'
    export default {
        mixins: [updateComponent],
        components: {

        },
        data() {
            return {
                // 查询条件
                conditionParams: {
                    // 日期范围
                    dateRange: [null, null],

                    // 资源
                    poolActive: '',
                    poolList: [{ value: '', name: '全部' }]
                },

                // 组件数据
                componentsData: [],

                cityName: window.cityName,
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
                    { 'componentName': 'resource-chart-overview', name: this.cityName },
                    { 'componentName': 'order-chart-overview' },
                    { 'componentName': 'alert-chart-my' },
                    { 'componentName': 'alert-today-overview' },
                    { 'componentName': 'alert-device-class' },
                    { 'componentName': 'alert-chart-top' },
                    { 'componentName': 'alert-new-hot' },
                    { 'componentName': 'resource-use-rate' },
                    { 'componentName': 'resource-pool-rate' },
                    { 'componentName': 'resource-user-rate-top' },
                    { 'componentName': 'resource-bizsys-rate-top' },
                    { 'componentName': 'order-type-distributed' },
                    { 'componentName': 'order-chart-statistics' },
                    { 'componentName': 'order-chart-analysis' },
                    { 'componentName': 'order-solve-time' },
                    { 'componentName': 'resource-pool-device' },
                    { 'componentName': 'resource-pool-user' },
                    { 'componentName': 'safe-bug-level' },
                    { 'componentName': 'safe-bizsys-bug-top' },
                    { 'componentName': 'safe-pool-bug' },
                    { 'componentName': 'safe-device-bug-top' }
                ]

                // 陕西环境，删除以下模块
                if (this.cityName === 'shanxi') {
                    let componentsExclude = [
                        'safe-bug-level',
                        'safe-bizsys-bug-top',
                        'safe-pool-bug',
                        'safe-device-bug-top',
                        'resource-user-rate-top',
                        'resource-bizsys-rate-top'
                    ]
                    componentsExclude.forEach(name => {
                        this.componentsData = this.componentsData.filter(item => {
                            return item.componentName !== name
                        })
                    })
                    // 插入 实时资源利用率TOP10 到第一个模块
                    this.componentsData.unshift({ 'componentName': 'resource-using-top10' })

                    // 广西环境，删除以下模块
                } else if (this.cityName === 'guangxi') {
                    let componentsExclude = [
                        'order-chart-overview',
                        'safe-bug-level',
                        'safe-bizsys-bug-top',
                        'safe-pool-bug',
                        'safe-device-bug-top',
                        'order-type-distributed',
                        'order-chart-statistics',
                        'order-chart-analysis',
                        'order-solve-time'
                    ]
                    componentsExclude.forEach(name => {
                        this.componentsData = this.componentsData.filter(item => {
                            return item.componentName !== name
                        })
                    })
                }

            },

            // 编辑页
            goEdit() {
                this.$store.commit('setComponent', { componentsDir: 'home', componentsData: this.componentsData })
                this.$router.push({ path: '/settings/customization/edits' })
            }
        },
        created() {
            this.getComponents()
        },
    }
</script>
 
<style  lang="scss" scoped>
    //自己样式
</style>
