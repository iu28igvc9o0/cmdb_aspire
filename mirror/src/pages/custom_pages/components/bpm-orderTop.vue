<template>
    <!-- 工单top -->
    <div class="content-chart">
        <section class="chart-title-wrap clearfix displaybox">
            <div class="chart-title boxflex01">
                <div class="displaybox">
                    <div>工单TOP（除工作报告流程）</div>
                </div>
            </div>
        </section>
        <section class="chart-section">
            <div class="chart-section-filter">
                <el-select class="chart-filter-item h30"
                           style="width:20%"
                           @change="(data)=>{onbind({item:{columnName:'btn-change-order'},model:{data:data}})}"
                           v-model="activeSelect"
                           placeholder="请选择">
                    <el-option v-for="item in selectList"
                               :key="item.value"
                               :label="item.label"
                               :value="item.value">
                    </el-option>
                </el-select>
                <tableTabs class="chart-filter-item"
                           :tabList="tabList"
                           :activeIndex="activeIndex"
                           @handleTabClick="(...data)=>{onbind({item:{columnName:'btn-change-date'},model:{dataList:data}})}"></tableTabs>
                <div class="chart-filter-item">
                    <span class="f14"> {{activeTab.time}}</span>
                </div>
            </div>
            <div class="chart-section-main"
                 v-loading="loading">
                <YwBarLine :barLineDatas="barLineDatas[0]"
                           v-if="barLineDatas[0].show"></YwBarLine>
            </div>
        </section>
    </div>
</template>

<script>
    import { xDay } from 'src/assets/js/utility/rb-filters.js'
    import rbBpmHomeServices from 'src/services/bpm/rb-bpm-home-services.js'
    import CommonOption from 'src/utils/commonOption.js'
    export default {
        mixins: [CommonOption],
        components: {
            tableTabs: () => import('./sub_components/table-tabs.vue'),
            YwBarLine: () => import('src/components/common/charts-lightColor/yw-barLine.vue'),
        },
        data() {
            return {
                activeSelect: 1,
                selectList: [],
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
                barLineDatas: [
                    {
                        show: false,
                        id: 'barLine-1',
                        chartOption: 'bar-line-option',
                        chartDatas: [],
                        option: {
                            series: [
                                { chartType: 'bar', seriesName: '工单数(个)', yAxisName: '工单数(个)', yData: 'cnt', xData: 'orderType' },
                                { chartType: 'line', seriesName: '闭单率(%)', yAxisName: '闭单率(%)', yData: 'endNumberRate', xData: 'orderType' }
                            ]
                        }
                    },
                ],
            }
        },

        methods: {
            // 表单及表格 回调事件
            onbind(data) {
                switch (data.item.columnName) {
                    // 改变工单
                    case 'btn-change-order':
                        this.query()
                        break
                    // 改变时间
                    case 'btn-change-date':
                        {
                            this.activeIndex = data.model.dataList[0] || 0
                            this.activeTab = this.tabList[this.activeIndex]
                            this.query()
                        }
                        break
                }
            },
            getOrderDatas() {
                this.selectList = [{
                    value: 1,
                    label: '全部工单'
                }, {
                    value: 2,
                    label: '我的工单'
                }]
            },
            query() {
                this.showLoading()
                let params = {
                    instType: this.activeSelect,
                    dateType: this.activeTab.id,
                    // pageNo: 1,
                    // pageSize: 10
                }
                rbBpmHomeServices.getWorkTop(params).then(res => {
                    this.tabList.forEach((item) => {
                        item.time = `${res.startTime} 至${res.endTime}`
                    })

                    this.barLineDatas[0].chartDatas = res.data.map((item) => {
                        item.endNumberRate = item.endNumberRate.split('%')[0]
                        return item
                    })
                    this.barLineDatas[0].show = false
                    this.$nextTick(() => {
                        this.barLineDatas[0].show = true
                    })
                }).finally(() => {
                    this.closeLoading()
                })
            },
            // 初始化
            init() {
                this.activeTab = this.tabList[0]
                this.getOrderDatas()
                this.query()
            },
        },
        mounted() {
            this.init()
        },

    }
</script>

<style lang="scss" scoped>
    @import "../mixin/drag.scss";
</style>

