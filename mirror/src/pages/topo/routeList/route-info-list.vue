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
                </div>
                <el-form style="width:100%;margin-left: 10px;"
                         label-width="120px"
                         class="components-condition yw-form"
                         label-position="left"
                         :model="form"
                         inline="true">
                    <el-row>
                        <el-col :span="8">
                            <el-form-item label="采集时间">
                                <el-date-picker v-model="collectDate"
                                                type="date"
                                                placeholder=""
                                                value-format="yyyy-MM-dd"
                                                :clearable="false"></el-date-picker>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="路由协议">
                                <el-input v-model="form.routeProto"
                                          clearable="true"></el-input>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="目的地址">
                                <el-input v-model="form.routeDest"
                                          clearable="true"></el-input>
                            </el-form-item>
                        </el-col>
                    </el-row>

                    <br />
                    <el-row>
                        <el-col :span="8">
                            <el-form-item label="掩码">
                                <el-input v-model="form.routeMask"
                                          clearable="true"></el-input>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item>
                                <span slot="label">路由的下一跳地址</span>
                                <el-input v-model="form.routeNextHop"
                                          clearable="true"></el-input>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="">
                                <el-button type="primary"
                                           size="small"
                                           @click="searchData">查询</el-button>
                                <el-button type="primary"
                                           size="small"
                                           @click="resetForm">重置</el-button>
                                <el-button size="small"
                                           @click="exportData">导出</el-button>
                            </el-form-item>
                        </el-col>
                    </el-row>
                </el-form>
                <div class="yw-el-table-wrap table">
                    <div style="border:1px solid #DEE9FC;">
                        <el-table stripe
                                  class="yw-el-table"
                                  :data="tableList"
                                  style="width: 100%;margin-top: 10px;"
                                  height="calc(100vh - 360px)"
                                  :default-sort="{prop: 'routeProto', order: 'ascending'}">
                            <el-table-column prop="routeProto"
                                             label="路由协议"
                                             :min-width="'120'"
                                             show-overflow-tooltip></el-table-column>
                            <el-table-column prop="routeDest"
                                             label="目的地址"
                                             :min-width="'120'"
                                             show-overflow-tooltip></el-table-column>
                            <el-table-column prop="routeMask"
                                             label="掩码"
                                             :min-width="'120'"
                                             show-overflow-tooltip></el-table-column>
                            <el-table-column prop="routeNextHop"
                                             label="路由的下一跳地址"
                                             :min-width="'120'"
                                             show-overflow-tooltip></el-table-column>
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
                </div>
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
                collectDate: '',
                tableHisList: [],
                tableList: [],
                form: {},
                deviceInfo: {},
                deviceStpInfo: {},
                timeVal: '',
                loading: false,
                currentPage: 1, // 当前页
                pageSize: 50, // 分页每页多少行数据
                pageSizes: [30, 50, 100], // 每页多少行数组
                total: 0, // 总共多少行数据,
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
            // 分页改变尺寸
            handleSizeChange(val) {
                this.currentPage = 1
                this.pageSize = val
                this.queryData()
            },
            // 分页改变当前页
            handleCurrentChange(val) {
                this.currentPage = val
                this.queryData()
            },
            resetForm() {
                let date = this.collectDate
                this.form = {}
                this.collectDate = date
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
            initDate() {
                let now = new Date()
                this.collectDate = moment(new Date(now.getTime() - 24 * 60 * 60 * 1000)).format('YYYY-MM-DD')
            },
            exportData() {
                let indexDate = this.getIndexDate()
                let obj = {
                    routeProto: this.form.routeProto,
                    routeDest: this.form.routeDest,
                    routeMask: this.form.routeMask,
                    routeNextHop: this.form.routeNextHop,
                    ip: this.$route.query.deviceIp,
                    idcType: this.$route.query.idcType,
                    collectDate: indexDate
                }
                this.loading = true
                this.rbHttp
                    .sendRequest({
                        method: 'POST',
                        url: '/v1/alerts/network/exportRouteInfo',
                        binary: true,
                        data: obj
                    })
                    .then(res => {
                        let blob = new Blob([res], { type: 'application/vnd.ms-excel' })
                        let objectUrl = URL.createObjectURL(blob)
                        // window.location.href = objectUrl
                        let downLoadElement = document.createElement('a')
                        downLoadElement.href = objectUrl
                        downLoadElement.download = this.$route.query.deviceIp + '_路由信息表数据.xlsx'
                        document.body.appendChild(downLoadElement)
                        downLoadElement.click()
                        document.body.removeChild(downLoadElement)
                        URL.revokeObjectURL(objectUrl)
                    }).finally(() => {
                        this.loading = false
                    })
            },
            getIndexDate() {
                let indexDate = ''
                if (this.collectDate) {
                    indexDate = moment(this.collectDate).format('YYYYMMDD')
                }
                return indexDate
            },
            searchData() {
                this.currentPage = 1
                this.queryData()
            },
            queryData() {

                let indexDate = this.getIndexDate()
                let obj = {
                    routeProto: this.form.routeProto,
                    routeDest: this.form.routeDest,
                    routeMask: this.form.routeMask,
                    routeNextHop: this.form.routeNextHop,
                    ip: this.$route.query.deviceIp,
                    idcType: this.$route.query.idcType,
                    collectDate: indexDate,
                    pageNum: this.currentPage,
                    pageSize: this.pageSize
                }
                this.loading = true
                this.rbHttp
                    .sendRequest({
                        method: 'POST',
                        url: '/v1/alerts/network/getRouteInfoList',
                        data: obj
                    })
                    .then(res => {
                        if (res) {
                            this.deviceStpInfo = res
                            this.tableList = res.routeDataList
                            this.total = res.total
                            // this.tableList = this.filterTable()
                            if (!this.collectDate) {
                                this.collectDate = moment(res.startTime).format('YYYY-MM-DD')
                            }
                        } else {
                            this.total = 0
                            if (!this.collectDate) {
                                this.initDate()
                            }
                            this.tableList = []
                        }

                    }).finally(() => {
                        this.loading = false
                    })
            },
            filterTable() {
                // let tableTemp = JSON.parse(JSON.stringify(this.tableHisList))
                let tableTemp = this.tableHisList.filter((item) => {
                    let flag = true
                    if (this.form.routeProto) {
                        if (item.routeProto != this.form.routeProto) {
                            return false
                        }
                    }
                    if (this.form.routeDest) {
                        if (item.routeDest != this.form.routeDest) {
                            return false
                        }
                    }
                    if (this.form.routeMask) {
                        if (item.routeMask != this.form.routeMask) {
                            return false
                        }
                    }
                    if (this.form.routeNextHop) {
                        if (item.routeNextHop != this.form.routeNextHop) {
                            return false
                        }
                    }
                    return flag
                })
                return tableTemp
            },
            sortTable(tableList) {
                tableList.sort(function (a, b) {
                    let aa = a.stpPort.split('_')[1]
                    let bb = b.stpPort.split('_')[1]
                    return aa - bb                })
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
