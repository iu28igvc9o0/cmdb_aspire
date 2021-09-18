<template>

    <div class="content-chart"
         style="width: 24.2%;">
        <section class="chart-title-wrap clearfix">
            <svg class="svg-icon svg-icon-24"
                 aria-hidden="true">
                <use xlink:href="#iconrenwu"></use>
            </svg>
            <span class="chart-title">当日任务执行状态</span>
        </section>

        <!-- 当日任务执行状态 -->
        <section class="chart-section">
            <div class="displaybox flex-wrap">
                <div class="text-block"
                     v-for="item in dataList"
                     :key="item.id"
                     :style="`background-image: ${defaultOption.backgroundImage}`">
                    <div class="f16"
                         style="color:#CAF4FF;">{{item.name}}</div>
                    <div class="orange f24">{{item.value | fixedNumberPointTwo}} {{item.unit}}</div>
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
                    { name: '单日任务成功率', key: 'task_success_rate', unit: '%', value: '' },
                    { name: '当前执行设备数量', key: 'device_num', unit: '台', value: '' },
                    { name: '执行平均任务耗时', key: 'task_average_time', unit: 's', value: '' },
                    { name: '执行最长任务耗时', key: 'task_max_time', unit: 's', value: '' },
                ]
            }
        },
        created() {
            this.queryTodayOpsTaskStatusStatistic()
        },
        methods: {
            // 今日任务
            queryTodayOpsTaskStatusStatistic() {
                rbAutoOperationHomeServicesFactory
                    .queryTodayOpsTaskStatusStatistic()
                    .then(res => {
                        this.dataList.forEach(item => {
                            if (item.key === 'task_success_rate') {
                                item.value = res[item.key] * 100 || 0
                            } else {
                                item.value = res[item.key] || 0
                            }
                        })
                    })
            },
        }

    }
</script>

<style lang="scss" scoped>
    .text-block {
        width: 47%;
        margin: rem(6) rem(5);
        padding: rem(24) rem(10);
        color: #ffffff;
        background-size: 100% 100%;
    }
</style>

