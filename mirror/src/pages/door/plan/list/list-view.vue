<template>
    <div class="bgwhite">
        <div class="yw-el-table-wrap">
            <el-table :data="dataList"
                      class="yw-el-table"
                      stripe
                      tooltip-effect="dark"
                      border
                      height="calc(100vh - 248px)"
                      @selection-change="handleSelectionChange"
                      v-loading="loading">
                <el-table-column type="index"
                                 label="序号"
                                 min-width="60"></el-table-column>
                <el-table-column prop="taskName"
                                 label="任务标题"
                                 min-width="140"
                                 show-overflow-tooltip>
                    <template slot-scope="scope">
                        <span class="blue pointer"
                              @click="viewTask(scope.row)">{{scope.row.taskName}}</span>
                        <span class="icon iconfont"
                              v-if="scope.row.messageCount">&#xe68d;</span>
                    </template>
                </el-table-column>
                <el-table-column prop="taskType"
                                 label="操作类型"
                                 width="70">
                    <template slot-scope="scope">
                        <span v-if="scope.row.taskType === '1'">割接</span>
                        <span v-if="scope.row.taskType === '2'">变更</span>
                    </template>
                </el-table-column>
                <el-table-column prop="idcType"
                                 label="资源池"
                                 width="110"></el-table-column>
                <el-table-column prop="taskStatus"
                                 label="状态"
                                 width="80">
                    <template slot-scope="scope">
                        <span v-if="scope.row.taskStatus === '1'"
                              class="status-marking bggrey">待执行</span>
                        <span v-else-if="scope.row.taskStatus === '2'"
                              class="status-marking">执行中</span>
                        <span v-else-if="(scope.row.taskStatus === '3' || scope.row.taskStatus === '4') && scope.row.taskResult === '1'"
                              class="status-marking bggreen">执行成功</span>
                        <span v-else-if="(scope.row.taskStatus === '3' || scope.row.taskStatus === '4') && scope.row.taskResult === '2'"
                              class="status-marking bgorange">执行失败</span>
                    </template>
                </el-table-column>
                <el-table-column prop="taskDescription"
                                 label="任务描述"
                                 width="160"
                                 show-overflow-tooltip></el-table-column>
                <el-table-column prop="taskStartTime"
                                 label="开始时间"
                                 width="140"></el-table-column>
                <el-table-column prop="taskEndTime"
                                 label="结束时间"
                                 width="140"></el-table-column>
                <el-table-column prop="orderId"
                                 label="工单号"
                                 width="280">
                    <template slot-scope="scope">
                        <span v-if="scope.row.taskStatus === '1'">--</span>
                        <span v-else>{{scope.row.orderId}}</span>
                    </template>
                </el-table-column>
            </el-table>
        </div>
        <div class="yw-page-wrap">
            <el-pagination @size-change="handleSizeChange"
                           @current-change="handleCurrentChange"
                           :current-page="currentPage"
                           :page-sizes="pageSizes"
                           :page-size="pageSize"
                           :total="total"
                           layout="total, sizes, prev, pager, next, jumper"></el-pagination>
        </div>
    </div>
</template>

<script>
    import rbAutoOperationMixin from 'src/services/auto_operation/rb-auto-operation-mixin.js'
    export default {
        name: 'DoorPlanListView',
        props: {
            dataList: {
                type: Array,
                default: []
            },
            total: {
                type: Number,
                default: 0
            },
            loading: {
                type: Boolean,
                default: false
            },
        },
        components: {
        },
        data() {
            return {
            }
        },
        mixins: [rbAutoOperationMixin],
        created() {
        },
        methods: {
            search() {
                let req = {
                    pageNum: this.currentPage,
                    pageSize: this.pageSize,
                }
                this.$emit('search', '3', req)
            },
            viewTask(row) {
                this.$emit('handleTaskClick', row)
            }
        }
    }
</script>

<style lang="scss" scoped>
</style>
