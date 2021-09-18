<template>
    <!-- 服务台： 割接公告 -->
    <div class="content-chart">
        <section class="chart-title-wrap clearfix">
            <span class="chart-title">割接公告</span>
            <div class="chart-filter">
                <el-button type="text"
                           @click="gotoMore">更多</el-button>
            </div>
        </section>
        <div class="yw-el-table-wrap">
            <el-table :data="historicalTaskList"
                      class="yw-el-table"
                      stripe
                      tooltip-effect="dark"
                      height="214"
                      v-loading="loading">
                <el-table-column prop="pipeline_instance_name"
                                 label="流程名称"></el-table-column>
                <el-table-column prop="operator"
                                 label="流程标题"
                                 width="120"></el-table-column>
                <el-table-column prop="status"
                                 label="发起时间"
                                 width="140"></el-table-column>
                <el-table-column prop="updater"
                                 label="到达时间"
                                 width="140"></el-table-column>
            </el-table>
        </div>
    </div>
</template>

<script>
    import rbAutoOperationHomeServicesFactory from 'src/services/auto_operation/rb-auto-operation-home-services.factory.js'
    import rbAutoOperationMixin from 'src/services/auto_operation/rb-auto-operation-mixin.js'

    export default {
        components: {
        },
        data() {
            return {
                historicalTaskList: [], // 最近任务执行记录
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
                        this.historicalTaskList = res.dataList
                    })
                    .catch(() => {
                        this.loading = false
                    })
            },
            // 割接公告
            gotoMore() {
                this.$router.push({
                    path: '/resource/flow',
                    query: {
                        routerHash: 'flow/retrieve',
                        currentTitle: '割接公告',
                        procDefKey: 'bggllcnew',
                    }
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

