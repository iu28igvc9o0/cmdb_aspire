<template>
    <!-- 服务台： 我的工单 -->
    <div class="content-chart">
        <section class="chart-title-wrap clearfix">
            <span class="chart-title">我的工单</span>
            <div class="chart-filter">
                <el-button type="text"
                           @click="gotoMore">更多</el-button>
            </div>
        </section>
        <div class="yw-el-table-wrap">
            <div class="mtop20">
                <el-tabs v-model="activeName"
                         type="card"
                         @tab-click="handleTabClick">
                    <el-tab-pane :key="item.key"
                                 v-for="item in tabList"
                                 :label="item.name"
                                 :name="item.key"></el-tab-pane>
                </el-tabs>
            </div>
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
        <div class="yw-page-wrap">
            <el-pagination @size-change="handleSizeChange"
                           @current-change="handleCurrentChange"
                           :current-page="currentPage"
                           :page-sizes="pageSizes"
                           :page-size="pageSize"
                           :total="total"
                           small
                           layout="total, sizes, prev, pager, next, jumper"></el-pagination>
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
                activeName: 'first',
                tabList: [
                    {
                        name: '我发起的工单',
                        key: 'first'
                    },
                    {
                        name: '待办工单',
                        key: 'second'
                    },
                    {
                        name: '未关闭工单',
                        key: 'third'
                    },
                    {
                        name: '已关闭工单',
                        key: 'fourth'
                    },
                ],
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
            handleTabClick(tab) {
                console.log(tab)
            },
            // 更多我的工单
            gotoMore() {
                this.$router.push({
                    path: '/resource/flow',
                    query: {
                        routerHash: 'flow/todoList',
                        currentTitle: '我的工单'
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

