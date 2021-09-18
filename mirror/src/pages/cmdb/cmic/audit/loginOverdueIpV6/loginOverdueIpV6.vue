<template>
    <!-- IP稽核IPV6-登记已过期IP -->
    <div class="components-container yw-dashboard"
         v-loading="loading">
        <ul class="conflict-title">
            <li><span>未登记IP总数：</span><span>{{ipCount.numOfUnPlanIp}}</span></li>
            <li><span>待处理IP数：</span><span>{{ipCount.numOfToBeProcessedIp}}</span></li>
            <li><span>暂不处理IP数：</span><span>{{ipCount.numOfNotProcessIp}}</span></li>
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
            <el-form-item label="资源池"
                          prop="dcList">
                <el-select v-model="dialogForm.dcList"
                           multiple
                           collapse-tags
                           clearable
                           placeholder="请选择">
                    <el-option v-for="item in resourceOptions"
                               :key="item.value"
                               :label="item.label"
                               :value="item.value">
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="网段类型"
                          prop="networkSegmentType">
                <el-select v-model="dialogForm.networkSegmentType"
                           @change="getSelectNetworkType"
                           clearable
                           style="width:120px;"
                           placeholder="请选择">
                    <el-option v-for="item in networkTypeOptions"
                               :key="item.value"
                               :label="item.label"
                               :value="item.value">
                    </el-option>
                </el-select>
                <el-select v-model="dialogForm.networkSegmentSubType"
                           clearable
                           style="width:120px;"
                           placeholder="请选择">
                    <el-option v-for="item in networkSubTypeOptions"
                               :key="item.value"
                               :label="item.label"
                               :value="item.value">
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="是否资源池管理"
                          label-width="100px"
                          prop="idcManageFlag">
                <el-select v-model="dialogForm.idcManageFlag"
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
            <el-form-item label="分配一级业务"
                          prop="businessName1">
                <el-select v-model="dialogForm.businessName1"
                           clearable
                           filterable
                           placeholder="请选择">
                    <el-option v-for="item in firstBusinessOptions"
                               :key="item.value"
                               :label="item.label"
                               :value="item.value">
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="分配独立业务"
                          prop="businessName2">
                <el-select v-model="dialogForm.businessName2"
                           clearable
                           filterable
                           placeholder="请选择">
                    <el-option v-for="item in aloneBusinessSystemOptions"
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
                <el-form-item label="网段名称"
                              prop="networkName">
                    <el-input v-model="dialogForm.networkName"></el-input>
                </el-form-item>
                <el-form-item label="地址用途"
                              prop="addressUse">
                    <el-select v-model="dialogForm.addressUse"
                               clearable
                               filterable
                               style="width:120px;"
                               placeholder="请选择">
                        <el-option v-for="item in ipUseOptions"
                                   :key="item.value"
                                   :label="item.label"
                                   :value="item.value">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="是否录入CMDB"
                              label-width="100px"
                              prop="cmdbManageFlag">
                    <el-select v-model="dialogForm.cmdbManageFlag"
                               clearable
                               style="width:120px;"
                               placeholder="请选择">
                        <el-option v-for="item in isCmdbManagerOptions"
                                   :key="item.value"
                                   :label="item.label"
                                   :value="item.value">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="申请人"
                              prop="requestPerson">
                    <el-input v-model="dialogForm.requestPerson"></el-input>
                </el-form-item>

                <el-form-item label="申请时间"
                              prop="requestTime">
                    <el-date-picker v-model="dialogForm.requestTime"
                                    format="yyyy-MM-dd HH:mm:ss"
                                    value-format="yyyy-MM-dd HH:mm:ss"
                                    type="datetimerange"
                                    range-separator="至"
                                    start-placeholder="开始日期"
                                    end-placeholder="结束日期">
                    </el-date-picker>
                </el-form-item>
                <el-form-item label="有效期"
                              prop="expirationDate">
                    <el-date-picker v-model="dialogForm.expirationDate"
                                    format="yyyy-MM-dd HH:mm:ss"
                                    value-format="yyyy-MM-dd HH:mm:ss"
                                    type="datetimerange"
                                    range-separator="至"
                                    start-placeholder="开始日期"
                                    end-placeholder="结束日期">
                    </el-date-picker>
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
                <el-form-item label="原因说明"
                              prop="reason">
                    <el-input v-model="dialogForm.reason"></el-input>
                </el-form-item>
                <el-form-item label="使用方独立业务"
                              label-width="100px"
                              prop="businessNameUse">
                    <el-select v-model="dialogForm.businessNameUse"
                               clearable
                               filterable
                               style="width:120px;"
                               placeholder="请选择">
                        <el-option v-for="item in onlineBusinessOptions"
                                   :key="item.value"
                                   :label="item.label"
                                   :value="item.value">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="操作人"
                              prop="operator">
                    <el-input v-model="dialogForm.operator"></el-input>
                </el-form-item>
            </div>
        </el-form>

        <div class="yw-table">
            <div class="deal-btn">
                <el-button v-if="buttonPermission.ip_check_operator_stop"
                           @click="notHandle">暂不处理</el-button>
                <el-button v-if="buttonPermission.ip_check_operator_continue"
                           @click="continueHandle">继续处理</el-button>
                <el-button v-if="buttonPermission.ip_register_update"
                           @click="handleRegister">IP登记</el-button>
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
        <!-- ip登记弹框 -->
        <ip-registration v-if="ipRegisterDialog&&flag"
                         ref="ipRegisterDialog"
                         @closeRegistrationDialog='closeRegistrationDialog'
                         :ipUseOptions='ipUseOptions'
                         :firstBusinessOptions='firstBusinessOptions'
                         :aloneBusinessSystemOptions='aloneBusinessSystemOptions'
                         :opIpType='opIpType'
                         :ipIds='ipIds'
                         :segmentIds='segmentIds'
                         :title='title'></ip-registration>
    </div>
