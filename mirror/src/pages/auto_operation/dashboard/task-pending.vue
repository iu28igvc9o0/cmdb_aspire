<template>
    <div class="content-chart mtop15"
         style="width: 24.2%;">
        <section class="chart-title-wrap clearfix">
            <svg class="svg-icon svg-icon-24"
                 aria-hidden="true">
                <use xlink:href="#icongongdan"></use>
            </svg>
            <span class="chart-title">待处理任务</span>
        </section>
        <section class="chart-section">
            <div class="yw-rank-table-wrap">
                <el-table :data="dataList"
                          class="yw-rank-table"
                          height="100%"
                          element-loading-background="rgba(0, 0, 0, 0.8)"
                          v-loading="loading">
                    <el-table-column prop="task_name"
                                     show-overflow-tooltip
                                     label="任务名称"></el-table-column>
                    <el-table-column prop="task_type"
                                     label="审核类型"
                                     width="100">
                        <template slot-scope="scope">
                            <span v-if="scope.row.task_type === 'sensitive'">敏感指令审核</span>
                            <span v-else-if="scope.row.task_type === 'selfRepair'">故障自愈</span>
                        </template>
                    </el-table-column>
                    <el-table-column label="操作"
                                     width="60">
                        <template slot-scope="scope">
                            <svg class="svg-icon svg-icon-24 pointer orange"
                                 @click="gotoList(scope.row)"
                                 aria-hidden="true">
                                <use xlink:href="#iconzhihang"></use>
                            </svg>
                        </template>
                    </el-table-column>
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
    import rbAutoOperationHomeServicesFactory from 'src/services/auto_operation/rb-auto-operation-home-services.factory.js'
    import rbAutoOperationMixin from 'src/services/auto_operation/rb-auto-operation-mixin.js'
    export default {
        data() {
            return {
                dataList: []
            }
        },
        mixins: [rbAutoOperationMixin],
        created() {
            this.search()
        },
        methods: {
            // 待处理任务
            search() {
                let req = {
                    page_no: this.currentPage,
                    page_size: this.pageSize
                }
                this.loading = true
                rbAutoOperationHomeServicesFactory
                    .queryToBeProcessedTaskList(req)
                    .then(res => {
                        this.loading = false
                        this.total = res.totalCount
                        this.dataList = res.dataList
                    })
                    .catch(() => {
                        this.loading = false
                    })
            },
            gotoList(row) {
                // 敏感指令
                if (row.task_type === 'sensitive') {
                    this.$router.push({
                        path: '/auto_operation/instruction_manage/audit',
                        query: {
                            deal_type: 1
                        }
                    })
                    // 故障自愈
                } else if (row.task_type === 'selfRepair') {
                    this.$router.push({
                        path: '/auto_operation/healing_manage/log',
                        query: {
                            status: '6'
                        }
                    })
                }
            }
        }

    }
</script>

<style lang="scss" scoped>
</style>

