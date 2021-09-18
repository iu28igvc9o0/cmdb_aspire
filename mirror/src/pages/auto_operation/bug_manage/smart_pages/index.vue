<template>
    <div class="preview">
        <!-- 条件、列表 -->
        <asp-smart-table ref="aspSmartTable"
                         :tableJson="pageJson"
                         v-model="model"
                         :status="status"
                         :beforeHttp="beforeHttp"
                         :afterHttp="afterHttp"
                         @on="onbind">
        </asp-smart-table>

        <!-- 新增、编辑弹框 -->
        <el-dialog width="750px"
                   class="yw-dialog"
                   title="漏洞信息"
                   v-if="editBoxShow"
                   :visible.sync="editBoxShow"
                   :destroy-on-close="true"
                   :close-on-click-modal="false">
            <div class="yw-dialog-main">
                <asp-smart-form ref="bugEditForm"
                                :formJson="bugEditJson"
                                :status="bugEditStatus"
                                :beforeHttpPro="beforeHttpPro"
                                :afterHttpPro="afterHttpPro"
                                v-model="bugEditModel"
                                @on="onbind">
                </asp-smart-form>
            </div>
        </el-dialog>

        <!-- 关联作业弹框 -->
        <el-dialog width="1100px"
                   class="yw-dialog"
                   title="关联作业"
                   v-if="chooseWorkBoxShow"
                   :visible.sync="chooseWorkBoxShow"
                   :destroy-on-close="true"
                   :close-on-click-modal="false">
            <div class="yw-dialog-main ym-table-height">
                <asp-smart-table ref="chooseWorkTable"
                                 :tableJson="chooseWork"
                                 v-model="chooseWork.model"
                                 :status="status"
                                 :beforeHttp="beforeHttp"
                                 :afterHttp="afterHttp"
                                 @on="onbind">
                </asp-smart-table>
            </div>
        </el-dialog>

        <!-- 关联修复作业弹框 -->
        <el-dialog width="1100px"
                   class="yw-dialog"
                   title="关联修复作业"
                   v-if="chooseFixWorkBoxShow"
                   :visible.sync="chooseFixWorkBoxShow"
                   :append-to-body="true"
                   :destroy-on-close="true"
                   :close-on-click-modal="false">
            <div class="yw-dialog-main ym-table-height">
                <asp-smart-table ref="chooseWorkFixTable"
                                 :tableJson="chooseWorkFix"
                                 v-model="chooseWorkFix.model"
                                 :status="status"
                                 :beforeHttp="beforeHttp"
                                 :afterHttp="afterHttp"
                                 @on="onbind">
                </asp-smart-table>
            </div>
        </el-dialog>

        <!-- 关联回退作业弹框 -->
        <el-dialog width="1100px"
                   class="yw-dialog"
                   title="关联回退作业"
                   v-if="chooseGobackWorkBoxShow"
                   :visible.sync="chooseGobackWorkBoxShow"
                   :append-to-body="true"
                   :destroy-on-close="true"
                   :close-on-click-modal="false">
            <div class="yw-dialog-main ym-table-height">
                <asp-smart-table ref="chooseWorkGobackTable"
                                 :tableJson="chooseWorkGoback"
                                 v-model="chooseWorkGoback.model"
                                 :status="status"
                                 :beforeHttp="beforeHttp"
                                 :afterHttp="afterHttp"
                                 @on="onbind">
                </asp-smart-table>
            </div>
        </el-dialog>
    </div>
</template>

