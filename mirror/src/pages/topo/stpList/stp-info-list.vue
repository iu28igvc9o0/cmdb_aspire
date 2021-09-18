<template>
    <div class="resourceManagement">
        <div class="managementRight"
             v-loading="loading"
             :element-loading-text="loading_text">
            <div class="form">
                <div class="tableBotton">
                    <div class="left">
                        <span style="font-size: 13px;color: black;margin-right: 20px;">设备IP：{{deviceInfo.ip}}</span>
                        <span style="font-size: 13px;color: black;margin-right: 20px;">资源池：{{deviceInfo.idcType}}</span>
                        <span style="font-size: 13px;color: black;margin-right: 20px;">POD名称：{{deviceInfo.podName}}</span>
                        <span style="font-size: 13px;color: black;margin-right: 20px;">机房：{{deviceInfo.roomId}}</span>
                        <span style="font-size: 13px;color: black;">机柜：{{deviceInfo.idcCabinet}}</span>
                    </div>
                    <div class="right">
                        <span style="font-size: 13px;"><i class="el-icon-info"></i>端口名称排序</span>
                    </div>
                </div>
                <el-row style="padding: 0px 10px 10px 10px;">
                    <el-col :span="12"
                            style="border:1px solid #92b4e9;  min-height: 40px;">
                        <div style="text-align:center; line-height:  40px"><span style="margin-right: 20px;">最新stp信息</span><span>采集时间：{{deviceStpInfo.indexDate}}</span></div>
                    </el-col>
                    <el-col :span="12"
                            style="border:1px solid #92b4e9;min-height: 40px;">
                        <div style="text-align:center;line-height:  40px "><span style="margin-right: 20px;">历史stp信息（最近7天）</span>
                            <span>
                                <el-select v-model="timeVal"
                                           placeholder="请选择"
                                           @change="changeQuery">
                                    <el-option v-for="item in timeList"
                                               :key="item.value"
                                               :label="item.label"
                                               :value="item.value">
                                    </el-option>
                                </el-select>
                            </span><span style="padding-left: 20px;">采集时间：{{historyDeviceStpInfo.indexDate}}</span></div>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="12">
                        <div class="yw-el-table-wrap table">
                            <div style="border:1px solid #DEE9FC">
                                <el-table stripe
                                          class="yw-el-table"
                                          :data="tableList"
                                          style="width: 100%"
                                          height="calc(100vh - 260px)">
                                    <el-table-column prop="ifDescr"
                                                     label="端口名称"
                                                     :min-width="'120'"
                                                     sortable
                                                     show-overflow-tooltip></el-table-column>
                                    <el-table-column prop="stpPortState"
                                                     label="当前状态"
                                                     :min-width="'120'"
                                                     sortable
                                                     show-overflow-tooltip></el-table-column>
                                    <el-table-column prop="stpPortDesignatedRoot"
                                                     label="根网桥"
                                                     :min-width="'120'"
                                                     sortable
                                                     show-overflow-tooltip></el-table-column>
                                    <el-table-column prop="stpPortDesignatedBridge"
                                                     label="指定网桥"
                                                     :min-width="'120'"
                                                     sortable
                                                     show-overflow-tooltip></el-table-column>
                                    <el-table-column prop="stpPortDesignatedPort"
                                                     label="指定端口"
                                                     :min-width="'120'"
                                                     sortable
                                                     show-overflow-tooltip></el-table-column>
                                    <el-table-column prop="stpPortForwardTransitions"
                                                     label="状态转变次数"
                                                     :min-width="'120'"
                                                     sortable
                                                     show-overflow-tooltip></el-table-column>
                                    <el-table-column prop="stpPortPathCost"
                                                     label="路径开销"
                                                     :min-width="'120'"
                                                     sortable
                                                     show-overflow-tooltip></el-table-column>
                                </el-table>
                            </div>
                        </div>
                    </el-col>
                    <el-col :span="12">
                        <div class="yw-el-table-wrap table">
                            <div style="border:1px solid #DEE9FC">
                                <el-table stripe
                                          class="yw-el-table"
                                          :data="historyTableList"
                                          style="width: 100%"
                                          height="calc(100vh - 260px)">
                                    <el-table-column prop="ifDescr"
                                                     label="端口名称"
                                                     :min-width="'120'"
                                                     sortable
                                                     show-overflow-tooltip></el-table-column>
                                    <el-table-column prop="stpPortState"
                                                     label="当前状态"
                                                     :min-width="'120'"
                                                     sortable
                                                     show-overflow-tooltip></el-table-column>
                                    <el-table-column prop="stpPortDesignatedRoot"
                                                     label="根网桥"
                                                     :min-width="'120'"
                                                     sortable
                                                     show-overflow-tooltip></el-table-column>
                                    <el-table-column prop="stpPortDesignatedBridge"
                                                     label="指定网桥"
                                                     :min-width="'120'"
                                                     sortable
                                                     show-overflow-tooltip></el-table-column>
                                    <el-table-column prop="stpPortDesignatedPort"
                                                     label="指定端口"
                                                     :min-width="'120'"
                                                     sortable
                                                     show-overflow-tooltip></el-table-column>
                                    <el-table-column prop="stpPortForwardTransitions"
                                                     label="状态转变次数"
                                                     :min-width="'120'"
                                                     sortable
                                                     show-overflow-tooltip></el-table-column>
                                    <el-table-column prop="stpPortPathCost"
                                                     label="路径开销"
                                                     :min-width="'120'"
                                                     sortable
                                                     show-overflow-tooltip></el-table-column>
                                </el-table>
                            </div>
                        </div>
                    </el-col>
                </el-row>
            </div>
        </div>
    </div>
