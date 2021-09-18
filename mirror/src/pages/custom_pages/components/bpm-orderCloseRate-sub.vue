<template>
    <!-- 部门闭单率-运维 -->
    <div class="content-chart">
        <section class="chart-title-wrap clearfix displaybox">
            <div class="chart-title boxflex01">
                <div class="displaybox">
                    <div>部门闭单率（除工作报告流程）> {{moduleData.datas.orgName}}
                        <div class="btn-back"
                             @click="onbind({item:{columnName:'btn-gotoBack'}})">
                            <svg class="svg-icon svg-icon-24 icon-back"
                                 aria-hidden="true">
                                <use xlink:href="#iconfanhui1"></use>
                            </svg>
                        </div>
                    </div>
                </div>
            </div>
        </section>
        <section class="chart-section">
            <div class="chart-section-filter">
                <tableTabs class="chart-filter-item"
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
    import tableJson from './smart_data/bpm-orderCloseRate-overview.json'
    import { xDay } from 'src/assets/js/utility/rb-filters.js'
    export default {
        props: ['moduleData'],
        components: {
            tableTabs: () => import('./sub_components/table-tabs.vue'),
        },
        data() {
            return {
                tableJson: tableJson,
                model: tableJson.model,

                activeIndex: 0,
                activeTab: {},
                tabList: [

                    {
                        id: 1,
                        name: '周',
                        time: `${xDay(new Date(), 0)}至${xDay(new Date(), 7)}`
                    },
                    {
                        id: 2,
                        name: '月',
                        time: `${xDay(new Date(), 0)}至${xDay(new Date(), 30)}`

                    }
                ],

            }
        },

        methods: {
            // 表单及表格 回调事件
            onbind(data) {
                switch (data.item.columnName) {
                    // 返回
                    case 'btn-gotoBack':
                        this.$emit('changeComponent', { url: 'bpm-orderCloseRate-overview', datas: '', conditions: { activeTab: this.activeTab } })
                        break
                    // 改变时间
                    case 'btn-change-date':
                        {
                            this.activeIndex = data.model.dataList[0] || 0
                            this.activeTab = this.tabList[this.activeIndex]
                            // 查询表格
                            this.$refs.smartTable.asp_sendTableQuery('table_bpm_orderCloseRate')
                        }
                        break
                    // 部门名称
                    // case 'department':
                    //     this.$emit('changeComponent', { url: 'bpm-orderCloseRate-xxxx', datas: data.row })
                    //     break
                }
            },
            // 智能表格页面所有请求前的前置操作
            beforeHttp({ tableItem, params, httpMethod, row }) {
                switch (tableItem.columnName) {
                    case 'table_bpm_orderCloseRate':
                        Object.assign(params, {
                            pageNo: params.page,
                            pageSize: params.rows,

                            dateType: this.activeTab.id,
                            orgName: this.moduleData.datas.department
                        })
                        break
                }
            },
            // 智能表格页面所有请求后置操作
            afterHttp({ tableItem, responseBody }) {
                switch (tableItem.columnName) {
                    case 'table_bpm_orderCloseRate':
                        this.tabList.forEach((item) => {
                            item.time = `${responseBody.startTime} 至${responseBody.endTime}`
                        })

                        responseBody.dataList = responseBody.data.map((item, index) => {
                            item.rank = index + 1
                            item.department = item.orgName
                            item.closeRate = (item.endNumRate || 0) + '%'
                            return item
                        })

                        responseBody.totalCount = responseBody.totalCnt

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
                switch (scope.row.rank) {
                    case 1:
                        color = '#f5222d'
                        break
                    case 2:
                        color = '#fa7f1c'
                        break
                    case 3:
                        color = '#efc311'
                        break
                    default:
                        color = '#666666'
                        break
                }
                callBack(tableName, 'rank', [], { content: `<span style="color:${color}">${scope.row.rank}</span>` })
            },
            // 重置json的http请求方法
            resetTableJsonHttp() {
                tableJson.list[0].http.methods = '/v1/xxxxxx'
            },

            // 初始化
            init() {
                // this.resetTableJsonHttp()
                if (this.moduleData.conditions && this.moduleData.conditions.activeTab) {
                    this.tabList.some((item, index) => {
                        if (item.name === this.moduleData.conditions.activeTab.name) {
                            this.activeTab = item
                            this.activeIndex = index
                            return true
                        } else {
                            return false
                        }
                    })
                } else {
                    this.activeTab = this.tabList[0]
                    this.activeIndex = 0
                }

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

