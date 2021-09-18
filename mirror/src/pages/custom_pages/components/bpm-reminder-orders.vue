<template>
    <!-- 服务台人员首页： 催办工单 组件 -->
    <div class="content-chart"
         v-loading="loading">
        <section class="chart-title-wrap clearfix displaybox">
            <div class="chart-title boxflex01">
                <div class="displaybox">
                    <div>催办工单</div>
                    <!-- tab -->
                    <tableTabs :tabList="tabList"
                               :activeIndex="activeIndex"
                               :showNum="false"
                               @handleTabClick="handleTabClick"></tableTabs>
                    <tableTabs :tabList="tabListTwo"
                               :activeIndex="activeIndexTwo"
                               :showNum="false"
                               @handleTabClick="handleTabClickTwo"></tableTabs>
                </div>
            </div>
            <div class="chart-filter">
                <!-- 显示不同区分 -->
                <div class="colorShow">
                    <span class="spanColor spanYellow"></span>
                    <span>预警</span>
                </div>
                <div class="colorShow">
                    <span class="spanColor spanRed"></span>
                    <span>超时</span>
                </div>
                <el-button type="primary"
                           size="small"
                           class="mleft20"
                           @click="pressToDo">
                    <i class="icon iconfont f14">&#xe6a1;</i>催办
                </el-button>
                <el-button size="small"
                           @click="exportList">导出</el-button>
                <el-button type="text"
                           class="mleft20"
                           @click="gotoMore">更多</el-button>
            </div>
        </section>
        <div class="chart-section p10">
            <div class="yw-el-table-wrap hp100 wp100 reminder-orders-list">
                <!-- 告警工单 -->
                <asp-smart-table v-if="activeIndexTwo === 0"
                                 ref="aspSmartTable"
                                 :tableJson="pageJson"
                                 v-model="model"
                                 :beforeTableRowRender="beforeTableRowRender"
                                 :beforeTableRender="beforeTableRender"
                                 :beforeHttp="beforeHttp"
                                 :afterHttp="afterHttp"
                                 @on="onbind">
                </asp-smart-table>
                <!-- 服务请求工单 -->
                <asp-smart-table v-if="activeIndexTwo === 1"
                                 ref="aspSmartTable"
                                 :tableJson="pageJsonRequest"
                                 v-model="pageJsonRequest.model"
                                 :beforeTableRowRender="beforeTableRowRender"
                                 :beforeTableRender="beforeTableRender"
                                 :beforeHttp="beforeHttp"
                                 :afterHttp="afterHttp"
                                 @on="onbind">
                </asp-smart-table>
                <!-- 服务事件工单 -->
                <asp-smart-table v-if="activeIndexTwo === 2"
                                 ref="aspSmartTable"
                                 :tableJson="pageJsonEvent"
                                 v-model="pageJsonEvent.model"
                                 :beforeTableRowRender="beforeTableRowRender"
                                 :beforeTableRender="beforeTableRender"
                                 :beforeHttp="beforeHttp"
                                 :afterHttp="afterHttp"
                                 @on="onbind">
                </asp-smart-table>
            </div>
        </div>
        <!-- 催办弹框 -->
        <el-dialog class="yw-dialog"
                   v-if="dialogBoxShow"
                   title="催办"
                   :visible.sync="dialogBoxShow"
                   :close-on-click-modal="false"
                   :destroy-on-close="true"
                   width="540px">
            <asp-smart-form ref="aspSmartForm"
                            :formJson="pressToDoJson"
                            :beforeHttpPro="beforeHttpPro"
                            :afterHttpPro="afterHttpPro"
                            v-model="pressToDoModel"
                            @on="onbind">
            </asp-smart-form>
        </el-dialog>
    </div>
</template>

