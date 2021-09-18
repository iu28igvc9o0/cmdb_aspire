<template>
    <!-- 监控：租户月度资源利用率最低Top5 -->
    <div class="content-chart"
         style="width: 100%;height:310px">
        <section class="chart-title-wrap clearfix">
            <svg class="svg-icon svg-icon-24"
                 aria-hidden="true">
                <use xlink:href="#iconliyongshuai"></use>
            </svg>
            <span class="chart-title">{{this.moduleName || chartData.name}}</span>
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
            </div>
        </section>

        <!-- 多图表 -->
        <section class="chart-section">
            <div class="chart-box-wrap">
                <div class="chart-box-item"
                     style="width:49%"
                     v-for="(subItem,subIndex) in chartData.chartList"
                     :key="subIndex">
                    <el-table class="yw-rank-table"
                              :data="subItem.tableDatas">
                        <el-table-column :label="titleItem.title"
                                         v-for="(titleItem,index) in subItem.tableTitle"
                                         :key="index"
                                         :width="'index' === titleItem.name ? '60' : 'auto'">
                            <template slot-scope="scope">
                                <span v-if="'index' === titleItem.name"
                                      :class="rankColor(scope.$index+1)">
                                    {{ scope.$index+1}}
                                </span>
                                <span v-else-if="'value' === titleItem.name"
                                      :class="rankColor(scope.$index+1)">
                                    {{ scope.row[titleItem.name]}}
                                </span>
                                <el-tooltip effect="dark"
                                            v-else
                                            :content="scope.row[titleItem.name]"
                                            placement="top-start">
                                    <span class="text-ellipse">{{ scope.row[titleItem.name] }}</span>
                                </el-tooltip>

                            </template>
                        </el-table-column>
                    </el-table>
                </div>
            </div>
        </section>
        <!-- 多图表 -->
    </div>
</template>

<script>

    import DrawChart from 'src/utils/drawCharts.js'

    export default {
        mixins: [DrawChart],
        components: {
            YwProgress: () => import('src/components/common/yw-progress.vue'),
        },
        data() {
            return {
                chartData: {
                    name: '租户月度资源利用率最低Top5',
                    filter: [{ name: 'X86服务器', label: 'X86服务器' }, { name: '云主机', label: '云主机' }],
                    activeFilter: 'X86服务器',
                    filter2: [{ name: 'CPU', label: 'cpu' }, { name: '内存', label: 'memory' }],
                    activeFilter2: 'cpu',
                    chartList: [{ tableTitle: [], tableDatas: [] }, { tableTitle: [], tableDatas: [] }],

                }
            }
        },
        methods: {
            // 获得数据
            getDatas() {

                let params = {
                    'idcType': this.conditionParams.poolActive,
                    'startDate': this.conditionParams.dateRange[0],
                    'endDate': this.conditionParams.dateRange[1],
                    'deviceType': this.chartData.activeFilter,
                    'sourceType': this.chartData.activeFilter2,
                    'stateType': 'avg',
                }
                this.$api.queryUserTop5(params).then((res) => {
                    let list = new Array(5)
                    let obj = {
                        bizSystem: '--',
                        department2: '--',
                        department1: '--',
                        value: '--'
                    }
                    if (res) {
                        res.slice(0, 5).forEach((item, index) => {
                            let temp = {
                                bizSystem: item.bizSystem ? item.bizSystem : '--',
                                department2: item.department2 ? item.department2 : '--',
                                department1: item.department1 ? item.department1 : '--',
                                value: item.value ? item.value.toFixed(2) + '%' : '--',
                            }
                            list[index] = temp
                        })

                        list.fill(obj, res.length, 5)
                    } else {
                        list.fill(obj, 0, 5)
                    }
                    this.chartData.chartList[0].tableDatas = list

                })

                let params2 = {
                    'idcType': this.conditionParams.poolActive,
                    'startDate': this.conditionParams.dateRange[0],
                    'endDate': this.conditionParams.dateRange[1],
                    'deviceType': this.chartData.activeFilter,
                    'sourceType': this.chartData.activeFilter2,
                    'stateType': 'max',
                }
                this.$api.queryUserTop5(params2).then((res) => {
                    let list = new Array(5)
                    let obj = {
                        bizSystem: '--',
                        department2: '--',
                        department1: '--',
                        value: '--'
                    }
                    if (res) {
                        res.forEach((item, index) => {
                            let temp = {
                                bizSystem: item.bizSystem ? item.bizSystem : '--',
                                department2: item.department2 ? item.department2 : '--',
                                department1: item.department1 ? item.department1 : '--',
                                value: item.value ? item.value.toFixed(2) + '%' : '--',
                            }
                            list[index] = temp
                        })

                        list.fill(obj, res.length, 5)
                    } else {
                        list.fill(obj, 0, 5)
                    }
                    this.chartData.chartList[1].tableDatas = list

                })

                let titleType = 'CPU'
                if (this.chartData.activeFilter2 == 'memory') {
                    titleType = '内存'
                }

                // 默认列表数据
                let tableList = [
                    { name: 'index', title: '排名' },
                    { name: 'bizSystem', title: '业务系统' },
                    { name: 'department2', title: '归属部门（二级）' },
                    { name: 'department1', title: '所属租户（一级）' }
                ]

                // 科管项目列表数据
                if (this.moduleName === '业务资源利用率') {

                    tableList = [
                        { name: 'index', title: '排名' },
                        { name: 'bizSystem', title: '业务系统' }
                    ]
                }

                this.chartData.chartList[0].tableTitle = this.$utils.deepClone(tableList)
                this.chartData.chartList[0].tableTitle.push(
                    { name: 'value', title: `${titleType}月度平均利用率` }
                )

                this.chartData.chartList[1].tableTitle = this.$utils.deepClone(tableList)
                this.chartData.chartList[1].tableTitle.push(
                    { name: 'value', title: `${titleType}月度峰值利用率` }
                )

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
    /deep/.chart-section {
        padding: 20px 30px;
        .chart-box-wrap {
            .chart-box-item + .chart-box-item:before {
                display: none;
            }
        }
    }
</style>

