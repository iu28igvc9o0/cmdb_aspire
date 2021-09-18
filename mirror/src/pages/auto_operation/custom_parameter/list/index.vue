<template>
    <div class="preview">
        <!-- 条件、列表 -->
        <asp-smart-table ref="aspSmartTable"
                         :tableJson="tableJson"
                         v-model="modelList"
                         :beforeHttp="beforeHttp"
                         :afterHttp="afterHttp"
                         :beforeTableRender="beforeTableRender"
                         :beforeTableRowRender="beforeTableRowRender"
                         :before-button="beforeButton"
                         @on="onbind">

        </asp-smart-table>

        <!-- 弹框 -->
        <el-dialog width="1100px"
                   class="yw-dialog"
                   :title="dialogTitle"
                   v-if="dialogShow"
                   :visible.sync="dialogShow"
                   :destroy-on-close="true"
                   :close-on-click-modal="false">
            <div class="yw-dialog-main ym-table-height">
                <asp-smart-form ref="dialogTable"
                                :status="status"
                                :formJson="dialogJson"
                                v-model="editModel"
                                :afterHttpPro="afterHttpPro"
                                @on="onbind">
                    <div slot="param-grouping">
                        <el-tag :key="tag.group_id"
                                style="margin-right: 5px;"
                                v-for="tag in groupTagids"
                                closable
                                :disable-transitions="false"
                                @close="handleClose(tag)"
                                size="small">
                            {{tag.group_name}}
                        </el-tag>
                        <el-popover placement="bottom-start"
                                    trigger="click">
                            <comtree :ref="treeName"
                                     :data="groupTreeData"
                                     :props="gruopTreeDefault"
                                     :exId="treeName"
                                     :ex-control="true"
                                     :ex-control-opt="customControl"
                                     ex-show-search
                                     @node-click="handleTreeClick">
                            </comtree>
                            <el-button slot="reference"
                                       class="mod-btn"
                                       size="small">请选择</el-button>
                        </el-popover>
                    </div>

                </asp-smart-form>
            </div>
        </el-dialog>
        <el-dialog class="yw-dialog"
                   title="新增子分组"
                   :visible.sync="departmentDialogVisible"
                   :modal="false"
                   :modal-append-to-body="false"
                   width="410px"
                   :append-to-body="true"
                   :before-close="handleDepartmentDialogClose">
            <section class="yw-dialog-main">
                <el-form class="yw-form is-required"
                         ref="groupAddForm"
                         :model="groupAddForm"
                         label-width="100px">
                    <el-form-item label="子分组名称"
                                  prop="name"
                                  :required="true">
                        <el-input v-model="groupAddForm.name"></el-input>
                    </el-form-item>
                    <el-form-item label="子分组描述">
                        <el-input v-model="groupAddForm.descr"></el-input>
                    </el-form-item>
                </el-form>
            </section>
            <section class="btn-wrap">
                <el-button type="primary"
                           @click="handleUpdateDepartment">确定</el-button>
                <el-button @click="departmentDialogVisible = false">取消</el-button>
            </section>
        </el-dialog>

    </div>
</template>

