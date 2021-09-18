<!--  -->
<template>
    <div class="preview">
        <!-- 条件、列表 -->
        <asp-smart-table ref="indexTable"
                         :tableJson="pageJson"
                         v-model="model"
                         :beforeTableRender="beforeTableRender"
                         :beforeHttp="beforeHttp"
                         :afterHttp="afterHttp"
                         @on="onbind">
        </asp-smart-table>

        <!-- <el-dialog width="1150px"
                   class="yw-dialog"
                   title="详情"
                   v-if="detailBoxShow"
                   :visible.sync="detailBoxShow"
                   :destroy-on-close="true"
                   :close-on-click-modal="false">
            <section class="yw-dialog-main">
                <asp-smart-table ref="detailTable"
                                 style="height:500px"
                                 :tableJson="detailJson"
                                 v-model="detailModel"
                                 :beforeTableRender="beforeTableRender"
                                 :beforeHttp="beforeHttp"
                                 :afterHttp="afterHttp"
                                 @on="onbind">
                </asp-smart-table>
            </section>
        </el-dialog> -->
        <!-- <el-dialog width="750px"
                   class="yw-dialog"
                   title="TON报表类型配置"
                   v-if="topNBoxShow"
                   :visible.sync="topNBoxShow"
                   :destroy-on-close="true"
                   :close-on-click-modal="false">
            <section class="yw-dialog-main">
                <el-form class="yw-form is-required"
                         :model="configureForm"
                         ref="configureForm"
                         :rules="rules"
                         label-width="120px">
                    <el-form-item label="TON排名："
                                  prop="top">
                        <el-input placeholder="请输入排名"
                                  v-model="configureForm.top"
                                  type="number"
                                  @change="toInteger"
                                  style="width:178px"></el-input>
                    </el-form-item>
                    <el-form-item label="指标名称：">
                        <div class="yw-form">
                            <div class="table-operate-wrap clearfix">
                                <el-button @click="showDialogBox()">选择端口指标</el-button>
                            </div>
                            <div class="yw-el-table-wrap">
                                <el-table :data="targetList"
                                          class="yw-el-table"
                                          stripe
                                          highlight-current-row
                                          tooltip-effect="dark"
                                          border>
                                    <el-table-column prop="creater"
                                                     label="指标名称"></el-table-column>
                                    <el-table-column prop="updater"
                                                     label="默认排序"></el-table-column>
                                    <el-table-column label="操作"
                                                     width="130">
                                        <template slot-scope="scope">
                                            <div class="yw-table-operator">
                                                <el-button type="text"
                                                           @click="removeRow(scope.row)">删除</el-button>
                                                <el-button type="text"
                                                           @click="sortRow(scope.row)">默认排序</el-button>
                                            </div>
                                        </template>
                                    </el-table-column>
                                </el-table>
                            </div>
                        </div>

                    </el-form-item>
                    <el-form-item label="展示字段：">
                        <div class="yw-el-table-wrap">
                            <el-table :data="fieldList"
                                      class="yw-el-table"
                                      stripe
                                      highlight-current-row
                                      tooltip-effect="dark"
                                      border>
                                <el-table-column prop="creater"
                                                 label="字段名称"></el-table-column>
                                <el-table-column prop="updater"
                                                 label="展示序号"></el-table-column>
                            </el-table>
                        </div>
                    </el-form-item>
                </el-form>
            </section>
            <section class="btn-wrap">
                <el-button type="primary"
                           @click="submit()">确认</el-button>
                <el-button @click="cancel()">取消</el-button>
            </section>
        </el-dialog> -->

        <el-drawer title="详情"
                   class="yw-dialog"
                   v-if="detailBoxShow"
                   :visible.sync="detailBoxShow"
                   :with-header="false"
                   :direction="rtl"
                   size="70%">
            <section class="yw-dialog-main">
                <asp-smart-table ref="detailTable"
                                 style="height:500px"
                                 :tableJson="detailJson"
                                 v-model="detailModel"
                                 :beforeTableRender="beforeTableRender"
                                 :beforeHttp="beforeHttp"
                                 :afterHttp="afterHttp"
                                 @on="onbind">
                </asp-smart-table>
            </section>
        </el-drawer>

        <el-drawer title="TON报表类型配置"
                   class="yw-dialog"
                   v-if="topNBoxShow"
                   :visible.sync="topNBoxShow"
                   :direction="rtl"
                   size="50%">
            <section class="yw-dialog-main">
                <el-form class="yw-form is-required"
                         :model="configureForm"
                         ref="configureForm"
                         :rules="rules"
                         label-width="120px">
                    <el-form-item label="TON排名："
                                  prop="top">
                        <el-input placeholder="请输入排名"
                                  v-model="configureForm.top"
                                  type="number"
                                  @change="toInteger"
                                  style="width:178px"></el-input>
                    </el-form-item>
                    <el-form-item label="指标名称：">
                        <div class="yw-form">
                            <div class="table-operate-wrap clearfix">
                                <el-button @click="showDialogBox()">选择端口指标</el-button>
                            </div>
                            <div class="yw-el-table-wrap">
                                <el-table :data="targetList"
                                          class="yw-el-table"
                                          stripe
                                          highlight-current-row
                                          tooltip-effect="dark"
                                          border>
                                    <el-table-column prop="creater"
                                                     label="指标名称"></el-table-column>
                                    <el-table-column prop="updater"
                                                     label="默认排序"></el-table-column>
                                    <el-table-column label="操作"
                                                     width="130">
                                        <template slot-scope="scope">
                                            <div class="yw-table-operator">
                                                <el-button type="text"
                                                           @click="removeRow(scope.row)">删除</el-button>
                                                <el-button type="text"
                                                           @click="sortRow(scope.row)">默认排序</el-button>
                                            </div>
                                        </template>
                                    </el-table-column>
                                </el-table>
                            </div>
                        </div>

                    </el-form-item>
                    <el-form-item label="展示字段：">
                        <div class="yw-el-table-wrap">
                            <el-table :data="fieldList"
                                      class="yw-el-table"
                                      stripe
                                      highlight-current-row
                                      tooltip-effect="dark"
                                      border>
                                <el-table-column prop="creater"
                                                 label="字段名称"></el-table-column>
                                <el-table-column prop="updater"
                                                 label="展示序号"></el-table-column>
                            </el-table>
                        </div>
                    </el-form-item>
                </el-form>
            </section>
            <section class="btn-wrap">
                <el-button type="primary"
                           @click="submit()">确认</el-button>
                <el-button @click="cancel()">取消</el-button>
            </section>
        </el-drawer>

        <!-- 选择指标 -->
        <selectTargetCom v-if="selectBox.dialogVisible"
                         :selectBox="selectBox"
                         @closeDialog="closeDialog"></selectTargetCom>

    </div>
