<template>
    <!-- 工单考核 -->
    <div class="content-chart">
        <section class="chart-title-wrap clearfix displaybox">
            <div class="chart-title boxflex01">
                <div class="displaybox">
                    <div>工单考核（除工作报告流程）</div>
                </div>
            </div>
        </section>
        <section class="chart-section">
            <div class="chart-section-filter">
                <tableTabs class="chart-filter-item"
                           :tabList="tabList"
                           :activeIndex="activeIndex"
                           @handleTabClick="(...data)=>{onbind({item:{columnName:'btn-change-order'},model:{dataList:data}})}"></tableTabs>
                <div class="chart-filter-item">
                    <span class="f14"> {{activeTab.time}}</span>
                </div>
            </div>
            <div class="chart-section-main table-box-wrap">
                <section class="table-row-wrap"
                         v-loading="loading">
                    <div class="yw-el-table-wrap"
                         v-for="(tableItem,tableIndex) in 2"
                         style="width:50%"
                         :key="tableIndex">
                        <el-table class="yw-el-table"
                                  :data="tableIndex === 0 ? result.slice(0,5) : result.slice(5)"
                                  stripe
                                  border>
                            <el-table-column label="排名"
                                             width="60">
                                <template slot-scope="scope">
                                    <span v-html="scope.row.rankHtml"></span>
                                    <!-- {{ scope.row.rank }} -->
                                </template>
                            </el-table-column>
                            <el-table-column label="姓名"
                                             width="80">
                                <template slot-scope="scope">
                                    {{ scope.row.name }}
                                </template>
                            </el-table-column>
                            <el-table-column label="部门名称">
                                <template slot-scope="scope">
                                    <el-tooltip class="item"
                                                :disabled="!scope.row.department || scope.row.department ==='--'"
                                                popper-class="tooltip-popper"
                                                effect="dark"
                                                :content="scope.row.department"
                                                placement="top-start">
                                        <span class="text-ellipse">{{ scope.row.department }}</span>
                                    </el-tooltip>

                                </template>
                            </el-table-column>
                            <el-table-column label="超时任务"
                                             width="80">
                                <template slot-scope="scope">
                                    {{ scope.row.task }}
                                </template>
                            </el-table-column>
                        </el-table>
                    </div>
                    <!-- 自定义空遮盖 -->
                    <div class="empty-wrap"
                         v-if="!result || result.length<1">
                        <span>暂无数据</span>
                    </div>
                </section>
                <YwPagination @handleSizeChange="handleSizeChange"
                              @handleCurrentChange="handleCurrentChange"
                              :current-page="currentPage"
                              :page-sizes="pageSizes"
                              :page-size="pageSize"
                              :total="total"></YwPagination>
            </div>
        </section>
    </div>
</template>

<script>
    import { xDay } from 'src/assets/js/utility/rb-filters.js'
    import YwPaginationOption from 'src/components/common/yw-pagination/yw-pagination.js'
    import rbBpmHomeServices from 'src/services/bpm/rb-bpm-home-services.js'
    import CommonOption from 'src/utils/commonOption.js'
    export default {
        mixins: [YwPaginationOption, CommonOption],
        components: {
            tableTabs: () => import('./sub_components/table-tabs.vue'),
            YwPagination: () => import('src/components/common/yw-pagination/yw-pagination.vue'),
        },
        data() {
            return {
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
                // 查询条件
                queryParams: {

                },
                // 查询结果
                result: []
            }
        },

        methods: {
            // 表单及表格 回调事件
            onbind(data) {
                switch (data.item.columnName) {
                    // 改变工单状态
                    case 'btn-change-order':
                        this.activeIndex = data.model.dataList[0] || 0
                        this.activeTab = this.tabList[this.activeIndex]
                        // 查询表格
                        this.query()

                        break
                }
            },
            // 设置参数
            setParams(activePagination) {

                if (activePagination) {
                    this.queryParams['pageNo'] = this.currentPage
                    this.queryParams['pageSize'] = this.pageSize
                } else {

                    this.queryParams = {
                        'pageNo': this.initPageChange(),
                        'pageSize': this.pageSize,

                        'dateType': this.activeTab.id,
                    }
                }

            },

            /** 查询
             * activePagination:分页活动下保持先前的查询条件
             */
            query(activePagination = false) {
                this.showLoading()
                this.total = 0
                this.result = []

                let colors = ['#f5222d', '#fa7f1c', '#efc311', '#666']
                this.setParams(activePagination)
                rbBpmHomeServices.getWorkAssessmentReport(this.queryParams).then(res => {
                    this.tabList.forEach((item) => {
                        item.time = `${res.startTime} 至${res.endTime}`
                    })

                    if (res.data && res.data.length > 0) {
                        this.total = res.totalCnt || 0
                        this.result = res.data.map((item) => {
                            let color = colors[item.rankNum - 1] ? colors[item.rankNum - 1] : colors[colors.length - 1]
                            item.rank = item.rankNum
                            item.rankHtml = `<span style="color:${color};">${item.rankNum}</span>`
                            item.name = item.userName
                            item.department = item.orgName
                            item.task = item.overTaskCnt
                            return item
                        })
                        // 空处理('--'填充)
                        if (this.result.length < 10) {
                            let emptySet = new Array(10 - this.result.length)
                            emptySet.fill({
                                rank: '--',
                                rankHtml: '--',
                                name: '--',
                                department: '--',
                                task: '--'
                            })
                            this.result = this.result.concat(emptySet)
                        }
                    }


                }).finally(() => {
                    this.closeLoading()
                })
            },
            init() {
                this.activeTab = this.tabList[0]
                this.pageSize = 10
                this.query()
            }
        },
        created() {
            this.init()

        },

    }
</script>

<style lang="scss" scoped>
    @import "../mixin/drag.scss";
    .table-row-wrap {
        position: relative;
        .empty-wrap {
            position: absolute;
            top: rem(38);
            width: 100%;
            height: calc(100% - 40px);
            display: flex;
            justify-content: center;
            align-items: center;
            font-size: rem(14);
            color: #666;
            text-align: center;
            background: #fff;
            z-index: 2;
            border: 1px solid #c3d5ff80;
        }
    }
</style>

