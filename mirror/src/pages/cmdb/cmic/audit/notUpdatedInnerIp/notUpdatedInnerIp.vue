<template>
    <!-- IP稽核内网IP-CMDB未更新IP -->
    <div class="components-container yw-dashboard"
         v-if="false"
         v-loading="loading">
        <ul class="conflict-title">
            <li><span>未更新IP总数：</span><span>{{recordCount.numOfProblemAsset}}</span></li>
            <li><span>待处理IP数：</span><span>{{recordCount.numOfToBeProcessedIp}}</span></li>
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
                           placeholder="请选择">
                    <el-option v-for="item in resourceOptions"
                               :key="item.value"
                               :label="item.label"
                               :value="item.value">
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="处理状态"
                          prop="processingStatus">
                <el-select v-model="dialogForm.processingStatus"
                           style="width:120px;"
                           placeholder="请选择">
                    <el-option v-for="item in processingStatusOptions"
                               :key="item.value"
                               :label="item.label"
                               :value="item.value">
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="是否已通知"
                          prop="isNotify">
                <el-select v-model="dialogForm.isNotify"
                           style="width:120px;"
                           placeholder="请选择">
                    <el-option v-for="item in notifyOptions"
                               :key="item.value"
                               :label="item.label"
                               :value="item.value">
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="工单号"
                          label-width="55px"
                          prop="orderNum">
                <el-input v-model="dialogForm.orderNum"></el-input>
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
                               style="width:120px;"
                               placeholder="请选择">
                        <el-option v-for="item in ipTypeOptions"
                                   :key="item.value"
                                   :label="item.label"
                                   :value="item.value">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="录入CMDB时间"
                              label-width="100px"
                              prop="entryCmdbTime">
                    <el-date-picker v-model="dialogForm.entryCmdbTime"
                                    format="yyyy-MM-dd HH:mm:ss"
                                    value-format="yyyy-MM-dd HH:mm:ss"
                                    type="datetimerange"
                                    range-separator="至"
                                    start-placeholder="开始日期"
                                    end-placeholder="结束日期">
                    </el-date-picker>
                </el-form-item>
                <el-form-item label="是否资源池管理"
                              label-width="100px"
                              prop="mgrByPool">
                    <el-select v-model="dialogForm.mgrByPool"
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
                <el-form-item label="主备"
                              label-width="55px"
                              prop="hostBackup">
                    <el-select v-model="dialogForm.hostBackup"
                               style="width:120px;"
                               placeholder="请选择">
                        <el-option v-for="item in hostBackupOptions"
                                   :key="item.value"
                                   :label="item.label"
                                   :value="item.value">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="未使用时长"
                              prop="unusedTimeBegin">
                    <el-input v-model="dialogForm.unusedTimeBegin"></el-input>
                    <span>至</span>
                    <el-input v-model="dialogForm.unusedTimeEnd"></el-input>
                </el-form-item>
                <el-form-item label="业务线"
                              prop="businessLevel1">
                    <el-select v-model="dialogForm.businessLevel1"
                               placeholder="请选择">
                        <el-option v-for="item in businessLevel1Options"
                                   :key="item.value"
                                   :label="item.label"
                                   :value="item.value">
                        </el-option>
                    </el-select>
                    <el-select v-model="dialogForm.businessLevel2"
                               placeholder="请选择">
                        <el-option v-for="item in businessLevel2Options"
                                   :key="item.value"
                                   :label="item.label"
                                   :value="item.value">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="原因说明"
                              prop="reason">
                    <el-input v-model="dialogForm.reason"></el-input>
                </el-form-item>
                <el-form-item label="操作人"
                              prop="operator">
                    <el-input v-model="dialogForm.operator"></el-input>
                </el-form-item>
            </div>
        </el-form>

        <div class="yw-table">
            <div class="deal-btn">
                <el-button @click="notHandle">暂不处理</el-button>
                <el-button @click="continueHandle">继续处理</el-button>
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
                          height="calc(100vh - 300px)"
                          @selection-change="handleSelectionChange">
                    <el-table-column type="selection"
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
    export default {
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
                    let unqualifiedIp = temp.filter(item => !this.isValidIps(item))
                    if (unqualifiedIp.length > 0) {
                        callback(new Error('请输入正确的IP'))
                    } else {
                        this.dialogForm.ipList = temp.join(',')
                        callback()
                    }

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
                    let unqualifiedIp = temp.filter(item => !this.isValidIps(item))
                    if (unqualifiedIp.length > 0) {
                        callback(new Error('请输入正确的资产管理IP'))
                    } else {
                        this.dialogForm.deviceIp = temp.join(',')
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
                    numOfToBeProcessedIp: ''
                },
                dialogForm: {
                    time: '',
                    ipList: '',
                    deviceIp: '',
                    dcList: '',
                    processingStatus: '',
                    isNotify: '',
                    orderNum: '',
                    operateTime: '',
                    ipType: '',
                    hostBackup: '',
                    mgrByPool: '',
                    entryCmdbTime: '',
                    duringSurvialTime: '',
                    unusedTimeBegin: '',
                    unusedTimeEnd: '',
                    businessLevel1: '',
                    businessLevel2: '',
                    reason: '',
                    operator: ''
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
                mgrByPoolOptions: [
                    {
                        label: '是',
                        value: '是'
                    },
                    {
                        label: '否',
                        value: '否'
                    }
                ],
                businessLevel1Options: [],
                businessLevel2Options: [],
                collectionData: {
                    tableHeader: [
                        {
                            label: '检测时间',
                            value: 'time',
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
                            label: '所属资源池',
                            value: 'dc',
                            sortable: false,
                            width: '120px'
                        },
                        {
                            label: '主备',
                            value: 'hostBackup',
                            sortable: false,
                            width: '100px'
                        },

                        {
                            label: '是否资源池管理',
                            value: 'mgrByPool',
                            sortable: false,
                            width: '120px'
                        },
                        {
                            label: '录入CMDB时间',
                            value: 'entryCmdbTime',
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
                            label: '未使用时长(小时)',
                            value: 'unusedTime',
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
                            label: '处理状态',
                            value: 'processingStatus',
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
                    deviceIp: [{ validator: validateIp1, trigger: 'blur' }]
                },
                isSenior: false,
                visible: false,
                manyIp: '',
                multipleSelection: []
            }
        },
        created() {
            let currentTime = new Date().getTime()
            let oldTime = currentTime - (1 * 24 * 60 * 60 * 1000)
            this.dialogForm.time = [this.formatDate(oldTime), this.formatDate(currentTime)]
            // this.getSelectValue('idcType2', 'resourceOptions')//获取资源池
            // this.getSelectValue('hostBackup', 'hostBackupOptions')//获取资源池
            // this.getTableList()
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
                                value: item.value
                            }
                        )
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
                    if (prop && prop !== 'time' && prop !== 'operateTime' && prop !== 'dcList') {
                        queryData[prop] = this.dialogForm[prop]
                    }

                }
                queryData.dcList = this.dialogForm.dcList ? [this.dialogForm.dcList] : []
                queryData.checkTimeBegin = this.dialogForm.time ? this.dialogForm.time[0] : ''
                queryData.checkTimeEnd = this.dialogForm.time ? this.dialogForm.time[1] : ''
                queryData.operatingTimeBegin = this.dialogForm.operateTime ? this.dialogForm.operateTime[0] : ''
                queryData.operatingTimeEnd = this.dialogForm.operateTime ? this.dialogForm.operateTime[1] : ''
                queryData.pageSize = this.collectionData.pagination.pageSize
                queryData.pageNo = this.collectionData.pagination.currentPage
                try {
                    let res = await rbIpCollectionServiceFactory.recordedCmdbSearch(queryData)
                    this.collectionData.tableBody = res.result || []
                    this.collectionData.pagination.total = res.count
                    this.recordCount = {
                        numOfProblemAsset: res.numOfProblemAsset,
                        numOfToBeProcessedIp: res.numOfToBeProcessedIp
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
                    if (prop && prop !== 'time' && prop !== 'operateTime' && prop !== 'dcList') {
                        queryData[prop] = this.dialogForm[prop]
                    }

                }
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
                        link.download = '存活未录入CMDB列表' // a标签添加属性
                        link.style.display = 'none'
                        link.href = URL.createObjectURL(blob)
                        document.body.appendChild(link)
                        link.click()
                        document.body.removeChild(link)
                    } else {
                        window.navigator.msSaveBlob(blob, '存活未录入CMDB.xlsx')
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
                let __this = this
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
                    let unqualifiedIp = temp.filter(item => !__this.isValidIps(item))
                    if (unqualifiedIp.length > 0) {
                        this.$alert(`以下IP不合法：${unqualifiedIp.join(',')}`, '提示')
                    } else {
                        this.visible = false
                        this.dialogForm.ipList = temp.join(',')
                    }

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
                    if (item && item.mainId) {
                        newArray.push(item.mainId)
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
                    let res = await rbIpCollectionServiceFactory.clashIpStatus(queryData)
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
            cellStyle({ row, columnIndex }) {
                if ((columnIndex === 3 && row.useStatus === '非法使用') || (columnIndex === 14 && row.processingStatus === '待处理')) {
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
