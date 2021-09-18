<template>
    <div class="components-container"
         v-loading="loading"
         :element-loading-text="loading_text">
        <el-form class="components-condition yw-form query_configuration"
                 style="margin:0px 0px"
                 :inline="true"
                 label-width="60px"
                 v-model="query">
            <el-form-item label="码表编码">
                <el-input clearable
                          v-model="query.codeCode"
                          placeholder="请输入编码"></el-input>
            </el-form-item>
            <el-form-item label="码表名称">
                <el-input clearable
                          v-model="query.codeName"
                          placeholder="请输入码表名称"></el-input>
            </el-form-item>
            <el-form-item label="模型分组">
                <el-select clearable
                           v-model="query.moduleCatalogId"
                           placeholder="请选择字段分组"
                           filterable>
                    <el-option v-for="item in ControlTypesList"
                               :key="item.value"
                               :label="item.value"
                               :value="item.label"></el-option>
                </el-select>
            </el-form-item>
            <section class="btn-wrap">
                <el-button type="primary"
                           @click="queryCodeList()">查询</el-button>
            </section>
        </el-form>
        <el-form class="yw-form query_configuration"
                 style="margin:10px 0px">
            <el-form class="table_Button">
                <el-button class="btn-icons-wrap"
                           type="primary"
                           @click="showCreate">新增</el-button>
            </el-form>
            <div class="yw-el-table-wrap">
                <el-table :data="codeList"
                          row-key="codeId"
                          ref="table"
                          class="yw-el-table"
                          stripe
                          tooltip-effect="dark"
                          @selection-change="handleSelectionChange"
                          border
                          height="calc(100vh - 270px)">
                    <el-table-column type="selection"
                                     min-width="20px"
                                     align="center"
                                     :selectable="selectable"></el-table-column>
                    <el-table-column prop="filedCode"
                                     label="码表编码"
                                     sortable
                                     min-width="120px"
                                     fixed
                                     :show-overflow-tooltip="true"></el-table-column>
                    <el-table-column prop="filedName"
                                     label="码表名称"
                                     sortable
                                     min-width="120px"
                                     fixed
                                     :show-overflow-tooltip="true"></el-table-column>
                    <el-table-column prop="catalogName"
                                     label="模型分组"
                                     sortable
                                     min-width="100px"
                                     :show-overflow-tooltip="true">
                        <template slot-scope="scope">
                            {{ scope.row.catalog != null?scope.row.catalog.catalogName:''}}
                        </template>
                    </el-table-column>
                    <el-table-column prop="controlName"
                                     sortable
                                     label="控件类型"
                                     min-width="100px"
                                     :show-overflow-tooltip="true">
                        <template slot-scope="scope">
                            {{ scope.row.controlType !=null?scope.row.controlType.controlName:''}}
                        </template>
                    </el-table-column>
                    <el-table-column prop="bindSourceType"
                                     sortable
                                     label="数据源类型"
                                     min-width="120px"
                                     :show-overflow-tooltip="true">
                        <template slot-scope="scope">
                            {{scope.row.codeBindSource != null?scope.row.codeBindSource.bindSourceType:''}}
                        </template>
                    </el-table-column>
                    <el-table-column prop="isBindSource"
                                     sortable
                                     label="是否绑定数据源"
                                     min-width="150px"
                                     :show-overflow-tooltip="true">
                        <template slot-scope="scope">
                            {{scope.row.isBindSource}}
                        </template>
                    </el-table-column>

                    <el-table-column prop="dictSource"
                                     sortable
                                     label="数据字典类型"
                                     min-width="120px"
                                     :show-overflow-tooltip="true">
                        <template slot-scope="scope">
                            {{ scope.row.codeBindSource == null ? '': scope.row.codeBindSource.dictSource}}
                        </template>
                    </el-table-column>
                    <el-table-column prop="tableSql"
                                     sortable
                                     label="SQL配置"
                                     min-width="120px"
                                     :show-overflow-tooltip="true">
                        <template slot-scope="scope">
                            {{ scope.row.codeBindSource == null ? '': scope.row.codeBindSource.tableSql}}
                        </template>
                    </el-table-column>
                    <el-table-column prop="refModuleName"
                                     sortable
                                     label="引用模型名称"
                                     min-width="120px"
                                     :show-overflow-tooltip="true">
                        <template slot-scope="scope">
                            {{ scope.row.codeBindSource == null ? '': scope.row.codeBindSource.refModuleName}}
                        </template>
                    </el-table-column>
                    <el-table-column prop="showModuleCodeName"
                                     sortable
                                     label="显示模型字段"
                                     min-width="120px"
                                     :show-overflow-tooltip="true">
                        <template slot-scope="scope">
                            {{ scope.row.codeBindSource == null ? '': scope.row.codeBindSource.showModuleCodeName}}
                        </template>
                    </el-table-column>
                    <el-table-column prop="isValidate"
                                     sortable
                                     label="是否验证"
                                     min-width="120px"
                                     :show-overflow-tooltip="true">
                        <template slot-scope="scope">
                            {{ scope.row.isValidate}}
                        </template>
                    </el-table-column>
                    <el-table-column prop="isApprove"
                                     sortable
                                     label="是否审核"
                                     min-width="120px"
                                     :show-overflow-tooltip="true">
                        <template slot-scope="scope">
                            {{ scope.row.isApprove}}
                        </template>
                    </el-table-column>
                    <el-table-column prop="approveType"
                                     sortable
                                     label="审核规则"
                                     min-width="120px"
                                     :show-overflow-tooltip="true">
                        <template slot-scope="scope">
                            {{ scope.row.approve == null ? '': scope.row.approve.approveType}}
                        </template>
                    </el-table-column>
                    <el-table-column prop="isCollect"
                                     sortable
                                     label="是否采集"
                                     min-width="120px"
                                     :show-overflow-tooltip="true">
                        <template slot-scope="scope">
                            {{ scope.row.isCollect}}
                        </template>
                    </el-table-column>
                    <el-table-column prop="collectType"
                                     sortable
                                     label="采集方式"
                                     min-width="120px"
                                     :show-overflow-tooltip="true">
                        <template slot-scope="scope">
                            {{ scope.row.codeCollect == null ? '': scope.row.codeCollect.collectType}}
                        </template>
                    </el-table-column>
                    <el-table-column prop="collectScriptId"
                                     sortable
                                     label="采集脚本"
                                     min-width="120px"
                                     :show-overflow-tooltip="true">
                        <template slot-scope="scope">
                            {{ scope.row.codeCollect == null ? '': scope.row.codeCollect.collectScriptId}}
                        </template>
                    </el-table-column>
                    <el-table-column prop="collectMapperKey"
                                     sortable
                                     label="映射KEY值"
                                     min-width="120px"
                                     :show-overflow-tooltip="true">
                        <template slot-scope="scope">
                            {{ scope.row.codeCollect == null ? '': scope.row.codeCollect.collectMapperKey}}
                        </template>
                    </el-table-column>
                    <el-table-column prop="collectFrequency"
                                     sortable
                                     label="采集频率"
                                     min-width="120px"
                                     :show-overflow-tooltip="true">
                        <template slot-scope="scope">
                            {{ scope.row.codeCollect == null ? '': scope.row.codeCollect.collectFrequency}}
                        </template>
                    </el-table-column>
                    <el-table-column prop="collectMapperKey"
                                     label="操作"
                                     min-width="80px"
                                     fixed="right">
                        <template slot-scope="scope">
                            <div class="yw-table-operator">
                                <el-button type="text"
                                           title="修改"
                                           icon="el-icon-edit"
                                           @click="editCode(scope.row)"></el-button>
                                <el-button type="text"
                                           v-if="scope.row.isBuiltIn !== '是'"
                                           title="删除"
                                           icon="el-icon-delete"
                                           @click="deleteCode(scope.row)"></el-button>
                            </div>
                        </template>
                    </el-table-column>
                </el-table>
            </div>
            <div class="yw-page-wrap">
                <YwPagination @handleSizeChange="handleSizeChange"
                              @handleCurrentChange="handleCurrentChange"
                              :current-page="query.currentPage"
                              :page-sizes="pageSizes"
                              :page-size="query.pageSize"
                              :total="total"></YwPagination>
            </div>
        </el-form>

        <code-edit v-if="edit.display"
                   :display="edit.display"
                   @setEditDisplay="setEditDisplay"
                   :codeId="edit.codeId"
                   :datasList="msgList"
                   @showLoading="showLoading"
                   @hideLoading="hideLoading"></code-edit>
    </div>
