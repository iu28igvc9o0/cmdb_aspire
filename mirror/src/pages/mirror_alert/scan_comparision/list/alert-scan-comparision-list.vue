<template>
    <div class="resourceManagement">
        <div class="managementRight"
             v-loading="loading"
             :element-loading-text="loading_text">
            <div class="condition">
                <div class="content">
                    <div class="center"
                         :class="{PODHeightZ:PODheightZ}">
                        <div>资源池</div>
                        <div class="POD">
                            <p class="PODZ"
                               v-for="item in poolList"
                               :key="item.id"
                               @click="queryByIdcType(item.value)"
                               :class='item.value==queryForm.idcType?"poolColor":""'><span></span>{{item.name}}</p>
                        </div>
                        <div v-show="ZYC"
                             class="moreClass"
                             @click="MoreZ()"
                             v-if="moreIconZ">更多<i class="el-icon-arrow-down"></i></div>
                        <div class="moreClass moreClassRight"
                             @click="MoreUPZ()"
                             v-else>收起<i class="el-icon-arrow-up"></i></div>
                    </div>
                    <div class="down"
                         :class="{activeDown0:inputHome}">
                        <div class="heightQuery"
                             :class="{activeDown:inputHome}">高级查询</div>
                        <div class="selectQuery"
                             :class="{activeDown1:inputHome}">
                            <span style="margin-left:4px;">
                                <el-input v-model="queryForm.deviceIp"
                                          placeholder="设备管理IP"
                                          style="width: 150px;"
                                          @blur="blurQuery"></el-input>
                            </span>
                            <span>
                                <el-date-picker v-model="queryForm.scanTime"
                                                type="datetimerange"
                                                range-separator="至"
                                                :clearable="true"
                                                start-placeholder="同步开始时间"
                                                end-placeholder="同步结束时间"
                                                value-format="yyyy-MM-dd HH:mm:ss"
                                                @change="changeQuery">
                                </el-date-picker>
                            </span>
                            <span style="display: inline-block;">
                                <el-select v-model="queryForm.synStatus"
                                           placeholder="同步状态"
                                           :clearable="true"
                                           @change="changeQuery">
                                    <el-option :label="'同步'"
                                               :value="'1'"></el-option>
                                    <el-option :label="'未同步'"
                                               :value="'2'"></el-option>
                                    <el-option :label="'同步失败'"
                                               :value="'3'"></el-option>
                                </el-select>
                            </span>
                            <span>
                                <el-date-picker v-model="queryForm.curMoniTime"
                                                type="datetimerange"
                                                range-separator="至"
                                                :clearable="true"
                                                start-placeholder="最新告警时间范围"
                                                end-placeholder="最新告警时间范围"
                                                value-format="yyyy-MM-dd HH:mm:ss"
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
                        <span style="padding-left: 30px;"><i class="el-icon-info"></i>设备已监控，未关联CMDB数据</span>
                        <span style="padding-left: 30px;">
                            <el-button class="btn-icons-wrap"
                                       icon="el-icon-delete"
                                       @click="deleteScanComparisionById()">删除</el-button>
                            <el-button class="btn-icons-wrap"
                                       icon="el-icon-refresh"
                                       @click="synScanComparision()">同步</el-button>
                        </span>
                    </div>
                    <div class="right">
                        <el-button class="btn-icons-wrap"
                                   icon="el-icon-upload2"
                                   @click="exportScanComparision()">导出</el-button>
                    </div>
                </div>
                <div class="yw-el-table-wrap table">
                    <div style="border:1px solid #DEE9FC">
                        <el-table stripe
                                  class="yw-el-table auto-height"
                                  :data="tableList"
                                  style="width: 100%"
                                  @selection-change="handleSelectionChange">
                            <el-table-column type="selection"></el-table-column>
                            <el-table-column prop="deviceIp"
                                             label="设备ip"
                                             :min-width="'120'"
                                             sortable
                                             show-overflow-tooltip></el-table-column>
                            <el-table-column prop="idcType"
                                             label="资源池"
                                             :min-width="'120'"
                                             sortable
                                             show-overflow-tooltip></el-table-column>
                            <el-table-column prop="startScanTime"
                                             label="开始扫描时间"
                                             :min-width="'120'"
                                             sortable
                                             show-overflow-tooltip></el-table-column>
                            <el-table-column prop="curScanTime"
                                             label="最新扫描时间"
                                             :min-width="'120'"
                                             sortable
                                             show-overflow-tooltip></el-table-column>
                            <el-table-column prop="synStatus"
                                             label="同步状态"
                                             :min-width="'120'"
                                             sortable
                                             show-overflow-tooltip></el-table-column>
                            <el-table-column prop="curMoniTime"
                                             label="最新告警时间"
                                             :min-width="'120'"
                                             sortable
                                             show-overflow-tooltip></el-table-column>
                            <el-table-column prop="curSynTime"
                                             label="最新同步时间"
                                             :min-width="'120'"
                                             sortable
                                             show-overflow-tooltip></el-table-column>
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
    import rbAlertScanComparisionServicesFactory from 'src/services/alert/rb-alert-scan-comparision-services.factory.js'
    export default {
        props: [],
        components: {
            importInstances: () => import('src/pages/cmdb/v2/instance/import/import-instance.vue'),
            YwPagination: () => import('src/components/common/yw-pagination.vue'),
        },
        data() {
            return {
                loading: false,
                loading_text: '正在加载数据...',
                moreIcon: true,
                moreIconZ: true,
                moreIconS: true,
                PODheightZ: false,
                PODheightS: false,
                PODheight: false,
                ZYC: false,
                pod: false,
                queryForm: {
                    idcType: '',
                    deviceIp: '',
                    scanTime: [],
                    synStatus: '',
                    curMoniTime: ''
                },
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
        mounted: function () {
            this.getIdcTypeList()
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
            // 将下拉框添加"全部"选项
            fillSelectOption(options, selectType) {
                const newOptions = [{ id: '', name: '全部', type: selectType, value: '', pid: '' }]
                if (options) {
                    options.forEach((item) => {
                        newOptions.push(item)
                    })
                }
                return newOptions
            },
            // 获取资源池
            getIdcTypeList() {
                this.rbHttp.sendRequest({
                    method: 'GET',
                    params: { 'type': 'idcType' },
                    url: '/v1/cmdb/configDict/getDictsByType'
                }).then((res) => {
                    this.poolList = this.fillSelectOption(res, 'idcType')
                    if (this.poolList.length > 9) {
                        this.PODheightZ = true
                        this.ZYC = true
                    }
                })
            },
            MoreZ() {
                this.rbHttp.sendRequest({
                    method: 'GET',
                    params: { 'type': 'idcType' },
                    url: '/v1/cmdb/configDict/getDictsByType'
                }).then((res) => {
                    this.poolList = this.fillSelectOption(res, 'idcType')
                    this.moreIconZ = !this.moreIconZ
                    if (this.poolList.length > 9) {
                        this.PODheightZ = true
                    }
                })
            },
            MoreUPZ() {
                this.rbHttp.sendRequest({
                    method: 'GET',
                    params: { 'type': 'idcType' },
                    url: '/v1/cmdb/configDict/getDictsByType'
                }).then((res) => {
                    this.poolList = this.fillSelectOption(res, 'idcType')
                    this.poolList = this.poolList.slice(0, 9)
                    this.moreIconZ = !this.moreIconZ
                    this.PODheightZ = false
                })
            },
            initQuery() {
                this.paginationData.currentPage = 1
                this.queryData()
            },
            queryData() {
                this.loading = true
                if (this.queryForm.scanTime) {
                    this.queryForm.startTime = this.queryForm.scanTime[0]
                    this.queryForm.endTime = this.queryForm.scanTime[1]
                }
                if (this.queryForm.curMoniTime) {
                    this.queryForm.curMoniStartTime = this.queryForm.curMoniTime[0]
                    this.queryForm.curMoniEndTime = this.queryForm.curMoniTime[1]
                }
                this.queryForm.synStatus = this.queryForm.synStatus === 'all' ? '' : this.queryForm.synStatus
                this.queryForm.pageNum = this.paginationData.currentPage
                this.queryForm.pageSize = this.paginationData.pageSize
                rbAlertScanComparisionServicesFactory.getScanComparisionList(this.queryForm).then((data) => {
                    this.paginationData.total = data.count
                    this.tableList = data.result
                }).finally(() => {
                    this.loading = false
                })
            },
            queryByIdcType(val) {
                this.queryForm.idcType = val === '全部' ? '' : val
                this.initQuery()
            },
            // 文本框blur离焦事件触发
            blurQuery() {
                this.initQuery()
            },
            // 下拉框change事件触发 查询CI列表
            changeQuery() {
                this.initQuery()
            },
            deleteScanComparisionById() {
                if (this.multipleSelection.length < 1) {
                    this.$alert('请选择数据进行清除', '警告', {
                        confirmButtonText: '确定'
                    })
                } else {
                    this.$confirm('确认删除？').then(() => {
                        let obj = []
                        this.multipleSelection.forEach((item) => {
                            obj.push(item.id)
                        })
                        rbAlertScanComparisionServicesFactory.deleteScanComparisionById(obj).then((data) => {
                            if (data === 'success') {
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
            synScanComparision() {
                if (this.multipleSelection.length < 1) {
                    this.$alert('请选择数据进行同步', '警告', {
                        confirmButtonText: '确定'
                    })
                } else {
                    this.$confirm('确认同步？').then(() => {
                        let obj = []
                        let userName = sessionStorage.getItem('username')
                        this.multipleSelection.forEach((item) => {
                            let o = {}
                            o.userName = userName
                            o.deviceIp = item.deviceIp
                            o.idcType = item.idcType
                            obj.push(o)
                        })
                        rbAlertScanComparisionServicesFactory.synScanComparision(obj).then((data) => {
                            if (data !== 'success') {
                                this.$message.error('同步失败')
                            }
                            this.initQuery()
                        })
                    })
                }
            },
            exportScanComparision() {
                this.loading = true
                this.loading_text = '正在导出数据,请稍等...'
                if (this.queryForm.scanTime !== '') {
                    this.queryForm.startTime = this.queryForm.scanTime[0]
                    this.queryForm.endTime = this.queryForm.scanTime[1]
                }
                this.queryForm.synStatus = this.queryForm.synStatus === 'all' ? '' : this.queryForm.synStatus
                this.queryForm.pageNum = this.paginationData.currentPage
                this.queryForm.pageSize = this.paginationData.pageSize
                rbAlertScanComparisionServicesFactory.exportScanComparision(this.queryForm).then((res) => {
                    let blob = new Blob([res], { type: 'application/vnd.ms-excel' })
                    let objectUrl = URL.createObjectURL(blob)
                    // window.location.href = objectUrl
                    let downLoadElement = document.createElement('a')
                    downLoadElement.href = objectUrl
                    downLoadElement.download = '告警扫描对账数据列表.xlsx'
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
    @import '../../../resource/iframe/iframeHome/components/rem.scss';

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
                position: absolute;
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
                .el-input__suffix .el-input__suffix-inner .el-select__caret {
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