<script>
    import tableJson from '../smart_data/list.json'
    import dialogJson from '../smart_data/dialog.json'
    // import { createDownloadFile } from 'src/utils/utils.js'
    import rbAutoOperationCustomParameterFactory from 'src/services/auto_operation/rb-auto-operation-custom-parameter.factory.js'
    import groupDataService from 'src/services/auto_operation/rb-auto-operation-group-services.js'
    import comtree from 'src/pages/auto_operation/components/tree.vue'

    export default {
        name: 'LldpList',
        components: {
            comtree
        },
        data() {
            const that = this
            return {
                status: 'hideA',
                // 表格
                editModel: {},
                tableJson: tableJson,
                modelList: tableJson.model,
                subFormSelectData: [],// 选中的表格行
                // 弹窗
                dialogShow: false,
                dialogJson: dialogJson,
                dialogTitle: '',
                dialogType: '',
                // 分组
                treeName: 'gtree',
                gruopTreeDefault: {
                    label: 'group_name',
                    children: 'sub_group_list'
                },
                groupTreeData: [],
                customControl: [
                    {
                        title: '新增子分组',
                        icon: 'el-icon-plus',
                        callback: that.customGroupAdd
                    }
                ],
                groupAddForm: {
                    parentid: '',
                    name: '',
                    descr: ''
                },
                groupTagids: [],
                typeOption: [],
                departmentDialogVisible: false
            }
        },
        watch: {
            groupTagids: {
                handler(val) {
                    this.dialogJson.model.group_id_list = []
                    val.forEach(item => {
                        this.dialogJson.model.group_id_list.push(item.group_id)
                    })
                },
                immediate: true,
                deep: true
            }
        },
        computed: {
            aspSmartTable() {
                return this.$refs.aspSmartTable
            },
            dialogTable() {
                return this.$refs.dialogTable
            }
        },
        created() {
            this.getGroupTree()
            this.getOptionList()
        },
        methods: {
            getOptionList() {
                rbAutoOperationCustomParameterFactory.loadAllParamTypeList().then(res => {
                    this.typeOption = res
                })
            },
            // 分组操作
            handleClose(tag) {
                this.groupTagids.splice(this.groupTagids.indexOf(tag), 1)
            },
            // 获取分组树
            getGroupTree() {
                groupDataService.getQueryGroupTree().then((res) => {
                    this.groupTreeData = res
                    // if (res.length > 0) {
                    //   this.deviceAuthDataExpanded = [res[0].group_id]
                    // }
                })
            },
            // 快速新增分组
            customGroupAdd(node, data) {
                this.departmentDialogVisible = true
                this.groupAddForm.parentid = data.group_id
            },
            // 确认资源弹窗
            handleTreeClick(data) {
                if (_.map(this.groupTagids, 'group_id').indexOf(data.group_id) === -1) {
                    this.groupTagids.push({ 'group_name': data.group_name, 'group_id': data.group_id })
                }
                this.treeVisible = false
            },
            // 关闭弹窗
            handleDepartmentDialogClose() {
                this.departmentDialogVisible = false
            },
            // 新增分组
            handleUpdateDepartment() {
                this.$refs['groupAddForm'].validate((valid) => {
                    if (valid) {
                        const params = {
                            parent_id: this.groupAddForm.parentid,
                            group_name: this.groupAddForm.name,
                            group_desc: this.groupAddForm.descr
                        }
                        groupDataService.saveGroup(params).then(() => {
                            this.$message({
                                message: '新增分组成功',
                                type: 'success'
                            })
                            this.getGroupTree()
                            this.resetGroupAddForm()
                            this.handleDepartmentDialogClose()
                        }).catch(() => {
                            this.$message({
                                message: '操作失败',
                                type: 'error'
                            })
                        })
                    } else {
                        return false
                    }
                })
            },
            resetGroupAddForm() {
                this.groupAddForm = {
                    parentid: '',
                    name: '',
                    descr: ''
                }
            },
            showStatus(data) {
                console.log(data)
                let selectData = data.row.param_type
                console.log('selectData', selectData)
                console.log('this.typeOption', this.typeOption)
                let o = this.typeOption.find(l => {
                    return l.param_type === selectData
                })
                console.log(o)
                if (o.auto_popup_flag === 'N') {
                    this.status = 'showA'
                } else {
                    this.status = 'hideA'
                }

            },
            // 表单及表格 回调事件
            onbind(data) {
                // 选择行
                if (data.type === 'multipleSelection') {
                    this.subFormSelectData = data.subFormSelectData
                }
                switch (data.item.columnName) {
                    // 配置跳转
                    case 'add-row':
                        {
                            this.groupTagids = []
                            this.dialogShow = true
                            this.dialogTitle = '新建自定义参数'
                            this.dialogType = 'add'
                            this.$nextTick(() => {
                                this.editModel = this.dialogJson.model // model 赋值
                                this.dialogJson.model.param_id = ''
                                console.log('this.editModel==', this.editModel)
                            })

                        }
                        break
                    case 'edit-row':
                        {
                            this.dialogShow = true
                            this.dialogTitle = '修改自定义参数'
                            this.dialogType = 'edit'
                            this.$nextTick(() => {
                                this.dialogJson.model.param_id = data.row.param_id
                                this.editModel = data.row// model 赋值
                                this.groupTagids = data.row.group_relation_list
                                this.showStatus(data)

                            })

                        }
                        break
                    case 'delete-row':
                        {
                            rbAutoOperationCustomParameterFactory.removeParam(data.row.param_id).then(res => {
                                if (res.flag === true) {
                                    this.aspSmartTable.asp_sendTableQuery('table_1608534401189')
                                } else {
                                    this.$message.error(res.error_tip)
                                }
                            })
                        }
                        break
                    case 'save-submit':
                        {

                            if (this.groupTagids && this.groupTagids.length > 0) {
                                if (this.dialogType === 'add') {
                                    rbAutoOperationCustomParameterFactory.createOpsParam(this.editModel).then(res => {
                                        if (res.flag === true) {
                                            this.dialogShow = false
                                            this.dialogTable.asp_resetFields()
                                            this.aspSmartTable.asp_sendTableQuery('table_1608534401189')
                                            this.groupTagids = []

                                        } else {
                                            this.$message.error(res.error_tip)
                                        }
                                    })
                                } else {
                                    rbAutoOperationCustomParameterFactory.updateOpsParam(this.editModel).then(res => {
                                        if (res.flag === true) {
                                            this.dialogShow = false
                                            this.dialogTable.asp_resetFields()
                                            this.aspSmartTable.asp_sendTableQuery('table_1608534401189')
                                            this.groupTagids = []
                                        } else {
                                            this.$message.error(res.error_tip)
                                        }
                                    })
                                }
                            } else {
                                this.$message.error('请选择参数分组')
                            }
                        }
                        break
                    case 'cacel':
                        {
                            this.dialogShow = false
                            this.groupTagids = []
                            this.editModel.param_code = ''
                            this.editModel.param_desc = ''
                            this.editModel.param_name = ''
                            this.editModel.param_type = ''
                            this.editModel.param_default_value = ''
                            this.editModel.group_id_list = []
                        }
                        break
                    case 'param_type':
                        {
                            let selectData = data.model.param_type
                            let o = data.item.options.find(l => {
                                return l.param_type === selectData
                            })
                            if (o.auto_popup_flag === 'N') {
                                this.status = 'showA'
                            } else {
                                this.status = 'hideA'
                            }
                        }
                        break
                }
            },
            /**
             * 智能表格页面所有请求前的前置操作
             * 例如：修改请求参数、修改请求方式、修改请求URL、或者请求条件不满足不给发送请求
             * @param tableItem 组件对象属性集
             * @param params 请求参数body
             * @param httpMethod.url 请求地址URL
             * @param httpMethod.type 请求方式，目前主要三种：'post+json', 'post+form', 'get'
             * @param row 当组件为表格并且是表格操作列触发的请求，此参数返回表格行数据，其它返回undefined
            */
            beforeHttp({ tableItem, params, httpMethod, row }, responseBody) {
                switch (tableItem.columnName) {
                    case 'table_1608534401189':
                        Object.assign(params, {
                            page_no: params.page,
                            page_size: params.rows,
                        })
                        break
                }
            },
            afterHttpPro({ item, parent, type, index, model, row, fileData, subFormSelectData }, responseBody, callback) {
                switch (item.columnName) {
                    case 'param_type':
                        responseBody.map(item => {
                            let keys = Object.keys(item)
                            item.label = item[keys[0]]
                            item.value = keys[0]
                            return item
                        })
                        this.$utils.smartFormSelectDataFormat(callback, responseBody)
                        break
                    default:
                        this.$utils.smartFormSelectDataFormat(callback, responseBody)

                }
                if (item.columnName === 'btn-add-submit' || item.columnName === 'btn-edit-submit') {
                    this.$utils.handleSmartResponse(this, responseBody.flag, responseBody.biz_data || responseBody.error_tip, 'editBoxShow')
                }
                // console.log('afterHttpPro===', { item, parent, type, index, model, row, fileData, subFormSelectData }, responseBody, callback)
            },

            /**
             * 智能表格页面所有请求后置操作
             * 例如：请求后的数据包体需要做二次处理
             * @param tableItem 组件对象属性集
             * @param responseBody 响应数据body
            */
            afterHttp({ tableItem, responseBody }) {
                switch (tableItem.columnName) {
                    // 表格
                    case 'table_1608534401189':
                        this.$utils.smartTableDataFormat(tableItem, responseBody)
                        break
                    // 参数类型下拉
                    case 'param_type':
                        responseBody.map(item => {
                            let keys = Object.keys(item)
                            item.label = item[keys[0]]
                            item.value = keys[0]
                            return item
                        })
                        this.$utils.smartTableSelectDataFormat(responseBody)
                        break
                }

            },
            /**
            * 智能表格页面上的按钮的前置操作：包括不限于查询区域，表格顶部、表格操作列
            * 例如：对操作按钮进行处理的数据进行预处理，或者对按钮请求进行个性胡逻辑判断等
            * 注意：本函数有next()回调函数需要执行，调取next()函数才能继续后面的业务逻辑，否则操作将中止
            * @param item 组件对象属性集
            * @param rowObj 当组件为表格操作列中的按钮，此参数返回表格行数据，其它返回undefined
            * @param next 回调函数
           */
            beforeButton({ item, rowObj, next }) {
                // switch (item.columnName) {
                //     // 异常日志
                //     case 'btn-operate-errorLogs':
                //         this.$refs.aspSmartTable.asp_setHidden('btn-operate-errorLogs', true)
                //         next(item, rowObj)
                //         break
                //     default:
                //         next(item, rowObj)
                //         break
                // }
                next(item, rowObj)
            },
            /**
             * 表格内容渲染之前的前置动作
             * @param tableName 当前表格名称
             * @param tableData 表格当页的数据
             * @param columnItem 表格当前列的信息
             * @param scope 表格行信息包含属性 $inde row等
             * @param callBack 参数说明如下
             * 参数一：指定修改的表格名称
             * 参数二：指定修改的列名
             * 参数三：指定索引集合，整列生效则传空数组[],指定某几行生效则传索引集合[1,3]
             * 参数四：显示内容{content：可以是文本也可以是html代码片段}
            */
            beforeTableRender({ tableName, tableData, columnItem, scope }, callBack) {
                switch (scope.row.param_type) {
                    case '1':
                        callBack(tableName, 'param_type', [], { content: '普通变量' })
                        break
                    case '2':
                        callBack(tableName, 'param_type', [], { content: '随机密码' })
                        break
                    case '3':
                        callBack(tableName, 'param_type', [], { content: '统一随机密码' })
                        break
                    case '4':
                        callBack(tableName, 'param_type', [], { content: '自定义密码' })
                        break
                    case 'BIZ_SYS':
                        callBack(tableName, 'param_type', [], { content: '业务系统' })
                        break
                    case 'FUN':
                        callBack(tableName, 'param_type', [], { content: '函数' })
                        break
                    case 'IP':
                        callBack(tableName, 'param_type', [], { content: '目标IP' })
                        break
                    case 'TASK_ID':
                        callBack(tableName, 'param_type', [], { content: '任务实例ID' })
                        break

                }
                if (scope.row.group_relation_list && scope.row.group_relation_list.length > 0) {
                    let relation = ''
                    scope.row.group_relation_list.forEach(val => {
                        relation += val.group_name
                    })
                    callBack(tableName, 'param_group_relation_list', [], { content: relation })

                }
            },

            /**
             * 智能表格监听所有行绘制的前置回调响应
             * @param item 组件对象属性集(类型、组件columnName，组件内元数属性)，columnName是每个组件的唯一标识码
             * @param tableData 表格数据模型
             * @param row:  表格组件当前绘制的行数据
             * @param rowClassName: 子表单组件当前行绘制class name
             * @param callback: 回调api
             * @param           callback回调api参数: rowClassName: 子表单组件当前行绘制class name
            */
            beforeTableRowRender({ item, tableData, row, rowClassName }) {
            },

        }
    }
</script>

<style lang="scss" scoped>
</style>

