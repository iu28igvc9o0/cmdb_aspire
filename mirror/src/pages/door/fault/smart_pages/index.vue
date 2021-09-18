<template>
    <div class="preview">
        <!-- 条件、列表 -->
        <asp-smart-table ref="aspSmartTable"
                         :tableJson="tableJson"
                         v-model="model"
                         :beforeHttp="beforeHttp"
                         :afterHttp="afterHttp"
                         :beforeTableRender="beforeTableRender"
                         :beforeTableRowRender="beforeTableRowRender"
                         :before-button="beforeButton"
                         @on="onbind">
        </asp-smart-table>

        <!-- 弹窗：数据编辑 v-if="dialogData.dialogVisible" -->
        <DialogDataEdit @closeDialog="(data)=>{hideDialog(dialogData,data)}"
                        :dialogMsg="dialogData"
                        @editData="editData"></DialogDataEdit>

        <!-- 弹窗：计划编辑 -->
        <DialogPlanEdit @closeDialog="(data)=>{hideDialog(dialogPlan,data)}"
                        :dialogMsg="dialogPlan"
                        @editData="editData"></DialogPlanEdit>
        <!-- 导入 -->
        <YwImport v-if="display.isImport"
                  :showImport="display.isImport"
                  @setImportDisplay="closeParent"
                  :importType="importType"
                  :importId="importId"></YwImport>
        <!-- [{'filecode':id,'filename':故障id}] -->
    </div>
</template>

