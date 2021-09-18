
<template>
    <!-- 自动化首页 -->
    <div class="components-container container-view">
        <!-- 数据面板 -->
        <el-row :gutter="17"
                type="flex">
            <el-col :span="num"
                    v-for="(num,index) in colList"
                    :key="index">
                <panel-box :panelData="panelDataList[index]"></panel-box>
            </el-col>
        </el-row>
        <!-- 任务图表、快捷入口等 -->
        <div class="component-list auto-operation mtop20">
            <template v-if="resetComponent">
                <component :is="item.componentName.default"
                           :conditionParams="conditionParams"
                           class="component-item"
                           v-for="(item,index) in componentsRequire"
                           :key="index"></component>
            </template>
        </div>

    </div>
</template>
 
<script>
    import rbAutoOperationHomeServicesFactory from 'src/services/auto_operation/rb-auto-operation-home-services.factory.js'
    import panelBox from './panel-box'
    import taskDuration from './task-duration'
    import statusList from 'src/pages/auto_operation/components/status-list.vue'
    import updateComponent from 'src/utils/updateComponent.js'
    export default {
        name: 'AutoOperationHome',
        components: {
            panelBox,
            taskDuration,
            statusList
        },
        data() {
            return {
                colList: [9, 9, 9, 9, 12],
                panelDataList: [
                    {
                        name: '自动化对象数量',
                        iconUrl: '/static/img/auto-operation/icon_dashboard_05.png',
                        count: '',
                    },
                    {
                        name: '7天自动化任务',
                        iconUrl: '/static/img/auto-operation/icon_dashboard_02.png',
                        count: '',
                    },
                    {
                        name: '7天自动巡检任务',
                        iconUrl: '/static/img/auto-operation/icon_dashboard_03.png',
                        count: '',
                    },
                    {
                        name: '7天故障自愈任务',
                        iconUrl: '/static/img/auto-operation/icon_dashboard_01.png',
                        count: '',
                    },
                    {
                        name: '30天内任务总数',
                        iconUrl: '/static/img/auto-operation/icon_dashboard_04.png',
                        count: '',
                    },
                ],
                historicalTaskList: [], // 最近任务执行记录
                hostStatistic: {}, // 业务主机
                pipelineStatistic: {}, // 常用作业
                recent30DaysRunStatistic: {}, // 近30天任务
                // 定时任务
                pipelineJobStatistic: {
                    runJobCount: 0,
                    pauseJobCount: 0
                },
                // 查询条件
                conditionParams: {
                    // 日期范围
                    dateRange: [null, null],

                    // 资源
                    poolActive: '',
                    poolList: [{ value: '', name: '全部' }]
                },
                // 组件列表
                componentsData: []
            }
        },
        mixins: [updateComponent],
        computed: {
            // 动态引入组件
            componentsRequire() {
                let componentsRequire = []
                this.componentsData.forEach(item => {
                    componentsRequire.push({ componentName: require(`./${item.componentName}.vue`) })
                })
                return componentsRequire
            }
        },
        created() {
            // panel-box
            this.queryNewIndexAgentStatistic()
            this.queryNewIndexPipeInstStatistic7Days()
            this.queryNewIndexInspectionTaskStatistic7Days()
            this.queryNewIndexAutoRepairInstanceStatistic7Days()
            this.queryNewIndexAllTasksStatistic30Days()

            // components

            this.getComponents()
        },
        methods: {

            // 获得模块数据
            getComponents() {
                this.componentsData = [
                    { 'componentName': 'task-run-7days' },
                    { 'componentName': 'work-order-distribution' },
                    { 'componentName': 'scene-entrance' },
                    { 'componentName': 'task-duration' },
                    { 'componentName': 'task-success-percent' },
                    { 'componentName': 'today-task' },
                    { 'componentName': 'task-status' },
                    { 'componentName': 'task-abnormal' },
                    { 'componentName': 'task-pending' },
                ]
            },
            // 业务主机
            queryIndexHostStatistic() {
                rbAutoOperationHomeServicesFactory
                    .queryIndexHostStatistic()
                    .then(res => {
                        this.hostStatistic = res
                    })
            },
            // 常用作业
            queryIndexPipelineStatistic() {
                rbAutoOperationHomeServicesFactory
                    .queryIndexPipelineStatistic()
                    .then(res => {
                        this.pipelineStatistic = res
                    })
            },
            // 近30天任务
            queryIndexRecent30DaysRunStatistic() {
                rbAutoOperationHomeServicesFactory
                    .queryIndexRecent30DaysRunStatistic()
                    .then(res => {
                        this.recent30DaysRunStatistic = res
                    })
            },
            // 定时任务
            queryIndexPipelineJobStatistic() {
                rbAutoOperationHomeServicesFactory
                    .queryIndexPipelineJobStatistic()
                    .then(res => {
                        this.pipelineJobStatistic = res
                    })
            },
            // 首页改版
            // 自动化对象数量
            queryNewIndexAgentStatistic() {
                rbAutoOperationHomeServicesFactory
                    .queryNewIndexAgentStatistic()
                    .then(res => {
                        this.panelDataList[0].count = res.total
                        this.panelDataList[0].alertCount = res.un_link
                    })
            },
            // 7日自动化任务
            queryNewIndexPipeInstStatistic7Days() {
                rbAutoOperationHomeServicesFactory
                    .queryNewIndexPipeInstStatistic7Days()
                    .then(res => {
                        this.panelDataList[1].count = res.total
                        this.panelDataList[1].failCount = res.fail
                    })
            },
            // 7日自动巡检任务
            queryNewIndexInspectionTaskStatistic7Days() {
                rbAutoOperationHomeServicesFactory
                    .queryNewIndexInspectionTaskStatistic7Days()
                    .then(res => {
                        this.panelDataList[2].count = res.total_num
                        this.panelDataList[2].failCount = res.failed_num
                    })
            },
            // 7日故障自愈任务
            queryNewIndexAutoRepairInstanceStatistic7Days() {
                rbAutoOperationHomeServicesFactory
                    .queryNewIndexAutoRepairInstanceStatistic7Days()
                    .then(res => {
                        this.panelDataList[3].count = res.total
                        this.panelDataList[3].failCount = res.fail
                    })
            },
            // 30日内所有任务
            queryNewIndexAllTasksStatistic30Days() {
                rbAutoOperationHomeServicesFactory
                    .queryNewIndexAllTasksStatistic30Days()
                    .then(res => {
                        this.panelDataList[4].count = res.total // 总数
                        this.panelDataList[4].successCount = res.success // 成功
                        this.panelDataList[4].failCount = res.fail // 失败
                        this.panelDataList[4].running = res.running // 运行中
                        this.panelDataList[4].alertCount = res.exception // 告警
                    })
            },
        },
    }
</script>
 
<style  lang="scss" scoped>
    @import "../assets/global.scss";
</style>
