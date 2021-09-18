<template>
    <!-- 告警：最新热点告警 模块标题调整为：当前告警 -->
    <div class="content-chart"
         style="width:100%;height:1000px">
        <section class="chart-title-wrap clearfix">
            <svg class="svg-icon svg-icon-24"
                 aria-hidden="true">
                <use xlink:href="#iconzuixinrediangaojing"></use>
            </svg>
            <span class="chart-title">{{chartData.name}}</span>
            <div class="chart-filter"
                 style="width:75%">
                <!-- <p class="chart-filter-item hot-status"
                   style="width:25%"><span class="hot-status-title">告警总数:</span><span class="hot-status-num text-ellipse"
                          @click="goto(0, '')">{{chartData.statusList.total}}</span></p>
                <p class="chart-filter-item"
                   style="width:50%">
                    <YwStatusLamp :datas="chartData.statusList.list"></YwStatusLamp>
                </p>
                <p class="chart-filter-item hot-status"
                   style="width:20%"><span class="hot-status-title">已确认:</span><span class="hot-status-num text-ellipse"
                          @click="goto(1, '')">{{chartData.statusList.confirmed}}</span></p> -->
                <el-radio-group class="yw-radio-button-wrap chart-filter-item"
                                v-model="chartData.activeFilter"
                                @change="changeTab">
                    <el-radio-button :label="tabItem.label"
                                     v-for="(tabItem,tabIndex) in chartData.filter"
                                     :key="tabIndex">{{tabItem.name}}</el-radio-button>
                </el-radio-group>
                <el-button class="chart-filter-item yw-chart-button-wrap"
                           style="margin-left:20px"
                           type="primary"
                           @click="exportDatas">
                    <svg class="svg-icon svg-icon-20"
                         style="margin:0 5px 0 0;"
                         aria-hidden="true">
                        <use xlink:href="#icondaochu1"></use>
                    </svg><span class="inline-block-middle">导出</span>
                </el-button>
            </div>
        </section>

        <!-- 多图表 -->
        <section class="chart-section">
            <div class="chart-box-wrap">
                <div class="chart-box-item"
                     style="width:100%">
                    <YwTable :tableTitles="tableTitles"
                             v-if="resetComponent"
                             :option="{'highlight-current-row':true,'enClick':true,'selfClass':'column-no-padding'}"
                             @sortTable="sortTable"
                             @clickTableRow="clickTableRow"
                             :tableDatas="tableDatas"></YwTable>

                </div>
            </div>
        </section>
        <!-- 多图表 -->
    </div>
</template>

