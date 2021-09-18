<template>
    <div @click="router()"
         class="resourceManagement">
        <el-form class="yw-form clearfix"
                 :inline="true"
                 label-width="80px">
            <el-form-item label>
                <el-button class="btn-time"
                           :class="timeRange === 'week' ? 'active' : 'normal'"
                           style="margin-left: 10px;"
                           type="text"
                           @click="getDataByTime('week')">本周</el-button>
                <el-button class="btn-time"
                           :class="timeRange === 'month' ? 'active' : 'normal'"
                           style="margin-left: 10px;"
                           type="text"
                           @click="getDataByTime('month')">本月</el-button>
                <el-button class="btn-time"
                           :class="timeRange === '1month' ? 'active' : 'normal'"
                           style="margin-left: 10px;"
                           type="text"
                           @click="getDataByTime('1month')">最近一个月</el-button>

            </el-form-item>
            <el-form-item label="自定义时间"
                          style="margin-left: 50px;">
                <el-date-picker v-model="dateRange"
                                style="width:338px"
                                @change="changeTab"
                                format="yyyy-MM-dd"
                                value-format="yyyy-MM-dd"
                                type="daterange"
                                range-separator="至"
                                start-placeholder="开始日期"
                                end-placeholder="结束日期">
                </el-date-picker>
            </el-form-item>
            <el-form-item label="时间粒度"
                          style="margin-left: 50px;">
                <el-button class="btn-time"
                           :class="granularity === 'day' ? 'active' : 'normal'"
                           style="margin-left: 10px;"
                           type="text"
                           @click="setGranularity('day')">日</el-button>
                <el-button class="btn-time"
                           :class="granularity === 'week' ? 'active' : 'normal'"
                           style="margin-left: 10px;"
                           type="text"
                           @click="setGranularity('week')">周</el-button>
                <el-button class="btn-time"
                           :class="granularity === 'month' ? 'active' : 'normal'"
                           style="margin-left: 10px;"
                           type="text"
                           @click="setGranularity('month')">月</el-button>
            </el-form-item>
            <div class="fr">

            </div>
        </el-form>
        <div class="managementRight">
            <div class="condition">
                <div class="content">
                    <div class="center"
                         :class="{ PODHeightZ: PODheightZ }">
                        <div>资源池</div>
                        <div class="POD">
                            <p class="PODZ"
                               v-for="item in poolList"
                               :key="item.id"
                               @click="pool(item.value)"
                               :class="item.value == queryForm.idcType ? 'poolColor' : ''">
                                <span></span>{{ item.name }}
                            </p>
                        </div>
                        <!-- <div v-show="ZYC"
                             class="moreClass"
                             @click="MoreZ()"
                             v-if="moreIconZ">
                            更多<i class="el-icon-arrow-down"></i>
                        </div>
                        <div class="moreClass moreClassRight"
                             @click="MoreUPZ()"
                             v-else>
                            收起<i class="el-icon-arrow-up"></i>
                        </div> -->
                        <div v-show="ZYC"
                             class="BOTTON"
                             style="float: right;margin-right: 10px;"
                             v-if="moreIconZ">
                            <el-button @click="MoreZ()">更多<i class="el-icon-arrow-down"></i></el-button>
                        </div>
                        <div class="BOTTON"
                             style="position: absolute;right: 10px;"
                             @click="MoreUPZ()"
                             v-else>
                            <el-button>收起<i class="el-icon-arrow-up"></i></el-button>
                        </div>

                    </div>
                    <div class="center"
                         :class="{PODHeightS:PODheightS}">
                        <div>网络设备类型</div>
                        <div class="POD">
                            <p class="PODS"
                               v-for="item in netDeviceTypeList"
                               :key="item.id"
                               @click="equipment(item.value, item.id)"
                               :class='item.value==queryForm.deviceType?"equipmentColor":""'><span></span>{{item.name}}</p>
                        </div>
                        <!-- <div v-show="SHFL"
                             class="moreClass"
                             @click="MoreS()"
                             v-if="moreIconS">更多<i class="el-icon-arrow-down"></i></div>
                                                          @click="MoreUPS()"
                             v-else>收起<i class="el-icon-arrow-up"></i></div>

                             -->
                        <div v-show="SHFL"
                             class="BOTTON"
                             style="float: right;margin-right: 10px;"
                             v-if="moreIconS">
                            <el-button @click="MoreS()">更多<i class="el-icon-arrow-down"></i></el-button>
                        </div>
                        <div class="BOTTON"
                             style="float: right;margin-right: 10px;"
                             @click="MoreUPS()"
                             v-else>
                            <el-button>收起<i class="el-icon-arrow-up"></i></el-button>
                        </div>
                    </div>
                    <div class="center"
                         :class="{ PODHeight: PODheight }">
                        <div>POD池</div>
                        <div class="POD">
                            <p class="PODp"
                               v-for="item in podList"
                               :key="item.id"
                               @click="selectPod(item.value)"
                               :class="item.value == queryForm.pod ? 'PODColor' : ''">
                                <span></span>{{ item.name }}
                            </p>
                        </div>
                        <!-- <div v-show="pod"
                             class="moreClass"
                             @click="More()"
                             v-if="moreIcon">
                            更多<i class="el-icon-arrow-down"></i>
                        </div>
                        <div class="moreClass moreClassRight"
                             @click="MoreUP()"
                             v-else>
                            收起<i class="el-icon-arrow-up"></i>
                        </div> -->
                        <div v-show="pod"
                             class="BOTTON"
                             style="float: right;margin-right: 10px;"
                             v-if="moreIcon">
                            <el-button @click="More()">更多<i class="el-icon-arrow-down"></i></el-button>
                        </div>
                        <div class="BOTTON"
                             style="position: absolute;right: 10px;"
                             @click="MoreUP()"
                             v-else>
                            <el-button>收起<i class="el-icon-arrow-up"></i></el-button>
                        </div>

                    </div>
                    <div class="down"
                         :class="{ activeDown0: inputHome }">
                        <div class="heightQuery"
                             :class="{ activeDown: inputHome }">
                            高级查询
                        </div>
                        <div class="selectQuery"
                             :class="{ activeDown1: inputHome }">
                            <p style="margin-left:4px;">
                                <el-input v-model="queryForm.ip"
                                          placeholder="设备管理IP"
                                          :clearable="true"
                                          @blur="blurQuery"></el-input>
                            </p>

                            <p>
                                <el-select v-model="queryForm.department1"
                                           placeholder="一级部门"
                                           filterable
                                           :clearable="true"
                                           @change="changeDepartment1">
                                    <el-option v-for="item in department1List"
                                               :key="item.id"
                                               :label="item.name"
                                               :value="item.value"
                                               prefix-icon="el-icon-search"></el-option>
                                </el-select>
                            </p>
                            <p>
                                <el-select v-model="queryForm.department2"
                                           placeholder="二级部门"
                                           filterable
                                           :clearable="true"
                                           @change="changeDepartment2">
                                    <el-option v-for="item in department2List"
                                               :key="item.id"
                                               :label="item.name"
                                               :value="item.value"
                                               prefix-icon="el-icon-search"></el-option>
                                </el-select>
                            </p>
                            <p>
                                <el-select v-model="queryForm.bizSystem"
                                           placeholder="业务系统"
                                           filterable
                                           :clearable="true"
                                           @change="changeQuery">
                                    <el-option v-for="(item, index) in bizSystemList"
                                               :key="index"
                                               :label="item.bizSystem"
                                               :value="item.bizSystem"
                                               prefix-icon="el-icon-search"></el-option>
                                </el-select>
                            </p>
                            <!-- <div @click="showInput"
                                 v-if="conditionShow">
                                更多<i class="el-icon-arrow-down"></i>
                            </div>
                            <div @click="showInput"
                                 style="margin-right:0"
                                 v-else>
                                收起<i class="el-icon-arrow-up"></i>
                            </div> -->
                            <div class="BOTTON"
                                 style="float: right;margin-right: 10px;"
                                 v-if="conditionShow">
                                <el-button @click="showInput()">更多<i class="el-icon-arrow-down"></i></el-button>
                            </div>
                            <div class="BOTTON"
                                 style="margin-right:0;"
                                 @click="showInput()"
                                 v-else>
                                <el-button>收起<i class="el-icon-arrow-up"></i></el-button>
                            </div>

                            <div @click="emptyScreening(true)">清空筛选</div>
                            <div v-show="inputShow">
                                <p>
                                    <el-input v-model="queryForm.deviceName"
                                              placeholder="设备名称"
                                              :clearable="true"
                                              @blur="blurQuery"></el-input>
                                </p>
                                <p>
                                    <el-input v-model="queryForm.deviceSN"
                                              placeholder="设备序列号"
                                              :clearable="true"
                                              @blur="blurQuery"></el-input>
                                </p>
                                <p>
                                    <el-input v-model="queryForm.deviceModel"
                                              placeholder="设备型号"
                                              :clearable="true"
                                              @blur="blurQuery"></el-input>
                                </p>
                                <p>
                                    <el-input v-model="queryForm.room"
                                              placeholder="机房位置"
                                              :clearable="true"
                                              @blur="blurQuery"></el-input>
                                </p>
                                <p>
                                    <el-input v-model="queryForm.deviceProject"
                                              placeholder="项目名称"
                                              :clearable="true"
                                              @blur="blurQuery"></el-input>
                                </p>
                                <p>
                                    <el-select v-model="queryForm.deviceStatus"
                                               placeholder="设备运行状态"
                                               filterable
                                               :clearable="true"
                                               @change="changeQuery">
                                        <el-option v-for="item in deviceStatusList"
                                                   :key="item.id"
                                                   :label="item.name"
                                                   :value="item.value"
                                                   prefix-icon="el-icon-search"></el-option>
                                    </el-select>
                                </p>
                                <!-- <p>
                  <el-select
                    v-model="queryForm.maintenanceFactory"
                    placeholder="维保厂家"
                    filterable
                    :clearable="true"
                    @change="changeQuery"
                  >
                    <el-option
                      v-for="(item,index) in maintenanceFactoryList"
                      :key="index"
                      :label="item"
                      :value="item"
                      prefix-icon="el-icon-search"
                    ></el-option>
                  </el-select>
                </p>-->
                                <p>
                                    <el-select v-model="queryForm.mfrFactory"
                                               placeholder="品牌"
                                               filterable
                                               :clearable="true"
                                               @change="changeQuery">
                                        <el-option v-for="(item,index) in mfrFactoryList"
                                                   :key="index"
                                                   :label="item"
                                                   :value="item"
                                                   prefix-icon="el-icresource/iframe/resourceManagementon-search"></el-option>
                                    </el-select>
                                </p>
                                <p>
                                    <el-input v-model="queryForm.deptOperation"
                                              placeholder="维护部门"
                                              :clearable="true"
                                              @blur="blurQuery"></el-input>
                                </p>
                                <p>
                                    <el-input v-model="queryForm.ops"
                                              placeholder="维护人员"
                                              :clearable="true"
                                              @blur="blurQuery"></el-input>
                                </p>
                                <p style="display: -webkit-inline-box;margin: 1px -9px;">
                                    <el-date-picker v-model="insertTime"
                                                    style="width: 280px"
                                                    type="daterange"
                                                    range-separator="至"
                                                    start-placeholder="设备录入日期"
                                                    end-placeholder="设备录入日期"
                                                    value-format="yyyy-MM-dd"
                                                    @change="changeQuery">
                                    </el-date-picker>
                                </p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="form">
                <div class="tableBotton">
                    <div class="left">
                        <el-button @click="exportData()">导出</el-button>
                    </div>
                </div>
                <div class="yw-el-table-wrap table">
                    <div style="border:1px solid #DEE9FC">
                        <el-table stripe
                                  class="yw-el-table"
                                  :data="instanceDataList"
                                  style="width: 100%"
                                  v-loading="loading"
                                  height="calc(100vh - 420px)"
                                  :default-sort="{ prop: 'date', order: 'descending' }"
                                  :header-cell-style="{
                                background: '#E8F0FC',
                                color: '#3A4154',
                                height: '19px'
                            }">
                            <el-table-column type="selection"
                                             width="40"></el-table-column>
                            <!-- sortable="custom" -->
                            <el-table-column prop="ip"
                                             label="管理IP"
                                             width="120"
                                             fixed
                                             show-overflow-tooltip>
                                <template slot-scope="scope">
                                    <el-button style="text-decoration:none"
                                               type="text"
                                               align="left"
                                               @click="toDetail(scope.row.id)">{{ scope.row.ip }}</el-button>
                                </template>
                            </el-table-column>
                            <el-table-column prop="device_name"
                                             label="设备名称"
                                             fixed
                                             show-overflow-tooltip>
                                <template slot-scope="scope">
                                    <el-button style="text-decoration:none"
                                               type="text"
                                               align="left"
                                               @click="toDetail(scope.row.id)">{{ scope.row.device_name }}</el-button>
                                </template>
                            </el-table-column>
                            <el-table-column prop="device_type"
                                             label="设备类型"
                                             fixed
                                             sortable
                                             show-overflow-tooltip>
                            </el-table-column>
                            <el-table-column prop="roomId"
                                             label="机房位置"
                                             fixed
                                             sortable
                                             show-overflow-tooltip>
                            </el-table-column>
                            <el-table-column prop="pod_name"
                                             label="POD池名称"
                                             fixed
                                             sortable
                                             show-overflow-tooltip>
                            </el-table-column>
                            <el-table-column prop="cpuMax"
                                             label="CPU最大利用率（%）"
                                             fixed
                                             sortable
                                             show-overflow-tooltip>
                                <template slot-scope="scope">
                                    <el-button type="text"
                                               @click="getTrends(scope.row ,'cpu','max')">{{getData(scope.row.cpuMax)}}</el-button>
                                </template>
                            </el-table-column>
                            <el-table-column prop="cpuAvg"
                                             label="CPU平均利用率（%）"
                                             fixed
                                             sortable
                                             show-overflow-tooltip>
                                <template slot-scope="scope">
                                    <el-button type="text"
                                               @click="getTrends(scope.row,'cpu','avg')">{{getData(scope.row.cpuAvg)}}</el-button>
                                </template>
                            </el-table-column>
                            <el-table-column prop="cpuMin"
                                             label="CPU最小利用率（%）"
                                             fixed
                                             sortable
                                             show-overflow-tooltip>
                                <template slot-scope="scope">
                                    <el-button type="text"
                                               @click="getTrends(scope.row,'cpu','min')">{{getData(scope.row.cpuMin)}}</el-button>
                                </template>
                            </el-table-column>
                            <el-table-column prop="memMax"
                                             label="内存最大利用率（%）"
                                             fixed
                                             sortable
                                             show-overflow-tooltip>
                                <template slot-scope="scope">
                                    <el-button type="text"
                                               @click="getTrends(scope.row,'mem','max')">{{getData(scope.row.memMax)}}</el-button>
                                </template>
                            </el-table-column>
                            <el-table-column prop="memAvg"
                                             label="内存平均利用率（%）"
                                             fixed
                                             sortable
                                             show-overflow-tooltip>
                                <template slot-scope="scope">
                                    <el-button type="text"
                                               @click="getTrends(scope.row,'mem','avg')">{{getData(scope.row.memAvg)}}</el-button>
                                </template>
                            </el-table-column>
                            <el-table-column prop="memMin"
                                             label="内存最小利用率（%）"
                                             fixed
                                             sortable
                                             show-overflow-tooltip>
                                <template slot-scope="scope">
                                    <el-button type="text"
                                               @click="getTrends(scope.row,'mem','min')">{{getData(scope.row.memMin)}}</el-button>
                                </template>
                            </el-table-column>
                        </el-table>
                        <YwPagination @handleSizeChange="handleSizeChange"
                                      @handleCurrentChange="handleCurrentChange"
                                      :current-page="paginationData.currentPage"
                                      :page-sizes="paginationData.selectPageSizes"
                                      :page-size="paginationData.pageSize"
                                      :total="paginationData.total"></YwPagination>
                    </div>
                </div>
                <importInstances ref="importInstances"
                                 v-if="display.isImport"
                                 :showImport="display.isImport"
                                 @setImportDisplay="setImportDisplay"
                                 :importType="importType"></importInstances>
            </div>
        </div>
        <DialogTrends v-if="dialogMsg.dialogVisible"
                      :dialogMsg="dialogMsg"
                      @closeDialog="closeDialogSearch"></DialogTrends>
    </div>
