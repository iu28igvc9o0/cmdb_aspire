<!--  -->
<template>
    <div>
        <el-form class="yw-form components-condition">
            <!-- <div class="table-operate-wrap clearfix">
                <el-form :inline="true">
                    <el-form-item label="异常检索：">
                        <el-checkbox v-model="searchParams.status_ipmiIp"
                                     true-label='11462'
                                     false-label='11463'>带外网络异常</el-checkbox>
                        <el-checkbox v-model="searchParams.status_serviceIp"
                                     true-label='11462'
                                     false-label='11463'>业务网络异常</el-checkbox>
                        <el-checkbox v-model="searchParams.status_manageIp"
                                     true-label='11462'
                                     false-label='11463'>管理网络异常</el-checkbox>
                    </el-form-item>
                    <el-form-item>
                        <el-button type="primary"
                                   @click="exportList()">导出</el-button>
                    </el-form-item>
                </el-form>
            </div> -->

            <div class="yw-el-table-wrap">
                <el-table class="yw-el-table"
                          style="cursor:pointer;margin-top:10px"
                          :data="tableDatas"
                          stripe
                          tooltip-effect="dark"
                          border
                          @selection-change="changeCheckbox"
                          height="calc(100vh - 280px)"
                          size="samll">
                    <el-table-column type="selection"
                                     width="40px"></el-table-column>
                    <el-table-column label="管理网状态"
                                     show-overflow-tooltip
                                     sortable
                                     width="120">
                        <template slot-scope="scope">
                            <template v-if="scope.row.ip">
                                <span v-if="scope.row.status_manageIp === '是'"
                                      class="status-marking bgred"></span>
                                <span v-else
                                      class="status-marking bggreen"></span>
                                {{scope.row.ip}}
                            </template>
                        </template>
                    </el-table-column>
                    <el-table-column label="业务网状态"
                                     show-overflow-tooltip
                                     sortable
                                     width="120"
                                     v-if="moduleData.condicationCode==='alert_phy'||moduleData.condicationCode==='alert_vm'">
                        <template slot-scope="scope">
                            <template v-if="scope.row.ServiceIP">
                                <span v-if="scope.row.status_serviceIp === '是'"
                                      class="status-marking bgred"></span>
                                <span v-else
                                      class="status-marking bggreen"></span>
                                {{scope.row.ServiceIP}}
                            </template>
                        </template>
                    </el-table-column>
                    <el-table-column label="带外网状态"
                                     show-overflow-tooltip
                                     sortable
                                     width="120"
                                     v-if="moduleData.condicationCode==='alert_phy'||moduleData.condicationCode==='alert_other'">
                        <template slot-scope="scope">
                            <template v-if="scope.row.ipmi_ip">
                                <span v-if="scope.row.status_ipmiIp === '是'"
                                      class="status-marking bgred"></span>
                                <span v-else
                                      class="status-marking bggreen"></span>
                                {{scope.row.ipmi_ip}}
                            </template>
                        </template>
                    </el-table-column>
                    <el-table-column label="最后更新时间"
                                     prop="latest_ping_time"
                                     show-overflow-tooltip
                                     sortable
                                     width="120">
                    </el-table-column>
                    <el-table-column label="设备级别"
                                     prop="device_level"
                                     sortable
                                     width="100">
                    </el-table-column>
                    <el-table-column label="业务系统级别"
                                     prop="bizsystem_level_dict_note_name"
                                     sortable
                                     width="120">
                    </el-table-column>
                    <el-table-column label="设备名称"
                                     width="100"
                                     prop="device_name"
                                     show-overflow-tooltip
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
                                     show-overflow-tooltip
                                     sortable>
                        <template slot-scope="scope">
                            {{ scope.row.bizSystem }}
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
    </div>
</template>

<script>
    import rbMonitorService from 'src/services/monitor/rb-monitor-service.factory.js'
    import cmdbService from 'src/services/cmdb/rb-cmdb-service.factory.js'

    import CommonOption from 'src/utils/commonOption.js'
    import YwPaginationOption from 'src/components/common/yw-pagination/yw-pagination.js'

    export default {
        data() {
            return {
                tableDatas: [],
                searchParams: {
                    status_manageIp: '11463',
                    status_ipmiIp: '11463',
                    status_serviceIp: '11463'
                },
                abnormalList: []
            }
        },
        components: {
            YwPagination: () => import('src/components/common/yw-pagination/yw-pagination.vue'),

        },
        mixins: [CommonOption, YwPaginationOption],
        props: ['moduleData'],
        mounted() {
            this.query()
            // this.getQueryParams()
        },
        methods: {
            setParams(activePagination) {

                if (activePagination) {
                    this.searchParams['currentPage'] = this.currentPage
                    this.searchParams['pageSize'] = this.pageSize
                } else {

                    this.queryParams = {
                        'currentPage': this.initPageChange(),
                        'pageSize': this.pageSize,

                        'idcType': this.moduleData['idcType'],
                        'pod_name': this.moduleData['pod_name'],
                        'roomId': this.moduleData['roomId'],
                        'device_type': this.moduleData['device_type'],
                        'department1': this.moduleData['department1'],
                        'department2': this.moduleData['department2'],
                        'bizSystem': this.moduleData['bizSystem'],
                        'ip': this.moduleData['ip'],


                    }
                }

            },
            query() {
                // let params = Object.assign(this.searchParams, this.moduleData)
                let params = this.moduleData
                params.currentPage = this.currentPage
                // params.currentPage = this.initPageChange()
                params.pageSize = this.pageSize
                // this.searchParams.currentPage = this.initPageChange()
                // this.searchParams.pageSize = this.pageSize

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
                            'ServiceIP': this.convertCmdb(item, 'ServiceIP'),
                            'ipmi_ip': this.convertCmdb(item, 'ipmi_ip'),
                            'department1': this.convertCmdb(item, 'department1'),
                            'department2': this.convertCmdb(item, 'department2'),
                            'bizSystem': this.convertCmdb(item, 'bizSystem'),
                            'status_ipmiIp': this.convertCmdb(item, 'status_ipmiIp'),
                            'status_manageIp': this.convertCmdb(item, 'status_manageIp'),
                            'status_serviceIp': this.convertCmdb(item, 'status_serviceIp'),
                            'latest_ping_time': this.convertCmdb(item, 'latest_ping_time'),
                            // 'device_level': this.convertCmdb(item, 'device_level'),
                            // 'bizsystem_level_dict_note_name': this.convertCmdb(item, 'bizsystem_level_dict_note_name'),

                            'bizsystem_level_dict_note_name': item['bizsystem_level_dict_note_name'],
                            'device_level': item['device_level'],
                            // 'MEMORY_PUSED': item['MEMORY_PUSED'],

                        }
                    })
                    console.log('this.tableDatas===', this.tableDatas)

                    return res

                })

            },
            changeCheckbox() { },
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
            getQueryParams() {
                let req = {
                    type: 'whether'
                }
                return cmdbService
                    .getDictsByType(req)
                    .then(res => {
                        this.abnormalList = res
                    })
            },

        },
        watch: {
            searchParams(newVal, oldVal) {
                if (newVal) {
                    // this.query()
                }
            },

        }
    }
</script>
<style lang='scss' scoped>
</style>