</template>
<script>
    import moment from 'moment'
    import rbCmdbServiceFactory from 'src/services/cmdb/rb-cmdb-service.factory.js'
    export default {
        props: [],
        data() {
            return {
                deviceInfo: {},
                deviceStpInfo: {},
                historyDeviceStpInfo: {},
                historyTableList: [],
                timeVal: '',
                timeList: [],
                loading: false,
                paginationData: {
                    currentPage: 1,
                    total: 0,
                    pageSize: 50,
                    selectPageSizes: [30, 50, 100],
                    sort: 'insert_time',
                    order: 'desc'
                }
            }
        },
        watch: {
        },
        created() {
        },
        mounted: function () {
            this.initQuery()
        },
        methods: {
            // 1 element相关方法 多选框
            handleSelectionChange(val) {
                this.multipleSelection = val
            },
            // 分页改变尺寸
            handleSizeChange(val) {
                this.paginationData.pageSize = val
                this.queryData()
            },
            // 分页改变当前页
            handleCurrentChange(val) {
                this.paginationData.currentPage = val
                this.queryData()
            },

            initQuery() {
                // this.paginationData.currentPage = 1
                this.getDeviceInfo()
                this.queryData()
            },
            getDeviceInfo() {
                // alert(JSON.stringify(this.$route.query))
                let queryParams = {
                    'idcType': this.$route.query.idcType,
                    'ip': this.$route.query.deviceIp
                }
                rbCmdbServiceFactory.queryDeviceByRoomAndIP(queryParams).then(data => {
                    this.deviceInfo = data
                })
            },
            queryData() {
                this.loading = true
                this.rbHttp
                    .sendRequest({
                        method: 'GET',
                        url: '/v1/alerts/network/getStpInfoList',
                        params: {
                            ip: this.$route.query.deviceIp,
                            // ip:'10.255.244.2',
                            // idcType:'业支域非池化'
                            idcType: this.$route.query.idcType
                        }
                    })
                    .then(res => {
                        if (res) {
                            this.deviceStpInfo = res
                            this.deviceStpInfo.indexDate = moment(res.indexDate).format('YYYY-MM-DD HH:mm:ss')
                            this.getHistoryDate(res.indexDate)
                            this.tableList = res.stpDataList
                            this.sortTable(this.tableList)
                        } else {
                            this.tableList = []
                        }

                    }).finally(() => {
                        this.loading = false
                    })
            },
            getHistoryDate(timeVal) {
                // timeVal = moment(timeVal).format('YYYY-MM-DD')
                let now = new Date(moment(timeVal).valueOf())
                this.timeList = []
                for (let i = 1; i < 8; i++) {
                    let val = {
                        label: moment(new Date(now.getTime() - 24 * 60 * 60 * 1000 * i)).format('YYYY-MM-DD'),
                        value: moment(new Date(now.getTime() - 24 * 60 * 60 * 1000 * i)).format('YYYYMMDD')
                    }
                    this.timeList.push(val)
                }
            },
            queryHisData() {
                this.loading = true
                this.rbHttp
                    .sendRequest({
                        method: 'GET',
                        url: '/v1/alerts/network/getStpInfoList',
                        params: {
                            ip: this.$route.query.deviceIp,
                            idcType: this.$route.query.idcType,
                            indexDate: this.timeVal
                        }
                    })
                    .then(res => {
                        if (res) {
                            this.historyDeviceStpInfo = res
                            this.historyDeviceStpInfo.indexDate = moment(res.indexDate).format('YYYY-MM-DD HH:mm:ss')
                            this.historyTableList = res.stpDataList
                            this.sortTable(this.historyTableList)
                        } else {
                            this.historyTableList = []
                        }

                    }).finally(() => {
                        this.loading = false
                    })
            },
            sortTable(tableList) {
                tableList.sort(function (a, b) {
                    let aa = a.stpPort.split('_')[1]
                    let bb = b.stpPort.split('_')[1]
                    return aa - bb                })
            },
            // 下拉框change事件触发 查询CI列表
            changeQuery() {
                this.queryHisData()
            }

        }
    }
