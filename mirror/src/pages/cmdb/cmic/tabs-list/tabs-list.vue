<template>
    <div class="components-container yw-dashboard"
         v-loading="loading">
        <el-form class="yw-form">
            <div class="table-operate-wrap clearfix">
                <el-button type="text"
                           icon="el-icon-plus"
                           @click="addDict()">新增</el-button>
                <el-button type="text"
                           icon="el-icon-edit"
                           @click="editDict()">编辑</el-button>
                <el-button type="text"
                           icon="el-icon-delete"
                           @click="deleteDict()">删除</el-button>
            </div>
            <div class="yw-el-table-wrap">
                <el-table class="yw-el-table"
                          border
                          :data="configDictData"
                          style="cursor: pointer;"
                          stripe
                          tooltip-effect="dark"
                          height="calc(100vh - 300px)"
                          @selection-change="handleSelectionChange">
                    <el-table-column type="selection"></el-table-column>
                    <el-table-column prop="modelName"
                                     label="模型名称"
                                     width="180px"
                                     :show-overflow-tooltip="true"> </el-table-column>
                    <el-table-column prop="tabName"
                                     label="TAB名称"
                                     width="180px"></el-table-column>
                    <el-table-column prop="subassemblyName"
                                     label="TAB组件名称"
                                     width="150px"
                                     :show-overflow-tooltip="true"> </el-table-column>
                    <el-table-column prop="paramConfig"
                                     label="参数配置"
                                     :show-overflow-tooltip="true"></el-table-column>
                    <el-table-column prop="callbackConfig"
                                     label="回调配置"
                                     :show-overflow-tooltip="true"></el-table-column>
                </el-table>
            </div>
            <div class="yw-page-wrap"
                 v-if="total>0">
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
                   :visible.sync="addDialog"
                   width="600px"
                   :close-on-click-modal="false"
                   :title="dialogTitle">
            <section class="yw-dialog-main">
                <el-form class="yw-form is-required"
                         :model="dialogForm"
                         :rules="dialogRules"
                         ref="dialogForm"
                         label-width="100px">
                    <el-form-item label="模型名称"
                                  class="model-name">
                        <treeselect class="yw-treeselect"
                                    :disabled="isEdit"
                                    :value-consists-of="valueConsistsOf"
                                    :options="moduleOptions"
                                    :default-expand-level="1"
                                    :limit="1"
                                    :clearable="false"
                                    :multiple="true"
                                    v-model="dialogForm.modelId"
                                    :normalizer="normalizer"
                                    noOptionsText="暂无数据"
                                    placeholder="请选择模型名称:">
                        </treeselect>
                    </el-form-item>
                    <el-form-item label="TAB名称"
                                  prop="tabName">
                        <el-input v-model="dialogForm.tabName"></el-input>
                    </el-form-item>
                    <el-form-item label="引入组件名称"
                                  prop="subassemblyName">
                        <el-input v-model="dialogForm.subassemblyName"></el-input>
                    </el-form-item>
                    <el-form-item label="显示顺序"
                                  prop="sortIndex">
                        <el-input v-model.number="dialogForm.sortIndex"></el-input>
                    </el-form-item>
                    <el-form-item label="参数配置"
                                  class="param-config"
                                  prop="paramConfig">
                        <el-input type="textarea"
                                  v-model="dialogForm.paramConfig"
                                  :rows="4"></el-input>
                        <span class="format-checks"
                              @click="paramCheck">检验JSON格式</span>
                    </el-form-item>
                    <el-form-item label="回调配置"
                                  class="param-config"
                                  prop="callbackConfig">
                        <el-input type="textarea"
                                  v-model="dialogForm.callbackConfig"
                                  :rows="4"></el-input>
                        <span class="format-checks"
                              @click="callbackCheck">检验JSON格式</span>
                    </el-form-item>
                </el-form>
            </section>
            <section class="btn-wrap">
                <el-button type="primary"
                           @click="saveAddForm('dialogForm')">确定</el-button>
                <el-button @click="cancel">取消</el-button>
            </section>
        </el-dialog>
        <!-- dialog -->
    </div>
</template>

