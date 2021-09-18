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
                   title="添加"
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

    import cmdbService from 'src/services/cmdb/rb-cmdb-service.factory.js'
    import YwCodeFrameOption from 'src/components/common/yw-code-frame/yw-code-frame.js'
    import rbCmdbModuleService from 'src/services/cmdb/rb-cmdb-module-service.factory.js'
    import cmdbServiceFactory from 'src/services/cmdb/rb-demand-service.factory.js'

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


                department1List: [],// 一级部门列表
                department2List: [],
                department1Index: [],
                idcTypeList: [],
                bizSystemList: [],
                filterBiz: [],// 过滤一级部门业务系统
                filterBizSys: []// 过滤二级部门业务系统
            }
        },
        mixins: [YwCodeFrameOption],
        computed: {
            // 一级部门列表
            department1ListStore() {
                return this.$store.state.autoOperation.department1List
            },
            // 业务系统列表
            bizSystemListStore() {
                return this.$store.state.autoOperation.bizSystemList
            },
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
                        console.log('res===', res)

                        this.$refs.aspSmartTable.asp_setOptions('poolName',
                            res)
                        this.$refs.editForm.asp_setOptions('poolName',
                            res)
                    }
                    // return res
                })
            },
            // 查询一级部门
            queryDepartment1() {
                let req = {
                    type: 'department1'
                }
                return cmdbService
                    .getDictsByType(req)
                    .then(res => {
                        this.department1List = res
                        this.$refs.aspSmartTable.asp_setOptions('department1',
                            res)
                        this.$store.commit('setDepartment1List', res)
                    })
            },
            // 查询二级部门
            queryDepartment2(index) {
                let req = {
                    type: 'department2',
                    pid: this.department1List[index].id
                }
                cmdbService
                    .getDictsByType(req)
                    .then(res => {
                        this.department2List = this.department2List.concat(res)
                        this.$refs.aspSmartTable.asp_setOptions('department2',
                            this.department2List)
                    })
            },
            // 查询业务系统 moduleType=default_business_system_module_id
            queryBizSystemList() {
                let req = {
                    params: {
                        moduleType: 'default_business_system_module_id'
                    }
                }
                return cmdbService
                    .getRefModuleData(req)
                    .then(res => {
                        this.$refs.aspSmartTable.asp_setOptions('bizSystem',
                            res)
                        this.bizSystemList = []
                        this.bizSystemList = res
                        this.$store.commit('setBizSystemList', res)
                    })
            },
            // 获取业务系统
            getBizSysList($event) {
                this.demand.bizSystem = ''
                let pid = $event.id
                let obj = {
                    'type': 'bizSystem',
                    'pid': pid,
                    'pValue': '',
                    'pType': ''
                }
                cmdbServiceFactory.getConfigDictByType(obj).then((res) => {
                    if (res) {
                        this.$refs.aspSmartTable.asp_setOptions('bizSystem',
                            res)
                        this.$store.commit('setBizSystemList', res)
                    }
                })
            },
            changeSearch(data) {
                this.formSearch.bizSystem = ''
                this.formSearch.bizSystem = data.row.bizSystem
                this.formSearch.bizSystem = this.formSearch.bizSystem.toString()
                this.formSearch.department1 = this.formSearch.department1.toString()
                this.formSearch.department2 = this.formSearch.department2.toString()
                this.formSearch.poolName = this.formSearch.poolName.toString()
                this.formSearch.status = this.formSearch.status.toString()
            },
            onbind(data) {
                this.formSearch = this.$utils.deepClone(data.model)
                switch (data.item.columnName) {
                    case 'department1':
                        {
                            this.department1Index = []
                            this.department2List = []
                            this.filterBiz = []
                            let deepBiz = JSON.parse(JSON.stringify(this.bizSystemListStore))
                            data.model.department2 = ''
                            this.$refs.aspSmartTable.asp_setOptions('department2',
                                this.department2List)
                            this.department1List.forEach((item, index) => {
                                data.model.department1.forEach(items => {
                                    if (item.name === items) {
                                        this.department1Index.push(index)
                                    }
                                })
                            })
                            data.model.department1.forEach(val => {
                                let filVal = deepBiz.filter(bizVal => {
                                    return bizVal.department1 === val
                                })

                                this.filterBiz = this.filterBiz.concat(filVal)
                            })
                            this.$refs.aspSmartTable.asp_setOptions('bizSystem',
                                this.filterBiz)
                            this.department1Index.forEach(items => {
                                this.queryDepartment2(items)
                            })
                            break
                        }
                    case 'department2':
                        {
                            this.filterBizSys = []
                            let deepBizSys = JSON.parse(JSON.stringify(this.filterBiz))
                            data.model.department2.forEach(val => {
                                let filVals = deepBizSys.filter(bizVals => {
                                    return bizVals.department2 === val
                                })
                                this.filterBizSys = this.filterBizSys.concat(filVals)
                            })
                            this.$refs.aspSmartTable.asp_setOptions('bizSystem',
                                this.filterBizSys)
                            break
                        }
                    case 'add-btn': {
                        this.editBoxShow = true
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
                this.queryBizSystemList()
                this.queryDepartment1()
                switch (tableItem.columnName) {
                    case 'table_report': {

                        Object.assign(params, {
                            pageNo: params.page,
                            pageSize: params.rows,
                            bizSystem: params.bizSystem.toString(),
                            department1: params.department1.toString(),
                            department2: params.department2.toString(),
                            poolName: params.poolName.toString(),
                            status: params.status.toString(),
                        })
                        break
                    }
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
            'model.department1'(val) {
                if (val && val.length > 0) {
                    this.filterBiz = []
                    this.bizSystemList = []
                    let deepBiz = JSON.parse(JSON.stringify(this.bizSystemListStore))
                    this.model.department1.forEach(val => {
                        let filVal = deepBiz.filter(bizVal => {
                            return bizVal.department1 === val
                        })
                        this.filterBiz = this.filterBiz.concat(filVal)
                    })
                    this.$refs.aspSmartTable.asp_setOptions('bizSystem',
                        this.filterBiz)
                } else {
                    this.model.bizSystem = []
                    this.$refs.aspSmartTable.asp_setOptions('bizSystem',
                        this.bizSystemListStore)
                }
            },
            'model.department2'(val) {
                if (this.model.department1.length > 0 && val.length === 0) {
                    this.$refs.aspSmartTable.asp_setOptions('bizSystem',
                        this.filterBiz)
                } else if (this.model.department1.length === 0 && val.length === 0) {
                    this.$refs.aspSmartTable.asp_setOptions('bizSystem',
                        this.bizSystemListStore)
                }
            },
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