<script>
    import pressToDoJson from './smart_data/dialog-press-to-do.json'
    import pageJson from './smart_data/bpm-reminder-orders-list.json'
    import pageJsonRequest from './smart_data/bpm-reminder-orders-list-request.json'
    import pageJsonEvent from './smart_data/bpm-reminder-orders-list-event.json'
    import tableTabs from './sub_components/table-tabs.vue'
    import rbBpmHomeServices from 'src/services/bpm/rb-bpm-home-services.js'
    import { createDownloadFile } from 'src/utils/utils.js'

    export default {
        props: {
            orderType: {
                type: String,
                default: 'all'
            },
            orderName: {
                type: String,
                default: '服务事件工单'
            },
        },
        components: {
            tableTabs
        },
        data() {
            return {
                loading: false,
                pageJsonRequest: pageJsonRequest, // 服务请求列表
                pageJsonEvent: pageJsonEvent, // 服务事件列表
                pageJson: pageJson, // 告警工单列表
                model: pageJson.model,
                pressToDoJson: pressToDoJson,   // 催办弹框
                pressToDoModel: pressToDoJson.model,
                searchWord: '',
                activeIndex: 0,
                activeIndexTwo: 0,
                tabListThree: [
                    {
                        name: '导出'
                    }
                ],
                tabListTwo: [
                    {
                        name: '告警工单',
                        status: 'gjcllc',
                        orderType: 'myList',
                    },
                    {
                        name: '服务请求工单',
                        status: 'fwqqlc',
                        orderType: 'myList',
                    },
                    {
                        name: '服务事件工单',
                        status: 'zxgllc',
                        orderType: 'myList',
                    }
                ],
                tabList: [
                    {
                        name: '待处理',
                        status: 'ToBeProcessed',
                        orderType: 'myList',
                    },
                    {
                        name: '处理中',
                        status: 'Processing',
                        orderType: 'myList',
                    }
                ],
                reqParams: {
                    taskName: 'ToBeProcessed',   // 节点(待处理：ToBeProcessed /处理中：Processing)
                    defKey: 'gjcllc'    // 工单KEY（告警：gjcllc/服务事件：zxgllc/租户请求：fwqqlc）
                }, // tab参数
                dataList: [], // 表格数据
                dialogBoxShow: false,
                rowSelected: [], // 已勾选行
            }
        },
        watch: {
        },
        computed: {
            curTab() {
                return this.tabList[this.activeIndex]
            },
            curTwoTab() {
                return this.tabListTwo[this.activeIndexTwo]
            },
            smartTable() {
                return this.$refs.aspSmartTable
            },
            curTableName() {
                let tableName
                if (this.activeIndexTwo === 1) {
                    tableName = 'bpm_reminder_orders_request'
                } else if (this.activeIndexTwo === 2) {
                    tableName = 'bpm_reminder_orders_event'
                } else {
                    tableName = 'bpm_reminder_orders'
                }
                return tableName
            }
        },
        mounted() {
        },
        methods: {
            // 导出列表
            exportList() {
                this.$message('请稍候')
                rbBpmHomeServices.exportRemindList(this.reqParams).then(res => {
                    if (res) {
                        this.$message.success('请求成功，下载中…')
                        createDownloadFile(res, '催办工单列表文件.xlsx')
                    }
                })
            },
            // 催办按钮
            pressToDo() {
                if (!this.rowSelected.length) {
                    this.$message.warning('请选择工单！')
                    return
                }
                this.dialogBoxShow = true
            },
            // 表单及表格 回调事件
            onbind(data) {
                // data: { item, parent, type, index, model, row, fileData, subFormSelectData }
                console.log(data)
                const routerHash = 'inst/' + (data.row && data.row.id)
                switch (data.item.columnName) {
                    // 取消
                    case 'btn-press-cacel':
                        this.dialogBoxShow = false
                        break
                    // 勾选行 subFormSelectData
                    case this.curTableName:
                        if (data.type === 'multipleSelection') {
                            this.$nextTick(() => {
                                // table_goback_work_selected
                                this.rowSelected = this.handleSelectedRow(data.subFormSelectData)
                            })
                        }
                        break
                    // 列表 工单号
                    case 'id':
                        this.$router.push({
                            path: '/resource/flow',
                            query: {
                                routerHash: routerHash,
                                currentTitle: data.row.id
                            }
                        })
                        break

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
                // osType ，TIMEOUT超时（红色），WARNING预警（橙色），NORMAL空白（白色）
                switch (row.osType) {
                    case 'TIMEOUT':
                        rowClassName = 'row-bgred'
                        break
                    case 'WARNING':
                        rowClassName = 'row-bgorange'
                        break
                }
                return rowClassName
            },
            /**
             * 表格内容渲染之前的前置动作，
             * @param tableName 当前表格名称
             * @param tableData 表格当页的数据
             * @param columnItem 表格当前列的信息
             * @param scope 表格行信息包含属性 $index row等
             * @param callback 回调事件，用于改变指定列的显示内容
             * @param callback 参数说明如下
             * 参数一：指定修改的表格名称
             * 参数二：指定修改的列名
             * 参数三：指定索引集合，整列生效则传空数组[],指定某几行生效则传索引集合[1,3]
             * 参数四：显示内容{ content: 可以是文本也可以是html代码片段}
             */
            beforeTableRender({ tableName, tableData, columnItem, scope }, callBack) {
                // callBack('preferentialList', 'commodityName', [], { content: '查看' })
                let className = 'status-marking'
                switch (scope.row.alertLevel) {
                    case '重大':
                        className += ' bgred'
                        break
                    case '高':
                        className += ' bgorange'
                        break
                    case '中':
                        className += ' bgyellow'
                        break
                    default:
                        className += ' bglightblue'
                        break
                }
                callBack(tableName, 'alertLevel', [], { content: `<span class="${className}">${scope.row.alertLevel}</span>` })
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
                    // 列表
                    case this.curTableName:
                        Object.assign(params, {
                            pageNo: params.page,
                            pageSize: params.rows,
                            // taskName: 'ToBeProcessed',   // 节点(待处理：ToBeProcessed /处理中：Processing)
                            // defKey: 'gjcllc',    // 工单KEY（告警：gjcllc/服务事件：zxgllc/租户请求：fwqqlc）
                        }, this.reqParams)
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
                    default:
                        this.$utils.smartTableDataFormat(tableItem, responseBody, 'rows', 'total')

                }
                // console.log('afterHttp====', tableItem, responseBody)
            },
            // 智能表单页面所有请求前置操作 
            beforeHttpPro({ item, parent, type, index, model, row, fileData, subFormSelectData }, httpObject, callback) {
                // console.log('beforeHttpPro===', { item, parent, type, index, model, row, fileData, subFormSelectData }, httpObject, callback)
                if (item.columnName === 'btn-press-submit') {
                    this.loading = true
                    httpObject.httpBody = Object.assign(httpObject.httpBody, {
                        procInstVoList: this.rowSelected,
                        type: 1,
                    })
                }
                callback(httpObject)
            },
            // 智能表单页面所有请求后置操作
            // 处理返回后的数据格式responseBody，smartLayout 要求格式必须统一为 {data: {}, status: 200}
            afterHttpPro({ item, parent, type, index, model, row, fileData, subFormSelectData }, responseBody, callback) {
                if (item.columnName === 'btn-press-submit') {
                    this.$utils.handleSmartResponse(this, responseBody.state, responseBody.message, 'dialogBoxShow')
                    this.loading = false
                }

                switch (item.columnName) {
                    default:
                        this.$utils.smartFormSelectDataFormat(callback, responseBody, responseBody.rows)
                }
            },
            selectable(row) {
                return row.status === 'running'
            },
            // 获取列表数据 
            search() {
                this.reqParams = {
                    defKey: this.curTwoTab.status,
                    taskName: this.curTab.status,
                }

                this.smartTable.asp_sendTableQuery(this.curTableName)
            },
            // 已选中行数据处理
            handleSelectedRow(list) {
                let arr = []
                list.forEach(item => {
                    let obj = {
                        procDefKey: '',
                        procInstId: ''
                    }
                    obj.procDefKey = this.curTwoTab.status    // 工单类型 tabListTwo status
                    obj.procInstId = item.id
                    arr.push(obj)
                })
                return arr
            },
            handleTabClick(index) {
                this.activeIndex = index
                this.currentPage = 1
                console.log(index)
                this.search()
            },
            handleTabClickTwo(index) {
                this.activeIndexTwo = index
                this.currentPage = 1
                console.log(index)
                this.search()
            },
            // 更多我的工单
            gotoMore() {
                const routerHash = 'flow/supervision'
                this.$router.push({
                    path: '/resource/flow',
                    query: {
                        routerHash: routerHash,
                        currentTitle: '流程集中督办'
                    }
                })
            },
            gotoDetail(row) {
                // 待办 task/2009241132348055991  
                // 已办、我发起的 inst/2009241132348055991
                let routerHash = ''
                if (this.curTab.type === 1) {
                    routerHash = 'task/' + row.id
                } else {
                    routerHash = 'inst/' + row.id
                }
                this.$router.push({
                    path: '/resource/flow',
                    query: {
                        routerHash: routerHash,
                        currentTitle: row.subject
                    }
                })
            },

        }

    } 
</script>

<style lang="scss" scoped>
    .colorShow {
      overflow: hidden;
      .spanColor {
        width: 10px;
        height: 10px;
        border-radius: 50%;
        display: inline-block;
        margin-left: 30px;
        margin-right: 7px;
      }
      .spanYellow {
        background: #ffe3b6;
      }
      .spanRed {
        background: #ffc4d2;
      }
    }
    .reminder-orders-list {
      /deep/ .el-table__body-wrapper {
        height: calc(100% - 28px) !important;
      }
    }
</style>