</template>

<script>
    import rbIpCollectionServiceFactory from 'src/services/cmdb/rb-cmdb-ip-management-factory.js'
    import IpRegistration from '../ipRegistrationDialog.vue'
    import buttonPermission from 'src/services/cmdb/rb-audit-permission.js'
    export default {
        name: 'CmdbCmicAuditLoginOverdueIpV6',
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
                    //     callback(new Error('请输入正确的IPV6'))
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
            return {
                dialogFormReason: false,
                reasonExplain: '',
                loading: false,
                ipCount: {
                    numOfUnPlanIp: '',
                    numOfToBeProcessedIp: '',
                    numOfNotProcessIp: ''
                },
                dialogForm: {
                    time: '',
                    ipList: '',
                    dcList: [],
                    networkSegmentType: '',
                    networkSegmentSubType: '',
                    idcManageFlag: '',
                    businessNameUse: '',
                    businessName1: '',
                    businessName2: '',
                    operateTime: '',
                    networkName: '',
                    addressUse: '',
                    cmdbManageFlag: '',
                    requestPerson: '',
                    requestTime: '',
                    expirationDate: '',
                    processingStatus: '',
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
                networkTypeOptions: [],
                networkSubTypeOptions: [],
                ipUseOptions: [],
                segmentNameOptions: [],
                firstBusinessOptions: [],
                aloneBusinessSystemOptions: [],
                mgrByPoolOptions: [],
                isCmdbManagerOptions: [],
                onlineBusinessOptions: [],
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
                            width: '200px'
                        },
                        {
                            label: '网段名称',
                            value: 'networkName',
                            sortable: false,
                            width: '120px'
                        },
                        {
                            label: '所属资源池',
                            value: 'dc',
                            sortable: false,
                            width: '125px'
                        },
                        {
                            label: '是否资源池管理',
                            value: 'idcManageFlag',
                            sortable: false,
                            width: '125px'
                        },
                        {
                            label: '网段类型',
                            value: 'networkSegmentType',
                            sortable: false,
                            width: '125px'
                        },
                        {
                            label: '网段子类',
                            value: 'networkSegmentSubType',
                            sortable: false,
                            width: '120px'
                        },
                        {
                            label: '地址用途',
                            value: 'addressUse',
                            sortable: false,
                            width: '120px'
                        },
                        {
                            label: '分配一级业务',
                            value: 'businessName1',
                            sortable: false,
                            width: '120px'
                        },
                        {
                            label: '分配独立业务',
                            value: 'businessName2',
                            sortable: false,
                            width: '120px'
                        },
                        {
                            label: '是否录入CMDB',
                            value: 'cmdbManageFlag',
                            sortable: false,
                            width: '120px'
                        },
                        {
                            label: '使用方独立业务',
                            value: 'businessNameUse',
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
                            width: '100px'
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
                    ipList: [{ validator: validateIp, trigger: 'blur' }]
                },
                isSenior: false,
                visible: false,
                manyIp: '',
                multipleSelection: [],
                ipRegisterDialog: false,
                flag: false,
                ipIds: [],
                segmentIds: [],
                opIpType: 'ipv6',
                title: '登记已过期IP',
                buttonPermission: buttonPermission
            }
        },
        components: {
            IpRegistration
        },
        created() {
            let currentTime = new Date().getTime()
            let oldTime = currentTime - (1 * 24 * 60 * 60 * 1000)
            this.dialogForm.time = [this.formatDate(oldTime), this.formatDate(currentTime)]
            this.getSelectValue('idcType2', 'resourceOptions')// 获取资源池
            this.getSelectValue('inner_segment_sub_use', 'ipUseOptions')// 地址用途
            this.getSelectValue('ipAllocationStatusType', 'assignOptions')// 分配状态
            this.getSelectValue('ipUseStatusType', 'useOptions')// 使用状态
            this.getSelectValue('whether', 'mgrByPoolOptions')// 是否资源池管理
            this.getSelectValue('whether', 'isCmdbManagerOptions')// 是否录入CMDB
            this.getResource('networkSegment', '', 'networkTypeOptions')// 网段类型
            this.getResource('financialBusiness', '', 'firstBusinessOptions')// 分配一级业务
            this.getResource('business1', '', 'aloneBusinessSystemOptions')// 分配独立业务
            this.getResource('business1', '', 'onlineBusinessOptions')// 使用方独立业务
            this.getTableList()
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

                } catch (error) {
                    this.$message({
                        message: error.data.errors[0].message,
                        type: 'error'
                    })
                }
            },
            // 获取网段类型下拉值
            async getResource(params, pid, type) {
                let queryData = {
                    type: params,
                    pid: pid
                }
                if (params === 'financialBusiness' || params === 'business1') {
                    queryData = {
                        type: params
                    }
                }
                try {
                    let res = await rbIpCollectionServiceFactory.getResource(queryData)
                    for (let item of res.data) {
                        if (params === 'financialBusiness' || params === 'business1') {
                            this[type].push(
                                {
                                    label: item.value,
                                    value: item.id
                                }
                            )
                        } else {
                            this[type].push(
                                {
                                    label: item.value,
                                    value: item.id
                                }
                            )

                        }

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
                    if (prop && prop !== 'time' && prop !== 'operateTime' && prop !== 'requestTime' && prop !== 'expirationDate') {
                        queryData[prop] = this.dialogForm[prop]
                    }

                }
                // let networkSegmentType = (this.networkTypeOptions.filter(item => item.value == this.dialogForm.networkSegmentType))[0]
                // let networkSegmentSubType = (this.networkSubTypeOptions.filter(item => item.value == this.dialogForm.networkSegmentSubType))[0]
                // queryData.networkSegmentType = networkSegmentType ? networkSegmentType.label : ''
                // queryData.networkSegmentSubType = networkSegmentSubType ? networkSegmentSubType.label : ''
                queryData.checkTimeBegin = this.dialogForm.time ? this.dialogForm.time[0] : ''
                queryData.checkTimeEnd = this.dialogForm.time ? this.dialogForm.time[1] : ''
                queryData.operatingTimeBegin = this.dialogForm.operateTime ? this.dialogForm.operateTime[0] : ''
                queryData.operatingTimeEnd = this.dialogForm.operateTime ? this.dialogForm.operateTime[1] : ''
                queryData.requestTimeBegin = this.dialogForm.requestTime ? this.dialogForm.requestTime[0] : ''
                queryData.requestTimeEnd = this.dialogForm.requestTime ? this.dialogForm.requestTime[1] : ''
                queryData.expirationDateBegin = this.dialogForm.expirationDate ? this.dialogForm.expirationDate[0] : ''
                queryData.expirationDateEnd = this.dialogForm.expirationDate ? this.dialogForm.expirationDate[1] : ''
                queryData.ipType = 'IPv6'
                queryData.pageSize = this.collectionData.pagination.pageSize
                queryData.pageNo = this.collectionData.pagination.currentPage
                try {
                    let res = await rbIpCollectionServiceFactory.loginOverdueIpV6Search(queryData)
                    this.collectionData.tableBody = res.result || []
                    this.collectionData.pagination.total = res.count
                    this.ipCount = {
                        numOfUnPlanIp: res.numOfUnPlanIp,
                        numOfToBeProcessedIp: res.numOfToBeProcessedIp,
                        numOfNotProcessIp: res.numOfNotProcessIp
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
            // 网段子类型联动
            getSelectNetworkType(val) {
                this.dialogForm.networkSegmentSubType = ''
                this.networkSubTypeOptions = []
                if (val) {
                    this.getResource('networkSegmentSub', val, 'networkSubTypeOptions')
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
                this.dialogForm.networkSegmentSubType = ''
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
                    if (prop && prop !== 'time' && prop !== 'operateTime' && prop !== 'requestTime' && prop !== 'expirationDate') {
                        queryData[prop] = this.dialogForm[prop]
                    }

                }
                // let networkSegmentType = (this.networkTypeOptions.filter(item => item.value == this.dialogForm.networkSegmentType))[0]
                // let networkSegmentSubType = (this.networkSubTypeOptions.filter(item => item.value == this.dialogForm.networkSegmentSubType))[0]
                // queryData.networkSegmentType = networkSegmentType ? networkSegmentType.label : ''
                // queryData.networkSegmentSubType = networkSegmentSubType ? networkSegmentSubType.label : ''
                queryData.checkTimeBegin = this.dialogForm.time ? this.dialogForm.time[0] : ''
                queryData.checkTimeEnd = this.dialogForm.time ? this.dialogForm.time[1] : ''
                queryData.operatingTimeBegin = this.dialogForm.operateTime ? this.dialogForm.operateTime[0] : ''
                queryData.operatingTimeEnd = this.dialogForm.operateTime ? this.dialogForm.operateTime[1] : ''
                queryData.requestTimeBegin = this.dialogForm.requestTime ? this.dialogForm.requestTime[0] : ''
                queryData.requestTimeEnd = this.dialogForm.requestTime ? this.dialogForm.requestTime[1] : ''
                queryData.expirationDateBegin = this.dialogForm.expirationDate ? this.dialogForm.expirationDate[0] : ''
                queryData.expirationDateEnd = this.dialogForm.expirationDate ? this.dialogForm.expirationDate[1] : ''
                queryData.ipType = 'IPv6'
                try {
                    let res = await rbIpCollectionServiceFactory.loginOverdueIpV6Export(queryData)
                    let blob = new Blob([res], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' })
                    let isSupportDownload = 'download' in document.createElement('a')
                    if (isSupportDownload) {
                        const link = document.createElement('a')
                        link.download = '存活未分配IP列表' // a标签添加属性
                        link.style.display = 'none'
                        link.href = URL.createObjectURL(blob)
                        document.body.appendChild(link)
                        link.click()
                        document.body.removeChild(link)
                    } else {
                        window.navigator.msSaveBlob(blob, '存活未分配IP列表.xlsx')
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
                    let res = await rbIpCollectionServiceFactory.loginOverdueIpUpdate(queryData)
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
            // 打开IP登记弹框
            handleRegister() {
                if (this.multipleSelection.length < 1) {
                    this.$alert('请至少选择一条数据', '通知', {
                        confirmButtonText: '确定'
                    })
                } else {
                    this.ipIds = []
                    this.segmentIds = []
                    for (let item of JSON.parse(JSON.stringify(this.multipleSelection))) {
                        if (item.ipId && item.segmentId) {
                            this.ipIds.push(item.ipId)
                            this.segmentIds.push(item.segmentId)
                        }
                    }
                    if (this.ipUseOptions.length > 0 && this.firstBusinessOptions.length > 0 && this.aloneBusinessSystemOptions.length > 0) {
                        this.flag = true
                        this.ipRegisterDialog = true
                    }
                }
            },
            // 关闭IP登记弹框
            closeRegistrationDialog(param) {
                this.ipRegisterDialog = param
            },
            // ip格式正则校验
            isValidIps(ip) {
                const reg = /\s*((([0-9A-Fa-f]{1,4}:){7}([0-9A-Fa-f]{1,4}|:))|(([0-9A-Fa-f]{1,4}:){6}(:[0-9A-Fa-f]{1,4}|((25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)(\.(25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)){3})|:))|(([0-9A-Fa-f]{1,4}:){5}(((:[0-9A-Fa-f]{1,4}){1,2})|:((25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)(\.(25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)){3})|:))|(([0-9A-Fa-f]{1,4}:){4}(((:[0-9A-Fa-f]{1,4}){1,3})|((:[0-9A-Fa-f]{1,4})?:((25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)(\.(25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)){3}))|:))|(([0-9A-Fa-f]{1,4}:){3}(((:[0-9A-Fa-f]{1,4}){1,4})|((:[0-9A-Fa-f]{1,4}){0,2}:((25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)(\.(25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)){3}))|:))|(([0-9A-Fa-f]{1,4}:){2}(((:[0-9A-Fa-f]{1,4}){1,5})|((:[0-9A-Fa-f]{1,4}){0,3}:((25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)(\.(25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)){3}))|:))|(([0-9A-Fa-f]{1,4}:){1}(((:[0-9A-Fa-f]{1,4}){1,6})|((:[0-9A-Fa-f]{1,4}){0,4}:((25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)(\.(25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)){3}))|:))|(:(((:[0-9A-Fa-f]{1,4}){1,7})|((:[0-9A-Fa-f]{1,4}){0,5}:((25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)(\.(25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)){3}))|:)))(%.+)?\s*/
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
    /deep/ .el-select__tags {
        flex-wrap: initial;
    }
</style>
