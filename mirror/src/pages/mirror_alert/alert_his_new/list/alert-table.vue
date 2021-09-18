<template>
    <div>
        <el-form class="yw-form">
            <div class="table-operate-wrap clearfix">
                <section class="fr">
                    <el-button class="btn-icons-wrap"
                               v-show="lockScgreen"
                               @click="lock">锁屏</el-button>
                    <el-button class="btn-icons-wrap"
                               v-show="goScgreen"
                               @click="go">刷屏</el-button>
                    <!-- <el-button class="btn-icons-wrap" v-popover:popover>选择列&nbsp;&nbsp;&nbsp;<i class="el-icon-arrow-down"></i></el-button> -->
                    <el-popover ref="popover"
                                placement="top"
                                trigger="click">
                        <div id="popover"
                             style="max-height: 300px;overflow-y: auto;">
                            <el-checkbox-group v-model="test"
                                               @change="handleCheckedColumnChange">
                                <div v-for="(column,index) in checkColumns"
                                     :key="index">
                                    <el-checkbox :label="column.keyCode">{{ column.keyName }}</el-checkbox>
                                </div>
                            </el-checkbox-group>
                        </div>
                    </el-popover>
                </section>
                <el-button class="btn-icons-wrap"
                           @click="resourceExport"
                           :disabled="exportDisabled">导出</el-button>
            </div>
            <!-- 告警列表 -->
            <div class="yw-el-table-wrap ywTabBorder">
                <el-table border
                          class="yw-el-table auto-height"
                          :data="activityAlertData"
                          style="cursor: pointer;"
                          stripe
                          tooltip-effect="dark"
                          v-loading="loading"
                          @selection-change="handleSelectionChange"
                          @row-dblclick="dblhandleCurrentChange($event)"
                          :header-cell-style="{background:'#DEE9FC',color:'#53607E'}">
                    <!-- <el-table-column type="selection" width="40px"></el-table-column> -->
                    <!-- <el-table-column label="级别" width="100px" fixed :sortable="true" :sort-method="sortByAlertLevel">
            <template slot-scope="scope">
              <rb-mirror-alert-status :mode="'list'" :status="scope.row.alert_level"></rb-mirror-alert-status>
            </template>
          </el-table-column>
          <el-table-column
            v-if="itemData.isListShow != '0'"
            v-for="itemData in modelFieldDataList"
            :key="itemData.fieldCode"
            :label="itemData.name"
            :prop="itemData.code"
            width="300px"
            sortable
            :show-overflow-tooltip="true"
            :width="flexColumnWidth(itemData.keyCode)"
          ></el-table-column> -->
                    <el-table-column v-for="(itemData, index) in modelFieldDataList"
                                     :key="itemData.fieldCode"
                                     :label="itemData.listShowName"
                                     :prop="itemData.fieldCode"
                                     sortable
                                     :fixed='index < 2'
                                     :show-overflow-tooltip="true"
                                     :width="((itemData.tableColumnWidth && itemData.tableColumnWidth > 0) ? itemData.tableColumnWidth : '200') + 'px'">
                        <template slot-scope="scope">
                            <rb-mirror-alert-status width="100px"
                                                    v-if="itemData.fieldCode === 'alert_level'"
                                                    :mode="'list'"
                                                    :status="scope.row.alert_level"></rb-mirror-alert-status>
                            <span v-else-if="itemData.fieldCode === 'order_id'"
                                  @click="gotoBPM(scope.row.order_id)"
                                  style="color:#0060DF">{{scope.row.order_id}}</span>
                            <span v-else>{{scope.row[itemData.fieldCode]}}</span>
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
    // import { alert_level, alert_type,relation, alert_from} from '../list/config/config.js'
    // import rbProjectDataServiceFactory from 'src/services/template/rb-template-services.factory.js'
    import rbMirrorCommonService from 'src/services/mirror/rb-mirror-common-services.factory.js'
    import rbAlertKanBanServiceFactory from 'src/services/alert/rb-alert-kanban-services.factory.js'
    import { formatDate } from 'src/assets/js/utility/rb-filters.js'
    import rbMirrorAlertStatus from 'src/pages/mirror_alert/common/rb-mirror-alert-status.vue'
    import rbConfigDictServiceFactory from 'src/services/cmdb/rb-configDict-service.factory.js'
    import rbMirrorAlertUseList from 'src/pages/mirror_alert/common/rb-mirror-alert-use-list.vue'
    import rbMirrorAlertNum from 'src/pages/mirror_alert/common/rb-mirror-alert-num.vue'
    import rbAlertFilterServicesFactory from 'src/services/alert/rb-alertFilter-services.factory.js'
    import Treeselect from '@riophae/vue-treeselect'
    import '@riophae/vue-treeselect/dist/vue-treeselect.css'
    import rbMirrorAlertVoiceNotify from 'src/pages/mirror_alert/common/rb-mirror-alert-voice-notify.vue'
    import rbAlertVoiceNotifyServicesFactory from 'src/services/alert/rb-alert-voice-notify-services.factory.js'
    import QueryObject from 'src/utils/queryObject.js'
    export default {
        mixins: [QueryObject],
        name: 'MirrorAlertAlertList',
        components: {
            rbMirrorAlertStatus,
            rbMirrorAlertNum,
            rbMirrorAlertUseList,
            Treeselect,
            rbMirrorAlertVoiceNotify,
            YwPagination: () => import('src/components/common/yw-pagination.vue')
        },
        props: ['alertType', 'alertLevel_default', 'queryDatas', 'queryDatasFile', 'modelFieldData'],
        data() {
            return {
                queryDatasList: {},
                queryDataParameterHis: {},
                modelFieldDataList: [], // 动态生成列 头部
                queryDataParameterFile: {},
                queryDataParameter: {
                    page_no: 1,
                    page_size: 50,
                    sort_name: '',
                    list: [

                    ]
                },
                cleanRules: {
                    cleanDialogTextArea: [
                        { required: true, message: '不能为空', trigger: 'blur' }
                    ]
                },
                loading: true,
                bizSysTree: null,
                bizSysDepChecked: [],
                bizDepTreeSelOpts: [],
                sceneId: '',
                filterFlag: false,
                filterCondition: '',
                filterData: [],
                filterId: '',
                filterSceneData: [],

                classA: 'classA',
                classB: 'classB',
                alertList: [],
                // 选择列存放的值
                filterForm: {},
                columnInfo: '',
                checkedcolumns: [],
                moduleId: '12345XYUEFKSLDDLDKFJAL',
                column_data: {},
                columnList: [],
                detailObjId: '',
                detailOrderStatus: '',
                // 多选框模板存放的值
                multipleSelection: [],
                activityAlertData: [], // 活动告警数据数组
                currentPage: 1, // 当前页
                pageSize: 50, // 分页每页多少行数据
                pageSizes: [20, 50, 100], // 每页多少行数组
                total: 0, // 总共多少行数据
                deviceList: [], // 查询出的设备
                lockScgreen: true,
                goScgreen: false,
                //  device_relation_value: '',
                //  monitor_relation_value: '',
                device_device: this.$route.query.deviceIp || '', // 包含的设备
                monitor_device: '', // 监控项的设备
                monitor_value: [],
                monitor_values: [],
                // 告警级别
                alert_level_value: '',
                alert_level: '',
                // 告警类型
                object_type: '',
                queryType: '',
                // 设备关系
                relation: '',
                // 业务系统
                bizSysList: [],
                // 资源池
                resourcePoors: [],
                // 机房
                idc_locations: [],
                // 设备类型
                device_types: [],
                // 设备分类
                device_classs: [],
                device_typess: [],
                alert_froms: [],
                device_mfrss: [],

                userResult: [],

                dicts: {},
                timer_frequency: 60000,
                exportDisabled: false,
                voiceNotifyDialog: false,
                alertVoiceNotifyDetail: {},
                orderType: 1
            }
        },
        mounted() {
            this.initAlertSum()
            this.init()
            // this.initFilter()
        },
        destroyed() {
            clearInterval(this.timer)
            clearInterval(this.voiceTimer)
            this.timer = null
            this.voiceTimer = null
        },
        methods: {
            getFilterData(condition, sceneId) {
                this.sceneId = sceneId
                this.filterFlag = true
                this.filterCondition = condition
                this.searchFilterData()
            },
            searchFilterData(valFile) {
                rbAlertFilterServicesFactory.getFilterData(valFile).then(res => {
                    this.activityAlertData = this.parseTableData(
                        this.packData(res.result)
                    )
                    this.total = res.count
                    this.$emit('sendTotal', this.total, this.currentPage, this.pageSize)
                    let sceneId = this.sceneId
                    if (this.filterSceneData.length > 0) {
                        let currentScene = this.filterSceneData.find(function (o) {
                            return o.sceneId === sceneId
                        })
                        if (currentScene !== undefined && currentScene !== null) {
                            currentScene.count = this.total
                            this.$forceUpdate()
                        }
                    }
                })
                    .catch(() => {
                        this.$message.error('查询失败')
                    })
            },
            // initFilter() {
            //     rbAlertFilterServicesFactory.getAll(true).then(res => {
            //         this.filterData = res
            //     })
            // },
            filterChange(val) {
                if (val === '' || val === null) {
                    this.filterSceneData = []
                } else {
                    rbAlertFilterServicesFactory.getFilterScene(val).then(res => {
                        this.filterSceneData = res
                    })
                }
            },
            initAlertSum() {
            },
            // 1 element相关方法 多选框
            handleSelectionChange(val) {
                this.alertList = []
                this.multipleSelection = val
                val.forEach(item => {
                    this.alertList.push({ alertId: item.alertId })
                })
            },
            // 分页改变尺寸
            handleSizeChange(val) {
                this.pageSize = val
                this.queryDatasList.page_size = val
                this.getTableData(this.queryDatasList)
            },

            // 分页改变当前页
            handleCurrentChange(val) {
                this.currentPage = val
                this.queryDatasList.page_no = val
                this.getTableData(this.queryDatasList)
            },
            // 对话框
            handleClose(done) {
                done()
            },
            changePool() {
                let _this = this
                _this.bizDepTreeSelOpts = []
                rbAlertKanBanServiceFactory
                    .bizSysTreeOptionByResourcePollSel(this.search.resource_pool || '')
                    .then(function (poolDeps) {
                        let sysDepts =
                            _this.bizSysTree && _this.bizSysTree.length > 0 && _this.bizSysTree[0].children && _this.bizSysTree[0].children.length > 0
                                ? _this.bizSysTree[0].children
                                : []
                        _.forEach(poolDeps, poolDep => {
                            if (poolDep) {
                                _(sysDepts)
                                    .filter(sysdep => {
                                        return poolDep === sysdep.id
                                    })
                                    .forEach(subdep => {
                                        _this.bizDepTreeSelOpts.push(subdep)
                                    })
                            }
                        })
                    })
            },
            cleanPool() {
                this.bizSysList = []
                this.search.system = ''
            },

            // 111111111111111111111111111111111111111111111111111111
            // 历史告警列表
            getTableData(obj) {
                this.rbHttp.sendRequest({
                    method: 'POST',
                    data: obj,
                    url: '/v2/alerts/alert_his/query'
                }).then((res) => {
                    this.total = res.count
                    // console.log(res);

                    // this.activityAlertData = res.result
                    this.activityAlertData = this.parseTableData(this.packData(res.result))
                    this.$emit('sendTotal', this.total)
                    this.loading = false
                })
            },
            parseTableData(obj) {
                let list = []
                obj.forEach(item => {
                    let idcType = item.idc_type ? item.idc_type + ' ' : ''
                    let projectName = item.project_name ? item.project_name + ' ' : ''
                    let podName = item.pod_name ? item.pod_name : ''
                    let deviceClass = item.device_class ? item.device_class + '-' : ''
                    let deviceType = item.device_type ? item.device_type : ''
                    item.idc_type = idcType + projectName + podName
                    item.device_class = deviceClass + deviceType
                    list.push(item)
                })
                return list
            },
            // 封装得到的数据
            packData(arr) {
                // 列表数据封装
                if (arr.forEach) {
                    arr.forEach(item => {
                        //            item.order_type = item.order_status === '1' ? '' : '告警工单'
                        item.order_type = rbMirrorCommonService.common(
                            'ORDERTYPE',
                            '1',
                            item.order_type
                        )
                        item.order_status = rbMirrorCommonService.common(
                            'ORDERSTATUS',
                            '1',
                            item.order_status
                        )
                        item.object_type = rbMirrorCommonService.common(
                            'OBJECTTYPE',
                            '1',
                            item.object_type
                        )
                        item.report_status = rbMirrorCommonService.common(
                            'REPORTSTATUS',
                            '1',
                            item.report_status + ''
                        )
                        item.trans_status = rbMirrorCommonService.common(
                            'TRANSSTATUS',
                            '1',
                            item.trans_status
                        )
                        item.report_type = rbMirrorCommonService.common(
                            'REPORTTYPE',
                            '1',
                            item.report_type + ''
                        )
                        item.cur_moni_time = formatDate(item.cur_moni_time)
                        item.alert_start_time = formatDate(item.alert_start_time)
                        item.alert_end_time = formatDate(item.alert_end_time)
                        item.deliver_time = formatDate(item.deliver_time)
                        item.confirmed_time = formatDate(item.confirmed_time)
                        item.report_time = formatDate(item.report_time)
                        item.trans_time = formatDate(item.trans_time)
                        item.create_time = formatDate(item.create_time)
                        item.clear_time = formatDate(item.clear_time)
                    })
                    return arr
                } else {
                    // 详情数据封装
                    //          arr.order_type = arr.order_status === '1' ? '' : '告警工单'
                    arr.order_type = rbMirrorCommonService.common(
                        'ORDERTYPE',
                        '1',
                        arr.order_type
                    )
                    arr.order_status = rbMirrorCommonService.common(
                        'ORDERSTATUS',
                        '1',
                        arr.order_status
                    )
                    arr.object_type = rbMirrorCommonService.common(
                        'OBJECTTYPE',
                        '1',
                        arr.object_type
                    )
                    arr.report_status = rbMirrorCommonService.common(
                        'REPORTSTATUS',
                        '1',
                        arr.report_status + ''
                    )
                    arr.trans_status = rbMirrorCommonService.common(
                        'TRANSSTATUS',
                        '1',
                        arr.trans_status
                    )
                    arr.report_type = rbMirrorCommonService.common(
                        'REPORTTYPE',
                        '1',
                        arr.report_type + ''
                    )
                    arr.cur_moni_time = formatDate(arr.cur_moni_time)
                    arr.alert_start_time = formatDate(arr.alert_start_time)
                    arr.deliver_time = formatDate(arr.deliver_time)
                    arr.confirmed_time = formatDate(arr.confirmed_time)
                    arr.report_time = formatDate(arr.report_time)
                    arr.trans_time = formatDate(arr.trans_time)
                    return arr
                }
            },
            // 封装时间戳
            packTime(str) {
                return formatDate(str)
            },
            // 告警时长
            packTimePoint(num) {
                let date = new Date().getTime()
                var total = (date - num) / 1000
                var day = parseInt(total / (24 * 60 * 60)) //  计算整数天数
                var afterDay = total - day * 24 * 60 * 60 //  取得算出天数后剩余的秒数
                var hour = parseInt(afterDay / (60 * 60)) //  计算整数小时数
                var afterHour = total - day * 24 * 60 * 60 - hour * 60 * 60 //  取得算出小时数后剩余的秒数
                var min = parseInt(afterHour / 60) //  计算整数分
                var second = parseInt(
                    total - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60
                ) //  取得算出分后剩余的秒数
                return `${day}d ${hour}h ${min}m ${second}s`
            },

            // 重置
            reset() {
                // this.device_relation_value = ''
                // this.monitor_relation_value = ''
                this.device_device = ''
                this.search.device_class = ''
                this.alert_level_value = ''
                this.search.system = ''
                this.search.resource_pool = ''
                // this.queryType = ''
                this.search.alert_from = []
                this.cancelSearch()
            },
            // 锁屏
            lock() {
                clearInterval(this.timer)
                this.lockScgreen = false
                this.goScgreen = true
            },
            // 刷屏
            go() {
                this.timer = setInterval(() => {
                    if (this.filterFlag) {
                        this.searchFilterData()
                    } else {
                        this.getTableData(this.queryDataParameter)
                    }
                }, this.timer_frequency)
                this.goScgreen = false
                this.lockScgreen = true
            },
            // 导出
            resourceExport() {
                this.loading = true
                this.exportDisabled = true

                // this.queryDataParameter.list = []
                console.log(this.queryDataParameter)
                // console.log('his')
                //  console.log(this.queryDataParameterHis);
                rbAlertKanBanServiceFactory
                    .ExportGridDataHis(this.queryDataParameter)
                    .then(res => {
                        // console.log(res);

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
                        // let blob = new Blob([res], { type: 'application/vnd.ms-excel' })
                        // let objectUrl = URL.createObjectURL(blob)
                        // let downLoadElement = document.createElement('a')
                        // downLoadElement.href = objectUrl
                        // downLoadElement.download = '历史告警数据列表.xlsx'
                        // document.body.appendChild(downLoadElement)
                        // downLoadElement.click()
                        // document.body.removeChild(downLoadElement)
                        // URL.revokeObjectURL(objectUrl)


                    })
                    .finally(() => {
                        this.loading = false
                        this.exportDisabled = false
                    })
                //     let page = {
                //         page_no: this.currentPage,
                //         page_size: this.pageSize,
                //         condition: this.filterCondition
                //     }
                //     if (this.filterFlag) {
                //         rbAlertFilterServicesFactory
                //   .ExportFilterGridData(page)
                //   .then(res => {
                //       let blob = new Blob([res], { type: 'application/vnd.ms-excel' })
                //       let objectUrl = URL.createObjectURL(blob)
                //       let downLoadElement = document.createElement('a')
                //       downLoadElement.href = objectUrl
                //       downLoadElement.download = '历史告警数据列表.xlsx'
                //       document.body.appendChild(downLoadElement)
                //       downLoadElement.click()
                //       document.body.removeChild(downLoadElement)
                //       URL.revokeObjectURL(objectUrl)
                //   })
                //   .finally(() => {
                //       this.loading = false
                //       this.exportDisabled = false
                //   })
                //     } else {

                // }
            },
            // resourceExport() {
            //     this.loading = true
            //     this.exportDisabled = true
            //     let page = {
            //         page_no: this.currentPage,
            //         page_size: this.pageSize,
            //         condition: this.filterCondition
            //     }
            //     if (this.filterFlag) {
            //         rbAlertFilterServicesFactory
            //   .ExportFilterGridData(page)
            //   .then(res => {
            //       let blob = new Blob([res], { type: 'application/vnd.ms-excel' })
            //       let objectUrl = URL.createObjectURL(blob)
            //       let downLoadElement = document.createElement('a')
            //       downLoadElement.href = objectUrl
            //       downLoadElement.download = '历史告警数据列表.xlsx'
            //       document.body.appendChild(downLoadElement)
            //       downLoadElement.click()
            //       document.body.removeChild(downLoadElement)
            //       URL.revokeObjectURL(objectUrl)
            //   })
            //   .finally(() => {
            //       this.loading = false
            //       this.exportDisabled = false
            //   })
            //     } else {
            //         rbAlertKanBanServiceFactory
            //   .ExportGridData(this.queryDataParameter)
            //   .then(res => {
            //       let blob = new Blob([res], { type: 'application/vnd.ms-excel' })
            //       let objectUrl = URL.createObjectURL(blob)
            //       let downLoadElement = document.createElement('a')
            //       downLoadElement.href = objectUrl
            //       downLoadElement.download = '历史告警数据列表.xlsx'
            //       document.body.appendChild(downLoadElement)
            //       downLoadElement.click()
            //       document.body.removeChild(downLoadElement)
            //       URL.revokeObjectURL(objectUrl)
            //   })
            //   .finally(() => {
            //       this.loading = false
            //       this.exportDisabled = false
            //   })
            //     }
            // },

            // 操作
            // 查看告警详情
            goDetail(alert_id) {
                this.$router.push({
                    path: '/mirror_alert/alert/detail',
                    query: {
                        alert_id: alert_id
                    }
                })
            },

            getTimerFrequency() {
                let obj = {
                    type: 'alert_his_timer_frequency'
                }
                rbConfigDictServiceFactory.getDictsByType(obj).then(res => {
                    if (res) {
                        this.timer_frequency = res[0].value
                    }
                })
            },
            initParam() {
                this.queryType = this.alertType === 'activity' ? 0 : 1
            },
            init() {
                console.log(this.$route.query)
                let json = JSON.parse(this.$route.query.filter)
                let arr = [{
                    fieldName: 'operate_status', // 条件名称
                    fieldType: 'in', // 类型
                    fieldValue: '0,1,3' // 选中的条件 0-待确认,1-已确认,4-待观察
                }]
                for (let i = 0; i < json.length; i++) {
                    if (json[i].code == 'cur_moni_time') {
                        arr.push({
                            fieldName: json[i].code,
                            fieldType: 'datetime',
                            fieldValue: json[i].name
                        })
                    } else {
                        arr.push({
                            fieldName: json[i].code,
                            fieldType: 'in_and',
                            fieldValue: json[i].name
                        })
                    }

                }
                if (this.$route.query.filter) {
                    this.queryDataParameter.list = arr
                }
                this.getTimerFrequency()
                if (this.timer) {
                    clearInterval(this.timer)
                } else {
                    this.timer = setInterval(() => {
                        this.getTableData(this.queryDataParameter)
                        this.getTimerFrequency()
                    }, this.timer_frequency)
                }
                // this.getMonitorValue()
                // this.getUserList()
                if (this.alertType === 'activity') {
                    this.voiceNotifyInit()
                }
            },
            // 自定义表头列宽
            flexColumnWidth(str) {
                let flexWidth = 150
                if (str === 'moni_index') {
                    flexWidth += 130
                } else if (str === 'alert_start_time') {
                    flexWidth += 35
                } else if (str === 'alert_end_time') {
                    flexWidth += 35
                }
                return flexWidth + 'px'
            },
            dblhandleCurrentChange(column) {
                this.$router.push({
                    path: '/mirror_alert/alert_his_new/detail',
                    query: {
                        alertId: column.alert_id,
                        detailType: 'alert',
                        modeNameList: this.modelFieldDataList
                    }
                })
            },
            sortByAlertLevel(obj1, obj2) {
                let val1 = obj1.alert_level
                let val2 = obj2.alert_level
                return val1 - val2
            },
            getAlertVoiceNotify() {
                rbAlertVoiceNotifyServicesFactory.getAlertVoiceNotify().then(res => {
                    this.alertVoiceNotifyDetail = res
                })
            },
            validateAddRequest() {
                let addForm = this.$refs.rbMirrorAlertVoiceNotify.AlertVoiceNotifyForm
                let obj = {}
                obj.addForm = addForm
                obj.flag = true
                if (!addForm.alertFilterId) {
                    obj.flag = false
                    obj.msg = '请先选择告警筛选器!'
                    return obj
                } else if (!addForm.alertFilterSceneId) {
                    obj.flag = false
                    obj.msg = '请先选择告警场景!'
                    return obj
                } else if (!addForm.alertExistTime) {
                    obj.flag = false
                    obj.msg = '请先填写告警持续时长!'
                    return obj
                } else {
                    return obj
                }
            },
            getAlertVoiceNotifyRequest(obj) {
                let request = {}
                request.uuid = obj.uuid
                request.language = obj.language
                request.voiceContent = obj.voiceContent
                request.isOpen = parseInt(obj.isOpen)
                request.alertFilterId = parseInt(obj.alertFilterId)
                request.alertFilterSceneId = parseInt(obj.alertFilterSceneId)
                request.content = obj.content.toString()
                request.alertExistTime = parseInt(obj.alertExistTime)
                request.voiceAlertId = this.alertVoiceNotifyDetail.voiceAlertId
                return request
            },
            createVoiceNotify() {
                let obj = this.validateAddRequest()
                if (!obj.flag) {
                    this.$alert(obj.msg, '警告', {
                        confirmButtonText: '确定'
                    })
                    return
                }
                rbAlertVoiceNotifyServicesFactory
                    .createdAlertVoiceNotify(this.getAlertVoiceNotifyRequest(obj.addForm))
                    .then(res => {
                        let content = this.alertVoiceNotifyDetail.uuid ? '更新' : '创建'
                        if (res === 'success') {
                            this.$message({
                                message: content + '成功!',
                                type: 'success'
                            })
                            this.voiceNotifyDialog = false
                            window.speechSynthesis.cancel()
                            if (obj.addForm.isOpen === 1) {
                                this.voiceNotifyInit()
                            } else {
                                clearInterval(this.voiceTimer)
                            }
                        } else {
                            this.voiceNotifyDialog = false
                            this.$message.error(content + '失败!')
                        }
                    })
            },
            voiceNotify() {
                rbAlertVoiceNotifyServicesFactory.getAlertVoiceNotify().then(result => {
                    if (result && result.isOpen === 1) {
                        rbAlertVoiceNotifyServicesFactory
                            .getAlertVoiceNotifyContent(result)
                            .then(res => {
                                if (res && res !== 'error') {
                                    let msg = new SpeechSynthesisUtterance()
                                    msg.text = res
                                    window.speechSynthesis.speak(msg)
                                }
                            })
                    }
                })
            },
            voiceNotifyInit() {
                this.voiceNotify()
                if (!this.voiceTimer) {
                    this.voiceTimer = setInterval(() => {
                        this.voiceNotify()
                    }, 60 * 1000)
                }
            },
            gotoBPM: function (bpm_id) {
                const url = `${sessionStorage.getItem(
                    'X7_SERVER_URL'
                )}/front/#/inst/${bpm_id}?mirrorToken=${sessionStorage.getItem('mirror')}`
                window.open(url, '_blank')
            },
        },
        watch: {
            queryDatas: {
                handler(val) {
                    console.log('val===', val)
                    let newjson = {
                        page_no: 1,
                        page_size: 50,
                        sort_name: '',
                        list: []
                    }
                    let arr = [{
                        fieldName: 'operate_status', // 条件名称
                        fieldType: 'in', // 类型
                        fieldValue: '0,1,3' // 选中的条件 0-待确认,1-已确认,4-待观察
                    }]
                    if (this.$route.query.filter) {
                        console.log(val)
                        console.log(this.$route.query)
                        let json = JSON.parse(this.$route.query.filter)

                        for (let i = 0; i < json.length; i++) {
                            if (json[i].code == 'cur_moni_time') {
                                arr.push({
                                    fieldName: json[i].code,
                                    fieldType: 'datetime',
                                    fieldValue: json[i].name
                                })
                            } else {
                                arr.push({
                                    fieldName: json[i].code,
                                    fieldType: 'in_and',
                                    fieldValue: json[i].name
                                })
                            }
                        }
                    }

                    this.queryDatasList = val
                    if (val.queryType != 3) {
                        this.queryDataParameterFile = {}
                        this.queryDataParameter = val
                        if (this.$route.query.filter) {
                            newjson.list = arr
                            this.getTableData(newjson)
                        } else {
                            this.getTableData(val)
                        }
                    }

                },
                immediate: true, // 初始化传值
                deep: true // 深度监听
            },
            modelFieldData: {
                handler(val) {
                    this.modelFieldDataList = val
                }
            },
        }
    }
</script>

<style lang="scss" scoped>
    .components-condition {
      padding-right: 250px;
    }
    .alertNumContent {
      display: inline-block;
      vertical-align: middle;
      margin-right: 10px;
      &:first-of-type {
        margin-left: 20px;
      }
      .total {
        display: inline-block;
        vertical-align: middle;
      }
    }

    .yw-form {
      .el-form__item {
        white-space: nowrap;
      }
    }
</style>

<style>
    .ywTabBorder .el-table__body-wrapper {
      height: calc(100% - 29px) !important;
    }
    .ywTabBorder .el-table__fixed-body-wrapper {
      top: 29px !important;
      height: calc(100% - 29px) !important;
    }
</style>

