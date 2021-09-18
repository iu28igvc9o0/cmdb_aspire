<template>
    <div class="components-container yw-dashboard"
         v-loading.fullscreen.lock="pageLoading">
        <el-form class="components-condition yw-form"
                 :inline="true"
                 label-width="60px">
            <el-form-item label="模板名称">
                <el-input v-model="checkedData.scheme_name_like"
                          placeholder="请输入内容"></el-input>
            </el-form-item>
            <el-form-item label="创建时间">
                <el-date-picker v-model="checkedData.time_range"
                                type="datetimerange"
                                range-separator="至"
                                start-placeholder="开始日期"
                                end-placeholder="结束日期"
                                value-format="yyyy-MM-dd HH:mm:ss"></el-date-picker>
            </el-form-item>
            <el-form-item label="规则分组"
                          style="width:270px;">
                <ctree @value-change="getvalue"
                       :values="checkedData.group_relation_list"></ctree>
            </el-form-item>

            <section class="btn-wrap">
                <el-button type="primary"
                           @click="search()">查询</el-button>
                <el-button @click="reset()">重置</el-button>
            </section>
        </el-form>

        <el-form class="yw-form">
            <div class="table-operate-wrap clearfix">
                <el-button type="text"
                           icon="el-icon-plus"
                           @click="goAdd()">新增</el-button>
                <el-button type="text"
                           icon="el-icon-delete"
                           @click="del">删除</el-button>
            </div>
            <div class="yw-el-table-wrap">
                <el-table :data="tableData"
                          class="yw-el-table"
                          stripe
                          highlight-current-row
                          tooltip-effect="dark"
                          border
                          height="calc(100vh - 270px)"
                          @selection-change="handleSelectionChange"
                          :default-sort="{prop: 'create_time', order: 'descending'}">
                    <el-table-column type="selection"></el-table-column>
                    <el-table-column prop="scheme_name"
                                     label="规则名称"
                                     sortable></el-table-column>
                    <el-table-column prop="group_relation_list_value"
                                     label="规则分组"></el-table-column>
                    <el-table-column prop="refer_pipeline_count"
                                     label="步骤数"></el-table-column>
                    <el-table-column prop="refer_pipeline_list_name"
                                     label="关联作业"></el-table-column>
                    <el-table-column prop="creater"
                                     label="创建人员"
                                     width="220px"></el-table-column>
                    <el-table-column prop="create_time"
                                     label="创建时间"
                                     sortable></el-table-column>
                    <el-table-column prop="description"
                                     label="规则描述"></el-table-column>
                    <el-table-column prop="operation"
                                     width="100"
                                     label="操作">
                        <template slot-scope="scope">
                            <el-button type="text"
                                       title="修改"
                                       icon="el-icon-edit"
                                       @click="update(scope.row)"></el-button>
                            <el-button type="text"
                                       title="克隆"
                                       icon="el-icon-copy-document"
                                       @click="copy(scope.row)"></el-button>
                            <el-button type="text"
                                       title="删除"
                                       icon="el-icon-delete"
                                       @click="delSingle(scope.row)"></el-button>
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
                               layout="total, sizes, prev, pager, next, jumper"
                               :total="total"></el-pagination>
            </div>
        </el-form>

        <!-- dialog -->
        <el-dialog class="yw-dialog"
                   :title="dialogName"
                   :visible.sync="templateEditorShow"
                   width="1250px"
                   :close-on-click-modal="false">
            <section class="yw-dialog-main">
                <template-editor ref="templateEditor"
                                 v-if="templateEditorShow"
                                 :currentTemplateInfo="currentTemplateInfo"></template-editor>
            </section>
            <section class="btn-wrap">
                <el-button type="primary"
                           @click="saveTemplate()">保存</el-button>
                <el-button @click="addCancel()">取消</el-button>
            </section>
        </el-dialog>
    </div>
</template>

