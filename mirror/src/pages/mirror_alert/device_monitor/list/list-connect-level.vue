<template>
    <!-- 告警级别表格（严重、高、中等） -->
    <el-form class="yw-form">
        <div class="yw-el-table-wrap">
            <el-table class="yw-el-table"
                      :data="tableDatas"
                      height="calc(100vh - 340px)"
                      stripe
                      border>
                <el-table-column label="告警级别"
                                 prop="alert_level"
                                 sortable
                                 width="100">
                    <template slot-scope="scope">
                        <AlertStatus width="100px"
                                     v-if="scope.row.alert_level"
                                     :mode="'list'"
                                     :status="scope.row.alert_level"></AlertStatus>
                    </template>
                </el-table-column>
                <el-table-column label="管理网状态"
                                 prop="alert_level"
                                 sortable
                                 width="110">
                </el-table-column>
                <el-table-column label="业务网状态"
                                 prop="alert_level"
                                 sortable
                                 width="110">
                </el-table-column>
                <el-table-column label="带外网状态"
                                 prop="alert_level"
                                 sortable
                                 width="110">
                </el-table-column>
                <el-table-column label="设备级别"
                                 prop="alert_level"
                                 sortable
                                 width="110">
                </el-table-column>
                <el-table-column label="业务系统级"
                                 prop="alert_level"
                                 sortable
                                 width="110">
                </el-table-column>
                <el-table-column label="设备名称"
                                 width="100"
                                 prop="host_name"
                                 sortable>
                    <template slot-scope="scope">
                        <a class="table-link"
                           @click="goToDevice(scope.row)">{{ scope.row['host_name'] }}</a>
                    </template>
                </el-table-column>
                <el-table-column label="设备分类"
                                 width="100"
                                 prop="device_class"
                                 sortable>
                    <template slot-scope="scope">
                        {{ scope.row['device_class'] }}
                    </template>
                </el-table-column>
                <el-table-column label="设备类型"
                                 width="100"
                                 prop="device_type"
                                 sortable>
                    <template slot-scope="scope">
                        {{ scope.row['device_type'] }}
                    </template>
                </el-table-column>
                <el-table-column label="IP"
                                 prop="device_ip"
                                 sortable
                                 width="140">
                    <template slot-scope="scope">
                        <a class="table-link"
                           @click="goToDevice(scope.row)">{{ scope.row['device_ip'] }}</a>
                    </template>
                </el-table-column>
                <el-table-column label="一级租户"
                                 width="120"
                                 prop="department1"
                                 sortable>
                    <template slot-scope="scope">
                        {{ scope.row.department1 }}
                    </template>
                </el-table-column>
                <el-table-column label="二级租户"
                                 width="150"
                                 prop="department2"
                                 sortable>
                    <template slot-scope="scope">
                        {{ scope.row.department2 }}
                    </template>
                </el-table-column>
                <el-table-column label="业务系统"
                                 width="120"
                                 prop="biz_sys"
                                 sortable>
                    <template slot-scope="scope">
                        {{ scope.row['biz_sys'] }}
                    </template>
                </el-table-column>
                <el-table-column label="指标名称"
                                 width="180"
                                 prop="key_comment"
                                 sortable>
                    <template slot-scope="scope">
                        {{ scope.row['key_comment'] }}
                    </template>
                </el-table-column>
                <el-table-column label="当前值"
                                 width="100"
                                 prop="kpi_value"
                                 sortable>
                    <template slot-scope="scope">
                        {{ scope.row['kpi_value'] }}
                    </template>
                </el-table-column>
                <el-table-column label="当前告警"
                                 width="120"
                                 prop="moni_index"
                                 sortable>
                    <template slot-scope="scope">
                        {{ scope.row['moni_index'] }}
                    </template>
                </el-table-column>
                <el-table-column label="告警时间"
                                 width="140"
                                 prop="cur_moni_time"
                                 sortable>
                    <template slot-scope="scope">
                        {{ scope.row['cur_moni_time'] | formatDate }}
                    </template>
                </el-table-column>
            </el-table>
        </div>
        <YwPagination @handleSizeChange="handleSizeChange"
                      @handleCurrentChange="handleCurrentChange"
                      :current-page="currentPage"
                      :page-sizes="pageSizes"
                      :page-size="pageSize"
                      :total="total"></YwPagination>
    </el-form>

</template>

<script>
    import CommonOption from 'src/utils/commonOption.js'
    import YwPaginationOption from 'src/components/common/yw-pagination/yw-pagination.js'
    import rbMonitorService from 'src/services/monitor/rb-monitor-service.factory.js'

    export default {
        mixins: [CommonOption, YwPaginationOption],
        props: ['moduleData'],
        components: {
            YwPagination: () => import('src/components/common/yw-pagination/yw-pagination.vue'),
            AlertStatus: () => import('src/pages/mirror_alert/common/rb-mirror-alert-status.vue'),
        },
        data() {
            return {
                tableTitles: [],
                tableDatas: [],
            }
        },

        methods: {
            // 设置参数
            setParams(activePagination) {

                if (activePagination) {
                    this.queryParams['currentPage'] = this.currentPage
                    this.queryParams['pageSize'] = this.pageSize
                } else {

                    this.queryParams = {
                        'currentPage': this.initPageChange(),
                        'pageSize': this.pageSize,

                        'idcType': this.moduleData.conditionParams['idcType'],
                        'pod_name': this.moduleData.conditionParams['pod_name'],
                        'roomId': this.moduleData.conditionParams['roomId'],
                        'device_type': this.moduleData.conditionParams['device_type'],
                        'department1': this.moduleData.conditionParams['department1'],
                        'department2': this.moduleData.conditionParams['department2'],
                        'bizSystem': this.moduleData.conditionParams['bizSystem'],
                        'device_ip': this.moduleData.conditionParams['ip'],
                        'device_id': this.moduleData.conditionParams['device_id'],
                        'alert_level': this.moduleData.tabConnectParams['name'],
                    }
                }

            },

            /** 查询
             * activePagination:分页活动下保持先前的查询条件
             */
            query(activePagination = false) {
                this.showFullScreenLoading({ text: '正在查询数据, 请稍等...' })
                this.setParams(activePagination)

                let params = this.queryParams

                return rbMonitorService.deviceAlertList(params).then((res) => {
                    this.total = res.totalSize || 0
                    this.tableDatas = res.data
                    return res
                }).finally(() => {
                    this.closeFullScreenLoading()
                })
            },

            // 跳转到设备分析页
            goToDevice(row) {
                let path = ''
                if (row['device_class'] === '服务器') {
                    path = '/mirror_alert/device_monitor/device_server'
                } else {
                    path = '/mirror_alert/device_monitor/device_network'
                }

                if (path) {
                    this.$router.push({
                        path: path,
                        query: {
                            deviceIP: row['device_ip'],
                            deviceIdByIp: row['device_id']
                        }
                    })
                }
            },
            // 跳转到设备指标页
            goToIndex() {

            }

        },
        mounted() {
            this.query()
        },
    }
</script>

<style lang="scss" scoped>
    /deep/.el-table .cell {
        white-space: nowrap;
        min-width: 50px;
    }
</style>
