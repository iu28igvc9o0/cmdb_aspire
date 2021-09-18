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

    </div>
</template>

<script>
    import tableJson from '../smart_data/cabinetList.json'
    import CommonOption from 'src/utils/commonOption.js'
    import alertCabinet from 'src/services/alert/rb-alert-cabinet-services.factory.js'
    import { createDownloadFile } from 'src/utils/utils.js'
    import YwCodeFrameOption from 'src/components/common/yw-code-frame/yw-code-frame.js'
    import rbCmdbModuleService from 'src/services/cmdb/rb-cmdb-module-service.factory.js'
    import CmdbFactory from 'src/services/auto_operation/rb-auto-operation-cmdb.factory.js'


    export default {
        mixins: [CommonOption, YwCodeFrameOption],
        name: 'FaultList',
        components: {

        },
        data() {
            return {
                // 表格
                formSearch: {},
                tableJson: tableJson,
                modelList: tableJson.model,
                subFormSelectData: [],// 选中的表格行
                idcTypeSelect: [],
                idcTypeList: [],
                roomSelect: [],
                roomList: [],
                changeVal: ''
            }
        },
        created() {
            this.init()
        },
        // SELECT DISTINCT r.id `id`, r.room_code `key`, r.room_code `value` FROM cmdb_idc_resource_relation c, cmdb_room_manager r WHERE c.idc_id = '6d40d7d3-90a7-11e9-bb30-0242ac110002' and r.is_delete = '0' AND r.id = c.room_id ORDER BY r.sort_index
        watch: {
            // 初始化资源池下拉
            idcTypeSelect: {
                immediate: true,
                deep: true,
                handler(val) {
                    if (val && val.length > 0) {
                        this.tableJson.list[0].list[0].options = this.tableJson.list[0].list[0].options.concat(this.idcTypeSelect)
                    }
                }
            },
            // 初始化机房位置下拉
            roomSelect: {
                immediate: true,
                deep: true,
                handler(val) {
                    if (val && val.length > 0) {
                        this.tableJson.list[0].list[1].options = this.tableJson.list[0].list[1].options.concat(this.roomSelect)
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
                            this.tableJson.list[0].list[1].options = res
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
                console.log('this.conditionList', this.conditionList)
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
                    // return res
                })
            },
            // 表单及表格 回调事件
            onbind(data) {
                this.formSearch = this.$utils.deepClone(data.model)
                if (data.type === 'change' && data.item.columnName === 'idcType') {
                    this.changeVal = data.model.idcType
                }
                // 选择行
                if (data.type === 'multipleSelection') {
                    this.subFormSelectData = data.subFormSelectData
                }
                let temp = []
                temp = this.subFormSelectData.map((item) => {
                    return item.id
                })
                switch (data.item.columnName) {
                    // 配置跳转
                    case 'config-cabinet':
                        {
                            this.$router.push({
                                path: '/mirror_alert/cabinet_fault/collocation_page',
                                query: {
                                    id: temp.toString()
                                }
                            })
                        }
                        break
                    // 机柜跳转
                    case 'cabinetCount':
                        {
                            if (data.row.cabinetAlertCount > 0) {
                                this.$router.push({
                                    path: '/mirror_alert/cabinet_fault/cabinet_aleart',
                                    query: {
                                        idcType: data.row.idcType,
                                        roomId: data.row.roomId,
                                        cabinetColumn: data.row.cabinetColumnName,
                                        cabinetCount: data.row.cabinetCount,
                                        deviceCount: data.row.deviceCount,
                                        alertStatus: data.row.alertStatus
                                    }
                                })

                            }
                        }
                        break
                    // 设备跳转
                    case 'deviceCount':
                        {
                            if (data.row.deviceAlertCount > 0) {
                                this.$router.push({
                                    path: '/mirror_alert/cabinet_fault/power_aleart',
                                    query: {
                                        idcType: data.row.idcType,
                                        roomId: data.row.roomId,
                                        cabinetColumn: data.row.cabinetColumnName,
                                        deviceCount: data.row.deviceCount,
                                    }

                                })
                            }
                        }
                        break
                    // 影响业务跳转
                    case 'bizSystemCount':
                        {
                            let countVal = ''
                            if (data.row.alertStatus === 1 || data.row.status === 0) {
                                countVal = data.row.bizSysCount
                            } else {
                                countVal = data.row.bizSystemCount

                            }

                            this.$router.push({
                                path: '/mirror_alert/cabinet_fault/influence_service',
                                query: {
                                    idcType: data.row.idcType,
                                    roomId: data.row.roomId,
                                    cabinetColumn: data.row.cabinetColumnName,
                                    bizSystemCount: countVal,
                                    alertStatus: data.row.alertStatus,
                                }

                            })
                        }
                        break
                    case 'export-cabinet': {
                        if (this.modelList.alertStatus === '') {
                            this.modelList.status = ''
                        } else if (this.modelList.alertStatus === '0') {
                            this.modelList.status = 0
                        } else {
                            this.modelList.status = 1
                        }
                        alertCabinet.exportCabinetColumnAlert(this.modelList).then(res => {
                            if (res) {
                                this.$message.success('下载成功')
                                createDownloadFile(res, '列头柜下电故障数据列表.xls')
                            }


                        })
                    }
                        break
                    case 'open-cabinet': {
                        alertCabinet.updateStatus({
                            id: temp.toString(),
                            status: 1
                        }).then(res => {
                            if (res === 'success') {
                                this.$refs.aspSmartTable.asp_sendTableQuery('table_1605082659220')
                            }
                        })
                    }
                        break
                    case 'close-cabinet': {
                        alertCabinet.updateStatus({
                            id: temp.toString(),
                            status: 0
                        }).then(res => {
                            if (res === 'success') {
                                this.$refs.aspSmartTable.asp_sendTableQuery('table_1605082659220')
                            }
                        })
                    }
                        break
                    case 'open-cabinet-row': {
                        alertCabinet.updateStatus({
                            id: data.row.id,
                            status: 1
                        }).then(res => {
                            if (res === 'success') {
                                this.$refs.aspSmartTable.asp_sendTableQuery('table_1605082659220')
                            }
                        })
                    }
                        break
                    case 'close-cabinet-row': {
                        alertCabinet.updateStatus({
                            id: data.row.id,
                            status: 0
                        }).then(res => {
                            if (res === 'success') {
                                this.$refs.aspSmartTable.asp_sendTableQuery('table_1605082659220')
                            }
                        })
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
            beforeHttp({ tableItem, params, httpMethod, row }) {
                if (params.alertStatus === '') {
                    params.status = ''
                } else if (params.alertStatus == 0) {
                    params.alertStatus = ''
                    params.status = 0
                } else {
                    params.status = 1
                }
                switch (tableItem.columnName) {
                    case 'table_1605082659220':
                        Object.assign(params, {
                            page_no: params.page,
                            page_size: params.rows,
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
                    case 'table_1605082659220':
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
                if (scope.row.deviceAlertCount > 0) {
                    callBack(tableName, 'deviceCount', [], { content: `${scope.row.deviceCount}(电源告警${scope.row.deviceAlertCount})` })

                } else {
                    callBack(tableName, 'deviceCount', [], { content: `<span class="row-word">${scope.row.deviceCount}</span>` })
                }
                if (scope.row.cabinetAlertCount > 0) {
                    callBack(tableName, 'cabinetCount', [], { content: `<a>${scope.row.cabinetCount}(下电${scope.row.cabinetAlertCount})</a>` })
                } else {
                    callBack(tableName, 'cabinetCount', [], { content: `<span class="row-word">${scope.row.cabinetCount}</span>` })

                }
                // if (scope.row.bizSystemCount == 0) {
                //     callBack(tableName, 'bizSystemCount', [], { content: `<span class="row-word">${scope.row.bizSystemCount}</span>` })
                // }
                if (scope.row.alertStatus === 1 || scope.row.status === 0) {
                    callBack(tableName, 'bizSystemCount', [], { content: `${scope.row.bizSysCount}` })
                } else {
                    callBack(tableName, 'bizSystemCount', [], { content: `${scope.row.bizSystemCount}` })
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
                switch (row.alertStatus) {
                    case 1:
                        rowClassName = 'row-bggreen'
                        break
                    case 2:
                        rowClassName = 'row-bgred'
                        break
                    case 3:
                        rowClassName = 'row-bgorange'
                        break
                    case 4:
                        rowClassName = 'row-bgyellow'
                        break
                }
                return rowClassName
            },

        }
    }
</script>

<style lang="scss" scoped>
</style>

