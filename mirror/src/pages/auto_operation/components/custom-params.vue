<template>
    <div>
        <el-button type="primary"
                   @click="showDialog">
            自定义参数
        </el-button>
        <!-- 已选中列表 -->
        <div class="yw-el-table-wrap">
            <el-table :data="selectedDataList"
                      class="yw-el-table"
                      stripe
                      tooltip-effect="dark"
                      border
                      v-loading="loading">
                <el-table-column prop="param_code"
                                 label="参数编码"></el-table-column>
                <el-table-column prop="param_name"
                                 label="参数名称"></el-table-column>
                <el-table-column prop="param_type"
                                 label="参数类型">
                    <template slot-scope="scope">
                        <span v-if="scope.row.param_type === '1'">常量</span>
                        <span v-if="scope.row.param_type === '2'">密码变量</span>
                    </template>
                </el-table-column>
                <el-table-column label="操作"
                                 width="60">
                    <template slot-scope="scope">
                        <div class="yw-table-operator">
                            <el-button type="text"
                                       title="删除"
                                       icon="el-icon-delete"
                                       @click="removeRow(scope.$index)"></el-button>
                        </div>
                    </template>
                </el-table-column>
            </el-table>
        </div>
        <!-- 待选择数据列表 -->
        <el-dialog class="yw-dialog"
                   title="选择参数"
                   :visible.sync="dialogShow"
                   :append-to-body="true"
                   width="700px"
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
                          @select="handleSelectionChange"
                          @select-all="handleSelectionChange"
                          @selection-change="selectRow">
                    <el-table-column type="selection"></el-table-column>
                    <el-table-column prop="param_code"
                                     label="参数编码"></el-table-column>
                    <el-table-column prop="param_name"
                                     label="参数名称"></el-table-column>
                    <el-table-column prop="param_type"
                                     label="参数类型">
                        <template slot-scope="scope">
                            <span v-if="scope.row.param_type === '1'">常量</span>
                            <span v-if="scope.row.param_type === '2'">密码变量</span>
                        </template>
                    </el-table-column>
                </el-table>
                <!-- <div class="yw-page-wrap">
                    <el-pagination @size-change="handleSizeChange"
                                   @current-change="handleCurrentChange"
                                   :current-page="currentPage"
                                   :page-sizes="pageSizes"
                                   :page-size="pageSize"
                                   :total="total"
                                   layout="total, sizes, prev, pager, next, jumper"></el-pagination>
                </div> -->
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
    import rbAutoOperationServicesFactory from 'src/services/auto_operation/rb-auto-operation-services.factory.js'
    export default {
        name: 'CustomParamsComponent',
        components: {},
        props: ['paramCode'],
        data() {
            return {
                dialogShow: false,
                dataList: [],
            }
        },
        mixins: [rbAutoOperationMixin, rbAutoOperationListChooseMixin],
        created() {
            this.search(1)
        },
        methods: {
            // 查询列表
            search(pageNum) {
                this.currentPage = pageNum ? pageNum : this.currentPage
                let req = {
                    page_no: this.currentPage,
                    page_size: this.pageSize
                }
                this.initPage = true
                this.loading = true
                this.dataList = []
                rbAutoOperationServicesFactory
                    .queryCustomParamsList(req)
                    .then(res => {
                        this.loading = false
                        this.dataList = res
                    })
                    .catch(error => {
                        this.loading = false
                        this.showErrorTip(error)
                    })
            },
        }
    }
</script>
