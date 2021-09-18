<template>

    <div class="content-chart"
         style="width: 24.2%;">
        <section class="chart-title-wrap clearfix">
            <svg class="svg-icon svg-icon-24"
                 aria-hidden="true">
                <use xlink:href="#iconkuaijiejian"></use>
            </svg>
            <span class="chart-title">场景快捷键</span>
        </section>

        <!-- 场景快捷键 -->
        <section class="chart-section">
            <div class="displaybox flex-wrap">
                <div class="text-block t-center pointer"
                     @click="gotoRunTask(item)"
                     v-for="item in dataList"
                     :key="item.id"
                     :style="`background-image: ${defaultOption.backgroundImage}`">
                    {{item.scenes_name}}
                </div>
            </div>
        </section>

        <run-work-dialog></run-work-dialog>
    </div>
</template>

<script>
    import runWorkDialog from '../work_manage/task/run-work-dialog.vue'
    import rbAutoOperationHomeServicesFactory from 'src/services/auto_operation/rb-auto-operation-home-services.factory.js'
    export default {
        data() {
            return {
                // 默认参数
                defaultOption: {
                    backgroundImage: 'url(' + require('src/assets/theme/dark/img/alert-border-bg.png') + ')',
                },
                dataList: []
            }
        },
        components: {
            runWorkDialog
        },
        created() {
            this.queryNormalScenesStatistic()
        },
        methods: {
            // 场景快捷键
            queryNormalScenesStatistic() {
                rbAutoOperationHomeServicesFactory
                    .queryNormalScenesStatistic()
                    .then(res => {
                        if (res.length) {
                            this.dataList = res
                        }
                    })
            },
            gotoRunTask(item) {
                this.$bus.emit('showRunWork', item.pipeline_id)
            }
        }

    }
</script>

<style lang="scss" scoped>
    .text-block {
        width: 45%;
        margin: rem(6) rem(10);
        padding: rem(21) rem(10);
        color: #ffffff;
        background-size: 100% 100%;
    }
</style>

