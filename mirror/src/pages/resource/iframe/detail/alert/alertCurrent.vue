<template>
    <div>
        <div class="yw-el-table-wrap">
            <el-table border
                      class="yw-el-table"
                      v-loading="loading"
                      height="calc(100vh - 260px)"
                      :data="activityAlertData"
                      style="cursor: pointer;"
                      stripe
                      tooltip-effect="dark">
                <el-table-column label="级别"
                                 width="75px">
                    <template slot-scope="scope">
                        <rb-mirror-alert-status :mode="'list'"
                                                :status="scope.row.alert_level"></rb-mirror-alert-status>
                    </template>
                </el-table-column>
                <el-table-column prop="device_ip"
                                 label="设备IP"
                                 width="115px"></el-table-column>
                <el-table-column prop="idc_type"
                                 label="资源池"
                                 width="110px"
                                 :show-overflow-tooltip="true"> </el-table-column>
                <!--<el-table-column prop="pod_name"-->
                <!--label="pod池"-->
                <!--width="85px"-->
                <!--:show-overflow-tooltip="true"-->
                <!--sortable> </el-table-column>-->
                <el-table-column prop="biz_sys"
                                 label="业务线"
                                 width="150px"
                                 :show-overflow-tooltip="true"></el-table-column>
                <el-table-column prop="device_class"
                                 label="设备分类"
                                 width="100px"
                                 :show-overflow-tooltip="true"></el-table-column>
                <!--<el-table-column prop="device_type"-->
                <!--label="设备类型"-->
                <!--width="100px"-->
                <!--:show-overflow-tooltip="true"-->
                <!--sortable></el-table-column>-->
                <el-table-column prop="moni_index"
                                 label="告警内容"
                                 width="300px"
                                 :show-overflow-tooltip="true"></el-table-column>
                <el-table-column prop="alert_start_time"
                                 label="告警开始时间"
                                 width="150px"
                                 :show-overflow-tooltip="true"></el-table-column>
                <el-table-column prop="cur_moni_time"
                                 label="当前告警时间"
                                 width="150px"
                                 :show-overflow-tooltip="true"></el-table-column>
                <el-table-column prop="order_id"
                                 label="工单编号"
                                 width="150px"
                                 :show-overflow-tooltip="true"></el-table-column>
            </el-table>
        </div>
        <div class="yw-page-wrap">
            <el-pagination @size-change="handleSizeChange"
                           @current-change="handleCurrentChange"
                           :current-page="currentPage"
                           :page-sizes="pageSizes"
                           :page-size="pageSize"
                           :total="total"
                           layout="total, sizes, prev, pager, next, jumper">
            </el-pagination>
        </div>
    </div>

</template>

<script>
    import rbMirrorAlertStatus from 'src/pages/mirror_alert/common/rb-mirror-alert-status.vue'
    import rbAlertServiceFactory from 'src/services/alert/rb-alert-services.factory.js'
    import { formatDate } from 'src/assets/js/utility/rb-filters.js'

    export default {
        components: {
            rbMirrorAlertStatus
        },
        props: ['deviceInfo'],
        data() {
            return {
                activityAlertData: [], // 数据
                currentPage: 1, // 当前页
                pageSize: 50, // 分页每页多少行数据
                pageSizes: [20, 50, 100], // 每页多少行数组
                total: 0, // 总共多少行数据
                loading: false
                // queryParams: JSON.parse(this.$route.query.queryParams),
                // instanceId: '',
            }
        },
        mounted: function () {
            if (this.deviceInfo.id) {
                this.getCurrentAlert(this.deviceInfo.id)
            }
        },
        methods: {
            // 根据IP + 资源池 查询当前告警列表
            getCurrentAlert(id) {
                // var params = {
                //    'page_size': this.pageSize,
                //  'page_no': this.currentPage,
                // 'idcType': idcType,
                // 'deviceIp': ip
                // }
                this.loading = true
                var params = { // 动态列表接口
                    page_no: this.currentPage,
                    page_size: this.pageSize,
                    sort_name: '',
                    list: [
                        {
                            fieldName: 'device_id', // 条件名称
                            fieldType: 'and', // 类型
                            fieldValue: id
                        }
                    ]
                }
                rbAlertServiceFactory.getCurrentAlertByInstanceId(params).then((res) => {
                    this.activityAlertData = this.parseTableData(res.result)
                    this.total = res.count
                    this.loading = false
                })
            },
            parseTableData(result) {
                let list = []
                result.forEach((item) => {
                    item.alert_start_time = formatDate(item.alert_start_time)
                    item.cur_moni_time = formatDate(item.cur_moni_time)
                    list.push(item)
                })
                return list
            },
            handleSizeChange(val) {
                this.pageSize = val
                this.getCurrentAlert(this.queryParams.id)
            },
            // 分页改变当前页
            handleCurrentChange(val) {
                this.currentPage = val
                this.getCurrentAlert(this.queryParams.id)
            },
        }

    }
</script>

<style scoped>
</style>
