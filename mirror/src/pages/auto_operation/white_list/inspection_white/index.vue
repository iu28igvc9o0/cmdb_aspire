<!--  -->
<template>
    <div class="preview">

        <asp-smart-table ref="aspSmartTable"
                         :tableJson="tableJson"
                         v-model="model"
                         :beforeHttp="beforeHttp"
                         :afterHttp="afterHttp"
                         :beforeTableRender="beforeTableRender"
                         :before-button="beforeButton"
                         @on="onbind">
        </asp-smart-table>

        <el-dialog width="750px"
                   class="yw-dialog"
                   title="新增巡检黑名单"
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
                    <div slot="inspection-slot">
                        <span class="span-length">{{inspectionName}}</span>
                        <el-button type="primary"
                                   @click="selectInspection">选择</el-button>
                    </div>
                    <div slot="script-slot">
                        <span class="span-length">{{scriptName}}</span>
                        <el-button type="primary"
                                   @click="selectScript">选择</el-button>
                    </div>
                    <div slot="upload-slot">
                        <unploadCom @filePath="filePath"
                                    @deleteFilePath="deleteFilePath"
                                    :filePathVal="filePathVal"></unploadCom>
                    </div>

                </asp-smart-form>
            </section>
        </el-dialog>

        <el-dialog width="90%"
                   class="yw-dialog"
                   title="关联黑名单"
                   v-if="connectBoxShow"
                   :visible.sync="connectBoxShow"
                   :destroy-on-close="true"
                   :close-on-click-modal="false">
            <connectDialog :whitelistId="whitelistId"
                           @showConnect="showConnect"></connectDialog>

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

        <el-dialog width="750px"
                   class="yw-dialog"
                   title="选择巡检"
                   v-if="selectInspectionShow"
                   :visible.sync="selectInspectionShow"
                   :destroy-on-close="true"
                   :close-on-click-modal="false">
            <selectInspectionCom @selectInspectionData="selectInspectionData"
                                 @inspectionShow="inspectionShow"></selectInspectionCom>
        </el-dialog>

        <!-- <el-dialog class="yw-dialog"
                   title="传参"
                   append-to-body
                   v-if="parameterDialogVisible"
                   :visible.sync="parameterDialogVisible"
                   width="900px">
            <passParamsCom :scriptParamList="scriptParamList"
                           :scriptParam="script_param"
                           @customParam="customParam"
                           @parameterDialog="parameterDialog"></passParamsCom>
        </el-dialog> -->

    </div>
</template>

