<template>
    <!-- 服务台人员首页： 我的工单 组件 -->
    <div class="content-chart">
        <section class="chart-title-wrap clearfix displaybox">
            <div class="chart-title boxflex01">
                <div class="displaybox">
                    <div>我的工单</div>
                    <!-- tab -->
                    <tableTabs :tabList="tabList"
                               :activeIndex="activeIndex"
                               @handleTabClick="handleTabClick"></tableTabs>
                </div>
            </div>
            <div class="chart-filter">
                <el-button type="primary"
                           size="small"
                           class="mleft10"
                           @click="pressToDo">
                    <i class="icon iconfont f14">&#xe6a1;</i>催办
                </el-button>
                <el-input v-model="searchWord"
                          class="mleft10"
                          clearable
                          placeholder="请输入流程标题">
                    <i slot="suffix"
                       class="el-input__icon el-icon-search"
                       @click="search"></i>
                </el-input>
                <el-button type="text"
                           class="mleft20"
                           @click="gotoMore">更多</el-button>
            </div>
        </section>
        <div class="chart-section p10">
            <div class="yw-el-table-wrap hp100 wp100">
                <!-- 条件、列表 -->
                <asp-smart-table ref="aspSmartTable"
                                 :tableJson="pageJson"
                                 v-model="model"
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
    import pageJson from './smart_data/bpm-my-orders-list.json'
    import tableTabs from './sub_components/table-tabs.vue'

    export default {
        components: {
            tableTabs
        },
        data() {
            return {
                pageJson: pageJson, // 列表
                model: pageJson.model,
                pressToDoJson: pressToDoJson,   // 催办弹框
                pressToDoModel: pressToDoJson.model,

                searchWord: '',
                activeIndex: 0,
                isInit: true,
                curTabStatus: '',
                tabList: [
                    {
                        name: '我发起的全部工单',
                        type: 3,
                        status: '',
                        orderType: 'myList',
                        count: '-',
                        countKey: 'myRequestTotal',
                        tabType: 'dropdown',
                        dropList: [
                            {
                                name: '我发起的全部工单',
                                type: 3,
                                status: '',
                                orderType: 'myList',
                                count: '-',
                                countKey: 'myRequestTotal',
                            },
                            {
                                name: '我发起的处理中工单',
                                type: 3,
                                status: 'running',
                                orderType: 'myList',
                                count: '-',
                                countKey: 'myRequestRun',
                            },
                            {
                                name: '我发起的已关闭工单',
                                type: 3,
                                status: 'end',
                                orderType: 'myList',
                                count: '-',
                                countKey: 'myRequestEnd',
                            }
                        ]
                    },
                    {
                        name: '待办',
                        type: 1,
                        status: '',
                        orderType: 'todoList',
                        count: '-',
                        countKey: 'todoCnt'
                    },
                    {
                        name: '已办',
                        type: 2,
                        status: '',
                        orderType: 'todoList',
                        count: '-',
                        countKey: 'doneCnt'
                    },
                ],
                dataList: [], // 表格数据
                dialogBoxShow: false,
                reqParams: {},
                rowSelected: [], // 已勾选行
            }
        },
        watch: {
        },
        computed: {
            curTab() {
                return this.tabList[this.activeIndex]
            },
            smartTable() {
                return this.$refs.aspSmartTable
            }
        },
        mounted() {
            this.search()
        },
        methods: {
            // 催办按钮
            pressToDo() {
                if (!this.rowSelected.length) {
                    this.$message.warning('请选择工单！')
                    return
                }
                this.dialogBoxShow = true
            },
            handleTabsCount(res) {
                // 初始化赋值
                if (this.isInit) {
                    this.tabList[0].count = res.myRequestTotal
                    this.isInit = false
                }

                this.tabList[0].dropList[0].count = res.myRequestTotal
                this.tabList[0].dropList[1].count = res.myRequestRun
                this.tabList[0].dropList[2].count = res.myRequestEnd
                this.tabList[1].count = res.todoCnt
                this.tabList[2].count = res.doneCnt
            },
            // 表单及表格 回调事件
            onbind(data) {
                // data: { item, parent, type, index, model, row, fileData, subFormSelectData }
                // const routerHash = 'inst/' + (data.row && data.row.id)
                switch (data.item.columnName) {
                    // 取消
                    case 'btn-press-cacel':
                        this.dialogBoxShow = false
                        break
                    // 勾选行 subFormSelectData
                    case 'bpm_my_orders':
                        if (data.type === 'multipleSelection') {
                            this.$nextTick(() => {
                                // table_goback_work_selected
                                this.rowSelected = this.handleSelectedRow(data.subFormSelectData)
                            })
                        }
                        break
                    // 列表 工单号
                    case 'id':
                        this.gotoDetail(data.row)
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
                    // 列表
                    case 'bpm_my_orders':
                        Object.assign(params, {
                            pageNo: params.page,
                            pageSize: params.rows,
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
                    // 列表
                    case 'bpm_my_orders':
                        this.handleTabsCount(responseBody)
                        responseBody = Object.assign(responseBody, responseBody.page)
                        this.$utils.smartTableDataFormat(tableItem, responseBody, 'page', this.curTab.countKey)
                        break
                    // 表格列表数据格式必须统一格式：this.$utils.smartTableDataFormat
                    default:
                        this.$utils.smartTableDataFormat(tableItem, responseBody)
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
                    subject: this.searchWord,   // 标题
                    type: this.curTab.type, // 1待办 2已办 3我发起的
                    status: this.curTabStatus,  // running处理中  end关闭；获取全部数据不传。
                    cntFlag: 'Y',    // Y -- 返回总数
                }
                this.smartTable.asp_sendTableQuery('bpm_my_orders')
            },
            // 已选中行数据处理
            handleSelectedRow(list) {
                let arr = []
                list.forEach(item => {
                    let obj = {
                        procDefKey: '',
                        procInstId: ''
                    }
                    obj.procDefKey = this.curTab.status    // 工单类型 tabListTwo status
                    obj.procInstId = item.id
                    arr.push(obj)
                })
                return arr
            },
            handleTabClick(index, isDropList, dropIndex) {
                this.activeIndex = index
                if (isDropList) {
                    Object.assign(this.tabList[index], this.curTab.dropList[dropIndex])
                    this.curTabStatus = this.curTab.dropList[dropIndex].status
                } else {
                    this.curTabStatus = this.curTab.status
                }
                this.currentPage = 1
                console.log(index)
                this.search()
            },
            // 更多我的工单
            gotoMore() {
                let routerHash = '', currentTitle = ''
                if (this.curTab.orderType === 'myList') {
                    routerHash = 'flow/request'
                    currentTitle = '我发起的'
                } else if (this.curTab.type === 2) {
                    routerHash = 'flow/done'
                    currentTitle = '已办事项'
                } else {
                    routerHash = 'flow/todoList'
                    currentTitle = '待办事项'
                }
                this.$router.push({
                    path: '/resource/flow',
                    query: {
                        routerHash: routerHash,
                        currentTitle: currentTitle
                    }
                })
            },
            gotoDetail(row) {
                // 待办 task/2009241132348055991  
                // 已办、我发起的 inst/2009241132348055991
                let routerHash = ''
                if (this.curTab.type === 1) {
                    routerHash = 'task/' + row.taskId
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
</style>

