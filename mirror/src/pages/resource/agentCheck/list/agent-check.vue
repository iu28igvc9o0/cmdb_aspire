<template>
    <div class="resourceManagement" >
        <div class="managementRight" v-loading="loading" :element-loading-text="loading_text">
            <div class="condition">
                <div class="content">
                    <div class="center" :class="{PODHeightZ:PODheightZ}">
                        <div>资源池</div>
                        <div class="POD">
                            <p class="PODZ" v-for="item in poolList" :key = "item.id"  @click="queryByIdcType(item.value)" :class='item.value==queryForm.idcType?"poolColor":""'><span></span>{{item.name}}</p>
                        </div>
                    </div>
                    <div class="down" :class="{activeDown0:inputHome}">
                        <div class="heightQuery" :class="{activeDown:inputHome}">高级查询</div>
                        <div class="selectQuery" :class="{activeDown1:inputHome}">
                            <span style="margin-left:4px;">
                                <el-input v-model="queryForm.ip" placeholder="设备管理IP" style="width: 150px;"  @blur="blurQuery"></el-input>
                            </span>
                            <span>
                                <el-date-picker v-model="scanTimeSection" type="datetimerange" range-separator="至"
                                                :clearable = "true"
                                                start-placeholder="扫描时间范围" end-placeholder="扫描时间范围" value-format="yyyy-MM-dd HH:mm:ss"
                                                @change="changeQuery">
                                </el-date-picker>
                            </span>
                        </div>
                    </div>
                </div>
            </div>
            <div class="form">
                <div class="tableBotton">
                    <div class="left">
                        <span style="font-size: 16px;color: black;">扫描结果</span>
                        <span style="padding-left: 30px;"><i class="el-icon-info"></i>默认展示昨天扫描的数据</span>
                        <span style="padding-left: 30px;">
                            <el-button class="btn-icons-wrap" icon="el-icon-delete" @click="deleteByIds()">删除</el-button>
                        </span>
                    </div>
                    <div class="right">
                        <el-button class="btn-icons-wrap" icon="el-icon-upload2"  @click="exportScanComparision()">导出</el-button>
                    </div>
                </div>
                <div class="yw-el-table-wrap table">
                    <div style="border:1px solid #DEE9FC">
                        <el-table
                            stripe
                            class="yw-el-table"
                            :data="tableList"
                            style="width: 100%"
                            height="calc(100vh - 400px)"
                            @selection-change="handleSelectionChange"
                        >
                            <el-table-column type="selection" ></el-table-column>
                            <el-table-column prop="ip" label="设备ip" :min-width="'100'" sortable show-overflow-tooltip></el-table-column>
                            <el-table-column prop="idcType" label="资源池" :min-width="'100'" sortable show-overflow-tooltip></el-table-column>
                            <el-table-column prop="deviceName" label="设备名称" :min-width="'120'" sortable show-overflow-tooltip></el-table-column>
                            <el-table-column prop="deviceType" label="设备类型" :min-width="'120'" sortable show-overflow-tooltip></el-table-column>
                            <el-table-column prop="syncStatus" label="同步状态" :min-width="'120'" sortable show-overflow-tooltip></el-table-column>
                            <el-table-column prop="cmdbUpdateTime" label="CMDB更新时间" :min-width="'120'" sortable :formatter="transferTime" show-overflow-tooltip></el-table-column>
                            <el-table-column prop="scanTime" label="扫描检查时间" :min-width="'120'" sortable :formatter="transferTime" show-overflow-tooltip></el-table-column>
                        </el-table>
                        <YwPagination @handleSizeChange="handleSizeChange"
                                      @handleCurrentChange="handleCurrentChange"
                                      :current-page="paginationData.currentPage"
                                      :page-sizes="paginationData.selectPageSizes"
                                      :page-size="paginationData.pageSize"
                                      :total="paginationData.total"></YwPagination>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>