<script>
    import tableJson from './smart_data/index.json'
    import editJson from './smart_data/hostEdit.json'

    // import cmdbService from 'src/services/cmdb/rb-cmdb-service.factory.js'
    import YwCodeFrameOption from 'src/components/common/yw-code-frame/yw-code-frame.js'
    import rbCmdbModuleService from 'src/services/cmdb/rb-cmdb-module-service.factory.js'
    // import cmdbServiceFactory from 'src/services/cmdb/rb-demand-service.factory.js'
    import rbAutoOperationBlackList from 'src/services/auto_operation/rb-auto-operation-black-list.js'
    import fileManageService from 'src/services/auto_operation/rb-auto-operation-file-manage-services.js'

    import connectDialog from '../common/connectDialog.vue'
    import selectScriptCom from 'src/pages/auto_operation/inspection/system_target/select-script.vue'
    import selectInspectionCom from 'src/pages/auto_operation/components/select-inspection.vue'
    import unploadCom from 'src/pages/auto_operation/components/black-upload.vue'

    export default {
        data() {
            return {
                tableJson: tableJson,
                model: tableJson.model,
                formSearch: {},

                editJson: editJson,   // 新增、编辑漏洞
                editModel: {},
                editStatus: 'add',
                editBoxShow: false,

                connectBoxShow: false,
                selectScriptShow: false,
                selectInspectionShow: false,
                idcTypeList: [],
                poolList: [],
                scriptName: '',
                inspectionName: '',
                filePathVal: [],
                whitelistId: '',

                subFormSelectData: [],

            }
        },
        mixins: [YwCodeFrameOption],
        components: {
            connectDialog,
            selectScriptCom,
            selectInspectionCom,
            unploadCom
        },
        computed: {
        },
        created() {
            this.init()
        },
        methods: {
            async init() {
                // 查询级联下拉框字段
                await this.queryConditionList({ condicationCode: 'cond_monitor_screen_view' })
                this.idcTypeList = this.conditionList.filter(item => {
                    return item.key === 'idcType'
                })
                this.getRefModuleDict(this.idcTypeList[0].frameDatas.codeObj.codeId, 'idcType')
            },
            // 引用模型
            getRefModuleDict(item, type) {
                let params = {
                    codeId: item
                }
                return rbCmdbModuleService.getRefModuleDict({ params: params }).then((res) => {
                    if (type === 'idcType') {
                        this.poolList = res
                        this.$refs.aspSmartTable.asp_setOptions('poolName',
                            res)
                    }
                    // return res
                })
            },
            // 选择脚本
            selectScript() {
                this.selectScriptShow = true
            },
            selectScriptData(data) {
                if (data.length) {
                    // let obj = {}
                    // obj.script_param = data[0].ops_param_code
                    // obj.customize_param = JSON.stringify(data[0].ops_param_reference_list)
                    this.editModel.script_id = data[0].script_id
                    this.editModel.script_name = data[0].script_name
                    this.scriptName = data[0].script_name
                    this.editModel.script_content_type = data[0].content_type
                    // this.editModel.content_type_desc = data[0].content_type_desc
                    this.editModel.script_group_name = data[0].group_name
                    // this.editModel.itemExt = obj
                    this.selectScriptShow = false
                } else {
                    this.$message.error('请选择脚本')
                }
            },
            scriptShow(data) {
                this.selectScriptShow = data
            },
            selectInspectionData(data) {
                console.log('data===', data)
                if (data.length) {
                    this.editModel.template_id = data[0].template_id
                    this.editModel.template_name = data[0].template_name
                    this.editModel.item_id = data[0].item_id
                    this.editModel.name = data[0].name
                    this.inspectionName = data[0].template_name
                    // this.editModel.itemExt = obj
                    this.selectInspectionShow = false
                } else {
                    this.$message.error('请选择巡检指标')
                }

            },
            selectInspection() {
                this.selectInspectionShow = true
            },
            inspectionShow(data) {
                this.selectInspectionShow = data
            },
            filePath(data) {
                this.filePathVal.push(data)
            },
            deleteFilePath(data) {
                this.filePathVal.forEach(item => {
                    if (item.indexOf(data) > -1) {
                        this.filePathVal.splice(item, 1)
                    }
                })
            },
            showConnect(data) {
                this.connectBoxShow = data
            },
            editShow() {
                this.editBoxShow = true
                this.$nextTick(() => {
                    this.$refs.editForm.asp_setOptions('pool_id',
                        this.poolList)
                })

            },
            onbind(data) {
                this.formSearch = this.$utils.deepClone(data.model)
                if (data.type === 'multipleSelection' || data.type === 'tableSelectAll') {
                    this.subFormSelectData = data.subFormSelectData
                }

                switch (data.item.columnName) {
                    case 'edit-row': {
                        this.editShow()
                        this.editModel = data.row
                        this.scriptName = data.row.script_name
                        this.scriptName = data.row.script_name
                        this.inspectionName = data.row.template_name
                        this.filePathVal = JSON.parse(data.row.attachment)
                        break
                    }
                    case 'connect-row': {
                        this.whitelistId = data.row.id
                        this.connectBoxShow = true
                        break
                    }
                    case 'delete-row': {
                        rbAutoOperationBlackList.removeWhitelistCruiseCheckById(data.row.id).then(res => { })
                        break
                    }
                    case 'exc-row': {
                        JSON.parse(data.row.attachment).forEach(item => {
                            fileManageService.downloadFile({ file_path: item, is_relative: 'Y' }).then(res => {
                                if (res.byteLength > 0) {
                                    this.$message.success('下载成功')
                                    let pathArr = item.split('/')
                                    let filename = pathArr[pathArr.length - 1]
                                    this.$utils.createDownloadFileBlob(res, filename)
                                }
                            })
                        })
                        break
                    }
                    case 'add-btn': {
                        this.editShow()
                        break
                    }
                    case 'save-btn': {
                        if (this.scriptName && this.inspectionName) {
                            this.editModel.whitelist_type = 'cruisecheck'
                            this.editModel.attachment_list = this.filePathVal
                            rbAutoOperationBlackList.saveWhitelistCruiseCheck(this.editModel).then(res => {
                                if (res.flag === true) {
                                    this.$refs.editForm.asp_resetFields()
                                    this.editModel = {}
                                    this.scriptName = ''
                                    this.inspectionName = ''
                                    this.editBoxShow = false
                                    this.$refs.aspSmartTable.asp_sendTableQuery('table_1614754213340')
                                }
                            })
                        } else {
                            this.$message.error('请选择脚本或巡检')
                        }
                        break
                    }
                    case 'cacel-btn': {
                        this.$refs.editForm.asp_resetFields()
                        this.editModel = {}
                        this.scriptName = ''
                        this.inspectionName = ''
                        this.editBoxShow = false
                        break
                    }
                    case 'connect-btn': {
                        if (this.subFormSelectData && this.subFormSelectData.length > 0) {
                            this.whitelistId = ''
                            this.subFormSelectData.forEach(item => {
                                if (this.whitelistId) {
                                    this.whitelistId += ',' + item.id
                                } else {
                                    this.whitelistId = item.id
                                }
                            })
                            this.connectBoxShow = true
                        } else {
                            this.$message.error('未选择黑名单，请选择黑名单')
                        }
                        break
                    }
                    // case 'pool_id': {
                    //     this.editModel.pool_name = data.model.key
                    //     break
                    // }
                }
            },
            beforeHttp({ tableItem, params, httpMethod, row }) {
                switch (tableItem.columnName) {
                    case 'table_1614754213340': {

                        Object.assign(params, {
                            page_no: params.page,
                            page_size: params.rows,
                            pool_id: params.pool_id.toString(),
                            creater: sessionStorage.getItem('namespace')
                        })
                        break
                    }
                }
            },
            afterHttp({ tableItem, responseBody }) {
                switch (tableItem.columnName) {
                    case 'table_1614754213340':
                        // responseBody.dataList = responseBody.result
                        // responseBody.totalCount = responseBody.count
                        this.$utils.smartTableDataFormat(tableItem, responseBody)
                        break
                }
            },
            beforeHttpPro({ item, parent, type, index, model, row, fileData, subFormSelectData }, httpObject, callback) {
                callback(httpObject)
            },
            afterHttpPro({ item, parent, type, index, model, row, fileData, subFormSelectData }, responseBody, callback) {
            },
            beforeButton({ item, rowObj, next }) {
                next(item, rowObj)
            },
            beforeTableRender({ tableName, tableData, columnItem, scope }, callBack) {
                switch (scope.row.status) {
                    case 'ON': {
                        callBack(tableName, 'status', [], { content: '启用' })
                        break
                    }
                    case 'OFF': {
                        callBack(tableName, 'status', [], { content: '禁用' })
                        break
                    }
                }

            },
        },
        watch: {
        }
    }

</script>
<style lang='scss' scoped>
    /deep/ .el-tag:nth-of-type(1).el-tag--info {
        width: 76px !important;
        text-overflow: ellipsis !important; /*让截断的文字显示为点点。还有一个值是clip意截断不显示点点*/
        white-space: nowrap !important; /*让文字不换行*/
        overflow: hidden !important; /*超出要隐藏*/
    }

    .span-length {
        display: inline-block;
        white-space: nowrap;
        width: 50%;
        overflow: hidden !important;
        text-overflow: ellipsis !important;
    }
</style>