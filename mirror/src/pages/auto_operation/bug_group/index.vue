<!-- 漏洞管理：漏洞分组 -->
<template>
    <div class="components-container yw-dashboard"
         v-loading.fullscreen.lock="pageLoading"
         :element-loading-text="loading_text">
        <el-form class="components-condition yw-form"
                 :model="formSearch"
                 @keyup.enter.native="search(1)"
                 ref="formSearch"
                 :inline="true"
                 label-width="65px">
            <el-form-item label="分组名称"
                          prop="name_like">
                <el-input v-model="formSearch.name_like"
                          placeholder="请输入分组名称"
                          clearable></el-input>
            </el-form-item>
            <el-form-item label="是否有效"
                          prop="is_valid">
                <el-select v-model="formSearch.is_valid"
                           clearable>
                    <el-option label="是"
                               value="1"></el-option>
                    <el-option label="否"
                               value="0"></el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="创建时间">
                <el-col :span="11">
                    <el-form-item prop="create_time_start">
                        <el-date-picker v-model="formSearch.create_time_start"
                                        value-format="yyyy-MM-dd HH:mm:ss"
                                        type="datetime"
                                        placeholder="开始时间"></el-date-picker>
                    </el-form-item>
                </el-col>
                <el-col class="line"
                        :span="1">-</el-col>
                <el-col :span="11">
                    <el-form-item prop="create_time_end">
                        <el-date-picker v-model="formSearch.create_time_end"
                                        value-format="yyyy-MM-dd HH:mm:ss"
                                        type="datetime"
                                        placeholder="结束时间"></el-date-picker>
                    </el-form-item>
                </el-col>
            </el-form-item>
            <el-form-item label="更新时间">
                <el-col :span="11">
                    <el-form-item prop="update_time_start">
                        <el-date-picker v-model="formSearch.update_time_start"
                                        value-format="yyyy-MM-dd HH:mm:ss"
                                        type="datetime"
                                        placeholder="开始时间"></el-date-picker>
                    </el-form-item>
                </el-col>
                <el-col class="line"
                        :span="1">-</el-col>
                <el-col :span="11">
                    <el-form-item prop="update_time_end">
                        <el-date-picker v-model="formSearch.update_time_end"
                                        value-format="yyyy-MM-dd HH:mm:ss"
                                        type="datetime"
                                        placeholder="结束时间"></el-date-picker>
                    </el-form-item>
                </el-col>
            </el-form-item>
            <section class="btn-wrap">
                <el-button type="primary"
                           @click="search(1)">查询</el-button>
                <el-button @click="reset()">重置</el-button>
            </section>
        </el-form>

        <div class="yw-form">
            <div class="table-operate-wrap clearfix">
                <el-button type="text"
                           icon="el-icon-plus"
                           @click="showDialogBox('addRow')">新建</el-button>
            </div>
            <div class="yw-el-table-wrap">
                <el-table :data="dataList"
                          class="yw-el-table"
                          stripe
                          highlight-current-row
                          tooltip-effect="dark"
                          border
                          height="calc(100vh - 275px)"
                          v-loading="loading">
                    <el-table-column prop="name"
                                     label="分组名称"
                                     show-overflow-tooltip
                                     width="120"></el-table-column>
                    <el-table-column prop="is_valid"
                                     label="是否有效">
                        <template slot-scope="scope">
                            <span v-if="scope.row.is_valid === '1'">是</span>
                            <span v-if="scope.row.is_valid === '0'">否</span>
                        </template>
                    </el-table-column>
                    <el-table-column prop="vulnerability_group_desc"
                                     label="分组描述"
                                     show-overflow-tooltip
                                     width="120"></el-table-column>
                    <el-table-column prop="create_time"
                                     label="创建时间"
                                     min-width="140"></el-table-column>
                    <el-table-column prop="creater"
                                     label="创建人"></el-table-column>
                    <el-table-column prop="update_time"
                                     label="修改时间"
                                     min-width="140"></el-table-column>
                    <el-table-column prop="updater"
                                     label="修改人"></el-table-column>
                    <el-table-column label="操作"
                                     width="130">
                        <template slot-scope="scope">
                            <div class="yw-table-operator">
                                <el-button type="text"
                                           title="详情"
                                           icon="el-icon-view"
                                           @click="showDialogBox('viewDetail', scope.row)"></el-button>
                                <el-button type="text"
                                           title="编辑"
                                           icon="el-icon-edit"
                                           @click="showDialogBox('editRow', scope.row)"></el-button>
                                <el-button type="text"
                                           title="删除"
                                           icon="el-icon-delete"
                                           @click="removeRow(scope.row.vulnerability_group_id)"></el-button>
                            </div>
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

        <!-- 新建、编辑分组 -->
        <el-dialog class="yw-dialog"
                   :title="dialogName"
                   :visible.sync="dialogBoxShow"
                   v-if="dialogBoxShow"
                   :close-on-click-modal="false"
                   width="80%">
            <section class="yw-dialog-main middle-size-form">
                <edit-group ref="editGroup"
                            :currentRowData="currentRowData"
                            :showBoxType="showBoxType"
                            @saveFormData="saveFormData"
                            @updateAddForm="updateAddForm"></edit-group>
            </section>
            <section class="btn-wrap"
                     v-show="!isReadonly">
                <el-button type="primary"
                           @click="validForm">保存</el-button>
                <el-button @click="addCancel">取消</el-button>
            </section>
        </el-dialog>
    </div>