<script>

    import templateEditor from './edit/template-editor.vue'
    import ctree from './edit/selectTree.vue'

    import rbAutoHealingServicesFactory from 'src/services/auto_operation/rb-auto-healing-services.factory.js'

    export default {
        components: {
            templateEditor,
            ctree
        },
        data() {
            return {
                pageLoading: false,
                dialogName: '',
                templateEditorShow: false,
                currentTemplateInfo: {},
                checkedData: {
                    scheme_name_like: '',
                    time_range: [],
                    group_relation_list: ''
                },
                selectoptions: [],
                // 多选框模板存放的值
                multipleSelection: [],
                tableData: [],
                // 当前页
                currentPage: 1,
                // 分页每页多少行数据
                pageSize: 10,
                // 每页多少行数组
                pageSizes: [10, 20, 50, 100],
                // 总共多少行数据
                total: 0,
                editorType: false
            }
        },
        methods: {
            handleSelectionChange(val) {
                this.multipleSelection = val
            },
            // 分页改变尺寸
            handleSizeChange(val) {
                this.pageSize = val
                this.getList()
            },
            // 分页改变当前页
            handleCurrentChange(val) {
                this.currentPage = val
                this.getList()
            },
            // 模板的增删改查
            getList() {
                let obj = {
                    scheme_name_like: this.checkedData.scheme_name_like || '',
                    group_id_list: this.checkedData.group_relation_list.group_id ? [this.checkedData.group_relation_list.group_id] : [],
                    create_time_start: this.checkedData.time_range ? this.checkedData.time_range[0] : null,
                    create_time_end: this.checkedData.time_range ? this.checkedData.time_range[1] : null,
                    page_no: this.currentPage,
                    page_size: this.pageSize,
                }
                rbAutoHealingServicesFactory.queryOpsAutoRepairSchemeList(obj).then((res) => {
                    this.total = res.totalCount * 1
                    if (res.dataList) {
                        res.dataList.forEach((v) => {
                            v.group_relation_list.length && (v.group_relation_list = v.group_relation_list[0])
                            v.group_relation_list_value = v.group_relation_list.group_name
                            if (v.refer_pipeline_list && v.refer_pipeline_list.length) {
                                let refer_pipeline_list_name = []
                                v.refer_pipeline_list.forEach((v1) => {
                                    refer_pipeline_list_name.push(v1.pipelineName)
                                })
                                v.refer_pipeline_list_name = refer_pipeline_list_name.join(',')
                            }
                        })
                        this.tableData = res.dataList
                    } else {
                        this.tableData = []
                    }
                })
            },
            // 业务逻辑
            search() {
                this.currentPage = 1
                this.getList()
            },
            reset() {
                this.checkedData = {
                    scheme_name_like: '',
                    time_range: [],
                    group_relation_list: ''
                }
            },
            getvalue(v) {
                this.checkedData.group_relation_list = v
            },
            goAdd() {
                this.dialogName = '新建模板'
                this.currentTemplateInfo = {
                    scheme_id: '',
                    scheme_name: '',
                    group_relation_list: '',
                    multi_items_apply_time: '',
                    refer_pipeline_count: 1,
                    refer_pipeline_list: [],
                    refer_apitem_list: [],
                    description: ''
                }
                this.editorType = true
                this.templateEditorShow = true
            },
            update(row) {
                this.dialogName = '编辑模板'
                this.currentTemplateInfo = row
                this.templateEditorShow = true
                this.editorType = false
            },
            copy(row) {
                row.refer_apitem_list.forEach(v => {
                    v.schemeId = ''
                })
                row.refer_pipeline_list.forEach(v => {
                    v.schemeId = ''
                })
                row.scheme_id = ''
                row.refer_apitem_list = []
                this.editorType = true
                this.dialogName = '编辑模板'
                this.currentTemplateInfo = row
                this.templateEditorShow = true
            },
            addCancel() {
                this.currentTemplateInfo = null
                this.templateEditorShow = false
            },

            saveTemplate() {
                this.$refs.templateEditor.$refs.templateForm.validate((valid) => {
                    if (!valid) {
                        this.$message('请先完善信息')
                        return
                    }
                    let refer_pipeline_list = JSON.parse(JSON.stringify(this.$refs.templateEditor.refer_pipeline_list))
                    let refer_apitem_list = JSON.parse(JSON.stringify(this.$refs.templateEditor.refer_apitem_list))

                    refer_pipeline_list.forEach((v, i) => {
                        v.order = i + 1
                    })
                    refer_apitem_list.forEach((v, i) => {
                        v.order = i + 1
                    })
                    if (refer_apitem_list.length > 1 && !this.$refs.templateEditor.listcurrentTemplateInfo.multi_items_apply_time) {
                        this.$message.error('自愈指标有多个时,规则匹配时间不能为空')
                        return
                    }
                    if (refer_apitem_list.length) {
                        let type = false
                        for (let i = 0; i < refer_apitem_list.length; i++) {
                            let v = refer_apitem_list[i]
                            if (v.apItemName && v.judgeSymbol && String(v.judgeValue)) {
                                !v.schemeId && this.$delete(v, 'id')
                                v.template_name && this.$delete(v, 'template_name')
                                continue
                            } else {
                                type = true
                                break
                            }
                        }
                        if (type) {
                            this.$message.error('自愈指标名称, 判断符号, 匹配值 不能为空')
                            return
                        }
                    }
                    if (refer_pipeline_list.length) {
                        let type = false
                        for (let i = 0; i < refer_pipeline_list.length; i++) {
                            let v = refer_pipeline_list[i]
                            !v.schemeId && this.$delete(v, 'id')
                            if (String(v.finishJudgeAction) && String(v.finishJudgeValue) && v.finishJudgeType) {
                                continue
                            } else {
                                if (refer_pipeline_list.length - 1 === i) {
                                    continue
                                } else {
                                    type = true
                                    break
                                }
                            }
                        }
                        if (type) {
                            this.$message.error('除最后一行, 返回类型, 返回值, 完毕动作 不能为空')
                            return
                        }
                    }
                    this.pageLoading = true
                    let obj = {
                        ...this.$refs.templateEditor.listcurrentTemplateInfo,
                        ...{ refer_pipeline_list: refer_pipeline_list },
                        ...{ refer_apitem_list: refer_apitem_list },
                        ...{ remove_pipeline_list: this.$refs.templateEditor.remove_pipeline_list },
                        ...{ remove_apitem_list: this.$refs.templateEditor.remove_apitem_list }
                    }
                    obj.refer_pipeline_count = refer_pipeline_list.length
                    obj.group_id_list = [obj.group_relation_list.group_id]

                    this.$delete(obj, 'group_relation_list_value')
                    this.$delete(obj, 'group_relation_list')
                    this.$delete(obj, 'refer_pipeline_list_name')
                    this.$delete(obj, 'update_time')
                    this.$delete(obj, 'create_time')
                    rbAutoHealingServicesFactory.saveOpsAutoRepairScheme(obj).then((res) => {
                        if (res.flag) {
                            this.$message({
                                message: '保存成功',
                                type: 'success'
                            })
                            this.getList()
                            this.currentTemplateInfo = null
                            this.templateEditorShow = false
                            this.pageLoading = false
                        } else {
                            this.pageLoading = false
                            this.$message.error(res.error_tip)
                        }
                    }).catch(() => {
                        this.pageLoading = false
                        this.$message.error('保存失败')
                    })
                })
            },

            del() {
                if (this.multipleSelection.length) {
                    this.$confirm('确认删除？').then(() => {
                        let str = ''
                        this.multipleSelection.forEach((item) => {
                            str += item.scheme_id
                            str += ','
                        })
                        let str1 = str.slice(0, -1)
                        let obj = {
                            schemeId: str1
                        }
                        rbAutoHealingServicesFactory.removeOpsAutoRepairScheme(obj).then(() => {
                            this.getList()
                            this.$message({
                                message: '删除成功',
                                type: 'success'
                            })
                        }).catch(() => {
                            this.$message.error('删除失败')
                        })
                    }).catch(() => {

                    })
                }
            },
            delSingle(row) {
                this.$confirm('确认删除？').then(() => {
                    let obj = {
                        schemeId: row.scheme_id
                    }
                    rbAutoHealingServicesFactory.removeOpsAutoRepairScheme(obj).then(() => {
                        this.getList()
                        this.$message({
                            message: '删除成功',
                            type: 'success'
                        })
                    }).catch(() => {
                        this.$message.error('删除失败')
                    })
                }).catch(() => {
                })
            }
        },
        mounted() {
            this.getList()
        }
    }
