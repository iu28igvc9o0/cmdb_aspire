
<template>
    <!-- 科管部告警首页 -->
    <div class="components-container container-view">
        <!-- 顶部模块flex布局 -->
        <el-row :gutter="17"
                justify="space-between"
                type="flex">
            <el-col :span="6">
                <alertOverview :isCustomPage="true"></alertOverview>
                <alertChartLevel :isCustomPage="true"></alertChartLevel>
                <alertDeviceClass2 :isCustomPage="true"></alertDeviceClass2>
            </el-col>
            <el-col :span="12">
                <alertHot :isCustomPage="true"></alertHot>
            </el-col>
            <el-col :span="6">
                <alertCompanyTop :isCustomPage="true"></alertCompanyTop>
                <alertTypeTop :isCustomPage="true"></alertTypeTop>

            </el-col>
        </el-row>
        <!-- 底部模块grid布局 -->
        <div class="component-list mtop20">
            <template v-if="resetComponent">
                <component :is="item.componentName.default"
                           class="component-item"
                           v-for="(item,index) in componentsRequire"
                           :conditionParams="conditionParams"
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
        components: {
            // 左侧模块
            alertOverview: () => import('src/pages/settings/customization/components/alert-overview.vue'),
            alertChartLevel: () => import('src/pages/settings/customization/components/alert-chart-level.vue'),
            alertDeviceClass2: () => import('src/pages/settings/customization/components/alert-device-class2.vue'),
            // 中间模块
            alertHot: () => import('src/pages/settings/customization/components/alert-hot.vue'),
            // 右侧模块
            alertCompanyTop: () => import('src/pages/settings/customization/components/alert-company-top.vue'),
            alertTypeTop: () => import('src/pages/settings/customization/components/alert-type-top.vue'),
        },
        data() {
            return {
                // 查询条件
                conditionParams: {
                    // 日期范围
                    dateRange: [],

                    // 资源
                    poolActive: '',
                    poolList: [{ value: '', name: '全部' }]
                },

                // 组件数据
                componentsData: [],
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
            // 获得数据
            getDatas() {
                this.getParams()
                this.getComponents()
            },

            // 获得查询条件
            getParams() {
                let params = {
                    type: 'idcType',
                    pid: '',
                    pValue: '',
                    pType: '',
                }
                this.$api.queryPool(params).then((res) => {
                    this.conditionParams.poolList = res
                    this.conditionParams.poolList.unshift({ name: '全部', value: '' })
                    this.conditionParams.poolActive = ''
                })
            },

            // 获得模块数据
            getComponents() {
                this.componentsData = [
                    // { componentName: 'alert-overview' },
                    // { componentName: 'alert-hot' },
                    // { componentName: 'alert-company-top', name: '科管告警首页' },
                    // { componentName: 'alert-chart-level' },
                    // { componentName: 'alert-type-top', name: '科管告警首页' },
                    // { componentName: 'alert-device-class2' },
                    // { componentName: 'alert-pod-top' },
                    { componentName: 'alert-chart-trend' },
                    { componentName: 'alert-chart-number' },
                ]
            },

            // 编辑页
            goEdit() {
                this.$store.commit('setComponent', { componentsDir: 'alert', componentsData: this.componentsData })
                this.$router.push({ path: '/settings/customization/edits' })
            },

            // 条件查询
            changeTab() {
                // 更新组件
                this.updateComponent()
            }
        },
        created() {
            this.getDatas()
        },
    }
</script>
 
<style  lang="scss" scoped>
    //自己样式
    /deep/.component-list {
        display: grid;
        grid-template-columns: 24.2% 50% 24.2%;
        justify-content: space-between;
        align-content: start;
        width: 100%;
        .component-item {
            width: 100% !important;
            &:nth-child(1),
            &:nth-child(2) {
                grid-column: span 3;
            }
        }
    }
    /deep/.component-section {
        .component-item {
            width: 100% !important;
        }
    }
</style>