</template>

<script>
    import comtree from 'src/pages/auto_operation/components/tree.vue'
    import editGroup from './edit-group'
    import rbAutoOperationMixin from 'src/services/auto_operation/rb-auto-operation-mixin.js'
    import bugManageService from 'src/services/auto_operation/rb-auto-operation-bug-manage-services.js'
    export default {
        name: 'BugGroupManage',
        components: { comtree, editGroup },
        data() {
            return {
                formSearch: {
                    name_like: '',
                    update_time_start: '',
                    update_time_end: '',
                    create_time_start: '',
                    create_time_end: '',
                    is_valid: ''
                },
                currentRowData: {
                    vulnerability_group_id: '',
                    name: '',
                    is_valid: '',
                    vulnerability_group_desc: '',
                    vulnerability_group_rule: ''
                },
                // 分组
                showBoxType: 'addRow', // 触发事件
                dialogBoxShow: false,
                dialogName: '添加分组',

                dataList: [] // 分组列表
            }
        },
        mixins: [rbAutoOperationMixin],
        created() {
            this.search()
        },
        watch: {
        },
        computed: {
            isReadonly() {
                return this.showBoxType === 'viewDetail'
            }
        },
        methods: {
            updateAddForm(data) {
                this.currentRowData = data
            },
            // 更新分组
            saveVulnerabilityGroup(req) {
                bugManageService.saveVulnerabilityGroup(req).then(res => {
                    if (res.flag) {
                        this.$message.success('保存成功')
                        this.search()
                        this.closeDialogBox()
                    } else {
                        this.$message.error(res.error_tip)
                    }
                })
            },
            // 保存信息
            validForm() {
                this.$refs.editGroup.validForm()
            },
            // 保存信息
            saveFormData() {
                let req = this.currentRowData
                if (this.showBoxType === 'editRow') {
                    req.vulnerability_group_id = this.currentRowData.vulnerability_group_id
                }
                this.saveVulnerabilityGroup(req)
            },
            // 删除分组
            removeRow(id) {
                this.$confirm('您确定要删除该分组吗？').then(() => {
                    this.loading = true
                    bugManageService.removeVulnerabilityGroup({ vulnerabilityGroupId: id }).then(res => {
                        if (res.flag) {
                            this.$message.success('删除成功')
                            this.search()
                        } else {
                            this.$message.error(res.error_tip)
                        }
                    })
                })
            },
            // 打开弹框
            showDialogBox(type, row) {
                this.showBoxType = type
                if (row) {
                    this.currentRowData = row
                } else {
                    this.currentRowData = null
                }
                if (type === 'addRow') {
                    this.dialogName = '添加分组'
                } else if (type === 'editRow') {
                    this.dialogName = '编辑分组'
                } else {
                    this.dialogName = '分组详情'
                }
                this.dialogBoxShow = true
            },
            closeDialogBox() {
                this.dialogBoxShow = false
            },

            search(pageNum) {
                this.currentPage = pageNum ? pageNum : this.currentPage
                let req = {
                    page_no: this.currentPage,
                    page_size: this.pageSize
                }
                req = Object.assign(req, this.formSearch)
                this.loading = true
                bugManageService
                    .queryVulnerabilityGroupList(req)
                    .then(res => {
                        this.loading = false
                        this.total = res.totalCount
                        this.dataList = res.dataList
                    })
                    .catch(res => {
                        this.showErrorTip(res)
                        this.loading = false
                    })
            },
            reset() {
                this.$refs['formSearch'].resetFields()
                this.search(1)
            },
            // 关闭弹框
            addCancel() {
                this.dialogBoxShow = false
            }
        }
    }
</script>

<style lang="scss" scoped></style>
