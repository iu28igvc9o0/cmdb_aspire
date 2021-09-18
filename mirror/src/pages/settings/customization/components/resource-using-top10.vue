<template>
    <!-- 陕西环境 综合首页、监控首页： 实时资源利用率TOP10 -->
    <div class="content-chart"
         style="width: 100%; height:482px">
        <section class="chart-title-wrap clearfix">
            <svg class="svg-icon svg-icon-24"
                 aria-hidden="true">
                <use xlink:href="#icongaojingneirong"></use>
            </svg>
            <span class="chart-title">{{this.moduleName || chartData.name}}</span>
        </section>

        <!-- 表格排行榜 -->
        <section class="chart-section">
            <div class="chart-box-wrap">
                <div class="chart-box-item"
                     style="width:33.3%"
                     v-for="(subItem,subIndex) in chartData.chartList"
                     :key="subIndex">
                    <el-table class="yw-rank-table"
                              v-loading="loading[subIndex]"
                              height="100%"
                              element-loading-text="请稍候"
                              element-loading-background="rgba(8, 33, 81, 0.9)"
                              :data="subItem.tableDatas">
                        <el-table-column :label="titleItem.title"
                                         show-overflow-tooltip
                                         v-for="(titleItem,index) in subItem.tableColumn"
                                         :key="index"
                                         :width="titleItem.width ? titleItem.width : 'auto'"
                                         :min-width="titleItem.minWidth ? titleItem.minWidth : 'auto'">
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
    </div>
</template>

<script>

    import DrawChart from 'src/utils/drawCharts.js'
    import rbAlertServices from 'src/services/alert/rb-alert-auto-order-services.factory.js'

    export default {
        mixins: [DrawChart],
        components: {
        },
        data() {
            return {
                loading: [false, false, false],
                chartData: {
                    name: '实时资源利用率TOP10',
                    chartList: [
                        { tableColumn: [], tableDatas: [] },
                        { tableColumn: [], tableDatas: [] },
                        { tableColumn: [], tableDatas: [] }
                    ],
                },
            }
        },
        methods: {
            // 获取业务资源利用率
            getDevicePusedTopN(tableIndex, kpi) {
                this.loading[tableIndex] = true
                let req = {
                    kpi: kpi,
                    idcType: this.conditionParams.poolActive,
                }
                rbAlertServices.getDevicePusedTopN(req).then((res) => {
                    let list = new Array(10)
                    let obj = {
                        bizSystem: '--',
                        ip: '--',
                        value: '--'
                    }
                    if (res) {
                        res.slice(0, 10).forEach((item, index) => {
                            let temp = {
                                bizSystem: item.bizSystem ? item.bizSystem : '--',
                                ip: item.ip ? item.ip : '--',
                                value: item.value ? item.value.toFixed(2) + '%' : '--',
                            }
                            list[index] = temp
                        })

                        list.fill(obj, res.length, 10)
                    } else {
                        list.fill(obj, 0, 10)
                    }
                    this.chartData.chartList[tableIndex].tableDatas = list
                    this.loading[tableIndex] = false
                })
            },
            // 获得数据
            getDatas() {
                // kpi 必填 String   指标 CPU_PUSED-cpu利用率；MEMORY_PUSED-内存利用率；DISK_PUSED_ROOT-磁盘根目录利用率
                this.getDevicePusedTopN('0', 'DISK_PUSED_ROOT')
                this.getDevicePusedTopN('1', 'CPU_PUSED')
                this.getDevicePusedTopN('2', 'MEMORY_PUSED')

                // 默认列表数据
                let columns = [
                    { name: 'index', title: '排名', width: '60' },
                    { name: 'bizSystem', title: '业务系统' },
                    { name: 'ip', title: '设备IP' }
                ]
                let lastColumns = [
                    { name: 'value', title: '磁盘(根目录使用率)', minWidth: '150' },
                    { name: 'value', title: 'CPU(五分钟CPU占有率)', minWidth: '150' },
                    { name: 'value', title: '内存(系统内存占有率)', minWidth: '150' }
                ]

                this.chartData.chartList.forEach((item, index) => {
                    item.tableColumn = this.$utils.deepClone(columns)
                    this.chartData.chartList[index].tableColumn.push(
                        lastColumns[index]
                    )
                })

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

