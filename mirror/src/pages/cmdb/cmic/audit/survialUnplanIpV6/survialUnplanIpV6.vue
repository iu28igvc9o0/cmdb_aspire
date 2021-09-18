<template>
    <!-- IP稽核IPV6-存活未规划IP -->
    <div class="components-container yw-dashboard"
         v-loading="loading">
        <ul class="conflict-title">
            <li><span>存活未规划IP总数：</span><span>{{unPlanCount.numOfUnPlanIp}}</span></li>
            <li><span>待处理IP数：</span><span>{{unPlanCount.numOfToBeProcessedIp}}</span></li>
            <li><span>暂不处理IP数：</span><span>{{unPlanCount.numOfNotProcessIp}}</span></li>
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
            <el-form-item label="网关设备IP"
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
                <el-button v-if="buttonPermission.ip_check_operator_stop"
                           @click="notHandle">暂不处理</el-button>
                <el-button v-if="buttonPermission.ip_check_operator_continue"
                           @click="continueHandle">继续处理</el-button>
                <el-button v-if="buttonPermission.ip_register_create"
                           @click="register">注册</el-button>
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
    </div>
</template>

<script>
    import rbIpCollectionServiceFactory from 'src/services/cmdb/rb-cmdb-ip-management-factory.js'
    import rbCmdbModuleServiceFactory from 'src/services/cmdb/rb-cmdb-module-service.factory'
    import buttonPermission from 'src/services/cmdb/rb-audit-permission.js'
    export default {
        name: 'CmdbCmicAuditSurvialUnplanIpV6',
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
                    //     callback(new Error('请输入正确的IPv6'))
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
                unPlanCount: {
                    numOfUnPlanIp: '',
                    numOfToBeProcessedIp: '',
                    numOfNotProcessIp: ''
                },
                dialogForm: {
                    time: [],
                    ipList: '',
                    deviceIp: '',
                    dcList: '',
                    processingStatus: '',
                    isNotify: '',
                    orderNum: '',
                    operateTime: '',
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
                            width: '180px'
                        },
                        {
                            label: '网关设备IP',
                            value: 'deviceIp',
                            sortable: false,
                            width: '120px'
                        },
                        {
                            label: '所属资源池',
                            value: 'dc',
                            sortable: false,
                            width: '120px'
                        },
                        {
                            label: '是否已通知',
                            value: 'isNotify',
                            sortable: false,
                            width: '100px'
                        },
                        {
                            label: '处理状态',
                            value: 'processingStatusDesc',
                            sortable: false
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
                            width: '140px'
                        },
                        {
                            label: '工单号',
                            value: 'orderNum',
                            sortable: false,
                            width: '140px'
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
                moduleId: 'ca0950aa6f844223b9a7f266d9a2bbd1',
                currentModuleInfo: null,// 当前模型信息
                parentModuleInfo: null,// 当前模型信息
                buttonPermission: buttonPermission
            }
        },
        created() {
            let currentTime = new Date().getTime()
            let oldTime = currentTime - (1 * 24 * 60 * 60 * 1000)
            this.dialogForm.time = [this.formatDate(oldTime), this.formatDate(currentTime)]
            this.getSelectValue('idcType2', 'resourceOptions')// 获取资源池
            this.getTableList()
            this.getModuleIdInforData()
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
                queryData.dc = this.dialogForm.dcList ? this.dialogForm.dcList : ''
                queryData.checkTimeBegin = this.dialogForm.time ? this.dialogForm.time[0] : ''
                queryData.checkTimeEnd = this.dialogForm.time ? this.dialogForm.time[1] : ''
                queryData.operatingTimeBegin = this.dialogForm.operateTime ? this.dialogForm.operateTime[0] : ''
                queryData.operatingTimeEnd = this.dialogForm.operateTime ? this.dialogForm.operateTime[1] : ''
                queryData.pageSize = this.collectionData.pagination.pageSize
                queryData.pageNo = this.collectionData.pagination.currentPage
                try {
                    let res = await rbIpCollectionServiceFactory.survialUnplanIpV6IpSearch(queryData)
                    this.collectionData.tableBody = res.result || []
                    this.collectionData.pagination.total = res.count
                    this.unPlanCount = {
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
                queryData.dc = this.dialogForm.dcList ? this.dialogForm.dcList : ''
                queryData.checkTimeBegin = this.dialogForm.time ? this.dialogForm.time[0] : ''
                queryData.checkTimeEnd = this.dialogForm.time ? this.dialogForm.time[1] : ''
                queryData.operatingTimeBegin = this.dialogForm.operateTime ? this.dialogForm.operateTime[0] : ''
                queryData.operatingTimeEnd = this.dialogForm.operateTime ? this.dialogForm.operateTime[1] : ''
                try {
                    let res = await rbIpCollectionServiceFactory.survialUnplanIpV6IpExport(queryData)
                    let blob = new Blob([res], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' })
                    let isSupportDownload = 'download' in document.createElement('a')
                    if (isSupportDownload) {
                        const link = document.createElement('a')
                        link.download = '存活未规划IP列表' // a标签添加属性
                        link.style.display = 'none'
                        link.href = URL.createObjectURL(blob)
                        document.body.appendChild(link)
                        link.click()
                        document.body.removeChild(link)
                    } else {
                        window.navigator.msSaveBlob(blob, '存活未规划IP列表.xlsx')
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
                    //     this.$alert(`以下IPv6不合法：${unqualifiedIp.join(',')}`, '提示')
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
                    operator: sessionStorage.getItem('username'),
                    opIpType: 'ipv61'
                }
                if (type === '1') {
                    queryData.notHandleReason = this.reasonExplain
                }
                try {
                    let res = await rbIpCollectionServiceFactory.survialIpV6IpUpdate(queryData)
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
            // 注册
            register() {
                let queryParams = {
                    instanceId: '',
                    moduleId: this.moduleId,
                    'device_class': {
                        id: this.parentModuleInfo && this.parentModuleInfo.module_id || '',
                        name: this.parentModuleInfo && this.parentModuleInfo.module_name || ''
                    },
                    'device_type': {
                        id: this.currentModuleInfo && this.currentModuleInfo.module_ud || '',
                        name: this.currentModuleInfo && this.currentModuleInfo.module_name || ''
                    },
                    originRouter: this.$route.fullPath,// 当前路由
                    state: 'add'
                }
                queryParams = JSON.stringify(queryParams)
                this.$router.push({ path: '/resource/iframe/add', query: { queryParams: queryParams, currentTitle: '网段信息' } })

            },
            getModuleIdInforData() {
                // 查询模型信息
                rbCmdbModuleServiceFactory.getParentInfo({ 'module_id': this.moduleId }).then((data) => {
                    if (data) {
                        this.currentModuleInfo = {
                            module_id: data.module_id,
                            module_name: data.module_name
                        }
                        this.parentModuleInfo = {
                            module_id: data.parent_id,
                            module_name: data.parent_name
                        }
                    }

                })
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
                if (columnIndex === 6 && row.processingStatus === '0') {
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