<script>
    import pageJson from 'src/pages/auto_operation/bug_manage/smart_pages/smart_data/index.json'
    import bugEditJson from 'src/pages/auto_operation/bug_manage/smart_pages/smart_data/bug-edit.json'
    import chooseWork from 'src/pages/auto_operation/bug_manage/smart_pages/smart_data/work-selected.json'
    import chooseWorkFix from 'src/pages/auto_operation/bug_manage/smart_pages/smart_data/choose-work-fix.json'
    import chooseWorkGoback from 'src/pages/auto_operation/bug_manage/smart_pages/smart_data/choose-work-goback.json'
    // import rbAutoOperationWorkServicesFactory from 'src/services/auto_operation/rb-auto-operation-work-services.factory.js'
    import bugManageService from 'src/services/auto_operation/rb-auto-operation-bug-manage-services.js'
    import rbAutoOperationMixin from 'src/services/auto_operation/rb-auto-operation-mixin.js'
    import { createDownloadFile } from 'src/utils/utils.js'

    export default {
        name: 'AutoOperationSmartBugManage',
        components: {
        },
        mixins: [rbAutoOperationMixin],
        data() {
            return {
                formSearch: {},
                editBoxShow: false,

                pageJson: pageJson, // 漏洞列表
                model: pageJson.model,
                status: 'create',

                bugEditJson: bugEditJson,   // 新增、编辑漏洞
                bugEditModel: {},
                bugEditStatus: 'add',

                chooseWork: chooseWork, // 已关联作业列表
                chooseWorkFix: chooseWorkFix, // 关联修复作业列表
                chooseWorkGoback: chooseWorkGoback, // 关联回退作业列表
                chooseWorkBoxShow: false,    // 已选择作业
                chooseFixWorkBoxShow: false,    // 选择修复作业
                chooseGobackWorkBoxShow: false,    // 选择回退作业
            }
        },
        watch: {
            // bugEditModel: {
            //     handler(val) {
            //         console.log('bugEditModel===', val, this.bugEditStatus)
            //         if (val.cveNumber || val.cncveNumber || val.cnvdNumber) {
            //             this.bugEditForm.asp_clearValidate('cnvdNumber')
            //         }
            //     },
            //     deep: true,
            //     immediate: true
            // }
        },
        computed: {
            smartTable() {
                return this.$refs.aspSmartTable
            },
            bugEditForm() {
                return this.$refs.bugEditForm
            },
        },
        mounted() {
            // this.model = this.pageJson.model // model 赋值
        },
        methods: {
            /**
             * 智能表格页面所有请求前的前置操作
             * 例如：修改请求参数、修改请求方式、修改请求URL、或者请求条件不满足不给发送请求
             * @param tableItem 组件对象属性集
             * @param params 请求参数body
             * @param httpMethod.url 请求地址URL
             * @param httpMethod.type 请求方式，目前主要三种：'post+json', 'post+form', 'get'
             * @param row 当组件为表格并且是表格操作列触发的请求，此参数返回表格行数据，其它返回undefined
            */
            beforeHttp({ tableItem, params, httpMethod, row }) {
                switch (tableItem.columnName) {
                    // 漏洞列表
                    case 'table_1598940302861':
                        Object.assign(params, {
                            pageNo: params.page,
                            pageSize: params.rows
                        })
                        break
                    // 待选修复作业列表
                    case 'table_fix_work':
                        Object.assign(params, {
                            page_no: params.page,
                            page_size: params.rows,
                            labelId: 'vulnerability'
                        })
                        break
                    // 已选中的修复作业列表
                    case 'table_fix_work_selected':
                        Object.assign(params, {
                            labelId: 'vulnerability',
                            pipelineIdList: tableItem.pipelineIdList || [274]
                        })
                        break
                    // 待选回退作业列表
                    case 'table_goback_work':
                        Object.assign(params, {
                            page_no: params.page,
                            page_size: params.rows,
                            labelId: 'vulnerability_goback'
                        })
                        break
                    // 已选中的修复作业列表
                    case 'table_goback_work_selected':
                        Object.assign(params, {
                            labelId: 'vulnerability_goback',
                            pipelineIdList: tableItem.goBackPipelineIdList
                        })
                        break

                }
                console.log('beforeHttp====', tableItem, params, httpMethod, row)
            },
            /**
             * 智能表格页面所有请求后置操作
             * 例如：请求后的数据包体需要做二次处理
             * @param tableItem 组件对象属性集
             * @param responseBody 响应数据body
            */
            afterHttp({ tableItem, responseBody }) {
                switch (tableItem.columnName) {
                    // 表格列表数据格式必须统一格式：this.$utils.smartTableDataFormat
                    // 危险等级接口：下拉框数据格式{data: [{},{}]}：option字段label、value必须与smartLayout上的设置一致
                    case 'riskLevel':
                        responseBody.map(item => {
                            let keys = Object.keys(item)
                            item.label = item[keys[0]]
                            item.value = keys[0]
                            return item
                        })
                        this.$utils.smartTableSelectDataFormat(responseBody)
                        break
                    // 分组名称接口
                    case 'groupId':
                        this.$utils.smartTableSelectDataFormat(responseBody, responseBody.dataList)
                        break
                    default:
                        this.$utils.smartTableDataFormat(tableItem, responseBody)

                }
                // console.log('afterHttp====', tableItem, responseBody)
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
                console.log('beforeButton===', item, rowObj, next)
                next(item, rowObj)
            },

            // 智能表单页面所有请求前置操作 
            beforeHttpPro({ item, parent, type, index, model, row, fileData, subFormSelectData }, httpObject, callback) {
                // console.log('beforeHttpPro===', { item, parent, type, index, model, row, fileData, subFormSelectData }, httpObject, callback)
                callback(httpObject)
            },
            // 智能表单页面所有请求后置操作
            // 处理返回后的数据格式responseBody，smartLayout 要求格式必须统一为 {data: {}, status: 200}
            afterHttpPro({ item, parent, type, index, model, row, fileData, subFormSelectData }, responseBody, callback) {
                switch (item.columnName) {
                    case 'riskLevel':
                        responseBody.map(item => {
                            let keys = Object.keys(item)
                            item.label = item[keys[0]]
                            item.value = keys[0]
                            return item
                        })
                        this.$utils.smartFormSelectDataFormat(callback, responseBody)
                        break
                    case 'groupId':
                        this.$utils.smartFormSelectDataFormat(callback, responseBody, responseBody.dataList)
                        break
                    default:
                        this.$utils.smartFormSelectDataFormat(callback, responseBody)

                }
                if (item.columnName === 'btn-add-submit' || item.columnName === 'btn-edit-submit') {
                    this.$utils.handleSmartResponse(this, responseBody.flag, responseBody.biz_data || responseBody.error_tip, 'editBoxShow')
                }
                // console.log('afterHttpPro===', { item, parent, type, index, model, row, fileData, subFormSelectData }, responseBody, callback)
            },
            // 表单及表格 回调事件
            onbind(data) {
                // data: { item, parent, type, index, model, row, fileData, subFormSelectData }
                console.log(data.item.columnName)
                this.formSearch = this.$utils.deepClone(data.model)
                switch (data.item.columnName) {
                    // 添加漏洞
                    case 'btn-table-add':
                        this.showBox()
                        this.bugEditStatus = 'add'
                        console.log('this.$refs.bugEditForm111===', this.$refs.bugEditForm)
                        this.$nextTick(() => {
                            this.bugEditModel = this.bugEditJson.model // model 赋值
                            delete this.bugEditModel.id
                            console.log('this.bugEditModel===', this.bugEditModel)
                        })
                        break
                    // 编辑漏洞
                    case 'btn-bug-edit':
                        this.showBox()
                        this.bugEditStatus = 'modify'
                        this.$nextTick(() => {
                            this.bugEditModel = data.row // model 赋值
                            console.log('this.bugEditModel===', this.bugEditModel)
                        })
                        break
                    // 取消编辑漏洞弹框
                    case 'btn-cancel':
                        this.hideBox()
                        break
                    // 提交编辑漏洞弹框
                    case 'btn-edit-submit':
                        this.$message.success('保存成功！')
                        this.hideBox()
                        break
                    // 提交编辑漏洞弹框
                    case 'btn-add-submit':
                        this.$message.success('保存成功！')
                        console.log('this.bugEditForm', this.bugEditForm)
                        console.log('this.bugEditForm', this.bugEditForm.asp_resetFields())
                        this.bugEditForm.asp_resetFields()
                        this.hideBox()
                        break
                    // 关联作业弹框
                    case 'btn-choose-work':
                        this.chooseWorkBoxShow = true
                        this.$nextTick(() => {
                            // table_goback_work_selected
                            // this.$refs.chooseWorkTable.tableModel.table_fix_work_selected = selectedList
                            console.log('btn-choose-work===', this.$refs.chooseWorkTable)
                        })
                        break
                    // 关联修复作业弹框
                    case 'btn-choose-fix-work':
                        this.chooseFixWorkBoxShow = true
                        break
                    // 勾选修复作业 subFormSelectData
                    case 'table_fix_work':
                        if (data.type === 'multipleSelection') {
                            this.$nextTick(() => {
                                // table_goback_work_selected
                                let workSelected = this.$refs.chooseWorkTable.tableModel.table_fix_work_selected
                                let arr = [].concat(workSelected, data.subFormSelectData)
                                this.$refs.chooseWorkTable.tableModel.table_fix_work_selected = this.$utils.reduceArrObj(arr, 'pipeline_id')
                                console.log('table_fix_work===', this.$refs.chooseWorkTable)
                            })
                        }
                        break
                    // 关联回退作业弹框
                    case 'btn-choose-goback-work':
                        this.chooseGobackWorkBoxShow = true
                        break
                    // 导出功能
                    case 'btn-table-export':
                        this.exportList()
                        break
                    case 'submit':
                        break
                }
            },

            showBox() {
                this.editBoxShow = true
            },
            hideBox() {
                this.editBoxShow = false
            },

            // 导出漏洞列表
            exportList() {
                this.$message('请稍候')
                bugManageService.exportVulnerability(this.formSearch).then(res => {
                    if (res) {
                        this.$message.success('请求成功，下载中…')
                        createDownloadFile(res, '漏洞列表文件.xls')
                    }
                })
            },
        }
    }
</script>
