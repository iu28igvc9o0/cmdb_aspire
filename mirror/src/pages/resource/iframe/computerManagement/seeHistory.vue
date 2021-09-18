<template>
    <div>
        <dialog-model ref="dialogModel"
                      :dialogAttribute="dialogAttribute"
                      @handleSureDialog="handleSureDialog"
                      @handleCancelDialog="handleCancelDialog">
            <template slot="content">
                <div class="yw-table">
                    <div class="yw-el-table-wrap"
                         v-loading="loading"
                         style="margin-top:10px;">
                        <el-table class="yw-el-table"
                                  :data="collectionData.tableBody"
                                  style="width:100%;cursor: pointer;"
                                  stripe
                                  border
                                  tooltip-effect="dark"
                                  height="calc(100vh - 300px)">
                            <el-table-column v-for="(item,index) in collectionData.tableHeader"
                                             :prop="item.value"
                                             :sortable="item.sortable"
                                             :label="item.label"
                                             :width="item.width"
                                             :show-overflow-tooltip="true"
                                             :key="index"> </el-table-column>
                        </el-table>
                    </div>
                    <div class="yw-page-wrap"
                         v-if="collectionData.pagination.total>0">
                        <el-pagination @size-change="handleSizeChange"
                                       @current-change="handleCurrentChange"
                                       :current-page="collectionData.pagination.currentPage"
                                       :page-sizes="collectionData.pageSizes"
                                       :page-size="collectionData.pagination.pageSize"
                                       :total="collectionData.pagination.total"
                                       layout="total, sizes, prev, pager, next, jumper">
                        </el-pagination>
                    </div>
                </div>
            </template>
        </dialog-model>
    </div>
</template>
<script>
    import rbCmdbServiceFactory from 'src/services/cmdb/rb-cmdb-service.factory.js'
    export default {
        components: {
            DialogModel: () => import('src/pages/resource/iframe/computerManagement/modelDialog.vue')
        },
        props: {
            historyParams: {
                type: String,
                default: ''
            }
        },
        data() {
            return {
                dialogAttribute: {
                    dialogWidth: '1200px',
                    dialogTitle: '网络线路历史数据',
                    dialogSure: '保存',
                    dialogCancel: '取消'
                },
                collectionData: {
                    tableHeader: [
                        {
                            label: '变更时间',
                            value: 'update_time',
                            sortable: false,
                            width: '140'
                        },
                        {
                            label: '资源池',
                            value: 'idcType_dict_note_name',
                            sortable: false,
                            width: '140'
                        },
                        {
                            label: '网络类型',
                            value: 'server_network_type_dict_note_name',
                            sortable: false
                        },
                        {
                            label: '计费方式',
                            value: 'server_bill_type_dict_note_name',
                            sortable: false
                        },
                        {
                            label: '单位',
                            value: 'server_bill_unit_dict_note_name',
                            sortable: false
                        },
                        {
                            label: '数量',
                            value: 'server_line_count',
                            sortable: false
                        },
                        {
                            label: '规格',
                            value: 'server_line_standard',
                            sortable: false
                        },
                        {
                            label: '线路名称',
                            value: 'server_line',
                            sortable: false
                        },
                        {
                            label: 'A端',
                            value: 'server_line_a',
                            sortable: false
                        },
                        {
                            label: 'Z端',
                            value: 'server_line_b',
                            sortable: false
                        }
                    ],
                    tableBody: [],
                    pagination: {
                        currentPage: 1,
                        pageSize: 20,
                        total: 0
                    },
                    pageSizes: [10, 20, 50, 100]
                },
                loading: false
            }
        },
        created() {
            this.getTableList()
        },
        methods: {
            async getTableList(type) {
                this.loading = true
                let queryData = {}
                if (type) {
                    this.collectionData.pagination.currentPage = 1
                }
                queryData.condicationCode = 'network_line_history_search'
                queryData.network_line_id = this.historyParams
                queryData.pageSize = this.collectionData.pagination.pageSize
                queryData.pageNo = this.collectionData.pagination.currentPage
                this.collectionData.tableBody = []
                this.collectionData.pagination.total = 0
                try {
                    let res = await rbCmdbServiceFactory.getInstanceList(queryData)
                    this.collectionData.tableBody = res.data || []
                    this.collectionData.pagination.total = res.totalSize
                } catch (error) {
                    this.$message({
                        message: error.data.errors[0].message,
                        type: 'error'
                    })
                } finally {
                    this.loading = false
                }
            },
            // 分页改变尺寸
            handleSizeChange(val) {
                this.collectionData.pagination.pageSize = val
                this.getTableList('find')
            },
            // 分页改变当前页
            handleCurrentChange(val) {
                this.collectionData.pagination.currentPage = val
                this.getTableList()
            },
            handleSureDialog(status) {
                this.$emit('closeHistoryDialog', status)
            },
            handleCancelDialog(status) {
                this.$emit('closeHistoryDialog', status)
            }
        }
    }
</script>
<style lang="scss" scoped>
    .yw-dialog /deep/ .yw-dialog-main {
        overflow-y: hidden;
    }
</style>