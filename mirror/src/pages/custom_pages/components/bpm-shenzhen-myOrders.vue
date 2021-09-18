<template>
    <!-- 服务台-深圳：我的工单 -->
    <div class="content-chart">
        <section class="chart-title-wrap clearfix displaybox">
            <div class="chart-title boxflex01">
                <div class="displaybox">
                    <div>我的工单</div>
                </div>
            </div>
            <div class="chart-filter">
                <el-button type="text"
                           class="mleft20"
                           @click="onbind({item:{columnName:'btn-more'}})">更多</el-button>
            </div>
        </section>
        <div class="chart-section p10">
            <div class="chart-section-filter">
                <!-- tab -->
                <tableTabs :tabList="tabListOrder"
                           :activeIndex="activeIndexOrder"
                           @handleTabClick="(...data)=>{onbind({item:{columnName:'btn-change-order'},model:{dataList:data}})}"></tableTabs>
                <tableTabs :tabList="tabListAlert"
                           v-if="activeTabOrder.tabId===1"
                           :activeIndex="activeIndexAlert"
                           @handleTabClick="(...data)=>{onbind({item:{columnName:'btn-change-alert'},model:{dataList:data}})}"></tableTabs>
            </div>
            <div class="chart-section-main">
                <asp-smart-table v-if="refresh"
                                 ref="smartTable"
                                 :tableJson="tableJson"
                                 v-model="model"
                                 :beforeHttp="beforeHttp"
                                 :afterHttp="afterHttp"
                                 :beforeTableRowRender="beforeTableRowRender"
                                 @on="onbind">
                </asp-smart-table>
            </div>
        </div>
    </div>
</template>