</template>
<script>
    import moment from 'moment'
    import rbCmdbServiceFactory from 'src/services/cmdb/rb-cmdb-service.factory.js'
    import { formatDate2 } from 'src/assets/js/utility/rb-filters.js'
    import rbmaintenanceCommonUtil from 'src/services/mainten/rb-cmdb-mainten-common.js'
    import configDictServiceFactory from 'src/services/cmdb/rb-configDict-service.factory.js'
    export default {
        props: ['queryType', 'parentParams'],
        components: {
            importInstances: () =>
                import('src/pages/cmdb/v2/instance/import/import-instance.vue'),
            YwPagination: () => import('src/components/common/yw-pagination.vue'),
            DialogTrends: () => import('src/pages/mirror_alert/net_ performance_analysis/trendsView.vue')
        },
        data() {
            return {
                loading: true,
                dialogMsg: {
                    dialogVisible: false,
                    data: {} // 数据
                },
                granularity: 'day',
                timeRange: 'week',
                dateRange: [],
                activeIndex: '1',
                queryParams: this.parentParams || {},
                queryKeys: [
                    'moduleId',
                    'idcType',
                    'pod',
                    'deviceClass',
                    'deviceType',
                    'deviceModel',
                    'department1',
                    'department2',
                    'bizSystem',
                    'ip',
                    'deviceName',
                    'deviceSN',
                    'room',
                    'deviceProject',
                    'deviceStatus',
                    'maintenanceFactory',
                    'mfrFactory',
                    'startInsertTime',
                    'endInsertTime',
                    'deptOperation',
                    'ops'
                ],
                moreIcon: true,
                moreIconZ: true,
                moreIconS: true,
                PODheightZ: false,
                PODheightS: false,
                PODheight: false,
                ZYC: false,
                SHFL: false,
                pod: false,
                importType: 'instance',
                display: {
                    isImport: false,
                    isEdit: false,
                    isSearchPlane: false,
                    isBatchUpdate: false
                },
                conditionShow: true,
                inputHome: false,
                inputShow: false,
                insertTime: '',
                queryForm: {
                    moduleId: '',
                    idcType: '',
                    pod: '',
                    deviceClass: '网络设备',
                    deviceType: '',
                    deviceModel: '',
                    department1: '',
                    department2: '',
                    bizSystem: '',
                    ip: '',
                    deviceName: '',
                    deviceSN: '',
                    room: '',
                    deviceProject: '',
                    deviceStatus: '',
                    maintenanceFactory: '',
                    mfrFactory: '',
                    // online_time: [],
                    startInsertTime: '',
                    endInsertTime: '',
                    deptOperation: '',
                    ops: ''
                },
                allDictList: [], // 所有的设备字典
                poolList: [], // 资源池列表
                deviceClassList: [], // 设备分类列表
                netDeviceTypeList: [],
                deviceTypeList: [], // 设备类型列表
                podList: [], // POD池列表
                department1List: [], // 一级部门列表
                department2List: [], // 二级部门列表
                bizSystemList: [], // 业务系统列表
                deviceStatusList: [], // 设备运行状态
                maintenanceFactoryList: [], // 维保厂家
                mfrFactoryList: [], // 品牌
                column_data: {},
                columnList: [],
                selectColumns: ['ip', 'device_name', 'insert_time', 'update_time'], // 默认展示的项
                dbFilterData: {}, // 数据中保存的选择列值
                instanceDataList: [], // 主机数据集合
                multipleSelection: [],
                multipleSelectionAll: [],
                paginationData: {
                    currentPage: 1,
                    total: 0,
                    pageSize: 50,
                    selectPageSizes: [30, 50, 100],
                    sort: 'insert_time',
                    order: 'desc'
                },
                moduleList: [] // 模型列表
            }
        },
        watch: {
            parentParams(val) {
                this.queryParams = val
                this.initParams()
                if (val.moduleId) {
                    this.queryForm.moduleId = val.moduleId
                }
                this.multipleSelectionAll = []
                this.searchInstanceList()
            }
        },
        created() {
            if (this.$route.query.parentParams) {
                this.queryParams = this.$route.query.parentParams
            }
            this.initParams()
        },
        mounted: function () {
            this.setDate('week')
            // 初始化字典值
            this.initDict()
            // 获取当前模型的过滤列
            this.getColumnFilter()
            // 获取表格数据信息
            this.searchInstanceList()
            if (!this.queryForm.moduleId) {
                this.queryModuleList()
            }
        },
        methods: {
            getData(val) {
                if (val) {
                    return (Math.round(val * 100) / 100)
                }
                return val
            },
            getTrends(val, item) {
                this.dialogMsg.data = {
                    ip: val.ip,
                    item: item,
                    idcType: val.idcType,
                    granularity: this.granularity,
                    dateRange: this.dateRange
                }
                this.dialogMsg.dialogVisible = true
            },
            // 关闭弹窗
            closeDialogSearch() {
                this.dialogMsg.dialogVisible = false
                this.dialogMsg.data = {}
            },
            setDate(flag) {
                let now = new Date()
                let nowDayOfWeek = now.getDay() // 今天本周的第几天
                let nowDay = now.getDate() // 当前日
                let nowMonth = now.getMonth() // 当前月
                let nowYear = now.getFullYear() // 当前年
                if (flag === 'week') {
                    let before = new Date(nowYear, nowMonth, nowDay - nowDayOfWeek + 1)
                    this.dateRange = [formatDate2(before), formatDate2(now)]
                } else if (flag === 'month') {
                    let before = new Date(nowYear, nowMonth, 1)
                    this.dateRange = [formatDate2(before), formatDate2(now)]
                } else if (flag === '1month') {
                    // now.setMonth(now.getMonth()+1);

                    let now1 = new Date()
                    now1.setDate(0)
                    let num = now1.getDate() - 1
                    let before = now.getTime() - 1000 * 60 * 60 * 24 * num
                    this.dateRange = [formatDate2(before), formatDate2(now)]
                }
            },
            router() {
                // console.log('this.$route.query.name', this.$route.query.name,this.$route.query.seriesName)
            },
            initParams() {
                let _t = this
                this.queryKeys.forEach(item => {
                    _t.queryForm[item] = ''
                })
                if (_t.queryParams && Object.keys(_t.queryParams).length > 0) {
                    Object.keys(_t.queryParams).forEach(item2 => {
                        _t.queryForm[item2] = _t.queryParams[item2]
                    })
                }
                if (_t.queryForm['startInsertTime'] && _t.queryForm['endInsertTime']) {
                    _t.insertTime = [
                        _t.queryForm['startInsertTime'],
                        _t.queryForm['endInsertTime']
                    ]
                }
            },
            // POD池更多
            More() {
                this.rbHttp
                    .sendRequest({
                        method: 'GET',
                        params: { type: 'pod_name' },
                        url: '/v1/cmdb/configDict/getDictsByType'
                    })
                    .then(res => {
                        this.podList = this.fillSelectOption(res, 'pod_name')
                        this.moreIcon = !this.moreIcon
                        if (this.podList.length > 12) {
                            this.PODheight = true
                        }
                    })
            },
            MoreUP() {
                this.rbHttp
                    .sendRequest({
                        method: 'GET',
                        params: { type: 'pod_name' },
                        url: '/v1/cmdb/configDict/getDictsByType'
                    })
                    .then(res => {
                        this.podList = this.fillSelectOption(res, 'pod_name')
                        this.podList = this.podList.slice(0, 12)
                        this.moreIcon = !this.moreIcon
                        this.PODheight = false
                    })
            },
            MoreZ() {
                this.rbHttp
                    .sendRequest({
                        method: 'GET',
                        params: { type: 'idcType' },
                        url: '/v1/cmdb/configDict/getDictsByType'
                    })
                    .then(res => {
                        this.poolList = this.fillSelectOption(res, 'idcType')
                        this.moreIconZ = !this.moreIconZ
                        if (this.poolList.length > 9) {
                            this.PODheightZ = true
                        }
                    })
            },
            MoreUPZ() {
                this.rbHttp
                    .sendRequest({
                        method: 'GET',
                        params: { type: 'idcType' },
                        url: '/v1/cmdb/configDict/getDictsByType'
                    })
                    .then(res => {
                        this.poolList = this.fillSelectOption(res, 'idcType')
                        this.poolList = this.poolList.slice(0, 9)
                        this.moreIconZ = !this.moreIconZ
                        this.PODheightZ = false
                    })
            },
            MoreS() {
                this.rbHttp
                    .sendRequest({
                        method: 'GET',
                        params: { type: 'device_type', pValue: '网络设备', pType: 'device_class' },
                        url: '/v1/cmdb/configDict/getDictsByType'
                    })
                    .then(res => {
                        this.netDeviceTypeList = this.fillSelectOption(res, 'deviceType')
                        this.moreIconS = !this.moreIconS
                        if (this.netDeviceTypeList.length > 6) {
                            this.PODheightS = true
                        }
                    })
            },
            MoreUPS() {
                this.rbHttp
                    .sendRequest({
                        method: 'GET',
                        params: { type: 'device_type', pValue: '网络设备', pType: 'device_class' },
                        url: '/v1/cmdb/configDict/getDictsByType'
                    })
                    .then(res => {
                        this.netDeviceTypeList = this.fillSelectOption(res, 'deviceType')
                        this.netDeviceTypeList = this.netDeviceTypeList.slice(0, 6)
                        this.moreIconS = !this.moreIconS
                        this.PODheightS = false
                    })
            },
            // 获取模型列表
            queryModuleList() {
                let _t = this
                rbCmdbServiceFactory.getModuleTree('').then(data => {
                    // console.log('xinzeng', data[0].childModules)
                    _t.moduleList = data
                })
            },

            checkTime() {
                if (null == this.dateRange || null == this.dateRange[0] || '' == this.dateRange[0]) {
                    this.$message.warning('请选择时间，不能为空！')
                }
                let now1 = new Date(moment(this.dateRange[0]).valueOf())
                now1.setMonth(now1.getMonth() + 1)
                now1.setDate(0)
                let num = now1.getDate()
                let monthTime = 1000 * 60 * 60 * 24 * num
                if ((moment(this.dateRange[1]).valueOf() - moment(this.dateRange[0]).valueOf()) >= monthTime) {
                    this.$message.warning('时间范围请不要超过一个月!')
                    return false
                }
                return true
            },

            // 获取CI列表信息
            queryData: function () {
                let flagTime = this.checkTime()
                if (!flagTime) {
                    return
                }

                this.queryForm.pageNumber = this.paginationData.currentPage
                this.queryForm.pageSize = this.paginationData.pageSize
                if (this.insertTime !== '' && this.insertTime !== null) {
                    // this.queryForm.online_time=this.insertTime
                    this.queryForm.startInsertTime = this.insertTime[0]
                    this.queryForm.endInsertTime = this.insertTime[1]
                } else {
                    this.queryForm.startInsertTime = ''
                    this.queryForm.endInsertTime = ''
                }
                this.queryForm.sortColumn = this.paginationData.sort
                this.queryForm.sortType = this.paginationData.order
                this.queryForm.queryType = this.queryType || 'normal'
                this.queryForm.deviceClass = '网络设备'
                let param = {
                    sendTimeStart: this.dateRange[0],
                    sendTimeEnd: this.dateRange[1],
                    granularity: this.granularity
                }
                this.loading = true
                rbCmdbServiceFactory
                    .getNetInstanceList(this.queryForm, param)
                    .then(data => {
                        this.paginationData.total = data.totalSize
                        this.instanceDataList = data.data
                        this.columnList = data.columnList
                        if (this.multipleSelectionAll[this.paginationData.currentPage - 1]) {
                            this.$nextTick(() => {
                                this.multipleSelectionAll[
                                    this.paginationData.currentPage - 1
                                ].forEach(item => {
                                    this.tableData.forEach(data => {
                                        if (data.id === item.id) {
                                            this.$refs.table.toggleRowSelection(data, true)
                                        }
                                    })
                                })
                            })
                        }
                    })
                    .finally(() => {
                        this.loading = false
                    })
            },

            // 带条件查询
            searchInstanceList: function () {
                this.paginationData.currentPage = 1
                this.queryData()
            },

            // 获取已保存的自定义列
            getColumnFilter() {
                let _this = this
                rbCmdbServiceFactory
                    .getColumnFilter({
                        menuType: 'CMDB_INSTANCE',
                        moduleId: this.queryForm.moduleId || ''
                    })
                    .then(data => {
                        _this.dbFilterData = data
                        // 获取表格头信息
                    })
            },

            // 跳转到设备详情
            toDetail(instanceId) {
                if ((this.queryForm.moduleId || '') === '') {
                    let _t = this
                    rbCmdbServiceFactory.getModuleByInstanceId(instanceId).then(data => {
                        if (data) {
                            let queryParams = {
                                moduleId: data.module.id,
                                name: data.module.name,
                                instanceId: instanceId,
                                state: 'detail'
                            }
                            queryParams = JSON.stringify(queryParams)
                            this.$router.push({ path: '/resource/iframe/detail', query: { queryParams: queryParams } })

                        } else {
                            _t.$message.error('获取模型数据失败')
                        }
                    })
                } else {
                    let queryParams = {
                        moduleId: this.queryForm.moduleId,
                        instanceId: instanceId,
                        state: 'detail'
                    }
                    queryParams = JSON.stringify(queryParams)
                    this.$router.push({ path: '/resource/iframe/detail', query: { queryParams: queryParams } })
                }
            },

            // 跳转修改页面
            toUpdate(instanceId) {
                if ((this.queryForm.moduleId || '') === '') {
                    let _t = this
                    rbCmdbServiceFactory.getModuleByInstanceId(instanceId).then(data => {
                        if (data) {
                            this.$router.push({
                                path: '/resource/iframe/add',
                                query: {
                                    moduleId: data.module.id,
                                    instanceId: instanceId,
                                    state: 'update'
                                }
                            })
                        } else {
                            _t.$message.error('获取模型数据失败')
                        }
                    })
                } else {
                    this.$router.push({
                        path: '/resource/iframe/add',
                        query: {
                            moduleId: this.queryForm.moduleId,
                            instanceId: instanceId,
                            state: 'update'
                        }
                    })
                }
            },

            // 初始化字典值
            initDict() {
                // 获取所有的字典值
                this.rbHttp
                    .sendRequest({
                        method: 'GET',
                        url: '/v1/cmdb/configDict/getAll'
                    })
                    .then(res => {
                        const deviceClassOptions = []
                        const deviceTypeOptions = []
                        const deviceStatusOptions = []
                        if (res) {
                            this.allDictList = res
                            res.forEach(item => {
                                if (item.colName === 'device_class') {
                                    deviceClassOptions.push(this.convertDictConfig(item))
                                }
                                if (item.colName === 'device_type') {
                                    deviceTypeOptions.push(this.convertDictConfig(item))
                                }
                                if (item.colName === 'device_status') {
                                    deviceStatusOptions.push(this.convertDictConfig(item))
                                }
                            })

                            this.deviceTypeList = deviceTypeOptions
                            this.deviceStatusList = deviceStatusOptions
                        }
                    })
                // 获取网络设备类型
                this.rbHttp
                    .sendRequest({
                        method: 'GET',
                        params: { type: 'device_type', pValue: '网络设备', pType: 'device_class' },
                        url: '/v1/cmdb/configDict/getDictsByType'
                    })
                    .then(res => {
                        this.netDeviceTypeList = this.fillSelectOption(res, 'deviceType')
                        this.netDeviceTypeList = this.netDeviceTypeList.slice(0, 6)
                        if (this.netDeviceTypeList.length > 5) {
                            // this.PODheightS = true;
                            this.SHFL = true
                        }
                    })
                // 获取POD池
                this.rbHttp
                    .sendRequest({
                        method: 'GET',
                        params: { type: 'pod_name' },
                        url: '/v1/cmdb/configDict/getDictsByType'
                    })
                    .then(res => {
                        this.podList = this.fillSelectOption(res, 'pod_name')
                        this.podList = this.podList.slice(0, 13)

                        if (this.podList.length > 12) {
                            this.pod = true
                        }
                    })
                // 获取资源池
                this.rbHttp
                    .sendRequest({
                        method: 'GET',
                        params: { type: 'idcType' },
                        url: '/v1/cmdb/configDict/getDictsByType'
                    })
                    .then(res => {
                        this.poolList = this.fillSelectOption(res, 'idcType')
                        if (this.poolList.length > 9) {
                            this.PODheightZ = true
                            this.ZYC = true
                        }
                    })
                // 获取维保厂家


                let req = {
                    'params': { 'produce_type': '维保供应商' }
                }
                rbmaintenanceCommonUtil.getProducesByType(req).then((res) => {
                    const list = []
                    for (let i in res) {
                        list.push(res[i].produce_name)
                    }
                    this.maintenanceFactoryList = list
                    this.mfrFactoryList = list
                })

                /* this.rbHttp
        .sendRequest({
          method: "GET",
          params: { type: "mainten_factory" },
          url: `/v1/cmdb/configDict/getDictsByType`
        })
        .then(res => {
          this.maintenanceFactoryList = res;
        }); */
                // 获取品牌
                /* this.rbHttp
        .sendRequest({
          method: "GET",
          params: { type: "device_mfrs" },
          url: `/v1/cmdb/configDict/getDictsByType`
        })
        .then(res => {
          this.mfrFactoryList = res;
        }); */
                // 加载一级部门
                this.queryDepartment1()
                // 加载二级部门
                this.queryDepartment2()
                // 加载业务系统
                this.queryBizSystem()
            },
            getDataByTime(val) {
                this.timeRange = val
                this.setDate(val)
                this.searchInstanceList()
            },
            setGranularity(val) {
                this.granularity = val
                // this.searchInstanceList();
            },
            // 选择资源池
            pool(item) {
                this.queryForm.idcType = item
                this.searchInstanceList()
            },

            // 选择设备分类
            equipment(item) {
                this.queryForm.deviceType = item
                this.searchInstanceList()
                /* if (dictId) {
        this.rbHttp
          .sendRequest({
            method: "GET",
            params: { type: "device_type", pid: dictId, pType: "device_class" },
            url: `/v1/cmdb/configDict/getDictsByType`
          })
          .then(res => {
            this.deviceTypeList = res;
          });
      } else {
        const deviceTypeOptions = [];
        this.allDictList.forEach(item => {
          if (item.colName === "device_type") {
            deviceTypeOptions.push(this.convertDictConfig(item));
          }
        });
        this.deviceTypeList = deviceTypeOptions;
      } */
            },

            // 获取一级部门
            queryDepartment1: function () {
                this.rbHttp
                    .sendRequest({
                        method: 'GET',
                        params: { type: 'department1' },
                        url: '/v1/cmdb/configDict/getDictsByType'
                    })
                    .then(res => {
                        this.department1List = res
                    })
            },

            // 获取二级部门
            queryDepartment2: function (department1) {
                const params = { type: 'department2', pid: department1 || '' }
                this.rbHttp
                    .sendRequest({
                        method: 'GET',
                        params: params,
                        url: '/v1/cmdb/configDict/getDictsByType'
                    })
                    .then(res => {
                        this.department2List = res
                    })
            },

            // 获取业务系统
            queryBizSystem: function () {

                this.queryForm.bizSystem = ''
                this.bizSystemList = []
                let data = {
                    'department1': this.queryForm.department1 || '',
                    'department2': this.queryForm.department2 || ''
                }
                configDictServiceFactory.getBizSystem(data).then((res) => {
                    this.bizSystemList = res
                })

                /* this.rbHttp
        .sendRequest({
          method: "POST",
          data: { department1: this.queryForm.department1|| "", department2:  this.queryForm.department2|| "" },
          url: `/v1/cmdb/module/module/data?moduleType=default_business_system_module_id`
        })
        .then(res => {
          this.bizSystemList = res;
        }); */
            },

            // 选择一级部门
            changeDepartment1(department) {
                let departmentId = ''
                this.queryForm.department2 = ''
                this.queryForm.bizSystem = ''
                this.department1List.forEach(item => {
                    if (item.name === department) {
                        departmentId = item.id
                    }
                })
                this.searchInstanceList()
                this.queryDepartment2(departmentId)
                this.queryBizSystem(departmentId)
            },

            // 选择二级部门
            changeDepartment2(department) {
                let departmentId = ''
                this.queryForm.bizSystem = ''
                this.department2List.forEach(item => {
                    if (item.name === department) {
                        departmentId = item.id
                    }
                })
                this.queryBizSystem(departmentId)
                this.searchInstanceList()
            },

            // 下拉框change事件触发 查询CI列表
            changeQuery() {
                this.searchInstanceList()
            },
            changeTab() {
                this.searchInstanceList()
            },
            // 文本框blur离焦事件触发 查询CI列表
            blurQuery() {
                this.searchInstanceList()
            },

            // 选择资源池
            selectPod(item) {
                this.queryForm.pod = item
                this.searchInstanceList()
            },
            // input框展示
            showInput() {
                this.inputShow = !this.inputShow
                this.inputHome = !this.inputHome
                this.conditionShow = !this.conditionShow
            },
            // 清空筛选
            emptyScreening(needSearch) {
                this.insertTime = ''
                Object.keys(this.queryForm).forEach(item => {
                    this.queryForm[item] = ''
                })
                if (needSearch) {
                    this.searchInstanceList()
                }
            },

            // 将下拉框添加"全部"选项
            fillSelectOption(options, selectType) {
                const newOptions = [
                    { id: '', name: '全部', type: selectType, value: '', pid: '' }
                ]
                if (options) {
                    options.forEach(item => {
                        newOptions.push(item)
                    })
                }
                return newOptions
            },

            // 将获取到的字典值转化为可识别的json对象
            convertDictConfig(dict) {
                return {
                    id: dict.dictId,
                    type: dict.colName,
                    name: dict.dictCode,
                    value: dict.dictNote,
                    pid: dict.upDict
                }
            },

            // 是否显示列
            showColumn(keyCode) {
                if (keyCode === 'ip') {
                    return false
                }
                if (keyCode === 'device_name') {
                    return false
                }
                if (keyCode === 'insert_time') {
                    return false
                }
                if (keyCode === 'update_time') {
                    return false
                }
                return this.column_data[keyCode] === true
            },

            // 每页显示记录数发生变化事件
            handleSizeChange(size) {
                this.paginationData.pageSize = size
                var maxPage = Math.ceil(this.paginationData.total / size)
                if (this.paginationData.currentPage > maxPage) {
                    this.paginationData.currentPage = maxPage
                }
                this.queryData()
            },

            // 当前页数发生变化事件
            handleCurrentChange(val) {
                this.multipleSelectionAll[
                    this.paginationData.currentPage - 1
                ] = this.multipleSelection
                this.paginationData.currentPage = val
                this.queryData()
            },

            // 选中复选框时间
            handleSelectionChange(val) {
                this.multipleSelection = val
            },

            // 自定义表格列宽度
            customWidth: function (label) {
                let width = 120
                if (label.length > 8) {
                    width = 160
                } else if (label.length > 6) {
                    width = 140
                }
                return width
            },



            // 导出功能
            exportData() {
                let flagTime = this.checkTime()
                if (!flagTime) {
                    return
                }
                this.loading = true
                let param = {
                    sendTimeStart: this.dateRange[0],
                    sendTimeEnd: this.dateRange[1],
                    granularity: this.granularity
                }
                this.queryForm.pageNumber = null
                this.queryForm.pageSize = null
                if (this.insertTime !== '' && this.insertTime != null) {
                    // this.queryForm.online_time=this.insertTime
                    this.queryForm.startInsertTime = this.insertTime[0]
                    this.queryForm.endInsertTime = this.insertTime[1]
                } else {
                    this.queryForm.startInsertTime = ''
                    this.queryForm.endInsertTime = ''
                }
                this.queryForm.sortColumn = this.paginationData.sort
                this.queryForm.sortType = this.paginationData.order
                this.queryForm.queryType = this.queryType || 'normal'
                rbCmdbServiceFactory
                    .exporNettInstanceList(this.queryForm, param)
                    .then(data => {
                        let blob = new Blob([data], { type: 'application/vnd.ms-excel' })
                        let objectUrl = URL.createObjectURL(blob)
                        // window.location.href = objectUrl
                        let downLoadElement = document.createElement('a')
                        downLoadElement.href = objectUrl
                        downLoadElement.download = '网络性能列表.xlsx'
                        document.body.appendChild(downLoadElement)
                        downLoadElement.click()
                        document.body.removeChild(downLoadElement)
                        URL.revokeObjectURL(objectUrl)
                    })
                    .finally(() => {
                        this.loading = false
                    })
            },
            add() {
                this.$router.push({
                    path: '/resource/iframe/add',
                    query: {
                        moduleId: this.queryForm.moduleId,
                        deviceClass: this.queryForm.moduleId,
                        state: 'add'
                    }
                })
            },
            handleSelect(item, child) {
                this.$router.push({
                    path: '/resource/iframe/add',
                    query: {
                        moduleId: child.id,
                        deviceClass: item.name,
                        deviceType: child.name,
                        state: 'add'
                    }
                })
            }
        }
    }
</script>
<style lang="scss" scoped>
    @import "src/pages/resource/iframe/iframeHome/components/rem.scss";
    .active {
        background: #269be0;
        color: #fff;
    }
    .normal {
        background-color: #fff;
    }
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
                            width: 46px;
                            height: 17px;
                            margin-top: 10px;
                            border: 1px solid #dfe9fc;
                            display: flex;
                            justify-content: center;
                            align-items: center;
                        }
                        .moreClassRight {
                            position: absolute;
                            //   line-height: 30px;
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
                                width: 100px !important;
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
                            margin-left: 15px;
                            p {
                                display: inline-block;
                                width: 15%;
                                margin: 0 4px;
                                div.el-select {
                                    width: 100%;
                                }
                            }
                            div {
                                float: right;
                                cursor: pointer;
                                margin-right: 10px;
                                color: #2089da;
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
                    height: 35px;
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
