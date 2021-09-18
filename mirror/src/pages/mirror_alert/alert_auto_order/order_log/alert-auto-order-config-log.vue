<template>
    <div class="components-container yw-dashboard">
        <el-form class="yw-form components-condition"
                 label-width="75px"
                 :inline="true"
                 :model="formData">
            <el-form-item label="派单规则">
                <el-select v-model="formData.orderConfigId" clearable>
                    <el-option v-for="(item, index) in orderConfigList"
                               :key="index"
                               :label="item.configName"
                               :value="item.uuid">
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="告警设备">
                <el-input v-model="formData.deviceIp"></el-input>
            </el-form-item>
            <el-form-item label="归属资源池">
                <el-select v-model="formData.idcType"
                           placeholder="请选择"
                           clearable
                           filterable
                           multiple
                           collapse-tags
                           @change="changePool()"
                           @clear="cleanPool()">
                    <el-option v-for="item in resourcePools"
                               :key="item.value"
                               :label="item.name"
                               :value="item.value"></el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="告警等级">
                <el-select v-model="formData.alertLevel"
                           placeholder="请选择"
                           clearable
                           filterable
                           multiple
                           collapse-tags>
                    <el-option v-for="item in alert_level"
                               :key="item.value"
                               :label="item.label"
                               :value="item.value">
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="派单时间">
                <el-date-picker v-model="formData.orderTime"
                                type="daterange"
                                range-separator="至"
                                start-placeholder="开始日期"
                                end-placeholder="结束日期"
                                value-format="yyyy-MM-dd"
                                size="mini"></el-date-picker>
            </el-form-item>
            <el-form-item label="告警内容">
                <el-input v-model="formData.moniIndex"></el-input>
            </el-form-item>
            <el-form-item label="派单类型">
                <el-select v-model="formData.orderType"
                           placeholder="请选择"
                           clearable
                           filterable
                           multiple
                           collapse-tags>
                    <el-option v-for="item in order_type"
                               :key="item.value"
                               :label="item.label"
                               :value="item.value">
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="业务系统">
                <treeselect class="yw-treeselect" v-model="bizSysDepChecked"
                            :multiple="true"
                            :limit="1"
                            :options="bizDepTreeSelOpts"
                            placeholder="请选择" />
            </el-form-item>
            <el-form-item label="告警来源">
                <el-select v-model="formData.source"
                           placeholder="请选择"
                           multiple
                           collapse-tags
                           clearable
                           filterable>
                    <el-option v-for="item in alert_froms"
                               :key="item.value"
                               :label="item.label"
                               :value="item.value"></el-option>
                </el-select>
            </el-form-item>
            <section class="btn-wrap">
                <el-button type="primary"
                           @click="getAlertAutoOrderLogList()">查询
                </el-button>
                <el-button @click="reset()">重置</el-button>
            </section>
        </el-form>

        <el-form class="yw-form" v-loading="loading">
            <div class="table-operate-wrap clearfix">
                <el-button type="text"
                           icon="el-icon-plus"
                           @click="exportBatch()"
                           :disabled="exportDisabled">导出
                </el-button>
            </div>
            <div class="yw-el-table-wrap">
                <el-table class="yw-el-table"
                          :data="orderConfigLogList"
                          height="calc(100vh - 320px)"
                          stripe
                          border>
                    <el-table-column type="selection" width="40"></el-table-column>
                    <el-table-column label="规则名称"
                                     width="160"
                                     prop="configName"
                                     :show-overflow-tooltip="true">
                    </el-table-column>
                    <el-table-column label="告警归属设备"
                                     width="100"
                                     prop="deviceIp"
                                     :show-overflow-tooltip="true">
                    </el-table-column>
                    <el-table-column label="告警等级"
                                     width="80">
                        <template slot-scope="scope">
                            <rb-mirror-alert-status :mode="'list'"
                                                    :status="scope.row.alertLevel"></rb-mirror-alert-status>
                        </template>
                    </el-table-column>
                    <el-table-column label="告警内容"
                                     width="300"
                                     prop="moniIndex"
                                     :show-overflow-tooltip="true">
                    </el-table-column>
                    <el-table-column label="当前告警时间"
                                     width="140"
                                     prop="curMoniTime"
                                     :show-overflow-tooltip="true">
                    </el-table-column>
                    <el-table-column label="告警开始时间"
                                     width="140"
                                     prop="alertStartTime"
                                     :show-overflow-tooltip="true">
                    </el-table-column>
                    <el-table-column label="告警归属资源池"
                                     width="120"
                                     prop="idcType"
                                     :show-overflow-tooltip="true">
                    </el-table-column>
                    <el-table-column label="告警来源"
                                     width="80"
                                     prop="source"
                                     :show-overflow-tooltip="true">
                    </el-table-column>
                    <el-table-column label="业务系统"
                                     width="160"
                                     prop="bizSys"
                                     :show-overflow-tooltip="true">
                    </el-table-column>
                    <el-table-column label="机房"
                                     width="120"
                                     prop="sourceRoom"
                                     :show-overflow-tooltip="true">
                    </el-table-column>
                    <el-table-column label="告警设备名称"
                                     width="100"
                                     prop="hostName"
                                     :show-overflow-tooltip="true">
                    </el-table-column>
                    <el-table-column label="设备分类"
                                     width="100"
                                     prop="deviceClass"
                                     :show-overflow-tooltip="true">
                    </el-table-column>
                    <el-table-column label="设备类型"
                                     width="100"
                                     prop="deviceType"
                                     :show-overflow-tooltip="true">
                    </el-table-column>
                    <el-table-column label="派单时间"
                                     width="140"
                                     prop="orderTime"
                                     :show-overflow-tooltip="true">
                    </el-table-column>
                    <el-table-column label="派单类型"
                                     width="140"
                                     prop="orderType"
                                     :show-overflow-tooltip="true">
                    </el-table-column>
                    <el-table-column label="告警工单"
                                     width="140"
                                     :show-overflow-tooltip="true">
                        <template slot-scope="scope">
                            <span @click="gotoBPM(scope.row.orderId)"
                                  style="color:#0060DF">{{scope.row.orderId}}</span>
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
    import QueryObject from 'src/utils/queryObject.js'
    import rbAlertAutoOrderServicesFactory from 'src/services/alert/rb-alert-auto-order-services.factory.js'
    import rbProjectDataServiceFactory from 'src/services/template/rb-template-services.factory.js'
    import rbAlertKanBanServiceFactory from 'src/services/alert/rb-alert-kanban-services.factory.js'
    import '@riophae/vue-treeselect/dist/vue-treeselect.css'
    import {alert_from, alert_level,order_type} from '../../alert_his/config/options.js'
    export default {
        mixins: [QueryObject],
        components: {
            YwPagination: () => import('src/components/common/yw-pagination.vue'),
            rbMirrorAlertStatus: () => import('src/pages/mirror_alert/common/rb-mirror-alert-status.vue'),
            Treeselect: () => import('@riophae/vue-treeselect'),
        },
        data() {
            return {
                formData: {
                    orderConfigId: this.$route.query.orderConfigId ? this.$route.query.orderConfigId : '',
                    deviceIp: '',
                    idcType: '',
                    alertLevel: '',
                    orderTime: [],
                    moniIndex: '',
                    source: []
                },
                orderConfigList: [],
                resourcePools: [],
                bizSysList: [],
                alert_level: [],
                bizSysDepChecked: [],
                bizDepTreeSelOpts: [],
                alert_froms: [],
                order_type: [],
                bizSysTree: null,
                bizFlag: false,
                queryParams: {},
                exportDisabled: false,
                loading: false,
                initFlag: true,
                orderConfigLogList: [],
            }
        },
        created() {
            this.alert_froms = alert_from
            this.order_type = order_type
            this.getAlertAutoOrderConfigList()
            this.getResourcePoor()
            this.getBizSysTreeData()
            this.getAlertLevel()
            this.getAlertAutoOrderLogList()
        },
        methods: {
            initOrderConfigLog () {
            },
            getAlertLevel () {
                this.alert_level = alert_level
                for (let i=0;i<this.alert_level.length;i++) {
                    if (this.alert_level[i].label === '全部') {
                        this.alert_level.splice(i,1)
                    }
                }
            },
            getAlertAutoOrderConfigList () {
                let params = {}
                //                params.pageNum = 0
                //                params.pageSize = this.pageSize
                rbAlertAutoOrderServicesFactory.getAlertAutoOrderConfigList(params).then((res) => {
                    this.orderConfigList = res.result
                })
            },
            // 获取资源池
            getResourcePoor() {
                let obj = {
                    'type': 'idcType',
                    'pid': ''
                }
                rbProjectDataServiceFactory.getConfigDictByType(obj).then((res) => {
                    if (res) {
                        this.resourcePools = res
                    }
                })
            },
            changePool() {
                let _this = this
                _this.bizDepTreeSelOpts = []
                rbAlertKanBanServiceFactory.bizSysTreeOptionByResourcePollSel(this.formData.idcType || '').then(function (poolDeps) {
                    let sysDepts = (
                        _this.bizSysTree &&
                        _this.bizSysTree.length > 0 &&
                        _this.bizSysTree[0].children &&
                        _this.bizSysTree[0].children.length > 0
                    ) ? _this.bizSysTree[0].children : []
                    _.forEach(poolDeps, poolDep => {
                        if (poolDep) {
                            _(sysDepts).filter(sysdep => {
                                return poolDep === sysdep.id
                            }).forEach(subdep => {
                                _this.bizDepTreeSelOpts.push(subdep)
                            })
                        }
                    })
                })
            },
            cleanPool() {
                this.bizSysList = []
                this.search.system = ''
                this.getBizSysList()
            },
            // 获取业务系统
            getBizSysList() {
                let obj = {
                    'type': 'bizSystem',
                    'pid': '',
                    'pValue': '',
                    'pType': ''
                }
                rbProjectDataServiceFactory.getConfigDictByType(obj).then((res) => {
                    if (res) {
                        this.bizSysList = res
                    }
                })
            },
            getBizSysTreeData () {
                rbAlertKanBanServiceFactory.bizSysTreeOptions().then(res => {
                    if (!this.bizSysTree) {
                        this.bizSysTree = res
                    }
                    this.bizDepTreeSelOpts = res
                    this.bizFlag = true
                })
            },
            getBizSysByChecked () {
                let sysDepChecked = this.bizSysDepChecked
                if (sysDepChecked && sysDepChecked.length === 1 && !sysDepChecked[0]) {
                    this.queryParams.bizSystem = ''
                    return
                }
                if (!this.bizSysTree || this.bizSysTree.length === 0) {
                    this.queryParams.bizSystem = ''
                    return
                }
                let target = []
                let list = this.bizSysTree[0].children
                let addEle = function (eles) {
                    if (!eles.children || eles.children.length === 0) {
                        target.push(eles.id)
                    } else {
                        _.forEach(eles.children, ele => {
                            addEle(ele)
                        })
                    }
                }
                let findEle = function (_array, _origin) {
                    _.forEach(_array, child => {
                        if (child.id === _origin) {
                            addEle(child)
                        } else if (child.children && child.children.length > 0) {
                            findEle(child.children, _origin)
                        }
                    })
                }
                _(sysDepChecked).forEach(ele => {
                    findEle(list, ele)
                })
                this.queryParams.bizSystem = _(target).uniq().join(',')
            },
            exportBatch() {
                this.loading = true
                this.exportDisabled = true
                rbAlertAutoOrderServicesFactory.exportAlertAutoOrderLogList(this.getQueryParams(false)).then((res) => {
                    let blob = new Blob([res], { type: 'application/vnd.ms-excel' })
                    let objectUrl = URL.createObjectURL(blob)
                    let downLoadElement = document.createElement('a')
                    downLoadElement.href = objectUrl
                    downLoadElement.download = '告警派单记录.xlsx'
                    document.body.appendChild(downLoadElement)
                    downLoadElement.click()
                    document.body.removeChild(downLoadElement)
                    URL.revokeObjectURL(objectUrl)
                }).finally(() => {
                    this.loading = false
                    this.exportDisabled = false
                })
            },
            // 分页改变尺寸
            handleSizeChange(val) {
                this.pageSize = val
                this.getAlertAutoOrderLogList()
            },
            handleCurrentChange(val) {
                this.currentPage = val
                this.getAlertAutoOrderLogList()
            },
            getAlertAutoOrderLogList() {
                this.loading = true
                rbAlertAutoOrderServicesFactory.getAlertAutoOrderLogList(this.getQueryParams(true)).then((data) => {
                    this.orderConfigLogList = data.result
                    this.total = data.count
                }).finally(() => {
                    this.loading = false
                    this.initFlag = false
                })
            },
            // 设置参数
            getQueryParams(activePagination) {
                if (!this.initFlag) {
                    this.getBizSysByChecked()
                }
                //                if (activePagination) {
                //                    this.queryParams['pageNum'] = this.currentPage
                //                    this.queryParams['pageSize'] = this.pageSize
                //                }
                let obj = {
                    'orderStartTime': this.formData.orderTime ? this.formData.orderTime[0] : null,
                    'orderEndTime': this.formData.orderTime ? this.formData.orderTime[1] : null,
                    'orderRule': this.formData.orderConfigId,
                    'deviceIp': this.formData.deviceIp,
                    'idcType': this.formData.idcType ? this.formData.idcType.toString() : '',
                    'alertLevel': this.formData.alertLevel ? this.formData.alertLevel.toString() : '',
                    'alertDescription': this.formData.moniIndex,
                    'alertSource': (this.formData.source && this.formData.source.length > 0) ? this.formData.source.join(',') : '',
                    'orderType': (this.formData.orderType && this.formData.orderType.length > 0) ? this.formData.orderType.join(',') : '',
                    'pageNum': activePagination ? this.currentPage : null,
                    'pageSize':activePagination ? this.pageSize : null
                }
                return obj

            },
            reset() {
                this.formData = {}
            },
            gotoBPM: function (bpm_id) {
                const url = `${sessionStorage.getItem(
                    'X7_SERVER_URL'
                )}/front/#/inst/${bpm_id}?mirrorToken=${sessionStorage.getItem('mirror')}`
                window.open(url, '_blank')
            },
        }
    }
</script>

<style lang="scss" scoped>
</style>
