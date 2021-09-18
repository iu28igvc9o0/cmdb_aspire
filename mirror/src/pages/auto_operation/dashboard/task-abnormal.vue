<template>
    <div class="content-chart mtop15"
         style="width: 74.8%;">
        <section class="chart-title-wrap clearfix">
            <svg class="svg-icon svg-icon-24"
                 aria-hidden="true">
                <use xlink:href="#icongaojing"></use>
            </svg>
            <span class="chart-title">任务执行异常列表</span>
            <div class="chart-filter">
                <el-button type="text"
                           @click="getMore">更多</el-button>
            </div>
        </section>
        <section class="chart-section">
            <div class="yw-rank-table-wrap">
                <el-table :data="dataList"
                          class="yw-rank-table"
                          height="100%"
                          element-loading-background="rgba(0, 0, 0, 0.8)"
                          v-loading="loading">
                    <el-table-column prop="pipeline_instance_name"
                                     show-overflow-tooltip
                                     label="任务名称"></el-table-column>
                    <el-table-column prop="operator"
                                     label="启动人"></el-table-column>
                    <el-table-column prop="status"
                                     label="任务状态">
                        <!-- 状态  9 成功   101 执行失败  102 执行超时 -->
                        <template slot-scope="scope">
                            <status-list :status="scope.row.status"></status-list>
                        </template>
                    </el-table-column>
                    <el-table-column prop="total_time"
                                     label="总耗时(s)"
                                     width="120"></el-table-column>
                </el-table>
            </div>
            <div class="yw-rank-table-pagination">
                <el-pagination @size-change="handleSizeChange"
                               @current-change="handleCurrentChange"
                               :current-page="currentPage"
                               :page-sizes="pageSizes"
                               :page-size="pageSize"
                               :total="total"
                               small
                               layout="total, prev, pager, next, jumper"></el-pagination>
            </div>
        </section>
    </div>
</template>

<script>
    import statusList from '../components/status-list'
    import rbAutoOperationHomeServicesFactory from 'src/services/auto_operation/rb-auto-operation-home-services.factory.js'
    import rbAutoOperationMixin from 'src/services/auto_operation/rb-auto-operation-mixin.js'
    export default {
        data() {
            return {
                dataList: []
            }
        },
        components: {
            statusList
        },
        mixins: [rbAutoOperationMixin],
        methods: {
            // 任务执行异常列表
            search() {
                let req = {
                    page_no: this.currentPage,
                    page_size: this.pageSize
                }
                this.loading = true
                rbAutoOperationHomeServicesFactory
                    .queryNewIndexExceptionPipeInstPageList(req)
                    .then(res => {
                        this.loading = false
                        this.total = res.totalCount
                        this.dataList = res.dataList
                    })
                    .catch(() => {
                        this.loading = false
                    })
            },
            getMore() {
                this.$router.push({
                    path: '/auto_operation/code_manage/run_history'
                })
            }
        },
        created() {
            this.search()
        }

    }
</script>

<style lang="scss" scoped>
</style>

