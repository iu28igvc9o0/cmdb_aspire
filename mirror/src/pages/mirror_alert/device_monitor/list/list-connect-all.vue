<template>
    <!-- 全量表格 -->
    <el-form class="yw-form">
        <div class="yw-el-table-wrap">
            <el-table class="yw-el-table"
                      :data="tableDatas"
                      height="calc(100vh - 340px)"
                      stripe
                      border>
                <el-table-column label="管理网状态"
                                 sortable
                                 width="100">
                    <template slot-scope="scope">
                        <template v-if="scope.row.ip">
                            <span v-if="scope.row.status_ipmiIp === '11462'"
                                  class="status-marking bgred"></span>
                            <span v-else
                                  class="status-marking bggreen"></span>
                            {{scope.row.ip}}
                        </template>
                    </template>
                </el-table-column>
                <el-table-column label="业务网状态"
                                 sortable
                                 width="100">
                    <template slot-scope="scope">
                        <template v-if="scope.row.ServiceIP">
                            <span v-if="scope.row.status_serviceIp === '11462'"
                                  class="status-marking bgred"></span>
                            <span v-else
                                  class="status-marking bggreen"></span>
                            {{scope.row.ServiceIP}}
                        </template>
                    </template>
                </el-table-column>
                <el-table-column label="带外网状态"
                                 sortable
                                 width="100">
                    <template slot-scope="scope">
                        <template v-if="scope.row.ipmi_ip">
                            <span v-if="scope.row.status_ipmiIp === '11462'"
                                  class="status-marking bgred"></span>
                            <span v-else
                                  class="status-marking bggreen"></span>
                            {{scope.row.ipmiIp}}
                        </template>
                    </template>

                </el-table-column>
                <el-table-column label="设备级别"
                                 prop="device_level"
                                 sortable
                                 width="100">
                </el-table-column>
                <el-table-column label="业务系统级"
                                 prop="bizsystem_level"
                                 sortable
                                 width="100">
                </el-table-column>

                <el-table-column label="设备名称"
                                 width="100"
                                 prop="device_name"
                                 sortable>
                    <template slot-scope="scope">
                        <a class="table-link"
                           @click="goToDevice(scope.row)">{{ scope.row['device_name'] }}</a>
                    </template>
                </el-table-column>
                <el-table-column label="设备分类"
                                 width="100"
                                 prop="device_class"
                                 sortable>
                    <template slot-scope="scope">
                        {{scope.row['device_class']}}
                    </template>
                </el-table-column>
                <el-table-column label="设备类型"
                                 width="120"
                                 prop="device_type"
                                 sortable>
                    <template slot-scope="scope">
                        {{ scope.row['device_type'] }}
                    </template>
                </el-table-column>
                <el-table-column label="IP"
                                 prop="ip"
                                 sortable
                                 width="140">
                    <template slot-scope="scope">
                        <a class="table-link"
                           @click="goToDevice(scope.row)">{{ scope.row.ip }}</a>
                    </template>
                </el-table-column>
                <el-table-column label="一级租户"
                                 width="140"
                                 prop="department1"
                                 sortable>
                    <template slot-scope="scope">
                        {{ scope.row.department1 }}
                    </template>
                </el-table-column>
                <el-table-column label="二级租户"
                                 width="180"
                                 prop="department2"
                                 sortable>
                    <template slot-scope="scope">
                        {{ scope.row.department2 }}
                    </template>
                </el-table-column>
                <el-table-column label="业务系统"
                                 width="120"
                                 prop="bizSystem"
                                 sortable>
                    <template slot-scope="scope">
                        {{ scope.row.bizSystem }}
                    </template>
                </el-table-column>
                <el-table-column label="CPU利用率"
                                 width="160"
                                 prop="CPU_PUSED"
                                 sortable>
                    <template slot-scope="scope">
                        <span v-if="scope.row['CPU_PUSED']"> {{ scope.row['CPU_PUSED']}}%</span>
                    </template>
                </el-table-column>
                <el-table-column label="内存利用率"
                                 width="160"
                                 prop="MEMORY_PUSED"
                                 sortable>
                    <template slot-scope="scope">
                        <span v-if="scope.row['MEMORY_PUSED']"> {{ scope.row['MEMORY_PUSED']}}%</span>

                    </template>
                </el-table-column>

                <el-table-column label="操作"
                                 width="100px">
                    <template slot-scope="scope">
                        <el-button @click="goToIndex(scope.row)"
                                   title="查看设备指标"
                                   type="text"
                                   icon="el-icon-view"></el-button>
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
        props: ['moduleData', 'optionsParams'],
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
                        'ip': this.moduleData.conditionParams['ip'],

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

                let params = Object.assign(this.queryParams, this.optionsParams)
                return rbMonitorService.instancePing(params).then((res) => {
                    this.cmdbColumnsConvert = res.columns || {}
                    this.total = res.totalSize || 0
                    this.tableDatas = res.data.map((item) => {
                        return {
                            'id': item.id,
                            'alert_level': item['alert_level'],

                            'device_name': this.convertCmdb(item, 'device_name'),
                            'device_class': this.convertCmdb(item, 'device_class'),
                            'device_class_id': item['device_class'],
                            'device_type': this.convertCmdb(item, 'device_type'),
                            'ip': this.convertCmdb(item, 'ip'),
                            'department1': this.convertCmdb(item, 'department1'),
                            'department2': this.convertCmdb(item, 'department2'),
                            'bizSystem': this.convertCmdb(item, 'bizSystem'),

                            'CPU_PUSED': item['CPU_PUSED'],
                            'MEMORY_PUSED': item['MEMORY_PUSED'],

                        }
                    })


                    return res

                }).finally(() => {
                    this.closeFullScreenLoading()
                })

            },

            // 跳转到设备分析页
            goToDevice(row) {
                let path = ''
                if (row['device_class_id'] === '9564') {
                    // 网络设备
                    path = '/mirror_alert/device_monitor/device_network'
                } else {
                    // 服务器
                    path = '/mirror_alert/device_monitor/device_server'
                }

                if (path) {
                    this.$router.push({
                        path: path,
                        query: {
                            deviceIP: row.ip,
                            deviceIdByIp: row.id
                        }
                    })
                }

            },
            // 跳转到设备指标页
            goToIndex(row) {
                this.$router.push({
                    path: '/mirror_alert/device_monitor/device_index',
                    query: {
                        row: row
                    }
                })
            }

        },
        mounted() {
            this.query()
        },
    }
</script>

<style lang="scss" scoped>
</style>