<script>
    import commonUtils from '../mixin/commonUtils'
    import tableJson from './smart_data/bpm-shenzhen-myOrders.json'
    import tableJsonTodo from './smart_data/bpm-shenzhen-myOrders-todo.json'
    import tableJsonDone from './smart_data/bpm-shenzhen-myOrders-done.json'
    export default {
        mixins: [commonUtils],
        components: {
            tableTabs: () => import('./sub_components/table-tabs.vue'),
        },
        data() {
            return {
                refresh: true,
                tableJson: tableJson,
                model: tableJson.model,
                // 查询参数
                reqParams: {},
                // tab
                activeIndexOrder: 1,// 选中的tab索引
                activeTabOrder: {},// 选中的tab对象
                tabListOrder: [
                    {
                        tabId: 3,
                        name: '我发起的全部工单',
                        status: '',
                        count: '0',
                        countKey: 'myRequestTotal',
                        tabType: 'dropdown',
                        dropList: [
                            {
                                name: '我发起的全部工单',
                                tabId: 3,
                                status: '',
                                orderType: 'myList',
                                count: '0',
                                countKey: 'myRequestTotal',
                            },
                            {
                                name: '我发起的处理中工单',
                                tabId: 3,
                                status: 'running',
                                orderType: 'myList',
                                count: '0',
                                countKey: 'myRequestRun',
                            },
                            {
                                name: '我发起的已关闭工单',
                                tabId: 3,
                                status: 'end',
                                orderType: 'myList',
                                count: '0',
                                countKey: 'myRequestEnd',
                            }
                        ]
                    },
                    {
                        tabId: 1,
                        name: '待办',
                        count: '0',
                        countKey: 'todoCnt',

                    },
                    {
                        tabId: 2,
                        name: '已办',
                        count: '0',
                        countKey: 'doneCnt',
                    }
                ],
                activeIndexAlert: 0,
                activeTabAlert: {},
                tabListAlert: [
                    {
                        tabId: '',
                        name: '全部',
                        html: '全部',
                    },
                    {
                        tabId: 'warnFlag',
                        name: '预警',
                        html: '<i style="display:inline-block;width:10px;height:10px;border-radius:100%;background:#FCCE01;margin-right:5px;"></i>预警',
                    },
                    {
                        tabId: 'timeOutFlag',
                        name: '超时',
                        html: '<i style="display:inline-block;width:10px;height:10px;border-radius:100%;background:#FC0201;margin-right:5px;"></i>超时',
                    }
                ]
            }
        },
        methods: {
            // 表单及表格 回调事件
            onbind(data) {
                switch (data.item.columnName) {
                    // 更多
                    case 'btn-more':
                        {
                            switch (this.activeIndexOrder) {
                                case 0:
                                    this.linkUrl('/resource/flow', { routerHash: 'flow/request', currentTitle: '我的工单' })
                                    break
                                case 1:
                                    this.linkUrl('/resource/flow', { routerHash: 'flow/todoList', currentTitle: '待办事项' })
                                    break
                                case 2:
                                    this.linkUrl('/resource/flow', { routerHash: 'flow/done', currentTitle: '已办事项' })
                                    break
                            }

                        }

                        break
                    // 改变工单状态
                    case 'btn-change-order':
                        this.handleTabClick(data.model.dataList[0], data.model.dataList[1], data.model.dataList[2])
                        break
                    // 改变告警状态
                    case 'btn-change-alert':
                        this.activeIndexAlert = data.model.dataList[0] || 0
                        this.activeTabAlert = this.tabListAlert[this.activeIndexAlert]
                        // 查询表格
                        this.$refs.smartTable.asp_sendTableQuery('table_shenzhen_myOrders')
                        break
                    // 标题字段
                    case 'subject':
                        {

                            if (this.activeTabOrder.tabId === 1) {
                                // 待办
                                this.linkUrl('/resource/flow', { routerHash: `task/${data.row.taskId}`, currentTitle: data.row.subject })
                            } else {
                                this.linkUrl('/resource/flow', { routerHash: `inst/${data.row.id}`, currentTitle: data.row.subject })
                            }


                        }

                        break
                    // 工单号字段(待办)
                    // case 'procInstId':
                    //     {
                    //         this.linkUrl('/resource/flow', { routerHash: `task/${data.row.taskId}`, currentTitle: data.row.subject })
                    //     }

                    //     break
                }
            },
            // 智能表格页面所有请求前的前置操作
            beforeHttp({ tableItem, params, httpMethod, row }) {
                switch (tableItem.columnName) {
                    case 'table_shenzhen_myOrders':
                        Object.assign(params, {
                            pageNo: params.page,
                            pageSize: params.rows,

                            'tab': this.activeTabOrder.tabId,
                            'timeOutFlag': this.activeTabAlert.tabId === 'timeOutFlag' ? 'Y' : '',
                            'warnFlag': this.activeTabAlert.tabId === 'warnFlag' ? 'Y' : '',
                            'status': this.activeTabOrder.status
                        })
                        break
                }
            },
            // 智能表格页面所有请求后置操作
            afterHttp({ tableItem, responseBody }) {
                switch (tableItem.columnName) {
                    case 'table_shenzhen_myOrders':
                        // 表格数据
                        responseBody.dataList = responseBody.data.rows
                        responseBody.totalCount = responseBody.data.total

                        // tab(只有待办工单时会返回tab数)
                        if (this.activeIndexOrder === 1) {
                            this.getTabDatas(responseBody)
                        }


                        this.$utils.smartTableDataFormat(tableItem, responseBody)
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
                // TIMEOUT超时（红色），WARNING预警（橙色），NORMAL空白（白色）
                switch (row.timeOutOverSee) {
                    case 'TIMEOUT':
                        rowClassName = 'row-bgred'
                        break
                    case 'WARNING':
                        rowClassName = 'row-bgorange'
                        break
                }
                return rowClassName
            },
            // 获得tab数据
            getTabDatas(responseBody = {}) {
                this.tabListOrder.forEach((itemTab) => {
                    itemTab.count = responseBody[itemTab.countKey] || 0
                    if (itemTab.dropList) {
                        itemTab.dropList.forEach((dropItem) => {
                            dropItem.count = responseBody[dropItem.countKey] || 0
                        })
                    }
                })
            },
            handleTabClick(index, isDropList, dropIndex) {
                this.activeIndexOrder = index
                let curTab = this.tabListOrder[this.activeIndexOrder]
                if (isDropList) {
                    Object.assign(this.tabListOrder[index], curTab.dropList[dropIndex])
                    this.activeTabOrder = curTab.dropList[dropIndex]
                } else {
                    this.activeTabOrder = curTab
                }


                this.currentPage = 1
                // 查询表格
                this.changeTableJson()
                this.$refs.smartTable.asp_sendTableQuery('table_shenzhen_myOrders')
                this.refresh = false
                this.$nextTick(() => {
                    this.refresh = true
                })
            },
            // 切换tableJson
            changeTableJson() {
                switch (this.activeIndexOrder) {
                    case 0:
                        this.tableJson = tableJson
                        this.model = tableJson.model
                        break
                    case 1:
                        this.tableJson = tableJsonTodo
                        this.model = tableJsonTodo.model
                        break
                    case 2:
                        this.tableJson = tableJsonDone
                        this.model = tableJsonDone.model
                        break
                }
            },
            // 初始化
            init() {
                this.activeTabOrder = this.tabListOrder[1]
                this.activeTabAlert = this.tabListAlert[0]
                this.changeTableJson()
            }
        },
        created() {
            this.init()
        }

    }
</script>

<style lang="scss" scoped>
    @import "../mixin/drag.scss";
</style>

