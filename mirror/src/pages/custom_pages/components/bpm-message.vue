<template>
    <!-- 服务台： 消息 -->
    <div class="content-chart">
        <section class="chart-title-wrap clearfix">
            <span class="chart-title">消息</span>
        </section>
        <div class="yw-el-table-wrap chart-section">
            <vueSeamlessScroll :data="dataList"
                               :class-option="defaultOption"
                               class="seamless-warp mleft10">
                <h3 class="medium"
                    v-for="item in dataList"
                    :key="item.pipeline_instance_id">{{ item.pipeline_instance_name }}</h3>
            </vueSeamlessScroll>
        </div>
    </div>
</template>

<script>
    import vueSeamlessScroll from 'vue-seamless-scroll'
    import rbAutoOperationHomeServicesFactory from 'src/services/auto_operation/rb-auto-operation-home-services.factory.js'
    import rbAutoOperationMixin from 'src/services/auto_operation/rb-auto-operation-mixin.js'

    export default {
        components: {
            vueSeamlessScroll
        },
        data() {
            return {
                dataList: [], // 最近任务执行记录
            }
        },
        computed: {
            // 滚动自定义
            defaultOption() {
                return {
                    step: 0.5, // 数值越大速度滚动越快
                    limitMoveNum: this.dataList.length, // 开始无缝滚动的数据量 this.dataList.length
                    hoverStop: true, // 是否开启鼠标悬停stop
                    direction: 1, // 0向下 1向上 2向左 3向右
                    openWatch: true, // 开启数据实时监控刷新dom
                    singleHeight: 0, // 单步运动停止的高度(默认值0是无缝不停止的滚动) direction => 0/1
                    singleWidth: 0, // 单步运动停止的宽度(默认值0是无缝不停止的滚动) direction => 2/3
                    waitTime: 20 // 单步运动停止的时间(默认值1000ms)
                }
            }
        },
        mixins: [rbAutoOperationMixin],
        methods: {
            // 获得数据
            getDatas() {
                let req = {
                    page_no: this.currentPage,
                    page_size: this.pageSize
                }
                this.loading = true
                rbAutoOperationHomeServicesFactory
                    .queryOpsPipelineInstanceList(req)
                    .then(res => {
                        this.loading = false
                        this.total = res.totalCount
                        this.dataList = res.dataList
                    })
                    .catch(() => {
                        this.loading = false
                    })
            },

        },
        mounted() {
            this.getDatas()
        }

    }
</script>

<style lang="scss" scoped>
</style>

