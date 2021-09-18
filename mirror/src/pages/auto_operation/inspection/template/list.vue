<template>
    <div class="components-container yw-dashboard"
         v-loading.fullscreen.lock="pageLoading">
        <el-form class="components-condition yw-form"
                 :inline="true"
                 label-width="60px">
            <el-form-item label="模板名称">
                <el-input v-model="name"
                          placeholder="请输入内容"></el-input>
            </el-form-item>
            <el-form-item label="创建时间">
                <el-date-picker v-model="time_range"
                                type="datetimerange"
                                range-separator="至"
                                start-placeholder="开始日期"
                                end-placeholder="结束日期"
                                value-format="yyyy-MM-dd HH:mm:ss">
                </el-date-picker>
            </el-form-item>
            <el-form-item label="巡检类型">
                <el-select v-model="type"
                           placeholder="请选择"
                           clearable>
                    <el-option v-for="item in options"
                               :key="item.value"
                               :label="item.label"
                               :value="item.value">
                    </el-option>
                </el-select>
            </el-form-item>

            <section class="btn-wrap">
                <el-button type="primary"
                           @click="search()">查询
                </el-button>
                <el-button @click="reset()">重置</el-button>
            </section>
        </el-form>

        <el-form class="yw-form">
            <div class="table-operate-wrap clearfix">
                <el-button type="text"
                           icon="el-icon-plus"
                           @click="goAdd()">新增
                </el-button>
                <el-button type="text"
                           icon="el-icon-delete"
                           @click="del">删除
                </el-button>
            </div>
            <div class="yw-el-table-wrap">
                <el-table :data="tableData"
                          class="yw-el-table"
                          stripe
                          tooltip-effect="dark"
                          border
                          height="calc(100vh - 270px)"
                          @selection-change="handleSelectionChange"
                          :default-sort="{prop: 'create_time', order: 'descending'}">
                    <el-table-column type="selection">
                    </el-table-column>
                    <el-table-column prop="name"
                                     label="模板名称"
                                     show-overflow-tooltip
                                     sortable>
                    </el-table-column>
                    <el-table-column prop="task_num"
                                     label="关联任务数"
                                     show-overflow-tooltip
                                     sortable
                                     width="110px">
                    </el-table-column>
                    <el-table-column prop="mon_type"
                                     label="监控类型"
                                     show-overflow-tooltip
                                     width="100px">
                    </el-table-column>
                    <el-table-column prop="type"
                                     label="巡检类型"
                                     show-overflow-tooltip
                                     width="100px">
                    </el-table-column>
                    <el-table-column prop="items"
                                     label="关联监控项"
                                     width="380px"
                                     :show-overflow-tooltip="true">
                        <template slot-scope="scope">
                            <span>
                                {{scope.row.items}}
                            </span>
                        </template>
                    </el-table-column>
                    <el-table-column prop="create_time"
                                     show-overflow-tooltip
                                     label="创建时间"
                                     sortable>
                    </el-table-column>
                    <el-table-column prop="description"
                                     show-overflow-tooltip
                                     label="模板描述">
                    </el-table-column>
                    <el-table-column prop="operation"
                                     width="120"
                                     label="操作">
                        <template slot-scope="scope">
                            <el-button type="text"
                                       title="修改"
                                       icon="el-icon-edit"
                                       @click="update(scope.row)">
                            </el-button>
                            <el-button type="text"
                                       title="克隆"
                                       icon="el-icon-copy-document"
                                       @click="copy(scope.row)">
                            </el-button>
                            <el-button type="text"
                                       title="导出指标项"
                                       icon="el-icon-download"
                                       @click="exportItemList(scope.row)">
                            </el-button>
                            <el-button type="text"
                                       title="删除"
                                       icon="el-icon-delete"
                                       @click="delSingle(scope.row)">
                            </el-button>
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
                               :total="total">
                </el-pagination>
            </div>
        </el-form>

        <!-- dialog -->

        <!-- dialog -->
        <el-dialog class="yw-dialog"
                   :title="dialogName"
                   :visible.sync="templateEditorShow"
                   width="1280px"
                   :close-on-click-modal="false">
            <section class="yw-dialog-main">
                <template-editor ref="templateEditor"
                                 :currentTemplateInfo="currentTemplateInfo"
                                 @closeDialog="closeEditDialog"
                                 @customParam="customParam"></template-editor>
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
    // import rbHttp from '../../../../assets/js/utility/rb-http.factory.js'
    import { typeOptions, expressionList } from './config/options.js'
    import rbProjectDataServiceFactory from 'src/services/template/rb-template-services.factory.js'
    import rbMirrorCommonService from 'src/services/mirror/rb-mirror-common-services.factory.js'
    import { formatDate } from 'src/assets/js/utility/rb-filters.js'
    import templateEditor from './edit/template-editor.vue'
    import rbAutoOperationServicesFactory from 'src/services/auto_operation/rb-auto-operation-services.factory.js'

    export default {
        components: {
            templateEditor
        },
        computed: {
            routerInfo() {
                return this.$route.query
            },
            templateModel() {
                return JSON.parse(sessionStorage.getItem('subFormSelectData'))
            }

        },
        data() {
            return {
                pageLoading: false,
                dialogName: '',
                templateEditorShow: false,
                currentTemplateInfo: {},
                name: '',
                time_range: [],
                // 巡检类型的选项
                options: [],
                // 巡检类型下拉框的值
                type: '',
                // 多选框模板存放的值
                multipleSelection: [],
                tableData: [],
                // 当前页
                currentPage: 1,
                // 分页每页多少行数据
                pageSize: 20,
                // 每页多少行数组
                pageSizes: [10, 20, 50, 100],
                // 总共多少行数据
                total: 0
            }
        },
        methods: {
            customParam(val) {
                if (val) {
                    let newVal = JSON.parse(val.customize_param)
                    this.currentTemplateInfo.item_list.forEach(item => {
                        if (item.key == newVal[0].entity_id) {
                            item.item_ext = val
                        }
                    })
                }
            },
            addCancel() {
                this.templateEditorShow = false
            },
            saveTemplate() {
                this.$refs.templateEditor.$refs.templateForm.validate((valid) => {
                    if (!valid) {
                        this.$message('请先完善信息')
                        return
                    }

                    this.pageLoading = true

                    let req = {
                        template_id: this.currentTemplateInfo.template_id,
                        mon_type: '3',
                        fun_type: '2',
                        sys_type: this.currentTemplateInfo.sys_type,
                        type: this.currentTemplateInfo.type,
                        description: this.currentTemplateInfo.description,
                        name: this.currentTemplateInfo.name,
                        item_list: this.currentTemplateInfo.item_list
                    }
                    // this.currentTemplateInfo.item_list.forEach(item => {
                    //     if (item.expression != '::contains::') {
                    //         item.value_type = 'FLOAT'
                    //     }
                    // })
                    if (this.currentTemplateInfo.template_id == '') {
                        rbProjectDataServiceFactory.creat(req).then((res) => {
                            if (res.template_id) {
                                this.templateEditorShow = false
                                this.template = res
                            }
                            this.$message.success('保存成功')
                            this.search()
                            this.pageLoading = false
                        }).catch(() => {
                            this.pageLoading = false
                            this.$message({
                                message: '保存失败',
                                type: 'error'
                            })
                        })
                    } else {
                        rbProjectDataServiceFactory.update(req).then((res) => {
                            if (res.template_id) {
                                this.templateEditorShow = false
                                this.template = res
                            }
                            this.$message('保存成功')
                            this.search()
                            this.pageLoading = false
                        }).catch(() => {
                            this.pageLoading = false
                            this.$message({
                                message: '模板创建失败',
                                type: 'error'
                            })
                        })
                    }
                })
            },
            handleSelectionChange(val) {
                this.multipleSelection = val
            },
            // 分页改变尺寸
            handleSizeChange(val) {
                this.pageSize = val
                this.search(1)
            },
            // 分页改变当前页
            handleCurrentChange(val) {
                this.currentPage = val
                this.search(1)
            },
            // 封装列表数据
            packData(arr) {
                arr.forEach((item) => {
                    if (item.create_time) {
                        item.create_time = formatDate(item.create_time)
                    }
                    item.type = rbMirrorCommonService.getType(item.type)
                    item.mon_type = rbMirrorCommonService.common('MONTYPE', '1', item.mon_type)
                })
                return arr
            },
            // 模板的增删改查
            getList(obj) {
                let obj1 = {
                    page_no: this.currentPage,
                    page_size: this.pageSize,
                    fun_type: '2'
                }
                if (obj) {
                    obj = Object.assign(obj, obj1)
                } else {
                    obj = obj1
                }
                rbProjectDataServiceFactory.getList(obj).then((res) => {
                    this.tableData = this.packData(res.result)
                    this.total = res.count
                })
            },
            // 业务逻辑
            search(num) {
                if (num !== 1) {
                    // 搜索前将当前页置为1
                    this.currentPage = 1
                }
                let obj = {
                    'name': this.name,
                    'type': this.type,
                    'create_time_start': this.time_range ? this.time_range[0] : '',
                    'create_time_end': this.time_range ? this.time_range[1] : ''
                }
                this.getList(obj)
            },
            reset() {
                this.name = ''
                this.type = ''
                this.time_range = []
            },
            goAdd() {
                // this.$root.$children[0].$children[0].reload()
                // this.$router.push('add')
                this.dialogName = '新建模板'
                this.currentTemplateInfo = {
                    template_id: '',
                    mon_type: '3',
                    fun_type: '2',
                    type: '',
                    description: '',
                    name: '',
                    sys_type: 'SCRIPT',
                    item_list: [],
                    isEdit: true
                }
                this.templateEditorShow = true
            },
            update(row) {
                let id = row.template_id
                var isDeleteItem = row.task_num == 0 ? true : false
                rbProjectDataServiceFactory.detail(id).then(res => {
                    if (res) {
                        this.dialogName = '编辑模板'
                        res.item_list.forEach(item => {
                            item.content_type_desc = rbMirrorCommonService.common('CONTENT_TYPE', '1', item.content_type)
                            if (item.expression) {
                                for (var i = 0; i < expressionList.length; i++) {
                                    let obj = expressionList[i]
                                    if (item.expression.indexOf(obj.value) !== -1) {
                                        item.match = item.expression.substring(item.expression.indexOf(obj.value) + obj.value.length)
                                        item.expression = obj.value
                                        break
                                    }
                                }
                            }
                        })
                        this.currentTemplateInfo = {
                            template_id: res.template_id,
                            mon_type: res.mon_type, // 监控类型 agent设备
                            fun_type: res.fun_type,// 巡检类型模板
                            name: res.name,
                            sys_type: res.sys_type,// 脚本类型
                            type: res.type,
                            description: res.description,
                            item_list: res.item_list,
                            isEdit: isDeleteItem
                        }
                        this.templateEditorShow = true
                    }
                }).catch(() => {
                    this.$message.error('修改失败')
                })
            },
            // 根据id查询脚本内容
            queryOpsScriptById(scriptId, item) {
                rbAutoOperationServicesFactory.queryOpsScriptById(scriptId).then(res => {
                    item.script_name = res.script_name
                    item.content_type_desc = rbMirrorCommonService.common('CONTENT_TYPE', '1', res.content_type)
                })
            },
            del() {
                let bool = true
                this.multipleSelection.forEach((item) => {
                    if (item.task_num > 0) {
                        bool = false
                    }
                })
                if (bool) {
                    this.$confirm('确认删除？').then(() => {
                        let str = ''
                        this.multipleSelection.forEach((item) => {
                            str += item.template_id
                            str += ','
                        })
                        let str1 = str.slice(0, -1)
                        let obj = {
                            'template_ids': str1
                        }
                        rbProjectDataServiceFactory.delete(obj).then(() => {
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
                } else {
                    this.$alert('删除模板中包含已关联任务的模板', '消息', {
                        confirmButtonText: '确定'
                    })
                }
            },
            exportItemList(row) {
                rbProjectDataServiceFactory.exportItemList(row.template_id).then((res) => {
                    try {
                        const link = window.URL.createObjectURL(new Blob([res], { type: 'application/vnd.ms-excel' }))
                        // console.log('personExport link ', link)
                        let downLoadElement = document.createElement('a')
                        downLoadElement.href = link
                        downLoadElement.download = row.name + '_指标列表.xls'
                        document.body.appendChild(downLoadElement)
                        downLoadElement.click()
                        document.body.removeChild(downLoadElement)
                        URL.revokeObjectURL(link)
                    } catch (err) {
                        this.$message({
                            message: '导出失败',
                            type: 'warning'
                        })
                    }
                })
            },
            delSingle(row) {
                if (row.task_num == 0) {
                    this.$confirm('确认删除？').then(() => {
                        let obj = {
                            'template_ids': row.template_id
                        }
                        rbProjectDataServiceFactory.delete(obj).then(() => {
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
                } else {
                    this.$alert('删除模板中包含已关联任务的模板', '消息', {
                        confirmButtonText: '确定'
                    })
                }
            },
            copy(row) {
                rbProjectDataServiceFactory.copy(row.template_id).then(() => {
                    this.getList()
                    this.$message({
                        message: '复制成功',
                        type: 'success'
                    })
                }).catch(() => {
                    this.$message.error('复制失败')
                })
            }
        },
        mounted() {
            this.options = typeOptions
            this.getList()
            // this.tableData = this.getTableData()
        },
        destroyed() {
            sessionStorage.removeItem('subFormSelectData')
        },
        watch: {
            // routerInfo: {
            //     handler(val) {
            //         if (val.showAdd === 'true') {
            //             let obj = {}
            //             obj.key = val.rowData.script_id
            //             obj.script_name = val.rowData.script_name
            //             if (val.rowData.script_content_type === 1) {
            //                 obj.content_type_desc = 'sh'
            //             } else if (val.rowData.script_content_type === 2) {
            //                 obj.content_type_desc = 'bat'
            //             } else {
            //                 obj.content_type_desc = 'python'
            //             }
            //             obj.value_type = 'LOG'
            //             obj.sys_type = 'SCRIPT'
            //             obj.biz_group = val.rowData.script_group_name
            //             obj.item_group = val.rowData.priority
            //             obj.name = val.rowData.item_name
            //             obj.expression = val.rowData.expression
            //             obj.match = val.rowData.default_threshold_val
            //             obj.prototype_id = val.rowData.id
            //             obj.item_ext = val.rowData.item_ext
            //             this.dialogName = '新建模板'
            //             this.currentTemplateInfo = {
            //                 template_id: '',
            //                 mon_type: '3',
            //                 fun_type: '2',
            //                 type: '',
            //                 description: '',
            //                 name: '',
            //                 sys_type: 'SCRIPT',
            //                 item_list: [],
            //                 isEdit: true
            //             }
            //             this.currentTemplateInfo.item_list.push(obj)
            //             this.templateEditorShow = true
            //         }
            //     },
            //     immediate: true,
            //     deep: true
            // },
            templateModel: {
                handler(val) {
                    if (val && val.length > 0) {
                        this.dialogName = '新建模板'
                        this.currentTemplateInfo = {
                            template_id: '',
                            mon_type: '3',
                            fun_type: '2',
                            type: '',
                            description: '',
                            name: '',
                            sys_type: 'SCRIPT',
                            item_list: [],
                            isEdit: true
                        }
                        val.forEach(item => {
                            let obj = {}
                            obj.key = item.script_id
                            obj.script_name = item.script_name
                            if (item.script_content_type === 1) {
                                obj.content_type_desc = 'sh'
                            } else if (item.script_content_type === 2) {
                                obj.content_type_desc = 'bat'
                            } else {
                                obj.content_type_desc = 'python'
                            }
                            obj.value_type = 'LOG'
                            obj.sys_type = 'SCRIPT'
                            obj.biz_group = item.script_group_name
                            obj.item_group = item.priority
                            obj.name = item.item_name
                            obj.expression = item.expression
                            obj.match = item.default_threshold_val
                            obj.prototype_id = item.id
                            obj.item_ext = item.item_ext
                            this.currentTemplateInfo.item_list.push(obj)
                        })
                        this.templateEditorShow = true
                    }
                },
                immediate: true,
                deep: true
            }
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
