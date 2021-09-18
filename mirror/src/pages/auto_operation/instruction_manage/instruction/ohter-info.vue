<template>
    <div class="components-container">
        <div class="run-code-box mtop10">
            <el-form class="yw-form is-required"
                     ref="addForm"
                     :model="addForm"
                     :rules="addFormRules"
                     label-width="120px">
                <el-form-item :label="selectText.title"
                              prop="selectedData">
                    <el-button type="primary"
                               @click="showServersDialog">{{selectText.btn}}</el-button>
                    <el-button type="primary"
                               @click="cloneRole"
                               v-if="selectType === 'review_role_ids'">
                        拷贝增加可执行角色
                    </el-button>
                    <span v-if="selectType === 'review_role_ids' && this.isRequire"
                          class="red">
                        <i class="el-icon-warning"></i> 响应方式为审核，该项必选
                    </span>
                    <div class="yw-el-table-wrap">
                        <el-table :data="addForm.selectedData"
                                  class="yw-el-table"
                                  stripe
                                  tooltip-effect="dark"
                                  border
                                  v-loading="loading">
                            <el-table-column type="index"
                                             label="序号"
                                             width="60"></el-table-column>
                            <el-table-column :prop="selectType==='target_ops_user'?'accountName':'name'"
                                             :label="selectText.column"></el-table-column>
                            <el-table-column label="操作"
                                             width="60">
                                <template slot-scope="scope">
                                    <div class="yw-table-operator">
                                        <el-button type="text"
                                                   title="删除"
                                                   icon="el-icon-delete"
                                                   @click="clearSelectedData(scope.$index)"></el-button>
                                    </div>
                                </template>
                            </el-table-column>
                        </el-table>
                    </div>
                </el-form-item>

            </el-form>
        </div>
        <!-- dialog -->
        <el-dialog class="yw-dialog"
                   :title="selectText.btn"
                   :visible.sync="serversBoxShow"
                   width="700px"
                   :close-on-click-modal="false">
            <!-- 列表 -->
            <section class="yw-dialog-main no-scroll">
                <el-table ref="serverTable"
                          :data="dataList"
                          :row-key="rowKey"
                          class="yw-el-table"
                          stripe
                          tooltip-effect="dark"
                          border
                          height="300"
                          v-loading="loading"
                          @selection-change="handleSelectionChange">
                    <el-table-column type="selection"
                                     :reserve-selection="true"></el-table-column>
                    <el-table-column sortable
                                     :prop="selectType==='target_ops_user'?'accountName':'name'"
                                     :label="selectText.column"></el-table-column>
                </el-table>
                <div class="yw-page-wrap">
                    <el-pagination @size-change="handleSizeChange"
                                   @current-change="handleCurrentChange"
                                   :current-page="currentPage"
                                   :page-sizes="pageSizes"
                                   :page-size="pageSize"
                                   :total="total"
                                   layout="total, sizes, prev, pager, next, jumper"></el-pagination>
                </div>
            </section>

            <section class="btn-wrap">
                <el-button type="primary"
                           @click="addToTargetList">添加</el-button>
                <el-button @click="serversBoxShow=false">取消</el-button>
            </section>
        </el-dialog>
    </div>
</template>

<script>
    import rbAutoOperationMixin from 'src/services/auto_operation/rb-auto-operation-mixin.js'
    import rbAutoOperationOhterMixin from 'src/services/auto_operation/rb-auto-operation-ohter-mixin.js'

    export default {
        components: {
        },
        props: ['dataList', 'listTotal', 'rowData', 'isPreview', 'selectType', 'isRequire'],
        data() {
            return {
            }
        },
        mixins: [rbAutoOperationMixin, rbAutoOperationOhterMixin],
        watch: {
            // 更新目标机器到当前行
            'addForm.selectedData'(newVal) {
                this.$emit('updateCurrentRow', this.rowData, this.selectType, newVal)
            },
        },
        computed: {
            selectText() {
                if (this.selectType === 'target_ops_user') {
                    return {
                        title: '可执行设备账户',
                        btn: '选择账户',
                        column: '账户名称',
                    }
                } else if (this.selectType === 'exec_role_ids') {
                    return {
                        title: '可执行系统角色',
                        btn: '选择角色',
                        column: '角色名称',
                    }
                } else {
                    return {
                        title: '执行审核赋权角色',
                        btn: '选择角色',
                        column: '角色名称',
                    }
                }
            },
            rowKey() {
                if (this.selectType === 'target_ops_user') {
                    return 'accountName'
                } else {
                    return 'name'
                }
            }
        },
        mounted() {
            setTimeout(() => {
                this.initPageInfo()
            }, 0)
        },
        methods: {
            // 初始化页面信息
            initPageInfo() {
                this.addForm.selectedData = this.rowData.rule_range[this.selectType]
                this.total = this.listTotal
            },
            // 校验表单
            validForm() {
                const addFormValid = new Promise((resolve, reject) => {
                    this.$refs.addForm.validate(valid => {
                        if (valid) {
                            resolve()
                        } else {
                            reject()
                        }
                    })
                })
                const codeFormValid = new Promise((resolve, reject) => {
                    this.$refs.codeEditor.$refs.codeForm.validate(valid => {
                        if (valid) {
                            resolve()
                        } else {
                            reject()
                        }
                    })
                })
                Promise.all([addFormValid, codeFormValid])
                    .then(() => {
                        this.$emit('updateScriptEditMoreValid')
                    })
                    .catch(() => {
                        this.$message('请先完善信息')
                    })
            },
        }
    }
</script>


<style lang="scss" scoped>
    .account-box {
        padding: 15px;
        border: 1px solid $color-border;
        background: $color-bg;
        overflow: hidden;
    }
    .mleft10 {
        margin-left: 20px;
    }
    .red {
        color: $color-tip-red;
    }
    .split-box {
        padding: 10px;
    }
    .yw-dialog-main.no-scroll {
        overflow: hidden;
    }
</style>