</script>

<style lang="scss" scoped>
    .component-container {
        width: 100%;
        height: calc(100% - 58px - 48px);
        padding: 14px 14px 0px;
        overflow-y: auto;
        overflow-x: hidden;
        background-color: #f4f4f4;
        header {
            height: 140px;
            //border:2px solid #dfdfdf;
            border: 2px solid #f0f0f0;
            display: flex;
            align-items: center;
            flex-wrap: wrap;
            .head {
                width: 100%;
                text-align: center;
                .template-name {
                    display: inline-block;
                }
                .moni-type {
                    margin-left: 1.9%;
                    display: inline-block;
                }
                .input-tem-name1 {
                    width: 8.7%;
                    min-width: 102px;
                    margin-left: 1%;
                }
                .list-sel {
                    width: 8.7%;
                    min-width: 102px;
                    margin-left: 1%;
                    display: inline-block;
                }
                div {
                    display: inline-block;
                }
                .time-range {
                    width: 36%;
                    min-width: 403px;
                    margin-left: 1%;
                    height: 34px;
                    line-height: 32px;
                    padding: 0 15px;
                }
                .creat-time {
                    margin-left: 1.9%;
                }
                .but {
                    width: 6%;
                }
            }
        }
        .body-container {
            //border:2px solid #dfdfdf;
            border: 2px solid #f0f0f0;
            margin-top: 10px;
            padding: 10px 5px;
            .el-button {
                height: 30px;
                width: 80px;
                padding: 8px 15px;
            }
            .block {
                margin-top: 30px;
                height: 50px;
                display: flex;
                align-items: center;
                justify-content: center;
            }
        }
    }
</style>
