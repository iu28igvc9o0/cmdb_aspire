<template>
    <!-- IP稽核内网IP-已录CMDB未存活IP -->
    <div class="components-container yw-dashboard"
         v-loading="loading">
        <ul class="conflict-title">
            <li><span>问题资产总数：</span><span>{{recordCount.numOfProblemAsset}}</span></li>
            <li><span>未存活IP总数：</span><span>{{recordCount.numOfUnsurvivingIp}}</span></li>
            <li><span>待处理资产数：</span><span>{{recordCount.numOfToBeProcessAsset}}</span></li>
        </ul>
        <el-form class="yw-form"
                 :model="dialogForm"
                 :inline="true"
                 :rules="formRules"
                 label-width="85px"
                 ref="dialogForm">
            <el-form-item label="检测时间"
                          prop="time">
                <el-date-picker v-model="dialogForm.time"
                                format="yyyy-MM-dd HH:mm:ss"
                                value-format="yyyy-MM-dd HH:mm:ss"
                                type="datetimerange"
                                range-separator="至"
                                start-placeholder="开始日期"
                                end-placeholder="结束日期">
                </el-date-picker>
            </el-form-item>
            <el-form-item label="IP"
                          label-width="30px"
                          prop="ipList">
                <el-input v-model="dialogForm.ipList"></el-input>
                <el-popover placement="bottom"
                            width="400"
                            trigger="manual"
                            v-model="visible">
                    <div class="many-ip">
                        <el-input type="textarea"
                                  v-model="manyIp"
                                  :rows="6"
                                  placeholder="可输入多个ip进行查询，逗号分隔或者回车"></el-input>
                        <div style="margin-top:10px;text-align:center;">
                            <el-button type="primary"
                                       @click="IpSure">确认</el-button>
                            <el-button @click="visible=false">取消</el-button>
                        </div>
                    </div>
                    <span slot="reference"
                          class="el-icon-plus add-ip"
                          @click="IpCheck"></span>
                </el-popover>
            </el-form-item>
            <el-form-item label="资产管理IP"
                          prop="deviceIp">
                <el-input v-model="dialogForm.deviceIp"></el-input>
            </el-form-item>
            <el-form-item label="所属资源池"
                          prop="dcList">
                <el-select v-model="dialogForm.dcList"
                           clearable
                           filterable
                           placeholder="请选择">
                    <el-option v-for="item in resourceOptions"
                               :key="item.value"
                               :label="item.label"
                               :value="item.value">
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="业务线"
                          prop="businessLevel1">
                <el-select v-model="dialogForm.businessLevel1"
                           clearable
                           filterable
                           placeholder="请选择"
                           @change="getSelectBusiness">
                    <el-option v-for="item in businessLevel1Options"
                               :key="item.value"
                               :label="item.label"
                               :value="item.value">
                    </el-option>
                </el-select>
                <el-select v-model="dialogForm.businessLevel2"
                           clearable
                           filterable
                           placeholder="请选择">
                    <el-option v-for="item in businessLevel2Options"
                               :key="item.value"
                               :label="item.label"
                               :value="item.value">
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="是否已通知"
                          prop="isNotify">
                <el-select v-model="dialogForm.isNotify"
                           clearable
                           style="width:120px;"
                           placeholder="请选择">
                    <el-option v-for="item in notifyOptions"
                               :key="item.value"
                               :label="item.label"
                               :value="item.value">
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="处理状态"
                          prop="processingStatus">
                <el-select v-model="dialogForm.processingStatus"
                           clearable
                           style="width:120px;"
                           placeholder="请选择">
                    <el-option v-for="item in processingStatusOptions"
                               :key="item.value"
                               :label="item.label"
                               :value="item.value">
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item>
                <el-button type="primary"
                           @click="search">查询</el-button>
                <el-button type="primary"
                           @click="handelSenior">高级查询</el-button>
                <el-button @click="reset">重置</el-button>
                <el-button @click="tableExport">导出</el-button>
            </el-form-item>
            <div class="senior-search"
                 v-show="isSenior">
                <el-form-item label="操作时间"
                              prop="operateTime">
                    <el-date-picker v-model="dialogForm.operateTime"
                                    format="yyyy-MM-dd HH:mm:ss"
                                    value-format="yyyy-MM-dd HH:mm:ss"
                                    type="datetimerange"
                                    range-separator="至"
                                    start-placeholder="开始日期"
                                    end-placeholder="结束日期">
                    </el-date-picker>
                </el-form-item>
                <el-form-item label="IP类型"
                              prop="ipType">
                    <el-select v-model="dialogForm.ipType"
                               clearable
                               style="width:120px;"
                               placeholder="请选择">
                        <el-option v-for="item in ipTypeOptions"
                                   :key="item.value"
                                   :label="item.label"
                                   :value="item.value">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="主备"
                              label-width="55px"
                              prop="hostBackup">
                    <el-select v-model="dialogForm.hostBackup"
                               clearable
                               style="width:120px;"
                               placeholder="请选择">
                        <el-option v-for="item in hostBackupOptions"
                                   :key="item.value"
                                   :label="item.label"
                                   :value="item.value">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="是否资源池管理"
                              label-width="100px"
                              prop="mgrByPool">
                    <el-select v-model="dialogForm.mgrByPool"
                               clearable
                               style="width:100px;"
                               placeholder="请选择">
                        <el-option v-for="item in mgrByPoolOptions"
                                   :key="item.value"
                                   :label="item.label"
                                   :value="item.value">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="最近存活时间"
                              prop="duringSurvialTime">
                    <el-date-picker v-model="dialogForm.duringSurvialTime"
                                    format="yyyy-MM-dd HH:mm:ss"
                                    value-format="yyyy-MM-dd HH:mm:ss"
                                    type="datetimerange"
                                    range-separator="至"
                                    start-placeholder="开始日期"
                                    end-placeholder="结束日期">
                    </el-date-picker>
                </el-form-item>
                <el-form-item label="未存活时长">
                    <el-form-item prop="unsurvivedDurationBegin">
                        <el-input v-model="dialogForm.unsurvivedDurationBegin"></el-input>
                    </el-form-item>
                    <span style="margin-right:6px;">至</span>
                    <el-form-item prop="unsurvivedDurationEnd">
                        <el-input v-model="dialogForm.unsurvivedDurationEnd"></el-input>
                    </el-form-item>
                </el-form-item>
                <el-form-item label="原因说明"
                              prop="reason">
                    <el-input v-model="dialogForm.reason"></el-input>
                </el-form-item>
                <el-form-item label="操作人"
                              prop="operator">
                    <el-input v-model="dialogForm.operator"></el-input>
                </el-form-item>
                <el-form-item label="设备状态"
                              label-width="70px"
                              prop="deviceStatus">
                    <el-select v-model="dialogForm.deviceStatus"
                               clearable
                               style="width:120px;"
                               placeholder="请选择">
                        <el-option v-for="item in deviceStatusOptions"
                                   :key="item.value"
                                   :label="item.label"
                                   :value="item.value">
                        </el-option>
                    </el-select>
                </el-form-item>
            </div>
        </el-form>

        <div class="yw-table">
            <div class="deal-btn">
                <el-button v-if="buttonPermission.ip_check_operator_stop"
                           @click="notHandle">暂不处理</el-button>
                <el-button v-if="buttonPermission.ip_check_operator_continue"
                           @click="continueHandle">继续处理</el-button>
            </div>
            <div class="yw-el-table-wrap">
                <el-table class="yw-el-table"
                          :data="collectionData.tableBody"
                          style="width:100%;cursor: pointer;"
                          stripe
                          border
                          tooltip-effect="dark"
                          :default-sort="{prop: 'date', order: 'descending'}"
                          :cell-style="cellStyle"
                          height="calc(100vh - 340px)"
                          @selection-change="handleSelectionChange">
                    <el-table-column type="selection"
                                     :selectable='checkboxT'
                                     width="55">
                    </el-table-column>
                    <el-table-column v-for="(item,index) in collectionData.tableHeader"
                                     :prop="item.value"
                                     :sortable="item.sortable"
                                     :label="item.label"
                                     :width="item.width"
                                     :show-overflow-tooltip="true"
                                     :key="index"> </el-table-column>
                </el-table>
            </div>
            <div class="yw-page-wrap"
                 v-if="collectionData.pagination.total>0">
                <el-pagination @size-change="handleSizeChange"
                               @current-change="handleCurrentChange"
                               :current-page="collectionData.pagination.currentPage"
                               :page-sizes="collectionData.pageSizes"
                               :page-size="collectionData.pagination.pageSize"
                               :total="collectionData.pagination.total"
                               layout="total, sizes, prev, pager, next, jumper">
                </el-pagination>
            </div>
        </div>
        <!-- dialog -->
        <el-dialog class="yw-dialog"
                   :visible.sync="dialogFormReason"
                   width="300px"
                   :close-on-click-modal="false"
                   title="原因说明">
            <section class="yw-dialog-main">
                <el-input type="textarea"
                          v-model="reasonExplain"
                          placeholder="请输入暂不处理原因说明"
                          :rows="6"></el-input>
            </section>
            <section class="btn-wrap">
                <el-button type="primary"
                           @click="handleReasonSure">确定</el-button>
                <el-button @click="dialogFormReason=false">取消</el-button>
            </section>
        </el-dialog>
        <!-- dialog -->
    </div>
