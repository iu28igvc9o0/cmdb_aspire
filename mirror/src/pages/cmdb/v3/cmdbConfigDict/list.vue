<template>
    <div class="components-container yw-dashboard"
         v-loading="loading"
         :element-loading-text="loading_text">
        <el-form class="components-condition yw-form"
                 :inline="true"
                 label-width="60px">
            <el-form-item label="配置编码">
                <el-input v-model="searchForm.configCode"></el-input>
            </el-form-item>
            <section class="btn-wrap">
                <el-button type="primary"
                           @click="query(true,1)">查询</el-button>
                <el-button @click="cancel()">重置</el-button>
            </section>
        </el-form>
        <div class="yw-el-table-wrap">
            <div class="table-operate-wrap clearfix">
                <el-button type="text"
                           icon="el-icon-plus"
                           @click="addRow()">新增</el-button>
            </div>
            <el-table class="yw-el-table"
                      border
                      :data="tableData"
                      style="cursor: pointer;margin-top:10px"
                      stripe
                      tooltip-effect="dark"
                      height="calc(100vh - 240px)">
                <el-table-column type="selection"></el-table-column>
                <el-table-column prop="configCode"
                                 label="配置编码"
                                 :show-overflow-tooltip="true"> </el-table-column>
                <el-table-column prop="configValue"
                                 label="配置值"
                                 :show-overflow-tooltip="true"></el-table-column>
                <el-table-column prop="configValueType"
                                 label="配置值类型"
                                 :show-overflow-tooltip="true"> </el-table-column>
                <el-table-column prop="configRemark"
                                 label="配置备注"
                                 :show-overflow-tooltip="true"></el-table-column>
                <el-table-column fixed="right"
                                 label="操作"
                                 width="70">
                    <template slot-scope="scope">
                        <el-button type="text"
                                   title="编辑"
                                   @click="updateRow(scope.row)">
                            <i style="color: #269BE0;"
                               class="el-icon-edit"></i>
                        </el-button>
                        <el-button type="text"
                                   title="删除"
                                   @click="deleteRow(scope.row)">
                            <i style="color: #269BE0;"
                               class="el-icon-delete"></i>
                        </el-button>
                    </template>
                </el-table-column>
            </el-table>
            <div class="yw-page-wrap">
                <YwPagination @handleSizeChange="handleSizeChange"
                              @handleCurrentChange="handleCurrentChange"
                              :current-page="currentPage"
                              :page-sizes="pageSizes"
                              :page-size="pageSize"
                              :total="total"></YwPagination>
            </div>
        </div>

        <!-- 新增拟态框 -->
        <el-dialog class="yw-dialog"
                   :visible.sync="addDialog"
                   width="430px"
                   :close-on-click-modal="false"
                   title="新增配置信息">
            <section class="yw-dialog-main">
                <el-form class="yw-form is-required"
                         :model="addForm"
                         ref="addForm"
                         :rules="addRule"
                         label-width="90px">
                    <el-form-item label="配置编码"
                                  prop="configCode">
                        <el-input v-model="addForm.configCode"></el-input>
                    </el-form-item>
                    <el-form-item label="配置值类型"
                                  prop="configValueType">
                        <el-select v-model="addForm.configValueType">
                            <el-option v-for="item in configValueTypeList"
                                       :key="item.value"
                                       :label="item.name"
                                       :value="item.value">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="配置值"
                                  prop="configValue">
                        <el-input type="textarea"
                                  rows="2"
                                  v-model="addForm.configValue"></el-input>
                    </el-form-item>
                    <el-form-item label="配置备注"
                                  prop="configRemark">
                        <el-input v-model="addForm.configRemark"></el-input>
                    </el-form-item>
                </el-form>
            </section>
            <section class="btn-wrap">
                <el-button type="primary"
                           @click="commitForm('add')">确定</el-button>
                <el-button @click="cancel('add')">取消</el-button>
            </section>
        </el-dialog>

        <!-- 编辑拟态框 -->
        <el-dialog class="yw-dialog"
                   :visible.sync="updateDialog"
                   width="430px"
                   :close-on-click-modal="false"
                   title="编辑配置信息">
            <section class="yw-dialog-main">
                <el-form class="yw-form is-required"
                         :model="updateForm"
                         ref="updateForm"
                         :rules="updateRule"
                         label-width="90px">
                    <el-form-item label="配置编码"
                                  prop="configCode">
                        <el-input v-model="updateForm.configCode"
                                  disabled></el-input>
                    </el-form-item>
                    <el-form-item label="配置值类型"
                                  prop="configValueType">
                        <el-select v-model="updateForm.configValueType">
                            <el-option v-for="item in configValueTypeList"
                                       :key="item.value"
                                       :label="item.name"
                                       :value="item.value">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="配置值"
                                  prop="configValue">
                        <el-input type="textarea"
                                  rows="2"
                                  v-model="updateForm.configValue"></el-input>
                    </el-form-item>
                    <el-form-item label="配置备注"
                                  prop="configRemark">
                        <el-input v-model="updateForm.configRemark"></el-input>
                    </el-form-item>
                </el-form>
            </section>
            <section class="btn-wrap">
                <el-button type="primary"
                           @click="commitForm('update')">确定</el-button>
                <el-button @click="cancel('update')">取消</el-button>
            </section>
        </el-dialog>
    </div>
