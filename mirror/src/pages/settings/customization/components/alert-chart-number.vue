<template>
    <!-- 告警：告警级别 -->
    <div class="content-chart"
         style="width: 100%;height:740px">
        <section class="chart-title-wrap clearfix">
            <svg class="svg-icon svg-icon-24"
                 aria-hidden="true">
                <use xlink:href="#icongaojingneirong"></use>
            </svg>
            <span class="chart-title">{{chartData.name}}</span>
            <div class="chart-filter">
                <el-radio-group class="yw-radio-button-wrap chart-filter-item"
                                v-model="chartData.activeFilter"
                                @change="changeTab">
                    <el-radio-button :label="tabItem.label"
                                     v-for="(tabItem,tabIndex) in chartData.filter"
                                     :key="tabIndex">{{tabItem.name}}</el-radio-button>
                </el-radio-group>
                <el-radio-group class="yw-radio-button-wrap chart-filter-item"
                                v-model="chartData.activeFilter2"
                                @change="changeTab">
                    <el-radio-button :label="tabItem.label"
                                     v-for="(tabItem,tabIndex) in chartData.filter2"
                                     :key="tabIndex">{{tabItem.name}}</el-radio-button>
                </el-radio-group>
                <el-date-picker class="yw-date-editor chart-filter-item"
                                style="width:190px"
                                v-model="chartData.dateRange"
                                @change="changeTab"
                                format="yyyy-MM-dd"
                                value-format="yyyy-MM-dd"
                                type="daterange"
                                :clearable="false"
                                range-separator="至"
                                start-placeholder="开始日期"
                                end-placeholder="结束日期">
                </el-date-picker>
            </div>
        </section>

        <!-- 多图表 -->
        <section class="chart-section">
            <div class="chart-box-wrap">
                <div class="chart-box-item"
                     style="width:100%;"
                     v-for="(subItem,subIndex) in chartData.chartList"
                     :key="subIndex">
                    <div class="yw-el-table-wrap">
                        <el-table class="yw-rank-table"
                                  :data="subItem.tableDatas"
                                  height="640px"
                                  show-summary>
                            <el-table-column label="排名"
                                             width="60">
                                <template slot-scope="scope">
                                    <span :class="rankColor(scope.$index+1)">
                                        {{ scope.$index + 1 }}
                                    </span>
                                </template>
                            </el-table-column>
                            <el-table-column label="告警内容">
                                <template slot-scope="scope">
                                    <el-tooltip class="item"
                                                effect="dark"
                                                :content="scope.row.colName"
                                                placement="top-start">
                                        <p class="line-2"
                                           style="height:46px">
                                            <span :style="`line-height: ${scope.row.colName.length > 80 ? 'auto':'46px'}`">
                                                {{ scope.row.colName }}
                                            </span>
                                        </p>
                                    </el-tooltip>
                                </template>
                            </el-table-column>
                            <el-table-column label="告警数量"
                                             prop="count"
                                             width="160">
                            </el-table-column>

                            <el-table-column label="占比">
                                <template slot-scope="scope">
                                    <YwProgress :datas="{percentage:scope.row.rate,index:scope.$index+1}"
                                                :option="progressOption"></YwProgress>
                                </template>
                            </el-table-column>
                        </el-table>
                    </div>
                </div>
            </div>
        </section>
        <!-- 多图表 -->
    </div>
</template>

<script>

    import DrawChart from 'src/utils/drawCharts.js'
    import { formatDate2 } from 'src/assets/js/utility/rb-filters.js'

    export default {
        mixins: [DrawChart],
        components: {
            YwProgress: () => import('src/components/common/yw-progress.vue'),
        },
        data() {
            return {
                progressOption: {
                    type: 'single',
                    label: {
                        // 位置
                        position: 'outer',// ['outer','inner']
                        // 百分比
                        percentage: {
                            show: true
                        },
                    }
                },
                chartData: {
                    name: '告警内容告警数量Top10',
                    filter: [{ name: '全部', label: '' }, { name: '服务器', label: '服务器' }, { name: '防火墙', label: '防火墙' }, { name: '路由器', label: '路由器' }, { name: '交换机', label: '交换机' }],
                    activeFilter: '',
                    filter2: [{ name: '严重', label: '5' }, { name: '高', label: '4' }, { name: '中', label: '3' }, { name: '低', label: '2' }],
                    activeFilter2: '5',
                    dateRange: [formatDate2(new Date().getTime() - 1000 * 60 * 60 * 24 * 30), formatDate2(new Date())],
                    chartList: [{ tableTitle: [], tableDatas: [], total: 0, }],

                }
            }
        },
        methods: {
            // 获得数据
            getDatas() {
                let params = {
                    'idcType': this.conditionParams.poolActive,
                    'startDate': this.chartData.dateRange[0],
                    'endDate': this.chartData.dateRange[1],
                    'deviceType': this.chartData.activeFilter,
                    'alertLevel': this.chartData.activeFilter2,
                }

                this.$api.queryAlertContent(params).then((res) => {
                    let result = res.result
                    let list = new Array(10)
                    let obj = {
                        colName: '--',
                        count: '--',
                        rate: '--',
                    }
                    if (result) {
                        result.slice(0, 10).forEach((item, index) => {

                            let rete = item.count / res.count
                            let temp = {
                                colName: item.colName ? item.colName : '--',
                                count: item.count ? item.count : '--',
                                rate: res.count ? Math.floor(rete * 100) : 0,
                            }
                            list[index] = temp
                        })

                        list.fill(obj, result.length, 10)
                    } else {
                        list.fill(obj, 0, 10)
                    }

                    this.chartData.chartList[0].tableDatas = list
                    this.chartData.chartList[0].total = res.count
                })

                this.chartData.chartList[0].tableTitle = [
                    { name: 'index', title: '排名' },
                    { name: 'colName', title: '告警内容' },
                    { name: 'count', title: '告警数量' },
                    { name: 'rate', title: '占比' },
                ]

            },

            // 获得总数
            getSummaries() {
                return this.chartData.chartList[0].total
            },

            changeTab() {
                this.getDatas()
            },

        },
        mounted() {
            this.getDatas()
        }

    }
</script>

<style lang="scss" scoped>
    /deep/.content-chart {
        .chart-section {
            padding: 10px 15px;
        }
    }
</style>