</template>

<script>
    import rbIpCollectionServiceFactory from 'src/services/cmdb/rb-cmdb-ip-management-factory.js'
    import buttonPermission from 'src/services/cmdb/rb-audit-permission.js'
    export default {
        name: 'CmdbCmicAuditRecordedCmdb',
        data() {
            var validateIp = (rule, value, callback) => {
                let newVal = this.deleteSpace(value)
                if (newVal) {
                    let temp = newVal.split(/[\n,，]/g)
                    for (let i = 0; i < temp.length; i++) {
                        if (temp[i] == '') {
                            temp.splice(i, 1)
                            // 删除数组索引位置应保持不变
                            i--
                        }
                    }
                    // let unqualifiedIp = temp.filter(item => !this.isValidIps(item))
                    // if (unqualifiedIp.length > 0) {
                    //     callback(new Error('请输入正确的IP'))
                    // } else {
                    //     this.dialogForm.ipList = temp.join(',')
                    //     callback()
                    // }
                    this.dialogForm.ipList = temp.join(',')
                    callback()

                } else {
                    callback()
                }
            }
            var validateIp1 = (rule, value, callback) => {
                let newVal = this.deleteSpace(value)
                if (newVal) {
                    let temp = newVal.split(/[\n,，]/g)
                    for (let i = 0; i < temp.length; i++) {
                        if (temp[i] == '') {
                            temp.splice(i, 1)
                            // 删除数组索引位置应保持不变
                            i--
                        }
                    }
                    // let unqualifiedIp = temp.filter(item => !this.isValidIps(item))
                    // if (unqualifiedIp.length > 0) {
                    //     callback(new Error('请输入正确的资产管理IP'))
                    // } else {
                    //     this.dialogForm.deviceIp = temp.join(',')
                    //     callback()
                    // }
                    this.dialogForm.deviceIp = temp.join(',')
                    callback()

                } else {
                    callback()
                }
            }
            var validateBeginTime = (rule, value, callback) => {
                let newVal1 = this.deleteSpace(value)
                let newVal2 = this.deleteSpace(this.dialogForm.unsurvivedDurationEnd)
                const reg = /^[+]{0,1}(\d+)$|^[+]{0,1}(\d+\.\d+)$/
                if (newVal1) {
                    if (!reg.test(newVal1)) {
                        return callback(new Error('请输入正确的未存活时长开始时间'))
                    }
                    if (newVal2 && parseFloat(newVal1) > parseFloat(newVal2)) {
                        callback(new Error('未存活时长开始时间应小于或等于结束时间'))
                    } else {
                        callback()
                    }
                } else {
                    callback()
                }
            }
            var validateEndTime = (rule, value, callback) => {
                let newVal1 = this.deleteSpace(value)
                let newVal2 = this.deleteSpace(this.dialogForm.unsurvivedDurationBegin)
                const reg = /^[+]{0,1}(\d+)$|^[+]{0,1}(\d+\.\d+)$/
                if (newVal1) {
                    if (!reg.test(newVal1)) {
                        return callback(new Error('请输入正确的未存活时长结束时间'))
                    }
                    if (newVal2 && parseFloat(newVal2) > parseFloat(newVal1)) {
                        callback(new Error('未存活时长结束时间应大于或等于开始时间'))
                    } else {
                        callback()
                    }
                } else {
                    callback()
                }
            }
            return {
                dialogFormReason: false,
                reasonExplain: '',
                loading: false,
                recordCount: {
                    numOfProblemAsset: '',
                    numOfUnsurvivingIp: '',
                    numOfToBeProcessAsset: ''
                },
                dialogForm: {
                    time: '',
                    ipList: '',
                    deviceIp: '',
                    dcList: '',
                    businessLevel1: '',
                    businessLevel2: '',
                    isNotify: '',
                    processingStatus: '',
                    operateTime: '',
                    ipType: '',
                    hostBackup: '',
                    mgrByPool: '',
                    surviveStatus: '',
                    duringSurvialTime: '',
                    unsurvivedDurationBegin: '',
                    unsurvivedDurationEnd: '',
                    reason: '',
                    operator: '',
                    deviceStatus:''
                },
                resourceOptions: [],
                processingStatusOptions: [
                    {
                        label: '待处理',
                        value: '0'
                    },
                    {
                        label: '暂不处理',
                        value: '1'
                    },
                    {
                        label: '处理中',
                        value: '2'
                    },
                    {
                        label: '已处理',
                        value: '3'
                    }
                ],
                notifyOptions: [
                    {
                        label: '未通知',
                        value: '未通知'
                    },
                    {
                        label: '已通知',
                        value: '已通知'
                    }
                ],
                ipTypeOptions: [
                    {
                        label: '管理IP',
                        value: '管理IP'
                    },
                    {
                        label: '业务IP1',
                        value: '业务IP1'
                    },
                    {
                        label: '业务IP2',
                        value: '业务IP2'
                    },
                    {
                        label: 'IPV6',
                        value: 'IPV6'
                    },
                    {
                        label: '其他IP',
                        value: '其他IP'
                    },
                    {
                        label: 'consoleIP',
                        value: 'consoleIP'
                    }
                ],
                hostBackupOptions: [],
                deviceStatusOptions: [],
                mgrByPoolOptions: [],
                surviveStatusOptions: [
                    {
                        label: '未存活',
                        value: '未存活'
                    },
                    {
                        label: '已存活',
                        value: '已存活'
                    }
                ],
                businessLevel1Options: [],
                businessLevel2Options: [],
                collectionData: {
                    tableHeader: [
                        {
                            label: '检测时间',
                            value: 'checkTime',
                            sortable: true,
                            width: '160px'
                        },
                        {
                            label: 'IP',
                            value: 'ip',
                            sortable: false,
                            width: '120px'
                        },
                        {
                            label: 'IP类型',
                            value: 'ipType',
                            sortable: false,
                            width: '100px'
                        },
                        {
                            label: '资产管理IP',
                            value: 'deviceIp',
                            sortable: false,
                            width: '125px'
                        },
                        {
                            label: '主备',
                            value: 'hostBackup',
                            sortable: false,
                            width: '100px'
                        },
                        {
                            label: '所属资源池',
                            value: 'dc',
                            sortable: false,
                            width: '120px'
                        },
                        {
                            label: '是否资源池管理',
                            value: 'mgrByPool',
                            sortable: false,
                            width: '120px'
                        },
                        {
                          label:'设备状态',
                          value: 'deviceStatus',
                          sortable: false,
                          width: '120px'
                        },
                        {
                            label: '所属独立业务',
                            value: 'businessLevel1',
                            sortable: false,
                            width: '120px'
                        },
                        {
                            label: '独立业务子模块',
                            value: 'businessLevel2',
                            sortable: false,
                            width: '120px'
                        },
                        {
                            label: '存活状态',
                            value: 'surviveStatus',
                            sortable: false,
                            width: '120px'
                        },
                        {
                            label: '最近存活时间',
                            value: 'lastSurviveTime',
                            sortable: false,
                            width: '120px'
                        },
                        {
                            label: '未存活时长(小时)',
                            value: 'unsurvivedDuration',
                            sortable: false,
                            width: '120px'
                        },
                        {
                            label: '处理状态',
                            value: 'processingStatusDesc',
                            sortable: false,
                            width: '120px'
                        },
                        {
                            label: '原因说明',
                            value: 'reason',
                            sortable: false,
                            width: '120px'
                        },
                        {
                            label: '操作人',
                            value: 'operator',
                            sortable: false,
                            width: '120px'
                        },
                        {
                            label: '操作时间',
                            value: 'operatingTime',
                            sortable: false,
                            width: '120px'
                        }
                    ],
                    tableBody: [],
                    pagination: {
                        currentPage: 1,
                        pageSize: 20,
                        total: 0
                    },
                    pageSizes: [10, 20, 50, 100]
                },
                formRules: {
                    ipList: [{ validator: validateIp, trigger: 'blur' }],
                    deviceIp: [{ validator: validateIp1, trigger: 'blur' }],
                    unsurvivedDurationBegin: [{ validator: validateBeginTime, trigger: 'blur' }],
                    unsurvivedDurationEnd: [{ validator: validateEndTime, trigger: 'blur' }]
                },
                isSenior: false,
                visible: false,
                manyIp: '',
                multipleSelection: [],
                buttonPermission: buttonPermission
            }
        },
        created() {
            let currentTime = new Date().getTime()
            let oldTime = currentTime - (1 * 24 * 60 * 60 * 1000)
            this.dialogForm.time = [this.formatDate(oldTime), this.formatDate(currentTime)]
            this.getSelectValue('idcType2', 'resourceOptions')// 获取资源池
            this.getSelectValue('hostBackup', 'hostBackupOptions')// 获取主备
            this.getResource('business1', '', 'businessLevel1Options')// 获取一级业务线
            this.getSelectValue('booleanYesNo', 'mgrByPoolOptions')// 是否资源池管理
        },
        methods: {
            // element相关方法 多选框
            handleSelectionChange(val) {
                this.multipleSelection = val
            },
            async getSelectValue(params, type) {
                try {
                    let res = await rbIpCollectionServiceFactory.getCommonSelectValue(params)
                    for (let item of res) {
                        this[type].push(
                            {
                                label: item.value,
                                value: item.id
                            }
                        )
                    }
                    if(type=='mgrByPoolOptions'){
                       let mgrByPool=this.mgrByPoolOptions.filter((item)=>{
                       return item.label==='是'
                       })
                       this.dialogForm.mgrByPool=mgrByPool[0].value
                       this.getResource('deviceStatus','','deviceStatusOptions')// 获取设备状态
                    }
                } catch (error) {
                    this.$message({
                        message: error.data.errors[0].message,
                        type: 'error'
                    })
                }
            },
            // 获取业务线下拉值
            async getResource(params, pid, type) {
                let queryData = {
                    type: params,
                    pid: pid
                }
                try {
                    let res = await rbIpCollectionServiceFactory.getResource(queryData)
                    for (let item of res.data) {
                        this[type].push(
                            {
                                label: item.value,
                                value: item.id
                            }
                        )
                    }
                     if(type==='deviceStatusOptions'){
                     let device=this.deviceStatusOptions.filter((item)=>{
                       return item.label==='上电'
                     })
                     this.dialogForm.deviceStatus=device[0].value
                     this.getTableList()
                    }

                } catch (error) {
                    this.$message({
                        message: error.data.errors[0].message,
                        type: 'error'
                    })
                }
            },
            async getTableList(type) {
                this.loading = true
                let queryData = {}
                if (type) {
                    this.collectionData.pagination.currentPage = 1
                }
                for (let prop in this.dialogForm) {
                    if (prop && prop !== 'time' && prop !== 'operateTime' && prop !== 'duringSurvialTime' && prop !== 'dcList') {
                        queryData[prop] = this.dialogForm[prop]
                    }

                }
                // let businessLevel1 = (this.businessLevel1Options.filter(item => item.value == this.dialogForm.businessLevel1))[0]
                // let businessLevel2 = (this.businessLevel2Options.filter(item => item.value == this.dialogForm.businessLevel2))[0]
                // queryData.businessLevel1 = businessLevel1 ? businessLevel1.label : ''
                // queryData.businessLevel2 = businessLevel2 ? businessLevel2.label : ''
                queryData.dcList = this.dialogForm.dcList ? [this.dialogForm.dcList] : []
                queryData.checkTimeBegin = this.dialogForm.time ? this.dialogForm.time[0] : ''
                queryData.checkTimeEnd = this.dialogForm.time ? this.dialogForm.time[1] : ''
                queryData.operatingTimeBegin = this.dialogForm.operateTime ? this.dialogForm.operateTime[0] : ''
                queryData.operatingTimeEnd = this.dialogForm.operateTime ? this.dialogForm.operateTime[1] : ''
                queryData.lastSurviveTimeBegin = this.dialogForm.duringSurvialTime ? this.dialogForm.duringSurvialTime[0] : ''
                queryData.lastSurviveTimeEnd = this.dialogForm.duringSurvialTime ? this.dialogForm.duringSurvialTime[1] : ''
                queryData.pageSize = this.collectionData.pagination.pageSize
                queryData.pageNo = this.collectionData.pagination.currentPage
                try {
                    let res = await rbIpCollectionServiceFactory.recordedCmdbSearch(queryData)
                    this.collectionData.tableBody = res.result || []
                    this.collectionData.pagination.total = res.count
                    this.recordCount = {
                        numOfProblemAsset: res.numOfProblemAsset,
                        numOfUnsurvivingIp: res.numOfUnsurvivingIp,
                        numOfToBeProcessAsset: res.numOfToBeProcessAsset
                    }
                } catch (error) {
                    this.$message({
                        message: error.data.errors[0].message,
                        type: 'error'
                    })
                } finally {
                    this.loading = false
                }

            },
            getSelectBusiness(val) {
                this.dialogForm.businessLevel2 = ''
                this.businessLevel2Options = []
                this.getResource('business2', val, 'businessLevel2Options')

            },
            search() {
                this.$refs['dialogForm'].validate((valid) => {
                    if (valid) {
                        this.getTableList('find')
                    } else {
                        return false
                    }
                })
            },
            reset() {
                this.$refs['dialogForm'].resetFields()
                this.dialogForm.businessLevel2 = ''
                this.getTableList('find')
            },
            async tableExport() {
                const loading = this.$loading({
                    lock: true,
                    text: '正在下载中，请等待...',
                    spinner: 'el-icon-loading',
                    background: 'rgba(0, 0, 0, 0.7)'
                })
                let queryData = {}
                for (let prop in this.dialogForm) {
                    if (prop && prop !== 'time' && prop !== 'operateTime' && prop !== 'dcList' && prop !== 'businessLevel1' && prop !== 'businessLevel2') {
                        queryData[prop] = this.dialogForm[prop]
                    }

                }
                let businessLevel1 = (this.businessLevel1Options.filter(item => item.value == this.dialogForm.businessLevel1))[0]
                let businessLevel2 = (this.businessLevel2Options.filter(item => item.value == this.dialogForm.businessLevel2))[0]
                queryData.businessLevel1 = businessLevel1 ? businessLevel1.label : ''
                queryData.businessLevel2 = businessLevel2 ? businessLevel2.label : ''
                queryData.dcList = this.dialogForm.dcList ? [this.dialogForm.dcList] : []
                queryData.checkTimeBegin = this.dialogForm.time ? this.dialogForm.time[0] : ''
                queryData.checkTimeEnd = this.dialogForm.time ? this.dialogForm.time[1] : ''
                queryData.operatingTimeBegin = this.dialogForm.operateTime ? this.dialogForm.operateTime[0] : ''
                queryData.operatingTimeEnd = this.dialogForm.operateTime ? this.dialogForm.operateTime[1] : ''
                try {
                    let res = await rbIpCollectionServiceFactory.recordedCmdbExport(queryData)
                    let blob = new Blob([res], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' })
                    let isSupportDownload = 'download' in document.createElement('a')
                    if (isSupportDownload) {
                        const link = document.createElement('a')
                        link.download = '已录CMDB未存活IP列表' // a标签添加属性
                        link.style.display = 'none'
                        link.href = URL.createObjectURL(blob)
                        document.body.appendChild(link)
                        link.click()
                        document.body.removeChild(link)
                    } else {
                        window.navigator.msSaveBlob(blob, '已录CMDB未存活IP列表.xlsx')
                    }
                } catch (error) {
                    console.log(error)
                    this.$message({
                        message: error.data.errors[0].message,
                        type: 'error'
                    })
                } finally {
                    loading.close()
                }
            },
            // 分页改变尺寸
            handleSizeChange(val) {
                this.collectionData.pagination.pageSize = val
                this.getTableList('find')

            },
            // 分页改变当前页
            handleCurrentChange(val) {
                this.collectionData.pagination.currentPage = val
                this.getTableList()
            },
            // 高级查询
            handelSenior() {
                this.isSenior = !this.isSenior
            },
            IpCheck() {
                this.visible = true
                this.manyIp = ''
            },
            IpSure() {
                // let __this = this
                let manyIp = this.manyIp.replace(/^\s+|\s+$/g, '')
                if (manyIp) {
                    let temp = manyIp.split(/[\n,，]/g)
                    for (let i = 0; i < temp.length; i++) {
                        if (temp[i] == '') {
                            temp.splice(i, 1)
                            // 删除数组索引位置应保持不变
                            i--
                        }
                    }
                    // let unqualifiedIp = temp.filter(item => !__this.isValidIps(item))
                    // if (unqualifiedIp.length > 0) {
                    //     this.$alert(`以下IP不合法：${unqualifiedIp.join(',')}`, '提示')
                    // } else {
                    //     this.visible = false
                    //     this.dialogForm.ipList = temp.join(',')
                    // }
                    this.visible = false
                    this.dialogForm.ipList = temp.join(',')

                } else {
                    this.$message({
                        type: 'error',
                        message: '文本框不能为空，请输入IP'
                    })
                }

            },
            // 暂不处理
            notHandle() {
                if (this.multipleSelection.length < 1) {
                    this.$alert('请至少选择一条数据', '通知', {
                        confirmButtonText: '确定'
                    })
                } else {
                    this.dialogFormReason = true
                    this.reasonExplain = ''
                }

            },
            // 继续处理
            continueHandle() {
                if (this.multipleSelection.length < 1) {
                    this.$alert('请至少选择一条数据', '通知', {
                        confirmButtonText: '确定'
                    })
                } else {
                    this.getHandleData('0')
                }

            },
            // 说明原因确认
            handleReasonSure() {
                if (this.deleteSpace(this.reasonExplain)) {
                    this.getHandleData('1')
                    this.dialogFormReason = false
                } else {
                    this.$message({ type: 'error', message: '说明原因不能为空' })
                }
            },
            // 暂不处理和继续处理调用接口方法
            async getHandleData(type) {
                let newArray = []
                for (let item of this.multipleSelection) {
                    if (item && item.id) {
                        newArray.push(item.id)
                    }
                }
                let queryData = {
                    mainId: newArray,
                    handleStatus: type,
                    operator: sessionStorage.getItem('username')
                }
                if (type === '1') {
                    queryData.notHandleReason = this.reasonExplain
                }
                try {
                    let res = await rbIpCollectionServiceFactory.recordedCmdbUpdate(queryData)
                    this.$message({
                        message: res.msg,
                        type: 'success'
                    })
                    this.getTableList('find')
                } catch (error) {
                    this.$message({
                        message: error.data.errors[0].message,
                        type: 'error'
                    })
                }
            },
            // ip格式正则校验
            isValidIps(ip) {
                const reg = /^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/
                return reg.test(ip)
            },
            // 去掉字符串前后空格
            deleteSpace(str) {
                return str.replace(/^\s+|\s+$/g, '')

            },
            // 日期格式转换
            formatDate(data) {
                let date = new Date(data)
                let YY = date.getFullYear() + '-'
                let MM = (date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1) + '-'
                let DD = (date.getDate() < 10 ? '0' + (date.getDate()) : date.getDate())
                let hh = (date.getHours() < 10 ? '0' + date.getHours() : date.getHours()) + ':'
                let mm = (date.getMinutes() < 10 ? '0' + date.getMinutes() : date.getMinutes()) + ':'
                let ss = (date.getSeconds() < 10 ? '0' + date.getSeconds() : date.getSeconds())
                return YY + MM + DD + ' ' + hh + mm + ss
            },
            checkboxT(row) {
                if (row.processingStatus === '3') {
                    return false
                } else {
                    return true
                }
            },
            cellStyle({ row, columnIndex }) {
                if (columnIndex === 13 && row.processingStatus === '0') {
                    return 'color:red;'
                } else {
                    return ''
                }
            }
        }
    }
</script>

<style lang="scss" scoped>
    .yw-form {
        display: block !important;
        flex: 0 !important;
    }
    .yw-form .el-form-item__content > .el-input {
        width: 178px;
    }
    .el-date-editor.el-date-editor--datetimerange.el-input__inner {
        width: 312px;
    }
    div.el-select {
        width: 178px;
    }
    .add-ip {
        color: #169ef4;
        font-size: 20px;
        cursor: pointer;
        vertical-align: sub;
    }
    .yw-table {
        margin-top: 20px;
    }
    .conflict-title {
        display: flex;
        color: red;
        font-size: 17px;
        margin-bottom: 10px;
        li {
            margin-right: 60px;
        }
    }
    .deal-btn {
        margin-bottom: 10px;
    }
</style>
