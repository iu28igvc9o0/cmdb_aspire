<template>

    <div class="content-chart"
         style="width: 24.2%;">
        <section class="chart-title-wrap clearfix">
            <svg class="svg-icon svg-icon-24"
                 aria-hidden="true">
                <use xlink:href="#iconrenwu"></use>
            </svg>
            <span class="chart-title">今日任务</span>
        </section>

        <!-- 今日任务 -->
        <section class="chart-section">
            <div class="displaybox flex-wrap">
                <div class="text-block"
                     v-for="item in dataList"
                     :key="item.key"
                     :style="`background-image: ${defaultOption.backgroundImage}`">
                    <div class="f16"
                         style="color:#CAF4FF;">{{item.name}}</div>
                    <div class="orange f24">{{item.value}}</div>
                </div>
            </div>
        </section>
    </div>
</template>

<script>
    import rbAutoOperationHomeServicesFactory from 'src/services/auto_operation/rb-auto-operation-home-services.factory.js'
    export default {
        data() {
            return {
                // 默认参数
                defaultOption: {
                    backgroundImage: 'url(' + require('src/assets/theme/dark/img/alert-border-bg.png') + ')',
                },
                dataList: [
                    { name: '手工任务', key: 'handle_ops_task_num', value: '' },
                    { name: '自动任务', key: 'auto_ops_task_num', value: '' },
                    { name: '巡检任务', key: 'inspection_task_num', value: '' },
                    { name: '故障自愈任务', key: 'auto_repair_task_num', value: '' },
                    { name: '审核任务', key: 'audit_task_num', value: '' },
                    { name: '失败任务', key: 'failed_task_num', value: '' },
                ]
            }
        },
        created() {
            this.queryTodayOpsTaskStatistic()
        },
        methods: {
            // 今日任务
            queryTodayOpsTaskStatistic() {
                rbAutoOperationHomeServicesFactory
                    .queryTodayOpsTaskStatistic()
                    .then(res => {
                        this.dataList.forEach(item => {
                            item.value = res[item.key]
                        })
                    })
            },
        }

    }
</script>

<style lang="scss" scoped>
    .text-block {
        width: 30%;
        margin: rem(6) rem(5);
        padding: rem(24) rem(10);
        color: #ffffff;
        background-size: 100% 100%;
    }
</style>

