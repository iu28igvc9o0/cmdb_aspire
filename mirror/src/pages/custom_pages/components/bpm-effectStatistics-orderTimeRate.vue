<template>
    <!-- 工单处理及时率 -->
    <div class="content-chart">
        <section class="chart-title-wrap clearfix displaybox">
            <div class="chart-title boxflex01">
                <div class="displaybox">
                    <div>效能展示（除工作报告流程）> 工单处理及时率
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
            <div class="chart-section-main">
                <YwLine :lineDatas="lineDatas[0]"
                        v-if="lineDatas[0].show"></YwLine>
            </div>
        </section>
    </div>
</template>

<script>
    import { xDay } from 'src/assets/js/utility/rb-filters.js'
    import rbBpmHomeServices from 'src/services/bpm/rb-bpm-home-services.js'
    import CommonOption from 'src/utils/commonOption.js'
    import commonUtils from '../mixin/commonUtils'
    export default {
        mixins: [CommonOption, commonUtils],
        props: ['moduleData'],
        components: {
            tableTabs: () => import('./sub_components/table-tabs.vue'),
            YwLine: () => import('src/components/common/charts-lightColor/yw-line.vue'),
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
                lineDatas: [
                    {
                        show: false,
                        id: 'line-1',
                        chartOption: 'line-option',
                        chartDatas: [],
                        option: {
                            legend: { show: false },
                            series: [
                                { seriesName: '处理及时率(%)', yAxisName: '处理及时率(%)', yData: 'cnt', xData: 'taskName' }
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
                    // 返回
                    case 'btn-gotoBack':
                        this.$emit('changeComponent', {
                            url: 'bpm-effectStatistics-overview',
                            datas: '',
                            conditions: { activeTab: this.activeTab, activeSelect: this.activeSelect }
                        })
                        break
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
                let params = {
                    instType: this.activeSelect,
                    dateType: this.activeTab.id,
                    workType: 1,// 1. 工单总数 2处理中工单 3超时工单 4关闭工单
                }
                rbBpmHomeServices.getInstEfficiencyReport(params).then(res => {
                    this.tabList.forEach((item) => {
                        item.time = `${res.startTime} 至${res.endTime}`
                    })
                    this.lineDatas[0].chartDatas = res.data
                    this.lineDatas[0].show = false
                    this.$nextTick(() => {
                        this.lineDatas[0].show = true
                    })
                })
            },

            // 初始化
            init() {
                this.getOrderDatas()
                // 设置查询参数
                this.setConditions()
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

