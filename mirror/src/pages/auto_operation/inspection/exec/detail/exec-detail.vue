<template>
    <div class="clearfix">
        <table class="bordered detailTable">
            <tr>
                <td width="10%">任务名称</td>
                <td width="40%">{{reportDetail.task_name}}</td>
                <td width="10%">运行时间</td>
                <td width="40%">{{reportDetail.create_time}}</td>
            </tr>
            <tr>
                <td width="10%">设备数</td>
                <td width="40%">{{reportDetail.device_num}}</td>
                <td width="10%">巡检项</td>
                <td width="40%">{{reportDetail.item_num}}</td>
            </tr>
        </table>
        <el-form class="components-condition yw-form"
                 :inline="true"
                 label-width="100px">
            <el-form-item label="脚本运行日志">
                <!--<el-input v-model="log"-->
                <!--placeholder="请输入内容"></el-input>-->
                <YwInputAdd :keys="logs"
                            :activeKey="activeKey"
                            @changeKey="changeKey"></YwInputAdd>
            </el-form-item>
            <el-form-item label="设备IP">
                <el-input v-model="object_id"
                          placeholder="请输入内容"></el-input>
            </el-form-item>
            <el-form-item label="巡检项">
                <el-select v-model="item_id"
                           clearable>
                    <el-option v-for="item in itemSelect"
                               :key="item.item_id"
                               :label="item.item_name"
                               :value="item.item_id"></el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="执行状态"
                          v-model="status">
                <el-select v-model="status"
                           clearable>
                    <el-option v-for="item in taskStatus"
                               :key="item.value"
                               :label="item.label"
                               :value="item.value"></el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="运行状态"
                          label-width="145px"
                          v-model="status">
                <el-select v-model="exec_status"
                           clearable>
                    <el-option v-for="item in taskExecStatus"
                               :key="item.value"
                               :label="item.label"
                               :value="item.value"></el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="指标分组">
                <el-select v-model="item_group"
                           clearable>
                    <el-option v-for="item in itemGroupList"
                               :key="item.key"
                               :label="item.value"
                               :value="item.key">
                    </el-option>
                </el-select>
            </el-form-item>
            <!--<el-form-item label="运行状态" v-model="status">-->
            <!--<el-option v-for="item in taskStatus"-->
            <!--:key="item.value"-->
            <!--:label="item.label"-->
            <!--:value="item.value">-->
            <!--</el-option>-->
            <!--</el-form-item>-->
            <section class="btn-wrap">
                <el-button type="primary"
                           @click="search()">查询
                </el-button>
                <el-button @click="reset()">重置</el-button>
            </section>
        </el-form>
        <el-form class="yw-form">
            <div class="table-operate-wrap clearfix">
            </div>
            <div class="yw-el-table-wrap">
                <el-table :data="tableData"
                          class="yw-el-table"
                          stripe
                          tooltip-effect="dark"
                          border
                          :default-sort="{prop: 'finish_time', order: 'descending'}">
                    <el-table-column prop="idc_type"
                                     label="资源池"
                                     show-overflow-tooltip
                                     width="100px">
                    </el-table-column>
                    <el-table-column prop="ip"
                                     label="设备IP"
                                     show-overflow-tooltip
                                     width="100px">
                    </el-table-column>
                    <el-table-column prop="device_name"
                                     label="主机名"
                                     show-overflow-tooltip
                                     width="120px">
                    </el-table-column>
                    <el-table-column prop="item_group"
                                     label="指标分组"
                                     show-overflow-tooltip
                                     width="100px">
                    </el-table-column>
                    <el-table-column prop="name"
                                     label="巡检项"
                                     show-overflow-tooltip
                                     width="100px">
                    </el-table-column>
                    <el-table-column prop="result_name"
                                     label="子项"
                                     show-overflow-tooltip
                                     align="center"
                                     width="100px">
                    </el-table-column>
                    <el-table-column prop="exec_status_desc"
                                     show-overflow-tooltip
                                     label="运行状态">
                    </el-table-column>
                    <el-table-column prop="status_desc"
                                     show-overflow-tooltip
                                     label="执行状态">
                    </el-table-column>
                    <el-table-column prop="value"
                                     show-overflow-tooltip
                                     label="执行结果">
                    </el-table-column>
                    <!--  <el-table-column prop="result_desc"
                                 label="结果描述" width="120px">
                    </el-table-column> -->
                    <el-table-column prop="log"
                                     label="运行日志"
                                     show-overflow-tooltip
                                     width="320px">
                        <template slot-scope="scope">
                            <!--<el-tooltip placement="top">-->
                            <!--<div slot="content">{{ scope.row.log }}</div>-->
                            <!--<span class="text-ellipse"-->
                            <!--:title="scope.row.log">{{ scope.row.log }}</span>-->
                            <!--</el-tooltip>-->
                            {{scope.row.log.length > 100 ? scope.row.log.substring(1, 100) + '...' : scope.row.log}}
                        </template>
                    </el-table-column>
                    <el-table-column label="操作"
                                     width="140">
                        <template slot-scope="scope">
                            <el-button type="text"
                                       @click="getLogDetails(scope.row)"
                                       title="日志详情"
                                       icon="el-icon-tickets"></el-button>
                        </template>
                    </el-table-column>

                </el-table>
            </div>
            <div class="yw-page-wrap">
                <el-pagination @size-change="handleSizeChange"
                               @current-change="handleCurrentChange"
                               :current-page="currentPage"
                               :page-sizes="pageSizes"
                               :page-size="pageSize"
                               layout="total, sizes, prev, pager, next, jumper"
                               :total="total">
                </el-pagination>
            </div>
        </el-form>
        <el-dialog append-to-body
                   class="yw-dialog"
                   title="日志详情"
                   :visible.sync="logDialogVisible"
                   :close-on-click-modal="false">
            <section class="yw-dialog-main">
                <div class="yw-content space-pre"
                     v-html="logmessage"></div>
            </section>
        </el-dialog>
    </div>