</template>

<script>
    import QueryObject from 'src/utils/queryObject.js'
    import CmdbConfigDictService from 'src/services/cmdb/rb-cmdb-cmdbConfigDict-service.factory.js'
    export default {
        mixins: [QueryObject],
        components: {
            YwPagination: () => import('src/components/common/yw-pagination.vue')
        },
        data() {
            var validJSONFormatter = (rule, value, callback) => {
                if ('json' == this.addForm.configValueType || 'json' == this.updateForm.configValueType) {
                    if (typeof value == 'string') {
                        try {
                            var obj = JSON.parse(value)
                            if (typeof obj == 'object' && obj) {
                                return callback()
                            }
                        } catch (e) {
                            return callback('json格式不正确')
                        }
                    }
                }
                return callback()
            }
            var validCodeUnique = (rule, value, callback) => {
                var param = {
                    configCode: value
                }
                CmdbConfigDictService.getOne(param).then((data) => {
                    if (data.flag) {
                        return callback('该Code已存在')
                    }
                    return callback()
                })
            }
            return {
                loading: false,
                loading_text: '请稍候...',
                tableData: [],
                configValueTypeList: [{
                    name: 'text',
                    value: 'text'
                }, {
                    name: 'json',
                    value: 'json'
                }],
                searchForm: {
                    configCode: ''
                },
                addDialog: false,
                addForm: {
                    configCode: '',
                    configValue: '',
                    configValueType: 'text',
                    configRemark: ''
                },
                addRule: {
                    configCode: [{ required: true, message: '请输入配置编码', trigger: 'blur' },
                    { validator: validCodeUnique, trigger: 'blur' }],
                    configValue: [{ required: true, message: '请输入配置值', trigger: 'blur' },
                    { validator: validJSONFormatter, trigger: 'blur' }],
                    configValueType: [{ required: true, message: '请输入配置值类型', trigger: 'blur' }],
                },
                updateDialog: false,
                updateForm: {
                    configCode: '',
                    configValue: '',
                    configValueType: 'text',
                    configRemark: ''
                },
                updateRule: {
                    configCode: [{ required: true, message: '请输入配置编码', trigger: 'blur' }],
                    configValue: [{ required: true, message: '请输入配置值', trigger: 'blur' },
                    { validator: validJSONFormatter, trigger: 'blur' }],
                    configValueType: [{ required: true, message: '请输入配置值类型', trigger: 'blur' }],
                }
            }
        },
        mounted() {
            this.query(true, 1)
        },
        methods: {
            query(flag, pageNo) {
                this.loading = true
                this.loading_text = '正在查询,请稍候...'
                this.currentPage = pageNo ? pageNo : this.currentPage
                let req = {
                    pageNo: this.currentPage,
                    pageSize: this.pageSize,
                }
                req = Object.assign(req, this.searchForm)
                CmdbConfigDictService.listWithPage(req).then((res) => {
                    this.total = res.totalSize
                    this.tableData = res.data
                }).finally(() => {
                    this.loading = false
                })
            },
            cancel(type) {
                if (type == 'add') {
                    this.addDialog = false
                    this.$refs['addForm'].resetFields()
                } else if (type == 'update') {
                    this.updateDialog = false
                } else {
                    this.searchForm.configCode = ''
                    this.query(true, 1)
                }
            },
            addRow() {
                this.addDialog = true
            },
            updateRow(row) {
                var obj = {
                    configCode: row.configCode,
                    configRemark: row.configRemark,
                    configValue: row.configValue,
                    configValueType: row.configValueType,
                    id: row.id
                }
                this.updateForm = obj
                this.updateDialog = true
            },
            deleteRow(row) {
                this.$confirm('确认是否删除', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'info'
                }).then(() => {
                    var param = {
                        id: row.id,
                        configCode: row.configCode,
                    }
                    CmdbConfigDictService.delete(param).then((res) => {
                        if (res.flag) {
                            this.$message.success('删除成功')
                        }
                    }).finally(() => {
                        this.query(true, 1)
                    })
                })
            },
            commitForm(type) {
                var init = {
                    configCode: '',
                    configValue: '',
                    configValueType: 'text',
                    configRemark: ''
                }
                if (type == 'add') {
                    this.$refs['addForm'].validate((flag) => {
                        if (flag) {
                            CmdbConfigDictService.save(this.addForm).then((res) => {
                                if (res.flag) {
                                    this.$message.success('新增成功')
                                }
                            }).finally(() => {
                                this.addDialog = false
                                this.addForm = init
                                this.query(true, 1)
                            })
                        }
                    })

                } else if (type == 'update') {
                    this.$refs['updateForm'].validate((flag) => {
                        if (flag) {
                            CmdbConfigDictService.update(this.updateForm).then((res) => {
                                if (res.flag) {
                                    this.$message.success('更新成功')
                                }
                            }).finally(() => {
                                this.updateDialog = false
                                this.query(true, 1)
                            })
                        }
                    })
                }
            }
        }
    }
</script>

<style lang="scss" scoped></style>
