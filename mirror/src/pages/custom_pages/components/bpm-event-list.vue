<template>
    <!-- 服务台： 事件信息 -->
    <div class="content-chart">
        <section class="chart-title-wrap clearfix">
            <span class="chart-title">事件信息</span>
            <div class="chart-filter">
                <el-button type="text">新建</el-button>
                <el-button type="text">派单</el-button>
                <el-button type="text">更多</el-button>
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
                <el-table-column type="selection"
                                 width="50"></el-table-column>
                <el-table-column prop="pipeline_instance_name"
                                 label="事件描述"
                                 show-overflow-tooltip
                                 min-width="200"></el-table-column>
                <el-table-column prop="operator"
                                 label="客户名称"
                                 width="120"></el-table-column>
                <el-table-column prop="operator"
                                 label="联系电话"
                                 width="120"></el-table-column>
                <el-table-column prop="operator"
                                 label="事件来源"
                                 width="120"></el-table-column>
                <el-table-column prop="operator"
                                 label="创建时间"
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
    import rbBpmHomeServices from 'src/services/bpm/rb-bpm-home-services.js'
    import rbAutoOperationMixin from 'src/services/auto_operation/rb-auto-operation-mixin.js'

    export default {
        components: {
        },
        data() {
            return {
                activeName: 'first',
                tabList: [
                    {
                        name: '告警工单',
                        key: 'first'
                    },
                    {
                        name: '其他工单',
                        key: 'second'
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
                rbBpmHomeServices
                    .getUserDraftList(req)
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

        },
        mounted() {
            this.getDatas()
        }

    }
</script>

<style lang="scss" scoped>
</style>