<script>
    import rbAgentCheckServicesFactory from 'src/services/cmdb/rb-agent-check-services.factory.js'
    export default {
        props: [],
        components: {
            YwPagination: () => import('src/components/common/yw-pagination.vue'),
        },
        data() {
            return {
                loading: false,
                loading_text: '正在加载数据...',
                queryForm: {
                    idcType: '',
                    ip: ''
                },
                scanTimeSection: [],
                paginationData: {
                    currentPage: 1,
                    total: 0,
                    pageSize: 50,
                    selectPageSizes: [30, 50, 100],
                    sort: 'insert_time',
                    order: 'desc'
                },
                poolList: [],
                conditionShow: true,
                inputHome: false,
                inputShow: false,
                tableList: [],
                multipleSelection: []
            }
        },
        watch: {
        },
        created() {
        },
        mounted: function() {
            this.getIdcTypeList()
            this.initScanTime()
            this.initQuery()
        },
        methods: {
            // 首次进入页面，初始化筛选时间
            initScanTime(){
                var yesterday = new Date()
                yesterday.setDate(yesterday.getDate() - 1)
                var year = yesterday.getFullYear()
                var month = yesterday.getMonth() + 1
                month = month < 10 ? '0' + month : '' + month
                var date = yesterday.getDate()
                date = date < 10 ? '0' + date : '' + date
                var dt = year + '-' + month + '-' + date
                var start = dt + ' 00:00:00'
                var end = dt + ' 23:59:59'
                this.scanTimeSection = [start,end]
                console.info(this.scanTimeSection)
            },
            // element相关方法 多选框
            handleSelectionChange (val) {
                this.multipleSelection = val
            },
            // 分页改变尺寸
            handleSizeChange (val) {
                this.paginationData.pageSize = val
                this.queryData()
            },
            // 分页改变当前页
            handleCurrentChange (val) {
                this.paginationData.currentPage = val
                this.queryData()
            },
            // 将下拉框添加"全部"选项
            fillSelectOption(options, selectType) {
                const newOptions = [{id: '', name: '全部', type: selectType, value: '', pid: ''}]
                if (options) {
                    options.forEach((item) => {
                        newOptions.push(item)
                    })
                }
                return newOptions
            },
            // 时间转换格式
            transferTime(row, column, cellValue){
                return cellValue.substring(0,cellValue.lastIndexOf('.'))
            },
            // 获取资源池
            getIdcTypeList () {
                this.rbHttp.sendRequest({
                    method: 'GET',
                    params: { 'type': 'idcType' },
                    url: '/v1/cmdb/configDict/getDictsByType'
                }).then((res) => {
                    this.poolList = this.fillSelectOption(res, 'idcType')
                })
            },
            initQuery () {
                this.paginationData.currentPage = 1
                this.queryData()
            },
            queryData () {
                this.loading_text = '正在查询数据,请稍等...'
                this.loading = true
                if (this.scanTimeSection) {
                    this.queryForm.scanStartTime = this.scanTimeSection[0]
                    this.queryForm.scanEndTime = this.scanTimeSection[1]
                } else {
                    this.queryForm.scanStartTime = null
                    this.queryForm.scanEndTime = null
                }
                this.queryForm.pageNo = this.paginationData.currentPage
                this.queryForm.pageSize = this.paginationData.pageSize
                rbAgentCheckServicesFactory.listByPage(this.queryForm).then( (res) => {
                    this.paginationData.total = res.totalSize
                    this.tableList = res.data
                }).finally(() => {
                    this.loading = false
                })
            },
            queryByIdcType (val) {
                this.queryForm.idcType = val === '全部' ? '' : val
                this.initQuery()
            },
            // 文本框blur离焦事件触发
            blurQuery() {
                this.initQuery()
            },
            // 下拉框change事件触发 查询CI列表
            changeQuery() {
                if (this.scanTimeSection) {
                    this.queryForm.scanStartTime = this.scanTimeSection[0]
                    this.queryForm.scanEndTime = this.scanTimeSection[1]
                } else {
                    this.queryForm.scanStartTime = null
                    this.queryForm.scanEndTime = null
                }
                this.initQuery()
            },
            deleteByIds () {
                if (this.multipleSelection.length < 1) {
                    this.$alert('请选择数据进行删除', '警告', {
                        confirmButtonText: '确定'
                    })
                } else {
                    this.$confirm('确认删除？').then(() => {
                        let obj = []
                        this.multipleSelection.forEach((item) => {
                            obj.push(item.id)
                        })
                        var req = {
                            ids: obj
                        }
                        rbAgentCheckServicesFactory.batchDelete(req).then((res) => {
                             if (res.flag) {
                                this.$message({
                                    message: '删除成功',
                                    type: 'success'
                                })
                                this.initQuery()
                            } else {
                                this.$message.error('删除失败')
                            }
                        })
                    })
                }
            },
            exportScanComparision () {
                this.loading = true
                this.loading_text = '正在导出数据,请稍等...'
                this.queryForm.pageNo = null
                this.queryForm.pageSize = null
                if (this.scanTimeSection) {
                    this.queryForm.scanStartTime = this.scanTimeSection[0]
                    this.queryForm.scanEndTime = this.scanTimeSection[1]
                } else {
                    this.queryForm.scanStartTime = null
                    this.queryForm.scanEndTime = null
                }
                rbAgentCheckServicesFactory.export(this.queryForm).then((res) => {
                    let blob = new Blob([res], { type: 'application/vnd.ms-excel' })
                    let objectUrl = URL.createObjectURL(blob)
                    let downLoadElement = document.createElement('a')
                    downLoadElement.href = objectUrl
                    downLoadElement.download = 'Agent设备检查.xlsx'
                    document.body.appendChild(downLoadElement)
                    downLoadElement.click()
                    document.body.removeChild(downLoadElement)
                    URL.revokeObjectURL(objectUrl)
                }).finally(() => {
                    this.loading = false
                })
            }
        }
    }
