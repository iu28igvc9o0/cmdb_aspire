<template>
    <!-- 服务台： 服务事件工单列表组件 -->
    <div class="content-chart"
         style="height: calc(100% - 180px); margin-top: 10px">
        <section class="chart-title-wrap clearfix displaybox">
            <div class="chart-title boxflex01">
                <div class="displaybox">
                    <div>{{orderName}}</div>
                    <!-- tab -->
                    <tableTabs :tabList="tabList"
                               :activeIndex="activeIndex"
                               @handleTabClick="handleTabClick"></tableTabs>
                </div>
            </div>
            <div class="chart-filter">
                <el-input v-model="searchWord"
                          clearable
                          placeholder="客户名称">
                    <i slot="suffix"
                       class="el-input__icon el-icon-search"
                       @click="search(1)"></i>
                </el-input>
                <el-button type="text"
                           class="mleft20"
                           @click="gotoMore">更多</el-button>
            </div>
        </section>
        <div class="chart-section p10">
            <div class="yw-el-table-wrap hp100 wp100">
                <!-- 条件、列表 -->
                <asp-smart-table ref="table_event_orders_list"
                                 :tableJson="pageJson"
                                 v-model="model"
                                 :beforeTableRender="beforeTableRender"
                                 :beforeHttp="beforeHttp"
                                 :afterHttp="afterHttp"
                                 @on="onbind">
                </asp-smart-table>
            </div>
        </div>
    </div>
</template>

<script>
    import pageJson from '../smart_data/bpm-server-event-orders-list.json'
    import tableTabs from './table-tabs.vue'
    import rbAutoOperationMixin from 'src/services/auto_operation/rb-auto-operation-mixin.js'

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
                pageJson: pageJson,
                model: pageJson.model,

                searchWord: '',
                activeIndex: 0,
                tabList: [
                    {
                        name: '全部',
                        type: 'ALL',
                        orderType: 'myList',
                    },
                    {
                        name: '处理中',
                        type: 'PROCESS',
                        orderType: 'myList',
                    },
                    {
                        name: '已关闭',
                        type: 'END',
                        orderType: 'myList',
                    },
                    {
                        name: '草稿',
                        type: 'DRAFT',
                        orderType: 'myList',
                    },
                ],
                dataList: [], // 表格数据
                reqParams: {
                    type: 'ALL'
                },
            }
        },
        watch: {
        },
        computed: {
            curTab() {
                return this.tabList[this.activeIndex]
            },
            smartTable() {
                return this.$refs.table_event_orders_list
            }
        },
        mixins: [rbAutoOperationMixin],
        mounted() {
        },
        methods: {
            // 表单及表格 回调事件
            onbind(data) {
                // data: { item, parent, type, index, model, row, fileData, subFormSelectData }
                console.log(data)
                const routerHash = 'inst/' + (data.row && data.row.id)
                switch (data.item.columnName) {
                    // 列表
                    case 'reqDesc':
                        this.$router.push({
                            path: '/resource/flow',
                            query: {
                                routerHash: routerHash,
                                currentTitle: data.row.reqDesc
                            }
                        })
                        break

                }
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
                switch (scope.row.reqLevel) {
                    case '高':
                        className += ' bgred'
                        break
                    case '中':
                        className += ' bgorange'
                        break
                    default:
                        className += ' bgyellow'
                        break
                }
                callBack(tableName, 'reqLevel', [], { content: `<span class="${className}">${scope.row.reqLevel}</span>` })
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
                    case 'table_event_orders_list':
                        Object.assign(params, {
                            pageNo: params.page,
                            pageSize: params.rows
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
            // 获取列表数据 
            search(num) {
                this.reqParams = {
                    customerName: this.searchWord,
                    type: this.curTab.type,
                }

                this.smartTable.asp_sendTableQuery('table_event_orders_list')

                // this.loading = true
                // rbBpmHomeServices
                //     .getOrderList(req)
                //     .then(res => {
                //         this.loading = false
                //         // 设置待办/处理中工单的数量
                //         this.dataList = res.page.rows
                //     })
                //     .catch(() => {
                //         this.loading = false
                //     })
            },
            handleTabClick(index) {
                this.activeIndex = index
                this.currentPage = 1
                console.log(index)
                this.search()
            },
            // 更多我的工单
            gotoMore() {
                const routerHash = 'flow/retrieve'
                this.$router.push({
                    path: '/resource/flow',
                    query: {
                        routerHash: routerHash,
                        currentTitle: '流程检索',
                        curTreeNode: 'event'
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
</style>

