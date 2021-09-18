<!--  -->
<template>
    <div class="preview">
        <!-- 条件、列表 -->
        <asp-smart-table ref="aspSmartTable"
                         :tableJson="pageJson"
                         v-model="model"
                         :status="status"
                         :beforeTableRender="beforeTableRender"
                         :beforeHttp="beforeHttp"
                         :afterHttp="afterHttp"
                         @on="onbind">
        </asp-smart-table>

        <el-dialog width="750px"
                   class="yw-dialog"
                   title="巡检指标"
                   v-if="editBoxShow"
                   :visible.sync="editBoxShow"
                   :destroy-on-close="true"
                   :close-on-click-modal="false">
            <section class="yw-dialog-main">
                <asp-smart-form ref="editForm"
                                :formJson="editJson"
                                :status="editStatus"
                                :beforeHttpPro="beforeHttpPro"
                                :afterHttpPro="afterHttpPro"
                                v-model="editModel"
                                @on="onbind">
                    <div slot="select-script">
                        <span>{{scriptName}}</span>
                        <el-button type="primary"
                                   @click="selectScript">选择</el-button>
                    </div>
                </asp-smart-form>
            </section>
            <section class="btn-wrap">
                <el-button type="primary"
                           @click="submit()">确认</el-button>
                <el-button @click="cancel()">取消</el-button>
            </section>

        </el-dialog>

        <el-dialog width="750px"
                   class="yw-dialog"
                   title="选择脚本"
                   v-if="selectScriptShow"
                   :visible.sync="selectScriptShow"
                   :destroy-on-close="true"
                   :close-on-click-modal="false">
            <selectScriptCom @selectScriptData="selectScriptData"
                             @scriptShow="scriptShow"></selectScriptCom>
        </el-dialog>

        <el-dialog class="yw-dialog"
                   title="传参"
                   append-to-body
                   v-if="parameterDialogVisible"
                   :visible.sync="parameterDialogVisible"
                   width="900px">
            <passParamsCom :scriptParamList="scriptParamList"
                           :scriptParam="script_param"
                           @customParam="customParam"
                           @parameterDialog="parameterDialog"></passParamsCom>
        </el-dialog>
    </div>
</template>