</script>
<style lang="scss" scoped>
    .resourceManagement {
        height: 100%;
        width: 100%;
        .managementRight {
            float: left;
            width: 100%;
            height: 100%;
            .condition {
                background: #fff;
                min-width: 986px;
                padding: 10px;
                .content {
                    width: 100%;
                    height: 100%;
                    border: 1px solid #dee9fc;
                    .center {
                        height: 38px;
                        border-bottom: 1px solid #dee9fc;
                        line-height: 38px;
                    }
                    .center {
                        div:nth-child(1) {
                            float: left;
                            width: 100px;
                            padding-left: 10px;
                            background: rgba(250, 251, 255, 1);
                            border-right: 1px solid #dee9fc;
                        }
                        .poolColor,
                        .equipmentColor {
                            color: #2089da;
                        }
                    }

                    .PODHeight,
                    .PODHeightZ,
                    .PODHeightS {
                        position: relative;
                        height: 61px !important;
                        div:nth-child(1) {
                            line-height: 61px;
                            height: 60px !important;
                            border-right: 1px solid #dee9fc;
                            background: rgba(250, 251, 255, 1);
                        }
                        .POD {
                            line-height: 30px;
                            width: calc(100% - 125px);
                        }
                    }
                    .center {
                        div:nth-child(1) {
                            height: 37px;
                        }
                        .moreClass {
                            right: 30px;
                            cursor: pointer;
                            color: #2089da;
                            float: right;
                            margin-right: 10px;
                        }
                        .moreClassRight {
                            position: absolute;
                            line-height: 30px;
                            right: 0;
                        }
                        .POD {
                            float: left;
                            padding-left: 10px;
                            .PODColor {
                                color: #2089da;
                            }
                            p {
                                display: inline-block;
                                width: 100px;
                                text-align: left;
                                cursor: pointer;
                            }
                            .PODp {
                                width: 60px !important;
                            }
                            .PODS {
                                width: 80px !important;
                            }
                            p:nth-child(1) {
                                width: 50px !important;
                            }
                        }
                    }
                    .down {
                        height: 50px;
                        .heightQuery {
                            float: left;
                            line-height: 50px;
                            width: 100px;
                            padding-left: 10px;
                            background: rgba(250, 251, 255, 1);
                            border-right: 1px solid #dee9fc;
                        }
                        .selectQuery {
                            float: left;
                            line-height: 50px;
                            width: calc(100% - 115px);
                            margin-left: 5px;
                            span {
                                margin-left: 5px;
                            }
                        }
                    }
                    .activeDown0 {
                        height: 137px !important;
                    }
                    .activeDown1 {
                        height: 137px !important;
                        line-height: 30px !important;
                        padding: 10px;
                    }
                    .activeDown {
                        height: 137px !important;
                        line-height: 137px !important;
                    }
                }
            }
            .form {
                margin-top: 10px;
                height: calc(100% - 195px);
                background: #fff;
                .tableBotton {
                    height: 40px;
                    padding: 10px;
                    overflow: hidden;
                    margin-bottom: 10px;
                    .left {
                        float: left;
                    }
                    .right {
                        float: right;
                        width: 100px;
                        div.el-select {
                            width: 100%;
                            border: 1px solid #2f91dc !important;
                            border-radius: 7px;
                            .el-input--suffix .el-input__inner {
                                border: #2f91dc 1px solid !important;
                            }
                            .el-input__suffix
                                .el-input__suffix-inner
                                .el-select__caret {
                                color: #2f91dc !important;
                            }
                        }
                        .el-select .el-input .el-select__caret {
                            color: #2f91dc !important;
                        }
                        ::-webkit-input-placeholder {
                            /*Webkit browsers*/
                            color: #2f91dc;
                        }
                    }
                }
                .table {
                    padding: 0 10px 10px 10px;
                    height: 100%;
                }
            }
        }
    }
</style>
<style lang="stylus">
    .tableBotton .right .el-select .el-input .el-select__caret {
        color: #2F91DC !important;
    }
</style>
