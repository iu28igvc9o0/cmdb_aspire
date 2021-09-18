<template>
    <!-- 效能展示:总览 -->
    <div class="content-chart">
        <section class="chart-title-wrap clearfix displaybox">
            <div class="chart-title boxflex01">
                <div class="displaybox">
                    <div>效能展示（除工作报告流程）</div>
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
            <div class="chart-section-main cube-wrap"
                 v-loading="loading">
                <div v-for="(item,itemIndex) in cubeList"
                     style="width:15.6%"
                     :key="itemIndex"
                     @click="gotoDetails(item)"
                     class="cube-item">
                    <img class="icon"
                         :src="'/static/img/custom_modules/' + item.iconUrl">
                    <el-tooltip class="item"
                                effect="dark"
                                :content="`${item.number}${item.cell}`"
                                placement="top">
                        <p class="number text-ellipse"
                           :class='item.color'>{{item.number}}<span class="cell"
                                  v-if="item.cell">{{item.cell}}</span></p>
                    </el-tooltip>

                    <el-tooltip class="item"
                                effect="dark"
                                :content="item.name"
                                placement="top">
                        <p class="name text-ellipse">{{item.name}}</p>
                    </el-tooltip>

                    <p class="split"></p>
                    <p class="percent-wrap">
                        {{item.percentName}}
                        <!-- <span>{{item.percent}}</span> -->
                        <YwIconTriangle :datas="item.percent"></YwIconTriangle>
                    </p>
                </div>
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
            YwIconTriangle: () => import('src/components/common/yw-icon-triangle.vue'),
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
                cubeList: []
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
                this.cubeList = [
                    {
                        iconUrl: 'circle-order-total.png',
                        color: 'blue',
                        number: 0,
                        numberKey: 'totalCnt',
                        cell: '',
                        name: '工单总数',
                        percentName: '环比:',
                        percent: '0%',
                        percentKey: 'totalCntMom',
                        detailPage: 'bpm-effectStatistics-orderTotal',// 详情页面名称
                        detailPageDatas: { title: '效能展示（除工作报告流程）> 工单总数', workType: 1 }
                    },
                    {
                        iconUrl: 'circle-order-solving.png',
                        color: 'blueLight',
                        number: 0,
                        numberKey: 'runCnt',
                        cell: '',
                        name: '处理中工单',
                        percentName: '环比:',
                        percent: '0%',
                        percentKey: 'runCntMom',
                        detailPage: 'bpm-effectStatistics-orderTotal',// 详情页面名称
                        detailPageDatas: { title: '效能展示（除工作报告流程）> 处理中工单', workType: 2 }
                    },
                    {
                        iconUrl: 'circle-order-overTime.png',
                        color: 'red',
                        number: 0,
                        numberKey: 'overCnt',
                        cell: '',
                        name: '超时工单',
                        percentName: '环比:',
                        percent: '0%',
                        percentKey: 'overCntMom',
                        detailPage: 'bpm-effectStatistics-orderTotal',// 详情页面名称
                        detailPageDatas: { title: '效能展示（除工作报告流程）> 超时工单', workType: 3 }
                    },
                    {
                        iconUrl: 'circle-order-close.png',
                        color: 'purple',
                        number: 0,
                        numberKey: 'endCnt',
                        cell: '',
                        name: '关闭工单',
                        percentName: '环比:',
                        percent: '0%',
                        percentKey: 'endCntMom',
                        detailPage: 'bpm-effectStatistics-orderTotal',// 详情页面名称
                        detailPageDatas: { title: '效能展示（除工作报告流程）> 关闭工单', workType: 4 }
                    },
                    {
                        iconUrl: 'circle-order-timely.png',
                        color: 'blue',
                        number: 0,
                        numberKey: 'endNumberRate',
                        cell: '%',
                        name: '工单处理及时率',
                        percentName: '环比:',
                        percent: '0%',
                        percentKey: 'endNumberRateMom',
                        // detailPage: 'bpm-effectStatistics-orderTotal',// 详情页面名称
                        // detailPageDatas: { title: '效能展示（除工作报告流程）> 工单处理及时率', workType: 5 }
                    },
                    {
                        iconUrl: 'circle-order-solveTime.png',
                        color: 'blue',
                        number: 0,
                        numberKey: 'averRunTime',
                        cell: 'h',
                        name: '工单处理时长',
                        percentName: '环比:',
                        percent: '0%',
                        percentKey: 'averRunTimeMom',
                        // detailPage: 'bpm-effectStatistics-orderTimeLength'// 详情页面名称
                        // detailPageDatas: { title: '效能展示（除工作报告流程）> 工单处理时长', workType: 6 }
                    }
                ]
                let params = {
                    instType: this.activeSelect,
                    dateType: this.activeTab.id
                }
                rbBpmHomeServices.getInstEfficiencyShow(params).then(res => {
                    this.tabList.forEach((item) => {
                        item.time = `${res.startTime} 至${res.endTime}`
                    })
                    if (res.data) {
                        this.cubeList.forEach((item) => {
                            item.number = res.data[item.numberKey]
                            item.percent = res.data[item.percentKey]
                        })
                    }

                }).finally(() => {
                    this.closeLoading()
                })

            },

            // 跳转到详情页面
            gotoDetails(item) {
                if (item.detailPage) {
                    this.$emit('changeComponent', {
                        url: item.detailPage,
                        datas: item.detailPageDatas,
                        conditions: { activeTab: this.activeTab, activeSelect: this.activeSelect }
                    })
                }
            },
            // 初始化
            init() {

                this.getOrderDatas()

                // 设置查询参数
                this.setConditions()

                this.query()
            }
        },
        mounted() {
            this.init()
        },

    }
</script>

<style lang="scss" scoped>
    @import "../mixin/drag.scss";
</style>

