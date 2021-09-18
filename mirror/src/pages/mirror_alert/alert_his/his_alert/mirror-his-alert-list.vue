<template>
    <div class="components-container">
        <div class="HISUP">
            <AlertList @queryDatasHis="formSendHis"
                       :sendTotalHis="total"
                       :sendPageSizeHis="pageSize"
                       :sendcurrentPagelHis="currentPage" />
        </div>

        <el-form class="yw-form alertListTable">
            <div class="table-operate-wrap clearfix">
                <el-button class="btn-icons-wrap"
                           @click="resourceExport">导出</el-button>
                <section class="fr">
                    <el-button class="btn-icons-wrap"
                               v-show="lockScgreen"
                               @click="lock">锁屏</el-button>
                    <el-button class="btn-icons-wrap"
                               v-show="goScgreen"
                               @click="go">刷屏</el-button>
                    <el-button class="btn-icons-wrap"
                               v-popover:popover>
                        选择列&nbsp;&nbsp;&nbsp;
                        <i class="el-icon-arrow-down"></i>
                    </el-button>
                    <el-popover ref="popover"
                                placement="top"
                                trigger="click">
                        <div id="popover"
                             style="max-height: 300px;overflow-y: auto;">
                            <el-checkbox-group v-model="test"
                                               @change="handleCheckedColumnChange">
                                <div v-for="(column,i) in checkColumns"
                                     :key="i">
                                    <el-checkbox :label="column.keyCode">{{ column.keyName }}</el-checkbox>
                                </div>
                            </el-checkbox-group>
                        </div>
                    </el-popover>
                </section>
            </div>
            <div class="yw-el-table-wrap ywTabBorder">
                <el-table class="yw-el-table"
                          height="calc(100vh - 400px)"
                          border
                          :data="activityAlertData"
                          style="cursor: pointer;"
                          stripe
                          tooltip-effect="dark"
                          @selection-change="handleSelectionChange"
                          v-loading="loading"
                          :header-cell-style="{background:'#DEE9FC',color:'#53607E'}"
                          @row-dblclick="dblhandleCurrentChange($event)">
                    <el-table-column type="selection"></el-table-column>
                    <el-table-column label="级别"
                                     width="75px"
                                     fixed
                                     :sortable="true"
                                     :sort-method="sortByAlertLevel">
                        <template slot-scope="scope">
                            <rb-mirror-alert-status :mode="'list'"
                                                    :status="scope.row.alert_level"></rb-mirror-alert-status>
                        </template>
                    </el-table-column>
                    <el-table-column prop="device_ip"
                                     label="设备IP"
                                     fixed
                                     width="115px"
                                     sortable></el-table-column>
                    <el-table-column prop="idc_type"
                                     label="资源池"
                                     width="110px"
                                     :show-overflow-tooltip="true"
                                     sortable></el-table-column>
                    <el-table-column prop="pod_name"
                                     label="pod池"
                                     width="85px"
                                     :show-overflow-tooltip="true"
                                     sortable></el-table-column>
                    <el-table-column prop="biz_sys"
                                     label="业务线"
                                     width="150px"
                                     :show-overflow-tooltip="true"
                                     sortable></el-table-column>
                    <el-table-column prop="device_class"
                                     label="设备分类"
                                     width="100px"
                                     :show-overflow-tooltip="true"
                                     sortable></el-table-column>
                    <el-table-column prop="device_type"
                                     label="设备类型"
                                     width="100px"
                                     :show-overflow-tooltip="true"
                                     sortable></el-table-column>
                    <el-table-column prop="moni_index"
                                     label="告警内容"
                                     width="300px"
                                     :show-overflow-tooltip="true"
                                     sortable></el-table-column>
                    <el-table-column prop="alert_start_time"
                                     label="告警开始时间"
                                     width="150px"
                                     sortable
                                     :show-overflow-tooltip="true"></el-table-column>
                    <el-table-column prop="clear_time"
                                     label="清除时间"
                                     width="150px"
                                     :show-overflow-tooltip="true"
                                     sortable></el-table-column>
                    <template v-if="itemData.keyIsSelected">
                        <el-table-column v-for="itemData in checkColumns"
                                         :key="itemData.keyName"
                                         :label="itemData.keyName"
                                         width="180px"
                                         sortable
                                         :show-overflow-tooltip="true"
                                         :widt="flexColumnWidth(itemData.keyCode)">
                            <!-- :width="flexColumnWidth(itemData.keyCode)" -->
                            <template slot-scope="scope">
                                <el-button v-if="itemData.keyCode === 'order_id'"
                                           @click="gotoBPM(scope.row.order_id)"
                                           type="text"
                                           size="small">{{scope.row[itemData.keyCode]}}</el-button>
                                <span v-else>{{scope.row[itemData.keyCode]}}</span>
                            </template>
                        </el-table-column>

                    </template>
                </el-table>
            </div>
            <div class="yw-page-wrap">
                <el-pagination @size-change="handleSizeChange"
                               @current-change="handleCurrentChange"
                               :current-page="currentPage"
                               :page-sizes="pageSizes"
                               :page-size="pageSize"
                               :total="total"
                               layout="total, sizes, prev, pager, next, jumper"></el-pagination>
            </div>
        </el-form>
    </div>