<script>
    import pageJson from 'src/pages/auto_operation/inspection/base_target/smart_data/index.json'
    import editJson from 'src/pages/auto_operation/inspection/base_target/smart_data/dialog.json'
    import selectScriptCom from 'src/pages/auto_operation/inspection/system_target/select-script.vue'
    import { createDownloadFile } from 'src/utils/utils.js'

    import rbAutoTargetServicesFactory from 'src/services/auto_operation/rb-auto-target-services.factory.js'
    import rbAutoOperationServicesFactory from 'src/services/auto_operation/rb-auto-operation-services.factory.js'
    import passParamsCom from 'src/pages/auto_operation/components/passParamsCom.vue'

    export default {
        data() {
            return {
                pageJson: pageJson, // 漏洞列表
                model: pageJson.model,
                status: 'create',
                formSearch: {},
                editJson: editJson,   // 新增、编辑漏洞
                editModel: {},
                editStatus: 'add',
                editBoxShow: false,

                selectScriptShow: false,
                multipleSelection: [],
                scriptName: '',
                subFormSelectData: [],

                parameterDialogVisible: false,
                showAddBtn: '',
                scriptParamList: [],
                rowData: {}

            }
        },
        components: {
            selectScriptCom,
            passParamsCom
        },
        created() {

        },
        watch: {
            multipleSelection: {
                handler(val) {
                    if (val && val.length > 0) {
                        this.scriptName = val[0].script_name
                    }
                },
                immediate: true,
                deep: true
            }
        },
        methods: {
            showBox() {
                this.editBoxShow = true
            },
            hideBox() {
                this.editBoxShow = false
            },
            submit() {
                this.$nextTick(() => {
                    if (this.scriptName) {
                        if (this.editStatus === 'add') {
                            this.editModel.prototype_label = 'cruise_baseline'
                            this.editModel.item_type = 'SCRIPT'
                            let obj = {}
                            obj.script_param = this.multipleSelection[0].ops_param_code
                            obj.customize_param = JSON.stringify(this.multipleSelection[0].ops_param_reference_list)
                            this.editModel.script_id = this.multipleSelection[0].script_id
                            this.editModel.script_name = this.multipleSelection[0].script_name
                            this.editModel.script_content_type = this.multipleSelection[0].content_type
                            this.editModel.content_type_desc = this.multipleSelection[0].content_type_desc
                            this.editModel.script_group_name = this.multipleSelection[0].group_name
                            this.editModel.itemExt = obj
                            delete this.editModel.id
                        }
                        this.savePrototypeSave(this.editModel)
                    } else {
                        this.$message.error('请先选择脚本')
                    }
                })
            },
            savePrototypeSave(req) {
                rbAutoTargetServicesFactory.monitorItemPrototypeSave(req).then(res => {
                    if (res.flag === true) {
                        this.$message.success('保存成功！')
                        this.$refs.editForm.asp_resetFields()
                        this.hideBox()
                        this.editModel = {}
                        this.scriptName = ''
                        this.$refs.aspSmartTable.asp_sendTableQuery('table_1611813834570')
                    } else {
                        this.$message.error(res.error_tip)
                    }
                })
            },
            openCloseRow(req) {
                rbAutoTargetServicesFactory.monitorItemPrototypeSave(req).then(res => {
                    if (res.flag === true) {
                        this.$message.success('操作成功！')
                        this.$refs.aspSmartTable.asp_sendTableQuery('table_1611813834570')
                    } else {
                        this.$message.error(res.error_tip)
                    }
                })
            },
            cancel() {
                this.$refs.editForm.asp_resetFields()
                this.editModel = {}
                this.scriptName = ''
                this.hideBox()
            },
            selectScriptData(data) {
                if (data.length) {
                    this.multipleSelection = []
                    this.multipleSelection = data
                    this.editModel.script_id = this.multipleSelection[0].script_id
                    this.editModel.script_name = this.multipleSelection[0].script_name
                    this.editModel.script_content_type = this.multipleSelection[0].content_type
                    this.editModel.content_type_desc = this.multipleSelection[0].content_type_desc
                    this.editModel.script_group_name = this.multipleSelection[0].group_name
                    this.selectScriptShow = false
                } else {
                    this.$message.error('请选择脚本')
                }
            },
            scriptShow(data) {
                this.selectScriptShow = data
            },
            selectScript() {
                this.selectScriptShow = true
            },
            customParam(val) {
                if (val) {
                    this.rowData.item_ext = val
                    this.savePrototypeSave(this.rowData)
                    // let newVal = JSON.parse(val.customize_param)
                    // this.currentTemplateInfo.item_list.forEach(item => {
                    //     if (item.key == newVal[0].entity_id) {
                    //         item.item_ext = val
                    //     }
                    // })
                }
            },
            parameterDialog(val) {
                this.parameterDialogVisible = val
            },
            beforeHttp({ tableItem, params, httpMethod, row }) {
                switch (tableItem.columnName) {
                    case 'table_1611813834570':
                        Object.assign(params, {
                            page_no: params.page,
                            page_size: params.rows,
                            prototype_label: 'cruise_baseline'
                        })
                        break
                }
            },
            afterHttp({ tableItem, responseBody }) {
                switch (tableItem.columnName) {
                    case 'table_1611813834570':
                        responseBody.dataList = responseBody.result
                        responseBody.totalCount = responseBody.count
                        this.$utils.smartTableDataFormat(tableItem, responseBody)
                        break
                }

            },
            beforeButton({ item, rowObj, next }) {
                next(item, rowObj)
            },
            beforeHttpPro({ item, parent, type, index, model, row, fileData, subFormSelectData }, httpObject, callback) {
                callback(httpObject)
            },
            afterHttpPro({ item, parent, type, index, model, row, fileData, subFormSelectData }, responseBody, callback) {
            },
            beforeTableRender({ tableName, tableData, columnItem, scope }, callBack) {
                switch (scope.row.baseline_flag) {
                    case 'Y': {
                        callBack(tableName, 'baseline_flag', [], { content: '是' })
                        break
                    }
                    case 'N': {
                        callBack(tableName, 'baseline_flag', [], { content: '否' })
                        break
                    }
                }
                switch (scope.row.multiline_flag) {
                    case 'Y': {
                        callBack(tableName, 'multiline_flag', [], { content: '是' })
                        break
                    }
                    case 'N': {
                        callBack(tableName, 'multiline_flag', [], { content: '否' })
                        break
                    }
                }
                switch (scope.row.priority) {
                    case '1': {
                        callBack(tableName, 'priority', [], { content: '低' })
                        break
                    }
                    case '2': {
                        callBack(tableName, 'priority', [], { content: '中' })
                        break
                    }
                    case '3': {
                        callBack(tableName, 'priority', [], { content: '高' })
                        break
                    }
                }
                switch (scope.row.expression) {
                    case '==': {
                        callBack(tableName, 'expression', [], { content: '=' })
                        break
                    }
                    case '::contains::': {
                        callBack(tableName, 'expression', [], { content: '包含' })
                        break
                    }
                    case '::cron::': {
                        callBack(tableName, 'expression', [], { content: '正则' })
                        break
                    }
                }
                switch (scope.row.status) {
                    case 'ON': {
                        callBack(tableName, 'status', [], { content: '开启' })
                        break
                    }
                    case 'OFF': {
                        callBack(tableName, 'status', [], { content: '禁用' })
                        break
                    }                }
                switch (scope.row.baseline_standard) {
                    case 'two_Parties': {
                        callBack(tableName, 'baseline_standard', [], { content: '两部委' })
                        break
                    }
                }
            },
            onbind(data) {

                // 选择行
                this.formSearch = this.$utils.deepClone(this.model)

                if (data.type === 'multipleSelection' || data.type === 'tableSelectAll') {
                    this.subFormSelectData = data.subFormSelectData
                }
                let temp = []
                temp = this.subFormSelectData.map((item) => {
                    return item.id
                })
                switch (data.item.columnName) {
                    case 'addAction': {
                        this.editStatus = 'add'
                        this.showBox()
                        break
                    }
                    case 'exportAction': {
                        let obj = Object.assign(this.formSearch, {
                            prototype_label: 'cruise_baseline'
                        })
                        rbAutoTargetServicesFactory.exportMonitorItemPrototypeList(obj).then(res => {
                            if (res) {
                                this.$message.success('下载成功')
                                createDownloadFile(res, '基线巡检列表.xls')
                            }
                        })
                        break
                    }
                    case 'creat-template': {
                        if (this.subFormSelectData && this.subFormSelectData.length > 0) {
                            sessionStorage.setItem('subFormSelectData', JSON.stringify(this.subFormSelectData))
                            this.$router.push({
                                path: '/auto_operation/inspection/template'
                            })
                            // this.$router.push({
                            //     path: '/auto_operation/inspection/template',
                            //     query: {
                            //         rowData: this.subFormSelectData,
                            //         showAdd: true
                            //     }
                            // })
                        } else {
                            this.$message.error('未选择巡检模版，请选择巡检模版')
                        }
                        break
                    }
                    case 'edit-row': {
                        this.editStatus = 'edit'
                        this.editModel = data.row
                        this.scriptName = data.row.script_name
                        this.showBox()
                        break
                    }
                    case 'delete-row': {
                        this.$confirm(' 是否删除该数据?', '提示', {
                            confirmButtonText: '确定',
                            cancelButtonText: '取消',
                            type: 'warning'
                        })
                            .then(() => {
                                rbAutoTargetServicesFactory.monitorItemPrototypeRemove(data.row.id).then(res => {
                                    if (res.flag === true) {
                                        this.$message({
                                            type: 'success',
                                            message: '删除成功!'
                                        })
                                        this.$refs.aspSmartTable.asp_sendTableQuery('table_1611813834570')
                                    } else {
                                        this.$message.error(res.error_tip)
                                    }
                                })
                            })
                            .catch(() => {
                                this.$message({
                                    type: 'info',
                                    message: '已取消删除'
                                })
                            })
                        break
                    }
                    case 'delete-list': {
                        this.$confirm(' 是否删除数据?', '提示', {
                            confirmButtonText: '确定',
                            cancelButtonText: '取消',
                            type: 'warning'
                        })
                            .then(() => {
                                rbAutoTargetServicesFactory.monitorItemPrototypebatchRemove(temp).then(res => {
                                    if (res.flag === true) {
                                        this.$message({
                                            type: 'success',
                                            message: '删除成功!'
                                        })
                                        this.$refs.aspSmartTable.asp_sendTableQuery('table_1611813834570')
                                    } else {
                                        this.$message.error(res.error_tip)
                                    }
                                })
                            })
                            .catch(() => {
                                this.$message({
                                    type: 'info',
                                    message: '已取消删除'
                                })
                            })
                        break
                    }
                    case 'start-row': {
                        this.$router.push({
                            path: '/auto_operation/code_manage/run',
                            query: {
                                scriptId: data.row.script_id,
                                type: 'fromCodeList'
                            }
                        })

                        break
                    }
                    case 'pass-row': {
                        this.showAddBtn = '传参'
                        this.rowData = data.row
                        this.$nextTick(() => {
                            this.script_param = ''
                            this.script_param = data.row.item_ext.script_param
                            if (data.row.item_ext.customize_param) {
                                this.scriptParamList = []
                                this.scriptParamList = JSON.parse(data.row.item_ext.customize_param)
                            } else {
                                rbAutoOperationServicesFactory.queryOpsScriptById(data.row.script_id).then(res => {
                                    this.scriptParamList = []
                                    this.scriptParamList = res.ops_param_reference_list
                                })
                            }
                            this.parameterDialogVisible = true
                        })
                        break
                    }
                    case 'syn-row': {
                        this.$confirm(' 是否同步该数据?', '提示', {
                            confirmButtonText: '确定',
                            cancelButtonText: '取消',
                            type: 'warning'
                        })
                            .then(() => {
                                rbAutoTargetServicesFactory.manualHandleApSchemeExecute(data.row.id).then(res => {
                                    if (res.flag === true) {
                                        this.$message({
                                            type: 'success',
                                            message: '同步成功!'
                                        })
                                        this.$refs.aspSmartTable.asp_sendTableQuery('table_1611813834570')
                                    } else {
                                        this.$message.error(res.error_tip)
                                    }
                                })
                            })
                            .catch(() => {
                                this.$message({
                                    type: 'info',
                                    message: '已取消删除'
                                })
                            })
                        break
                    }
                    case 'open-row': {
                        let obj = {}
                        obj = data.row
                        obj.status = 'ON'
                        this.openCloseRow(obj)
                        break
                    }
                    case 'close-row': {
                        let obj = {}
                        obj = data.row
                        obj.status = 'OFF'
                        this.openCloseRow(obj)
                        break
                    }
                }
            }

        }
    }

</script>
<style lang='scss' scoped>
</style>