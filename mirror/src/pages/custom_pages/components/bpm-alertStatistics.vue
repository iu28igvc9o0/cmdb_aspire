<template>
    <!-- 部门闭单率-总览 -->
    <div class="content-chart">
        <section class="chart-title-wrap clearfix displaybox">
            <div class="chart-title boxflex01">
                <div class="displaybox clearfix">
                    <div>告警统计</div>
                    <tableTabs class="chart-filter-item position-right"
                               :tabList="tabListProject"
                               :activeIndex="activeIndexProject"
                               @handleTabClick="(...data)=>{onbind({item:{columnName:'btn-change-project'},model:{dataList:data}})}"></tableTabs>
                </div>
            </div>
        </section>
        <section class="chart-section">
            <div class="chart-section-filter">
                <tableTabs class="chart-filter-item mleft20Reverse"
                           :tabList="tabList"
                           :activeIndex="activeIndex"
                           @handleTabClick="(...data)=>{onbind({item:{columnName:'btn-change-date'},model:{dataList:data}})}"></tableTabs>
                <div class="chart-filter-item">
                    <span class="f14"> {{activeTab.time}}</span>
                </div>
            </div>
            <div class="chart-section-main">
                <asp-smart-table ref="smartTable"
                                 :tableJson="tableJson"
                                 v-model="model"
                                 :beforeHttp="beforeHttp"
                                 :afterHttp="afterHttp"
                                 :beforeTableRender="beforeTableRender"
                                 @on="onbind">
                </asp-smart-table>
            </div>
        </section>
    </div>
</template>

<script>
    import tableJson from './smart_data/bpm-alertStatistics.json'
    import { xDay } from 'src/assets/js/utility/rb-filters.js'
    export default {
        components: {
            tableTabs: () => import('./sub_components/table-tabs.vue'),
        },
        data() {
            return {
                tableJson: tableJson,
                model: tableJson.model,

                // 日期
                activeIndex: 0,
                activeTab: {},
                tabList: [

                    // {
                    //     id: 1,
                    //     name: '周',
                    //     time: `${xDay(new Date(), 0)}至${xDay(new Date(), 7)}`
                    // },
                    {
                        id: 2,
                        name: '月',
                        time: `${xDay(new Date(), 0)}至${xDay(new Date(), 30)}`

                    }
                ],
                // 项目分类
                activeIndexProject: 0,
                activeTabProject: {},
                tabListProject: [

                    {
                        id: 1,
                        name: '资源池',
                    },
                    {
                        id: 2,
                        name: '其他项目',
                    }
                ],

            }
        },

        methods: {
            // 表单及表格 回调事件
            onbind(data) {
                switch (data.item.columnName) {
                    // 改变资源池项目
                    case 'btn-change-project':
                        {
                            this.activeIndexProject = data.model.dataList[0] || 0
                            this.activeTabProject = this.tabListProject[this.activeIndexProject]
                            // 查询表格
                            this.$refs.smartTable.asp_sendTableQuery('table_bpm_alertStatistics')
                        }
                        break
                    // 改变时间
                    case 'btn-change-date':
                        {
                            this.activeIndex = data.model.dataList[0] || 0
                            this.activeTab = this.tabList[this.activeIndex]
                            // 查询表格
                            this.$refs.smartTable.asp_sendTableQuery('table_bpm_alertStatistics')
                        }
                        break
                    // 部门名称
                    case 'department':
                        {
                            if (data.row.departmentType === 'yunwei') {
                                this.$emit('changeComponent', 'bpm-orderCloseRate-sub')
                            }
                        }
                        break
                }
            },
            // 智能表格页面所有请求前的前置操作
            beforeHttp({ tableItem, params, httpMethod, row }) {
                switch (tableItem.columnName) {
                    case 'table_bpm_alertStatistics':
                        Object.assign(params, {
                            pageNo: params.page,
                            pageSize: params.rows,

                            dateType: this.activeTab.id,
                            alarmType: this.activeTabProject.id
                        })
                        break
                }
            },
            // 智能表格页面所有请求后置操作
            afterHttp({ tableItem, responseBody }) {
                switch (tableItem.columnName) {
                    case 'table_bpm_alertStatistics':
                        this.tabList.forEach((item) => {
                            item.time = responseBody.alarmMonth
                        })

                        responseBody.dataList = responseBody.data.rows.map((item, index) => {
                            item.idcType = item.alarmName
                            item.alertTotal = item.alarmTotal
                            item.serious = item.greatCnt
                            item.high = item.highCnt
                            item.middle = item.middleCnt
                            return item
                        })

                        responseBody.totalCount = responseBody.data.total

                        this.$utils.smartTableDataFormat(tableItem, responseBody)
                        break
                }
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

                let color = '#666666'

                switch (columnItem.columnName) {
                    case 'serious':
                        color = '#f5222d'
                        break
                    case 'high':
                        color = '#fa7f1c'
                        break
                    case 'middle':
                        color = '#efc311'
                        break
                    default:
                        color = '#666666'
                        break
                }
                callBack(tableName, columnItem.columnName, [], { content: `<span style="color:${color}">${scope.row[columnItem.columnName]}</span>` })
            },
            // 重置json的http请求方法
            resetTableJsonHttp() {
                tableJson.list[0].http.methods = '/v1/alerts/xxxxxx'
            },

            // 初始化
            init() {
                // this.resetTableJsonHttp()
                this.activeTab = this.tabList[0]
                this.activeTabProject = this.tabListProject[0]
            },
        },
        created() {
            this.init()
        }

    }
</script>

<style lang="scss" scoped>
    @import "../mixin/drag.scss";
</style>