</template>

<script>
    import rbMirrorCommonService from 'src/services/mirror/rb-mirror-common-services.factory.js'
    import rbAlertKanBanServiceFactory from 'src/services/alert/rb-alert-kanban-services.factory.js'
    import { formatDate } from 'src/assets/js/utility/rb-filters.js'
    import rbMirrorAlertStatus from 'src/pages/mirror_alert/common/rb-mirror-alert-status.vue'
    import rbCmdbServiceFactory from 'src/services/cmdb/rb-cmdb-service.factory.js'
    import rbAlertServicesFactory from 'src/services/alert/rb-alert-services.factory.js'
    import Treeselect from '@riophae/vue-treeselect'
    import '@riophae/vue-treeselect/dist/vue-treeselect.css'
    import AlertList from '../../alert/list/alert-list.vue'

    export default {
        name: 'MirrorAlertAlertHisHisAlert',
        components: {
            rbMirrorAlertStatus,
            Treeselect,
            AlertList
        },
        data() {
            return {
                queryDataParameterHis: {},
                loading: true,
                bizSysTree: null,
                bizSysDepChecked: [],
                bizDepTreeSelOpts: [],
                test: [],
                checkColumns: [
                    {
                        keyCode: 'alert_end_time',
                        keyName: '自动解除时间',
                        keyIsSelected: false
                    },
                    { keyCode: 'source_room', keyName: '机房位置', keyIsSelected: true },
                    { keyCode: 'device_mfrs', keyName: '设备厂商', keyIsSelected: false },
                    { keyCode: 'device_model', keyName: '设备型号', keyIsSelected: false },
                    { keyCode: 'host_name', keyName: '主机名称', keyIsSelected: false },
                    { keyCode: 'moni_object', keyName: '监控对象', keyIsSelected: false },
                    { keyCode: 'object_type', keyName: '告警类型', keyIsSelected: false },
                    { keyCode: 'alert_count', keyName: '告警数量', keyIsSelected: false },
                    { keyCode: 'report_type', keyName: '通知方式', keyIsSelected: false },
                    { keyCode: 'report_status', keyName: '通知状态', keyIsSelected: false },
                    { keyCode: 'report_time', keyName: '通知时间', keyIsSelected: false },
                    { keyCode: 'trans_status', keyName: '转派状态', keyIsSelected: false },
                    { keyCode: 'trans_user', keyName: '转派人', keyIsSelected: false },
                    { keyCode: 'trans_time', keyName: '转派时间', keyIsSelected: false },
                    {
                        keyCode: 'to_confirm_user',
                        keyName: '待确认人',
                        keyIsSelected: false
                    },
                    { keyCode: 'order_type', keyName: '派单类型', keyIsSelected: false },
                    { keyCode: 'order_status', keyName: '派单状态', keyIsSelected: false },
                    { keyCode: 'order_id', keyName: '工单号', keyIsSelected: false },
                    { keyCode: 'deliver_time', keyName: '派单时间', keyIsSelected: false },
                    { keyCode: 'confirmed_user', keyName: '确认人', keyIsSelected: false },
                    {
                        keyCode: 'confirmed_time',
                        keyName: '确认时间',
                        keyIsSelected: false
                    },
                    {
                        keyCode: 'confirmed_content',
                        keyName: '确认内容',
                        keyIsSelected: false
                    },
                    { keyCode: 'clear_user', keyName: '清除人', keyIsSelected: false },
                    { keyCode: 'clear_content', keyName: '清除描述', keyIsSelected: false }
                ],
                columnFilter: {
                    alert_end_time: false,
                    source_room: false,
                    device_mfrs: false,
                    device_model: false,
                    host_name: false,
                    biz_sys: false,
                    moni_object: false,
                    object_type: false,
                    alert_count: false,
                    report_type: false,
                    report_status: false,
                    report_time: false,
                    trans_status: false,
                    trans_user: false,
                    trans_time: false,
                    to_confirm_user: false,
                    order_type: false,
                    order_status: false,
                    order_id: false,
                    deliver_time: false,
                    confirmed_user: false,
                    confirmed_time: false,
                    confirmed_content: false,
                    clear_user: false,
                    clear_content: false
                },
                classA: 'classA',
                classB: 'classB',
                alertList: [],
                // 选择列存放的值
                filterForm: {},
                columnInfo: '',
                checkedcolumns: [],
                moduleId: '12345678XYUEFKSLDDLDAL',
                column_data: {},
                columnList: [],
                detailObjId: '',
                detailOrderStatus: '',
                //
                alertObj: {},
                alertObjFlag: false,
                lockScgreen: true,
                goScgreen: false,
                multipleSelection: [], // 多选框模板存放的值
                activityAlertData: [], // 活动告警数据数组
                currentPage: 1, // 当前页
                pageSize: 50, // 分页每页多少行数据
                pageSizes: [20, 50, 100], // 每页多少行数组
                total: 0, // 总共多少行数据
                relation: [],
                timer_frequency: 60000,
                exportDisabled: false
            }
        },
        mounted() {
            this.getColumnFilter()
        },
        destroyed() {
            clearInterval(this.timer)
            this.timer = null
        },
        methods: {
            // 条件筛选过来的参数
            formSendHis(datas) {
                datas.queryType = '3'
                // datas.page_no = 1
                // datas.page_size = 50

                this.queryDataParameterHis = datas
                this.getTableData(datas)
            },
            // 锁屏
            lock() {
                clearInterval(this.timer)
                this.timer = null
                this.goScgreen = true
                this.lockScgreen = false
            },
            // 刷屏
            go() {
                if (this.timer) {
                    clearInterval(this.timer)
                } else {
                    this.timer = setInterval(() => {
                        this.getTableData(this.queryDataParameterHis)
                    }, this.timer_frequency)
                }
                this.goScgreen = false
                this.lockScgreen = true
            },
            // 1 element相关方法 多选框
            handleSelectionChange(val) {
                this.multipleSelection = val
            },
            // 分页改变尺寸
            handleSizeChange(val) {
                this.pageSize = val
                // this.$emit("sendPageSizeHis", this.pageSize)
                this.queryDataParameterHis.page_size = val
                this.getTableData(this.queryDataParameterHis)
            },
            // 分页改变当前页
            handleCurrentChange(val) {
                this.currentPage = val
                // this.$emit("sendcurrentPagelHis", this.currentPage)
                this.queryDataParameterHis.page_no = val
                this.getTableData(this.queryDataParameterHis)
            },
            // 获取列表数据相关方法
            getTableData(obj) {
                rbAlertKanBanServiceFactory.getAlertList(obj).then(res => {
                    // this.activityAlertData = res.result;
                    this.activityAlertData = this.packData(res.result)
                    this.total = res.count
                    this.$emit('sendTotalHis', this.total)
                })
                    .finally(() => {
                        this.loading = false
                    })
            },
            // 封装得到的数据
            packData(arr) {
                // 列表数据封装
                if (arr.forEach) {
                    arr.forEach((item) => {
                        //            item.order_type = item.order_status === '1' ? '' : '告警工单'
                        item.order_status = rbMirrorCommonService.common('ORDERSTATUS', '1', item.order_status)
                        item.object_type = rbMirrorCommonService.common('OBJECTTYPE', '1', item.object_type)
                        item.report_status = rbMirrorCommonService.common('REPORTSTATUS', '1', item.report_status)
                        item.trans_status = rbMirrorCommonService.common('TRANSSTATUS', '1', item.trans_status)
                        item.order_type = rbMirrorCommonService.common('ORDERTYPE', '1', item.order_type)
                        item.report_type = rbMirrorCommonService.common('REPORTTYPE', '1', item.report_type)
                        item.cur_moni_time = formatDate(item.cur_moni_time)
                        item.alert_start_time = formatDate(item.alert_start_time)
                        item.alert_end_time = formatDate(item.alert_end_time)
                        item.deliver_time = formatDate(item.deliver_time)
                        item.clear_time = formatDate(item.clear_time)
                        item.confirmed_time = formatDate(item.confirmed_time)
                        item.report_time = formatDate(item.report_time)
                        item.trans_time = formatDate(item.trans_time)
                    })
                    return arr
                } else {
                    // 详情数据封装
                    //          arr.order_type = arr.order_status === '1' ? '' : '告警工单'
                    arr.order_status = rbMirrorCommonService.common('ORDERSTATUS', '1', arr.order_status)
                    arr.object_type = rbMirrorCommonService.common('OBJECTTYPE', '1', arr.object_type)
                    arr.report_status = rbMirrorCommonService.common('REPORTSTATUS', '1', arr.report_status)
                    arr.trans_status = rbMirrorCommonService.common('TRANSSTATUS', '1', arr.trans_status)
                    arr.order_type = rbMirrorCommonService.common('ORDERTYPE', '1', arr.order_type)
                    arr.report_type = rbMirrorCommonService.common('REPORTTYPE', '1', arr.report_type)
                    arr.cur_moni_time = formatDate(arr.cur_moni_time)
                    arr.alert_start_time = formatDate(arr.alert_start_time)
                    arr.deliver_time = formatDate(arr.deliver_time)
                    arr.clear_time = formatDate(arr.clear_time)
                    arr.confirmed_time = formatDate(arr.confirmed_time)
                    arr.report_time = formatDate(arr.report_time)
                    arr.trans_time = formatDate(arr.trans_time)
                    return arr
                }
            },
            // 导出
            resourceExport() {
                this.loading = true
                this.exportDisabled = true
                const ExportQuery = this.queryDataParameterHis
                console.log(ExportQuery)

                rbAlertKanBanServiceFactory
                    .ExportGridData1(ExportQuery)
                    .then(res => {
                        if (res.code === '0000') {
                            let downLoadElement = document.createElement('a')
                            downLoadElement.href = res.path
                            // downLoadElement.download = '历史告警数据列表.xlsx'
                            downLoadElement.setAttribute('download', '历史告警数据列表.xlsx')
                            document.body.appendChild(downLoadElement)
                            downLoadElement.click()
                            document.body.removeChild(downLoadElement)
                        } else {
                            this.$message.error(res.message)
                        }
                    })
                    .finally(() => {
                        this.loading = false
                        this.exportDisabled = false
                    })
            },

            // 选择列

            // 1.获取选择列表的初始值
            getColumnFilter() {
                rbCmdbServiceFactory
                    .getColumnFilter({ menuType: 'alert', moduleId: this.moduleId })
                    .then(data => {
                        this.filterForm = data
                        if (data.columnInfo === null) {
                            this.checkColumns.forEach(item => {
                                this.columnFilter[item.keyCode] = false
                            })
                        } else {
                            this.columnFilter = JSON.parse(data.columnInfo)
                        }
                        this.queryFields = []
                        this.test = []
                        this.checkColumns.forEach(item => {
                            if (this.columnFilter[item.keyCode]) {
                                item.keyIsSelected = true
                                this.test.push(item.keyCode)
                                this.queryFields.push(item)
                            } else {
                                item.keyIsSelected = false
                            }
                        })
                    })
            },
            // 2.更新选择的变化
            handleCheckedColumnChange(val) {
                this.checkColumns.forEach(item => {
                    this.columnFilter[item.keyCode] = false
                })
                val.forEach(item => {
                    this.columnFilter[item] = true
                })
                let data = this.filterForm
                data.columnInfo = JSON.stringify(this.columnFilter)
                data.columnMap = this.columnFilter
                rbCmdbServiceFactory.updateColumnFilter(data).then(() => {
                    this.getColumnFilter()
                    this.getTableData(this.queryDataParameterHis)
                })
            },
            // 自定义表头列宽
            flexColumnWidth(str) {
                let flexWidth = 120
                if (str === 'moni_index') {
                    flexWidth += 130
                } else if (str === 'alert_start_time') {
                    flexWidth += 35
                } else if (str === 'alert_end_time') {
                    flexWidth += 35
                }
                return flexWidth + 'px'
            },
            getMonitorValue() {
                rbAlertServicesFactory.getHisMonitorValue().then(res => {
                    for (let i = 0; i < res.length; i++) {
                        this.monitor_values.push(res[i])
                    }
                })
            },
            sortByAlertLevel(obj1, obj2) {
                let val1 = obj1.alert_level
                let val2 = obj2.alert_level
                return val1 - val2
            },
            dblhandleCurrentChange(column) {
                this.$router.push({
                    path: '/mirror_alert/alert_his/his_detail',
                    query: {
                        alertId: column.alert_id,
                        detailType: 'alertHis'
                    }
                })
            },
            gotoBPM: function (bpm_id) {
                const url = `${sessionStorage.getItem(
                    'X7_SERVER_URL'
                )}/front/#/inst/${bpm_id}?mirrorToken=${sessionStorage.getItem('mirror')}`
                window.open(url, '_blank')
            },
        },
        watch: {
            // formSendHis: {
            //     handler(val) {
            //         console.log('历史告警查询条件', val.queryType)
            //         // val.queryType = '3'
            //         this.queryDataParameterHis = val
            //     },
            //     immediate: true, // 初始化传值
            //     deep: true // 深度监听
            // },
        }
    }
</script>

<style lang="scss" scoped>
    @import "../../../mirror_alert/alert/list/alert.scss";
    .components-condition {
        padding-right: 280px;
    }

    .yw-form .el-form-item__label {
        width: auto;
    }
</style>
<style lang="stylus">
    .ywTabBorder .el-table__body-wrapper {
        height: calc(100% - 40px) !important;
    }

    button.el-button.el-button--default:hover {
        border: 1px solid #2089DA;
    }

    button.el-button.el-button--default {
        border: 1px solid #2089DA;
        padding: 5px 15px;
    }

    .HISUP .alertListTable, .HISUP .FILE {
        display: none;
    }

    .HISUP .BOTTONBIZ {
        right: 27px !important;
    }

    .HISUP .HISquery {
        display: block !important;
    }

    .ywTabBorder .el-table__body-wrapper {
        height: calc(100% - 29px) !important;
    }

    .ywTabBorder .el-table__fixed-body-wrapper {
        top: 29px !important;
        height: calc(100% - 29px) !important;
    }
</style>
