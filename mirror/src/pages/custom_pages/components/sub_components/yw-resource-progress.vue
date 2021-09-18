<template>
    <div>
        <!-- 计算资源进度条：单行进度条（标签在外面） -->
        <section class="yw-progress light-blue">
            <!-- <div class="flex-between mtop2 p010 ">
                <div>本月申请(台)：0</div>
                <div>本月回收(台)：0</div>
            </div> -->
            <div class="box-progress">
                <el-progress :percentage="handleRate(data.un_install_count, data.total_count)"
                             :show-text="false"
                             :stroke-width="4"></el-progress>
                <div class="flex-between mtop5">
                    <div>未部署采集点数量</div>
                    <div>{{handleRate(data.un_install_count, data.total_count)}}%, {{data.un_install_count || 0}}个</div>
                </div>
            </div>
        </section>
    </div>
</template>
<script>
    export default {
        props: {
            data: {
                type: Object,
                default() {
                    return {}
                }
            },
        },
        data() {
            return {
            }
        },
        methods: {
            handleRate(num01 = 0, num02 = 0) {
                if (!num01 && !num02) {
                    return 0
                }
                return (num01 / num02).toFixed(4) * 100
            }
        },
        mounted() {
        }

    }
</script>
<style lang="scss" scoped>
    .yw-progress {
        line-height: 90%;
        margin-bottom: 8px;
        .box-progress {
            border: 1px solid $color-link;
            margin-top: 5px;
            padding: 2px 10px 5px;
        }
    }
</style>