<script>
    import tableJson from './smart_data/faultList.json'
    import CommonOption from 'src/utils/commonOption.js'
    import rbFaultServices from 'src/services/door/rb-fault-services.factory.js'
    import DialogDataEdit from './dialog-data-edit.vue'
    import DialogPlanEdit from './dialog-plan-edit.vue'
    import { FAULT_TABLE_HEAD } from 'src/assets/js/constant/rb-mirror-mirror.constant.js'

    export default {
        mixins: [CommonOption],
        name: 'FaultList',
        components: {
            DialogDataEdit,
            DialogPlanEdit,
            YwImport: () => import('src/components/common/yw-import.vue')

        },
        data() {
            return {
                // 表格
                formSearch: {},
                exportData: {},
                tableJson: tableJson,
                model: tableJson.model,
                subFormSelectData: [],// 选中的表格行
                // dialog
                dialogData: {
                    dialogVisible: false,
                    data: {}
                },
                dialogPlan: {
                    dialogVisible: false,
                    data: {}
                },
                display: {
                    isImport: false
                },
                importType: 'fault',
                idList: '',
                tableHead: []
            }
        },


        methods: {
            // 表单及表格 回调事件
            onbind(data) {
                this.formSearch = this.$utils.deepClone(data.model)
                if (data.type === 'multipleSelection') {
                    this.subFormSelectData = data.subFormSelectData
                }
                console.log(data.item)
                switch (data.item.columnName) {

                    // 数据编辑
                    case 'btn-table-data-edit':
                        {

                            // let temp = this.subFormSelectData.map((item) => {
                            //     return item.id
                            // })
                            // this.deleteDatas(temp)
                            this.showDialog(this.dialogData, data.row)
                        }

                        break
                    // 后续计划编辑
                    case 'btn-table-plan-edit':
                        this.showDialog(this.dialogPlan, data.row)
                        break
                    // 数据导出
                    case 'btn-table-data-export':
                        this.exportList()
                        break
                    // 报告导出
                    case 'btn-table-report-export':
                        this.exportReport()
                        break
                    // 数据导入
                    case 'btn-table-data-import':
                        if (this.subFormSelectData && this.subFormSelectData.length > 0) {
                            this.importId = this.subFormSelectData.map((item) => {
                                return item.id
                            })
                        }
                        this.display.isImport = true
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
            beforeHttp({ tableItem, params, httpMethod, row }) {
                switch (tableItem.columnName) {
                    // 
                    case 'table_fault':
                        Object.assign(params, {
                            pageNum: params.page,
                            pageSize: params.rows,
                            // startTime: formatDate(params.startTime),
                            // endTime: formatDate(params.endTime),
                            // status: params.status
                        })
                        break
                }
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
                    case 'table_fault':
                        // responseBody.dataList = responseBody.data.result.map((item) => {
                        //     if (item.receiverName && item.receiverName !== 'null') {
                        //         item.receiverMobile = `${item.receiverName}<${item.receiverMobile}>`
                        //     }
                        //     return item
                        // })
                        responseBody.dataList = responseBody.result
                        responseBody.totalCount = responseBody.count

                        this.$utils.smartTableDataFormat(tableItem, responseBody)
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
                // let img = 'email-success.png'
                // switch (scope.row.status) {
                //     case 0:
                //         img = 'email-error.png'
                //         break
                //     default:
                //         img = 'email-success.png'
                //         break
                // }
                // callBack(tableName, 'status', [], { content: `<img src="/static/img/sms/${img}">` })
            },
            beforeTableRowRender({ item, tableData, row, rowClassName }) {
                if (row.faultReportTimely === '2' || row.reportTimely === '2') {
                    return rowClassName = 'row-bgred'
                } else {
                    return rowClassName = 'row-border-right'
                }
            },

            showDialog(dialog, data) {
                dialog.dialogVisible = true
                dialog.data = data
            },
            hideDialogTags(dialog, data) {
                dialog.dialogVisible = false
                if (data && data.type === 'update') {
                    // 保存的操作

                }

            },
            // 导出故障列表
            exportList() {
                this.$message('请稍候')
                rbFaultServices.exportFaultData(this.formSearch).then(res => {
                    if (res.code === '0000') {
                        this.$message.success('请求成功，下载中…')
                        let downLoadElement = document.createElement('a')
                        downLoadElement.href = res.path
                        downLoadElement.setAttribute('download', '故障列表.xlsx')
                        document.body.appendChild(downLoadElement)
                        downLoadElement.click()
                        document.body.removeChild(downLoadElement)
                    } else {
                        this.$message.error(res.message)
                    }
                })
            },
            // this.smartTable.asp_sendTableQuery('table_fault')
            editData(val) {
                if (val) {
                    this.smartTable.asp_sendTableQuery('table_fault')
                }
            },
            // 导出故障报告
            exportReport() {
                let temp = this.subFormSelectData.map((item) => {
                    return item.id
                })
                this.$message('请稍候')
                rbFaultServices.exportFaultReport(temp).then(res => {
                    if (res) {
                        this.$message.success('请求成功，下载中…')
                        let downLoadElement = document.createElement('a')
                        downLoadElement.href = res.path
                        downLoadElement.setAttribute('download', '故障报告.zip')
                        document.body.appendChild(downLoadElement)
                        downLoadElement.click()
                        document.body.removeChild(downLoadElement)
                    } else {
                        this.$message.error(res.message)
                    }
                })
            },
            // 动态列
            getOrInsertColumnFilter() {
                // let params = {
                //     'faultId': true, 'faultReportUser': true,
                //     'faultReporMobile': true, 'faultReportEmail': true,
                //     'faultReportBizsys': true, 'faultReportTime': true,
                //     'faultHappenTime': true, 'faultReportTimely': true,
                //     'faultIdcType': true, 'faultOrderId': true,
                //     'faultContent': true, 'faultRegtime': true,
                //     'reportTitle': true, 'reportUser': true,
                //     'reportMobile': true, 'reportEmail': true,
                //     'reportBizsys': true, 'reportWnId': true,
                //     'reportOrderId': true, 'reportCreateTime': true,
                //     'reportPlainFinish': true, 'reportFinishTime': true,
                //     'reportTimely': true, 'reportPlatformRecoverytime': true,
                //     'reportPlatformRecoveryhours': true, 'reportBizsysRecoverytime': true,
                //     'reportBizsysRecoveryhours': true, 'reportAffectBizsyss': true,
                //     'reportAffectDescribe': true, 'reportType': true,
                //     'reportResson': true, 'reportProducer': true,
                //     'reportNature': true, 'reportDeductPoints': true,
                //     'reportEnclosure': true, 'reportFollowPlan': true,
                //     'reportFollowPlanExplain': true, 'reportRemark': true
                // }
                // let data = {
                //     columnMap: params,
                //     columnInfo: JSON.stringify(params),
                //     id: '611f70b0d0024462aae2bc9161743ade',
                //     insertTime: 1605801600000,
                //     menuType: 'fault',
                //     moduleId: 'fault',
                //     updateTime: 1605801600000
                // }
                rbFaultServices.getOrInsertColumnFilter().then(res => {

                })
                // rbFaultServices.updateColumnFilter(data).then(res => {
                //     console.log('动态===', res)
                // })

            }
        },
        created() {
            // this.getOrInsertColumnFilter()
            this.tableHead = FAULT_TABLE_HEAD
            console.log(this.tableHead)
        }
    }
</script>

<style lang="scss" scoped>
    .el-table--border td {
        border-right: none !important;
    }
</style>
