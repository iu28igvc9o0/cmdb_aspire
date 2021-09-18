<template>
    <!-- 模型分组管理 -->
    <el-dialog class="yw-dialog"
               v-if="dialogMsg.dialogVisible"
               width="700px"
               title="模型分组管理"
               @close="submit"
               :visible.sync="dialogMsg.dialogVisible">
        <div style="max-height:400px;"
             v-if="resetComponent">
            <!-- 表格 -->
            <section style="margin-bottom:20px;">
                <el-tabs class="yw-tabs">
                    <el-tab-pane label="模型展示">
                    </el-tab-pane>
                </el-tabs>
                <el-form class="yw-form">
                    <div class="table-operate-wrap clearfix">
                    </div>
                    <div class="yw-el-table-wrap">
                        <el-table :data="tableData"
                                  height="200px"
                                  border
                                  :default-expand-all="true"
                                  row-key="id"
                                  highlight-current-row
                                  :tree-props="{children: 'child', hasChildren: 'hasChildren'}"
                                  class="yw-el-table">
                            <el-table-column prop="catalogName"
                                             label="分组名称"></el-table-column>
                            <el-table-column prop="catalogCode"
                                             label="分组编码">
                            </el-table-column>

                            <el-table-column label="操作"
                                             :width="180">
                                <template slot-scope="scope">
                                    <el-button @click="add(scope.row)"
                                               title="新增子节点"
                                               type="text"
                                               icon="el-icon-plus"></el-button>
                                    <el-button @click="edit(scope.row)"
                                               title="编辑"
                                               type="text"
                                               v-if="scope.row.catalogName!='全部'"
                                               icon="el-icon-edit"></el-button>
                                    <!-- <el-button v-if="(!scope.row.child || scope.row.child.length<1) && scope.row.catalogName!='全部'"
                             @click="del(scope.row)"
                             type="text"
                             title="删除"
                             icon="el-icon-delete"></el-button> -->
                                    <el-button v-if="scope.row.catalogName!='全部'"
                                               @click="del(scope.row)"
                                               type="text"
                                               title="删除"
                                               icon="el-icon-delete"></el-button>
                                    <el-button @click="up(scope.row)"
                                               type="text"
                                               title="升序"
                                               v-if="scope.row.catalogName!='全部'"
                                               icon="el-icon-top"></el-button>
                                    <el-button @click="down(scope.row)"
                                               type="text"
                                               title="降序"
                                               v-if="scope.row.catalogName!='全部'"
                                               icon="el-icon-bottom"></el-button>
                                </template>
                            </el-table-column>
                        </el-table>
                    </div>
                </el-form>
            </section>
            <!-- 表格 -->

            <!-- 编辑 -->
            <section v-if="activeData">
                <el-tabs class="yw-tabs">
                    <el-tab-pane :label="formData.statusTabs[formData.status]">
                    </el-tab-pane>
                </el-tabs>
                <el-form class="yw-form components-condition"
                         label-width="70px"
                         :inline="true"
                         :rules="rules"
                         ref="formData"
                         :model="formData">
                    <el-form-item label="分组名称"
                                  prop="catalogName">
                        <el-input v-model="formData.catalogName"
                                  style="width:120px;"></el-input>
                    </el-form-item>
                    <el-form-item label="分组编码"
                                  prop="catalogCode">
                        <el-input v-model="formData.catalogCode"
                                  :disabled="formData.status === 'edit'"
                                  style="width:120px;"></el-input>
                    </el-form-item>

                    <div class="btn-wrap">
                        <el-button type="primary"
                                   @click="save('formData')">保存</el-button>
                    </div>
                </el-form>
            </section>
            <!-- 编辑 -->

            <!-- 确定 -->
            <!-- <section class="btn-wrap">
        <el-button type="primary"
                   @click="submit()">确定</el-button>
      </section> -->
            <!-- 确定 -->
        </div>
    </el-dialog>

</template>