</template>

<script>
    import pageJson from 'src/pages/netconfigs/port/smart_data/index.json'
    import detailJson from 'src/pages/netconfigs/port/smart_data/detail.json'

    import selectTargetCom from './selectTargetCom.vue'

    import YwCodeFrameOption from 'src/components/common/yw-code-frame/yw-code-frame.js'
    import rbCmdbModuleService from 'src/services/cmdb/rb-cmdb-module-service.factory.js'
    import CmdbFactory from 'src/services/auto_operation/rb-auto-operation-cmdb.factory.js'

    //  import { createDownloadFile } from 'src/utils/utils.js'
    // import rbNetContrastServicesFactory from 'src/services/net/rb-net-contrast-services.factory.js'

    export default {
        mixins: [YwCodeFrameOption],
        data() {
            return {
                // 列表
                pageJson: pageJson,
                model: pageJson.model,
                formSearch: {},
                // 详情
                detailJson: detailJson,
                detailModel: detailJson.model,
                detailBoxShow: false,
                // 报表配置
                topNBoxShow: false,
                targetList: [{ creater: '111', updater: '是' }, { creater: '222', updater: '否' }, { creater: '333', updater: '否' }, { creater: '444', updater: '否' }],
                fieldList: [],
                configureForm: {
                    top: ''
                },
                rules: {
                    top: [{ required: true, message: '请输入排名', trigger: 'blur' }]
                },
                // 选择指标
                selectBox: {
                    dialogVisible: false,
                    data: []
                },
                // 查询
                idcTypeSelect: [],
                idcTypeList: [],
                roomSelect: [],
                roomList: [],
                changeVal: ''
            }
        },
        components: {
            selectTargetCom
        },
        created() {
            this.init()
        },
        watch: {
            // 初始化资源池下拉
            idcTypeSelect: {
                immediate: true,
                deep: true,
                handler(val) {
                    if (val && val.length > 0) {
                        this.$refs.indexTable.asp_setOptions('idcType', this.idcTypeSelect)
                    }
                }
            },
            // 初始化机房位置下拉
            roomSelect: {
                immediate: true,
                deep: true,
                handler(val) {
                    if (val && val.length > 0) {
                        this.$refs.indexTable.asp_setOptions('roomId', this.roomSelect)
                    }
                }
            },
            // 机房级联效果
            changeVal: {
                immediate: true,
                deep: true,
                handler(val) {
                    if (val) {
                        let list = this.idcTypeSelect.filter(item => {
                            if (item.key == val) {
                                return item
                            }
                        })
                        this.getCondicationGetList = this.getCondicationGetList.filter(item => {
                            return item.cmdbCode.filedCode === 'idcType'
                        })
                        let sqlList = this.getCondicationGetList[0].cmdbCode.cascadeList.filter(item => {
                            return item.subFiledCode === 'roomId'
                        })
                        let sqlstrt = sqlList[0].sqlString.replace('?', list[0].id)
                        CmdbFactory.queryTableData({ sql: sqlstrt }).then(res => {
                            this.$refs.indexTable.asp_setOptions('roomId', res)

                        })
                    } else {
                        this.getRefModuleDict(this.roomList[0].frameDatas.codeObj.codeId, 'roomId')
                    }
                }
            }
        },
        methods: {
            // 初始化
            async init() {
                // 查询级联下拉框字段
                await this.queryConditionList({ condicationCode: 'cond_monitor_screen_view' })
                this.idcTypeList = this.conditionList.filter(item => {
                    return item.key === 'idcType'
                })
                this.roomList = this.conditionList.filter(item => {
                    return item.key === 'roomId'
                })
                this.getRefModuleDict(this.idcTypeList[0].frameDatas.codeObj.codeId, 'idcType')
                this.getRefModuleDict(this.roomList[0].frameDatas.codeObj.codeId, 'roomId')
            },
            // 引用模型
            getRefModuleDict(item, type) {
                let params = {
                    codeId: item
                }
                return rbCmdbModuleService.getRefModuleDict({ params: params }).then((res) => {
                    if (type === 'idcType') {
                        this.idcTypeSelect = res
                    } else {
                        this.roomSelect = res
                    }
                })
            },
            toInteger() {
                let reg = /^[0-9]+$/
                if (!reg.test(this.configureForm.top)) {
                    this.$message.warning('只能输入正整数')
                    // 用以在dom渲染挂载后重新触发dom渲染挂载
                    this.$nextTick(() => {
                        this.configureForm.top = parseInt(this.configureForm.top)
                    })
                }
            },
            submit() {
                this.$refs.configureForm.validate((valid) => {
                    if (valid) {
                        this.$refs.configureForm.resetFields()
                    } else {
                        return false
                    }
                })
            },
            cancel() {
                this.$refs.configureForm.resetFields()
                this.topNBoxShow = false
            },
            showDialogBox() {
                this.selectBox.dialogVisible = true
            },
            // 关闭选择弹窗
            closeDialog(date) {
                this.selectBox.dialogVisible = false
                //                 if (date && date.type === 'update') {
                //      确认的操作
                //     console.log('date.userResult===', date.userResult)
                //     this.dialogUser.date = []
                //     if (this.currentUserFlag) {
                //         let a = []
                //         a.push(this.myuser)
                //         console.log(a)
                //         this.addForm.defensetor = []
                //         this.addForm.defensetor = a.concat(date.userResult)
                //     } else {
                //         this.addForm.defensetor = []
                //         this.addForm.defensetor = date.userResult
                //     }
                //     this.dialogUser.date = date.userResult
                // }

            },
            // 删除
            removeRow(row) {
                this.targetList.splice(row, 1)
            },
            // 默认排序
            sortRow(row) {
                row.updater = '是'
                this.targetList.forEach(item => {
                    if (item.creater !== row.creater && item.updater === '是') {
                        item.updater = '否'
                    }
                })
            },
            beforeHttp({ tableItem, params, httpMethod, row }) {
                switch (tableItem.columnName) {
                    case 'table_1611813834570':
                        Object.assign(params, {
                            page_no: params.page,
                            page_size: params.rows,
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
            },
            onbind(data) {
                // 选择行
                // this.formSearch = this.$utils.deepClone(this.model)
                // if (data.type === 'multipleSelection') {
                //     this.subFormSelectData = data.subFormSelectData
                // }
                // let temp = []
                // temp = this.subFormSelectData.map((item) => {
                //     return item.id
                // })
                if (data.type === 'change' && data.item.columnName === 'idcType') {
                    this.changeVal = data.model.idcType
                    data.model.roomId = ''
                }
                switch (data.item.columnName) {
                    case 'exprot-btn': {
                        break
                    }
                    case 'topN-configure': {
                        this.topNBoxShow = true
                        break
                    }
                }
            }

        }
    }

</script>
<style lang='scss' scoped>
    /deep/ input::-webkit-outer-spin-button,
    /deep/ input::-webkit-inner-spin-button {
        -webkit-appearance: none !important;
    }
    /deep/ input[type="number"] {
        -moz-appearance: textfield !important;
    }

    .btn-wrap {
        position: absolute;
        bottom: 20px;
        left: 20px;
    }
</style>