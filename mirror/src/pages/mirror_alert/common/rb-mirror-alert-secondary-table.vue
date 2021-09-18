<template>
    <div>
        <div>
            <el-row :gutter="20"
                    type="flex"
                    justify="space-between">
                <el-col :span="5">
                    <div><span>衍生告警记录数:{{total}}</span></div>
                </el-col>
                <el-col :span="15">
                    <div>
                        <span>告警时间范围:</span>
                        <span>
                            <!-- <el-date-picker v-model="cur_moni_time_from"
                                            type="date"
                                            placeholder="选择日期"
                                            value-format="yyyy-MM-dd"
                                            size="mini"
                                            style="width: 150px"></el-date-picker>
                        </span>
                        <span>至</span>
                        <span>
                            <el-date-picker v-model="cur_moni_time_to"
                                            type="date"
                                            placeholder="选择日期"
                                            value-format="yyyy-MM-dd"
                                            size="mini"
                                            style="width: 150px"></el-date-picker> -->
                            <el-date-picker v-model="cur_moni_time_from"
                                            type="datetimerange"
                                            range-separator="至"
                                            start-placeholder="开始时间"
                                            end-placeholder="结束时间"
                                            value-format="yyyy-MM-dd HH:mm:ss"></el-date-picker>
                        </span>
                    </div>
                </el-col>
                <el-col :span="4">
                    <div>
                        <el-button type="primary"
                                   size="mini"
                                   plain
                                   @click="search()">查询</el-button>
                        <el-button type="primary"
                                   size="mini"
                                   plain
                                   @click="reset()">重置</el-button>
                    </div>
                </el-col>
            </el-row>
        </div>
        <!-- table -->
        <div class="yw-el-table-wrap">
            <el-table class="yw-el-table"
                      :data="result"
                      height="calc(100vh - 310px)"
                      stripe
                      border>
                <el-table-column type="selection"
                                 width="40"></el-table-column>
                <el-table-column label="规则名称"
                                 width="160">
                    <template slot-scope="scope">
                        <a>{{scope.row.primary_secondary_name}}</a>
                    </template>
                </el-table-column>
                <el-table-column v-for="itemData in listShowList"
                                 :key="itemData.fieldCode"
                                 :label="itemData.listShowName"
                                 :width="((itemData.tableColumnWidth && itemData.tableColumnWidth > 0) ? itemData.tableColumnWidth : '180') + 'px'">
                    <template slot-scope="scope">
                        <label v-if="itemData.dataType === 'datetime'">
                            {{scope.row[itemData.fieldCode] | formatDate}}
                        </label>
                        <rb-mirror-alert-status v-else-if="itemData.fieldCode === 'alert_level'"
                                                :mode="'list'"
                                                :status="scope.row.alert_level">
                        </rb-mirror-alert-status>
                        <label v-else-if="itemData.fieldCode !== 'primary_secondary_alert_id'">
                            {{scope.row[itemData.fieldCode]}}
                        </label>
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
    </div>
</template>
<script>
    import { detail_his } from 'src/pages/mirror_alert/alert/config/options.js'
    import rbHttp from 'src/assets/js/utility/rb-http.factory'
    import rbAlertPrimarySecondaryServiceFactory from 'src/services/alert/rb-alert-primary-secondary-service.factory'
    export default {
        components: {
            YwPagination: () => import('src/components/common/yw-pagination.vue'),
            rbMirrorAlertStatus: () => import('src/pages/mirror_alert/common/rb-mirror-alert-status.vue')
        },
        props: [
            'alertId'
        ],
        data() {
            return {
                listShowList: [],
                alert_id: '',
                result: [],
                detail_his: [],
                currentPage: 1, // 当前页
                pageSize: 50, // 分页每页多少行数据
                pageSizes: [20, 50, 100], // 每页多少行数组
                total: 0, // 总共多少行数据
                cur_moni_time_from: []
            }
        },
        mounted: function () {
            this.start()
        },
        methods: {
            // 分页改变尺寸
            handleSizeChange(val) {
                this.pageSize = val
                this.init_()
            },
            // 分页改变当前页
            handleCurrentChange(val) {
                this.currentPage = val
                this.init_()
            },
            getRequest() {
                let req = {
                    'page_no': this.currentPage,
                    'page_size': this.pageSize,
                    'list': []
                }
                req.list.push({
                    'fieldName': 'primary_secondary_alert_id',
                    'fieldType': 'and',
                    'fieldValue': this.alertId
                })
                if (this.cur_moni_time_from && this.cur_moni_time_from.length > 0) {
                    req.list.push({
                        'fieldName': 'cur_moni_time',
                        'fieldType': 'datetime',
                        'fieldValue': this.cur_moni_time_from.join(',')
                    })
                }
                return req
            },
            init_() {
                if (this.alertId) {
                    let obj = this.getRequest()
                    rbAlertPrimarySecondaryServiceFactory.getprimarySecondaryAlertList(obj).then((res) => {
                        this.total = res.count
                        this.result = res.result
                    })
                }
            },
            queryModel() {
                rbHttp.sendRequest({
                    method: 'GET',
                    url: '/v2/alerts/model/field/listByTableName/alert_primary_secondary_alerts'
                }).then((res) => {
                    // 列表头部展示数据
                    let objList = res.filter((item) => {
                        return item.isListShow == 1
                    })
                    this.listShowList = objList
                })
            },
            start() {
                this.detail_his = detail_his
                this.queryModel()
                this.init_()
            },
            search() {
                this.init_()
            },
            reset() {
                this.cur_moni_time_from = []
            }
        },
        watch: {
            obj: {
                handler: function () {
                    this.start()
                }
            }
        }
    }
</script>
<style scoped lang="scss">
    /deep/ .el-table {
        overflow: auto;
        .el-table__body-wrapper {
            overflow: auto !important;
        }
    }
</style>