</script>
<style lang="scss" scoped>

    @import "../../../resource/iframe/iframeHome/components/rem.scss";

    .resourceManagement {
        height:100%;
        width:100%;
        .managementRight {
            float:left;
            width:100%;
            height:100%;
            .condition {
                background:#fff;
                min-width:986px;
                padding:10px;
                .content {
                    width:100%;
                    height:100%;
                    border: 1px solid #DEE9FC;
                    .center {
                        height:38px;
                        border-bottom: 1px solid #DEE9FC;
                        line-height:38px;
                    }
                    .center {
                        div:nth-child(1) {
                            float:left;
                            width:100px;
                            padding-left:10px;
                            background:rgba(250, 251, 255, 1);
                            border-right:1px solid #DEE9FC;
                        }
                        .poolColor, .equipmentColor{
                            color:#2089DA;
                        }
                    }

                    .PODHeight, .PODHeightZ, .PODHeightS {
                        position: relative;
                        height:61px !important;
                        div:nth-child(1) {
                            line-height:61px;
                            height:60px !important;
                            border-right:1px solid #DEE9FC;
                            background:rgba(250, 251, 255, 1);
                        }
                        .POD {
                            line-height:30px;
                            width:calc(100% - 125px)
                        }
                    }
                    .center {
                        div:nth-child(1) {
                            height:37px;
                        }
                        .moreClass {
                            right:30px;
                            cursor: pointer;
                            color: #2089DA;
                            float:right;
                            margin-right:10px;
                        }
                        .moreClassRight {
                            position: absolute;
                            line-height:30px;
                            right:0
                        }
                        .POD {
                            float:left;
                            padding-left:10px;
                            .PODColor {
                                color: #2089DA
                            }
                            p {
                                display:inline-block;
                                width:100px;
                                text-align: left;
                                cursor:pointer;
                            }
                            .PODp {
                                width:60px !important;
                            }
                            .PODS {
                                width:80px !important;
                            }
                            p:nth-child(1) {
                                width:50px !important;
                            }
                        }
                    }
                    .down {
                        height:50px;
                        .heightQuery {
                            float:left;
                            line-height:50px;
                            width:100px;
                            padding-left:10px;
                            background:rgba(250, 251, 255, 1);
                            border-right:1px solid #DEE9FC;
                        }
                        .selectQuery {
                            float: left;
                            line-height:50px;
                            width: calc(100% - 115px);
                            margin-left: 5px;
                            span {
                                margin-left: 5px;
                            }
                        }
                    }
                    .activeDown0 {
                        height:137px !important;
                    }
                    .activeDown1 {
                        height:137px !important;
                        line-height:30px !important;
                        padding:10px;
                    }
                    .activeDown {
                        height:137px !important;
                        line-height:137px !important;
                    }

                }
            }
            .form {
                margin-top: 10px;
                height:calc(100% - 195px);
                background:#fff;
                .tableBotton {
                    height:40px;
                    padding:10px;
                    overflow: hidden;
                    margin-bottom:10px;
                    .left {
                        float:left;
                    }
                    .right {
                        float:right;
                        width:100px;
                        div.el-select {
                            width:100%;
                            border: 1px solid #2F91DC !important;
                            border-radius: 7px;
                            .el-input--suffix .el-input__inner {
                                border: #2F91DC 1px solid !important;
                            }
                            .el-input__suffix .el-input__suffix-inner .el-select__caret {
                                color: #2F91DC !important;
                            }
                        }
                        .el-select .el-input .el-select__caret {
                            color: #2F91DC !important;
                        }
                        ::-webkit-input-placeholder{/*Webkit browsers*/
                            color:#2F91DC;
                        }
                    }
                }
                .table {
                    padding:0 10px 10px 10px;
                    height:100%
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