<script>
    import updateComponent from 'src/utils/updateComponent.js'
    import QueryObject from 'src/utils/queryObject.js'
    import { alert_level_obj } from 'src/pages/mirror_alert/alert_his/config/options.js'
    import { formatDate, formatToTime } from 'src/assets/js/utility/rb-filters.js'

    export default {
        mixins: [QueryObject, updateComponent],
        props: {
            conditionParams: {
                type: Object,
                default() {
                    return {}
                }
            }
        },
        components: {
            YwAlertHot: () => import('src/components/common/yw-alert-hot.vue'),
            YwStatusLamp: () => import('src/components/common/yw-status-lamp.vue'),
            YwTable: () => import('src/components/common/yw-table-autoScroll.vue'),
        },
        data() {
            return {
                chartData: {
                    name: '当前告警',
                    filter: [{ name: '待处理', label: 0 }, { name: '处理中', label: 1 }],
                    activeFilter: 0,
                    statusList: {
                        total: 0,
                        list: [],
                        confirmed: 0
                    },
                    filter2: [{ name: '导出', label: '导出' }],
                    activeFilter2: '导出',
                },
                tableTitles: [],
                tableDatas: [],
            }
        },
        methods: {
            // 表格点击事件
            clickTableRow(row) {
                let id = row.alert_id
                this.$router.push({
                    path: '/mirror_alert/alert/detail',
                    query: {
                        alertId: id,
                        detailType: 'alert'
                    }
                })

            },
            // 表格排序
            sortTable(datas) {
                this.tableDatas = datas
            },
            goto(status, level) {
                let path = '/mirror_alert/alert/manage'
                if (status === 3 || status === '3') {
                    path = '/mirror_alert/alert_his/his_alert'
                    status = 3
                }
                this.$router.push({
                    path: path,
                    query: {
                        alertType: 'main',
                        operation_status: status,
                        alert_level: level === '' ? '' : 5 - level
                    }
                })
            },
            // 获得数据
            getDatas() {
                // this.getAlertTotalDatas()
                this.getTableDatas()
            },
            changeTab() {
                this.getDatas()
            },
            getTableDatas() {
                // 更新表格
                // this.updateComponent()

                this.tableTitles = [
                    { name: 'level', title: '级别', style: { sortable: true, isHtml: true, width: 70 } },
                    { name: 'device_ip', title: '设备IP', style: { sortable: true, width: 110 } },
                    { name: 'idc_type', title: '资源池', style: { sortable: true, width: 80, align: 'left' } },
                    { name: 'device_class', title: '告警分类', style: { sortable: true, width: 100 } },
                    { name: 'cur_moni_time', title: '告警时间', style: { sortable: true, isHtml: true, width: 100 } },
                    { name: 'moni_index', title: '告警内容', style: {} },
                ]
                let params = {
                    'idcType': this.conditionParams.poolActive || '',
                    'limit': 30,
                    'operateStatus': this.chartData.activeFilter
                }
                this.$api.queryAlertHotNew(params).then((res) => {
                    this.tableDatas = res.map((item) => {
                        let levelFlag = `<span class="alert-status-icon bg-linear-${alert_level_obj[item.alert_level].color}">${alert_level_obj[item.alert_level].name}</span>`

                        return {
                            'device_ip': item['device_ip'],
                            // 'device_ip': '255.255.255.255',
                            'idc_type': item['idc_type'] + ' ',
                            'level': item.alert_level,
                            'levelHtml': levelFlag,
                            'biz_sys': item['biz_sys'],
                            'device_class': item['device_class'],
                            'moni_index': item['moni_index'],
                            'cur_moni_time': formatDate(item['cur_moni_time']),
                            'cur_moni_timeHtml': formatToTime(item['cur_moni_time']),
                            'alert_id': item['alert_id']
                        }
                    })
                })

            },
            getAlertTotalDatas() {
                this.chartData.statusList.list = [
                    {
                        status: 'red',
                        num: 0
                    },
                    {
                        status: 'orange',
                        num: 0
                    },
                    {
                        status: 'yellow',
                        num: 0
                    },
                    {
                        status: 'blue',
                        num: 0
                    }
                ]

                let params = {
                    'idcType': this.conditionParams.poolActive,
                }
                this.$api.queryAlertOverview(params).then((res) => {

                    this.chartData.statusList.confirmed = res.confirmed.summary || 0

                    this.chartData.statusList.total = res.toBeResolved.summary || 0
                    this.chartData.statusList.list[0].num = res.toBeResolved.serious || 0
                    this.chartData.statusList.list[1].num = res.toBeResolved.high || 0
                    this.chartData.statusList.list[2].num = res.toBeResolved.medium || 0
                    this.chartData.statusList.list[3].num = res.toBeResolved.low || 0
                })
            },
            exportDatas() {
                let params = {
                    'idcType': this.conditionParams.poolActive,
                    // 'limit': 20
                }
                this.rbHttp.sendRequest({
                    method: 'POST',
                    data: params,
                    binary: true,
                    url: '/v1/alerts/indexPage/exportLatest'
                }).then((res) => {
                    this.exportFiles({
                        data: res,
                        fileType: 'application/vnd.ms-excel',
                        fileName: '最新热点告警.xls'
                    })
                    return res
                })
            },

        },
        mounted() {
            this.$utils.creatInterval(this, this.getDatas, 300000)
        },
        activated() {
            this.$utils.creatInterval(this, this.getDatas, 300000)
        }

    }
</script>

<style lang="scss" scoped>
    .chart-section {
        padding: 15px;
    }
    .chart-filter {
        .hot-status {
            font-size: $font-14;
            color: $color-base;
            .hot-status-title {
                display: inline-block;
                vertical-align: middle;
            }
            .hot-status-num {
                width: 40%;
            }
        }
    }
    .chart-title-wrap {
        .svg-icon {
            transform: rotate(90reg);
        }
    }
</style>

