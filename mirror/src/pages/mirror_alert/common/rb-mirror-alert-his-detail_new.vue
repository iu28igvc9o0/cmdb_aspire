<template>
    <div class="components-container yw-dashboard">
        <el-collapse class="yw-dashboard-section"
                     v-model="activeCollapseNames">
            <el-collapse-item name="1">
                <template slot="title">
                    详情
                </template>
                <section class="operator-wrap">
                    <!-- <el-button icon="fa fa-location-arrow"
                     @click="work.workDialog=true"
                     v-if="detailType === 'alert'"
                     :disabled="is_work">派单</el-button>
          <el-button icon="el-icon-delete"
                     v-if="detailType === 'alert'"
                     @click="clean.cleanDialog=true">清除</el-button> -->
                    <el-button icon="el-icon-refresh"
                               @click="refresh()">刷新</el-button>
                </section>
                <el-form class="yw-form form-details"
                         :inline="true">
                    <!-- <el-form-item label="告警级别" v-if="this.detailShowlist.length>0">
              <rb-mirror-alert-status :mode="'detail'"
                                    :status="obj.alert_level"></rb-mirror-alert-status>
          </el-form-item> -->
                    <el-form-item v-for="(item,index) in detailShowlist"
                                  :key="index"
                                  :label="item.detailShowName">
                        <a v-if="item.fieldCode === 'order_id'"
                           style="text-decoration: none;cursor:pointer"
                           @click="gotoBPM(obj[item.fieldCode])">{{obj[item.fieldCode]}}</a>
                        <span v-else-if="item.detailShowName === '告警级别'">
                            <rb-mirror-alert-status :mode="'detail'"
                                                    :status="item.alertDetail.alert_level"></rb-mirror-alert-status>
                        </span>
                        <span v-else-if="item.dataType === 'datetime'">
                            {{obj[item.fieldCode] | formatDate}}
                        </span>
                        <span v-else>
                            <span v-if="Object.keys(obj).indexOf(item.fieldCode) !== -1">{{item.alertDetail[item.fieldCode]}}</span>&nbsp;&nbsp;&nbsp;&nbsp;
                            <a v-if="item.fieldCode === 'remark'"
                               style="padding-left: 10px"
                               @click="change.remarkChangeDialog=true">[修改]</a>
                            <a v-else-if="item.fieldCode === 'host_name'  && item.alertDetail.device_id"
                               style="text-decoration: none;cursor:pointer"
                               @click="toDeviceDetail()">设备详情</a>
                        </span>

                    </el-form-item><br />
                </el-form>
            </el-collapse-item>
            <el-collapse-item name="2">
                <template slot="title">
                    记录
                </template>

                <el-tabs class="yw-tabs"
                         v-model="activeName"
                         @tab-click="handleClick">
                    <el-tab-pane label="衍生告警"
                                 name="six"
                                 v-if="obj.source === '衍生告警'">
                        <rb-mirror-alert-derive-table v-if="obj.source === '衍生告警' && tableList.six.flag"
                                                      status="history"
                                                      ref="rbMirrorAlertDeriveTable"
                                                      :alertId="obj.alert_id"></rb-mirror-alert-derive-table>
                    </el-tab-pane>
                    <el-tab-pane label="告警上报记录"
                                 name="first">
                        <rb-mirror-alert-gen-table v-if="tableList.first.flag"
                                                   ref="rbMirrorAlertGenTable"
                                                   :obj="obj"
                                                   :type="detailType"></rb-mirror-alert-gen-table>
                    </el-tab-pane>
                    <el-tab-pane label="告警操作记录"
                                 name="second">
                        <rb-mirror-alert-record-table v-if="tableList.second.flag"
                                                      ref="rbMirrorAlertRecordTable"
                                                      :obj="obj"></rb-mirror-alert-record-table>
                    </el-tab-pane>
                    <el-tab-pane label="告警通知记录"
                                 name="third">
                        <rb-mirror-alert-notify-table v-if="tableList.third.flag"
                                                      ref="rbMirrorAlertNotifyTable"
                                                      :obj="obj"></rb-mirror-alert-notify-table>
                    </el-tab-pane>
                    <el-tab-pane label="历史告警记录"
                                 name="four"
                                 v-if="detailType === 'alert'">
                        <rb-mirror-alert-his-table v-if="tableList.four.flag"
                                                   ref="rbMirrorAlertHisTable"
                                                   :obj="obj"></rb-mirror-alert-his-table>
                    </el-tab-pane>
                    <el-tab-pane label="监控信息"
                                 name="five"
                                 v-if="obj.source === 'ZABBIX'">
                        <rb-mirror-alert-trigger-table v-if="tableList.five.flag"
                                                       ref="rbMirrorAlertTriggerTable"
                                                       :obj="obj"></rb-mirror-alert-trigger-table>
                    </el-tab-pane>

                    <el-tab-pane label="sysLog"
                                 name="seven"
                                 v-if="obj.device_class === '网络设备'">
                        <rb-mirror-sys-log-table v-if="obj.device_class === '网络设备' && tableList.seven.flag"
                                                 :obj="obj"></rb-mirror-sys-log-table>
                    </el-tab-pane>
                    <el-tab-pane label="关联关系"
                                 name="eight"
                                 v-if="obj.object_type === '设备'">
                        <portRelation v-if="tableList.eight.flag"
                                      :deviceId="obj.device_id"></portRelation>
                    </el-tab-pane>
                    <el-tab-pane label="设备配置及维保信息"
                                 name="nine"
                                 v-if="obj.object_type === '设备'">
                        <rb-mirror-alert-device v-if="tableList.nine.flag"
                                                :instanceDetail="instanceDetail"></rb-mirror-alert-device>
                    </el-tab-pane>
                    <el-tab-pane label="次要告警"
                                 name="ten"
                                 v-if="obj.r_alert_id && obj.r_alert_id.indexOf('primary_') === 0">
                        <rb-mirror-alert-secondary-table v-if="tableList.ten.flag"
                                                         ref="rbMirrorAlertSecondaryTable"
                                                         :alertId="obj.alert_id"></rb-mirror-alert-secondary-table>
                    </el-tab-pane>
                    <el-tab-pane label="影响范围"
                                 name="ele">
                        <rb-mirror-alert-influence v-if="tableList.ele.flag"
                                                   :alertId="obj.alert_id"
                                                   :source="obj.source"></rb-mirror-alert-influence>
                    </el-tab-pane>

                </el-tabs>
            </el-collapse-item>
        </el-collapse>
        <el-dialog class="yw-dialog"
                   title="修改"
                   :visible.sync="change.remarkChangeDialog"
                   width="500px"
                   :close-on-click-modal="false"
                   :close-on-press-escape="false">
            <section class="yw-dialog-main">
                <el-form class="yw-form"
                         label-width="82px">
                    <el-form-item label="修改内容">
                        <el-input type="textarea"
                                  :rows="5"
                                  v-model="change.remarkChangeContent"></el-input>
                    </el-form-item>
                </el-form>
            </section>
            <section class="btn-wrap">
                <el-button type="primary"
                           @click="submitChange()">确定</el-button>
                <el-button @click="cancel('change')">取消</el-button>
            </section>
        </el-dialog>
    </div>
