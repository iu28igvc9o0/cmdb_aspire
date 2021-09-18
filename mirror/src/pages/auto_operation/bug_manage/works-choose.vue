<template>
    <div>
        <el-button type="primary"
                   @click="showDialog">
            {{workType}}
        </el-button>
        <!-- 已选中列表 -->
        <div class="yw-el-table-wrap mtop10">
            <el-table :data="selectedDataList"
                      class="yw-el-table"
                      stripe
                      tooltip-effect="dark"
                      border
                      height="300"
                      v-loading="loading">
                <template v-for="(column, index) in tableColumns">
                    <el-table-column :key="index + new Date().getTime()"
                                     :prop="column.prop"
                                     :label="column.label"
                                     :width="column.width"
                                     :min-width="column.minWidth"
                                     :show-overflow-tooltip="column.showTooltip"></el-table-column>
                </template>
                <el-table-column label="操作"
                                 width="60">
                    <template slot-scope="scope">
                        <el-button type="text"
                                   title="删除"
                                   icon="el-icon-delete"
                                   @click="removeRow(scope.$index)"></el-button>
                    </template>
                </el-table-column>
            </el-table>
        </div>
        <!-- 待选择数据列表 -->
        <el-dialog class="yw-dialog"
                   title="选择作业"
                   :visible.sync="dialogShow"
                   :append-to-body="true"
                   width="90%"
                   height="300"
                   :close-on-click-modal="false">
            <section class="yw-dialog-main no-scroll">
                <el-table ref="dataTable"
                          :data="dataList"
                          class="yw-el-table"
                          stripe
                          tooltip-effect="dark"
                          border
                          height="300"
                          v-loading="loading"
                          @selection-change="handleSelectionChange">
                    <el-table-column type="selection"></el-table-column>
                    <template v-for="(column, index) in tableColumns">
                        <el-table-column :key="index + new Date().getTime()"
                                         :prop="column.prop"
                                         :label="column.label"
                                         :width="column.width"
                                         :min-width="column.minWidth"
                                         :show-overflow-tooltip="column.showTooltip"></el-table-column>
                    </template>
                </el-table>
                <div class="yw-page-wrap">
                    <el-pagination @size-change="handleSizeChange"
                                   @current-change="handleCurrentChange"
                                   :current-page="currentPage"
                                   :page-sizes="pageSizes"
                                   :page-size="pageSize"
                                   :total="originWorkData.totalCount"
                                   layout="total, sizes, prev, pager, next, jumper"></el-pagination>
                </div>
            </section>
            <section class="btn-wrap">
                <el-button type="primary"
                           @click="addToSelectedList">添加
                </el-button>
                <el-button @click="hideDialog">取消</el-button>
            </section>
        </el-dialog>
    </div>
</template>

<script>
    import rbAutoOperationMixin from 'src/services/auto_operation/rb-auto-operation-mixin.js'
    import rbAutoOperationListChooseMixin from 'src/services/auto_operation/rb-auto-operation-list-choose-mixin.js'
    export default {
        name: 'WorksChooseComponent',
        components: {},
        props: {
            currentRowData: {
                type: Object,
                default() {
                    return {}
                }
            },
            originWorkData: {
                type: Object,
                default() {
                    return {}
                }
            },
            workType: {
                type: String,
                default() {
                    return '选择修复作业'
                }
            },
            // 接口返回已选中的列表
            propKey: {
                type: String,
                default() {
                    return 'pipelineIdList'
                }
            },
        },
        data() {
            return {
                dialogShow: false,
                dataList: [],
                curKey: 'pipeline_id',  // 比对是否选中该行的key
                tableColumns: [
                    {
                        prop: 'pipeline_name',
                        label: '作业名称',
                        width: '160',
                        minWidth: '100',
                        showTooltip: true,
                    },
                    {
                        prop: 'step_count',
                        label: '步骤数',
                        width: '80',
                    },
                    {
                        prop: 'creater',
                        label: '创建人',
                        width: '80',
                    },
                    {
                        prop: 'updater',
                        label: '最后修改人',
                        width: '90',
                    },
                    {
                        prop: 'create_time',
                        label: '创建时间',
                        width: '140',
                    },
                    {
                        prop: 'update_time',
                        label: '修改时间',
                        width: '140',
                    },
                    {
                        prop: 'pipeline_id',
                        label: 'ID',
                        width: '60',
                    },
                    {
                        prop: 'label_id',
                        label: '分类',
                        width: '140',
                    },
                ]
            }
        },
        watch: {
            // 更新已选中行，接口提交
            selectedDataList(val) {
                let pipelineIds = []
                val.forEach(item => {
                    pipelineIds.push(item[this.curKey])
                })
                this.$emit('setSelectedKey', pipelineIds)
            },
            currentRowData: {
                handler(newVal) {
                    this.curPropKey = this.propKey
                    this[this.curPropKey] = newVal[this.curPropKey]
                },
                deep: true,
                immediate: true
            },
            originWorkData: {
                handler(newVal) {
                    this.dataList = newVal.dataList || []
                },
                deep: true,
                immediate: true,
            },
        },
        mixins: [rbAutoOperationMixin, rbAutoOperationListChooseMixin],
        created() {

        },
        methods: {
        }
    }
</script>
