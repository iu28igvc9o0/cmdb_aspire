
<template>
    <!-- 科管部监控首页 -->
    <div class="components-container container-view">
        <div class="component-condition clearfix">
            <el-date-picker class="yw-date-editor-big fr"
                            v-model="conditionParams.dateRange"
                            style="width:338px"
                            @change="changeTab"
                            format="yyyy-MM-dd"
                            value-format="yyyy-MM-dd"
                            type="daterange"
                            range-separator="至"
                            start-placeholder="开始日期"
                            end-placeholder="结束日期">
            </el-date-picker>
        </div>
        <!-- 数据面板 -->
        <el-row :gutter="17"
                type="flex">
            <el-col :span="num"
                    v-for="(num,index) in colList"
                    :key="index">
                <template v-if="resetComponent">
                    <panel-box :panelData="panelDataList[index]"></panel-box>
                </template>
            </el-col>
        </el-row>
        <div class="component-list mtop20">
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
    import rbKeguanServices from 'src/services/iframe/rb-keguan-services.js'
    import updateComponent from 'src/utils/updateComponent.js'
    import { formatDate2 } from 'src/assets/js/utility/rb-filters.js'
    import panelBox from './panel-box'

    export default {
        mixins: [updateComponent],
        components: {
            panelBox
        },
        data() {
            return {

                colList: [12, 12, 12, 12],
                panelDataList: [
                    {
                        iconUrl: '/static/img/auto-operation/icon_dashboard_05.png',
                        s_count: 0,
                        h_count: 0,
                        m_count: 0,
                        l_count: 0,
                        device_class: '服务器',
                        device_count: 0
                    },
                    {
                        iconUrl: '/static/img/auto-operation/icon_dashboard_02.png',
                        s_count: 0,
                        h_count: 0,
                        m_count: 0,
                        l_count: 0,
                        device_class: '网络设备',
                        device_count: 0
                    },
                    {
                        iconUrl: '/static/img/auto-operation/icon_dashboard_03.png',
                        s_count: 0,
                        h_count: 0,
                        m_count: 0,
                        l_count: 0,
                        device_class: '存储设备',
                        device_count: 0
                    },
                    {
                        iconUrl: '/static/img/auto-operation/icon_dashboard_01.png',
                        s_count: 0,
                        h_count: 0,
                        m_count: 0,
                        l_count: 0,
                        device_class: '安全设备',
                        device_count: 0
                    },
                ],
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
            // 获取告警总览
            getAlertView(req) {
                rbKeguanServices
                    .getAlertView(req)
                    .then(res => {
                        this.panelDataList.forEach(item => {
                            res.forEach(info => {
                                if (item.device_class === info.device_class) {
                                    item = Object.assign(item, info)
                                }
                            })
                        })
                    })
            },
            // 获得查询条件
            getParams() {
                let now = new Date()
                let before = now.getTime() - 1000 * 60 * 60 * 24 * 7
                if (!this.conditionParams.dateRange.length) {
                    this.conditionParams.dateRange = [formatDate2(before), formatDate2(now)]
                }
                this.getAlertView({
                    startDate: this.conditionParams.dateRange[0],
                    endDate: this.conditionParams.dateRange[1]
                })
            },

            // 获得模块数据
            getComponents() {
                this.componentsData = [
                    { 'componentName': 'resource-use-rate-keguan' },    // 资源利用率
                    { 'componentName': 'resource-use-tendency' },   // 资源利用率趋势
                    { 'componentName': 'resource-use-view' },   // 存储资源使用总览
                    { 'componentName': 'monitor-chart-top5-keguan', name: '业务资源利用率' }]
            },

            // 编辑页
            goEdit() {
                this.$store.commit('setComponent', { componentsDir: 'monitor', componentsData: this.componentsData })
                this.$router.push({ path: '/settings/customization/edits' })
            },

            // 条件查询
            changeTab() {
                // 更新组件
                this.updateComponent()
                this.getDatas()
            }
        },
        created() {
            this.getDatas()
        },
    }
</script>
 
<style  lang="scss" scoped>
    //自己样式
    /deep/ .el-radio-button__inner {
        width: 48px !important;
    }
</style>