<script>
    import updateComponent from 'src/utils/updateComponent.js'
    import CommonOption from 'src/utils/commonOption.js'
    import rbCmdbModuleServiceFactory from 'src/services/cmdb/rb-cmdb-module-service.factory'
    import { isEnUnderLine } from 'src/utils/validate.js'

    export default {
        mixins: [updateComponent, CommonOption],
        props: ['dialogMsg'],
        components: {
        },
        data() {
            return {
                // 当前选中操作的数据
                activeData: '',
                // 表格数据
                tableData: [],
                // 表单数据
                formData: {
                    status: 'add',// 表单状态（新增、编辑）
                    statusTabs: {
                        add: '新增子节点',
                        edit: '编辑当前节点'
                    },
                    catalogName: '',
                    catalogCode: ''
                },
                // 校验规则
                rules: {
                    catalogName: [
                        { required: true, message: '不能为空', trigger: 'blur' },
                        // { min: 3, max: 5, message: '长度在 3 到 5 个字符', trigger: 'blur' }
                    ],
                    catalogCode: [
                        {
                            required: true,
                            message: '不能为空',
                        }, {
                            min: 1,
                            max: 30,
                            message: '长度在 1 到 30 个字符',
                            trigger: 'blur'
                        },
                        {
                            validator: isEnUnderLine,
                            trigger: 'blur'
                        }
                    ],
                },
            }
        },
        methods: {
            query() {
                this.showFullScreenLoading()

                let params = {
                    catalogId: ''
                }
                rbCmdbModuleServiceFactory.getAllTree(params).then((data) => {
                    this.tableData = [data]
                    this.reset()
                    this.updateComponent()
                }).finally(() => {
                    this.closeFullScreenLoading()
                })
            },

            add(row) {
                this.activeData = row
                this.reset()
                this.formData.status = 'add'
            },
            edit(row) {
                this.activeData = row
                this.formData.status = 'edit'
                this.formData.catalogName = row.catalogName
                this.formData.catalogCode = row.catalogCode
            },
            del(row) {
                this.$confirm('确定删除吗?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.deleteDatas(row)
                }).catch(() => {
                })
            },
            deleteDatas(row) {
                this.showFullScreenLoading({ text: '正在删除数据, 请稍等...' })
                let params = {
                    id: row.id,
                    // "catalogCode": row.catalogCode,
                    // "catalogName": row.catalogName,
                    // "parentCatalogId": row.parentCatalogId,
                    // "sortIndex": row.sortIndex,
                    // "isDelete": row.isDelete,
                }
                rbCmdbModuleServiceFactory.deleteTree(params).then((data) => {
                    if (data.flag == 'success') {
                        this.$message.success('删除成功！')
                        this.activeData = ''
                        this.query()
                    } else {
                        this.$message.error(data.msg)
                    }
                }).finally(() => {
                    this.closeFullScreenLoading()
                })
            },
            up(row) {
                this.showFullScreenLoading({ text: '正在排序数据, 请稍等...' })
                let params = {
                    catalogId: row.id,
                    sortType: 'up'
                }
                rbCmdbModuleServiceFactory.sortTree(params).then((data) => {
                    if (data.flag == 'success') {
                        this.$message.success('排序成功！')
                        this.query()
                    } else {
                        this.$message.error(data.msg)
                    }
                }).finally(() => {
                    this.closeFullScreenLoading()
                })

            },
            down(row) {
                this.showFullScreenLoading({ text: '正在排序数据, 请稍等...' })
                let params = {
                    catalogId: row.id,
                    sortType: 'down'
                }
                rbCmdbModuleServiceFactory.sortTree(params).then((data) => {
                    if (data.flag == 'success') {
                        this.$message.success('排序成功！')
                        this.query()
                    } else {
                        this.$message.error(data.msg)
                    }
                }).finally(() => {
                    this.closeFullScreenLoading()
                })
            },
            save(formName) {
                this.$refs[formName].validate(async (valid) => {
                    if (valid) {
                        this.showFullScreenLoading({ text: '正在保存数据, 请稍等...' })
                        let validRepeat = true
                        if (validRepeat) {
                            switch (this.formData.status) {
                                // 添加
                                case 'add': {
                                    let params = {
                                        'catalogCode': this.formData.catalogCode,
                                        'catalogName': this.formData.catalogName,
                                        'parentCatalogId': this.activeData.id
                                    }
                                    rbCmdbModuleServiceFactory.addTree(params).then((data) => {
                                        if (data.flag == 'success') {
                                            this.$message.success('添加成功！')
                                            this.query()
                                        } else {
                                            this.$message.error(data.msg)
                                        }
                                    }).finally(() => {
                                        this.closeFullScreenLoading()
                                    })
                                    break
                                }
                                // 编辑
                                case 'edit': {
                                    let params2 = {
                                        'id': this.activeData.id,
                                        'catalogName': this.formData.catalogName
                                    }
                                    rbCmdbModuleServiceFactory.updateTree(params2).then((data) => {
                                        if (data.flag == 'success') {
                                            this.$message.success('修改成功！')
                                            this.query()
                                        } else {
                                            this.$message.error(data.msg)
                                        }
                                    }).finally(() => {
                                        this.closeFullScreenLoading()
                                    })
                                    break
                                }
                            }
                        }
                    } else {
                        return false
                    }
                })

            },
            // 校验名称是否重复
            validTreeRepeat() {
                let valid = false
                let params = {
                    'catalogCode': this.formData.catalogCode,
                    'catalogName': this.formData.catalogName,
                    'parentCatalogId': this.activeData.id
                }

                return rbCmdbModuleServiceFactory.validTreeRepeat(params).then((data) => {
                    if (data.flag === 'success') {
                        valid = true
                    } else {
                        this.$message.error('模型分组已经存在，请重新输入名称和编码！')
                        valid = false
                    }
                    return valid
                })

            },
            submit() {
                this.$emit('closeDialog', 'update')
            },
            reset() {
                this.formData.catalogName = ''
                this.formData.catalogCode = ''
            }
        },
        mounted() {
            this.query()
        }
    }

</script>
<style lang="scss" scoped>
    .components-condition {
        padding-right: 120px;
    }
</style>