</template>
<script>
    import { taskStatus, taskExecStatus } from '../config/options.js'
    import rbProjectDataServiceFactory from 'src/services/template/rb-template-services.factory.js'
    import { formatDate } from 'src/assets/js/utility/rb-filters.js'
    import QueryObject from 'src/utils/queryObject.js'
    import _ from 'lodash'
    export default {
        props: ['reportId', 'taskId'],
        mixins: [QueryObject],
        data() {
            return {
                itemGroupList: [],
                item_group: '',
                activeKey: '',
                logmessage: '',
                logDialogVisible: false,
                // 当前页
                currentPage: 1,
                // 分页每页多少行数据
                pageSize: 10,
                total: 0,
                // 每页多少行数组
                pageSizes: [10, 20, 50, 100],
                logs: [],
                id: this.reportId,
                object_id: null,
                item_id: null,
                status: null,
                exec_status: null,
                reportDetail: {},
                itemSelect: [],
                taskStatus: taskStatus,
                taskExecStatus: taskExecStatus,
                tableData: []
            }
        },
        components: {
            YwInputAdd: () => import('src/components/common/yw-input-add.vue'),
        },
        watch: {
            reportId() {
                this.id = this.reportId
                this.init()
            }
        },
        mounted() {
            let itemGroup = this.$store.state.homeStore.dictObj.item_group
            if (itemGroup) {
                Object.keys(itemGroup).forEach(item => {
                    this.itemGroupList.push({ 'key': item, 'value': itemGroup[item].name })
                })
            }
            this.init()
        },
        methods: {
            // 分页改变尺寸
            handleSizeChange(val) {
                this.pageSize = val
                this.search(1)
            },
            // 分页改变当前页
            handleCurrentChange(val) {
                this.currentPage = val
                this.search(1)
            },
            changeKey(val) {
                this.logs = val
            },
            getLogDetails(row) {
                this.logmessage = row.log
                this.logDialogVisible = true
            },
            init() {
                // this.itemGroupList.push({'key': '', 'value': '全部'})
                this.tableData = []
                this.itemSelect = []
                this.reset()
                // 获取报告基本详情
                this.getReportDetail()
                // 获取巡检项
                this.getItemSelect()
                this.search()
            },
            getReportDetail() {
                rbProjectDataServiceFactory.getReportBaseInfo(this.id).then(res => {
                    this.reportDetail.task_name = res.task_name
                    this.reportDetail.task_id = res.task_id
                    this.reportDetail.device_num = res.device_num
                    this.reportDetail.create_time = formatDate(res.create_time)
                    this.reportDetail.item_num = res.item_num
                })
            },
            getItemSelect() {
                rbProjectDataServiceFactory.getItemSelect(this.taskId).then(res => {
                    this.itemSelect = res
                })
            },
            search(num) {
                if (num !== 1) {
                    // 搜索前将当前页置为1
                    this.currentPage = 1
                }
                let obj = {
                    logs: this.logs,
                    object_id: this.object_id,
                    item_id: this.item_id,
                    status: this.status,
                    exec_status: this.exec_status,
                    item_group: this.item_group,
                    report_id: this.reportId,
                    page_no: this.currentPage,
                    page_size: this.pageSize
                }
                rbProjectDataServiceFactory.reportItemList(obj).then(res => {
                    this.tableData = this.parseData(res.result)
                    this.tableData.forEach(item => {
                        if (item.result_desc && item.result_desc != '') {
                            item.value = item.result_desc
                        }
                        this.itemGroupList.forEach(group => {
                            if (group.key == item.report_item_ext.itemGroup) {
                                item.item_group = group.value
                            }

                        })
                    })
                    this.total = res.count
                })
            },
            reset() {
                this.log = null
                this.object_id = null
                this.item_id = null
                this.status = null
                this.exec_status = null
            },
            parseData(data) {
                data.forEach(item => {
                    if (item.exec_status) {
                        item.exec_status_desc = item.exec_status == '0' ? '异常' : '正常'
                    }
                    if (item.status) {
                        item.status_desc = _.filter(taskStatus, o => {
                            return o.value == item.status
                        })[0].label
                    }
                })
                return data
            }
        }
    }
</script>

<style lang="scss" scoped>
    .yw-el-table-wrap {
        height: 300px;
        overflow-y: auto;
    }
    .detailTable {
        *border-collapse: collapse; /* IE7 and lower */
        border-spacing: 0;
        width: 100%;
        margin-bottom: 10px;
        background: #fff;
    }

    .bordered {
        border: solid $color-border 1px;
        -moz-border-radius: 2px;
        -webkit-border-radius: 2px;
        border-radius: 2px;
    }

    .bordered tr:hover {
        background: $color-bg;
        -o-transition: all 0.1s ease-in-out;
        -webkit-transition: all 0.1s ease-in-out;
        -moz-transition: all 0.1s ease-in-out;
        -ms-transition: all 0.1s ease-in-out;
        transition: all 0.1s ease-in-out;
    }

    .bordered td,
    .bordered th {
        border-left: 1px solid $color-border;
        border-top: 1px solid $color-border;
        padding: 10px;
        text-align: left;
    }

    .bordered tr:first-child td {
        border-top: 0;
    }

    el-form {
        border: solid $color-border 1px;
    }

    .yw-content {
        /*min-height: 200px;*/
        /*max-height: 400px;*/
        overflow-y: auto;
    }
</style>