</template>

<script>
    import { alert_notice_types } from 'src/pages/mirror_alert/alert/config/options.js'
    import rbAlertServiceFactory from 'src/services/alert/rb-alert-services.factory.js'
    import rbMirrorCommonService from 'src/services/mirror/rb-mirror-common-services.factory.js'
    import rbMirrorAlertStatus from 'src/pages/mirror_alert/common/rb-mirror-alert-status.vue'
    import rbAlertKanBanServiceFactory from 'src/services/alert/rb-alert-kanban-services.factory.js'
    import rbMirrorAlertNotifyTable from 'src/pages/mirror_alert/common/rb-mirror-alert-notify-table.vue'
    import rbMirrorAlertGenTable from 'src/pages/mirror_alert/common/rb-mirror-alert-gen-table.vue'
    import rbMirrorAlertRecordTable from 'src/pages/mirror_alert/common/rb-mirror-alert-record-table.vue'
    import rbMirrorAlertTriggerTable from 'src/pages/mirror_alert/common/rb-mirror-alert-trigger-table.vue'
    import rbCmdbServiceFactory from 'src/services/cmdb/rb-cmdb-service.factory.js'
    import rbMirrorAlertHisTable from 'src/pages/mirror_alert/common/rb-mirror-alert-his-table.vue'
    import rbMirrorAlertDeriveTable from 'src/pages/mirror_alert/common/rb-mirror-alert-derive-table.vue'
    import rbMirrorSysLogTable from 'src/pages/mirror_alert/common/rb-mirror-sys-log-table.vue'
    import portRelation from 'src/pages/resource/iframe/detail/portRelation.vue'
    import rbMirrorAlertInfluence from 'src/pages/mirror_alert/common/rb-mirror-alert-influence-table.vue'

    import rbMirrorAlertDevice from 'src/pages/mirror_alert/common/rb-mirror-alert-device.vue'
    import rbMirrorAlertSecondaryTable from 'src/pages/mirror_alert/common/rb-mirror-alert-secondary-table'
    import { formatDate } from 'src/assets/js/utility/rb-filters.js'
    export default {
        components: {
            rbMirrorAlertStatus,
            rbMirrorAlertNotifyTable,
            rbMirrorAlertGenTable,
            rbMirrorAlertRecordTable,
            rbMirrorAlertTriggerTable,
            rbMirrorAlertHisTable,
            rbMirrorAlertDeriveTable,
            rbMirrorSysLogTable,
            portRelation,
            rbMirrorAlertDevice,
            rbMirrorAlertSecondaryTable,
            rbMirrorAlertInfluence
        },
        props: [
            'detailType', 'alertId',
            'modeNameList'
        ],
        data() {
            return {
                detailShowlist: [], // 详情展示字段

                activeCollapseNames: ['1', '2'],
                obj: {},
                note_total_count: 0,
                is_work: true,
                is_clean: false,
                activeName: 'first',
                module: '',
                alert_notice_type: '全部',
                alert_notice_types: [],
                alert_gen_list: [],
                alert_record_list: [],
                alert_notify_list: [],
                alert_notify_list_remaek: [],
                // 告警派单
                work: {
                    workDialog: false,
                    work_type: ''
                },
                // 告警清除
                clean: {
                    cleanDialog: false,
                    cleanDialogTextArea: ''
                },
                cleanRules: {
                    cleanDialogTextArea: [
                        { required: true, message: '不能为空', trigger: 'blur' }
                    ]
                },
                change: {
                    remarkChangeDialog: false,
                    remarkChangeContent: ''
                },
                monit: {
                    dialog: false,
                    device_ip: '', // 设备IP
                    agent_ip: '', // 归属代理IP
                    monit_obj: '', // 监控对象
                    metric_name: '', // 监控指标名称
                    metric_key: '', // 监控指标key
                    monit_period: '', // 监控周期
                    triggers: [], // 触发器信息
                    metrics: [] // 监控项数据
                },
                isDeviceDetailShow: true,
                orderType: 1,
                instanceDetail: {},
                tableList: {
                    first: {
                        flag: true,
                    },
                    second: {
                        flag: false,
                    },
                    third: {
                        flag: false,
                    },
                    four: {
                        flag: false,
                    },
                    five: {
                        flag: false,
                    },
                    six: {
                        flag: false,
                    },
                    seven: {
                        flag: false,
                    },
                    eight: {
                        flag: false,
                    },
                    nine: {
                        flag: false,
                    },
                    ten: {
                        flag: false,
                    },
                    ele: {
                        flag: false,
                    },
                },
            }
        },
        mounted() {
            this.alert_notice_types = alert_notice_types
            //    this.alertDetailInit()
        },
        methods: {
            gotoBPM(bpm_id) {
                const url = `${sessionStorage.getItem(
                    'X7_SERVER_URL'
                )}/front/#/inst/${bpm_id}?mirrorToken=${sessionStorage.getItem('mirror')}`
                window.open(url, '_blank')
            },
            handleClick(tab) {
                this.tableList[tab.name].flag = true
                Object.keys(this.tableList).forEach((item) => {
                    if (item !== tab.name) {
                        this.tableList[item].flag = false
                    }
                })
            },
            refresh() {
                this.refreshGen()
            },
            refreshGen() {
                this.tableList.first.flag = false // 销毁组件
                this.$nextTick(() => {
                    this.tableList.first.flag = true // 重建组件
                })
                this.tableList.second.flag = false // 销毁组件
                this.$nextTick(() => {
                    this.tableList.second.flag = true // 重建组件
                })
                this.tableList.third.flag = false // 销毁组件
                this.$nextTick(() => {
                    this.tableList.third.flag = true // 重建组件
                })
                this.tableList.four.flag = false // 销毁组件
                this.$nextTick(() => {
                    this.tableList.four.flag = true // 重建组件
                })
                this.tableList.five.flag = false // 销毁组件
                this.$nextTick(() => {
                    this.tableList.five.flag = true // 重建组件
                })
                this.tableList.six.flag = false // 销毁组件
                this.$nextTick(() => {
                    this.tableList.six.flag = true // 重建组件
                })
                this.tableList.seven.flag = false // 销毁组件
                this.$nextTick(() => {
                    this.tableList.seven.flag = true // 重建组件
                })
                this.tableList.eight.flag = false // 销毁组件
                this.$nextTick(() => {
                    this.tableList.eight.flag = true // 重建组件
                })
                this.tableList.nine.flag = false // 销毁组件
                this.$nextTick(() => {
                    this.tableList.nine.flag = true // 重建组件
                })
                this.tableList.ten.flag = false // 销毁组件
                this.$nextTick(() => {
                    this.tableList.ten.flag = true // 重建组件
                })
                this.tableList.ele.flag = false // 销毁组件
                this.$nextTick(() => {
                    this.tableList.ele.flag = true // 重建组件
                })
            },
            alertDetailInit() {
                if (this.detailType === 'alert') {
                    this.getAlertDetail(this.alertId)
                } else if (this.detailType === 'alertHis') {
                    this.getAlertHisDetail(this.alertId)
                }
            },

            // 当前告警详情
            async getAlertDetail(id) {
                await this.rbHttp.sendRequest({
                    method: 'GET',
                    url: '/v2/alerts/alert_his/detail/' + id
                }).then((res) => {
                    this.obj = res
                    if (this.obj.object_type == '1') {
                        this.obj.object_type = '设备'
                    } else if (this.obj.object_type == '2') {
                        this.obj.object_type = '业务系统'
                    } else if (this.obj.object_type == '3') {
                        this.obj.object_type = '机柜'
                    }
                })

                // 模型字段接口详情展示字段
                this.rbHttp.sendRequest({
                    method: 'GET',
                    url: '/v2/alerts/model/field/listByTableName/alert_alerts_his'
                }).then((res) => {
                    // 列表头部展示数据
                    let objList = res.filter((item) => {
                        return item.isDetailShow == 1
                    })

                    this.detailShowlist = objList.map(item => {
                        return Object.assign(item, { alertDetail: this.obj })
                    })

                    this.detailShowlist.forEach(item => {

                        item.alertDetail.cur_moni_time = formatDate(item.alertDetail.cur_moni_time)
                        item.alertDetail.alert_start_time = formatDate(item.alertDetail.alert_start_time)
                        item.alertDetail.create_time = formatDate(item.alertDetail.create_time)
                        item.alertDetail.clear_time = formatDate(item.alertDetail.clear_time)
                        return this.detailShowlist
                    })
                    console.log(this.detailShowlist)
                    let array = this.detailShowlist
                    array.sort(function (a, b) { // 数组对象方法排序:
                        return a.detailShowSort - b.detailShowSort
                    })

                })

                // for(item in this.detailShowlist) {
                //     if(Object.keys(this.obj).indexOf(detailShowlist[item].fieldCode) !== 1) {
                //       this.detailShowlist = arr.map(item=>{
                //           return Object.assign(item,this.obj)
                //     })
                // }
                // }

            },

            getAlertHisDetail(id) {
                rbAlertServiceFactory.getHisAlertDetail(id).then((res) => {
                    this.note_total_count = parseInt(res['note_success_count']) + parseInt(res['note_fall_count'])
                    this.obj = res
                    this.alert_gen_list = this.obj.alert_gen_list
                    this.alert_record_list = this.obj.alert_record_list
                    this.alert_notify_list = this.obj.alert_notify_list
                    this.alert_notify_list_remaek = this.obj.alert_notify_list
                    if (this.obj.order_status === '1') this.is_work = false
                    this.isDeviceDetailShow = this.obj.object_type === '1'
                    this.obj.order_status = rbMirrorCommonService.common('ORDERSTATUS', '1', this.obj.order_status)
                    this.obj.object_type = rbMirrorCommonService.common('OBJECTTYPE', '1', this.obj.object_type)
                    this.obj.order_type = rbMirrorCommonService.common('ORDERTYPE', '1', this.obj.order_type)
                    if (res.device_id) this.getModuleByInstanceId(res.device_id)
                    this.obj.clear_time = res.alert_end_time ? res.alert_end_time : res.clear_time
                })
            },
            selectChange() {
                let list = []
                if (this.alert_notice_type !== '2') {
                    let val = rbMirrorCommonService.common('REPORTTYPE', '1', this.alert_notice_type)
                    this.alert_notify_list_remaek.forEach(item => {
                        if (item.report_type === val) {
                            list.push(item)
                        }
                    })
                    this.alert_notify_list = list
                } else if (this.alert_notice_type === '2') {
                    this.alert_notify_list = []
                    this.alert_notify_list = this.alert_notify_list_remaek
                }
            },
            startWork() {
                if (this.is_work === false) {
                    this.work.workDialog = true
                }
            },
            submitWork() {
                this.$confirm('确认派单？').then(() => {
                    const namespace = sessionStorage.getItem('namespace')
                    let obj = {
                        'namespace': namespace,
                        'order_type': this.orderType,
                        'alert_ids': this.obj.alert_id
                    }
                    rbAlertKanBanServiceFactory.alertHandle(obj).then((res) => {
                        let successNum = res.substring(8)
                        let message = ''
                        let type = ''
                        if (successNum !== 0) {
                            message = '派单成功: ' + successNum
                            type = 'success'
                        } else {
                            message = '派单失败'
                            type = 'error'
                        }
                        this.$message({
                            message: message,
                            type: type
                        })
                        if (type === 'success') {
                            this.getDetail()
                        }
                    }).catch(() => {
                        this.$message.error('派单失败')
                    })
                })
                this.cancel('work')
            },
            // 开始清除
            startClean() {
                if (this.is_clean === false) {
                    this.clean.cleanDialog = true
                }
            },
            submitClean() {
                this.$refs['formClean'].validate((valid) => {
                    if (valid) {
                        const namespace = sessionStorage.getItem('namespace')
                        let obj = {
                            'namespace': namespace,
                            'alert_ids': this.obj.alert_id,
                            'content': this.clean.cleanDialogTextArea,
                            'auto_type': -1,
                            'start_time': '',
                            'end_time': ''
                        }
                        rbAlertKanBanServiceFactory.deleteAlert(obj).then((res) => {
                            if (res) {
                                this.$message({
                                    message: '删除成功',
                                    type: 'success'
                                })
                                this.goBack()
                            }
                        }).catch(() => {
                            this.$message.error('删除失败')
                        })
                    } else {

                        return false
                    }
                })
                this.cancel('clean')
            },
            submitChange() {
                alert(this.detailType)
                if (this.detailType === 'alert') {
                    this.changeAlert()
                } else if (this.detailType === 'alertHis') {
                    this.changeAlertHis()
                }
            },
            changeAlert() {
                rbAlertServiceFactory.changeRemarkContent(this.obj.alert_id, this.change.remarkChangeContent).then((res) => {
                    if (res === 'success') {
                        this.cancel('change')
                        this.$message({
                            message: '修改成功!',
                            type: 'success'
                        })
                        this.alertDetailInit()
                    } else {
                        this.$message.error('修改失败!')
                    }
                })
            },
            changeAlertHis() {
                rbAlertServiceFactory.changeHisAlertRemarkContent(this.obj.alert_id, this.change.remarkChangeContent).then((res) => {
                    if (res === 'success') {
                        this.cancel('change')
                        this.$message({
                            message: '修改成功!',
                            type: 'success'
                        })
                        this.alertDetailInit()
                    } else {
                        this.$message.error('修改失败!')
                    }
                })
            },
            cancel(obj) {
                if (obj === 'work') {
                    this.work.workDialog = false
                    //          this.work.work_type = '0'
                } else if (obj === 'clean') {
                    this.clean.cleanDialog = false
                    this.clean.cleanDialogTextArea = ''
                } else if (obj === 'change') {
                    this.change.remarkChangeDialog = false
                    this.change.remarkChangeContent = ''
                }
            },
            // 导出

            resourceExport(module) {
                let excelName = ''
                if (module === 'gen') {
                    excelName = '告警上报数据列表.xls'
                } else if (module === 'record') {
                    excelName = '告警操作数据列表.xls'
                } else if (module === 'notify') {
                    excelName = '告警通知数据列表.xls'
                }
                rbAlertKanBanServiceFactory.ExportCorfGridData({ 'alert_id': this.obj.alert_id, 'module': module }).then((res) => {
                    let blob = new Blob([res], { type: 'application/vnd.ms-excel' })
                    let objectUrl = URL.createObjectURL(blob)
                    let downLoadElement = document.createElement('a')
                    downLoadElement.href = objectUrl
                    downLoadElement.download = excelName
                    document.body.appendChild(downLoadElement)
                    downLoadElement.click()
                    document.body.removeChild(downLoadElement)
                    URL.revokeObjectURL(objectUrl)
                })
            },
            goBack() {
                this.$router.push({
                    path: '/mirror_alert/alert/manage',
                    query: {
                        alertType: 'alert_detail',
                    }
                })
            },
            toDeviceDetail() {
                this.rbHttp.sendRequest({
                    method: 'POST',
                    params: {
                        'token': '5245ed1b-6345-11e',
                        'condicationCode': 'alert_query_ci_detail',
                        'id': this.obj.device_id
                    },
                    url: '/v1/cmdb/restful/common/instance/detail'
                }).then((data) => {
                    if (data.module_id) {
                        let queryParams = {
                            moduleId: data.module_id,
                            // name: data.module.name,
                            instanceId: this.obj.device_id,
                            state: 'detail'
                        }
                        queryParams = JSON.stringify(queryParams)
                        this.$router.push({ path: '/resource/iframe/detail', query: { queryParams: queryParams } })
                    } else {
                        this.$message.error('获取模型数据失败')
                    }
                })
            },
            getModuleByInstanceId(instanceId) {
                rbCmdbServiceFactory.getModuleByInstanceId(instanceId).then((data) => {
                    this.getInstanceDetail(instanceId, data.module.code)
                })
            },
            getInstanceDetail(instanceId, code) {
                let params = {
                    moduleCode: code
                }
                rbCmdbServiceFactory.getFullInstance(instanceId, params).then((data) => {
                    this.instanceDetail = data
                })
            },
        },
        watch: {
            alertId: {
                handler: function () {
                    if (this.detailType) {
                        this.alertDetailInit()
                    }
                },
                immediate: true
            }
        }
    }
</script>

<style lang="scss" scoped>
    .form-details {
        .el-form-item {
            width: 24%;
        }
    }
    .form-details .el-form-item__label {
        width: 100px;
    }
</style>