<script>
    import rbTabsServiceFactory from 'src/services/cmdb/rb-cmdb-tabs-factory.js'
    import Treeselect from '@riophae/vue-treeselect'
    import '@riophae/vue-treeselect/dist/vue-treeselect.css'
    import tableManagement from 'src/services/condition_management/table_management.js'
    export default {
        components: {
            Treeselect
        },
        data() {
            var checkAge = (rule, value, callback) => {
                if (!value && value !== 0) {
                    return callback(new Error('显示顺序不能为空'))
                } else {
                    if (!Number.isInteger(value)) {
                        callback(new Error('显示顺序为数字值'))
                    } else {
                        if (value < 0) {
                            callback(new Error('显示顺序为正整数'))
                        } else {
                            callback()
                        }
                    }
                }

            }
            return {
                loading: false,
                multipleSelection: [],
                configDictData: [],
                currentPage: 1, // 当前页
                pageSize: 20, // 分页每页多少行数据
                pageSizes: [10, 20, 50, 100], // 每页多少行数组
                total: 0, // 总共多少行数据
                addDialog: false,
                dialogTitle: '新增TAB管理',
                isEdit: false,
                dialogForm: {
                    modelId: null,
                    tabName: '',
                    subassemblyName: '',
                    sortIndex: '',
                    paramConfig: '',
                    callbackConfig: ''
                },
                dialogRules: {
                    tabName: [{ required: true, message: '请输入TAB名称', trigger: 'blur' }],
                    subassemblyName: [{ required: true, message: '请输入引入组件名称', trigger: 'blur' }],
                    sortIndex: [{ required: true, message: '请输入显示顺序', trigger: 'blur' }, { validator: checkAge, trigger: 'blur' }]
                },
                isParam: false,
                isCallback: false,
                moduleOptions: [],
                normalizer(node) {
                    if (node.childModules && !node.childModules.length) {
                        delete node.childModules
                    }
                    return {
                        id: node.id,
                        label: node.name,
                        children: node.childModules,
                    }
                },
                valueConsistsOf: 'ALL',
                storageParam: '',
                storageCallback: ''
            }
        },
        mounted() {
            this.search()
            // 模型名称
            tableManagement.getTreeList('').then(res => {
                this.moduleOptions = res
            })
        },
        methods: {
            // element相关方法 多选框
            handleSelectionChange(val) {
                this.multipleSelection = val
            },
            // 分页改变尺寸
            handleSizeChange(val) {
                this.currentPage = 1
                this.pageSize = val
                this.search()
            },
            // 分页改变当前页
            handleCurrentChange(val) {
                this.currentPage = val
                this.search()
            },
            search() {
                this.loading = true
                let obj = {
                    pageSize: this.pageSize,
                    pageNo: this.currentPage
                }
                rbTabsServiceFactory.getModelTabsList(obj).then((res) => {
                    this.configDictData = res.data
                    this.total = res.totalSize
                    this.loading = false
                })
            },
            cancel() {
                this.addDialog = false
            },
            addDict() {
                this.addDialog = true
                this.isEdit = false
                this.dialogTitle = '新增TAB管理'
                this.$nextTick(() => {
                    this.$refs['dialogForm'].resetFields()
                    this.dialogForm.modelId = []
                    this.isParam = false
                    this.isCallback = false
                })

            },
            editDict() {
                if (this.multipleSelection.length !== 1) {
                    this.$alert('请选择一条数据进行修改', '通知', {
                        confirmButtonText: '确定'
                    })
                } else {
                    this.addDialog = true
                    this.isEdit = true
                    this.dialogTitle = '编辑TAB管理'
                    this.$nextTick(() => {
                        this.$refs['dialogForm'].resetFields()
                        this.dialogForm.modelId = []
                        this.isParam = false
                        this.isCallback = false
                        let obj = {
                            tabsId: this.multipleSelection[0].tabsId
                        }
                        rbTabsServiceFactory.getModelTabsById(obj).then((res) => {
                            if (res.success) {
                                this.dialogForm = res.data
                                this.dialogForm.modelId = res.data.modelId.split(',')
                                this.storageParam = res.data.paramConfig
                                this.storageCallback = res.data.callbackConfig

                            } else {
                                this.$message({
                                    message: res.msg,
                                    type: 'error'
                                })
                            }
                        })
                    })
                }
            },
            saveAddForm(formName) {
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        if (!this.dialogForm.modelId || this.dialogForm.modelId.length < 1) {
                            this.$message({
                                message: '请选择模型名称',
                                type: 'error'
                            })
                            return false
                        }
                        if (this.isEdit) {
                            if (this.dialogForm.paramConfig === this.storageParam) {
                                this.isParam = true
                            } else {
                                this.isParam = false
                            }
                            if (this.dialogForm.callbackConfig === this.storageCallback) {
                                this.isCallback = true
                            } else {
                                this.isCallback = false
                            }
                        }
                        if (this.dialogForm.paramConfig && !this.isParam) {
                            this.$message({
                                message: '请进行参数配置的JSON格式校验',
                                type: 'error'
                            })
                            return false
                        } else if (this.dialogForm.callbackConfig && !this.isCallback) {
                            this.$message({
                                message: '请进行回调配置的JSON格式校验',
                                type: 'error'
                            })
                            return false
                        } else {
                            this.addDialog = false
                            let obj = JSON.parse(JSON.stringify(this.dialogForm))
                            obj.modelId = this.dialogForm.modelId.join(',')
                            if (this.isEdit) {
                                obj.tabsId = this.multipleSelection[0].tabsId
                            } else {
                                obj.tabsId = ''
                            }
                            rbTabsServiceFactory.saveModelTabs(obj).then((res) => {
                                if (res.success) {
                                    this.$message({
                                        message: res.msg,
                                        type: 'success'
                                    })
                                    this.search()
                                } else {
                                    this.$message({
                                        message: res.msg,
                                        type: 'error'
                                    })
                                }
                            })
                        }

                    } else {
                        return false
                    }
                })
            },
            deleteDict() {
                if (this.multipleSelection.length < 1) {
                    this.$alert('请至少选择一条数据进行删除', '通知', {
                        confirmButtonText: '确定'
                    })
                } else {
                    this.$confirm('确认删除吗？', '提示', {
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        type: 'warning'
                    }).then(() => {
                        let idList = []
                        let idListData = JSON.parse(JSON.stringify(this.multipleSelection))
                        for (let item of idListData) {
                            if (item.tabsId) {
                                idList.push(item.tabsId)
                            }
                        }
                        let obj = {
                            idList: idList.join(',')
                        }
                        console.log(obj, 'obj')
                        rbTabsServiceFactory.deleteModelTabsById(obj).then((res) => {
                            if (res.success) {
                                this.$message({
                                    message: res.msg,
                                    type: 'success'
                                })
                                this.search()
                            } else {
                                this.$message({
                                    message: res.msg,
                                    type: 'error'
                                })
                            }
                        })

                    }).catch(() => {
                        this.$message({
                            type: 'info',
                            message: '已取消删除'
                        })
                    })
                }
            },
            paramCheck() {
                // 参数配置json格式校验
                if (this.dialogForm.paramConfig) {
                    this.isParam = this.isJSON(this.dialogForm.paramConfig)
                    if (!this.isParam) {
                        this.$message({
                            type: 'error',
                            message: '请输入正确的JSON格式'
                        })
                    } else {
                        this.$message({
                            type: 'success',
                            message: 'JSON格式校验通过'
                        })
                    }
                } else {
                    this.isParam = true
                }

            },
            callbackCheck() {
                // 回掉配置json格式校验
                if (this.dialogForm.callbackConfig) {
                    this.isCallback = this.isJSON(this.dialogForm.callbackConfig)
                    if (!this.isCallback) {
                        this.$message({
                            type: 'error',
                            message: '请输入正确的JSON格式'
                        })
                    } else {
                        this.$message({
                            type: 'success',
                            message: 'JSON格式校验通过'
                        })
                    }
                } else {
                    this.isCallback = true
                }
            },
            // json格式校验
            isJSON(str) {
                if (typeof str == 'string') {
                    try {
                        var obj = JSON.parse(str)
                        console.log(obj)
                        if (typeof obj == 'object' && obj) {
                            return true
                        } else {
                            return false
                        }

                    } catch (e) {
                        return false
                    }
                } else {
                    return false
                }
            }
        }
    }
</script>

<style lang="scss" scoped>
    .param-config {
        position: relative;
    }
    .format-checks {
        position: absolute;
        right: 50px;
        top: 40px;
        color: #2727ff;
        cursor: pointer;
        text-decoration: underline;
    }
    .model-name /deep/ .el-form-item__label::before {
        content: "*";
        color: red;
        margin-left: -10px;
        padding-right: 4px;
    }
</style>
