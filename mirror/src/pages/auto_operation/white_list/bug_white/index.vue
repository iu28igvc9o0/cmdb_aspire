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
                   title="新增漏洞黑名单"
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
                </asp-smart-form>
            </section>
        </el-dialog>

    </div>
</template>

<script>
    import tableJson from './smart_data/index.json'
    import editJson from './smart_data/hostEdit.json'

    // import cmdbService from 'src/services/cmdb/rb-cmdb-service.factory.js'
    import YwCodeFrameOption from 'src/components/common/yw-code-frame/yw-code-frame.js'
    import rbCmdbModuleService from 'src/services/cmdb/rb-cmdb-module-service.factory.js'
    // import cmdbServiceFactory from 'src/services/cmdb/rb-demand-service.factory.js'

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

                poolList: [],
                idcTypeList: [],
            }
        },
        mixins: [YwCodeFrameOption],
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
                        this.$refs.aspSmartTable.asp_setOptions('pool_id',
                            res)
                    }
                    // return res
                })
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
                switch (data.item.columnName) {
                    case 'add-btn': {
                        this.editShow()
                        break
                    }
                    case 'save-btn': {
                        this.$refs.editForm.asp_resetFields()
                        this.editBoxShow = false
                        break
                    }
                    case 'cacel-btn': {
                        this.$refs.editForm.asp_resetFields()
                        this.editBoxShow = false
                        break
                    }
                }
            },
            beforeHttp({ tableItem, params, httpMethod, row }) {
                switch (tableItem.columnName) {
                    case 'table_1615254040246': {

                        Object.assign(params, {
                            page_no: params.page,
                            page_size: params.rows,
                            creater: sessionStorage.getItem('namespace')
                        })
                        break
                    }
                }
            },
            afterHttp({ tableItem, responseBody }) {
                switch (tableItem.columnName) {
                    case 'table_1615254040246':
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
</style>