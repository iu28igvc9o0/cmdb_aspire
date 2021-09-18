<template>
    <div class="components-container  yw-dashboard">
        <el-form class="components-condition yw-form"
                 :inline="true"
                 label-width="65px">
            <el-form-item label="配置名称">
                <el-input v-model="configure_name"></el-input>
            </el-form-item>
            <el-form-item label="配置类型">
                <el-select v-model="alert_level_value"
                           placeholder="请选择配置类型"
                           clearable
                           filterable>
                    <el-option v-for="item in configure_type"
                               :key="item.value"
                               :label="item.label"
                               :value="item.value">
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item>
                <el-button type="primary"
                           @click="query">查询</el-button>
            </el-form-item>
        </el-form>

        <el-form class="yw-form">

            <el-button class="btn-icons-wrap"
                       type="text"
                       icon="el-icon-plus"
                       @click="add">新增</el-button>
            <el-button class="btn-icons-wrap"
                       type="text"
                       icon="el-icon-delete"
                       @click="add">批量删除</el-button>

            <div class="yw-el-table-wrap">
                <el-table :data="tableData"
                          class="yw-el-table"
                          height="calc(100vh - 600px)"
                          stripe
                          tooltip-effect="dark"
                          border
                          @selection-change="handleSelectionChange"
                          @row-dblclick="dblHandleCurrentChange($event)"
                          v-loading="loading">
                    <el-table-column type="selection"
                                     min-width="20px"
                                     align="center"></el-table-column>
                    <el-table-column fixed
                                     prop="id"
                                     label=配置名称
                                     width="">
                    </el-table-column>
                    <el-table-column prop="editer"
                                     label="配置类型"
                                     width="">
                    </el-table-column>
                    <el-table-column prop="updated_at"
                                     label="查询条件"
                                     width="">
                    </el-table-column>
                    <el-table-column fixed="right"
                                     label="操作"
                                     width="100">
                        <template slot-scope="scope">
                            <el-button type="text"
                                       @click="deleteRow(scope.row.name,scope.row.id)"><i style="color: #269BE0;"
                                   class="el-icon-delete"></i></el-button>&nbsp;&nbsp;&nbsp;
                            <el-button type="text"
                                       @click="Edit(scope.row.name,scope.row.id)"><i style="color: #269BE0;"
                                   class="el-icon-edit"></i></el-button>
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
                               layout="total, sizes, prev, pager, next, jumper">
                </el-pagination>
            </div>
        </el-form>

        <!-- dialog -->
        <el-dialog class="yw-dialog"
                   title="编辑"
                   :visible.sync="addAlertNotifyConfig"
                   width="500px"
                   :close-on-click-modal="false"
                   :modal="false">
            <section class="yw-dialog-main">
                <el-form class="components-condition yw-form"
                         :model="form"
                         :inline="true"
                         label-width="65px">
                    <el-form-item label="配置名称">
                        <el-input v-model="configure_name"></el-input>
                    </el-form-item>
                    <el-form-item label="配置类型">
                        <el-select v-model="alert_level_value"
                                   placeholder="请选择配置类型"
                                   clearable
                                   filterable>
                            <el-option v-for="item in configure_type"
                                       :key="item.value"
                                       :label="item.label"
                                       :value="item.value">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="菜单名称">
                        <el-select v-model="alert_level_value"
                                   placeholder="请选择菜单名称"
                                   clearable
                                   filterable>
                            <el-option v-for="item in configure_type"
                                       :key="item.value"
                                       :label="item.label"
                                       :value="item.value">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="码表名称">
                        <el-select v-model="alert_level_value"
                                   placeholder="请选择码表名称"
                                   clearable
                                   filterable>
                            <el-option v-for="item in configure_type"
                                       :key="item.value"
                                       :label="item.label"
                                       :value="item.value">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="默认值">
                        <el-select v-model="alert_level_value"
                                   placeholder="请输入默认值"
                                   clearable
                                   filterable>
                            <el-option v-for="item in configure_type"
                                       :key="item.value"
                                       :label="item.label"
                                       :value="item.value">
                            </el-option>
                        </el-select>
                    </el-form-item>
                </el-form>
            </section>
            <section class="btn-wrap">
                <el-button type="primary"
                           @click="addSubmit()">保存</el-button>
                <el-button @click="addCancel()">取消</el-button>
            </section>
        </el-dialog>
    </div>
</template>

<script>
    import rbAlertFilterServicesFactory from 'src/services/alert/rb-alertFilterScene-services.factory.js'
    export default {

        data() {
            return {
                addAlertNotifyConfig: false,
                currentPage: 1,
                pageSize: 50,
                total: 1111111111,
                pageSizes: [20, 50, 100],
                configure_name: '',
                form: {},
                tableData: [] // 列表数据
            }
        },
        mounted() {
            this.search()
        },
        methods: {
            Edit(name, id) {
                this.addAlertNotifyConfig = true
                this.form = {}
                rbAlertFilterServicesFactory.getByKey(id).then((res) => {
                    console.log(res, id)
                    this.form = res
                    this.editName = res.name
                    this.editFlag = true
                    this.edit = true
                })
            },
            add() {
                this.addAlertNotifyConfig = true
            },
            addCancel() {
                this.addAlertNotifyConfig = false
            },
            addSubmit() {
                this.addAlertNotifyConfig = false
            },
            deleteRow(name, id) {
                this.$confirm('删除场景 ' + id + '?', '删除', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    rbAlertFilterServicesFactory.delete(id).then(() => {
                        this.$message.success('删除成功')
                        this.search() // 重新查询数据
                    }).catch(() => {
                        this.$message.error('删除失败')
                    })
                })
            },
            search() {
                let page = {
                    order: 'DESC',
                    page_no: this.currentPage,
                    page_size: this.pageSize,
                    sort_name: 'updated_at',
                    name: ''
                }

                rbAlertFilterServicesFactory.getList(page).then((res) => {
                    this.tableData = res.result
                    this.total = res.count
                }).catch(() => {
                    this.$message.error('查询失败')
                })
            },
        },
    }
</script>

<style scoped>
</style>