<template>
    <!-- 监控： 业务资源利用率 -->
    <div class="content-chart"
         style="width: 100%;height:310px">
        <section class="chart-title-wrap clearfix">
            <svg class="svg-icon svg-icon-24"
                 aria-hidden="true">
                <use xlink:href="#icongaojingneirong"></use>
            </svg>
            <span class="chart-title">{{this.moduleName || chartData.name}}</span>
            <div class="chart-filter">
                <el-radio-group class="yw-radio-button-wrap chart-filter-item"
                                v-model="tabData.activeFilter"
                                @change="getDatas">
                    <el-radio-button :label="tabItem.label"
                                     v-for="(tabItem,tabIndex) in tabData.filter"
                                     :key="tabIndex">{{tabItem.name}}</el-radio-button>
                </el-radio-group>
                <el-radio-group class="yw-radio-button-wrap chart-filter-item mleft10"
                                v-model="tabData.activeFilter2"
                                @change="getDatas">
                    <el-radio-button :label="tabItem.label"
                                     v-for="(tabItem,tabIndex) in tabData.filter2"
                                     :key="tabIndex">{{tabItem.name}}</el-radio-button>
                </el-radio-group>
                <el-select v-model="segmentAddressSelected"
                           @change="getDatas"
                           filterable
                           class="mleft10"
                           placeholder="请选择分类">
                    <el-option v-for="val in segmentAddressList"
                               :key="val.field_value"
                               :label="val.field_name"
                               :value="val.field_value"></el-option>
                </el-select>
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
    import rbKeguanServices from 'src/services/iframe/rb-keguan-services.js'

    export default {
        mixins: [DrawChart],
        components: {
        },
        data() {
            return {
                chartData: {
                    name: '业务资源利用率',
                    chartList: [
                        { tableTitle: [], tableDatas: [] },
                        { tableTitle: [], tableDatas: [] }
                    ],
                },
                tabData: {
                    filter: [
                        { label: 'X86服务器', name: '物理机' },
                        { label: '虚拟机', name: '虚拟机' }
                    ],
                    activeFilter: '虚拟机',
                    filter2: [
                        { name: 'CPU', label: 'cpu' },
                        { name: '内存', label: 'memory' },
                        { name: '磁盘', label: 'disk' }
                    ],
                    activeFilter2: 'cpu',
                },
                // 网段列表
                segmentAddressList: [
                    {
                        field_name: '全部网段',
                        field_value: ''
                    }
                ],
                segmentAddressSelected: '',
            }
        },
        methods: {
            // 获取网段地址列表
            getAllSegmentAddress() {
                rbKeguanServices.getAllSegmentAddress().then((res) => {
                    this.segmentAddressList = [].concat(this.segmentAddressList, res)
                })
            },
            // 获取业务资源利用率
            getBizSystemDeviceUsedRate(stateType) {
                let params = {
                    'startDate': this.conditionParams.dateRange[0],
                    'endDate': this.conditionParams.dateRange[1],
                    'deviceType': this.tabData.activeFilter,
                    'sourceType': this.tabData.activeFilter2,
                    'segmentAddr': this.segmentAddressSelected,
                    'stateType': stateType,
                }
                let dataIndex = stateType === 'avg' ? 0 : 1
                rbKeguanServices.getBizSystemDeviceUsedRate(params).then((res) => {
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
                    this.chartData.chartList[dataIndex].tableDatas = list
                })
            },
            // 获得数据
            getDatas() {
                this.getBizSystemDeviceUsedRate('avg')
                this.getBizSystemDeviceUsedRate('max')

                let titleType = 'CPU'
                if (this.tabData.activeFilter2 == 'memory') {
                    titleType = '内存'
                } else if (this.tabData.activeFilter2 == 'disk') {
                    titleType = '磁盘'
                }

                // 默认列表数据
                let tableList = [
                    { name: 'index', title: '排名' },
                    { name: 'bizSystem', title: '业务系统' }
                ]

                this.chartData.chartList[0].tableTitle = this.$utils.deepClone(tableList)
                this.chartData.chartList[0].tableTitle.push(
                    { name: 'value', title: `${titleType}月度平均利用率` }
                )

                this.chartData.chartList[1].tableTitle = this.$utils.deepClone(tableList)
                this.chartData.chartList[1].tableTitle.push(
                    { name: 'value', title: `${titleType}月度峰值利用率` }
                )

            },
        },
        mounted() {
            this.getDatas()
            this.getAllSegmentAddress()
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