</template>

<script>
    import cmdbDictService from 'src/services/cmdb/rb-configDict-service.factory'
    import tableManagement from 'src/services/condition_management/table_management.js'
    export default {
        components: {
            YwPagination: () => import('src/components/common/yw-pagination.vue'),
            codeEdit: () => import('../table_management/edit')
        },
        data() {
            return {
                msgList: {},
                // 查询条件
                query: {
                    codeCode: '',
                    codeName: '',
                    moduleCatalogId: '',
                    currentPage: 1,
                    pageSize: 50
                },
                ControlTypesList: [], // 模型分组
                pageSizes: [10, 20, 50, 100], // 每页多少行数组
                total: 0, // 总共多少行数据
                // 弹窗参数
                edit: {
                    display: false,
                    codeId: ''
                },
                selectCode: [],
                loading: false,
                loading_text: '正在加载数据...',
                codeList: [],
                dictList: {}
            }
        },
        mounted() {
            this.getDistinctDictType()
            this.ControlTypes()
        },
        watch: {},
        methods: {
            showLoading() {
                this.loading = true
            },
            hideLoading() {
                this.loading = false
            },
            setEditDisplay(val) {
                this.edit.display = val
                this.$forceUpdate() // 编辑成功刷新页面
                //
                // this.query.codeCode = ''
                // this.query.codeName = ''
                // this.query.moduleCatalogId = ''
                setTimeout(() => {
                    this.queryCodeList()
                }, 500)
            },
            showCreate() {
                this.edit = {
                    display: true,
                    codeId: ''
                }
            },
            // 模型分组
            ControlTypes() {
                tableManagement.getcontrolList().then(data => {
                    for (let item in data) {
                        this.ControlTypesList.push({
                            label: data[item].id,
                            value: data[item].catalogName
                        })
                    }
                })
                    .catch(() => {
                        this.$message.error('查询模型分组失败')
                    })
            },
            // 单行删除
            deleteCode(row) {
                let page = {
                    codeId: row.codeId
                }
                let _this = this
                this.$confirm('确认要删除选中的码表吗?').then(() => {
                    tableManagement.getDeleteCodeList(page).then(item => {
                        if (item.flag === 'error') {
                            _this.$message.error(item.msg)
                        } else {
                            _this.$message.success('删除成功')
                            _this.queryCodeList()
                        }
                    })
                })
            },
            // searchData() {
            //     this.queryCodeList()
            // },
            queryCodeList() {
                this.codeList = []
                this.loading = true
                this.query.currentPage = 1
                let queryObject = this.query
                tableManagement.getCodeList(queryObject).then(data => {
                    this.codeList = data.data
                    this.total = data.totalSize
                }).catch(() => {
                    this.$message.error('查询码表信息失败')
                }).finally(() => {
                    this.loading = false
                })
            },
            // 分页改变尺寸
            handleSizeChange(val) {
                this.query.pageSize = val
                this.queryCodeList()
            },
            // 分页改变当前页
            handleCurrentChange(val) {
                this.query.currentPage = val
                this.loading = true
                let queryObject = this.query
                tableManagement.getCodeList(queryObject).then(data => {
                    this.codeList = data.data
                    this.total = data.totalSize
                }).catch(() => {
                    this.$message.error('查询码表信息失败')
                }).finally(() => {
                    this.loading = false
                })
            },
            // 修改
            editCode(rowData) {
                this.msgList = rowData
                this.edit = {
                    display: true,
                    // codeId: rowData.codeId
                    codeId: rowData
                }
            },
            // tabel 多选项
            handleSelectionChange(val) {
                this.selectCode = val
            },
            getDistinctDictType() {
                cmdbDictService.getDistinctDictType().then(data => {
                    data.forEach(item => {
                        this.$set(this.dictList, item.col_name, item.description)
                    })
                    this.queryCodeList()
                })
                    .catch(() => {
                        this.$message.error('获取字典列表失败')
                    })
            }
        }
    }
</script>
<style lang="scss" scoped>
    /deep/ .components-condition {
        padding-right: 66px;
    }
</style>
<style lang="scss" scoped>
    .query_configuration {
        background: #fff;
        box-shadow: 0px 0px 8px 0px rgba(4, 0, 0, 0.13);
        border-radius: 6px;
        padding: 5px 10px;
        margin: 4px 4px 0 4px;
        width: 100%;
    }
    .table_Button {
        height: 40px;
        padding-top: 5px;
    }
</style>
