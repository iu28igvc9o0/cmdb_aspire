<template>

    <div class="content-chart"
         style="width: 24.2%;">
        <section class="chart-title-wrap clearfix">
            <svg class="svg-icon svg-icon-24"
                 aria-hidden="true">
                <use xlink:href="#icongongdan"></use>
            </svg>
            <span class="chart-title">{{chartData.name}}</span>
        </section>

        <!-- 多图表 -->
        <section class="chart-section">
            <div class="yw-rank-table-wrap">
                <vue-scroll style="width:100%;height:100%;">
                    <el-table class="yw-rank-table"
                              style="min-width:430px"
                              v-loading="!chartData.chartList[0].tableDatas.length"
                              :data="chartData.chartList[0].tableDatas">
                        <el-table-column label="排名"
                                         :width="45">
                            <template slot-scope="scope">
                                <span :class="rankColor(scope.$index+1)">
                                    {{ scope.row.number}}
                                </span>
                            </template>
                        </el-table-column>
                        <el-table-column label="工单类型"
                                         :width="60">
                            <template slot-scope="scope">
                                <el-tooltip effect="dark"
                                            :content="scope.row.orderType"
                                            placement="top-start">
                                    <span class="text-ellipse">
                                        {{ scope.row.orderType}}
                                    </span>
                                </el-tooltip>

                            </template>
                        </el-table-column>
                        <el-table-column label="最长处理 时长(小时)"
                                         :min-width="150">
                            <template slot-scope="scope">
                                <YwProgress :datas="{percentage: handleRate(scope.row.rate), index:scope.$index+1, num:scope.row.runTime}"
                                            :option="progressOption"></YwProgress>
                            </template>

                        </el-table-column>
                        <el-table-column label="平均处理时长(小时)"
                                         :width="80">
                            <template slot-scope="scope">
                                <span class="text-ellipse">
                                    {{ scope.row.averRunTime}}
                                </span>
                            </template>
                        </el-table-column>
                        <el-table-column label="超SLA 工单量"
                                         :width="60">
                            <template slot-scope="scope">
                                <el-tooltip effect="dark"
                                            :content="scope.row.overTimeNumber"
                                            placement="top-start">
                                    <span class="text-ellipse">
                                        {{ scope.row.overTimeNumber}}
                                    </span>
                                </el-tooltip>
                            </template>
                        </el-table-column>
                    </el-table>
                </vue-scroll>
            </div>

        </section>
        <!-- 多图表 -->
    </div>
</template>

<script>
    import DrawChart from 'src/utils/drawCharts.js'
    export default {
        components: {
            YwProgress: () => import('src/components/common/yw-progress.vue'),
        },
        mixins: [DrawChart],
        data() {
            return {
                progressOption: {
                    type: 'single',
                    // 样式
                    style: {
                        width: '60%'
                    },
                    label: {
                        // 百分比
                        percentage: {
                            show: false
                        },
                        // 本身数据
                        num: {
                            show: true
                        }
                    }
                },
                chartData: {
                    name: '工单分类处理时长分析',
                    filter: [{ name: '物理机', label: 'X86服务器' }, { name: '虚拟机', label: '云主机' }],
                    activeFilter: 'X86服务器',
                    chartList: [{ tableTitle: [], tableDatas: [] }],
                }
            }
        },
        methods: {
            handleRate: (rate) => {
                if (rate < 0) {
                    return 0
                }
                return rate
            },
            // 获得数据
            getDatas() {

                let params = {

                }
                this.$api.queryOrderSolveTime(params).then((res) => {
                    if (res) {
                        this.chartData.chartList[0].tableDatas = res.slice(0, 8).map((item, index) => {
                            return {
                                number: item.number,
                                orderType: item.orderType,
                                runTime: item.runTime,
                                averRunTime: item.averRunTime,
                                overTimeNumber: item.overTimeNumber,
                                rate: 100 - index * 20,// (80%)
                            }
                        })
                    }

                })

                this.chartData.chartList[0].tableTitle = [
                    { name: 'number', title: '排名' },
                    { name: 'orderType', title: '工单类型' },
                    { name: 'runTime', title: '最长处理 时长(小时)' },
                    { name: 'averRunTime', title: '平均处理时长(小时)' },
                    { name: 'overTimeNumber', title: '超SLA 工单量' },
                ]

            },


        },
        mounted() {
            this.getDatas()
        }

    }
</script>

<style lang="scss" scoped>
    /deep/.chart-section {
        padding: 8px;
    }
    /deep/ .yw-rank-table {
        &.el-table {
            thead {
                th {
                    font-size: $font-12;
                }
            }
            tbody {
                td {
                    padding: 4px 0;
                }
            }
        }
    }
</style>

